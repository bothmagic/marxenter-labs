/*
 * Created on 23.03.2010
 *
 */
package org.jdesktop.swingx.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;

public class TableSizeLastColumn {
    
    public static void main(String[] args) {
        JXTable table = new JXTable();

        table.setAutoResizeMode(JXTable.AUTO_RESIZE_LAST_COLUMN);

        table.setColumnFactory(new ColumnFactory() {
            @Override
            public void configureColumnWidths(JXTable table,
                    TableColumnExt columnExt) {
                super.configureColumnWidths(table, columnExt);

                switch (columnExt.getModelIndex()) {
                case 0:
                case 1:
                    columnExt.setPreferredWidth(100);
                    break;
                case 2:
                    columnExt.setPreferredWidth(200);
                }
            }
        });

//        table.setAutoResizeMode(JXTable.AUTO_RESIZE_OFF);
//        table.setHorizontalScrollEnabled(true);

        table.setColumnFactory(new ColumnFactory() {
            @Override
            public void configureColumnWidths(JXTable table, TableColumnExt columnExt) {
                super.configureColumnWidths(table, columnExt);

                switch (columnExt.getModelIndex()) {
                    case 0:
                    case 1:
                        columnExt.setPreferredWidth(100);
                        columnExt.setMaxWidth(Short.MAX_VALUE);  // <-- this makes it work but I'm not sure why.
                        break;
                    case 2:
                        columnExt.setPreferredWidth(200);
                }
            }
        });

        Object[][] rowData = { { 1, 2, 3, }, { 4, 5, 6, } };
        Object[] columnNames = { 'A', 'B', 'C' };
        table.setModel(new DefaultTableModel(rowData, columnNames));

        JTable core = new JTable();
        core.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        core.setModel(new DefaultTableModel(rowData, columnNames));
        for (int i = 0; i < core.getColumnCount(); i++) {
            TableColumn column = core.getColumnModel().getColumn(i);
            if (column.getModelIndex() < 2) {
                column.setPreferredWidth(100);
            } else {
                column.setPreferredWidth(200);
            }
        }
        
        JScrollPane tableScroll = new JScrollPane(table);

        JFrame frame = new JFrame();
        frame.add(tableScroll, BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}