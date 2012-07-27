package org.jdesktop.swingx.rollover;

import java.util.List;

import javax.swing.Action;

import org.jdesktop.beans.AbstractBean;

/**
 * a quick@dirty model ... just to have something to show.
 */
public class SampleTaskPaneModel extends AbstractBean {
    private boolean expanded;
    private String title;
    private List<Action> actions;
    
    public List<Action> getActions() {
        return actions;
    }
    
    public void setActions(List<Action> actions) {
        List<Action> old = getActions();
        this.actions = actions;
        firePropertyChange("actions", old, getActions());
    }
    
    public boolean isExpanded() {
        return expanded;
    }
    
    public void setExpanded(boolean expanded) {
        boolean old = isExpanded();
        this.expanded = expanded;
        firePropertyChange("expanded", old, isExpanded());
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        String old = getTitle();
        this.title = title;
        firePropertyChange("title", old, getTitle());
    }
     
    
}