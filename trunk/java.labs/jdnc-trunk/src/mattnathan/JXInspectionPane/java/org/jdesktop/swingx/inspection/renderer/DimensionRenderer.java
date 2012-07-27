/*
 * $Id: DimensionRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection.renderer;

import java.awt.Dimension;
import org.jdesktop.swingx.*;

/**
 * Renders Dimension type objects.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DimensionRenderer extends AbstractLabelRenderer {
    public DimensionRenderer() {
        super();
    }





    /**
     * Adds support for converting Dimension objects into {@code <width> x <height>}.
     *
     * @param value The value to convert.
     * @return A string representing the value.
     */
    @Override
    protected String toString(JXInspectionPane pane, Object value) {
        return value instanceof Dimension ? ((Dimension) value).width + " x " + ((Dimension) value).height : "";
    }

}
