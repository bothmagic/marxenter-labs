// ============================================================================
// $Id: CellAddressScheme.java 1290 2007-05-01 02:18:21Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

/**
 * Converts strings to and from cell addresses.  When a spreadsheet cell's
 * script is parsed, the bindings object uses an instance of this interface
 * to recognize when a cell reference has occurred in a script, and to
 * ensure that the proper cell is bound to the given name.
 */
public interface CellAddressScheme {

    /**
     * @returns true if the string contains a legitimate cell address
     */
    public boolean isCellAddress(String str);

    /**
     * @returns 0-based column number contained in the string.
     */
    public int getColumnNum(String str);

    /**
     * @returns 0-based row number contained in the string.
     */
    public int getRowNum(String str);

    /**
     * @returns column address string of the given 0-based column number
     */
    public String getColumnHdr(int col);

    /**
     * @returns row address string of the given 0-based row number
     */
    public String getRowHdr(int row);

    /**
     * @returns string address string of the given 0-based row, column pair
     */
    public String getAddress(int row, int col);
        
}

