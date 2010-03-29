package org.hibernate.mediator.stubs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.tool.hbm2x.HibernateMappingExporter;

public class HibernateMappingExporterStub {
	
	protected HibernateMappingExporter hibernateMappingExporter;

	protected HibernateMappingExporterStub(ConfigurationStub cfg, File outputdir) {
		hibernateMappingExporter = new HibernateMappingExporter(cfg.configuration, outputdir);    	
    }

	public void setGlobalSettings(HibernateMappingGlobalSettingsStub hmgs) {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		
	}

	protected File getOutputDirectory() {
		return hibernateMappingExporter.getOutputDirectory();
	}

	protected void setOutputDirectory(File outputdir) {
		hibernateMappingExporter.setOutputDirectory(outputdir);
	}

	@SuppressWarnings("unchecked")
	protected void exportPOJO(Map additionalContext, POJOClassStub element) {
		// TODO: protected - call via reflection
		Method m = null;
		try {
			m = hibernateMappingExporter.getClass().getDeclaredMethod("exportPOJO");//$NON-NLS-1$
		} catch (SecurityException e) {
			//TODO:
		} catch (NoSuchMethodException e) {
			//TODO:
		}
		m.setAccessible(true);
		try {
			m.invoke(hibernateMappingExporter, additionalContext, element.pojoClass);
		} catch (IllegalArgumentException e) {
			//TODO:
		} catch (IllegalAccessException e) {
			//TODO:
		} catch (InvocationTargetException e) {
			//TODO:
		}
	}

	public ArtifactCollectorStub getArtifactCollector() {
		return new ArtifactCollectorStub(hibernateMappingExporter.getArtifactCollector());
	}

}
