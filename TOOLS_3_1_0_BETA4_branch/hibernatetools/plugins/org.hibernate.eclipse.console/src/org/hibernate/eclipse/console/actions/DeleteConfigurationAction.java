/*
 * Created on 2004-11-01 by max
 * 
 */
package org.hibernate.eclipse.console.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;

/**
 * @author max
 *
 */
public class DeleteConfigurationAction extends SelectionListenerAction {
	
	public DeleteConfigurationAction() {
		super("Delete Configuration");
		setEnabled(false);
	}

	public void run() {
		List selectedNonResources = getSelectedNonResources();
		
		Iterator iter = selectedNonResources.iterator();
		while (iter.hasNext() ) {
			ConsoleConfiguration element = (ConsoleConfiguration) iter.next();
			KnownConfigurations.getInstance().removeConfiguration(element);
		}
	}	
	
	protected boolean updateSelection(IStructuredSelection selection) {
		if(!selection.isEmpty() ) {
			Iterator iter = getSelectedNonResources().iterator();
			while (iter.hasNext() ) {
				Object element = iter.next();
				if(element instanceof ConsoleConfiguration) {
					return true;
				}
			}
		}
		return false;
	}
}
