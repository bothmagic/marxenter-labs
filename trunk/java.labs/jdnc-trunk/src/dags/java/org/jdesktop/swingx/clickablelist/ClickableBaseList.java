package org.jdesktop.swingx.clickablelist;

import java.awt.Color;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.decorator.RolloverHighlighter;

/**
 *
 * @author dags
 */
public class ClickableBaseList extends JXList {
    
    private ClickableListCellRenderer renderer = null;
    
    /** Creates a new instance of ClickableBaseList */
    public ClickableBaseList() {
        
//        this.setHighlighters(new HighlighterPipeline(new Highlighter[]{ AlternateRowHighlighter.linePrinter }));
//        this.getHighlighters().addHighlighter(new RolloverHighlighter(Color.RED, Color.WHITE ));
//        this.setRolloverEnabled(true);
        
        renderer = new ClickableListCellRenderer();
        this.setCellRenderer(renderer);
    }
}
