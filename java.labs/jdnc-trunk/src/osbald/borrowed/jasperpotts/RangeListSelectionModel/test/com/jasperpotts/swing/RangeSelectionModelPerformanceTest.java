package com.jasperpotts.swing;

import junit.framework.TestCase;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * RangeSelectionModelTest
 *
 * @author Created by Jasper Potts (03-Aug-2005)
 */
public class RangeSelectionModelPerformanceTest extends TestCase {

    /**
     * Named Constructor
     *
     * @param name The name of this test
     */
    public RangeSelectionModelPerformanceTest(String name) {
        super(name);
    }

    private int[][] testData = new int[4][20000];

    @Override protected void setUp() throws Exception {
        System.out.println("DataSetSize = " + testData[0].length);
        int[] counts = new int[5];
        for (int i = 0; i < testData[0].length; i++) {
            testData[0][i] = (int)(Math.random()*5);
            testData[1][i] = (int)(Math.random()*1000000);
            testData[2][i] = (int)(Math.random()*1000000);
            testData[3][i] = (Math.random()>0.5)?1:0;

            counts[testData[0][i]] ++;
        }
        System.out.println("isSelectedIndex = "+counts[0]);
        System.out.println("setSelectionInterval = "+counts[1]);
        System.out.println("removeSelectionInterval = "+counts[2]);
        System.out.println("addSelectionInterval = "+counts[3]);
        System.out.println("removeIndexInterval = "+counts[4]);
    }

    public void testRandomOurs() {
        RangeListSelectionModel model = new RangeListSelectionModel();
        model.clearSelection();
        int maxRanges = 0;
        for (int i = 0; i < testData[0].length; i++) {
            int iOperation = testData[0][i];
            int val1 = testData[1][i];
            int val2 = testData[2][i];
            switch (iOperation){
                case 0:
                    model.isSelectedIndex(val1);
                    break;
                case 1:
                    model.setSelectionInterval(val1,val2);
                    break;
                case 2:
                    model.removeSelectionInterval(val1,val2);
                    break;
                case 3:
                    model.addSelectionInterval(val1,val2);
                    break;
                case 4:
                    model.removeIndexInterval(val1,val2);
                    break;
            }
            maxRanges = Math.max(maxRanges,model.getNumberOfRanges());
        }
        System.out.println("rangeListModel.getNumberOfRanges() = " + model.getNumberOfRanges());
        System.out.println("maxRanges = " + maxRanges);
    }

    public void testRandomDefault() {
        ListSelectionModel oModel = new DefaultListSelectionModel();
        oModel.clearSelection();
        for (int i = 0; i < testData[0].length; i++) {
            int iOperation = testData[0][i];
            int val1 = testData[1][i];
            int val2 = testData[2][i];
            switch (iOperation){
                case 0:
                    oModel.isSelectedIndex(val1);
                    break;
                case 1:
                    oModel.setSelectionInterval(val1,val2);
                    break;
                case 2:
                    oModel.removeSelectionInterval(val1,val2);
                    break;
                case 3:
                    oModel.addSelectionInterval(val1,val2);
                    break;
                case 4:
                    oModel.removeIndexInterval(val1,val2);
                    break;
            }
        }
    }

    RangeListSelectionModel rangeListModel = new RangeListSelectionModel();
    DefaultListSelectionModel defaultModel = new DefaultListSelectionModel();

    public void testSetSelectedItemRanges() {
        rangeListModel.clearSelection();
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            rangeListModel.setSelectionInterval(val1,val1);
        }
    }

    public void testSetSelectedItemDefault() {
        defaultModel.clearSelection();
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            defaultModel.setSelectionInterval(val1,val1);
        }
    }

    public void testAddSelectionIntervalRanges() {
        rangeListModel.clearSelection();
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            rangeListModel.addSelectionInterval(val1,val1+5);
        }
        System.out.println("rangeListModel.getNumberOfRanges() = " + rangeListModel.getNumberOfRanges());
    }

    public void testAddSelectionIntervalDefault() {
        defaultModel.clearSelection();
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            defaultModel.addSelectionInterval(val1,val1+5);
        }
    }

    public void testIsSelectedIndexRanges() {
        System.out.println("testing isSelectedIndex() on " + rangeListModel.getNumberOfRanges()+" ranges");
        for (int i = 0; i < testData[1].length; i++) {
            rangeListModel.isSelectedIndex(i);
        }
    }

    /** Sample Test Case */
    public void testIsSelectedIndexDefault() {
        for (int i = 0; i < testData[1].length; i++) {
            defaultModel.isSelectedIndex(i);
        }
    }

    public void testRemoveIndexIntervalRanges() {
        rangeListModel.clearSelection();
        defaultModel.setSelectionInterval(2000000,2000000);
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            rangeListModel.removeIndexInterval(val1,val1);
        }
    }

    public void testRemoveIndexIntervalDefault() {
        defaultModel.clearSelection();
        defaultModel.setSelectionInterval(2000000,2000000);
        for (int i = 0; i < testData[1].length; i++) {
            int val1 = testData[1][i];
            defaultModel.removeIndexInterval(val1,val1);
        }
    }


}
