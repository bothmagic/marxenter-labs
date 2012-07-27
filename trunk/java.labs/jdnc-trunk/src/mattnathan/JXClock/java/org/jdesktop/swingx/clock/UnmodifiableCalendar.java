/*
 * $Id: UnmodifiableCalendar.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Defines an immutable calendar instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class UnmodifiableCalendar extends Calendar {
    private final Calendar c;
    protected UnmodifiableCalendar(Calendar c) {
        if (c == null) {
            throw new NullPointerException("c cannot be null");
        }
        this.c = c;
    }





    /**
     * Adds or subtracts the specified amount of time to the given calendar field, based on the calendar's rules.
     *
     * @param field the calendar field.
     * @param amount the amount of date or time to be added to the field.
     */
    @Override
    public void add(int field, int amount) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Compares this object with the specified object for order.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
     *   than the specified object.
     */
    @Override
    public int compareTo(Calendar o) {
        return c.compareTo(o);
    }





    /**
     * Converts the current millisecond time value {@link #time} to calendar field values in {@link #fields fields[]}.
     */
    @Override
    protected void computeFields() {
    }





    /**
     * Converts the current calendar field values in {@link #fields fields[]} to the millisecond time value {@link
     * #time}.
     */
    @Override
    protected void computeTime() {
    }





    /**
     * Returns the highest minimum value for the given calendar field of this <code>Calendar</code> instance.
     *
     * @param field the calendar field.
     * @return the highest minimum value for the given calendar field.
     */
    @Override
    public int getGreatestMinimum(int field) {
        return c.getGreatestMinimum(field);
    }





    /**
     * Returns the lowest maximum value for the given calendar field of this <code>Calendar</code> instance.
     *
     * @param field the calendar field.
     * @return the lowest maximum value for the given calendar field.
     */
    @Override
    public int getLeastMaximum(int field) {
        return c.getLeastMaximum(field);
    }





    /**
     * Returns the maximum value for the given calendar field of this <code>Calendar</code> instance.
     *
     * @param field the calendar field.
     * @return the maximum value for the given calendar field.
     */
    @Override
    public int getMaximum(int field) {
        return c.getMaximum(field);
    }





    /**
     * Returns the minimum value for the given calendar field of this <code>Calendar</code> instance.
     *
     * @param field the calendar field.
     * @return the minimum value for the given calendar field.
     */
    @Override
    public int getMinimum(int field) {
        return c.getMinimum(field);
    }





    /**
     * Adds or subtracts (up/down) a single unit of time on the given time field without changing larger fields.
     *
     * @param field the time field.
     * @param up indicates if the value of the specified time field is to be rolled up or rolled down. Use true if
     *   rolling up, false otherwise.
     */
    @Override
    public void roll(int field, boolean up) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Returns whether this <code>Calendar</code> represents a time after the time represented by the specified
     * <code>Object</code>.
     *
     * @param when the <code>Object</code> to be compared
     * @return <code>true</code> if the time of this <code>Calendar</code> is after the time represented by
     *   <code>when</code>; <code>false</code> otherwise.
     */
    @Override
    public boolean after(Object when) {
        return c.after(when);
    }





    /**
     * Returns whether this <code>Calendar</code> represents a time before the time represented by the specified
     * <code>Object</code>.
     *
     * @param when the <code>Object</code> to be compared
     * @return <code>true</code> if the time of this <code>Calendar</code> is before the time represented by
     *   <code>when</code>; <code>false</code> otherwise.
     */
    @Override
    public boolean before(Object when) {
        return c.before(when);
    }





    /**
     * Returns the value of the given calendar field.
     *
     * @param field the given calendar field.
     * @return the value for the given calendar field.
     */
    @Override
    public int get(int field) {
        return c.get(field);
    }





    /**
     * Returns the maximum value that the specified calendar field could have, given the time value of this
     * <code>Calendar</code>.
     *
     * @param field the calendar field
     * @return the maximum of the given calendar field for the time value of this <code>Calendar</code>
     */
    @Override
    public int getActualMaximum(int field) {
        return c.getActualMaximum(field);
    }





    /**
     * Returns the minimum value that the specified calendar field could have, given the time value of this
     * <code>Calendar</code>.
     *
     * @param field the calendar field
     * @return the minimum of the given calendar field for the time value of this <code>Calendar</code>
     */
    @Override
    public int getActualMinimum(int field) {
        return c.getActualMinimum(field);
    }





    /**
     * Gets what the first day of the week is; e.g., <code>SUNDAY</code> in the U.S., <code>MONDAY</code> in France.
     *
     * @return the first day of the week.
     */
    @Override
    public int getFirstDayOfWeek() {
        return c.getFirstDayOfWeek();
    }





    /**
     * Gets what the minimal days required in the first week of the year are; e.g., if the first week is defined as
     * one that contains the first day of the first month of a year, this method returns 1.
     *
     * @return the minimal days required in the first week of the year.
     */
    @Override
    public int getMinimalDaysInFirstWeek() {
        return c.getMinimalDaysInFirstWeek();
    }





    /**
     * Returns this Calendar's time value in milliseconds.
     *
     * @return the current time as UTC milliseconds from the epoch.
     */
    @Override
    public long getTimeInMillis() {
        return c.getTimeInMillis();
    }





    /**
     * Gets the time zone.
     *
     * @return the time zone object associated with this calendar.
     */
    @Override
    public TimeZone getTimeZone() {
        return c.getTimeZone();
    }





    /**
     * Tells whether date/time interpretation is to be lenient.
     *
     * @return <code>true</code> if the interpretation mode of this calendar is lenient; <code>false</code>
     *   otherwise.
     */
    @Override
    public boolean isLenient() {
        return c.isLenient();
    }





    /**
     * Adds the specified (signed) amount to the specified calendar field without changing larger fields.
     *
     * @param field the calendar field.
     * @param amount the signed amount to add to the calendar <code>field</code>.
     */
    @Override
    public void roll(int field, int amount) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Sets the given calendar field to the given value.
     *
     * @param field the given calendar field.
     * @param value the value to be set for the given calendar field.
     */
    @Override
    public void set(int field, int value) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Sets what the first day of the week is; e.g., <code>SUNDAY</code> in the U.S., <code>MONDAY</code> in France.
     *
     * @param value the given first day of the week.
     */
    @Override
    public void setFirstDayOfWeek(int value) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Specifies whether or not date/time interpretation is to be lenient.
     *
     * @param lenient <code>true</code> if the lenient mode is to be turned on; <code>false</code> if it is to be
     *   turned off.
     */
    @Override
    public void setLenient(boolean lenient) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Sets what the minimal days required in the first week of the year are; For example, if the first week is
     * defined as one that contains the first day of the first month of a year, call this method with value 1.
     *
     * @param value the given minimal days required in the first week of the year.
     */
    @Override
    public void setMinimalDaysInFirstWeek(int value) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Sets this Calendar's current time from the given long value.
     *
     * @param millis the new time in UTC milliseconds from the epoch.
     */
    @Override
    public void setTimeInMillis(long millis) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }





    /**
     * Sets the time zone with the given time zone value.
     *
     * @param value the given time zone.
     */
    @Override
    public void setTimeZone(TimeZone value) {
        throw new UnsupportedOperationException("This calendar instance is immutable");
    }
}
