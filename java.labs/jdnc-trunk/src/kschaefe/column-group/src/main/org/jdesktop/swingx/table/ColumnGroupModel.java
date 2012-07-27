/*
 * $Id: AbstractEnum.java 2997 2009-01-30 13:48:26Z kschaefe $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

/**
 * @author Karl George Schaefer
 *
 */
public class ColumnGroupModel implements TableColumnModelExt {
    /** Width margin between each column */
    protected int columnMargin;

    /** A local cache of the combined width of all columns */
    protected int totalColumnWidth;
    
    private final List<TableColumn> preOrder = new ArrayList<TableColumn>();
    private final List<TableColumn> leaves = new ArrayList<TableColumn>();
    private final EventListenerList listenerList = new EventListenerList();
    
    public ColumnGroupModel() {
        //TODO implement createSelectionModel
//        setSelectionModel(createSelectionModel()); 
        setColumnMargin(1); 
        invalidateWidthCache(); 
        setColumnSelectionAllowed(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addColumnModelListener(TableColumnModelListener x) {
        listenerList.add(TableColumnModelListener.class, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeColumnModelListener(TableColumnModelListener x) {
        listenerList.remove(TableColumnModelListener.class, x);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableColumn getColumn(int columnIndex) {
        return preOrder.get(columnIndex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableColumnExt getColumnExt(int columnIndex) {
        TableColumn column = getColumn(columnIndex);
        
        if (column instanceof TableColumnExt) {
            return (TableColumnExt) column;
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableColumnExt getColumnExt(Object identifier) {
        for (TableColumn column : preOrder) {
            if ((column instanceof TableColumnExt) && (identifier.equals(column.getIdentifier()))) {
                return (TableColumnExt) column;
            }
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return preOrder.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount(boolean includeHidden) {
        if (includeHidden) {
            //TODO implement
            return getColumnCount();
        }
        
        return getColumnCount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnIndex(Object columnIdentifier) {
        if (columnIdentifier == null) {
            throw new IllegalArgumentException("Identifier is null");
        }

        int index = 0;
        
        for (TableColumn aColumn : preOrder) {
            if (columnIdentifier.equals(aColumn.getIdentifier())) {
                return index;
            }
            
            index++;
        }
        
        throw new IllegalArgumentException("Identifier not found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnIndexAtX(int x) {
        if (x < 0) {
            return -1;
        }
        
        int index = 0;
        
        for (TableColumn column : leaves) {
            x = x - column.getWidth();
            if (x < 0) {
                return index;
            }
            
            index++;
        }
        
        return -1; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<TableColumn> getColumns() {
        return Collections.enumeration(preOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TableColumn> getColumns(boolean includeHidden) {
        if (includeHidden) {
            //TODO implement
            return Collections.list(getColumns());
        }
        
        return Collections.list(getColumns());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTotalColumnWidth() {
        if (totalColumnWidth == -1) { 
            recalcWidthCache(); 
        }
        return totalColumnWidth;
    }

    /**
     * 
     */
    protected void recalcWidthCache() {
        long width = 0;
        
        for (TableColumn leaf : leaves) {
            width += leaf.getWidth();
        }
        
        totalColumnWidth = width > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) width;
    }

    private void invalidateWidthCache() { 
        totalColumnWidth = -1; 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnMargin() {
        return columnMargin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColumnMargin(int newMargin) {
        if (newMargin != columnMargin) {
            columnMargin = newMargin;
            //TODO implement notification
            // Post columnMarginChanged event notification.
//            fireColumnMarginChanged();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListSelectionModel getSelectionModel() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionModel(ListSelectionModel newModel) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getColumnSelectionAllowed() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColumnSelectionAllowed(boolean flag) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getSelectedColumns() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectedColumnCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addColumn(TableColumn column) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveColumn(int columnIndex, int newIndex) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeColumn(TableColumn column) {
        // TODO Auto-generated method stub
        
    }
}
