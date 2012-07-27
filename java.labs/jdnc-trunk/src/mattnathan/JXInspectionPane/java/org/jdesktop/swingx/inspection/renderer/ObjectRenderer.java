/*
 * $Id: ObjectRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection.renderer;

import org.jdesktop.swingx.*;



/**
 * A renderer for rendering Object types. This can act as the base-class for all label based renderers as it provides a
 * protected toString(String) method which can be used to convert the value into a string.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ObjectRenderer extends AbstractLabelRenderer {
    public ObjectRenderer() {
        super();
    }





    /**
     * Convert the given value into a String for representation. This calls toString on the object if not null else
     * returns "". This should not return null.
     *
     * @param value The value to convert.
     * @return A string representing the value.
     */
    @Override
    protected String toString(JXInspectionPane pane, Object value) {
        return value == null ? "" : value.toString();
    }
}
