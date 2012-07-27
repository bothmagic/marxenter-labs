/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import javax.swing.plaf.ColorUIResource;
import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.DefaultsList;
import org.jdesktop.swingx.plaf.macosx.CustomAquaButtonBorder;

/**
 * Loads the default settings in the provided LookAndFeel for the JXSwitch component.
 * @author Ryan Cuprak
 */
public class SwitchComponentAddon extends AbstractComponentAddon {

/**
     * Resource files
     */
    public static final String RESOURCES = "/org/jdesktop/swingx/Switch.properties";

    public SwitchComponentAddon() {
        super("JXSwitch");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add("TextField.backgroundErrorColor", new ColorUIResource(Color.RED));

        // Register the components
        defaults.add("SwitchUI", "org.jdesktop.swingx.plaf.SwitchUI");

        // Switch Button
        defaults.add("Switch.outlineTop", new ColorUIResource(125, 125, 125));
        defaults.add("Switch.outlineBottom", new ColorUIResource(190, 190, 190));
        defaults.add("Switch.outlineRight", new ColorUIResource(161, 161, 161));
        defaults.add("Switch.outlineLeft", new ColorUIResource(165, 165, 165));
        defaults.add("Switch.leverTop", new ColorUIResource(34, 56, 135));
        defaults.add("Switch.leverBottom", new ColorUIResource(102, 136, 213));
        defaults.add("Switch.leverRight", new ColorUIResource(34, 56, 135));
        defaults.add("Switch.leverLeft", new ColorUIResource(64, 98, 186));
        

        defaults.add("Switch.backgroundGradientStart", new ColorUIResource(205, 205, 205));
        defaults.add("Switch.backgroundGradientStop", new ColorUIResource(Color.WHITE));
        defaults.add("Switch.onPaintGradientStart", new ColorUIResource(63, 91, 177));
        defaults.add("Switch.onPaintGradientStop", new ColorUIResource(137, 172, 250));
        defaults.add("Switch.offLabelTextColor", new ColorUIResource(117, 117, 117));

        // Switch Lever
        defaults.add("Switch.paintGradientStart", new ColorUIResource(182, 182, 182));
        defaults.add("Switch.paintGradientStop", new ColorUIResource(251, 251, 251));
        defaults.add("Switch.levelTop", new ColorUIResource(145, 145, 145));
        defaults.add("Switch.levelRight", new ColorUIResource(159, 159, 159));
        defaults.add("Switch.levelBottom", new ColorUIResource(151, 151, 151));
        defaults.add("Switch.levelLeft", new ColorUIResource(173, 173, 173));
        // Register border stuff
        

        try {
            PropertyResourceBundle prb = new PropertyResourceBundle(SwitchComponentAddon.class.getResourceAsStream(RESOURCES));
            defaults.add("Switch.onLabel", prb.getString("switchOnLabel"));
            defaults.add("Switch.offLabel", prb.getString("switchOffLabel"));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load resources");
        }
    }

    /**
     * Add the MacDefaults
     * @param addon - look and feel addon
     * @param defaults - defaults
     */
    @Override
    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add("Switch.border", new CustomAquaButtonBorder(5)); // TODO MacOS X L&F
        defaults.add("aquaFocusButton.innerColor", new ColorUIResource(98, 170, 223));
        defaults.add("aquaFocusButton.middleColor", new ColorUIResource(144, 192, 228));
        defaults.add("aquaFocusButton.outerColor", new ColorUIResource(190, 214, 231));
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addWindowsDefaults(addon, defaults);
    }


}
