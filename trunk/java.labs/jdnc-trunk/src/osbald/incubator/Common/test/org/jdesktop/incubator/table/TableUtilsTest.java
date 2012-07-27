package org.jdesktop.incubator.table;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 23-Jan-2009
 * Time: 13:13:38
 */

import junit.framework.TestCase;

import java.util.List;

public class TableUtilsTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
    }

    public void testCollateSequences() throws Exception {
        List<int[]> ranges = (List<int[]>) TableUtils.collateSequences(1, 2, 3, 5, 7, 8, 9);
        assertEquals(3, ranges.size());
        assertTrue(arrayEquals(new int[]{1, 3}, ranges.get(2)));
        assertTrue(arrayEquals(new int[]{5}, ranges.get(1)));
        assertTrue(arrayEquals(new int[]{7, 9}, ranges.get(0)));

        ranges = (List<int[]>) TableUtils.collateSequences(3);
        assertEquals(1, ranges.size());
        assertTrue(arrayEquals(new int[]{3}, ranges.get(0)));

        ranges = (List<int[]>) TableUtils.collateSequences(100, 101, 102, 103, 104, 105, 106, 107);
        assertEquals(1, ranges.size());
        assertTrue(arrayEquals(new int[]{100, 107}, ranges.get(0)));

        ranges = (List<int[]>) TableUtils.collateSequences(1, 3, 5, 7, 9);
        assertEquals(5, ranges.size());
        assertTrue(arrayEquals(new int[]{1}, ranges.get(4)));
        assertTrue(arrayEquals(new int[]{3}, ranges.get(3)));
        assertTrue(arrayEquals(new int[]{5}, ranges.get(2)));
        assertTrue(arrayEquals(new int[]{7}, ranges.get(1)));
        assertTrue(arrayEquals(new int[]{9}, ranges.get(0)));

        ranges = (List<int[]>) TableUtils.collateSequences(new int[0]);
        assertEquals(0, ranges.size());
    }

    boolean arrayEquals(int[] a, int[] b) {
        for (int i = a.length - 1; i >= 0; i--) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
}