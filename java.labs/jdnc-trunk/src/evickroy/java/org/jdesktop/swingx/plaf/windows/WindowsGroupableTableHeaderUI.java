/*
 * $Id: WindowsGroupableTableHeaderUI.java 688 2005-09-16 05:29:54Z evickroy $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.windows;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI;

/**
 * Windows implementation of the <code>JXGroupableTableHeader</code> UI. <br>
 * 
 */
public class WindowsGroupableTableHeaderUI extends BasicGroupableTableHeaderUI {

  public static ComponentUI createUI(JComponent c) {
    return new WindowsGroupableTableHeaderUI();
  }

  protected void installDefaults() {
    super.installDefaults();
  }

}
