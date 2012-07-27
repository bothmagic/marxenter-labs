package org.jdesktop.swingx.demo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;

import org.jdesktop.swingx.graphics.DropShadowGraphics2D;

public class DropShadowGraphicsDemo extends JFrame {
    public DropShadowGraphicsDemo() {
        super("Drop Shadow Graphics");

        add(new ShadowCanvas());

        pack();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static class ShadowCanvas extends JComponent {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 400);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                      RenderingHints.VALUE_ANTIALIAS_ON);

            DropShadowGraphics2D g2 = new DropShadowGraphics2D(graphics);
            g2.setColor(Color.RED);
            g2.drawLine(50, 50, 120, 120);
            g2.setColor(Color.BLUE);
            g2.drawString("Hello World", 200, 100);
            g2.drawRect(350, 140, 20, 20);
            g2.setColor(Color.GREEN);
            g2.fillRect(300, 220, 100, 100);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DropShadowGraphicsDemo().setVisible(true);
            }
        });
    }
}
