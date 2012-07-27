package org.jdesktop.swingx.table;


import junit.framework.TestCase;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.table.ColumnControlButton.ColumnVisibilityAction;

public class DialogColumnControlButtonTest extends TestCase {

    /**
     * test that the actions synch's its own selected property with 
     * the column's visible property.
     *
     */
    public void testColumnVisibilityAction() {
        JXTable table = new JXTable(10, 3);
        table.setColumnControlVisible(true);
        table.setColumnControl(new DialogColumnControlButton
                (table, new ColumnControlIcon()));
        ColumnControlButton columnControl = (ColumnControlButton) table.getColumnControl();
        ColumnVisibilityAction action = columnControl.getColumnVisibilityActions().get(0);
        TableColumnExt columnExt = table.getColumnExt(0);
        boolean visible = columnExt.isVisible();
        // sanity
        assertTrue(visible);
        assertEquals(columnExt.isVisible(), action.isSelected());
        action.setSelected(!visible);
        // hmmm... here it's working? unexpected
        assertEquals(!visible, columnExt.isVisible());
    }
    

}
