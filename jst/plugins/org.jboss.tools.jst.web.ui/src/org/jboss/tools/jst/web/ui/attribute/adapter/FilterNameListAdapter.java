/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.ui.attribute.adapter;

import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.FileSystemsHelper;
import org.jboss.tools.common.model.ui.attribute.*;
import org.jboss.tools.common.model.ui.attribute.adapter.*;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;
import org.jboss.tools.jst.web.webapp.model.WebAppConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class FilterNameListAdapter extends DefaultComboBoxValueAdapter {

	protected IListContentProvider createListContentProvider(XAttribute attribute) {
		FilterNameListContentProvider p = new FilterNameListContentProvider();
		p.setModel(model, modelObject);
		p.setAttribute(attribute);
		return p;	
	}

}

class FilterNameListContentProvider extends DefaultXAttributeListContentProvider {
	private XModel model;
	private XModelObject object;
	
	public void setModel(XModel model, XModelObject context) {
		this.model = model;
		object = context;
	}

	protected void loadTags() {
		XModelObject webxml = FileSystemsHelper.getFile(object);
		if(webxml == null) {
			webxml = WebAppHelper.getWebApp(model);
		}
		if(webxml != null) {
			XModelObject[] os = WebAppHelper.getFilters(webxml);
			tags = new String[os.length];
			for (int i = 0; i < tags.length; i++) tags[i] = os[i].getAttributeValue(WebAppConstants.FILTER_NAME);
		}
	}

}
