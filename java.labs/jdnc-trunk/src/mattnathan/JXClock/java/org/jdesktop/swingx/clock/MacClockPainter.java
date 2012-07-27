/*
 * $Id: MacClockPainter.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import java.util.Calendar;

import org.jdesktop.swingx.JXClock;

/**
 * Paints a clock face as defined by the JXClock in the style of the Mac OSX clock Widget. This has no mac specific code
 * so can be used under any operating system.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MacClockPainter extends AbstractClockPainter {
    private static final DateFormat AM_PM_FORMATTER = new SimpleDateFormat("a");

    private float roundness = 20;
    private Insets facePadding = new Insets(16, 16, 16, 16);
    private float border = 2;
    private boolean useLight = true;

    public MacClockPainter() {
        super();
    }





    /**
     * Subclasses must implement this method and perform custom painting operations here.
     *
     * @param g The Graphics2D object in which to paint
     * @param object Object
     * @param width int
     * @param height int
     */
    @Override
    protected void doPaint(Graphics2D g, JXClock object, int width, int height) {
        int hour = object.getTime().get(Calendar.HOUR_OF_DAY);
        useLight = hour < 18 && hour > 6;

        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        paintBackground(g, object, width, height);
        paintClockFace(g, object, width, height);
        paintClockNumbers(g, object, width, height);

        paintMiniteHand(g, object, width, height);
        paintHourHand(g, object, width, height);
        paintHandAnchor(g, object, width, height);
        paintSecondHand(g, object, width, height);

        paintHighlight(g, object, width, height);

        paintAMPM(g, object, width, height);
    }





    protected void paintAMPM(Graphics2D g, JXClock clock, int width, int height) {
        Calendar c = clock.getTime();
        String s = AM_PM_FORMATTER.format(c.getTime());
        g.setFont(clock.getFont().deriveFont(Font.PLAIN, clock.getFont().getSize2D() * 0.9f));
        FontMetrics fm = g.getFontMetrics();
        int ascent = fm.getAscent();
        int w = fm.stringWidth(s);
        float x = (width - w) / 2f;
        float y = (facePadding.top - ascent) / 2f + (ascent);
        g.setColor(Color.GRAY);
        g.drawString(s, x, y);
        g.setColor(Color.BLACK);
        g.drawString(s, x, y - 0.5f);
    }





    protected void paintHandAnchor(Graphics2D g, JXClock clock, int width, int height) {
        float size = 10;
        Ellipse2D circle = new Ellipse2D.Float((width - size) / 2f, (height - size) / 2f, size, size);
        g.setColor(Color.WHITE);
        g.fill(circle);

        if (useLight) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(new Color(0xEAEAEA));
        }
        g.draw(circle);

        size -= 5;
        circle.setFrame((width - size) / 2f, (height - size) / 2f, size, size);
        g.setColor(new Color(0xFF1515));
        g.fill(circle);
    }





    protected void paintMiniteHand(Graphics2D g, JXClock clock, int width, int height) {
        final float padding = 14;
        final float length = ((height - facePadding.top - facePadding.bottom) / 2f) - padding;

        int min = clock.getTime().get(Calendar.MINUTE);
        float angle = (float) (min / 60f * Math.PI * 2);

        paintHand(g, clock, width, height, length, angle);
    }





    protected void paintHourHand(Graphics2D g, JXClock clock, int width, int height) {
        final float padding = 30;
        final float length = ((height - facePadding.top - facePadding.bottom) / 2f) - padding;
        Calendar c = clock.getTime();
        float hour = c.get(Calendar.HOUR) + (c.get(Calendar.MINUTE) / 60f);
        float angle = (float) (hour / 12f * Math.PI * 2);

        paintHand(g, clock, width, height, length, angle);
    }





    private void paintHand(Graphics2D g, JXClock clock, int width, int height, float length, float angle) {
        final float center = 8;
        Point2D point = new Point2D.Float();

        float cx = width / 2f;
        float cy = height / 2f;

        getPoint(angle, cx, cy, length, point);

        float px = (float) point.getX();
        float py = (float) point.getY();

        if (useLight) {
            g.setPaint(new GradientPaint(px, py, Color.BLACK, cx, cy, new Color(0x484848), true));
        } else {
            g.setColor(Color.WHITE);
        }

        GeneralPath path = new GeneralPath();
        path.moveTo(px, py);

        getPoint(angle + (float) (Math.PI / 2f), cx, cy, center / 2f, point);
        path.lineTo((float) point.getX(), (float) point.getY());

        getPoint(angle - (float) (Math.PI / 2f), cx, cy, center / 2f, point);
        path.lineTo((float) point.getX(), (float) point.getY());

        path.closePath();

        g.fill(path);

        if (useLight) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(new Color(0xEAEAEA));
        }
        g.setStroke(new BasicStroke(0.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(path);

    }





    protected void paintSecondHand(Graphics2D g, JXClock clock, int width, int height) {
        final Color fill = new Color(0xFF1515);
        final Color end = new Color(0xD17079);
        final float weight = 2f;
        final float padding = 7;
        final Line2D line = new Line2D.Float();

        int second = clock.getTime().get(Calendar.SECOND);
        float percentage = second / 60f;
        float cx = width / 2f;
        float cy = height / 2f;
        float length = (height - facePadding.top - facePadding.bottom - padding - padding) / 2f;

        g.setColor(fill);
        g.setStroke(new BasicStroke(weight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        float angle = (float) (percentage * Math.PI * 2);
        Point2D point = new Point2D.Float();
        getPoint(angle, cx, cy, length, point);

        float endx = (float) point.getX();
        float endy = (float) point.getY();
        g.setPaint(new GradientPaint(cx, cy, fill, endx, endy, end, true));
        line.setLine(cx, cy, endx, endy);
        g.draw(line);
    }





    protected void paintHighlight(Graphics2D g, JXClock clock, int width, int height) {
        final Ellipse2D shape = new Ellipse2D.Float();
        float add = border / 2f;
        shape.setFrame(facePadding.left + add, facePadding.top + add,
                       width - facePadding.left - facePadding.right - add - add, height - facePadding.top - facePadding.bottom - add - add);

        final Paint highlight = new GradientPaint(0, facePadding.top, new Color(1, 1, 1, 0.3f), 0, height / 2.1f, new Color(1, 1, 1, 0f), true);
        g.setPaint(highlight);
        Area highlightShape = new Area(shape);
        highlightShape.subtract(new Area(new Ellipse2D.Float(1, height / 2f - facePadding.top * 1.3f, width, height / 1.8f)));
        highlightShape.subtract(new Area(new Rectangle(0, height / 2, width, height / 2)));
        g.fill(highlightShape);

    }





    protected void paintClockNumbers(Graphics2D g, JXClock clock, int width, int height) {
        final float padding = 14;

        if (useLight) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(Color.WHITE);
        }
        g.setFont(clock.getFont().deriveFont(Font.PLAIN));
        FontMetrics fm = g.getFontMetrics();

        float cx = width / 2f;
        float cy = height / 2f;
        float length = (height - facePadding.top - facePadding.bottom - padding - padding) / 2f;
        int sh = fm.getAscent();
        Point2D point = new Point2D.Float();

        for (int i = 0; i < 12; i++) {
            float percentage = ((i + 1) % 12) / 12f;

            float angle = (float) (percentage * Math.PI * 2);
            getPoint(angle, cx, cy, length, point);

            String s = String.valueOf(i + 1);
            int sw = fm.stringWidth(s);

            float sx = (float) point.getX() - (sw / 2f);
            float sy = (float) point.getY() + (sh / 2f);

            g.drawString(s, sx, sy);
        }
    }





    protected void paintClockFace(Graphics2D g, JXClock clock, int width, int height) {
        final Color light = new Color(0xEAEAEA);
        final Color dark = Color.BLACK;
        final Color border = new Color(99, 99, 99, 100);
        final Ellipse2D shape = new Ellipse2D.Float();

        shape.setFrame(facePadding.left, facePadding.top, width - facePadding.left - facePadding.right, height - facePadding.top - facePadding.bottom);

        if (useLight) {
            g.setColor(light);
        } else {
            g.setColor(dark);
        }

        g.fill(shape);

        g.setStroke(new BasicStroke(this.border));
        g.setColor(border);
        g.draw(shape);

    }





    protected void paintBackground(Graphics2D g, JXClock clock, int width, int height) {
        final Color light = new Color(0x484848);
        final Color dark = Color.BLACK;
        final RoundRectangle2D rr = new RoundRectangle2D.Float();

        g.setColor(dark);
        rr.setRoundRect(0.5, 0.5, width - 1, height - 1, roundness, roundness);
        g.fill(rr);

        Shape clip = g.getClip();
        rr.setFrame(0.5 + border, 0.5 + border, width - border - border - 1, height - border - border - 1);
        g.clipRect(0, 0, width, height >> 1);
        g.setColor(light);
        g.fill(rr);
        g.setClip(clip);
    }
}
