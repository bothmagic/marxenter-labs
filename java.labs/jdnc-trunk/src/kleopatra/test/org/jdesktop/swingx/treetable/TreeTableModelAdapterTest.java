/*
 * Created on 14.07.2008
 *
 */
package org.jdesktop.swingx.treetable;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.table.treetable.NodeChangedMediator;
import org.jdesktop.swingx.table.treetable.NodeModel;
import org.jdesktop.swingx.table.treetable.TreeTableModelAdapter;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.util.Contract;
import org.jdesktop.test.TreeModelReport;

public class TreeTableModelAdapterTest extends InteractiveTestCase {

    NodeModel nodeModel;
    DefaultTreeModel treeModel;
    TreeTableModel treeTableModel;

    public void testColumnCount() {
        assertEquals(nodeModel.getColumnCount(), treeTableModel.getColumnCount());
    }
    
    
    public void testSetValueNodeChangedEvent() {
        TreeModelReport report = new TreeModelReport(treeTableModel);
        MutableTreeNode child = (MutableTreeNode) treeModel.getChild(treeModel.getRoot(), 0);
        assertTrue("sanity: treeTableModel is editable at " + child, treeTableModel.isCellEditable(child, 0));
        treeTableModel.setValueAt("newValue", child, 0);
        assertEquals(1, report.getEventCount());
    }
    
    /**
     * Test that a TabularTreeModel is not-editable without NodeChangedMediator.
     */
    public void testEditableMediator() {
        TreeTableModelAdapter model = new TreeTableModelAdapter(treeModel, nodeModel);
        MutableTreeNode child = (MutableTreeNode) treeModel.getChild(treeModel.getRoot(), 0);
        assertTrue("sanity: NodeModel is editable at " + child, nodeModel.isCellEditable(child, 0));
        assertFalse("TabularTreeModel must not be editable without mediator " + child, model.isCellEditable(child, 0));
        model.setNodeChangedMediator(NodeChangedMediator.DEFAULT);
        assertTrue("TabularTreeModel must not be editable without mediator " + child, model.isCellEditable(child, 0));
    }
    
    
    public void testTreeStructureChangeEvent() {
        TreeModelReport report = new TreeModelReport(treeTableModel);
        MutableTreeNode child = (MutableTreeNode) treeModel.getChild(treeModel.getRoot(), 0);
        treeModel.nodeStructureChanged(child);
        assertEquals(1, report.getEventCount());
        assertEquals(report.getEventCount(), report.getStructureEventCount());
        assertSame(child, report.getLastEvent().getTreePath().getLastPathComponent());
        assertSame(treeTableModel, report.getLastEvent().getSource());
    }

    public void testTreeChangeEvent() {
        TreeModelReport report = new TreeModelReport(treeTableModel);
        MutableTreeNode child = (MutableTreeNode) treeModel.getChild(treeModel.getRoot(), 0);
        treeModel.nodeChanged(child);
        assertEquals(1, report.getEventCount());
        assertEquals(report.getEventCount(), report.getUpdateEventCount());
        assertSame(child, report.getLastEvent().getChildren()[0]);
        assertSame(treeTableModel, report.getLastEvent().getSource());
    }

    public void testTreeDeleteEvent() {
        TreeModelReport report = new TreeModelReport(treeTableModel);
        MutableTreeNode child = (MutableTreeNode) treeModel.getChild(treeModel.getRoot(), 0);
        treeModel.removeNodeFromParent(child);
        assertEquals(1, report.getEventCount());
        assertEquals(report.getEventCount(), report.getDeleteEventCount());
        assertSame(child, report.getLastEvent().getChildren()[0]);
        assertSame(treeTableModel, report.getLastEvent().getSource());
    }

    public void testTreeInsertEvent() {
        TreeModelReport report = new TreeModelReport(treeTableModel);
        MutableTreeNode child = new DefaultMutableTreeNode("newChild");
        treeModel.insertNodeInto(child, (MutableTreeNode) treeModel.getRoot(), 0);
        assertEquals(1, report.getEventCount());
        assertEquals(report.getEventCount(), report.getInsertEventCount());
        assertSame(child, report.getLastEvent().getChildren()[0]);
        assertSame(treeTableModel, report.getLastEvent().getSource());
    }
    
    @Override
    protected void setUp() throws Exception {
        treeModel = (DefaultTreeModel) new JTree().getModel();
        nodeModel = createDefaultNodeModel();
        treeTableModel = new TreeTableModelAdapter(treeModel, nodeModel, NodeChangedMediator.DEFAULT);
    }


    public static NodeModel createDefaultNodeModel() {
        NodeModel model = new NodeModel() {

            public Class<?> getColumnClass(int columnIndex) {
                return Object.class;
            }

            public int getColumnCount() {
                return 1;
            }

            public String getColumnName(int column) {
                return null;
            }

            public int getHierarchicalColumn() {
                // TODO Auto-generated method stub
                return 0;
            }

            public Object getValueAt(Object node, int column) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                return treeNode.getUserObject();
            }

            public boolean isCellEditable(Object node, int column) {
                return true;
            }

            public void setValueAt(Object value, Object node, int column) {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                treeNode.setUserObject(value);
            }};
        return model;
    }
    
    
}
