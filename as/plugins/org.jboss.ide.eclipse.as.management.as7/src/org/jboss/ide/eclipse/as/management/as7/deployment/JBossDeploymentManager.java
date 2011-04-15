package org.jboss.ide.eclipse.as.management.as7.deployment;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.as.core.server.internal.v7.IJBoss7DeploymentManager;
import org.jboss.ide.eclipse.as.management.as7.deployment.TypedDeployer.DeploymentResult;

public class JBossDeploymentManager implements IJBoss7DeploymentManager {

	public DeploymentResult deployAsync(String host, int port, String deploymentName,
			File file, IProgressMonitor monitor) throws Exception {
		TypedDeployer deployer = new TypedDeployer(host, port);
		return deployer.deploy(deploymentName, file);
	}

	public DeploymentResult deploySync(String host, int port, String deploymentName,
			File file, IProgressMonitor monitor) throws Exception {
		TypedDeployer deployer = new TypedDeployer(host, port);
		return deployer.deploySync(deploymentName, file, monitor);
	}

	public DeploymentResult undeployAsync(String host, int port, String deploymentName,
			boolean removeFile, IProgressMonitor monitor) throws Exception  {
		TypedDeployer deployer = new TypedDeployer(host, port);
		return deployer.undeploy(deploymentName);
	}

	public DeploymentResult syncUndeploy(String host, int port, String deploymentName,
			boolean removeFile, IProgressMonitor monitor) throws Exception {
		TypedDeployer deployer = new TypedDeployer(host, port);
		return deployer.undeploySync(deploymentName, monitor);
	}

}
