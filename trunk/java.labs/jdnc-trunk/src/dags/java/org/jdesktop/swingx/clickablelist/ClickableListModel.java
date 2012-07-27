package org.jdesktop.swingx.clickablelist;

import java.util.ArrayList;
import javax.swing.DefaultListModel;

/**
 *
 * @author Diego Gil
 */
public class ClickableListModel extends DefaultListModel  {
    
    /** Creates a new instance of ClickableListModel */
    public ClickableListModel() {
    }
    
    public ArrayList getClickedElements() {
        ArrayList al = new ArrayList();
        
        for (int i = 0; i < this.getSize(); i++) {
            ClickableListElement el = (ClickableListElement)this.getElementAt(i);
            if (el.isSelected()) {
                al.add(el);
            }
        }
        return al;
    }
}
