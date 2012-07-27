/*
 * $Id: JXListPanel.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.awt.*;

import javax.swing.*;

/**
 * A form control to represents arrays or list of items.
 * This will be adapted into some sort of list editor.
 */
public class JXListPanel extends JPanel {

    private static Dimension PREFSIZE = new Dimension(200, 34);
    private JList list;

    public JXListPanel() {
	setLayout(new BorderLayout());

	list = new JList();
	list.setPrototypeCellValue("MondayXXXXXXXXXXX");

	add(new JScrollPane(list));
    }

    public JList getList() {
	return list;
    }

    public Dimension getPreferredSize() {
	return PREFSIZE;
    }

    public Dimension getMaximiumSize() {
	return getPreferredSize();
    }

}
