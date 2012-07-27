/*
 * $Id: ScalePolicyTest.java 2728 2008-10-07 16:00:19Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.util;

import java.awt.Dimension;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy.BOTH;
import static org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy.FIXED_RATIO;
import static org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy.HORIZONTAL;
import static org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy.VERTICAL;
import static org.jdesktop.swingx.util.ScalePolicy.ResizePolicy.BEST_FIT;
import static org.jdesktop.swingx.util.ScalePolicy.ResizePolicy.GROW;
import static org.jdesktop.swingx.util.ScalePolicy.ResizePolicy.NONE;
import static org.jdesktop.swingx.util.ScalePolicy.ResizePolicy.SHRINK;

/**
 * ScalePolicy Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>03/25/2007</pre>
 */
public class ScalePolicyTest extends TestCase {

    public ScalePolicyTest(String name) {
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





    public void testConstructor() {
        try {
            new ScalePolicy(null, BEST_FIT);
            Assert.fail("No NullPointerException");
        } catch (NullPointerException ex) {
        }
        try {
            new ScalePolicy(BOTH, null);
            Assert.fail("No NullPointerException");
        } catch (NullPointerException ex) {
        }
        try {
            new ScalePolicy(null, null);
            Assert.fail("No NullPointerException");
        } catch (NullPointerException ex) {
        }
    }





    public void testValueOf() {
        testValueOfValues(HORIZONTAL, SHRINK);
        testValueOfValues(HORIZONTAL, GROW);
        testValueOfValues(HORIZONTAL, BEST_FIT);
        testValueOfValues(VERTICAL, SHRINK);
        testValueOfValues(VERTICAL, GROW);
        testValueOfValues(VERTICAL, BEST_FIT);
        testValueOfValues(BOTH, SHRINK);
        testValueOfValues(BOTH, GROW);
        testValueOfValues(BOTH, BEST_FIT);
        testValueOfValues(FIXED_RATIO, SHRINK);
        testValueOfValues(FIXED_RATIO, GROW);
        testValueOfValues(FIXED_RATIO, BEST_FIT);
        testValueOfNone(HORIZONTAL);
        testValueOfNone(VERTICAL);
        testValueOfNone(BOTH);
        testValueOfNone(FIXED_RATIO);

    }





    private void testValueOfNone(ScalePolicy.DimensionPolicy dp) {
        ScalePolicy sp = ScalePolicy.valueOf(dp, NONE);
        Assert.assertNotNull(sp);
        assertEquals(FIXED_RATIO, sp.getDimensionPolicy());
        assertEquals(NONE, sp.getResizePolicy());
        assertEquals(sp, ScalePolicy.valueOf(dp, NONE));
    }





    private void testValueOfValues(ScalePolicy.DimensionPolicy dp, ScalePolicy.ResizePolicy rp) {
        ScalePolicy sp = ScalePolicy.valueOf(dp, rp);
        Assert.assertNotNull(sp);
        assertEquals(dp, sp.getDimensionPolicy());
        assertEquals(rp, sp.getResizePolicy());
        assertEquals(sp, ScalePolicy.valueOf(dp, rp));
    }





    public void testFitInto_sameSize() {
        Dimension s = new Dimension(100, 100);
        Dimension t = new Dimension(100, 100);
        Dimension e = new Dimension(100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, SHRINK), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, GROW), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, BEST_FIT), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, SHRINK), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, GROW), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, BEST_FIT), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, SHRINK), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, GROW), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, BEST_FIT), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, SHRINK), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, GROW), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, BEST_FIT), s, t, e);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, NONE), s, t, e);
    }





    public void testFitInto_smaller() {
        Dimension s = new Dimension(50, 50);
        Dimension t = new Dimension(100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, GROW), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, BEST_FIT), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, GROW), s, t, 50, 100);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, BEST_FIT), s, t, 50, 100);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, BEST_FIT), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, BEST_FIT), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, NONE), s, t, 50, 50);
    }





    public void testFitInto_larger() {
        Dimension s = new Dimension(100, 100);
        Dimension t = new Dimension(50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, SHRINK), s, t, 50, 100);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, BEST_FIT), s, t, 50, 100);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, SHRINK), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, BEST_FIT), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, BEST_FIT), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, SHRINK), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, GROW), s, t, 100, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, BEST_FIT), s, t, 50, 50);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, NONE), s, t, 100, 100);
    }





    public void testFitInto_ratio_grow() {
        Dimension s = new Dimension(100, 50);
        Dimension t = new Dimension(200, 200);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, SHRINK), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, GROW), s, t, 200, 50);
        testFitIntoImpl(ScalePolicy.valueOf(HORIZONTAL, BEST_FIT), s, t, 200, 50);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, SHRINK), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, GROW), s, t, 100, 200);
        testFitIntoImpl(ScalePolicy.valueOf(VERTICAL, BEST_FIT), s, t, 100, 200);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, SHRINK), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, GROW), s, t, 200, 200);
        testFitIntoImpl(ScalePolicy.valueOf(BOTH, BEST_FIT), s, t, 200, 200);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, SHRINK), s, t, 100, 50);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, GROW), s, t, 200, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, BEST_FIT), s, t, 200, 100);
        testFitIntoImpl(ScalePolicy.valueOf(FIXED_RATIO, NONE), s, t, 100, 50);
    }





    private void testFitIntoImpl(ScalePolicy sp, Dimension expected) {
        testFitIntoImpl(sp, new Dimension(50, 50), new Dimension(100, 100), expected);
    }





    private void testFitIntoImpl(ScalePolicy sp, Dimension source, Dimension target, int width, int height) {
        testFitIntoImpl(sp, source, target, new Dimension(width, height));
    }





    private void testFitIntoImpl(ScalePolicy sp, Dimension source, Dimension target, Dimension expected) {
        assert sp != null;
        assert source != null;
        assert target != null;
        assert expected != null;

        Dimension sc = (Dimension) source.clone();
        Dimension tc = (Dimension) target.clone();
        Dimension result = sp.fitInto(source, target, null);
        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);
        // check returned instance
        Assert.assertFalse(source == result);
        Assert.assertFalse(target == result);

        // check passed modifiers
        Assert.assertEquals(sc, source);
        Assert.assertEquals(tc, target);

        // with passed result dimension
        Dimension resultc = (Dimension) result.clone();
        Dimension result2 = sp.fitInto(source, target, resultc);
        Assert.assertTrue(resultc == result2);
        Assert.assertEquals(result, result2);
    }





    public static Test suite() {
        return new TestSuite(ScalePolicyTest.class);
    }
}
