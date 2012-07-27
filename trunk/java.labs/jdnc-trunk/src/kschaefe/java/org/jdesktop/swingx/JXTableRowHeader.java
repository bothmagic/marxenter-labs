/*
 * $Id: JXTableRowHeader.java 3294 2010-08-03 17:49:29Z kschaefe $
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
package org.jdesktop.swingx;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * The class {@code JXTableRowHeader} is used to create a column which contains
 * row header cells. By default a table will not show a row header. The user may
 * manually add a row header to the {@code JScrollPane} row header view port.
 * 
 * @see javax.swing.JScrollPane
 */
public class JXTableRowHeader extends JComponent {
    private static class InternalTableColumnModel extends
            DefaultTableColumnModel {
        public InternalTableColumnModel() {
            addColumn(new TableColumnExt(0, 75));
            getColumn(0).setHeaderValue("");
        }
    }

    private class HeaderResizeListener implements TableModelListener {
        public void tableChanged(TableModelEvent e) {
            // pack before setting preferred width.
            headerTable.packAll();

            TableColumn column = headerTable.getColumnModel().getColumn(0);

            if (column.getPreferredWidth() != getWidth()) {
                headerTable.setPreferredScrollableViewportSize(new Dimension(
                        column.getPreferredWidth(), 0));
            }
        }
    }

    /**
     * The headerTable used to create the row header column.
     */
    protected final JXTable headerTable;

    private JTable table;

    /**
     * Create a row header from the given {@code JTable}. This row header will
     * have the same {@code TableModel} and {@code ListSelectionModel} as the
     * incoming table.
     * 
     * @param table
     *            the table for which to produce a row header.
     */
    public JXTableRowHeader(JTable table) {
        this.table = table;
        this.headerTable = new JXTable(table.getModel(),
                new InternalTableColumnModel(), table.getSelectionModel()) {
            public Object getValueAt(int rowIndex, int columnIndex) {
                return getRowName(rowIndex);
            }
        };

        setLayout(new GridLayout(1, 1));

        this.headerTable.getModel().addTableModelListener(
                new HeaderResizeListener());
        this.headerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.headerTable.getTableHeader().setReorderingAllowed(false);
        this.headerTable.getTableHeader().setResizingAllowed(false);
        add(this.headerTable);

        // pack before setting preferred width.
        this.headerTable.packAll();

        TableColumn column = this.headerTable.getColumnModel().getColumn(0);
        column.setCellRenderer(createDefaultRenderer());
        this.headerTable.setPreferredScrollableViewportSize(new Dimension(
                column.getPreferredWidth(), 0));
        this.headerTable.setInheritsPopupMenu(true);
    }

    /**
     * Returns a default renderer to be used when no row header renderer is
     * defined by the constructor.
     * 
     * @return the default row header renderer
     */
    protected TableCellRenderer createDefaultRenderer() {
        // It would be nice if the following line worked
        // return headerTable.getTableHeader().getDefaultRenderer();

        // TODO get a rollover enabled renderer
        return new DefaultTableRenderer();
    }

    /**
     * Sets the default renderer to be used when no <code>headerRenderer</code>
     * is defined by a <code>TableColumn</code>.
     * 
     * @param defaultRenderer
     *            the default renderer
     */
    public void setDefaultRenderer(TableCellRenderer defaultRenderer) {
        headerTable.getColumn(0).setCellRenderer(defaultRenderer);
    }

    /**
     * Returns the default renderer used when no <code>headerRenderer</code>
     * is defined by a <code>TableColumn</code>.
     * 
     * @return the default renderer
     */
    public TableCellRenderer getDefaultRenderer() {
        return headerTable.getColumn(0).getCellRenderer();
    }

    /**
     * Returns the rectangle containing the header tile at <code>row</code>.
     * When the <code>row</code> parameter is out of bounds this method uses
     * the same conventions as the <code>JTable</code> method
     * <code>getCellRect</code>.
     * 
     * @return the rectangle containing the header tile at <code>row</code>
     * @see JTable#getCellRect
     */
    public Rectangle getHeaderRect(int row) {
        return headerTable.getCellRect(row, 0, true);
    }

    /**
     * @param table
     *            the table to set
     */
    public void setTable(JTable table) {
        this.table = table;
        headerTable.setModel(table.getModel());
        headerTable.setSelectionModel(table.getSelectionModel());
    }

    /**
     * @return the table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * {@inheritDoc}
     */
    public String getToolTipText(MouseEvent event) {
        return headerTable.getToolTipText(event);
    }

    /**
     * Returns the index of the row that <code>point</code> lies in, or -1 if
     * the result is not in the range [0, <code>getRowCount()</code>-1].
     * 
     * @param point
     *            the location of interest
     * @return the index of the row that <code>point</code> lies in, or -1 if
     *         the result is not in the range [0, <code>getRowCount()</code>-1]
     * @see JTable#columnAtPoint
     */
    public int rowAtPoint(Point point) {
        return headerTable.rowAtPoint(point);
    }

    /**
     * Returns the row name for this row.
     * <p>
     * This implementation returns the row as a counting number ({@code row + 1}).
     * 
     * @param row
     *            the row in the view being required.
     * @return the name of the row at position {@code row} in the view where the
     *         first row is row 0.
     */
    public String getRowName(int row) {
        return Integer.toString(row + 1);
    }
    //
    // public AccessibleContext getAccessibleContext() {
    // if (accessibleContext == null) {
    // accessibleContext = new AccessibleJXRowTableHeader();
    // }
    //
    // return accessibleContext;
    // }
    //
    // protected class AccessibleJXRowTableHeader extends AccessibleJComponent {
    // }
}
