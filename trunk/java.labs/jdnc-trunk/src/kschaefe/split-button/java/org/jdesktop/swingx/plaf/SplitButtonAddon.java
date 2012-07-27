package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;

import org.jdesktop.swingx.JXSplitButton;
import org.jdesktop.swingx.plaf.basic.BasicSplitButtonUI;
import org.jdesktop.swingx.plaf.metal.MetalSplitButtonUI;

public class SplitButtonAddon extends AbstractComponentAddon {
    private static class ArrowIcon implements Icon {
        int posX, posY;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            // Draw the arrow
            posX = x;
            posY = y;
            int newY = (getIconHeight() - 5) / 2;
            int xPoints[] = new int[3];
            int yPoints[] = new int[3];
            Color old = g.getColor();
            //FIXME use UIManagerExt for these properties and finalize
            g.setColor(UIManager.getLookAndFeelDefaults().getColor("Button.shadow"));
            if (c.getComponentOrientation().isLeftToRight()) {
                //arrowLabel.setAlignmentX(1.0f);
                xPoints[0] = x + 4;
                yPoints[0] = newY;
                xPoints[1] = x + 10;
                yPoints[1] = newY;
                xPoints[2] = x + 7;
                yPoints[2] = newY + 4;
                g.drawLine(x, y + 5, x, getIconHeight() - 5);
                g.setColor(UIManager.getLookAndFeelDefaults().getColor("Button.highlight"));
                g.drawLine(x + 1, y + 5, x + 1, getIconHeight() - 5);
            } else {
                //arrowLabel.setAlignmentX(0.0f);
                xPoints[0] = x + 6;
                yPoints[0] = newY;
                xPoints[1] = x + 12;
                yPoints[1] = newY;
                xPoints[2] = x + 9;
                yPoints[2] = newY + 4;
                g.drawLine(x + 15, y + 5, x + 15, getIconHeight() - 5);
                g.setColor(UIManager.getLookAndFeelDefaults().getColor("Button.highlight"));
                g.drawLine(x + 16, y + 5, x + 16, getIconHeight() - 5);
            }
            g.setColor(Color.black);
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(old);
        }
        public int getIconWidth() {
            return 16;
        }
        public int getIconHeight() {
            //TODO compute this value to be the button height
            int ret = 16;
            return ret;
        }
    }

    public SplitButtonAddon() {
        super("AbstractSplitButton");
    }
    
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXSplitButton.uiClassID, BasicSplitButtonUI.class.getName());
        defaults.add("SplitButton.slaveIcon", new IconUIResource(new ArrowIcon()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addMetalDefaults(addon, defaults);
        
        defaults.add(JXSplitButton.uiClassID, MetalSplitButtonUI.class.getName());
    }
}
