/*
 * $Id: JXInspectionPaneAddon.java 2647 2008-08-06 10:13:06Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import java.util.Arrays;
import java.util.List;
import org.jdesktop.swingx.util.*;

/**
 * Addon for JXInspectionPane.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXInspectionPaneAddon extends AbstractComponentAddon {
    public JXInspectionPaneAddon() {
        super("JXInspectionPane");
    }





    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {

        defaults.add("InspectionPaneUI", "org.jdesktop.swingx.plaf.basic.BasicInspectionPaneUI");
        // look
        defaults.add("InspectionPane.font", UIManager.getFont("Label.font"));
        defaults.add("InspectionPane.foreground", UIManager.getFont("Label.foreground"));
        defaults.add("InspectionPane.backgroundWatermarkPainter", new UIDefaults.ProxyLazyValue(
                "org.jdesktop.swingx.plaf.basic.BasicPainters", "createInspectionPaneWatermarkPainter"));
        defaults.add("InspectionPane.backgroundPainter", new UIDefaults.ProxyLazyValue(
                "org.jdesktop.swingx.plaf.basic.BasicPainters", "createInspectionPaneBackgroundPainter"));
        // gaps
        defaults.add("InspectionPane.iconTextGap", 14);
        defaults.add("InspectionPane.titleDescriptionGap", 1);
        defaults.add("InspectionPane.headerDetailsGap", 12);
        defaults.add("InspectionPane.descriptionDetailsGap", 2);
        defaults.add("InspectionPane.detailsRowGap", 2);
        defaults.add("InspectionPane.detailsColumnGap", 12);
        defaults.add("InspectionPane.labelValueGap", 3);
        // max sizes
        defaults.add("InspectionPane.maxHeaderSize", 250);
        defaults.add("InspectionPane.maxValueSize", 135);
        defaults.add("InspectionPane.maxLabelSize", (int) Short.MAX_VALUE);
        // others
        defaults.add("InspectionPane.minFirstColumnRows", 2);
        defaults.add("InspectionPane.titleScale", 1.2f);
        defaults.add("InspectionPane.labelHorizontalAlignment", JLabel.TRAILING);
        defaults.add("InspectionPane.labelPostscript", ":");
        defaults.add("InspectionPane.opaque", Boolean.TRUE);
        // renderer settings
        defaults.add("InspectionPane.DateRenderer.format", new UIDefaults.ProxyLazyValue(
                "java.text.SimpleDateFormat", "getInstance"));
        defaults.add("InspectionPane.ColorRenderer.colorPolicy", "NONE");
    }





    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        if (UIManager.getLookAndFeel().getName().startsWith("GTK")) {
            addGTKDefaults(addon, defaults);
        } else {
            super.addWindowsDefaults(addon, defaults);
            defaults.add("InspectionPane.border", new UIDefaults.ProxyLazyValue(
                    "org.jdesktop.swingx.plaf.windows.WindowsBorders", "createInspectionPaneBorder"));
            defaults.add("InspectionPane.backgroundWatermark", new UIDefaults.ProxyLazyValue(
                    "org.jdesktop.swingx.plaf.windows.WindowsIcons", "createInspectionPaneWatermark"));
//            defaults.add("InspectionPane.backgroundWatermarkPainter", new UIDefaults.ProxyLazyValue(
//                    "org.jdesktop.swingx.plaf.windows.WindowsPainters", "createInspectionPaneWatermarkPainter"));
            defaults.add("InspectionPane.iconLocationPolicy", LocationPolicy.valueOf(LocationPolicy.CENTER));

            defaults.add("InspectionPane.labelForeground", new Color(0x737373));
            defaults.add("InspectionPane.backgroundPainter", new UIDefaults.ProxyLazyValue(
                    "org.jdesktop.swingx.plaf.windows.WindowsPainters", "createInspectionPaneBackgroundPainter"));
        }
    }





    protected void addGTKDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        addBasicDefaults(addon, defaults);
        defaults.add("InspectionPane.opaque", Boolean.FALSE);
    }
}
