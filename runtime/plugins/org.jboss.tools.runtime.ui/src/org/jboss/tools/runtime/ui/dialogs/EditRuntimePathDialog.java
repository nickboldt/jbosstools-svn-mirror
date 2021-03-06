/*************************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.runtime.ui.dialogs;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;

/**
 * @author snjeza
 * 
 */
public class EditRuntimePathDialog extends Dialog {
	
	private RuntimePath runtimePath;
	private Set<RuntimePath> runtimePaths;
	private CheckboxTreeViewer treeViewer;

	public EditRuntimePathDialog(Shell parentShell, RuntimePath runtimePath) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER
				| SWT.RESIZE | getDefaultOrientation());
		this.runtimePath = runtimePath;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText("Edit runtime detection path");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite contents = new Composite(area, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 700;
		contents.setLayoutData(gd);
		contents.setLayout(new GridLayout(1, false));
		applyDialogFont(contents);
		initializeDialogUnits(area);

		Composite pathComposite = new Composite(contents, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		pathComposite.setLayoutData(gd);
		pathComposite.setLayout(new GridLayout(3, false));
		Label pathLabel = new Label(pathComposite, SWT.NONE);
		pathLabel.setText("Path:");
		
		final Text pathText = new Text(pathComposite, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		pathText.setLayoutData(gd);
		pathText.setText(runtimePath.getPath());
		
		Button browseButton = new Button(pathComposite, SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IDialogSettings dialogSettings = RuntimeUIActivator.getDefault().getDialogSettings();
				
				DirectoryDialog dialog = new DirectoryDialog(getShell());
				dialog.setMessage("Edit path");
				dialog.setFilterPath(pathText.getText());
				final String path = dialog.open();
				if (path == null) {
					return;
				}
				runtimePath.setPath(path);
				dialogSettings.put(RuntimeUIActivator.LASTPATH, path);
				pathText.setText(path);
//				Set<RuntimePath> runtimePaths2 = new HashSet<RuntimePath>();
//				RuntimePath runtimePath2 = new RuntimePath(path);
//				runtimePath2.setScanOnEveryStartup(runtimePath.isScanOnEveryStartup());
//				runtimePaths2.add(runtimePath2);
				RuntimeUIActivator.refreshRuntimes(getShell(), runtimePaths, treeViewer, false, 15);
				
//				runtimePath = runtimePath2;
//				runtimePaths = runtimePaths2;
			}
		
		});
		
		Label refreshLabel = new Label(pathComposite, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		refreshLabel.setLayoutData(gd);
		refreshLabel.setText("Runtimes found at this path. Remove the check mark for any runtimes you do not want identified.");
		
		final Button refreshButton = new Button(pathComposite, SWT.NONE);
		refreshButton.setText("Refresh...");
		refreshButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				RuntimeUIActivator.refreshRuntimes(getShell(), getRuntimePaths(), treeViewer, false, 15);
			}
		
		});
		
		pathText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				runtimePath.setPath(pathText.getText());
				if (!pathText.getText().isEmpty()) {
					refreshButton.setEnabled( (new File(pathText.getText()).isDirectory()) );
				}
			}
		});
		refreshButton.setEnabled( (new File(pathText.getText()).isDirectory()) );
		
		Set<RuntimePath> runtimePaths = getRuntimePaths();
		treeViewer = RuntimeUIActivator.createRuntimeViewer(runtimePaths, contents, 100);
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				RuntimeDefinition definition = (RuntimeDefinition) event.getElement();
				definition.setEnabled(!definition.isEnabled());
			}
		});
		return area;
	}

	private Set<RuntimePath> getRuntimePaths() {
		if (runtimePaths == null) {
			runtimePaths = new HashSet<RuntimePath>();
			runtimePaths.add(runtimePath);
		}
		return runtimePaths;
	}

}
