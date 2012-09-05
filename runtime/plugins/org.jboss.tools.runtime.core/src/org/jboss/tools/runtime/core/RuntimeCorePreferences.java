package org.jboss.tools.runtime.core;

import java.util.Set;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.jboss.tools.runtime.core.model.IRuntimeDetector;
import org.osgi.service.prefs.BackingStoreException;

public class RuntimeCorePreferences {	
	// Preference key
	private static final String ENABLED_DETECTORS = "enabledDetectors";
	private IEclipsePreferences prefs;

	private static RuntimeCorePreferences INSTANCE;
	public static RuntimeCorePreferences getDefault() {
		if( INSTANCE == null )
			INSTANCE = new RuntimeCorePreferences();
		return INSTANCE;
	}
	
	public String[] getEnabledRuntimeDetectors() {
		String enabledDetectors = getPreferences().get(ENABLED_DETECTORS,null);
		return enabledDetectors == null ? null : enabledDetectors.split(",");
	}
	
	public void saveEnabledDetectors() {
		saveEnabledDetectors(RuntimeCoreActivator.getDefault().getDeclaredRuntimeDetectors());
	}
	
	public void saveEnabledDetectors(Set<IRuntimeDetector> allDetectors) {
		StringBuilder builder = new StringBuilder();
		for (IRuntimeDetector detector:allDetectors) {
			if (detector.isEnabled()) {
				builder.append(detector.getId());
				builder.append(",");
			}
		}
		String enabled = builder.toString();
		int index = enabled.lastIndexOf(",");
		if (index != -1) {
			enabled = enabled.substring(0, index);
		}
		getPreferences().put(ENABLED_DETECTORS, enabled);
		try {
			getPreferences().flush();
		} catch (BackingStoreException e) {
			RuntimeCoreActivator.getDefault().logError(e);
		}
	}
	
	IEclipsePreferences getPreferences() {
		if (prefs == null) {
			prefs = ConfigurationScope.INSTANCE.getNode(RuntimeCoreActivator.PLUGIN_ID);
		}
		return prefs;
	}
}
