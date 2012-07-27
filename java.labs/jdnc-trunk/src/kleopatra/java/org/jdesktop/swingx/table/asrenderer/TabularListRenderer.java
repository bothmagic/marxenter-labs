/*
 * Created on 12.09.2006
 *
 */
package org.jdesktop.swingx.table.asrenderer;

import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory.UIColorHighlighter;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * A <code>ListCellRenderer</code> which uses a one-row JXTable as rendering
 * component.
 *  
 * @author Jeanette Winzenburg
 * 
 * @deprecated need to be updated to SwingX renderer suppor via a custom provider. There
 *  is an example for a TableProvider in the kleopatra foreign hierarchy (package bolsover,
 *  as the example is borrowed from there). 
 */
@Deprecated
public class TabularListRenderer implements ListCellRenderer {
    private OneRowTableModel model;

    private JXTable table;

    private ListCellRenderer renderer;

    private boolean enableHighlight;

    private MAlternateRowHighlighter highlighter;

    private int viewColumn;

    public TabularListRenderer() {
        this(false, 3);
    }

    public TabularListRenderer(boolean highlight, Object[] columnNames) {
        renderer = new DefaultListCellRenderer();
        model = new OneRowTableModel(columnNames);
        table = new JXTable(model);
        ColumnFactory factory = new ColumnFactory() {

            @Override
            public void packColumn(JXTable table, TableColumnExt columnExt, int margin, int max) {
                super.packColumn(table, columnExt, margin, max);
                columnExt.setPreferredWidth((int) (columnExt.getPreferredWidth() * 1.7));
            }
            
        };
        table.setColumnControlVisible(true);
        table.setColumnFactory(factory);
        table.packAll();
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setSortable(false);
        enableHighlighter(highlight);
    }

    public TabularListRenderer(boolean highlight, int columns) {
        renderer = new DefaultListCellRenderer();
        model = new OneRowTableModel(columns);
        table = new JXTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setSortable(false);
        enableHighlighter(highlight);
    }

    public void enableHighlighter(boolean highlight) {
        enableHighlight = highlight;
        if (enableHighlight) {
            highlighter = new MAlternateRowHighlighter();
            table.addHighlighter(highlighter);
        } else {
            highlighter = null;
            table.setHighlighters();
        }
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (index >= 0) {
            model.setRow((Object[]) value);
            if (isSelected) {
                table.addRowSelectionInterval(0, 0);
            } else {
                table.clearSelection();
            }
            if (highlighter != null) {
                highlighter.setInvers((index % 2) == 0);
            }
            return table;
        }
        if (value instanceof Object[]) {
            value = ((Object[]) value)[viewColumn];
        }
        return renderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
    }

    private static class OneRowTableModel extends DefaultTableModel {
        public OneRowTableModel(int columnCount) {
            super(0, columnCount);
            addRow(new Object[columnCount]);
        }

        public OneRowTableModel(Object[] columnNames) {
            super(columnNames, 0);
            addRow(new Object[columnNames.length]);
        }

        @SuppressWarnings("all")
        public void setRow(Object[] row) {
            Vector vector = (Vector) dataVector.get(0);
            int size = Math.min(vector.size(), row.length);
            for (int col = 0; col < size; col++) {
                vector.setElementAt(row[col], col);
            }
        }
    }

    private static class MAlternateRowHighlighter extends UIColorHighlighter {

        public MAlternateRowHighlighter() {
            super(HighlightPredicate.EVEN);
        }

        public void setInvers(boolean invers) {
            setHighlightPredicate(invers ? HighlightPredicate.ODD
                    : HighlightPredicate.EVEN);
        }

    }

}
