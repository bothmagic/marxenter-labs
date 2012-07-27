/*
 * $Id: VistaStopwatchPainter.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.Icon;

import java.util.Calendar;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines a vista style stopwatch painter. This simply adds an inner clock to display milliseconds.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class VistaStopwatchPainter extends VistaClockPainter {
    private final Icon millisecondIcon;
    public VistaStopwatchPainter() {
        super();
        PainterIcon<JXClock> millisecondIcon = new PainterIcon<JXClock>(80, 80, new MillisecondPainter());
        millisecondIcon.setScalePolicy(ScalePolicy.NONE);
        this.millisecondIcon = millisecondIcon;
    }





    @Override
    protected void paintBackground(Graphics2D g, JXClock c, int width, int height) {
        super.paintBackground(g, c, width, height);
        paintMilliseconds(g, c, width, height);
    }





    protected void paintMilliseconds(Graphics2D g, JXClock c, int width, int height) {
        int size = height / 4;
        float x = (width - size) * 0.5f;
        float y = (height - size) * 0.8f;
        IconUtilities.paintChild(millisecondIcon, c, g, (int) x, (int) y, size, size, ScalePolicy.FIXED_RATIO);
    }





    private static class MillisecondPainter extends VistaClockPainter {
        public MillisecondPainter() {
        }





        @Override
        protected void paintSecondHand(Graphics2D g, JXClock c, int width, int height) {
            int millis = c.getTime().get(Calendar.MILLISECOND);
            float angle = (float) (millis / 1000f * Math.PI * 2);
            float cx = width / 2f;
            float cy = height / 2f;
            getPoint(angle, cx, cy, cx - borderSize - 10, point);
            line.setLine(cx, cy, point.getX(), point.getY());
            getPoint(angle + (float) Math.PI, cx, cy, 10, point);
            line.setLine(point.getX(), point.getY(), line.getX2(), line.getY2());

            g.setStroke(new BasicStroke(0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(handColor);
            g.draw(line);
        }





        @Override
        protected void paintShine(Graphics2D g, JXClock c, int width, int height) {}





        @Override
        protected void paintHourHand(Graphics2D g, JXClock c, int width, int height) {}





        @Override
        protected void paintMinuteHand(Graphics2D g, JXClock c, int width, int height) {}





        @Override
        protected void paintBackground(Graphics2D g, JXClock c, int width, int height) {
//            float dim = width / (2 * ROOT_2);
//            float cx = width / 2f;
//            float cy = height / 2f;
            resetFrame(ellipse, width, height);
//            g.setPaint(new GradientPaint(cx - dim, cy - dim, new Color(0xD6DFE4), cx, cy, Color.WHITE, true));
            g.setColor(new Color(1, 1, 1, 0.35f));
            g.fill(ellipse);

        }





        @Override
        protected void paintNumbers(Graphics2D g, JXClock c, int width, int height) {
            final float borderOffset = 10;
            final float majorSize = 6;
            final Color majorColor = new Color(0x818C92);
//            final Color minorColor = new Color(0xBCBDBF);
//            final Color minorHiglightColor = new Color(1, 1, 1, 0.8f);

            float cx = width / 2f;
            float cy = height / 2f;

            g.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < 12; i++) {
                float angle = (float) (i / 12f * Math.PI * 2);

                g.setColor(majorColor);
                getPoint(angle, cx, cy, cx - borderOffset, point);
                line.setLine(cx, cy, point.getX(), point.getY());
                getPoint(angle, cx, cy, cx - borderOffset - majorSize, point);
                line.setLine(point.getX(), point.getY(), line.getX2(), line.getY2());
                g.draw(line);
            }

        }
    }
}
