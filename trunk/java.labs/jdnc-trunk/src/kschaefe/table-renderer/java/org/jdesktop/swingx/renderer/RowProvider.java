/*
 * $Id: RowProvider.java 2385 2008-04-07 01:30:10Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.renderer;

import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;

/**
 * A component provider which uses a {@code JTable}.
 * 
 * @author Karl Schaefer
 */
public class RowProvider extends ComponentProvider<JTable> {
    private static class OneRowTableModel extends AbstractTableModel {
        private int columnCount;

        private List<?> row;

        public OneRowTableModel(int columnCount) {
            this.columnCount = columnCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getColumnCount() {
            return columnCount;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getRowCount() {
            return 1;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (row == null || columnIndex >= row.size()) {
                return "";
            }

            return row.get(columnIndex);
        }

        public void setRow(List<?> row) {
            this.row = row;
            fireTableRowsUpdated(0, 0);
        }
    }

    private ListValue converter;

    private int columnCount;

    /**
     * A default provider, it is equivalent to passing {@code (null, 1)} to
     * {@link #RowProvider(ListValue, int)}.
     */
    public RowProvider() {
        this(null);
    }

    /**
     * A provider using single-celled table rows and the specified converter.
     * 
     * @param converter
     *            the converter to use
     */
    public RowProvider(ListValue converter) {
        this(converter, 1);
    }

    /**
     * A provider using the default converter and the specified number of columns.
     * 
     * @param columnCount
     *            the number of columns to display
     * @throws IllegalArgumentException
     *             if {@code columnCount < 1}
     */
    public RowProvider(int columnCount) {
        this(null, columnCount);
    }

    /**
     * A provider using the specified converter and number of columns.
     * 
     * @param converter
     *            the converter to use, or {@link ListValue#DEFAULT_VALUE} if {@code null}
     * @param columnCount
     *            the number of columns to display
     * @throws IllegalArgumentException
     *             if {@code columnCount < 1}
     */
    public RowProvider(ListValue converter, int columnCount) {
        super(converter, SwingConstants.CENTER);
        
        if (columnCount < 1) {
            throw new IllegalArgumentException();
        }

        this.converter = converter == null ? ListValue.DEFAULT_VALUE : converter;
        this.columnCount = columnCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureState(CellContext context) {
        rendererComponent.setModel(new OneRowTableModel(columnCount));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected JTable createRendererComponent() {
        return new JXTable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void format(CellContext context) {
        List<?> row = converter.getList(context.getValue());
        ((OneRowTableModel) rendererComponent.getModel()).setRow(row);
    }
}
