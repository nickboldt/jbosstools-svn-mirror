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
package org.jboss.tools.smooks.graphical.editors.process;

import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class ProcessTaskAnalyzer {

	public void analyzeTaskNode(ProcessType process, SmooksResourceListType resourceList) {
		process.getTask().clear();
		if (resourceList == null)
			return;
		// Input task node must be in process:
		TaskType inputTask = ProcessFactory.eINSTANCE.createTaskType();
		inputTask.setId(TaskTypeManager.TASK_ID_INPUT);
		inputTask.setName(TaskTypeManager.getTaskLabel(TaskTypeManager.TASK_ID_INPUT));

		process.addTask(inputTask);

		List<AbstractResourceConfig> resourceConfigList = resourceList.getAbstractResourceConfig();

		// the java mapping is the next task node of input task:
		TaskType javaMappingTask = null;
		for (Iterator<?> iterator = resourceConfigList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (abstractResourceConfig instanceof BeanType) {
				javaMappingTask = ProcessFactory.eINSTANCE.createTaskType();
				javaMappingTask.setId(TaskTypeManager.TASK_ID_JAVA_MAPPING);
				javaMappingTask.setName(TaskTypeManager.getTaskLabel(TaskTypeManager.TASK_ID_JAVA_MAPPING));
				inputTask.addTask(javaMappingTask);
				break;
			}
		}

		// the apply template is the next task node of java mapping task:
		if (javaMappingTask != null) {
			for (Iterator<?> iterator = resourceConfigList.iterator(); iterator.hasNext();) {
				AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
				if (abstractResourceConfig instanceof Freemarker) {
					String messageType = SmooksModelUtils.getParamValue(((Freemarker) abstractResourceConfig)
							.getParam(), SmooksModelUtils.KEY_TEMPLATE_TYPE);
					if (SmooksModelUtils.FREEMARKER_TEMPLATE_TYPE_CSV.equals(messageType)) {
						TemplateAppyTaskNode templateTask = (TemplateAppyTaskNode) ProcessFactory.eINSTANCE
								.createTemplateTask();
						templateTask.setType(messageType);
						templateTask.addSmooksModel(abstractResourceConfig);
						javaMappingTask.addTask(templateTask);
					}
				}
			}
		}
	}
}
