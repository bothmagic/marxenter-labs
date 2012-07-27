/*
 * $Id: MetalDatePickerButton.java 2043 2007-12-17 00:58:28Z kschaefe $
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.CellRendererPane;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;

/**
 * @author Karl George Schaefer
 *
 */
public class MetalDatePickerButton extends JButton {
    protected JXDatePicker datePicker;
    protected JXMonthView monthView;
    protected CellRendererPane rendererPane;
    protected Icon comboIcon;
    protected boolean iconOnly = false;

    public final JXDatePicker getComboBox() { return datePicker;}
    public final void setComboBox( JXDatePicker dp ) { datePicker = dp;}

    public final Icon getComboIcon() { return comboIcon;}
    public final void setComboIcon( Icon i ) { comboIcon = i;}

    public final boolean isIconOnly() { return iconOnly;}
    public final void setIconOnly( boolean isIconOnly ) { iconOnly = isIconOnly;}

    MetalDatePickerButton() {
        super( "" );
        DefaultButtonModel model = new DefaultButtonModel() {
            public void setArmed( boolean armed ) {
                super.setArmed( isPressed() ? true : armed );
            }
        };
        setModel( model );
    }

    public MetalDatePickerButton( JXDatePicker dp, Icon i, 
                                CellRendererPane pane, JXMonthView list ) {
        this();
        datePicker = dp;
        comboIcon = i;
        rendererPane = pane;
        monthView = list;
        setEnabled( datePicker.isEnabled() );
    }

    public MetalDatePickerButton( JXDatePicker dp, Icon i, boolean onlyIcon,
                                CellRendererPane pane, JXMonthView list ) {
        this( dp, i, pane, list );
        iconOnly = onlyIcon;
    }

    public boolean isFocusTraversable() {
        return false;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        // Set the background and foreground to the combobox colors.
        if (enabled) {
            setBackground(datePicker.getBackground());
            setForeground(datePicker.getForeground());
        } else {
            setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            setForeground(UIManager.getColor("ComboBox.disabledForeground"));
        }           
    }

    public void paintComponent( Graphics g ) {
        boolean leftToRight = datePicker.getComponentOrientation().isLeftToRight();

        // Paint the button as usual
        super.paintComponent( g );

        Insets insets = getInsets();

        int width = getWidth() - (insets.left + insets.right);
        int height = getHeight() - (insets.top + insets.bottom);

        if ( height <= 0 || width <= 0 ) {
            return;
        }

        int left = insets.left;
        int top = insets.top;
        int right = left + (width - 1);
        int bottom = top + (height - 1);

        int iconWidth = 0;
        int iconLeft = (leftToRight) ? right : left;

        // Paint the icon
        if ( comboIcon != null ) {
            iconWidth = comboIcon.getIconWidth();
            int iconHeight = comboIcon.getIconHeight();
            int iconTop = 0;

            if ( iconOnly ) {
                iconLeft = (getWidth() / 2) - (iconWidth / 2);
                iconTop = (getHeight() / 2) - (iconHeight / 2);
            }
            else {
                if (leftToRight) {
                    iconLeft = (left + (width - 1)) - iconWidth;
                }
                else {
                    iconLeft = left;
                }
                iconTop = (top + ((bottom - top) / 2)) - (iconHeight / 2);
            }

            comboIcon.paintIcon( this, g, iconLeft, iconTop );

            // Paint the focus
            if ( datePicker.hasFocus() && (!MetalDatePickerUI.usingOcean() ||
                                         datePicker.isEditable())) {
                g.setColor( MetalLookAndFeel.getFocusColor() );
                g.drawRect( left - 1, top - 1, width + 3, height + 1 );
            }
        }

        if (MetalDatePickerUI.usingOcean()) {
            // With Ocean the button only paints the arrow, bail.
            return;
        }

        // Let the renderer paint
        if ( ! iconOnly && datePicker != null ) {
//            ListCellRenderer renderer = datePicker.getRenderer();
//            Component c;
//            boolean renderPressed = getModel().isPressed();
//            c = renderer.getListCellRendererComponent(listBox,
//                                                      datePicker.getSelectedItem(),
//                                                      -1,
//                                                      renderPressed,
//                                                      false);
//            c.setFont(rendererPane.getFont());
//
//            if ( model.isArmed() && model.isPressed() ) {
//                if ( isOpaque() ) {
//                    c.setBackground(UIManager.getColor("Button.select"));
//                }
//                c.setForeground(datePicker.getForeground());
//            }
//            else if ( !datePicker.isEnabled() ) {
//                if ( isOpaque() ) {
//                    c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
//                }
//                c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
//            }
//            else {
//                c.setForeground(datePicker.getForeground());
//                c.setBackground(datePicker.getBackground());
//            }
//
//
//            int cWidth = width - (insets.right + iconWidth);
//            
//            // Fix for 4238829: should lay out the JPanel.
//            boolean shouldValidate = false;
//            if (c instanceof JPanel)  {
//                shouldValidate = true;
//            }
//            
//            if (leftToRight) {
//                rendererPane.paintComponent( g, c, this, 
//                                             left, top, cWidth, height, shouldValidate );
//            }
//            else {
//                rendererPane.paintComponent( g, c, this, 
//                                             left + iconWidth, top, cWidth, height, shouldValidate );
//            }
        }
    }
    
    public Dimension getMinimumSize() {
        Dimension ret = new Dimension();
        Insets insets = getInsets();
        ret.width = insets.left + getComboIcon().getIconWidth() + insets.right;
        ret.height = insets.bottom + getComboIcon().getIconHeight() + insets.top;
        return ret;        
    }
}
