/*
 * Created on 06.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.util.Calendar;
import java.util.Date;

/**
 * Field type of a Calendar. 
 */
public enum FieldType {
    
    /**
     * A day, assumed to be the lowest level (for now). 
     */
    DAY(Calendar.DATE),
    
    /**
     * A Month, has Day as sub unit.
     */
    MONTH (Calendar.MONTH, DAY),
    
    /**
     * A Year, has Month as sub unit.
     */
    YEAR (Calendar.YEAR, MONTH),
    
    /**
     * A Decade - 10 years - has Year as sub unit.
     */
    DECADE (CalendarUtils.DECADE, YEAR);
    
    private int calendarField;
    private FieldType cellType;
    
    FieldType (int calendarField) {
        this(calendarField, null);
    }
    
    FieldType(int calendarField, FieldType cellType) {
        this.calendarField = calendarField;
        this.cellType = cellType;
    }
    /**
     * Returns the corresponding field in Calendar.
     * @return
     */
    public int getCalendarField() {
        return calendarField;
    }
    
    /**
     * Returns the amount of one unit increment.
     * @return
     */
    public int getUnitIncrement() {
        return 1;
    }
    
    /**
     * Returns the type of its sub-units.
     * @return
     */
    public FieldType getCellType() {
        return cellType;
    }
    
    /**
     * Returns true if the calendar field is known to the Calendar class, false
     * if it is custom.
     * 
     * @return
     */
    public boolean isNativeCalendarField() {
        return getCalendarField() < CalendarUtils.DECADE;
    }
    
    /**
     * 
     * @param calendar
     * @param amount
     */
    public void add(Calendar calendar, int amount) {
        CalendarUtils.add(calendar, getCalendarField(), amount);
    }

    public int get(Calendar calendar) {
        return CalendarUtils.get(calendar, getCalendarField());
    }
    
    public void set(Calendar calendar, int value) {
        CalendarUtils.set(calendar, getCalendarField(), value);
    }

    public int getCellCalendarField() {
        if (isNativeCalendarField())
            return getCellType() != null ? getCellType().getCalendarField() : 0;
        // FIXME hard-coded    
        return CalendarUtils.YEAR_IN_DECADE;    
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
        if (getCellType() == null) return false;
        return getCellType().isContainedInPage(range, date);
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
        return CalendarUtils.isSame(range, date, getCalendarField());
    }

}