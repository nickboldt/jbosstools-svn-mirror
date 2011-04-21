/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.jsp.outline.cssdialog.events;

import java.util.EventObject;

/**
 * An event which indicates that a style change action occurred.
 */
public class ChangeStyleEvent extends EventObject {

	private static final long serialVersionUID = 3260787731782837929L;

	/**
	 * Constructor.
	 *
	 * @param source the Component that originated the event
	 */
    public ChangeStyleEvent(Object source) {
        super(source);
    }
}
