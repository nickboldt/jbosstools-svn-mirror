/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.ws.jaxrs.core.internal.configuration;

import junit.framework.Assert;

import org.jboss.tools.ws.jaxrs.core.AbstractCommonTestCase;
import org.jboss.tools.ws.jaxrs.core.configuration.ProjectNatureUtils;
import org.junit.Test;

/**
 * @author Xi
 * 
 */
public class ProjectNatureUtilsTestCase extends AbstractCommonTestCase {

	@Test
	public void shouldVerifyProjectNatureIsNotInstalled() throws Exception {
		Assert.assertFalse("Wrong result", ProjectNatureUtils.isProjectNatureInstalled(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
	}
	
	@Test
	public void shouldVerifyProjectNatureIsInstalled() throws Exception {
		Assert.assertTrue("Wrong result", ProjectNatureUtils.isProjectNatureInstalled(javaProject.getProject(), "org.eclipse.jdt.core.javanature"));
	}
	
	@Test
	public void shouldInstallAndUninstallProjectNature() throws Exception {
		Assert.assertFalse("Wrong result", ProjectNatureUtils.isProjectNatureInstalled(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertTrue("Wrong result", ProjectNatureUtils.installProjectNature(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertFalse("Wrong result", ProjectNatureUtils.installProjectNature(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertTrue("Wrong result", ProjectNatureUtils.isProjectNatureInstalled(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertTrue("Wrong result", ProjectNatureUtils.uninstallProjectNature(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertFalse("Wrong result", ProjectNatureUtils.uninstallProjectNature(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
		Assert.assertFalse("Wrong result", ProjectNatureUtils.isProjectNatureInstalled(javaProject.getProject(), ProjectNatureUtils.JAXRS_NATURE_ID));
	}
	

}
