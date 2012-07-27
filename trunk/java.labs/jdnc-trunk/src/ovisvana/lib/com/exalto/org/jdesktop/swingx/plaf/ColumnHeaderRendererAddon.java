/*
 * $Id: ColumnHeaderRendererAddon.java 1050 2007-01-26 11:11:44Z ovisvana $
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
package com.exalto.org.jdesktop.swingx.plaf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.LookAndFeel;

import org.jdesktop.swingx.table.ColumnHeaderRenderer;
import org.jdesktop.swingx.util.OS;
import com.exalto.org.jdesktop.swingx.plaf.AbstractComponentAddon;

/**
 * Addon for ColumnHeaderRenderer.<p>
 * Loads LF specific sort icons.
 * 
 * @author Jeanette Winzenburg
 *
 */
public class ColumnHeaderRendererAddon extends AbstractComponentAddon {
    
  public ColumnHeaderRendererAddon() {
    super("ColumnHeaderRenderer");
  }
  
//OV  public void initialize(LookAndFeelAddons addon) {
  public void initialize(Addons addon) {
      List defaults = new ArrayList();
      String upIcon = null;
      String downIcon = null;
      if (isMac(addon)) {
          upIcon = "sort-osx-up.png";
          downIcon = "sort-osx-dn.png";
      } else if (isWindows(addon)) {
          if (isXP(addon)) {
              upIcon = "sort-xp-up.png";
              downIcon = "sort-xp-dn.png";
              
          } else {
              upIcon = "sort-w2k-up.png";
              downIcon = "sort-w2k-dn.png";
              
          }
      } else if (isSynth()) {
          upIcon = "sort-gtk-up.png";
          downIcon = "sort-gtk-dn.png";
          
      } else {
          upIcon = "sort-jlf-up.png";
          downIcon = "sort-jlf-dn.png";
      }
      defaults.addAll(Arrays.asList(new Object[] { 
              ColumnHeaderRenderer.UP_ICON_KEY, 
                  LookAndFeel.makeIcon(getClass(), "resources/" + upIcon),
              ColumnHeaderRenderer.DOWN_ICON_KEY, 
                  LookAndFeel.makeIcon(getClass(), "resources/" + downIcon),
      }));
      addon.loadDefaults(defaults.toArray());
  }


//OV  public void uninitialize(LookAndFeelAddons addon) {
  public void uninitialize(Addons addon) {
      List defaults = new ArrayList();
      defaults.addAll(Arrays.asList(new Object[] { 
              ColumnHeaderRenderer.UP_ICON_KEY, null,
              ColumnHeaderRenderer.DOWN_ICON_KEY,  null,
      }));
      addon.loadDefaults(defaults.toArray());
  }

//OV  private boolean isXP(LookAndFeelAddons addon) {
  private boolean isXP(Addons addon) {  
      return OS.isWindowsXP();
  }

}
