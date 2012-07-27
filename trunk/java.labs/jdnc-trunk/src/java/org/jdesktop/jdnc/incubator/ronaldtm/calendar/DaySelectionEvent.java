/*
 * $Id: DaySelectionEvent.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.util.EventObject;

/**
 * @author Ronald Tetsuo Miura
 */
public class DaySelectionEvent extends EventObject {

    /** */
    private final long _anchor;

    /** */
    private final long _lead;

    /**
     * @param model the DaySelectionModel that may have changed.
     * @param anchor the first day whose selection may have changed.
     * @param lead the last day whose selection may have changed.
     */
    public DaySelectionEvent(DaySelectionModel model, long anchor, long lead) {
        super(model);
        this._anchor = anchor;
        this._lead = lead;
    }

    /**
     * Returns the DaySelectionModel that may have changed.
     * @return the DaySelectionModel that may have changed.
     */
    public DaySelectionModel getModel() {
        return (DaySelectionModel) getSource();
    }

    /**
     * Returns the first day whose selection may have changed.
     * @return the first day whose selection may have changed.
     */
    public long getAnchor() {
        return this._anchor;
    }

    /**
     * Returns the last day whose selection may have changed.
     * @return the last day whose selection may have changed.
     */
    public long getLead() {
        return this._lead;
    }
}