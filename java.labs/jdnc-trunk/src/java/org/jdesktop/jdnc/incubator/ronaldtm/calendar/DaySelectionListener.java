/*
 * $Id: DaySelectionListener.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.util.EventListener;

/**
 * The listener that's notified when a lists selection value changes.
 * @author Ronald Tetsuo Miura
 */
public interface DaySelectionListener extends EventListener {
    /**
     * Called whenever the value of the selection changes.
     * @param evt the event that characterizes the change.
     */
    public void selectionChanged(DaySelectionEvent evt);
}