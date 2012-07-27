/*
 * $Id: BasicXSliderUI.java 1767 2007-09-24 21:05:09Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.JXSlider;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.event.SliderChangeEvent;
import org.jdesktop.swingx.event.SliderChangeListener;
import org.jdesktop.swingx.plaf.LazyActionMap;
import org.jdesktop.swingx.plaf.UIListener;
import org.jdesktop.swingx.plaf.XSliderUI;
import org.jdesktop.swingx.slider.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

/**
 * Defines a basic look and feel delegate for a JXSlider class.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicXSliderUI extends XSliderUI implements LazyActionMap.ActionMapLoader {
    protected static final String CELL_RENDERER_PANE_COMPONENT = "cellRendererPane";

    /**
     * Singleton instance of this class.
     */
    private static ComponentUI SINGLETON;

    /**
     * Create the ui for this class.
     *
     * @param c The component the ui is to be applied to.
     * @return The ui for the given component.
     */
    public static ComponentUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicXSliderUI();
        }
        return SINGLETON;
    }


    private Adapter adpater;

    /**
     * Install any child components in this method.
     * <p/>
     * <p>Example Implementation:</p>
     * <p><pre><code>
     * {@code @Override}
     * protected static final String TITLE_COMPONENT = "title";
     * protected void installComponent(MyComp c) {
     *    addUIComponent(c, createTitleComponent(c), TITLE_COMPONENT);
     * }
     * <p/>
     * protected JComponent createTitleComponent(MyComp c) {
     *    return new JLabel();
     * }
     * </code></pre></p>
     *
     * @param component The container to add the children to.
     */
    @Override
    protected void installComponents(JXSlider component) {
        addUIComponent(component, new CellRendererPane(), CELL_RENDERER_PANE_COMPONENT);
    }


    /**
     * Installs the common defaults for the source component. This installs the background, foreground, font, border,
     * backgroundPainter and foregroundPainter properties. Default background, foreground and font properties will be
     * provided if no ui specified values can be found.
     *
     * @param component The comonent to install the defaults on.
     */
    protected void installDefaults(JXSlider component) {
        super.installDefaults(component);

        LookAndFeelUtilities.installProperty(component, "valueRenderer", new DefaultSliderValueRenderer());
        LookAndFeelUtilities.installProperty(component, "trackRenderer", new IconSliderTrackRenderer());
    }


    /**
     * Uninstalls the default properties for this component. This uninstalls the border, font, backgroundPainter and
     * foregroundPainter properties.
     *
     * @param component The component to uninstall the defaults from.
     */
    protected void uninstallDefaults(JXSlider component) {
        getAdapter(component).dispose(component);

        LookAndFeelUtilities.installProperty(component, "trackRenderer", null);
        LookAndFeelUtilities.installProperty(component, "valueRenderer", null);

        super.uninstallDefaults(component);
    }


    /**
     * Returns the specified component's preferred size appropriate for the look and feel. If <code>null</code> is
     * returned, the preferred size will be calculated by the component's layout manager instead (this is the preferred
     * approach for any component with a specific layout manager installed). The default implementation of this method
     * returns <code>null</code>.
     *
     * @param c the component whose preferred size is being queried; this argument is often ignored, but might be used
     *          if the UI object is stateless and shared by multiple components
     * @return The preferred size of this component.
     * @see javax.swing.JComponent#getPreferredSize
     * @see java.awt.LayoutManager#preferredLayoutSize
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        JXSlider slider = (JXSlider) c;
        boolean horizontal = slider.getOrientation() == JXSlider.Orientation.HORIZONTAL;
        Dimension result = new Dimension(0, 0);
        float baseline = 0;

        SliderTrackRenderer trackRenderer = slider.getTrackRenderer();
        if (trackRenderer != null) {
            Component track = trackRenderer.getSliderTrackRendererComponent(slider, slider.getMinimum(), slider.getMaximum(), slider.getMinimum(),
                    null, true, slider.isEnabled());
            Dimension pref = track.getPreferredSize();
            result.setSize(pref);
            if (horizontal) {
                baseline = pref.height * track.getAlignmentY();
            } else {
                baseline = pref.width * track.getAlignmentX();
            }
        }

        SliderValueRenderer valueRenderer = slider.getValueRenderer();
        if (valueRenderer != null) {
            Component val = valueRenderer.getSliderValueRendererComponent(slider, 0, -1, null, -1, null, false, true, false);
            Dimension pref = val.getPreferredSize();
            if (horizontal) {
                int h = result.height;
                float a = val.getAlignmentY();
                int base = (int) (pref.height * a);
                result.height = (int) (Math.max(baseline, base) + Math.max(h - baseline, pref.height - base));
                result.width = Math.max(result.width, pref.width);
            } else {
                int w = result.width;
                float a = val.getAlignmentX();
                int base = (int) (pref.width * a);
                result.width = (int) (Math.max(baseline, base) + Math.max(w - baseline, pref.width - base));
                result.height = Math.max(result.height, pref.height);

            }
        }

        // we give a preferred primary axis of 200 pixels.
        int preferredPrimary = getUIProperty(slider, "preferredAxisSize", 200);
        if (horizontal) {
            if (result.width < preferredPrimary) {
                result.width = preferredPrimary;
            }
        } else {
            if (result.height < preferredPrimary) {
                result.height = preferredPrimary;
            }
        }

        result = LookAndFeelUtilities.addInsets(slider, result);
        Rectangle r = new Rectangle();
        r.setSize(result);
        addFocusInsets(slider, r);
        result.setSize(r.width, r.height);

        return result;
    }


    /**
     * Paints the specified component appropriate for the look and feel.
     * This method is invoked from the <code>ComponentUI.update</code> method when
     * the specified component is being painted.  Subclasses should override
     * this method and use the specified <code>Graphics</code> object to
     * render the content of the component.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     * @see #update
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        /**
         * First we paint the ends, then the track and decorations (like ticks) then we paint the MarkerGroups.
         */
        Insets borderInsets = c.getInsets();

        int x = borderInsets.left;
        int y = borderInsets.top;
        int w = c.getWidth() - (borderInsets.left + borderInsets.right);
        int h = c.getHeight() - (borderInsets.top + borderInsets.bottom);

        JXSlider slider = (JXSlider) c;

        Projection proj = slider.getProjection();
        ViewRange view = proj.project(slider, w);
        boolean horizontal = slider.getOrientation() == JXSlider.Orientation.HORIZONTAL;

        CellRendererPane rp = (CellRendererPane) getUIComponent(slider, CELL_RENDERER_PANE_COMPONENT);

        Rectangle bounds = new Rectangle();
        // first we calculate the center bounds of the slider to give a common area to work with
        Dimension pref = slider.getPreferredSize();
        pref.width -= borderInsets.left + borderInsets.right;
        pref.height -= borderInsets.top + borderInsets.bottom;
        if (horizontal) {
            if (h > pref.height) {
                bounds.setBounds(x, y + ((h - pref.height) >> 1), w, pref.height);
            } else {
                bounds.setBounds(x, y, w, h);
            }
        } else {
            if (w > pref.width) {
                bounds.setBounds(x + ((w - pref.width) >> 1), y, pref.width, h);
            } else {
                bounds.setBounds(x, y, w, h);
            }
        }

        // adjust for focus bounds
        subtractFocusInsets(slider, bounds);

        int baseline = paintTrack(g, slider, view, bounds, bounds.height >> 1);

        SliderValueRenderer r = slider.getValueRenderer();
        if (r != null) {
            long min = view.getMinimumView();
            long max = view.getMaximumView();

            MarkerGroup selectedGroup = slider.getMarkerSelectedGroup();
            int selectedIndex = -1;

            for (int i = 0, n = slider.getMarkerGroupCount(); i < n; i++) {
                MarkerGroup group = slider.getMarkerGroup(i);
                if (group != selectedGroup) {
                    paintMarkers(g, slider, group, i, r, rp, min, max, false, null, bounds, horizontal, baseline);
                } else {
                    selectedIndex = i;
                }
            }

            if (selectedGroup != null) {
                boolean valSelected = slider.isMarkerValueSelected();
                Long selectedVal = null;
                if (valSelected) {
                    selectedVal = slider.getMarkerSelectedValue();
                }
                paintMarkers(g, slider, selectedGroup, selectedIndex, r, rp, min, max, true, selectedVal, bounds, horizontal, baseline);
            }
        }

        if (slider.isFocusOwner()) {
            // reset the area bounds for focus
            if (horizontal) {
                if (h > pref.height) {
                    bounds.setBounds(x, y + ((h - pref.height) >> 1), w, pref.height);
                } else {
                    bounds.setBounds(x, y, w, h);
                }
            } else {
                if (w > pref.width) {
                    bounds.setBounds(x + ((w - pref.width) >> 1), y, pref.width, h);
                } else {
                    bounds.setBounds(x, y, w, h);
                }
            }

            paintFocus(g, slider, bounds);
        }
    }


    /**
     * Adjusts the given bounds so that the focus rectangle is taken into account.
     *
     * @param slider The source slider.
     * @param bounds The bounds to adjust.
     */
    protected void subtractFocusInsets(JXSlider slider, Rectangle bounds) {
        Insets i = getUIProperty(slider, "focusInsets");
        Border b = getUIProperty(slider, "focusBorder");
        if (i != null) {
            LookAndFeelUtilities.subtractInsets(bounds, i);
        }
        if (b != null) {
            LookAndFeelUtilities.subtractInsets(bounds, b.getBorderInsets(slider));
        }
    }


    /**
     * Adjusts the given bounds so that the focus rectangle is taken into account.
     *
     * @param slider The source slider.
     * @param bounds The bounds to adjust.
     */
    protected void addFocusInsets(JXSlider slider, Rectangle bounds) {
        Insets i = getUIProperty(slider, "focusInsets");
        Border b = getUIProperty(slider, "focusBorder");
        if (i != null) {
            LookAndFeelUtilities.addInsets(bounds, i);
        }
        if (b != null) {
            LookAndFeelUtilities.addInsets(bounds, b.getBorderInsets(slider));
        }
    }


    /**
     * Paints the markers from the given group onto the track.
     *
     * @param g             The graphics to paint to.
     * @param slider        The slider whose marks to paint.
     * @param group         The group to get the markers from.
     * @param i             the index of the marker group in the slider
     * @param r             The renderer to use to paint the markers.
     * @param rp            The pane used to paint the markers.
     * @param min           The minimum marker to paint.
     * @param max           The maximum marker to paint.
     * @param selected      Whether this marker is selected.
     * @param selectedValue The selected value or null if no value is selected.
     * @param bounds        The bounds to paint the markers in.
     * @param horizontal    Whether the track is horizontal or vertical.
     * @param baseline      The baseline to align to markers to.
     */
    protected void paintMarkers(Graphics g, JXSlider slider, MarkerGroup group, int i,
                                SliderValueRenderer r, CellRendererPane rp,
                                long min, long max, boolean selected, Long selectedValue,
                                Rectangle bounds, boolean horizontal, int baseline) {
        MarkerRange markers = group.getMarkers(min, max);
        int selIndex = -1;
        boolean focused = slider.isFocusOwner();
        for (int j = 0, l = markers.getSize(); j < l; j++) {
            long val = markers.get(j);
            if (selectedValue != null && val == selectedValue) {
                selIndex = j;
            } else {
                if (!(group instanceof RepeatingMarkerGroup)) {
                    Component comp = r.getSliderValueRendererComponent(slider, val, i, group, j, markers,
                            focused && selected && selectedValue != null && val == selectedValue, slider.isEnabled(), false);
                    Dimension pref = comp.getPreferredSize();
                    float ax = comp.getAlignmentX();
                    float ay = comp.getAlignmentY();

                    int cx;
                    int cy;
                    if (horizontal) {
                        cx = bounds.x + (int) (bounds.width * ((val - min) / (double) (max - min))) - (int) (pref.width * ax);
                        cy = bounds.y + baseline - (int) (pref.height * ay);
                    } else {
                        cx = bounds.x + baseline - (int) (pref.width * ax);
                        cy = bounds.y + (int) (bounds.height * ((val - min) / (double) (max - min))) - (int) (pref.height * ay);
                    }
                    int cw = pref.width;
                    int ch = pref.height;

                    rp.paintComponent(g, comp, slider, cx, cy, cw, ch, true);
                } else {
                    //NB RJO - Pretty hacky of me.. just wanted to see what it'd look like (if repeatedmarkers === ticks)
                    g.setColor(Color.LIGHT_GRAY);
                    if (horizontal) {
                        int cx = bounds.x + (int) (bounds.width * ((val - min) / (double) (max - min)));
                        int cy = bounds.y + baseline;
                        g.drawLine(cx, cy + 2, cx, cy + 8);
                    } else {
                        int cx = bounds.x + baseline;
                        int cy = bounds.y + (int) (bounds.height * ((val - min) / (double) (max - min)));
                        g.drawLine(cx + 2, cy, cx + 8, cy);
                    }
                }
            }
        }
        if (selIndex >= 0) {
            Component comp = r.getSliderValueRendererComponent(slider, selectedValue, i, group, selIndex, markers,
                    focused && selected && selectedValue != null, slider.isEnabled(), false);
            Dimension pref = comp.getPreferredSize();
            float ax = comp.getAlignmentX();
            float ay = comp.getAlignmentY();

            int cx;
            int cy;
            if (horizontal) {
                cx = bounds.x + (int) (bounds.width * ((selectedValue - min) / (double) (max - min))) - (int) (pref.width * ax);
                cy = bounds.y + baseline - (int) (pref.height * ay);
            } else {
                cx = bounds.x + baseline - (int) (pref.width * ax);
                cy = bounds.y + (int) (bounds.height * ((selectedValue - min) / (double) (max - min))) - (int) (pref.height * ay);
            }
            int cw = pref.width;
            int ch = pref.height;

            rp.paintComponent(g, comp, slider, cx, cy, cw, ch, true);

        }
    }


    /**
     * Paint the track of the slider. This should also alter a number of other properties on the passed in objects. The
     * bounds object should be altered to reflect the actual bounds of the track as it has been painted. The returned
     * int should represent the baseline or anchor point along the sliders orientation axis for values to be renderered
     * relative to.
     *
     * @param g        The graphics to paint to.
     * @param slider   The slider to paint.
     * @param view     The view range to paint.
     * @param bounds   The rectangle to paint the sliders track into and to place the track bounds in.
     * @param baseline The line along the orientation axis of the passed bounds to place the track.
     * @return The baseline within the resultant bounds of the preferred layout coordinate.
     */
    protected int paintTrack(Graphics g, JXSlider slider, ViewRange view, Rectangle bounds, int baseline) {
        int result = 0;
        SliderTrackRenderer r = slider.getTrackRenderer();
        if (r == null) {
            result = baseline;
        } else {
            boolean horizontal = slider.getOrientation() == JXSlider.Orientation.HORIZONTAL;

            long val = slider.isMarkerValueSelected() ? slider.getMarkerSelectedValue() : 0;
            boolean editable = slider.isMarkerValueSelected() && slider.isEditable(slider.getMarkerSelectedGroup(), val);

            CellRendererPane rp = (CellRendererPane) getUIComponent(slider, CELL_RENDERER_PANE_COMPONENT);

            Component c = r.getSliderTrackRendererComponent(slider, slider.getMinimum(), slider.getMaximum(), 0, view, editable, slider.isEnabled());

            Dimension size = c.getPreferredSize();

            if (horizontal) {
                bounds.y += (bounds.height - size.height) * ((float) baseline / (float) bounds.height);
                bounds.height = size.height;
                result = (int) (c.getAlignmentY() * bounds.height);
            } else {
                bounds.x += (bounds.width - size.width) * ((float) baseline / (float) bounds.width);
                bounds.width = size.width;
                result = (int) (c.getAlignmentX() * bounds.width);
            }

            rp.paintComponent(g, c, slider, bounds);
        }
        return result;
    }


    /**
     * Paint the focus for this slider.
     *
     * @param g      The graphics to paint to.
     * @param slider The slider to paint the focus of.
     * @param bounds The bounds of the focus.
     */
    protected void paintFocus(Graphics g, JXSlider slider, Rectangle bounds) {
        Border focusBorder = getUIProperty(slider, "focusBorder");
        if (focusBorder != null) {
            focusBorder.paintBorder(slider, g, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }


    /**
     * Get the number of pixels to move by when scroll by block is invoked. A block is generally regarded as a page or
     * number of units invoked by page up/down or other page scroll trigger. This first checks any user client
     * properties then any ui set properties then uses a default based on the minimum and maximum view values.
     *
     * @param slider The slider to scroll.
     * @return The block size.
     */
    protected int getBlockSize(JXSlider slider) {
        Integer userSet = getUIProperty(slider, "blockSize");

        int result;
        if (userSet == null) {
            long min = locationToValue(slider, 0, 0);
            long max = locationToValue(slider, slider.getWidth(), slider.getHeight());
            result = (int) (Math.abs(max - min) / 10);
        } else {
            result = userSet;
        }
        return result;
    }


    /**
     * Get the number of pixels to scroll by when moving by one unit. A unit is usually a single line or element in a
     * view and for example can be triggered by the arrow keys.
     *
     * @param slider The slider to get the unit increment for.
     * @return The unit increment value.
     */
    protected int getUnitSize(JXSlider slider) {
        int result;
        Integer userSet = getUIProperty(slider, "unitSize");

        if (userSet == null) {
            long min = locationToValue(slider, 0, 0);
            long max = locationToValue(slider, slider.getWidth(), slider.getHeight());
            result = (int) (Math.abs(max - min) / 100);
        } else {
            result = userSet;
        }

        return result;
    }


    /**
     * Moves the active value either positively of negitively by one block unit.
     *
     * @param s              The slider to adjust.
     * @param positive       Whether the value should be increased or decreased.
     * @param valueAdjusting Whether this is just a point in a series of changes.
     */
    public void scrollByBlock(JXSlider s, boolean positive, boolean valueAdjusting) {
        if (s.isMarkerValueSelected()) {
            long val = s.getMarkerSelectedValue();
            if (positive) {
                val += getBlockSize(s);
            } else {
                val -= getBlockSize(s);
            }
            setActiveValue(s, val, valueAdjusting);
        }
    }


    /**
     * Move the active value by one unit.
     *
     * @param s              The slider to adjust.
     * @param positive       {@code true} if the value should bve increased.
     * @param valueAdjusting Whether this is just a point in a series of changes.
     */
    public void scrollByUnit(JXSlider s, boolean positive, boolean valueAdjusting) {
        if (s.isMarkerValueSelected()) {
            long val = s.getMarkerSelectedValue();
            if (positive) {
                val += getUnitSize(s);
            } else {
                val -= getUnitSize(s);
            }
            setActiveValue(s, val, valueAdjusting);
        }
    }


    /**
     * Changes the active value to the value given. If there is no active value then this does nothing.
     *
     * @param s              The slider to adjust.
     * @param value          The new value for the active value to take.
     * @param valueAdjusting Whether this is just a point in a series of changes.
     */
    public void setActiveValue(JXSlider s, long value, boolean valueAdjusting) {
        MarkerGroup m = s.getMarkerSelectedGroup();
        if (m != null && s.isMarkerValueSelected()) {
            long old = s.getMarkerSelectedValue();

            if (s.isEditable(m, old)) {
                MarkerMutator mutator = s.getMutator();
                mutator.mutate(s.getModel(), m, old, value, valueAdjusting);
            }
        }
    }


    /**
     * Get the visible value closest to given coordinates.
     *
     * @param slider The slider to get the value from.
     * @param x      The x coordinate for the query.
     * @param y      The y coordinate for the query.
     * @return The value closest to the given coords.
     */
    public long locationToValue(JXSlider slider, int x, int y) {
        long result = -1;
        Projection proj = slider.getProjection();
        /** @todo Convert this to use track bounds instead of size */
        Rectangle cb = new Rectangle(0, 0, slider.getWidth(), slider.getHeight());
        boolean horizontal = slider.getOrientation() == JXSlider.Orientation.HORIZONTAL;
        int size = horizontal ? cb.width : cb.height;
        ViewRange range = proj.project(slider, size);
        long vmin = range.getMinimumView();
        long vmax = range.getMaximumView();
        switch (slider.getOrientation()) {
            case HORIZONTAL:
                result = vmin + (long) Math.round((((long) x - (long) cb.x) / (double) cb.width) * (vmax - vmin));
                break;
            case VERTICAL:
                result = vmin + (long) Math.round((((long) y - (long) cb.y) / (double) cb.height) * (vmax - vmin));
                break;
        }
        return result;

    }


    /**
     * Convers the given value into a point on the slider. If the given value is not visible then null is returned.
     *
     * @param slider The slider to get the coordinate for.
     * @param value  The vlaue to get the location of.
     * @return The location of the given value in the slider.
     */
    public Point valueToLocation(JXSlider slider, long value) {
        return null;
    }


    /**
     * Attempts to select the value at the given coordinates. If a value has been selected, even if it was already
     * selected, true is returned else false is returned.
     *
     * @param slider The slider to select a point in.
     * @param x      The x coord to select.
     * @param y      The y coord to select.
     * @return true if a selection has been made at the given corrdinates.
     */
    protected boolean selectValueAt(JXSlider slider, int x, int y) {
        /**
         * To find the thumb that should be selected we first get the default thumb size and find all values that lie
         * within +/- thumb size wither side of the value closest to (x, y).
         *
         * We then iterate through all editable values found checking the renderer component for containment starting
         * with the currently selected MarkerGroup then working backwards through the sliders MarkerGroups (as this is
         * the order we paint them on top of each other).
         *
         * If a value is found to be contained within the given corrds then that value is selected and true is returned
         * else nothing happens and false is returned.
         */

        boolean result = false;

        MarkerSelectionModel sel = slider.getMarkerSelectionModel();

        SliderValueRenderer r = slider.getValueRenderer();
        if (r != null && sel != null) { // cant select a value if they arent rendererd
            // get the closest value to the coordinates
            long value = locationToValue(slider, x, y);

            // get the enclosing mark renderer component by passing null for MarkerGroup and MarkerRange
            Component standardMark = r.getSliderValueRendererComponent(slider, value, -1, null, -1, null, true, true, false);
            Dimension pref = standardMark.getPreferredSize();

            Rectangle bounds = new Rectangle(x - pref.width, y - pref.height, pref.width * 2, pref.height * 2);
            long min = locationToValue(slider, x - pref.width, y - pref.height);
            long max = locationToValue(slider, x + pref.width, y + pref.height);

            boolean horizontal = slider.getOrientation() == JXSlider.Orientation.HORIZONTAL;
            int baseline = horizontal ? slider.getHeight() / 2 - bounds.y : slider.getWidth() / 2 - bounds.x;

            MarkerGroup selectGroup = null;
            Long selectVal = null;

            MarkerGroup selectedMarker = slider.getMarkerSelectedGroup();
            if (selectedMarker != null) {
                int markerIndex = slider.indexOfMarker(selectedMarker);
                MarkerRange range = selectedMarker.getMarkers(min, max);
                for (int i = 0, n = range.getSize(); i < n; i++) {
                    long val = range.get(i);
                    if (slider.isEditable(selectedMarker, val)) {
                        Component comp = r.getSliderValueRendererComponent(slider, val, markerIndex, selectedMarker, i, range, false, true, false);
                        pref = comp.getPreferredSize();
                        float ax = comp.getAlignmentX();
                        float ay = comp.getAlignmentY();

                        int cx;
                        int cy;
                        if (horizontal) {
                            cx = bounds.x + (int) (bounds.width * ((val - min) / (double) (max - min))) - (int) (pref.width * ax);
                            cy = bounds.y + baseline - (int) (pref.height * ay);
                        } else {
                            cx = bounds.x + baseline - (int) (pref.width * ax);
                            cy = bounds.y + (int) (bounds.height * ((val - min) / (double) (max - min))) - (int) (pref.height * ay);
                        }
                        int cw = pref.width;
                        int ch = pref.height;

                        comp.setBounds(cx, cy, cw, ch);
                        if (comp.contains(x - cx, y - cy)) {
                            selectGroup = selectedMarker;
                            selectVal = val;
                            break;
                        }
                    }
                }
            }

            if (selectGroup == null) {
                for (int j = 0, l = slider.getMarkerGroupCount(); j < l; j++) {
                    MarkerGroup group = slider.getMarkerGroup(j);
                    if (group != selectedMarker) {
                        MarkerRange range = group.getMarkers(min, max);
                        for (int i = 0, n = range.getSize(); i < n; i++) {
                            long val = range.get(i);
                            if (slider.isEditable(group, val)) {
                                Component comp = r.getSliderValueRendererComponent(slider, val, j, group, i, range, false, true, false);
                                pref = comp.getPreferredSize();
                                float ax = comp.getAlignmentX();
                                float ay = comp.getAlignmentY();

                                int cx;
                                int cy;
                                if (horizontal) {
                                    cx = bounds.x + (int) (bounds.width * ((val - min) / (double) (max - min))) - (int) (pref.width * ax);
                                    cy = bounds.y + baseline - (int) (pref.height * ay);
                                } else {
                                    cx = bounds.x + baseline - (int) (pref.width * ax);
                                    cy = bounds.y + (int) (bounds.height * ((val - min) / (double) (max - min))) - (int) (pref.height * ay);
                                }
                                int cw = pref.width;
                                int ch = pref.height;

                                comp.setBounds(cx, cy, cw, ch);
                                if (comp.contains(x - cx, y - cy)) {
                                    selectGroup = group;
                                    selectVal = val;
                                    break;
                                }

                            }
                        }
                    }
                }
            }

            if (selectGroup != null) { // dont clear selection
                sel.setSelectedGroup(selectGroup);
                assert selectVal != null;
                sel.setSelectedValue(selectVal);
                result = true;
            }
        }
        return result;
    }


    /**
     * Called by {@link propertyChange(C, PropertChangeEvent)} when a property has changed.
     *
     * @param c        The source component.
     * @param property The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    @Override
    protected void propertyChange(JXSlider c, String property, Object oldValue, Object newValue) {
        c.revalidate();
        c.repaint();
    }


    /**
     * Overriden to add a SliderChangeListener to the given source.
     *
     * @param component The component to add the listeners to.
     */
    @Override
    protected void installListeners(JXSlider component) {
        super.installListeners(component);
        component.addSliderChangeListener(createSliderModelListener(component));
        component.addMouseListener(createMouseListener(component));
        component.addMouseMotionListener(createMouseMotionListener(component));
        component.addFocusListener(createFocusListener(component));
        component.addChangeListener(createChangeListener(component));
    }


    private Adapter getAdapter(JXSlider slider) {
        if (adpater == null) {
            adpater = new Adapter(slider);
        }

        return adpater;
    }


    protected SliderChangeListener createSliderModelListener(JXSlider slider) {
        return getAdapter(slider);
    }


    protected MouseListener createMouseListener(JXSlider slider) {
        return getAdapter(slider);
    }


    protected MouseMotionListener createMouseMotionListener(JXSlider slider) {
        return getAdapter(slider);
    }


    protected FocusListener createFocusListener(JXSlider slider) {
        return getAdapter(slider);
    }


    protected ChangeListener createChangeListener(JXSlider slider) {
        return getAdapter(slider);
    }


    /**
     * Add any key bindings to the component.
     *
     * @param c The component to add key bindings to.
     */
    @Override
    protected void installKeyBindings(JXSlider c) {
        InputMap imap = getInputMap(JComponent.WHEN_FOCUSED, c);
        SwingUtilities.replaceUIInputMap(c, JComponent.WHEN_FOCUSED, imap);
        LazyActionMap.installLazyActionMap(c, this, "XSlider.actionMap");
    }


    /**
     * Uninstall any key bindings from the source component.
     *
     * @param component The component to remove key bindings from.
     */
    @Override
    protected void uninstallKeyBindings(JXSlider component) {
        SwingUtilities.replaceUIActionMap(component, null);
        SwingUtilities.replaceUIInputMap(component, JComponent.WHEN_FOCUSED, null);
    }


    InputMap getInputMap(int condition, JXSlider s) {
        InputMap result = null;
        if (condition == JComponent.WHEN_FOCUSED) {
            result = (InputMap) LookAndFeelUtilities.get("XSlider.focusInputMap", "Slider.focusInputMap");
            InputMap rtlKeyMap;

            if (!s.getComponentOrientation().isLeftToRight() &&
                    ((rtlKeyMap = (InputMap) LookAndFeelUtilities.get("XSlider.focusInputMap.RightToLeft", "Slider.focusInputMap.RightToLeft")) != null)) {
                rtlKeyMap.setParent(result);
                result = rtlKeyMap;
            }

        }
        return result;
    }


    public void loadActionMap(LazyActionMap map) {
        map.put(new Actions(Actions.POSITIVE_UNIT_INCREMENT));
        map.put(new Actions(Actions.POSITIVE_BLOCK_INCREMENT));
        map.put(new Actions(Actions.NEGATIVE_UNIT_INCREMENT));
        map.put(new Actions(Actions.NEGATIVE_BLOCK_INCREMENT));
        map.put(new Actions(Actions.MIN_SCROLL_INCREMENT));
        map.put(new Actions(Actions.MAX_SCROLL_INCREMENT));
    }


    /**
     * Requests that focus be assigned to the given slider.
     *
     * @param slider The slider to assign focus to.
     */
    protected void requestFocus(JXSlider slider) {
        if (slider.isEnabled()) {
            slider.requestFocusInWindow();
        }
    }


    private static class Actions extends org.jdesktop.swingx.plaf.UIAction {
        private static enum ScrollType {
            POSITIVE,
            NEGATIVE,
            MIN,
            MAX
        }


        public static final String POSITIVE_UNIT_INCREMENT = "positiveUnitIncrement";
        public static final String POSITIVE_BLOCK_INCREMENT = "positiveBlockIncrement";
        public static final String NEGATIVE_UNIT_INCREMENT = "negativeUnitIncrement";
        public static final String NEGATIVE_BLOCK_INCREMENT = "negativeBlockIncrement";
        public static final String MIN_SCROLL_INCREMENT = "minScroll";
        public static final String MAX_SCROLL_INCREMENT = "maxScroll";

        public Actions(String name) {
            super(name);
        }


        public void actionPerformed(ActionEvent e) {
            JXSlider slider = (JXSlider) e.getSource();
            BasicXSliderUI ui = slider.getUI() instanceof BasicXSliderUI ? (BasicXSliderUI) slider.getUI() : null;
            String name = getName();

            if (ui == null) {
                return;
            }

            if (name == POSITIVE_UNIT_INCREMENT) {
                scroll(slider, ui, ScrollType.POSITIVE, false);

            } else if (name == POSITIVE_BLOCK_INCREMENT) {
                scroll(slider, ui, ScrollType.POSITIVE, true);

            } else if (name == NEGATIVE_UNIT_INCREMENT) {
                scroll(slider, ui, ScrollType.NEGATIVE, false);

            } else if (name == NEGATIVE_BLOCK_INCREMENT) {
                scroll(slider, ui, ScrollType.NEGATIVE, true);

            } else if (name == MIN_SCROLL_INCREMENT) {
                scroll(slider, ui, ScrollType.MIN, false);

            } else if (name == MAX_SCROLL_INCREMENT) {
                scroll(slider, ui, ScrollType.MAX, false);
            }
        }


        private void scroll(JXSlider s, BasicXSliderUI ui, ScrollType type, boolean block) {
            boolean inverted = false;
            boolean positive = false;

            switch (type) {
                case POSITIVE:
                    positive = true;
                case NEGATIVE:
                    if (inverted) {
                        positive = !positive;
                    }
                    if (block) {
                        ui.scrollByBlock(s, positive, false);
                    } else {
                        ui.scrollByUnit(s, positive, false);
                    }
                    break;
                case MAX:
                    positive = true;
                case MIN:
                    if (inverted) {
                        positive = !positive;
                    }
                    if (positive) {
                        ui.setActiveValue(s, s.getMaximum(), false);
                    } else {
                        ui.setActiveValue(s, s.getMinimum(), false);
                    }
                    break;
            }
        }

    }


    @UIListener
    private static class Adapter implements SliderChangeListener, MouseListener, MouseMotionListener, FocusListener, ChangeListener, ActionListener {

        /**
         * Provides the repeated events needed for continuous scrolling when mous pressing in the track.
         */
        private Timer scrollTimer;
        private long pressedOffset = 0;
        // used to determin the current direction of timed events.
        private boolean positive = false;
        private Point mouseLocation = new Point();
        private boolean valueAdjusting = false;
        // This is used for the Timers repeating on mouse pressed.
        private JXSlider source;

        public Adapter(JXSlider slider) {
            scrollTimer = new Timer(getUIProperty(slider, "scrollTimerInterval", 100), this);
            scrollTimer.setInitialDelay(getUIProperty(slider, "scrollTimerDelay", 300));
        }


        public void dispose(JXSlider slider) {
            scrollTimer.stop();
        }


        private void repaint(EventObject e) {
            JXSlider source = (JXSlider) e.getSource();
            source.repaint();
        }


        public void sliderRangeChanged(SliderChangeEvent e) {
            repaint(e);
        }


        public void sliderMarkerGroupAdded(SliderChangeEvent e) {
            repaint(e);
        }


        public void sliderMarkerGroupRemoved(SliderChangeEvent e) {
            repaint(e);
        }


        public void sliderMarkerGroupChanged(SliderChangeEvent e) {
            repaint(e);
        }


        public void mouseClicked(MouseEvent e) {
        }


        public void mousePressed(MouseEvent e) {
            JXSlider s = (JXSlider) e.getSource();
            BasicXSliderUI ui = (BasicXSliderUI) s.getUI();

            mouseLocation.setLocation(e.getX(), e.getY());

            // request focus
            ui.requestFocus(s);
            if (ui.selectValueAt(s, mouseLocation.x, mouseLocation.y)) {
                // save cache of original value for dragging.
                assert s.isMarkerValueSelected();

                long selVal = s.getMarkerSelectedValue();
                long mouseVal = ui.locationToValue(s, mouseLocation.x, mouseLocation.y);

                this.pressedOffset = selVal - mouseVal;
                valueAdjusting = true;
            } else if (s.isMarkerValueSelected()) {
                // adjust selected value by track.
                scrollTimer.stop();
                long val = ui.locationToValue(s, mouseLocation.x, mouseLocation.y);
                valueAdjusting = true;
                ui.scrollByBlock(s, positive = val > s.getMarkerSelectedValue(), true);
                source = s;
                scrollTimer.start();
            }
        }


        public void mouseReleased(MouseEvent e) {
            if (valueAdjusting) {
                valueAdjusting = false;

                JXSlider s = (JXSlider) e.getSource();
                BasicXSliderUI ui = (BasicXSliderUI) s.getUI();
                assert s.isMarkerValueSelected();
                ui.setActiveValue(s, s.getMarkerSelectedValue(), false);
            }
            scrollTimer.stop();
            source = null;

        }


        public void mouseEntered(MouseEvent e) {
        }


        public void mouseExited(MouseEvent e) {
        }


        public void mouseDragged(MouseEvent e) {
            mouseLocation.setLocation(e.getX(), e.getY());
            if (valueAdjusting && !scrollTimer.isRunning()) {
                JXSlider s = (JXSlider) e.getSource();
                BasicXSliderUI ui = (BasicXSliderUI) s.getUI();
                long newVal = ui.locationToValue(s, mouseLocation.x, mouseLocation.y) + pressedOffset;
                ui.setActiveValue(s, newVal, true);
            }
        }


        public void mouseMoved(MouseEvent e) {
        }


        public void focusGained(FocusEvent e) {
            repaint(e);
        }


        public void focusLost(FocusEvent e) {
            repaint(e);
        }


        public void stateChanged(ChangeEvent e) {
            repaint(e);
        }


        public void actionPerformed(ActionEvent e) {
            BasicXSliderUI ui = (BasicXSliderUI) source.getUI();
            if (positive) {
                if (source.getMarkerSelectedValue() < ui.locationToValue(source, mouseLocation.x, mouseLocation.y)) {
                    ui.scrollByBlock(source, true, true);
                } else {
                    valueAdjusting = false;
                    scrollTimer.stop();
                }
            } else {
                if (source.getMarkerSelectedValue() > ui.locationToValue(source, mouseLocation.x, mouseLocation.y)) {
                    ui.scrollByBlock(source, false, true);
                } else {
                    valueAdjusting = false;
                    scrollTimer.stop();
                }
            }
        }

    }
}
