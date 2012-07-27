/*
 * Created on 27.06.2008
 *
 */
package netbeans.xoutline;

import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TableModelReport;
import org.jdesktop.test.TestUtils;
import org.jdesktop.test.TreeModelReport;

/**
 * Test state, tree, table events (no expansion) 
 */
public class XOutlineModelTest extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(XOutlineModelTest.class
            .getName());
    
    private XDefaultTTM treeTableModel;
    private XOutlineModel outline;
    private Object root;

    /**
     * test rowheight default and setting taken.
     */
    public void testRowHeight() {
        assertEquals(0, outline.getRowHeight());
        int rowHeight = 20;
        outline.setRowHeight(rowHeight);
        assertEquals(rowHeight, outline.getRowHeight());
    }
    
    /**
     * test rowheight default and setting taken.
     */
    public void testRowHeightNegative() {
        assertEquals(0, outline.getRowHeight());
        int rowHeight = -1;
        outline.setRowHeight(rowHeight);
        assertEquals(rowHeight, outline.getRowHeight());
    }
    
    /**
     * test rowheight bound property.
     */
    public void testRowHeightPropertyChange() {
        assertEquals(0, outline.getRowHeight());
        int rowHeight = 20;
        PropertyChangeReport report = new PropertyChangeReport();
        outline.addPropertyChangeListener(report);
        
        outline.setRowHeight(rowHeight);
        TestUtils.assertPropertyChangeEvent(report, "rowHeight", 0, rowHeight);
    }
    
    public void testRowHeightCompareJTree() {
        JTree outline = new JTree();
        assertEquals(0, outline.getRowHeight());
        int rowHeight = 20;
        outline.setRowHeight(rowHeight);
        assertEquals(rowHeight, outline.getRowHeight());
    }
    
    /**
     * test setting the large model property: basics sync
     */
    public void testLargeModelBasics() {
        int rowHeight = 20;
        // need rowHeight > 0 for largeModel
        outline.setRowHeight(rowHeight);
        assertFalse(outline.isLargeModel());
        outline.setLargeModel(true);
        assertTrue("largeModel must be true", outline.isLargeModel());
        assertTrue("root must be visible", outline.isRootVisible());
        assertEquals(rowHeight, outline.getRowHeight());
    }
    
    /**
     * JTree/UI only allow the largeModel property for rowHeight > 0 (due
     * to FixedHeightLayout). Do the same - but document.
     * 
     */
    public void testLargeModelRevokedZeroHeight() {
        outline.setLargeModel(true);
        assertEquals(outline.getRowHeight() > 0, outline.isLargeModel());
    }
    /**
     * JTree/UI only allow the largeModel property for rowHeight > 0 (due
     * to FixedHeightLayout). Do the same - but document.
     * 
     */
    public void testLargeModelNonZeroHeight() {
        outline.setRowHeight(20);
        outline.setLargeModel(true);
        assertEquals(outline.getRowHeight() > 0, outline.isLargeModel());
    }
    /**
     * test setting the large model property: expansionstate must be synched
     */
    public void testLargeModelAllExpanded() {
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        int rows = outline.getRowCount();
        // need rowHeight > 0 for largeModel
        outline.setRowHeight(20);
        outline.setLargeModel(true);
        assertTrue("largeModel must be true", outline.isLargeModel());
        assertEquals(rows, outline.getRowCount());
    }
    
    /**
     * test large model: must fire property change.
     */
    public void testLargeModelProperty() {
        boolean largeModel = outline.isLargeModel();
        // need rowHeight > 0 for largeModel
        outline.setRowHeight(20);
        PropertyChangeReport report = new PropertyChangeReport();
        outline.addPropertyChangeListener(report);
        outline.setLargeModel(!largeModel);
        TestUtils.assertPropertyChangeEvent(report, "largeModel", largeModel, !largeModel);
    }
    /**
     * test removed from nested collapsed folder.
     * 
     * PENDING: the implementation does not "bubbles up" the deep removes, correct? 
     * Don't think so, but we had debates about it.
     */
    public void testRemovedUnderNestedCollapsed() {
        MutableTreeTableNode child = (MutableTreeTableNode) outline.getNodeForRow(1);
        outline.collapseRow(0);
        TableModelReport report = new TableModelReport(outline);
        treeTableModel.removeNodeFromParent((MutableTreeTableNode) outline.getChild(child, 0));
        assertEquals("no events fired of nested removes", 0, report.getEventCount());
//        assertEquals(report.getUpdateEventCount(), report.getEventCount());
//        assertEquals(1, report.getUpdateEventCount());
//        assertEquals("update on row of grandparent of removed", 
//                0, report.getLastEvent().getFirstRow());
    }

    /**
     * test removed from collapsed folder.
     */
    public void testRemovedUnderCollapsed() {
        MutableTreeTableNode child = (MutableTreeTableNode) outline.getNodeForRow(1);
        TableModelReport report = new TableModelReport(outline);
        treeTableModel.removeNodeFromParent((MutableTreeTableNode) outline.getChild(child, 0));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals(report.getUpdateEventCount(), report.getEventCount());
        assertEquals("update on row of parent of removed", 
                1, report.getLastEvent().getFirstRow());
    }

    /**
     * test inserted into collapsed folder.
     */
    public void testInsertedUnderCollapsed() {
        MutableTreeTableNode child = (MutableTreeTableNode) outline.getNodeForRow(1);
        TableModelReport report = new TableModelReport(outline);
        treeTableModel.insertNodeInto(new DefaultMutableTreeTableNode("newchild"), child, 0);
        assertEquals(1, report.getUpdateEventCount());
        assertEquals(report.getUpdateEventCount(), report.getEventCount());
        assertEquals("update on row of parent of inserted", 
                1, report.getLastEvent().getFirstRow());
    }
    
    /**
     * test inserted into leaf (purely sanity ...)
     */
    public void testInsertedIntoLeaf() {
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        MutableTreeTableNode child = (MutableTreeTableNode) outline.getNodeForRow(2);
        assertTrue("sanity: is leaf before insert", outline.isLeaf(child));
        TableModelReport report = new TableModelReport(outline);
        treeTableModel.insertNodeInto(new DefaultMutableTreeTableNode("newchild"), child, 0);
        assertFalse("sanity: is folder after insert", outline.isLeaf(child));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals(report.getUpdateEventCount(), report.getEventCount());
    }
    
    public void testNullStructureChanged() {
        ((DefaultXOutlineModel)outline).broadcaster.treeStructureChanged(null);
    }

    public void testNullTreeNodesChanged() {
        ((DefaultXOutlineModel)outline).broadcaster.treeNodesChanged(null);
    }

    public void testNullTreeNodesInserted() {
        ((DefaultXOutlineModel)outline).broadcaster.treeNodesInserted(null);
    }

    public void testNullTreeNodesRemoved() {
        ((DefaultXOutlineModel)outline).broadcaster.treeNodesRemoved(null);
    }
    /**
     * Test mapping tree structureChanged to table event (old problem)
     * Here: test default mapper - subtree maps to datachanged.
     */
    public void testTreeStructureChangedOnSubtree() {
        TableModelReport report = new TableModelReport(outline);
        TreePath subTreePath = new TreePath(treeTableModel.getPathToRoot(
                ((TreeTableNode) root).getChildAt(0)));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(subTreePath );
        assertEquals(1, report.getEventCount());
        TableModelEvent lastEvent = report.getLastEvent();
        assertTrue("expected structure changed but was " + TableModelReport.printEvent(lastEvent), 
                report.isDataChanged(lastEvent));
    }

    /**
     * Test table structure changed (old problem)
     */
    public void testSetModelFiresTableStructureChanged() {
        TableModelReport report = new TableModelReport(outline);
        ((DefaultXOutlineModel) outline).setTreeTableModel(null);
        assertEquals(1, report.getEventCount());
        TableModelEvent lastEvent = report.getLastEvent();
        assertTrue("expected structure changed but was " + TableModelReport.printEvent(lastEvent), 
                report.isStructureChanged(lastEvent));
    }
    
    /**
     * test column count reset (was: NPE)
     */
    public void testSetModelNullUpdatesColumnCount() {
        ((DefaultXOutlineModel) outline).setTreeTableModel(null);
        assertEquals("columnCount must be reset", 0, outline.getColumnCount());
    }

    /**
     * test row count reset: done by resetting layout cache.
     */
    public void testSetModelNullUpdatesRowCount() {
        ((DefaultXOutlineModel) outline).setTreeTableModel(null);
        assertEquals("rowcount must be reset", 0, outline.getRowCount());
    }

    
    /**
     * test null tree model.
     */
    public void testSetModelNull() {
        TreeModelReport report = new TreeModelReport(outline);
        ((DefaultXOutlineModel) outline).setTreeTableModel(null);
        assertEquals(1, report.getStructureEventCount());
        assertEquals(1, report.getEventCount());
     }
    
    /**
     * test null root and event notification
     */
    public void testNullRoot() {
        TreeModelReport report = new TreeModelReport(outline);
        treeTableModel.setRoot(null);
        assertEquals(1, report.getStructureEventCount());
        assertEquals(1, report.getEventCount());
    }
    
    /**
     * Test setModel on xOutline: here - treemodellisteners re-wired.
     */
    public void testSetModelRewiresListeners() {
        assertEquals("sanity: one listener after init", 
                1, treeTableModel.getTreeModelListeners().length);
        XDefaultTTM newModel = createCustomTreeTableModelFromDefault();
        ((DefaultXOutlineModel) outline).setTreeTableModel(newModel);
        assertEquals("listeners to old model must be removed", 
                0, treeTableModel.getTreeModelListeners().length);
        assertEquals(1, newModel.getTreeModelListeners().length);
    }
 
    public void testRootVisibleOnNullRoot() {
        treeTableModel.setRoot(null);
        assertEquals("sanity: no rows", 0, outline.getRowCount());
        TableModelReport report = new TableModelReport(outline);
        outline.setRootVisible(!outline.isRootVisible());
        assertEquals("must not fire row event on empty tree", 0, report.getEventCount());
    }

    /**
     * Test setter and bound property. 
     * 
     */
    public void testRootVisibleProperty() {
        int countWithRoot = outline.getRowCount();
        assertTrue("sanity: assume that root visible initially", outline.isRootVisible());
        assertTrue("sanity: assume expanded", countWithRoot > 1);
        PropertyChangeReport report = new PropertyChangeReport();
        outline.addPropertyChangeListener(report);
        outline.setRootVisible(false);
        assertFalse("sanity: root changed to invisible ", outline.isRootVisible());
        assertEquals(countWithRoot -1, outline.getRowCount());
        TestUtils.assertPropertyChangeEvent(report, "rootVisible", true, false);
    }
   
    /**
     * Test visibility change fires table event (remove)
     */
    public void testRootVisibleTableEventDelete() {
        TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        outline.setRootVisible(false);
        assertEquals(1, report.getDeleteEventCount());
        assertEquals(0, report.getLastDeleteEvent().getFirstRow());
    }
    
    /**
     * Test visibility change fires table event (add)
     */
    public void testRootVisibleTableEventInsert() {
        outline.setRootVisible(false);
        TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        outline.setRootVisible(true);
        assertEquals(1, report.getInsertEventCount());
        assertEquals(0, report.getLastInsertEvent().getFirstRow());
    }
    /**
     * Test table model events after deleting multiple discontigous nodes.
     */
    public void testInsertEventRowMappingDiscontiguous() {
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        // food node
        final int row = 11; 
        MutableTreeTableNode parent = (MutableTreeTableNode) outline.getNodeForRow(row);
        int first = 0;
        MutableTreeTableNode firstChild = new DefaultMutableTreeTableNode("firstAdded");
        int last = parent.getChildCount() + 1;
        MutableTreeTableNode lastChild = new DefaultMutableTreeTableNode("secondAdded");
        final TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        
        parent.insert(firstChild, first);
        parent.insert(lastChild, last);
        
        treeTableModel.getTreeModelSupport().fireChildrenAdded(outline.getPathForRow(row), 
                new int[] {first, last}, new Object[]{firstChild, lastChild});
        assertEquals("tableModel must have fired", 2, report.getEventCount());
        assertEquals("the event type must be update", 2, report
                .getInsertEventCount());
        // PENDING: report/infrastructure should support testing multiple events
        TableModelEvent event = report.getLastInsertEvent();
        assertEquals("same index for first and last", event.getFirstRow(), event.getLastRow());
        assertEquals("the updated row ", row + 1 + last, event.getFirstRow());
    }

    /**
     * Test table model events after deleting multiple discontigous nodes.
     */
    public void testDeleteEventRowMappingDiscontiguous() {
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        // food node
        final int row = 11; 
        MutableTreeTableNode parent = (MutableTreeTableNode) outline.getNodeForRow(row);
        int first = 0;
        MutableTreeTableNode firstChild = (MutableTreeTableNode) parent.getChildAt(first);
        int last = parent.getChildCount() - 1;
        MutableTreeTableNode lastChild = (MutableTreeTableNode) parent.getChildAt(last);
        final TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        
        parent.remove(firstChild);
        parent.remove(lastChild);
        
        treeTableModel.getTreeModelSupport().fireChildrenRemoved(outline.getPathForRow(row), 
                new int[] {first, last}, new Object[]{firstChild, lastChild});
        assertEquals("tableModel must have fired", 2, report.getEventCount());
        assertEquals("the event type must be update", 2, report
                .getDeleteEventCount());
        // PENDING: report/infrastructure should support testing multiple events
        TableModelEvent event = report.getLastDeleteEvent();
        assertEquals("same index for first and last", event.getFirstRow(), event.getLastRow());
        assertEquals("the updated row ", row + 1 + first, event.getFirstRow());
    }

    /**
     * Test table model event after change root (== content only, no structural)
     */
    public void testUpdateEventRoot() {
        final TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        treeTableModel.setValueAt("games", treeTableModel.getRoot(), 0);
        int row = 0;
        assertEquals("table value must be new", "games", outline.getValueAt(row, 0));
        assertEquals("tableModel must have fired", 1, report.getEventCount());
        assertEquals("the event type must be update", 1, report
                .getUpdateEventCount());
        TableModelEvent event = report.getLastUpdateEvent();
        assertEquals("sanity: one row updated", event.getFirstRow(), event.getLastRow());
        assertEquals("the updated row ", row, event.getFirstRow());
        
    }
    
    /**
     * Test table model events after updating multiple discontigous nodes.
     */
    public void testUpdateEventRowMappingDiscontigous() {
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        // sports node
        final int row = 6; 
        TreeTableNode parent = (MutableTreeTableNode) outline.getNodeForRow(row);
        int first = 0;
        TreeTableNode firstChild = parent.getChildAt(first);
        int last = parent.getChildCount() - 1;
        TreeTableNode lastChild = parent.getChildAt(last);
        firstChild.setUserObject(firstChild.getUserObject() + "XX");
        lastChild.setUserObject(lastChild.getUserObject() + "YY");
        final TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        treeTableModel.getTreeModelSupport().fireChildrenChanged(outline.getPathForRow(row), 
                new int[] {first, last}, new Object[]{firstChild, lastChild});
        assertEquals("tableModel must have fired", 2, report.getEventCount());
        assertEquals("the event type must be update", 2, report
                .getUpdateEventCount());
        TableModelEvent event = report.getLastUpdateEvent();
        assertEquals("last updated row", row + 1 + last, event.getLastRow());
    }
    
    /**
     * Test table model event after change of single event.
     */
    public void testUpdateEventRowMappingAllExpanded() {
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        final int row = 6; //outline.getRowCount() - 1;
        Object node = outline.getNodeForRow(row);
        TreePath path = outline.getPathForRow(row);
        assertEquals("sanity: round trip", row, outline.getRowForPath(path));
        final TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        treeTableModel.setValueAt("games", node, 0);
        assertEquals("table value must be new", "games", outline.getValueAt(row, 0));
        assertEquals("tableModel must have fired", 1, report.getEventCount());
        assertEquals("the event type must be update", 1, report
                .getUpdateEventCount());
        TableModelEvent event = report.getLastUpdateEvent();
        assertEquals("sanity: one row updated", event.getFirstRow(), event.getLastRow());
        assertEquals("the updated row ", row, event.getFirstRow());
    }

    public void testOutlineTableNotificationTableUpdate() {
        Object newValue = "something unexpected";
        TableModelReport report = new TableModelReport();
        outline.addTableModelListener(report);
        outline.setValueAt(newValue, 0, 0);
        assertEquals(1, report.getUpdateEventCount());
        assertEquals(newValue, outline.getValueAt(0, 0));
    }

    
    /**
     * 
     */
    public void testEditableSetValue() {
        assertEquals("editable ", treeTableModel.isCellEditable(root, 0), 
                outline.isCellEditable(0, 0));
        Object newValue = "something unexpected";
        outline.setValueAt(newValue, 0, 0);
        assertEquals("value set", newValue, outline.getValueAt(0, 0));
        
    }
    
    /**
     * 
     */
    public void testNodeForRow() {
        Object node = outline.getNodeForRow(0);
        assertNotNull(node);
        assertEquals(root, node);
        for (int i = 0; i < outline.getChildCount(node); i++) {
            Object child = outline.getNodeForRow(i + 1);
            assertEquals(outline.getChild(node, i), child);
        }
    }
    
    /**
     * Issue: initial expanded state of pathSupport.
     */
    public void testExpandedInitial() {
        assertTrue("root is expanded", outline.isExpanded(0));
        TreePath path = outline.getPathForRow(0);
        assertEquals("pathSupport must have same expanded state as layoutCache", 
                outline.getLayout().isExpanded(path), 
                outline.getTreePathSupport().isExpanded(path));
    }

    /**
     * Issue: initial expanded state of pathSupport.
     */
    public void testVisibleInitial() {
        assertTrue("root is expanded", outline.isExpanded(0));
        TreePath path = outline.getPathForRow(0);
        assertTrue("root must be visible", 
                outline.getTreePathSupport().isVisible(path));
        TreePath childPath = outline.getPathForRow(1);
        assertNotNull("sanity: childpath not null", childPath);
        assertTrue("first child of expanded root must be visible", 
                outline.getTreePathSupport().isVisible(childPath));
    }

    /**
     * Sanity: test expectation of initial state.
     */
    public void testDefaultState() {
        assertTrue("default root is visible", outline.isRootVisible());
        assertFalse("no large model", outline.isLargeModel());
        assertTrue("root is expanded", outline.isExpanded(0));
        assertEquals("root and direct children visible", 
                treeTableModel.getChildCount(root) + 1,
                outline.getRowCount());
        assertFalse("root must not be leaf", outline.isLeaf(0));
    }
    /**
     * Pure sanity: make sure the findPanelTree is working as expected.
     */
    public void testUnderlyingTreeNotificationOnSetName() {
        Object newValue = "something unexpected";
        // sanity - the underlying model is notifying
        TreeModelReport report = new TreeModelReport(treeTableModel);
        treeTableModel.setValueAt(newValue, root, 0);
        assertEquals(1, report.getUpdateEventCount());
        assertEquals(newValue, treeTableModel.getValueAt(root, 0));
    }
    

    /**
     * Creates and returns a custom model from JXTree default model. The model
     * is of type DefaultTreeModel, allowing for easy insert/remove.
     * 
     * @return
     */
    private XDefaultTTM createCustomTreeTableModelFromDefault() {
        JXTree tree = new JXTree();
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        XDefaultTTM customTreeTableModel = XDefaultTTM
                .convertDefaultTreeModel(treeModel);

        return customTreeTableModel;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        treeTableModel = (XDefaultTTM) createCustomTreeTableModelFromDefault();
        outline = new DefaultXOutlineModel(treeTableModel);
        root = treeTableModel.getRoot();
        
    }
    
    
}
