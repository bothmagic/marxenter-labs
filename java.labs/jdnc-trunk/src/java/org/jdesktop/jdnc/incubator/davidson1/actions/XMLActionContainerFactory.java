/*
 * $Id: XMLActionContainerFactory.java 147 2004-10-29 17:01:32Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1.actions;

import org.jdesktop.swing.actions.ActionContainerFactory;
import org.jdesktop.swing.actions.ActionManager;

public class XMLActionContainerFactory extends ActionContainerFactory {

    public XMLActionContainerFactory(ActionManager manager) {
        super(manager);
    }

    // menu: a list of action and separator elements into a JMenu
    // toolBar: a list of action and separator elements into a JToolbar
    // popupMenu: a list of action and separator elements into a popup menu
    // menuBar: a list of menu elements into a JMenuBar
}
