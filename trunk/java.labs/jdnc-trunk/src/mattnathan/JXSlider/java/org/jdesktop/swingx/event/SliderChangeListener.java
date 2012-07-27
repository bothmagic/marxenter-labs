/*
 * $Id: SliderChangeListener.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import java.util.EventListener;

/**
 * Defines an observer for the SliderModel. This provides notification of changes to the model range and the addition,
 * removal and change of the MarkerGroups on that model. The types of the events passed to each of the methods of this
 * interface will be that suggested by the method names.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see SliderChangeEvent
 * @see org.jdesktop.swingx.slider.SliderModel
 */
public interface SliderChangeListener extends EventListener {
    /**
     * Called when the range, minimum and maximum, values of the model change.
     *
     * @param e The event describing the change.
     */
    public void sliderRangeChanged(SliderChangeEvent e);





    /**
     * Called when a MarkerGroup has been added to the model.
     *
     * @param e The event describing the change.
     */
    public void sliderMarkerGroupAdded(SliderChangeEvent e);





    /**
     * Called when a MarkerGroup has been removed from the model.
     *
     * @param e The event describing the change.
     */
    public void sliderMarkerGroupRemoved(SliderChangeEvent e);





    /**
     * Called when a MarkerGroup has changed its internal values.
     *
     * @param e The event describing the change.
     */
    public void sliderMarkerGroupChanged(SliderChangeEvent e);
}
