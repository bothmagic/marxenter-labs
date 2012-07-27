package org.jdesktop.swingx.incubator.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 29-Mar-2004
 * Time: 14:13:15
 */

@SuppressWarnings("unchecked")
public class SimpleTableModel extends javax.swing.table.AbstractTableModel {
    private List values;
    private boolean editable;

    public SimpleTableModel(Collection values) {
        this.values = new ArrayList(values);
    }

    public Collection getValues() {
        return Collections.unmodifiableList(this.values);
    }

    public void setValues(Collection values) {
        this.values = new ArrayList(values);
        fireTableDataChanged();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getColumnCount() {
        return 1;
    }

    public int getRowCount() {
        return values.size();
    }

    public String getColumnName(int col) {
        return "";
    }

    public Object getValueAt(int row, int col) {
        return values.get(col);
    }

    public void setValueAt(Object value, int row, int col) {
        if (isEditable()) {
            values.set(col, value);
            fireTableDataChanged();
        }
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        return isEditable();
    }
}