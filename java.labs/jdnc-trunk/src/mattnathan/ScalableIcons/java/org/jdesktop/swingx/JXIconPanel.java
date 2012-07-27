/*
 * $Id: JXIconPanel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.event.DynamicObject;
import org.jdesktop.swingx.plaf.JXIconPanelAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.util.LocationPolicy;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Simple component that displays an icon. This component is ScalableIcon and DynamicObject aware and as such will
 * repaint when the icon has changed. The Icon itself will be painted using the ScalePolicy and LocationPolicy set on
 * this component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXIconPanel extends JXComponent {

    static {
        LookAndFeelAddons.contribute(new JXIconPanelAddon());
    }





    private static final String uiClassID = "IconPanelUI";

    private ChangeListener changeListener; // holds the reference to the listener that repaints on dynamic change

    private Icon icon;
    private LocationPolicy locationPolicy;
    private ScalePolicy scalePolicy;

    /**
     * Creates a new default IconPanel with no icon.
     */
    public JXIconPanel() {
        this(null);
    }





    /**
     * Create a new icon panel with the given icon.
     *
     * @param icon The icon to paint. This can be null.
     */
    public JXIconPanel(Icon icon) {
        this(icon, null, null);
    }





    /**
     * Create a new icon panel with the given icon and location policy.
     *
     * @param icon The icon to paint, can be null.
     * @param locationPolicy The location policy, can be null.
     */
    public JXIconPanel(Icon icon, LocationPolicy locationPolicy) {
        this(icon, locationPolicy, null);
    }





    /**
     * Create a new icon panel with the given icon and scale policy.
     *
     * @param icon The icon to paint.
     * @param scalePolicy The scale policy to use to paint the icon.
     */
    public JXIconPanel(Icon icon, ScalePolicy scalePolicy) {
        this(icon, null, scalePolicy);
    }





    /**
     * Create a new icon panel with the given properties.
     *
     * @param icon The icon to paint.
     * @param locationPolicy The location to paint the icon.
     * @param scalePolicy The scale policy to use to paint the icon.
     */
    public JXIconPanel(Icon icon, LocationPolicy locationPolicy, ScalePolicy scalePolicy) {
        if (locationPolicy == null) {
            locationPolicy = LocationPolicy.valueOf(LocationPolicy.CENTER);
        }
        if (scalePolicy == null) {
            scalePolicy = ScalePolicy.FIXED_RATIO;
        }
        this.icon = icon;
        if (icon instanceof DynamicObject) {
            ((DynamicObject) icon).addChangeListener(changeListener = new UpdateAdapter());
        }
        this.locationPolicy = locationPolicy;
        this.scalePolicy = scalePolicy;
        updateUI();
    }





    /**
     * @return {@code "IconPanelUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * Get the icon this component represents. This may be null.
     *
     * @return The icon this component represents.
     * @see #getIcon
     */
    public Icon getIcon() {
        return icon;
    }





    /**
     * Get the location policy for painting the icon. This will never be null.
     *
     * @return The location policy.
     * @see #setLocationPolicy
     * @see #getScalePolicy
     */
    public LocationPolicy getLocationPolicy() {
        return locationPolicy;
    }





    /**
     * Get the scale policy for painting the icon. This will never be null.
     *
     * @return The scale policy.
     * @see #setScalePolicy
     * @see #getLocationPolicy
     */
    public ScalePolicy getScalePolicy() {
        return scalePolicy;
    }





    /**
     * Set the icon to paint. This can be set to null and in this case will paint no icon. If the given icon is an
     * instance of DynamicObject then a ChangeListener will be added to the icon and this component will be repainted
     * whenever a change event occurs.
     *
     * @param icon The icon to paint.
     */
    public void setIcon(Icon icon) {
        Icon old = getIcon();
        if (old instanceof DynamicObject) {
            ((DynamicObject) old).removeChangeListener(changeListener);
        }
        this.icon = icon;
        icon = getIcon();
        if (icon instanceof DynamicObject) {
            if (changeListener == null) {
                changeListener = new UpdateAdapter();
            }
            ((DynamicObject) icon).addChangeListener(changeListener);
        }
        if (old != icon) {
            firePropertyChange("icon", old, icon);
            revalidate(); // the icon may have changed size
            repaint();
        }
    }





    /**
     * Set the location policy to use when locating the child icon. If {@code null} is passed this will use
     * LocationPolicy.valueOf(LocationPolicy.CENTER).
     *
     * @param locationPolicy The location policy for painting the child icon.
     * @see #getLocationPolicy
     * @see #setScalePolicy
     */
    public void setLocationPolicy(LocationPolicy locationPolicy) {
        if (locationPolicy == null) {
            locationPolicy = LocationPolicy.valueOf(LocationPolicy.CENTER);
        }

        LocationPolicy old = getLocationPolicy();
        this.locationPolicy = locationPolicy;
        firePropertyChange("locationPolicy", old, getLocationPolicy());
        repaint();
    }





    /**
     * Set the scale policy for painting the icon. If {@code null} is passed then the scale policy will be set to
     * ScalePolicy.FIXED_RATIO.
     *
     * @param scalePolicy The scale policy for painting the icon.
     * @see #getScalePolicy
     * @see #setLocationPolicy
     */
    public void setScalePolicy(ScalePolicy scalePolicy) {
        if (scalePolicy == null) {
            scalePolicy = ScalePolicy.FIXED_RATIO;
        }
        ScalePolicy old = getScalePolicy();
        this.scalePolicy = scalePolicy;
        firePropertyChange("scalePolicy", old, getScalePolicy());
        repaint();
    }





    /**
     * Repaints the component when changes occur in the child icon.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class UpdateAdapter implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            repaint();
        }
    }
}
