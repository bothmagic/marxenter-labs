package org.jdesktop.swingx;

/*
 * |-------------------------------------------------------------------|
 * |                               Item                                |
 * |                                                                   |
 * |-------------------------------------------------------------------|
 * |       |           Identifiers          |                          |
 * |  Dept |--------------------------------|       Description        |
 * |       |  Id    |    UPC    |    SKU    |                          |
 * |-------------------------------------------------------------------|
 * |       |        |           |           |                          |
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Container;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import org.jdesktop.jdnc.JNTable;
import javax.swing.SpringLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.table.ColumnGroup;
import org.jdesktop.swingx.table.MultiLineTableHeader;

public class ColumnGroupDemo extends JFrame {
    
    ColumnGroupDemo() {
        super( "Column Group Header Example" );

        TableColumnModel columnModel = null;
        
/*        DefaultTableModel multidataModel = new DefaultTableModel();
        multidataModel.setDataVector(new Object[][]{
            {"100", "1000001", "378659843212", "843231278659", "Swingx - The Definitive Guide"},
            {"100", "2100010", "461923479231", "938046273451", "JDNC For Dummies"}},
            new Object[]{"Dept", "Item\nId", "Item\nUPC", "Item\nSKU", "Description"});
*/
        DefaultTableModel dataModel = new DefaultTableModel();
        dataModel.setDataVector(new Object[][]{
            {"100", "1000001", "378659843212", "843231278659", "Swingx - The Definitive Guide"},
            {"100", "2100010", "461923479231", "938046273451", "JDNC For Dummies"}},
            new Object[]{"Dept", "Id", "UPC", "SKU", "Description"});

        Container pane = this.getContentPane();
        FlowLayout layout = new FlowLayout();
        pane.setLayout(layout);
    
        JXTable normalTable = new JXTable();
        normalTable.setColumnControlVisible(true);
        normalTable.setModel(dataModel);
        normalTable.setSortable(false);
/*        
        JTable multiTable = new JTable();
        multiTable.setModel(multidataModel);
        MultiLineTableHeader renderer = new MultiLineTableHeader();
        columnModel = multiTable.getColumnModel();
        columnModel.getColumn(1).setHeaderRenderer(renderer);
        columnModel.getColumn(2).setHeaderRenderer(renderer);
        columnModel.getColumn(3).setHeaderRenderer(renderer);
*/        
        JXTable table = new JXTable();
        table.setColumnControlVisible(true);
        table.setModel(dataModel);
        table.setSortable(false);
        
        columnModel = table.getColumnModel();
        ColumnGroup group = new ColumnGroup( "Description");
        group.add(columnModel.getColumn(0));
        ColumnGroup subGroup = new ColumnGroup("Identifiers");
        group.add(subGroup);
        group.add(columnModel.getColumn(4));
        subGroup.add(columnModel.getColumn(1));
        subGroup.add(columnModel.getColumn(2));
        subGroup.add(columnModel.getColumn(3));

        JXGroupableTableHeader header = new JXGroupableTableHeader(columnModel);
        header.addColumnGroup(group);
        header.addColumnGroup(subGroup);
        table.setTableHeader(header);
        
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

        setSize(800, 600);
        Dimension size = getSize();
        size.width -= 25;
        size.height = 100;
        
        JScrollPane scroll = new JScrollPane(normalTable);
        normalTable.setPreferredSize(size);
        scroll.setPreferredSize(size);
        pane.add(scroll);

        JScrollPane scroll2 = new JScrollPane(table);
        size.height = 200;
        table.setPreferredSize(size);
        scroll2.setPreferredSize(size);
        pane.add(scroll2);
        
//        JScrollPane scroll = new JScrollPane(multiTable);
//        pane.add(scroll);
    }
    
    public static void main(String[] args) {
        try {
//            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//            UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
            UIManager.setLookAndFeel("com.birosoft.liquid.LiquidLookAndFeel");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
        ColumnGroupDemo frame = new ColumnGroupDemo();
        frame.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent e ) {
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }
}

