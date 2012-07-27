/*
 * Created on 12.04.2007
 *
 */
package org.jdesktop.beansbindingx;

import java.beans.PropertyVetoException;
import java.util.List;

import org.jdesktop.application.Action;
import org.jdesktop.beans.AbstractBean;

/**
 * PENDING: this is in model-coordinates! Need some way to hook in 
 * view <--> model coordinate mapping
 */
public class Navigation extends AbstractBean {
    private Object selectedElement;
    private List list;

    public Navigation(List list) {
        this.list = list;
    }
    
    @Action 
    public void next() {
        int next = getSelectedIndex() + 1;
        if (next >= list.size()) {
           next = 0; 
        }
        setSelectedIndex(next);
    }
    
    @Action
    public void previous() {
        int prev = getSelectedIndex() - 1;
        if (prev < 0) {
            prev = list.size() - 1;
        }
        setSelectedIndex(prev);
    }
    
    public void setSelectedElement(Object selectedElement) {
        Object old = getSelectedElement();
        // don't want to fire on null/null... 
        // Hmm .. check spec as to when that should happen
        if (equals(old, selectedElement)) return;
        try {
            fireVetoableChange("selectedElement", old, selectedElement);
        } catch (PropertyVetoException e) {
            // veto'd - don't change
            return;
        }
        this.selectedElement = selectedElement;
        firePropertyChange("selectedElement", old, getSelectedElement());
    }
    
    private boolean equals(Object old, Object other) {
        if ((old == null) && (other == null)) return true;
        if (old != null) return old.equals(other);
        return false;
    }

    public Object getSelectedElement() {
        return selectedElement;
    }
    
    /**
     * Note: the index is in model coordinates!
     * Binding cannot yet cope with sorting/filtering anyway. 
     *  
     * @return
     */
    public int getSelectedIndex() {
        return list.indexOf(getSelectedElement());
    }
    
    public void setSelectedIndex(int selectedIndex) {
        Object element = null;
        if ((selectedIndex >= 0) && (selectedIndex < list.size())) {
            element = list.get(selectedIndex);
        }
        setSelectedElement(element);
    }
    
    
}
