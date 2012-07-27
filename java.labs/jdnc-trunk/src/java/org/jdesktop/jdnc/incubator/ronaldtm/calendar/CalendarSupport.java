/*
 * $Id: CalendarSupport.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
final class CalendarSupport {

    /** */
    private CalendarModel _model;

    /** */
    private ChangeListener _changeListener;

    /**
     * @param model
     * @param changeListener
     */
    public CalendarSupport(CalendarModel model, ChangeListener changeListener) {

        this._changeListener = changeListener;
        setModel(model);
    }

    /**
     * @return Returns the model.
     */
    public CalendarModel getModel() {
        return this._model;
    }

    /**
     * @param model The model to set.
     */
    public void setModel(CalendarModel model) {
        if (getModel() != null) {
            getModel().removeChangeListener(this._changeListener);
        }
        this._model = model;
        model.addChangeListener(this._changeListener);
    }
}