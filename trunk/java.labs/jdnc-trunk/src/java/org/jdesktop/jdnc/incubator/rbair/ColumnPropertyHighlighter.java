/*
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;

import org.jdesktop.jdnc.incubator.rbair.swing.decorator.AlternateRowHighlighter;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.ComponentAdapter;

/*
 * package-private class for implementing a highlighter which can
 * customize the foreground, background, and font for cells on a column
 * basis.
 */
class ColumnPropertyHighlighter extends AlternateRowHighlighter {

    private HashMap columnProperties = new HashMap();

    public ColumnPropertyHighlighter() {
        super(null, null, null);
    }

    public void setColumnBackground(String columnName, Color background) {
        if (background != null) {
            columnProperties.put(columnName + ".background", background);
        }
        else {
            columnProperties.remove(columnName + ".background");
        }
    }

    public Color getColumnBackground(String columnName) {
        return (Color) columnProperties.get(columnName + ".background");
    }

    public void setColumnForeground(String columnName, Color foreground) {
        if (foreground != null) {
            columnProperties.put(columnName + ".foreground", foreground);
        }
        else {
            columnProperties.remove(columnName + ".foreground");
        }
    }

    public Color getColumnForeground(String columnName) {
        return (Color) columnProperties.get(columnName + ".foreground");
    }

    public void setColumnFont(String columnName, Font font) {
        if (font != null) {
            columnProperties.put(columnName + ".font", font);
        }
        else {
            columnProperties.remove(columnName + ".font");
        }
    }

    public Font getColumnFont(String columnName) {
        return (Font) columnProperties.get(columnName + ".font");
    }

    protected void applyFont(Component renderer, ComponentAdapter adapter) {
        Font font = getColumnFont(adapter.getColumnName(adapter.column));
        if (font != null) {
            renderer.setFont(font);
        }
    }

    protected Color computeBackground(Component renderer,
                                      ComponentAdapter adapter) {
        Color seed = getColumnBackground(adapter.getColumnName(adapter.column));
        if (seed == null) {
            seed = (adapter.row % 2) == 0 ?
                oddRowBackground : evenRowBackground;
        }
        if (seed == null) {
            seed = background == null ? adapter.getComponent().getBackground() :
                background;
        }
        return adapter.isSelected() ? computeSelectedBackground(seed) : seed;
    }

    protected Color computeForeground(Component renderer,
                                      ComponentAdapter adapter) {
        Color seed = getColumnForeground(adapter.getColumnName(adapter.column));
        if (seed == null) {
            seed = foreground == null ? adapter.getComponent().getForeground() :
                foreground;
        }
        return adapter.isSelected() ? computeSelectedForeground(seed) : seed;

    }
}
