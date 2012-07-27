/*
 * Created on 27.06.2008
 *
 */
package netbeans.xoutline;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.test.TableModelReport;
import org.jdesktop.test.TreeExpansionReport;
import org.jdesktop.test.TreeWillExpandReport;

/**
 * Expansion related tests.
 * 
 */
public class XOutlineModelExpansionTest extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(XOutlineModelExpansionTest.class
            .getName());
    
    private XDefaultTTM treeTableModel;
    private XOutlineModel outline;
    private Object root;

    /**
     * test that expansion listeners are wired.
     */
    public void testGetListeners() {
        assertEquals(1, outline.getTreeExpansionListeners().length);
        assertEquals(1, outline.getTreeWillExpandListeners().length);
    }
    /**
     * Test remove updates expanded descendants. Here: parent collapsed.
     */
    public void testRemovedExpandedDescendantCollapsedParent() {
        TreePath rootPath = outline.getPathForRow(0);
        TreePath childPath = outline.getPathForRow(1);
        MutableTreeTableNode node = (MutableTreeTableNode) outline.getNodeForRow(1);
        outline.expandPath(childPath);
        outline.collapsePath(rootPath);
        TreePath[] contained = outline.getTreePathSupport().getDescendantToggledPaths(rootPath);
        assertEquals("expanded paths must contain childPath " + childPath, true,
                Arrays.asList(contained).contains(childPath));
        treeTableModel.removeNodeFromParent(node);
        TreePath[] removed = outline.getTreePathSupport().getDescendantToggledPaths(rootPath);
        assertEquals("expanded paths must not contain childPath " + childPath, false,
                Arrays.asList(removed).contains(childPath));
    }

    /**
     * Test remove updates expanded descendants. Here: parent expanded.
     */
    public void testRemovedExpandedDescendant() {
        TreePath rootPath = outline.getPathForRow(0);
        TreePath childPath = outline.getPathForRow(1);
        MutableTreeTableNode node = (MutableTreeTableNode) outline.getNodeForRow(1);
        outline.expandPath(childPath);
        TreePath[] contained = outline.getTreePathSupport().getDescendantToggledPaths(rootPath);
        assertEquals("expanded paths must contain childPath " + childPath, true,
                Arrays.asList(contained).contains(childPath));
        treeTableModel.removeNodeFromParent(node);
        TreePath[] removed = outline.getTreePathSupport().getDescendantToggledPaths(rootPath);
        assertEquals("expanded paths must not contain childPath " + childPath, false,
                Arrays.asList(removed).contains(childPath));
    }
    /**
     * Structure change on unrelated substree must not
     * clear the expanded markers.
     */
    public void testStructureChangeSibling() {
        TreePath firstPath = outline.getPathForRow(1);
        TreePath lastPath = outline.getPathForRow(3);
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        outline.collapseRow(0);
        assertTrue("sanity", outline.hasBeenExpanded(lastPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("sibling hasBeenExpanded unchanged", 
                true,
                outline.hasBeenExpanded(lastPath));
    }

    /**
     * Structure change on unrelated substree must not
     * clear the expanded markers.
     * Compare tree behaviour.
     */
    public void testStructureChangeSiblingCompareJTree() {
        XDefaultTTM treeTableModel = createCustomTreeTableModelFromDefault();
        JTree outline = new JTree(treeTableModel);
        TreePath firstPath = outline.getPathForRow(1);
        TreePath lastPath = outline.getPathForRow(3);
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        outline.collapseRow(0);
        assertTrue("sanity", outline.hasBeenExpanded(lastPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("sibling hasBeenExpanded unchanged", 
                true,
                outline.hasBeenExpanded(lastPath));
        
    }

    /**
     * Structure change on substree must clear expanded markers
     * clear the expanded markers. This is how JTree works.
     * 
     * Here we test if invisible (because hidden under collapsed
     * parent). TreePathSupport deviates from JTree in that it treats
     * invisible (that is hidden under a collapsed parent) expanded
     * nodes the same way as visible. JTree collapses the former.
     * 
     */
    public void testStructureChangeSubtree() {
        TreePath firstPath = outline.getPathForRow(1);
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        outline.collapseRow(0);
        assertTrue("sanity", outline.hasBeenExpanded(firstPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("subtree expansion state unchanged", 
                true,
                outline.hasBeenExpanded(firstPath));

    }

    /**
     * Structure change on substree must clear expanded markers
     * clear the expanded markers. This is how JTree works.
     * 
     * Here we test if invisible (because hidden under collapsed
     * parent). Expanded node is collapsed. 
     * 
     */
    public void testStructureChangeSubtreeCompareJTree() {
        XDefaultTTM treeTableModel = createCustomTreeTableModelFromDefault();
        JTree outline = new JTree(treeTableModel);
        TreePath firstPath = outline.getPathForRow(1);
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        outline.collapseRow(0);
        assertTrue("sanity", outline.hasBeenExpanded(firstPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("changed subtree hasBeenExpanded cleared", 
                false,
                outline.hasBeenExpanded(firstPath));
    }

    /**
     * Structure change on substree must clear expanded markers of 
     * descendants. This is how JTree works.
     * 
     * Here we test a visible node: expanded state unchanged
     */
    public void testStructureChangeSubtreeExpanded() {
        TreePath firstPath = outline.getPathForRow(1);
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        assertTrue("sanity", outline.hasBeenExpanded(firstPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("subtree hasbeen expanded changed", 
                true,
                outline.hasBeenExpanded(firstPath));
    }
    /**
     * Structure change on unrelated substree must not
     * clear the expanded markers.
     * Compare tree behaviour.
     */
    public void testStructureChangeSubtreeExpandedCompareJTree() {
        XDefaultTTM treeTableModel = createCustomTreeTableModelFromDefault();
        JTree outline = new JTree(treeTableModel);
        TreePath firstPath = outline.getPathForRow(1);
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        assertTrue("sanity", outline.hasBeenExpanded(firstPath));
        treeTableModel.getTreeModelSupport().fireTreeStructureChanged(firstPath);
        assertEquals("changed subtree hasBeenExpanded cleared", 
                true,
                outline.hasBeenExpanded(firstPath));
    }
    /**
     * test childless root.
     */
    public void testCollapseExpandChildlessRootHidden() {
        DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode("i'm childless") {

            @Override
            public boolean isLeaf() {
                return false;
            }

        };
        DefaultTreeTableModel tree = new DefaultTreeTableModel(node);
//        tree.set
        XOutlineModel outline = new DefaultXOutlineModel(tree);
        TreePath rootPath = outline.getPathForRow(0);
        outline.setRootVisible(false);
        assertTrue("root is expanded", outline.isExpanded(rootPath));
        TableModelReport report = new TableModelReport(outline);
        int rows = outline.getRowCount();
        outline.collapsePath(rootPath);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertFalse("childless folder is collapsed", outline.isExpanded(rootPath));
        assertEquals("collapse hidden childless root must not fire events", 
                0, report.getEventCount());
        report.clear();
        outline.expandPath(rootPath);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertTrue("childless folder is expanded", outline.isExpanded(rootPath));
        assertEquals("collapse hidden childless root must not fire events", 
                0, report.getEventCount());
       
    }

    /**
     * test childless root.
     */
    public void testCollapseExpandChildlessRoot() {
        DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode("i'm childless") {

            @Override
            public boolean isLeaf() {
                return false;
            }

        };
        DefaultTreeTableModel tree = new DefaultTreeTableModel(node);
//        tree.set
        XOutlineModel outline = new DefaultXOutlineModel(tree);
        assertFalse("root is folder", outline.isLeaf(0));
        assertTrue("root is expanded", outline.isExpanded(0));
        TableModelReport report = new TableModelReport(outline);
        int rows = outline.getRowCount();
        int childRow = 0;
        outline.collapseRow(childRow);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertFalse("childless folder is collapsed", outline.isExpanded(childRow));
        assertEquals("must fire an update for root", 1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
        report.clear();
        outline.expandRow(childRow);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertTrue("childless folder is expanded", outline.isExpanded(childRow));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
       
    }
    /**
     * Childless folder as last child of root
     */
    public void testCollapseExpandChildlessFolderLast() {
        
        DefaultMutableTreeTableNode folder = new DefaultMutableTreeTableNode("childless folder") {

            @Override
            public boolean isLeaf() {
                return false;
            }
            
        };
        int childCount = treeTableModel.getChildCount(root);
        int childRow = childCount + 1;
        treeTableModel.insertNodeInto(folder, (MutableTreeTableNode) root, childCount);
        assertEquals("sanity: inserted as last child", folder, outline.getNodeForRow(childRow));
        assertFalse("sanity: not expanded", outline.isExpanded(childRow));
        TableModelReport report = new TableModelReport(outline);
        int rows = outline.getRowCount();
        outline.expandRow(childRow);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertTrue("childless folder is expanded", outline.isExpanded(childRow));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
        report.clear();
        outline.collapseRow(childRow);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertFalse("childless folder is collapsed", outline.isExpanded(childRow));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
       
    }

    /**
     * Childless folder as first child of root.
     */
    public void testCollapseExpandChildlessFolderFirst() {
        
        DefaultMutableTreeTableNode folder = new DefaultMutableTreeTableNode("childless folder") {

            @Override
            public boolean isLeaf() {
                return false;
            }
            
        };
        treeTableModel.insertNodeInto(folder, (MutableTreeTableNode) root, 0);
        assertEquals("sanity: inserted as first child", folder, outline.getNodeForRow(1));
        assertFalse("sanity: not expanded", outline.isExpanded(1));
        TableModelReport report = new TableModelReport(outline);
        int rows = outline.getRowCount();
        outline.expandRow(1);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertTrue("childless folder is expanded", outline.isExpanded(1));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
        report.clear();
        outline.collapseRow(1);
        assertEquals("count not changed", rows, outline.getRowCount());
        assertFalse("childless folder is collapsed", outline.isExpanded(1));
        assertEquals(1, report.getUpdateEventCount());
        assertEquals("update must be the only event fired", 
                report.getUpdateEventCount(), report.getEventCount());
       
    }
    /**
     * Test event mapping for hidden root.
     */
    public void testCollapseExpandHiddenRoot() {
        TreePath rootPath = outline.getPathForRow(0);
        outline.setRootVisible(false);
        TableModelReport report = new TableModelReport(outline);
        outline.collapsePath(rootPath);
        assertEquals("must not fire update for hidden root", 
                report.getDeleteEventCount(), report.getEventCount());
        report.clear();
        outline.expandPath(rootPath);
        assertEquals("must not fire update for hidden root", 
                report.getInsertEventCount(), report.getEventCount());
        
    }
    /**
     * test mapping of expansion to table events: here collapse.
     */
    public void testHasBeenExpanded() {
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        assertEquals("sanity: all expanded", 16, outline.getRowCount());
        // first child 
        TreePath childPath = outline.getPathForRow(1);
        outline.collapseRow(0);
        assertTrue(outline.hasBeenExpanded(childPath));
        outline.expandRow(0);
        assertTrue(outline.isExpanded(childPath));
    }
    
    /**
     * JTree collapse has side-effect of making the path visible.
     */
    public void testCollapseLeaf() {
        TreePath childPath = outline.getPathForRow(1);
        Object grandChild = outline.getChild(childPath.getLastPathComponent(), 0);
        TreePath grandChildPath = childPath.pathByAddingChild(grandChild);
        outline.collapsePath(grandChildPath);
        assertTrue("grandChild must be visible after collapse", 
                outline.getTreePathSupport().isVisible(grandChildPath));
    }

    
    /**
     * JTree expand has side-effect of making the path visible.
     */
    public void testExpandCompareJTree() {
        JTree outline = new JTree();
        TreePath rootPath = outline.getPathForRow(0);
        outline.collapsePath(rootPath);
        Object child = outline.getModel()
            .getChild(rootPath.getLastPathComponent(), 0);
        TreePath childPath = rootPath.pathByAddingChild(child);
        TreeExpansionReport report = new TreeExpansionReport();
        outline.addTreeExpansionListener(report);
        outline.expandPath(childPath);
        assertTrue("child must be expanded", outline.isExpanded(childPath));
        assertTrue("child must be visible after expand", 
                outline.isVisible(childPath));
    }

    /**
     * JTree expand has side-effect of making the path visible.
     */
    public void testExpandVisible() {
        TreePath rootPath = outline.getPathForRow(0);
        outline.collapsePath(rootPath);
        Object child = outline
            .getChild(rootPath.getLastPathComponent(), 0);
        TreePath childPath = rootPath.pathByAddingChild(child);
        outline.expandPath(childPath);
        assertTrue("child must be expanded", outline.isExpanded(childPath));
        assertEquals("TreePathSupport same expanded", outline.isExpanded(childPath),
                outline.getTreePathSupport().isExpanded(childPath));
        assertTrue("child must be visible after expand", 
                outline.isVisible(childPath));
    }
    /**
     * JTree collapse has side-effect of making the path visible.
     */
    public void testCollapseLeafCompareJTree() {
        JTree outline = new JTree();
        TreePath childPath = outline.getPathForRow(1);
        Object grandChild = outline.getModel()
            .getChild(childPath.getLastPathComponent(), 0);
        TreePath grandChildPath = childPath.pathByAddingChild(grandChild);
        outline.collapsePath(grandChildPath);
        assertTrue("grandChild must be visible after collapse", 
                outline.isVisible(grandChildPath));
    }

    /**
     * Issue: TreePathSupport marks leafs as expanded.
     */
    public void testExpandLeaf() {
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        Object grandChild = outline.getNodeForRow(2);
        assertTrue("sanity: grandchild is leaf", outline.isLeaf(grandChild));
        TreePath grandChildPath = outline.getPathForRow(2);
        assertFalse("leaf cannot be expanded", outline.isExpanded(grandChildPath));
        assertEquals("pathSupport must report same expanded as cache",
                outline.getLayout().isExpanded(grandChildPath),
                outline.getTreePathSupport().isExpanded(grandChildPath));
    }
    
    /**
     * expand/collapse must handle null.
     */
    public void testExpandNullPath() {
        outline.getLayout().setExpandedState(null, true);
        outline.getTreePathSupport().expandPath(null);
    }

    /**
     * expand/collapse must handle null.
     */
    public void testCollapseNullPath() {
        outline.getLayout().setExpandedState(null, false);
        outline.getTreePathSupport().collapsePath(null);
    }

    /**
     * test that layout and treePathSupport have same state after collapse.
     * 
     * Security net before moving layout update completely out off
     * broadcaster into pathSupport.
     */
    public void testCollapseUpdateLayout() {
       TreePath path = outline.getPathForRow(0);
       outline.collapsePath(path); 
       assertFalse("root must not be expanded", outline.isExpanded(path));
       assertEquals("support synched with layout", 
               outline.getTreePathSupport().isExpanded(path),
               outline.getLayout().isExpanded(path));
    }
    
    /**
     * test that layout and treePathSupport have same state after collapse.
     * 
     * Security net before moving layout update completely out off
     * broadcaster into pathSupport.
     */
    public void testExpandChildCycleUpdateLayout() {
       TreePath path = outline.getPathForRow(1);
       outline.expandPath(path); 
       assertTrue("path must be expanded", outline.isExpanded(path));
       assertEquals("support synched with layout", 
               outline.getTreePathSupport().isExpanded(path),
               outline.getLayout().isExpanded(path));
       outline.collapsePath(path);
       assertFalse("path must not be expanded", outline.isExpanded(path));
       assertEquals("support synched with layout", 
               outline.getTreePathSupport().isExpanded(path),
               outline.getLayout().isExpanded(path));
    }
    
    /**
     * test that layout and treePathSupport have same state after collapse.
     * 
     * Security net before moving layout update completely out off
     * broadcaster into pathSupport.
     */
    public void testExpandChildCollapseParentUpdateLayout() {
       TreePath childPath = outline.getPathForRow(1);
       outline.expandPath(childPath); 
       TreePath path = outline.getPathForRow(0);
       outline.collapsePath(path);
       assertFalse("path must not be expanded", outline.isExpanded(childPath));
       assertEquals("support synched with layout", 
               outline.getTreePathSupport().isExpanded(childPath),
               outline.getLayout().isExpanded(childPath));
    }
    
    /**
     * Sanity: descendants after various collapse/expands.
     */
    public void testExpandedDescendantsMore() {
        TreePath parent = outline.getPathForRow(0);
        outline.expandRow(1);
        TreePath[] descendants = outline.getTreePathSupport().getExpandedDescendants(parent);
        assertEquals(2, descendants.length);
        outline.collapseRow(0);
        assertEquals(0, outline.getTreePathSupport().getExpandedDescendants(parent).length);
        outline.expandRow(0);
        assertEquals(2, outline.getTreePathSupport().getExpandedDescendants(parent).length);
    }
    
    /**
     * Sanity: be sure to get the assumption about expandedDescandants correct.
     * The expanded node is contained in the collection.
     */
    public void testExpandedDescendants() {
        TreePath parent = outline.getPathForRow(0);
        TreePath[] descendants = outline.getTreePathSupport().getExpandedDescendants(parent);
        assertEquals(1, descendants.length);
        assertEquals("expanded node itself is contained", parent, descendants[0]);
        outline.collapsePath(parent);
        assertEquals(0, outline.getTreePathSupport().getExpandedDescendants(parent).length);
    }
    
    public void testExpandedDescendantsCompareJTree() {
        JTree outline = new JTree();
        TreePath parent = outline.getPathForRow(0);
        Enumeration<TreePath> descendants = outline.getExpandedDescendants(parent);
        assertNotNull(descendants);
        assertTrue(descendants.hasMoreElements());
        assertEquals(parent, descendants.nextElement());
        assertFalse(descendants.hasMoreElements());
    }
    
    /**
     * Test multiple inserts from expanding paths under collapsed
     * parent.
     */
    public void testExpandHiddenEventMapping() {
        TreePath childPath = outline.getPathForRow(1);
        outline.collapseRow(0);
        TableModelReport report = new TableModelReport(outline);
        outline.expandPath(childPath);
        assertEquals(2, report.getInsertEventCount());
    }
    
    /**
     * test mapping of expansion to table events: here collapse.
     */
    public void testCollapseAllExpandedEventMapping() {
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        int expandedCount = outline.getRowCount();
        assertEquals("sanity: all expanded", 16, expandedCount);
        TableModelReport report = new TableModelReport(outline);
        outline.collapseRow(0);
        assertEquals("sanity: root collapsed, children hidden", 1, outline.getRowCount());
        assertEquals("got one update for collapsed row", 1, report.getUpdateEventCount());
        assertEquals("for collapsed row", 0, report.getLastUpdateEvent().getFirstRow());
        assertEquals("got one delete for hidden children", 1, report.getDeleteEventCount());
        assertEquals("for first hidden children", 1, report.getLastDeleteEvent().getFirstRow());
        assertEquals("for last hidden Children", expandedCount - 1, report.getLastDeleteEvent().getLastRow());
    }

    /**
     * test mapping of expansion to table events: here collapse.
     */
    public void testCollapseEventMapping() {
        assertEquals("sanity: root expanded, original count", 4, outline.getRowCount());
        TableModelReport report = new TableModelReport(outline);
        outline.collapseRow(0);
        assertEquals("sanity: root collapsed, children hidden", 1, outline.getRowCount());
        assertEquals("got one update for collapsed row", 1, report.getUpdateEventCount());
        assertEquals("for collapsed row", 0, report.getLastUpdateEvent().getFirstRow());
        assertEquals("got one delete for hidden children", 1, report.getDeleteEventCount());
        assertEquals("for first hidden children", 1, report.getLastDeleteEvent().getFirstRow());
        assertEquals("for last hidden Children", 3, report.getLastDeleteEvent().getLastRow());
    }

    /**
     * test mapping of expansion to table events: here expand.
     */
    public void testExpandAllCollapsedEventMapping() {
        // expand all
        for (int i = 0; i < outline.getRowCount(); i++) {
            outline.expandRow(i);
        }
        int expandedCount = outline.getRowCount();
        assertEquals("sanity: all expanded", 16, expandedCount);
        outline.collapseRow(0);
        assertEquals("sanity: root collapsed, children hidden", 1, outline.getRowCount());
        TableModelReport report = new TableModelReport(outline);
        outline.expandRow(0);
        assertEquals("sanity: root expanded, original count", expandedCount, outline.getRowCount());
        assertEquals("got one update", 1, report.getUpdateEventCount());
        assertEquals("for expanded row", 0, report.getLastUpdateEvent().getFirstRow());
        assertEquals("got one insert for expanded children", 1, report.getInsertEventCount());
        assertEquals("for first shown children", 1, report.getLastInsertEvent().getFirstRow());
        assertEquals("for last shown Children", expandedCount - 1, report.getLastInsertEvent().getLastRow());
    }

    /**
     * test mapping of expansion to table events: here expand. Had previously collapsed.
     */
    public void testExpandEventMapping() {
        outline.collapseRow(0);
        assertEquals("sanity: root collapsed, children hidden", 1, outline.getRowCount());
        TableModelReport report = new TableModelReport(outline);
        outline.expandRow(0);
        assertEquals("sanity: root expanded, original count", 4, outline.getRowCount());
        assertEquals("got one update for collapsed row", 1, report.getUpdateEventCount());
        assertEquals("for collapsed row", 0, report.getLastUpdateEvent().getFirstRow());
        assertEquals("got one insert for expanded children", 1, report.getInsertEventCount());
        assertEquals("for first hidden children", 1, report.getLastInsertEvent().getFirstRow());
        assertEquals("for last hidden Children", 3, report.getLastInsertEvent().getLastRow());
    }

    /**
     * simple expand: expand first child of expanded root.
     */
    public void testExpandFirstMapping() {
        TableModelReport report = new TableModelReport(outline);
        outline.expandRow(1);
        assertEquals("got one update for expanded row", 1, report.getUpdateEventCount());
        assertEquals("expanded == updated", 1, report.getLastUpdateEvent().getFirstRow());
        assertEquals("got one insert for expanded children", 1, report.getInsertEventCount());
        assertEquals("first inserted by expansion", 2, report.getLastInsertEvent().getFirstRow());
        assertEquals("last inserted by expansion", 5, report.getLastInsertEvent().getLastRow());
        
    }
    /**
     * test will expansion listener notification: expanded.
     */
    public void testTreeWillExpansionEventVeto() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        assertFalse("path must not be expanded", outline.isExpanded(path));
        TreeWillExpandReport report = new TreeWillExpandReport(true);
        outline.addTreeWillExpandListener(report);
        outline.expandPath(path);
        assertEquals("path not expanded after veto", false, outline.isExpanded(path));
        assertEquals(1, report.getExpandedEventCount());
        assertEquals(path, report.getLastExpandedEvent().getPath());
        assertEquals(outline, report.getLastExpandedEvent().getSource());
    }
    
    /**
     * Test will expansion listener notification: collapse.
     */
    public void testTreeWillCollapseEventVeto() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        outline.expandPath(path);
        assertTrue("path must not be expanded", outline.isExpanded(path));
        TreeWillExpandReport report = new TreeWillExpandReport(true);
        outline.addTreeWillExpandListener(report);
        outline.collapsePath(path);
        assertEquals("path not collapsed after veto", true, outline.isExpanded(path));
        assertEquals(1, report.getCollapsedEventCount());
        assertEquals(path, report.getLastCollapsedEvent().getPath());
        assertEquals(outline, report.getLastCollapsedEvent().getSource());
    }

    /**
     * test will expansion listener notification: expanded.
     */
    public void testTreeWillExpansionEvent() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        assertFalse("path must not be expanded", outline.isExpanded(path));
        TreeWillExpandReport report = new TreeWillExpandReport();
        outline.addTreeWillExpandListener(report);
        outline.expandPath(path);
        assertEquals("path expanded", true, outline.isExpanded(path));
        assertEquals(1, report.getExpandedEventCount());
        assertEquals(path, report.getLastExpandedEvent().getPath());
        assertEquals(outline, report.getLastExpandedEvent().getSource());
    }
    
    /**
     * Test will expansion listener notification: collapse.
     */
    public void testTreeWillCollapseEvent() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        outline.expandPath(path);
        assertTrue("path must not be expanded", outline.isExpanded(path));
        TreeWillExpandReport report = new TreeWillExpandReport();
        outline.addTreeWillExpandListener(report);
        outline.collapsePath(path);
        assertEquals("path collapsed ", false, outline.isExpanded(path));
        assertEquals(1, report.getCollapsedEventCount());
        assertEquals(path, report.getLastCollapsedEvent().getPath());
        assertEquals(outline, report.getLastCollapsedEvent().getSource());
    }

    /**
     * test expansion listener notification: exanded.
     */
    public void testTreeExpansionEvent() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        assertFalse("path must not be expanded", outline.isExpanded(path));
        TreeExpansionReport report = new TreeExpansionReport();
        outline.addTreeExpansionListener(report);
        outline.expandPath(path);
        assertEquals(1, report.getExpandedEventCount());
        assertEquals(path, report.getLastExpandedEvent().getPath());
        assertEquals(outline, report.getLastExpandedEvent().getSource());
    }
    
    /**
     * Test expansion listener notification: collapse.
     */
    public void testTreeCollapseEvent() {
        // first child of root
        TreePath path = outline.getPathForRow(1);
        outline.expandPath(path);
        assertTrue("path must not be expanded", outline.isExpanded(path));
        TreeExpansionReport report = new TreeExpansionReport();
        outline.addTreeExpansionListener(report);
        outline.collapsePath(path);
        assertEquals(1, report.getCollapsedEventCount());
        assertEquals(path, report.getLastCollapsedEvent().getPath());
        assertEquals(outline, report.getLastCollapsedEvent().getSource());
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
