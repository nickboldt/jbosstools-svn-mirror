package org.jboss.tools.flow.jpdl4.strategy;

import org.jboss.tools.flow.common.model.Container;
import org.jboss.tools.flow.common.strategy.AcceptsElementStrategy;
import org.jboss.tools.flow.jpdl4.model.Process;
import org.jboss.tools.flow.jpdl4.model.StartState;

public class ProcessAcceptsElementStrategy implements AcceptsElementStrategy {
	
	private Process process;

	public boolean acceptsElement(Object element) {
		if (process == null) {
			return false;
		} else if (element instanceof StartState) {
			return process.getStartState() == null;
		} else {
			return true;
		}
	}
	
	public void setContainer(Container container) {
		if (container instanceof Process) {
			this.process = (Process)container;
		}
	}

}
