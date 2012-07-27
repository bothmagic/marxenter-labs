/*
 * $Id: BasicPainters.java 2636 2008-08-06 09:29:51Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXInspectionPane;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.plaf.XComponentUI;
import org.jdesktop.swingx.util.LocationPolicy;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines a set of factory methods for creating Painters.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicPainters {

    private static InspectionPaneWatermarkPainter inspectionPaneWatermarkPainter;
    /**
     * Creates a painter that will paint a watermark on a JXInspectionPane. The watermark that is painted will be an
     * Icon found as a UI property with the key backgroundWatermak at a location specified by
     * backgroundWatermarkLocation.
     *
     * @return The painter.
     */
    public static Painter<JXInspectionPane> createInspectionPaneWatermarkPainter() {
        if (inspectionPaneWatermarkPainter == null) {
            inspectionPaneWatermarkPainter = new InspectionPaneWatermarkPainter();
        }
        return inspectionPaneWatermarkPainter;
    }





    /**
     * Creates a painter that will paint the background of an JXInspecitonPane. This simply returns the watermark
     * painter.
     *
     * @return The painter.
     * @see #createInspectionPaneWatermarkPainter
     */
    @SuppressWarnings("unchecked")
    public static Painter<? super JXInspectionPane> createInspectionPaneBackgroundPainter() {
        return (Painter<? super JXInspectionPane>) UIManager.get("InspectionPane.backgroundWatermarkPainter");
    }





    private static class InspectionPaneWatermarkPainter extends AbstractPainter<JXInspectionPane> {
        @Override
        protected void doPaint(Graphics2D g, JXInspectionPane pane, int width, int height) {
            Icon watermark = XComponentUI.getUIProperty(pane, "backgroundWatermark");
            if (watermark != null) {
                IconUtilities.paintChild(watermark, pane, g, 0, 0, width, height,
                                         XComponentUI.getUIProperty(pane, "backgroundWatermarkLocation", LocationPolicy.valueOf(LocationPolicy.EAST)),
                                         ScalePolicy.NONE); // none means only scale if a ScalableIcon
            }
        }
    }
}
