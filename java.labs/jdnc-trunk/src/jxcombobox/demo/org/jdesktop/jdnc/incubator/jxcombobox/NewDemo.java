/*
 * NewDemo.java
 *
 * Created on 15. März 2005, 19:08
 */

package org.jdesktop.jdnc.incubator.jxcombobox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.*;
import org.jdesktop.jdnc.incubator.jxcombobox.colorcombo.ColorComboBox;
import org.jdesktop.jdnc.incubator.jxcombobox.smiliescombo.SmilieComboBox;

/**
 *
 * @author Thomas
 */
public class NewDemo {
    
    /** Creates a new instance of NewDemo */
    public NewDemo() {
    }
    
    public static void createGui(java.awt.Container container) {
        container.setLayout(new java.awt.BorderLayout());
        container.add(getLnFPanel(container),java.awt.BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("Multiple columns", getComboBoxPanel());
        tabbedPane.add("Mouse-over effect", new TablePanel());
        tabbedPane.add("Ricardo special", getSpecialPanel());
        container.add(tabbedPane,java.awt.BorderLayout.CENTER);
    }
    
    private static JPanel getSpecialPanel() {
        JPanel sPanel = new JPanel();
        sPanel.setBorder(new EmptyBorder(20,20,20,20));
        sPanel.setLayout(new java.awt.FlowLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(4,2,5,5));
        try {
            panel.add(new JLabel("SmilieComboBox:"));
            panel.add(new SmilieComboBox());
            panel.add(new JLabel("ColorComboBox:"));
            ColorComboBox colorBox = new ColorComboBox();
            // When hitting the button, a focus lost event happens on the combobox.
            // Therefore BasicComboBoxUI.Handler.focusLost hides the popup if the popup is lightweight.
            colorBox.setLightWeightPopupEnabled(false);
            /*colorBox.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuCanceled(PopupMenuEvent e) {
                    System.out.println("Popup canceled");
                }
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    System.out.println("Popup will be hidden");
                    //new Throwable().printStackTrace();
                }
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    System.out.println("Popup will be shown - source: " + e.getSource());
                    final JComponentComboBox comp = ((JComponentComboBox) e.getSource());
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            final Window window = SwingUtilities.windowForComponent(comp.getPopupComponent());
                            if (window != null) {
                                System.out.println("window focusable: " + window.getFocusableWindowState());
                                window.setFocusableWindowState(true);
                            }
                        }
                    });
                }
            });*/
            panel.add(colorBox);
            
            panel.add(new JLabel("TextFieldBox:"));
            panel.add(getTextFieldBox());
            
            panel.add(new JLabel("This panel in a new frame:"));
            JButton button = new JButton("open new frame");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                    frame.add(getSpecialPanel());
                    frame.pack();
                    frame.setVisible(true);
                }
            });
            panel.add(button);
        } catch (Exception e) {
            
        }
        
        sPanel.add(panel);
        
        return sPanel;
    }
    
    private static JComboBox getTextFieldBox() throws IncompatibleLookAndFeelException {
        JComponentComboBox cb = new JComponentComboBox();
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2,1,5,5));
        p.setBorder(new EmptyBorder(5,5,5,5));
        final JTextField textField = new JTextField("Textfields don't work in all situations.");
        p.add(textField);
        JButton button = new JButton("clear textfield");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });
        p.add(button);
        cb.setPopupComponent(p);
        cb.setLightWeightPopupEnabled(false);
        /*cb.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuCanceled(PopupMenuEvent e) {
                    System.out.println("Popup canceled");
                }
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    System.out.println("Popup will be hidden");
                    //new Throwable().printStackTrace();
                }
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                    System.out.println("Popup will be shown");
                    System.out.println("Popup will be shown - source: " + e.getSource());
                    final JComponentComboBox comp = ((JComponentComboBox) e.getSource());
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            final Window window = SwingUtilities.windowForComponent(comp.getPopupComponent());
                            if (window != null) {
                                System.out.println("window focusable: " + window.getFocusableWindowState());
                                window.setFocusableWindowState(true);
                            }
                        }
                    });
                }
            });*/
        return cb;
    }
    
    private static JPanel getComboBoxPanel() {
        JPanel sPanel = new JPanel();
        sPanel.setBorder(new EmptyBorder(20,20,20,20));
        sPanel.setLayout(new java.awt.FlowLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridLayout(4,2,5,5));
        panel.add(new JLabel("Standard JComboBox:"));
        panel.add(new JComboBox(new String[]{"Campione", "Huml", "Walrath", "Zakhour", "Milne", "Summers", "Atwood"}));
        
        panel.add(new JLabel("Table popup:"));
        final JTableComboBox comboBox = getTableComboBox();
        panel.add(comboBox);
        
        panel.add(new JLabel("Show grid:"));
        JCheckBox showGridCheckBox = new JCheckBox();
        showGridCheckBox.setSelected(true);
        showGridCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                comboBox.setShowTableGrid(cb.isSelected());
            }
        });
        panel.add(showGridCheckBox);
        
        panel.add(new JLabel("Show headers:"));
        JCheckBox showHeadersCheckBox = new JCheckBox();
        showHeadersCheckBox.setSelected(true);
        showHeadersCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                comboBox.setShowTableHeaders(cb.isSelected());
            }
        });
        panel.add(showHeadersCheckBox);
        
        sPanel.add(panel);
        
        return sPanel;
    }
    
    // A combobox containing a JTable in the custom popup
    private static JTableComboBox getTableComboBox() {
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
        // MyTableModel only overrides getColumnClass to demonstrate JCheckBox rendering
        TableModel tableModel = new MyTableModel(data, columnNames);
        
        JTableComboBox tableComboBox=null;
        try {
            tableComboBox = new JTableComboBox(tableModel, 1);
        } catch (IncompatibleLookAndFeelException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tableComboBox;
    }
    
    // JTable would not render Boolean values using JCheckBox with the DefaultTableModel
    static class MyTableModel extends DefaultTableModel {
        public MyTableModel(Object[][] data, Object[] columnNames) {
            super(data,columnNames);
        }
        public Class getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }
        public boolean isCellEditable(int row, int columnIndex) {
            return false;
        }
    }
    
    public static JPanel getLnFPanel(java.awt.Container container) {
        JPanel lnfPanel = new JPanel();
        lnfPanel.setLayout(new java.awt.FlowLayout());
        lnfPanel.add(new JLabel("Look&Feel:"));
        lnfPanel.add(getLookAndFeelComboBox(container));
        return lnfPanel;
    }
    
    // a combobox that let the user choose a look&feel
    private static JComboBox getLookAndFeelComboBox(final java.awt.Container container) {
        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        //final HashMap<String, String> lafName2ClassName = new HashMap<String, String>();
        final HashMap lafName2ClassName = new HashMap();
        
        for(int i=0,n=lafInfo.length;i<n;i++) {
            lafName2ClassName.put(lafInfo[i].getName(), lafInfo[i].getClassName());
        }
        
        try {
            Class.forName("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            lafName2ClassName.put("JGoodies Plastic 3D","com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (ClassNotFoundException e) {}
        
        try {
            Class.forName("com.jgoodies.looks.windows.WindowsLookAndFeel");
            lafName2ClassName.put("JGoodies Windows","com.jgoodies.looks.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {}
        
        try {
            Class.forName("com.incors.plaf.alloy.AlloyLookAndFeel");
            lafName2ClassName.put("Alloy","com.incors.plaf.alloy.AlloyLookAndFeel");
        } catch (ClassNotFoundException e) {}
        
        try {
            Class.forName("com.pagosoft.plaf.PgsLookAndFeel");
            lafName2ClassName.put("PGS","com.pagosoft.plaf.PgsLookAndFeel");
        } catch (ClassNotFoundException e) {}
        
        final JComboBox lafComboBox = new JComboBox(lafName2ClassName.keySet().toArray());
        lafComboBox.setSelectedItem(UIManager.getLookAndFeel().getName());
        
        lafComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                // Avoid NPE from MetalScrollPaneUI.uninstallUI
                // Workaround for http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5096948
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        String lafName = (String)lafComboBox.getSelectedItem();
                        // 1.5 String lafClassName = lafName2ClassName.get(lafName);
                        String lafClassName = (String) lafName2ClassName.get(lafName);
                        try {
                            UIManager.setLookAndFeel(lafClassName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SwingUtilities.updateComponentTreeUI(container);
                        container.validate();
                    }
                });
            }
        });
        
        return lafComboBox;
    }
    
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(3);
                createGui(frame);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    
}
