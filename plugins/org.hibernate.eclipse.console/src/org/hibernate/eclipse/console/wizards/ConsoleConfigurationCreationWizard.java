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
package org.hibernate.eclipse.console.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.hibernate.console.ImageConstants;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences.ConfigurationMode;
import org.hibernate.eclipse.console.EclipseConsoleConfiguration;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.utils.EclipseImages;
import org.hibernate.eclipse.console.utils.ProjectUtils;
import org.hibernate.eclipse.launch.ICodeGenerationLaunchConstants;
import org.hibernate.eclipse.launch.IConsoleConfigurationLaunchConstants;
import org.hibernate.eclipse.nature.HibernateNature;
import org.hibernate.util.StringHelper;

/**
 * @author max
 */
public class ConsoleConfigurationCreationWizard extends Wizard implements
		INewWizard {

	private ConsoleConfigurationWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public ConsoleConfigurationCreationWizard() {
		super();
        setDefaultPageImageDescriptor(EclipseImages.getImageDescriptor(ImageConstants.NEW_WIZARD) );
		setNeedsProgressMonitor(true);
	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ConsoleConfigurationWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		try {
			page.performFinish();
		} catch (CoreException ce) {
			HibernateConsolePlugin.getDefault().showError(getShell(), HibernateConsoleMessages.AddConfigurationAction_problem_add_console_config,  ce);
		}
		return true;
	}

	public boolean performCancel() {
		try {
			page.performCancel();
		} catch (CoreException ce) {
			HibernateConsolePlugin.getDefault().showError(getShell(), HibernateConsoleMessages.AddConfigurationAction_problem_add_console_config,  ce);
		}
        return true;
    }

	// Only used by tests - see JBIDE-2734
	static protected void createConsoleConfiguration(
			final Shell shell,
			final EclipseConsoleConfiguration oldConfig,
			final String configName,
			ConfigurationMode cmode, final String projectName, boolean useProjectClasspath, String entityResolver, IPath propertyFilename,
			IPath cfgFile, IPath[] mappings, IPath[] classpaths, String persistenceUnitName, String namingStrategy, IProgressMonitor monitor)
		throws CoreException {

		monitor.beginTask(HibernateConsoleMessages.ConsoleConfigurationCreationWizard_configuring_hibernate_console, IProgressMonitor.UNKNOWN);

		if(oldConfig!=null) {
			KnownConfigurations.getInstance().removeConfiguration( oldConfig, false );
		}

		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType( ICodeGenerationLaunchConstants.CONSOLE_CONFIGURATION_LAUNCH_TYPE_ID );
		String launchName = launchManager.generateUniqueLaunchConfigurationNameFrom(configName);
		ILaunchConfigurationWorkingCopy wc = launchConfigurationType.newInstance(null, launchName);
		wc.setAttribute( IConsoleConfigurationLaunchConstants.CONFIGURATION_FACTORY, cmode.toString());
		wc.setAttribute( IConsoleConfigurationLaunchConstants.PROJECT_NAME, projectName );

		wc.setAttribute( IConsoleConfigurationLaunchConstants.PROPERTY_FILE, safePathName(propertyFilename) );
		wc.setAttribute( IConsoleConfigurationLaunchConstants.CFG_XML_FILE, safePathName(cfgFile) );
		wc.setAttribute( IConsoleConfigurationLaunchConstants.PERSISTENCE_UNIT_NAME, persistenceUnitName );

		wc.setAttribute( IConsoleConfigurationLaunchConstants.NAMING_STRATEGY, namingStrategy );
		wc.setAttribute( IConsoleConfigurationLaunchConstants.ENTITY_RESOLVER, entityResolver );

		IRuntimeClasspathEntry[] projectEntries = new IRuntimeClasspathEntry[0];
		if(useProjectClasspath) {
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
			projectEntries = JavaRuntime.computeUnresolvedRuntimeClasspath(wc);

		}

		if(classpaths.length>0) {
			List<String> user = new ArrayList<String>();
			for (int i = 0; i < projectEntries.length; i++) {
				user.add( projectEntries[i].getMemento() );
			}
			for (int i = 0; i < classpaths.length; i++) {
				IPath entry = classpaths[i];
				IRuntimeClasspathEntry userEntry = JavaRuntime.newArchiveRuntimeClasspathEntry( entry );
				user.add( userEntry.getMemento() );
			}
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, user);
		} else {
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
			wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, (String)null);
		}

		List<String> mappingFiles = new ArrayList<String>();
		for (int i = 0; i < mappings.length; i++) {
			mappingFiles.add(mappings[i].toPortableString());
		}
		wc.setAttribute( IConsoleConfigurationLaunchConstants.FILE_MAPPINGS, mappingFiles );

		wc.doSave();

		Display.getDefault().syncExec(new Runnable() {
            public void run() {
            	if(StringHelper.isNotEmpty( projectName )) {
        			IJavaProject project = ProjectUtils.findJavaProject( projectName );
        			if(project.exists()) {
        				HibernateNature hibernateNature = HibernateNature.getHibernateNature( project );
        				if(hibernateNature==null) { // project not enabled at all
        					String out = NLS.bind(HibernateConsoleMessages.ConsoleConfigurationCreationWizard_the_project, projectName, configName);
        					if( MessageDialog.openConfirm( shell, HibernateConsoleMessages.ConsoleConfigurationCreationWizard_enable_hibernate_features, out)) {
        						ProjectUtils.toggleHibernateOnProject( project.getProject(), true, configName );
        					}
        				}
        				else {
        					String defaultConsoleConfigurationName = hibernateNature.getDefaultConsoleConfigurationName();

        					if((oldConfig!=null && oldConfig.getName().equals(defaultConsoleConfigurationName)) ||
        							defaultConsoleConfigurationName.equals(hibernateNature.getDefaultConsoleConfigurationName())) { // an update so its just forced in there.
        						ProjectUtils.toggleHibernateOnProject( project.getProject(), true, configName );
        					} else if(defaultConsoleConfigurationName==null) {
            					String out = NLS.bind(HibernateConsoleMessages.ConsoleConfigurationCreationWizard_the_project_named, projectName, configName);
        						if(MessageDialog.openConfirm( shell, HibernateConsoleMessages.ConsoleConfigurationCreationWizard_enable_hibernate_features, out)) {
        							ProjectUtils.toggleHibernateOnProject( project.getProject(), true, configName );
        						}
        					} else { // hibernate enabled, but not this exact one
            					String out = NLS.bind(HibernateConsoleMessages.ConsoleConfigurationCreationWizard_the_project_named_have, new Object[]{ projectName, defaultConsoleConfigurationName, configName });
        						if(MessageDialog.openConfirm( shell, HibernateConsoleMessages.ConsoleConfigurationCreationWizard_enable_hibernate_features, out)) {
        							ProjectUtils.toggleHibernateOnProject( project.getProject(), true, configName );
        						}
        					}
        				}
        			}
        		}

            }});
			monitor.worked(1);
	}

	private static String safePathName(IPath propertyFilename) {
		if(propertyFilename==null) {
			return null;
		} else {
			return propertyFilename.toOSString();
		}
	}




	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
