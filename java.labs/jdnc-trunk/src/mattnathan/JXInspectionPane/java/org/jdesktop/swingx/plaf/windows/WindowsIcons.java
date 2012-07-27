package org.jdesktop.swingx.plaf.windows;

import javax.swing.*;
import org.jdesktop.swingx.icon.DefaultScalableIcon;
import org.jdesktop.swingx.IconFactory;
import org.jdesktop.swingx.util.ScalePolicy;
import org.jdesktop.swingx.image.ImageUtilities;

import java.io.File;

public class WindowsIcons {
    private WindowsIcons() {
        super();
    }





    private static Icon inspectionPaneWatermark;
    public static Icon createInspectionPaneWatermark() {
        if (inspectionPaneWatermark == null) {
            DefaultScalableIcon icon = (DefaultScalableIcon) IconFactory.createScalableIcon(true,
                  WindowsIcons.class.getResource("resources" + File.pathSeparatorChar + "vista-energy.png"), 511, 259);
            icon.setScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.SHRINK));
            icon.setInterpolation(ImageUtilities.Interpolation.BILINEAR_MULTISTEP);
            inspectionPaneWatermark = IconFactory.createBorderIcon(icon, BorderFactory.createEmptyBorder(8, 0, 0, 6));
        }
        return inspectionPaneWatermark;
    }
}
