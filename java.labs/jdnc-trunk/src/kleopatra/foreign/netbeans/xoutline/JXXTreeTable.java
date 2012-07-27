/*
 * $Id: JXXTreeTable.java 3205 2009-08-31 13:18:21Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


package netbeans.xoutline;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.UIAction;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * <p><code>JXTreeTable</code> is a specialized {@link javax.swing.JTable table}
 * consisting of a single column in which to display hierarchical data, and any
 * number of other columns in which to display regular data. The interface for
 * the data model used by a <code>JXTreeTable</code> is
 * {@link org.jdesktop.swingx.treetable.TreeTableModel}. It extends the
 * {@link javax.swing.tree.TreeModel} interface to allow access to cell data by
 * column indices within each node of the tree hierarchy.</p>
 *
 * <p>The most straightforward way create and use a <code>JXTreeTable</code>, is to
 * first create a suitable data model for it, and pass that to a
 * <code>JXTreeTable</code> constructor, as shown below:
 * <pre>
 *  TreeTableModel  treeTableModel = new FileSystemModel(); // any TreeTableModel
 *  JXTreeTable     treeTable = new JXTreeTable(treeTableModel);
 *  JScrollPane     scrollpane = new JScrollPane(treeTable);
 * </pre>
 * See {@link javax.swing.JTable} for an explanation of why putting the treetable
 * inside a scroll pane is necessary.</p>
 *
 * <p>A single treetable model instance may be shared among more than one
 * <code>JXTreeTable</code> instances. To access the treetable model, always call
 * {@link #getTreeTableModel() getTreeTableModel} and
 * {@link #setTreeTableModel(org.jdesktop.swingx.treetable.TreeTableModel) setTreeTableModel}.
 * <code>JXTreeTable</code> wraps the supplied treetable model inside a private
 * adapter class to adapt it to a {@link javax.swing.table.TableModel}. Although
 * the model adapter is accessible through the {@link #getModel() getModel} method, you
 * should avoid accessing and manipulating it in any way. In particular, each
 * model adapter instance is tightly bound to a single table instance, and any
 * attempt to share it with another table (for example, by calling
 * {@link #setModel(javax.swing.table.TableModel) setModel})
 * will throw an <code>IllegalArgumentException</code>!
 *
 * @author Philip Milne
 * @author Scott Violet
 * @author Ramesh Gupta
 */
public class JXXTreeTable extends JXTable {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(JXXTreeTable.class
            .getName());
//    /**
//     * Key for clientProperty to decide whether to apply hack around #168-jdnc.
//     */
//    public static final String DRAG_HACK_FLAG_KEY = "treeTable.dragHackFlag";
//    /**
//     * Key for clientProperty to decide whether to apply hack around #766-swingx.
//     */
//    public static final String DROP_HACK_FLAG_KEY = "treeTable.dropHackFlag";
// 
    private TreeTableModel treeTableModel;
    
    /**
     * Editor used to edit cells within the
     *  {@link #isHierarchical(int) hierarchical} column.
     */
    private BasicTreeTableRenderer hierarchicalEditor;
    
    private BasicTreeTableRenderer hierarchicalRenderer;
    private boolean scrollsOnExpand;
    private boolean showsRootHandles;
    private boolean expandsSelectedPaths;
    private int toggleClickCount;
    private ListToTreeSelectionModelWrapper selectionWrapper;

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    public static final String uiClassID = "TreeTableUI";
    
    /**
     * Initialization that would ideally be moved into various look and feel
     * classes.
     */
    static {
        LookAndFeelAddons.contribute(new TreeTableAddon());
    }
    
    
    
    @Override
    public void updateUI() {
        super.updateUI();
    }
    
    /**
     * Returns a string that specifies the name of the L&F class
     * that renders this component.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }


    /**
     * Constructs a JXTreeTable using a
     * {@link org.jdesktop.swingx.treetable.DefaultTreeTableModel}.
     */
    public JXXTreeTable() {
        this(new DefaultTreeTableModel());
    }

    /**
     * Constructs a JXTreeTable using the specified
     * {@link org.jdesktop.swingx.treetable.TreeTableModel}.
     *
     * @param treeModel model for the JXTreeTable
     */
    public JXXTreeTable(TreeTableModel treeModel) {
        this(new DefaultXOutlineModel(treeModel));
    }
    
    private JXXTreeTable(DefaultXOutlineModel outline) {
        super(outline);
        init();
        initActions();
        super.setSortable(false);
        setShowGrid(false, false);
    }

    private void init() {
        treeTableModel = ((DefaultXOutlineModel) getModel()).getTreeTableModel();
        hierarchicalRenderer = new BasicTreeTableRenderer();
        hierarchicalEditor = new BasicTreeTableRenderer();
        // Force the JTable and JTree to share their row selection models.
         selectionWrapper =
            new ListToTreeSelectionModelWrapper();
        setSelectionModel(selectionWrapper.getListSelectionModel());
        // notify the ui
        firePropertyChange("treeSelectionModel", null, selectionWrapper);
        setRootVisible(false);
        setToggleClickCount(2);
        setExpandsSelectedPaths(true);
    }
    
    
    private void initActions() {
        // Register the actions that this class can handle.
        ActionMap map = getActionMap();
        map.put("expand-all", new Actions("expand-all"));
        map.put("collapse-all", new Actions("collapse-all"));
    }

    /**
     * A small class which dispatches actions.
     * TODO: Is there a way that we can make this static?
     */
    private class Actions extends UIAction {
        Actions(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent evt) {
            if ("expand-all".equals(getName())) {
        expandAll();
            }
            else if ("collapse-all".equals(getName())) {
                collapseAll();
            }
        }
    }

    /** 
     * overridden to do nothing. 
     * 
     * TreeTable is not sortable by default, because 
     * Sorters/Filters currently don't work properly.
     * 
     */
    @Override
    public void setSortable(boolean sortable) {
        // no-op
    }

    
    

    /**
     * Sets the data model for this JXTreeTable to the specified
     * {@link org.jdesktop.swingx.treetable.TreeTableModel}. The same data model
     * may be shared by any number of JXTreeTable instances.
     *
     * @param treeModel data model for this JXTreeTable
     */
    public void setTreeTableModel(TreeTableModel treeModel) {
        TreeTableModel old = getTreeTableModel();
        treeTableModel = treeModel;
        if (getModel() instanceof DefaultXOutlineModel) {
            ((DefaultXOutlineModel) getModel()).setTreeTableModel(treeModel);
        } else {
            setModel(new DefaultXOutlineModel(treeModel));
        }
        firePropertyChange("treeTableModel", old, getTreeTableModel());
    }

    /**
     * Returns the underlying TreeTableModel for this JXTreeTable.
     *
     * @return the underlying TreeTableModel for this JXTreeTable
     */
    public TreeTableModel getTreeTableModel() {
        return treeTableModel;
    }

    /**
     * Convenience cover method to grab the model typed as XOutlineModel.
     * 
     * @return the tableModel casted to an XOutlineModel
     */
    private XOutlineModel getXOutlineModel() {
        return (XOutlineModel) getModel();
    }
    
    
    public int getVisualDepth(int row) {
        return getXOutlineModel().getDepth(row);
    }

    /**
     * <p>Overrides superclass version to make sure that the specified
     * {@link javax.swing.table.TableModel} is compatible with JXTreeTable before
     * invoking the inherited version.</p>
     *
     * <p>Because JXTreeTable internally adapts an
     * {@link org.jdesktop.swingx.treetable.TreeTableModel} to make it a compatible
     * TableModel, <b>this method should never be called directly</b>. Use
     * {@link #setTreeTableModel(org.jdesktop.swingx.treetable.TreeTableModel) setTreeTableModel} instead.</p>
     *
     * <p>While it is possible to obtain a reference to this adapted
     * version of the TableModel by calling {@link javax.swing.JTable#getModel()},
     * any attempt to call setModel() with that adapter will fail because
     * the adapter might have been bound to a different JXTreeTable instance. If
     * you want to extract the underlying TreeTableModel, which, by the way,
     * <em>can</em> be shared, use {@link #getTreeTableModel() getTreeTableModel}
     * instead</p>.
     *
     * @param tableModel must be a TreeTableModelAdapter
     * @throws IllegalArgumentException if the specified tableModel is not an
     * instance of TreeTableModelAdapter
     */
    @Override
    public final void setModel(TableModel tableModel) { // note final keyword
        if (!(tableModel instanceof XOutlineModel)) {
            throw new IllegalArgumentException("TableModel must be of type XOutlineModel");
        }
        super.setModel(tableModel);
    }


//    
//    @Override
//    public void tableChanged(TableModelEvent e) {
//        if (isStructureChanged(e) || isUpdate(e)) {
//            super.tableChanged(e);
//        } else {
//            resizeAndRepaint();
//        }
//    }

    
    /**
     * Throws UnsupportedOperationException because variable height rows are
     * not supported.
     *
     * @param row ignored
     * @param rowHeight ignored
     * @throws UnsupportedOperationException because variable height rows are
     * not supported
     */
    @Override
    public final void setRowHeight(int row, int rowHeight) {
        throw new UnsupportedOperationException("variable height rows not supported");
    }

    /**
     * Sets the row height for this JXTreeTable and forwards the 
     * row height to the renderering tree.
     * 
     * PENDING: delegate to outlineModel? LayoutCache has height?
     * 
     * @param rowHeight height of a row.
     */
    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        getXOutlineModel().setRowHeight(rowHeight);
    }


    /**
     * Collapses all nodes in the treetable.
     */
    public void collapseAll() {
        for (int i = getRowCount() - 1; i >= 0 ; i--) {
            collapseRow(i);
        }
    }

    /**
     * Expands all nodes in the treetable.
     */
    public void expandAll() {
        if (getRowCount() == 0) {
            expandRoot();
        }
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
    }

    /**
     * Expands the root path, assuming the current TreeModel has been set.
     */
    private void expandRoot() {
        TreeModel              model = getTreeTableModel();
        if(model != null && model.getRoot() != null) {
            expandPath(new TreePath(model.getRoot()));
        }
    }

    /**
     * Collapses the node at the specified path in the treetable.
     *
     * @param path path of the node to collapse
     */
    public void collapsePath(TreePath path) {
        getXOutlineModel().collapsePath(path);
    }

    /**
     * Expands the the node at the specified path in the treetable.
     *
     * @param path path of the node to expand
     */
    public void expandPath(TreePath path) {
        getXOutlineModel().expandPath(path);
    }

    /**
     * Makes sure all the path components in path are expanded (except
     * for the last path component) and scrolls so that the 
     * node identified by the path is displayed. Only works when this
     * <code>JTree</code> is contained in a <code>JScrollPane</code>.
     * 
     * (doc copied from JTree)
     * 
     * PENDING: JW - where exactly do we want to scroll? Here: the scroll
     * is in vertical direction only. Might need to show the tree column?
     * 
     * @param path  the <code>TreePath</code> identifying the node to
     *          bring into view
     */
    public void scrollPathToVisible(TreePath path) {
        if (path == null) return;
        expandPath(path);
        int row = getRowForPath(path);
        scrollRowToVisible(row);
    }

    
    /**
     * Collapses the row in the treetable. If the specified row index is
     * not valid, this method will have no effect.
     */
    public void collapseRow(int row) {
        getXOutlineModel().collapseRow(row);
    }

    /**
     * Expands the specified row in the treetable. If the specified row index is
     * not valid, this method will have no effect.
     */
    public void expandRow(int row) {
        getXOutlineModel().expandRow(row);
    }

    
    /**
     * Returns true if the value identified by path is currently viewable, which
     * means it is either the root or all of its parents are expanded. Otherwise,
     * this method returns false.
     *
     * @return true, if the value identified by path is currently viewable;
     * false, otherwise
     */
    public boolean isVisible(TreePath path) {
        return getXOutlineModel().isVisible(path);
    }

    /**
     * Returns the number of visible nodes in the subtree identified by path.
     * 
     * @param path the subtree to count the visible nodes for.
     * @return the number of visible nodes in the subtree
     */
    public int getVisibleChildCount(TreePath path) {
        return getXOutlineModel().getVisibleChildCount(path);
    }
    
    /**
     * Ensures that the node identified by path is currently viewable.
     *
     * @param path  the <code>TreePath</code> to make visible
     */
    public void makeVisible(TreePath path) {
        if(path != null) {
            TreePath        parentPath = path.getParentPath();

            if(parentPath != null) {
                expandPath(parentPath);
            }
        }
        
    }
    
    /**
     * Returns true if the node identified by path is currently expanded.
     * Otherwise, this method returns false.
     *
     * @param path path
     * @return true, if the value identified by path is currently expanded;
     * false, otherwise
     */
    public boolean isExpanded(TreePath path) {
        return getXOutlineModel().isExpanded(path);
    }

    /**
     * Returns true if the node at the specified display row is currently expanded.
     * Otherwise, this method returns false.
     *
     * @param row row
     * @return true, if the node at the specified display row is currently expanded.
     * false, otherwise
     */
    public boolean isExpanded(int row) {
        return getXOutlineModel().isExpanded(row);
    }

    /**
     * Returns true if the node identified by path is currently collapsed, 
     * this will return false if any of the values in path are currently not 
     * being displayed.   
     *
     * @param path path
     * @return true, if the value identified by path is currently collapsed;
     * false, otherwise
     */
    public boolean isCollapsed(TreePath path) {
        return !isExpanded(path);
    }

    /**
     * Returns true if the node at the specified display row is collapsed.
     *
     * @param row row
     * @return true, if the node at the specified display row is currently collapsed.
     * false, otherwise
     */
    public boolean isCollapsed(int row) {
        return !isExpanded(row);
    }

    public boolean isLeaf(int row) {
        return getXOutlineModel().isLeaf(row);
    }
    
    /**
     * Returns an <code>Enumeration</code> of the descendants of the
     * path <code>parent</code> that
     * are currently expanded. If <code>parent</code> is not currently
     * expanded, this will return <code>null</code>.
     * If you expand/collapse nodes while
     * iterating over the returned <code>Enumeration</code>
     * this may not return all
     * the expanded paths, or may return paths that are no longer expanded.
     *
     * @param parent  the path which is to be examined
     * @return an <code>Enumeration</code> of the descendents of 
     *		<code>parent</code>, or <code>null</code> if
     *		<code>parent</code> is not currently expanded
     */
    
    public TreePath[] getExpandedDescendants(TreePath parent) {
    	return getXOutlineModel().getExpandedDescendants(parent);
    }

    
    /**
     * Returns the TreePath for a given x,y location.
     *
     * @param x x value
     * @param y y value
     *
     * @return the <code>TreePath</code> for the givern location.
     */
     public TreePath getPathForLocation(int x, int y) {
        int row = rowAtPoint(new Point(x,y));
        if (row == -1) {
          return null;  
        }
        return getPathForRow(row);
     }

    /**
     * Returns the TreePath for a given row.
     *
     * @param row
     *
     * @return the <code>TreePath</code> for the given row.
     */
     public TreePath getPathForRow(int row) {
        return getXOutlineModel().getPathForRow(row);
     }

     /**
      * Returns the row for a given TreePath.
      *
      * @param path
      * @return the row for the given <code>TreePath</code>.
      */
     public int getRowForPath(TreePath path) {
       return getXOutlineModel().getRowForPath(path);
     }

//------------------------------ exposed Tree properties

     /**
      * Determines whether or not the root node from the TreeModel is visible.
      *
      * @param visible true, if the root node is visible; false, otherwise
      */
     public void setRootVisible(boolean visible) {
         getXOutlineModel().setRootVisible(visible);
     }

     /**
      * Returns true if the root node of the tree is displayed.
      *
      * @return true if the root node of the tree is displayed
      */
     public boolean isRootVisible() {
         return getXOutlineModel().isRootVisible();
     }


    /**
     * Sets the value of the <code>scrollsOnExpand</code> property for the tree
     * part. This property specifies whether the expanded paths should be scrolled
     * into view. In a look and feel in which a tree might not need to scroll
     * when expanded, this property may be ignored.
     *
     * The default value is false.
     * 
     * PENDING implement property change, let UI update
     * 
     * @param scroll true, if expanded paths should be scrolled into view;
     * false, otherwise
     */
    public void setScrollsOnExpand(boolean scroll) {
        boolean oldValue = getScrollsOnExpand();
        this.scrollsOnExpand = scroll;
        firePropertyChange("scrollsOnExpand", oldValue, getScrollsOnExpand());
    }

    /**
     * Returns the value of the <code>scrollsOnExpand</code> property.
     *
     * @return the value of the <code>scrollsOnExpand</code> property
     */
    public boolean getScrollsOnExpand() {
        return scrollsOnExpand;
    }

    /**
     * Sets the value of the <code>showsRootHandles</code> property for the tree
     * part. This property specifies whether the node handles should be displayed.
     * If handles are not supported by a particular look and feel, this property
     * may be ignored.
     * 
     * PENDING implement property change, let UI update
     *
     * @param visible true, if root handles should be shown; false, otherwise
     */
    public void setShowsRootHandles(boolean visible) {
        this.showsRootHandles = visible;
        repaint();
    }

    /**
     * Returns the value of the <code>showsRootHandles</code> property.
     *
     * @return the value of the <code>showsRootHandles</code> property
     */
    public boolean getShowsRootHandles() {
        return showsRootHandles;
    }

    /**
     * Sets the value of the <code>expandsSelectedPaths</code> property for the tree
     * part. This property specifies whether the selected paths should be expanded.
     * 
     * The default value is true.
     * 
     * PENDING implement property change, let UI update
     *
     * @param expand true, if selected paths should be expanded; false, otherwise
     */
    public void setExpandsSelectedPaths(boolean expand) {
        boolean oldValue = getExpandsSelectedPaths();
        this.expandsSelectedPaths = expand;
        firePropertyChange("expandsSelectedPaths", oldValue, getExpandsSelectedPaths());
    }

    /**
     * Returns the value of the <code>expandsSelectedPaths</code> property.
     *
     * @return the value of the <code>expandsSelectedPaths</code> property
     */
    public boolean getExpandsSelectedPaths() {
        return expandsSelectedPaths;
    }


    /**
     * Returns the number of mouse clicks needed to expand or close a node.
     *
     * @return number of mouse clicks before node is expanded
     */
    public int getToggleClickCount() {
        return toggleClickCount;
    }

    /**
     * Sets the number of mouse clicks before a node will expand or close.
     * The default is two. 
     *
     * PENDING implement property change, let UI update
     * @param clickCount the number of clicks required to expand/collapse a node.
     */
    public void setToggleClickCount(int clickCount) {
        this.toggleClickCount = clickCount;
    }

    /**
     * Returns true if the tree is configured for a large model.
     * The default value is false.
     * 
     * @return true if a large model is suggested
     * @see #setLargeModel
     */
    public boolean isLargeModel() {
        return getXOutlineModel().isLargeModel();
    }

    /**
     * Specifies whether the UI should use a large model.
     * (Not all UIs will implement this.) <p>
     * 
     * <strong>NOTE</strong>: this method is exposed for completeness - 
     * currently it's not recommended 
     * to use a large model because there are some issues 
     * (not yet fully understood), namely
     * issue #25-swingx, and probably #270-swingx. 
     * 
     * @param newValue true to suggest a large model to the UI
     */
    public void setLargeModel(boolean newValue) {
        getXOutlineModel().setLargeModel(newValue);
    }

//------------------------------ exposed tree listeners
    
    /**
     * Adds a listener for <code>TreeExpansion</code> events.
     * 
     * TODO (JW): redirect event source to this. 
     * 
     * @param tel a TreeExpansionListener that will be notified 
     * when a tree node is expanded or collapsed
     */
    public void addTreeExpansionListener(TreeExpansionListener tel) {
        getXOutlineModel().addTreeExpansionListener(tel);
    }

    /**
     * Removes a listener for <code>TreeExpansion</code> events.
     * @param tel the <code>TreeExpansionListener</code> to remove
     */
    public void removeTreeExpansionListener(TreeExpansionListener tel) {
        getXOutlineModel().removeTreeExpansionListener(tel);
    }

    /**
     * Adds a listener for <code>TreeSelection</code> events.
     * TODO (JW): redirect event source to this. 
     * 
     * @param tsl a TreeSelectionListener that will be notified 
     * when a tree node is selected or deselected
     */
    public void addTreeSelectionListener(TreeSelectionListener tsl) {
        getTreeSelectionModel().addTreeSelectionListener(tsl);
    }

    /**
     * Removes a listener for <code>TreeSelection</code> events.
     * @param tsl the <code>TreeSelectionListener</code> to remove
     */
    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
        getTreeSelectionModel().removeTreeSelectionListener(tsl);
    }

    /**
     * Adds a listener for <code>TreeWillExpand</code> events.
     * TODO (JW): redirect event source to this. 
     * 
     * @param tel a TreeWillExpandListener that will be notified 
     * when a tree node will be expanded or collapsed 
     */
    public void addTreeWillExpandListener(TreeWillExpandListener tel) {
        getXOutlineModel().addTreeWillExpandListener(tel);
    }

    /**
     * Removes a listener for <code>TreeWillExpand</code> events.
     * @param tel the <code>TreeWillExpandListener</code> to remove
     */
    public void removeTreeWillExpandListener(TreeWillExpandListener tel) {
        getXOutlineModel().removeTreeWillExpandListener(tel);
     }
 
    
    /**
     * Returns the selection model for the tree portion of the this treetable.
     *
     * @return selection model for the tree portion of the this treetable
     */
    public TreeSelectionModel getTreeSelectionModel() {
        return selectionWrapper;    // RG: Fix JDNC issue 41
    }

      /**
       * Copied from JXTreeTableModel.
       */
//    @Override
//    public void tableChanged(TableModelEvent e) {
//        if (isStructureChanged(e) || isUpdate(e)) {
//            super.tableChanged(e);
//        } else {
//            resizeAndRepaint();
//        }
//    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden warn from using it (would love to throw a UnsupportedOperationException
     * but can't as super.super.init calls this): we need
     * a coupled tree- and tableSelectionModel which is not yet ready.
     * So treat it as immutable for now.
     * 
     */
    @Override
    public void setSelectionModel(ListSelectionModel newModel) {
        super.setSelectionModel(newModel);
//        throw new UnsupportedOperationException(
//                "tree/tableSelection models must be coupled - currently immutable");
    }

    /**
     * Determines if the specified column is defined as the hierarchical column.
     * 
     * @param column
     *            zero-based index of the column in view coordinates
     * @return true if the column is the hierarchical column; false otherwise.
     * @throws IllegalArgumentException
     *             if the column is less than 0 or greater than or equal to the
     *             column count
     */
    public boolean isHierarchical(int column) {
        if (column < 0 || column >= getColumnCount()) {
            throw new IllegalArgumentException("column must be valid, was" + column);
        }
        
        return (getHierarchicalColumn() == column);
    }

    /**
     * Returns the index of the hierarchical column. This is the column that is
     * displayed as the tree.
     * 
     * @return the index of the hierarchical column, -1 if there is
     *   no hierarchical column
     * 
     */
    public int getHierarchicalColumn() {
        return convertColumnIndexToView(getXOutlineModel().getHierarchicalColumn());
    }
    
    /**
     * Temporary api to replace the treeCellRenderer (which now isn't ;-)
     * @param hierarchical
     */
//    public void setTreeCellRenderer(DefaultTreeTableRenderer hierarchical) {
//        this.hierarchicalRenderer = hierarchical;
//    }
    /**
     * 
     * PENDING JW: need to support custom tree icons. If the icon
     * is defined in a "normal" wrappee it leads to both default node icon
     * and icon in content. Currently, the TreePanelProvider checks if
     * the wrappee is a WrappingProvider and if so, uses its StringValue
     * and the provider's wrappee. Need to simplify/expose in api?
     *  
     * {@inheritDoc}
     */
    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        TableCellRenderer renderer = super.getCellRenderer(row, column);
        if (isHierarchical(column) && (renderer instanceof DefaultTableRenderer)) {
            //PENDING JW: would like to hide this in the ui
            // override BasicTableUI.paintCell would be the exact place
            // buut: the complete paintXX support methods are privat 
            ComponentProvider<?> provider = ((DefaultTableRenderer) renderer).getComponentProvider();
            ((BasicTreeTablePanelProvider) hierarchicalRenderer.getComponentProvider()).setWrappee(provider);
            return hierarchicalRenderer;
        }
        
        return renderer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor editor = super.getCellEditor(row, column);
        if (isHierarchical(column)) {
            hierarchicalEditor.setCellEditorDelegate(editor);
            return hierarchicalEditor;
        }
        
        return editor;
    }
    
    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to message the tree directly if the column is the view index of
     * the hierarchical column. <p>
     * 
     * PENDING JW: revisit once we switch to really using a table renderer. As is, it's
     * a quick fix for #821-swingx: string rep for hierarchical column incorrect.
     */
    @Override
    public String getStringAt(int row, int column) {
        return super.getStringAt(row, column);
    }


    /**
     * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
     * to listen for changes in the ListSelectionModel it maintains. Once
     * a change in the ListSelectionModel happens, the paths are updated
     * in the DefaultTreeSelectionModel.
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
        /** Set to true when we are updating the ListSelectionModel. */
        protected boolean updatingListSelectionModel;

        public ListToTreeSelectionModelWrapper() {
            super();
            getListSelectionModel().addListSelectionListener
                (createListSelectionListener());
        }

        /**
         * Returns the list selection model. ListToTreeSelectionModelWrapper
         * listens for changes to this model and updates the selected paths
         * accordingly.
         */
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }

        /**
         * This is overridden to set <code>updatingListSelectionModel</code>
         * and message super. This is the only place DefaultTreeSelectionModel
         * alters the ListSelectionModel.
         */
        @Override
        public void resetRowSelection() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                }
                finally {
                    updatingListSelectionModel = false;
                }
            }
            // Notice how we don't message super if
            // updatingListSelectionModel is true. If
            // updatingListSelectionModel is true, it implies the
            // ListSelectionModel has already been updated and the
            // paths are the only thing that needs to be updated.
        }

        /**
         * Creates and returns an instance of ListSelectionHandler.
         */
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }

        /**
         * If <code>updatingListSelectionModel</code> is false, this will
         * reset the selected paths from the selected rows in the list
         * selection model.
         */
        protected void updateSelectedPathsFromSelectedRows() {
            if (!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    if (listSelectionModel.isSelectionEmpty()) {
                        clearSelection();
                    } else {
                        // This is way expensive, ListSelectionModel needs an
                        // enumerator for iterating.
                        int min = listSelectionModel.getMinSelectionIndex();
                        int max = listSelectionModel.getMaxSelectionIndex();

                        List<TreePath> paths = new ArrayList<TreePath>();
                        for (int counter = min; counter <= max; counter++) {
                            if (listSelectionModel.isSelectedIndex(counter)) {
                                TreePath selPath = getPathForRow(counter);

                                if (selPath != null) {
                                    paths.add(selPath);
                                }
                            }
                        }
                        setSelectionPaths(paths.toArray(new TreePath[paths.size()]));
                        // need to force here: usually the leadRow is adjusted 
                        // in resetRowSelection which is disabled during this method
                        leadRow = leadIndex;
                    }
                }
                finally {
                    updatingListSelectionModel = false;
                }
            }
        }

        /**
         * Class responsible for calling updateSelectedPathsFromSelectedRows
         * when the selection of the list changse.
         */
        class ListSelectionHandler implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateSelectedPathsFromSelectedRows();
                }
            }
        }
    }

    /**
     * Returns the adapter that knows how to access the component data model.
     * The component data adapter is used by filters, sorters, and highlighters.
     *
     * @return the adapter that knows how to access the component data model
     */
    @Override
    protected ComponentAdapter getComponentAdapter() {
        if (dataAdapter == null) {
            dataAdapter = new OutlineAdapter(this);
        }
        return dataAdapter;
    }

    // PENDING JW: currently not sortable/filterable view == model row
    public static class OutlineAdapter extends TableAdapter {
        
        public OutlineAdapter(JXXTreeTable component) {
            super(component);
        }

        @Override
        public int getDepth() {
            XOutlineModel model = getOutlineModel();
            if (model != null) {
                return model.getDepth(row);
            }
            return super.getDepth();
        }

        @Override
        public boolean isExpanded() {
            return getOutline().isExpanded(row);
        }

        @Override
        public boolean isHierarchical() {
            return getOutline().isHierarchical(column);
        }

        @Override
        public boolean isLeaf() {
            return getOutline().isLeaf(row);
        }
        
        protected XOutlineModel getOutlineModel() {
            return getOutline().getXOutlineModel();
        }
        
        public JXXTreeTable getOutline() {
            return (JXXTreeTable) getTable();
        }
    }

}
