/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.xml;

import java.util.Properties;

import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart (dpeng@redhat.com)
 *
 */
public class XSDStructuredDataWizard extends AbstractStructuredDdataWizard {

	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.xml.AbstractStructuredDdataWizard#createAbstractFileSelectionWizardPage()
	 */
	@Override
	protected AbstractFileSelectionWizardPage createAbstractFileSelectionWizardPage() {
		// TODO Auto-generated method stub
		return new XSDStructuredDataWizardPage("XSD");
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		return SmooksModelUtils.INPUT_TYPE_XSD;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getProperties()
	 */
	public Properties getProperties() {
		Properties pro = new Properties();
		pro.setProperty("rootElement", ((XSDStructuredDataWizardPage)page).getRootElementName());
		return pro;
	}

}
