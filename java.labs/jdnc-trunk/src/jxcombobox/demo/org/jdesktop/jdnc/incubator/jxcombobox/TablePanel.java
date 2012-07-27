/*
 * ComboBoxTableDemo.java
 *
 * Created on 14. März 2005, 09:26
 */

package org.jdesktop.jdnc.incubator.jxcombobox;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;


/**
 *
 * @author Thomas
 */
public class TablePanel extends javax.swing.JPanel {
    
    /** Creates a new instance of ComboBoxTableDemo */
    public TablePanel() {
        super();
        setLayout(new java.awt.BorderLayout());
        add(getTablePanel(),java.awt.BorderLayout.CENTER);
        add(getControlPanel(),java.awt.BorderLayout.SOUTH);
    }
    
    ComboBoxTable table;
    
    private void createTable() {
        String[] columnNames = {"First Name", "Last Name", "Sport", "# of Years", "Vegetarian"};
        Object[][] data = {
            {"Mary", "Campione", "Snowboarding", new Integer(5), Boolean.FALSE},
            {"Alison", "Huml", "Rowing", new Integer(3), Boolean.TRUE},
            {"Kathy", "Walrath", "Knitting", new Integer(2), Boolean.FALSE},
            {"Sharon", "Zakhour", "Speed reading", new Integer(20), Boolean.TRUE},
            {"Philip", "Milne", "Pool", new Integer(10), Boolean.FALSE},
            {"Mike", "Summers", "Tennis", new Integer(1), Boolean.TRUE},
            {"Merissa", "Atwood", "Golf", new Integer(14), Boolean.FALSE},
        };
        table = new ComboBoxTable();
        table.setModel(new DefaultTableModel(data, columnNames));
    }
    
    private JPanel getTablePanel() {
        createTable();
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(new EmptyBorder(20,20,20,20));
        tablePanel.setLayout(new java.awt.GridLayout(1,1));
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        return tablePanel;
    }
    
    private JPanel getControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(20,20,20,20));
        controlPanel.setLayout(new java.awt.GridLayout(4,2,5,5));
        
        controlPanel.add(new JLabel("mouse-over effect:"));
        JCheckBox mouseOverCheckBox = new JCheckBox();
        mouseOverCheckBox.setSelected(table.isMouseOverActive());
        mouseOverCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                table.setMouseOverActive(cb.isSelected());
            }
        });
        controlPanel.add(mouseOverCheckBox);
        
        controlPanel.add(new JLabel("column selection allowed:"));
        JCheckBox columnCheckBox = new JCheckBox();
        columnCheckBox.setSelected(table.getColumnSelectionAllowed());
        columnCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                table.setColumnSelectionAllowed(cb.isSelected());
            }
        });
        controlPanel.add(columnCheckBox);
        
        controlPanel.add(new JLabel("row selection allowed:"));
        JCheckBox rowCheckBox = new JCheckBox();
        rowCheckBox.setSelected(table.getRowSelectionAllowed());
        rowCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                table.setRowSelectionAllowed(cb.isSelected());
            }
        });
        controlPanel.add(rowCheckBox);
        
        controlPanel.add(new JLabel("selection mode:"));
        JComboBox modeComboBox = new JComboBox(new String[] {"single", "single interval", "multiple interval"});
        modeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                if (cb.getSelectedItem().equals("single")) {
                    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                } else if (cb.getSelectedItem().equals("single interval")) {
                    table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                } else if (cb.getSelectedItem().equals("multiple interval")) {
                    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                } 
            }
        });
        controlPanel.add(modeComboBox);

        return controlPanel;
    }
}
