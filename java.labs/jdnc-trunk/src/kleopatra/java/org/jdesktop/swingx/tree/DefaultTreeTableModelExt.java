/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import java.util.Vector;

import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class DefaultTreeTableModelExt extends DefaultTreeTableModel {

    private EditableValue editableValue;

    public DefaultTreeTableModelExt(TreeTableNode root, 
            Vector<String> vectorNames) {
        super(root, vectorNames);
    }

    public void setEditableValue(EditableValue editableValue) {
        this.editableValue = editableValue;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        if (setValueForPath((TreeTableNode) path.getLastPathComponent(), newValue)) {
            modelSupport.firePathChanged(path);
        }
    }

    private boolean setValueForPath(TreeTableNode node, Object newValue) {
        if (editableValue == null) return false;
        return editableValue.setValue(node, newValue);
    }
    
    
}
