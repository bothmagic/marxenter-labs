/*
 * DataTableXMLBasicExporter.java
 *
 * Created on March 3, 2005, 1:47 PM
 */

package org.jdesktop.dataset.io.transfer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataRow;

/**
 * <p>A DataTableExporter that exports in a simple XML format to a Writer. The XML format has a single
 * root representing the entire table, with one child element below the root per row, named after the 
 * table. Columns are their own named elements below each table node, with the content being the columns
 * content as a unconverted String. See Richard Bair's Adventure Builder demo files for the schema for this.
 *
 * <p>Writers will be buffered automatically if they are not already.
 *
 * <p>The XML is written out as a String; no validation and no doctype is added to the XML.
 *
 * <p>As a convenience, launch the export with the Exporter class, {@link Exporter#toSimpleXML(DataTable)} or
 * {@link Exporter#toSimpleXML(String, DataTable, Writer)}. 
 *
 * <p>As the Writer is provided <b>to</b> an instance of this class, it is not closed when export is complete--
 * you must do this. However, contents are flushed (as buffering is used).
 *
 * <p>For more details on Exporters, see the interface docs for {@link DataTableExporter}.
 * 
 * @author Patrick Wright
 */
public class DataTableXMLBasicExporter implements DataTableExporter {
    private StringBuilder output;
    private Writer sink;
    private String rootName;
    private List<String> errors;
    private List<DataColumn> columns;
    private String tableName;
    
    /** Default constructor */
    private DataTableXMLBasicExporter() {
        this.output = new StringBuilder();
        this.errors = new ArrayList<String>();
        this.columns = new ArrayList<DataColumn>();
        this.rootName = "root";
        this.tableName = "table";
    }
    
    /** A new instance for a given BufferedWriter. */
    public DataTableXMLBasicExporter(BufferedWriter writer) {
        this();
        this.sink = writer;
    }
    
    /** A new instance for a given Writer. */
    public DataTableXMLBasicExporter(Writer writer) {
        this(new BufferedWriter(writer));
    }
    
    /** Begins the export; call this before any of the export proc. methods */
    public void startExport() {
    }
    
    /**
     * Exports column metadata to the sink; but only column names are stored in
     * this export format.
     */
    public void exportColumnMetaData(Iterable<DataColumn> cols) {
        if ( output.length() == 0 ) {
            for ( DataColumn col : cols ) {
                this.columns.add(col);
            }
        }
    }
    
    /** Exports the table name; should call this before {@link #exportRow(DataRow)} */
    public void exportTableName(String tableName) {
        this.tableName = tableName;
    }
    
    /**
     * Exports a row of data from a table. In this format, values are stored as Strings
     * using the toString() method to resolve non-string values. Columns for the row
     * are nested in their own <tablename> element, using either the name specified
     * in {@link #exportTableName(String)}, or a default name.
     */
    public void exportRow(DataRow row) {
        //System.out.println("export row: " + columns);
        if ( columns.size() > 0 ) {
            output.append("\t<");
            output.append(tableName);
            output.append(">\n");
            for ( DataColumn col : columns ) {
                output.append("\t\t<");
                output.append(col.getName());
                output.append(">");
                output.append(row.getValue(col));
                output.append("</");
                output.append(col.getName());
                output.append(">\n");
            }
            output.append("\t</");
            output.append(tableName);
            output.append(">\n");
        } else {
            //System.out.println("NOTHING TO EXPORT");
        }
    }
    
    /** Returns true if there have been errors during the export. */
    public boolean hasErrors() {
        return errors.size() > 0;
    }
    
    /** Returns the count of errors in the export. */
    public int errorCount() {
        return errors.size();
    }
    
    /** Returns a list of errors messages from the export. */
    public Iterable<String> errors() {
        return errors;
    }

    /** 
     * Ends the export and releases resources. Note that the Writer used by this instance
     * will be flushed and closed.
     */
    public void endExport() {
        output.insert(0, "<?xml version=\"1.0\" ?>\n<" + rootName + ">\n");
        output.append("</");
        output.append(rootName);
        output.append(">\n");
        
        char c[] = output.toString().toCharArray();
        
        try {
            this.sink.write(c, 0, c.length);
            this.sink.flush();
        } catch ( IOException e ) {
            errors.add("Either could not write, flush or close the target sink, IOException: " + e.getMessage());
        }
    }
    
    /** Returns the document's root name. */
    public String getRootName() {
        return rootName;
    }
    
    /** Sets the document's root name. */
    public void setRootName(String rootName) {
        this.rootName = rootName;
    }
}