/*
 * $Id: JXSliderAddon.java 2766 2008-10-09 15:32:38Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIDefaults.ProxyLazyValue;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;

/**
 * Defines the addon for the JXSlider component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXSliderAddon extends AbstractComponentAddon {
    private boolean internal = false;
    public JXSliderAddon() {
        super("JXSlider");
    }





    /**
     * Adds default key/value pairs to the given list. Simply adds the Basic ui binding for the uiClassID.
     *
     * @param addon The addons object.
     * @param defaults The list of defaults to add to.
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        if (isGTK(addon) && !internal) {
            internal = true;
            try {
                addGTKDefaults(addon, defaults);
            } finally {
                internal = false;
            }
        } else {
            final String p = "org.jdesktop.swingx.plaf.basic";

            defaults.add("XSliderUI", p + ".BasicXSliderUI");

            // track
            defaults.add("XSlider.trackIcon", new ProxyLazyValue(p + ".BasicIcons", "createSliderTrackIcon"));
            defaults.add("XSlider.trackSize", 4);
            defaults.add("XSlider.focusBorder", new ProxyLazyValue(p + ".BasicBorders", "createSliderFocusBorder"));
            defaults.add("XSlider.focusInsets", new InsetsUIResource(1, 1, 1, 1));
            defaults.add("XSlider.border", UIManager.get("Slider.border"));
            defaults.add("XSlider.opaque", Boolean.TRUE);
            defaults.add("XSlider.background", UIManager.get("Slider.background"));
            defaults.add("XSlider.foreground", new ColorUIResource(Color.BLACK));

        }
    }





    protected void addGTKDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
        defaults.add("XSlider.trackIcon",
                new ProxyLazyValue("org.jdesktop.swingx.plaf.gtk.GTKIcons", "createSliderTrackIcon"));
        defaults.add("XSlider.opaque", Boolean.FALSE);
//        defaults.add("XSlider.focusInsets");
//        defaults.add(new Insets(2, 1, 0, 1));

    }





    /**
     * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     *
     * @param addon LookAndFeelAddons
     * @param defaults List
     */
    @Override
    protected void addMotifDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
        defaults.add("XSlider.trackIcon", new ProxyLazyValue("org.jdesktop.swingx.plaf.motif.MotifIcons", "createSliderTrackIcon"));
        defaults.add("XSlider.focusBorder", new BorderUIResource(BorderFactory.createEmptyBorder()));
        defaults.add("XSlider.focusInsets", new InsetsUIResource(0, 0, 0, 0));
    }





    /**
     * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     *
     * @param addon LookAndFeelAddons
     * @param defaults List
     */
    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
        defaults.add("XSlider.focusBorder", new BorderUIResource(BorderFactory.createEmptyBorder()));
        defaults.add("XSlider.focusInsets", null);
    }





    /**
     * Default implementation calls {@link #addBasicDefaults(LookAndFeelAddons, DefaultsList)}
     *
     * @param addon LookAndFeelAddons
     * @param defaults List
     */
    @Override
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
        defaults.add("XSlider.opaque", Boolean.FALSE);
    }





    protected boolean isGTK(LookAndFeelAddons addons) {
        return UIManager.getLookAndFeel().getName().contains("GTK");
    }
}
