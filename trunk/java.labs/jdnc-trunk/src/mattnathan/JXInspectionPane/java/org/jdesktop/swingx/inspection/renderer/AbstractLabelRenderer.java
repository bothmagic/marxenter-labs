/*
 * $Id: AbstractLabelRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXInspectionPane;
import org.jdesktop.swingx.inspection.InspectionValueRenderer;

/**
 * Provides the basic label instance renderer overriding propagation methods for performance reasons.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractLabelRenderer extends JLabel implements InspectionValueRenderer {

    private String lookAndFeel;
    public AbstractLabelRenderer() {
        lookAndFeel = UIManager.getLookAndFeel().getName();
        setVerticalAlignment(TOP);
    }

    /**
     * Return the component which is responsible for rendering the given value.
     *
     * @param pane The container for the details.
     * @param value The details value.
     * @param label String
     * @param row The row this will be painted at.
     * @param editable Whether the value is editable.
     * @param enabled Whether this component is enabled.
     * @return The component responsible for rendering the value.
     */
    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row,
          boolean editable, boolean enabled) {
        if (UIManager.getLookAndFeel().getName() != lookAndFeel) {
            lookAndFeel = UIManager.getLookAndFeel().getName();
            updateUI();
        }
        setEnabled(enabled);
        setForeground(pane.getForeground());
        setFont(pane.getFont());
        setText(toString(pane, value));

        installUIProperties(pane, value, label, row, editable, enabled);
        return this;
    }





    /**
     * Install the UI set properties for this render pass. This, by default, will install the font and foreground of the
     * label based on the valueForeground and valueFont ui properties. If these properties are not found then nothing is
     * changed.
     *
     * <p>When overriding this method it should be noted that it will be called after all parent-level properties have
     * been set. That is once the properties have been set from the parent JXInspectionPane then this method will be
     * called so that the ui can override the default parent got properties.
     *
     * @param pane The parent.
     * @param value The value to render.
     * @param label The label for the value.
     * @param row The row to render.
     * @param editable Whether the value is editable.
     * @param enabled Whether the value is enabled.
     */
    protected void installUIProperties(JXInspectionPane pane, Object value, String label, int row, boolean editable, boolean enabled) {
        Font f = getUIProperty(pane, "valueFont");
        if (f != null) {
            setFont(f);
        }

        Color fg = getUIProperty(pane, "valueForeground");
        if (fg != null) {
            setForeground(fg);
        }
    }





    /**
     * Get a UI property for the given property string. This will look in the panes client properties first then check
     * the UIManagers defaults table. This may return null if no value was found.
     *
     * <p>Example: if the property "valueFont" is requested then first the client property "valueFont" will be looked
     * for, if not found then the UIManager defaults property "InspectionPane.valueFont" will be looked for. If neither
     * are found then null is returned.
     *
     * @param pane The parent to get the property for.
     * @param property The name of the property.
     * @return The value requested.
     */
    @SuppressWarnings("unchecked")
    protected <T> T getUIProperty(JXInspectionPane pane, String property) {
        T result;
        result = (T) pane.getClientProperty(property);
        if (result == null) {
            result = (T) UIManager.get("InspectionPane." + property);
        }
        return result;
    }





    /**
     * Convert the given value into a String for representation.
     *
     * @param pane the source pane
     * @param value The value to convert.
     * @return A string representing the value.
     */
    protected abstract String toString(JXInspectionPane pane, Object value);





    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    @Override
    public void validate() {}





    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
    @Override
    public void invalidate() {}





    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     *
     * @since 1.5
     */
    @Override
    public void repaint() {}





    /**
     * Overridden for performance reasons.
     * See the <a href="#override">Implementation Note</a>
     * for more information.
     */
    @Override
    public void revalidate() {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param tm long
     * @param x int
     * @param y int
     * @param width int
     * @param height int
     */
    @Override
    public void repaint(long tm, int x, int y, int width, int height) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param r Rectangle
     */
    @Override
    public void repaint(Rectangle r) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue Object
     * @param newValue Object
     */
    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Strings get interned...
        if (propertyName == "text") {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue byte
     * @param newValue byte
     */
    @Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue char
     * @param newValue char
     */
    @Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue short
     * @param newValue short
     */
    @Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue int
     * @param newValue int
     */
    @Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue long
     * @param newValue long
     */
    @Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue float
     * @param newValue float
     */
    @Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue double
     * @param newValue double
     */
    @Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {}





    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more information.
     *
     * @param propertyName String
     * @param oldValue boolean
     * @param newValue boolean
     */
    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

}
