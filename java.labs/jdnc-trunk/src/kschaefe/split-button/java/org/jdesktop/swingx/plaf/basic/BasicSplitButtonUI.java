package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonUI;

import org.jdesktop.swingx.JXSplitButton;

/**
 * An implementation of the {@code SplitButtionUI} for the
 * {@code BasicLookAndFeel}.
 */
public class BasicSplitButtonUI extends BasicButtonUI {
    private static class ArrowIcon implements Icon, UIResource {
        private final int margin = 3;

        private final int width = 7;

        private final int height = 4;

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int newY = (c.getHeight() - height) / 2;
            int xPoints[] = new int[3];
            int yPoints[] = new int[3];

            Color old = g.getColor();
            g.setColor(Color.BLACK);

            xPoints[0] = x + margin;
            yPoints[0] = newY;
            xPoints[1] = x + margin + width;
            yPoints[1] = newY;
            xPoints[2] = x + margin + width / 2;
            yPoints[2] = newY + height;

            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(old);
        }

        public int getIconWidth() {
            return width + margin * 2;
        }

        public int getIconHeight() {
            return height + margin * 2;
        }
    }

    private static final BasicSplitButtonUI ui = new BasicSplitButtonUI();

    private JXSplitButton button;

    public static ComponentUI createUI(JComponent c) {
        return ui;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        button = (JXSplitButton) c;

        installComponents();

    }

    protected void installComponents() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallUI(JComponent c) {
        uninstallListeners();
        uninstallDefaults();
        uninstallComponents();

        button.setLayout(null);
        button = null;
        super.uninstallUI(c);
    }

    protected void uninstallListeners() {
    }

    protected void uninstallDefaults() {

    }

    protected void uninstallComponents() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        Dimension d = super.getPreferredSize(c);
        
        d.width += ((JXSplitButton) c).getDropDownIcon().getIconWidth();
        
        return d;
    }
}
