package org.jdesktop.swingx.plaf.gtk;

import org.jdesktop.swingx.icon.AbstractScalableIcon;
import org.jdesktop.swingx.util.ScalePolicy;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.SystemColor;

public class GTKIcons {
    private GTKIcons() {
        super();
    }





    private static Icon sliderTrackIcon;
    public static Icon createSliderTrackIcon() {
        if (sliderTrackIcon == null) {
            sliderTrackIcon = new SliderTrackIcon();
        }
        return sliderTrackIcon;
    }





    private static class SliderTrackIcon extends AbstractScalableIcon {
        public SliderTrackIcon() {
            super(ScalePolicy.BOTH);
        }





        @Override
        protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
            g2.setColor(SystemColor.control);
            g2.fillRect(x, y, width, height);
            g2.setColor(SystemColor.controlShadow);
            g2.drawRect(x, y, width - 1, height - 1);
        }





        public int getIconWidth() {
            return 5;
        }





        public int getIconHeight() {
            return 5;
        }

    }
}
