/*
 * $Id: FastFileChooserDemo.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.fastfilechooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Demo class for FastFileChooserDemo.
 *
 * @author weebib
 */
public class FastFileChooserDemo {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {

				}
				JFrame frame = new JFrame("FastFileChooser");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new FastFileChooser(new File(System.getProperty("user.dir"))));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
