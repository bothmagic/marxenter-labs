/*
 * $Id: MarksWidgetTest.java 3 2004-08-31 19:21:31Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1;

import junit.framework.TestCase;

/**
 * A simple JUnit test case which tests the functionality of the MarksWidget
 * class.
 * 
 * @author Mark Davidson
 */
public class MarksWidgetTest extends TestCase {

    private MarksWidget widget;
    
    protected void setUp() {
	widget = new MarksWidget();
    }

    protected void tearDown() {
	widget = null;
    }

    public void testProperty() {
        assertEquals("public", widget.getProperty());
    }

    /**
     * Test for package private methods.
     */
    public void testPPProperty() {
	assertEquals("not public", widget.getPackagePrivateProperty());
    }

}
