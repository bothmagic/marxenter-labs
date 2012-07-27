/*
 * $Id: LocationPolicy.java 2728 2008-10-07 16:00:19Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.util;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.SwingConstants;

/**
 * Defines a location policy for placing a dimension within another dimension.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class LocationPolicy implements SwingConstants {

    /**
     * Cache of location policy instances. Maximum of 9, one for every compass location + center.
     */
    private static final LocationPolicy[] locationPolicys = new LocationPolicy[9];

    private final float horizontalAlignment;
    private final float verticalAlignment;





    /**
     * Create a new location policy based on one of the {@link javax.swing.SwingConstants} compass point values.
     * Possible values are:
     * <p/>
     * <ul> <li>{@link javax.swing.SwingConstants#NORTH}</li> <li>{@link javax.swing.SwingConstants#NORTH_EAST}</li>
     * <li>{@link javax.swing.SwingConstants#NORTH_WEST}</li> <li>{@link javax.swing.SwingConstants#SOUTH}</li>
     * <li>{@link javax.swing.SwingConstants#SOUTH_EAST}</li> <li>{@link javax.swing.SwingConstants#SOUTH_WEST}</li>
     * <li>{@link javax.swing.SwingConstants#EAST}</li> <li>{@link javax.swing.SwingConstants#WEST}</li> <li>{@link
     * javax.swing.SwingConstants#CENTER}</li> </ul>
     * <p/>
     * Instead of using this constructor a user should use {@link #valueOf(int)} instead as this provides a caching
     * mechanism for this common case.
     *
     * @param compassPoint The policy location of this instance.
     * @throws IllegalArgumentException if the given value does not match one of the valid compass points.
     * @see #valueOf(int)
     */
    public LocationPolicy(int compassPoint) {
        switch (compassPoint) {
            case NORTH:
                this.horizontalAlignment = 0.5f;
                this.verticalAlignment = 0f;
                break;
            case SOUTH:
                this.horizontalAlignment = 0.5f;
                this.verticalAlignment = 1f;
                break;
            case EAST:
                this.horizontalAlignment = 1f;
                this.verticalAlignment = 0.5f;
                break;
            case WEST:
                this.horizontalAlignment = 0f;
                this.verticalAlignment = 0.5f;
                break;
            case NORTH_WEST:
                this.horizontalAlignment = 0f;
                this.verticalAlignment = 0f;
                break;
            case NORTH_EAST:
                this.horizontalAlignment = 1f;
                this.verticalAlignment = 0f;
                break;
            case SOUTH_WEST:
                this.horizontalAlignment = 0f;
                this.verticalAlignment = 1f;
                break;
            case SOUTH_EAST:
                this.horizontalAlignment = 1f;
                this.verticalAlignment = 1f;
                break;
            case CENTER:
                this.horizontalAlignment = 0.5f;
                this.verticalAlignment = 0.5f;
                break;
            default:
                throw new IllegalArgumentException("unknown compas point: " + compassPoint);
        }
    }





    /**
     * Create a new LocationPolicy with the given horizontal and vertical parameters.
     *
     * @param horizontalAlignment The horizontal alignment of this policy.
     * @param verticalAlignment   The vertical alignment of this policy.
     */
    public LocationPolicy(float horizontalAlignment, float verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        this.horizontalAlignment = horizontalAlignment;
    }





    /**
     * Translates the given {@code size} to fit within the given {@code target} placing the result in {@code result}. If
     * {@code result} is null then a new Point is created and returned. If the target size is smaller that the source
     * size then the resultant points coordinates may be negative.
     *
     * @param source The source dimension to locate.
     * @param target The target dimension to place the source into.
     * @param result The point to place the result in and to return.
     * @return The result of the location.
     *
     * @throws NullPointerException if either source or target are null.
     */
    public Point locate(Dimension source, Dimension target, Point result) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        if (target == null) {
            throw new NullPointerException("target cannot be null");
        }

        if (result == null) {
            result = new Point();
        } else {
            result.setLocation(0, 0);
        }

        int sw = source.width;
        int sh = source.height;

        int tw = target.width;
        int th = target.height;

        float v = getVerticalAlignment();
        float h = getHorizontalAlignment();

        float x = (tw - sw) * h;
        float y = (th - sh) * v;

        result.setLocation(x, y);

        return result;
    }





    /**
     * Get the horizontal alignment of this location policy. This is a value between 0 and 1 inclusive.
     *
     * @return The horizontal alignment.
     */
    public float getHorizontalAlignment() {
        return horizontalAlignment;
    }





    /**
     * Get the vertical alignment of this location policy. this is a value between 0 and 1 inclusive.
     *
     * @return The vertical alignment.
     */
    public float getVerticalAlignment() {
        return verticalAlignment;
    }





    /**
     * Gets a cached version of the LocationPolicy that will satisfy the given location. This will return a cached
     * instance for that compass point.
     *
     * @param location The compass point location policy to get.
     * @return The location policy for the given compass point. This will not be null.
     */
    public static LocationPolicy valueOf(int location) {
        if (location < 0 && location >= locationPolicys.length) {
            throw new IllegalArgumentException("location must be a valid compass point: " + location);
        }

        LocationPolicy policy = locationPolicys[location];
        if (policy == null) {
            policy = locationPolicys[location] = new LocationPolicy(location);
        }
        return policy;
    }
}
