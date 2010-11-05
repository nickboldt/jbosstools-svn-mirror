/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ui.bot.ext.gef;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefFigureCanvas;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;

/**
 * Gef figure bot controler. It performs actions which are missing or works
 * unreliably in standard SWTBot
 * 
 * @author jpeterka
 * 
 */
public class SWTBotGefFigure {

	private IFigure figure;
	private Logger log = Logger.getLogger(SWTBotGefFigure.class);
	private SWTBotGefEditor editor;
	private SWTBot bot;
	private SWTBotGefFigureCanvas canvas;
	private SWTBotGefEditPart part;

	private SWTBotGefFigure(SWTBotGefEditor editor, SWTBotGefEditPart part,
			IFigure figure) {
		this.editor = editor;
		this.bot = editor.bot();
		this.canvas = SWTBotGefFinder.findCanvas(editor);
		this.part = part;
		this.figure = figure;
	}

	/**
	 * Default Constructor, requires gef editor and gef part
	 * 
	 * @param editor
	 * @param part
	 */
	public SWTBotGefFigure(SWTBotGefEditor editor, SWTBotGefEditPart part) {
		this(editor, part, ((GraphicalEditPart) part.part()).getFigure());
	}

	/**
	 * Return figures bounds
	 */
	public Rectangle getBounds() {
		return getBounds(this.getFigure());
	}

	private Rectangle getBounds(IFigure figure) {
		final Rectangle bounds = figure.getBounds().getCopy();
		return bounds;
	}

	/**
	 * Perform log report for figure
	 */
	public void logInfo() {
		logInfo(this.figure, 1);
	}

	private void logInfo(IFigure figure, int level) {
		Rectangle rect = getBounds(figure);

		log.info("Figure relative level:" + level);
		log.info("Figure clas:" + figure.getClass()); //$NON-NLS-1$
		log.info("Figure bounds:" + rect.x + "," + rect.y + "," + (rect.x + rect.width) + "," + (rect.y + rect.height)); //$NON-NLS //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

		if (figure instanceof Label) {
			Label label = (Label) figure;
			log.info("Label text:" + label.getText());
		}
		log.info("--------------------------------------------------"); //$NON-NLS-1$
	}

	/**
	 * Perform log information for entire figure subtree
	 */
	public void logTreeInfo() {
		logTreeInfo(this.figure, 1);
	}

	private void logTreeInfo(IFigure figure, int level) {
		@SuppressWarnings("unchecked")
		List<IFigure> children = (List<IFigure>) figure.getChildren();
		for (IFigure f : children) {
			if (f.getChildren().isEmpty())
				logInfo(f, level);
			else
				logTreeInfo(f, level + 1);
		}
	}

	/**
	 * Use it carefully and inside UIThread only!
	 * 
	 * @return
	 */
	public IFigure getFigure() {
		return this.figure;
	}

	/**
	 * Returns Label Figure which contains given text
	 * 
	 * @param string
	 * @return
	 */
	public SWTBotGefFigure labelFigure(String string) {
		List<IFigure> figures = new ArrayList<IFigure>();
		getSubFigures(this.figure, figures);
		for (IFigure figure : figures) {
			if (figure instanceof Label) {
				Label label = (Label) figure;
				log.info(label.getText());
				if (label.getText().equalsIgnoreCase(string))
					return new SWTBotGefFigure(editor, part, label);
			}
		}
		throw new WidgetNotFoundException("No Label with " + string + " found");
	}

	/**
	 * Returns text of Label Figure
	 * 
	 * @return
	 */
	public String getText() {
		if (figure instanceof Label) {
			return ((Label) figure).getText();
		}
		throw new WidgetNotFoundException("Widget is not Label type");
	}

	/**
	 * Performs setText action for Label figure in GEF editor
	 * 
	 * @param str
	 */
	public void setText(String str) {
		part.select();
		SWTBotGefMouse mouse = new SWTBotGefMouse(bot, canvas);
		mouse.moveAndClick(this);
		SWTBotText text = bot.text(0);
		canvas.typeText(text.widget, str);
	}

	private void getSubFigures(IFigure figure, List<IFigure> figures) {
		@SuppressWarnings("unchecked")
		List<IFigure> children = (List<IFigure>) figure.getChildren();
		for (IFigure f : children) {
			if (f.getChildren().isEmpty())
				figures.add(f);
			else
				getSubFigures(f, figures);
		}
	}

	/**
	 * Calculates center point of the figure
	 * 
	 * @return
	 */
	public Point getCenter() {
		Rectangle bounds = getBounds();
		return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height
				/ 2);
	}

}
