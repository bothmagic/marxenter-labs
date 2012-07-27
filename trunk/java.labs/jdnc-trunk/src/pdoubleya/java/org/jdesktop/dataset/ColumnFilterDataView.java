package org.jdesktop.dataset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple view of a table which shows or hides columns; only the getColumns()
 * method is affected, other methods which take column references pass through
 * to the underlying table. 
 *
 * @author Patrick Wright
 */
public class ColumnFilterDataView extends DataView {
    private Set<DataColumn> columnSet;
    
    /** Creates a new instance of ColumnFilterDataView */
    public ColumnFilterDataView(DataView source) {
        super(source);
        this.columnSet = new HashSet<DataColumn>(source.getColumns());
    }
    
    public void hideColumn(String column) {
        DataColumn col = getSource().getColumn(column);
        if ( col != null ) {
            columnSet.remove(col);
        }
    }
    
    public void hideColumns(String... columns) {
        for ( String column : columns ) {
            hideColumn(column);
        }
    }

    public void hideAll() {
        for ( DataColumn col : getSource().getColumns()) {
            hideColumn(col.getName());
        }
    }

    public void showColumn(String column) {
        DataColumn col = getSource().getColumn(column);
        if ( col != null ) {
            columnSet.add(col);
        }
    }
    
    public void showColumns(String... columns) {
        for ( String column : columns ) {
            showColumn(column);
        }
    }

    public void showAll() {
        for ( DataColumn col : getSource().getColumns() ) {
            showColumn(col.getName());
        }
    }

    public List<DataColumn> getColumns() {
        // we could just pass back the column set, but the order won't be preserved with respect 
        // to the source table
        List<DataColumn> cols = new ArrayList<DataColumn>(columnSet.size());
        for ( DataColumn col : getSource().getColumns() ) {
            if ( columnSet.contains(col)) {
                cols.add(col);
            }
        }
        return cols;
    }
}
