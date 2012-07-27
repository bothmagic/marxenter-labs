/*
 * $Id: VistaStopwatchIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import org.jdesktop.swingx.JXStopwatch;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Provides a vista style stopwatch icon.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class VistaStopwatchIcon extends PainterIcon<JXStopwatch> {
    public VistaStopwatchIcon() {
        super(124, 124, new VistaStopwatchPainter());
        setScalePolicy(ScalePolicy.NONE);
    }
}
