/*
 * $Id: DaySelectionSupport.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

/**
 * @author Ronald Tetsuo Miura
 */
final class DaySelectionSupport {

    /** */
    private DaySelectionModel _model;

    /** */
    private DaySelectionListener _listener;

    /**
     * @param model
     * @param listener
     */
    public DaySelectionSupport(DaySelectionModel model, DaySelectionListener listener) {
        this._listener = listener;
        model.addDaySelectionListener(listener);
        setModel(model);
    }

    /**
     * @return Returns the model.
     */
    public DaySelectionModel getModel() {
        return this._model;
    }

    /**
     * @param model The model to set.
     */
    public void setModel(DaySelectionModel model) {
        if (getModel() != null) {
            getModel().removeDaySelectionListener(this._listener);
        }
        this._model = model;
        model.addDaySelectionListener(this._listener);
    }
}