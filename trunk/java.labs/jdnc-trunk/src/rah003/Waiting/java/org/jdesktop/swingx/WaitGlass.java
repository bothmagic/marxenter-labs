package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.Animator.EndBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXBusyLabel.Direction;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.image.ColorTintFilter;
import org.jdesktop.swingx.image.FastBlurFilter;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;

public class WaitGlass extends JXPanel {

    private JPanel busyPanel = new JPanel(new GridLayout(1, 2));
    private final FadePanel fadePanel;
    private Animator a;

    public WaitGlass() {
        setLayout(null);
        MattePainter bkground = new MattePainter(PaintUtils.BLUE_EXPERIENCE, true);
        setBackgroundPainter(bkground);
        busyPanel.setOpaque(false);
        JXBusyLabel busy = getBusy();
        busyPanel.add(busy);
        JXBusyLabel busy2 = getBusy();
        busyPanel.add(busy2);
        add(busyPanel);
        fadePanel = new FadePanel();
        add(fadePanel);
        busy.setBusy(true);
        busy2.setBusy(true);
        busy2.setDirection(Direction.LEFT);
        setOpaque(false);
    }

    private JXBusyLabel getBusy() {
        JXBusyLabel label = new JXBusyLabel(new Dimension(100, 96));
        BusyPainter painter = new BusyPainter(new Rectangle2D.Float(0, 0, 12.0f, 1), new Ellipse2D.Float(14.5f, 14.5f,
                67.0f, 18.09f));
        painter.setTrailLength(20);
        painter.setPoints(50);
        painter.setFrame(29);
        label.setPreferredSize(new Dimension(100, 96));
        label.setIcon(new EmptyIcon(100, 96));
        label.setBusyPainter(painter);
        painter.setHighlightColor(new Color(44, 61, 146).darker());
        painter.setBaseColor(new Color(168, 204, 241).brighter());
        return label;
    }

    @Override
    public void doLayout() {
        int w = getWidth();
        int h = getHeight() - 300;
        Dimension d = busyPanel.getPreferredSize();
        busyPanel.setBounds((w - d.width) / 2, (h - d.height) / 2, d.width, d.height);
        int dh = d.height;
        if (fadePanel != null) {
            fadePanel.doLayout();
            d = fadePanel.getPreferredSize();
            fadePanel.setBounds((w - d.width) / 2, (h + dh) / 2 + 10, d.width, d.height);
        }
    }

    public void setText(final String string) {
        if (a != null && a.isRunning()) {
            a.stop();
            System.out.println("too fast");
        }
        try {
            a = PropertySetter.createAnimator(1900, fadePanel, "fraction", 0f, 1f, 0f);
            a.setEndBehavior(EndBehavior.HOLD);
            a.addTarget(new TimingTargetAdapter() {
                boolean done = false;

                @Override
                public void begin() {
                    super.begin();
                    done = false;
                    fadePanel.setText(string);
                }

                @Override
                public void timingEvent(float f) {
                    if (f > 0.5 && !done) {
                        fadePanel.setFall(true);
                        done = true;
                    }
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }

        a.start();
    }

    public static class FadePanel extends JPanel {
        private JXPanel p1 = new JXPanel(new BorderLayout());
        private JXLabel l1 = new JXLabel();
        private int fontSize = l1.getFont().getSize();
        private Font font = l1.getFont();
        private ColorTintFilter ctf = new ColorTintFilter(new Color(44, 61, 146).darker(), 1f);
        private float fraction;
        private boolean fall;

        public FadePanel() {
            super(null);
            setOpaque(false);
            p1.setOpaque(false);
            p1.add(l1);
            add(p1);
            l1.setLineWrap(true);
        }

        @Override
        public void doLayout() {
            p1.doLayout();
            Dimension d1 = p1.getPreferredSize();
            p1.setBounds(0, fall ? getHeight() - (int) (getHeight() * fraction) : 0, d1.width, d1.height);
        }

        public void setFraction(float f) {
            fraction = f;
            AbstractPainter p = (AbstractPainter) l1.getForegroundPainter();
            if (!fall) {
                l1.setFont(font.deriveFont(fontSize * 2 * (fall ? f * f : f)));
                p.setFilters(ctf);
            } else {
                p.setFilters(new FastBlurFilter(1 + ((int) (10 * fraction)) / 5), ctf);
            }

            doLayout();
            p1.setAlpha(f);
            if (f < 0.1) {
                fall = false;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension d = p1.getPreferredSize();
            return new Dimension(d.width, d.height * 9);
        }

        @Override
        public Dimension getSize() {
            Dimension d = p1.getPreferredSize();
            return new Dimension(d.width, d.height * 9);
        }

        @Override
        public Dimension getSize(Dimension rv) {
            Dimension d = p1.getPreferredSize();
            rv.width = d.width;
            rv.height = d.height * 9;
            return rv;
        }

        public void setText(String text) {
            l1.setText(text);
        }

        public void setFall(boolean fall) {
            this.fall = fall;
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setPreferredSize(new Dimension(400, 600));
        WaitGlass glass = new WaitGlass();
        f.setGlassPane(glass);
        glass.setVisible(true);

        f.pack();
        f.setVisible(true);
        int x = 1;
        while (f.isShowing()) {
            try {
                Thread.currentThread().sleep(2000);
                glass.setText("Waiting for " + x++ + " seconds.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
