/*
 * JDNCUtils.java
 *
 * Created on 28 de Junho de 2005, 11:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.jdnc.incubator.rlopes.jdncutils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.jdesktop.jdnc.JNTable;


/**
 *
 * @author Ricardo Lopes
 */
public class JDNCUtils {
        
    /**
     * Adds the filter panel to the JNTable, if the JNTable is already added to a container the
     * JNTable is removed from that container and replaced with a panel containing the table and the filter panel
     *
     * @param jnTable the JNTable
     * @param position the position for the filter panel, this values must be one of SwingConstants.TOP or SwingConstants.BOTTOM, throws an IllegalArgumentException if not
     * @param columnIndex the index of the tablemodel to filter
     * 
     * @return the JPanel containing the JNTable and the filter panel. If the JNTable was already added to a container it was replaced with this object.
     *
     * @throws IllegalArgumentException is the position is not one of SwingConstants.TOP, SwingConstants.BOTTOM
     * @throws RuntimeException if the JNTable already has a filter panel
     */
    public static Container hookFilterPanel(JNTable jnTable, int position, int columnIndex) {
        if ((position != SwingConstants.TOP) && (position != SwingConstants.BOTTOM)) {
            throw new IllegalArgumentException("Invalid position : " + position);
        }
        
        Container parentContainer = jnTable.getParent();
        if (parentContainer != null) {
            for (Component component : parentContainer.getComponents()) {
                if (component instanceof JComponent) {
                    if (FilterPanel.CLIENT_PROPERTY_FILTER_PANEL_VALUE.equals(((JComponent)component).getClientProperty(FilterPanel.CLIENT_PROPERTY_FILTER_PANEL_KEY))) {
                        throw new RuntimeException("A filter panel was already added to this JNTable.");
                    }
                }
            }
        }
        
        FilterPanel filterPanel = createFilterPanel(jnTable, columnIndex);
        
        JPanel container = new JPanel(new BorderLayout());
        container.add(jnTable, BorderLayout.CENTER);
        
        if (position == SwingConstants.TOP) {
            container.add(filterPanel, BorderLayout.NORTH);
        } else {
            container.add(filterPanel, BorderLayout.SOUTH);
        }
        
        if (parentContainer != null) {
            parentContainer.remove(jnTable); // remove the previouly added JNTable
            parentContainer.add(container); // add the new panel with the JNTable and the filter panel
            parentContainer.validate(); // force a layout on the container
            parentContainer.repaint();
            filterPanel.setInputFocus();
        }
        
        return container;
    }
    
    /**
     * removes the filter panel from the JNTable
     */
    public static void unhookFilterPanel(JNTable jnTable) {
        Container parentContainer = jnTable.getParent();
        for (Component component : parentContainer.getComponents()) {
            if (component instanceof JComponent) {
                if (FilterPanel.CLIENT_PROPERTY_FILTER_PANEL_VALUE.equals(((JComponent)component).getClientProperty(FilterPanel.CLIENT_PROPERTY_FILTER_PANEL_KEY))) {
                    parentContainer.remove(component); // remove the filter panel
                    parentContainer.validate(); // force a layout on the container
                    parentContainer.repaint(); 
                    break;
                }
            }
        }
    }
    
        
    protected static FilterPanel createFilterPanel(JNTable jnTable, int columnIndex) {
        return new FilterPanel(jnTable, columnIndex);
    }
    
}

