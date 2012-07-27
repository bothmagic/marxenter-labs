/*
 * $Id: DefaultCalendarModel.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public class DefaultCalendarModel implements CalendarModel {

    /** */
    private Set _changeListeners = new HashSet();

    /** */
    private boolean _enabled = true;

    /** */
    private Calendar _calendar = Calendar.getInstance();

    /**
     */
    public DefaultCalendarModel() {
        this(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * @param timeInMillis
     */
    public DefaultCalendarModel(long timeInMillis) {
        setTime(timeInMillis);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#addChangeListener(javax.swing.event.ChangeListener)
     */
    public void addChangeListener(ChangeListener listener) {
        this._changeListeners.add(listener);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#removeChangeListener(javax.swing.event.ChangeListener)
     */
    public void removeChangeListener(ChangeListener listener) {
        this._changeListeners.remove(listener);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#isEnabled()
     */
    public boolean isEnabled() {
        return this._enabled;
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        this._enabled = enabled;
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#getTime()
     */
    public long getTime() {
        return this._calendar.getTimeInMillis();
    }

    /**
     * @param millis
     */
    public void setTime(long millis) {
        if (this._calendar.getTimeInMillis() != millis) {
            this._calendar.setTimeInMillis(millis);
            fireChangeEvent(new ChangeEvent(this));
        }
    }

    /**
     * @param evt -
     */
    protected void fireChangeEvent(ChangeEvent evt) {
        Iterator it = this._changeListeners.iterator();
        while (it.hasNext()) {
            ChangeListener listener = (ChangeListener) it.next();
            listener.stateChanged(evt);
        }
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#get(int)
     */
    public int get(int field) {
        return this._calendar.get(field);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#set(int, int)
     */
    public void set(int field, int value) {
        if ((field == Calendar.YEAR) && (value <= 0)) {
            fireChangeEvent(new ChangeEvent(this));
        } else if (this._calendar.get(field) != value) {
            this._calendar.set(field, value);
            fireChangeEvent(new ChangeEvent(this));
        }
    }
}