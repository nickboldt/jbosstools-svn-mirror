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
package org.jboss.tools.smooks.graphical.editors.editparts;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Shape;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeConnectionEditPart;

/**
 * @author Dart
 * 
 */
public class TriggerConnectionEditPart extends TreeNodeConnectionEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.tree.editparts.TreeNodeConnectionEditPart#
	 * createConnectionFigure()
	 */
	@Override
	protected Connection createConnectionFigure() {
		Connection c = super.createConnectionFigure();
		if (c instanceof Shape) {
			((Shape) c).setLineWidth(2);
			((Shape) c).setForegroundColor(FigureUtilities.darker(GraphicsConstants.FONT_COLOR));
		}
		return c;
	}
}
