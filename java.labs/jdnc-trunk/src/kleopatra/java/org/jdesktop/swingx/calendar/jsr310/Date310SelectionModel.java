/*
 * $Id: Date310SelectionModel.java 3220 2010-01-20 12:18:36Z kleopatra $
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

import java.util.Locale;
import java.util.SortedSet;

import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.TimeZone;



/**
 * The Model used by calendar components. It controls the Calendar to use and 
 * keeps selection-related state.
 * 
 * @author Joshua Outwater
 */
public interface Date310SelectionModel {
    public static enum SelectionMode {
        /**
         * Mode that allows for selection of a single day.
         */
        SINGLE_SELECTION,
        /**
         * Mode that allows for selecting of multiple consecutive days.
         */
        SINGLE_INTERVAL_SELECTION,
        /**
         * Mode that allows for selecting disjoint days.
         */
        MULTIPLE_INTERVAL_SELECTION
    }

//---------------------- mode    
    /**
     * Get the selection mode.
     *
     * @return return the current selection mode
     */
    public SelectionMode getSelectionMode();

    /**
     * Set the selection mode.
     *
     * @param mode new selection mode
     */
    public void setSelectionMode(final SelectionMode mode);

    
//-------------------- calendar    
    
    /**
     * Gets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in the U.S., <code>Calendar.MONDAY</code>
     * in France.  This is needed when the model selection mode is 
     * <code>WEEK_INTERVAL_SELECTION</code>.
     * 
     * PENDING JW: move week-interval selection from JXMonthView into the model.
     *
     * @return int The first day of the week.
     * @see #setFirstDayOfWeek(int)
     */
    public DayOfWeek getFirstDayOfWeek();

    
    /**
     * Returns the TimeZone of this model.
     * 
     * @return the TimeZone of this model.
     * @see #setTimeZone(TimeZone)
     */
    public TimeZone getTimeZone();
    

    /**
     * Sets the TimeZone of this model. Fires a DateSelectionEvent of type 
     * CALENDAR_CHANGED if the new value is different from the old.
     * 
     * The default value depends on the Calendar's default.
     * 
     * PENDING JW: actually, it's a bound property. Use a propertyChangeListener?
     * 
     * @param timeZone the TimeZone to use in this model, must not be null.
     * @see #getTimeZone()
     */
    public void setTimeZone(TimeZone timeZone);
    
    /**
     * Returns the Locale of this model's calendar.
     * @return the Locale of this model's calendar.
     */
    public Locale getLocale();

    /**
     * Sets the Locale of this model's calendar. Fires a DateSelectionEvent of type 
     * CALENDAR_CHANGED if the new value is different from the old. <p>
     * 
     * The default value is Locale.default(). <p>
     * 
     * PENDING JW: fall back to JComponent.getDefaultLocale instead? We use this
     *   with components anyway? <p>
     * PENDING JW: actually, it's a bound property. Use a propertyChangeListener?
     * 
     * @param locale the Locale to use. If null, the default Locale is used.
     */
    public void setLocale(Locale locale);
    
    //-------------------- selection 
    
    /**
     * Adds the specified selection interval to the selection model.
     *
     * @param startDate interval start date, must not be null
     * @param endDate   interval end date >= start date, must not be null
     * @throws NullPointerException if any of the dates is null
     */
    public void addSelectionInterval(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Sest the specified selection interval to the selection model.
     *
     * @param startDate interval start date, must not be null
     * @param endDate   interval end date >= start date, must not be null
     * @throws NullPointerException if any of the dates is null
     */
    public void setSelectionInterval(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Removes the specifed selection interval from the selection model. If
     * the selection is changed by this method, it fires a DateSelectionEvent
     * of type DATES_REMOVED.
     *
     * @param startDate interval start date, must not be null
     * @param endDate   interval end date >= start date, must not be null
     * @throws NullPointerException if any of the dates is null
     */
    public void removeSelectionInterval(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Clears any selection from the selection model. Fires an Event of 
     * type SELECTION_CLEARED if there had been a selection, does nothing
     * otherwise.
     */
    public void clearSelection();

    /**
     * Returns the current selection.
     *
     * @return sorted set of selected dates, guaranteed to be never null.
     */
    public SortedSet<LocalDateTime> getSelection();

    /**
     * Returns the earliest date in the selection or null if the selection is empty.
     * 
     * @return the earliest date in the selection, or null if isSelectionEmpty.
     * 
     * @see #getLastSelectionDate()
     * @see #getSelection()
     * @see #isSelectionEmpty()
     */
    public LocalDateTime getFirstSelectionDate();

    /**
     * Returns the latest date in the selection or null if the selection is empty.
     * 
     * @return the lastest date in the selection, or null if isSelectionEmpty.
     * 
     * @see #getFirstSelectionDate()
     * @see #getSelection()
     * @see #isSelectionEmpty()
     */
    public LocalDateTime getLastSelectionDate();


    /**
     * Returns true if the date specified is selected, false otherwise. <p>
     * 
     * Note: it is up to implementations to define the exact notion of selected.
     * It does not imply the exact date as given is contained the set returned from 
     * getSelection().
     * 
     * @param date date to check for selection, must not be null
     * @return true if the date is selected, false otherwise
     * @throws NullPointerException if the date is null
     */
    public boolean isSelected(final LocalDateTime date);

    
    /**
     * Returns true if the selection is empty, false otherwise.
     *
     * @return true if the selection is empty, false otherwise
     */
    public boolean isSelectionEmpty();


    /**
     * Returns a <code>SortedSet</code> of <code>LocalDateTime</codes>s that are unselectable.
     *
     * @return sorted set of dates
     */
    public SortedSet<LocalDateTime> getUnselectableDates();

    /**
     * Sets a collection of dates which are not selectable.<p>
     * 
     * Note: it is up to implementations to define the exact notion of unselectableDate.
     * It does not imply the only the exact date as given is unselectable, it might
     * have a period like "all dates on the same day".
     * 
     * PENDING JW: any collection would do - why insist on a SortedSet?
     *
     * @param unselectableDates dates that are unselectable, must not be null and 
     *   must not contain null dates.
     */
    public void setUnselectableDates(SortedSet<LocalDateTime> unselectableDates);

    /**
     * Returns true is the specified date is unselectable.
     *
     * @param unselectableDate the date to check for unselectability, must not be null.
     * @return true is the date is unselectable, false otherwise
     */
    public boolean isUnselectableDate(LocalDateTime unselectableDate);

    /**
     * Return the upper bound date that is allowed to be selected for this
     * model.
     *
     * @return upper bound date or null if not set
     */
    public LocalDateTime getUpperBound();

    /**
     * Set the upper bound date that is allowed to be selected for this model.
     *
     * @param upperBound upper bound
     */
    public void setUpperBound(final LocalDateTime upperBound);

    /**
     * Return the lower bound date that is allowed to be selected for this
     * model.
     *
     * @return lower bound date or null if not set
     */
    public LocalDateTime getLowerBound();

    /**
     * Set the lower bound date that is allowed to be selected for this model.
     *
     * @param lowerBound lower bound date or null if not set
     */
    public void setLowerBound(final LocalDateTime lowerBound);

    /**
     * Set the property to mark upcoming selections as intermediate/
     * final. This will fire a event of type adjusting_start/stop.
     * 
     * The default value is false.
     * 
     * Note: Client code marking as intermediate must take care of
     * finalizing again.
     * 
     * @param adjusting a flag to turn the adjusting property on/off.
     */
    public void setAdjusting(boolean adjusting);

    /**
     * Returns the property to decide whether the selection is 
     * intermediate or final.
     * 
     * @return the adjusting property.
     */
    public boolean isAdjusting();

    /**
     * Add the specified listener to this model.
     *
     * @param listener listener to add to this model
     */
    public void addDateSelectionListener(Date310SelectionListener listener);

    /**
     * Remove the specified listener to this model.
     *
     * @param listener listener to remove from this model
     */
    public void removeDateSelectionListener(Date310SelectionListener listener);

}
