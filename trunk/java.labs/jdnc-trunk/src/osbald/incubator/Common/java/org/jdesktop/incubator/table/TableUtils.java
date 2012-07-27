package org.jdesktop.incubator.table;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (www.osbald.co.uk)
 * Date: 19-Jan-2004
 * Time: 17:54:55
 */

public class TableUtils {

    public static void stopEditing(JTable table) {
        if (table.getCellEditor() != null) {
            table.getCellEditor().stopCellEditing();
        }
    }

    public static int firstEditableColumn(JTable table, int row) {
        int i = 0;
        while (!table.isCellEditable(row, i) && i < table.getColumnCount()) {
            i++;
        }
        return i < table.getColumnCount() ? i : -1;
    }

    public static boolean editCellAt(final JTable table, final int row, final int column) {
        stopEditing(table);
        //? scrollToRow(table, row, column);
        Container parent = table.getParent();
        if (parent instanceof JViewport) {
            ((JViewport) parent).scrollRectToVisible(table.getCellRect(row, column, true));
        }
        //todo would prefer the TableCellEditor requested the focus rather than pushing the focus after the event
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (table.getCellEditor() != null) {
                    Component component = table.getCellEditor().getTableCellEditorComponent(table,
                                                                                            table.getValueAt(row, column), false, row, column);
                    component.requestFocusInWindow();
                }
            }
        });
        return table.editCellAt(row, column);
    }

    public static void scrollToRow(JTable table, int row) {
        scrollToRow(table, row, 0);
    }

    public static void scrollToRow(JTable table, int row, int column) {
        Container parent = table.getParent();
        if (parent instanceof JViewport) {
            JViewport viewport = (JViewport) parent;
            if (viewport.getView() == table) {
                Rectangle cellRect = table.getCellRect(row, column, true);
                Rectangle viewRect = viewport.getViewRect();
                if (!viewRect.contains(cellRect)) {
                    Rectangle union = viewRect.union(cellRect);
                    int x = (int) (union.getX() + union.getWidth() - viewRect.getWidth());
                    int y = (int) (union.getY() + union.getHeight() - viewRect.getHeight());
                    viewport.setViewPosition(new Point(x, y));
                }
                //viewport.scrollRectToVisible(table.getCellRect(row, column, true));
            }
        }
    }

    public static void setRowsVisible(JTable table, int rows) {
        int height = 0;
        for (int row = 0; row < rows; row++) {
            height += table.getRowHeight(row);
        }
        table.setPreferredScrollableViewportSize(
                new Dimension(table.getPreferredScrollableViewportSize().width, height));
    }

    /** Find any sequential grouping of indexes (in order to optimize updates & events) */
    public static List<int[]> collateSequences(int... indexes) {
        if (indexes == null || indexes.length == 0) {
            return Collections.emptyList();
        } else if (indexes.length == 1) {
            return Collections.singletonList(new int[] {indexes[0]});
        }

        Arrays.sort(indexes);
        List<int[]> sequences = new ArrayList<int[]>();
        int rangeEnd = indexes.length - 1;
        for (int i = rangeEnd - 1, pos = rangeEnd;
             i >= 0 && indexes[i] >= 0; i--, pos = i + 1) {
            if (indexes[pos] - indexes[i] > 1) {
                // end of previous sequence
                if (rangeEnd - pos > 0) {
                    sequences.add(new int[] {indexes[pos], indexes[rangeEnd]});
                } else {
                    sequences.add(new int[] {indexes[pos]});
                }
                if (i == 0) {
                    // reached start of ranges, add whatever is leftover
                    sequences.add(new int[] {indexes[0]});
                }
                rangeEnd = i;
            } else if (i == 0) {
                // reached start of ranges, add whatever is leftover
                sequences.add(new int[] {indexes[i], indexes[rangeEnd]});
            }
        }
        return sequences;
    }

}
