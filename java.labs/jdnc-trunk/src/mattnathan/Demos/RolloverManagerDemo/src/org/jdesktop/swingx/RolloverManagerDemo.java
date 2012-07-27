/*
 * $Id: RolloverManagerDemo.java 2743 2008-10-08 14:17:26Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import org.jdesktop.swingx.rollover.DefaultRolloverListCellRenderer;
import org.jdesktop.swingx.rollover.DefaultRolloverTreeCellRenderer;

public class RolloverManagerDemo extends JPanel {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("RolloverManagerTest Demo", new RolloverManagerDemo()).setVisible(true);
    }





    public RolloverManagerDemo() {
        try {
            jbInit();
        } catch (Exception ex) {
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
        }
    }





    private void jbInit() throws Exception {
        for (int i = 1; i <= 15; i++) {
            listModel.addElement("Item " + i);
        }

        RolloverManager.install(tree, true);

        RolloverManager.install(list, true); // install rollover effects for a list

        RolloverModel hyperlinkModel = RolloverManager.install(hyperlink, true);
        hyperlinkModel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(RolloverManagerDemo.this,
                                              "You just invoked an action by clicking on a standard JLabel!\n\nClever you :o)",
                                              "actionPerformed Invoked", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.setLayout(borderLayout1);
        this.setOpaque(false);
        contents.setOpaque(false);
        contents.setLayout(flowLayout1);
        hyperlink.setText("JLabel");
        list.setCellRenderer(listCellRenderer);
        list.setModel(listModel);
        flowLayout1.setHgap(30);
        treeScrollPane.setPreferredSize(new Dimension(200, 200));
        listScrollPane.setPreferredSize(new Dimension(200, 200));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Components Focusable");
        jCheckBox1.addActionListener(new RolloverManagerTest_jCheckBox1_actionAdapter(this));
        controls.setLayout(flowLayout2);
        flowLayout2.setAlignment(FlowLayout.LEFT);
        paintFocus.setSelected(true);
        paintFocus.setText("Paint Focus");
        paintFocus.addActionListener(new RolloverManagerTest_paintFocus_actionAdapter(this));
        this.add(contents, java.awt.BorderLayout.CENTER);
        contents.add(hyperlink);
        contents.add(listScrollPane);
        contents.add(treeScrollPane);
        treeScrollPane.getViewport().add(tree);
        listScrollPane.getViewport().add(list);
        this.add(controls, java.awt.BorderLayout.SOUTH);
        controls.add(jCheckBox1);
        controls.add(paintFocus);
        tree.setCellRenderer(treeCellRenderer);

    }





    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel contents = new JPanel();
    private JLabel hyperlink = new JLabel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            RolloverModel m = RolloverManager.getRolloverModel(this);
            if (m.isRollover()) {
                g.setColor(getForeground());
                g.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
            }
        }
    };
    private ListCellRenderer listCellRenderer = DefaultRolloverListCellRenderer.VISTA_LIST_CELL_RENDERER;
    private TreeCellRenderer treeCellRenderer = DefaultRolloverTreeCellRenderer.VISTA_TREE_CELL_RENDERER;

    private JScrollPane listScrollPane = new JScrollPane();
    private JList list = new JList();
    private DefaultListModel listModel = new DefaultListModel();
    private FlowLayout flowLayout1 = new FlowLayout();
    private JScrollPane treeScrollPane = new JScrollPane();
    private JTree tree = new JTree();
    private JCheckBox jCheckBox1 = new JCheckBox();
    private JPanel controls = new JPanel();
    private FlowLayout flowLayout2 = new FlowLayout();
    private JCheckBox paintFocus = new JCheckBox();
    public void jCheckBox1_actionPerformed(ActionEvent e) {
        list.setFocusable(jCheckBox1.isSelected());
        tree.setFocusable(jCheckBox1.isSelected());
    }





    public void paintFocus_actionPerformed(ActionEvent e) {
        Boolean b = paintFocus.isSelected();
        list.putClientProperty(DefaultRolloverListCellRenderer.PAINT_FOCUS_PROPERTY, b);
        tree.putClientProperty(DefaultRolloverTreeCellRenderer.PAINT_FOCUS_PROPERTY, b);
    }
}







class RolloverManagerTest_paintFocus_actionAdapter implements ActionListener {
    private RolloverManagerDemo adaptee;
    RolloverManagerTest_paintFocus_actionAdapter(RolloverManagerDemo adaptee) {
        this.adaptee = adaptee;
    }





    public void actionPerformed(ActionEvent e) {
        adaptee.paintFocus_actionPerformed(e);
    }
}







class RolloverManagerTest_jCheckBox1_actionAdapter implements ActionListener {
    private RolloverManagerDemo adaptee;
    RolloverManagerTest_jCheckBox1_actionAdapter(RolloverManagerDemo adaptee) {
        this.adaptee = adaptee;
    }





    public void actionPerformed(ActionEvent e) {
        adaptee.jCheckBox1_actionPerformed(e);
    }
}
