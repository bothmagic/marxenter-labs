/*
 * $Id: InspectionValueRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

import java.awt.Component;

import org.jdesktop.swingx.JXInspectionPane;

/**
 * Defines a renderer for the values of an inspection panels details.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface InspectionValueRenderer {
    /**
     * Return the component which is responsible for rendering the given value. This can be called frequently with
     * different values and standard practice is to return a single configured object instead of a new object each time.
     *
     * @param pane The container for the details.
     * @param value The details value.
     * @param label The label applied to the value.
     * @param row The row this will be painted at.
     * @param editable Whether the value is editable.
     * @param enabled Whether this component is enabled.
     * @return The component responsible for rendering the value.
     */
    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row, boolean editable, boolean enabled);
}
