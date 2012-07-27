/*
 * Created on 13.06.2008
 *
 */
package netbeans.xoutline;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.FixedHeightLayoutCache;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.VariableHeightLayoutCache;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * Implementation based on TreeTableModel
 */
public class DefaultXOutlineModel extends AbstractBean 
    implements XOutlineModel {

    private TreeTableModel treeTableModel;

    private AbstractLayoutCache layout;
    private TreePathSupport treePathSupport;
    // visibility widened for testing
    XOutlineEventBroadcaster broadcaster;

    private boolean largeModel;
    
    public DefaultXOutlineModel() {
        this(null);
    }
    
    public DefaultXOutlineModel(TreeTableModel model) {
        
        treePathSupport = new TreePathSupport(this);
        layout = new VariableHeightLayoutCache();
        layout.setModel(this);
        // PENDING JW: pathSupport must be initialized before broadcaster
        // position of layout cache is irrelevant
        broadcaster = new XOutlineEventBroadcaster(this);
        
        setRootVisible(true);
        setTreeTableModel(model);
    }
 
    public void setTreeTableModel(TreeTableModel treeTableModel) {
        Object oldValue = getTreeTableModel();
        this.treeTableModel = treeTableModel;
        firePropertyChange("treeTableModel", oldValue, getTreeTableModel());
    }

    public TreeTableModel getTreeTableModel() {
        return treeTableModel;
    }
    
    // ------------------ convenience (temporary)
    TreeModel getTreeModel() {
        return treeTableModel;
    }



// -------------- xoutlineModel
    
    // ------------------- conversion
    
    @Override
    public Object getNodeForRow(int row) {
        TreePath path = getLayout().getPathForRow(row);
        return path != null ? path.getLastPathComponent() : null;
    }
    
    @Override
    public TreePath getPathForRow(int row) {
        return getLayout().getPathForRow(row);
    }
    
    @Override
    public int getRowForPath(TreePath path) {
        return getLayout().getRowForPath(path);
    }

    @Override
    public boolean isLeaf(int row) {
        Object node = getNodeForRow(row);
        return isLeaf(node);
    }

    //----------------- expansion/collapse
    @Override
    public void expandRow(int row) {
        expandPath(getPathForRow(row));
    }
    
    public void expandPath(TreePath path) {
        getTreePathSupport().expandPath(path);
    }

    @Override
    public void collapseRow(int row) {
        collapsePath(getPathForRow(row));
    }
    
    public void collapsePath(TreePath path) {
        getTreePathSupport().collapsePath(path);
    }
    
    
    
    @Override
    public boolean isExpanded(int row) {
        TreePath path = getPathForRow(row);
        return isExpanded(path); 
    }
    
    
    @Override
    public boolean isExpanded(TreePath path) {
        return getLayout().isExpanded(path);
    }
   
    
    /**
     * Returns a flag indicating whether the given path
     * has been expanded at some time.
     * 
     * @param path the path the check. 
     * @return a flag indicating whether the given path has
     *  been expanded, or false if it is null.
     */
    @Override
    public boolean hasBeenExpanded(TreePath path) {
        return getTreePathSupport().hasBeenExpanded(path);
    }
    
    @Override
    public TreePath[] getExpandedDescendants(TreePath parent) {
        return getTreePathSupport().getExpandedDescendants(parent);
    }


    @Override
    public void addTreeWillExpandListener(TreeWillExpandListener tel) {
        getTreePathSupport().addTreeWillExpandListener(tel);
        
    }

    @Override
    public void removeTreeWillExpandListener(TreeWillExpandListener tel) {
        getTreePathSupport().removeTreeWillExpandListener(tel);
        
    }

    
    @Override
    public TreeExpansionListener[] getTreeExpansionListeners() {
        return getTreePathSupport().getTreeExpansionListeners();
    }

    @Override
    public TreeWillExpandListener[] getTreeWillExpandListeners() {
        return getTreePathSupport().getTreeWillExpandListeners();
    }

    @Override
    public void addTreeExpansionListener(TreeExpansionListener tel) {
        getTreePathSupport().addTreeExpansionListener(tel);
        
    }

    @Override
    public void removeTreeExpansionListener(TreeExpansionListener tel) {
        getTreePathSupport().removeTreeExpansionListener(tel);
        
    }


    //--------------------- layout
    
    @Override
    public int getDepth(int row) {
        return getDepth(getPathForRow(row));
    }
    
    public int getDepth(TreePath path) {
        if (path == null) return 0;
        int off = isRootVisible() ? 0 : 1;
        int depth = Math.max(path.getPathCount() - off, 0);
        return depth;
    }

    @Override
    public boolean isRootVisible() {
       return getLayout().isRootVisible(); 
    }
    
    
    @Override
    public void setRootVisible(boolean visible) {
        boolean oldValue = isRootVisible();
        getLayout().setRootVisible(visible);
        firePropertyChange("rootVisible", oldValue, isRootVisible());
    }

    @Override
    public boolean isVisible(TreePath path) {
        return getTreePathSupport().isVisible(path);
    }

    @Override
    public int getVisibleChildCount(TreePath parent) {
        return getLayout().getVisibleChildCount(parent);
    }


    /**
     * Sets the rowHeight. This is a bound property.
     * 
     * Default value is 0.
     * 
     * @param the new rowHeight.
     */
    @Override
    public void setRowHeight(int rowHeight) {
        int oldValue = getRowHeight();
        getLayout().setRowHeight(rowHeight);
        firePropertyChange("rowHeight", oldValue, getRowHeight());
    }
    
    @Override
    public int getRowHeight() {
        return getLayout().getRowHeight();
    }
    
    //------------------ treetableModel api
    

    @Override
    public int getHierarchicalColumn() {
        return treeTableModel.getHierarchicalColumn();
    }


    
    //------------------ outlineModel
    @Override
    public AbstractLayoutCache getLayout() {
        return layout;
    }

    @Override
    public TreePathSupport getTreePathSupport() {
        return treePathSupport;
    }

    /**
     * {@inheritDoc}<p>
     * 
     * The default value is false. Allows true only if getRowHeight > 0.
     * 
     */
    @Override
    public void setLargeModel(boolean largeModel) {
        if (getRowHeight() < 1) largeModel = false;
        boolean oldValue = isLargeModel();
        if (oldValue == largeModel) return;
        this.largeModel = largeModel;
        updatedLayoutCache();
        firePropertyChange("largeModel", oldValue, isLargeModel());
        // PENDING if layout property is exposed, should fire a propertyChange
        // on replacing
    }

    /**
     * Called on change of property largeModel (only if changed). Creates 
     * a new LayoutCache and configures with the state of the old, as 
     * appropriate.
     * 
     * Here:
     *  - rootVisible
     *  - model
     *  - rowHeight
     *  
     *  PENDING JW: BasicTreeUI does more (nodeDimension? ComponentListener?)
     */
    private void updatedLayoutCache() {
        boolean rootVisible = isRootVisible();
        int rowHeight = getRowHeight();
        if (layout != null) {
            layout.setModel(null);
        }
        if (isLargeModel()) {
            layout = new FixedHeightLayoutCache();
        } else {
            layout = new VariableHeightLayoutCache();
        }
        layout.setModel(this);
        layout.setRootVisible(rootVisible);
        layout.setRowHeight(rowHeight);
    }

    @Override
    public boolean isLargeModel() {
        return largeModel;
    }

    
// --------------- TableModel    
    @Override
    public Class<?> getColumnClass(int column) {
        return treeTableModel.getColumnClass(column);
    }

    @Override
    public int getColumnCount() {
        return treeTableModel != null ? treeTableModel.getColumnCount() : 0;
    }

    @Override
    public String getColumnName(int column) {
        return treeTableModel.getColumnName(column);
    }

    @Override
    public int getRowCount() {
        return getLayout().getRowCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object node = getNodeForRow(rowIndex);
        return treeTableModel.getValueAt(node, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        Object node = getNodeForRow(rowIndex);
        return treeTableModel.isCellEditable(node, columnIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Object node = getNodeForRow(rowIndex);
        treeTableModel.setValueAt(value, node, columnIndex);

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        broadcaster.removeTableModelListener(l);

    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        broadcaster.addTableModelListener(l);
    }

    
//------------------ TreeModel    
    @Override
    public Object getChild(Object parent, int index) {
        return treeTableModel.getChild(parent, index);
    }

    @Override
    public int getChildCount(Object parent) {
        return treeTableModel.getChildCount(parent);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return treeTableModel.getIndexOfChild(parent, child);
    }

    @Override
    public Object getRoot() {
        return treeTableModel != null ? treeTableModel.getRoot() : null;
    }

    @Override
    public boolean isLeaf(Object node) {
        return treeTableModel.isLeaf(node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // TODO Auto-generated method stub

    }
    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        broadcaster.removeTreeModelListener(l);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        broadcaster.addTreeModelListener(l);
    }


}
