/*
 * $Id$
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;

/**
 * Controller to navigate on a Calendar page in a grid of details. Navigation
 * supports both "horizontal" and "vertical" unit/block increment/decrement  
 * as appropriate to CellType/PageType pair which defines the grid.<p>
 * 
 * 
 * @author Jeanette Winzenburg
 */
public class CalendarNavigator implements Navigator {
    
    // constants for vertical navigation - context and layout dependent?
    private int verticalCellUnit;
    private int verticalPageUnit;
    
    private FieldType pageType;
    /**
     * Keeps the current selection.
     */
    protected Calendar calendar;
    
    /**
     * Instantiates a Navigator with PageType MONTH, using the default calendar 
     * instance with the default date.
     */
    public CalendarNavigator() {
        this(Calendar.getInstance(), FieldType.MONTH, 7, 5);
    }
  
    /**
     * Instantiates a Navigator with the given Calendar with the given FieldType for
     * a page and vertical scroll unit.
     * 
     * @param calendar The calendar to use
     * @param pageType the FieldType for a page, must have a detailsField
     * @param verticalCellUnit the number of cell values which constitutes a vertical cell
     *    move
     * @param verticalPageMultiplier the multiplier for vertical page scroll
     * 
     * @throws IllegalArgumentException if the pageType has no valid cell type
     */
    public CalendarNavigator(Calendar calendar, FieldType pageType, 
            int verticalCellUnit, int verticalPageMultiplier) {
        if (pageType.getCellType() == null) throw
            new IllegalArgumentException("Illegal page type, must have a not-null cell type");
        this.pageType = pageType;
        this.verticalCellUnit = verticalCellUnit;
        this.verticalPageUnit = verticalPageMultiplier * verticalCellUnit;
        setCalendar(calendar);
    }
    
    /**
     * @inherited <p>
     * 
     * Note: The new calendar completely defines the lead/page properties. 
     * If the state of the old calendar - if any - is needed, it's
     * the client code responsibility to grab and save the old state before
     * calling this method.
     * 
     */
    @Override
    public void setCalendar(Calendar calendar) {
        this.calendar = (Calendar) calendar.clone();
        // JW: protect ourselves - make sure the calendar is flushed
        this.calendar.getTimeInMillis();
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public Calendar getPage() {
        Calendar cellValue = getLead();
        CalendarUtils.startOf(cellValue, getPageType().getCalendarField());
        return cellValue;
    }

    
    /** 
     * @inherited <p>
     */
    @Override
    public Calendar getFirstCell() {
        Calendar page = getPage();
        // FIXME JW: cleanup ... add subclasses to handle
        if (FieldType.MONTH == getPageType()) {
            CalendarUtils.startOf(page, Calendar.WEEK_OF_YEAR);
        } else if (FieldType.DECADE == getPageType()) {
            page.add(Calendar.YEAR, -1);
        }
        return page;
    }

    /** 
     * @inherited <p>
     */
    @Override
    public Calendar getLead() {
        return (Calendar) calendar.clone();
    }

    /** 
     * @inherited <p>
     */
    @Override
    public int getLeadValue() {
        if (getPageType().isNativeCalendarField())
            return calendar.get(getCellType().getCalendarField());
        int realValue = getCellType().get(getLead());
        return realValue % 10;
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void setLeadValue(int leadValue) {
        if (getPageType().isNativeCalendarField()) {
            if (leadValue < getMinimumLeadValue()) {
                int diff = getMinimumLeadValue() - leadValue;
                calendar.set(getCellType().getCalendarField(), getMinimumLeadValue());
                calendar.add(getCellType().getCalendarField(), -diff);
            } else {
                calendar.set(getCellType().getCalendarField(), leadValue);
            }
            
        } else {
            if (leadValue < getMinimumLeadValue()) {
                int diff = getMinimumLeadValue() - leadValue;
                // FIXME - move into fieldType insteead of hard-coding
                // this here will not survive addition of century!
                CalendarUtils.set(calendar, CalendarUtils.YEAR_IN_DECADE, getMinimumLeadValue());
                CalendarUtils.add(calendar, Calendar.YEAR, -diff);
            } else {
                CalendarUtils.set(calendar, CalendarUtils.YEAR_IN_DECADE, leadValue);
            }
                
        }
        calendar.getTimeInMillis();
    }

    /** 
     * @inherited <p>
     */
    @Override
    public Date getLeadDate() {
        return calendar.getTime();
    }

    /** 
     * @inherited <p>
     */
    @Override
    public void setLeadDate(Date date) {
        calendar.setTime(date);
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public int getMaximumLeadValue() {
        if (getPageType().isNativeCalendarField())
            return calendar.getActualMaximum(getCellType().getCalendarField());
        return 9;
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public int getMinimumLeadValue() {
        if (getPageType().isNativeCalendarField())
            return calendar.getActualMinimum(getCellType().getCalendarField());
        return 0;
    }
    
    
    
    /** 
     * @inherited <p>
     */
    @Override
    public int getVerticalCellUnit() {
        return verticalCellUnit;
    }

    /** 
     * @inherited <p>
     */
    @Override
    public int getVerticalPageUnit() {
        return verticalPageUnit;
    }

//-------------------- navigation
    /** 
     * @inherited <p>
     */
    @Override
    public void nextCell() {
        calendar.add(getCellType().getCalendarField(), 1);
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void previousCell() {
        calendar.add(getCellType().getCalendarField(), -1);
    }

    /** 
     * @inherited <p>
     */
    @Override
    public void nextPage() {
        pageType.add(calendar, pageType.getUnitIncrement());
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void previousPage() {
        pageType.add(calendar, - pageType.getUnitIncrement());
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void lowerCell() {
        calendar.add(getCellType().getCalendarField(), verticalCellUnit);
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void upperCell() {
        calendar.add(getCellType().getCalendarField(), -verticalCellUnit);
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void lowerPage() {
        calendar.add(getCellType().getCalendarField(), verticalPageUnit);
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public void upperPage() {
        calendar.add(getCellType().getCalendarField(), -verticalPageUnit);
    }
    
    
    
    /** 
     * @inherited <p>
     */
    @Override
    public FieldType getPageType() {
        return pageType;
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public FieldType getCellType() {
        return getPageType().getCellType();
    }


}

