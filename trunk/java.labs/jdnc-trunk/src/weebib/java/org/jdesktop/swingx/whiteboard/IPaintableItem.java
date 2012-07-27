/*
 * $Id: IPaintableItem.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard;

import java.awt.*;

/**
 * A IPaintableItem is any item that can be rendered within a Whiteboard instance to which it can be dynamically added.
 */
public interface IPaintableItem {
	public void paint(Graphics2D g);
	public String getDescription();
}
