/*
 * $Id: DigitalClockPainter.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import java.util.Calendar;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.clock.AbstractClockPainter;

/**
 * Paints a digital clock.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DigitalClockPainter extends AbstractClockPainter {

    private NumberPainter[] numberPainters;
    private final RoundRectangle2D rRectangle = new RoundRectangle2D.Float();
    private final Rectangle2D rectangle = new Rectangle2D.Float();
    private Color emptyNumberColor = new Color(0, 0, 0, 0.15f);

    public DigitalClockPainter() {
        super();
        numberPainters = new NumberPainter[10];
        for (int i = 0; i < 10; i++) {
            numberPainters[i] = new DefaultNumberPainter(i);
        }
    }





    @Override
    protected void doPaint(Graphics2D g, JXClock c, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Rectangle2D r = paintBackground(g, c, width, height);

        float ox = (float) r.getX();
        float oy = (float) r.getY();
        float ow = (float) r.getWidth();
        float oh = (float) r.getHeight();

        float w = ow;
        float h = oh / 2f;
        float x = ox;
        float y = oy + (oh - h) / 2f;
        paintNumbers(g, c, x, y, w, h);
        paintShine(g, c, ox, oy, ow, oh);
    }





    protected void paintShine(Graphics2D g, JXClock c, float x, float y, float width, float height) {
        g.setColor(new Color(1, 1, 1, 0.25f));
        rectangle.setFrame(x, y, width, height / 2f);
        g.fill(rectangle);
    }





    protected void paintNumbers(Graphics2D g, JXClock c, float x, float y, float width, float height) {
        final float padding = 3;
        Calendar cal = c.getTime();

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        x += padding;

        x += numberPainters[hour / 10].paint(g, x, y + height, height);
        x += padding;
        x += numberPainters[hour % 10].paint(g, x, y + height, height);
        x += padding;

        if ((sec & 1) != 0) { // if odd
            g.setColor(emptyNumberColor);
        } else {
            g.setColor(Color.BLACK); // this way they will be on when the minutes change
        }
        rectangle.setFrame(x, y + height / 2f - 3, 3, 3);
        g.fill(rectangle);
        rectangle.setFrame(x, y + height - 3, 3, 3);
        g.fill(rectangle);
        x += 3;

        x += padding;

        x += numberPainters[min / 10].paint(g, x, y + height, height);
        x += padding;
        x += numberPainters[min % 10].paint(g, x, y + height, height);

    }





    protected Rectangle2D paintBackground(Graphics2D g, JXClock c, int width, int height) {
        final float round = 13;
        rRectangle.setRoundRect(0.5f, 0.5f, width - 1, height - 1, round, round);

        Shape clip = g.getClip();
        g.clip(rRectangle);

        g.setColor(new Color(0xC8C8C8));
        g.fill(rRectangle);

        rRectangle.setRoundRect(2f, -1.5f, width - 4, 6, 8, 8);
        g.setColor(new Color(1, 1, 1, 0.85f));
        g.fill(rRectangle);

        rRectangle.setRoundRect(2f, height - 0.5f - 4, width - 4, 6, 8, 8);
        g.setColor(new Color(1, 1, 1, 0.5f));
        g.fill(rRectangle);

        rRectangle.setRoundRect(4.5f, 4.5f, width - 9, height - 9, round / 2f, round / 2f);
        g.setColor(Color.WHITE);
        g.fill(rRectangle);
        g.setColor(new Color(0x878787));
        g.draw(rRectangle);

        rectangle.setFrame(8.5f, 8.5f, width - 17, height - 17);
        g.setColor(new Color(0xB3CBCB));
        g.fill(rectangle);
        rectangle.setFrame(8.5f, 9f, width - 17, height - 18);
        g.setColor(new Color(0x7B8483));
        g.draw(rectangle);
        rectangle.setFrame(8.5f, 8.5f, width - 17, height - 17);
        g.setColor(new Color(0x576264));
        g.draw(rectangle);

        g.setClip(clip);

        rRectangle.setRoundRect(0.5f, 0.5f, width - 1, height - 1, round, round);
        g.setColor(new Color(0xB8B8B8));
        g.draw(rRectangle);

        rectangle.setFrame(8.5f, 8.5f, width - 17, height - 17);
        return rectangle;
    }





    private static interface NumberPainter {
        public float paint(Graphics2D g, float x, float y, float size);
    }







    private static class DefaultNumberPainter implements NumberPainter {

        private static final int TOP = 0;
        private static final int TOP_LEFT = 1;
        private static final int TOP_RIGHT = 2;
        private static final int CENTER = 3;
        private static final int BOTTOM_LEFT = 4;
        private static final int BOTTOM_RIGHT = 5;
        private static final int BOTTOM = 6;

        private static final Shape[] PARTS;

        static {
            final float h = 1;
            //noinspection PointlessArithmeticExpression
            final float w = h * 0.6f;
            final float gap = 0.05f;
            final float gap2 = gap / 2f;
            final float size = w / 5f;

            GeneralPath topLeft = new GeneralPath();
            topLeft.moveTo(0, gap2);
            topLeft.lineTo(size, size + gap2);
            topLeft.lineTo(size, h / 2f - gap2);
            topLeft.lineTo(0, h / 2f - gap2);
            topLeft.lineTo(0, gap2);

            GeneralPath top = new GeneralPath();
            top.moveTo(gap2, 0);
            top.lineTo(w - gap2, 0);
            top.lineTo(w - gap2 - size, size);
            top.lineTo(size + gap2, size);
            top.lineTo(gap2, 0);

            GeneralPath topRight = new GeneralPath();
            topRight.moveTo(w, gap2);
            topRight.lineTo(w, h / 2f - gap2);
            topRight.lineTo(w - size, h / 2f - gap2);
            topRight.lineTo(w - size, size + gap2);
            topRight.lineTo(w, gap2);

            GeneralPath bottomLeft = new GeneralPath();
            bottomLeft.moveTo(0, h / 2f + gap2);
            bottomLeft.lineTo(size, h / 2f + gap2);
            bottomLeft.lineTo(size, h - size - gap2);
            bottomLeft.lineTo(0, h - gap2);
            bottomLeft.lineTo(0, h / 2f + gap2);

            GeneralPath bottom = new GeneralPath();
            bottom.moveTo(gap2, h);
            bottom.lineTo(size + gap2, h - size);
            bottom.lineTo(w - size - gap2, h - size);
            bottom.lineTo(w - gap2, h);
            bottom.lineTo(gap2, h);

            GeneralPath bottomRight = new GeneralPath();
            bottomRight.moveTo(w, h / 2f + gap2);
            bottomRight.lineTo(w, h - gap2);
            bottomRight.lineTo(w - size, h - size - gap2);
            bottomRight.lineTo(w - size, h / 2f + gap2);
            bottomRight.lineTo(w, h / 2f + gap2);

            GeneralPath center = new GeneralPath();
            center.moveTo(size + gap, (h - size) / 2f);
            center.lineTo(w - size - gap, (h - size) / 2f);
            center.lineTo(w - size - gap, (h + size) / 2f);
            center.lineTo(size + gap, (h + size) / 2f);
            center.lineTo(size + gap, (h - size) / 2f);

            PARTS = new Shape[7];
            PARTS[TOP] = top;
            PARTS[TOP_LEFT] = topLeft;
            PARTS[TOP_RIGHT] = topRight;
            PARTS[BOTTOM] = bottom;
            PARTS[BOTTOM_LEFT] = bottomLeft;
            PARTS[BOTTOM_RIGHT] = bottomRight;
            PARTS[CENTER] = center;
        }





        private final byte parts;
        @SuppressWarnings({"PointlessBitwiseExpression"}) // kept for readability
        public DefaultNumberPainter(int number) {
            switch (number) {
                case 0:
                    parts = (1 << TOP) | (1 << TOP_LEFT) | (1 << TOP_RIGHT) | (1 << BOTTOM_LEFT) | (1 << BOTTOM_RIGHT) | (1 << BOTTOM);
                    break;
                case 1:
                    parts = (1 << TOP_RIGHT) | (1 << BOTTOM_RIGHT);
                    break;
                case 2:
                    parts = (1 << TOP) | (1 << TOP_RIGHT) | (1 << CENTER) | (1 << BOTTOM_LEFT) | (1 << BOTTOM);
                    break;
                case 3:
                    parts = (1 << TOP) | (1 << TOP_RIGHT) | (1 << CENTER) | (1 << BOTTOM_RIGHT) | (1 << BOTTOM);
                    break;
                case 4:
                    parts = (1 << TOP_LEFT) | (1 << CENTER) | (1 << TOP_RIGHT) | (1 << BOTTOM_RIGHT);
                    break;
                case 5:
                    parts = (1 << TOP) | (1 << TOP_LEFT) | (1 << CENTER) | (1 << BOTTOM_RIGHT) | (1 << BOTTOM);
                    break;
                case 6:
                    parts = (1 << TOP) | (1 << TOP_LEFT) | (1 << BOTTOM_LEFT) | (1 << BOTTOM) | (1 << BOTTOM_RIGHT) | (1 << CENTER);
                    break;
                case 7:
                    parts = (1 << TOP) | (1 << TOP_RIGHT) | (1 << BOTTOM_RIGHT);
                    break;
                case 8:
                    parts = (1 << TOP) | (1 << TOP_RIGHT) | (1 << TOP_LEFT) | (1 << CENTER) | (1 << BOTTOM) | (1 << BOTTOM_RIGHT) | (1 << BOTTOM_LEFT);
                    break;
                case 9:
                    parts = (1 << TOP) | (1 << TOP_RIGHT) | (1 << TOP_LEFT) | (1 << CENTER) | (1 << BOTTOM) | (1 << BOTTOM_RIGHT);
                    break;
                default:
                    throw new IllegalArgumentException("number must be in range 0 <= number <= 9: " + number);
            }

        }





        public float paint(Graphics2D g, float x, float y, float size) {
            AffineTransform t = g.getTransform();

            g.scale(size, size);
            g.translate(x / size, (y - size) / size);
            Color bg = new Color(0, 0, 0, 0.15f);
            Color fg = Color.BLACK;
            for (int i = 0, n = PARTS.length; i < n; i++) {
                Shape s = PARTS[i];
                if ((parts & (1 << i)) == 0) {
                    g.setColor(bg);
                } else {
                    g.setColor(fg);
                }
                g.fill(s);
            }

            g.setTransform(t);

            return size * 0.6f;
        }
    }
}
