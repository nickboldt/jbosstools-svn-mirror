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
package org.jboss.tools.jst.web.tld.model.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.jst.web.tld.URIConstants;
import org.jboss.tools.jst.web.tld.model.TLDUtil;

public class TLDToPaletteHelper {
    public static final String START_TEXT = "start text"; //$NON-NLS-1$
    public static final String END_TEXT = "end text"; //$NON-NLS-1$
    public static final String REFORMAT = "automatically reformat tag body"; //$NON-NLS-1$
    public static final String DESCRIPTION = "description"; //$NON-NLS-1$
    public static final String URI = URIConstants.LIBRARY_URI;
    public static final String DEFAULT_PREFIX = URIConstants.DEFAULT_PREFIX;
    public static final String ADD_TAGLIB = "add taglib"; //$NON-NLS-1$

    public TLDToPaletteHelper() {}

    public XModelObject createMacroByTag(XModelObject tag, XModel model) {
        Properties p = new Properties();
        String parentname = getTldName(tag.getParent());
        String prefix = (parentname.length() == 0) ? "" : parentname + ":"; //$NON-NLS-1$ //$NON-NLS-2$
        String shortname = tag.getAttributeValue("name"); //$NON-NLS-1$
        String name = prefix + shortname;
        String tagname = shortname; ///name;
		p.setProperty("name", shortname); //$NON-NLS-1$
        boolean empty = "empty".equals(tag.getAttributeValue("bodycontent")); //$NON-NLS-1$ //$NON-NLS-2$
        if(!empty) p.setProperty(END_TEXT, "</" + tagname + ">"); //$NON-NLS-1$ //$NON-NLS-2$
        p.setProperty(START_TEXT, getStartText(tag, empty, tagname));
        p.setProperty(DESCRIPTION, getTagDescription(tag, empty, name));
        if(!empty) p.setProperty(REFORMAT, "yes"); //$NON-NLS-1$
        return model.createModelObject("SharableMacroHTML", p); //$NON-NLS-1$
    }

    public static String getTldName(XModelObject tld) {
    	if(tld == null) return ""; //$NON-NLS-1$
        String n = tld.getAttributeValue("shortname"); //$NON-NLS-1$
    	if(n == null) return ""; //$NON-NLS-1$
        if(n.length() == 0) {
            n = tld.getAttributeValue("name"); //$NON-NLS-1$
            int q = n.lastIndexOf('-');
            if(q >= 0) n = n.substring(q + 1);
        }
        int s = n.lastIndexOf(' ');
        if(s >= 0) n = n.substring(s + 1);
        return n.toLowerCase();
    }

    private String getStartText(XModelObject tag, boolean empty, String name) {
        StringBuffer sb = new StringBuffer();
        sb.append("<").append(name); //$NON-NLS-1$
        XModelObject[] as = tag.getChildren();
        boolean found = false;
        for (int i = 0; i < as.length; i++) {
            if(!TLDUtil.isAttribute(as[i])) continue;
            String required = as[i].getAttributeValue("required"); //$NON-NLS-1$
            if(!"true".equals(required) && !"yes".equals(required)) continue; //$NON-NLS-1$ //$NON-NLS-2$
            sb.append(' ').append(as[i].getAttributeValue("name")).append("=\""); //$NON-NLS-1$ //$NON-NLS-2$
            if(!found) {
                sb.append('|');
                found = true;
            }
            sb.append('"');
        }
        if(empty) sb.append("/"); //$NON-NLS-1$
        sb.append(">"); //$NON-NLS-1$
        return sb.toString();
    }

    private String getTagDescription(XModelObject tag, boolean empty, String name) {
        StringBuffer sb = new StringBuffer();
        // TODO i18n the string Syntax and Attributes might need translating
        sb.append("<b>Syntax:</b><br><code>");
        if (empty) sb.append("&lt;" + name + " /&gt;"); else sb.append("&lt;" + name + "&gt;</code><br><code>&lt;/" + name + "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        sb.append("</code><br>"); //$NON-NLS-1$
        sb.append("<b>Attributes:</b><br><code>");
		int k = 0;
		 XModelObject[] as = tag.getChildren();
		 for (int i = 0; i < as.length; i++) {
			 if(!TLDUtil.isAttribute(as[i])) continue;
			 if(!isRequired(as[i])) continue;
			 sb.append("<b>").append(as[i].getAttributeValue("name")).append("</b>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			 ++k;
			 if(k < as.length) sb.append(", "); //$NON-NLS-1$
		 }
		 for (int i = 0; i < as.length; i++) {
			 if(isRequired(as[i])) continue;
			 sb.append(as[i].getAttributeValue("name")); //$NON-NLS-1$
			 ++k;
			 if(k < as.length) sb.append(", "); //$NON-NLS-1$
		}
        
        sb.append("</code>"); //$NON-NLS-1$

        return sb.toString();
    }

    private boolean isRequired(XModelObject attr) {
        String required = attr.getAttributeValue("required"); //$NON-NLS-1$
        return ("true".equals(required) || "yes".equals(required)); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public XModelObject createTabByTLD(XModelObject tld, XModel model) {
    	return createGroupByTLD(tld, model, "SharablePageTabHTML"); //$NON-NLS-1$
    }

    public XModelObject createGroupByTLD(XModelObject tld, XModel model) {
    	return createGroupByTLD(tld, model, "SharableGroupHTML"); //$NON-NLS-1$
    }

    private XModelObject createGroupByTLD(XModelObject tld, XModel model, String entity) {
        Properties p = new Properties();
        p.setProperty("name", capitalize(getTldName(tld))); //$NON-NLS-1$
        p.setProperty(DESCRIPTION, TLDUtil.getTagDescription(tld));
        p.setProperty(DEFAULT_PREFIX, getTldName(tld));
        p.setProperty(URIConstants.LIBRARY_URI, "" + tld.getAttributeValue("uri")); //$NON-NLS-1$ //$NON-NLS-2$
        XModelObject tab = model.createModelObject(entity, p);
        XModelObject[] tags = tld.getChildren();
        for (int i = 0; i < tags.length; i++)
          if(TLDUtil.isTag(tags[i])) tab.addChild(createMacroByTag(tags[i], model));
        return tab;
    }


    private String capitalize(String s) {
        return (s.length() == 0) ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}

