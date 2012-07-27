/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

class AutoSuggestPopup extends JPanel {
    JList list;
    
    public AutoSuggestPopup(ListModel model) {
        super(new BorderLayout());
        
        list = new JList(model);
        add(new JScrollPane(list));
    }
}