package swingx.plaf.animated;

import java.awt.Component;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

/**
 * PENDING JW: add to swingx zoo?
 */
public class ViewRowHighlightPredicate implements HighlightPredicate {

    int row;
    
    public ViewRowHighlightPredicate(int row) {
        this.row = row;
    }
    
    @Override
    public boolean isHighlighted(Component renderer,
            ComponentAdapter adapter) {
        return adapter.row == row;
    }

    public int getRow() {
        return row;
    }
}