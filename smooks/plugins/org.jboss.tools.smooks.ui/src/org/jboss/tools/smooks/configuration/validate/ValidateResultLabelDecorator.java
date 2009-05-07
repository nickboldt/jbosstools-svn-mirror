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

package org.jboss.tools.smooks.configuration.validate;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelDecorator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.FileEditorInput;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class ValidateResultLabelDecorator extends LabelDecorator implements ILabelDecorator, ILightweightLabelDecorator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ILabelDecorator#decorateImage(org.eclipse.swt
	 * .graphics.Image, java.lang.Object)
	 */
	public Image decorateImage(Image image, Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ILabelDecorator#decorateText(java.lang.String,
	 * java.lang.Object)
	 */
	public String decorateText(String text, Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {

	}

	protected int markErrorWarningPropertyUI(Diagnostic diagnostic, Object model) {
		if (diagnostic == null || diagnostic.getSeverity() == Diagnostic.OK) {
			return Diagnostic.OK;
		}
		List<?> data = diagnostic.getData();
		for (Object object : data) {
			if (object instanceof EObject) {
				EObject eObject = (EObject) object;
				if (eObject instanceof AbstractAnyType) {
					if (eObject == model) {
						return diagnostic.getSeverity();
					}
				}
			}
		}

		List<Diagnostic> children = diagnostic.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			Diagnostic diagnostic2 = (Diagnostic) iterator.next();
			int i = markErrorWarningPropertyUI(diagnostic2, model);
			if (i != -1) {
				return i;
			}
		}

		return -1;

	}

	public void decorate(Object element, IDecoration decoration) {
		try {
			element = AdapterFactoryEditingDomain.unwrap(element);
			if (element instanceof AbstractAnyType) {
				IResource resource = SmooksUIUtils.getResource((EObject) element);
				IWorkbenchWindow window = SmooksConfigurationActivator.getDefault().getWorkbench().getActiveWorkbenchWindow();
				if (window == null) {
					return;
				}
				SmooksMultiFormEditor editor = (SmooksMultiFormEditor) window.getActivePage().findEditor(
						new FileEditorInput((IFile) resource));
				int type = markErrorWarningPropertyUI(editor.getDiagnostic(), element);
				decoration.addOverlay(null, IDecoration.BOTTOM_RIGHT);
				if (type == Diagnostic.ERROR) {
					decoration.addOverlay(SmooksConfigurationActivator.getDefault().getImageRegistry().getDescriptor(
							GraphicsConstants.IMAGE_OVR_ERROR), IDecoration.BOTTOM_RIGHT);
				}
				if (type == Diagnostic.WARNING) {
					decoration.addOverlay(SmooksConfigurationActivator.getDefault().getImageRegistry().getDescriptor(
							GraphicsConstants.IMAGE_OVR_WARING), IDecoration.BOTTOM_RIGHT);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@Override
	public Image decorateImage(Image image, Object element, IDecorationContext context) {
		return null;
	}

	@Override
	public String decorateText(String text, Object element, IDecorationContext context) {
		return null;
	}

	@Override
	public boolean prepareDecoration(Object element, String originalText, IDecorationContext context) {
		return true;
	}

}
