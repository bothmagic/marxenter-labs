package org.jdesktop.jdnc.incubator.jxcombobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * <p>
 * This is an enhanced combobox that can display multiple columns inside
 * its popup menu.
 * </p>
 * <p>
 * One of the columns is binded to the combobox.
 * </p>
 * <p>
 * Usage example:
 * </p>
 * <code>
 * String[] columnNames = {"Name", "Age"};<br/>
 * Object[][] data = { {"Sergi", new Integer(12)}, {"Alison", new Integer(32)}, {"Kathy", new Integer(75)} };<br/>
 * DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);<br/>
 * JTableComboBox tableComboBox=null;<br/>
 * try {<br/>
 * &nbsp;&nbsp;tableComboBox = new JTableComboBox(tableModel, 0);<br/>
 * } catch (IncompatibleLookAndFeelException e) {<br/>
 * &nbsp;&nbsp;[...]<br/>
 * }<br/>
 * </code>
 * <p><i>
 * You need to take care that the column that is binded to the combobox does not
 * contain duplicates; the following needs to be true for all <code>i!=j</code>:
 * </i></p>
 * <code>tableModel.getValueAt(i, comboBoxColumnIndex)!=tableModel.getValueAt(j, comboBoxColumnIndex)</code>
 * @see JComponentComboBox
 * @author Thomas Bierhance
 */
public class JTableComboBox extends JComponentComboBox {
    /**
     * the table that is displayed inside the popup menu
     */
    private ComboBoxTable table;
    
    public JTableComboBox() throws IncompatibleLookAndFeelException {
        super();
        
        table = new ComboBoxTable();
        
        // the popup menu already paints a border => table without border
        table.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        
        // allow single row selection only
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // hide the popup when the user clicked on the table (and therefore selected a row)
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                hidePopup();
                table.mouseOverHandler.column=-1;
                table.mouseOverHandler.row=-1;
            }
        });
        
        // use a scrollable view for the table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        
        setPopupComponent(scrollPane);
    }
    
    /**
     * Creates a new JTableComboBox object.
     * @param tableModel the model that will be used for the
     * table inside the popup menu
     * @param comboBoxColumnIndex the index of the column
     * that will be binded to
     * the combobox
     * @throws IncompatibleLookAndFeelException in case the popup component
     * could not be accessed
     */
    public JTableComboBox(TableModel tableModel, final int comboBoxColumnIndex) throws IncompatibleLookAndFeelException {
        this();
        setModel(tableModel, comboBoxColumnIndex);        
    }
    
    public void setModel(TableModel tableModel, final int comboBoxColumnIndex) {
        // create the model that is shared by the combobox and the table
        TableComboBoxModel tableComboBoxModel = new TableComboBoxModel(tableModel, comboBoxColumnIndex, table.getSelectionModel());
        table.setModel(tableComboBoxModel);
        this.setModel(tableComboBoxModel);
        
        // This listener scrolls to the selected row when the selection changes (and the popup is visible)
        tableComboBoxModel.addListDataListener(new javax.swing.event.ListDataListener() {
            public void contentsChanged(javax.swing.event.ListDataEvent event) {
                if (isPopupVisible()) {
                    table.scrollRectToVisible(table.getCellRect(getSelectedIndex(), comboBoxColumnIndex, false));
                }
            }
            public void intervalRemoved(javax.swing.event.ListDataEvent event) {}
            public void intervalAdded(javax.swing.event.ListDataEvent event) {}
        });
        
        resizeTable();
    }
    
    public void setShowTableHeaders(boolean showHeaders) {
        table.setShowHeaders(showHeaders);
    }
    
    public void setShowTableGrid(boolean showGrid) {
        table.setShowGrid(showGrid);
    }
    
    protected void resizeTable() {
        if (table!=null) table.resize(3, getScrollBarWidth(), getMaximumRowCount());
    }
    
    /**
     * Returns the width of the vertical scrollbar or <code>0</code> if the scrollbar should not be visible.
     * @return the width of the scrollbar
     */
    protected int getScrollBarWidth() {
        JScrollPane scrollPane = ((JScrollPane) getPopupComponent());
        if (scrollPane==null) return 0;
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        javax.swing.plaf.ComponentUI scrollBarUI = scrollBar.getUI();
        int width;
        // the scrollbar should be visible only if the model contains more
        // rows than those that are displayed
        if (this.getMaximumRowCount()<table.getRowCount()) {
            width = scrollBarUI.getPreferredSize(scrollBar).width;
        } else {
            width = 0;
        }
        return width;
    }
    
    public void setMaximumRowCount(int count) {
        super.setMaximumRowCount(count);
        // resize is necessary as the maximum row count
        // is part of the size calculations
        resizeTable();
    }
    
    /**
     * Returns the table that is displayed
     * inside the popup menu
     * @return the table inside the popup menu
     */
    public JTable getPopupTable() {
        return table;
    }
    
    public void updateUI() {
        super.updateUI();
        // renderers might have changed so recalculate all sizes
        resizeTable();
    }
}