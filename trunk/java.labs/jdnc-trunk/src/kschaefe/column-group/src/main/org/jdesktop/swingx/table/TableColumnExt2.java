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
import java.util.List;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * A subclass to add the extended functionality needed for the grouping. This would be merged with
 * the parent class when pushed into SwingX.
 * 
 * @author Karl George Schaefer
 */
public class TableColumnExt2 extends TableColumnExt {
    public TableColumnExt2() { }

    public TableColumnExt2(int modelIndex) {
        super(modelIndex);
    }

    public TableColumnExt2(int modelIndex, int width) {
        super(modelIndex, width);
    }

    public TableColumnExt2(int modelIndex, int width, TableCellRenderer cellRenderer,
            TableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
    }

    public TableColumnExt2(TableColumnExt columnExt) {
        super(columnExt);
    }

    //BEGIN NEW CODE HERE !!!
    protected List<TableColumnExt2> children = new ArrayList<TableColumnExt2>();
    
    TableColumnExt2 parent;
    
    public TableColumnExt2 getParent() {
        return parent;
    }

    /**
     * Add a {@code TableColumn} to this group.
     * 
     * @param column
     *            the table column to add
     */
    public void add(TableColumnExt2 column) {
        if (column == this) {
            throw new IllegalArgumentException("cannot add to self");
        }
        
        if (column.parent != null && column.parent != this) {
            column.parent.remove(column);
        }
        
        children.add(column);
        column.parent = this;
    }

    /**
     * Remove a {@code TableColumn} from this group.
     * 
     * @param column
     *            the table column to remove
     */
    public void remove(TableColumnExt2 column) {
        children.remove(column);
    }
    
    public boolean isVisible() {
        if (parent != null && !parent.visible) {
            return false;
        }
        
        return visible;
    }
    
    public int getWidth() {
        if (children.isEmpty()) {
            return super.getWidth();
        }
        
        long size = 0;
        
        for (TableColumnExt2 child : children) {
            if (child.isVisible()) {
                size += child.getWidth();
            }
        }
        
        return size > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) size;
    }
}
