/*
 * Created on 09.09.2008
 *
 */
package org.jdesktop.swingx.table;

import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.InteractiveTestCase;

public class TableHeaderFillerTest extends InteractiveTestCase {

    public void testRemoveColumn() {
        JTable table = new JTable(10, 2);
        TableColumn column = table.getColumnModel().getColumn(0);
        assertEquals(1, column.getPropertyChangeListeners().length);
        table.removeColumn(column);
        assertEquals(0, column.getPropertyChangeListeners().length);
    }

    public void testAddColumn() {
        JTable table = new JTable(10, 2);
        table.addColumn(new TableColumn(0));
        TableColumn column = table.getColumnModel().getColumn(table.getColumnCount() - 1);
        
        assertEquals(1, column.getPropertyChangeListeners().length);
        table.removeColumn(column);
        assertEquals(0, column.getPropertyChangeListeners().length);
    }
    
    public void testRemoveColumnWithFiller() {
        JTable table = new JTable(10, 2);
        TableColumn column = table.getColumnModel().getColumn(0);
        new TableHeaderFiller(table);
        assertEquals(2, column.getPropertyChangeListeners().length);
        table.removeColumn(column);
        assertEquals(0, column.getPropertyChangeListeners().length);
    }

    public void testAddColumnWithFiller() {
        JTable table = new JTable(10, 2);
        new TableHeaderFiller(table);
        table.addColumn(new TableColumn(0));
        TableColumn column = table.getColumnModel().getColumn(table.getColumnCount() - 1);
        
        assertEquals(2, column.getPropertyChangeListeners().length);
        table.removeColumn(column);
        assertEquals(0, column.getPropertyChangeListeners().length);
    }

    public void testSetColumnModelWithFiller() {
        JTable table = new JTable(10, 2);
        new TableHeaderFiller(table);
        TableColumnModel model = new DefaultTableColumnModel();
        model.addColumn(new TableColumn(0));
        model.addColumn(new TableColumn(1));
        table.setColumnModel(model);
        TableColumn column = table.getColumnModel().getColumn(0);
        assertEquals(2, column.getPropertyChangeListeners().length);
        table.removeColumn(column);
        assertEquals(0, column.getPropertyChangeListeners().length);
    }

}
