/*
 * $Id: VistaClockPainter.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.jdesktop.swingx.JXClock;

/**
 * Defines a Windows Vista style clock as seen in the vista date and time display. This resembles a wristwatch style
 * render.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class VistaClockPainter extends AbstractClockPainter {

    protected final Ellipse2D ellipse = new Ellipse2D.Float();
    protected final Point2D point = new Point2D.Float();
    protected final Line2D line = new Line2D.Float();
    protected static final float ROOT_2 = (float) Math.sqrt(2);





    protected float borderSize = 3;
    protected Color handColor = new Color(0x353D40);

    public VistaClockPainter() {
        super();
    }





    @Override
    protected void doPaint(Graphics2D g, JXClock c, int width, int height) {
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        paintBackground(g, c, width, height);
        paintNumbers(g, c, width, height);
        paintHands(g, c, width, height);
        paintShine(g, c, width, height);
        paintBorder(g, c, width, height);
    }





    protected void paintBackground(Graphics2D g, JXClock c, int width, int height) {
        float dim = width / (2 * ROOT_2);
        float cx = width / 2f;
        float cy = height / 2f;
        resetFrame(ellipse, width, height);
        g.setPaint(new GradientPaint(cx - dim, cy - dim, Color.WHITE, cx, cy, new Color(0xD6DFE4), true));
        g.fill(ellipse);
    }





    protected void paintBorder(Graphics2D g, JXClock c, int width, int height) {
        resetFrame(ellipse, width, height);
        Area a = new Area(ellipse);
        scale(ellipse, borderSize);
        a.subtract(new Area(ellipse));

        g.setPaint(new GradientPaint(width / 2f, height * 0.08f, new Color(0xF1F5F8), width, 0, new Color(0x979CA2), false));
        g.fill(a);

        g.setStroke(new BasicStroke(1f));
        g.setColor(new Color(0x909193));
        g.draw(a);
        ellipse.setFrame(ellipse.getX() + 0.5f, ellipse.getY() + 0.5f, ellipse.getWidth() - 1, ellipse.getHeight() - 1);
        g.draw(ellipse);
    }





    protected void paintShine(Graphics2D g, JXClock c, int width, int height) {
        Area a;

        resetFrame(ellipse, width, height);
        scale(ellipse, borderSize * 2.1f);
        a = new Area(ellipse);
        ellipse.setFrame(ellipse.getX(), ellipse.getY() + borderSize * 0.9f, ellipse.getWidth() * 1.03f, ellipse.getHeight());
        a.subtract(new Area(ellipse));

        g.setColor(new Color(0, 0, 0, 0.13f));
        g.fill(a);

        resetFrame(ellipse, width, height);
        a = new Area(ellipse);
        float w = width * 1.3f;
        float h = height * 0.75f;
        ellipse.setFrame(0.5f + (width - w) / 2f + width * 0.1f, 0.5f + height / 2.7f, w, h);
        a.subtract(new Area(ellipse));

        AffineTransform rot = AffineTransform.getRotateInstance( -0.2, width / 2f, height / 2f);

        g.setColor(new Color(1, 1, 1, 0.3f));
        g.fill(rot.createTransformedShape(a));

    }





    protected void paintHands(Graphics2D g, JXClock c, int width, int height) {
        paintMinuteHand(g, c, width, height);
        paintHourHand(g, c, width, height);
        paintSecondHand(g, c, width, height);

        final float centerSize = 4;
        g.setColor(Color.BLACK);
        ellipse.setFrame((width - centerSize) / 2f, (height - centerSize) / 2f, centerSize, centerSize);
        g.fill(ellipse);
    }





    protected void paintSecondHand(Graphics2D g, JXClock c, int width, int height) {
        int sec = getSecond(c);
        float angle = (float) (sec / 60f * Math.PI * 2);
        float cx = width / 2f;
        float cy = height / 2f;
        getPoint(angle, cx, cy, cx - borderSize - 10, point);
        line.setLine(cx, cy, point.getX(), point.getY());
        getPoint(angle + (float) Math.PI, cx, cy, 10, point);
        line.setLine(point.getX(), point.getY(), line.getX2(), line.getY2());

        g.setStroke(new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(handColor);
        g.draw(line);
    }





    protected void paintMinuteHand(Graphics2D g, JXClock c, int width, int height) {
        int min = getMinute(c);
        float angle = (float) (min / 60f * Math.PI * 2);
        float cx = width / 2f;
        float cy = height / 2f;
        getPoint(angle, cx, cy, cx - borderSize - 16, point);
        line.setLine(cx, cy, point.getX(), point.getY());
        g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(handColor);
        g.draw(line);
    }





    protected void paintHourHand(Graphics2D g, JXClock c, int width, int height) {
        float hour = getHour(c) + getMinute(c) / 60f;
        float angle = (float) (hour / 12f * Math.PI * 2);
        float cx = width / 2f;
        float cy = height / 2f;
        getPoint(angle, cx, cy, cx - borderSize - 28, point);
        line.setLine(cx, cy, point.getX(), point.getY());
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(handColor);
        g.draw(line);
    }





    protected void paintNumbers(Graphics2D g, JXClock c, int width, int height) {
        final float borderOffset = 10;
        final float majorSize = 6;
        final Color majorColor = new Color(0x818C92);
        final Color minorColor = new Color(0xBCBDBF);
        final Color minorHiglightColor = new Color(1, 1, 1, 0.8f);

        float cx = width / 2f;
        float cy = height / 2f;

        g.setStroke(new BasicStroke(1.3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < 60; i++) {
            float angle = (float) (i / 60f * Math.PI * 2);

            if (i % 5 == 0) {
                g.setColor(majorColor);
                getPoint(angle, cx, cy, cx - borderOffset, point);
                line.setLine(cx, cy, point.getX(), point.getY());
                getPoint(angle, cx, cy, cx - borderOffset - majorSize, point);
                line.setLine(point.getX(), point.getY(), line.getX2(), line.getY2());
                g.draw(line);

            } else {

                getPoint(angle, cx, cy, cx - borderOffset - 1, point);
                ellipse.setFrame(point.getX() - 1, point.getY() - 1, 2, 2);
                g.setColor(minorColor);
                g.fill(ellipse);

                ellipse.setFrame(ellipse.getX(), ellipse.getY(), ellipse.getWidth() - 0.6f, ellipse.getHeight() - 0.6f);
                g.setColor(minorHiglightColor);
                g.fill(ellipse);
            }
        }

    }





    private void scale(Ellipse2D ellipse, float scale) {
        ellipse.setFrame(ellipse.getX() + scale, ellipse.getY() + scale, ellipse.getWidth() - scale - scale, ellipse.getHeight() - scale - scale);
    }





    protected void resetFrame(Ellipse2D ellipse, int width, int height) {
        ellipse.setFrame(0.5f, 0.5f, width - 1, height - 1);
    }
}
