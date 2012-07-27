/*
 * $Id: IconSliderTrackRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXIconPanel;
import org.jdesktop.swingx.JXSlider;

/**
 * Renderer that uses the default UI icon for track renderer.
 *
 * <p> <strong><a name="override">Implementation Note:</a></strong> This class overrides <code>invalidate</code>,
 * <code>validate</code>, <code>revalidate</code>, <code>repaint</code>, <code>isOpaque</code>, and
 * <code>firePropertyChange</code> solely to improve performance. If not overridden, these frequently called methods
 * would execute code paths that are unnecessary for the default renderer. If you write your own renderer,
 * take care to weigh the benefits and drawbacks of overriding these methods.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IconSliderTrackRenderer extends JXIconPanel implements SliderTrackRenderer {

    private long minimum;
    private long maximum;
    private long value;
    private ViewRange view;
    private JXSlider slider;
    private boolean valueEditable;
    private boolean sliderEnabled;

    public IconSliderTrackRenderer() {
        setIcon(createDefaultIcon());
    }





    protected Icon createDefaultIcon() {
        return UIManager.getIcon("XSlider.trackIcon");
    }





    /**
     * Return a component that can be used to represent the track of a JXSlider.
     *
     * @param slider The slider to represent.
     * @param minimum The minimum value for the track.
     * @param maximum The maximum value for the track.
     * @param value The value currently active.
     * @param view The current view on the track.
     * @param valueEditable boolean
     * @param enabled boolean
     * @return The component to use to represent the track of a JXSlider.
     */
    public Component getSliderTrackRendererComponent(JXSlider slider, long minimum, long maximum, long value, ViewRange view, boolean valueEditable,
          boolean enabled) {
        configure(slider, minimum, maximum, value, view, valueEditable, enabled);
        return this;
    }





    protected void configure(JXSlider slider, long minimum, long maximum, long value, ViewRange view, boolean valueEditable, boolean enabled) {
        this.slider = slider;
        this.minimum = minimum;
        this.maximum = maximum;
        this.value = value;
        this.view = view;
        this.sliderEnabled = enabled;
        this.valueEditable = valueEditable;
    }





    public long getMaximum() {
        return maximum;
    }





    public long getMinimum() {
        return minimum;
    }





    public JXSlider getSlider() {
        return slider;
    }





    public long getValue() {
        return value;
    }





    public ViewRange getView() {
        return view;
    }





    public boolean isSliderEnabled() {
        return sliderEnabled;
    }





    public boolean isValueEditable() {
        return valueEditable;
    }





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
        if (propertyName == "icon") {
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
