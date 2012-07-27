/*
 * $Id: TimelineModel.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.timeline;

import javax.swing.event.ChangeListener;

/**
 * Defines a model for a timeline. This represents a time bound and range representing the current view. This model is
 * equivalent to a {@link javax.swing.BoundedRangeModel} but uses longs to represent its values instead of ints. The
 * long values of this models properties represent milliseconds since the epoch as defined by System.currentTimeMillis
 * and used by Date. The reason Date is not used is to avoid the overhead of cloning Date objects as the Date object is
 * mutable.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see javax.swing.BoundedRangeModel
 * @see java.lang.System#currentTimeMillis
 * @see java.util.Date
 */
public interface TimelineModel {
    /**
     * Returns the minimum acceptable value.
     *
     * @return the value of the minimum property
     * @see #setMinimum
     */
    long getMinimum();





    /**
     * Sets the model's minimum to <I>newMinimum</I>.   The
     * other three properties may be changed as well, to ensure
     * that:
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newMinimum the model's new minimum
     * @see #getMinimum
     * @see #addChangeListener
     */
    void setMinimum(long newMinimum);





    /**
     * Returns the model's maximum.  Note that the upper
     * limit on the model's value is (maximum - extent).
     *
     * @return the value of the maximum property.
     * @see #setMaximum
     * @see #setExtent
     */
    long getMaximum();





    /**
     * Sets the model's maximum to <I>newMaximum</I>. The other
     * three properties may be changed as well, to ensure that
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newMaximum the model's new maximum
     * @see #getMaximum
     * @see #addChangeListener
     */
    void setMaximum(long newMaximum);





    /**
     * Returns the model's current value.  Note that the upper
     * limit on the model's value is <code>maximum - extent</code>
     * and the lower limit is <code>minimum</code>.
     *
     * @return  the model's value
     * @see     #setValue
     */
    long getValue();





    /**
     * Sets the model's current value to <code>newValue</code> if <code>newValue</code>
     * satisfies the model's constraints. Those constraints are:
     * <pre>
     * minimum <= value <= value+extent <= maximum
     * </pre>
     * Otherwise, if <code>newValue</code> is less than <code>minimum</code>
     * it's set to <code>minimum</code>, if its greater than
     * <code>maximum</code> then it's set to <code>maximum</code>, and
     * if it's greater than <code>value+extent</code> then it's set to
     * <code>value+extent</code>.
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param newValue the model's new value
     * @see #getValue
     */
    void setValue(long newValue);





    /**
     * This attribute indicates that any up-coming changes to the value
     * of the model should be considered a single event. This attribute
     * will be set to true at the start of a series of changes to the value,
     * and will be set to false when the value has finished changing.  Normally
     * this allows a listener to only take action when the final value change in
     * committed, instead of having to do updates for all intermediate values.
     *
     * @param b true if the up-coming changes to the value property are part of a series
     */
    void setValueIsAdjusting(boolean b);





    /**
     * Returns true if the current changes to the value property are part
     * of a series of changes.
     *
     * @return the valueIsAdjustingProperty.
     * @see #setValueIsAdjusting
     */
    boolean getValueIsAdjusting();





    /**
     * Returns the model's extent, the length of the inner range that
     * begins at the model's value.
     *
     * @return  the value of the model's extent property
     * @see     #setExtent
     * @see     #setValue
     */
    long getExtent();





    /**
     * Sets the model's extent.  The <I>newExtent</I> is forced to
     * be greater than or equal to zero and less than or equal to
     * maximum - value.
     * <p>
     * The extent represents the size of the view.
     * <p>
     * Notifies any listeners if the model changes.
     *
     * @param  newExtent the model's new extent
     * @see #getExtent
     * @see #setValue
     */
    void setExtent(long newExtent);





    /**
     * This method sets all of the model's data with a single method call.
     * The method results in a single change event being generated. This is
     * convenient when you need to adjust all the model data simultaneously and
     * do not want individual change events to occur.
     *
     * @param value     a long giving the current value
     * @param extent    a long giving the amount by which the value can "jump"
     * @param min       a long giving the minimum value
     * @param max       a long giving the maximum value
     * @param adjusting a boolean, true if a series of changes are in
     *                  progress
     *
     * @see #setValue
     * @see #setExtent
     * @see #setMinimum
     * @see #setMaximum
     * @see #setValueIsAdjusting
     */
    void setRangeProperties(long value, long extent, long min, long max, boolean adjusting);





    /**
     * Adds a ChangeListener to the model's listener list.
     *
     * @param x the ChangeListener to add
     * @see #removeChangeListener
     */
    void addChangeListener(ChangeListener x);





    /**
     * Removes a ChangeListener from the model's listener list.
     *
     * @param x the ChangeListener to remove
     * @see #addChangeListener
     */
    void removeChangeListener(ChangeListener x);
}
