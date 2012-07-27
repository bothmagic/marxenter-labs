/*
 * $Id: DetailsPanel.java 2686 2008-09-08 10:34:48Z osbald $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.search.TableSearchable;

/**
 *  Provides the details view mode. This is basically a JXTable.
 *
 * @author Rob Stone
 */
class DetailsPanel extends JXTable implements JXListView.ListViewPanel
{
    private JXListView listView;
    private final CellRenderer cellRenderer=new CellRenderer();

    /**
     * A custom search class that uses 'getDisplayValue' to ensure
     * that the items being searched for match those displayed by the model/view
     */
    private class DetailsSearchable extends TableSearchable {

        public DetailsSearchable(JXTable table) {
            super(table);
        }

        @Override
        protected SearchResult findMatchAt(
            final Pattern pattern,
            final int row,
            final int column)
        {
            final String text=listView==null ?
                null :
                listView.getModel().getDisplayValue(
                    convertRowIndexToModel(row),
                    convertColumnIndexToModel(column));
            
            if ((text!=null) && (text.length()>0 )) {
                final Matcher matcher=pattern.matcher(text);
                if (matcher.find()) {
                    return createSearchResult(matcher, row, column);
                }
            }
            return null;
        }        
    }
    
    /**
     * Provides the rendering for our table, we don't need the full flexibility
     * of the normal Table rendering system.
     */
    private class CellRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(
            final JTable table,
            final Object value,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int column)
        {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            final JLabel label=(JLabel)this;
            final int modelRow=((JXTable)table).convertRowIndexToModel(row);
            final int modelCol=((JXTable)table).convertColumnIndexToModel(column);

            // Icon is only displayed for column 0 of the model
            if (modelCol==0)
            {
                ImageIcon icon=listView.getModel().getSmallIcon(modelRow);
                if (isSelected && icon!=null)
                {
                    icon=SelectedIcon.get(icon);
                }
                label.setIcon(icon);
            }
            else
            {
                label.setIcon(null);
            }

            final String displayValue=listView.getModel().getDisplayValue(modelRow, modelCol);
            label.setText(displayValue);

            label.setToolTipText(listView.getModel().getToolTipText(modelRow, modelCol));

            return this;
        }
    }

    /**
     * Create the details view.
     */
    public DetailsPanel()
    {
        super();
        setDefaultRenderer(Object.class, cellRenderer);
        setSearchable(new DetailsSearchable(this));
        setShowGrid(false);
        setColumnControlVisible(true);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                final int row=rowAtPoint(e.getPoint());
                if (row>=0)
                {
                    if (SwingUtilities.isRightMouseButton(e))
                    {
                        if (!isRowSelected(row))
                        {
                            try
                            {
                                final Robot robot=new Robot();
                                robot.mousePress(InputEvent.BUTTON1_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                            }
                            catch (AWTException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                else
                {
                    // Clicked outside of table rows, clear the selection
                    clearSelection();
                }
            }
        });
    }

    /*
     * See interface
     */
    public void setListView(final JXListView listView)
    {
        this.listView=listView;

        // Pass in the model if we have one.
        if (listView.getModel()!=null)
        {
            setModel(listView.getModel());
        }
    }

    /*
     * See interface
     */
    public void modelChanged(final TableModelEvent tableModelEvent)
    {
        if (listView.getModel()!=null)
        {
            setModel(listView.getModel());
        }
    }

    /*
     * See interface
     */
    public void refreshView()
    {
        // If the small icon size changes then we want to change our row
        // height to match.
        setRowHeight(Math.max(listView.getSmallIconSize().height, getFont().getSize()));
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
