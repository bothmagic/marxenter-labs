/*
 * DelimitedDTExporter.java
 *
 * Created on March 2, 2005, 9:07 PM
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
 * <p>An exporter for DataTables to a delimited format, e.g. CSV or TSV; see {@link DataTableExporter}
 * for details. The DelimitedDTExporter assumes you are writing to a non-contested stream location
 * for which you have access writes: e.g. a flat file in a directory with write permissions.
 *
 * <p>Not thread-safe; create one instance per execution. 
 * 
 * <p>A delimiter is a single character exported between every column; empty columns ("" or null)
 * receive two delimiters in a row. The default delimiter here is a comma, and can be overridden in
 * {@link #setDelimiter(char)} before export starts. Lines are delimited by "\n", but that can also
 * be overridden by {@link #setLineTerminator(String)}, before the export begins.
 *
 * <p>Column names (headers) may be stored or not, using the Headers enumeration; either NONE or FIRST_LINE
 * is supported. Column names are also delimiter-separated on their own line. If NONE is specified, the
 * first line in the output stream will be the first row of data.
 *
 * @author patrick
 */
public class DelimitedDTExporter implements DataTableExporter {
    private char delimiter;
    private String lineTerminator;
    private Headers headersFlag;
    private StringBuilder output;
    private Writer sink;
    private List<String> errors;
    private List<String> columns;
    
    public enum Headers {NONE, FIRST_LINE};
    
    
    private DelimitedDTExporter() {
        this.delimiter = ',';
        this.lineTerminator = "\n";
        this.output = new StringBuilder();
        this.headersFlag = Headers.NONE;
        this.errors = new ArrayList<String>();
        this.columns = new ArrayList<String>();
    }
    
    /** Creates a new instance of DelimitedDTExporter */
    public DelimitedDTExporter(BufferedWriter writer) {
        this();
        this.headersFlag = headersFlag;
        this.sink = writer;
    }
    
    /** Creates a new instance of DelimitedDTExporter */
    public DelimitedDTExporter(Writer writer) {
        this(new BufferedWriter(writer));
    }
    
    /** Begins the export; call this before any of the export proc. methods */
    public void startExport() {
        // nothing to do
    }
    
    /**
     * Exports column metadata to the sink; but only column names are stored in
     * this export format. The method is ignored if the headers flag to set to NONE
     * (default);
     */
    public void exportColumnMetaData(Iterable<DataColumn> cols) {
        if ( output.length() == 0 ) {
            
            for ( DataColumn col : cols ) {
                if ( headersFlag == Headers.FIRST_LINE ) output.append(col.getName() + delimiter);
                this.columns.add(col.getName());
            }
            // remove trailing delimiter
            if ( headersFlag == Headers.FIRST_LINE ) output.deleteCharAt(output.length() - 1);
        }
    }
    
    /**
     * Exports a row of data from a table. In this format, values are stored as Strings
     * using the toString() method to resolve non-string values.
     */
    public void exportRow(DataRow row) {
        //System.out.println("export row: " + columns);
        if ( columns.size() > 0 ) {
            for ( String col : columns ) {
                //System.out.println("col " + col + " val " + row.getValue(col));
                output.append(row.getValue(col) + "" + delimiter);
            }
            // remove trailing
            output.deleteCharAt(output.length() - 1);
            output.append(lineTerminator);
        } else {
            //System.out.println("NOTHING TO EXPORT");
        }
    }
    
    /** Ignored in this export format. */
    public void exportTableName(String tableName) {
        // not stored in this format
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
    
    public void endExport() {
        // remove trailing line terminator
        if ( output.substring(output.length() - lineTerminator.length()).equals(lineTerminator))
            output.delete(output.length() - lineTerminator.length(), output.length());
        
        char c[] = output.toString().toCharArray();
        
        try {
            this.sink.write(c, 0, c.length);
            this.sink.flush();
            this.sink.close();
        } catch ( IOException e ) {
            errors.add("Either could not write, flush or close the target sink, IOException: " + e.getMessage());
        }
    }
    
    public char getDelimiter() {
        return delimiter;
    }
    
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }
    
    public String getLineTerminator() {
        return lineTerminator;
    }
    
    public void setLineTerminator(String lineTerminator) {
        this.lineTerminator = lineTerminator;
    }
    
}
