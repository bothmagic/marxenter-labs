/*
 * Created on 04.02.2010
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.calendar.FieldType;

/**
 * Controller to navigate on a Calendar page in a grid of details. Navigation
 * supports both "horizontal" and "vertical" unit/block increment/decrement  
 * as appropriate to CellType/PageType pair which defines the grid.<p>
 * 
 */
public interface Navigator {

    public static final String NEXT_CELL_KEY = "Navigator.nextCell";

    public static final String PREVIOUS_CELL_KEY = "Navigator.previousCell";

    public static final String NEXT_PAGE_KEY = "Navigator.nextPage";

    public static final String PREVIOUS_PAGE_KEY = "Navigator.previousPage";

    public static final String UPPER_CELL_KEY = "Navigator.upperCell";

    public static final String LOWER_CELL_KEY = "Navigator.lowerCell";

    public static final String UPPER_PAGE_KEY = "Navigator.upperPage";

    public static final String LOWER_PAGE_KEY = "Navigator.lowerPage";
    public static final String ZOOM_OUT_KEY = "Navigator.zoomOut";
    public static final String ZOOM_IN_KEY = "Navigator.zoomIn";
    
    
    /**
     * Sets the calendar to navigate upon. <p>
     * 
     * @param calendar the calendar to navigate upon.
     */
    void setCalendar(Calendar calendar);
    
    /**
     * Returns a Calendar which is set to the first possible value of the current
     * page. The current page is the closest period of type PageType which contains the
     * current cell value.
     * @return
     */
    Calendar getPage();

    /**
     * Returns the Calendar which corresponds to the first cell of the grid of dates.
     * This may differ from <code>getPage</code> if the grid may contain dates before
     * the page period. 
     * 
     * @return a Calendar corresponding to the first cell of the grid
     * 
     * @see #getPage()
     */
    Calendar getFirstCell();

    /**
     * Returns a clone of the calendar with its current value.
     * <p>
     * PENDING JW: decide to use either the lead or leadDate methods, depends
     * on actual usage
     * 
     * @return
     */
    Calendar getLead();

    /**
     * Returns the value of the lead in terms of the cellType.
     * 
     * @return
     */
    int getLeadValue();

    /**
     * Sets the current cell to the given value.
     * 
     * @param leadValue
     */
    void setLeadValue(int leadValue);

    /**
     * Returns the Date at the current cell.<p>
     * 
     * PENDING JW: return a Calendar instead of its date?
     * @return
     */
    Date getLeadDate();

    /**
     * Sets the current cell to the given Date.
     * 
     * @param date the new value of the current cell, must not be null.
     */
    void setLeadDate(Date date);

    /**
     * Returns the maximally possible value for the lead in the current
     * page.
     * 
     * @return
     */
    int getMaximumLeadValue();

    /**
     * Returns the minimal possible value for the lead in the current
     * page.
     * 
     * @return
     */
    int getMinimumLeadValue();

    /**
     * Returns the number of cells which constitute a one-cell vertical scroll.
     * 
     * @return
     */
    int getVerticalCellUnit();
    
    /**
     * Returns the number of cells which constitute a one-page vertical scroll.
     * @return
     */
    int getVerticalPageUnit();
    
    /**
     * 
     * @return
     */
    FieldType getPageType();

    /**
     * @return
     */
    FieldType getCellType();

    /**
     * Increments the current cell value by one unit.
     */
    void nextCell();

    /**
     * Decrements the current cell value by one unit.
     */
    void previousCell();

    /**
     * Increments the current page value by the unit amount defined
     * in the PageType.
     */
    void nextPage();

    /**
     * Decrements the current page value by the unit amount defined
     * in the PageType.
     */
    void previousPage();

    /**
     * Increments the current cell value by a context specific value ("vertical 
     * scrolling")
     */
    void lowerCell();

    /**
     * Decrements the curent cell value by a context specific value ("vertical
     * scrolling")
     */
    void upperCell();

    /**
     * Increments the current page value by one, following the same constraints 
     * as multiple lowerCell calls.
     */
    void lowerPage();

    /**
     * Decrements the current page value by one, following the same constraints 
     * as multiple uppderPage calls.
     */
    void upperPage();


}