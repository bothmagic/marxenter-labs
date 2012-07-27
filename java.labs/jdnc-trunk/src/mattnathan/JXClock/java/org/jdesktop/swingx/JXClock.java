/*
 * $Id: JXClock.java 2634 2008-08-06 09:26:39Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import org.jdesktop.swingx.clock.ClockModel;
import org.jdesktop.swingx.clock.DefaultClockModel;
import org.jdesktop.swingx.event.DynamicObject;
import org.jdesktop.swingx.plaf.ClockUI;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Defines a simple graphical representation of time. This relies on a ClockModel to maintain and notify of time changes
 * which will then be displayed via the installed ClockUI instance. You can create multiple clocks which show the same
 * time under different time zones by setting the same ClockModel on each JXClock and changing the targetTimeZone
 * property to the chosen zone.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXClock extends JXComponent implements DynamicObject {

    private static final String uiClassID = "ClockUI";

    /**
     * Single instance as this object only has a source which doesn't change.
     */
    private transient ChangeEvent changeEvent = null;
    private transient ChangeListener modelAdapter = null;

    private ClockModel model;
    private TimeZone targetTimeZone;
    private Locale targetLocale;

    /**
     * Create a standard JXClock using the current time.
     */
    public JXClock() {
        this( -1);
    }





    /**
     * Create a new JXClock using the given initial time.
     *
     * @param initialValue The initial time.
     */
    public JXClock(long initialValue) {
        super();
        DefaultClockModel model = new DefaultClockModel(initialValue);
        model.start();
        init(model);
    }





    /**
     * Create a new clock using the given ClockModel.
     *
     * @param model The model to get the clocks time from.
     */
    public JXClock(ClockModel model) {
        init(model);
    }





    /**
     * Assigns the model to this component and initialises the ui delegate.
     *
     * @param model The model for this component.
     */
    protected void init(ClockModel model) {
        setModel(model);
        updateUI();
    }





    /**
     * @return {@code "ClockUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * {@inheritDoc}
     */
    @Override
    public ClockUI getUI() {
        return (ClockUI) super.getUI();
    }





    /**
     * Override this method if you don't want to implement the whole ComponentAddon API. Example implementation would be
     * for MyComponent.createUI() you would return BasicMyComponentUI.createUI(this). By default this returns a UI
     * delegate which supports the Painter additions added in this component.
     *
     * @return The fallback UI for this component.
     */
    @Override
    protected ComponentUI createUI() {
        return org.jdesktop.swingx.plaf.basic.BasicClockUI.createUI(this);
    }





    /**
     * Get the time for this clock. This will return time in the currently set TimeZone.
     *
     * @return The current time of this clock.
     * @see #setTargetTimeZone
     */
    public Calendar getTime() {
        return toTargetTimeZone(getModel().getTime());
    }





    /**
     * Converts the given Calendar into the target TimeZone.
     *
     * @param time The current Calendar.
     * @return The resultant Calendar.
     */
    protected Calendar toTargetTimeZone(Calendar time) {
        if (time.getTimeZone() != getTargetTimeZone()) {
            Calendar tmp = Calendar.getInstance(getTargetTimeZone());
            tmp.setTimeInMillis(time.getTimeInMillis());
            time = tmp;
        }
        return time;
    }





    /**
     * Get the model for this component. This should never be null.
     *
     * @return The model for this clock.
     * @see #setModel
     */
    public ClockModel getModel() {
        return model;
    }





    /**
     * Set the model for this component. The model cannot be null.
     *
     * @param model The model for this clock.
     * @throws NullPointerException if the given model is null.
     * @see #getModel
     */
    public void setModel(ClockModel model) {
        if (model == null) {
            throw new NullPointerException("model cannot be null");
        }
        ClockModel old = getModel();
        if (old != null) { // can be null when getModel is overriden or on first call.
            old.removeChangeListener(modelAdapter);
        }
        this.model = model;
        model = getModel();
        if (model != null) {
            if (modelAdapter == null) {
                modelAdapter = new ModelAdapter();
            }
            model.addChangeListener(modelAdapter);
        }
        firePropertyChange("model", old, model);
    }





    /**
     * Get the timezone this clock should interpret its models time under.
     *
     * @return The model timezone.
     */
    public TimeZone getTargetTimeZone() {
        return targetTimeZone == null ? TimeZone.getDefault() : targetTimeZone;
    }





    /**
     * Get the locale this clock should interpret its models time under.
     *
     * @return The model targetLocale
     */
    public Locale getTargetLocale() {
        return targetLocale == null ? Locale.getDefault() : targetLocale;
    }





    /**
     * Set the locale to interpret the models time under.
     *
     * @param targetLocale Locale
     */
    public void setTargetLocale(Locale targetLocale) {
        Locale old = getTargetLocale();
        this.targetLocale = targetLocale;
        firePropertyChange("targetLocale", old, getTargetLocale());
    }





    /**
     * Get the time zone the models time should be interpreted under.
     *
     * @param targetTimeZone The time zone.
     */
    public void setTargetTimeZone(TimeZone targetTimeZone) {
        TimeZone old = getTargetTimeZone();
        this.targetTimeZone = targetTimeZone;
        firePropertyChange("targetTimeZone", old, getTargetTimeZone());
    }





    /**
     * Notify listeners that a change has occurred with the model of this class.
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }





    /**
     * Add a listener for notification of changes to this object.S
     *
     * @param l The listener to add.
     * @see javax.swing.event.EventListenerList#add
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }





    /**
     * Remove a listener added through the addChangeListener method.
     *
     * @param l The change listener to remove
     * @see javax.swing.event.EventListenerList#remove
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }





    /**
     * Get all listeners added to this object.
     *
     * @return The list of change listeners. This will never be null.
     * @see javax.swing.event.EventListenerList#getListeners
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }





    /**
     * Handles the forwarding of events from the model to listeners registered on this class.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ModelAdapter implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }
}
