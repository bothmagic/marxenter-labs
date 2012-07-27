/*
 * $Id: XMLActionFactory.java 147 2004-10-29 17:01:32Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1.actions;

import java.net.URL;

import javax.swing.KeyStroke;

import org.jdesktop.swing.Application;

import org.jdesktop.swing.actions.AbstractActionExt;
import org.jdesktop.swing.actions.ActionManager;
import org.jdesktop.swing.actions.ActionFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A factory which takes an XML document and populates the ActionManager
 *
 * @author Mark Davidson
 */
public class XMLActionFactory extends ActionFactory {

    private static XMLActionFactory INSTANCE;

    private XMLActionFactory() {
    }

    public static XMLActionFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XMLActionFactory();
        }
        return INSTANCE;
    }

    /**
     * Loads all the actions in the in the url which represents an
     * xml document into the action manager.
     */
    public void loadActions(URL url) {
        loadActions(XMLParser.getInstance().getDocument(url));
    }

    /**
     * Loads all the actions in the DOM into the action manager.
     */
    public void loadActions(Document document) {
        NodeList nodes = document.getElementsByTagName("action");

        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                addAction((Element)node);
            }
        }
    }

    private void addAction(Element elem) {
        // extract all the components of the action
        String id = elem.getAttribute("xml:id");
        String name = elem.getAttribute("title");
        String mnemonic = elem.getAttribute("mnemonic");
        String smicon = elem.getAttribute("smallIcon");
        String lgicon = elem.getAttribute("icon");
        String keystroke = elem.getAttribute("accelerator");
        String desc = elem.getAttribute("description");
        String group = elem.getAttribute("group");
        boolean toggle = elem.hasAttribute("toggle");

        action(id, name, mnemonic, desc, smicon, lgicon,
               keystroke, group, toggle);
    }

    /**
     * Convenience method to create actions with all the attributes. A factory
     * method for other factory methods.
     */
    private AbstractActionExt action(String id, String name, String mnemonic, String desc,
                                     String smicon, String lgicon, String keystroke,
                                     String group, boolean toggle) {
        // only add actions once
        if (id == null || id.length() == 0) {
            return null;
        }
        AbstractActionExt action = ActionFactory.createTargetableAction(id,
                                                  name, mnemonic, toggle, group);

        ActionFactory.decorateAction(action, desc, desc,
                                     Application.getIcon(smicon, this),
                                     Application.getIcon(lgicon, this),
                                     KeyStroke.getKeyStroke(keystroke));

        ActionManager manager = Application.getInstance().getActionManager();
        return (AbstractActionExt)manager.addAction(action);
    }
}
