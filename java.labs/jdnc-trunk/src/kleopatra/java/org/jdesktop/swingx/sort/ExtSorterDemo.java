/*
 * Created on 23.03.2010
 *
 */
package org.jdesktop.swingx.sort;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * Demo of extended functionality of BasicRowSorter:
 * 
 * 1) Sorting null: Collator doesn't support comparing nulls, 
 * so I would go with adding a flag to setComparator if it should always be used.
 * 2) Fixed start/end rows: never filtered and some extra checks in compare(int, int).
 * Provided by Walterln in sun dev forum thread:
 * 
 * http://forums.sun.com/thread.jspa?messageID=10935442#10935442
  */
public class ExtSorterDemo {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                
                TableModel model = new AbstractTableModel() {
 
                    public Object getValueAt(int rowIndex, int columnIndex) {
                        if(rowIndex == columnIndex) {
                            return null;
                        }
                        else {
                            return rowIndex * columnIndex;
                        }
                    }
 
                    public int getRowCount() {
                        return 11;
                    }
 
                    public int getColumnCount() {
                        return 4;
                    }
                    
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return Integer.class;
                    }
                };
                JTable table = new JTable(model);
                table.setAutoCreateRowSorter(false);
                BasicTableRowSorter sorter = new BasicTableRowSorter(model);
                sorter.setFixedStartRows(1);
                sorter.setFixedEndRows(2);
                sorter.setComparator(1, new Comparator<Integer>() {
                    public int compare(Integer o1, Integer o2) {
                        // sort null last
                        if(o1 == o2) {
                            return 0;
                        }
                        else if(o1 == null) {
                            return 1;
                        }
                        else if(o2 == null) {
                            return -1;
                        }
                        else {
                            return o1.compareTo(o2);
                        }
                    }
                }, true);
                table.setRowSorter(sorter);
 
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.getContentPane().add(new JScrollPane(table));
                JTextArea area = new JTextArea("First and last two rows are fixed.\nColumn B sorts null last instead of first.");
                area.setEditable(false);
                frame.getContentPane().add(area, BorderLayout.PAGE_END);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
