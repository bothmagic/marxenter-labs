/*
 * $Id: AbstractEnum.java 2997 2009-01-30 13:48:26Z kschaefe $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt2;

/**
 * Creates two tables. The first uses normal headers, the second uses groupable headers.
 * 
 * <pre>
 * |-------------------------------------------------------------------|
 * |                               Item                                |
 * |                                                                   |
 * |-------------------------------------------------------------------|
 * |       |           Identifiers          |                          |
 * |  Dept |--------------------------------|       Description        |
 * |       |  Id    |    UPC    |    SKU    |                          |
 * |-------------------------------------------------------------------|
 * |       |        |           |           |                          |
 * </pre>
 * 
 * @author Karl Schaefer
 * @author evickroy (original demo)
 */
public class ColumnGroupDemo extends JFrame {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void frameInit() {
        super.frameInit();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Column Group Header Example");

        DefaultTableModel dataModel = new DefaultTableModel();
        dataModel.setDataVector(new Object[][] {
            {"100", "1000001", "378659843212", "843231278659", "Swingx - The Definitive Guide"},
            {"100", "2100010", "461923479231", "938046273451", "JDNC For Dummies"},
        }, new Object[] {
                "Dept", "Id", "UPC", "SKU", "Description"
        });

        JXTable normalTable = new JXTable();
        normalTable.setColumnControlVisible(true);
        normalTable.setModel(dataModel);
        normalTable.setSortable(false);
        add(new JScrollPane(normalTable), BorderLayout.NORTH);

        JXTable groupedTable = new JXTable();
        groupedTable.setColumnFactory(new ColumnFactory() {
            /**
             * {@inheritDoc}
             */
            public TableColumnExt2 createTableColumn(int modelIndex) {
                return new TableColumnExt2(modelIndex);
            }
        });
        groupedTable.setColumnControlVisible(true);
        groupedTable.setModel(dataModel);
        groupedTable.setSortable(false);
        add(new JScrollPane(groupedTable));

        TableColumnModel columnModel = groupedTable.getColumnModel();
        TableColumnExt2 group = new TableColumnExt2();
        group.setHeaderValue("Item");
        group.add((TableColumnExt2) columnModel.getColumn(0));
        TableColumnExt2 subGroup = new TableColumnExt2();
        subGroup.setHeaderValue("Identifiers");
        group.add(subGroup);
        group.add((TableColumnExt2) columnModel.getColumn(4));
        subGroup.add((TableColumnExt2) columnModel.getColumn(1));
        subGroup.add((TableColumnExt2) columnModel.getColumn(2));
        subGroup.add((TableColumnExt2) columnModel.getColumn(3));

        JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
        groupedTable.setTableHeader(header);

        TableColumn column = columnModel.getColumn(0);
        column.setMaxWidth(75);
        column = columnModel.getColumn(1);
        column.setMaxWidth(150);
        column = columnModel.getColumn(2);
        column.setPreferredWidth(125);
        column.setMaxWidth(125);
        column = columnModel.getColumn(3);
        column.setPreferredWidth(125);
        column.setMaxWidth(125);

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ColumnGroupDemo().setVisible(true);
            }
        });
    }
}
