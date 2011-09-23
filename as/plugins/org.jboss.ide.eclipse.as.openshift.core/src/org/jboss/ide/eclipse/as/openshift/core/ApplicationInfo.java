/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.ide.eclipse.as.openshift.core;

import java.util.Date;

/**
 * @author André Dietisheim
 */
public class ApplicationInfo {

	private String name;
	private String uuid;
	private String embedded;
	private Cartridge cartridge;
	private Date creationTime;

	public ApplicationInfo(String name, String uuid, String embedded, Cartridge cartridge, Date creationTime) {
		this.name = name;
		this.uuid = uuid;
		this.embedded = embedded;
		this.cartridge = cartridge;
		this.creationTime = creationTime;
	}

	public String getName() {
		return name;
	}

	public String getEmbedded() {
		return embedded;
	}

	public String getUuid() {
		return uuid;
	}

	public Cartridge getCartridge() {
		return cartridge;
	}

	public Date getCreationTime() {
		return creationTime;
	}

}
