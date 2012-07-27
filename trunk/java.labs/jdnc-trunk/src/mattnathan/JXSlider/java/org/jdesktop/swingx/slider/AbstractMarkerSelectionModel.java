/*
 * $Id: AbstractMarkerSelectionModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.event.AbstractDynamicObject;

/**
 * Defines an abstract selection model for active marker values in a SldierModel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractMarkerSelectionModel extends AbstractDynamicObject implements MarkerSelectionModel {
    protected AbstractMarkerSelectionModel() {
        super();
    }
}
