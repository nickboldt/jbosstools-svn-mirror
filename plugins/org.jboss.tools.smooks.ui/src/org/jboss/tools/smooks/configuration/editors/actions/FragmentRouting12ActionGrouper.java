package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.jmsrouting12.Router;

public class FragmentRouting12ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof Router) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Fragment Routing v1.2";
	}

}
