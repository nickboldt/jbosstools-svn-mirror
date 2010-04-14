/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.seam.ui.test.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.jboss.tools.seam.core.test.project.facet.AbstractSeamFacetTest;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;

/**
 * @author daniel
 *
 */
public class SeamProjectNamesTest extends AbstractSeamFacetTest {
	public static final String WAR = "war";
	public static final String EAR = "ear";
	
	public SeamProjectNamesTest(String name) {
		super(name);
	}

	protected void checkProjectNamesCreation(String warProjectName, String earProjectName, String ejbProjectName, String testProjectName, String seamVersion, String deployType, boolean createTestProject) throws CoreException{
		IDataModel model = createSeamDataModel(deployType);
		
		// set property to create test project
		model.setProperty(ISeamFacetDataModelProperties.TEST_PROJECT_CREATING, new Boolean(createTestProject));
		
		model.setStringProperty(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME, seamVersion);
		
		model.setStringProperty(ISeamFacetDataModelProperties.SEAM_EAR_PROJECT, earProjectName);
		model.setStringProperty(ISeamFacetDataModelProperties.SEAM_EJB_PROJECT, ejbProjectName);
		model.setStringProperty(ISeamFacetDataModelProperties.SEAM_TEST_PROJECT, testProjectName);

		final IFacetedProject fproj = createSeamProject(warProjectName, model);
		
		final IProject proj = fproj.getProject();

		assertNotNull(proj);
		assertTrue(proj.exists());
		if(createTestProject){
			assertTrue(proj.getWorkspace().getRoot().getProject(testProjectName).exists());
			//IProject testProject = proj.getWorkspace().getRoot().getProject(proj.getName() + "-test");
		}else{
			assertFalse(proj.getWorkspace().getRoot().getProject(testProjectName).exists());
		}
		
		if(WAR.equals(deployType)){
			assertFalse(proj.getWorkspace().getRoot().getProject(earProjectName).exists());
			assertFalse(proj.getWorkspace().getRoot().getProject(ejbProjectName).exists());
		}else if(EAR.equals(deployType)){
			assertTrue(proj.getWorkspace().getRoot().getProject(earProjectName).exists());
			assertTrue(proj.getWorkspace().getRoot().getProject(ejbProjectName).exists());
		}
	}
	
	public void testSeamWarProjectWithTestProject() throws CoreException{
		checkProjectNamesCreation("seam12_war_t", "ear_seam12_war_t", "ejb_seam12_war_t", "test_seam12_war_t", SEAM_1_2_0, WAR, true);
	}

	public void testSeamWarProjectWithoutTestProject() throws CoreException{
		checkProjectNamesCreation("seam12_war", "ear_seam12_war", "ejb_seam12_war", "test_seam12_war", SEAM_1_2_0, WAR, false);
	}

	public void testSeamEarProjectWithTestProject() throws CoreException{
		checkProjectNamesCreation("seam12_ear_t", "ear_seam12_ear_t", "ejb_seam12_ear_t", "test_seam12_ear_t", SEAM_1_2_0, EAR, true);
	}

	public void testSeamEarProjectWithoutTestProject() throws CoreException{
		checkProjectNamesCreation("seam12_ear", "ear_seam12_ear", "ejb_seam12_ear", "test_seam12_ear", SEAM_1_2_0, EAR, false);
	}
}
