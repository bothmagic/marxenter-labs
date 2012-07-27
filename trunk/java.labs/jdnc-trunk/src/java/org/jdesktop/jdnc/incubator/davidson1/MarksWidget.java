/*
 * $Id: MarksWidget.java 3 2004-08-31 19:21:31Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1;

import javax.swing.*;

/**
 * This is an example of a custom component that could be included into the JDNC
 * project.
 * 
 * @author Mark Davidson
 */
public class MarksWidget extends JPanel {
    
    private String prop;
    private String ppprop;

    public MarksWidget() {
	add(new JButton("Press Me"));
	prop = "public";
	ppprop = "not public";
    }

    /**
     * Set a public property
     *
     * @param prop the value of the property to set.
     */
    public void setProperty(String prop) {
	this.prop = prop;
    }

    public String getProperty() {
	return prop;
    }

    /**
     * Set a package private property. This javadoc *will not* be generated.
     */
    void setPackagePrivateProperty(String pp) {
	this.ppprop = pp;
    }

    String getPackagePrivateProperty() {
	return ppprop;
    }

    /**
     * This is the main entry point to the class and is optional
     */
    public static void main(String[] args) {
	System.out.println("Invoked main method of MarksWidget.... ");
    }
}
