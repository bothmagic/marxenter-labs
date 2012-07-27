package org.jdesktop.swingx.painter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.*;
import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * Simple demo showing a possible application of the LensFlarePainter.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class LensFlarePainterDemo extends JXComponent implements MouseMotionListener, MouseListener {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestFrame("LensFlarePainter Demo", new LensFlarePainterDemo(), Color.BLACK).setVisible(true);
            }
        });
    }





    private LensFlarePainter<LensFlarePainterDemo> painter;
    private JButton startAnimation;





    public LensFlarePainterDemo() {
        updateUI();
        init();
    }





    private void start() {
        Animator animator = new Animator(2000);
        Evaluator<LocationPolicy> evaluator = new Evaluator<LocationPolicy>() {
            @Override
            public LocationPolicy evaluate(LocationPolicy locationPolicy, LocationPolicy locationPolicy1, float v) {
                float h1 = locationPolicy.getHorizontalAlignment();
                float v1 = locationPolicy.getVerticalAlignment();
                float h2 = locationPolicy1.getHorizontalAlignment();
                float v2 = locationPolicy1.getVerticalAlignment();
                return new LocationPolicy(h1 + (h2 - h1) * v, v1 + (v2 - v1) * v);
            }
        };
        KeyFrames kf = new KeyFrames(KeyValues.create(evaluator,
                painter.getFlareLocation(),
                new LocationPolicy(0.05f, 0.55f),
                new LocationPolicy(0.95f, 0.45f)),
                new KeyTimes(0, 0.02f, 1f));
        animator.addTarget(new PropertySetter(painter, "flareLocation", kf));
        animator.addTarget(new TimingTargetAdapter() {
            @Override
            public void timingEvent(float v) {
                repaint();
            }





            @Override
            public void begin() {
                startAnimation.setEnabled(false);
            }





            @Override
            public void end() {
                startAnimation.setEnabled(true);
            }
        });
        animator.setAcceleration(0.3f);
        animator.setDeceleration(0.25f);
        animator.setDuration(10000);
        animator.start();
    }





    private void init() {
        painter = new LensFlarePainter<LensFlarePainterDemo>();
        setPreferredSize(new Dimension(800, 600));

        JXPanel foreground = new JXPanel();
        foreground.setOpaque(false);
        foreground.setBackgroundPainter(painter.getForegroundPainter());
        setLayout(new BorderLayout());

        JXComponent background = new JXComponent() {
            {
                updateUI();
            }
        };
        background.setBackgroundPainter(painter.getBackgroundPainter());
        background.setForegroundPainter(new TextPainter<Object>("Lens Flare Painter", foreground.getFont().deriveFont(62f), Color.BLACK));
        background.setLayout(new BorderLayout());
        background.add(foreground, BorderLayout.CENTER);
        background.addMouseListener(this);
        background.addMouseMotionListener(this);

        startAnimation = new JButton(new AbstractAction("Start Animation") {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });

        add(background, BorderLayout.CENTER);
        add(startAnimation, BorderLayout.SOUTH);
        setBackgroundPainter(new StarPainter<LensFlarePainterDemo>());
    }





    protected void setFlareLocation(MouseEvent e) {
        if (startAnimation.isEnabled()) {
            Component source = (Component) e.getSource();
            painter.setFlareLocation(new LocationPolicy(e.getX() / (float) source.getWidth(), e.getY() / (float) source.getHeight()));
            repaint();
        }
    }





    public void mouseDragged(MouseEvent e) {
        setFlareLocation(e);
    }





    public void mouseMoved(MouseEvent e) {
    }





    public void mouseClicked(MouseEvent e) {
    }





    public void mousePressed(MouseEvent e) {
        setFlareLocation(e);
    }





    public void mouseReleased(MouseEvent e) {
    }





    public void mouseEntered(MouseEvent e) {
    }





    public void mouseExited(MouseEvent e) {
    }





    private static class StarPainter<T> extends AbstractPainter<T> {
        private int count = 500;
        private int variation = 15;
        private SoftReference<BufferedImage[]> variations;

        private int minColor = 150;
        private int maxColor = 255;
        private int minSize = 2;
        private int maxSize = 16;
        private float glareThreshold = 0.5f;
        private float minAlpha = 0.2f;
        private float maxAlpha = 1f;

        private final Line2D lineCache = new Line2D.Float();
        private final Ellipse2D ellipseCache = new Ellipse2D.Float();





        @Override
        protected void doPaint(Graphics2D g, T t, int width, int height) {
            Random r = new Random(0);
            BufferedImage[] variations = this.variations == null ? null : this.variations.get();
            if (variations == null) {

                variations = new BufferedImage[variation];

                for (int i = 0; i < variation; i++) {
                    int size = minSize + r.nextInt(maxSize - minSize);
                    Color color = new Color(
                            minColor + r.nextInt(maxColor - minColor),
                            minColor + r.nextInt(maxColor - minColor),
                            minColor + r.nextInt(maxColor - minColor));
                    float glare = r.nextFloat();
                    BufferedImage img = g.getDeviceConfiguration().createCompatibleImage(size, size, Transparency.TRANSLUCENT);
                    Graphics2D imgg = img.createGraphics();
                    imgg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    imgg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                    paintStar(imgg, size, size, color, glare > glareThreshold);
                    imgg.dispose();
                    variations[i] = img;
                }
                this.variations = new SoftReference<BufferedImage[]>(variations);

                r.setSeed(0);
            }

            Composite old = g.getComposite();
            for (int i = 0; i < count; i++) {
                int index = r.nextInt(variation);
                int x = (int) (r.nextFloat() * width);
                int y = (int) (r.nextFloat() * height);
                float alpha = minAlpha + (r.nextFloat() * (maxAlpha - minAlpha));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g.drawImage(variations[index], x, y, null);
            }
            g.setComposite(old);
        }





        private void paintStar(Graphics2D g, int w, int h, Color color, boolean showGlare) {
            if (w <= 3 || h <= 3) {
                g.setColor(color);
                ellipseCache.setFrame(0.5, 0.5, w - 1, h - 1);
                g.fill(ellipseCache);
            } else {
                RadialGradientPaint paint = new RadialGradientPaint(
                        w / 2f, h / 2f, w / 2f,
                        new float[]{0f, 1f},
                        new Color[]{color, new Color(1f, 1f, 1f, 0f)},
                        MultipleGradientPaint.CycleMethod.NO_CYCLE);
                g.setPaint(paint);
                if (showGlare) {
                    lineCache.setLine(w / 2f, 0.5, w / 2f, h - 1);
                    g.draw(lineCache);
                    lineCache.setLine(0.5, h / 2f, w - 1, h / 2f);
                    g.draw(lineCache);
                }
                float oval = w * 0.2f;
                ellipseCache.setFrame((w - oval) / 2f, (h - oval) / 2f, oval, oval);
                g.fill(ellipseCache);
            }
        }
    }
}
