/*
 * $Id: MarkerChangeListener.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import java.util.EventListener;

/**
 * Defines an Observer type for notifications of changes to values in a MarkerGroup. There is a special case for events
 * fired where MarkerChangeEvent.isUniversalEvent is true, these cases signify that all markers for the source
 * MarkerGroup have been affected by the event. For example if markerChanged is called and universalEvent is true is
 * shows that all markers have changed; similarly for removed and added markers.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface MarkerChangeListener extends EventListener {
    /**
     * Called when a value has changed within a marker group.
     *
     * @param e The event fired.
     */
    void markerChanged(MarkerChangeEvent e);





    /**
     * Called when a marker has been added to a marker group.
     *
     * @param e The event fired.
     */
    void markerAdded(MarkerChangeEvent e);





    /**
     * Called when a marker has been removed from a marker group.
     *
     * @param e The event fired.
     */
    void markerRemoved(MarkerChangeEvent e);
}
