/*
 * $Id: DefaultClockPainter.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.util.Calendar;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.painter.AbstractPainter;

/**
 * Paints a very simple clock.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultClockPainter extends AbstractPainter<JXClock> {

    private final Ellipse2D ellipse = new Ellipse2D.Float();
    private final Line2D line = new Line2D.Float();

    public DefaultClockPainter() {
        super();
    }





    @Override
    protected void doPaint(Graphics2D g, JXClock object, int width, int height) {
        width--;
        height--;
        int size = width > height ? height : width;

        int x = (width - size) >> 1;
        int y = (height - size) >> 1;

        g.setColor(object.getForeground());
        ellipse.setFrame(x + 0.5, y + 0.5, size, size);
        g.draw(ellipse);

        float cx = x + (size / 2f) + 0.5f;
        float cy = y + (size / 2f) + 0.5f;

        Calendar c = object.getTime();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);

        float secLen = (size >> 1) * 0.9f;
        float minLen = secLen * 0.90f;
        float hourLen = minLen * 0.80f;

        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        paintLine(g, cx, cy, secLen, sec / 60f);
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        paintLine(g, cx, cy, minLen, min / 60f);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        paintLine(g, cx, cy, hourLen, hour / 12f);
        g.setStroke(s);

//        ellipse.setFrame(cx - 1, cx - 1, 2, 2);
//        g.fill(ellipse);
    }





    private void paintLine(Graphics2D g, float cx, float cy, float length, float percentage) {
        float angle = (float) (percentage * Math.PI * 2);
        double endx;
        double endy;

        if (angle >= 0 && angle < Math.PI / 2f) {
            endx = cx + (length * Math.sin(angle));
            endy = cy - (length * Math.cos(angle));

        } else if (angle >= Math.PI / 2f && angle < Math.PI) {
            angle -= Math.PI / 2f;
            endx = cx + (length * Math.cos(angle));
            endy = cy + (length * Math.sin(angle));

        } else if (angle >= Math.PI && angle < Math.PI * 1.5f) {
            angle -= Math.PI;
            endx = cx - (length * Math.sin(angle));
            endy = cy + (length * Math.cos(angle));
        } else {
            angle -= Math.PI * 1.5f;
            endx = cx - (length * Math.cos(angle));
            endy = cy - (length * Math.sin(angle));
        }
        line.setLine(cx, cy, endx, endy);
        g.draw(line);
    }
}
