/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.tree.DefaultXTreeCellEditor;

public class TreeEditor extends DefaultTreeEditor {

    private EditableValue editableValue;

    public TreeEditor() {
        super();
    }

    public void setEditableValue(EditableValue editableValue) {
       this.editableValue = editableValue; 
    }
    
    public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded, boolean leaf, int row) {
        if (editableValue != null) {
            value = editableValue.getValue(value);
        }
        return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
    }

}
