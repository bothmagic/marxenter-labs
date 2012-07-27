/*
 * $Id: AbstractComponentAddon.java 1050 2007-01-26 11:11:44Z ovisvana $
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
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.UIManager;

import org.jdesktop.swingx.plaf.aqua.AquaLookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jdesktop.swingx.plaf.motif.MotifLookAndFeelAddons;
import com.exalto.org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;
import com.exalto.org.jdesktop.swingx.plaf.ComponentAddon;
import com.exalto.org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * Ease the work of creating an addon for a component.<br>
 * 
 * @author <a href="mailto:fred@L2FProd.com">Frederic Lavigne</a>
 */
public abstract class AbstractComponentAddon implements ComponentAddon {

  private String name;
  
  protected AbstractComponentAddon(String name) {
    this.name = name;
  }
  
  public final String getName() {
    return name;
  }

//OV  public void initialize(LookAndFeelAddons addon) {
  public void initialize(Addons addon) {
    addon.loadDefaults(getDefaults(addon));
  }

//OV  public void uninitialize(LookAndFeelAddons addon) {
  public void uninitialize(Addons addon) {
    addon.unloadDefaults(getDefaults(addon));
  }
  
  /**
   * Adds default key/value pairs to the given list.
   * 
   * @param addon
   * @param defaults
   */
  //OV protected void addBasicDefaults(LookAndFeelAddons addon, List defaults) {
  protected void addBasicDefaults(Addons addon, List defaults) {
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
  //OV protected void addMacDefaults(LookAndFeelAddons addon, List defaults) {
  protected void addMacDefaults(Addons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
//OV  protected void addMetalDefaults(LookAndFeelAddons addon, List defaults) {
  protected void addMetalDefaults(Addons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }
  
  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
//OV  protected void addMotifDefaults(LookAndFeelAddons addon, List defaults) {
protected void addMotifDefaults(Addons addon, List defaults) {  
    addBasicDefaults(addon, defaults);
  }

  /**
   * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, List)}
   * 
   * @param addon
   * @param defaults
   */
//OV  protected void addWindowsDefaults(LookAndFeelAddons addon, List defaults) {
protected void addWindowsDefaults(Addons addon, List defaults) {
    addBasicDefaults(addon, defaults);
  }
    
  /**
   * Gets the defaults for the given addon.
   * 
   * Based on the addon, it calls
   * {@link #addMacDefaults(LookAndFeelAddons, List)} if isMac()
   * or
   * {@link #addMetalDefaults(LookAndFeelAddons, List)} if isMetal()
   * or
   * {@link #addMotifDefaults(LookAndFeelAddons, List)} if isMotif()
   * or
   * {@link #addWindowsDefaults(LookAndFeelAddons, List)} if isWindows()
   * or
   * {@link #addBasicDefaults(LookAndFeelAddons, List)} if none of the above was called.
   * @param addon
   * @return an array of key/value pairs. For example:
   * <pre>
   * Object[] uiDefaults = {
   *   "Font", new Font("Dialog", Font.BOLD, 12),
   *   "Color", Color.red,
   *   "five", new Integer(5)
   * };
   * </pre>
   */
  //OV private Object[] getDefaults(LookAndFeelAddons addon) {
  private Object[] getDefaults(Addons addon) {
  	
    List defaults = new ArrayList();
    if (isWindows(addon)) {
      addWindowsDefaults(addon, defaults);
    } else if (isMetal(addon)) {
      addMetalDefaults(addon, defaults);
    } else if (isMac(addon)) {
      addMacDefaults(addon, defaults);
    } else if (isMotif(addon)) {
      addMotifDefaults(addon, defaults);
    } else {
      // at least add basic defaults
      addBasicDefaults(addon, defaults);
    }
    return defaults.toArray();
  }

  //
  // Helper methods to make ComponentAddon developer life easier
  //

  /**
   * Adds the all keys/values from the given named resource bundle to the
   * defaults
   */
  protected void addResource(List defaults, String bundleName) {
    ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
    
    for (Enumeration keys = bundle.getKeys(); keys.hasMoreElements(); ) {
      String key = (String) keys.nextElement();      
      defaults.add(key);
      defaults.add(bundle.getObject(key));
    }
  }
  
  /**
   * @return true if the addon is the Windows addon or its subclasses
   */
  protected boolean isWindows(Addons addon) {
    return addon instanceof WindowsLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Metal addon or its subclasses
   */
  protected boolean isMetal(Addons addon) {
    return addon instanceof MetalLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Aqua addon or its subclasses
   */
  protected boolean isMac(Addons addon) {
    return addon instanceof AquaLookAndFeelAddons;
  }
  
  /**
   * @return true if the addon is the Motif addon or its subclasses
   */
  protected boolean isMotif(Addons addon) {
    return addon instanceof MotifLookAndFeelAddons;
  }

  /**
   * @return true if the current look and feel is one of JGoodies Plastic l&fs
   */
  protected boolean isPlastic() {
    return (UIManager.getLookAndFeel().getClass().getName().indexOf("Plastic") > 0);
  }

  /**
   * @return true if the current look and feel is Synth l&f
   */
  protected boolean isSynth() {
    return UIManager.getLookAndFeel().getClass().getName().indexOf("Synth") > 0;    
  }
  
}
