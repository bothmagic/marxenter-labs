package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.icon.AbstractScalableIcon;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.JXSlider;
import org.jdesktop.swingx.slider.IconSliderTrackRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Dimension;

/**
 * Generated JavaDoc Comment.
 *
 * @author <a href="mailto:matt.nathan@paphotos.com">Matt Nathan</a>
 */
public class BasicIcons {

    private static ScalableIcon sliderTrackIcon;





    private BasicIcons() {
    }





    public static ScalableIcon createSliderTrackIcon() {
        if (sliderTrackIcon == null) {
            sliderTrackIcon = new SliderTrackIcon();
        }
        return sliderTrackIcon;
    }





    private static class SliderTrackIcon extends AbstractScalableIcon {

        private int size;
        private Color shadowColor;
        private Color highlightColor;





        public SliderTrackIcon() {
            size = LookAndFeelUtilities.getInt("XSlider.trackSize", 4);

            highlightColor = LookAndFeelUtilities.getColor("XSlider.highlight", "Slider.highlight");
            if (highlightColor == null) {
                highlightColor = new Color(0xFFFFFF);
            }
            shadowColor = LookAndFeelUtilities.getColor("XSlider.shadow", "Slider.shadow");
            if (shadowColor == null) {
                shadowColor = new Color(0x808080);
            }
        }





        protected Color getShadowColor() {
            return shadowColor;
        }





        protected Color getHighlightColor() {
            return highlightColor;
        }





        @Override
        protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height) {
            /**
             * Unfortunately there is no easy way to paint only the track of a JSlider, even though BasicSliderUI
             * has a paintTrack method. This is mostly due to the validation of the bounds of the track which occurs in
             * the paint method and also Synth look and feels (ala GTK) ignore this method completely, as does CDE/Motif
             * and as such can't be relied upon.
             *
             * The other method of painting the track is to move the thumb and paint the whole component. BasicSliderUI
             * provides the setThumbLocation method but again the Synth look and feels ignore this. Also the track on
             * the Ocean themed cross platform look and feel paints incorrectly if the thumb is not present to obscure
             * the discrepancies.
             *
             * For these reasons This implementation simply uses the BasicSliderUIs method of painting the track.
             */
            int cw = width;
            int ch = height;

            g.translate(x, y);

            g.setColor(getShadowColor());
            g.drawLine(0, 0, cw - 1, 0);
            g.drawLine(0, 1, 0, ch - 1);

            g.setColor(getHighlightColor());
            g.drawLine(0, ch - 1, cw - 1, ch - 1);
            g.drawLine(cw - 1, 1, cw - 1, ch - 1);

            g.setColor(Color.BLACK);
            boolean horizontal = getOrientation(c) == JXSlider.Orientation.HORIZONTAL;
            if (horizontal) {
                g.drawLine(1, 1, cw - 2, 1);
            } else {
                g.drawLine(1, 1, 1, ch - 2);
            }
            g.translate(-x, -y);

        }





        public int getIconWidth() {
            return size;
        }





        public int getIconHeight() {
            return size;
        }





        protected JXSlider.Orientation getOrientation(Component c) {
            JXSlider.Orientation result = JXSlider.Orientation.HORIZONTAL;
            if (c instanceof JXSlider) {
                result = ((JXSlider) c).getOrientation();
            } else if (c instanceof IconSliderTrackRenderer) {
                result = ((IconSliderTrackRenderer) c).getSlider().getOrientation();
            }

            return result;
        }





        /**
         * Uses the JXSliders orientation property to stretch the track to fit the area only along the required axis.
         *
         * @param c      The component calling the paint.
         * @param width  The target width for the icon.
         * @param height the target height for the icon.
         * @param result The results will be placed in here.
         * @return The dimension the icon will support.
         */
        @Override
        public Dimension fitInto(Component c, int width, int height, Dimension result) {
            if (result == null) {
                result = new Dimension();
            }

            switch (getOrientation(c)) {
                case VERTICAL:
                    result.height = height;
                    result.width = Math.min(getIconWidth(), width); // === track width
                    break;
                case HORIZONTAL:
                default:
                    result.width = width;
                    result.height = Math.min(getIconHeight(), height); // === track height
                    break;
            }

            return result;
        }

    }
}
