/*
 * $Id: ValidatingCellEditorTableTest.java 3182 2009-07-01 20:01:16Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.table.ValidatingTableColumnExt;
import org.jdesktop.swingx.validator.IntegerRangeValidator;

/**
 *
 */
public class ValidatingCellEditorTableTest extends JFrame {
    /**
     * {@inheritDoc}
     */
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        DefaultTableModel model = new DefaultTableModel(3, 2) {
            public Class<?> getColumnClass(int columnIndex) {
                return Integer.class;
            }
        };
        model.setValueAt(1, 0, 0);
        model.setValueAt(2, 0, 1);
        model.setValueAt(3, 1, 0);
        model.setValueAt(4, 1, 1);
        model.setValueAt(5, 2, 0);
        model.setValueAt(6, 2, 1);
        
        JTable table = new ValidatingTable(model);
//        table.setDefaultEditor(Integer.class, new LoggingNumberEditorExt());
        ((ValidatingTableColumnExt) table.getColumnModel().getColumn(0))
                .setValidator(new IntegerRangeValidator(3, 8));
        
        add(new JScrollPane(table));
        
        pack();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ValidatingCellEditorTableTest().setVisible(true);
            }
        });
    }

}
