/*
 * $Id: ImageRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection.renderer;

import java.awt.Component;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jdesktop.swingx.JXInspectionPane;

/**
 * Renderer supporting both Image and Icon types.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ImageRenderer extends AbstractLabelRenderer {
    public ImageRenderer() {
        super();
    }





    /**
     * Always returns "".
     *
     * @param pane JXInspectionPane
     * @param value The value to convert.
     * @return A string representing the value.
     */
    @Override
    protected String toString(JXInspectionPane pane, Object value) {
        return "";
    }





    /**
     * Adds an icon to the label supporting Image and Icon types.
     *
     * @param pane The container for the details.
     * @param value The details value.
     * @param label String
     * @param row The row this will be painted at.
     * @param editable Whether the value is editable.
     * @param enabled Whether this component is enabled.
     * @return The component responsible for rendering the value.
     */
    @Override
    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row, boolean editable,
          boolean enabled) {
        if (value instanceof Icon) {
            setIcon((Icon) value);
        } else if (value instanceof Image) {
            setIcon(new ImageIcon((Image) value));
        } else {
            setIcon(null);
        }
        return super.getInspectionValueRendererComponent(pane, value, label, row, editable, enabled);
    }
}
