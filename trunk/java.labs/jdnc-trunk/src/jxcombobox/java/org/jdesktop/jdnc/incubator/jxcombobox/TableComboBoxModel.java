package org.jdesktop.jdnc.incubator.jxcombobox;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 * <p>
 * This model is combines a {@link javax.swing.table.TableModel TableModel},
 * a {@link javax.swing.ListSelectionModel ListSelectionModel}
 * and a {@link javax.swing.ComboBoxModel ComboBoxModel} into a synchronized model.
 * When shared by a {@link javax.swing.JComboBox JComboBox}
 * and a {@link javax.swing.JTable JTable} content and selection of the two will be
 * synchronized.
 * </p>
 * <p>
 * As a basis, the user needs to provide a TableModel, a ListSelectionModel and
 * the index of the column that should be used for the combobox. TableComboBoxModel
 * then merely passes nearly all messages on to the TableModel and on to the ListSelectionModel.
 * </p>
 * @author Thomas Bierhance
 */
class TableComboBoxModel implements TableModel, ComboBoxModel {
    TableModel tableModel;
    int comboBoxColumnIndex;
    ListSelectionModel listSelectionModel;
    
    public TableComboBoxModel(TableModel tableModel, int comboBoxColumnIndex, ListSelectionModel listSelectionModel) {
        this.tableModel = tableModel;
        this.comboBoxColumnIndex = comboBoxColumnIndex;
        this.listSelectionModel = listSelectionModel;
        // synchronize the combobox when a new tablerow is selected
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                selectedValueChanged(event.getSource());
            }
        });
    }
    
    private void selectedValueChanged(Object source) {
        fireItemStateChanged(new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, -1, -1));
    }
    
    // TableModel-------------------------------------
    public int getColumnCount() {
        return tableModel.getColumnCount();
    }
    
    public int getRowCount() {
        return tableModel.getRowCount();
    }
    
    public String getColumnName(int columnIndex) {
        return tableModel.getColumnName(columnIndex);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableModel.getValueAt(rowIndex, columnIndex);
    }

    public void addTableModelListener(javax.swing.event.TableModelListener tableModelListener) {
        tableModel.addTableModelListener(tableModelListener);
    }

    public Class getColumnClass(int columnIndex) {
        return tableModel.getColumnClass(columnIndex);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return tableModel.isCellEditable(rowIndex, columnIndex);
    }

    public void removeTableModelListener(javax.swing.event.TableModelListener tableModelListener) {
        tableModel.removeTableModelListener(tableModelListener);
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        tableModel.setValueAt(value, rowIndex, columnIndex);
    }

    // ComboBoxModel-------------------------------------
    public Object getElementAt(int index) {
        return getValueAt(index, comboBoxColumnIndex);
    }

    public Object getSelectedItem() {
        if (listSelectionModel.getLeadSelectionIndex() >= 0) {
            return getValueAt(listSelectionModel.getLeadSelectionIndex(), comboBoxColumnIndex);
        } else {
            return null;
        }
    }

    public int getSize() {
        return getRowCount();
    }

    // 1.5 List<ListDataListener> listDataListeners = new ArrayList<ListDataListener>();
    List listDataListeners = new ArrayList(); // 1.4
    
    private void fireItemStateChanged(ListDataEvent event) {
        // 1.5 for (ListDataListener listDataListener : listDataListeners) {
        for(java.util.Iterator i=listDataListeners.iterator();i.hasNext();) { // 1.4
            ListDataListener listDataListener=(ListDataListener)i.next(); // 1.4
            listDataListener.contentsChanged(event);
        }
    }

    public void removeListDataListener(ListDataListener listDataListener) {
        listDataListeners.remove(listDataListener);
    }
    
    public void addListDataListener(ListDataListener listDataListener) {
        listDataListeners.add(listDataListener);
    }
    
    public void setSelectedItem(Object item) {
        // find the row that is corresponding to the selected item and synchronize
        for (int rowIndex=0, rowCount=getRowCount(); rowIndex<rowCount; rowIndex++) {
            if (getValueAt(rowIndex, comboBoxColumnIndex) == item) {
                listSelectionModel.setSelectionInterval(rowIndex, rowIndex);
                selectedValueChanged(this);
            }
        }
    }
}