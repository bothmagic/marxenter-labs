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
package org.jdesktop.swingx.plaf.macosx;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;
import org.jdesktop.swingx.util.RoundedColorizedRectangle2D;

/**
 * Custom AquaButtonBorder
 * @author Ryan Cuprak
 */
public class CustomAquaButtonBorder implements Border, UIResource {

    /**
     * Add a 3 pixel buffer around the component
     */
    private Insets insets = new Insets(3,3,3,3);

    /**
     * Inner boarder
     */
    private RoundedColorizedRectangle2D inner;

    /**
     * Middle border
     */
    private RoundedColorizedRectangle2D middle;

    /**
     * Outer border
     */
    private RoundedColorizedRectangle2D outer;

    /**
     * Radius
     */
    private int radius;

    /**
     * CustomAquaButtonBorder
     * @param radius - radius of the button
     */
    public CustomAquaButtonBorder(int radius) {
        this.radius = radius;
    }

    /**
     * Paints the border for he component.
     * @param c - button instance
     * @param g - graphics context
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - width of the component
     * @param height - height of the component
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if(inner == null) {
            inner = new RoundedColorizedRectangle2D(0,0,0,0,radius,UIManager.getColor("aquaFocusButton.innerColor"));
            middle = new RoundedColorizedRectangle2D(0,0,0,0,radius,UIManager.getColor("aquaFocusButton.middleColor"));
            outer = new RoundedColorizedRectangle2D(0,0,0,0,radius,UIManager.getColor("aquaFocusButton.outerColor"));
        }
        if(c.hasFocus()) {
            Graphics2D g2d = (Graphics2D)g;
            Object savedAliasingState = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            outer.setFrame(x,y,width-1,height-1);
            outer.drawBounds(g2d);
            middle.setFrame(x+1,y+1,width-3,height-3);
            middle.drawBounds(g2d);
            inner.setFrame(x+2,y+2,width-5,height-5);
            inner.drawBounds(g2d);
            inner.drawBounds(g2d);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,savedAliasingState);
        }
    }

    /**
     * Returns the insets
     * @param c - component
     * @return Insets
     */
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    /**
     * Returns true - the order is opaque
     * @return true
     */
    public boolean isBorderOpaque() {
        return true;
    }
}
