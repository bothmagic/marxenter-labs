/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package netbeans.xoutline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.util.Contract;


/** Responsible for handling tree model events from the user-supplied treemodel
 * portion of a DefaultOutlineModel, translating them into appropriate 
 * TableModelEvents and refiring these events to listeners on the table model.
 * <p>
 * This class could be (and originally was) incorporated directly into 
 * DefaultOutlineModel, but is separated for better readability and separation
 * of concerns.
 *
 * @author  Tim Boudreau
 * 
 * changed: removed TableModelListener - all model change notification from the outline 
 * come via tree events
 * 
 */
class XOutlineEventBroadcaster implements TreeModelListener, 
    ExtTreeWillExpandListener, TreeExpansionListener, PropertyChangeListener {
    
    
    /** The model we will proxy */
    private DefaultXOutlineModel model;
    
    /** The last event sent to treeWillExpand/Collapse, used to compare against the
     * next value sent to treeExpanded/Collapse */
    private TreeExpansionEvent inProgressEvent = null;
    

    /** Are we in the middle of firing multiple TableModelEvents for a single
     * TreeModelEvent. */
    private boolean inMultiEvent = false;
    
    //Some constants we use to have a single method handle all translated
    //event firing
    private static final int NODES_CHANGED = 0;
    private static final int NODES_INSERTED = 1;
    private static final int NODES_REMOVED = 2;
    private static final int STRUCTURE_CHANGED = 3;
    

    /** List of table model listeners */
    private List<TableModelListener> tableListeners = new ArrayList<TableModelListener>();
    
    /** List of tree model listeners */
    private List<TreeModelListener> treeListeners = new ArrayList<TreeModelListener>();

    private PendingExpansionState pendingExpansionState;
    
    
    /** Creates a new instance of EventBroadcaster which will
     * produce events for the passed DefaultOutlineModel model.  */
    public XOutlineEventBroadcaster(DefaultXOutlineModel model) {
        setModel (model);
    }
    
    
//***************** Bean properties/convenience getters & setters ************    
    /** Flag which is set to true while multiple TableModelEvents generated
     * from a single TreeModelEvent are being fired, so clients can avoid
     * any model queries until all pending changes have been fired.  The
     * main thing to avoid is any mid-process repaints, which can only happen
     * if the response to an event will be to call paintImmediately(). 
     * <p>
     * This value is guaranteed to be true for the first of a group of
     * related events, and false if tested in response to the final event.
     */
    public boolean areMoreEventsPending() {
        return inMultiEvent;
    }
    
    /** Get the outline model for which this broadcaster will proxy events*/
    private DefaultXOutlineModel getModel() {
        return model;
    }
    
    /** Set the outline model this broadcaster will proxy events for */
    private void setModel(DefaultXOutlineModel model) {
        this.model = model;
        getTreePathSupport().addTreeExpansionListener(this);
        getTreePathSupport().addTreeWillExpandListener(this);
        model.addPropertyChangeListener(this);
        installTreeModelListeners();
    }
    

    /**
     * Invoked if the outline's treeTableModel property changed.
     * 
     * @param oldValue the old value, may be null.
     */
    private void treeModelChanged(TreeModel oldValue) {
        uninstallTreeModelListeners(oldValue);
        installTreeModelListeners();
        rootChanged();
    }

    /** 
     * Notifies registered TreeModelListeners that the tree's root has
     * been replaced. Can cope with a null root.
     */
    private void rootChanged() {

        Object root = getModel().getRoot();
        /*
         * Undocumented. I think it is the only reasonable/possible solution to
         * use use null as path if there is no root. TreeModels without root
         * aren't important anyway, since JTree doesn't support them (yet).
         */
        TreePath path = (root != null) ? new TreePath(root) : null;
        treeStructureChanged(new TreeModelEvent(getModel(), path));
    }

    private void installTreeModelListeners() {
        if (getTreeModel() != null) {
            getTreeModel().addTreeModelListener(this); 
        }
    }
    
    private void uninstallTreeModelListeners(TreeModel oldModel) {
        if (oldModel != null) {
            oldModel.removeTreeModelListener(this);
        }
        
    }

    /** Convenience getter for the proxied model's layout cache */
    private AbstractLayoutCache getLayout() {
        return getModel().getLayout();
    }
    
    /** Convenience getter for the proxied model's TreePathSupport */
    private TreePathSupport getTreePathSupport() {
        return getModel().getTreePathSupport();
    }
    
    /** Convenience getter for the proxied model's user-supplied TreeModel */
    private TreeModel getTreeModel() {
        return getModel().getTreeModel();
    }
    
    
//******************* Event source implementation **************************
    
    /** Add a table model listener.  All events fired by this EventBroadcaster
     * will have the OutlineModel as the event source */
    public synchronized void addTableModelListener(TableModelListener l) {
        tableListeners.add (l);
    }
    
    /** Add a tree model listener.  All events fired by this EventBroadcaster
     * will have the OutlineModel as the event source */
    public synchronized void addTreeModelListener(TreeModelListener l) {
        treeListeners.add (l);
    }    
    
    /** Remove a table model listener.  */
    public synchronized void removeTableModelListener(TableModelListener l) {
        tableListeners.remove(l);
    }
    
    /** Remove a tree model listener.  */
    public synchronized void removeTreeModelListener(TreeModelListener l) {
        treeListeners.remove(l);
    }
    
    /** Fire a table change to the list of listeners supplied. The event should
     * already have its source set to be the OutlineModel we're proxying for. */
    private void fireTableChange (TableModelEvent e, TableModelListener[] listeners) {
        //Event may be null for offscreen info, etc.
//        if (e == null) {
//            return;
//        }
        
        assert (e.getSource() == getModel());
        
        for (int i=0; i < listeners.length; i++) {
            listeners[i].tableChanged(e);
        }
    }
    
    /** Convenience method to fire a single table change to all listeners */
    private void fireTableChange (TableModelEvent e) {
        //Event may be null for offscreen info, etc.
        // JW: not exactly - table interprets a null as structureChanged
//        if (e == null) {
//            return;
//        }
        inMultiEvent = false;
        fireTableChange(e, getTableModelListeners());
    }
    
    /** Fires multiple table model events, setting the inMultiEvent flag
     * as appropriate. */
    private void fireTableChange (TableModelEvent[] e) {
        //Event may be null for offscreen info, etc.
        if (e == null || e.length==0) {
            return;
        }
        
        TableModelListener[] listeners = getTableModelListeners();
        inMultiEvent = e.length > 1;
        try {
            for (int i=0; i < e.length; i++) {
                fireTableChange (e[i], listeners);
                if (i == e.length-1) {
                    inMultiEvent = false;
                }
            }
        } finally {
            inMultiEvent = false;
        }
    }
    
    /** Fetch an array of the currently registered table model listeners */
    private TableModelListener[] getTableModelListeners() {
        TableModelListener[] listeners = null;
        synchronized (this) {
            listeners = new TableModelListener[
                tableListeners.size()];
            
            listeners = tableListeners.toArray(listeners);
        }
        return listeners;
    }
    
    /** Fire the passed TreeModelEvent of the specified type to all
     * registered TreeModelListeners.  The passed event should already have
     * its source set to be the model. */
    private synchronized void fireTreeChange (TreeModelEvent e, int type) {
        //Event may be null for offscreen info, etc.
        if (e == null) {
            return;
        }
        assert (e.getSource() == getModel());
        
        TreeModelListener[] listeners = null;
        synchronized (this) {
            listeners = new TreeModelListener[treeListeners.size()];
            listeners = treeListeners.toArray(listeners);
        }
        
        //Now refire it to any listeners
        for (int i=0; i < listeners.length; i++) {
            switch (type) {
                case NODES_CHANGED :
                    listeners[i].treeNodesChanged(e);
                    break;
                case NODES_INSERTED :
                    listeners[i].treeNodesInserted(e);
                    break;
                case NODES_REMOVED :
                    listeners[i].treeNodesRemoved(e);
                    break;
                case STRUCTURE_CHANGED :
                    listeners[i].treeStructureChanged(e);
                    break;
                default :
                    assert false;
            }
        }
    }    
    
//******************* Event listener implementations ************************    
 
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("rootVisible".equals(evt.getPropertyName())) {
            rootVisibleChanged();
        } else if ("treeTableModel".equals(evt.getPropertyName())) {
            treeModelChanged((TreeModel) evt.getOldValue());
        }
        
    }

    private void rootVisibleChanged() {
        // nothing to show/hide
        if (getModel().getRoot() == null)
            return;
        if (getModel().isRootVisible()) {
            fireTableRowInserted(0);
        } else {
            fireTableRowRemoved(0);
        }

    }

    private void fireTableRowRemoved(int row) {
        fireTableChange(createTableRowEvent(row, TableModelEvent.DELETE));
    }
    
    private void fireTableRowInserted(int row) {
        fireTableChange(createTableRowEvent(row, TableModelEvent.INSERT));
    }

    
    private TableModelEvent createTableRowEvent(int row, int type) {
        return createTableRowEvent(row, row, type);
    }

    private TableModelEvent createTableRowEvent(int first, int last, int type) {
        return new TableModelEvent(getModel(), first, last, TableModelEvent.ALL_COLUMNS, type);
    }


   
    /** Process a change event from the user-supplied tree model.
     * Order of operations: 
     * <ol>
     * <li>Create one or more table model events (more than one if the
     * incoming event affects discontiguous rows) reflecting the effect
     * of the tree change</li>
     * <li>Call the method with the same signature as this one on the
     * layout cache, so it will update its state appropriately</li>
     * <li>Refire the same tree event with the OutlineModel we're
     *   proxying as the source</li>
     * <li>Fire the generated TableModelEvent(s)</li></ol>
     */
    public void treeNodesChanged(TreeModelEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e == null) return;
        
        TableModelEvent[] events = translateEvent(e, NODES_CHANGED);
        getLayout().treeNodesChanged(e);
        fireTreeChange (translateEvent(e), NODES_CHANGED);
        fireTableChange(events);
    }
    
    /** Process a node insertion event from the user-supplied tree model 
     * Order of operations: 
     * <ol>
     * <li>Create one or more table model events (more than one if the
     * incoming event affects discontiguous rows) reflecting the effect
     * of the tree change</li>
     * <li>Call the method with the same signature as this one on the
     * layout cache, so it will update its state appropriately</li>
     * <li>Refire the same tree event with the OutlineModel we're
     *   proxying as the source</li>
     * <li>Fire the generated TableModelEvent(s)</li></ol>
     */
    public void treeNodesInserted(TreeModelEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e == null) return;
        
        TableModelEvent[] events = translateEvent(e, NODES_INSERTED);
        getLayout().treeNodesInserted(e);
        fireTreeChange (translateEvent(e), NODES_INSERTED);
        fireTableChange(events);
    }
    
    /** Process a node removal event from the user-supplied tree model 
     * Order of operations: 
     * <ol>
     * <li>Create one or more table model events (more than one if the
     * incoming event affects discontiguous rows) reflecting the effect
     * of the tree change</li>
     * <li>Call the method with the same signature as this one on the
     * layout cache and path support, so they will update their state appropriately</li>
     * <li>Refire the same tree event with the OutlineModel we're
     *   proxying as the source</li>
     * <li>Fire the generated TableModelEvent(s)</li>
     * </ol>
     */
    public void treeNodesRemoved(TreeModelEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e == null) return;
        // create the table mapped events before doing anything else (need
        // expansion state before internal updates)
        TableModelEvent[] events = translateEvent(e, NODES_REMOVED);
        // update layout and path internals
        // PENDING JW: move to pathSupport?
        // contra would be that we neede to pass the event
        getLayout().treeNodesRemoved(e);
        getTreePathSupport().treeNodesRemoved(e.getTreePath(), e.getChildren());
        // fire event
        fireTreeChange (translateEvent(e), NODES_REMOVED);
        fireTableChange(events);
    }
    
    /** Process a structural change event from the user-supplied tree model.
     * This will result in a generic &quot;something changed&quot; 
     * TableModelEvent being fired.  */
    public void treeStructureChanged(TreeModelEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e == null) return;
        
        // PENDING JW: move to pathSupport?
        // contra would be that we neede to pass the event
        getLayout().treeStructureChanged(e);
        getTreePathSupport().treeStructureChanged(e.getTreePath());
        
        fireTreeChange (translateEvent(e), STRUCTURE_CHANGED);
        fireTableChange (mapTreeStructureChanged(e));
    }
    
    /**
     * Maps a tree structure changed to a table event which must be 
     * either a structure changed or dataChanged as appropriate. Subclasses
     * can override to implement a different strategy.<p>
     * 
     * Here: same as old JXTreeTable, that is structureChange on the root 
     * is mapped to structureChange, on other subtrees mapped to dataChanged.<p>
     * 
     * PENDING JW: add a pluggable handler
     * 
     * @param e TreeModelEvent received in treeStructureChanged
     * @return returns a table event of type structureChanged or dataChanged. May be 
     *   null to denote a structureChanged.
     */
    protected TableModelEvent mapTreeStructureChanged(TreeModelEvent e) {
        if ((e.getTreePath() == null) || (e.getTreePath().getParentPath() == null)) {
            // map to table structure changed
            return new TableModelEvent(getModel(), TableModelEvent.HEADER_ROW);
        }
        return new TableModelEvent(getModel());
    }

    
//------------------------- Handle expansion events.
    
    /**
     * Encapsulates logic to map expansion events into appropriate TableModelEvents. <p>
     *  
     */
    public class PendingExpansionState {
        boolean expand;
        /** the path that will be expanded/collapsed, assumed to be not-null*/
        TreePath pendingPath;
        /** 
         * The row for the pendingPath. This will always be >= 0 for a well-behaved
         * expansion controller (as the TreePathSupport) which must expand all 
         * parents before doing a expand/collapse to the path it really is interested.
         */
        int rowForPendingPath;
        /** State for pending expand. It is the path to the next row. */ 
        TreePath pathAfter;
        /**
         *  State for pending collapse. It is the row previous to the youngest
         *  kin (sister or aunt) or the last row of the model, if we are youngest.
         */
        int lastRow;

        /**
         * Clears internal state. Clients should call this before throwing away the
         * instance to cleanup pending references.
         */
        public void reset() {
            rowForPendingPath = -1;
            pathAfter = null;
            lastRow = -1;
            pendingPath = null;
        }
        
        /**
         * Returns the row of the expanded/collapsed path. Makes sense only after
         * the state has been initialized with one of the willXX methods.
         * 
         * @return the row of the expanded/collapsed path.
         */
        public int getExpansionRow() {
            if (pendingPath == null) 
                throw new IllegalStateException("no expansion pending");
            return rowForPendingPath;
        }
        /**
         * Initializes the internal state
         * from the model's state before expansion. 
         * 
         * @param path the TreePath identifying the node which will be expanded,
         *   must not be null
         * @throws NullPointerException if path is null.  
         */
        public void willExpand(TreePath path) {
            Contract.asNotNull(path, "path to collapse must not be null");
            init(path, true);
            if (hasMoreRows(rowForPendingPath)) {
                pathAfter = getModel().getPathForRow(rowForPendingPath + 1);
            }
        }
        
        /**
         * Creates and returns a TableModelEvent of type INSERT for the
         * rows which have become visible or null if the folder was childless.<p>
         * 
         * @param path the TreePath identifying the node which has been expanded,
         *   must not be null
         * @return a TableModelEvent of type Insert as appropriate for the expansion.
         *   Will be null when expanding a childless folder.
         * @throws IllegalStateException if the pending state (as installed in willXX)
         *    does not match the given path.
         */
        public TableModelEvent hasExpanded(TreePath path) {
            if (!expand) {
                throw new IllegalStateException("pending type must be expanded");
            }
            if (pendingPath != path) {
                throw new IllegalStateException("path must be same as in willExpand " +
                		"\n expected: " + pendingPath 
                		+ "\n received: " + path);
            }
            int rowExpanded = getModel().getRowForPath(path);
            if (rowExpanded != rowForPendingPath) {
                throw new IllegalStateException("row has changed .. weird");
            }
            if (pathAfter == null) {
                lastRow = getModel().getRowCount() - 1;
            } else {
                lastRow = getModel().getRowForPath(pathAfter) - 1;
            }
            if (lastRow <= rowForPendingPath)  {
                return null;
            }
            int firstRow = rowForPendingPath +1;
            return new TableModelEvent(getModel(), firstRow, lastRow, 
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        }
        
        /**
         * Initializes the internal state
         * from the model's state before collapsing. 
         * 
         * @param path the TreePath identifying the node which will be collapsed,
         *   must not be null
         * @throws NullPointerException if path is null.  
         */
        public void willCollapse(TreePath path) {
            Contract.asNotNull(path, "path to collapse must not be null");
            init(path, false);
            TreePath parentPath = path.getParentPath();
            if (parentPath == null) {
                // I'm root
                lastRow = getModel().getRowCount() -1;
            } else {
                TreePath notYoungestAncestor = getNotYoungest(path);
                if (notYoungestAncestor == null) {
                    lastRow = getModel().getRowCount() - 1;
                } else {
                    // PENDING JW: check for original parent?
                    Object me = notYoungestAncestor.getLastPathComponent();
                    TreePath safeParentPath = notYoungestAncestor.getParentPath();
                    Object parent = safeParentPath.getLastPathComponent();
                    int myAge = getModel().getIndexOfChild(parent, me);
                    Object youngerSister = getModel().getChild(parent, myAge + 1);
                    TreePath sisterPath = safeParentPath.pathByAddingChild(youngerSister);
                    lastRow = getModel().getRowForPath(sisterPath) - 1;
                }
            }
        }

        /**
         * Creates and returns a TableModelEvent of type DELETE for the
         * rows which were hidden or null if the folder was childless.<p>
         * 
         * @param path the TreePath identifying the node which will be collapsed.
         * @return a TableModelEvent of type DELETE as appropriate for the collapse.
         *   Will be null when expanding a childless folder.
         * @throws IllegalStateException if the pending state (as installed in willXX)
         *    does not match the given path.
         */
        public TableModelEvent hasCollapsed(TreePath path) {
            if (expand) {
                throw new IllegalStateException("pending type must be collapsed");
            }
            if (pendingPath != path) {
                throw new IllegalArgumentException("path must be same as in willCollapse " +
                                "\n expected: " + pendingPath 
                                + "\n received: " + path);
            }
            int rowExpanded = getModel().getRowForPath(path);
            if (rowExpanded != rowForPendingPath) {
                throw new IllegalStateException("row has changed .. weird");
            }
            if (lastRow <= rowForPendingPath) {
                return null;
            }
            int firstRow = rowForPendingPath +1;
            return new TableModelEvent(getModel(), firstRow, lastRow, 
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        }

        /**
         * Recursively (through parent paths) looks for and returns a path where the
         *  last path component is not the last child. 
         * @param path
         * @return a tree path where the last component is not
         *    a youngest child. Null if all ancestors are youngest.
         */
        private TreePath getNotYoungest(TreePath path) {
            // PENDING JW - can move intial check for root into this
            TreePath parentPath = path.getParentPath();
            if (parentPath != null) {
                Object me = path.getLastPathComponent();
                Object parent = parentPath.getLastPathComponent();
                int myAge = getModel().getIndexOfChild(parent, me);
                if (myAge < getModel().getChildCount(parent) - 1) {
                    return path;
                }
                return getNotYoungest(parentPath);
            }
            return null;
        }

        /**
         * Inits common internal state of the given type from the path.
         * 
         * @param path the TreePath to install from.
         * @param type type of exansion event, true means expand, false means collapse
         */
        private void init(TreePath path, boolean type) {
            reset();
            pendingPath = path;
            expand = type;
            rowForPendingPath = getModel().getRowForPath(path);
        }
        
        private boolean hasMoreRows(int row) {
            return row < (getModel().getRowCount() - 1);
        }

        
    }
    
    /**
     * Lazily creates and returns the PendingExpansionState.
     * 
     * @return the pendingExpansionState
     */
    private PendingExpansionState getPendingExpansionState() {
        if (pendingExpansionState == null) {
            pendingExpansionState = new PendingExpansionState();
        }
        return pendingExpansionState;
    }

    /**
     * Clears pending state.
     */
    private void clearPendingExpansionState() {
        inProgressEvent = null;
        if (pendingExpansionState != null) {
            pendingExpansionState.reset();
        }
    }
    
// --------------------- implement Ext/Tree/Will/ExpansionListener
    
    /**
     * 
     * {@inheritDoc} <p>
     * 
     * Prepares internal state as appropriate for the received expansion event.<p>
     * 
     * Note: as we are not a general purpose TreeExpansionListener, we can impose
     * constraints on the notifier. It's the TreePathSupport which must take care
     * to not fire nulls.
     * 
     * @param the expansion event fired from the TreePathSupport. Must not be null
     *   and must not have a null treePath.
     * @throws NullPointerException if the event or the path are null.
     */
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        assert SwingUtilities.isEventDispatchThread();
        getPendingExpansionState().willCollapse(event.getPath());
        // PENDING JW: really want this? We are not a general purpose listener
        // and we rely on willXX/vetoed coming in pairs from TreePathSupport anyway
        inProgressEvent = event;
    }
    
    /**
     * 
     * {@inheritDoc} <p>
     * 
     * Prepares internal state as appropriate for the received expansion event.<p>
     * 
     * Note: as we are not a general purpose TreeExpansionListener, we can impose
     * constraints on the notifier. It's the TreePathSupport which must take care
     * to not fire nulls.
     * 
     * @param the expansion event fired from the TreePathSupport. Must not be null
     *   and must not have a null treePath.
     * @throws NullPointerException if the event or the path are null.
     */
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        assert SwingUtilities.isEventDispatchThread();
        getPendingExpansionState().willExpand(event.getPath());
        // PENDING JW: really want this? We are not a general purpose listener
        // and we rely on willXX/vetoed coming in pairs from TreePathSupport anyway
        inProgressEvent = event;
    }

    /**
     * 
     * {@inheritDoc} <p>
     * 
     * Fires TableModelEvents as appropriate for the received expansion event.<p>
     * 
     * Note: as we are not a general purpose TreeExpansionListener, we can impose
     * constraints on the notifier. It's the TreePathSupport which must take care
     * to not fire nulls.
     * 
     * @param the expansion event fired from the TreePathSupport. Must not be null
     *   and must not have a null treePath.
     * @throws NullPointerException if the event or the path are null.
     */
    public void treeCollapsed(TreeExpansionEvent event) {
        assert SwingUtilities.isEventDispatchThread();
        Contract.asNotNull(event, "expansion event must not be null");
        fireExpansionTableEvent(getPendingExpansionState().hasCollapsed(event.getPath()), 
                getPendingExpansionState().getExpansionRow());
        clearPendingExpansionState();
    }

    /**
     * 
     * {@inheritDoc} <p>
     * 
     * Fires TableModelEvents as appropriate for the received expansion event.<p>
     * 
     * Note: as we are not a general purpose TreeExpansionListener, we can impose
     * constraints on the notifier. It's the TreePathSupport which must take care
     * to not fire nulls.
     * 
     * @param the expansion event fired from the TreePathSupport. Must not be null
     *   and must not have a null treePath.
     * @throws NullPointerException if the event or the path are null.
     */
    public void treeExpanded(TreeExpansionEvent event) {
        assert SwingUtilities.isEventDispatchThread();
        
        fireExpansionTableEvent(getPendingExpansionState().hasExpanded(event.getPath()), 
                getPendingExpansionState().getExpansionRow());
        clearPendingExpansionState();
    }
    
    /** 
     * {@inheritDoc} <p>
     * 
     * Implemented to clear any pending state.
     * 
     * @throws IllegalStateException if the event is not the same as in the 
     *    willXX method.
     */
    public void treeExpansionVetoed(TreeExpansionEvent event, ExpandVetoException exception) {
        assert SwingUtilities.isEventDispatchThread();
        
        //Make sure the event that was vetoed is the one we're interested in
        if (event != inProgressEvent) 
            throw new IllegalStateException("Illegal state: vetoed expansion event doesn't pair "
                    + "\n expected: " + inProgressEvent
                    + "\n received: " + event);
        clearPendingExpansionState();
    }

    /**
     * Fires TableEvents as appropriate. Will fire the given event if not null,
     * will create and fire a update on the given row if >= 0. <p>
     * 
     * @param tableEvent the insert/delete event as mapped from an expansion event,
     *    may be null if a childless folder was expanded/collapsed.
     * @param row the row which was expanded/collapsed. May be -1 for a hidden root. 
     */
    private void fireExpansionTableEvent(TableModelEvent tableEvent,
            int row) {
        List<TableModelEvent> events = new ArrayList<TableModelEvent>();
        if (row >= 0) {
            TableModelEvent evt = new TableModelEvent (getModel(), row, row, 0,
                    TableModelEvent.UPDATE);
            events.add(evt);
        }
        if (tableEvent != null) {
            events.add(tableEvent);
        }
        if (events.size() > 0) {
            fireTableChange(events.toArray(new TableModelEvent[] {}));
        }
    }


    
//******************* Event translation routines ****************************
    
    
    /** 
     * Creates and returns an identical TreeModelEvent with the model we are proxying
     * as the event source .
     * 
     * @param the event to translate.
     * @return an identical event with the source reset to model.
     */
    private TreeModelEvent translateEvent (TreeModelEvent e) {
        //Create a new TreeModelEvent with us as the source
        // BEWARE JW: must use getTreePath on th eold event to cope with
        // null root
        TreeModelEvent nue = new TreeModelEvent (getModel(), e.getTreePath(), 
            e.getChildIndices(), e.getChildren());
        return nue;
    }
    
    /** 
     * Creates and returns one or more TableModelEvents as appropriate for
     * the given treeModelEvent of type. The translates events are guaranteed
     * to cover contigous rows.
     * 
     * @param e the treeModelEvent to translate
     * @param type the type of the treeModelEvent.
     * @return one or more TableModelEvents translated from the given tree evnt.
     */
    private TableModelEvent[] translateEvent (TreeModelEvent e, int type) {

        TreePath path = e.getTreePath();
        int row = getModel().getRowForPath(path);
        
        //If the node is not expanded, we simply fire a change
        //event for the parent
        // JW: incorrect --- need to update the expanded descendants in treePathSupport
        // for a removed.
        boolean inClosedNode = !getModel().isExpanded(path);
        if (inClosedNode) {
            //If the node is closed, no expensive checks are needed - just
            //fire a change on the parent node in case it needs to update
            //its display
            if (row != -1) {
                switch (type) {
                    case NODES_CHANGED :
                    case NODES_INSERTED :
                    case NODES_REMOVED :
                        return new TableModelEvent[] {
                            createTableRowEvent(row, TableModelEvent.UPDATE) };
                    default: 
                        assert false : "Unknown event type " + type;
                }
            }
            //In a closed node that is not visible, no event needed
            return new TableModelEvent[0];
        }
        
        boolean discontiguous = isDiscontiguous(e);
        
        Object[] blocks;
        if (discontiguous) {
            blocks = getContiguousIndexBlocks(e, type == NODES_REMOVED);
        } else {
            blocks = new Object[] {e.getChildIndices()};
        }
        
        
        TableModelEvent[] result = new TableModelEvent[blocks.length];
        for (int i=0; i < blocks.length; i++) {
            
            int[] currBlock = (int[]) blocks[i];
            switch (type) {
                case NODES_CHANGED :
                    result[i] = createTableChangeEvent (e, currBlock);
                    break;
                case NODES_INSERTED :
                    result[i] = createTableInsertionEvent (e, currBlock);
                    break;
                case NODES_REMOVED :
                    result[i] = createTableDeletionEvent (e, currBlock);
                    break;
                default :
                    assert false : "Unknown event type: " + type;
            }            
        }
        return result;
    }
    

    /** 
     * Creates and returns TableModelEvent of type UPDATE for the passed TreeModelEvent and the 
     * contiguous subrange of the TreeModelEvent's getChildIndices() value 
     * 
     * @param e the treeModelEvent to map
     * @param indices a contiguous subrange of the treeModel's getChildIndices
     * @return a TableModelEvent of type UPDATE
     * 
     */
    private TableModelEvent createTableChangeEvent (TreeModelEvent e, int[] indices) {
        
        TreePath path = e.getTreePath();
        // change on root
        if (indices ==  null) {
            int row = getModel().getRowForPath(path);
            return createTableRowEvent(row, TableModelEvent.UPDATE);
        }
        
//        Object[] children = e.getChildren();
        Object[] children = getChildrenForIndices(e, indices);
        Object firstChild = children[0];
        TreePath firstChildPath = path.pathByAddingChild(firstChild);
        int first = getModel().getRowForPath(firstChildPath);
        int last = first;
        if (indices.length > 1) {
            Object lastChild = children[indices.length - 1];
            TreePath lastChildPath = path.pathByAddingChild(lastChild);
            last = getModel().getRowForPath(lastChildPath);
        }
        
        //TODO - does not need to be ALL_COLUMNS, but we need a way to determine
        //which column index is the tree
        // JW: disagree - node change might affect the other columns as well
        return createTableRowEvent(first, last, TableModelEvent.UPDATE);
    }
    
    /**
     * Creates and returns TableModelEvent of type INSERTED for the passed
     * TreeModelEvent and the contiguous subrange of the TreeModelEvent's
     * getChildIndices() value
     * 
     * @param e the treeModelEvent to map
     * @param indices a contiguous subrange of the treeModel's getChildIndices
     * @return a TableModelEvent of type INSERTED
     * 
     */
    private TableModelEvent createTableInsertionEvent(TreeModelEvent e,
            int[] indices) {

        TreePath path = e.getTreePath();
        int row = getModel().getRowForPath(path);

        boolean realInsert = getModel().isExpanded(path);

        if (realInsert) {
            if (indices.length == 1) {
                // Only one index to change, fire a simple event. It
                // will be the first index in the array + the row +
                // 1 because the 0th child of a node is 1 greater than
                // its row index
                int affectedRow = row + indices[0] + 1;
                return createTableRowEvent(affectedRow, TableModelEvent.INSERT);

            } else {
                // Find the first and last indices. Add one since it is at
                // minimum the first index after the affected row, since it
                // is a child of it.
                int lowest = indices[0] + 1;
                int highest = indices[indices.length - 1] + 1;
                return createTableRowEvent(row + lowest, row + highest,
                        TableModelEvent.INSERT);

            }
        }
        // Nodes were inserted in an unexpanded parent. Just fire
        // a change for that row and column so that it gets repainted
        // in case the node there changed from leaf to non-leaf
        // TODO - specify only the tree column
        // JW: don't agree - might affect other columns as well
        return createTableRowEvent(row, TableModelEvent.UPDATE);
    }
    
    
    /** Create a deletion TableModelEvent for the passed TreeModelEvent and the 
     * contiguous subrange of the TreeModelEvent's getChildIndices() value
     * 
     *  PENDING JW: code murk - this method removes the expanded descendants 
     *  from the treePathSupport if needed. Doesn't belong here: side-effect is
     *  that they are not removed if the removed path wasn't visible.
     */
    private TableModelEvent createTableDeletionEvent (TreeModelEvent e, int[] indices) {
        
        TreePath path = e.getTreePath();
        int row = getModel().getRowForPath(path);
        if (row == -1) {
            //XXX could calculate based on last visible row?
            //return null;
            // 
            // never mind, just assume that the row -1 is the invisible
            // root node and in such case the calculation bellow
            // will just succeed and returning null was even more stupid ...
        }
        
        int countRemoved = indices.length;
        
        //Get the subset of the children in the event that correspond
        //to the passed indices
        Object[] children = getChildrenForIndices(e, indices);
        
        for (int i=0; i < children.length; i++) {
            TreePath childPath = path.pathByAddingChild(children[i]);
            if (getModel().isExpanded(childPath)) {
                
                int visibleChildren = 
                    getModel().getVisibleChildCount(childPath);
                
                countRemoved += visibleChildren;
            }
            // JW: beware - the original removed path instead of childPath!
//            getTreePathSupport().removePath(childPath);
        }

        //Add in the first index, and add one to it since the 0th
        //will have the row index of its parent + 1
        int firstRow = row + indices[0] + 1;
            int lastRow = firstRow + (countRemoved - 1);
       return createTableRowEvent(firstRow, lastRow,
                TableModelEvent.DELETE);        
    }


//**************** Static utility routines *****************************    

    /** Determine if the indices referred to by a TreeModelEvent are
     * contiguous.  If they are not, we will need to generate multiple
     * TableModelEvents for each contiguous block */
    private static boolean isDiscontiguous (TreeModelEvent e) {
        int[] indices = e.getChildIndices();
        if (indices == null || indices.length == 1) {
            return false;
        }
        Arrays.sort(indices);
        int lastVal = indices[0];
        for (int i=1; i < indices.length; i++) {
            if (indices[i] != lastVal + 1) {
                return true;
            } else {
                lastVal++;
            }
        }
        return false;
    }
    
    /** Returns an array of int[]s each one representing a contiguous set of 
     * indices in the tree model events child indices - each of which can be
     * fired as a single TableModelEvent.  The length of the return value is
     * the number of TableModelEvents required to represent this TreeModelEvent.
     * If reverseOrder is true (needed for remove events, where the last indices
     * must be removed first or the indices of later removals will be changed),
     * the returned int[]s will be sorted in reverse order, and the order in
     * which they are returned will also be from highest to lowest. */
    private static Object[] getContiguousIndexBlocks (TreeModelEvent e, boolean reverseOrder) {
        int[] indices = e.getChildIndices();
        
        //Quick check if there's only one index
        if (indices.length == 1) {
            return new Object[] {indices};
        }
        
       ArrayList<ArrayList<Integer>> al = new ArrayList<ArrayList<Integer>>();
        
        //Sort the indices as requested
        if (reverseOrder) {
            inverseSort (indices);
        } else {
            Arrays.sort (indices);
        }


        //The starting block
        ArrayList<Integer> currBlock = new ArrayList<Integer>(indices.length / 2);
        al.add(currBlock);
        
        //The value we'll check against the previous one to detect the
        //end of contiguous segment
        int lastVal = -1;
        
        //Iterate the indices
        for (int i=0; i < indices.length; i++) {
            if (i != 0) {
                //See if we've hit a discontinuity
                boolean newBlock = reverseOrder ? indices[i] != lastVal - 1 :
                    indices[i] != lastVal + 1;
                    
                if (newBlock) {
                    currBlock = new ArrayList<Integer>(indices.length - 1);
                    al.add(currBlock);
                }
            }
            currBlock.add (new Integer(indices[i]));
            lastVal = indices[i];
        }
        
        ArrayList<int[]> res = new ArrayList<int[]>(al.size());
        for (int i=0; i < al.size(); i++) {
            ArrayList<Integer> curr = al.get(i);
            Integer[] ints = (Integer[]) curr.toArray(new Integer[0]);
            
            res.add(toArrayOfInt(ints));
        }
        
        return res.toArray();
    }
    
    /** Get the children from a TreeModelEvent associated with the set of
     * indices passed. */
    private Object[] getChildrenForIndices (TreeModelEvent e, int[] indices) {
        //XXX performance - better way to do this may be to have
        //getContinguousIndexBlocks instead construct sub-treemodelevents - 
        //that would save having to do these iterations later to extract the
        //children.
        
        //At the same time, discontiguous child removals are relatively rare
        //events - optimizing them heavily may not be a good use of time.
        Object[] children = e.getChildren();
        int[] allIndices = e.getChildIndices();
        
        ArrayList<Object> al = new ArrayList<Object>();
        
        for (int i=0; i < indices.length; i++) {
            int pos = Arrays.binarySearch (allIndices, indices[i]);
            if (pos > -1) {
                al.add (children[pos]);
            }
            if (al.size() == indices.length) {
                break;
            }
        }
        return al.toArray();
    }
    
    
    /** Converts an Integer[] to an int[] */
    private static int[] toArrayOfInt (Integer[] ints) {
        int[] result = new int[ints.length];
        for (int i=0; i < ints.length; i++) {
            result[i] = ints[i].intValue();
        }
        return result;
    }
    
    
    
    /** Sort an array of ints from highest to lowest */
    private static void inverseSort (int[] array) {
        //XXX replace with a proper sort algorithm at some point -
        //this is brute force
        for (int i=0; i < array.length; i++) {
            array[i] *= -1;
        }
        Arrays.sort(array);
        for (int i=0; i < array.length; i++) {
            array[i] *= -1;
        }
    }
    


}
