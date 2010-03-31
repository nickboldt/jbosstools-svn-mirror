package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.mediator.Messages;

public class ConfigurationStubJDBCMetaData extends ConfigurationStub {

	protected JDBCMetaDataConfiguration jdbcMetaDataConfiguration;
	
	protected ConfigurationStubJDBCMetaData(JDBCMetaDataConfiguration configuration) {
		super(configuration);
		if (configuration == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		jdbcMetaDataConfiguration = configuration;
	}

	public void setProperties(Properties properties) {
		jdbcMetaDataConfiguration.setProperties(properties);
	}

	public void setPreferBasicCompositeIds(boolean flag) {
		jdbcMetaDataConfiguration.setPreferBasicCompositeIds(flag);
	}

	public void setReverseEngineeringStrategy(ReverseEngineeringStrategyStub reverseEngineeringStrategy) {
		jdbcMetaDataConfiguration.setReverseEngineeringStrategy(reverseEngineeringStrategy.reverseEngineeringStrategy);
	}

	public void readFromJDBC() {
		jdbcMetaDataConfiguration.readFromJDBC();
	}

	public ArrayList<TableStub> getTableMappingsArr() {
		ArrayList<TableStub> arr = new ArrayList<TableStub>(); 
		Iterator<?> it = jdbcMetaDataConfiguration.getTableMappings();
		while (it.hasNext() ) {
			Object obj = it.next();
			if (obj != null) {
				arr.add(new TableStub(obj));
			}
		}
		return arr;
	}

	public Iterator<TableStub> getTableMappingsIt() {
		return getTableMappingsArr().iterator();
	}

}
