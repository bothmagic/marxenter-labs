/*
 * SmilieComboBoxRenderer.java
 *
 * Created on 25 de Fevereiro de 2005, 18:24
 */

package org.jdesktop.jdnc.incubator.rlopes.smiliescombo;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Lopes
 */
public class SmilieComboBoxRenderer extends DefaultListCellRenderer {
    

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                       boolean isSelected, boolean cellHasFocus) {
        
        Smilie smilie = (Smilie) value;
        return smilie.getCachedRenderer();
    }
    
}

