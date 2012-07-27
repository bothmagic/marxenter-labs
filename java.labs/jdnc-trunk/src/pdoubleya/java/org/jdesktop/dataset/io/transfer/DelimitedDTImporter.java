/** By Patrick Wright, licensed under the LGPL */
package org.jdesktop.dataset.io.transfer;

import java.io.*;
import java.util.*;



/**
 * <p>Imports single-character delimited data into a DataSet, where rows are broken into
 * lines, e.g. for CSV or TSV files; default for comma-separated, line-oriented input sources.
 *
 * <p>Not thread-safe; create one instance per input source.
 *
 * <p>It is not possible to provide a row count when using the importer, as the source is read
 * on-the-fly. Default delimiter is comma; you can override this with
 * {@link #setDelimiter(char)} before starting the import. The input source may include column
 * headers or not; currently only first line headers are supported. By default assumes
 * the input has no headers and columns are given generic, numbered names; to read column
 * headers from the input, use {@link #setHeadersFlag(Headers)} before the import.
 *
 * <p>As the Reader is provided <b>to</b> an instance of this class, it is not closed when export is complete--
 * you must do this.
 *
 *
 * @author Patrick Wright
 */
public class DelimitedDTImporter implements DataTableImporter {
    public enum Headers {NONE, FIRST_LINE};
    private enum ImportStatus {NOT_STARTED, IN_PROCESS, ENDED};
    
    private char delimiter;
    private Reader dataReader;
    private LineNumberReader lnr;
    private List errors;
    private List headers;
    private Headers headersFlag;
    private int colCount;
    private int lineNo = 0;
    private String firstLine;
    private String delimiterRegex;
    private ImportStatus currentImportStatus;
    
    private DelimitedDTImporter() {
        this.setDelimiter(',');
        this.setHeadersFlag(Headers.NONE);
        this.errors = new ArrayList<String>();
        this.currentImportStatus = ImportStatus.NOT_STARTED;
    }
    
    /** Creates a new instance of DelimitedDSImporter */
    public DelimitedDTImporter(BufferedReader reader) {
        this();
        this.dataReader = reader;
        this.lnr = new LineNumberReader(reader);
    }
    
    public DelimitedDTImporter(File file) throws IOException {
        this(new FileReader(file));
    }
    
    public DelimitedDTImporter(Reader reader) {
        this(new BufferedReader(reader));
    }
    
    public DelimitedDTImporter(InputStream is) {
        this(new InputStreamReader(is));
    }
    
    /**
     * Initializes the import process. Must be called before accessing any of the table data; call endImport()
     * when done. The importer doesn't support multiple trips through the source, so calling startImport()
     * multiple times is pointless.
     *
     * TODO: Imports for delimited sources can be restarted if the input source was a file, at which point it
     * a start will begin reading again from the beginning.
     * TODO: for multiple trips, use the CachingDSImporter;
     */
    public void startImport() {
        if ( currentImportStatus != ImportStatus.NOT_STARTED ) {
            throw new RuntimeException("Import already started; restart not allowed.");
        }
        try {
            if ( firstLine == null ) {
                firstLine = lnr.readLine();
                parseLine(firstLine);
            }
        } catch ( IOException e ) {
            throw new RuntimeException("Could not start import; read first line failed: " + e.getMessage());
        }
        this.currentImportStatus = ImportStatus.IN_PROCESS;
    }
    
    
    /**
     * Returns the table name for the source, if it could be read. The standard "delimited" format
     * is just be a list of lines, separated with a delimiter, with just column data, so this implementation
     * always returns the constant String DataTableImporter.DEFAULT_TABLE_NAME.
     *
     * @return See desc.
     */
    public String tableName() {
        checkInProcess("tableName()");
        return DEFAULT_TABLE_NAME;
    }
    
    /**
     * Returns an Iterator over the column names for the data source. If HAS_COL_HEADERS was specified
     * in the constructor, the column names are taken from the first row of the input source. Otherwise
     * the default column names "col-0" through "col-n" (where is the column count) is returned.
     */
    public Iterable<String> columnNames() {
        checkInProcess("columnNames()");
        return headers;
    }
    
    /**
     * Returns the count of columns in the source data. Delimited files are expected to have the same
     * schema for each row, so this is the count of columns in the first row.
     */
    public int columnCount() {
        checkInProcess("columnCount()");
        if ( lineNo == 0 )
            rowData();
        return colCount;
    }
    
    /**
     * <p>Returns an Iterator of String data read from the source. Each element is a single String separated
     * on the line by the delimiter.
     *
     * <p>Blank lines are skipped.
     *
     * <p>Column count comes from the first readable line.
     *
     * <p>If the column count for the next row to read does not match the expected column count, an error is logged,
     * and the next line is read.
     */
    public Iterable<String> rowData() {
        checkInProcess("rowData()");
        
        List<String> data = null;
        try {
            if ( lineNo == 1 && firstLine != null && headersFlag == Headers.NONE ) {
                // HACK
                data = parseLine(firstLine);
            } else {
                data = parseLine(lnr.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not start import; read line failed: " + e.getMessage());
        }
        return data;
    }
    
    private List<String> parseLine(String line) {
        //System.out.println("  parseLine " + lineNo + ": " + line);
        List<String> data = new ArrayList<String>();
        if ( line != null ) {
            try {
                String cols[] = line.split(delimiterRegex);
                //System.out.println("Split to " + cols.length + " col");
                
                if ( lineNo == 0 ) {
                    colCount = cols.length;
                    firstLine = line;
                    initHeaders(cols);
                }
                
                if ( lineNo > 0 || ( lineNo == 0 && getHeadersFlag() == Headers.NONE )) {
                    //System.out.println("OK ADDING ROW");
                    if ( cols.length == colCount ) {
                        //System.out.println("GOOD ON COL COUNT");
                        data = new ArrayList<String>();
                        for ( String col : cols ) {
                            data.add(col.trim());
                        }
                        //System.out.println("ROW IS " + data);
                    } else {
                        errors.add("Column count on line " + (lineNo + 1) + " was not same as for first line; expected " + colCount + ", found " + cols.length);
                    }
                } else {
                    //System.out.println("NOT ADDING ROW");
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            lineNo++;
        }
        return data;
    }
    
    /**
     * Ends the import process. Must be called after accessing the table data to release resources. If a Reader
     * was provided in the constructor, it will <b>not</b> be closed. Call {@link #startImport()} to start the process.
     */
    public void endImport() {
        if ( currentImportStatus != ImportStatus.IN_PROCESS ) {
            throw new RuntimeException("Operation not in-progress, endImport() not allowed");
        }
        this.currentImportStatus = ImportStatus.ENDED;
    }
    
    private void checkInProcess(String operation) {
        switch (currentImportStatus) {
            case NOT_STARTED:
                throw new RuntimeException("Importer not started, operation not allowed {" + operation + "}");
            case ENDED:
                throw new RuntimeException("Import ended, operation not allowed {" + operation + "}");
        }
    }
    
    /**
     * If the source contains column names, these are read from the first non-blank row and stored in the headers
     * member List. Subsequent calls to this method are ignored after the headers are read.
     */
    private void initHeaders(String cols[]) {
        if ( this.headers == null ) {
            headers = new ArrayList(cols.length);
            
            //System.out.println("Current headers flag " + this.getHeadersFlag());
            if ( this.getHeadersFlag() == Headers.FIRST_LINE ) {
                // TODO: ?
                for ( String col : cols ) {
                    headers.add(col);
                }
            } else {
                for ( int i = 0; i < cols.length; i++ ) {
                    headers.add("col-" + i);
                }
            }
        }
        //System.out.println("  headers: " + headers);
    }
    
    /**
     * Returns an iterator over error messages produced when reading the input.
     */
    public Iterable<String> errors() {
        return errors;
    }
    
    public boolean hasErrors() {
        return errors.iterator().hasNext();
    }
    
    public int errorCount() {
        return errors.size();
    }
    
    public char getDelimiter() {
        return delimiter;
    }
    
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
        String pre = "";
        switch ( delimiter ) {
            case '|':
            case '\\':
            case '?':
            case '$':
            case '^':
            case '[':
            case ']':
            case '(':
            case ')':
            case '.':
            case '*':
            case '+':
                pre = "\\";
                break;
            default:
                // nothing
        }
        delimiterRegex = pre + delimiter;
    }
    
    private Headers getHeadersFlag() {
        return headersFlag;
    }
    
    private void setHeadersFlag(Headers flag) {
        this.headersFlag = flag;
        //System.out.println("Headers set to: " + this.headersFlag + ", param " + flag);
    }
    
    public void setHasHeaders(boolean has) {
        setHeadersFlag((has ? Headers.FIRST_LINE : Headers.NONE));
    }
}
