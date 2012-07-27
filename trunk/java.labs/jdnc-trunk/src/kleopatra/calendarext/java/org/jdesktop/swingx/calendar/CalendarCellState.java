/*
 * $Id: CalendarState.java 3100 2008-10-14 22:33:10Z rah003 $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * States of a cell in a calendar page.
 * 
 * @author Jeanette Winzenburg
 */
public enum CalendarCellState {
    TODAY_CELL(true, true),
    DAY_OFF_CELL(true, false),
    DAY_CELL(true, true, FieldType.MONTH, DAY_OFF_CELL),
    WEEK_OF_YEAR_TITLE(false, false),
    DAY_OF_WEEK_TITLE(false, false), 
    PAGE_TITLE(false, false),
    // zoomed states
    MONTH_CELL(true, true, FieldType.YEAR),
    YEAR_OFF_CELL(true, false, FieldType.DECADE),
    YEAR_CELL(true, true, FieldType.DECADE, YEAR_OFF_CELL);
    
    private boolean onPage;
    private FieldType pageType;
    private boolean content;
    private CalendarCellState offState;
    
    CalendarCellState(boolean content, boolean onPage) {
        this(content, onPage, FieldType.MONTH);
    }
    
    CalendarCellState(boolean content, boolean onPage, FieldType pageType) {
        this(content, onPage, pageType, null);
    }
    
    CalendarCellState(boolean content, boolean onPage, FieldType pageType, CalendarCellState offState) {
        this.onPage = onPage;
        this.pageType = pageType;
        this.content = content;
        this.offState = offState;
    }
    
    public boolean isOnPage() {
        return onPage;
    }
    
    public FieldType getPageType() {
        return pageType;
    }
    
//    public CalendarCellState getOffState() {
//        return offState;
//    }
    
//    public boolean hasOffState() {
//        return isContent() && offState != null;
//    }
    
    /**
     * Returns a boolean indicating whether the type represents a content state. 
     * If false, the type represents a title/header field.
     * @return true for content types, false for header types
     */
    public boolean isContent() {
        return content;
        
    }
    
    /**
     * Returns a boolean indicating whether the type is content and the date 
     * inside the page.
     * 
     * @return
     */
    public boolean isOnPage(Calendar page, Date date) {
        return isContent() && isContainedInPage(page, date);
        
    }
    
    /**
     * Returns a boolean indicating whether the given date is contained in the 
     * Calendar, using the range of the pageType's cell.
     * 
     * @param range the calendar which defines the range
     * @param date the date to check
     * @return a boolean indicating whether the given dat is contained in the
     *   Calendar.
     */
    public boolean isContainedInCell(Calendar range, Date date) {
        return pageType.isContainedInCell(range, date);
    }
    
    /**
     * Returns a boolean indicating whether the given date is contained in the 
     * Calendar, using the range of the pageType's cell.
     * 
     * @param range the calendar which defines the range
     * @param date the date to check
     * @return a boolean indicating whether the given dat is contained in the
     *   Calendar.
     */
    public boolean isContainedInPage(Calendar range, Date date) {
        return pageType.isContainedInPage(range, date);
    }
}