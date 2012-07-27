// ============================================================================
// $Id: DefaultCellAddressScheme.java 1290 2007-05-01 02:18:21Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts addresses in a traditional spreadsheet form to and from row/column pair addresses.
 * The form supported by this scheme is given by the regular expression <tt>[A-Z]+[0-9]*</tt>,
 * where the column is described by the alphabetic part and the row is described by the
 * numeric part.
 */
class DefaultCellAddressScheme implements CellAddressScheme {
    
    Pattern pattern = Pattern.compile("([A-Z]+)([0-9]*)");
    
    public boolean isCellAddress(String str){
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public int getColumnNum(String str) {
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        String col = matcher.group(1);
        if (col.length() == 1) {
            return col.charAt(0) - 'A';
        }

        int len = col.length(), val = 0, pos = 1;
        for(int i = 1; i <= len; ++i) {
            int c = col.charAt(len - i) - 'A';
            if (i > 1)
                ++c;
                    
            val += c * pos;
            
            pos *= 26;
        }

        return val;
    }

    public int getRowNum(String str) {
        Matcher matcher = pattern.matcher(str);
        boolean b = matcher.matches();
        return Integer.parseInt(matcher.group(2));
    }

    public String getColumnHdr(int col) {
        StringBuilder buf = new StringBuilder();
        while(true) {
            buf.insert(0, (char)('A' + col % 26));

            col /= 26;
            if (col == 0)
                break;

            --col;
        }

        return buf.toString();
    }

    public String getRowHdr(int row) {
        return String.valueOf(row);
    }
    
    public String getAddress(int row, int col) {
        return getColumnHdr(col)+getRowHdr(row);
    }
}

