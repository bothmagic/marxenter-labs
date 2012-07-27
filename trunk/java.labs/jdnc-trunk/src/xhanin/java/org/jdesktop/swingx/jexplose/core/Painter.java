/*
 * $Id: Painter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * For internal use only
 */
public interface Painter {
    public void prepare(JComponent comp);
    public void paint(Graphics g);
    public void dispose();
}
