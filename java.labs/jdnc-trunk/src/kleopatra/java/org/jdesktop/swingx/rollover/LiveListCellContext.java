package org.jdesktop.swingx.rollover;

import javax.swing.JList;

import org.jdesktop.swingx.renderer.ListCellContext;

/**
 * Custom cell context: has live property.
 */
public class LiveListCellContext extends ListCellContext {
    boolean live;
    
    @Override
    public void installContext(JList component, Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf) {
        super.installContext(component, value, row, column, selected, focused,
                expanded, leaf);
        live = false;
    }

    public void installContext(JList component, Object value, int row, int column, boolean selected, boolean focused, boolean expanded, boolean leaf, boolean live) {
        super.installContext(component, value, row, column, selected, focused,
                expanded, leaf);
        this.live = live;
    }
    
    public boolean isLive() {
        return live;
    }
}