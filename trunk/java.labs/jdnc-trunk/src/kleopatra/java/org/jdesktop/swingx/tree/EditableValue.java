/*
 * Created on 09.08.2007
 *
 */
package org.jdesktop.swingx.tree;

public interface EditableValue {

    Object getValue(Object node);
    boolean setValue(Object node, Object newValue);
    
}
