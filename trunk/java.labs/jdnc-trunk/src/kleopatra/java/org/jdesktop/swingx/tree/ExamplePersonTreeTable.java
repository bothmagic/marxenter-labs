/*
 * Created on 19.06.2007
 *
 */
package org.jdesktop.swingx.tree;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;

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
public class ExamplePersonTreeTable {
    private JXTreeTable treeTable;

    public ExamplePersonTreeTable() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            JXErrorPane.showDialog(e);
        }
        ExamplePersonTreeTable test = new ExamplePersonTreeTable();
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

                DefaultTreeTableModelExt personTreeTableModel = generateTestModel();
                treeTable = new JXTreeTable(personTreeTableModel);
                treeTable.setEditable(true);
                
                EditableValue ev = new EditableValue() {

                    public Object getValue(Object node) {
                        if (node instanceof AbstractMutableTreeTableNode) {
                            node = ((AbstractMutableTreeTableNode) node).getUserObject();
                        }
                        if (node instanceof Person) {
                            return ((Person) node).getPhoneNbr();
                        }
                        return null;
                    }

                    public boolean setValue(Object node, Object newValue) {
                        if (!(node instanceof AbstractMutableTreeTableNode))                           
                            return false;
                        Object value = ((AbstractMutableTreeTableNode) node).getUserObject();
                        if (value instanceof Person) {
                            ((Person) value).setPhoneNbr(String.valueOf(newValue));
                            return true;
                        }
                        return false;
                    }
                    
                };
                personTreeTableModel.setEditableValue(ev);
                TreeCellEditor editor = new CustomTreeEditor(ev);
                StringValue sv = new StringValue() {

                    public String getString(Object value) {
                        if (value instanceof Person) {
                            Person person = (Person) value;
                            return person.getLastName() + ", " + person.getFirstName();
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
                 JXTree tree = new JXTree(personTreeTableModel);
                 tree.setEditable(true);
                 tree.setCellRenderer(new DefaultTreeRenderer());
                 tree.setCellEditor(editor);
                 window.add(new JScrollPane(tree));
//                window.add(new JScrollPane(treeTable));
//                 let's use 2/3 of the screen
                Dimension screenSize = Toolkit.getDefaultToolkit()
                        .getScreenSize();
                window.setSize(screenSize.width * 2 / 3,
                        screenSize.height * 2 / 3);
                // center the window
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
            PersonTTNode insertAtNode;
            String famName = null;
            boolean isFamilyNode = false;
            if (selp == null) {
                //nothing selected, create a new family
                insertAtNode = (PersonTTNode) treeTable
                        .getTreeTableModel().getRoot();
                famName = JOptionPane.showInputDialog(SwingUtilities
                        .windowForComponent(treeTable),
                        "What's the new Family name?");
                isFamilyNode = true;
            } else {
                insertAtNode = (PersonTTNode) selp
                        .getLastPathComponent();
                // pick the closest family node, in this case adding a child  
                // to a PersonNode doesn't make sense.
                if (insertAtNode.isLeaf()
                        && !(insertAtNode.getParent() == treeTable
                                .getTreeTableModel().getRoot())) {
                    insertAtNode = (PersonTTNode) insertAtNode
                            .getParent();
                    isFamilyNode = false;
                }
                famName = ((Person) insertAtNode.getUserObject()).getLastName();
            }
            Person nuObj = generateTestPerson(famName, isFamilyNode);
            MutableTreeTableNode nuNode = new PersonTTNode(nuObj);
            //insertAtNode.add(nuNode);
            int idx = insertAtNode.getChildCount();

            DefaultTreeTableModel myModel = ((DefaultTreeTableModel) treeTable
                    .getTreeTableModel());
            myModel.insertNodeInto(nuNode, insertAtNode, idx);

            treeTable.expandPath(new TreePath(myModel
                    .getPathToRoot(insertAtNode)));
            treeTable.getTreeSelectionModel().setSelectionPath(
                    new TreePath(myModel.getPathToRoot(nuNode)));
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
            try {
                DefaultTreeTableModel myModel = (DefaultTreeTableModel) treeTable
                        .getTreeTableModel();
                TreePath selp = treeTable.getTreeSelectionModel()
                        .getSelectionPath();
                if (selp == null) {
                    JOptionPane
                            .showMessageDialog(
                                    treeTable,
                                    "You Must select a node in the tree before you can delete",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    PersonTTNode toBeDeletedNode = (PersonTTNode) selp
                            .getLastPathComponent();
                    // be nice and ask about it
                    int confirm = JOptionPane.showConfirmDialog(SwingUtilities
                            .windowForComponent(treeTable),
                            "Are you sure you want to delete "
                                    + toBeDeletedNode.getUserObject()
                                            .toString() + "?");
                    if (confirm != JOptionPane.OK_OPTION) {
                        return;
                    }
                    // find out which node to select next
                    MutableTreeTableNode parentNode = (MutableTreeTableNode) toBeDeletedNode
                            .getParent();

                    int currIdx = myModel.getIndexOfChild(parentNode,
                            toBeDeletedNode);
                    TreeTableNode nextToBeSelected = null;
                    if (parentNode.getChildCount() > currIdx) {
                        nextToBeSelected = parentNode.getChildAt(currIdx);
                    }
                    if (nextToBeSelected == null && currIdx > 0) {
                        nextToBeSelected = parentNode.getChildAt(currIdx + 1);
                    } else {
                        nextToBeSelected = (MutableTreeTableNode) toBeDeletedNode
                                .getParent();
                    }
                    myModel.removeNodeFromParent(toBeDeletedNode);

                    treeTable.getTreeSelectionModel().setSelectionPath(
                            new TreePath(myModel
                                    .getPathToRoot(nextToBeSelected)));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JXErrorPane.showDialog(ex);
            }
        }
    }

    /**
     * Generates a PersonTreeTableModel of fake persons.
     * @return
     */
    public DefaultTreeTableModelExt generateTestModel() {
        Set<Person> list = new TreeSet<Person>();
        // gen test persons
        for (int i = 0; i < 20; i++) {
            list.add(generateTestPerson(null, false));
        }
        String prevLast = "";
        String currentLast = "";
        PersonTTNode currentNameNode = null;
        // shouldn't be visible
        MutableTreeTableNode aRoot = new PersonTTNode(new Person());
        for (Person testPerson : list) {
            currentLast = testPerson.getLastName();
            if (currentLast.equals(prevLast)) {
                currentNameNode.add(new PersonTTNode(testPerson));
            } else {
                if (currentNameNode != null) {
                    aRoot.insert(currentNameNode, aRoot.getChildCount());
                }
                currentNameNode = new PersonTTNode(new Person("", "",
                        testPerson.getLastName(), ""));
                currentNameNode.add(new PersonTTNode(testPerson));
                prevLast = currentLast;
            }
        }
        String[] columnNames = { "First", "Middle", "Last",
        "Phone" };

        Vector<String> vectorNames = new Vector<String>(Arrays
        .asList(columnNames));
        return new DefaultTreeTableModelExt(aRoot, vectorNames);
    }


    private static Random myRandomInstance = new Random();

    /**
     * @return a String of U.S. phone format (nnn) nnn-nnnn
     */
    private static String genPhone() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(myRandomInstance.nextInt(10));
        }
        sb.insert(6, '-');
        sb.insert(3, ") ");
        sb.insert(0, '(');
        return sb.toString();
    }

    private static String[] FIRST_NAMES = { "Amy", "Bob", "Cedric", "Dan",
            "Erica", "Fred" };

    private static String[] LAST_NAMES = { "Atkins", "Boer", "Celsius",
            "Drake", "Evans", "Foo" };

    /**
     * 
     * @param familyName if null it will be generated
     * @param isFamilyNode if true don't generate first, middle, phone. 
     * @return a Person object
     */
    public static Person generateTestPerson(String familyName,
            boolean isFamilyNode) {
        int firstN = myRandomInstance.nextInt(FIRST_NAMES.length);
        int middleN = myRandomInstance.nextInt(FIRST_NAMES.length);
        int lastN = myRandomInstance.nextInt(LAST_NAMES.length);
        if (isFamilyNode) {
            return new Person("", "", (familyName != null ? familyName
                    : LAST_NAMES[lastN]), "");
        } else {
            return new Person(FIRST_NAMES[firstN], FIRST_NAMES[middleN]
                    .substring(0, 1), (familyName != null ? familyName
                    : LAST_NAMES[lastN]), genPhone());
        }
    }


}