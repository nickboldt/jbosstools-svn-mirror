/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.DOMWriter;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.osgi.util.NLS;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.Settings;
import org.hibernate.console.execution.DefaultExecutionContext;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContextHolder;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences.ConfigurationMode;
import org.hibernate.console.stubs.ConfigStubFactory;
import org.hibernate.console.stubs.ConfigurationStub;
import org.hibernate.console.stubs.SessionStub;
import org.hibernate.console.stubs.SessionStubFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.resolver.DialectFactory;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;
import org.hibernate.util.ConfigHelper;
import org.hibernate.util.ReflectHelper;
import org.hibernate.util.StringHelper;
import org.hibernate.util.XMLHelper;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ConsoleConfiguration implements ExecutionContextHolder {

	private ExecutionContext executionContext;
	private ConsoleConfigClassLoader classLoader = null;

	/* TODO: move this out to the actual users of the configuraiton/sf ? */
	private ConfigurationStub configStub;
	private SessionStubFactory sessionStubFactory;

	private ConsoleConfigurationPreferences prefs = null;
	
	/** Unique name for this configuration */
	public String getName() {
		return prefs.getName();
	}

	public ConsoleConfiguration(ConsoleConfigurationPreferences config) {
		prefs = config;
	}

	public Object execute(Command c) {
		return executionContext.execute(c);
	}

	/**
	 * Reset so a new configuration or sessionfactory is needed.
	 *
	 */
	public void reset() {
		// reseting state
		closeSessionFactory();
		if (executionContext != null) {
			executionContext.execute(new ExecutionContext.Command() {
	
				public Object execute() {
					configStub.cleanUp();
					return null;
				}
			});
		}
		configStub = null;
		cleanUpClassLoader();
		fireConfigurationReset();
		executionContext = null;
	}

	public void cleanUpClassLoader() {
		ClassLoader classLoaderTmp = classLoader;
		while (classLoaderTmp != null) {
			if (classLoaderTmp instanceof ConsoleConfigClassLoader) {
				((ConsoleConfigClassLoader)classLoaderTmp).close();
			}
			classLoaderTmp = classLoaderTmp.getParent();
		}
		classLoader = null;
	}

	public void build() {
		configStub = buildWith(null, true);
		fireConfigurationBuilt();
	}

	/**
	 * @return
	 *
	 */
	public ConfigurationStub buildWith(final ConfigurationStub cfg, final boolean includeMappings) {
		if (classLoader == null) {
			classLoader = createClassLoader();
		}
		executionContext = new DefaultExecutionContext(getName(), classLoader);
		ConfigurationStub result = (ConfigurationStub) executionContext.execute(new ExecutionContext.Command() {
			public Object execute() {
				ConfigStubFactory csf = new ConfigStubFactory(prefs);
				return csf.createConfiguration(cfg, includeMappings);
			}
		});
		return result;
	}

	/*
	 * try get a path to the sql driver jar file from DTP connection profile
	 */
	protected String getConnectionProfileDriverURL() {
		String connectionProfile = prefs.getConnectionProfileName();
		if (connectionProfile == null) {
			return null;
		}
		IConnectionProfile profile = ProfileManager.getInstance().getProfileByName(connectionProfile);
		if (profile == null) {
			return null;
		}
		ConfigStubFactory.refreshProfile(profile);
		//
		Properties cpProperties = profile.getProperties(profile.getProviderId());
		String driverJarPath = cpProperties.getProperty("jarList"); //$NON-NLS-1$
		return driverJarPath;
	}

	/*
	 * get custom classpath URLs 
	 */
	protected URL[] getCustomClassPathURLs() {
		URL[] customClassPathURLsTmp = prefs.getCustomClassPathURLS();
		URL[] customClassPathURLs = null;
		String driverURL = getConnectionProfileDriverURL();
		URL url = null;
		if (driverURL != null) {
			try {
				url = new URL("file:/" + driverURL); //$NON-NLS-1$
			} catch (MalformedURLException e) {
				// just ignore
			}
		}
		// should DTP connection profile driver jar file be inserted
		boolean insertFlag = ( url != null );
		if (insertFlag) {
			for (int i = 0; i < customClassPathURLsTmp.length; i++) {
				if (url.equals(customClassPathURLsTmp[i])) {
					insertFlag = false;
					break;
				}
			}
		}
		if (insertFlag) {
			customClassPathURLs = new URL[customClassPathURLsTmp.length + 1];
	        System.arraycopy(customClassPathURLsTmp, 0, 
	        		customClassPathURLs, 0, customClassPathURLsTmp.length);
	        // insert DTP connection profile driver jar file URL after the default classpath entries
			customClassPathURLs[customClassPathURLsTmp.length] = url;
		}
		else {
			customClassPathURLs = customClassPathURLsTmp;
		}
		return customClassPathURLs;
	}
	
	protected ConsoleConfigClassLoader createClassLoader() {
		final URL[] customClassPathURLs = getCustomClassPathURLs();
		ConsoleConfigClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ConsoleConfigClassLoader>() {
			public ConsoleConfigClassLoader run() {
				return new ConsoleConfigClassLoader(customClassPathURLs, getParentClassLoader()) {
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						try {
							return super.findClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
		
					protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
						try {
							return super.loadClass(name, resolve);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
		
					public Class<?> loadClass(String name) throws ClassNotFoundException {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
				};
			}
		});
		return classLoader;
	}

	protected ClassLoader getParentClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	public ConfigurationStub getConfiguration() {
		return configStub;
	}
	/**
	 * @return
	 */
	public boolean hasConfiguration() {
		return configStub != null;
	}

	public void buildSessionFactory() {
		execute(new ExecutionContext.Command() {
			public Object execute() {
				if (sessionStubFactory != null) {
					throw new HibernateConsoleRuntimeException(ConsoleMessages.ConsoleConfiguration_factory_not_closed_before_build_new_factory);
				}
				sessionStubFactory = new SessionStubFactory(executionContext, configStub);
				fireFactoryBuilt();
				return null;
			}
		});
	}

	public SessionStubFactory getSessionStubFactory() {
		return sessionStubFactory;
	}

	/**
	 * Given a ConsoleConfiguration and a query this method validates the query through hibernate if a sessionfactory is available.
	 * @param query
	 * @param allowEL if true, EL syntax will be replaced as a named variable
	 * @throws HibernteException if something is wrong with the query
	 */
	public void checkQuery(String query, boolean allowEL) {
		if (isSessionFactoryCreated()) {
			sessionStubFactory.checkQuery(query, allowEL);
		}		
	}
	

	int execcount;
	List<ConsoleConfigurationListener> consoleCfgListeners = new ArrayList<ConsoleConfigurationListener>();

	public QueryPage executeHQLQuery(final String hql) {
		return executeHQLQuery(hql, new QueryInputModel());
	}

	public QueryPage executeHQLQuery(final String hql, final QueryInputModel model) {

		return (QueryPage) executionContext.execute(new ExecutionContext.Command() {

			public Object execute() {
				SessionStub sessionStub = getSessionStubFactory().openSession();
				QueryPage qp = new HQLQueryPage(ConsoleConfiguration.this.getName(), hql, model);
				qp.setSessionStub(sessionStub);

				qp.setId(++execcount);
				fireQueryPageCreated(qp);
				return qp;
			}

		});
	}

	public QueryPage executeBSHQuery(final String queryString, final QueryInputModel model) {
		return (QueryPage) executionContext.execute(new ExecutionContext.Command() {

			public Object execute() {
				SessionStub sessionStub = getSessionStubFactory().openSession();
				QueryPage qp = new JavaPage(ConsoleConfiguration.this.getName(), queryString, model);
				qp.setSessionStub(sessionStub);

				qp.setId(++execcount);
				fireQueryPageCreated(qp);
				return qp;
			}

		});
	}
	
	private void fireConfigurationBuilt() {
		for (ConsoleConfigurationListener view : consoleCfgListeners) {
			view.configurationBuilt(this);
		}
	}	

	private void fireConfigurationReset() {
		for (ConsoleConfigurationListener view : consoleCfgListeners) {
			view.configurationReset(this);
		}
	}	

	private void fireQueryPageCreated(QueryPage qp) {
		for (ConsoleConfigurationListener view : consoleCfgListeners) {
			view.queryPageCreated(qp);
		}
	}

	private void fireFactoryBuilt() {
		for (ConsoleConfigurationListener view : consoleCfgListeners) {
			view.sessionFactoryBuilt(this);
		}
	}

	private void fireFactoryClosing(SessionStubFactory ssf) {
		for (ConsoleConfigurationListener view : consoleCfgListeners) {
			view.sessionFactoryClosing(this);
		}
	}

	public void addConsoleConfigurationListener(ConsoleConfigurationListener v) {
		consoleCfgListeners.add(v);
	}

	public void removeConsoleConfigurationListener(ConsoleConfigurationListener sfListener) {
		consoleCfgListeners.remove(sfListener);
	}

	public ConsoleConfigurationListener[] getConsoleConfigurationListeners() {
		return consoleCfgListeners.toArray(new ConsoleConfigurationListener[consoleCfgListeners.size()]);
	}


	public boolean isSessionFactoryCreated() {
		return sessionStubFactory != null && sessionStubFactory.isSessionFactoryCreated();
	}

	public String generateSQL(final String query) {
		String res = ""; //$NON-NLS-1$
		if (sessionStubFactory != null && executionContext != null) {
			res = (String)executionContext.execute(new ExecutionContext.Command() {
				public Object execute() {
					return sessionStubFactory.generateSQL(query);
				}
			});
		}
		return res;
	}

	public ConsoleConfigurationPreferences getPreferences() {
		return prefs;
	}
	
	public File getConfigXMLFile() {
		File configXMLFile = null;
		if (prefs != null) {
			configXMLFile = prefs.getConfigXMLFile();
		}
		if (configXMLFile == null && classLoader != null) {
			URL url = classLoader.findResource("hibernate.cfg.xml"); //$NON-NLS-1$
			if (url != null) {
				URI uri = null;
				try {
					uri = url.toURI();
					configXMLFile = new File(uri);
				} catch (URISyntaxException e) {
					// ignore
				}
			}
		}
		if (configXMLFile == null) {
			URL url = Environment.class.getClassLoader().getResource("hibernate.cfg.xml"); //$NON-NLS-1$
			if (url != null) {
				URI uri = null;
				try {
					uri = url.toURI();
					configXMLFile = new File(uri);
				} catch (URISyntaxException e) {
					// ignore
				}
			}
		}
		return configXMLFile;
	}

	public String toString() {
		return getClass().getName() + ":" + getName(); //$NON-NLS-1$
	}

	public ExecutionContext getExecutionContext() {
		return executionContext;
	}

	public void closeSessionFactory() {
		if (sessionStubFactory != null) {
			fireFactoryClosing(sessionStubFactory);
			sessionStubFactory.close();
			sessionStubFactory = null;
		}
	}

	public Settings getSettings(final ConfigurationStub cfg) {
		return (Settings) execute(new Command() {

			public Object execute() {
				return cfg.buildSettings();
			}

		});
	}

	public Settings getSettings2() {
		ConfigurationStub cfg = buildWith(null, false);
		Settings settings = getSettings(cfg);
		return settings;
	}

	public EntityResolver getEntityResolver() {
		if (configStub == null) {
			build();
			buildSessionFactory();
		}
		return configStub.getEntityResolver();
	}

	public IHQLCodeAssist getHQLCodeAssist() {
		if (configStub == null) {
			try{
			 	build();
			 	buildMappings();
			} catch (HibernateException e){
//				String mess = NLS.bind(HibernateConsoleMessages.CompletionHelper_error_could_not_build_cc, consoleConfiguration.getName());
				throw e;
				//HibernateConsolePlugin.getDefault().logErrorMessage(mess, e);
			}
		}
		return configStub.getHQLCodeAssist();
	}
	
	public void buildMappings() {
		execute(new ExecutionContext.Command() {
			public Object execute() {
				if (configStub != null) {
					configStub.buildMappings();
				}
				return null;
			}
		} );
	}

	@SuppressWarnings("unchecked")
	public Iterator<Throwable> doSchemaExport() {
		return (Iterator<Throwable>)execute(new ExecutionContext.Command() {
			public Object execute() {
				if (configStub != null) {
					return configStub.doSchemaExport();
				}
				return null;
			}
		} );
	}
}