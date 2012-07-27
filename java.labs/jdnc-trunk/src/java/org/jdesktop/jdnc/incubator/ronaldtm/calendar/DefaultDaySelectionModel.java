/*
 * $Id: DefaultDaySelectionModel.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Ronald Tetsuo Miura
 */
public class DefaultDaySelectionModel implements DaySelectionModel {

    /** */
    private static final int DAYS_IN_WEEK = 7;

    /** */
    private long _selectionAnchor = NONE;

    /** */
    private long _selectionLead = NONE;

    /** */
    private long _start = NONE;

    /** */
    private long _end = NONE;

    /** */
    private int _mode;

    /** */
    private int _firstDayOfWeek = Calendar.SUNDAY;

    /** */
    private Set _listeners = new HashSet();

    /** */
    private Calendar _cal = Calendar.getInstance();

    /**
     */
    public DefaultDaySelectionModel() {
        //
    }

    /**
     * @param mode
     */
    public DefaultDaySelectionModel(int mode) {
        this.setSelectionMode(mode);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#isSelectable(long)
     */
    public boolean isSelectable(long time) {
        return true;
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#isSelected(long)
     */
    public boolean isSelected(long time) {
        this._cal.setTimeInMillis(time);
        this._cal.set(Calendar.HOUR_OF_DAY, 0);
        this._cal.set(Calendar.MINUTE, 0);
        this._cal.set(Calendar.SECOND, 0);
        this._cal.set(Calendar.MILLISECOND, 0);
        time = this._cal.getTimeInMillis();
        return (time >= this._start) && (time <= this._end);
    }

    /**
     * @param selectionAnchor -
     * @param selectionLead -
     */
    protected void adjustSelection(long selectionAnchor, long selectionLead) {
        long formerAnchor = getSelectionAnchor();
        long formerLead = getSelectionLead();

        long start;
        long end;

        if (this._mode == SINGLE_SELECTION) {
            start = selectionLead;
            end = selectionLead;
        } else {
            start = Math.min(selectionAnchor, selectionLead);
            end = Math.max(selectionAnchor, selectionLead);
        }

        this._cal.setTimeInMillis(start);
        this._cal.set(Calendar.HOUR_OF_DAY, 0);
        this._cal.set(Calendar.MINUTE, 0);
        this._cal.set(Calendar.SECOND, 0);
        this._cal.set(Calendar.MILLISECOND, 0);
        start = this._cal.getTimeInMillis();

        if (this._mode == SINGLE_SELECTION) {
            end = start;
        } else {
            this._cal.setTimeInMillis(end);
            this._cal.set(Calendar.HOUR_OF_DAY, 0);
            this._cal.set(Calendar.MINUTE, 0);
            this._cal.set(Calendar.SECOND, 0);
            this._cal.set(Calendar.MILLISECOND, 0);
            end = this._cal.getTimeInMillis();

            if (this._mode == WEEK_SELECTION) {
                this._cal.setTimeInMillis(start);
                int count = 1;
                while (this._cal.getTimeInMillis() < end) {
                    this._cal.add(Calendar.DAY_OF_MONTH, 1);
                    count++;
                }
                if (true || count > 7) {
                    this._cal.setTimeInMillis(start);
                    int dayOfWeek = this._cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek != getFirstDayOfWeek()) {
                        // Move the start date back to the first day of the
                        // week.
                        int daysFromStart = dayOfWeek - getFirstDayOfWeek();
                        if (daysFromStart < 0) {
                            daysFromStart += DAYS_IN_WEEK;
                        }
                        this._cal.add(Calendar.DAY_OF_YEAR, -daysFromStart);
                        count += daysFromStart;
                        start = this._cal.getTimeInMillis();
                    }
                    // Make sure we have full weeks. Otherwise modify the
                    // end date.

                    int remainder = count % 7;
                    if (remainder != 0) {
                        this._cal.setTimeInMillis(end);
                        this._cal.add(Calendar.DAY_OF_YEAR, (7 - remainder));
                        end = this._cal.getTimeInMillis();
                    }
                }
            }
        }

        this._start = start;
        this._end = end;
        if (selectionLead > selectionAnchor) {
            this._selectionLead = end;
            this._selectionAnchor = start;
        } else {
            this._selectionAnchor = end;
            this._selectionLead = start;
        }
        if ((formerAnchor != getSelectionAnchor()) || (formerLead != getSelectionLead())) {
            fireDaySelectionEvent(new DaySelectionEvent(this, getSelectionAnchor(),
                getSelectionLead()));
        }
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#getSelectionAnchor()
     */
    public long getSelectionAnchor() {
        return this._selectionAnchor;
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#getSelectionLead()
     */
    public long getSelectionLead() {
        return this._selectionLead;
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#setSelectionAnchor(long)
     */
    public void setSelectionAnchor(long time) {
        adjustSelection(time, time);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#setSelectionLead(long)
     */
    public void setSelectionLead(long time) {
        adjustSelection((this._selectionAnchor != NONE) ? this._selectionAnchor : time, time);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#setSelection(long)
     */
    public void setSelection(long time) {
        adjustSelection(time, time);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#setSelection(long, long)
     */
    public void setSelection(long anchor, long lead) {
        adjustSelection(anchor, lead);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#getSelectionMode()
     */
    public int getSelectionMode() {
        return this._mode;
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#setSelectionMode(int)
     */
    public void setSelectionMode(int mode) {
        this._mode = mode;
        adjustSelection(getSelectionAnchor(), getSelectionLead());
    }

    /**
     * @return -
     */
    public final int getFirstDayOfWeek() {
        return this._firstDayOfWeek;
    }

    /**
     * @param firstDayOfWeek -
     */
    public final void setFirstDayOfWeek(int firstDayOfWeek) {
        if ((firstDayOfWeek != Calendar.SUNDAY)
            && (firstDayOfWeek != Calendar.MONDAY)
            && (firstDayOfWeek != Calendar.TUESDAY)
            && (firstDayOfWeek != Calendar.WEDNESDAY)
            && (firstDayOfWeek != Calendar.THURSDAY)
            && (firstDayOfWeek != Calendar.FRIDAY)
            && (firstDayOfWeek != Calendar.SATURDAY)) {

            throw new IllegalArgumentException(firstDayOfWeek + " is not a valid day of week");
        }
        this._firstDayOfWeek = firstDayOfWeek;
        adjustSelection(getSelectionAnchor(), getSelectionLead());
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#addDaySelectionListener(
     *      org.jdesktop.swing.calendar.DaySelectionListener)
     */
    public void addDaySelectionListener(DaySelectionListener listener) {
        this._listeners.add(listener);
    }

    /**
     * @see org.jdesktop.swing.calendar.DaySelectionModel#removeDaySelectionListener(
     *      org.jdesktop.swing.calendar.DaySelectionListener)
     */
    public void removeDaySelectionListener(DaySelectionListener listener) {
        this._listeners.remove(listener);
    }

    /**
     * @param evt
     */
    protected void fireDaySelectionEvent(DaySelectionEvent evt) {
        Iterator it = this._listeners.iterator();
        while (it.hasNext()) {
            DaySelectionListener listener = (DaySelectionListener) it.next();
            listener.selectionChanged(evt);
        }
    }

}