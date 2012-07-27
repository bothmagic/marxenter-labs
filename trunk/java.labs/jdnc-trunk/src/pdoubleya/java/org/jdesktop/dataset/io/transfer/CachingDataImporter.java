package org.jdesktop.dataset.io.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author patrick
 */
public class CachingDataImporter extends AbstractTableImporter {
    private enum State { NOT_STARTED, IN_PROCESS, ENDED, RESTARTED };

    private State currentState;
    private String tableName;
    private List<String> errors;
    private List<String> columns;
    private List<List> rows; 
    
    private DataTableImporter parent;
    
    /** Creates a new instance of CachingDataImporter */
    public CachingDataImporter(DataTableImporter parent) {
        super(parent);
        this.parent = parent;
        this.errors = new ArrayList<String>();
        this.columns = new ArrayList<String>();
        this.rows = new ArrayList<List>();
    }

    public void startImport() {
        switch ( currentState ) {
            case NOT_STARTED:
                currentState = State.IN_PROCESS;
                parent.startImport();
                break;
            case IN_PROCESS:
                return;
            case ENDED:
                currentState = State.RESTARTED;
                break;
            case RESTARTED:
                return;
        }
    }

    public String tableName() {
        if ( tableName == null ) {
            tableName = super.tableName();
        }
        return tableName;
    }

    public Iterable<String> errors() {
        if ( errors == null ) {
            //errors = new ArrayList
        }
        Iterable retValue;
        
        retValue = super.errors();
        return retValue;
    }

    public int columnCount() {

        int retValue;
        
        retValue = super.columnCount();
        return retValue;
    }

    public Iterable<String> columnNames() {

        Iterable retValue;
        
        retValue = super.columnNames();
        return retValue;
    }

    public boolean hasErrors() {

        boolean retValue;
        
        retValue = super.hasErrors();
        return retValue;
    }

    public Iterable rowData() {

        Iterable retValue;
        
        retValue = super.rowData();
        return retValue;
    }

    public int errorCount() {

        int retValue;
        
        retValue = super.errorCount();
        return retValue;
    }
    
    public void endImport() {
        switch ( currentState ) {
            case NOT_STARTED:
                throw new RuntimeException("Import ended before it was started; call startImport() first.");
            case IN_PROCESS:
                currentState = State.ENDED;
                parent.endImport();
                break;
            case ENDED:
                System.err.println("endImport() called when already stopped.");
                break;
            case RESTARTED:
                currentState = State.ENDED;
                break;
        }
    }
}
