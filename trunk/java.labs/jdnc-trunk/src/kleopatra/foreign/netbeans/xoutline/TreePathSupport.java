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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/** Manages expanded/collapsed paths for the Outline.  Provides services similar
 * to those JTree implements inside its own class body.  Propagates changes
 * in expanded state to the layout cache.
 * <p>
 * Principally what this class does is manage the state of expanded paths which
 * are not visible, or whose parents have been closed/opened.  Whereas the
 * layout cache retains information only about what is visibly expanded, this
 * class manages information about any path that has been expanded at some
 * point in the lifetime of an Outline, so that for example, if A contains B
 * contains C, and A and B and C are expanded, then the user collapses A,
 * and later re&euml;expands A, B and C will retain their expanded state and
 * appear as they did the last time A was expanded.
 * <p>
 * When nodes are removed, the OutlineModel must call removePath() for any
 * defunct paths to avoid memory leaks by the TreePathSupport holding 
 * references to defunct nodes and not allowing them to be garbage collected.
 * <p>
 * Its <code>addTreeWillExpandListener</code> code supports 
 * <code>ExtTreeWillExpandListener</code>, so such a listener may be notified
 * if some other listener vetos a pending expansion event.
 *
 * @author  Tim Boudreau
 * 
 * PENDING JW: the expansion control is different from a JTree!
 * 
 * - expandPath doesn't care about parents
 * - collapsePath doesn't care about making the path visible
 * 
 * c&p'ed more code from JTree - now expands parents on expand/collapse
 * 
 */
public final class TreePathSupport {
    /**
     * Max number of stacks to keep around.
     */
    private static int                TEMP_STACK_SIZE = 11;
    private XOutlineModel model;
    private Map<TreePath,Boolean> expandedPaths = new HashMap<TreePath,Boolean>();
    private List<TreeExpansionListener> expansionListeners = new ArrayList<TreeExpansionListener>();
    private List<TreeWillExpandListener> willExpandListeners = 
        new ArrayList<TreeWillExpandListener>();
    private Stack<Stack<TreePath>> expandedStack;
    
    /** Creates a new instance of TreePathSupport */
    public TreePathSupport(XOutlineModel mdl) {
        expandedStack = new Stack<Stack<TreePath>>();

        this.model = mdl;
        model.addPropertyChangeListener(createOutlineModelListener());
        if ((model != null) && (model.getRoot() != null) && 
                !model.isLeaf(model.getRoot())) {
            expandedPaths.put(new TreePath(model.getRoot()),
                    Boolean.TRUE);

        }
    }
    
    private PropertyChangeListener createOutlineModelListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("largeModel".equals(evt.getPropertyName())) {
                    updateLayoutCacheExpandedNodes();
                }
                
            }
            
        };
        return l;
    }

    /** Clear all expanded path data.  This is called if the tree model fires
     * a structural change, and any or all of the nodes it contains may no
     * longer be present. */
    public void clear() {
        expandedPaths.clear();
    }
    
    /**
     * Update internal state as appropriate for removing children 
     * from the node identified by the path.<p>
     *  
     * @param path the TreePath with removed children.
     */
    public void treeNodesRemoved(TreePath parent, Object[] children) {
        // must not happen for correct TreeModelEvent - but core guards against
        if (children == null) return;
        TreePath            rPath;
        Vector<TreePath>    toRemove
            = new Vector<TreePath>(Math.max(1, children.length));

        for(int counter = children.length - 1; counter >= 0; counter--) {
            rPath = parent.pathByAddingChild(children[counter]);
            if(expandedPaths.get(rPath) != null)
                toRemove.addElement(rPath);
        }
        if(toRemove.size() > 0)
            removeDescendantToggledPaths(toRemove.elements());

        if(model == null || model.isLeaf(parent.getLastPathComponent()))
            expandedPaths.remove(parent);

    }
    
    /**
     * Update internal state as appropriate for the structural 
     * change of the node identified by path.<p>
     * 
     * Note: this implements a slightly different behaviour as JTree.
     * Here an previously expanded node itself keeps its expansion always
     * (as long as its state hadn't changed to leaf).
     * JTree does so only for visible expanded nodes.  
     *  
     * @param path the TreePath with structural change underneath.
     */
    public void treeStructureChanged(TreePath parent) {
        // new root or model
        if ((parent == null) || (parent.getPathCount() == 1)) {
            clear();
            if(model.getRoot() != null &&
                    !model.isLeaf(model.getRoot())) {
                     // Mark the root as expanded, if it isn't a leaf.
                     expandedPaths.put(parent, Boolean.TRUE);
                 }
        }
        else if(expandedPaths.get(parent) != null) {
            // PENDING JW: replace vector
            Vector<TreePath>    toRemove = new Vector<TreePath>(1);
            boolean             isExpanded = isExpanded(parent);
            boolean hasBeenExpanded = hasBeenExpanded(parent);
            boolean isLeaf = model.isLeaf(parent.getLastPathComponent());

            toRemove.addElement(parent);
            // remove all, including the node whose structure was changed
            removeDescendantToggledPaths(toRemove.elements());
            // reset state of node itself
            if(isExpanded) { // implies visible
                if(isLeaf) { // changed to leaf
                    collapsePath(parent);
                } else { // still folder
                    expandedPaths.put(parent, Boolean.TRUE);
                }
            } else if (hasBeenExpanded && !isLeaf){ // marked as expanded but not visible
                    expandedPaths.put(parent, Boolean.TRUE);
            }
        }

    }

    /**
     * 
     * @param path the path whose parents should be expanded.
     *  
     * @throws ExpandVetoException if a willExpandListener vetos the
     *   expansion of any parent.
     */
    private void expandParents(TreePath path) throws ExpandVetoException {
        Stack<TreePath>         stack;
        TreePath      parentPath = path.getParentPath();

        if (expandedStack.size() == 0) {
            stack = new Stack<TreePath>();
        }
        else {
            stack = expandedStack.pop();
        }

        try {
            while(parentPath != null) {
                if(isExpanded(parentPath)) {
                    parentPath = null;
                }
                else {
                    stack.push(parentPath);
                    parentPath = parentPath.getParentPath();
                }
            }
            for(int counter = stack.size() - 1; counter >= 0; counter--) {
                parentPath = (TreePath)stack.pop();
                if(!isExpanded(parentPath)) {
                    TreeExpansionEvent e = new TreeExpansionEvent(model, parentPath);
                    try {
                        expandSinglePath(e);
                    } catch (ExpandVetoException eve) {
                        fireTreeExpansionVetoed(e, eve);
                        // rethrow
                        throw eve;
                    }
                }
            }
        }
        finally {
            if (expandedStack.size() < TEMP_STACK_SIZE) {
                stack.removeAllElements();
                expandedStack.push(stack);
            }
        }
    }

    /**
     * Expand a path. Notifies the layout cache of the change, stores the
     * expanded path info (so reexpanding a parent node also reexpands this path
     * if a parent node containing it is later collapsed). Fires TreeWillExpand
     * and TreeExpansion events. 
     * 
     * If the last item in the path is a leaf this will have no effect.
     * @param path  the <code>TreePath</code> identifying a node
     *  
     */
    public void expandPath(TreePath path) {
        if ((path == null) || model.isLeaf(path.getLastPathComponent())) return; 
        
        try {
            expandParents(path);
        } catch (ExpandVetoException eve) {
            return;
        }
        if (Boolean.TRUE.equals(expandedPaths.get(path))) {
            // It's already expanded, don't waste cycles firing bogus events
            return;
        }
        TreeExpansionEvent e = new TreeExpansionEvent(model, path);
        try {
            expandSinglePath(e);
        } catch (ExpandVetoException eve) {
            fireTreeExpansionVetoed(e, eve);
        }
    }

    /**
     * Collapse a path. Notifies the layout cache of the change, stores the
     * expanded path info (so reexpanding a parent node also reexpands this path
     * if a parent node containing it is later collapsed). Fires TreeWillExpand
     * and TreeExpansion events.<p>
     * 
     * 
     * 
     * @param path  the <code>TreePath</code> identifying a node
     */
    public void collapsePath(TreePath path) {
        if (path == null)
            return;
        try {
            expandParents(path);
        } catch (ExpandVetoException eve) {
            return;
        }
        if (Boolean.FALSE.equals(expandedPaths.get(path))) {
            // It's already collapsed, don't waste cycles firing bogus events
            return;
        }
        TreeExpansionEvent e = new TreeExpansionEvent(model, path);
        try {
            fireTreeWillExpand(e, false);
            doCollapse(path);
            fireTreeExpansion(e, false);
        } catch (ExpandVetoException eve) {
            fireTreeExpansionVetoed(e, eve);
        }
    }


    /**
     * Tries to expand the single path denoted by the event. Notifies 
     * listeners as appropriate, does nothing on vetos.
     * 
     * @param e the expansionEvent to serve, getPath must not be null
     * @throws ExpandVetoException the veto from any of the listeners
     */
    private void expandSinglePath(TreeExpansionEvent e)
            throws ExpandVetoException {
        fireTreeWillExpand(e, true);
        doExpand(e.getPath());
        fireTreeExpansion(e, true);
    }

    /**
     * Internal state update to expand the path. This is called after
     * willExpandListners didn't bark and before the expansionListeners are
     * notified. Updates the layout as appropriate.
     * 
     * @param path the path that should be marked collapsed, must not be null
     *   and not a leaf.
     */
    private void doExpand(TreePath path) {
        expandedPaths.put(path, Boolean.TRUE);
//        path = new TreePath(path.getPath());
        // synch the path itself
        // PENDING JW: getExpandedDesc tests against sameness of parent - 
        // doesn't include if same, does include if not same (but equals)
        // bug or feature?
        model.getLayout().setExpandedState(path, true);
        // synch all expanded descendants
        TreePath[] descendants = getExpandedDescendants(path);
        for (TreePath treePath : descendants) {
            model.getLayout().setExpandedState(treePath, true);
        }
    }

    public void updateLayoutCacheExpandedNodes() {
        if (model.getRoot() != null) {
            updateLayoutCacheExpandedNodes(new TreePath(model.getRoot()));
        }
        
    }

    /**
     * 
     * @param treePath the path to updated the descendants on the layout
     *   must not be null.
     */
    private void updateLayoutCacheExpandedNodes(TreePath path) {
        // synch the path itself
//        model.getLayout().setExpandedState(path, true);
        // synch all expanded descendants
        TreePath[] descendants = getExpandedDescendants(path);
        for (TreePath treePath : descendants) {
            model.getLayout().setExpandedState(treePath, true);
        }
        
    }

    /**
     * Internal state update to collapse the path. This is called after
     * willExpandListners didn't bark and before the expansionListeners are
     * notified. Updates the layout as appropriate.
     * 
     * @param path the path that should be marked collapsed, must not be null.
     */
    private void doCollapse(TreePath path) {
        expandedPaths.put(path, Boolean.FALSE);
        if (isVisible(path)) {
            model.getLayout().setExpandedState(path, false);
        }
    }
    
    /** Remove a path's data from the list of known paths.  Called when
     * a tree model deletion event occurs */
    public void removePath (TreePath path) {
        expandedPaths.remove(path);
    }
    
    private void fireTreeExpansion (TreeExpansionEvent e, boolean expanded) {
        int size = expansionListeners.size();
        
        TreeExpansionListener[] listeners = new TreeExpansionListener[size];
        synchronized (this) {
            listeners = expansionListeners.toArray(listeners);
        }
        for (int i=0; i < listeners.length; i++) {
            if (expanded) {
                listeners[i].treeExpanded(e);
            } else {
                listeners[i].treeCollapsed(e);
            }
        }
    }
    
    private void fireTreeWillExpand (TreeExpansionEvent e, boolean expanded) throws ExpandVetoException {
        int size = willExpandListeners.size();
        
        TreeWillExpandListener[] listeners = new TreeWillExpandListener[size];
        synchronized (this) {
            listeners = willExpandListeners.toArray(listeners);
        }
        for (int i=0; i < listeners.length; i++) {
            if (expanded) {
                listeners[i].treeWillExpand(e);
            } else {
                listeners[i].treeWillCollapse(e);
            }
        }
    }
    
    private void fireTreeExpansionVetoed (TreeExpansionEvent e, ExpandVetoException ex) {
        int size = willExpandListeners.size();
        
        TreeWillExpandListener[] listeners = new TreeWillExpandListener[size];
        synchronized (this) {
            listeners = willExpandListeners.toArray(listeners);
        }
        for (int i=0; i < listeners.length; i++) {
            if (listeners[i] instanceof ExtTreeWillExpandListener) {
                ((ExtTreeWillExpandListener) listeners[i]).treeExpansionVetoed(e,
                    ex);
            }
        }
    }
    
    
    /**
     * Returns true if the node identified by the path has ever been
     * expanded.
     * @return true if the <code>path</code> has ever been expanded
     */
    public boolean hasBeenExpanded(TreePath path) {
	return (path != null && expandedPaths.get(path) != null);
    }

    /**
     * Returns true if the node identified by the path is currently expanded,
     * 
     * @param path  the <code>TreePath</code> specifying the node to check
     * @return false if any of the nodes in the node's path are collapsed, 
     *               true if all nodes in the path are expanded
     */
    public boolean isExpanded(TreePath path) {
	if(path == null)
	    return false;

	// Is this node expanded?
	Object value = expandedPaths.get(path);

	if(value == null || !((Boolean)value).booleanValue())
	    return false;

	// It is, make sure its parent is also expanded.
	TreePath parentPath = path.getParentPath();

	if(parentPath != null)
	    return isExpanded(parentPath);
        return true;
    }
    
    /**
     * PENDING: replace enumeration with ... iterator? We have arrays all over 
     * the place, should we?
     * 
     * @param toRemove
     */
     protected void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {
	 if(toRemove != null) {
	     while(toRemove.hasMoreElements()) {
                 TreePath[] descendants = getDescendantToggledPaths(
                    (TreePath) toRemove.nextElement());
                 for (int i=0; i < descendants.length; i++) {
                     expandedPaths.remove(descendants[i]);
                 }
	     }
	 }
     }
     
    protected TreePath[] getDescendantToggledPaths(TreePath parent) {
	if(parent == null)
	    return null;

	ArrayList<TreePath> descendants = new ArrayList<TreePath>();
        Iterator<TreePath> nodes = expandedPaths.keySet().iterator();
        TreePath path;
        while (nodes.hasNext()) {
            path = nodes.next();
            if (parent.isDescendant(path)) {
                descendants.add(path);
            }
        }
        TreePath[] result = new TreePath[descendants.size()];
        return descendants.toArray(result);
    }
    
    /**
     * Returns true if the value identified by path is currently viewable,
     * which means it is either the root or all of its parents are expanded.
     * Otherwise, this method returns false. 
     *
     * @return true if the node is viewable, otherwise false
     */
    public boolean isVisible(TreePath path) {
        if(path != null) {
	    TreePath parentPath = path.getParentPath();

	    if(parentPath != null) {
		return isExpanded(parentPath);
            }
	    // Root.
	    return true;
	}
        return false;
    }    
    
    /**
     * Returns an array of <code>TreePath</code> of the descendants of the
     * path <code>parent</code> that
     * are currently expanded. If <code>parent</code> is not currently
     * expanded, this will return an empty array.
     * If you expand/collapse nodes while
     * iterating over the returned <code>TreePath</code>
     * this may not return all
     * the expanded paths, or may return paths that are no longer expanded.<p>
     * 
     * Note: the parent is considered its own descendant, so if it is expanded
     * it will be contained in the array.
     *
     * @param parent  the path which is to be examined
     * @return an array of <code>TreePaths</code> of the descendents of 
     *          <code>parent</code>. Empty if parent not expanded.
     */
    public TreePath[] getExpandedDescendants(TreePath parent) {
        TreePath[] result = new TreePath[0];
	if(isExpanded(parent)) {
            TreePath path;
            Boolean value;
            List<TreePath> results = null;

            if (!expandedPaths.isEmpty()) {

                Iterator<TreePath> i = expandedPaths.keySet().iterator();

                while(i.hasNext()) {
                    path = i.next();
                    value = expandedPaths.get(path);

                    // Add the path if it is expanded, a descendant of parent,
                    // and it is visible (all parents expanded). This is rather
                    // expensive!
                    // PENDING JW: test for sameness - really intended?
                    if(path != parent && value != null &&
                       value.booleanValue() &&
                        parent.isDescendant(path) && isVisible(path)) {
                        if (results == null) {
                            results = new ArrayList<TreePath>();
                        }
                        results.add (path);
                    }
                }
                if (results != null) {
                    result = results.toArray(result);
                }
            }
        }
        return result;
    }    
    
    /** Add a TreeExpansionListener.  If the TreeWillExpandListener implements
     * ExtTreeExpansionListener, it will be notified if another 
     * TreeWillExpandListener vetoes the expansion event */
    public synchronized void addTreeExpansionListener (TreeExpansionListener l) {
        expansionListeners.add(l);
    }
    
    public synchronized void removeTreeExpansionListener (TreeExpansionListener l) {
        expansionListeners.remove(l);
    }
    
    public synchronized TreeExpansionListener[] getTreeExpansionListeners() {
        if (expansionListeners.isEmpty()) {
            return new TreeExpansionListener[0];
        }
        return expansionListeners.toArray(new TreeExpansionListener[expansionListeners.size()]);
        
    }
    
    public synchronized void addTreeWillExpandListener (TreeWillExpandListener l) {
        willExpandListeners.add(l);
    }
    
    public synchronized void removeTreeWillExpandListener (TreeWillExpandListener l) {
        willExpandListeners.remove(l);
    }

    public synchronized TreeWillExpandListener[] getTreeWillExpandListeners() {
        if (willExpandListeners.isEmpty()) {
            return new TreeWillExpandListener[0];
        }
        return willExpandListeners.toArray(new TreeWillExpandListener[willExpandListeners.size()]);
    }

}
