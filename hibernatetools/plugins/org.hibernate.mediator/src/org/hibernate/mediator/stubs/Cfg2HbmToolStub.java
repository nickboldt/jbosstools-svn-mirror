package org.hibernate.mediator.stubs;

import org.hibernate.tool.hbm2x.Cfg2HbmTool;

public class Cfg2HbmToolStub {
	protected Cfg2HbmTool cfg2HbmTool;

	protected Cfg2HbmToolStub(Object cfg2HbmTool) {
		this.cfg2HbmTool = (Cfg2HbmTool)cfg2HbmTool;
	}
	
	public static Cfg2HbmToolStub newInstance() {
		return new Cfg2HbmToolStub(new Cfg2HbmTool());
	}

	public String getTag(PersistentClassStub persistentClass) {
		return cfg2HbmTool.getTag(persistentClass.persistentClass);
	}

	public String getTag(PropertyStub property) {
		return cfg2HbmTool.getTag(property.property);
	}
}
