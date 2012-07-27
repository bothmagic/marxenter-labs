/*
 * $Id: AbstractDynamicObject.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Simple class which provides support for ChangeListener events.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 1.0
 */
public abstract class AbstractDynamicObject implements DynamicObject {

    /**
     * Lazy instance of a listener list.
     */
    private transient EventListenerList listenerList;
    /**
     * Lazy instance of the ChangeEvent.
     */
    private transient ChangeEvent changeEvent = null;

    /**
     * Subclass constructor for use when extending.
     */
    protected AbstractDynamicObject() {
    }





    /**
     * Add a listener for notification of changes to this object.
     *
     * @param l ChangeListener
     */
    public void addChangeListener(ChangeListener l) {
        if (l != null) {
            if (listenerList == null) {
                listenerList = new EventListenerList();
            }
            listenerList.add(ChangeListener.class, l);
        }
    }





    /**
     * Get all listeners added to this object.
     *
     * @return ChangeListener[]
     */
    public ChangeListener[] getChangeListeners() {
        ChangeListener[] result;
        if (listenerList == null) {
            result = new ChangeListener[0];
        } else {
            result = listenerList.getListeners(ChangeListener.class);
        }
        return result;
    }





    /**
     * Remove a listener added through the addChangeListener method.
     *
     * @param l ChangeListener
     */
    public void removeChangeListener(ChangeListener l) {
        if (listenerList != null) {
            listenerList.remove(ChangeListener.class, l);
        }
    }





    /**
     * Call this method when a change occurs that requires notification.
     */
    protected void fireStateChanged() {
        // Guaranteed to return a non-null array
        if (listenerList != null) {
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this changeEvent
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ChangeListener.class) {
                    // Lazily create the changeEvent:
                    if (changeEvent == null) {
                        changeEvent = createChangeEvent();
                    }
                    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
                }
            }
        }
    }





    /**
     * Hook to allow changing of the creation of the event. This is designed to
     * allow subclasses to provide different sources.
     *
     * @return ChangeEvent
     */
    protected ChangeEvent createChangeEvent() {
        return new ChangeEvent(this);
    }





    /**
     * Class which provides public access to protected methods for classes which
     * cannot extend AbstractDynamicObject for some reason.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static class Delegate extends AbstractDynamicObject {
        /**
         * The source object for this dynamic object
         */
        private Object source;

        /**
         * Create a new Dynamic adapter for the given source.
         *
         * @param source Object
         */
        public Delegate(Object source) {
            if (source == null) {
                throw new IllegalArgumentException("source cannot be null");
            }
            this.source = source;
        }





        /**
         * Overriden to promote the access to be public.
         */
        @Override
        public void fireStateChanged() {
            super.fireStateChanged();
        }





        /**
         * Overriden to provide a different source to the event.
         *
         * @return ChangeEvent
         */
        @Override
        protected ChangeEvent createChangeEvent() {
            return new ChangeEvent(source);
        }
    }
}
