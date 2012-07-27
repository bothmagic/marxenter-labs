/*
 * $Id: JXListView.java 2416 2008-04-23 10:48:33Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.jdesktop.swingx.decorator.Highlighter;

/**
 * Provides the a list view based on that used by Windows Explorer. This provides
 * several (2 at the moment) different views onto the same information. A details
 * mode (think JXTable) that displays detailed information, and an icon mode
 * that provides more of an overview. This is a sub class of a ScrollPane because
 * I couldn't think of any instance where you wouldn't want it contained in
 * a scroll pane - this also saves us from having to do the stuff that is
 * required of scroll pane containable components to make them play nicely with
 * the scroll pane.
 *
 * @author Rob Stone
 */
public class JXListView extends JScrollPane implements TableModelListener
{
    public enum Mode {Details,Icon};

    private static final Dimension DEFAULT_SMALL_ICON_SIZE=new Dimension(16, 16);
    private static final Dimension DEFAULT_LARGE_ICON_SIZE=new Dimension(48, 48);

    private Dimension smallIconSize=DEFAULT_SMALL_ICON_SIZE;
    private Dimension largeIconSize=DEFAULT_LARGE_ICON_SIZE;

    private Mode mode;
    private ListViewModel model;
    private ListViewPanel currentView;
    private final Map<Integer, Point> positions=new HashMap<Integer, Point>();
    private final DetailsPanel detailsView=new DetailsPanel();
    private final IconPanel iconView=new IconPanel();
    private boolean autoArrange=true;

    /*
     * All views implement this
     */
    interface ListViewPanel
    {
        /**
         * Attaches the view to this list view
         * @param listView the list view to attach to
         */
        void setListView(final JXListView listView);

        /**
         * Lets the view know that the model has changed
         * @param tableModelEvent indicates why the model has changed, can be <code>null</code>
         */
        void modelChanged(final TableModelEvent tableModelEvent);

        /**
         * Asks the view to refresh itself.
         */
        void refreshView();
    }

    /**
     * Creates the list view
     */
    public JXListView()
    {
        setMode(Mode.Details);
        setBackground(Color.WHITE);
    }

    /**
     * @return the current view mode
     */
    public Mode getMode()
    {
        return mode;
    }

    /**
     * Sets the view mode
     * @param mode the new view mode
     */
    public void setMode(final Mode mode)
    {
        final Mode old=getMode();
        this.mode = mode;

        currentView=mode==Mode.Details ? detailsView : iconView;
        currentView.setListView(this);
        currentView.refreshView();

        setViewportView((Component)currentView);
        ((JComponent)currentView).requestFocusInWindow();

        firePropertyChange("mode", old, mode);
    }

    /**
     * @return the current model
     */
    public ListViewModel getModel()
    {
        return model;
    }

    /**
     * Sets the current model
     * @param model the model
     * @param clearPositions <code>true</code> clear out any stored icon positions, <code>false</code> leave the stored icon positions
     */
    public void setModel(final ListViewModel model, final boolean clearPositions)
    {
        final ListViewModel old=this.model;
        this.model = model;

        firePropertyChange("model", old, model);
        if (model!=old)
        {
            // Clear out the cache of selected icon images.
            SelectedIcon.clearCache();

            // New model, clear out any icon positions and let the views know
            // about the change
            if (clearPositions)
            {
                clearPositions();
            }
            model.addTableModelListener(this);
            detailsView.modelChanged(null);
            iconView.modelChanged(null);

            if (old!=null)
            {
                old.removeTableModelListener(this);
            }
        }
    }

    /**
     * @returns whether (<code>true</code>) or not (<code>false</code>) we are in auto-arrange mode
     */
    public boolean isAutoArrange()
    {
        return autoArrange;
    }

    /**
     * Sets the new auto-arrange mode
     * @param autoArrange the new auto-arrange mode
     */
    public void setAutoArrange(final boolean autoArrange)
    {
        final boolean old=isAutoArrange();
        this.autoArrange = autoArrange;
        firePropertyChange("autoArrange", old, autoArrange);

        // Force a redraw and get the view to update itself
        invalidate();
        currentView.refreshView();
    }

    /**
     * Returns a stored position for the given row
     * @param rowIndex the row
     * @return the rows position, can be <code>null</code>
     */
    public Point getPosition(final int rowIndex)
    {
        return positions.get(rowIndex);
    }

    /**
     * Stores a position for the given row
     * @param rowIndex the row
     * @param position the position
     */
    public void setPosition(final int rowIndex, final Point position)
    {
        positions.put(rowIndex, position);
    }

    /**
     * Clears the position for the given row
     * @param rowIndex the row to clear
     */
    public void clearPosition(final int rowIndex)
    {
        positions.remove(rowIndex);
    }

    /**
     * @return the table that provides our details view
     */
    public JXTable getTable()
    {
        return detailsView;
    }

    /**
     * @return the size of small icons
     */
    public Dimension getSmallIconSize()
    {
        return smallIconSize;
    }

    /**
     * Sets the size of small icons
     * @param smallIconSize the small icon size
     */
    public void setSmallIconSize(final Dimension smallIconSize)
    {
        final Dimension old=getSmallIconSize();
        this.smallIconSize = smallIconSize;
        firePropertyChange("smallIconSize", old, smallIconSize);
        currentView.refreshView();
    }

    /**
     * @return the size of large icons
     */
    public Dimension getLargeIconSize()
    {
        return largeIconSize;
    }

    /**
     * Sets the size of large icons
     * @param largeIconSize the large icon size
     */
    public void setLargeIconSize(final Dimension largeIconSize)
    {
        final Dimension old=getLargeIconSize();
        this.largeIconSize = largeIconSize;
        firePropertyChange("largeIconSize", old, largeIconSize);
        currentView.refreshView();
    }

    /**
     * Allows highlighters to be passed on to the details view JXTable.
     * @param highlighters the highlighters to pass on
     */
    public void setHighlighters(final Highlighter... highlighters)
    {
        getTable().setHighlighters(highlighters);
    }

    /**
     * @return the current row highlighters
     */
    public Highlighter[] getHighlighters()
    {
        return getTable().getHighlighters();
    }

    /**
     * @return the selection model.
     */
    public ListSelectionModel getSelectionModel()
    {
        return getTable().getSelectionModel();
    }

    /**
     * Sets the selection model
     * @param newModel the new selection model
     */
    public void setSelectionModel(final ListSelectionModel newModel)
    {
        getTable().setSelectionModel(newModel);
    }

    /**
     * @returns the currently selected rows
     */
    public int[] getSelectedRows()
    {
        return detailsView.getSelectedRows();
    }

    /**
     * Clears any stored positions
     */
    public void clearPositions()
    {
        positions.clear();
    }

    @Override
    public void addMouseListener(final MouseListener mouseListener)
    {
        detailsView.addMouseListener(mouseListener);
        iconView.addMouseListener(mouseListener);
    }

    @Override
    public void removeMouseListener(final MouseListener mouseListener)
    {
        detailsView.removeMouseListener(mouseListener);
        iconView.removeMouseListener(mouseListener);
    }

    @Override
    public void addKeyListener(final KeyListener keyListener)
    {
        detailsView.addKeyListener(keyListener);
        iconView.addKeyListener(keyListener);
    }

    @Override
    public void removeKeyListener(final KeyListener keyListener)
    {
        detailsView.removeKeyListener(keyListener);
        iconView.removeKeyListener(keyListener);
    }

    @Override
    public Dimension getPreferredSize()
    {
        return currentView==null || !autoArrange ? super.getPreferredSize() : ((Container)currentView).getPreferredSize();
    }

    /*
     * See interface
     */
    public void tableChanged(final TableModelEvent tableModelEvent)
    {
        iconView.modelChanged(tableModelEvent);
    }
}
