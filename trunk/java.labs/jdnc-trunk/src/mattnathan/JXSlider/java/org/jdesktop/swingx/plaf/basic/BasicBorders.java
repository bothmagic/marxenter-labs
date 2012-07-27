package org.jdesktop.swingx.plaf.basic;

import javax.swing.border.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.plaf.basic.*;

public class BasicBorders {
    private BasicBorders() {
        super();
    }





    private static Border sliderFocusBorder;
    public static Border createSliderFocusBorder() {
        if (sliderFocusBorder == null) {
            sliderFocusBorder = new SliderFocusBorder();
        }
        return sliderFocusBorder;
    }





    private static class SliderFocusBorder extends AbstractBorder {
        /**
         * This default implementation does no painting.
         * @param c the component for which this border is being painted
         * @param g the paint graphics
         * @param x the x position of the painted border
         * @param y the y position of the painted border
         * @param width the width of the painted border
         * @param height the height of the painted border
         */
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(c.getForeground());
            BasicGraphicsUtils.drawDashedRect(g, x, y, width, height);
        }





        /**
         * Re-initialises the insets parameter with this Border's current Insets.
         * @param c the component for which this border insets value applies
         * @param insets the object to be re-initialised
         * @return the <code>insets</code> object
         */
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }
            insets.set(1, 1, 1, 1);
            return insets;
        }





        /**
         * This default implementation returns a new <code>Insets</code>
         * instance where the <code>top</code>, <code>left</code>,
         * <code>bottom</code>, and
         * <code>right</code> fields are set to <code>0</code>.
         * @param c the component for which this border insets value applies
         * @return the new <code>Insets</code> object initialised to 0
         */
        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, null);
        }

    }
}
