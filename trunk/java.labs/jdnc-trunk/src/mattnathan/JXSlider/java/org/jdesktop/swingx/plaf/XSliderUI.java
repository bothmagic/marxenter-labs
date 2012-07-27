/*
 * $Id: XSliderUI.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXSlider;

/**
 * Provides the required methods for UI delegates who represent JXSliders.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see JXSlider
 */
public abstract class XSliderUI extends XComponentUI<JXSlider> {
    protected XSliderUI() {
        super("XSlider");
    }
}
