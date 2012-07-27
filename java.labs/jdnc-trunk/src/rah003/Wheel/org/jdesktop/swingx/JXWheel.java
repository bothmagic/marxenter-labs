package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.Animator.EndBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class JXWheel extends JComponent {
    private static final int PREF_HEIGHT = 100;
    private static final int DEF_TICK_COUNT = 9;

    private int tickCount = 9;

    private double shift;

    private BufferedImage[] images;

    public JXWheel() {
        this(null, DEF_TICK_COUNT);
    }

    public JXWheel(BufferedImage[] images, int count) {
        setOpaque(false);
        setPreferredSize(new Dimension(20, PREF_HEIGHT));
        setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY, 1), BorderFactory.createLoweredBevelBorder()));
        addMouseListener(new MouseAdapter() {
            Animator a;
            int startY;
            int stopY;
            long startT;
            long stopT;

            public void mousePressed(MouseEvent e) {
                startY = e.getYOnScreen();
                startT = System.currentTimeMillis();
            }

            public void mouseReleased(MouseEvent e) {
                stopY = e.getYOnScreen();
                stopT = System.currentTimeMillis();
                int dist = stopY - startY;
                long dur = stopT - startT;
                final JXWheel w = (JXWheel) e.getSource();
                if (a != null && a.isRunning()) {
                    a.stop();
                }
                if (dist == 0 || dur == 0) {
                    return;
                }
                double speed = Math.abs((double) dist / dur);
                a = new Animator((int) dur + 1000);

                a.addTarget(new PropertySetter(w, "shift", w.getShift(), w.getShift() + Math.signum(dist)
                        * (Math.PI * speed)));
                a.setInterpolator(new SplineInterpolator(0, 0.2f, 0, 1));
                a.setEndBehavior(EndBehavior.HOLD);
                a.start();

            }
        });

        this.tickCount = count;
        this.images = images;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle b = getBounds();
        Insets i = getInsets();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(i.left, i.top, b.width - i.left - i.right, b.height - i.top - i.bottom);
        int h = b.height - i.top - i.bottom;
        double rad = (double) h / 2.0;
        for (int m = -tickCount; m < tickCount; m++) {
            // TODO: should paint from top to middle and from bottom to middle to ensure middle one is on top of others
            double tau = (Math.PI / 2 - shift - (m + .5) * Math.PI / tickCount);
            if (tau > Math.PI / 2 || tau < -Math.PI / 2) {
                continue;
            }
            int y = (int) (i.top + h - rad * (1 + Math.sin(tau)));
            // drawing
            if (this.images == null || this.images.length == 0) {
                drawDefault(b,tau,i,y,g2d);
            } else {
                drawIcons((m + tickCount) % images.length, b,tau,i,y,g2d);
            }
        }
        g2d.dispose();
    }

    protected void drawIcons(int iconIdx, Rectangle b, double tau, Insets i, int y, Graphics2D g2d) {
        BufferedImage image = images[iconIdx];
        double w = (double) (b.width - i.right - i.left)/image.getWidth();
        double height = (double)image.getHeight() * (Math.cos(tau)/image.getHeight());
        height *= w; 
        AffineTransform aff = new AffineTransform();
        aff.setToScale(w, height);
        g2d.drawImage(images[iconIdx], new AffineTransformOp(aff,null), i.left, (int)(y - height/2));
    }

    protected void drawDefault(Rectangle b, double tau, Insets i, int y, Graphics2D g2d) {
        int height = (int) Math.round(b.height / 20 * Math.cos(tau));
        int arc = Math.min(10, height / 2);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRoundRect(i.left + b.height / 50, y + 1, b.width - i.right - i.left - 1, height, arc, arc);// (,
                                                                                                            // y);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(i.left, y - height / 2, b.width - i.right - i.left - 1, height, arc, arc);// (, y);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(i.left, y - height / 2, b.width - i.right - i.left - 1, height, arc, arc);// (, y);
    }

    /**
     * @return the shift
     */
    public double getShift() {
        return shift;
    }

    /**
     * Valid values are anything between 0 to PI. All values exceeding this range will be % to fit in the range.
     * 
     * @param shift the shift to set
     */
    public void setShift(double shift) {
        int sig = (tickCount % 2 == 0) ? 2 : 1;
        if (shift > Math.PI) {
            setShift(shift - Math.PI + sig * Math.PI/tickCount);
        } else if (shift < 0) {
            setShift(this.shift = shift + Math.PI - sig * Math.PI/tickCount);
        } else {
            this.shift = shift;
        }
        repaint();
    }

    /**
     * @return the tickCount
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * @param tickCount the tickCount to set
     */
    public void setTickCount(int tickCount) {
        this.tickCount = tickCount;
    }

    public BufferedImage getScreenshot(Rectangle r) {
        BufferedImage img = GraphicsUtilities.createCompatibleImage(r.width, r.height);
        Graphics g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, r.width, r.height);
        g.translate(0, -r.y);
        this.paintComponent(g);
        g.dispose();
        return img;
    }
}
