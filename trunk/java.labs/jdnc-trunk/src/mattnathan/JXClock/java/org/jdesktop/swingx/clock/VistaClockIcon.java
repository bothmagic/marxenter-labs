/*
 * $Id: VistaClockIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines a clock icon under the vista look and feel. This has no vista specific code so can be used under any look and
 * feel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class VistaClockIcon extends PainterIcon<JXClock> {
    public VistaClockIcon() {
        super(124, 124, new VistaClockPainter());
        setScalePolicy(ScalePolicy.NONE);
    }
}
