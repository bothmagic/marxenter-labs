/*
 * $Id: JXPopupMenuDemo.java 2673 2008-08-28 17:55:25Z kschaefe $
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
package org.jdesktop.swingx.jxpopupmenu;

import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.action.AbstractActionExt;

/**
 * A demo program to show how to use the {@code JXPopupMenu}.
 * <p>
 * Copied from src/kschaefe/menu to work with rasto's menu.
 * 
 * @author Karl Schaefer
 */
public class JXPopupMenuDemo extends JXFrame {
    private class SetLabelTextAction extends AbstractActionExt {
        public SetLabelTextAction(String name) {
            super(name);
        }
        
        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            label.setText(getName() + " " + isSelected());
        }
    }
    
    private JLabel label;
    
    /**
     * {@inheritDoc}
     */
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JToolBar tb = new JToolBar();
        tb.add(new SetLabelTextAction("ToolBar"));
        
        JXPopupMenu popup = new JXPopupMenu("");
        popup.setMaximumMenuItems(5);
        popup.add(new SetLabelTextAction("Std Action"));
        
        SetLabelTextAction sea = new SetLabelTextAction("CheckBox 1");
        sea.setStateAction();
        popup.add(sea);
        
        sea = new SetLabelTextAction("CheckBox 2");
        sea.setStateAction();
        sea.setSelected(true);
        popup.add(sea);
        
        JMenu submenu = new JXMenu("Submenu");
        submenu.add(new SetLabelTextAction("Aaa"));
        submenu.add(new SetLabelTextAction("Bbb"));
        submenu.add(new SetLabelTextAction("Ccc"));
        submenu.add(new SetLabelTextAction("Ddd"));
        submenu.add(new SetLabelTextAction("Eee"));
        submenu.add(new SetLabelTextAction("Fff"));
        submenu.add(new SetLabelTextAction("Ggg"));
        popup.add(submenu);
        
//        popup.addSeparator();
        
        sea = new SetLabelTextAction("Radio 1");
        sea.setStateAction();
        sea.setGroup("group1");
        popup.add(sea);
        
        sea = new SetLabelTextAction("Radio 2");
        sea.setStateAction();
        sea.setGroup("group1");
        sea.setSelected(true);
        popup.add(sea);
        
//        popup.addSeparator();
        
        sea = new SetLabelTextAction("Group 2 - 1");
        sea.setStateAction();
        sea.setGroup("group2");
        sea.setSelected(true);
        popup.add(sea);
        
        sea = new SetLabelTextAction("Group 2 - 2");
        sea.setStateAction();
        sea.setGroup("group2");
        popup.add(sea);
        
        tb.setComponentPopupMenu(popup);
        
        setToolBar(tb);
        
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label);
    }
    
    /**
     * @param args unused
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JXPopupMenuDemo test = new JXPopupMenuDemo();
                test.setSize(300, 300);
                
                test.setVisible(true);
            }
        });
    }
}
