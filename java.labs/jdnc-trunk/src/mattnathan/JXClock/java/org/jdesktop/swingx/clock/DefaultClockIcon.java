/*
 * $Id: DefaultClockIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Combines the painting code for the DefaultClockPainter with the dimension of this icon.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultClockIcon extends PainterIcon<JXClock> {
    public DefaultClockIcon() {
        super(48, 48, new DefaultClockPainter());
        setScalePolicy(ScalePolicy.NONE); // doing this will use Java2D Graphics scaling to scale the icon.
    }
}
