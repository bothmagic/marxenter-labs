/*
 * $Id: CalendarModel.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public interface CalendarModel {

    /**
     * @param listener
     */
    public void addChangeListener(ChangeListener listener);

    /**
     * @param listener
     */
    public void removeChangeListener(ChangeListener listener);

    /**
     * @return
     */
    public boolean isEnabled();

    /**
     * @param enabled
     */
    public void setEnabled(boolean enabled);

    /**
     * @return
     */
    public long getTime();

    /**
     * @param time
     */
    public void setTime(long time);

    /**
     * @param field
     * @return
     */
    public int get(int field);

    /**
     * @param field
     * @param value
     */
    public void set(int field, int value);
}