/*
 * $Id: BasicTimelineUI.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.plaf.TimelineUI;
import org.jdesktop.swingx.*;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.icon.*;
import java.util.*;
import org.jdesktop.swingx.util.*;
import org.jdesktop.swingx.timeline.*;

/**
 * Basic look and feel for a JXTimeline component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicTimelineUI extends TimelineUI {
    /**
     * Defines a direction. This is mostly used when painting directional widgets, e.g. arrows.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum Direction {
        UP, DOWN, LEFT, RIGHT
    }







    private static BasicTimelineUI SINGLETON = null;
    public static BasicTimelineUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicTimelineUI();
        }
        return SINGLETON;
    }





    protected BasicTimelineUI() {
        super();
        setInstallDefaultPropertySupport(true);
    }





    private static final Rectangle rect = new Rectangle();
    @Override
    public void paint(Graphics g, JComponent c) {
        JXTimeline tl = (JXTimeline) c;
        Rectangle bounds = LookAndFeelUtilities.getInnerBounds(c, null);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(tl.getForeground());

        ViewRange vr = tl.getViewRange();

        long max = vr.getViewMaximum(tl, bounds.width);
        long min = vr.getViewMinimum(tl, bounds.width);

        switch (tl.getOrientation()) {
            case VERTICAL:
                paintVertical(tl, g2, min, max, bounds);
                break;
            case HORIZONTAL:
            default:
                paintHorizontal(tl, g2, min, max, bounds);
                break;

        }
    }





    protected void paintHorizontal(JXTimeline tl, Graphics2D g, long min, long max, Rectangle bounds) {
        final Rectangle rect = new Rectangle(bounds);
        float center = bounds.width / 2f;

        int x;
        int y = bounds.y;
        int w = bounds.width;
        int h = 0;

        rect.width = (int) center;
        if (min == tl.getMinimum()) {
            paintDoneTerminator(g, tl, Direction.LEFT, rect);
        } else {
            paintMoreTerminator(g, tl, Direction.LEFT, rect);
        }
        x = rect.x + rect.width;
        w -= rect.width;
        if (rect.height > h) {
            y = rect.y;
            h = rect.height;
        }

        rect.setBounds(bounds);
        rect.x += (int) center;
        rect.width = bounds.width - (int) center;

        if (max == tl.getMaximum()) {
            paintDoneTerminator(g, tl, Direction.RIGHT, rect);
        } else {
            paintMoreTerminator(g, tl, Direction.RIGHT, rect);
        }
        w -= rect.width;
        if (rect.height > h) {
            y = rect.y;
            h = rect.height;
        }

        bounds.setBounds(x, y, w, h);
        paintCenter(g, tl, tl.getOrientation(), bounds);

        long val = tl.getValue();
        double valStart = (val - min) / (double) (max - min) * w;
        double valEnd = (val + tl.getExtent() - min) / (double) (max - min) * w;
        bounds.setBounds((int) (x + valStart), y, (int) (valEnd - valStart), h);
        if (bounds.width <= 0) {
            bounds.width = 1;
        }
        paintValue(g, tl, bounds);
    }





    protected void paintVertical(JXTimeline tl, Graphics2D g, long min, long max, Rectangle bounds) {
        final Rectangle rect = new Rectangle(bounds);
        float center = bounds.height / 2f;

        int x = bounds.x;
        int y;
        int w = 0;
        int h = bounds.height;

        rect.height = (int) center;
        if (min == tl.getMinimum()) {
            paintDoneTerminator(g, tl, Direction.UP, rect);
        } else {
            paintMoreTerminator(g, tl, Direction.UP, rect);
        }
        y = rect.y + rect.height;
        h -= rect.height;
        if (rect.width > w) {
            x = rect.x;
            w = rect.width;
        }

        rect.setBounds(bounds);
        rect.y += (int) center;
        rect.height = bounds.height - (int) center;

        if (max == tl.getMaximum()) {
            paintDoneTerminator(g, tl, Direction.DOWN, rect);
        } else {
            paintMoreTerminator(g, tl, Direction.DOWN, rect);
        }
        h -= rect.height;
        if (rect.width > w) {
            x = rect.x;
            w = rect.width;
        }

        bounds.setBounds(x, y, w, h);
        paintCenter(g, tl, tl.getOrientation(), bounds);

        long val = tl.getValue();
        double valStart = (val - min) / (double) (max - min) * h;
        double valEnd = (val + tl.getExtent() - min) / (double) (max - min) * h;
        bounds.setBounds(x, (int) (y + valStart), w, (int) (valEnd - valStart));
        if (bounds.height <= 0) {
            bounds.height = 1;
        }
        paintValue(g, tl, bounds);

    }





    /**
     * Paints the center of the timeline. The bit between the ends, typically a straight line.
     *
     * @param g Graphics2D
     * @param c JXTimeline
     * @param o Orientation
     * @param r Rectangle
     */
    protected void paintCenter(Graphics2D g, JXTimeline c, JXTimeline.Orientation o, Rectangle r) {
        c.putClientProperty("CenterBounds", r.clone());
        r.height--;
        r.width--;
        switch (o) {
            case VERTICAL:
                g.draw(new Line2D.Float(r.x + r.width / 2f, r.y, r.x + r.width / 2f, r.y + r.height));
                break;
            case HORIZONTAL:
            default:
                g.draw(new Line2D.Float(r.x, r.y + r.height / 2f, r.x + r.width, r.y + r.height / 2f));
                break;

        }
    }





    /**
     * Paint the value and extent into the given rectangle.
     *
     * @param g The graphics to paint to.
     * @param c The source component.
     * @param valueBounds The rectangle to paint the value to.
     */
    protected void paintValue(Graphics2D g, JXTimeline c, Rectangle valueBounds) {
        g.setColor(c.getBackground());
        g.fillRect(valueBounds.x, valueBounds.y, valueBounds.width - 1, valueBounds.height - 1);
        g.setColor(c.getForeground());
        g.drawRect(valueBounds.x, valueBounds.y, valueBounds.width - 1, valueBounds.height - 1);

    }





    protected void paintTicks(Graphics2D g, JXTimeline c, long[] ticks) {
        switch (c.getOrientation()) {
            case HORIZONTAL:
                paintHorizontalTicks(g, c, ticks);
                break;
            case VERTICAL:
                paintVerticalTicks(g, c, ticks);
                break;
        }
    }





    protected void paintHorizontalTicks(Graphics2D g, JXTimeline c, long[] ticks) {

    }





    protected void paintVerticalTicks(Graphics2D g, JXTimeline c, long[] ticks) {

    }





    /**
     * Paints to the given graphics to signify that there is more of the timeline to see. Typically this will be an
     * arrow or dotted line.
     *
     * <ul>
     * <li>{@code upMoreIcon}</li>
     * <li>{@code downMoreIcon}</li>
     * <li>{@code leftMoreIcon}</li>
     * <li>{@code rightMoreIcon}</li>
     * </ul>
     *
     * depending on the Direction given.
     *
     * @param g The graphics to paint to.
     * @param c The source component.
     * @param d The direction to paint the arrow.
     * @param area The area to paint the arrow in.
     */
    protected void paintMoreTerminator(Graphics2D g, JXTimeline c, Direction d, Rectangle area) {
        Icon i;
        LocationPolicy lp;

        switch (d) {
            case UP:
                i = getUIProperty(c, "upMoreIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.NORTH);
                break;
            case DOWN:
                i = getUIProperty(c, "downMoreIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.SOUTH);
                break;
            case LEFT:
                i = getUIProperty(c, "leftMoreIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.WEST);
                break;
            case RIGHT:
                i = getUIProperty(c, "rightMoreIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.EAST);
                break;
            default:
                throw new AssertionError("Fortten case in Direction: " + d);
        }

        if (i != null) {
            area.setBounds(IconUtilities.paintChild(i, c, g, area.x, area.y, area.width, area.height, lp));
        } else {
            area.setBounds(0, 0, 0, 0);
        }
    }





    /**
     * Paints a end which represents the end of a timeline. This is typically a solid line perpendicular to the
     * timeline.
     *
     * <ul>
     * <li>{@code upDoneIcon}</li>
     * <li>{@code downDoneIcon}</li>
     * <li>{@code leftDoneIcon}</li>
     * <li>{@code rightDoneIcon}</li>
     * </ul>
     *
     * @param g The graphics to paint to.
     * @param c The source component.
     * @param d The Direction to paint in.
     * @param area The area to paint.
     */
    protected void paintDoneTerminator(Graphics2D g, JXTimeline c, Direction d, Rectangle area) {
        Icon i;
        LocationPolicy lp;

        switch (d) {
            case UP:
                i = getUIProperty(c, "upDoneIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.NORTH);
                break;
            case DOWN:
                i = getUIProperty(c, "downDoneIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.SOUTH);
                break;
            case LEFT:
                i = getUIProperty(c, "leftDoneIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.WEST);
                break;
            case RIGHT:
                i = getUIProperty(c, "rightDoneIcon");
                lp = LocationPolicy.valueOf(LocationPolicy.EAST);
                break;
            default:
                throw new AssertionError("Fortten case in Direction: " + d);
        }

        if (i != null) {
            area.setBounds(IconUtilities.paintChild(i, c, g, area.x, area.y, area.width, area.height, lp));
        } else {
            area.setBounds(0, 0, 0, 0);
        }

    }





    @Override
    protected void propertyChange(JXTimeline tl, String property, Object oldValue, Object newValue) {
        boolean override = property == null;
        if (override ||
            property == "orientation") {
            tl.revalidate();
            tl.repaint();
        }
    }





    @Override
    public Rectangle getTimestampBounds(JXTimeline tl, long timestamp) {
        Rectangle result = null;
        long min = tl.getMinimum();
        long max = tl.getMaximum();
        if (min <= timestamp && max >= timestamp) {
            result = new Rectangle();
            JXTimeline.Orientation o = tl.getOrientation();
            ViewRange vr = tl.getViewRange();
            Rectangle r = (Rectangle) tl.getClientProperty("CenterBounds");
            int size = o == JXTimeline.Orientation.HORIZONTAL ? r.width : r.height;
            long vmin = vr.getViewMinimum(tl, size);
            long vmax = vr.getViewMaximum(tl, size);

            int coord = (int) ((timestamp - vmin) / (double) (vmax - vmin) * size);
            result.width = result.height = 1;
            switch (o) {
                case HORIZONTAL:
                    result.x = coord + r.x;
                    result.y = r.y;
                    result.height = r.height;
                    break;
                case VERTICAL:
                    result.y = coord + r.y;
                    result.width = r.width;
                    result.x = r.x;
                    break;
            }
            if (timestamp < vmin || timestamp > vmax) {
                result.width = -result.width;
                result.height = -result.height;
            }
        }
        return result;
    }





    @Override
    public long locationToTimestamp(JXTimeline tl, Point point) {
        long result = -1;
        ViewRange vr = tl.getViewRange();
        Rectangle cb = (Rectangle) tl.getClientProperty("CenterBounds");
        int size = tl.getOrientation() == JXTimeline.Orientation.HORIZONTAL ? cb.width : cb.height;
        long vmin = vr.getViewMinimum(tl, size);
        long vmax = vr.getViewMaximum(tl, size);
        switch (tl.getOrientation()) {
            case HORIZONTAL:
                result = vmin + (long) ((((long) point.x - (long) cb.x) / (double) cb.width) * (vmax - vmin));
                break;
            case VERTICAL:
                result = vmin + (long) ((((long) point.y - (long) cb.y) / (double) cb.height) * (vmax - vmin));
                break;
        }
        return result;
    }
}
