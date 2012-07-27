/*
 * $Id: JXSplitButton.java 3193 2009-07-09 16:49:01Z kschaefe $
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
package org.jdesktop.swingx;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.SplitButtonAddon;

/**
 * A push button with two parts, a master and a slave. The slave button creates
 * a popup menu that configures, alters, or performs actions on behalf of the
 * master.
 */
@SuppressWarnings("serial")
public class JXSplitButton extends JButton {
    public static final String uiClassID = "SplitButtonUI";
    
    /**
     * The menu that is presented to the user when the slave button is clicked.
     */
    protected JPopupMenu slaveMenu;
    
    private boolean lazyMaster;
    
    static {
        LookAndFeelAddons.contribute(new SplitButtonAddon());
    }
    
    /**
     * 
     */
    public JXSplitButton() {
        super();
    }

    /**
     * @param a
     */
    public JXSplitButton(Action a) {
        super(a);
    }

    /**
     * @param icon
     */
    public JXSplitButton(Icon icon) {
        super(icon);
    }

    /**
     * @param text
     * @param icon
     */
    public JXSplitButton(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * @param text
     */
    public JXSplitButton(String text) {
        super(text);
    }
    
    public static void main(String args[]) throws Exception {
        JDialog dialog = new JDialog();
//        dialog.setContentPane(new JPanel());
//        dialog.setLayout(new GridLayout(1, 1));
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(Color.GREEN);
        JXSplitButton button = new JXSplitButton("Text");
        dialog.add(button);
        button.setBounds(50, 50, 50 + button.getPreferredSize().width, button.getPreferredSize().height);
//        button.setBackground(Color.PINK);
//        button.setBorder(BorderFactory.createEmptyBorder());
//        System.err.println(button.getBorder() + "");
        dialog.setSize(350, 200);
        dialog.setVisible(true);
        
//        System.err.println(dialog.getContentPane());
    }
    
    /**
     * {@inheritDoc}
     */
    public String getUIClassID() {
        return uiClassID;
    }
//
//    /**
//     * Resets the UI property to a value from the current look and
//     * feel.
//     *
//     * @see JComponent#updateUI()
//     */
//    public void updateUI() {
//        setUI((SplitButtonUI) LookAndFeelAddons
//                .getUI(this, SplitButtonUI.class));
//        invalidate();
//    }
//    
//    /**
//     * {@inheritDoc}
//     */
//    public SplitButtonUI getUI() {
//        return (SplitButtonUI) super.getUI();
//    }
//    
//    /**
//     * Sets the look and feel (L&F) object that renders this component.
//     * 
//     * @param ui
//     *            the {@code SplitButtonUI} L&F object
//     * @see java.swing.UIDefaults#getUI
//     */
//    public void setUI(SplitButtonUI ui) {
//        if (this.ui != ui) {
//            super.setUI(ui);
//            repaint();
//        }
//    }

    /**
     * @return Returns the lazyMaster.
     */
    public boolean isLazyMaster() {
        return lazyMaster;
    }

    /**
     * @param lazyMaster The lazyMaster to set.
     */
    public void setLazyMaster(boolean lazyMaster) {
        boolean oldValue = isLazyMaster();
        this.lazyMaster = lazyMaster;
        firePropertyChange("lazyMaster", oldValue, isLazyMaster());
    }

    /**
     * @return
     */
    public Icon getDropDownIcon() {
        return null;
    }
}
