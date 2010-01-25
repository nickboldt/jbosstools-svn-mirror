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
package org.jboss.tools.smooks.graphical.editors.model.javamapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EStructuralFeatureImpl.ContainmentUpdatingFeatureMapEntry;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.WrapperItemProvider;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.configuration.SmooksConfigurationActivator;
import org.jboss.tools.smooks.configuration.editors.GraphicsConstants;
import org.jboss.tools.smooks.configuration.editors.uitls.ProjectClassLoader;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.graphical.editors.IGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigChildNodeGraphModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateConnection;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateNodeGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.FreemarkerTemplateXMLModel;
import org.jboss.tools.smooks.graphical.editors.model.freemarker.IFreemarkerTemplateModel;
import org.jboss.tools.smooks.graphical.editors.template.SmooksFreemarkerTemplateGraphicalEditor;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.DecodeParamType;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.javabean12.ValueType;
import org.jboss.tools.smooks.templating.model.ModelBuilder;
import org.jboss.tools.smooks.templating.template.TemplateBuilder;
import org.jboss.tools.smooks.templating.template.ValueMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Dart
 * 
 */
public class JavaBeanChildGraphModel extends AbstractResourceConfigChildNodeGraphModel {

	private IGraphicalEditorPart editorPart;

	public JavaBeanChildGraphModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider,
			IEditingDomainProvider domainProvider, IGraphicalEditorPart editorPart) {
		super(data, contentProvider, labelProvider, domainProvider);
		this.editorPart = editorPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel#isLinkable
	 * (java.lang.Class)
	 */
	@Override
	public boolean isLinkable(Class<?> connectionType) {
		if (connectionType == null) {
			return true;
		}
		if (TreeNodeConnection.class.isAssignableFrom(connectionType)) {
			Object data = this.getData();
			data = AdapterFactoryEditingDomain.unwrap(data);
			if (SmooksUIUtils.getBeanIDRefFeature((EObject) data) != null) {
				return true;
			}
		}
		if (data instanceof org.jboss.tools.smooks.model.javabean12.ValueType
				|| connectionType == FreemarkerTemplateConnection.class) {
			return true;
		}
		// if (connectionType == ValueBindingConnection.class) {
		// Object data = this.getData();
		// data = AdapterFactoryEditingDomain.unwrap(data);
		// if (SmooksUIUtils.getSelectorFeature((EObject)data) != null) {
		// return true;
		// }
		// }
		return false;
	}

	protected boolean inJavaMapping() {
		if (SmooksFreemarkerTemplateGraphicalEditor.ID.equals(editorPart.getID())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #canLinkWithSource(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithSource(Object model) {
		// TODO Auto-generated method stub
		if (!inJavaMapping())
			return false;
		return super.canLinkWithSource(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #canLinkWithTarget(java.lang.Object)
	 */
	@Override
	public boolean canLinkWithTarget(Object model) {
		AbstractSmooksGraphicalModel gm = (AbstractSmooksGraphicalModel) model;
		Object m = gm.getData();
		Object obj = getData();
		AbstractSmooksGraphicalModel pm = gm;
		while (pm != null && !(pm instanceof FreemarkerTemplateGraphicalModel)) {
			pm = pm.getParent();
		}
		obj = AdapterFactoryEditingDomain.unwrap(obj);
		if (obj instanceof ValueType) {
			if (m instanceof IFreemarkerTemplateModel && pm instanceof FreemarkerTemplateGraphicalModel) {
				TemplateBuilder builder = ((FreemarkerTemplateGraphicalModel) pm).getTemplateBuilder();
				if (((IFreemarkerTemplateModel) m).isHidden(builder)) {
					return false;
				}

				if (m instanceof FreemarkerTemplateXMLModel) {
					if (!((FreemarkerTemplateXMLModel) m).getXMLNodeChildren().isEmpty()) {
						return false;
					}
				}

				List<TreeNodeConnection> exsitingConnection = gm.getTargetConnections();
				if (!exsitingConnection.isEmpty())
					return false;
				for (Iterator<?> iterator = exsitingConnection.iterator(); iterator.hasNext();) {
					TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
					if (treeNodeConnection.getSourceNode() == this) {
						return false;
					}
				}

				AbstractSmooksGraphicalModel pgm = gm;
				while (pgm != null && pgm instanceof FreemarkerTemplateNodeGraphicalModel) {
					Object pd = ((FreemarkerTemplateNodeGraphicalModel) pgm).getData();
					if (pd instanceof IFreemarkerTemplateModel) {
						IFreemarkerTemplateModel iFreemarkerTemplateModel = (IFreemarkerTemplateModel) pd;
						if (iFreemarkerTemplateModel.isManyOccurs() && pgm.getTargetConnections().isEmpty()) {
							Node modelNode = iFreemarkerTemplateModel.getModelNode();
							if (modelNode instanceof Element) {
								return !ModelBuilder.getEnforceCollectionSubMappingRules((Element) modelNode);
							} else {
								return false;
							}
						}
					}
					pgm = pgm.getParent();
				}
				return true;
			}

		}
		if (!inJavaMapping())
			return false;
		return super.canLinkWithTarget(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getImage()
	 */
	@Override
	public Image getImage() {
		if (parentIsCollection()) {
			return SmooksConfigurationActivator.getDefault().getImageRegistry().get(
					GraphicsConstants.IMAGE_JAVA_COLLECTION);
		}
		if (parentIsArray()) {
			return SmooksConfigurationActivator.getDefault().getImageRegistry().get(GraphicsConstants.IMAGE_JAVA_ARRAY);
		}
		return super.getImage();
	}

	public boolean parentIsCollection() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			return SmooksUIUtils.isCollectionJavaGraphModel(parent);
		}
		return false;
	}

	public boolean parentIsArray() {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof EObject) {
			EObject parent = ((EObject) model).eContainer();
			return SmooksUIUtils.isArrayJavaGraphModel(parent);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getChildren()
	 */
	@Override
	public List<AbstractSmooksGraphicalModel> getChildren() {
		List<AbstractSmooksGraphicalModel> list = Collections.emptyList();
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #addTargetConnection(org.jboss.tools
	 * .smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void addTargetConnection(TreeNodeConnection connection, AbstractSmooksGraphicalModel sourceNode) {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof ValueType) {
			EObject owner = (EObject) model;
			AbstractSmooksGraphicalModel targetGraphModel = connection.getSourceNode();
			Object tm = targetGraphModel.getData();
			if (tm instanceof IXMLStructuredObject) {
				
				CompoundCommand compoundCommand = new CompoundCommand();
				
				String selector = SmooksUIUtils.generateFullPath((IXMLStructuredObject) tm, "/"); //$NON-NLS-1$
				Command command = SetCommand.create(domainProvider.getEditingDomain(), owner,
						Javabean12Package.Literals.VALUE_TYPE__DATA, selector);
				compoundCommand.append(command);
				
				DecoderRecorder recoder = (DecoderRecorder) connection.getData();
				
				String dataDecoder = getDataDecoder(connection);
				List<?> oldParameters = null;
				if(recoder != null){
					dataDecoder = recoder.getDecoder();
					oldParameters = recoder.getDecoderParameters();
				}
				
				if(oldParameters != null){
					Command addParamsCommand = AddCommand.create(domainProvider.getEditingDomain(), owner,
							Javabean12Package.Literals.VALUE_TYPE__DECODE_PARAM, oldParameters);
					compoundCommand.append(addParamsCommand);
				}
				
				if (dataDecoder != null) {
					Command decoderSetCommand = SetCommand.create(domainProvider.getEditingDomain(), owner,
							Javabean12Package.Literals.VALUE_TYPE__DECODER, dataDecoder);
					compoundCommand.append(decoderSetCommand);
				}
				
				domainProvider.getEditingDomain().getCommandStack().execute(compoundCommand);
				
				if (this.targetConnections.indexOf(connection) == -1) {
					this.targetConnections.add(connection);
					support.firePropertyChange(PRO_ADD_TARGET_CONNECTION, null, connection);
				}
			}
		} else {
			super.addTargetConnection(connection, sourceNode);
		}
	}

	protected String getDataDecoder(TreeNodeConnection connection) {
		Object data = getData();
		data = AdapterFactoryEditingDomain.unwrap(data);
		if(((ValueType)data).getDecoder() != null) return null;
		return SmooksUIUtils.getDefualtDecoder((ValueType)data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.graphical.editors.model.
	 * AbstractResourceConfigChildNodeGraphModel
	 * #removeTargetConnection(org.jboss
	 * .tools.smooks.gef.tree.model.TreeNodeConnection)
	 */
	@Override
	public void removeTargetConnection(TreeNodeConnection connection) {
		Object model = getData();
		model = AdapterFactoryEditingDomain.unwrap(model);
		if (model instanceof ValueType) {
			EObject owner = (EObject) model;
			
			DecoderRecorder recorder = new DecoderRecorder();
			
			CompoundCommand compoundCommand = new CompoundCommand();
			
			Command deleteDataCommand = SetCommand.create(domainProvider.getEditingDomain(), owner, Javabean12Package.Literals.VALUE_TYPE__DATA, null);
			compoundCommand.append(deleteDataCommand);
			
			String decoder = ((ValueType)model).getDecoder();
			if(decoder != null){
				Command deleteDecoderCommand = SetCommand.create(domainProvider.getEditingDomain(), owner, Javabean12Package.Literals.VALUE_TYPE__DECODER, null);
				compoundCommand.append(deleteDecoderCommand);
				recorder.setDecoder( decoder);
			}
			
			if(!((ValueType)model).getDecodeParam().isEmpty()){
				List<DecodeParamType> tempList = new ArrayList<DecodeParamType>();
				tempList.addAll(((ValueType)model).getDecodeParam());
				recorder.setDecoderParameters(tempList);
				Command remvoeParameterCommand = DeleteCommand.create(domainProvider.getEditingDomain(), ((ValueType)model).getDecodeParam());
				compoundCommand.append(remvoeParameterCommand);
			}
			domainProvider.getEditingDomain().getCommandStack().execute(compoundCommand);
			connection.setData(recorder);
			if (this.targetConnections.indexOf(connection) != -1) {
				this.targetConnections.remove(connection);
				support.firePropertyChange(PRO_REMOVE_TARGET_CONNECTION, connection, null);
			}
		} else {
			super.removeTargetConnection(connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.gef.tree.model.TreeNodeModel#getText()
	 */
	@Override
	public String getText() {
		int index = this.getParent().getChildrenWithoutDynamic().indexOf(this);
		if (parentIsArray() && index == 0) {
			return Messages.JavaBeanChildGraphModel_Array_Entry;
		}
		if (parentIsCollection() && index == 0) {
			return Messages.JavaBeanChildGraphModel_Collection_Entry;
		}
		return super.getText();
	}
	
	private class DecoderRecorder{
		private String decoder=null;
		private List<?> decoderParameters = null;
		/**
		 * @return the decoder
		 */
		public String getDecoder() {
			return decoder;
		}
		/**
		 * @param decoder the decoder to set
		 */
		public void setDecoder(String decoder) {
			this.decoder = decoder;
		}
		/**
		 * @return the decoderParameters
		 */
		public List<?> getDecoderParameters() {
			return decoderParameters;
		}
		/**
		 * @param decoderParameters the decoderParameters to set
		 */
		public void setDecoderParameters(List<?> decoderParameters) {
			this.decoderParameters = decoderParameters;
		}
		
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.graphical.editors.model.AbstractResourceConfigGraphModel#addMappingTypeInfo(org.jboss.tools.smooks.templating.template.ValueMapping)
	 */
	@Override
	public void addMappingTypeInfo(ValueMapping mapping) {
		if(getData() instanceof WrapperItemProvider) {
			WrapperItemProvider sourceData = (WrapperItemProvider) getData();
			BeanType bean = (BeanType) sourceData.getOwner();
			ValueType valueBinding = (ValueType) ((ContainmentUpdatingFeatureMapEntry)sourceData.getValue()).getValue();
			
			mapping.setValueType(getPropertyType(bean, valueBinding));
			if(mapping.getEncodeProperties() == null) {
				mapping.setEncodeProperties(getDecoderParams(valueBinding));
			}
		}				
	}
	
	private Class<?> getPropertyType(BeanType bean, ValueType valueBinding) {
		try {
			IJavaProject project = SmooksUIUtils.getJavaProject(bean);
			ProjectClassLoader classLoader = new ProjectClassLoader(project);
			Class<?> beanClass = classLoader.loadClass(bean.getClass_());
			String targetProperty = valueBinding.getProperty();
			
			if(targetProperty != null && !targetProperty.trim().equals("")) {
				StringBuilder getterNameBuilder = new StringBuilder();
				
				getterNameBuilder.append(targetProperty);
				getterNameBuilder.setCharAt(0, Character.toUpperCase(targetProperty.charAt(0)));
				getterNameBuilder.insert(0, "get");
				
				try {
					Method getterMethod = beanClass.getMethod(getterNameBuilder.toString(), new Class[] {});
					return getterMethod.getReturnType();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
				}
			}			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Properties getDecoderParams(ValueType valueBinding) {
		Properties decodeParams = new Properties();
		EList decodeParamsList = valueBinding.getDecodeParam();
		
		for(int i = 0; i < decodeParamsList.size(); i++) {
			DecodeParamType decodeParamObj = (DecodeParamType) decodeParamsList.get(i);
			decodeParams.setProperty(decodeParamObj.getName(), decodeParamObj.getValue());
		}
		
		return decodeParams;
	}
}
