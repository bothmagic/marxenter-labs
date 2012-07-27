/*
 * $Id: JXStopwatch.java 2634 2008-08-06 09:26:39Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.util.Calendar;
import java.util.TimeZone;

import org.jdesktop.swingx.clock.ClockModel;
import org.jdesktop.swingx.clock.DefaultStopwatchModel;
import org.jdesktop.swingx.clock.StopwatchModel;
import javax.swing.plaf.ComponentUI;

/**
 * Represents a stopwatch. This class provides, via its model, functionality to start timing an event. Once an event is
 * being timed moments can be marked for future reference, this allows multiple events to be measured at the same time.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXStopwatch extends JXClock {
    private static final String uiClassID = "StopwatchUI";

    /**
     * Create a new stopwatch model.
     */
    public JXStopwatch() {
        this(new DefaultStopwatchModel());
    }





    /**
     * Create a new Stopwatch with the given model.
     *
     * @param model The stopwatch model.
     */
    public JXStopwatch(StopwatchModel model) {
        super(model);
    }





    /**
     * {@inheritDoc}
     */
    @Override
    public StopwatchModel getModel() {
        return (StopwatchModel)super.getModel();
    }





    /**
     * @return {@code "StopwatchUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
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
        return org.jdesktop.swingx.plaf.basic.BasicStopwatchUI.createUI(this);
    }





    /**
     * Set the model for this stopwatch.
     *
     * @param model The model.
     */
    public void setModel(StopwatchModel model) {
        super.setModel(model);
    }





    /**
     * Ensures that the set model is an instance of StopwatchModel.
     *
     * @param model An instance of StopwatchModel
     * @see #setModel(StopwatchModel)
     */
    @Override
    public void setModel(ClockModel model) {
        if (!(model instanceof StopwatchModel)) {
            throw new ClassCastException("The model for JXStopwatch should be a StopwatchModel: " + model);
        }
        setModel((StopwatchModel) model);
    }





    /**
     * Returns the GMT TimeZone as a stopwatch is purely relational time.
     *
     * @return The model timezone.
     */
    @Override
    public TimeZone getTargetTimeZone() {
        return TimeZone.getTimeZone("GMT");
    }





    /**
     * Delegate method java.util.Calendar getMarker(int index) to getModel() : StopwatchModel
     *
     * @param index The index of the marker
     * @return The marker. This will be in the current target TimeZone.
     */
    public Calendar getMarker(int index) {
        return toTargetTimeZone(getModel().getMarker(index));
    }





    /**
     * Delegate method int getMarkerCount() to getModel() : StopwatchModel
     *
     * @return The number of markers.
     */
    public int getMarkerCount() {
        return getModel().getMarkerCount();
    }





    /**
     * Delegate method boolean isRunning() to getModel() : StopwatchModel
     *
     * @return {@code true} if the stopwatch is running.
     */
    public boolean isRunning() {
        return getModel().isRunning();
    }





    /**
     * Delegate method void mark(boolean stopTimer) to getModel() : StopwatchModel
     *
     * @param stopTimer {@code true} if the stopwatch should also stop running.
     */
    public void mark(boolean stopTimer) {
        getModel().mark(stopTimer);
    }





    /**
     * Delegate method void reset() to getModel() : StopwatchModel
     */
    public void reset() {
        getModel().reset();
    }





    /**
     * Delegate method void start() to getModel() : StopwatchModel
     */
    public void start() {
        getModel().start();
    }

}
