package org.jdesktop.swingx.plaf.motif;

import javax.swing.*;
import java.awt.Component;
import java.awt.Graphics;

public class MotifIcons {
    private MotifIcons() {
        super();
    }





    public static Icon createSliderTrackIcon() {
        return EMPTY_ICON;
    }





    private static final Icon EMPTY_ICON = new Icon() {
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }





        public int getIconWidth() {
            return 0;
        }





        public int getIconHeight() {
            return 0;
        }
    };
}
