/*
 * $Id: PrintAction.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.actions;

import java.net.URL;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdesktop.jdnc.incubator.rbair.swing.actions.TargetableAction;

public class PrintAction extends TargetableAction  {

    private final static Icon defaultIcon;

    static {
        URL url = PrintAction.class.getResource("/toolbarButtonGraphics/general/Print24.gif");
        defaultIcon = url == null ? null : new ImageIcon(url);
    }

    public PrintAction() {
        this("Print", "print", defaultIcon);
    }

    public PrintAction(String name, String id, Icon icon) {
        super(name, id, icon);
        putValue(Action.SHORT_DESCRIPTION, name);
    }
}
