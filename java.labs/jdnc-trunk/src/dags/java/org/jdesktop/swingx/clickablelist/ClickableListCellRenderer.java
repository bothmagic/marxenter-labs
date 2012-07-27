/*
 * ClickCellRenderer.java
 *
 * Created on 31 de enero de 2005, 12:28
 */

package org.jdesktop.swingx.clickablelist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import org.jdesktop.swingx.RolloverRenderer;

/**
 *
 * @author dags
 */
public class ClickableListCellRenderer extends JPanel implements ListCellRenderer, RolloverRenderer {
    
    private JCheckBox checkBox = new JCheckBox();
    
    Color oddBackground      = UIManager.getColor("Menu.background");
    Color oddForeground      = UIManager.getColor("Menu.foreground");
    
    Color evenBackground     = new Color(255,255,255);
    Color evenForeground     = Color.black;
    
    Color selectedBackground = UIManager.getColor("Menu.selectionBackground");
    Color selectedForeground = UIManager.getColor("Menu.selectionForeground");
    
    /** Creates a new instance of ClickCellRenderer */
    public ClickableListCellRenderer() {
        
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        checkBox.setOpaque(true);
        checkBox.setIconTextGap(6);
        
        add(checkBox, BorderLayout.CENTER);
    }
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        ClickableListElement el = (ClickableListElement)value;
        
        checkBox.setSelected(el.isSelected());
        checkBox.setText(el.getDisplayName());
        checkBox.setToolTipText(el.getDisplayName());
        
        //
        // all is to mimic highlighters, which doesnt work with this renderer ..
        //
        switch (index % 2) {
            case 0:
                checkBox.setBackground(isSelected ? selectedBackground : evenBackground);
                checkBox.setForeground(isSelected ? selectedForeground : evenForeground);
                break;
            case 1:
                checkBox.setBackground(isSelected ? selectedBackground : oddBackground);
                checkBox.setForeground(isSelected ? selectedForeground : oddForeground);
                break;
        }
        return this;
    }

    public void doClick() {
        // RolloverRenderer interface : what is that for ? 
        System.out.println("doClick");
    }

    public boolean isEnabled() {
        return true;
    }
}
