/*
 * Exporter.java
 *
 * Created on March 3, 2005, 1:24 PM
 */

package org.jdesktop.dataset.io.transfer;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataTable;


/**
 * <p>Static utility class for exporting DataTables to a Writer.
 *
 * @author Patrick Wright
 */
public final class Exporter {
    
    /** Exports entire DataTable to  standard out, CSV format. */
    public static void toCSV(DataTable table) {
        toCSV(table, new BufferedWriter(new OutputStreamWriter(System.out)));
    }

    /** Exports entire DataTable to  standard out, tab-delimited format. */
    public static void toTab(DataTable table) {
        toTab(table, new BufferedWriter(new OutputStreamWriter(System.out)));
    }
    
    /** Exports entire DataTable to specified Writer, CSV format */
    public static void toCSV(DataTable table, Writer writer) {
        toDelimited(table, ',', writer);
    }
    
    /** Exports entire DataTable to specified Writer, CSV format */
    public static void toTab(DataTable table, Writer writer) {
        toDelimited(table, '\t', writer);
    }
    
    /** Exports entire DataTable to specified Writer, char-delimited format */
    public static void toDelimited(DataTable table, char delimiter, Writer writer) {
        DelimitedDTExporter exp = new DelimitedDTExporter(writer);
        exp.setDelimiter(delimiter);
        exportTable( table, exp );
    }
    
    /** Exports entire DataTable to standard out, simple XML format */
    public static void toSimpleXML(DataTable table) {
        toSimpleXML("root", table, new BufferedWriter(new OutputStreamWriter(System.out)));
    }
    
    /** Exports entire DataTable to specified Writer, simple XML format */
    public static void toSimpleXML(String rootName, DataTable table, Writer writer) {
        DataTableXMLBasicExporter exp = new DataTableXMLBasicExporter(writer);
        exp.setRootName(rootName);
        exportTable(table, exp);
    }
    
    /** Exports entire DataTable to specified DataTableExporter. Handles export calls in proper order. */
    public static void exportTable(DataTable table, DataTableExporter exp) {
        exp.startExport();
        exp.exportTableName(table.getName());
        exp.exportColumnMetaData(table.getColumns());
        for ( DataRow row : table.getRows()) {
            exp.exportRow(row);
        }
        exp.endExport();
    }
}
