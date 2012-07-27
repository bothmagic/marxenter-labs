/*
 * $Id: MultiCellTableCellRenderer.java 989 2006-12-28 11:56:54Z bolsover $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * $Log$
 * Revision 1.1  2006/12/28 11:56:54  bolsover
 * *** empty log message ***
 *
 * @author David Bolsover
 *
 */
package org.jdesktop.swingx.treetable.multicell;


import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MultiCellTableCellRenderer extends DefaultTableCellRenderer{
    
    /**
     * Creates a new instance of MultiCellTableCellRenderer
     */
    
    public MultiCellTableCellRenderer() {
    }
    
    /**
     * Just returns the value to assign to the cell at [row, column] cast to a Component - this allows us of JXTable values etc. 
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      
        return (Component)value;
    }
    
    

}
