/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.integration;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.uiutils.RESTFullExplorer;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView;
import org.jboss.tools.ws.ui.bot.test.widgets.WsTesterView.Request_Type;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
@Require(server = @Server(state = ServerState.Running), perspective = "Java EE")
public class JAXRSToolingIntegrationTest extends RESTfulTestBase {

	private static String projectName = "integration1"; 
	private String localhostUrl = "http://localhost:8080/";
	private String serviceUrl = localhostUrl + projectName + "/rest/"; 
	private WsTesterView wsTesterView = new WsTesterView();
	
	@BeforeClass
	public static void prepareWS() {
		importRestWSProject(projectName);
		servers.addProjectToServer(
				projectName, configuredState.getServer().name);
	}
	
	@After
	public void minimizeTester() {
		/* minimize web service tester */
		wsTesterView.minimize();
	}
	
	@Test
	public void testGetMethod() {
		/* get JAX-RS REST Web Services */
		restfulWizard = new RESTFullExplorer(projectName);
		
		/* run on server - web service tester should be shown */
		runRestServiceOnConfiguredServer(restfulWizard.restService("GET"));
		assertWebServiceTesterIsActive();
		
		/* test generated url and response after invoking */
		wsTesterView.show();
		assertEquals(serviceUrl + "get", wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, Request_Type.GET);
		assertEquals("GET method", wsTesterView.getResponseBody());
		assertEquals("[HTTP/1.1 200 OK]", wsTesterView.getResponseHeaders()[0]);
	}
	
	@Test
	public void testPostMethod() {
		/* get JAX-RS REST Web Services */
		restfulWizard = new RESTFullExplorer(projectName);
		
		/* run on server - web service tester should be shown */
		runRestServiceOnConfiguredServer(restfulWizard.restService("POST"));
		assertWebServiceTesterIsActive();
		
		/* test generated url and response after invoking */
		wsTesterView.show();
		assertEquals(serviceUrl + "post", wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, Request_Type.POST);
		assertEquals("POST method", wsTesterView.getResponseBody());
		assertEquals("[HTTP/1.1 200 OK]", wsTesterView.getResponseHeaders()[0]);
	}
	
	@Test
	public void testPutMethod() {
		/* get JAX-RS REST Web Services */
		restfulWizard = new RESTFullExplorer(projectName);
		
		/* run on server - web service tester should be shown */
		runRestServiceOnConfiguredServer(restfulWizard.restService("PUT"));
		assertWebServiceTesterIsActive();
		
		/* test generated url and response after invoking */
		wsTesterView.show();
		assertEquals(serviceUrl + "put", wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, Request_Type.PUT);
		assertEquals("PUT method", wsTesterView.getResponseBody());
		assertEquals("[HTTP/1.1 200 OK]", wsTesterView.getResponseHeaders()[0]);
	}
	
	@Test
	public void testDeleteMethod() {
		/* get JAX-RS REST Web Services */
		restfulWizard = new RESTFullExplorer(projectName);
		
		/* run on server - web service tester should be shown */
		runRestServiceOnConfiguredServer(restfulWizard.restService("DELETE"));
		assertWebServiceTesterIsActive();
		
		/* test generated url and response after invoking */
		wsTesterView.show();
		assertEquals(serviceUrl + "delete", wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, Request_Type.DELETE);
		assertEquals("DELETE method", wsTesterView.getResponseBody());
		assertEquals("[HTTP/1.1 200 OK]", wsTesterView.getResponseHeaders()[0]);
	}
	
	@Test
	public void testUnavailableServiceMethod() {
		/* get JAX-RS REST Web Services */
		restfulWizard = new RESTFullExplorer(projectName);
		
		/* run on server - web service tester should be shown */
		runRestServiceOnConfiguredServer(restfulWizard.restService("GET"));
		assertWebServiceTesterIsActive();
		
		/* test generated url and response after invoking */
		wsTesterView.show();
		assertEquals(serviceUrl + "get", wsTesterView.getServiceURL());
		
		invokeMethodInWSTester(wsTesterView, Request_Type.POST);
		assertFalse(wsTesterView.getResponseBody().equals("GET method"));
		assertEquals("[HTTP/1.1 405 Method Not Allowed]", wsTesterView.getResponseHeaders()[0]);
	}
	
}
