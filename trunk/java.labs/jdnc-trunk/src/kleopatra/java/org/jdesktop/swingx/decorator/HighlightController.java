/*
 * Created on 29.03.2008
 *
 */
package org.jdesktop.swingx.decorator;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.UIManager;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.incubatorutil.ColorUtils;

public class HighlightController {
    List<JXList> lists;
    List<JXTable> tables;

    CompoundHighlighterExt compound;
    
    public HighlightController() {
        lists = new ArrayList<JXList>();
        tables = new ArrayList<JXTable>();
        prepareHighlighters();
    }

    public void addHighlightable(JXTable table) {
        tables.add(table);
        table.setHighlighters(compound);
    }
    
    public void addHighlightable(JXList list) {
        lists.add(list);
        list.setHighlighters(compound);
    }
    
    private void prepareHighlighters() {
        compound = new CompoundHighlighterExt();
    }

    public void addHighlighter(Object identifier,
            Collection<Object> selectedValues, boolean allColumns, Color color) {
        HighlightPredicate predicate = new ValueMappedHighlightPredicate(
                identifier, allColumns, selectedValues.toArray());
        ColorHighlighter hl = new BlendingColorHighlighter(color);
        hl.setHighlightPredicate(predicate);
        compound.addHighlighter(hl);
    }

    public void addHighlighter(Object identifier,
            Comparable first, Comparable last, boolean allColumns, Color color) {
        HighlightPredicate predicate = new RangeHighlightPredicate(
                identifier, allColumns, first, last);
        ColorHighlighter hl = new BlendingColorHighlighter(color);
        hl.setHighlightPredicate(predicate);
        compound.addHighlighter(hl);
    }
    public void resetHighlighters() {
            compound.removeAllHighlighters();
    }
    
    public static class CompoundHighlighterExt extends CompoundHighlighter {
        
        public void removeAllHighlighters() {
            if (highlighters.size() == 0) return;
            for (Highlighter hl : highlighters) {
                hl.removeChangeListener(getHighlighterChangeListener());
            }
            highlighters.clear();
            fireStateChanged();
        }
    }

    /**
     * Custom background highlighter which tries to blend the highlight with
     * selection and adjust the foreground to be readable.
     */
    public static class BlendingColorHighlighter extends ColorHighlighter {
        private static final Color SELECTION_BACKGROUND = 
            UIManager.getColor("Table.selectionBackground");
        private Color rawSelectionBackground;
        
        public BlendingColorHighlighter(Color background) {
            super();
            rawSelectionBackground = SELECTION_BACKGROUND;
            setBackground(background);
        }


        private void blendSelectionBackground() {
            setSelectedBackground(ColorUtil.interpolate(getBackground(), rawSelectionBackground, 0.5f));
        }

        @Override
        public void setBackground(Color color) {
            super.setBackground(color);
            setForeground(ColorUtils.isDark(color) ? Color.white : Color.black);
            if (rawSelectionBackground != null) {
                blendSelectionBackground();
            }
        }

        @Override
        public void setSelectedBackground(Color color) {
            super.setSelectedBackground(color);
            setSelectedForeground(ColorUtils.isDark(getSelectedBackground()) ? Color.white : Color.black);
        }
        
        
    }
    
    /**
     * Custom predicate which returns true if the filtered cell value
     * of a given testColumn is contained in a list of values.
     * PENDING: logic similar to search/pattern, enough to abstract?
     */
    public static class ValueMappedHighlightPredicate implements HighlightPredicate {

        private List<Object> values;
        private Object identifier;
        private boolean allColumns;
        
        public ValueMappedHighlightPredicate(Object testIdentifier, boolean allColumns, Object... values) {
            this.values = Arrays.asList(values);
            this.identifier = testIdentifier;
            this.allColumns = allColumns;
        }
        
        public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            int modelColumn = adapter.getColumnIndex(identifier);
            if (modelColumn < 0) return false;
            if (!allColumns && (modelColumn != adapter.convertColumnIndexToModel(adapter.column)))
                return false;
            return values.contains(adapter.getValue(modelColumn));
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ValueMappedHighlightPredicate)) return false;
            ValueMappedHighlightPredicate other = (ValueMappedHighlightPredicate) obj;
            return identifier.equals(other.identifier) 
                && (allColumns == other.allColumns) 
                && values.equals(other.values);
        }
        
    }

    /**
     * Custom predicate which returns true if the filtered cell value
     * of a given testColumn is contained in a list of values.
     * PENDING: logic similar to search/pattern, enough to abstract?
     */
    public static class RangeHighlightPredicate implements HighlightPredicate {

         private Object identifier;
        private boolean allColumns;
        private Comparable last;
        private Comparable first;
        
        public RangeHighlightPredicate(Object testIdentifier, boolean allColumns, Comparable first, Comparable last) {
            this.first = first;
            this.last = last;
            this.identifier = testIdentifier;
            this.allColumns = allColumns;
        }
        
        public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
            int modelColumn = adapter.getColumnIndex(identifier);
            if (modelColumn < 0) return false;
            if (!allColumns && (modelColumn != adapter.convertColumnIndexToModel(adapter.column)))
                return false;
            Object value = adapter.getValue(modelColumn);
            return (first.compareTo(value) <= 0) && (last.compareTo(value) >= 0);
//            try {
//            } catch (Exception e) {
//                
//            }
//            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof RangeHighlightPredicate)) return false;
            RangeHighlightPredicate other = (RangeHighlightPredicate) obj;
            return identifier.equals(other.identifier) 
                && (allColumns == other.allColumns) 
                && first.equals(other.first)
                && last.equals(other.last);
        }
        
    }

}