/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.util;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

/**
 * Rounded Rectangle 2D with each line segment potentially having a different
 * color.
 * @author Ryan Cuprak
 */
public class RoundedColorizedRectangle2D extends RectangularShape {

    /**
     * Top color
     */
    private Color top;

    /**
     * Bottom color
     */
    private Color bottom;

    /**
     * Right color
     */
    private Color right;

    /**
     * Left color
     */
    private Color left;

    /**
     * X coordinate
     */
    private float x;

    /**
     * Y coordinate
     */
    private float y;

    /**
     * Width
     */
    private float width;

    /**
     * Height
     */
    private float height;

    /**
     * Radius
     */
    private int radius;

    /**
     * General path that makes up this shape
     */
    private GeneralPath generalPath;

    /**
     * Segments, use these to draw highlights etc.
     */
    private java.util.List<Segment> segments;

    /**
     * Creates a new RoundedColorizedRectangle
     * @param x - x position
     * @param y - y position
     * @param width - width
     * @param height - height
     * @param radius - radius of the button
     * @param color - color to be used (single color button)
     */
    public RoundedColorizedRectangle2D(int x, int y, int width, int height, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.top = color;
        this.right = color;
        this.bottom = color;
        this.left = color;
    }

    /**
     * Creates a new RoundedColorizedRectangle2D
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - width
     * @param height - height
     * @param radius - radius of the corners
     * @param top - top color
     * @param bottom - bottom color
     * @param right - right color
     * @param left - left color
     */
    public RoundedColorizedRectangle2D(int x, int y, int width, int height, int radius, Color top, Color bottom, Color right, Color left) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
        init();
    }

    /**
     * Initializes the RoundedRect along with the segments and general path.
     */
    protected void init() {
        float diameter = radius*2;
        segments = new ArrayList<Segment>();
        segments.add(new Segment(new Arc2D.Float(x,y,diameter,diameter,90,90,Arc2D.OPEN),top,"topLeft"));
        segments.add(new Segment(new Line2D.Float(x+radius,y,x+width-radius,y),top,"top"));
        segments.add(new Segment(new Arc2D.Float(x+width-diameter,y,diameter,diameter,0,90,Arc2D.OPEN),top,"topRight"));
        segments.add(new Segment(new Line2D.Float(x+width,y+radius,x+width,y+height-radius),right,"right"));
        segments.add(new Segment(new Arc2D.Float(x+width-diameter,y+height-diameter,diameter,diameter,270,90,Arc2D.OPEN),bottom,"bottomRight"));
        segments.add(new Segment(new Line2D.Float(x+radius,y+height,x+width-radius,y+height),bottom,"bottom"));
        segments.add(new Segment(new Arc2D.Float(x,y+height-diameter,diameter,diameter,180,90,Arc2D.OPEN),bottom,"bottomLeft"));
        segments.add(new Segment(new Line2D.Float(x,y+radius,x,y+height-radius),left,"left"));
        generalPath = new GeneralPath();
        Rectangle2D rect  = new Rectangle2D.Float(x+radius,y,width-diameter,height);
        Rectangle2D left  = new Rectangle2D.Float(x,y+radius,radius,height-diameter);
        Rectangle2D right = new Rectangle2D.Float(x+width-radius,y+radius,radius,height-diameter);
        Ellipse2D topLeft = new Ellipse2D.Float(x,y,diameter,diameter);
        Ellipse2D bottomLeft = new Ellipse2D.Float(x,height-diameter,diameter,diameter);
        Ellipse2D topRight = new Ellipse2D.Float(width-diameter,y,diameter,diameter);
        Ellipse2D bottomRight = new Ellipse2D.Float(width-diameter,height-diameter,diameter,diameter);
        generalPath.append(topLeft,true);
        generalPath.append(bottomLeft,true);
        generalPath.append(topRight,true);
        generalPath.append(bottomRight,true);
        generalPath.append(rect,true);
        generalPath.append(left,true);
        generalPath.append(right,true);
        generalPath.closePath();
    }

    /**
     * Draws the bounds with our custom colors
     * @param g2d - graphics context
     */
    public void drawBounds(Graphics2D g2d) {
        for(Segment segment : segments) {
            segment.draw(g2d);
        }
    }

    public Rectangle2D getBounds2D() {
        return generalPath.getBounds2D();
    }

    public boolean contains(double x, double y) {
        return generalPath.contains(x,y);
    }

    /**
     * Sets corder radius
      * @param radius - radius of the corders
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }


    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    /**
     * We are never empty!
     * @return false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        this.x = (float)x;
        this.y = (float)y;
        this.width = (float)w;
        this.height = (float)h;
        init();
    }

    /**
     * Sets the frame including the radius
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - width
     * @param h - height
     * @param radius - radius
     */
    public void setFrame(double x, double y, double w, double h, double radius) {
        this.x = (float)x;
        this.y = (float)y;
        this.width = (float)w;
        this.height = (float)h;
        this.radius = (int)radius;
        init();
    }

    public boolean intersects(double x, double y, double w, double h) {
        return generalPath.intersects(x,y,w,h);
    }

    public boolean contains(double x, double y, double w, double h) {
        return generalPath.contains(x,y,w,h);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return generalPath.getPathIterator(at);
    }

    /**
     * Segment
     */
    public static class Segment {

        /**
         * Color of the segment
         */
        private Color color;

        /**
         * Shape
         */
        private Shape shape;

        /**
         * Informational/debugging purposes
         */
        private String description;

        /**
         * Constructs a new segment
         * @param shape - shape
         * @param color - color
         * @param description - description
         */
        public Segment(Shape shape, Color color, String description) {
            this.color = color;
            this.shape = shape;
            this.description = description;
        }

        /**
         * Returns the shape
         * @return shape
         */
        public Shape getShape() {
            return shape;
        }

        public void draw(Graphics2D g) {
            g.setColor(color);
            g.draw(shape);
        }

        /**
         * Returns the description of the text
         * @return String
         */
        @Override
        public String toString() {
            return description;
        }

    }

}
