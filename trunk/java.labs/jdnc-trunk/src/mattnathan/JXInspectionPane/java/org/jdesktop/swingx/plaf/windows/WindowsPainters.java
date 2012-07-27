/*
 * $Id: WindowsPainters.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.windows;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.JXInspectionPane;
import org.jdesktop.swingx.image.ImageUtilities;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.plaf.XComponentUI;
import org.jdesktop.swingx.plaf.basic.BasicPainters;

/**
 * Defines painters for a Windows look and feel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class WindowsPainters extends BasicPainters {
    private static InspectionPaneBackground inspectionPaneBackground;

    public static Painter<JXInspectionPane> createInspectionPaneBackgroundPainter() {
        if (inspectionPaneBackground == null) {
            inspectionPaneBackground = new InspectionPaneBackground();
        }
        return inspectionPaneBackground;
    }





    /**
     * Painter that paints a soft gradient in windows vista blues with a small shadow at the top and a watermark.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected static class InspectionPaneBackground extends AbstractPainter<JXInspectionPane> implements UIResource {
        private final Color right = new Color(0xBBD9F0);
        private final Color middle = new Color(0xEAF9FD);
        private final Color left = new Color(0xF3FBFE);

        private final int shadowHeight = 8;
        private final Color top = new Color(0x66689BC6, true);
        private final Color bottom = new Color(0x689BC6, true);

        public InspectionPaneBackground() {
        }





        @Override
        protected void doPaint(Graphics2D g, JXInspectionPane object, int width, int height) {
            int gradWidth = width >> 1;
            if (gradWidth > 0) {
                BufferedImage img = ImageUtilities.createCompatibleImage(g, width, 1);
                Graphics2D g2 = img.createGraphics();
                g2.setPaint(new GradientPaint(0, 0, left, gradWidth, 0, middle, true));
                g2.fillRect(0, 0, gradWidth, 1);
                g2.setPaint(new GradientPaint(gradWidth, 0, middle, width, 0, right, true));
                g2.fillRect(gradWidth, 0, (width - gradWidth), 1);
                g2.dispose();
                g.drawImage(img, 0, 0, width, height, null);
                ImageUtilities.releaseImage(img);
            } else {
                g.setColor(middle);
                g.fillRect(0, 0, width, height);
            }

            BufferedImage img = ImageUtilities.createCompatibleTranslucentImage(g, 1, shadowHeight);
            Graphics2D g2 = img.createGraphics();
            g2.setPaint(new GradientPaint(0, 0, top, 0, shadowHeight, bottom, true));
            g2.fillRect(0, 0, 1, shadowHeight);
            g2.dispose();
            g.drawImage(img, 0, 0, width, shadowHeight, null);
            ImageUtilities.releaseImage(img);

            Painter<? super JXInspectionPane> watermarkPainter = XComponentUI.getUIProperty(object, "backgroundWatermarkPainter");
            if (watermarkPainter != null) {
                watermarkPainter.paint(g, object, width, height);
            }
        }
    }

}
