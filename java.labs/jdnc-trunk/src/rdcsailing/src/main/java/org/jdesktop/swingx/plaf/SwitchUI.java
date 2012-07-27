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
package org.jdesktop.swingx.plaf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import org.jdesktop.swingx.JXSwitch;
import org.jdesktop.swingx.util.RoundedColorizedRectangle2D;

/**
 * UI Delegate for the Switch UI component.
 * @author Ryan Cuprak
 */
public class SwitchUI extends ButtonUI {

    /**
     * Height of the switch
     */
    public static final int HEIGHT = 28;

    /**
     * Width of the switch
     */
    public static final int WIDTH = 94;

    /**
     * Radius of the rounded button
     */
    public static final int RADIUS = 5;

    /**
     * Percentage of the control that is the switch
     */
    protected float LEVER_PERCENT = 0.43f;

    /**
     * Percentage of the control that the "On" side consumes
     */
    protected float ON_WIDTH_PERCENT = 0.59f;

    /**
     * Gradient paint used to paint the button
     */
    private GradientPaint leverPaint;

    /**
     * Instantiates a new switch UI
     */
    private static SwitchUI switchUI;

    /**
     * Returns the width of the lever
     * @param component - component
     * @return lever width
     */
    public int getLeverWidth(JComponent component) {
        Insets in = component.getInsets();
        return (int)(LEVER_PERCENT * (component.getWidth() - in.right - in.left));
    }

    /**
     * Returns the width of the on the "on"
     * @param component - component
     * @return width of on
     */
    public int getOnWidth(JComponent component) {
        Insets in = component.getInsets();
        return (int)((ON_WIDTH_PERCENT) * component.getWidth() - in.right - in.left) + RADIUS;
    }

    /**
     * Creates the ComponentUI - only one is instantiated. All state resides in the Switch
     * @param component - com
     * @return ComponentUI
     */
    public static ComponentUI createUI(JComponent component) {
        if(component instanceof JXSwitch) {
            if(switchUI == null) {
                switchUI = new SwitchUI();
            }
            return switchUI;
        }
        throw new IllegalArgumentException("Expected an instance of Switch.");
    }


    /**
     * Copies values from UIDefaults to the component.
     * Values provided by an editor are then overwritten
     * @param c - component
     */
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        if(!(c instanceof JXSwitch)) {
            throw new IllegalArgumentException("Expected switch component.");
        }
        c.setBorder(UIManager.getBorder("Switch.border"));
    }

    /**
     * Un-installs listeners and properties.
     * This is invoked when we switch look and feels
     * @param c - component
     */
    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
    }

    /**
     * Paints the component
     * @param g - graphics context
     * @param c - component - component
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Insets is = c.getInsets();
        JXSwitch switchComponent = (JXSwitch)c;
        SwitchLever lever = switchComponent.getSwitchLever();
        int height = c.getHeight() - is.bottom - is.top;
        int width = c.getWidth() - is.right - is.left;
        RoundedColorizedRectangle2D buttonOutline = new RoundedColorizedRectangle2D(0,0,width,height,RADIUS,UIManager.getColor("Switch.outlineTop"),
                UIManager.getColor("Switch.outlineBottom"),UIManager.getColor("Switch.outlineRight"),UIManager.getColor("Switch.outlineLeft"));
        RoundedColorizedRectangle2D switchOutline = new RoundedColorizedRectangle2D(0,0,getOnWidth(c),height,RADIUS,UIManager.getColor("Switch.leverTop"),
                UIManager.getColor("SwitchLever.bottom"),UIManager.getColor("Switch.leverRight"),UIManager.getColor("Switch.leverLeft"));
        leverPaint = new GradientPaint(0,0,UIManager.getColor("Switch.paintGradientStart"),0,height,UIManager.getColor("Switch.paintGradientStop"));
        Graphics2D g2d = (Graphics2D)g;
        // Translate over - to observe insets
        g2d.translate(is.left,is.top);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0,0,UIManager.getColor("Switch.backgroundGradientStart"),0,
                height,UIManager.getColor("Switch.backgroundGradientStop"));
        // Button background and border
        Paint pt = g2d.getPaint();
        g2d.setPaint(gp);
        g2d.fill(buttonOutline);
        buttonOutline.drawBounds(g2d);
        g2d.setPaint(pt);
        if(lever.getX() > 0) {
            Paint onPaint = new GradientPaint(0,0,UIManager.getColor("Switch.onPaintGradientStart"),0,height,
                    UIManager.getColor("Switch.onPaintGradientStop"));
            g2d.setPaint(onPaint);
            Rectangle clipping = new Rectangle(0,0,lever.getX()+ RADIUS,height);
            Shape savedClip = g2d.getClip();
            g2d.setClip(clipping);
            g2d.fill(switchOutline);
            switchOutline.drawBounds(g2d);
            clipping = new Rectangle(1,1,lever.getX(),height);
            g2d.setClip(clipping);
            String onTxt = UIManager.getString("Switch.onLabel");
            int x = (lever.getOnPosition() / 2) - (g2d.getFontMetrics().stringWidth(onTxt)/2);
            int y = ((height/2 + (g2d.getFontMetrics().getHeight()/2))) - g2d.getFontMetrics().getDescent();
            g2d.setColor(Color.WHITE);
            Font ft = g2d.getFont().deriveFont(Font.BOLD,14);
            g2d.setFont(ft);
            g2d.drawString(onTxt,x,y);
            g2d.setClip(savedClip);
        }
        drawLever(g2d,switchComponent,height);
        if(lever.getX() < lever.getOnPosition()) {
            Font ft = g2d.getFont().deriveFont(Font.BOLD,14);
            g2d.setFont(ft);
            int y = ((height/2 + (g2d.getFontMetrics().getHeight()/2))) - g2d.getFontMetrics().getDescent();
            int offCenter = (width - lever.getWidth())/2 + lever.getWidth();
            String offLabel = UIManager.getString("Switch.offLabel");
            Shape savedClip = g2d.getClip();
            g2d.setClip(buttonOutline);
            int x = lever.getX() + (offCenter - (g2d.getFontMetrics().stringWidth(offLabel)/2));
            g2d.setColor(UIManager.getColor("Switch.offLabelTextColor"));
            g2d.drawString(offLabel,x,y);
            g2d.setClip(savedClip);
        }
        if(!switchComponent.isEnabled()) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,width,height);
        }
        g2d.translate(-is.top,-is.bottom);
    }

    /**
     * Returns the preferred size of the component
     * @return Dimension
     */
    @Override
    public Dimension getPreferredSize(JComponent component) {
        Insets insets = component.getInsets();
        int y = insets.top + insets.bottom;
        int x = insets.left + insets.right;
        return new Dimension(WIDTH+1+x,HEIGHT+1+y);
    }

    /**
     * Draws the lever:
     * Paints the
     * <ol>
     *  <li>Fills the switch
     *  <li>Draws the border
     * </ol>
     * @param g2d - graphics context
     * @param switchComponent - switch component
     * @param height - height of the component
     */
    public void drawLever(Graphics2D g2d, JXSwitch switchComponent,int height) {
        SwitchLever switchLever = switchComponent.getSwitchLever();
        g2d.translate(switchLever.getX(),0);
        Paint pt = g2d.getPaint();
        g2d.setPaint(leverPaint);
        RoundedColorizedRectangle2D leverOutline = new RoundedColorizedRectangle2D(0,0,switchLever.getWidth(),height,RADIUS,UIManager.getColor("Switch.levelTop"),
                UIManager.getColor("Switch.levelBottom"),UIManager.getColor("Switch.levelRight"),UIManager.getColor("Switch.levelLeft"));
        g2d.fill(leverOutline);
        leverOutline.drawBounds(g2d);
        g2d.setPaint(pt);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(RADIUS,1,getLeverWidth(switchComponent)-RADIUS,1);
        g2d.translate(-switchLever.getX(),0);
    }

}
