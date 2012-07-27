package com.jasperpotts.swing;

import junit.framework.TestCase;

import javax.swing.DefaultListSelectionModel;

/**
 * RangeSelectionModelTest
 *
 * @author Created by Jasper Potts (03-Aug-2005)
 */
public class RangeSelectionModelTest extends TestCase {

    private RangeListSelectionModel rangeListSelectionModel = null;
    private DefaultListSelectionModel defaultListSelectionModel = null;

    /**
     * Named Constructor
     *
     * @param name The name of this test
     */
    public RangeSelectionModelTest(String name) {
        super(name);
    }

    /** Sets up the fixture, for example, open a network connection. This method is called before a test is executed. */
    @Override protected void setUp() throws Exception {
        rangeListSelectionModel = new RangeListSelectionModel();
        defaultListSelectionModel = new DefaultListSelectionModel();
    }

    /** Tears down the fixture, for example, close a network connection. This method is called after a test is executed. */
    @Override protected void tearDown() throws Exception {
        rangeListSelectionModel = null;
        defaultListSelectionModel = null;
    }

    public void testSetSelection() {
        clearModels();
        rangeListSelectionModel.setSelectionInterval(10,20);
        defaultListSelectionModel.setSelectionInterval(10,20);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(30,40);
        defaultListSelectionModel.setSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(50,60);
        defaultListSelectionModel.setSelectionInterval(50,60);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(15,25);
        defaultListSelectionModel.setSelectionInterval(15,25);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(15,55);
        defaultListSelectionModel.setSelectionInterval(15,55);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(2,70);
        defaultListSelectionModel.setSelectionInterval(2,70);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(1,1);
        defaultListSelectionModel.setSelectionInterval(1,1);
        compareModels();
        compareModels();
    }

    public void testSetSelectionReversed() {
        clearModels();
        rangeListSelectionModel.setSelectionInterval(20,10);
        defaultListSelectionModel.setSelectionInterval(20,10);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(40,30);
        defaultListSelectionModel.setSelectionInterval(40,30);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(60,50);
        defaultListSelectionModel.setSelectionInterval(60,50);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(25,15);
        defaultListSelectionModel.setSelectionInterval(25,15);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(55,15);
        defaultListSelectionModel.setSelectionInterval(55,15);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(70,2);
        defaultListSelectionModel.setSelectionInterval(70,2);
        compareModels();
    }

    public void testAddSelection() {
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,20);
        defaultListSelectionModel.addSelectionInterval(10,20);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(30,40);
        defaultListSelectionModel.addSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(50,60);
        defaultListSelectionModel.addSelectionInterval(50,60);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(15,25);
        defaultListSelectionModel.addSelectionInterval(15,25);
        compareModels();
        System.out.println("m_oCatalogSelectionModel = " + rangeListSelectionModel);
        rangeListSelectionModel.addSelectionInterval(48,49);
        defaultListSelectionModel.addSelectionInterval(48,49);
        System.out.println("m_oCatalogSelectionModel = " + rangeListSelectionModel);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(15,55);
        defaultListSelectionModel.addSelectionInterval(15,55);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(2,5);
        defaultListSelectionModel.addSelectionInterval(2,5);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(7,8);
        defaultListSelectionModel.addSelectionInterval(7,8);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(1,99);
        defaultListSelectionModel.addSelectionInterval(1,99);
        compareModels();
    }

    public void testAddSelectionReversed() {
        clearModels();
        rangeListSelectionModel.addSelectionInterval(20,10);
        defaultListSelectionModel.addSelectionInterval(20,10);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(40,30);
        defaultListSelectionModel.addSelectionInterval(40,30);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(60,50);
        defaultListSelectionModel.addSelectionInterval(60,50);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(25,15);
        defaultListSelectionModel.addSelectionInterval(25,15);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(55,15);
        defaultListSelectionModel.addSelectionInterval(55,15);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(7,2);
        defaultListSelectionModel.addSelectionInterval(7,2);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(99,1);
        defaultListSelectionModel.addSelectionInterval(99,1);
        compareModels();
    }


    public void testRemoveSelectionInterval() {
        clearModels();
        rangeListSelectionModel.setSelectionInterval(25,75);
        defaultListSelectionModel.setSelectionInterval(25,75);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(23,25);
        defaultListSelectionModel.removeSelectionInterval(23,25);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(10,30);
        defaultListSelectionModel.removeSelectionInterval(10,30);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(70,80);
        defaultListSelectionModel.removeSelectionInterval(70,80);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(40,50);
        defaultListSelectionModel.removeSelectionInterval(40,50);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(10,55);
        defaultListSelectionModel.removeSelectionInterval(10,55);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(10,95);
        defaultListSelectionModel.removeSelectionInterval(10,95);
        compareModels();
        rangeListSelectionModel.setSelectionInterval(25,75);
        defaultListSelectionModel.setSelectionInterval(25,75);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(25,50);
        defaultListSelectionModel.removeSelectionInterval(25,50);
        compareModels();
    }

    public void testDeleteSelectionReversed() {
        clearModels();
        rangeListSelectionModel.setSelectionInterval(75,25);
        defaultListSelectionModel.setSelectionInterval(75,25);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(30,10);
        defaultListSelectionModel.removeSelectionInterval(30,10);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(80,70);
        defaultListSelectionModel.removeSelectionInterval(80,70);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(50,40);
        defaultListSelectionModel.removeSelectionInterval(50,40);
        compareModels();
        rangeListSelectionModel.removeSelectionInterval(55,10);
        defaultListSelectionModel.removeSelectionInterval(55,10);
        compareModels();
    }

    public void testRemoveIndexInterval() {
        // remove a chunk inside selection
        clearModels();
        rangeListSelectionModel.setSelectionInterval(10,40);
        defaultListSelectionModel.setSelectionInterval(10,40);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(20,30);
        defaultListSelectionModel.removeIndexInterval(20,30);
        compareModels();
        // remove a chunk below all selections
        clearModels();
        rangeListSelectionModel.addSelectionInterval(40,50);
        defaultListSelectionModel.addSelectionInterval(40,50);
        rangeListSelectionModel.addSelectionInterval(80,90);
        defaultListSelectionModel.addSelectionInterval(80,90);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(20,30);
        defaultListSelectionModel.removeIndexInterval(20,30);
        compareModels();
        // remove a chunk inside selection with another selection above
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,35);
        defaultListSelectionModel.addSelectionInterval(10,35);
        compareModels();
        rangeListSelectionModel.addSelectionInterval(40,50);
        defaultListSelectionModel.addSelectionInterval(40,50);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(20,30);
        defaultListSelectionModel.removeIndexInterval(20,30);
        compareModels();
        // remove a chunk with several selection in it and one above
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,15);
        defaultListSelectionModel.addSelectionInterval(10,15);
        rangeListSelectionModel.addSelectionInterval(20,25);
        defaultListSelectionModel.addSelectionInterval(20,25);
        rangeListSelectionModel.addSelectionInterval(30,35);
        defaultListSelectionModel.addSelectionInterval(30,35);
        rangeListSelectionModel.addSelectionInterval(80,85);
        defaultListSelectionModel.addSelectionInterval(80,85);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(5,40);
        defaultListSelectionModel.removeIndexInterval(5,40);
        compareModels();
        // remove a chunk above all selections
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,15);
        defaultListSelectionModel.addSelectionInterval(10,15);
        rangeListSelectionModel.addSelectionInterval(20,25);
        defaultListSelectionModel.addSelectionInterval(20,25);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(30,40);
        defaultListSelectionModel.removeIndexInterval(30,40);
        compareModels();
        // remove a chunk that matches exactly one selection
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,15);
        defaultListSelectionModel.addSelectionInterval(10,15);
        rangeListSelectionModel.addSelectionInterval(20,25);
        defaultListSelectionModel.addSelectionInterval(20,25);
        rangeListSelectionModel.addSelectionInterval(30,35);
        defaultListSelectionModel.addSelectionInterval(30,35);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(20,25);
        defaultListSelectionModel.removeIndexInterval(20,25);
        compareModels();
        // remove a chunk between two selections
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,15);
        defaultListSelectionModel.addSelectionInterval(10,15);
        rangeListSelectionModel.addSelectionInterval(50,55);
        defaultListSelectionModel.addSelectionInterval(50,55);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(30,40);
        defaultListSelectionModel.removeIndexInterval(30,40);
        compareModels();
        // remove a chunk that has half of one and whole one and half on another and one abobe ande below
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,15);
        defaultListSelectionModel.addSelectionInterval(10,15);
        rangeListSelectionModel.addSelectionInterval(40,45);
        defaultListSelectionModel.addSelectionInterval(40,45);
        rangeListSelectionModel.addSelectionInterval(50,55);
        defaultListSelectionModel.addSelectionInterval(50,55);
        rangeListSelectionModel.addSelectionInterval(60,65);
        defaultListSelectionModel.addSelectionInterval(60,65);
        rangeListSelectionModel.addSelectionInterval(90,95);
        defaultListSelectionModel.addSelectionInterval(90,95);
        compareModels();
        rangeListSelectionModel.removeIndexInterval(42,62);
        defaultListSelectionModel.removeIndexInterval(42,62);
        compareModels();
    }


    public void testInsertIndexInterval() {
        // insert a chunk inside selection
        clearModels();
        rangeListSelectionModel.setSelectionInterval(10,40);
        defaultListSelectionModel.setSelectionInterval(10,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(20,30,false);
        defaultListSelectionModel.insertIndexInterval(20,30,false);
        compareModels();
        // insert a chunk between two selections
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,20);
        defaultListSelectionModel.addSelectionInterval(10,20);
        rangeListSelectionModel.addSelectionInterval(30,40);
        defaultListSelectionModel.addSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(25,10,false);
        defaultListSelectionModel.insertIndexInterval(25,10,false);
        compareModels();
        // insert a chunk inside selection
        clearModels();
        rangeListSelectionModel.setSelectionInterval(10,40);
        defaultListSelectionModel.setSelectionInterval(10,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(20,30,true);
        defaultListSelectionModel.insertIndexInterval(20,30,true);
        compareModels();
        // insert a chunk between two selections
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,20);
        defaultListSelectionModel.addSelectionInterval(10,20);
        rangeListSelectionModel.addSelectionInterval(30,40);
        defaultListSelectionModel.addSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(25,10,true);
        defaultListSelectionModel.insertIndexInterval(25,10,true);
        compareModels();
        // insert chunk in begining of selection
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,20);
        defaultListSelectionModel.addSelectionInterval(10,20);
        rangeListSelectionModel.addSelectionInterval(30,40);
        defaultListSelectionModel.addSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(10,10,true);
        defaultListSelectionModel.insertIndexInterval(10,10,true);
        compareModels();
        // insert chunk in last val of selection
        clearModels();
        rangeListSelectionModel.addSelectionInterval(10,20);
        defaultListSelectionModel.addSelectionInterval(10,20);
        rangeListSelectionModel.addSelectionInterval(30,40);
        defaultListSelectionModel.addSelectionInterval(30,40);
        compareModels();
        rangeListSelectionModel.insertIndexInterval(20,25,true);
        defaultListSelectionModel.insertIndexInterval(20,25,true);
        compareModels();
        System.out.println("m_oCatalogSelectionModel = " + rangeListSelectionModel);
    }

    /** Sample Test Case */
    public void testRandom1000() {
        for(int op=0;op<1000;op++){
            int iOperation = (int)(Math.random()*5);
            int val1 = (int)(Math.random()*1000);
            int val2 = (int)(Math.random()*1000);
            switch (iOperation){
                case 0:
                    System.out.println("addSelectionInterval("+val1+","+val2+")");
                    rangeListSelectionModel.addSelectionInterval(val1,val2);
                    defaultListSelectionModel.addSelectionInterval(val1,val2);
                    System.out.println("             "+ rangeListSelectionModel);
                    break;
                case 1:
                    System.out.println("setSelectionInterval("+val1+","+val2+")");
                    rangeListSelectionModel.setSelectionInterval(val1,val2);
                    defaultListSelectionModel.setSelectionInterval(val1,val2);
                    System.out.println("             "+ rangeListSelectionModel);
                    break;
                case 2:
                    System.out.println("removeSelectionInterval("+val1+","+val2+")");
                    rangeListSelectionModel.removeSelectionInterval(val1,val2);
                    defaultListSelectionModel.removeSelectionInterval(val1,val2);
                    System.out.println("             "+ rangeListSelectionModel);
                    break;
                case 3:
                    System.out.println("removeIndexInterval("+val1+","+val2+")");
                    rangeListSelectionModel.removeIndexInterval(val1,val2);
                    defaultListSelectionModel.removeIndexInterval(val1,val2);
                    System.out.println("             "+ rangeListSelectionModel);
                    break;
                case 4:
                    int val3 = (int)(Math.random()*10);
                    boolean before = (Math.random() > 0.5);
                    System.out.println("insertIndexInterval("+val1+","+val3+","+before+")");
                    rangeListSelectionModel.insertIndexInterval(val1,val3,before);
                    defaultListSelectionModel.insertIndexInterval(val1,val3,before);
                    System.out.println("             "+ rangeListSelectionModel);
                    break;
            }
            compareModels();
        }
    }


    // =================================================================================================================
    // Helper Methods

    private void clearModels(){
        rangeListSelectionModel.clearSelection();
        defaultListSelectionModel.clearSelection();
        compareModels();
    }

    public static String defaultToString(DefaultListSelectionModel model){
        StringBuilder oBuf = new StringBuilder();
        oBuf.append("DefaultListSelectionModel[");
        oBuf.append("A=");
        oBuf.append(model.getAnchorSelectionIndex());
        oBuf.append(", ");
        oBuf.append("L=");
        oBuf.append(model.getLeadSelectionIndex());
        oBuf.append(", ");
        oBuf.append("O=(");
        oBuf.append(model.getMinSelectionIndex());
        oBuf.append("->");
        oBuf.append(model.getMaxSelectionIndex());
        oBuf.append("), [");
        if (!model.isSelectionEmpty()){
            boolean bLastSelected = false;
            for (int i = model.getMinSelectionIndex(); i <= model.getMaxSelectionIndex();i++) {
                boolean bIsSelected = model.isSelectedIndex(i);
                if (bIsSelected && !bLastSelected){
                    oBuf.append("(").append(i).append("->");
                } else if (!bIsSelected && bLastSelected){
                    oBuf.append(i - 1).append("), ");
                }
                bLastSelected = bIsSelected;
            }
        }
        oBuf.append(model.getMaxSelectionIndex()).append(")");
        oBuf.append("]]");
        return oBuf.toString();
    }

    private void compareModels(){
        // check min/max
        assertEquals("MinSelectionIndex \n["+defaultToString(defaultListSelectionModel)+"]\n["+
                rangeListSelectionModel +"]\n",
                    defaultListSelectionModel.getMinSelectionIndex(),
                    rangeListSelectionModel.getMinSelectionIndex()
            );
        assertEquals("MaxSelectionIndex \n["+defaultToString(defaultListSelectionModel)+"]\n["+
                rangeListSelectionModel +"]\n",
                    defaultListSelectionModel.getMaxSelectionIndex(),
                    rangeListSelectionModel.getMaxSelectionIndex()
            );
        // check anchor/lead
        assertEquals("AnchorSelectionIndex \n["+defaultToString(defaultListSelectionModel)+"]\n["+
                rangeListSelectionModel +"]\n",
                    defaultListSelectionModel.getAnchorSelectionIndex(),
                    rangeListSelectionModel.getAnchorSelectionIndex()
            );
        assertEquals("LeadSelectionIndex \n["+defaultToString(defaultListSelectionModel)+"]\n["+
                rangeListSelectionModel +"]\n\n",
                    defaultListSelectionModel.getLeadSelectionIndex(),
                    rangeListSelectionModel.getLeadSelectionIndex()
            );
        // check selection values
        for (int i = 0; i< defaultListSelectionModel.getMaxSelectionIndex()+10;i++){
            assertEquals("Item["+i+"] Selection Dosn't Match \n["+defaultToString(defaultListSelectionModel)+"]\n["+
                    rangeListSelectionModel +"]\n",
                    defaultListSelectionModel.isSelectedIndex(i),
                    rangeListSelectionModel.isSelectedIndex(i)
            );
        }
    }
}
