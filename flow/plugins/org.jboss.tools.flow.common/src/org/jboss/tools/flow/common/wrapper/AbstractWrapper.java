package org.jboss.tools.flow.common.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Element;

public abstract class AbstractWrapper implements Wrapper {

    private Element element;
    private transient List<ModelListener> listeners = new ArrayList<ModelListener>();
    private transient Map<Object, List<Element>> children = new HashMap<Object, List<Element>>();
    
    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
    
	public void addChild(Object type, Element element) {
		List<Element> childList = children.get(type);
		if (childList == null) {
			childList = new ArrayList<Element>();
			children.put(type, childList);
		}
		childList.add(element);
		notifyListeners(ADD_ELEMENT, type, this, null, element);
	}
	
	public void removeChild(Object type, Element element) {
		List<Element> childList = children.get(type);
		if (childList == null) return;
		childList.remove(element);
		if (childList.isEmpty()) {
			children.remove(type);
		}
		notifyListeners(REMOVE_ELEMENT, type, this, element, null);
	}
	
    public void setMetaData(String name, Object value) {
    	if (element != null) {
    		element.setMetaData(name, value);
    	}
    }
    
    public Object getMetaData(String name) {
    	if (element != null) {
    		return element.getMetaData(name);
    	}
    	return null;
    }
    
	public void addListener(ModelListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ModelListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners(int type, Object discriminator, Object object, Object oldValue, Object newValue) {
		notifyListeners(new ModelEvent(type, discriminator, object, oldValue, newValue));
	}
	
	public void notifyListeners(ModelEvent event) {
		for (ModelListener listener: listeners) {
			listener.modelChanged(event);
		}
	}
	
	public void notifyListeners(int change) {
		notifyListeners(change, null, null, null, null);
	}

	public Object getEditableValue() {
		if (getPropertySource() != null) {
			getPropertySource().getEditableValue();
		}
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (getPropertySource() != null) {
			return getPropertySource().getPropertyDescriptors();
		}
		return null;
	}

	public Object getPropertyValue(Object id) {
		if (getPropertySource() != null) {
			return getPropertySource().getPropertyValue(id);
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if (getPropertySource() != null) {
			return getPropertySource().isPropertySet(id);
		}
		return false;
	}

	public void resetPropertyValue(Object id) {
		if (getPropertySource() != null) {
			getPropertySource().resetPropertyValue(id);
		}
	}

	public void setPropertyValue(Object id, Object newValue) {
		if (getPropertySource() != null) {
			Object oldValue = getPropertySource().getPropertyValue(id);
			getPropertySource().setPropertyValue(id, newValue);
			notifyListeners(CHANGE_PROPERTY, id, this, oldValue, newValue);
		}		
	}
	
	protected abstract IPropertySource getPropertySource(); 	

   @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == Element.class) {
    		return element;
    	}
    	return null;
    }

}
