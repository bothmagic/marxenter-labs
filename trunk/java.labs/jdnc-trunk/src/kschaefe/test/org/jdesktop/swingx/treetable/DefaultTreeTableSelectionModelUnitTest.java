/**
 * 
 */
package org.jdesktop.swingx.treetable;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.tree.TreePath;

import junit.framework.TestCase;

import org.jdesktop.swingx.JXTree;

/**
 * This test case ensures that selections made against one model type ({@code Tree}
 * or {@code List}) forward to the other. Although
 * {@code TreeTableSelectionModel} and {@code DefaultTreeTableSelectionModel}
 * were designed for working with tree table, the test case runs against a
 * seperated {@code JXTree} and {@code JTable} to make it easier to query
 * selection propogation.
 */
public class DefaultTreeTableSelectionModelUnitTest extends TestCase {
    private JXTree tree;
    private JTable table;
    
    private DefaultMutableTreeTableNode root;
    private DefaultMutableTreeTableNode child1;
    private DefaultMutableTreeTableNode child2;
    private DefaultMutableTreeTableNode grandchild1;
    private DefaultMutableTreeTableNode grandchild2;
    private DefaultMutableTreeTableNode grandchild3;
    private DefaultMutableTreeTableNode grandchild4;
    private DefaultMutableTreeTableNode grandchild5;
    private DefaultMutableTreeTableNode grandchild6;
    
    private TreeTableNode createTree() {
        root = new DefaultMutableTreeTableNode("root");
        
        child1 = new DefaultMutableTreeTableNode("child1");
        grandchild1 = new DefaultMutableTreeTableNode("grandchild1");
        child1.add(grandchild1);
        grandchild2 = new DefaultMutableTreeTableNode("grandchild2");
        child1.add(grandchild2);
        grandchild3 = new DefaultMutableTreeTableNode("grandchild3");
        child1.add(grandchild3);
        root.add(child1);
        
        child2 = new DefaultMutableTreeTableNode("child2");
        grandchild4 = new DefaultMutableTreeTableNode("grandchild4");
        child2.add(grandchild4);
        grandchild5 = new DefaultMutableTreeTableNode("grandchild5");
        child2.add(grandchild5);
        grandchild6 = new DefaultMutableTreeTableNode("grandchild6");
        child2.add(grandchild6);
        root.add(child2);
        
        return root;
    }

    /*
     * Must be run AFTER createTree.  This is a hack.
     */
    private Vector<Vector<DefaultMutableTreeTableNode>> createTableData() {
        Vector<Vector<DefaultMutableTreeTableNode>> data = new Vector<Vector<DefaultMutableTreeTableNode>>();
        
        Vector<DefaultMutableTreeTableNode> column = new Vector<DefaultMutableTreeTableNode>();
        column.add(root);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(child1);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild1);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild2);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild3);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(child2);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild4);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild5);
        data.add(column);
        
        column = new Vector<DefaultMutableTreeTableNode>();
        column.add(grandchild6);
        data.add(column);
        
        return data;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        tree = new JXTree(new DefaultTreeTableModel(createTree()));
        
        Vector<String> names = new Vector<String>();
        names.add("only column");
        
        table = new JTable(createTableData(), names);
        
        //Assign selection model, this is why we're here
        DefaultTreeTableSelectionModel sm = new DefaultTreeTableSelectionModel();
        tree.setSelectionModel(sm);
        tree.expandAll();
        table.setSelectionModel(sm);
        
        //verify we are dealing with the same data
        assertEquals(tree.getRowCount(), table.getRowCount());
    }
    
    public void testSetSelectionPath() {
        //single selection method, mode is irrelevant
        
        //test tree to table
        tree.getSelectionModel().setSelectionPath(new TreePath(root));
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 0);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(0));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPath(new TreePath(new Object[]{root, child1}));
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 1);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(1));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPath(new TreePath(new Object[]{root, child2, grandchild6}));
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 8);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(8));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPath(null);
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), -1);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), -1);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        for (int i = 0, len = tree.getRowCount(); i < len; i++) {
            assertFalse(table.getSelectionModel().isSelectedIndex(i));
        }
        assertTrue(table.getSelectionModel().isSelectionEmpty());
                
        //table does not contain this method
    }
    
    /**
     * Tests setSelectionPaths for the single select modes.
     */
    public void testSetSelectionPathsWithSingleSelect() {
        //single selection mode
        tree.getSelectionModel().setSelectionMode(
                DefaultTreeTableSelectionModel.SINGLE_TREE_SELECTION);

        //TODO propogate?
//        assertEquals(table.getSelectionModel().getSelectionMode(),
//                DefaultTreeTableSelectionModel.SINGLE_SELECTION);
        
        //test tree to table
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(root)});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 0);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(0));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        //only takes first arg for path when select single
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(root), new TreePath(new Object[]{root, child1})});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 0);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(0));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        //only takes first arg for path when select single
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(new Object[]{root, child1}), new TreePath(new Object[]{root, child2, grandchild6})});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 1);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(1));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        //only takes first arg for path when select single
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(new Object[]{root, child2, grandchild6}), null});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 8);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(8));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        //only takes first arg for path when select single
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{null, new TreePath(root)});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), -1);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), -1);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        for (int i = 0, len = tree.getRowCount(); i < len; i++) {
            assertFalse(table.getSelectionModel().isSelectedIndex(i));
        }
        assertTrue(table.getSelectionModel().isSelectionEmpty());
        
        //table does not contain this method
    }
    
    /**
     * Tests setSelectionPaths for the single select modes.
     */
    public void testSetSelectionPathsWithSingleGroup() {
        //single selection mode
        tree.getSelectionModel().setSelectionMode(
                DefaultTreeTableSelectionModel.CONTIGUOUS_TREE_SELECTION);
        
        //TODO propogate?
//        assertEquals(table.getSelectionModel().getSelectionMode(),
//                DefaultTreeTableSelectionModel.SINGLE_INTERVAL_SELECTION);
        
        //test tree to table
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(root)});
        
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 0);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 0);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(0));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(root), new TreePath(new Object[]{root, child1})});
        
        //TODO build set selection calls for batch selecting
        //adds each individually, this is a bug
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 1);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 0);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(0));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(new Object[]{root, child1}), new TreePath(new Object[]{root, child1, grandchild1})});
        
        //TODO build set selection calls for batch selecting
        //adds each individually, this is a bug
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 2);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 2);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 2);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 1);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(1));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        tree.getSelectionModel().setSelectionPaths(new TreePath[]{new TreePath(new Object[]{root, child2, grandchild4}), new TreePath(new Object[]{root, child2, grandchild5}), new TreePath(new Object[]{root, child2, grandchild6})});
        
        //TODO build set selection calls for batch selecting
        //adds each individually, this is a bug
        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), 8);
        assertEquals(table.getSelectionModel().getMinSelectionIndex(), 6);
        assertFalse(table.getSelectionModel().getValueIsAdjusting());
        assertTrue(table.getSelectionModel().isSelectedIndex(8));
        assertFalse(table.getSelectionModel().isSelectionEmpty());
        
        //only takes first arg for path when select single
//        tree.getSelectionModel().setSelectionPaths(new TreePath[]{null, new TreePath(root)});
//        
//        assertEquals(table.getSelectionModel().getAnchorSelectionIndex(), 8);
//        assertEquals(table.getSelectionModel().getLeadSelectionIndex(), 8);
//        assertEquals(table.getSelectionModel().getMaxSelectionIndex(), -1);
//        assertEquals(table.getSelectionModel().getMinSelectionIndex(), -1);
//        assertFalse(table.getSelectionModel().getValueIsAdjusting());
//        for (int i = 0, len = tree.getRowCount(); i < len; i++) {
//            assertFalse(table.getSelectionModel().isSelectedIndex(i));
//        }
//        assertTrue(table.getSelectionModel().isSelectionEmpty());
        
        //table does not contain this method
    }
}
