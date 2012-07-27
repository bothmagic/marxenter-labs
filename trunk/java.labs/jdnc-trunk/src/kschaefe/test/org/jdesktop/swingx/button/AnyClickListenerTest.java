/*
 * $Id: AnyClickListenerTest.java 2392 2008-04-10 02:38:02Z kschaefe $
 * 
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
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
package org.jdesktop.swingx.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * 
 */
@SuppressWarnings("nls")
public class AnyClickListenerTest extends JFrame {
    /**
     * {@inheritDoc}
     */
    protected void frameInit() {
        super.frameInit();

        setTitle("Any Click Test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton button = new JButton("Test");
        AnyClickListener.installListener(button, InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK
                | InputEvent.BUTTON3_MASK);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println(e);
            }
        });
        add(button);

        pack();
    }

    /**
     * Application entry point.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AnyClickListenerTest().setVisible(true);
            }
        });
    }

}
