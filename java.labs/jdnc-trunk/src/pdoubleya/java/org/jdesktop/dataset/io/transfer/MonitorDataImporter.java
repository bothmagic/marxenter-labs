package org.jdesktop.dataset.io.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>MonitorDataImporter is a {@link DataTableImporter} that logs import activity
 * to the system console. It can wrap any other DataTableImporter set in the
 * constructor. You can use the Monitor around any DataTableImporter without affecting
 * the import contents (e.g. non-destructively).
 *
 * @author Patrick Wright
 */
public class MonitorDataImporter extends AbstractTableImporter {
    private DataTableImporter parent;
    
    /** Creates a new instance of MonitorDataImporter */
    public MonitorDataImporter(DataTableImporter parent) {
        super(parent);
        this.parent = parent;
    }
    
    private void monitor(String msg) {
        System.out.println("Import Monitor: " + msg);
    }
    
    public void startImport() {
        monitor("Import started.");
        parent.startImport();
    }
    
    public String tableName() {
        String tableName = super.tableName();
        monitor("Table to import is: " + tableName);
        return tableName;
    }
    
    public Iterable<String> errors() {
        return parent.errors();
    }
    
    public int columnCount() {
        return parent.columnCount();
    }
    
    public Iterable<String> columnNames() {
        Iterable<String> cols = super.columnNames();
        List<String> bak = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        
        for ( String col : cols ) {
            bak.add(col);
            sb.append(col + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        monitor("Columns in import. " + sb);
        return bak;
    }
    
    public boolean hasErrors() {
        return parent.hasErrors();
    }
    
    int rowCount;
    
    public Iterable<String> rowData() {
        Iterable<String> rows = super.rowData();
        List<String> bak = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        
        for ( String row : rows ) { 
            bak.add(row);
        }
        monitor(rowCount++ + " rows in imported so far.");
        return bak;
    }
    
    public int errorCount() {
        return parent.errorCount();
    }
    
    public void endImport() {
        monitor("Import ended. " + rowCount + " rows imported.");
        parent.endImport();
    }
}
