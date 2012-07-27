/*
 * $Id: DefaultTableModelExt.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.table;

import java.util.List;
import java.util.Vector;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;

/**
 * Adds metadata support to table model.
 *
 * @author Ramesh Gupta
 */
public class DefaultTableModelExt extends javax.swing.table.DefaultTableModel {

    public DefaultTableModelExt() {
    }

    public DefaultTableModelExt(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    public DefaultTableModelExt(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public DefaultTableModelExt(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    public DefaultTableModelExt(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    public DefaultTableModelExt(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public Class getColumnClass(int columnIndex) {
        Class	klass = Object.class;
        if (metadata == null) {		// for backwards compatibility
            klass = inferColumnClass(columnIndex);
        }
        else {
            /** @todo get column class from metadata,
             * mapping from SQL to Java as necessary. */
        }
        return klass;
    }

    /**
     * Infers column class from the object in the specified column of the first
     * row. If that cell is empty, this method returns <code>Object.class</code>
     * as the column class.
     *
     * @param columnIndex zero-based index of a table column
     * @return the class of the object in the specified column of the first row,
     * 		or Object.class if the cell is empty
     */
    protected Class inferColumnClass(int columnIndex) {
        Vector	row = (Vector) dataVector.elementAt(0);	// first row only

        if (row == null) {
            return Object.class;
        }
        else {
            final Object cell = row.get(columnIndex);
            return (cell == null) ? Object.class : cell.getClass();
        }
    }

    /**
     * Determines if the specified cell is editable or not. Returns false if it
     * can determine that the entire specified column is uneditable. Otherwise,
     * it defers the decision to the superclass.
     *
     * @param row zero-based row index
     * @param columnIndex zero-based column index
     * @return true if the cell is editable; false otherwise
     */
    public boolean isCellEditable(int row, int columnIndex) {
        boolean editable = true;
        /** @todo support isEditable() in TableColumnExt
        if (metadata != null) {
            // check if the entire column is read-only. If so, we can just
            // return false without checking if the individual cell is editable.
        }
		*/

		// Column may have writable cells. Check editability on specific cell.
        if (editable) {
            editable = super.isCellEditable(row, columnIndex);
        }
        return editable;
    }

    /**
     * Temporary placeholder. Integrate with real metadata support.
     */
    public Object getMetadata() {
        return metadata;
    }

    protected	Object	metadata = null;	/** @todo Integrate with Amy's metadata */
}
