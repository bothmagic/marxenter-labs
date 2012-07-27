/*
 * $Id: Main.java 133 2004-10-19 00:48:00Z gonzo $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
 
package org.jdesktop.jdnc.incubator.gonzo.editor;

import org.jdesktop.swing.JXFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Main {
    
    private static final String TITLE = "MyEditor";

    public static void main(String[] args) {
        new Main();
    }
    
    public Main() {
        JXFrame f = new JXFrame(TITLE);
        final Editor e = new Editor();
        
        f.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent we) {
                e.setFocus();
            }
        });
        
        f.getContentPane().add(e);
        f.pack();
        f.setVisible(true);
    }
}
