/*
 * $Id: ConsoleCalendar.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public class ConsoleCalendar implements ChangeListener {

    /**
     * Comment for <code>_model</code>
     */
    private CalendarModel _model;

    /**
     */
    public ConsoleCalendar() {
        setModel(new DefaultCalendarModel());
    }

    /**
     * @param time -
     */
    public ConsoleCalendar(long time) {
        setModel(new DefaultCalendarModel(time));
    }

    /**
     * @param model
     */
    public ConsoleCalendar(CalendarModel model) {
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
        if (this._model != null) {
            this._model.removeChangeListener(this);
        }
        this._model = model;
        this._model.addChangeListener(this);
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //$NON-NLS-1$
        System.out.println(df.format(new Date(getModel().getTime())));
    }
}