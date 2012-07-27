/*
 * $Id: LocationPolicyTest.java 2728 2008-10-07 16:00:19Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.util;

import java.awt.Dimension;
import java.awt.Point;
import static javax.swing.SwingConstants.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * LocationPolicy Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>03/25/2007</pre>
 */
public class LocationPolicyTest extends TestCase {

    public LocationPolicyTest(String name) {
        super(name);
    }





    @Override
    public void setUp() throws Exception {
        super.setUp();
    }





    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }





    public void testConstructor_compass() throws Exception {
        new LocationPolicy(NORTH);
        new LocationPolicy(NORTH_EAST);
        new LocationPolicy(NORTH_WEST);
        new LocationPolicy(SOUTH);
        new LocationPolicy(SOUTH_EAST);
        new LocationPolicy(SOUTH_WEST);
        new LocationPolicy(EAST);
        new LocationPolicy(WEST);
        new LocationPolicy(CENTER);
    }





    public void testConstructor() throws Exception {
        new LocationPolicy(0, 0);
        new LocationPolicy(0, 1);
        new LocationPolicy(1, 0);
        new LocationPolicy(1, 1);
        new LocationPolicy(0.5f, 0.5f);
    }





    public void testLocate_NORTH() throws Exception {
        testLocate(LocationPolicy.valueOf(NORTH), new Point(25, 0));
    }





    public void testLocate_NORTH_EAST() throws Exception {
        testLocate(LocationPolicy.valueOf(NORTH_EAST), new Point(50, 0));
    }





    public void testLocate_NORTH_WEST() throws Exception {
        testLocate(LocationPolicy.valueOf(NORTH_WEST), new Point(0, 0));
    }





    public void testLocate_SOUTH() throws Exception {
        testLocate(LocationPolicy.valueOf(SOUTH), new Point(25, 50));
    }





    public void testLocate_SOUTH_EAST() throws Exception {
        testLocate(LocationPolicy.valueOf(SOUTH_EAST), new Point(50, 50));
    }





    public void testLocate_SOUTH_WEST() throws Exception {
        testLocate(LocationPolicy.valueOf(SOUTH_WEST), new Point(0, 50));
    }





    public void testLocate_EAST() throws Exception {
        testLocate(LocationPolicy.valueOf(EAST), new Point(50, 25));
    }





    public void testLocate_WEST() throws Exception {
        testLocate(LocationPolicy.valueOf(WEST), new Point(0, 25));
    }





    public void testLocate_CENTER() throws Exception {
        testLocate(LocationPolicy.valueOf(CENTER), new Point(25, 25));
    }





    private void testLocate(LocationPolicy policy, Point expected) {
        testLocate(policy, new Dimension(50, 50), new Dimension(100, 100), expected);
    }





    private void testLocate(LocationPolicy policy, Dimension source, Dimension target, Point expected) {
        assert policy != null;
        assert source != null;
        assert target != null;
        assert expected != null;

        Point result = policy.locate(source, target, null);
        Assert.assertEquals("locate result", expected, result);
    }





    public void testConstructor_compass_error() throws Exception {
        try {
            new LocationPolicy(-1);
            Assert.fail("No IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
        try {
            new LocationPolicy(10);
            Assert.fail("No IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
        }
    }





    public static Test suite() {
        return new TestSuite(LocationPolicyTest.class);
    }
}
