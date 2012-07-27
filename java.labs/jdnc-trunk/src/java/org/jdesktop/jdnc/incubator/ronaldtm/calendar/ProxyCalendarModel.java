/*
 * $Id: ProxyCalendarModel.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ronald Tetsuo Miura
 */
public class ProxyCalendarModel extends DefaultCalendarModel {

    /**
     * Comment for <code>_model</code>
     */
    private CalendarModel _model;

    /**
     * Comment for <code>_modelListener</code>
     */
    private ChangeListener _modelListener = new ModelListener();

    /**
     * @param model
     */
    public ProxyCalendarModel(CalendarModel model) {
        setModel(model);
    }

    /**
     * @return Returns the model.
     */
    public CalendarModel getModel() {
        return this._model;
    }

    /**
     * @param model
     */
    public void setModel(CalendarModel model) {
        if (this._model != null) {
            this._model.removeChangeListener(this._modelListener);
        }

        model.addChangeListener(this._modelListener);
        this._model = model;
        fireChangeEvent(new ChangeEvent(this));
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#addChangeListener(javax.swing.event.ChangeListener)
     */
    public void addChangeListener(ChangeListener listener) {
        super.addChangeListener(listener);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#get(int)
     */
    public int get(int field) {
        CalendarModel model = getModel();
        if (model == null) {
            return -1;
        }
        return getModel().get(field);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#getTime()
     */
    public long getTime() {
        CalendarModel model = getModel();
        if (model == null) {
            return -1;
        }
        return getModel().getTime();
    }

    /**
     * @see org.jdesktop.swing.calendar.DefaultCalendarModel#setTime(long)
     */
    public void setTime(long time) {
        CalendarModel model = getModel();
        if (model == null) {
            return;
        }
        getModel().setTime(time);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#isEnabled()
     */
    public boolean isEnabled() {
        CalendarModel model = getModel();
        if (model == null) {
            return false;
        }
        return getModel().isEnabled();
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#removeChangeListener(javax.swing.event.ChangeListener)
     */
    public void removeChangeListener(ChangeListener listener) {
        super.removeChangeListener(listener);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#set(int, int)
     */
    public void set(int field, int value) {
        CalendarModel model = getModel();
        if (model == null) {
            return;
        }
        getModel().set(field, value);
    }

    /**
     * @see org.jdesktop.swing.calendar.CalendarModel#setEnabled(boolean)
     */
    public void setEnabled(boolean enabled) {
        CalendarModel model = getModel();
        if (model == null) {
            return;
        }
        getModel().setEnabled(enabled);
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ModelListener implements ChangeListener {

        /**
         * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
         */
        public void stateChanged(ChangeEvent e) {
            fireChangeEvent(new ChangeEvent(this));
        }
    }
}