/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.hibernate.eclipse.console.workbench;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.hibernate.console.ImageConstants;
import org.hibernate.console.stubs.PersistentClassStub;
import org.hibernate.console.stubs.PropertyStub;
import org.hibernate.eclipse.console.utils.EclipseImages;

public class PersistentClassWorkbenchAdapter extends BasicWorkbenchAdapter {

	@SuppressWarnings("unchecked")
	public Object[] getChildren(Object o) {
		PersistentClassStub pc = (PersistentClassStub) o;
		PropertyStub identifierProperty = pc.getIdentifierProperty();
		List<PropertyStub> properties = new ArrayList<PropertyStub>();
		
		if(identifierProperty!=null) {
			properties.add(identifierProperty);
		}
		
		Iterator<PropertyStub> propertyClosureIterator = new JoinedIterator(properties.iterator(), pc.getPropertyClosureIterator());
		return toArray(propertyClosureIterator, PropertyStub.class, null);
	}

	
	public ImageDescriptor getImageDescriptor(Object object) {
		return EclipseImages.getImageDescriptor(ImageConstants.MAPPEDCLASS);
	}

	public String getLabel(Object o) {
		PersistentClassStub pc = (PersistentClassStub) o;
		return HibernateWorkbenchHelper.getLabelForClassName(pc.getEntityName());
	}

	public Object getParent(Object o) {
		return null;
	}

	
}
