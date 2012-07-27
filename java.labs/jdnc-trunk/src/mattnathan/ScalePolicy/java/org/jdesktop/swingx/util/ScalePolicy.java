/*
 * $Id: ScalePolicy.java 2728 2008-10-07 16:00:19Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.util;

import java.awt.Dimension;

/**
 * Defines a policy for scaling a dimension. This provides all the common configuration parameters for a scale via the
 * DimensionPolicy and ResizePolicy enums. This class is usually used in conjunction with the {@link
 * org.jdesktop.swingx.util.LocationPolicy} class.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see org.jdesktop.swingx.util.LocationPolicy
 */
public class ScalePolicy {

    /**
     * Common ScalePolicy equivalent to {@code ScalePolicy.valueOf(DimensionPolicy.FIXED_RATIO,ResizePolicy.NONE)}.
     */
    public static final ScalePolicy NONE;
    /**
     * Common ScalePolicy equivalent to {@code ScalePolicy.valueOf(DimensionPolicy.FIXED_RATIO,
     *ResizePolicy.BEST_FIT)}.
     */
    public static final ScalePolicy FIXED_RATIO;
    /**
     * Common ScalePolicy equivalent to {@code ScalePolicy.valueOf(DimensionPolicy.BOTH,ResizePolicy.BEST_FIT)}.
     */
    public static final ScalePolicy BOTH;

    /**
     * ScalePolicy cache. Once for each combination of HORIZONTAL, VERTICAL, BOTH, FIXED_RATIO with GROW, SHRINK,
     * BEST_FIT plus a single entry for NONE.
     */
    private static final ScalePolicy[] scalePolicys = new ScalePolicy[13];





    /**
     * Defines how the dimensions are scaled for the ScalePolicy.
     */
    public static enum DimensionPolicy {

        /**
         * Only the horizontal dimension is scaled. The vertical dimension is not adjusted.
         */
        HORIZONTAL,
        /**
         * Only the vertical dimension is changed, the horizontal dimension is not adjusted.
         */
        VERTICAL,
        /**
         * Both horizontal and vertical dimensions are changed independently. No consideration for aspect ratio is
         * made.
         */
        BOTH,
        /**
         * Both the horizontal and vertical dimensions are changed and the aspect ratio is maintained.
         */
        FIXED_RATIO
    }





    /**
     * Defines how the dimension are scaled to match the target.
     */
    public static enum ResizePolicy {

        /**
         * The dimensions will only ever get smaller than their original sizes. The source Dimension will never get
         * bigger than it already is.
         */
        SHRINK,
        /**
         * The dimensions will only ever get larger than their original sizes. The source dimension will never get
         * smaller than it already is.
         */
        GROW,
        /**
         * The source dimension will be scaled so that it fits exactly within the target dimension. This may involve
         * either increasing or decreasing its size.
         */
        BEST_FIT,
        /**
         * No change to the source dimension sill be made.
         */
        NONE
    }





    static {
        NONE = ScalePolicy.valueOf(DimensionPolicy.FIXED_RATIO, ResizePolicy.NONE);
        FIXED_RATIO = ScalePolicy.valueOf(DimensionPolicy.FIXED_RATIO, ResizePolicy.BEST_FIT);
        BOTH = ScalePolicy.valueOf(DimensionPolicy.BOTH, ResizePolicy.BEST_FIT);
    }





    private final DimensionPolicy dimensionPolicy;
    private final ResizePolicy resizePolicy;





    /**
     * Creates a new resize policy using the given configuration properties. Neither property can be null. The {@link
     * #valueOf(org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy,org.jdesktop.swingx.util.ScalePolicy.ResizePolicy)}
     * method should be used instead of this constructor.
     *
     * @param dimensionPolicy How dimension should be handled for this policy.
     * @param resizePolicy    How scaling should be handled for this policy.
     * @throws NullPointerException if either given value is null.
     * @see #valueOf(org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy,org.jdesktop.swingx.util.ScalePolicy.ResizePolicy)
     */
    public ScalePolicy(DimensionPolicy dimensionPolicy, ResizePolicy resizePolicy) {
        if (dimensionPolicy == null) {
            throw new NullPointerException("dimensionPolicy cannot be null");
        }
        if (resizePolicy == null) {
            throw new NullPointerException("resizePolicy cannot be null");
        }
        this.resizePolicy = resizePolicy;
        this.dimensionPolicy = dimensionPolicy;
    }





    /**
     * Fit the given source dimension inside the given target dimension given this ScalePolicys properties. The result
     * will be placed into the given {@code result} dimension and returned. If the given result dimension is null then a
     * new dimension is created.
     *
     * @param source The source dimension.
     * @param target The target dimension.
     * @param result The dimension to place the result in.
     * @return The result of the scale process.
     */
    public Dimension fitInto(Dimension source, Dimension target, Dimension result) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        if (target == null) {
            throw new NullPointerException("target cannot be null");
        }
        if (result == null) {
            result = new Dimension(source);
        } else {
            result.setSize(source);
        }

        ResizePolicy resizePolicy = getResizePolicy();
        if (resizePolicy != ResizePolicy.NONE) {
            DimensionPolicy dimensionPolicy = getDimensionPolicy();

            boolean shrink = resizePolicy == ResizePolicy.BEST_FIT || resizePolicy == ResizePolicy.SHRINK;
            boolean grow = resizePolicy == ResizePolicy.BEST_FIT || resizePolicy == ResizePolicy.GROW;

            int tw = target.width;
            int th = target.height;

            int sw = source.width;
            int sh = source.height;

            // adjust resize policy dims
            if (!shrink) {
                tw = Math.max(tw, sw);
                th = Math.max(th, sh);
            }

            if (!grow) {
                tw = Math.min(tw, sw);
                th = Math.min(th, sh);
            }

            int rw = sw;
            int rh = sh;
            switch (dimensionPolicy) {
                case FIXED_RATIO:
                    float ratio = (float) sw / (float) sh;
                    int extw = (int) (th * ratio);
                    int exth = (int) (tw / ratio);
                    if (exth > th) {
                        rh = th;
                        rw = extw;
                    } else if (extw > tw) {
                        rw = tw;
                        rh = exth;
                    } else {
                        rw = tw;
                        rh = th;
                    }
                    break;
                case BOTH:
                    rw = tw;
                    rh = th;
                    break;
                case HORIZONTAL:
                    rw = tw;
                    break;
                case VERTICAL:
                    rh = th;
                    break;
            }

            result.setSize(rw, rh);
        }
        return result;
    }





    /**
     * Get the resize policy used to configure the scale process.
     *
     * @return The resize policy.
     */
    public ResizePolicy getResizePolicy() {
        return resizePolicy;
    }





    /**
     * Get the dimension policy used to configure the scale process.
     *
     * @return The dimension policy.
     */
    public DimensionPolicy getDimensionPolicy() {
        return dimensionPolicy;
    }





    /**
     * Gets a cached instance of a ScalePolicy. If the given ResizePolicy is {@link
     * org.jdesktop.swingx.util.ScalePolicy.ResizePolicy#NONE} then the DimensionPolicy is ignored and a default value
     * of {@link org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy#FIXED_RATIO} will be used as this will give the
     * same behaviour.
     *
     * @param dimensionPolicy The DimensionPolicy the returned ScalePolicy will have.
     * @param resizePolicy    The ResizePolicy the returned ScalePolicy will have.
     * @return A ScalePolicy with the given properties.
     *
     * @throws NullPointerException if either given value is null.
     */
    public static ScalePolicy valueOf(DimensionPolicy dimensionPolicy, ResizePolicy resizePolicy) {
        if (dimensionPolicy == null) {
            throw new NullPointerException("dimensionPolicy cannot be null");
        }
        if (resizePolicy == null) {
            throw new NullPointerException("resizePolicy cannot be null");
        }
        int di = dimensionPolicy.ordinal();
        int ri = resizePolicy.ordinal();
        int i = resizePolicy == ResizePolicy.NONE ? scalePolicys.length - 1 : (di + (4 * ri));
        ScalePolicy result = scalePolicys[i];
        if (result == null) {
            result = resizePolicy == ResizePolicy.NONE ?
                    new ScalePolicy(DimensionPolicy.FIXED_RATIO, resizePolicy) :
                    new ScalePolicy(dimensionPolicy, resizePolicy);
            scalePolicys[i] = result;
        }
        return result;
    }
}
