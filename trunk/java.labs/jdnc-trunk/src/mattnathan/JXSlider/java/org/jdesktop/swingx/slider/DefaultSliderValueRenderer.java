/*
 * $Id: DefaultSliderValueRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;

import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.JXSlider;

/**
 * Default value renderer who paints the value the same as a standard JSliders thumb. In order to get the sliders thumb
 * a number of hacks need to be used as it's hard to separate the thumb from the remainder of the sliders painting.
 *
 * <p> <strong><a name="override">Implementation Note:</a></strong> This class overrides <code>invalidate</code>,
 * <code>validate</code>, <code>revalidate</code>, <code>repaint</code>, <code>isOpaque</code>, and
 * <code>firePropertyChange</code> solely to improve performance. If not overridden, these frequently called methods
 * would execute code paths that are unnecessary for the default renderer. If you write your own renderer,
 * take care to weigh the benefits and drawbacks of overriding these methods.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultSliderValueRenderer extends JXComponent implements SliderValueRenderer {

    private JSlider slider;
    private CellRendererPane cellRenderer;
    private String lnfClassName;
    private boolean hasFocus = false;

    public DefaultSliderValueRenderer() {
        lnfClassName = UIManager.getLookAndFeel().getClass().getName();
        slider = new JSlider(0, 0, 0) {
            @Override
            public boolean hasFocus() {
                return DefaultSliderValueRenderer.this.hasFocus();
            }





            @Override
            public boolean isEnabled() {
                return DefaultSliderValueRenderer.this.isEnabled();
            }
        };
        slider.setPaintTrack(false);
        slider.setPaintLabels(false);
        slider.setPaintTicks(false);
        slider.setOpaque(false);
        Insets i = slider.getInsets();
        slider.setBorder(BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));
        setOpaque(false);

        // uses CellRendererPane so that we have more flexibility when painting the slider child.
        cellRenderer = new CellRendererPane();
        setBackgroundPainter(null);
        add(cellRenderer);
        updateUI();
    }





    /**
     * Return a component that can be used to layout and paint the given value on the given JXSlider.
     *
     * @param slider The slider the value belongs to.
     * @param value The value to render.
     * @param groupIndex The index within the sliders model of the MarkerGroup the value originated from.
     * @param group The group the value and MarkerRange originated from.
     * @param rangeIndex The index within the MarkerRange of the value.
     * @param range The marker range the value is collected from.
     * @param hasFocus Whether the given value has the active focus.
     * @param enabled Whether the JXSlider and the MarkerGroup are enabled.
     * @param valueAdjusting boolean
     * @return The component used to layout and paint the given value on the slider.
     */
    public Component getSliderValueRendererComponent(JXSlider slider, long value,
          int groupIndex, MarkerGroup group,
          int rangeIndex, MarkerRange range,
          boolean hasFocus, boolean enabled, boolean valueAdjusting) {

        enabled = enabled && (group == null || slider.isEditable(group, value));

        revalidateLookAndFeelValues();

        setEnabled(enabled);
        this.hasFocus = hasFocus;
        this.slider.setOrientation(slider.getOrientation() == JXSlider.Orientation.HORIZONTAL ? JSlider.HORIZONTAL : JSlider.VERTICAL);

        this.slider.putClientProperty("Slider.paintThumbArrowShape", slider.getClientProperty("XSlider.paintThumbArrowShape"));

        Dimension thumbSize = getThumbSize(this.slider);
        setPreferredSize(thumbSize);

        return this;
    }





    @Override
    public boolean hasFocus() {
        return hasFocus;
    }





    /**
     * gets the size of the thumbnail for the slider value.
     *
     * @param slider JSlider
     * @return Dimension
     */
    protected Dimension getThumbSize(JSlider slider) {
        Dimension result = null;

        /**
         * To find the thumb size we first check the Slider.thumbIcon properties for the icon
         * If we don't find it there we try to get it by reflection on the BasicSliderUI instance
         * If that fails we default to the copied BasicSliderUI implementation
         */

        // get from thumb icon
        Icon thumbIcon;
        if (slider.getOrientation() == JSlider.VERTICAL) {
            thumbIcon = UIManager.getIcon("Slider.verticalThumbIcon");
        } else {
            thumbIcon = UIManager.getIcon("Slider.horizontalThumbIcon");
        }
        if (thumbIcon != null) {
            result = new Dimension(thumbIcon.getIconWidth(), thumbIcon.getIconHeight());
        }

        if (result == null) {
            SliderUI ui = slider.getUI();
            if (ui instanceof javax.swing.plaf.basic.BasicSliderUI) {
                // get from reflection on
                try {
                    Method m = ui.getClass().getDeclaredMethod("getThumbSize");
                    m.setAccessible(true);
                    Dimension d = (Dimension) m.invoke(ui);
                    result = d;
                } catch (SecurityException ex) {
                    // ignore and fall through to default implementation
                } catch (NoSuchMethodException ex) {
                    throw new InternalError("method getThumbSize should exist in " + ui);
                } catch (InvocationTargetException ex) {
                    Throwable cause = ex.getCause();
                    if (cause instanceof RuntimeException) {
                        throw (RuntimeException) cause;
                    } else if (cause instanceof Error) {
                        throw (Error) cause;
                    } else {
                        throw (Error)new InternalError("Method getThumbSize in " + ui + " doesnt throw any checked exceptions").initCause(cause);
                    }
                } catch (IllegalArgumentException ex) {
                    throw (Error)new InternalError("getThumbSize in " + ui + " takes no arguments").initCause(ex);
                } catch (IllegalAccessException ex) {
                    // ignore and fall through to default implementation
                }
            }
        }

        if (result == null) {
            // copied from BasicSliderUI
            result = new Dimension();

            if (slider.getOrientation() == JSlider.VERTICAL) {
                result.width = 20;
                result.height = 11;
            } else {
                result.width = 11;
                result.height = 20;
            }

        }
        return result;
    }





    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Icon thumbIcon;
        if (slider.getOrientation() == JSlider.VERTICAL) {
            thumbIcon = UIManager.getIcon("Slider.verticalThumbIcon");
        } else {
            thumbIcon = UIManager.getIcon("Slider.horizontalThumbIcon");
        }
        if (thumbIcon != null) {
            thumbIcon.paintIcon(this, g, 0, 0);
        } else {
            slider.setBounds(0, 0, getWidth(), getHeight());
            SliderUI ui = slider.getUI();
            if (ui instanceof BasicSliderUI) {
                ((BasicSliderUI) ui).setThumbLocation(0, 0);
            }
            cellRenderer.paintComponent(g, slider, this, 0, 0, getWidth(), getHeight());
            slider.setBounds( -getWidth(), -getHeight(), 0, 0);
        }
    }





    protected void revalidateLookAndFeelValues() {
        String tmp = UIManager.getLookAndFeel().getClass().getName();
        if (!tmp.equals(lnfClassName)) {
            slider.setBorder(null);
            slider.updateUI();
            Insets i = slider.getInsets();
            slider.setBorder(BorderFactory.createEmptyBorder(i.top, i.left, i.bottom, i.right));
        }
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
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}





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
