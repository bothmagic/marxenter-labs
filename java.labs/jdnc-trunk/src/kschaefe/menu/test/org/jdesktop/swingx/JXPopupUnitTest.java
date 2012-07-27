/*
 * $Id: JXPopupUnitTest.java 1862 2007-11-01 17:18:11Z kschaefe $
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import junit.framework.TestCase;

import org.jdesktop.swingx.action.AbstractActionExt;

/**
 *
 */
public class JXPopupUnitTest extends TestCase {
    private static class ActionExt extends AbstractActionExt {
        public void actionPerformed(ActionEvent e) {
            //does nothing
        }
    }
    
    private JXPopupMenu menu;
    
    protected void setUp() {
        menu = new JXPopupMenu();
    }
    
    public void testStandardActions() {
        Action a = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                //does nothing
            }
        };
        
        JMenuItem mi = menu.add(a);
        assertTrue(mi instanceof JMenuItem);
        assertFalse(mi instanceof JRadioButtonMenuItem);
        assertFalse(mi instanceof JCheckBoxMenuItem);
        
        mi = menu.add(new ActionExt());
        assertTrue(mi instanceof JMenuItem);
        assertFalse(mi instanceof JRadioButtonMenuItem);
        assertFalse(mi instanceof JCheckBoxMenuItem);
    }
    
    public void testCheckBoxActions() {
        ActionExt a = new ActionExt();
        a.setStateAction();
        
        JMenuItem mi = menu.add(a);
        assertTrue(mi instanceof JMenuItem);
        assertFalse(mi instanceof JRadioButtonMenuItem);
        assertTrue(mi instanceof JCheckBoxMenuItem);
        assertFalse(mi.isSelected());
        
        a.setSelected(true);
        
        mi = menu.add(a);
        assertTrue(mi instanceof JMenuItem);
        assertFalse(mi instanceof JRadioButtonMenuItem);
        assertTrue(mi instanceof JCheckBoxMenuItem);
        assertTrue(mi.isSelected());
    }
    
    public void testRadioButtonActions() {
        ActionExt a = new ActionExt();
        a.setStateAction();
        a.setGroup("group1");
        
        JMenuItem mi = menu.add(a);
        assertTrue(mi instanceof JMenuItem);
        assertTrue(mi instanceof JRadioButtonMenuItem);
        assertFalse(mi instanceof JCheckBoxMenuItem);
        assertFalse(mi.isSelected());
        
        a.setSelected(true);
        
        JMenuItem mi2 = menu.add(a);
        assertTrue(mi2 instanceof JMenuItem);
        assertTrue(mi2 instanceof JRadioButtonMenuItem);
        assertFalse(mi2 instanceof JCheckBoxMenuItem);
        assertTrue(mi2.isSelected());
        
        mi.doClick();
        assertTrue(mi.isSelected());
        assertFalse(mi2.isSelected());
        
        a = new ActionExt();
        a.setStateAction();
        a.setGroup("group2");
        a.setSelected(false);
        
        JMenuItem mi3 = menu.add(a);
        assertTrue(mi3 instanceof JMenuItem);
        assertTrue(mi3 instanceof JRadioButtonMenuItem);
        assertFalse(mi3 instanceof JCheckBoxMenuItem);
        assertFalse(mi3.isSelected());
        
        a.setSelected(true);
        
        JMenuItem mi4 = menu.add(a);
        assertTrue(mi4 instanceof JMenuItem);
        assertTrue(mi4 instanceof JRadioButtonMenuItem);
        assertFalse(mi4 instanceof JCheckBoxMenuItem);
        assertTrue(mi4.isSelected());
        
        mi3.doClick();
        assertTrue(mi3.isSelected());
        assertFalse(mi4.isSelected());

        //ensure that this group doesn't effect the other
        assertTrue(mi.isSelected());
        assertFalse(mi2.isSelected());
    }
}
