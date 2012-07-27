/*
 * $Id: MattePainterTmp.java 2730 2008-10-08 09:08:28Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.painter;

import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Point2D;

/**
 * Fill in for MattePainter while they add the right constructors for stretching the paint. This defaults to stretching
 * the paint and support fractional GrapdientPaints like the 'old' versions.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MattePainterTmp<T> extends MattePainter<T> {
    public MattePainterTmp() {
        this(null, true);
    }





    public MattePainterTmp(Paint paint) {
        this(paint, true);
    }





    public MattePainterTmp(Paint paint, boolean stretch) {
        super(paint);
        setPaintStretched(stretch);
    }





    @Override
    public void doPaint(Graphics2D g, T object, int width, int height) {
        Paint p = getFillPaint();
        if (isPaintStretched() && p instanceof GradientPaint) {
            GradientPaint gp = (GradientPaint) p;
            Point2D one = gp.getPoint1();
            Point2D two = gp.getPoint2();

            gp = new GradientPaint((float) one.getX() * width, (float) one.getY() * height, gp.getColor1(),
                                   (float) two.getX() * width, (float) two.getY() * height, gp.getColor2(), gp.isCyclic());
            g.setPaint(gp);
            g.fill(provideShape(g, object, width, height));
        } else {
            super.doPaint(g, object, width, height);
        }
    }
}
