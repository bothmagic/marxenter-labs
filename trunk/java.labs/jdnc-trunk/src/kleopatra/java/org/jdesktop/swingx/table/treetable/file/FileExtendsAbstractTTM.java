/*
 * Created on 14.12.2005
 *
 */
package org.jdesktop.swingx.table.treetable.file;

import java.util.Date;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.table.treetable.file.FileNode.RootFileNode;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;


/**
 * FileSystem TreeTableModel using latest api.
 * Extends AbstractTTM. Internally, keeps the hierarchical
 * structure in FileNodes which are not TreeTableNodes.
 * 
 */
public class FileExtendsAbstractTTM extends AbstractTreeTableModel {

//    FileNode root;
//    TreeModelSupport support;
//    JFileChooser fileChooser;
    
    // implement TreeTableModelExt
    public boolean isHierarchicalColumn(int column) {
        return column == getHierarchicalColumn();
    }

    @Override
    public int getHierarchicalColumn() {
        return 0;
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch(column) {
        case 0:
            return String.class;
        case 1:
            return Integer.class;
        case 2:
            return Date.class;
        default:
            return Object.class;
            }
    }

    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
        case 0:
            return "Name";
        case 1:
            return "Size";
        case 2:
            return "Modification Date";
        default:
            return "Column " + column;
            }
    }

    public Object getValueAt(Object node, int column) {
        FileNode fileNode = (FileNode) node;
        switch(column) {
        case 0:
            return fileNode.getDisplayName();
        case 1:
            return fileNode.getFileSize();
        case 2:
            return fileNode.getLastChanged();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        FileNode fileNode = (FileNode) node;
        return isHierarchicalColumn(column) && !fileNode.isDrive();
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        if (!isCellEditable(node, column)) return;
        FileNode fileNode = (FileNode) node;
        FileNode parentNode = fileNode.getParent();
        fileNode.renameFile(value);
        // notify listeners
        getTreeModelSupport().firePathChanged(getPathToRoot(fileNode));
        int oldIndex = getIndexOfChild(parentNode, node);
        parentNode.sortChildren();
        int newIndex = getIndexOfChild(parentNode, node);
        if (oldIndex != newIndex) {
           // the node has moved, need to split the notification
           // into a removed/added 
           TreePath parentPathToRoot = getPathToRoot(parentNode);
           getTreeModelSupport().fireChildRemoved(parentPathToRoot, oldIndex, node); 
           getTreeModelSupport().fireChildAdded(parentPathToRoot, newIndex, node);
        }
    }
        
    private TreeModelSupport getTreeModelSupport() {
        return modelSupport;
    }

    private TreePath getPathToRoot(FileNode node) {
        return new TreePath(getPathToRoot(node, 0));
    }

    private FileNode[] getPathToRoot(FileNode node, int depth) {
        FileNode[]              retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if(node == null) {
            if(depth == 0)
                return null;
            else
                retNodes = new FileNode[depth];
        }
        else {
            depth++;
            if(node == root)
                retNodes = new FileNode[depth];
            else
                retNodes = getPathToRoot(node.getParent(), depth);
            retNodes[retNodes.length - depth] = node;
        }
        return retNodes;
    }

//----------------------- implement TreeModel
    @Override
    public Object getRoot() {
        if (root == null) {
            root = new RootFileNode();
        }
        return root;
    }

    public Object getChild(Object parent, int index) {
        return ((FileNode) parent).getChildren()[index];
    }

    public int getChildCount(Object parent) {
        return ((FileNode) parent).getChildren().length;
    }

    @Override
    public boolean isLeaf(Object node) {
        return ((FileNode) node).isLeaf();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object value) {
        Object node = path.getLastPathComponent();
        setValueAt(value, node, getHierarchicalColumn());
        
    }

    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
            if (getChild(parent, i).equals(child)) { 
                return i; 
            }
        }
        return -1; 
    }

    public String convertValueToText(Object value) {
        return String.valueOf(getValueAt(value, getHierarchicalColumn()));
    }
    

}
