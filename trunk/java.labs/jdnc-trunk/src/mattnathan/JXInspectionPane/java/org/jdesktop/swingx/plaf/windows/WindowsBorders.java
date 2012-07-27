package org.jdesktop.swingx.plaf.windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

import org.jdesktop.swingx.image.ImageUtilities;

public class WindowsBorders {
    private WindowsBorders() {
        super();
    }





    private static Border inspectionPaneBorder;
    public static Border createInspectionPaneBorder() {
        if (inspectionPaneBorder == null) {
            inspectionPaneBorder = new InspectionPaneBorder();
        }
        return inspectionPaneBorder;
    }





    private static class InspectionPaneBorder extends AbstractBorder {
        private static final Color top = new Color(1, 1, 1, 0.1f);
        private static final Color middle = new Color(1, 1, 1, 0.6f);

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, super.getBorderInsets(c));
        }





        @Override
        public boolean isBorderOpaque() {
            return false;
        }





        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            BufferedImage img = ImageUtilities.createCompatibleTranslucentImage(g, 1, height);
            Graphics2D g2 = img.createGraphics();

            g2.setPaint(new GradientPaint(0, 0, top, 0, height / 2f, middle, true));
            g2.fillRect(0, 0, 1, height);

            g2.dispose();
            g.drawImage(img, x, y, null);
            g.drawImage(img, x + width - 1, y, null);
            ImageUtilities.releaseImage(img);
        }





        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(4, 4, 4, 4);
            return insets;
        }

    }
}
