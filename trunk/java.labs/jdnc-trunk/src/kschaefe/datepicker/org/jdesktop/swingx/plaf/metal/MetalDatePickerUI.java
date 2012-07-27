/*
 * $Id: MetalDatePickerUI.java 1773 2007-09-27 20:47:20Z kschaefe $
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
package org.jdesktop.swingx.plaf.metal;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

/**
 * The basic implementation of a <code>DatePickerUI</code>.
 * <p>
 * 
 * 
 * @author Joshua Outwater
 * @author Jeanette Winzenburg
 * @author Karl Schaefer
 */
public class MetalDatePickerUI extends BasicDatePickerUI {
    static boolean usingOcean() {
        return MetalLookAndFeel.getCurrentTheme() instanceof OceanTheme;
    }
    
    public static ComponentUI createUI(JComponent c) {
        return new MetalDatePickerUI();
    }
    
    public void paint(Graphics g, JComponent c) {
        if (usingOcean()) {
            super.paint(g, c);
        }
    }

    protected JButton createArrowButton() {
        boolean iconOnly = (datePicker.isEditable() || usingOcean());
        JButton button = new MetalDatePickerButton(datePicker,
                                                  new MetalComboBoxIcon(),
                                                  iconOnly,
                                                  currentValuePane,
                                                  monthBox );
        button.setMargin( new Insets( 0, 1, 1, 3 ) );
        if (usingOcean()) {
            // Disabled rollover effect.
            button.putClientProperty(new StringBuffer("NoButtonRollover"),
                                     Boolean.TRUE);
        }
        updateButtonForOcean(button);
        return button;
    }
    
    private void updateButtonForOcean(JButton button) {
        if (usingOcean()) {
            // Ocean renders the focus in a different way, this
            // would be redundant.
            button.setFocusPainted(datePicker.isEditable());
        }
    }
}
