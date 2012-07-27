// ============================================================================
// $Id: JXSpreadsheetTest.java 1473 2007-07-04 02:29:01Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

import junit.framework.TestCase;

public class JXSpreadsheetTest extends TestCase {
    
    public JXSpreadsheetTest (String name){ super(name); }

    public void testCellConstant() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("42", 0, 0);
        assertCell(sheet.getCell(0,0), new Integer(42));
    }

    public void testCellName() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("42", 0, 0);
        sheet.setCellName("origin", 0, 0);
        assertCell(sheet.getCellByName("origin"), new Integer(42));
    }

    public void testCellNameRef() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("42", 0, 0);
        sheet.setCellName("origin", 0, 0);

        sheet.setCellScript("origin", 1, 1);
        assertCell(sheet.getCell(1,1), new Integer(42));
    }

    public void testCellUpdate() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("42", 0, 0);
        sheet.setCellName("origin", 0, 0);

        sheet.setCellScript("origin", 1, 1);
        assertCell(sheet.getCell(1,1), new Integer(42));

        sheet.setCellScript("27", 0, 0);
        assertCell(sheet.getCell(1,1), new Integer(27));
    }

    public void testGlobalNames() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("sheet", 0, 0);
        assertCell(sheet.getCell(0,0), sheet);

        Cell cellA1 = sheet.setCellScript("cell", 1, 1);
        assertCell(sheet.getCell(1,1), cellA1);
    }

    public void testCellAddressRef() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellScript("42", 0, 0);
        sheet.setCellScript("27", 1, 0);
        sheet.setCellScript("A0+A1", 2, 0);
        assertCell(sheet.getCell(2,0), new Double(69.0));
    }

    public void testCircularRef() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);
        sheet.setCellName("origin", 0, 0);
        Cell cell = sheet.setCellScript("origin", 0, 0);

        assertFalse(cell.isValid());
        assertEquals(Cell.CIRCULAR_REF_ERR, cell.getErrorMsg());
        assertTrue(cell.getException() instanceof CircularReferenceException);
    }

    public void testSetValueObj() {
        JXSpreadsheet sheet = new JXSpreadsheet(3,3);

        sheet.setCellValue(Integer.valueOf(42), 0, 0);
        sheet.setCellScript("27", 1, 0);
        sheet.setCellScript("A0+A1", 2, 0);
        assertCell(sheet.getCell(0,0), Integer.valueOf(42));
        assertCell(sheet.getCell(2,0), new Double(69.0));
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    
    private void assertCell(Cell cell, Object value) {
        if (cell.isValid()) {
            assertEquals(value, cell.getValue());
        }
        else {
            System.err.println("Cell contents are " +cell.getErrorMsg());
            System.err.println("Cell exception is " +cell.getException());
            fail("Cell "+cell+" is invalid");
        }
    }

    static public void main(String[] args) {
        junit.swingui.TestRunner.run(JXSpreadsheetTest.class);
    }
}
