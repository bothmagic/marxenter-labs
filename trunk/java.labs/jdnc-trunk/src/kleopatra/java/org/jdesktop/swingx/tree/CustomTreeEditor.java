/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;

public class CustomTreeEditor extends AbstractCellEditor 
    implements TreeCellEditor
    {

    private EditableValue editableValue;
    private JTextField field;
    
    public CustomTreeEditor() {
        this(null);
    }
    public CustomTreeEditor(EditableValue editableValue) {
        init();
        setEditableValue(editableValue);
    }
    
    private void init() {
        field = new JTextField();
        field.addActionListener(getTextFieldListener());
    }
    
    private ActionListener getTextFieldListener() {
        ActionListener textFieldListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
                
            }
            
        };
        return textFieldListener;
    }
    
    public Object getCellEditorValue() {
        return field.getText();
    }

    public void setEditableValue(EditableValue editableValue) {
        this.editableValue = editableValue;
    }
    
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        value = getContent(value);
        setValue(value);
        return field;
    }

    private void setValue(Object value) {
        if (value == null) {
            value = "";
        }
        field.setText(String.valueOf(value));
    }
    
    private Object getContent(Object value) {
        if (editableValue != null) {
            return editableValue.getValue(value);
        }
        return value;
    }

}
