/*
 * $Id: DataGrid.java 752 2005-11-01 13:19:52Z pdoubleya $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.dataset.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataTable;

/**
 *
 * @author Patrick Wright
 */
public class DataGrid {
    private List columnNames;
    private List data;
    
    public DataGrid() {
        super();
        columnNames = new ArrayList();
        data = new ArrayList();
    }
    
    public static DataGrid newGrid(DataTable table) {
        DataGrid grid = new DataGrid();
        for ( DataColumn col : table.getColumns()) {
            grid.addColumn(col.getName());
        }
        for ( DataRow row : table.getRows()) {
            grid.addRow();
            for ( DataColumn col : table.getColumns()) {
                grid.addValue(row.getValue(col).toString());
            }
        }
        return grid;
    }
    
    public void addColumn(String columnName) {
        columnNames.add(columnName);
    }
    public void addRow() {
        data.add(new ArrayList());
    }
    public void addValue(Object value) {
        List row = (List)data.get(data.size() - 1);
        row.add(value);
    }
    public void dump(PrintWriter pw) {
        try {
            if ( columnNames.size() == 0 ) return;
            
            Iterator cols = columnNames.iterator();
            int totalLen = 0;
            int[] lengths = new int[columnNames.size()];
            String colSep = " | ";
            int col = 0;
            while ( cols.hasNext()) {
                String name = (String)cols.next();
                lengths[col] = name.length() + colSep.length();
                totalLen += lengths[col];
                pw.print(name + colSep);
                col++;
            }
            pw.println();
            for ( int i=0; i < totalLen; i++ ) {
                pw.print("=");
            }
            pw.println();
            
            Iterator rows = data.iterator();
            while ( rows.hasNext()) {
                Iterator values = ((List)rows.next()).iterator();
                col = 0;
                while ( values.hasNext()) {
                    pw.print(getPaddedString((String)values.next(), lengths[col++] - colSep.length()) + colSep);
                }
                pw.println();
            }
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String getPaddedString(String val, int padTo) {
        val = ( val == null ? "{null}" : val.trim());
        int valLen = val.length();
        if ( valLen > padTo ) {
            return val.substring(0, padTo);
        } else {
            return val + BUFFER.substring(0, padTo - valLen);
        }
    }
    
    private static final String BUFFER;
    static {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i < 50; i++) {
            sb.append(' ');
        }
        BUFFER = sb.toString();
    }
}