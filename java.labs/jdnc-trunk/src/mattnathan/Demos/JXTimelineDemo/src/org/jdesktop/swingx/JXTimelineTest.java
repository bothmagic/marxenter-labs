/*
 * $Id: JXTimelineTest.java 2789 2008-10-14 15:51:42Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.SwingUtilities;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.timeline.DefaultViewRange;

/**
 * Simple visual check for the JXTimeline component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
@SuppressWarnings({"UtilityClassWithoutPrivateConstructor"})
public class JXTimelineTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }


    private static void createAndShowGUI(String[] args) {
//        final Rectangle r = new Rectangle();
        long now = System.currentTimeMillis();
        long dif = 100000000L;
//        JXTimeline tl = new JXTimeline() {
        JXTimeline tl = new JXTimeline(now, dif / 5, now - dif, now + dif + dif + dif) {
//        JXTimeline tl = new JXTimeline(450, 175, 0, 1000) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                long min = getMinimum();
                long max = getMaximum();
                long dif = max - min;
                long inc = 1000L * 60 * 60 * 24 * 365 * 100000000;
                if (dif < inc * 5L) {
                    inc = dif / 50L;
                }
                long start = locationToTimestamp(0, 0);
                start -= ((start / (double) inc) - (long) (start / (double) inc)) * inc;
                if (start < 0) {
                    start = 0;
                }
                long end = locationToTimestamp(getWidth(), getHeight());
//                System.out.println("inc = " + inc);
//                System.out.println("start = " + start);
//                System.out.println("end   = " + end);
                DateFormat df = SimpleDateFormat.getInstance();
                Date d = new Date();
                for (long i = start; i < end; i += inc) {
                    Rectangle r = getTimestampBounds(i);
                    if (r != null) {
                        if (r.width > 0) {
                            g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
                            if (getOrientation() == Orientation.VERTICAL) {
                                d.setTime(i);
                                g.drawString(df.format(d), r.x + r.width + 2, r.y + 6);
                            }
                        }
                    }
                    if (i + inc < i) {
                        break;
                    }
                }
            }
        };
        tl.setViewRange(new DefaultViewRange());

        class Adapter extends MouseAdapter {
            private Animator anim;
            @Override
            public void mouseClicked(MouseEvent e) {
                event(e, true);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                event(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            private void event(MouseEvent e) {
                event(e, false);
            }





            private void event(MouseEvent e, boolean animate) {
                JXTimeline tl = (JXTimeline) e.getSource();
                long timestamp = tl.locationToTimestamp(e.getPoint());

                if (animate) {
                    if (anim != null) {
                        anim.cancel();
                    }
                    anim = new Animator(200);
                    anim.addTarget(new PropertySetter(tl, "value", tl.getValue(), timestamp));
                    anim.setAcceleration(0.6f);
                    anim.setDeceleration(0.2f);
                    anim.setResolution(20);
                    anim.start();
                } else {
                    tl.setValue(timestamp);
                    tl.repaint();
                }
            }
        }
        Adapter ml = new Adapter();
        tl.addMouseListener(ml);
        //tl.addMouseMotionListener(ml);

//        tl.setOrientation(JXTimeline.Orientation.VERTICAL);

        new TestFrame("JXTimelineTest Test", tl).setVisible(true, 400, 400);
    }
}
