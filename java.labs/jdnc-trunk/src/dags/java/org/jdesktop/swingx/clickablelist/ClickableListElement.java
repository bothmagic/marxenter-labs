package org.jdesktop.swingx.clickablelist;

/**
 *
 * @author Diego Gil
 */
public class ClickableListElement extends Object {
   
    private Object id = null;
    private Object name = null;
    private boolean selected = false;
    
    /** Creates a new instance of ClickableListElementjava */
    public ClickableListElement() {
    }

    public ClickableListElement(Object id, Object name) {
        this.id = id;
        this.name = name;
    }
    
    public void setId(Object id) {
        this.id = id;
    }
    
    public void setName(Object name) {
        this.name = name;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public String toString() {
        return name.toString();
    }
    
    public Object getId() {
        return id;
    }
    
    public void toggleSelection() {
        setSelected(!selected);
    }
    
    public String getDisplayName() {
        return name.toString();
    }
}
