/*
 * $Id: GroupableTableHeaderUI.java,v 1.2 2005/09/22 03:58:41 evickroy Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.awt.Point;

/**
 * Pluggable UI for <code>JXGroupableTableHeader</code>.
 */
public interface GroupableTableHeaderUI {
    public abstract int columnAtPoint(Point point);
}
