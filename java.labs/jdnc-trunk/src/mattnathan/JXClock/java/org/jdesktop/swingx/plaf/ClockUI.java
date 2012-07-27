/*
 * $Id: ClockUI.java 2634 2008-08-06 09:26:39Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXClock;

/**
 * Defines the abstract ui delegate instance for use with the JXClock component.
 *
 * @param <C> The type of clock.
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class ClockUI<C extends JXClock> extends XComponentUI<C> {
    public ClockUI() {
        super("Clock");
    }
}
