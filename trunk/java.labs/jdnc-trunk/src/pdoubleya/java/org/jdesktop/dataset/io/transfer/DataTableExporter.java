package org.jdesktop.dataset.io.transfer;


import org.jdesktop.dataset.DataColumn;

import org.jdesktop.dataset.DataRow;

/**
 * <p>Acts as a sort of bridge between a {@link org.jdesktop.dataset.DataTable} and some output some data sink, allowing 
 * one to extract DataTables into different target formats without coupling the table with
 * the format. The model is inspired by the patterns used for importing and exporting table
 * data presented by <a href="http://www.holub.com">Allen Holub</a> in his book <b>Holub on
 * Patterns</b> (highly recommended). This interface and its implementations contain none of
 * his code, just the good ideas found in them.
 *
 * <p>Implementations of DataTableExporter present a standard means of moving data from a DataTable
 * to an external storage format. The exporter is a reification of the Builder pattern (according
 * to Holub, myself, I agree): the director of the export wants to build something without knowing
 * how that thing is built. An DataTableExporter is the Builder, and an implementation is
 * the ConcreteBuilder. 
 * 
 * <p>An export starts with startExport() and ends with
 * endExport(); in between one can store the table name, column metadata, and row data. Exporters
 * do not have to enforce uniqueness or implement any data structure restrictions; so, if writing
 * to a validated format (like an XML with schema), you could possibly create an invalid export. Also,
 * exporters may vary in their ability to handle storage, concurrency conflicts, etc. On the other hand,
 * if you just need to get data out of your application into, say, a text file, and exporter can
 * make that very simple for you, even if some information is lost.
 *
 * <p>Generally speaking, exporting is not the same thing as "storing" in the sense one says of
 * working with a database, say, a relational database. The analogy is more like writing to a file,
 * rather than updating some resource which may be edited concurrently by other users.
 *
 * <p>The default implementation is always to write the column data as String.
 *
 * <p>DataTableExporters try to be as forgiving as possible about errors. Individual exporters
 * may be configurable about how forgiving they are--they may skip rows instead of stopping an export,
 * for example.
 *
 * @author Patrick Wright
 */
public interface DataTableExporter {
    /** Initiates the export; must be called before any other methods. */
    void startExport();
    
    /** 
     * Optionally implemented, stores the table's name in the export target; some storage formats
     * don't allow for a table name. Check the implementation's docs.
     */
    void exportTableName(String tableName);
    
    /** 
     * Exports the column's metadata--column name, type, required attribute, etc. Different storage
     * formats will have varying levels of support for different metadata, so check implementation 
     * docs.
     */
    void exportColumnMetaData(Iterable<DataColumn> cols);
    
    /**
     * Stores the data in a single row of the DataTable, provided as DataValues. A given storage format
     * may have varying levels of support for different datatypes and type conversions: for example,
     * in a CSV text file we would expect to store values as simple Strings, convertible in reverse
     * to an equivalent Java datatype. Check implementation docs.
     */ 
    void exportRow(DataRow rowValues);
    
    /** 
     * Returns an Iterable of error messages resulting from the export.
     */
    Iterable<String> errors();
    
    /**
     * Returns true if there were errors during the export that did not cause an exception.
     */
    boolean hasErrors();
    
    /**
     * Returns the number of errors during the export.
     */
    int errorCount();
    
    /** Ends the export and releases resources; must be called when the export is complete. */
    void endExport();
}