/*
 * $Id: AbstractSliderModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import javax.swing.event.EventListenerList;

import org.jdesktop.swingx.event.SliderChangeEvent;
import org.jdesktop.swingx.event.SliderChangeListener;
import org.jdesktop.swingx.event.*;

/**
 * Defines the abstract portion of the SliderModel. This contains the event handling code only.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractSliderModel implements SliderModel {
    private static final SliderChangeListener[] EMPTY_ARRAY = new SliderChangeListener[0];
    private transient EventListenerList listenerList;

    /**
     * Create a new slider model.
     */
    public AbstractSliderModel() {
        super();
    }





    /**
     * Returns the index of the given MArkerGroup within this model. Return -1 if the MarkerGroup doesn not exist.
     *
     * @param markerGroup The marker group to check for.
     * @return The index of the group in this model.
     */
    public int indexOf(MarkerGroup markerGroup) {
        int result = -1;
        for (int i = 0, n = getMarkerGroupCount(); i < n; i++) {
            if (markerGroup == getMarkerGroup(i)) {
                result = i;
                break;
            }
        }
        return result;
    }





    /**
     * Notify listeners of changes to this model.
     */
    protected void fireSliderRangeChanged() {
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            SliderChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == SliderChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new SliderChangeEvent(this);
                    }
                    ((SliderChangeListener) listeners[i + 1]).sliderRangeChanged(event);
                }
            }
        }
    }





    /**
     * Notify listeners of changes to this model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     */
    protected void fireSliderMarkerGroupAdded(int index0, int index1) {
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            SliderChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == SliderChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_ADDED, index0, index1);
                    }
                    ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupAdded(event);
                }
            }
        }
    }





    /**
     * Notify listeners of changes to this model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     */
    protected void fireSliderMarkerGroupRemoved(int index0, int index1) {
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            SliderChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == SliderChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_REMOVED, index0, index1);
                    }
                    ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupRemoved(event);
                }
            }
        }
    }





    /**
     * Notify listeners of changes to this model.
     *
     * @param index0 The first changed index.
     * @param index1 The last changed index.
     * @param cause The cause of the event.
     */
    protected void fireSliderMarkerGroupChanged(int index0, int index1, MarkerChangeEvent cause) {
        if (listenerList != null) {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            SliderChangeEvent event = null;
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == SliderChangeListener.class) {
                    // Lazily create the event:
                    if (event == null) {
                        event = new SliderChangeEvent(this, SliderChangeEvent.Type.MARKER_GROUP_CHANGED, index0, index1, cause);
                    }
                    ((SliderChangeListener) listeners[i + 1]).sliderMarkerGroupChanged(event);
                }
            }
        }
    }





    public void addSliderChangeListener(SliderChangeListener l) {
        if (l != null) {
            if (listenerList == null) {
                listenerList = new EventListenerList();
            }
            listenerList.add(SliderChangeListener.class, l);
        }
    }





    public void removeSliderChangeListener(SliderChangeListener l) {
        if (l != null && listenerList != null) {
            listenerList.remove(SliderChangeListener.class, l);
        }
    }





    public SliderChangeListener[] getSliderChangeListeners() {
        return listenerList == null ? EMPTY_ARRAY : listenerList.getListeners(SliderChangeListener.class);
    }
}
