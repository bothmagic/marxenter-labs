/*
 * $Id: DatePickerAddon.java 2627 2008-08-05 12:51:32Z kschaefe $
 * 
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.plaf;

import javax.swing.UIManager;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

/**
 * The Look and Feel Addon for the JXDatePicker.
 * 
 * @author Joshua Outwater
 * @author Karl Schaefer
 */
public class DatePickerAddon extends AbstractComponentAddon {
    public DatePickerAddon() {
        super("JXDatePicker");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXDatePicker.uiClassID, BasicDatePickerUI.class.getName());
        defaults.add("JXDatePicker.font", UIManager.get("ComboBox.font")); //sansSerifPlain12
        defaults.add("JXDatePicker.background", UIManager.getColor("window"));
        defaults.add("JXDatePicker.foreground", UIManager.getColor("textText"));
        defaults.add("JXDatePicker.buttonBackground", UIManager.getColor("control"));
        defaults.add("JXDatePicker.buttonShadow", UIManager.getColor("controlShadow"));
        defaults.add("JXDatePicker.buttonDarkShadow", UIManager.getColor("controlDkShadow"));
        defaults.add("JXDatePicker.buttonHighlight", UIManager.getColor("controlLtHighlight"));
        defaults.add("JXDatePicker.selectionBackground", UIManager.getColor("textHighlight"));
        defaults.add("JXDatePicker.selectionForeground", UIManager.getColor("textHighlightText"));
        defaults.add("JXDatePicker.disabledBackground", UIManager.getColor("control"));
        defaults.add("JXDatePicker.disabledForeground", UIManager.getColor("textInactiveText"));
        defaults.add("JXDatePicker.isEnterSelectablePopup", Boolean.TRUE);
        defaults.add("JXDatePicker.ancestorInputMap", new Object[] {
                   "ESCAPE", "hidePopup",
                  "PAGE_UP", "pageUpPassThrough",
                "PAGE_DOWN", "pageDownPassThrough",
                     "HOME", "homePassThrough",
                      "END", "endPassThrough",
                    "ENTER", "enterPressed"
            });
//            "JXDatePicker.border", new BorderUIResource(BorderFactory.createCompoundBorder(
//                    LineBorder.createGrayLineBorder(),
//                    BorderFactory.createEmptyBorder(3, 3, 3, 3)))
    }
    
    /**
     * {@inheritDoc}
     */
//    @Override
//    protected void addMetalDefaults(LookAndFeelAddons addon, List<Object> defaults) {
//        super.addMetalDefaults(addon, defaults);
//        
//    }
}
