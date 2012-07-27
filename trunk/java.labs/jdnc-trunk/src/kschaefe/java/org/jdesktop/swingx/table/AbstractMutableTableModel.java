/**
 * 
 */
package org.jdesktop.swingx.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Karl George Schaefer
 */
public abstract class AbstractMutableTableModel<R, C> extends
        AbstractTableModel implements MutableTableModel<R, C> {
    protected final List<R> data;
    
    public AbstractMutableTableModel() {
        this(null);
    }
    
    public AbstractMutableTableModel(List<R> data) {
        this.data = data == null ? createDefaultList() : data;
    }
    
    /**
     * Creates the list that backs this model
     * @return
     */
    protected List<R> createDefaultList() {
        return new ArrayList<R>();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addColumn(Object columnName, C columnData) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addRow(R rowData) {
        insertRow(getRowCount(), rowData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertRow(int row, R rowData) {
        data.add(row, rowData);
        
        fireTableRowsInserted(row, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveRow(int start, int end, int to) {
        int shift = to - start;
        int first;
        int last;
        
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        
        List<R> moved = new ArrayList<R>();
        
        for (int i = end; i >= start; i--) {
            moved.add(data.remove(i));
        }
        
        data.addAll(to, moved);
        
        fireTableRowsUpdated(first, last);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRow(int row) {
        data.remove(row);
        
        fireTableRowsDeleted(row, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        return data.size();
    }
}
