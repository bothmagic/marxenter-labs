/*
 * $Id: AbstractMarkerGroup.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.util.ConcurrentModificationException;

import org.jdesktop.swingx.event.AbstractDynamicObject;
import javax.swing.event.*;
import org.jdesktop.swingx.event.*;

/**
 * Defines the abstract portion of the MarkerGroup interface. This takes the event listener support from
 * AbstractDynamicObject.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractMarkerGroup implements MarkerGroup {

    protected static final MarkerRange EMPTY_RANGE = new EmptyMarkerRange();
    private static final MarkerChangeListener[] EMPTY_ARRAY = {};

    private EventListenerList listenerList;

    /**
     * Keeps track of the number of times fireStateChanged() has been called. If a change is made that does not call
     * this method then the modification count should be incremented manually.
     */
    protected int modificationCount = 0;

    /**
     * When non-null contains the value that is currently being adjusted.
     */
    protected MLong valueAdjustingMarker = null;

    /**
     * Create a new marker group.
     */
    public AbstractMarkerGroup() {
        super();
    }





    /**
     * Returns whether the given value is currently being adjusted.
     *
     * @param value The value to check for change.
     * @return {@code true} if the given value is being adjusted.
     */
    public boolean isValueAdjusting(long value) {
        return valueAdjustingMarker != null && valueAdjustingMarker.get() == value;
    }





    /**
     * Set whether the given value is the one currently being adjusted.
     *
     * @param value The value to mark as being adjusted.
     * @param valueAdjusting Whether the value is being adjusted or not.
     */
    public void setValueAdjusting(long value, boolean valueAdjusting) {
        if (valueAdjusting) {
            if (valueAdjustingMarker != null) {
                valueAdjustingMarker.set(value);
            } else {
                valueAdjustingMarker = new MLong(value);
            }
        } else {
            if (valueAdjustingMarker != null && valueAdjustingMarker.get() == value) {
                valueAdjustingMarker = null;
            }
        }
    }





    /**
     * Fires a value change and updates the valueAdjusting value at the same time.
     *
     * @param oldValue The old value
     * @param newValue The new value
     */
    protected void fireMarkerChanged(long oldValue, long newValue) {
        boolean b = isValueAdjusting(oldValue);
        if (b) {
            setValueAdjusting(newValue, true);
        }
        fireMarkerChanged(oldValue, newValue, b);
    }





    /**
     * Fires a marker changed event with the given properties.
     *
     * @param oldValue The old value.
     * @param newValue The new value.
     * @param valueAdjusting {@code true} if the value is still adjusting.
     */
    protected void fireMarkerChanged(long oldValue, long newValue, boolean valueAdjusting) {
        ++modificationCount;
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            MarkerChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MarkerChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new MarkerChangeEvent(this, oldValue, newValue, valueAdjusting);
                    }
                    ((MarkerChangeListener) listeners[i + 1]).markerChanged(event);
                }
            }
        }
    }





    /**
     * Fires a value change and updates the valueAdjusting value at the same time.
     *
     * @param value The old value
     */
    protected void fireMarkerRemoved(long value) {
        boolean b = isValueAdjusting(value);
        if (b) {
            setValueAdjusting(value, false);
        }
        fireMarkerRemoved(value, b);
    }





    /**
     * Fires a marker changed event with the given properties.
     *
     * @param value The old value.
     * @param valueAdjusting {@code true} if the value is still adjusting.
     */
    protected void fireMarkerRemoved(long value, boolean valueAdjusting) {
        ++modificationCount;
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            MarkerChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MarkerChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new MarkerChangeEvent(this, value, valueAdjusting, MarkerChangeEvent.Type.MARKER_REMOVED);
                    }
                    ((MarkerChangeListener) listeners[i + 1]).markerRemoved(event);
                }
            }
        }
    }





    /**
     * Fires a marker changed event with the given properties.
     *
     * @param value The old value.
     * @param valueAdjusting {@code true} if the value is still adjusting.
     */
    protected void fireMarkerAdded(long value, boolean valueAdjusting) {
        ++modificationCount;
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            MarkerChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == MarkerChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new MarkerChangeEvent(this, value, valueAdjusting, MarkerChangeEvent.Type.MARKER_ADDED);
                    }
                    ((MarkerChangeListener) listeners[i + 1]).markerAdded(event);
                }
            }
        }
    }





    /**
     * {@inheritDoc}
     */
    public void addMarkerChangeListener(MarkerChangeListener l) {
        if (l != null) {
            if (listenerList == null) {
                listenerList = new EventListenerList();
            }
            listenerList.add(MarkerChangeListener.class, l);
        }
    }





    /**
     * {@inheritDoc}
     */
    public void removeMarkerChangeListener(MarkerChangeListener l) {
        if (listenerList != null) {
            listenerList.remove(MarkerChangeListener.class, l);
        }
    }





    /**
     * {@inheritDoc}
     */
    public MarkerChangeListener[] getMarkerChangeListeners() {
        return listenerList == null ? EMPTY_ARRAY : listenerList.getListeners(MarkerChangeListener.class);
    }





    /**
     * Wraps the given markerRange in a FailFastMarkerRange which will throw an exception if accessed after a change has
     * been made in this marker group.
     *
     * @param markerRange The range to make safe.
     * @return The safe marker range.
     */
    protected MarkerRange makeSafe(MarkerRange markerRange) {
        if (markerRange == null) {
            throw new NullPointerException("markerRange cannot be null");
        }
        return new FailFastMarkerRange(markerRange);
    }





    /**
     * Simple instance of MarkerRange which contains no results. Invalidate this object when the containing group
     * changes.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected static class EmptyMarkerRange implements MarkerRange {
        /**
         * Always return 0.
         *
         * @return 0.
         */
        public int getSize() {
            return 0;
        }





        /**
         * Always throws an IndexOutOfBoundsException as the size is always 0.
         *
         * @param index the index to return
         * @return nothing as an exception is always thrown.
         */
        public long get(int index) {
            throw new IndexOutOfBoundsException(index + " out of range 0");
        }
    }







    /**
     * MarkerRange which can be used to ensure concurrent modification of a MarkerGroup cannot happen.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected class FailFastMarkerRange implements MarkerRange {
        /**
         * The range to delegate to.
         */
        private MarkerRange delegate;
        /**
         * The modification count of the enclosing object at this objects creation time.
         */
        private final int modificationCount;

        /**
         * Wrap the given range so that it becomes safe with respect to concurrent modifications.
         *
         * @param delegate The range to wrap.
         */
        public FailFastMarkerRange(MarkerRange delegate) {
            this.delegate = delegate;
            this.modificationCount = AbstractMarkerGroup.this.modificationCount;
        }





        public int getSize() {
            checkSafety();
            return delegate.getSize();
        }





        public long get(int index) {
            checkSafety();
            return delegate.get(index);
        }





        /**
         * Checks the modification count of this against that of the enclosing class. If they differ an exception is
         * thrown.
         */
        protected void checkSafety() {
            if (modificationCount != AbstractMarkerGroup.this.modificationCount) {
                throw new ConcurrentModificationException("Cannot access a MarkerRange after MarkerGroup modification");
            }
        }
    }







    /**
     * Defines a simple nullable long value.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private static final class MLong {
        public long value;
        public MLong(long value) {
            this.value = value;
        }





        public long get() {
            return value;
        }





        public void set(long value) {
            this.value = value;
        }
    }
}
