/*
 * $Id: JXDatePickerTest.java 1776 2007-09-28 12:40:35Z osbald $
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
package org.jdesktop.swingx;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Karl George Schaefer
 *
 */
@SuppressWarnings("serial")
public class JXDatePickerTest extends JFrame {
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JXDatePicker picker = new JXDatePicker();
        picker.setEditable(true);
        add(picker);
        
        pack();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXDatePickerTest().setVisible(true);
            }
        });
    }
}
