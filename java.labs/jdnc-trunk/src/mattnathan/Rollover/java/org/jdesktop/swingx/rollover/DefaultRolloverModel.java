/*
 * $Id: DefaultRolloverModel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.rollover;

import org.jdesktop.swingx.RolloverModel;

import javax.swing.DefaultButtonModel;

/**
 * Provides a default implementation of the RolloverModel interface. This will
 * fire a change event whenever the state of its ARMED, SELECTED, PRESSED or
 * ROLLOVER properties change.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 */
public class DefaultRolloverModel extends DefaultButtonModel implements RolloverModel {
    /**
     * Create a default RolloverModel
     */
    public DefaultRolloverModel() {
        super();
    }
}
