package org.jdesktop.incubator.util;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 26-Jan-2009
 * Time: 11:25:26
 */

import junit.framework.TestCase;

public class DynamicIntArrayTest extends TestCase {

    public void testBasicUsage() throws Exception {
        DynamicIntArray array = new DynamicIntArray();
        array.add(1);
        array.add(2);
        array.add(3);
        int[] result;
        assertTrue(arrayEquals(new int[]{1, 2, 3}, result = array.toArray()));
        array = new DynamicIntArray();
        array.add(1, 2).add(3);
        assertTrue(arrayEquals(result, array.toArray()));
        assertTrue(arrayEquals(array.slice(0, 2), array.toArray()));
    }

    public void testMaximumHint() throws Exception {
        DynamicIntArray array = new DynamicIntArray(5, 100);
        for (int i = 0; i < array.getMaximumLength(); i++) {
            array.add(i + 1);
        }
        assertEquals(100, array.size());
        assertTrue(arrayEquals(array.slice(0, 99), array.toArray()));
        assertEquals(100, array.toArray()[99]);

        array.add(1, 2, 3, 4, 5);
        assertEquals(105, array.size());
        assertEquals(105, array.toArray().length);
    }

    boolean arrayEquals(int[] a, int[] b) {
        for (int i = a.length - 1; i >= 0; i--) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
}