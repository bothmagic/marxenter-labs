/*
 * Created on 14.12.2005
 *
 */
package org.jdesktop.swingx.table.treetable.file;

import java.util.Arrays;
import java.util.Date;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;


/**
 * FileSystem TreeTableModel using latest api.
 * Wraps the files into custom TreeTableNode and extends DefaultTreeTableModel.
 * 
 */
public class FileExtendsDefaultTTM extends  DefaultTreeTableModel {

   
    public FileExtendsDefaultTTM() {
        super(new FileScratchTTNode.RootFileNode(),
                Arrays.asList(new Object[] {"Name", "Size", "Modification Date"}));
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

    @Override
    public void setValueAt(Object value, Object node, int column) {
        if (!isCellEditable(node, column)) return;
        FileScratchTTNode fileNode = (FileScratchTTNode) node;
        FileScratchTTNode parentNode = fileNode.getParent();
        fileNode.renameFile(value);
        // notify listeners
        modelSupport.firePathChanged(new TreePath(getPathToRoot(fileNode)));
        int oldIndex = getIndexOfChild(parentNode, node);
        parentNode.sortChildren();
        int newIndex = getIndexOfChild(parentNode, node);
        if (oldIndex != newIndex) {
           // the node has moved, need to split the notification
           // into a removed/added 
           TreePath parentPathToRoot = new TreePath(getPathToRoot(parentNode));
           modelSupport.fireChildRemoved(parentPathToRoot, oldIndex, node); 
           modelSupport.fireChildAdded(parentPathToRoot, newIndex, node);
        }
    }
        
//----------------------- implement TreeModel

    @Override
    public void valueForPathChanged(TreePath path, Object value) {
        Object node = path.getLastPathComponent();
        setValueAt(value, node, getHierarchicalColumn());
    }

}
