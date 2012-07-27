/*
 * $Id: ColorRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection.renderer;

import org.jdesktop.swingx.JXInspectionPane;
import java.awt.*;
import org.jdesktop.swingx.inspection.renderer.ColorRenderer.ColorPolicy;

/**
 * A renderer for Color types.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ColorRenderer extends AbstractLabelRenderer {

    /**
     * Defines the type of color scheme used to render this color.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 1.0
     */
    public static enum ColorPolicy {
        /**
         * The color is used as the foreground color.
         */
        FOREGROUND,
        /**
         * The color is used as the background color.
         */
        BACKGROUND,
        /**
         * No color changes are made.
         */
        NONE,
        /**
         * Uses the color as the background and inverts for the foreground.
         */
        INVERT
    }







    private boolean policySet = false;
    private ColorPolicy policy = ColorPolicy.NONE;

    public ColorRenderer() {
        super();
    }





    public ColorRenderer(ColorPolicy policy) {
        this.policy = policy;
        policySet = true;
    }





    public void setColorPolicy(ColorPolicy policy) {
        this.policy = policy;
        policySet = true;
    }





    public ColorPolicy getColorPolicy() {
        return policy;
    }





    public ColorPolicy getUsableColorPolicy(JXInspectionPane pane) {
        ColorPolicy result = getColorPolicy();
        if (!policySet) {
            Object o = getUIProperty(pane, "ColorRenderer.colorPolicy");
            if (o instanceof ColorPolicy) {
                result = (ColorPolicy) o;
            } else if (o != null) {
                try {
                    result = ColorPolicy.valueOf(o.toString());
                } catch (Exception ex) {
                    result = ColorPolicy.NONE;
                }
            }
        }
        return result;
    }





    /**
     * Convert the given value into a String for representation.
     *
     * @param pane JXInspectionPane
     * @param value The value to convert.
     * @return A string representing the value.
     */
    @Override
    protected String toString(JXInspectionPane pane, Object value) {
        String result = "";
        if (value instanceof Color) {
            Color c = (Color) value;
            result = '#' + Integer.toHexString(c.getAlpha() < 255 ? c.getRGB() : c.getRGB() & 0xffffff).toUpperCase();
        }
        return result;
    }





    /**
     * Install the UI set properties for this render pass.
     *
     * @param pane The parent.
     * @param value The value to render.
     * @param label The label for the value.
     * @param row The row to render.
     * @param editable Whether the value is editable.
     * @param enabled Whether the value is enabled.
     */
    @Override
    protected void installUIProperties(JXInspectionPane pane, Object value, String label, int row, boolean editable, boolean enabled) {
        setOpaque(false);
        super.installUIProperties(pane, value, label, row, editable, enabled);
        Color c = (Color) value;
        switch (getUsableColorPolicy(pane)) {
            case INVERT:
                setForeground(new Color(~c.getRGB()));
            case BACKGROUND:
                setOpaque(true);
                setBackground(c);
                break;
            case FOREGROUND:
                setForeground(c);
                break;
            case NONE:
            default:
                break;
        }
    }
}
