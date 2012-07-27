/*
 * $Id: Day310SelectionModel.java 3339 2011-02-04 17:03:23Z kleopatra $
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

import java.util.Comparator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.util.Contract;

/**
 * 
 * DaySelectionModel is a (temporary?) implementation of DateSelectionModel 
 * which normalizes all dates to the start of the day, that is zeroes all 
 * time fields. Responsibility extracted from JXMonthView (which must
 * respect rules of model instead of trying to be clever itself).
 * 
 * @author Joshua Outwater
 */
public class Day310SelectionModel extends AbstractDate310SelectionModel {
    private SelectionMode selectionMode;
    private SortedSet<LocalDateTime> selectedDates;
    private SortedSet<LocalDateTime> unselectableDates;

    /**
     * 
     */
    public Day310SelectionModel() {
        this(null);
    }

    /**
     * 
     */
    public Day310SelectionModel(Locale locale) {
        super(locale);
        this.listenerMap = new EventListenerMap();
        this.selectionMode = SelectionMode.SINGLE_SELECTION;
        this.selectedDates = new TreeSet<LocalDateTime>(new DatePartComparator());
        this.unselectableDates = new TreeSet<LocalDateTime>(new DatePartComparator());
        
    }
    /**
     * 
     */
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    /**
     * 
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        clearSelection();
    }

    //---------------------- selection ops    
    /**
     * {@inheritDoc}
     */
    public void addSelectionInterval(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            return;
        }
//        startDate = startOfDay(startDate);
//        endDate = startOfDay(endDate);
        boolean added = false;
        switch (selectionMode) {
            case SINGLE_SELECTION:
                if (isSelected(startDate)) return;
                clearSelectionImpl();
                added = addSelectionImpl(startDate, startDate);
                break;
            case SINGLE_INTERVAL_SELECTION:
                if (isIntervalSelected(startDate, endDate)) return;
                clearSelectionImpl();
                added = addSelectionImpl(startDate, endDate);
                break;
            case MULTIPLE_INTERVAL_SELECTION:
                if (isIntervalSelected(startDate, endDate)) return;
                added = addSelectionImpl(startDate, endDate);
                break;
            default:
                break;
        }
        if (added) {
            fireValueChanged(EventType.DATES_ADDED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setSelectionInterval(LocalDateTime startDate, LocalDateTime endDate) {
//        startDate = startOfDay(startDate);
//        endDate = startOfDay(endDate);
        if (SelectionMode.SINGLE_SELECTION.equals(selectionMode)) {
           if (isSelected(startDate)) return;
           endDate = startDate;
        } else {
            if (isIntervalSelected(startDate, endDate)) return;
        }
        clearSelectionImpl();
        if (addSelectionImpl(startDate, endDate)) {
            fireValueChanged(EventType.DATES_SET);
        }
    }

    /**
     * Checks and returns if the single date interval bounded by startDate and endDate
     * is selected. This is useful only for SingleInterval mode.
     * 
     * @param startDate the start of the interval
     * @param endDate the end of the interval, must be >= startDate
     * @return true the interval is selected, false otherwise.
     */
    private boolean isIntervalSelected(LocalDateTime startDate, LocalDateTime endDate) {
        if (isSelectionEmpty()) return false;
//        startDate = startOfDay(startDate);
//        endDate = startOfDay(endDate);
        return selectedDates.first().equals(startDate) 
           && selectedDates.last().equals(endDate);
    }

    /**
     * {@inheritDoc}
     */
    public void removeSelectionInterval(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            return;
        }
//        startDate = startOfDay(startDate);
        // subset upper bound is exclusive
//        endDate = startOfDay(endDate).plus(Period.days(1));
        endDate = endDate.plusDays(1);
        SortedSet<LocalDateTime> datesToRemove = selectedDates.subSet(startDate, endDate); 
        if (!datesToRemove.isEmpty()) {
            selectedDates.removeAll(datesToRemove);
            fireValueChanged(EventType.DATES_REMOVED);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void clearSelection() {
        if (isSelectionEmpty()) return;
        clearSelectionImpl();
        fireValueChanged(EventType.SELECTION_CLEARED);
    }

    private void clearSelectionImpl() {
        selectedDates.clear();
    }

    /**
     * {@inheritDoc}
     */
    public SortedSet<LocalDateTime> getSelection() {
        return new TreeSet<LocalDateTime>(selectedDates);
    }

    /**
     * {@inheritDoc}
     */
    public LocalDateTime getFirstSelectionDate() {
        return isSelectionEmpty() ? null : selectedDates.first();
    }

    /**
     * {@inheritDoc}
     */
    public LocalDateTime getLastSelectionDate() {
        return isSelectionEmpty() ? null : selectedDates.last();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelected(LocalDateTime date) {
        // JW: don't need Contract ... startOfDay will throw NPE if null
//        return selectedDates.contains(startOfDay(date));
        Contract.asNotNull(date, "date must not be null");
        return selectedDates.contains(date);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSelectionEmpty() {
        return selectedDates.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    public SortedSet<LocalDateTime> getUnselectableDates() {
        return new TreeSet<LocalDateTime>(unselectableDates);
    }

    /**
     * {@inheritDoc}
     */
    public void setUnselectableDates(SortedSet<LocalDateTime> unselectables) {
        this.unselectableDates.clear();
        for (LocalDateTime date : unselectables) {
//            unselectableDates.add(startOfDay(date));
            unselectableDates.add(date);
        }
        for (LocalDateTime unselectableDate : this.unselectableDates) {
            removeSelectionInterval(unselectableDate, unselectableDate);
        }
        fireValueChanged(EventType.UNSELECTED_DATES_CHANGED);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isUnselectableDate(LocalDateTime date) {
//        date = startOfDay(date);
        return upperBound != null && date.isAfter(upperBound) ||
                lowerBound != null && date.isBefore(lowerBound) ||
                unselectableDates != null && unselectableDates.contains(date);
    }


    private boolean addSelectionImpl(final LocalDateTime startDate, final LocalDateTime endDate) {
        boolean hasAdded = false;
        LocalDateTime date = startDate;
        while (!date.isAfter(endDate)) {
            if (!isUnselectableDate(date)) {
                hasAdded = selectedDates.add(date) || hasAdded;
            }
            date = date.plus(Period.ofDays(1));
        }
        return hasAdded;
    }

    public static class DatePartComparator implements Comparator<LocalDateTime> {

        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o1.toLocalDate().compareTo(o2.toLocalDate());
        }
        
    }

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc} <p>
     * 
     * Implemented to return the start of the day which contains the date.
     */
//    public LocalDateTime getNormalizedDate(LocalDateTime date) {
//        Contract.asNotNull(date, "date must not be null");
//        return startOfDay(date);
//    }

}