/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.template;

import java.util.Map;

import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.mapping.VpeDomMapping;
import org.jboss.tools.vpe.editor.util.HTML;
import org.mozilla.interfaces.nsIDOMDocument;
import org.mozilla.interfaces.nsIDOMElement;
import org.mozilla.interfaces.nsIDOMNode;
import org.mozilla.interfaces.nsIDOMNodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VpeFacetCreator extends VpeAbstractCreator {
	private boolean caseSensitive;

	VpeFacetCreator(Element element, VpeDependencyMap dependencyMap, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	@Override
	public VpeCreatorInfo create(VpePageContext pageContext, Node sourceNode, nsIDOMDocument visualDocument, nsIDOMElement visualElement, Map visualNodeMap) {
		VpeCreatorInfo creatorInfo = null;

		boolean isHeader = false;
		boolean isFooter = false;

		Node nameAttr = sourceNode.getAttributes().getNamedItem("name");
		if (nameAttr != null) {
			String name = nameAttr.getNodeValue();
			isHeader = name.equals("header");
			isFooter = name.equals("footer");
		}

		if (isHeader || isFooter) {
			Node sourceParent = sourceNode.getParentNode();
			if (sourceParent != null) {
				nsIDOMNode visualParent = null;
				VpeDomMapping domMapping = pageContext.getDomMapping();
				if (sourceParent != null && domMapping != null) {
					visualParent = pageContext.getDomMapping().getVisualNode(sourceParent);
				}

				nsIDOMNode header = null;
				nsIDOMNode footer = null;

				if (visualParent != null && visualParent.getNodeName().equalsIgnoreCase("table")) {
					nsIDOMNodeList children = visualParent.getChildNodes();
					long count = children != null ? children.getLength() : 0;
					if (count > 0) {
						for (long i = 0; i < count; i++) {
							nsIDOMNode node = children.item(i);
							if (node.getNodeType() == nsIDOMNode.ELEMENT_NODE) {
								if (isHeader && HTML.TAG_THEAD.equalsIgnoreCase(node.getNodeName())) {
									header = node;
									break;
								} else if (isFooter && HTML.TAG_TFOOT.equalsIgnoreCase(node.getNodeName())) {
									footer = node;
									break;
								}
							}
						}
					}
				}

				nsIDOMElement cell = null;
				int columnsCount = getColumnsCount(sourceParent);
				if (isHeader) {
					cell = makeCell(columnsCount, HTML.TAG_TH, visualDocument);
				} else if (isFooter) {
					cell = makeCell(columnsCount, HTML.TAG_TD, visualDocument);
				}
				if (cell != null) {
					if (isHeader) {
						setCellClass(cell, getTableAttrValue(sourceParent, "headerClass"));
					} else if (isFooter) {
						setCellClass(cell, getTableAttrValue(sourceParent, "footerClass"));
					}
					creatorInfo = new VpeCreatorInfo(cell);
				}
			}
		}
		return creatorInfo;
	}



	/* (non-Javadoc)
	 * @see org.jboss.tools.vpe.editor.template.VpeAbstractCreator#isRecreateAtAttrChange(org.jboss.tools.vpe.editor.context.VpePageContext, org.w3c.dom.Element, org.mozilla.interfaces.nsIDOMDocument, org.mozilla.interfaces.nsIDOMNode, java.lang.Object, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isRecreateAtAttrChange(VpePageContext pageContext,
			Element sourceElement, nsIDOMDocument visualDocument,
			nsIDOMNode visualNode, Object data, String name, String value) {
		return true;
	}

	public Node getNodeForUptate(VpePageContext pageContext, Node sourceNode, Node visualNode, Map visualNodeMap) {
		return sourceNode.getParentNode();
	}

	private void setCellClass(nsIDOMElement cell, String className) {
		if (cell != null) {
			if (className != null && className.trim().length() > 0) {
				cell.setAttribute("class", className);
			}
		}
	}

	private String getTableAttrValue(Node dataTableNode, String attrName) {
		if (dataTableNode != null) {
			Node attr = dataTableNode.getAttributes().getNamedItem(attrName);
			if (attr != null) {
				return attr.getNodeValue();
			}
		}
		return null;
	}

	private int getColumnsCount(Node dataTableNode) {
		int count = 0;
		NodeList childs = dataTableNode.getChildNodes();
		int length = childs != null ? childs.getLength() : 0;
		for (int i = 0; i < length; i++) {
			Node child = childs.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().indexOf(":column") > 0) {
				count++;
			}
		}
		return count;
	}

	private nsIDOMElement makeCell(int columnCount, String cellTag, nsIDOMDocument visualDocument) {
		nsIDOMElement visualCell = visualDocument.createElement(cellTag);
		if (columnCount > 1) {
			visualCell.setAttribute("colspan", "" + columnCount);
		}
		return visualCell;
	}
}
