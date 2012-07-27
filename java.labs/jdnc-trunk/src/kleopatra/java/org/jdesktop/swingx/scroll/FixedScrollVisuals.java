/*
 * Created on 29.06.2007
 *
 */
package org.jdesktop.swingx.scroll;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;


public class FixedScrollVisuals {

    
    private Component getContent() {
        JWTable table = new JWTable(createTableModel(20, 7));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setVisibleRowCount(10);
        table.setColumnControlVisible(true);
        return new JXScrollPane(table);
    }
    
    private TableModel createTableModel(final int rows, final int columns) {
        DefaultTableModel model = new DefaultTableModel(rows, columns);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                model.setValueAt("r/c " + row + "/" + column, row, column);
            }
        }
        return model;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JXFrame frame = new JXFrame("Early experiments: fixed columns",
                        true);
                frame.add(new FixedScrollVisuals().getContent());
                frame.pack();
                frame.setVisible(true);
            }
        });        
    }

}
