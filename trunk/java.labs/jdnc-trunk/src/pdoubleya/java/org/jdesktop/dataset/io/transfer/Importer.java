/*
 * Importer.java
 *
 * Created on March 3, 2005, 1:16 PM
 */

package org.jdesktop.dataset.io.transfer;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;

/**
 * <p>Utility class for importing DataTables from a Stream; all methods are static.
 *
 * @author Patrick Wright
 */
public final class Importer {
    
    /** Imports a new DataTable from a Reader, in CSV format, using a new (blank) DataSet as the parent. */
    public static DataTable fromCSV(Reader reader) {
        return fromCSV(new DataSet(), "IMPORTED_TABLE", reader);
    }
    
    /** Imports a new DataTable from a Reader, in CSV format, using the specified DataSet as the parent, and with the specified table name. */
    public static DataTable fromCSV(DataSet dataSet, String tableName, Reader reader) {
        return fromDelimited(dataSet, tableName, reader, ',', false);
    }

    /** 
     * Imports a new DataTable from a Reader, in CSV format, using the specified DataSet as the parent, and with the specified table name
     * and with column headers read from the first line of input. 
     */
    public static DataTable fromCSVWithHeaders(DataSet dataSet, String tableName, Reader reader) {
        return fromDelimited(dataSet, tableName, reader, ',', true);
    }

    /** Imports a new DataTable from a Reader, in tab-delimited format, using a new (blank) DataSet as the parent. */
    public static DataTable fromTab(Reader reader) {
        return fromTab(new DataSet(), DataTableImporter.DEFAULT_TABLE_NAME, reader);
    }

    /** Imports a new DataTable from a Reader, in tab-delimited format, using the specified DataSet as the parent, and with the specified table name. */
    public static DataTable fromTab(DataSet dataSet, String tableName, Reader reader) {
        return fromDelimited(dataSet, tableName, reader, '\t', false);
    }

    /** Imports a new DataTable from a Reader, in char-delimited format, using the specified DataSet as the parent, and with the specified table name. */
    public static DataTable fromDelimited(DataSet dataSet, String tableName, Reader reader, char delimiter, boolean withHeaders) {
        DelimitedDTImporter imp = new DelimitedDTImporter(reader);
        imp.setDelimiter(delimiter);
        imp.setHasHeaders(withHeaders);
        DataTable table = importTable(dataSet, imp);
        table.setName(tableName);
        return table;
    }

    /** Imports a new DataTable from a Reader, using the specified DataTableImporter. Calls import methods in proper sequence for the import.  */
    public static DataTable importTable(DataSet parentDS, DataTableImporter fromImporter) {
        // SAMPLE: how to monitor an import
        //fromImporter = new MonitorDataImporter(fromImporter);
        
        fromImporter.startImport();
        
        DataTable table = parentDS.createTable();
        table.setName(fromImporter.tableName());
        
        List<String> headerNames = new ArrayList<String>();
        Iterable<String> headers = fromImporter.columnNames();
        for ( String header : headers ) {
            table.createColumn(header);
            headerNames.add(header);
        }
        //System.out.println("Header names " + headerNames);
        
        int cnt = 0;
        while ( true ) {
            Iterable vals = fromImporter.rowData();
            if ( vals.iterator().hasNext() ) {
                int i = 0;
                DataRow drow = table.appendRowNoEvent();
                for ( Object val : vals) {
                    drow.setValue( headerNames.get(i++), val);
                }
                //System.out.println("  Added row:" + drow);
                cnt++;
            } else break;
        }
        //System.out.println("*** Added " + cnt + " rows");
        if ( fromImporter.hasErrors()) {
            System.err.println("Data load incomplete: " + fromImporter.errorCount() + " rows could not be loaded, and were skipped.");
            System.err.println("Error messages follow:");
            Iterable<String> errors = fromImporter.errors();
            for ( String err : errors ) {
                System.err.println("Skipped a line because: " + err);
            }
        }
       
        fromImporter.endImport();
        
        return table;
    }
}
