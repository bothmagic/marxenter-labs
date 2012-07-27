/*
 * $Id: DigitalClockIcon.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines the size and painter for displaying a clock in digital form.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DigitalClockIcon extends PainterIcon<JXClock> {
    public DigitalClockIcon() {
        super(73, 46, new DigitalClockPainter());
        setScalePolicy(ScalePolicy.NONE);
    }
}
