package org.jboss.tools.flow.common.editpart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.registry.ElementRegistry;
import org.jboss.tools.flow.common.wrapper.ConnectionWrapper;
import org.jboss.tools.flow.common.wrapper.Wrapper;

public class DefaultEditPartFactory implements EditPartFactory {

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart result = null;
        if (!(model instanceof Wrapper)) return result;
        Object element = ((Wrapper)model).getElement();
        if (element != null && element instanceof Element) {
        	result = ElementRegistry.createEditPart((Element)element);
        } else if (model instanceof ConnectionWrapper) {
        	result = new ConnectionEditPart();
        }
        if (result != null) {
        	result.setModel(model);
        }
        return result;
    }
    
//    private EditPart createEditPart(Object element) {
//    	if (element instanceof Flow) {
//    		return new RootEditPart();
//    	} else if (element instanceof Container) {
//    		return new ContainerEditPart();
//    	} else if (element instanceof Connection) {
//    		return new ConnectionEditPart();
//    	} else if (element instanceof Node) {
//    		return ElementRegistry.createEditPart((Node)element);
//    	} else  {
//    		return null;
//    	}
//    }
    
}
