/*
 * $Id: IconPanel.java 1281 2007-04-30 09:51:05Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;

import org.jdesktop.swingx.JXListView.ListViewPanel;

/**
 * Provides the icon view mode. This should really have a plaf delegate.
 *
 * @author Rob Stone
 */
class IconPanel extends JPanel implements ListViewPanel, MouseListener, MouseMotionListener, KeyListener, FocusListener
{
    // Specifies the border around our complete icon (including the text).
    // This should really be end-user changeable
    private static final int BORDER_WIDTH=8;

    // Rely on Table for these settings
    static final Color SELECTION_FOREGROUND=(Color)UIManager.getLookAndFeelDefaults().get("Table.selectionForeground");
    static final Color SELECTION_BACKGROUND=(Color)UIManager.getLookAndFeelDefaults().get("Table.selectionBackground");
    static final Color SELECTION_AREA=(Color)UIManager.getLookAndFeelDefaults().get("Table.light");

    private final IconLabelUI iconLabelUI=new IconLabelUI(this);
    private final Dimension preferredSize=new Dimension();
    private final IconLabel cell=new IconLabel();
    private final LinkedHashMap<Integer,Integer> paintOrder=new LinkedHashMap<Integer,Integer>();
    private final Map<Integer, Integer> selectedRows=new HashMap<Integer, Integer>();
    private final Map<Integer, Point> startDragModelPoints=new HashMap<Integer, Point>();
    private final Rectangle selectionRectangle=new Rectangle();

    private JXListView listView;
    private Dimension cellSize;
    private Dimension cellHitSize;
    private Point startDragMousePoint;

    private boolean dragSelect=false;
    private boolean finishedDragSelect=false;
    private int focusedRow=-1;

    /**
     *  A wrapper for JLabel, provides the extra bits that we need to know
     *  about when drawing our icons.
     */
    static class IconLabel extends JLabel
    {
        private boolean hasFocus;
        private IconPanel iconPanel;

        @Override
        public boolean hasFocus()
        {
            return hasFocus;
        }

        public void setHasFocus(boolean hasFocus)
        {
            this.hasFocus = hasFocus;
        }

        public IconPanel getIconPanel()
        {
            return iconPanel;
        }

        public void setIconPanel(IconPanel iconPanel)
        {
            this.iconPanel = iconPanel;
        }
    }

    /**
     *  Creates the panel
     */
    public IconPanel()
    {
        // Make sure that we can display tooltips
        final ToolTipManager toolTipManager=ToolTipManager.sharedInstance();
        toolTipManager.registerComponent(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addFocusListener(this);
        cell.setVerticalTextPosition(JLabel.BOTTOM);
        cell.setHorizontalTextPosition(JLabel.CENTER);
        cell.setOpaque(false);
        cell.setUI(iconLabelUI);
        cell.setIconPanel(this);
    }

    /**
     *  Paints the icon.
     *  @param graphics the graphics context
     *  @param row the row to paint
     */
    private void paintIcon(
        final Graphics graphics,
        final int row)
    {
        final Rectangle rect=getCellRect(row);
        ImageIcon icon=listView.getModel().getLargeIcon(row);
        if (icon==null)
        {
            // Fallback to small icon if no large version is available.
            icon=listView.getModel().getSmallIcon(row);
        }
        final String value=listView.getModel().getDisplayValue(row, 0).toString();

        cell.setText(value);

        if (selectedRows.containsKey(row))
        {
            // This icon is selected, we will 'borrow' the disabled drawing code
            // of the label to make it draw us slightly differently when disabled
            cell.setForeground(SELECTION_FOREGROUND);
            cell.setEnabled(false);
            if (icon!=null)
            {
                cell.setDisabledIcon(SelectedIcon.get(icon));
            }
            else
            {
                cell.setDisabledIcon(null);
            }
        }
        else
        {
            cell.setForeground(listView.getForeground());
            cell.setEnabled(true);
            cell.setIcon(icon);
        }

        // Does this icon currently have focus ?
        cell.setHasFocus(row==focusedRow);

        // Draw the icon
        SwingUtilities.paintComponent(graphics, cell, this, rect);
    }

    /**
     *  Calculates where the icon should be drawn.
     *  Based on the JTable getCellRect code.
     *
     *  @param row  specifies what is to be drawn.
     *  @return where to draw the icon.
     */
    private Rectangle getCellRect(final int row)
    {
        final Rectangle rect=new Rectangle(cellSize);

        if (listView.isAutoArrange() || listView.getPosition(row)==null)
        {
            final int viewWidth=listView.getViewport().getWidth();
            int width=(viewWidth/cellSize.width)*cellSize.width;
            if (width==0)
            {
                width=cellSize.width;
            }
            rect.x=row * cellSize.width;
            if (rect.x + cellSize.width >= width)
            {
                rect.x=rect.x % width;
            }
            rect.y=((row * cellSize.height) / width) * cellSize.height;
            final Point p=new Point(rect.x, rect.y);
            listView.setPosition(row, p);
        }
        else
        {
            final Point p=listView.getPosition(row);
            rect.x=p.x;
            rect.y=p.y;
        }
        return rect;
    }

    /**
     *  Creates the painting order based on the initial model. This order may
     *  change if the user moves icons around. We always want the last selected
     *  icon to be displayed on top.
     */
    private void rebuildPaintOrder()
    {
        final ListViewModel model=listView.getModel();

        for (int row=0, rowCount=model.getRowCount(); row<rowCount; row++)
        {
            paintOrder.put(row, row);
        }
    }

    /**
     *  Takes a point and turns it into a model row if it is contained by
     *  any of our icons.
     *  @param point the point to convert
     *  @return the model row, or -1 if the point didn't match anything.
     */
    private int getRowFromPoint(final Point point)
    {
        int modelRow=-1;

        for (int row=0, numRows=listView.getModel().getRowCount();
             row<numRows;
             row++)
        {
            final Point modelPoint=listView.getPosition(row);
            if (modelPoint!=null)
            {
                final Rectangle rect=new Rectangle(
                    modelPoint.x + BORDER_WIDTH,
                    modelPoint.y + BORDER_WIDTH,
                    cellSize.width-BORDER_WIDTH,
                    cellSize.height-BORDER_WIDTH);
                if (rect.contains(point))
                {
                    modelRow=row;
                    break;
                }
            }
        }

        return modelRow;
    }

    /**
     *  Adds a row to the list of selected rows
     *  @param row the row to add
     */
    private void addToSelection(final int row)
    {
        selectedRows.put(row, row);
        startDragModelPoints.put(row, (Point)listView.getPosition(row).clone());
        // Make sure the paint order is always up to date
        paintOrder.remove(row);
        paintOrder.put(row, row);
    }

    /**
     *  Removes a row from the list of selected rows
     *  @param row the row to remove
     */
    private void removeFromSelection(final int row)
    {
        selectedRows.remove(row);
        startDragModelPoints.remove(row);
    }

    /**
     *  Called when the selection has changed. Passes the selection information
     *  on to the table that displays our details view. Don't really like this
     *  approach, the selection model should really be part of the list view and
     *  this should then be passed on to the various views...
     */
    private void selectionUpdated()
    {
        final ListSelectionModel selectionModel=listView.getSelectionModel();
        selectionModel.clearSelection();

        for (Integer selectedRow : selectedRows.values())
        {
            final int row=listView.getTable().convertRowIndexToView(selectedRow);
            selectionModel.addSelectionInterval(row, row);
        }
    }

    /**
     *  Calculates our preferred size. This is determined by whether or not
     *  we are in auto-arrange mode and the positions of our icons.
     */
    private void setPreferredSize()
    {
        int maxWidth=0;
        int maxHeight=0;

        for (int row=0, numRows=listView.getModel().getRowCount();
             row<numRows;
             row++)
        {
            Point p=listView.getPosition(row);
            if (p==null)
            {
                // No position yet - calculate it and look again
                getCellRect(row);
                p=listView.getPosition(row);
            }
            maxWidth=Math.max(maxWidth, p.x + cellSize.width);
            maxHeight=Math.max(maxHeight, p.y + cellSize.height);
        }

        final int oldWidth=preferredSize.width;
        final int oldHeight=preferredSize.height;

        preferredSize.width=listView.isAutoArrange() ? listView.getViewport().getWidth() : maxWidth;
        preferredSize.height=maxHeight;

        if (oldWidth!=preferredSize.width ||
            oldHeight!=preferredSize.height)
        {
            revalidate();
        }
    }

    /**
     *  Returns the row that currently has keyboard focus.
     *  @return the focused row
     */
    public int getFocusedRow()
    {
        return focusedRow;
    }

    /*
     * See interface
     */
    public void setListView(final JXListView listView)
    {
        this.listView=listView;
    }

    /*
     * See interface
     */
    public void modelChanged(final TableModelEvent tableModelEvent)
    {
        paintOrder.clear();
        if (tableModelEvent!=null &&
            tableModelEvent.getFirstRow()==TableModelEvent.HEADER_ROW)
        {
            // Table structure has changed. Clear down all stored state as it
            // won't/can't be the same for the new model.
            paintOrder.clear();
            selectedRows.clear();
            startDragModelPoints.clear();
            focusedRow=-1;
            startDragMousePoint=null;
            dragSelect=false;
            finishedDragSelect=false;
        }
        repaint();
    }

    /*
     * See interface
     */
    public void mouseClicked(MouseEvent mouseEvent)
    {
    }

    /*
     * See interface
     */
    public void mousePressed(final MouseEvent mouseEvent)
    {
        requestFocusInWindow();
        startDragMousePoint=mouseEvent.getPoint();

        int row=getRowFromPoint(startDragMousePoint);
        if (row!=-1)
        {
            // Select (or deselect) one of our icons
            if (mouseEvent.isControlDown())
            {
                if (selectedRows.containsKey(row))
                {
                    removeFromSelection(row);
                }
                else
                {
                    addToSelection(row);
                }
            }
            else
            {
                if ((selectedRows.size()>0 && !finishedDragSelect) ||
                    !selectedRows.containsKey(row))
                {
                    selectedRows.clear();
                    startDragModelPoints.clear();
                }
                addToSelection(row);
            }

            if (focusedRow!=-1)
            {
                focusedRow=row;
            }

            selectionUpdated();
            repaint();
        }
        else
        {
            // No icons have been clicked, so start a drag select
            selectedRows.clear();
            startDragModelPoints.clear();
            selectionUpdated();

            dragSelect=true;
        }
    }

    /*
     * See interface
     */
    public void mouseReleased(MouseEvent mouseEvent)
    {
        startDragMousePoint=null;
        finishedDragSelect=dragSelect;
        dragSelect=false;
        repaint();
    }

    /*
     * See interface
     */
    public void mouseEntered(MouseEvent mouseEvent)
    {
    }

    /*
     * See interface
     */
    public void mouseExited(MouseEvent mouseEvent)
    {
    }

    /*
     * See interface
     */
    public void mouseDragged(MouseEvent mouseEvent)
    {
        if (startDragMousePoint!=null)
        {
            if (dragSelect)
            {
                final Point currentDragMousePoint=mouseEvent.getPoint();

                selectionRectangle.x=startDragMousePoint.x<currentDragMousePoint.x ? startDragMousePoint.x : currentDragMousePoint.x;
                selectionRectangle.y=startDragMousePoint.y<currentDragMousePoint.y ? startDragMousePoint.y : currentDragMousePoint.y;
                selectionRectangle.width=Math.abs(startDragMousePoint.x-currentDragMousePoint.x);
                selectionRectangle.height=Math.abs(startDragMousePoint.y-currentDragMousePoint.y);

                // Look for all icons that are contained within the selection
                // area.
                selectedRows.clear();
                startDragModelPoints.clear();
                for (
                    int row=0, numRows=listView.getModel().getRowCount();
                    row<numRows;
                    row++)
                {
                    final Point p=listView.getPosition(row);

                    if (p!=null && selectionRectangle.intersects(
                        new Rectangle(
                            p.x+BORDER_WIDTH,
                            p.y+BORDER_WIDTH,
                            cellHitSize.width,
                            cellHitSize.height)))
                    {
                        addToSelection(row);
                    }
                }
                selectionUpdated();
            }
            else if (!listView.isAutoArrange())
            {
                // Move the selected icon(s) to the relevant positions
                final Point mousePoint=mouseEvent.getPoint();
                final int xdiff=mousePoint.x-startDragMousePoint.x;
                final int ydiff=mousePoint.y-startDragMousePoint.y;
                for (Integer row : selectedRows.keySet())
                {
                    final Point start=startDragModelPoints.get(row);
                    final Point current=listView.getPosition(row);
                    current.x=Math.max(start.x+xdiff,0);
                    current.y=Math.max(start.y+ydiff,0);
                }
                setPreferredSize();
            }

            repaint();
        }
    }

    /*
     * See interface
     */
    public void mouseMoved(MouseEvent mouseEvent)
    {
    }

    /*
     * See interface
     */
    public void refreshView()
    {
        // Build up selection based on the list views table
        selectedRows.clear();
        startDragModelPoints.clear();
        startDragMousePoint=null;
        dragSelect=false;
        finishedDragSelect=false;

        for (int selectedRow : listView.getSelectedRows())
        {
            final int row=listView.getTable().convertRowIndexToModel(selectedRow);
            selectedRows.put(row, row);
        }

        cellSize=new Dimension(listView.getLargeIconSize().width + BORDER_WIDTH*2 + 4, listView.getLargeIconSize().height + BORDER_WIDTH*2 + 4);
        cellHitSize=new Dimension(listView.getLargeIconSize());
        setPreferredSize();
        repaint();
    }

    /*
     * See interface
     */
    public void keyTyped(final KeyEvent e)
    {
    }

    /*
     * See interface
     */
    public void keyPressed(final KeyEvent e)
    {
        // Allow the user to navigate around the icons using the cursor keys.
        // At the moment this only handles left/right, Windows also uses up/down.
        // I guess this should probably tie into how native platforms do it ?

        if (focusedRow==-1)
        {
            focusedRow=selectedRows.size()==0 ? 0 : selectedRows.values().toArray(new Integer[] {})[0];
        }

        if (e.getKeyCode()==KeyEvent.VK_LEFT)
        {
            focusedRow--;
            if (focusedRow<0)
            {
                focusedRow=listView.getModel().getRowCount()-1;
            }
        }
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
        {
            focusedRow++;
            if (focusedRow>=listView.getModel().getRowCount())
            {
                focusedRow=0;
            }
        }

        if (focusedRow>=0)
        {
            if (e.isControlDown())
            {
                if (e.getKeyCode()==KeyEvent.VK_SPACE)
                {
                    if (selectedRows.containsKey(focusedRow))
                    {
                        removeFromSelection(focusedRow);
                    }
                    else
                    {
                        addToSelection(focusedRow);
                    }
                    selectionUpdated();
                }
            }
            else
            {
                selectedRows.clear();
                startDragModelPoints.clear();
                addToSelection(focusedRow);
                selectionUpdated();
            }
        }

        repaint();
    }

    /*
     * See interface
     */
    public void keyReleased(final KeyEvent e)
    {
    }

    /*
     * See interface
     */
    public void focusGained(FocusEvent focusEvent)
    {
        // Whenever focus changes repaint() so that focus stuff is redrawn
        repaint();
    }

    /*
     * See interface
     */
    public void focusLost(FocusEvent focusEvent)
    {
        // Whenever focus changes repaint() so that focus stuff is redrawn
        repaint();
    }

    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);

        // Redraw our icons, if we don't yet know the paint order then find out.
        if (paintOrder.size()==0)
        {
            rebuildPaintOrder();
        }

        final int width=listView.isAutoArrange() ? listView.getViewport().getWidth() : getWidth();
        final int height=listView.isAutoArrange() ? listView.getViewport().getHeight() : getHeight();
        g.setColor(listView.getBackground());
        g.fillRect(0, 0, width, height);

        // If we are drag selecting then draw the selection area first
        if (dragSelect)
        {
            g.setColor(SELECTION_FOREGROUND);

            g.setColor(SELECTION_BACKGROUND);
            g.drawRect(
                selectionRectangle.x-1,
                selectionRectangle.y-1,
                selectionRectangle.width+1,
                selectionRectangle.height+1);
            g.setColor(SELECTION_AREA);
            g.fillRect(
                selectionRectangle.x,
                selectionRectangle.y,
                selectionRectangle.width,
                selectionRectangle.height);
        }

        // Now draw each of the icons
        final ListViewModel model=listView.getModel();

        for (Integer row : paintOrder.values())
        {
            paintIcon(g, row);
        }

        setPreferredSize();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return preferredSize;
    }

    @Override
    public String getToolTipText(final MouseEvent event)
    {
        final ListViewModel model=listView.getModel();
        final int row=getRowFromPoint(event.getPoint());

        String tip=null;

        if (row>=0 && row<model.getRowCount() && model.getColumnCount()>1)
        {
            tip=model.getToolTipText(row, 1);
        }

        if (tip==null)
        {
            tip=getToolTipText();
        }

        return tip;
    }

    @Override
    public String getToolTipText()
    {
        return listView==null ? null : listView.getToolTipText();
    }

    @Override
    public Font getFont()
    {
        return listView==null ? super.getFont() : listView.getFont();
    }

    @Override
    public Color getBackground()
    {
        return listView==null ? super.getBackground() : listView.getBackground();
    }

    @Override
    public Color getForeground()
    {
        return listView==null ? super.getBackground() : listView.getForeground();
    }
}
