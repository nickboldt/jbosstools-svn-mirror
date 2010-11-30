package org.jboss.tools.bpmn2.gmf.notation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.bpmn2.BaseElement;
import org.eclipse.bpmn2.FlowNode;
import org.eclipse.bpmn2.SequenceFlow;
import org.eclipse.bpmn2.di.BPMNEdge;
import org.eclipse.dd.dc.Bounds;
import org.eclipse.dd.dc.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.notation.Bendpoints;
import org.eclipse.gmf.runtime.notation.Connector;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;
import org.eclipse.gmf.runtime.notation.impl.ConnectorImpl;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2VisualIDRegistry;

public class BpmnEdgeImpl extends ConnectorImpl implements Connector {
	
	BPMNEdge bpmnEdge;
	
	public BpmnEdgeImpl(BPMNEdge edge) {
		this.bpmnEdge = edge;
	}
	
	public BPMNEdge getBPMNEdge() {
		return bpmnEdge;
	}

	@Override
	public void setElement(EObject newElement) {
		super.setElement(newElement);
		if (newElement instanceof BaseElement) {
			bpmnEdge.setBpmnElement((BaseElement) newElement);
		}
	}
	
	@Override
	public void setBendpoints(Bendpoints bendpoints) {
		if (bendpoints != null && bendpoints instanceof BpmnBendpointsImpl) {
			BpmnBendpointsImpl bpmnBendpoints = (BpmnBendpointsImpl)bendpoints;
			if (bpmnBendpoints.getEdge() != this) { 
				bpmnBendpoints.setEdge(this);
				// force refresh of the points list
				bpmnBendpoints.setPoints(bpmnBendpoints.getPoints());
			}
		} 
		super.setBendpoints(bendpoints);
	}
		
	public void initialize(Diagram parentView, Map<EObject, View> viewMap) {
		initializeElement(viewMap);
		initializeBendpoints();
		setType(Bpmn2VisualIDRegistry.getType(
						Bpmn2VisualIDRegistry.getLinkWithClassVisualID(getElement())));
		parentView.insertEdge(this);
	}
	
	private void initializeBendpoints() {
		RelativeBendpoints bendpoints = BpmnNotationFactory.INSTANCE.createRelativeBendpoints();
		List<Point> waypoints = getBPMNEdge().getWaypoint();
		ArrayList<RelativeBendpoint> points = new ArrayList<RelativeBendpoint>(waypoints.size());
		for (Point waypoint : waypoints) {
			float sourceX = 0, sourceY = 0, targetX = 0, targetY = 0;
			View source = getSource();
			if (source instanceof BpmnShapeImpl) {
				Bounds bounds = ((BpmnShapeImpl)source).getBPMNShape().getBounds();
				sourceX = waypoint.getX() - bounds.getX();
				sourceY = waypoint.getY() - bounds.getY();
			}
			View target = getTarget();
			if (target instanceof BpmnShapeImpl) {
				Bounds bounds = ((BpmnShapeImpl)target).getBPMNShape().getBounds();
				targetX = waypoint.getX() - bounds.getX();
				targetY = waypoint.getY() - bounds.getY();
			}
			points.add(new RelativeBendpoint((int)sourceX, (int)sourceY, (int)targetX, (int)targetY));
		}
		bendpoints.setPoints(points);
		setBendpoints(bendpoints);
		
	}

	private void initializeElement(Map<EObject, View> viewMap) {
		EObject element = bpmnEdge.getBpmnElement();
		if (element != null) {
			super.setElement(element);
		}
		if (element instanceof SequenceFlow) {
			SequenceFlow sequenceFlow = (SequenceFlow)element;
			FlowNode source = sequenceFlow.getSourceRef();
			FlowNode target = sequenceFlow.getTargetRef();
			View sourceView = viewMap.get(source);
			View targetView = viewMap.get(target);
			setSource(sourceView);
			setTarget(targetView);
		}
	}

}
