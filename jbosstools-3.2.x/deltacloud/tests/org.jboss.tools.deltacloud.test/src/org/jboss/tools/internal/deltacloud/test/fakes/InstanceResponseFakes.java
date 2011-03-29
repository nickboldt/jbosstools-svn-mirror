/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.internal.deltacloud.test.fakes;

import org.jboss.tools.deltacloud.core.client.Instance.InstanceState;

/**
 * @author André Dietisheim
 */
public class InstanceResponseFakes {

	public static class InstanceActionResponse {
		public static final String url = "http://try.steamcannon.org/deltacloud/api/instances/i-6f16e503/start";
		public static final String method = "post";
		public static final String name = "start";
		public static final String response = ServerResponseFakes.getActionXML(url, method, name);
	}

	public static class InstanceResponse {
		public static final String url1 = "http://try.steamcannon.org/deltacloud/api/instances/i-6f16e503";
		public static final String id1 = "i-6f16e503";
		public static final String name1 = "ami-7d07ec14";
		public static final String ownerId1 = "357159121505";
		public static final String image1Url = "http://try.steamcannon.org/deltacloud/api/images/ami-7d07ec14";
		public static final String image1Id = "ami-7d07ec14";
		public static final String realm1Url = "http://try.steamcannon.org/deltacloud/api/realms/us-east-1a";
		public static final String realm1Id = "us-east-1a";
		public static final InstanceState state = InstanceState.RUNNING;
		public static final String hardwareProfile1Url = "http://try.steamcannon.org/deltacloud/api/hardware_profiles/m1.small";
		public static final String hardwareProfile1Id = "m1.small";
		public static final String keyname1 = "ad10";
		public static final String actionNameStop = "stop";
		public static final String actionNameReboot = "reboot";
		public static final String publicAddress1 = "ec2-50-16-108-18.compute-1.amazonaws.com";
		public static final String privateAddress1 = "ec2-50-16-108-18.compute-1.amazonaws.com";

		public static final String response = getInstanceResponseXML(url1, id1, name1, ownerId1, image1Url,
				image1Id, realm1Url, realm1Id, state, hardwareProfile1Url, hardwareProfile1Id, keyname1,
				actionNameStop, actionNameReboot, publicAddress1, privateAddress1);
	}

	public static class InstancesResponse {

		public static final String url1 = "http://try.steamcannon.org/deltacloud/api/instances/i-6f16e503";
		public static final String id1 = "i-6f16e503";
		public static final String name1 = "ami-7d07ec14";
		public static final String ownerId1 = "357159121505";
		public static final String image1Url = "http://try.steamcannon.org/deltacloud/api/images/ami-7d07ec14";
		public static final String image1Id = "ami-7d07ec14";
		public static final String realm1Url = "http://try.steamcannon.org/deltacloud/api/realms/us-east-1a";
		public static final String realm1Id = "us-east-1a";
		public static final InstanceState state = InstanceState.RUNNING;
		public static final String hardwareProfile1Url = "http://try.steamcannon.org/deltacloud/api/hardware_profiles/m1.small";
		public static final String hardwareProfile1Id = "m1.small";
		public static final String keyname1 = "ad10";
		public static final String actionNameStop = "stop";
		public static final String actionNameReboot = "reboot";
		public static final String publicAddress1 = "ec2-50-16-108-18.compute-1.amazonaws.com";
		public static final String privateAddress1 = "ec2-50-16-108-18.compute-1.amazonaws.com";

		public static final String url2 = "http://try.steamcannon.org/deltacloud/api/instances/i-6f16e553";
		public static final String id2 = "i-6f16e503";
		public static final String name2 = "ami-7d07ec14";
		public static final String ownerId2 = "357159121505";
		public static final String image2Url = "http://try.steamcannon.org/deltacloud/api/images/ami-7d07ec17";
		public static final String image2Id = "ami-7d07ec14";
		public static final String realm2Url = "http://try.steamcannon.org/deltacloud/api/realms/us-east-2a";
		public static final String realm2Id = "us-east-2a";
		public static final InstanceState state2 = InstanceState.STOPPED;
		public static final String hardwareProfile2Url = "http://try.steamcannon.org/deltacloud/api/hardware_profiles/m1.large";
		public static final String hardwareProfile2Id = "m1.large";
		public static final String keyname2 = "ad11";
		public static final String publicAddress2 = "ec2-50-16-108-19.compute-2.amazonaws.com";
		public static final String privateAddress2 = "ec2-50-16-108-19.compute-2.amazonaws.com";

		public static final String response =
				"<instances>"
						+ getInstanceResponseXML(url1, id1, name1, ownerId1, image1Url,
								image1Id, realm1Url, realm1Id, state, hardwareProfile1Url, hardwareProfile1Id,
								keyname1,
								actionNameStop, actionNameReboot, publicAddress1, privateAddress1)
						+ getInstanceResponseXML(url2, id2, name2, ownerId2, image2Url,
								image2Id, realm2Url, realm2Id, state, hardwareProfile2Url, hardwareProfile2Id,
								keyname2,
								actionNameReboot, actionNameReboot, publicAddress2, privateAddress2)
						+ "</instances>";

	}

	private static final String getInstanceResponseXML(String url, String id, String name, String owner,
			String imageUrl, String imageId, String realmUrl, String realmId, InstanceState state,
			String hardwareProfileUrl, String hardwareProfileId, String keyname, String actionName1,
			String actionName2, String publicAddress, String privateAddress) {
		return "<instance href=\""
				+ url
				+ "\" id=\""
				+ id
				+ "\">"
				+ "<name>"
				+ name
				+ "</name>"
				+ "<owner_id>"
				+ owner
				+ "</owner_id>"
				+ "<image href=\""
				+ imageUrl
				+ "\" id=\""
				+ imageId
				+ "\"/>"
				+ getRealmResponseXML(realmUrl, realmId)
				+ "<state>"
				+ state.toString()
				+ "</state>"
				+ getHardwareProfileXML(hardwareProfileUrl, hardwareProfileId)
				+ "<actions>"
				+ ServerResponseFakes.getActionXML("http://try.steamcannon.org/deltacloud/api/instances/" + id
						+ "/reboot", "post", actionName1)
				+ ServerResponseFakes.getActionXML("http://try.steamcannon.org/deltacloud/api/instances/" + id
						+ "/stop", "post", actionName2)
				+ "</actions>"
				+ "<public_addresses>"
				+ getAddressXML(publicAddress)
				+ "</public_addresses>"
				+ "<private_addresses>"
				+ getAddressXML(privateAddress)
				+ "</private_addresses>"
				+ "<authentication type='key'>"
				+ "<login>"
				+ "<keyname>" + keyname + "</keyname>"
				+ "</login>"
				+ "</authentication>"
				+ "</instance>";
	}

	private static String getAddressXML(String address) {
		return "<address>" + address + "</address>";
	}

	private static String getHardwareProfileXML(String url, String id) {
		return "<hardware_profile href=\"" + url + "\" id=\"" + id + "\"></hardware_profile>";
	}

	private static String getRealmResponseXML(String url, String id) {
		return "<realm href=\"" + url + "\" id=\"" + id + "\"/>";
	}

}
