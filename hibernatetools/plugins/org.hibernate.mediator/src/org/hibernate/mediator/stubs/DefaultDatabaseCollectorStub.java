package org.hibernate.mediator.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.hibernate.mediator.base.HObject;

public class DefaultDatabaseCollectorStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.DefaultDatabaseCollector"; //$NON-NLS-1$

	protected DefaultDatabaseCollectorStub(Object defaultDatabaseCollector) {
		super(defaultDatabaseCollector, CL);
	}
	
	public static DefaultDatabaseCollectorStub newInstance() {
		return new DefaultDatabaseCollectorStub(HObject.newInstance(CL));
	}

	@SuppressWarnings("unchecked")
	public Iterator<Entry<String, List<TableStub>>> getQualifierEntries() {
		Iterator<Map.Entry<String, List>> it = (Iterator<Map.Entry<String, List>>)invoke(mn());
		HashMap<String, List<TableStub>> map = new HashMap<String, List<TableStub>>();
		while (it.hasNext()) {
			Map.Entry<String, List> entry = it.next();
			ArrayList<TableStub> arr = new ArrayList<TableStub>();
			Iterator itValue = (Iterator)entry.getValue().iterator();
			while (itValue.hasNext()) {
				Object obj = itValue.next();
				if (obj != null) {
					arr.add(new TableStub(obj));
				}
			}
			map.put(entry.getKey(), arr);
		}
		return map.entrySet().iterator();
	}
}
