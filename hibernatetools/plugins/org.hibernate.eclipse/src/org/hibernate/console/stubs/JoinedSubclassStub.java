package org.hibernate.console.stubs;

import org.hibernate.mapping.JoinedSubclass;

public class JoinedSubclassStub extends SubclassStub {
	
	protected JoinedSubclass joinedSubclass;

	protected JoinedSubclassStub(Object joinedSubclass) {
		super(joinedSubclass);
		this.joinedSubclass = (JoinedSubclass)joinedSubclass;
	}
	
	public static JoinedSubclassStub newInstance(PersistentClassStub persistentClass) {
		return new JoinedSubclassStub(persistentClass);
	}
	
	public void setTable(TableStub table) {
		joinedSubclass.setTable(table.getTable());
	}
	
	public void setKey(KeyValueStub key) {
		joinedSubclass.setKey(key.keyValue);
	}

}
