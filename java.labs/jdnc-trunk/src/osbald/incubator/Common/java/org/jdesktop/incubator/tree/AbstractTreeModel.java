package org.jdesktop.incubator.tree;

import org.jdesktop.swingx.tree.TreeModelSupport;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 29-Mar-2004
 * Time: 14:13:13
 */

/* PENDING how far to go with this one? leaves choice between a MultiList type storage
or composite nodes (node hasChildren and optionally a known parent) is pretty common
too, plus a better good reason to abandon 'standard' DefaultTreeModel/TreeNode where 
you don't want the extra pain of maintaining two independent hierarchies.
*/

public abstract class AbstractTreeModel implements TreeModel {
    /**
     * Provides support for event dispatching.
     */
    protected TreeModelSupport modelSupport;

    protected AbstractTreeModel() {
        this.modelSupport = new TreeModelSupport(this);
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        /*PENDING more suitable for EditableTreeModel?
        Object child = path.getLastPathComponent();
        if (!newValue.equals(child)) {
            if (path == null || path.getPathCount() <= 1) {
                modelSupport.fireChildChanged(path, 0, child);
            } else {
                Object parent = path.getPathComponent(path.getPathCount() - 2);
                modelSupport.fireChildChanged(path, getIndexOfChild(parent, child), child);
            }
        }
        */
    }

    public void addTreeModelListener(TreeModelListener listener) {
        modelSupport.addTreeModelListener(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        modelSupport.removeTreeModelListener(listener);
    }

    public void removeAllListeners() {
        for (TreeModelListener listener : modelSupport.getTreeModelListeners()) {
            modelSupport.removeTreeModelListener(listener);
        }
    }
}
