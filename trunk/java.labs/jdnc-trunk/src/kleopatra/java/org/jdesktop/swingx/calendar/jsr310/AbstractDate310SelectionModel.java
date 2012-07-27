/*
 * $Id: AbstractDate310SelectionModel.java 3339 2011-02-04 17:03:23Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.calendar.jsr310;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.time.calendar.Clock;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;
import javax.time.calendar.TimeZone;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.util.Contract;


/**
 * Abstract base implementation of DateSelectionModel. Implements
 * notification, Calendar related properties and lower/upper bounds.
 * 
 * @author Jeanette Winzenburg
 */
public abstract class AbstractDate310SelectionModel implements Date310SelectionModel {
    public static final SortedSet<LocalDateTime> EMPTY_DATES = 
        Collections.unmodifiableSortedSet(new TreeSet<LocalDateTime>());
    
    protected EventListenerMap listenerMap;
    protected boolean adjusting;
//    protected Calendar calendar;
    protected LocalDateTime upperBound;
    protected LocalDateTime lowerBound;

    /** 
     * the locale used by the calendar. <p>
     * NOTE: need to keep separately as a Calendar has no getter.
     */
    protected Locale locale;

    private TimeZone timeZone;

    /**
     * Instantiates a DateSelectionModel with default locale.
     */
    public AbstractDate310SelectionModel() {
        this(null);
    }
    
    /**
     * Instantiates a DateSelectionModel with the given locale. If the locale is
     * null, the Locale's default is used.
     * 
     * PENDING JW: fall back to JComponent.getDefaultLocale instead? We use this
     *   with components anyway?
     * 
     * @param locale the Locale to use with this model, defaults to Locale.default()
     *    if null.
     */
    public AbstractDate310SelectionModel(Locale locale) {
        this.listenerMap = new EventListenerMap();
        // PENDING JW: hack around missing default TimeZone in jsr310
        Clock clock = Clock.systemDefaultZone();
        this.timeZone = clock.zonedDateTime().getZone();
        setLocale(locale);
    }


    /**
     * {@inheritDoc}
     */
    public DayOfWeek getFirstDayOfWeek() {
        // PENDING JW: hack around missing firstDayOfWeek in jsr310
        return DateTimeUtils.getFirstDayOfWeek(getLocale());
    }


//    /**
//     * {@inheritDoc}
//     */
//    public int getMinimalDaysInFirstWeek() {
//        // PENDING JW: hack around different minimalDaysInFirstWeek in jsr310
//        return DateTimeUtils.getMinimalDaysInFirstWeek(getLocale());
//    }

    
    /**
     * {@inheritDoc}
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * {@inheritDoc}
     */
    public void setTimeZone(TimeZone timeZone) {
        Contract.asNotNull(timeZone, "TimeZone must not be null");
        if (areEqual(getTimeZone(), timeZone)) return;
        TimeZone oldTimeZone = getTimeZone();
        this.timeZone = timeZone;
        adjustDatesToTimeZone(oldTimeZone);
        fireValueChanged(EventType.CALENDAR_CHANGED);
    }

    /**
     * Adjusts all stored dates to a new time zone.
     * This method is called after the change had been made. <p>
     * 
     * This implementation resets all dates to null, clears everything. 
     * Subclasses may override to really map to the new time zone.
     *
     * @param oldTimeZone the old time zone
     * 
     */
    protected void adjustDatesToTimeZone(TimeZone oldTimeZone) {
        clearSelection();
        setLowerBound(null);
        setUpperBound(null);
        setUnselectableDates(EMPTY_DATES);
    }
    
    /**
     * {@inheritDoc}
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * {@inheritDoc}
     */
    public void setLocale(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        if (locale.equals(getLocale())) return;
        this.locale = locale;
        fireValueChanged(EventType.CALENDAR_CHANGED);
    }

//------------------- utility methods
    
    /**
     * Returns the start of the day of the given date in this model's calendar.
     * NOTE: the calendar is changed by this operation.
     * 
     * @param date the LocalDateTime to get the start for.
     * @return the LocalDateTime representing the start of the day of the input date.
     */
//    protected LocalDateTime startOfDay(LocalDateTime date) {
//        return LocalDateTime.dateMidnight(date);
//    }

    /**
     * Returns the end of the day of the given date in this model's calendar.
     * NOTE: the calendar is changed by this operation.
     * 
     * @param date the LocalDateTime to get the start for.
     * @return the LocalDateTime representing the end of the day of the input date.
     */
//    protected LocalDateTime endOfDay(LocalDateTime date) {
//        return CalendarUtils.endOfDay(calendar, date);
//    }

    /**
     * Returns a boolean indicating whether the given dates are on the same day in
     * the coordinates of the model's calendar.
     * 
     * @param selected one of the dates to check, must not be null.
     * @param compare the other of the dates to check, must not be null.
     * @return true if both dates represent the same day in this model's calendar.
     */
//    protected boolean isSameDay(LocalDateTime selected, LocalDateTime compare) {
//        return selected.toLocalDate().equals(compare.toLocalDate());
//    }

//------------------- bounds

    /**
     * {@inheritDoc}
     */
    public LocalDateTime getUpperBound() {
        return upperBound;
    }

    /**
     * {@inheritDoc}
     */
    public void setUpperBound(LocalDateTime upperBound) {
//        if (upperBound != null) {
//            upperBound = getNormalizedDate(upperBound);
//        }
        if (areEqual(upperBound, getUpperBound()))
            return;
        this.upperBound = upperBound;
        if (this.upperBound != null && !isSelectionEmpty()) {
            removeSelectionInterval(this.upperBound.plus(Period.ofNanos(1)),
                    getLastSelectionDate());
        }
        fireValueChanged(EventType.UPPER_BOUND_CHANGED);
    }

    /**
     * {@inheritDoc}
     */
    public LocalDateTime getLowerBound() {
        return lowerBound;
    }

    /**
     * {@inheritDoc}
     */
    public void setLowerBound(LocalDateTime lowerBound) {
//        if (lowerBound != null) {
//            lowerBound = getNormalizedDate(lowerBound);
//        }
        if (areEqual(lowerBound, getLowerBound()))
            return;
        this.lowerBound = lowerBound;
        if (this.lowerBound != null && !isSelectionEmpty()) {
            // Remove anything below the lower bound
            removeSelectionInterval(getFirstSelectionDate(), this.lowerBound.minus(Period.ofNanos(1)));
        }
        fireValueChanged(EventType.LOWER_BOUND_CHANGED);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isAdjusting() {
        return adjusting;
    }

    /**
     * {@inheritDoc}
     */
    public void setAdjusting(boolean adjusting) {
        if (adjusting == isAdjusting()) return;
        this.adjusting = adjusting;
       fireValueChanged(adjusting ? EventType.ADJUSTING_STARTED : EventType.ADJUSTING_STOPPED);
        
    }

//----------------- notification    
    /**
     * {@inheritDoc}
     */
    public void addDateSelectionListener(Date310SelectionListener l) {
        listenerMap.add(Date310SelectionListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    public void removeDateSelectionListener(Date310SelectionListener l) {
        listenerMap.remove(Date310SelectionListener.class, l);
    }

    public List<Date310SelectionListener> getDateSelectionListeners() {
        return listenerMap.getListeners(Date310SelectionListener.class);
    }

    protected void fireValueChanged(Date310SelectionEvent.EventType eventType) {
        List<Date310SelectionListener> listeners = getDateSelectionListeners();
        Date310SelectionEvent e = null;

        for (Date310SelectionListener listener : listeners) {
            if (e == null) {
                e = new Date310SelectionEvent(this, eventType, isAdjusting());
            }
            listener.valueChanged(e);
        }
    }

    /**
     * Checks the given objects for being equal.
     * 
     * @param current one of the objects to compare
     * @param date the otherr of the objects to compare
     * @return true if the two given dates both are null or both are not null and equal, 
     *  false otherwise.
     */
    protected static boolean areEqual(Object current, Object date) {
        if ((date == null) && (current == null)) {
            return true;
        }
        if (date != null) {
           return date.equals(current);
        }
        return false;
    }


}
