/*
 * Created on 20.06.2005
 *
 */
package org.jdesktop.swingx.tree;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * A static snapshot of a container hierarchy.
 * 
 * It's a from-scratch implementation of a TreeTableModelExt
 * using TreeModelSupport to delegate model change notification.
 * 
 * @author Jeanette Winzenburg
 */
public class ComponentTreeTableModel implements TreeTableModel {


    Container root;
    TreeModelSupport support;
    
    
    public ComponentTreeTableModel(Container root) {
        setRoot(root);
    }

    public void setRoot(Container root) {
        if (root == null) {
            root = new JXFrame();
        }
        this.root = root;
        getTreeModelSupport().fireNewRoot();
    }
    
    protected TreeModelSupport getTreeModelSupport() {
        if (support == null) {
            support = new TreeModelSupport(this);
            
        }
        return support;
    }

    //  ------------------TreeModel
    
    
    public Object getChild(Object parent, int index) {
        return ((Container) parent).getComponent(index);
    }
    
    public int getChildCount(Object parent) {
        return parent instanceof Container ? ((Container) parent).getComponentCount() : 0;
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        Component[] children = ((Container) parent).getComponents();
        for (int i = 0; i < children.length; i++) {
            if (children[i].equals(child)) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }
 
    public String convertValueToText(Object node) {
        Object hierarchicalValue = getValueAt(node, getHierarchicalColumn());
        return String.valueOf(hierarchicalValue);// hierarchicalValue != null ? hierarchicalValue.toString() : "";
    }
//------------------ TreeTableModel    

    public Class<?> getColumnClass(int column) {
        switch (column) {
        case 0:
            return String.class;
        case 1:
            return Class.class;
        case 2:
            return Dimension.class;
        default:
            return Object.class;
        }
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Name";
        case 1:
            return "Type";
        case 2:
            return "Size";
        default:
            return "Column " + column;
        }
    }
    
    public Object getValueAt(Object node, int column) {
        Component comp = (Component) node;
        switch (column) {
        case 0:
            return comp.getName();
        case 1:
            return comp.getClass();
        case 2:
            return comp.getSize();
        default:
            return null;
        }
    }

    public void setValueAt(Object value, Object node, int column) {
        if (column != getHierarchicalColumn()) return;
        ((Component) node).setName(String.valueOf(value));
        getTreeModelSupport().firePathChanged(getPathToRoot((Component)node));
    }

    public TreePath getPathToRoot(Component node) {
        return new TreePath(getPathToRoot(node, 0));
    }

    private Component[] getPathToRoot(Component node, int depth) {
        Component[]              retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if(node == null) {
            if(depth == 0)
                return null;
            else
                retNodes = new Component[depth];
        }
        else {
            depth++;
            if(node == root)
                retNodes = new Component[depth];
            else
                retNodes = getPathToRoot(node.getParent(), depth);
            retNodes[retNodes.length - depth] = node;
        }
        return retNodes;
    }

    public boolean isHierarchicalColumn(int column) {
        return column == getHierarchicalColumn();
    }

    public int getHierarchicalColumn() {
        return 0;
    }
    
    public boolean isCellEditable(Object node, int column) {
        return column == getHierarchicalColumn();
    }

    public Object getRoot() {
        return root;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        Object node = path.getLastPathComponent();
        setValueAt(newValue, node, getHierarchicalColumn());
        
    }

    public void addTreeModelListener(TreeModelListener l) {
        getTreeModelSupport().addTreeModelListener(l);
        
    }

    public void removeTreeModelListener(TreeModelListener l) {
       getTreeModelSupport().removeTreeModelListener(l);
        
    }

    
}
