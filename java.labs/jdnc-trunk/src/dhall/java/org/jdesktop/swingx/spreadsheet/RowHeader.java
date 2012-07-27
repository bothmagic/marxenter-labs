// ============================================================================
// $Id: RowHeader.java 1290 2007-05-01 02:18:21Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

class RowHeader extends JComponent {
    static final long serialVersionUID = -1375303876648436931L;

    private JTable _table;
    private TableCellRenderer _renderer;
    private JTableHeader _header;
    private CellRendererPane _rendererPane;
    private Font _headerFont;

    public RowHeader(JTable table) {
        _table = table;
        _header = table.getTableHeader();
        _renderer = _header.getDefaultRenderer();
        _rendererPane = new CellRendererPane();
        add(_rendererPane);
        
        Component rendererComponent =
            _renderer.getTableCellRendererComponent(_table, "0", false, false, 0, -1);

        _headerFont = rendererComponent.getFont();
        
        setFont(_headerFont);
        setBackground(rendererComponent.getBackground());
        setForeground(rendererComponent.getForeground());
    }
    
    public TableCellRenderer getRenderer() { return _renderer; }
    public void setRenderer(TableCellRenderer renderer) { _renderer = renderer; }

    public void setRowCount(int count) {
        resize(getPreferredSize());
    }
    
    public Dimension getPreferredSize() {
        Border border = (Border) UIManager.getDefaults().get("TableHeader.cellBorder");
        Insets insets = border.getBorderInsets(_header);
        FontMetrics metrics = getFontMetrics(_headerFont);
        Dimension dim = new Dimension( metrics.stringWidth("99999") +insets.right +insets.left,
                                       _table.getRowHeight() * _table.getRowCount());
        return dim;
    }

    
    protected void paintComponent(Graphics g) {
        Rectangle cellRect = new Rectangle(0,0,getWidth(),_table.getRowHeight(0));
        int rowMargin = _header.getColumnModel().getColumnMargin() - 1;
        for (int i = 0; i < _table.getRowCount(); ++i) {
            int rowHeight = _table.getRowHeight(i);
            cellRect.height = rowHeight - rowMargin;
            paintCell(g, cellRect, i);
            cellRect.y += rowHeight;
        }
    }
    
    private void paintCell(Graphics g, Rectangle cellRect, int rowIndex) {
        Component component =
            _renderer.getTableCellRendererComponent(_table,rowIndex,false,false,rowIndex,-1);
        
        _rendererPane.paintComponent(g, component, this, cellRect.x, cellRect.y,
                                     cellRect.width, cellRect.height, true);
    }
}

