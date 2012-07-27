/*
 * $Id: JXSlideoutBarDemo.java 2355 2008-03-27 03:10:59Z kschaefe $
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

import java.awt.BorderLayout;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

/**
 *
 */
public class JXSlideoutBarDemo extends JXFrame {
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        add(new JTextPane(), BorderLayout.CENTER);
        
        JXSlideoutBar bar = new JXSlideoutBar();
        bar.add(new JXFindPanel(), "Find");
        bar.add(new JXLabel("Some demo text."), "Demo");
        bar.add(new JXLabel("<html>Even <font color=red>more</font> demo text.</html>"), "Etc.");
        add(bar, BorderLayout.WEST);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        final JXSlideoutBarDemo test = new JXSlideoutBarDemo();
        
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                test.setSize(600, 600);
                test.setVisible(true);
            }
            
        });
    }

}
