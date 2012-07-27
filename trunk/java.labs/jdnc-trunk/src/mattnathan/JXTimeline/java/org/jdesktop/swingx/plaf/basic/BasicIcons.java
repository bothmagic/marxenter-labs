/*
 * $Id: BasicIcons.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.icon.AbstractScalableIcon;
import org.jdesktop.swingx.plaf.basic.BasicTimelineUI.Direction;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines factory methods for creating icons for the Basic look and feel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicIcons {
    
    private BasicIcons() {
    }



    public static Icon createTimelineLeftMoreIcon() {
        return createTimelineMoreIcon(Direction.LEFT);
    }





    public static Icon createTimelineRightMoreIcon() {
        return createTimelineMoreIcon(Direction.RIGHT);
    }





    public static Icon createTimelineUpMoreIcon() {
        return createTimelineMoreIcon(Direction.UP);
    }





    public static Icon createTimelineDownMoreIcon() {
        return createTimelineMoreIcon(Direction.DOWN);
    }





    private static Icon[] timelineMoreIcons;
    protected static Icon createTimelineMoreIcon(Direction d) {
        if (timelineMoreIcons == null) {
            timelineMoreIcons = new Icon[Direction.values().length];
        }
        Icon p = timelineMoreIcons[d.ordinal()];
        if (p == null) {
            p = new TimelineMoreIcon(d);
            timelineMoreIcons[d.ordinal()] = p;
        }
        return p;
    }





    public static Icon createTimelineLeftDoneIcon() {
        return createTimelineDoneIcon(Direction.LEFT);
    }





    public static Icon createTimelineRightDoneIcon() {
        return createTimelineDoneIcon(Direction.RIGHT);
    }





    public static Icon createTimelineUpDoneIcon() {
        return createTimelineDoneIcon(Direction.UP);
    }





    public static Icon createTimelineDownDoneIcon() {
        return createTimelineDoneIcon(Direction.DOWN);
    }





    private static Icon[] timelineDoneIcons;
    protected static Icon createTimelineDoneIcon(Direction d) {
        if (timelineDoneIcons == null) {
            timelineDoneIcons = new Icon[Direction.values().length];
        }
        Icon p = timelineDoneIcons[d.ordinal()];
        if (p == null) {
            p = new TimelineDoneIcon(d);
            timelineDoneIcons[d.ordinal()] = p;
        }
        return p;
    }





    protected abstract static class AbstractDirectionIcon extends AbstractScalableIcon {

        private Direction direction;

        public AbstractDirectionIcon() {
            this(Direction.RIGHT);
        }





        public AbstractDirectionIcon(Direction direction) {
            setScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.BOTH, ScalePolicy.ResizePolicy.SHRINK));
            this.direction = direction;
        }





        public Direction getDirection() {
            return direction;
        }





        @Override
        protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
            paintIconImpl(c, g2, x, y, width, height, getDirection());
        }





        protected abstract void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height, Direction direction);
    }







    protected static class TimelineDoneIcon extends AbstractDirectionIcon {

        private float primarySize;
        private float secondarySize;
        private float gap;

        public TimelineDoneIcon(Direction direction) {
            this(direction, 2, 1, 1);
        }





        public TimelineDoneIcon(Direction direction, float primarySize, float secondarySize, int gap) {
            super(direction);
            this.primarySize = primarySize;
            this.secondarySize = secondarySize;
            this.gap = gap;
        }





        @Override
        protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height, Direction direction) {
//            width--;
//            height--;
            Rectangle2D rect = new Rectangle2D.Float();

            switch (direction) {
                case UP:
                    rect.setFrame(x, y, width, primarySize);
                    g.fill(rect);
                    rect.setFrame(x, y + primarySize + gap, width, secondarySize);
                    g.fill(rect);
                    break;
                case DOWN:
                    rect.setFrame(x, y + height - primarySize, width, primarySize);
                    g.fill(rect);
                    rect.setFrame(x, y + height - (primarySize + gap) - secondarySize, width, secondarySize);
                    g.fill(rect);
                    break;
                case LEFT:
                    rect.setFrame(x, y, primarySize, height);
                    g.fill(rect);
                    rect.setFrame(x + primarySize + gap, y, secondarySize, height);
                    g.fill(rect);
                    break;
                case RIGHT:
                    rect.setFrame(x + width - primarySize, y, primarySize, height);
                    g.fill(rect);
                    rect.setFrame(x + width - (primarySize + gap) - secondarySize, y, secondarySize, height);
                    g.fill(rect);
                    break;
                default:
                    throw new AssertionError("Forgot Direction case: " + direction);
            }
        }





        public int getIconWidth() {
            int result;
            switch (getDirection()) {
                case UP:
                case DOWN:
                    result = 10;
                    break;
                case LEFT:
                case RIGHT:
                default:
                    result = (int) Math.ceil(primarySize + gap + secondarySize);
                    break;
            }

            return result;
        }





        public int getIconHeight() {
            int result;
            switch (getDirection()) {
                case UP:
                case DOWN:
                    result = (int) Math.ceil(primarySize + gap + secondarySize);
                    break;
                case LEFT:
                case RIGHT:
                default:
                    result = 10;
                    break;
            }

            return result;

        }
    }







    /**
     * Paints an arrow to show that there is more.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected static class TimelineMoreIcon extends AbstractDirectionIcon {

        private int maxSpan;

        public TimelineMoreIcon() {
            this(Direction.RIGHT);
        }





        public TimelineMoreIcon(Direction direction) {
            this(direction, Integer.MAX_VALUE);
        }





        public TimelineMoreIcon(Direction direction, int maxSpan) {
            super(direction);
            this.maxSpan = maxSpan;
        }





        @Override
        protected void paintIconImpl(Component object, Graphics2D g, int x, int y, int w, int h, Direction direction) {
            h--;
            w--;
            float middle;
            int half;
            float span;

            Line2D line = new Line2D.Float();

            switch (direction) {
                case UP:
                    middle = w / 2f;
                    half = w >> 1;
                    span = Math.min(half, maxSpan);
                    line.setLine(x + middle, y, x + middle - span, y + Math.min(h, span));
                    g.draw(line);
                    line.setLine(x + middle, y, x + middle + span, y + Math.min(h, span));
                    g.draw(line);
                    line.setLine(x + middle, y, x + middle, y + h);
                    g.draw(line);
                    break;
                case DOWN:
                    middle = w / 2f;
                    half = w >> 1;
                    span = Math.min(half, maxSpan);
                    line.setLine(x + middle, y + h, x + middle - span, y + h - Math.min(h, span));
                    g.draw(line);
                    line.setLine(x + middle, y + h, x + middle + span, y + h - Math.min(h, span));
                    g.draw(line);
                    line.setLine(x + middle, y, x + middle, y + h);
                    g.draw(line);
                    break;
                case LEFT:
                    middle = h / 2f;
                    half = h >> 1;
                    span = Math.min(half, maxSpan);
                    line.setLine(x, y + middle, x + Math.min(w, span), y + middle - span);
                    g.draw(line);
                    line.setLine(x, y + middle, x + Math.min(w, span), y + middle + span);
                    g.draw(line);
                    line.setLine(x, y + middle, x + w, y + middle);
                    g.draw(line);
                    break;
                case RIGHT:
                    middle = h / 2f;
                    half = h >> 1;
                    span = Math.min(half, maxSpan);
                    line.setLine(x + w, y + middle, x + w - Math.min(w, span), y + middle - span);
                    g.draw(line);
                    line.setLine(x + w, y + middle, x + w - Math.min(w, span), y + middle + span);
                    g.draw(line);
                    line.setLine(x, y + middle, x + w, y + middle);
                    g.draw(line);
                    break;
                default:
                    throw new AssertionError("Fortten case in Direction: " + direction);
            }

        }





        public int getIconWidth() {
            int result;
            switch (getDirection()) {
                case UP:
                case DOWN:
                    result = 10;
                    break;
                case LEFT:
                case RIGHT:
                default:
                    result = 5;
                    break;
            }

            return result;
        }





        public int getIconHeight() {
            int result;
            switch (getDirection()) {
                case UP:
                case DOWN:
                    result = 5;
                    break;
                case LEFT:
                case RIGHT:
                default:
                    result = 10;
                    break;
            }

            return result;

        }

    }
}
