/*
 * $Id: CancelTaskIcon.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.icon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Icon class for rendering an icon which indicates that a task can be cancelled.
 * @author Richard Bair
 * @version 1.0
 */
public class CancelTaskIcon implements Icon {
    private int width = 11;
    private int height = 11;

    public CancelTaskIcon() {
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color origColor = g.getColor();
        g.setColor(new Color(200, 64, 56));
        g.drawRect(0, 0, 10, 10);
        g.setColor(new Color(240, 120, 120));
        g.drawRect(1, 1, 8, 8);
        g.setColor(new Color(232, 48, 56));
        g.fillRect(2, 2, 7, 7);
        g.setColor(origColor);
    }

}
