/*
 * $Id: MetalLookAndFeelAddons.java 1050 2007-01-26 11:11:44Z ovisvana $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.exalto.org.jdesktop.swingx.plaf.metal;

import org.jdesktop.swingx.plaf.basic.BasicLookAndFeelAddons;
import com.exalto.org.jdesktop.swingx.plaf.Addons;

/**
 * MetalLookAndFeelAddons.<br>
 *
 */
public class MetalLookAndFeelAddons extends BasicLookAndFeelAddons implements Addons {

  public void initialize() {
    super.initialize();
    loadDefaults(getDefaults());
  }

  public void uninitialize() {
    super.uninitialize();
    unloadDefaults(getDefaults());
  }
  
  private Object[] getDefaults() {
    Object[] defaults =
      new Object[] {
//        "DirectoryChooserUI",
//        "org.jdesktop.jdnc.swing.plaf.windows.WindowsDirectoryChooserUI",
    };
    return defaults;
  }
  
}
