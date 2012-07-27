package org.jdesktop.dataset.io.transfer;


import org.jdesktop.dataset.DataTable;

/**
 * <p>Acts as a sort of bridge between some data source and a {@link DataTable}, allowing one to build
 * DataTables from input sources in different source formats without coupling the table with
 * the format. The model is inspired by the patterns used for importing and exporting table
 * data presented by <a href="http://www.holub.com">Allen Holub</a> in his book <b>Holub on
 * Patterns</b> (highly recommended). This interface and its implementations contain none of
 * his code, just the good ideas found in them.
 *
 * <p>Implementations of DataTableImporter present a standard means of querying a data source for
 * a table's data as stored in the input source. An import starts with startImport() and ends with
 * endImport(); in between one can pick up the table name, column names, and row data. There is no
 * guarrantee that an Importer will allow source data to be read more than once.
 *
 * <p>The default implementation is always to return the column data as String.
 *
 * <p>DataTableImporters try to be as forgiving as possible about errors. Individual importers
 * may be configurable about how forgiving they are--they may skip rows instead of stopping an import,
 * for example.
 *
 * @author Patrick Wright
 */
public interface DataTableImporter {
    final String DEFAULT_TABLE_NAME = "IMPORTED_TABLE";
    
    /** Initiates the import; must be called before any other methods. */
    void startImport();
    
    /** 
     * Returns the table name, if the input source can provide it. This will be the constant
     * value DEFAULT_TABLE_NAME in this interface if no table name is provided in the data
     * source. 
     */
    String tableName();
    
    /** 
     * Returns the count of columns per row in the table.
     */
    int columnCount();
    
    /** 
     * Returns the String column names, with a default name of "col-n"
     * (where n is the column number) if columns have no names or if the importer doesn't 
     * know what they are.
     */
    Iterable<String> columnNames();
    
    /**
     * Returns the data in a single row of the source, as an Iterable set of Strings. The Iterable's
     * Iterator will be empty if there are no more rows. For those data sources that mix a "header" row 
     * (with column names) in the same format as a data row, rowData() does not return these header rows.
     */ 
    Iterable rowData();
    
    /** 
     * Returns an Iterable of error messages resulting from the load.
     */
    Iterable<String> errors();
    
    /**
     * Returns true if there were errors during the load that did not cause an exception.
     */
    boolean hasErrors();
    
    /**
     * Returns the number of errors during the load.
     */
    int errorCount();
    
    /** Ends the import and releases resources; must be called after any other methods. */
    void endImport();
    
}