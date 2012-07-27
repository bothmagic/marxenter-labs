/*
 * $Id: JXTimeline.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import org.jdesktop.swingx.timeline.*;
import javax.swing.event.*;
import org.jdesktop.swingx.plaf.*;
import java.awt.*;

/**
 * Represents a timeline, a line from one date to another with a selected view.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXTimeline extends JXComponent {

    private static final String uiClassID = "TimelineUI";

    static {
        LookAndFeelAddons.contribute(new JXTimelineAddon());
    }





    /**
     * Defines the orientation values possible for this timeline.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 1.0
     */
    public static enum Orientation {
        /**
         * Horizontal orientation.
         */
        HORIZONTAL,
        /**
         * Vertical orientation.
         */
        VERTICAL
    }







    /**
     * Only one <code>ChangeEvent</code> is needed per model instance since the
     * event's only (read-only) state is the source property.  The source
     * of events generated here is always "this".
     */
    protected transient ChangeEvent changeEvent = null;

    /**
     * Forwards events from this components models to observers on this component for the corresponding events.
     */
    private ChangeListener changeListener;

    /**
     * The model for this component.
     */
    private TimelineModel model;

    /**
     * Used to restrict the view of the timeline.
     */
    private ViewRange viewRange;

    /**
     * The orientation of this component.
     */
    private Orientation orientation = Orientation.HORIZONTAL;

    /**
     * Creates a new timeline with a default model.
     */
    public JXTimeline() {
        this(new DefaultTimelineModel());
    }





    /**
     * Creates a new timeline using the given boundary values.
     *
     * @param value The value.
     * @param extent The extent.
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     */
    public JXTimeline(long value, long extent, long minimum, long maximum) {
        this(new DefaultTimelineModel(value, extent, minimum, maximum));
    }





    /**
     * Create a new timeline with the given model.
     *
     * @param model TimelineModel
     */
    public JXTimeline(TimelineModel model) {
        setModel(model);
        viewRange = IdentityViewRange.getInstance();
        updateUI();
    }





// *****************************************************************************
// **
// ** Bean property values
// **
// *****************************************************************************
    /**
     * Get the orientation for this timeline. This will not return null.
     *
     * @return The orientation for the timeline.
     * @see #setOrientation
     */
    public Orientation getOrientation() {
        assert orientation != null;
        return orientation;
    }





    /**
     * Set the orientation for this timeline. This cannot be set to null.
     *
     * @param orientation The orientation for the timeline.
     * @throws NullPointerException if the given value is null.
     * @see #getOrientation
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            throw new NullPointerException("orientation cannot be null");
        }
        Orientation old = getOrientation();
        this.orientation = orientation;
        firePropertyChange("orientation", old, getOrientation());
    }





    /**
     * Get the object used to determine the visible portion of the timeline. This will never return null.
     *
     * @return The view range.
     * @see #setViewRange
     */
    public ViewRange getViewRange() {
        assert viewRange != null;
        return viewRange;
    }





    /**
     * Set the range for the visible timeline. This can be set to null and in this case will act as though the view is
     * set to an instance of IdentityViewRange.
     *
     * @param viewRange The range for the view.
     * @see #getViewRange
     * @see IdentityViewRange
     */
    public void setViewRange(ViewRange viewRange) {
        if (viewRange == null) {
            viewRange = IdentityViewRange.getInstance();
        }
        ViewRange old = getViewRange();
        this.viewRange = viewRange;
        firePropertyChange("viewRange", old, getViewRange());
    }





// *****************************************************************************
// **
// ** Data model support
// **
// *****************************************************************************


    /**
     * Get the model used by this timeline.
     *
     * @return The timeline model.
     */
    public TimelineModel getModel() {
        return model;
    }





    /**
     * Set the model to use for this timeline.
     *
     * @param model The timeline model.
     */
    public void setModel(TimelineModel model) {
        TimelineModel old = getModel();

        if (model != old) {
            if (old != null) {
                old.removeChangeListener(changeListener);
            }

            this.model = model;

            model = getModel();
            if (model != null) {
                if (changeListener == null) {
                    changeListener = createChangeListener();
                }
                model.addChangeListener(changeListener);
            }
            firePropertyChange("model", old, model);
        }
    }





    /**
     * Delegate method long getExtent() to getModel() : TimelineModel
     *
     * @return long
     */
    public long getExtent() {
        return getModel().getExtent();
    }





    /**
     * Delegate method long getMaximum() to getModel() : TimelineModel
     *
     * @return long
     */
    public long getMaximum() {
        return getModel().getMaximum();
    }





    /**
     * Delegate method long getMinimum() to getModel() : TimelineModel
     *
     * @return long
     */
    public long getMinimum() {
        return getModel().getMinimum();
    }





    /**
     * Delegate method long getValue() to getModel() : TimelineModel
     *
     * @return long
     */
    public long getValue() {
        return getModel().getValue();
    }





    /**
     * Delegate method boolean getValueIsAdjusting() to getModel() : TimelineModel
     *
     * @return boolean
     */
    public boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }





    /**
     * Delegate method void setExtent(long newExtent) to getModel() : TimelineModel
     *
     * @param newExtent long
     */
    public void setExtent(long newExtent) {
        getModel().setExtent(newExtent);
    }





    /**
     * Delegate method void setMaximum(long newMaximum) to getModel() : TimelineModel
     *
     * @param newMaximum long
     */
    public void setMaximum(long newMaximum) {
        getModel().setMaximum(newMaximum);
    }





    /**
     * Delegate method void setMinimum(long newMinimum) to getModel() : TimelineModel
     *
     * @param newMinimum long
     */
    public void setMinimum(long newMinimum) {
        getModel().setMinimum(newMinimum);
    }





    /**
     * Delegate method void setRangeProperties(long value, long extent, long min, long max, boolean adjusting) to
     * getModel() : TimelineModel
     *
     * @param value long
     * @param extent long
     * @param min long
     * @param max long
     * @param adjusting boolean
     */
    public void setRangeProperties(long value, long extent, long min, long max, boolean adjusting) {
        getModel().setRangeProperties(value, extent, min, max, adjusting);
    }





    /**
     * Delegate method void setValue(long newValue) to getModel() : TimelineModel
     *
     * @param newValue long
     */
    public void setValue(long newValue) {
        getModel().setValue(newValue);
        repaint();
    }





    /**
     * Delegate method void setValueIsAdjusting(boolean b) to getModel() : TimelineModel
     *
     * @param b boolean
     */
    public void setValueIsAdjusting(boolean b) {
        getModel().setValueIsAdjusting(b);
    }





// *****************************************************************************
// **
// ** UI delegate utilities
// **
// *****************************************************************************

    /**
     * Find the timestamp that maps closest to the given point.
     *
     * @param location The point to find a timestamp close to.
     * @return The timestamp closest to the given point.
     * @see TimelineUI#locationToTimestamp
     */
    public long locationToTimestamp(Point location) {
        return getUI().locationToTimestamp(this, location);
    }





    /**
     * Find the timestamp closest to the given point.
     *
     * @param x The x coordinate of the point.
     * @param y The y coordinate of the point.
     * @return The timestamp closest to the given points.
     * @see #locationToTimestamp(Point)
     * @see TimelineUI#locationToTimestamp
     */
    public long locationToTimestamp(int x, int y) {
        return locationToTimestamp(new Point(x, y));
    }





    /**
     * Gets the bounds surrounding the given timestamp. If the given timestamp is outside this timeline's model then null
     * is returned. If the given timestamp is outside the timeline's ViewBounds then the width and height will be equal
     * to 0 - their original values. The width and height will never be 0.
     *
     * @param timestamp The timestamp to find the bounds of.
     * @return The rectangle containing the given timestamp.
     * @see TimelineUI#getTimestampBounds
     */
    public Rectangle getTimestampBounds(long timestamp) {
        return getUI().getTimestampBounds(this, timestamp);
    }





// *****************************************************************************
// **
// ** Listener Support
// **
// *****************************************************************************


    /**
     * Create a change listener to forward change events to observers on this component.
     *
     * @return The change listener.
     */
    protected ChangeListener createChangeListener() {
        return new ModelBinding();
    }





    /**
     * Adds a <code>ChangeListener</code>. The change listeners are notified each
     * time any one of the model properties changes.
     *
     * @param l the ChangeListener to add
     * @see #removeChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }





    /**
     * Removes a <code>ChangeListener</code>.
     *
     * @param l the <code>ChangeListener</code> to remove
     * @see #addChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }





    /**
     * Returns an array of all the change listeners
     * registered on this <code>DefaultBoundedRangeModel</code>.
     *
     * @return all of this model's <code>ChangeListener</code>s
     *   or an empty array if no change listeners are currently
     *   registered
     *
     * @see #addChangeListener
     * @see #removeChangeListener
     */
    public ChangeListener[] getChangeListeners() {
        return listenerList.getListeners(ChangeListener.class);
    }





    /**
     * Runs each <code>ChangeListener</code>'s <code>stateChanged</code> method.
     *
     * @see #setRangeProperties
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }





// *****************************************************************************
// **
// ** UI Delegate support
// **
// *****************************************************************************

    /**
     * @return {@code "TimelineUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * Overriden to focus the return argument.
     *
     * @return The ui for this component.
     */
    @Override
    public TimelineUI getUI() {
        return (TimelineUI)super.getUI();
    }





    /**
     * Forwards change events from the model to observers on the containing class.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ModelBinding implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }
}
