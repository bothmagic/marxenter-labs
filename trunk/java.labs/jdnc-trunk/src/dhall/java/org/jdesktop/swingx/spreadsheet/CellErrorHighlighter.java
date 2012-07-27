/*
 * Created on 08.05.2007
 *
 */
package org.jdesktop.swingx.spreadsheet;

import java.awt.Color;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.ConditionalHighlighter;

/**
 * Highlighter for invalid cells.
 */
public class CellErrorHighlighter extends ConditionalHighlighter {

    public CellErrorHighlighter() {
        super(null, Color.RED, -1, -1);
    }
    
    @Override
    protected boolean test(ComponentAdapter adapter) {
        JXSpreadsheet.SpreadsheetTable table =
            (JXSpreadsheet.SpreadsheetTable) adapter.getComponent();
        
        Cell cell = table.getSpreadsheet().getCellIfPresent(adapter.row, adapter.column);
        return cell == null ? false : !cell.isValid();
    }

}
