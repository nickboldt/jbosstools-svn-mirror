/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.xulrunner;

import java.text.MessageFormat;

import org.jboss.tools.vpe.xulrunner.browser.XulRunnerBrowser;

/**
 * @author eskimo
 *
 */
public class PlatformIsNotSupportedException extends XulRunnerException {

	/**
	 * 
	 */
	public PlatformIsNotSupportedException() {
		super(MessageFormat.format(
				VpeXulrunnerMessages.CURRENT_PLATFORM_IS_NOT_SUPPORTED,
				XulRunnerBrowser.CURRENT_PLATFORM_ID));
	}
}
