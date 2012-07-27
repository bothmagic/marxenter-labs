/*
 * Created on 19.06.2007
 *
 */
package org.jdesktop.swingx.tree;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * Example of the basics of JXTreeTable with the 
 * DefaultTreeTableModel and DefaultMutableTreeTableNodes.
 * Everything in one class to get a simple runnable example.
 * This version is meant to work with what was in CVS on June 11, 2007.
 * 
 * 
 * see https://swingx.dev.java.net/
 * @author Joergen Rapp
 *      @version 1.1
 */
public class ExampleComponentTreeTable {
    private JXTreeTable treeTable;

    public ExampleComponentTreeTable() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            JXErrorPane.showDialog(e);
        }
        ExampleComponentTreeTable test = new ExampleComponentTreeTable();
        test.create();
    }

    /**
     * creates the test app on the event dispatch thread.
     * 
     */
    public void create() {
        Runnable r = new Runnable() {
            public void run() {
                JFrame window = new JFrame("Example of an editable JXTreeTable");
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JToolBar toolbar = new JToolBar();
                window.add(toolbar, BorderLayout.NORTH);
                toolbar.add(new InsertNodeAction());
                toolbar.add(new DeleteNodeAction());

                TreeTableModel personTreeTableModel = generateTestModel();
                treeTable = new JXTreeTable(personTreeTableModel);
                treeTable.setRootVisible(true);
                treeTable.setEditable(true);
                StringValue sv = new StringValue() {

                    public String getString(Object value) {
                        if (value instanceof Component) {
                            return ((Component) value).getName();
                        }
                        return StringValues.TO_STRING.getString(value);
                    }
                    
                };
                TreeCellRenderer renderer = new DefaultTreeRenderer(sv);
                treeTable.setTreeCellRenderer(renderer);
                // add a highlighter, pretty.
                treeTable.addHighlighter(HighlighterFactory
                        .createAlternateStriping());
//                 same in tree
                 JXTree tree = new JXTree(personTreeTableModel) {

                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        if (getRowCount() < 3) return;
                        Rectangle r = getRowBounds(2);
                        g.drawRect(r.x, r.y, r.width, r.height);
                    }
                     
                 };
                 tree.setEditable(true);
                 tree.setCellRenderer(treeTable.getTreeCellRenderer());
                 TreeEditor treeEditor = new TreeEditor();
                 treeEditor.setEditableValue(((ComponentTTModel) personTreeTableModel).getEditableValue());
                tree.setCellEditor(treeEditor);
                 window.add(new JScrollPane(tree));
//                window.add(new JScrollPane(treeTable));
//                 let's use 2/3 of the screen
                Dimension screenSize = Toolkit.getDefaultToolkit()
                        .getScreenSize();
                window.setSize(screenSize.width * 2 / 3,
                        screenSize.height * 2 / 3);
                // center the window
                window.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                window.setLocationRelativeTo(null);
                
                window.setVisible(true);
            }
        };
        EventQueue.invokeLater(r);
    }

    /**
     * Inserts a new node, the kind and name depends on the selected node.
     *
     */
    class InsertNodeAction extends AbstractAction {
        InsertNodeAction() {
            super("Insert");
        }

        public void actionPerformed(ActionEvent e) {
            TreePath selp = treeTable.getTreeSelectionModel()
                    .getSelectionPath();
            if ((selp == null) || !(selp.getLastPathComponent() instanceof Container)) return;
            Container parent = (Container) selp.getLastPathComponent();
            ComponentTTModel model = (ComponentTTModel) treeTable.getTreeTableModel();
            model.add(parent, createComponent(parent.getComponentCount()));
        }
    }

    /**
     * Deletes a node after one is selected.
     *
     */
    class DeleteNodeAction extends AbstractAction {
        DeleteNodeAction() {
            super("Delete");
        }

        public void actionPerformed(ActionEvent e) {
            TreePath selp = treeTable.getTreeSelectionModel()
                    .getSelectionPath();
            if ((selp == null) 
                    || !(selp.getLastPathComponent() instanceof Component))
                return;
            Component parent = (Component) selp.getLastPathComponent();
            ComponentTTModel model = (ComponentTTModel) treeTable
                    .getTreeTableModel();
            model.remove(parent);
        }
    }

    /**
     * Generates a PersonTreeTableModel of fake persons.
     * @return
     */
    public TreeTableModel generateTestModel() {
        Container container = new JXPanel();
        for (int i = 0; i < 10; i++) {
            JComponent label = createComponent(i);
            container.add(label);
        }
        return new ComponentTTModel(container);
    }

    /**
     * @param i
     * @return
     */
    private JComponent createComponent(int i) {
        JXPanel label = new JXPanel();
        label.setName("myname " + i);
        return label;
    }



}