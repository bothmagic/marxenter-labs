/*
 * Created on 13.06.2008
 *
 */
package netbeans.xoutline;

import java.beans.PropertyChangeListener;

import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.TableModel;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Internal model for a TreeTable. It is-a TableModel, TreeModel and has
 * additional responsibilities of 
 * <ul>
 *  <li>expansion control 
 *  <li> treePath <--> row conversion functionality.
 * </li> 
 * 
 * While this is inspired by the original OutlineModel (Netbeans) its role 
 * shifted and moved more into the background. The model for a SwingX TreeTable 
 * is a TreeTableModel. The XOutlineModel replaces both the rendering tree and
 * the (internal) TreeTableModelAdapter. Client code should not talk to this
 * directly. The one exception might be the BasicTreeTableUI which is still very
 * much in evolution. <p>
 * 
 * Compared with the original OutlineModel (Netbeans) it exposes the functionality
 * of its internal controllers in terms of cover methods. The goal is to hide the
 * internal controller altogether. Actually, I don't think that there's much
 * need for an interface - don't expect too many implementations because the
 * details of getting it correct are really complex.<p>
 * 
 * 
 * 
 */
public interface XOutlineModel extends TableModel, TreeModel {

    //--------------- conversion
    public Object getNodeForRow(int row);
    
    public TreePath getPathForRow(int row);
    public int getRowForPath(TreePath path);
    
    // ------------- treeModel in row coordinates
    
    public boolean isLeaf(int row);
    
    //-------------------- expansion/collapse state
    public void expandRow(int row);
    public void expandPath(TreePath path);
    
    public void collapseRow(int row);
    public void collapsePath(TreePath path);
    
//    public void toggleExpansion(int row);
    
    public boolean isExpanded(int row);
    public boolean isExpanded(TreePath path);
    public boolean hasBeenExpanded(TreePath grandChildPath);
    

    public TreePath[] getExpandedDescendants(TreePath parent);
    
    public void addTreeExpansionListener(TreeExpansionListener tel);
    public void removeTreeExpansionListener(TreeExpansionListener tel);
    public TreeExpansionListener[] getTreeExpansionListeners();
    
    public void addTreeWillExpandListener(TreeWillExpandListener tel);
    public void removeTreeWillExpandListener(TreeWillExpandListener tel);
    public TreeWillExpandListener[] getTreeWillExpandListeners();
    
    //----------------- layoutCache
    
    /**
     * Sets the largeModel property.
     * 
     * Note: whether or not the new value is taken may depend on the
     * implementation (or more precisely: on the LayoutCache used).
     * 
     * @param the new value.
     */
    public void setLargeModel(boolean largeModel);
    
    /** 
     * Determine if the model is a large-model.  Large model trees keep less
     * internal state information, relying on the TreeModel more.  Essentially
     * they trade performance for scalability. An OutlineModel may be large
     * model or small model; primarily this affects the type of layout cache
     * used, just as it does with JTree. 
     */
    public boolean isLargeModel();

    
    public int getDepth(int row);

    public void setRowHeight(int rowHeight);
    int getRowHeight();

    boolean isRootVisible();
    public void setRootVisible(boolean visible);

    public boolean isVisible(TreePath path);
    int getVisibleChildCount(TreePath parent);
    
    void addPropertyChangeListener(PropertyChangeListener l);
    void removePropertyChangeListener(PropertyChangeListener l);

    //---   TreeTableModel api
    public int getHierarchicalColumn();

    // internal controllers
    
    /** 
     * Get the <code>TreePathSupport</code> object this model uses to manage
     * information about expanded nodes.  <code>TreePathSupport</code> implements
     * logic for tracking expanded nodes, manages <code>TreeWillExpandListener</code>s,
     * and is a repository for preserving expanded state information about nodes whose parents
     * are currently collapsed.  JTree implements very similar logic internally
     * to itself.
     * <p>
     * <i>(PENDING) It is not yet determined if TreePathSupport will remain a
     * public class.</i>
     * 
     * PENDING JW: should not be exposed (if possible) - model should know it all.
     */
    public TreePathSupport getTreePathSupport ();
    
    /** 
     * Get the layout cache which is used to track the visual state of nodes.
     * This is typically one of the standard JDK layout cache classes, such
     * as <code>VariableHeightLayoutCache</code> or <code>
     * FixedHeightLayoutCache</code>.  
     * 
     * PENDING JW: should not be exposed (if possible) - model should know it all.
     */
    public AbstractLayoutCache getLayout ();

}
