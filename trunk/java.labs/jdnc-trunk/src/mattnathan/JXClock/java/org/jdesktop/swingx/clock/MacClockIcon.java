/*
 * $Id: MacClockIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines a clock that looks like the clock in Mac OS X Dashboard.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MacClockIcon extends PainterIcon<JXClock> {
    public MacClockIcon() {
        super(145, 145, new MacClockPainter());
        setScalePolicy(ScalePolicy.NONE);
    }
}
