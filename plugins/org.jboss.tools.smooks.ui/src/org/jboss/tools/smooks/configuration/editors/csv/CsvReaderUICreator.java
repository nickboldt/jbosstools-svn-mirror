/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.csv;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.jboss.tools.smooks.configuration.editors.AttributeFieldEditPart;
import org.jboss.tools.smooks.configuration.editors.PropertyUICreator;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.csv.CsvPackage;

/**
 * @author Dart Peng (dpeng@redhat.com) Date Apr 10, 2009
 */
public class CsvReaderUICreator extends PropertyUICreator {


	private List<FieldText> fieldsList = new ArrayList<FieldText>();


	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.configuration.editors.IPropertyUICreator#
	 * createPropertyUI(org.eclipse.ui.forms.widgets.FormToolkit,
	 * org.eclipse.swt.widgets.Composite,
	 * org.eclipse.emf.edit.provider.IItemPropertyDescriptor, java.lang.Object,
	 * org.eclipse.emf.ecore.EAttribute)
	 */
	public AttributeFieldEditPart createPropertyUI(FormToolkit toolkit, Composite parent,
			IItemPropertyDescriptor propertyDescriptor, Object model, EAttribute feature,
			ISmooksModelProvider formEditor, IEditorPart part) {

		if (feature == CsvPackage.eINSTANCE.getCsvReader_Encoding()) {
		}
		if (feature == CsvPackage.eINSTANCE.getCsvReader_Fields()) {
		}
		if (feature == CsvPackage.eINSTANCE.getCsvReader_Quote()) {
		}
		if (feature == CsvPackage.eINSTANCE.getCsvReader_Separator()) {
		}
		if (feature == CsvPackage.eINSTANCE.getCsvReader_SkipLines()) {
		}

		return super.createPropertyUI(toolkit, parent, propertyDescriptor, model, feature, formEditor, part);
	}

	@Override
	public boolean ignoreProperty(EAttribute feature) {
		if (feature.equals(CsvPackage.Literals.CSV_READER__FIELDS)) {
			return true;
		}
		return super.ignoreProperty(feature);
	}

	@Override
	public List<AttributeFieldEditPart> createExtendUIOnBottom(AdapterFactoryEditingDomain editingdomain,
			FormToolkit toolkit, Composite parent, Object model, ISmooksModelProvider formEditor, IEditorPart editorPart) {
		createFiledsComposite(editingdomain, toolkit, parent, model, formEditor);
		return super.createExtendUIOnBottom(editingdomain, toolkit, parent, model, formEditor, editorPart);
	}

	private void createFiledsComposite(AdapterFactoryEditingDomain editingdomain, FormToolkit toolkit,
			Composite parent, Object model, ISmooksModelProvider formEditor) {
		fieldsList.clear();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		Group fieldsComposite = new Group(parent, SWT.NONE);
		fieldsComposite.setBackground(toolkit.getColors().getBackground());
		fieldsComposite.setText(Messages.CsvReaderUICreator_FieldsLabel);
		fieldsComposite.setLayoutData(gd);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		fieldsComposite.setLayout(gl);

		IItemPropertySource propertySource = (IItemPropertySource) editingdomain.getAdapterFactory().adapt(model,
				IItemPropertySource.class);
		final IItemPropertyDescriptor descriptor = propertySource.getPropertyDescriptor(model,
				CsvPackage.Literals.CSV_READER__FIELDS);

		final Object readOnlyMoel = model;

		String fields = (String) SmooksUIUtils.getEditValue(descriptor, model);

		gd = new GridData(GridData.FILL_BOTH);
		final TableViewer fieldsViewer = new TableViewer(fieldsComposite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		fieldsViewer.getControl().setLayoutData(gd);
		fieldsViewer.getTable().setLinesVisible(true);
		fieldsViewer.setContentProvider(new FieldsContentProvider());
		fieldsViewer.setLabelProvider(new FieldsLabelProvider());

		CellEditor fieldCellEditor = new TextCellEditor(fieldsViewer.getTable(), SWT.BORDER);

		fieldsViewer.getTable().setLinesVisible(true);

		fieldsViewer.setCellEditors(new CellEditor[] { fieldCellEditor });

		fieldsViewer.setColumnProperties(new String[] { "field" }); //$NON-NLS-1$

		fieldsViewer.setCellModifier(new ICellModifier() {

			public void modify(Object element, String property, Object value) {
				Object el = null;
				if (element instanceof Item) {
					el = ((Item) element).getData();
				}
				if (el == null)
					return;
				if (el instanceof FieldText && value instanceof String) {
					if (property.equals("field")) { //$NON-NLS-1$

						if (value.toString().equals(((FieldText) el).getText())) {
							return;
						}
						((FieldText) el).setText(value.toString());

						fieldsViewer.refresh(el);
						setFieldsValue(readOnlyMoel, descriptor);
					}

				}
			}

			public Object getValue(Object element, String property) {
				if (element instanceof FieldText) {
					if (property.equals("field")) { //$NON-NLS-1$
						return ((FieldText) element).getText();
					}
				}

				return null;
			}

			public boolean canModify(Object element, String property) {
				if (element instanceof FieldText) {
					if (property.equals("field")) { //$NON-NLS-1$
						return true;
					}
				}
				return false;
			}
		});
		if (fields == null) {
			fields = ""; //$NON-NLS-1$
		}
		fillFieldsList(fields);
		fieldsViewer.setInput(fieldsList);

		Composite buttonComposite = toolkit.createComposite(fieldsComposite);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		buttonComposite.setLayoutData(gd);

		GridLayout bgl = new GridLayout();
		buttonComposite.setLayout(bgl);

		gd = new GridData(GridData.FILL_HORIZONTAL);

		final Button addButton = new Button(buttonComposite, SWT.NONE);
		addButton.setLayoutData(gd);
		addButton.setText(Messages.CsvReaderUICreator_AddButtonLabel);

		final Button removeButton = new Button(buttonComposite, SWT.NONE);
		removeButton.setLayoutData(gd);
		removeButton.setText(Messages.CsvReaderUICreator_RemoveButtonLabel);

		addButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				addButton.setEnabled(false);
				try {
					FieldText field = new FieldText("field"); //$NON-NLS-1$
					fieldsList.add(field);
					fieldsViewer.refresh();
					setFieldsValue(readOnlyMoel, descriptor);
				} catch (Throwable t) {

				} finally {
					addButton.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection s = (IStructuredSelection) fieldsViewer.getSelection();
				fieldsList.removeAll(s.toList());
				fieldsViewer.refresh();
				setFieldsValue(readOnlyMoel, descriptor);
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void setFieldsValue(Object model, IItemPropertyDescriptor ps) {
		String fieldsString = ""; //$NON-NLS-1$
		for (int i = 0; i < fieldsList.size(); i++) {
			FieldText fieldText = fieldsList.get(i);
			fieldsString += fieldText.getText();
			if (i < fieldsList.size() - 1) {
				fieldsString += ","; //$NON-NLS-1$
			}
		}
		ps.setPropertyValue(model, fieldsString);
	}

	private void fillFieldsList(String fieldsString) {
		if (fieldsString == null || fieldsString.length() == 0)
			return;
		String input = fieldsString.toString();
		input = input.trim();
		if (input.indexOf(",") != -1) { //$NON-NLS-1$
			String[] fields = input.split(","); //$NON-NLS-1$
			if (fields != null && fields.length > 0) {
				for (int i = 0; i < fields.length; i++) {
					String field = fields[i];
					if (field != null) {
						field = field.trim();
						fieldsList.add(new FieldText(field));
					}
				}
			}
		}else{
			fieldsList.add(new FieldText(input));
		}
	}

	private class FieldsLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof FieldText) {
				return ((FieldText) element).getText();
			}
			return super.getText(element);
		}

	}

	private class FieldsContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List<?>) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[] {};
		}
	}

	private class FieldText {
		private String text = null;

		public FieldText(String t) {
			setText(t);
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}
}