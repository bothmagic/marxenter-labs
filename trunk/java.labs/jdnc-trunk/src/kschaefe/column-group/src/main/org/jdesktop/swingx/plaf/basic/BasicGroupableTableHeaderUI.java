/*
 * $Id: BasicGroupableTableHeaderUI.java,v 1.5 2006/09/28 07:29:19 evickroy Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;


import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.plaf.GroupableTableHeaderUI;
import org.jdesktop.swingx.table.TableColumnExt2;

/**
 *
 * @author erik
 */
public class BasicGroupableTableHeaderUI extends BasicTableHeaderUI implements
        GroupableTableHeaderUI {
    protected class GroupableMouseInputHandler extends MouseInputHandler {
        private int mouseXOffset;
        
        //c&p'd from BasicTableHeaderUI.MouseInputHandler; original should be protected
        protected TableColumn getResizingColumn(Point p, int column) {
            if (column == -1) {
                return null;
            }
            Rectangle r = header.getHeaderRect(column);
            r.grow(-3, 0);
            if (r.contains(p)) {
                return null;
            }
            int midPoint = r.x + r.width / 2;
            int columnIndex;
            if (header.getComponentOrientation().isLeftToRight()) {
                columnIndex = (p.x < midPoint) ? column - 1 : column;
            } else {
                columnIndex = (p.x < midPoint) ? column : column - 1;
            }
            if (columnIndex == -1) {
                return null;
            }
            return header.getColumnModel().getColumn(columnIndex);
        }

        public void mouseReleased(MouseEvent e) {
            realDraggedColumn = null;
            super.mouseReleased(e);
        }
        
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            realDraggedColumn = null;
            header.setDraggedColumn(null);
            header.setResizingColumn(null);
            header.setDraggedDistance(0);

            Point p = e.getPoint();

            // First find which header cell was hit
            TableColumnModel columnModel = header.getColumnModel();
            int index = header.columnAtPoint(p);

            if (index != -1) {
                // The last 3 pixels + 3 pixels of next column are for resizing
                TableColumn resizingColumn = getResizingColumn(p, index);
                if (!canResize(resizingColumn, header) && header.getReorderingAllowed()) {
                    TableColumn hitColumn = columnModel.getColumn(index);
                    //determine if hit column was me or ancestor; set real hit
                    realDraggedColumn = getHitColumn(hitColumn, p.y);
                    header.setDraggedColumn(hitColumn);
                    mouseXOffset = p.x;
                }
            }
        }
        
        protected TableColumn getHitColumn(TableColumn aColumn, int y) {
            if (aColumn instanceof TableColumnExt2) {
                int index = 0;
                
                for (int h = 0, i = 0; i < groupHeights.size(); i++) {
                    h += groupHeights.get(i);
                    
                    if (y <= h) {
                        break;
                    }
                    
                    index++;
                }
                
                List<TableColumnExt2> hierarchy = new ArrayList<TableColumnExt2>();
                for (TableColumnExt2 cur = (TableColumnExt2) aColumn; cur != null; cur = cur.getParent()) {
                    hierarchy.add(0, cur);
                }
                
                if (index < hierarchy.size()) {
                    return hierarchy.get(index);
                }
            }
            
            return aColumn;
        }

        public void mouseDragged(MouseEvent e) {
            TableColumn draggedColumn = header.getDraggedColumn();
            int mouseX = e.getX();
            boolean headerLeftToRight = header.getComponentOrientation().isLeftToRight();

            if (draggedColumn != null) {
                TableColumnModel cm = header.getColumnModel();
                int draggedDistance = mouseX - mouseXOffset;
                int direction = (draggedDistance < 0) ? -1 : 1;
                int columnIndex = viewIndexForColumn(draggedColumn);
                int newColumnIndex = columnIndex + (headerLeftToRight ? direction : -direction);
                if (0 <= newColumnIndex && newColumnIndex < cm.getColumnCount()) {
                    int width = cm.getColumn(newColumnIndex).getWidth();
                    if (Math.abs(draggedDistance) > (width / 2)) {
                        JTable table = header.getTable();

                        mouseXOffset = mouseXOffset + direction * width;
                        header.setDraggedDistance(draggedDistance - direction * width);

                        // Cache the selected column.
                        int selectedIndex = table
                                .convertColumnIndexToModel(getSelectedColumnIndex());

                        // Now do the move.
                        cm.moveColumn(columnIndex, newColumnIndex);

                        // Update the selected index.
                        selectColumn(table.convertColumnIndexToView(selectedIndex), false);

                        return;
                    }
                }
                setDraggedDistance(draggedDistance, columnIndex);
            } else {
                super.mouseDragged(e);
            }
        }

        //c&p'd from BasicTableHeaderUI.MouseInputHandler; original should be protected
        private void setDraggedDistance(int draggedDistance, int column) {
            header.setDraggedDistance(draggedDistance);
            
            if (column != -1) {
                header.getColumnModel().moveColumn(column, column);
            }
        }
    }
    
    private static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR); 

    //c&p'd from BasicTableHeaderUI; original should be protected
    protected static boolean canResize(TableColumn column, JTableHeader header) {
        return (column != null) && header.getResizingAllowed() && column.getResizable();
    }
    
    //
    // Instance Variables
    //
    
    private final List<Integer> groupHeights = new ArrayList<Integer>();
    private int selectedColumnIndex = 0;
    
    //TODO reset to 0 when PCE fire renderer change
    private int defaultHeaderHeight = 0;

    //
    //  The installation/uninstall procedures and support
    //
    
    public static ComponentUI createUI(JComponent h) {
        return new BasicGroupableTableHeaderUI();
    }
    
    //
    //  Factory methods for the Listeners
    //

    /**
     * Creates the mouse listener for the JTableHeader.
     */
    protected MouseInputListener createMouseInputListener() {
        return new GroupableMouseInputHandler();
    }
    
    //
    // Support for keyboard and mouse access
    //

    private int getSelectedColumnIndex() {
        int numCols = header.getColumnModel().getColumnCount();
        if (selectedColumnIndex >= numCols && numCols > 0) {
            selectedColumnIndex = numCols - 1;
        }
        return selectedColumnIndex;
    }

    //
    // Paint Methods and support
    //

    public void paint(Graphics g, JComponent component) {
        if (header.getColumnModel().getColumnCount() <= 0) {
            return;
        }
        
        boolean ltr = header.getComponentOrientation().isLeftToRight();

        Rectangle clip = g.getClipBounds();
        Point left = clip.getLocation();
        Point right = new Point(clip.x + clip.width - 1, clip.y);
        TableColumnModel cm = header.getColumnModel();
        int cMin = header.columnAtPoint(ltr ? left : right);
        int cMax = header.columnAtPoint(ltr ? right : left);
        // This should never happen.
        if (cMin == -1) {
            cMin = 0;
        }
        // If the table does not have enough columns to fill the view we'll get -1.
        // Replace this with the index of the last column.
        if (cMax == -1) {
            cMax = cm.getColumnCount() - 1;
        }

        TableColumn draggedColumn = header.getDraggedColumn();
        int columnWidth;
        Rectangle cellRect = header.getHeaderRect(ltr ? cMin : cMax);
        TableColumnExt2 aColumn;
        Rectangle draggedCellRect = null;
        
        if (ltr) {
            for (int column = cMin; column <= cMax; column++) {
                aColumn = (TableColumnExt2) cm.getColumn(column);
                
                int i = paintGroup(g, aColumn.getParent(), cellRect);
                cellRect.height = 0;
                
                for (; i < groupHeights.size(); i++) {
                    cellRect.height += groupHeights.get(i);
                }
                
                columnWidth = aColumn.getWidth();
                cellRect.width = columnWidth;
                
                if (isDraggedColumn(aColumn)) {
                    //TODO getting width this way, but not height
                    if (draggedCellRect == null) {
                        draggedCellRect = new Rectangle(cellRect);
                    } else {
                        draggedCellRect.add(cellRect);
                    }
                } else {
                    paintCell(g, cellRect, column);
                }
                cellRect.x += columnWidth;
            }
        } else {
            for (int column = cMax; column >= cMin; column--) {
                aColumn = (TableColumnExt2) cm.getColumn(column);
                int i = paintGroup(g, aColumn.getParent(), cellRect);
                cellRect.height = 0;
                
                for (; i < groupHeights.size(); i++) {
                    cellRect.height += groupHeights.get(i);
                }
                
                columnWidth = aColumn.getWidth();
                cellRect.width = columnWidth;
                
                if (isDraggedColumn(aColumn)) {
                    draggedCellRect = new Rectangle(cellRect);
                } else {
                    paintCell(g, cellRect, column);
                }
                cellRect.x += columnWidth;
            }
        }

        // Paint the dragged column if we are dragging.
        if (draggedColumn != null) {
            int draggedColumnIndex = viewIndexForColumn(draggedColumn);

            // Draw a gray well in place of the moving column.
            g.setColor(header.getParent().getBackground());
            g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width,
                    draggedCellRect.height);

            draggedCellRect.x += header.getDraggedDistance();

            // Fill the background.
            g.setColor(header.getBackground());
            g.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width,
                    draggedCellRect.height);

            paintCell(g, draggedCellRect, draggedColumnIndex);
        }

        // Remove all components in the rendererPane.
        rendererPane.removeAll();
        paintedGroups.clear();
    }
    
    protected TableColumn realDraggedColumn;

    protected boolean isDraggedColumn(TableColumn col) {
        if (realDraggedColumn == null) {
            return false;
        }
        
        if (col == realDraggedColumn) {
            return true;
        }
        
        if (col instanceof TableColumnExt2) {
            return isDraggedColumn(((TableColumnExt2) col).getParent());
        }
        
        return false;
    }
    
    private Set<TableColumnExt2> paintedGroups = new HashSet<TableColumnExt2>();
    
    /**
     * @param parent
     */
    private int paintGroup(Graphics g, TableColumnExt2 group, Rectangle cellRect) {
        if (group == null) {
            cellRect.y = 0;
            return 0;
        }

        int index = paintGroup(g, group.getParent(), cellRect);
        
        cellRect.width = group.getWidth();
        cellRect.height = groupHeights.get(index);
        
        if (!paintedGroups.contains(group)) {
            paintedGroups.add(group);
            
            Component component = getHeaderRenderer(group);
            rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
                    cellRect.width, cellRect.height, true);
            //TODO compute draggedCellRect here?
        }
        
        cellRect.y += cellRect.height;
        
        return index + 1;
    }

    private Component getHeaderRenderer(int columnIndex) {
        TableColumn aColumn = header.getColumnModel().getColumn(columnIndex);
        TableCellRenderer renderer = aColumn.getHeaderRenderer();
        
        if (renderer == null) {
            renderer = header.getDefaultRenderer();
        }

        boolean hasFocus = !header.isPaintingForPrint()
                && (columnIndex == getSelectedColumnIndex()) && header.hasFocus();
        
        return renderer.getTableCellRendererComponent(header.getTable(), aColumn.getHeaderValue(),
                false, hasFocus, -1, columnIndex);
    }

    private void paintCell(Graphics g, Rectangle cellRect, int columnIndex) {
        Component component = getHeaderRenderer(columnIndex); 
        rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
                            cellRect.width, cellRect.height, true);
    }

    /**
     * Retrieves view index for the specified column.
     * 
     * @param aColumn
     *            Table column.
     * @return View index for the specified column.
     */
    protected int viewIndexForColumn(TableColumn aColumn) {
            TableColumnModel cm = header.getColumnModel();
            for (int column = 0; column < cm.getColumnCount(); column++) {
                    if (cm.getColumn(column) == aColumn) {
                            return column;
                    }
            }
            return -1;
    }
    
    //
    // Size Methods
    //

    //Modified from BasicTableHeader to handle groups
    private int getHeaderHeight() {
        TableColumnModel columnModel = header.getColumnModel();
        
        for (int column = 0; column < columnModel.getColumnCount(); column++) {
            TableColumn aColumn = columnModel.getColumn(column);
            List<Integer> currentHeights = new ArrayList<Integer>();
            
            for (TableColumnExt2 col = (TableColumnExt2) aColumn; col != null; col = col.getParent()) {
                boolean isDefault = (aColumn.getHeaderRenderer() == null);
                
                // Configuring the header renderer to calculate its preferred size
                // is expensive. Optimize this by assuming the default renderer
                // always has the same height as the first non-zero height that
                // it returns for a non-null/non-empty value.
                if (defaultHeaderHeight == 0) {
                    Component comp = getHeaderRenderer(column);
                    defaultHeaderHeight = comp.getPreferredSize().height;
                }
                
                if (isDefault) {
                    //potential bug if the defaultHeight was returned as 0
                    currentHeights.add(defaultHeaderHeight);
                } else {
                    currentHeights.add(getHeaderRenderer(column).getPreferredSize().height);
                }
            }
            
            updateGroupHeights(currentHeights);
        }
        
        int maxHeight = 0;
        
        for (Integer i : groupHeights) {
            maxHeight += i;
        }
        
        return maxHeight;
    }

    /**
     * @param currentHeights
     */
    private void updateGroupHeights(List<Integer> currentHeights) {
        Collections.reverse(currentHeights);
        
        for (int i = 0, len = currentHeights.size(); i < len; i++) {
            if (i < groupHeights.size()) {
                groupHeights.add(i, Math.max(groupHeights.remove(i), currentHeights.get(i)));
            } else {
                groupHeights.add(currentHeights.get(i));
            }
        }
    }

    //copied from BasicTableHeader
    private Dimension createHeaderSize(long width) {
        TableColumnModel columnModel = header.getColumnModel();
        // None of the callers include the intercell spacing, do it here.
        if (width > Integer.MAX_VALUE) {
            width = Integer.MAX_VALUE;
        }
        return new Dimension((int)width, getHeaderHeight());
    }

    /**
     * Return the minimum size of the header. The minimum width is the sum 
     * of the minimum widths of each column (plus inter-cell spacing).
     */
    //copied from BasicTableHeader
    public Dimension getMinimumSize(JComponent c) {
        long width = 0;
        Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn aColumn = enumeration.nextElement();
            width = width + aColumn.getMinWidth();
        }
        return createHeaderSize(width);
    }

    /**
     * Return the preferred size of the header. The preferred height is the 
     * maximum of the preferred heights of all of the components provided 
     * by the header renderers. The preferred width is the sum of the 
     * preferred widths of each column (plus inter-cell spacing).
     */
    //copied from BasicTableHeader
    public Dimension getPreferredSize(JComponent c) {
        long width = 0;
        Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn aColumn = enumeration.nextElement();
            width = width + aColumn.getPreferredWidth();
        }
        return createHeaderSize(width);
    }

    /**
     * Return the maximum size of the header. The maximum width is the sum 
     * of the maximum widths of each column (plus inter-cell spacing).
     */
    //copied from BasicTableHeader
    public Dimension getMaximumSize(JComponent c) {
        long width = 0;
        Enumeration<TableColumn> enumeration = header.getColumnModel().getColumns();
        while (enumeration.hasMoreElements()) {
            TableColumn aColumn = enumeration.nextElement();
            width = width + aColumn.getMaxWidth();
        }
        return createHeaderSize(width);
    }

    public int columnAtPoint(Point point) {
        int x = point.x;
        if (!header.getComponentOrientation().isLeftToRight()) {
            x = getWidthInRightToLeft() - x;
        }
        
        TableColumnModel model = header.getColumnModel();
        int index = model.getColumnIndexAtX(x);
        
        if (groupHeights.isEmpty()) {
            return index;
        }
        
        TableColumnExt2 col = (TableColumnExt2) model.getColumn(index);
        int ancestorOrSelf = 0;
        
        for (int i = 0, sum = 0; i < groupHeights.size(); i++) {
            sum += groupHeights.get(i);
            
            if (point.y <= sum) {
                ancestorOrSelf = i;
            }
        }
        
        return columnFromHierarchy(col, ancestorOrSelf).getModelIndex();
    }
    
    //c&p'd from JTableHeader (modified for use outside of class
    private int getWidthInRightToLeft() {
        JTable table = header.getTable();
        if ((table != null) && (table.getAutoResizeMode() != JTable.AUTO_RESIZE_OFF)) {
            return table.getWidth();
        }
        return header.getWidth();
    }
    
    private TableColumnExt2 columnFromHierarchy(TableColumnExt2 col, int position) {
        List<TableColumnExt2> hierarchy = new ArrayList<TableColumnExt2>();
        
        for (TableColumnExt2 cur = col; cur == null; cur = cur.getParent()) {
            hierarchy.add(0, cur);
        }
        
        if (position >= hierarchy.size()) {
            return hierarchy.get(hierarchy.size() - 1);
        } else {
            return hierarchy.get(position);
        }
    }
    
    //c&p'd from BasicTableHeaderUI
    void selectColumn(int newColIndex, boolean doScroll) {
        Rectangle repaintRect = header.getHeaderRect(selectedColumnIndex);
        header.repaint(repaintRect);
        selectedColumnIndex = newColIndex;
        repaintRect = header.getHeaderRect(newColIndex);
        header.repaint(repaintRect);
        if (doScroll) {
            scrollToColumn(newColIndex);
        }
        return;
    }
    
    /**
     * Used by selectColumn to scroll horizontally, if necessary,
     * to ensure that the newly selected column is visible.
     */
    //c&p'd from BasicTableHeaderUI
    private void scrollToColumn(int col) {
        Container container;
        JTable table;
        
        //Test whether the header is in a scroll pane and has a table.
        if ((header.getParent() == null) ||
            ((container = header.getParent().getParent()) == null) ||
            !(container instanceof JScrollPane) ||
            ((table = header.getTable()) == null)) {
            return;
        }

        //Now scroll, if necessary.
        Rectangle vis = table.getVisibleRect();
        Rectangle cellBounds = table.getCellRect(0, col, true);
        vis.x = cellBounds.x;
        vis.width = cellBounds.width;
        table.scrollRectToVisible(vis);
    }

    //
    //TODO new code
    //
    
    private List<TableColumnExt2> getGroupsForColumn(TableColumnExt2 col) {
        List<TableColumnExt2> groups = new ArrayList<TableColumnExt2>();
        
        for (TableColumnExt2 tc = col.getParent(); tc != null; tc = tc.getParent()) {
            groups.add(tc);
        }
        
        return groups;
    }
//    
//    private Dimension getSizeOfGroup(ColumnGroup group) {
//        TableCellRenderer r = group.getCellRenderer();
//        
//        if (r == null) {
//            r = header.getDefaultRenderer();
//        }
//        
//        int h = r.getTableCellRendererComponent(header.getTable(),
//                group.getHeaderValue(), false, false, -1, -1).getHeight();
//        
//        return new Dimension(group.getWidth(), h);
//    }

    /**
     * Retrieves renderer for the specified column header.
     * 
     * @param columnIndex
     *            Column index.
     * @return Renderer for the specified column header.
     */
    protected Component getHeaderRenderer(TableColumnExt2 group) {
        TableCellRenderer renderer = group.getHeaderRenderer();
        
        if (renderer == null) {
            renderer = header.getDefaultRenderer();
        }
        
        return renderer.getTableCellRendererComponent(
            header.getTable(), group.getHeaderValue(), false, false, -1, -1);
    }

}
