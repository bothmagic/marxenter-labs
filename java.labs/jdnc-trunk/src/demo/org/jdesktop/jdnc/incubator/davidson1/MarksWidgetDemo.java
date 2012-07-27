/*
 * $Id: MarksWidgetDemo.java 3 2004-08-31 19:21:31Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1;

import javax.swing.JFrame;

/**
 * The demo class should illustrate the feature of the submitted subsystem.
 * 
 * @author Mark Davidson
 */
public class MarksWidgetDemo {

    public static void main(String[] args) {
	JFrame frame = new JFrame("Mark's Widget Demo");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(new MarksWidget());
	frame.pack();
	frame.setVisible(true);
    }
}
