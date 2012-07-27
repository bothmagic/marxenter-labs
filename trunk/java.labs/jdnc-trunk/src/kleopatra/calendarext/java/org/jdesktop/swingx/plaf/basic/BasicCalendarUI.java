/*
 * $Id: BasicMonthViewUI.java 3571 2010-01-05 15:07:17Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.event.DateSelectionListener;
import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
import org.jdesktop.swingx.plaf.CalendarUI;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.plaf.basic.PagingAnimator.Direction;

/**
 * Base implementation of the <code>JXCalendar</code> UI. It represents a 
 * zoomable range calender component (Vista-alike). Always shows exactly 
 * one Page (== aka block range like month, year, decade ...) comprised of
 * a header part with next/prev/zoomout buttons and a details part in which
 * the appropriate Cells (aka unit range like day, month, year ..) are layed
 * out in a grid.
 * 
 * <p>
 * The header part is an live component, that is added to the container hierarchy.
 * <p>
 * 
 * The grid part is painted with the help of a renderer, similar to the painting
 * of collection components (like JTable, JTree, ..). This allows per PageCellState
 * configuration and Highlighter support. The UI itself does layout and positioning of
 * the rendering components. Plus updating on property changes received from the 
 * calendarView. <p>
 * 
 * 
 * <p>   
 * Painting: coordinate systems. PENDING JW: this part is outdated.
 * 
 * <ul>
 * <li> Screen coordinates of months/days, accessible via the getXXBounds() methods. These
 * coordinates are absolute in the system of the calendarView. 
 * <li> The grid of visible months with logical row/column coordinates. The logical 
 * coordinates are adjusted to ComponentOrientation. 
 * <li> The grid of days in a month with logical row/column coordinates. The logical 
 * coordinates are adjusted to ComponentOrientation. The columns 
 * are the (optional) week header and the days of the week. The rows are the day header  
 * and the weeks in a month. The day header shows  the localized names of the days and 
 * has the row coordinate DAY_HEADER_ROW. It is shown always.
 * The row header shows the week number in the year and has the column coordinate WEEK_HEADER_COLUMN. It
 * is shown only if the showingWeekNumber property is true.  
 * </ul>
 * 
 * 
 * @author dmouse
 * @author rbair
 * @author rah003
 * @author Jeanette Winzenburg
 */
public class BasicCalendarUI extends CalendarUI {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger.getLogger(BasicCalendarUI.class
            .getName());
    

    // constants for day columns
    protected static final int WEEK_HEADER_COLUMN = 0;
    protected static final int DAYS_IN_WEEK = 7;
    protected static final int FIRST_DAY_COLUMN = WEEK_HEADER_COLUMN + 1;
    protected static final int LAST_DAY_COLUMN = FIRST_DAY_COLUMN + DAYS_IN_WEEK -1;

    // constants for day rows (aka: weeks)
    protected static final int DAY_HEADER_ROW = 0;
    protected static final int WEEKS_IN_MONTH = 6;
    protected static final int FIRST_WEEK_ROW = DAY_HEADER_ROW + 1;
    protected static final int LAST_WEEK_ROW = FIRST_WEEK_ROW + WEEKS_IN_MONTH - 1;



    /** the component we are installed for. */
    protected JXCalendar calendarView;
    // listeners
    private PropertyChangeListener propertyChangeListener;
    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;
    private Handler handler;

    
    //---------- fields related to selection/navigation


    /** flag indicating keyboard navigation. */
    private boolean usingKeyboard = false;

    //------------------ visuals

    protected boolean isLeftToRight;

    
    /** height of month header including the calendarView's box padding. */
    private int fullMonthBoxHeight;
    /** 
     * width of a "day" box including the calendarView's box padding
     * this is the same for days-of-the-week, weeks-of-the-year and days
     */
    private int dayBoxWidth;
    /** 
     * height of a "day" box including the calendarView's box padding
     * this is the same for days-of-the-week, weeks-of-the-year and days
     */
    private int dayBoxHeight;
    
    /**
     * width of the general detail box (i.e. non-day).
     */
    private int detailBoxWidth;
    /**
     * width of the general detail box (i.e. non-day).
     */
    private int detailBoxHeight;
    
    /** the width the page. */
    private int pageWidth;
    /** the height the page. */
    private int pageHeight;

    /**
     * The bounding box of the page. 
     */
    protected Rectangle pageRectangle = new Rectangle();
    

    /**
     * The CellRendererPane for stamping rendering comps.
     */
    private CellRendererPane rendererPane;

    /**
     * The CalendarHeaderHandler which provides the header component if zoomable.
     */
    private CalendarHeader calendarHeader;

    /**
     * Navigator used by this ui. Protected to test - DO NOT USE!
     */
    protected ZoomableNavigator navigator;


    private PagingAnimator pagingAnimator;
    
//---------------------- componentUI, un-/install
    
    @SuppressWarnings({"UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new BasicCalendarUI();
    }

    /**
     * Installs the component as appropriate for the current lf.
     * 
     * PENDING JW: clarify sequence of installXX methods. 
     */
    @Override
    public void installUI(JComponent c) {
        calendarView = (JXCalendar)c;
        calendarView.setLayout(createLayoutManager());
        
        pagingAnimator = new PagingAnimator(this);
        
        // PENDING JW: move to installDefaults or installComponents?
        installRenderer();
        
        installDefaults();
        installDelegate();
        installKeyboardActions();
        installComponents();
        updateLocale(false);
        installListeners();
    }


    @Override
    public void uninstallUI(JComponent c) {
        uninstallRenderer();
        uninstallListeners();
        uninstallKeyboardActions();
        uninstallDefaults();
        uninstallComponents();
        calendarView.setLayout(null);
        calendarView = null;
    }

    /**
     * Creates and installs the calendar header handler. 
     */
    protected void installComponents() {
        setCalendarHeader(createCalendarHeader());
        getCalendarHeader().install(calendarView);
    }

    /**
     * Uninstalls the calendar header handler.
     */
    protected void uninstallComponents() {
        getCalendarHeader().uninstall(calendarView);
        setCalendarHeader(null);
    }


    /**
     * Creates and installs the renderingHandler and infrastructure to use it.
     */
    protected void installRenderer() {
            rendererPane = new CellRendererPane();
            calendarView.add(rendererPane);
    }
    
    /**
     * Uninstalls the renderingHandler and infrastructure that used it.
     */
    protected void uninstallRenderer() {
        calendarView.remove(rendererPane);
        rendererPane = null;
    }


    
    /**
     * Creates and returns a calendar header handler which provides and configures
     * a component for use in a zoomable calendarView. Subclasses may override to return
     * a custom handler.<p>
     * 
     * This implementation first queries the UIManager for class to use and returns 
     * that if available, returns a BasicCalendarHeaderHandler if not.
     * 
     * @return a calendar header handler providing a component for use in zoomable
     *   calendarView.
     * 
     * @see #getHeaderFromUIManager()  
     * @see CalendarHeaderHandler
     * @see BasicCalendarHeaderHandler  
     */
    protected CalendarHeader createCalendarHeader() {
        return new BasicCalendarHeaderX();
    }

    
    /**
     * @param calendarHeader the calendarHeaderHandler to set
     */
    protected void setCalendarHeader(CalendarHeader calendarHeader) {
        this.calendarHeader = calendarHeader;
    }
    
    /**
     * @return the calendarHeaderHandler
     */
    protected CalendarHeader getCalendarHeader() {
        return calendarHeader;
    }

    /**
     * Installs default values. <p>
     * 
     * This is refactored to only install default properties on the calendarView.
     * Extracted install of this delegate's properties into installDelegate. 
     *  
     */
    protected void installDefaults() {
        LookAndFeel.installProperty(calendarView, "opaque", Boolean.TRUE);
        
       // @KEEP JW: do not use the core install methods (might have classloader probs)
        // instead access all properties via the UIManagerExt ..
        //        BasicLookAndFeel.installColorsAndFont(calendarView, 
//                "JXCalendar.background", "JXCalendar.foreground", "JXCalendar.font");
        
        if (SwingXUtilities.isUIInstallable(calendarView.getBackground())) {
            calendarView.setBackground(UIManagerExt.getColor("JXCalendar.background"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getForeground())) {
            calendarView.setForeground(UIManagerExt.getColor("JXCalendar.foreground"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getFont())) {
            // PENDING JW: missing in managerExt? Or not applicable anyway?
            calendarView.setFont(UIManager.getFont("JXCalendar.font"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getMonthStringBackground())) {
            calendarView.setMonthStringBackground(UIManagerExt.getColor("JXCalendar.monthStringBackground"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getMonthStringForeground())) {
            calendarView.setMonthStringForeground(UIManagerExt.getColor("JXCalendar.monthStringForeground"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getSelectionBackground())) {
            calendarView.setSelectionBackground(UIManagerExt.getColor("JXCalendar.selectedBackground"));
        }
        if (SwingXUtilities.isUIInstallable(calendarView.getSelectionForeground())) {
            calendarView.setSelectionForeground(UIManagerExt.getColor("JXCalendar.selectedForeground"));
        }
        calendarView.setBoxPaddingX(UIManagerExt.getInt("JXCalendar.boxPaddingX"));
        calendarView.setBoxPaddingY(UIManagerExt.getInt("JXCalendar.boxPaddingY"));
    }

    /**
     * Installs this ui delegate's properties.
     */
    protected void installDelegate() {
        isLeftToRight = calendarView.getComponentOrientation().isLeftToRight();
        navigator = new ZoomableNavigator();
        updateNavigatorFromSelection(true);
    }
    
    protected void uninstallDefaults() {}

    protected void installKeyboardActions() {
        // Setup the keyboard handler.
        installKeyBindings(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = calendarView.getActionMap();
        
        AbstractActionExt commit = new AbstractActionExt() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                commit();
            }
        };
        
        AbstractActionExt cancel = new AbstractActionExt() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };
        actionMap.put(JXCalendar.COMMIT_KEY, commit);
        actionMap.put(JXCalendar.CANCEL_KEY, cancel);
        
        AbstractActionExt nextCell = new AbstractActionExt() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nextCell();
            }
            
        };
        
        actionMap.put(Navigator.NEXT_CELL_KEY, nextCell);
        
        AbstractActionExt previousCell = new AbstractActionExt() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                previousCell();
            }
        };
        actionMap.put(Navigator.PREVIOUS_CELL_KEY, previousCell);
        
        
        // PENDING JW: complete (year-, decade-, ?? ) and consolidate with KeyboardAction
        // additional navigation actions
        AbstractActionExt prevPage = new AbstractActionExt() {

            public void actionPerformed(ActionEvent e) {
                previousPage();
            }
            
        };
        actionMap.put(Navigator.PREVIOUS_PAGE_KEY, prevPage);
        
        AbstractActionExt nextPage = new AbstractActionExt() {

            public void actionPerformed(ActionEvent e) {
                nextPage();
            }
            
        };
        
        actionMap.put(Navigator.NEXT_PAGE_KEY, nextPage);
        
        AbstractActionExt upperPage = new AbstractActionExt() {
            
            public void actionPerformed(ActionEvent e) {
                upperPage();
            }
            
        };
        actionMap.put(Navigator.UPPER_PAGE_KEY, upperPage);
        
        
        AbstractActionExt lowerPage = new AbstractActionExt() {
            
            public void actionPerformed(ActionEvent e) {
                lowerPage();
            }
            
        };
        actionMap.put(Navigator.LOWER_PAGE_KEY, lowerPage);
        
        
        AbstractActionExt upperCell = new AbstractActionExt() {
            
            public void actionPerformed(ActionEvent e) {
                upperCell();
            }
            
        };
        actionMap.put(Navigator.UPPER_CELL_KEY, upperCell);
        
        
        AbstractActionExt lowerCell = new AbstractActionExt() {
            
            public void actionPerformed(ActionEvent e) {
                lowerCell();
            }
            
        };
        actionMap.put(Navigator.LOWER_CELL_KEY, lowerCell);
        
        AbstractActionExt zoomIn = new AbstractActionExt() {
            
            public void actionPerformed(ActionEvent e) {
                commit();
            }
            
        };
        actionMap.put(Navigator.ZOOM_IN_KEY, zoomIn);
        
        AbstractActionExt zoomOut = new AbstractActionExt() {

            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
            
        };
        actionMap.put(Navigator.ZOOM_OUT_KEY, zoomOut);
        
        
        
    }

    
    /**
     * @param inputMap
     */
    private void installKeyBindings(int type) {
        InputMap inputMap = calendarView.getInputMap(type);
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), JXCalendar.COMMIT_KEY);
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), JXCalendar.CANCEL_KEY);
        
        // @KEEP quickly check #606-swingx: keybindings not working in internalframe
        // eaten somewhere
//        inputMap.put(KeyStroke.getKeyStroke("F1"), "selectPreviousDay");

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), Navigator.PREVIOUS_CELL_KEY);
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), Navigator.NEXT_CELL_KEY);
        inputMap.put(KeyStroke.getKeyStroke("control LEFT"), Navigator.PREVIOUS_PAGE_KEY);
        inputMap.put(KeyStroke.getKeyStroke("control RIGHT"), Navigator.NEXT_PAGE_KEY);

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), Navigator.LOWER_CELL_KEY);
        inputMap.put(KeyStroke.getKeyStroke("UP"), Navigator.UPPER_CELL_KEY);
        inputMap.put(KeyStroke.getKeyStroke("PAGE_DOWN"), Navigator.LOWER_PAGE_KEY);
        inputMap.put(KeyStroke.getKeyStroke("PAGE_UP"), Navigator.UPPER_PAGE_KEY);

        inputMap.put(KeyStroke.getKeyStroke("control UP"), Navigator.ZOOM_OUT_KEY);
        inputMap.put(KeyStroke.getKeyStroke("control DOWN"), Navigator.ZOOM_IN_KEY);
        
    }

    /**
     * @param inputMap
     */
    private void uninstallKeyBindings(int type) {
        InputMap inputMap = calendarView.getInputMap(type);
        inputMap.clear();
    }

    protected void uninstallKeyboardActions() {}

    protected void installListeners() {
        propertyChangeListener = createPropertyChangeListener();
        mouseListener = createMouseListener();
        mouseMotionListener = createMouseMotionListener();
        
        calendarView.addPropertyChangeListener(propertyChangeListener);
        calendarView.addMouseListener(mouseListener);
        calendarView.addMouseMotionListener(mouseMotionListener);

        calendarView.getSelectionModel().addDateSelectionListener(getHandler());
    }

    protected void uninstallListeners() {
        calendarView.getSelectionModel().removeDateSelectionListener(getHandler());
        calendarView.removeMouseMotionListener(mouseMotionListener);
        calendarView.removeMouseListener(mouseListener);
        calendarView.removePropertyChangeListener(propertyChangeListener);

        mouseMotionListener = null;
        mouseListener = null;
        propertyChangeListener = null;
    }

//-------------------------- callbacks from change notification
    
    /**
     * @param evt
     */
    private void updateFromSelectionModelChanged(DateSelectionModel old) {
        if (old != null) {
            old.removeDateSelectionListener(getHandler());
        }
        calendarView.getSelectionModel().addDateSelectionListener(getHandler());
        updateNavigatorFromSelection(true);
    }

    /**
     * Binds/clears the keystrokes in the component input map, 
     * based on the calendarView's componentInputMap enabled property.
     * 
     * @see org.jdesktop.swingx.JXCalendar#isComponentInputMapEnabled()
     */
    protected void updateComponentInputMap() {
        if (calendarView.isComponentInputMapEnabled()) {
            installKeyBindings(JComponent.WHEN_IN_FOCUSED_WINDOW);
        } else {
            uninstallKeyBindings(JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }



    /**
     * Updates internal state according to calendarView's locale. Revalidates the
     * calendarView if the boolean parameter is true.
     * 
     * @param revalidate a boolean indicating whether the calendarView should be 
     * revalidated after the change.
     */
    protected void updateLocale(boolean revalidate) {
        if (revalidate) {
            calendarView.invalidate();
            calendarView.validate();
        }
    }

//---------------------- listener creation    
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected LayoutManager createLayoutManager() {
        return getHandler();
    }

    protected MouseListener createMouseListener() {
        return getHandler();
    }

    protected MouseMotionListener createMouseMotionListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }

        return handler;
    }

    public boolean isUsingKeyboard() {
        return usingKeyboard;
    }

    public void setUsingKeyboard(boolean val) {
        usingKeyboard = val;
    }

//----------------- implement CalendarUI
    

    /**
     * Returns the cell date at the given location. May be null if the
     * coordinates don't map to a cell in the current page.
     * <p> 
     * Mapping pixel to date.<p>
     *
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the Date at the given location or null if the location
     *   doesn't map to a cell in the current page. 
     *   
     */ 
    @Override
    public Calendar getCellAtLocation(int x, int y) {
        Point dayInGrid = getCellGridPositionAtLocation(x, y);
        if (dayInGrid == null) return null;
        return getCell(dayInGrid.y, dayInGrid.x);
    }
    
    /**
     * {@inheritDoc} <p>
     */
    @Override
    public Calendar getCell(int row, int column) {
        checkValidCoordinates(row, column);
        Calendar calendar = getFirstCellOnPage(); //getPage();
        if (isMonthPage()) {
            if (row != DAY_HEADER_ROW) {
                row = row - FIRST_WEEK_ROW;
            } 
            if (column != WEEK_HEADER_COLUMN) {
                column = column - FIRST_DAY_COLUMN;
            } 
        }
        int cellValue = row * navigator.getVerticalCellUnit() + column;
        calendar.add(getCellType().getCalendarField(), cellValue);
        return calendar;
    }
    
    /**
     * Checks whether the given coordinates are valid on the current page, throws
     * IllegalArgumentException if not.
     * 
     * @param row the logical row index in grid of date cells, must be
     *   valid in the current page type
     * @param column the logical column index in the grid of date cells, must
     *    be valid in the current page type
     */
    private void checkValidCoordinates(int row, int column) {
        if (row < 0 || row >= getRowCount() ||
                column < 0 || column >= getColumnCount()) 
          throw new IllegalArgumentException(
                    "invalid coordinates, expected positive and less than (row/column): " 
                    + (getRowCount() - 1) + " / " + (getColumnCount() - 1)
                    + " but was: " + row + " / " + column);
    }

    /**
     * Returns the calendarView's calendar configured to the start of the current page.
     * 
     * @return the calendarView's calendar configured to the current page.
     */
    @Override
    public Calendar getPage() {
        return navigator.getPage();
    }
    
    
    /** 
     * @inherited <p>
     */
    @Override
    public Calendar getLead() {
        return navigator.getLead();
    }
    
    /**
     * {@inheritDoc} <p>
     */
    @Override
    public FieldType getPageType() {
        return navigator.getPageType();
    }

    
    /** 
     * @inherited <p>
     */
    @Override
    public int getColumnCount() {
        int count = navigator.getVerticalCellUnit();
        return isMonthPage() ? count + 1 : count;
    }

    
    /** 
     * @inherited <p>
     */
    @Override
    public int getRowCount() {
        if (isMonthPage()) {
            return WEEKS_IN_MONTH + 1;
        }
        int count = navigator.getVerticalPageUnit() / navigator.getVerticalCellUnit();
        return count;
    }

    
    /** 
     * @inherited <p>
     */
    @Override
    public CalendarCellState getCellState(int row, int column) {
        if (isMonthPage()) {
            if (WEEK_HEADER_COLUMN == column) {
                return CalendarCellState.WEEK_OF_YEAR_TITLE;
            }
            if (DAY_HEADER_ROW == row) {
                return CalendarCellState.DAY_OF_WEEK_TITLE;
            }
        }
        Date date = getCellDate(row, column);
        Calendar page = getPage();
        boolean onPage = getPageType().isContainedInPage(page, date);
        switch (getPageType()) {
        case MONTH:
            if (!onPage) {
                return CalendarCellState.DAY_OFF_CELL;
            }
            return calendarView.isToday(date) ? CalendarCellState.TODAY_CELL : CalendarCellState.DAY_CELL;
        case YEAR:
            return CalendarCellState.MONTH_CELL;
        case DECADE:
            return onPage ? CalendarCellState.YEAR_CELL : CalendarCellState.YEAR_OFF_CELL;
        }
        throw new IllegalStateException("unhandled page type");
    }


    /**
     * Returns the monthViews calendar configured to the given time.
     * 
     * NOTE: it's safe to change the calendar state without resetting because
     * it's JXCalendar's responsibility to protect itself.
     * 
     * @param date the date to configure the calendar with
     * @return the calendarView's calendar, configured with the given date.
     */
    protected Calendar getCalendar(Date date) {
        Calendar calendar = getPage();
        calendar.setTime(date);
        return calendar;
    }

//--------------------- convenience delegates to navigator
    
    /**
     * @return
     */
    private FieldType getCellType() {
        return navigator.getCellType();
    }

    
    private Calendar getFirstCellOnPage() {
        return navigator.getFirstCell();
    }
    
    
    
//------------------------ mapping details coordinates

    /**
     * Returns the logical coordinates of the details box which contains the given
     * location. The p.x of the returned value represents the column in the 
     * grid of details, the p.y represents the row . Both are zero-based.
     *  The transformation takes care of
     * ComponentOrientation.
     * <p>
     * 
     * Mapping pixel to logical grid coordinates.
     * 
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the logical coordinates of the cell in the grid of cells 
     *   
     *         
     * @see #getCellBounds(int, int)     
     */
    protected Point getCellGridPositionAtLocation(int x, int y) {
        Rectangle detailsBounds = getPageDetailsBounds();
        if (!detailsBounds.contains(x, y)) return null;
        int calendarRow = (y - detailsBounds.y) / getCellHeight(); 
        int calendarColumn = (x - detailsBounds.x) / getCellWidth();
        if (!isLeftToRight) {
            int leading = detailsBounds.x + detailsBounds.width;
            calendarColumn = (leading - x) / getCellWidth();
        }
        
        if (isMonthPage()) {
           calendarRow += DAY_HEADER_ROW;
           calendarColumn += FIRST_DAY_COLUMN;
           if (calendarView.isShowingWeekNumber()) {
               calendarColumn--;
           }
        }
        return new Point(calendarColumn, calendarRow);
    }

    /**
     * Returns the bounds of the detail box at the given logical coordinates
     * in the grid of details.<p>
     * 
     * Mapping logical grid position to pixel.
     * 
     * @param row the rowIndex in the grid of details.
     * @param column the columnIndex in the grid details.
     * @return the bounds of the details at the given logical logical position.
     * 
     * @see #getCellGridPositionAtLocation(int, int)
     */
    protected Rectangle getCellBounds(int row, int column) {
        Rectangle detailsBounds = getPageDetailsBounds();
        if (isMonthPage()) {
            if ((WEEK_HEADER_COLUMN == column) && !calendarView.isShowingWeekNumber()) return null;
            row = row - DAY_HEADER_ROW;
            column = column - FIRST_DAY_COLUMN;
            if (calendarView.isShowingWeekNumber()) {
                column++;
            }
        }
        detailsBounds.y +=  row * getCellHeight();
        if (isLeftToRight) {
           detailsBounds.x += column * getCellWidth(); 
        } else {
            int leading = detailsBounds.x + detailsBounds.width - getCellWidth(); 
            detailsBounds.x = leading - column * getCellWidth();
        }
        detailsBounds.width = getCellWidth();
        detailsBounds.height = getCellHeight();
        return detailsBounds;
    }

    
    /**
     * Returns the bounds of the details cell which contains the
     * given location. The bounds are in calendar's screen coordinate system.
     * <p>
     * 
     * Note: this is a pure geometric mapping. The returned rectangle need not
     * necessarily map to a date in the month which contains the location, it
     * can represent a week-number/column header or a leading/trailing date.
     * 
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the bounds of the cell which contains the location, or null if
     *         outside
     */
    protected Rectangle getCellBoundsAtLocation(int x, int y) {
        Rectangle detailsBounds = getPageDetailsBounds();
        if (!detailsBounds.contains(x, y)) return null;
        // calculate row/column in absolute grid coordinates
        int row = (y - detailsBounds.y) / getCellHeight();
        int column = (x - detailsBounds.x) / getCellWidth();
        return new Rectangle(detailsBounds.x + column * getCellWidth(), detailsBounds.y
                + row * getCellHeight(), getCellWidth(), getCellHeight());
    }

    /**
     * Returns the Date defined by the logical 
     * grid coordinates, convenience method.
     * 
     */
    private Date getCellDate(int row, int column) {
         return getCell(row, column).getTime();
    }


    // ----------------------- mapping day coordinates
    
/*-------------------- PENDING JW: 
 * back-conversion from Date to locigal is still missing in moving to generalized cell detail
 * used internally only (and circular) - so skipping for now. 
 * Nevertheless, for symetry, there should be a public getBoundsOfDate(Date) as the reverse of
 * getDateAtLocation(x, y)
 */    
    
    // ------------------- mapping page parts 
 
    /**
     * Returns the bounds of the page. 
     * 
     * @return the bounds of the page
     */
    protected Rectangle getPageBounds() {
       return new Rectangle(pageRectangle); 
    }
    

    
    /**
     * Returns the bounds of the page header. 
     * The bounds are in calendar's coordinate system.
     * 
     * @return the bounds of the page header in the calendar's coordinate system.
     */
    protected Rectangle getPageHeaderBounds() {
        Rectangle header = getPageBounds();
        header.height = getPageHeaderHeight();
        return header;
    }
    
    /**
     * Returns the bounds of the page details in the calendar's coordinate system.
     * 
     * @return the bounds of the page details in the calendar's coordinate system.
     */
    protected Rectangle getPageDetailsBounds() {
        Rectangle page = getPageBounds();
        page.y = page.y + getPageHeaderHeight();
        page.height = page.height - getPageHeaderHeight();
        return page;
    }

    
    //---------------- accessors for sizes
    
    /**
     * Returns the size of a month.
     * @return the size of a month.
     */
    protected Dimension getPageSize() {
        return new Dimension(pageWidth, pageHeight);
    }
    
    /**
     * Returns the height of the month header.
     * 
     * @return the height of the month header.
     */
    protected int getPageHeaderHeight() {
        return fullMonthBoxHeight;
    }

    /**
     * Returns the size of a detail box for a month page.
     * 
     * @return the size of a month.
     */
    protected Dimension getDaySize() {
        return new Dimension(dayBoxWidth, dayBoxHeight);
    }
    
    /**
     * Returns the size of the general (not of type day) details box
     * @return the size of a general details box.
     */
    protected Dimension getDetailsSize() {
        return new Dimension(detailBoxWidth, detailBoxHeight);
    }


    /**
     * Returns the size of the cell box as appropriate for the current page type.
     * 
     * @return the size of a cell as appropriate for the current page type.
     */
    protected Dimension getCellSize() {
        return new Dimension(getCellWidth(), getCellHeight());
    }
    
    /**
     * Returns the width of the cell box as appropriate for the current page type.
     * 
     * @return
     */
    private int getCellWidth() {
        return isMonthPage() ? dayBoxWidth : detailBoxWidth;
    }

    /**
     * Returns the width of the cell box as appropriate for the current page type.
     *  
     * @return
     */
    private int getCellHeight() {
        return isMonthPage() ? dayBoxHeight : detailBoxHeight;
    }

//--------------------- convenience methods
    
    /**
     * @return
     */
    private boolean isMonthPage() {
        return (getPageType() == FieldType.MONTH);
    }

    /**
     * @return
     */
    private boolean isDecadePage() {
        return (getPageType() == FieldType.DECADE);
    }

    /**
     * Returns the coordinates of the first content cell in the grid.
     * 
     * @return
     */
    private Point getFirstCell() {
        if (isMonthPage()) {
            return new Point(FIRST_DAY_COLUMN, FIRST_WEEK_ROW);
        }
        return new Point(0, 0);
    }
    
    /**
     * Returns the coordinates of the last content cell in the grid.
     * 
     * @return
     */
    private Point getLastCell() {
        if (isMonthPage()) {
            return new Point(LAST_DAY_COLUMN, LAST_WEEK_ROW);
        }
        return new Point(getColumnCount() -1, getRowCount() - 1);
    }
    

    // ------------------- painting

    /**
     * Overridden to extract the background painting for ease-of-use of 
     * subclasses.
     */
    @Override
    public void update(Graphics g, JComponent c) {
        paintBackground(g);
        paint(g, c);
    }

    /**
     * Paints the background of the component. This implementation fill the
     * calendarView's area with its background color if opaque, does nothing
     * if not opaque. Subclasses can override but must comply to opaqueness
     * contract.
     * 
     * @param g the Graphics to fill.
     * 
     */
    protected void paintBackground(Graphics g) {
        if (calendarView.isOpaque()) {
            g.setColor(calendarView.getBackground());
            g.fillRect(0, 0, calendarView.getWidth(), calendarView.getHeight());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        // <snip> Scrolling Animation
        // Delegate painting to animator
        // </snip>    
        if (pagingAnimator.isRunning()) {
            pagingAnimator.paintIcon(g);
        } else {
            paintDetails(g);

        }

        // Rectangle clip = g.getClipBounds();
        // Rectangle bounds = getPageBounds();
        // g.setColor(Color.MAGENTA);
        // g.drawRect(clip.x, clip.y, clip.width -1, clip.height-1);
        // g.setColor(Color.GREEN);
        // g.drawRect(bounds.x, bounds.y, bounds.width-1, bounds.height -1);
        //        
    }

    /**
     * @param g
     */
    protected void paintDetails(Graphics g) {
        paintColumnHeader(g);
        paintRowHeader(g);
        paintCells(g);
    }

    /**
     * Paints the "content" (aka: not headers) cells of the current page.
     * 
     * @param g the graphics to paint into
     */
    protected void paintCells(Graphics g) {
        Calendar calendar = getFirstCellOnPage();
        Point firstCell = getFirstCell();
        Point lastCell = getLastCell();
        for (int row = firstCell.y; row <= lastCell.y; row++) {
            for (int column = firstCell.x; column <= lastCell.x; column++) {
                paintCell(g, calendar, row, column);
                navigator.getCellType().add(calendar, 1);
            }
        }
    }
    /**
     * Paints the day column header, if appropriate. This implementation
     * does nothing if the page type doesn't represent a month.
     * 
     * @param g the graphics to paint into
     */
    protected void paintColumnHeader(Graphics g) {
        if (!isMonthPage()) return;
        paintColumnHeaderSeparator(g);
        Calendar cal = getFirstCellOnPage();
        for (int i = FIRST_DAY_COLUMN; i <= LAST_DAY_COLUMN; i++) {
            paintCell(g, cal, 0, i);
            cal.add(Calendar.DATE, 1);
        }
    }

    /**
     * Paints the day column header, if needed. This implementation does nothing
     * if the page type does not represent a month or the calendar isn't
     * showing week numbers.
     * 
     * @param g the graphics to paint into
     */
    protected void paintRowHeader(Graphics g) {
        if (!isMonthPage() || !calendarView.isShowingWeekNumber())
            return;
        paintRowHeaderSeparator(g);
    
        int weeks = getWeeks();
        // the calendar passed to the renderers
        Calendar weekCalendar = getPage();
        // we loop by logical row (== week in month) coordinates 
        for (int week = FIRST_WEEK_ROW; week < FIRST_WEEK_ROW + weeks; week++) {
            paintCell(g, weekCalendar, week, 0);
            weekCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
    }


    /**
     * Paints a cell in the details grid of the current page.<p>
     * 
     * Note: the calendar must not be changed!
     * 
     * @param g the graphics to paint into
     * @param calendar the calendar specifying the cell date, must not be null
     * @param row the logical row index of the cell
     * @param column the logical column index of the cell
     */
    protected void paintCell(Graphics g, Calendar calendar, int row, int column) {
        JComponent comp = calendarView.prepareRendererComponent(calendar, 
                row, column);
        Rectangle bounds = getCellBounds(row, column);
        rendererPane.paintComponent(g, comp, calendarView, bounds.x, bounds.y,
                bounds.width, bounds.height, true);
    }

    /**
     * Paints the separator between row header (weeks of year) and days.
     * 
     * Note: the given calendar must not be changed.
     * @param g the graphics to paint into
     * @param month the calendar specifying the first day of the month to
     *        paint, must not be null
     */
    protected void paintRowHeaderSeparator(Graphics g) {
        Rectangle r = getSeparatorBounds(FIRST_WEEK_ROW, WEEK_HEADER_COLUMN);
        if (r == null) return;
        g.setColor(calendarView.getForeground());
        g.drawLine(r.x, r.y, r.x, r.y + r.height);
    }

    /**
     * Paints the separator between column header (days of week) and days.
     * 
     * Note: the given calendar must not be changed.
     * @param g the graphics to paint into
     * @param month the calendar specifying the the first day of the month to
     *        paint, must not be null
     */
    protected void paintColumnHeaderSeparator(Graphics g) {
        Rectangle r = getSeparatorBounds(DAY_HEADER_ROW, FIRST_DAY_COLUMN);
        if (r == null) return;
        g.setColor(calendarView.getForeground());
        g.drawLine(r.x, r.y, r.x + r.width, r.y);
    }
    
    /**
     * @param month
     * @param row
     * @param column
     * @return
     */
    private Rectangle getSeparatorBounds(int row, int column) {
        Rectangle separator = getCellBounds(row, column);
        if (separator == null) return null;
        if (column == WEEK_HEADER_COLUMN) {
            separator.height *= WEEKS_IN_MONTH;
            if (isLeftToRight) {
                separator.x += separator.width - 1;
            }
            separator.width = 1;
        } else if (row == DAY_HEADER_ROW) {
            int oldWidth = separator.width;
            separator.width *= DAYS_IN_WEEK;
            if (!isLeftToRight) {
                separator.x -= separator.width - oldWidth;
            }
            separator.y += separator.height - 1;
            separator.height = 1;
        }
        return separator;
    }

    /**
     * Returns the number of weeks to paint in the current page which is
     * expected to be of type MONTH.
     * 
     * @return the number of weeks of this month.
     */
    protected int getWeeks() {
        Calendar page = getPage();
        Calendar calendar = getFirstCellOnPage();
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        int count = 1;
        while (getPageType().isContainedInPage(page, calendar.getTime())) {
            count++;
            calendar.add(Calendar.WEEK_OF_MONTH, 1);
        }
        return count;
    }

    

    /**
     * @param g
     */
    @SuppressWarnings("unused")
    private void debugPaintGrid(Graphics g) {
        Rectangle bounds = getPageBounds(); 
        if (bounds == null) return;
        g.setColor(Color.RED);
        for (int i = 0; i <= 3; i++) {
            g.drawLine(bounds.x, 
                    bounds.y + getPageHeaderHeight() + i * getCellHeight(), 
                    bounds.x + bounds.width, 
                    bounds.y + getPageHeaderHeight() + i * getCellHeight());
        }
        for (int i = 0; i <=4; i++) {
            g.drawLine(bounds.x + i * getCellWidth(), 
                    bounds.y + getPageHeaderHeight(), 
                    bounds.x + i * getCellWidth(), 
                    bounds.y + bounds.height);
            
        }
    }



    /**
     * NOTE: this is for testing only - DO NOT USE!
     * @param id
     * @return
     */
    protected boolean performAction(Object id) {
        Action action = calendarView.getActionMap().get(id);
        action.actionPerformed(null);
        return true;
    }
    
//-------------------------- action methods
    

    // PENDING JW: this isss whacky ... must be calculated
    // move to navigator: that's where the details of the grid are
    // controlled.
    private int getCellValue(int row, int column) {
        if (isMonthPage()) {
            row -= FIRST_WEEK_ROW;
        }
        int cellValue = row * navigator.getVerticalCellUnit() + column;
        if (isDecadePage()) {
            cellValue--;
        } else if (isMonthPage()){
            Calendar page = getPage();
            Calendar date = getFirstCellOnPage();
            while(date.before(page)) {
                cellValue--;
                date.add(Calendar.DATE, 1);
            }
            
        }
        return cellValue;
    }
    
    private void setLeadValue(int row, int column) {
        int cellValue = getCellValue(row, column);
        Direction direction = null;
//        if (isMonthPage()) {
//            if (cellValue < navigator.getMinimumLeadValue()) {
//                direction = Direction.BACKWARD;
//            } else if (cellValue > navigator.getMaximumLeadValue()) {
//                direction = Direction.FORWARD;
//            }
//            if (direction != null) {
//                pagingAnimator.beforeMove(direction);
//            } else {
////                pagingAnimator.stop();
//            }
//        }
        navigator.setLeadValue(cellValue);
        updateSelectionFromNavigator(direction);
    }
    
    private void nextPage() {
        // <snip> Scrolling Animation
        // Prepare the animation
        pagingAnimator.beforeMove(Direction.FORWARD);
        // </snip>    
        navigator.nextPage();
        updateSelectionFromNavigator(Direction.FORWARD);
    }

    private void previousPage() {
        pagingAnimator.beforeMove(Direction.BACKWARD);
        navigator.previousPage();
        updateSelectionFromNavigator(Direction.BACKWARD);
    }
    
    private void upperPage() {
        pagingAnimator.beforeMove(Direction.BACKWARD);
        navigator.upperPage();
        updateSelectionFromNavigator(Direction.BACKWARD);
    }
    
    private void lowerPage() {
        pagingAnimator.beforeMove(Direction.FORWARD);
        navigator.lowerPage();
        updateSelectionFromNavigator(Direction.FORWARD);
    }

    /**
     * 
     */
    private void previousCell() {
        pagingAnimator.beforeMove(Direction.BACKWARD);
        Calendar page = getPage();
        navigator.previousCell();
        updateSelectionFromNavigator(getPageType().isContainedInPage(page, getPage().getTime())
                ? null : Direction.BACKWARD);
    }

    /**
     * 
     */
    private void nextCell() {
        pagingAnimator.beforeMove(Direction.FORWARD);
        Calendar page = getPage();
        navigator.nextCell();
        updateSelectionFromNavigator(getPageType().isContainedInPage(page, getPage().getTime())
                ? null : Direction.FORWARD);
    }
    
    /**
     * 
     */
    private void upperCell() {
        pagingAnimator.beforeMove(Direction.BACKWARD);
        Calendar page = getPage();
        navigator.upperCell();
        updateSelectionFromNavigator(getPageType().isContainedInPage(page, getPage().getTime())
                ? null : Direction.BACKWARD);
    }
    
    /**
     * 
     */
    private void lowerCell() {
        pagingAnimator.beforeMove(Direction.FORWARD);
        Calendar page = getPage();
        navigator.lowerCell();
        updateSelectionFromNavigator(getPageType().isContainedInPage(page, getPage().getTime())
                ? null : Direction.FORWARD);
    }


    private void commit() {
        if (navigator.isZoomed()) {
            navigator.zoomIn();
            updateZoomOutEnabled();
            calendarView.ensurePageVisible();
            calendarView.repaint();
        } else {
            if (canCommitLead()) {
                navigator.commitLead();
                calendarView.commitSelection();
            } else {
                commitError();
            }
        }
    }
    
    /**
     * Checks and updates the endable property of the zoomout action. 
     */
    private void updateZoomOutEnabled() {
        calendarView.getActionMap().get(Navigator.ZOOM_OUT_KEY).setEnabled(!isDecadePage());
    }

    private boolean canCommitLead() {
        return !calendarView.isUnselectableDate(getLead().getTime());
    }

    protected void commitError() {
        
        // TODO Auto-generated method stub
        
    }

    private void zoomOut() {
        navigator.zoomOut();
        updateZoomOutEnabled();
        calendarView.ensurePageVisible();
//        LOG.info("paging animator running?: " + pagingAnimator.isRunning());
        calendarView.repaint();
    }
    
    private void cancel() {
       navigator.cancelLead();
       updateSelectionFromNavigator(null);
       calendarView.setSelectionDate(navigator.getLeadDate());
       calendarView.cancelSelection();
    }
    
    /**
     * @param direction TODO
     * 
     */
    private void updateSelectionFromNavigator(Direction direction) {
        calendarView.getSelectionModel().setAdjusting(true);
        calendarView.setSelectionDate(navigator.getLeadDate());
        // <snip> Scrolling Animation
        // Complete and start animation if appropriate
        if (direction != null) {
            pagingAnimator.afterMove(direction);
            // </snip>    
        }
        calendarView.ensurePageVisible();
        // PENDING JW: any way to get rid of this manual repaint?
        // needed only if navigating unselectable dates
        calendarView.repaint();
    }
    
    private void updateNavigatorFromSelection(boolean calendarChanged) {
        if (calendarChanged) {
            navigator.setCalendar(calendarView.getSelectionModel().getCalendar());
        }
        navigator.setLeadDate(calendarView.isSelectionEmpty() ?
                getDefaultLeadDate() :
                calendarView.getSelectionDate());
        navigator.commitLead();
        calendarView.ensurePageVisible();
    }

    protected Date getDefaultLeadDate() {
        return calendarView.getToday();
    }
    
 
    
//------------------ Handler implementation 
//  

    private class Handler implements  
        MouseListener, MouseMotionListener, LayoutManager,
            PropertyChangeListener, DateSelectionListener {
        private boolean armed;

        public void mouseClicked(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            if (!getPageBounds().contains(e.getPoint())) return;
            
            // If we were using the keyboard we aren't anymore.
            setUsingKeyboard(false);

            if (!calendarView.isEnabled()) {
                return;
            }

            if (!calendarView.hasFocus() && calendarView.isFocusable()) {
                calendarView.requestFocusInWindow();
            }

//            updateLead(e);
            
            // Arm so we fire action performed on mouse release.
            armed = updateLead(e);
        }

        
        public void mouseReleased(MouseEvent e) {
            if (!getPageBounds().contains(e.getPoint())) return;
            // If we were using the keyboard we aren't anymore.
            setUsingKeyboard(false);

            if (!calendarView.isEnabled()) {
                return;
            }

            if (!calendarView.hasFocus() && calendarView.isFocusable()) {
                calendarView.requestFocusInWindow();
            }
            
            if (armed) {
                // PENDING JW: keep armed if commit not successful?
                commit();
            }
            armed = false;
        }

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseDragged(MouseEvent e) {
            if (!getPageBounds().contains(e.getPoint())) return;
            // If we were using the keyboard we aren't anymore.
            setUsingKeyboard(false);
            if (!calendarView.isEnabled()) {
                return;
            }
            // Set trigger.
            armed = updateLead(e);
        }

        /**
         * @param e
         */
        private boolean updateLead(MouseEvent e) {
            Point p = getCellGridPositionAtLocation(e.getX(), e.getY());
            if ((p == null) || !getCellState(p.y, p.x).isContent()) return false;
            setLeadValue(p.y, p.x);
            return true;
        }
        public void mouseMoved(MouseEvent e) {}

//------------------------ layout
        
        
        private Dimension preferredSize = new Dimension();

        public void addLayoutComponent(String name, Component comp) {}

        public void removeLayoutComponent(Component comp) {}

        public Dimension preferredLayoutSize(Container parent) {
            layoutContainer(parent);
            return new Dimension(preferredSize);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public void layoutContainer(Container parent) {

            int maxMonthWidth = 0;
            int maxMonthHeight = 0;
            Calendar calendar = getCalendar();
            for (int i = calendar.getMinimum(Calendar.MONTH); i <= calendar.getMaximum(Calendar.MONTH); i++) {
                calendar.set(Calendar.MONTH, i);
                CalendarUtils.startOfMonth(calendar);
                JComponent comp = calendarView.prepareRendererComponent(calendar, CalendarCellState.PAGE_TITLE);
                Dimension pref = comp.getPreferredSize();
                maxMonthWidth = Math.max(maxMonthWidth, pref.width);
                maxMonthHeight = Math.max(maxMonthHeight, pref.height);
            }
            
            int maxBoxWidth = 0;
            int maxBoxHeight = 0;
            calendar = getCalendar();
            CalendarUtils.startOfWeek(calendar);
            for (int i = 0; i < JXCalendar.DAYS_IN_WEEK; i++) {
                JComponent comp = calendarView.prepareRendererComponent(calendar, CalendarCellState.DAY_OF_WEEK_TITLE);
                Dimension pref = comp.getPreferredSize();
                maxBoxWidth = Math.max(maxBoxWidth, pref.width);
                maxBoxHeight = Math.max(maxBoxHeight, pref.height);
                calendar.add(Calendar.DATE, 1);
            }
            
            calendar = getCalendar();
            for (int i = 0; i < calendar.getMaximum(Calendar.DAY_OF_MONTH); i++) {
                JComponent comp = calendarView.prepareRendererComponent(calendar, CalendarCellState.DAY_CELL);
                Dimension pref = comp.getPreferredSize();
                maxBoxWidth = Math.max(maxBoxWidth, pref.width);
                maxBoxHeight = Math.max(maxBoxHeight, pref.height);
                calendar.add(Calendar.DATE, 1);
            }
            
            int dayColumns = JXCalendar.DAYS_IN_WEEK;
            if (calendarView.isShowingWeekNumber()) {
                dayColumns++;
            }
            
            if (maxMonthWidth > maxBoxWidth * dayColumns) {
                //  monthHeader pref > sum of box widths
                // handle here: increase day box width accordingly
                double diff = maxMonthWidth - (maxBoxWidth * dayColumns);
                maxBoxWidth += Math.ceil(diff/(double) dayColumns);
                
            }
            
            dayBoxWidth = maxBoxWidth;
            dayBoxHeight = maxBoxHeight;
            // PENDING JW: huuh? what we doing here?
            int boxHeight = maxBoxHeight - 2 * calendarView.getBoxPaddingY();
            fullMonthBoxHeight = Math.max(boxHeight, maxMonthHeight) ; 

            // Keep track of calendar width and height for use later.
            pageWidth = dayBoxWidth * JXCalendar.DAYS_IN_WEEK;
            if (calendarView.isShowingWeekNumber()) {
                pageWidth += dayBoxWidth;
            }
            
            pageHeight = (dayBoxHeight * 7) + fullMonthBoxHeight;
            
            // Calculate size of general detail box 
            detailBoxWidth = pageWidth / 4;
            detailBoxHeight = (dayBoxHeight * 7) / 3;
            
            

            preferredSize.height = pageHeight;
            preferredSize.width = pageWidth;
            // Add insets to the dimensions.
            Insets insets = calendarView.getInsets();
            preferredSize.width += insets.left + insets.right;
            preferredSize.height += insets.top + insets.bottom;
           
            int leading = calendarView.getWidth() > pageWidth ? 
                    (calendarView.getWidth() - pageWidth) / 2 : 0;
            int top = calendarView.getHeight() > pageHeight ? 
                    (calendarView.getHeight() - pageHeight) / 2 : 0;
            pageRectangle.setBounds(
//                    (calendarView.getWidth() - pageWidth) / 2,
//                    (calendarView.getHeight() - pageHeight) / 2,
                    leading,
                    top,
                    pageWidth, 
                    pageHeight);
            
            getCalendarHeader().getHeaderComponent().setBounds(
                    getPageHeaderBounds());
         }
        
        /**
         * Returns the view's calendar adjusted to the first day of month. This 
         * is used for measuring only.
         * @return
         */
        private Calendar getCalendar() {
            Calendar calendar = calendarView.getPage();
            calendar.set(Calendar.DATE, 1);
            return calendar;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String property = evt.getPropertyName();

            if ("componentOrientation".equals(property)) {
                isLeftToRight = calendarView.getComponentOrientation().isLeftToRight();
                calendarView.revalidate();
                calendarView.repaint();
            } else if (JXCalendar.SELECTION_MODEL.equals(property)) {
                updateFromSelectionModelChanged((DateSelectionModel) evt.getNewValue());
            } else if ("firstDisplayedDay".equals(property)) {
                calendarView.repaint();
            } else if (JXCalendar.BOX_PADDING_X.equals(property) 
                    || JXCalendar.BOX_PADDING_Y.equals(property) 
                    || JXCalendar.DAYS_OF_THE_WEEK.equals(property) 
                    || "border".equals(property) 
                    || "showingWeekNumber".equals(property)
                   
                    ) {
                calendarView.revalidate();
                calendarView.repaint();
            } else if ("componentInputMapEnabled".equals(property)) {
                updateComponentInputMap();
            } else if ("locale".equals(property)) { // "locale" is bound property
                updateLocale(true);
            } else {
                calendarView.repaint();
//                LOG.info("got propertyChange:" + property);
            }
        }

        public void valueChanged(DateSelectionEvent ev) {
            if (EventType.CALENDAR_CHANGED == ev.getEventType()) {
                // here we ask the model directly - it might happen that we get
                // notified before the calendarView has updated itself, f.i.
                // after changing the ui delegate
                updateNavigatorFromSelection(true);
            } else if (!calendarView.getSelectionModel().isAdjusting()) {
                updateNavigatorFromSelection(false);
            }
            calendarView.repaint();
        }


    }


}
