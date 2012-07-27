/*
 * $Id: FastFileChooserDemoJW.java 3203 2009-08-11 08:09:35Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package weebib.fastfilechooser;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Demo class for FastFileChooserDemo.
 * 
 * @author weebib
 */
public class FastFileChooserDemoJW {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {

                }
                JFrame frame = new JFrame("FastFileChooser - use Filters");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new FastFileChooserWithTableJW(new File(System
                        .getProperty("user.dir"))));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
