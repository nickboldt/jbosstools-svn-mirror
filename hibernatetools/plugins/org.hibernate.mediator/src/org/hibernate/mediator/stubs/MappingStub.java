package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class MappingStub extends HObject {
	public static final String CL = "org.hibernate.engine.Mapping"; //$NON-NLS-1$

	protected MappingStub(Object mapping) {
		super(mapping, CL);
	}
}
