/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import org.eclipse.egit.ui.Activator;
import org.eclipse.egit.ui.UIPreferences;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;

/**
 * @author André Dietisheim
 * @author Rob Stryker
 */
public class AdapterWizardPageModel extends ObservableUIPojo {

	private static final String REMOTE_NAME_DEFAULT = "origin";

	public static final String PROPERTY_REPO_PATH = "repositoryPath";
	public static final String PROPERTY_REMOTE_NAME = "remoteName";

	public static final String CREATE_SERVER = "createServer";
	public static final String MODE = "serverMode";
	public static final String MODE_SOURCE = "serverModeSource";
	public static final String MODE_BINARY = "serverModeBinary";
	public static final String RUNTIME_DELEGATE = "runtimeDelegate";
	public static final String SERVER_TYPE = "serverType";

	private String repositoryPath;
	private String remoteName;

	private ImportProjectWizardModel wizardModel;

	public AdapterWizardPageModel(ImportProjectWizardModel wizardModel) {
		this.wizardModel = wizardModel;
		this.remoteName = REMOTE_NAME_DEFAULT;
	}

	public String getRepositoryPath() {
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		firePropertyChange(PROPERTY_REPO_PATH, this.repositoryPath, this.repositoryPath = repositoryPath);
		wizardModel.setCloneDirectory(repositoryPath);
	}

	public void resetRepositoryPath() {
		setRepositoryPath(getEGitDefaultRepositoryPath() + wizardModel.getApplication().getName());
	}

	public String getEGitDefaultRepositoryPath() {
		String destinationDir =
				Activator.getDefault().getPreferenceStore().getString(UIPreferences.DEFAULT_REPO_DIR);
		return destinationDir;
	}

	public String getRemoteName() {
		return remoteName;
	}

	public void setRemoteName(String remoteName) {
		firePropertyChange(PROPERTY_REMOTE_NAME, this.remoteName, this.remoteName = remoteName);
		wizardModel.setRemoteName(remoteName);
	}

	public void resetRemoteName() {
		setRemoteName(REMOTE_NAME_DEFAULT);
	}

	// TODO is this the best way? Or should we expose ONLY getters to the parent
	// model?
	public ImportProjectWizardModel getParentModel() {
		return wizardModel;
	}
}
