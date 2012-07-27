/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import java.awt.Component;
import java.awt.Container;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.tree.TreePath;


public class ComponentTTModel extends ComponentTreeTableModel {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ComponentTTModel.class
            .getName());
    private EditableValue editableValue;

    public ComponentTTModel(Container root) {
        super(root);
    }

   
    public EditableValue getEditableValue() {
        if (editableValue == null) {
            editableValue = new EditableValue() {

                public Object getValue(Object node) {
                    if (node == null) return null;
                    return getValueAt(node, getHierarchicalColumn());
                }

                public boolean setValue(Object node, Object newValue) {
                    if ((node == null) || !isCellEditable(node, getHierarchicalColumn())) return false;
                    setValueAt(newValue, node, getHierarchicalColumn());
                    return true;
                }
                
            };
        }
        return editableValue;
    }
    
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        if (!getEditableValue().setValue(path.getLastPathComponent(), newValue)) return;
        getTreeModelSupport().firePathChanged(path);
    }


     public void add(Container parent, JComponent comp) {
        parent.add(comp);
        getTreeModelSupport().fireChildAdded(getPathToRoot(parent), 
                parent.getComponentCount() - 1, comp);
    }

    public void remove(Component comp) {
        Container parent = comp.getParent();
        if (parent == null) return;
        Component[] components = parent.getComponents();
        int index = -1;
        for (int i = 0; i < components.length; i++) {
            if (components[i] == comp) {
                index = i;
                break;
            }
        }
        parent.remove(comp);
        getTreeModelSupport().fireChildRemoved(getPathToRoot(parent), index, comp);
        
    }

    
}
