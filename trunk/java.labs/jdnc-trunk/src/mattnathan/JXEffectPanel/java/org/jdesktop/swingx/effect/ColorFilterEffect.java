/*
 * $Id: ColorFilterEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.image.BandCombineOp;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Applies a transform to the bands of the source. The transformation is represented as a float[][] where there should
 * be a cell for each band in the source image optionally plus one. For full details see {@link
 * java.awt.image.BandCombineOp BandCombineOp}.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see java.awt.image.BandCombineOp
 */
public class ColorFilterEffect<T> extends AbstractFilterEffect<T> {
    /**
     * the filter used to not transform the source.
     */
    public static final float[][] IDENTITY_FILTER = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}
    };
    /**
     * The effect which does not transform the source.
     */
    public static final GraphicsEffect<Object> IDENTITY = new ColorFilterEffect<Object>(IDENTITY_FILTER);

    /**
     * The filter to only show the red component of the source. Alpha is preserved.
     */
    public static final float[][] RED_FILTER = {{1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}
    };
    /**
     * Filter to only show the red component. Alpha is preserved.
     */
    public static final GraphicsEffect<Object> RED = new ColorFilterEffect<Object>(RED_FILTER);

    /**
     * The filter to only show the green channel.
     */
    public static final float[][] GREEN_FILTER = {{0, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}
    };
    /**
     * Filter to only show the green component. Alpha is preserved.
     */
    public static final GraphicsEffect<Object> GREEN = new ColorFilterEffect<Object>(GREEN_FILTER);

    /**
     * The filter to only show the blue channel.
     */
    public static final float[][] BLUE_FILTER = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}
    };
    /**
     * Filter to only show the blue component. Alpha is preserved.
     */
    public static final GraphicsEffect<Object> BLUE = new ColorFilterEffect<Object>(BLUE_FILTER);

    /**
     * The filter to only show the alpha component.
     */
    public static final float[][] ALPHA_FILTER = {{0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 1}
    };
    /**
     * Filter the alpha channel.
     */
    public static final GraphicsEffect<Object> ALPHA = new ColorFilterEffect<Object>(ALPHA_FILTER);

    /**
     * Filter to convert the source to grayscale.
     */
    public static final float[][] GRAYSCALE_FILTER = {{0.3f, 0.59f, 0.11f, 0}, {0.3f, 0.59f, 0.11f, 0}, {0.3f, 0.59f, 0.11f, 0}, {0, 0, 0, 1}
    };
    /**
     * The effect to convert a source to grayscale.
     */
    public static final GraphicsEffect<Object> GRAYSCALE = new ColorFilterEffect<Object>(GRAYSCALE_FILTER);

    /**
     * The operation applied to the source.
     */
    private BandCombineOp operation;





    /**
     * Create a new identity effect.
     */
    public ColorFilterEffect() {
        this(null);
    }





    /**
     * Create an effect which applies the given colour filter to the source.
     *
     * @param filter The filter to apply.
     * @see java.awt.image.BandCombineOp
     */
    public ColorFilterEffect(float[][] filter) {
        this.operation = filter == null ? null : new BandCombineOp(filter, null);
    }





    /**
     * Get the filter used for converting the source colours. This never returns null.
     *
     * @return The filter used.
     */
    public float[][] getFilter() {
        return operation == null ? IDENTITY_FILTER : operation.getMatrix();
    }





    /**
     * Set the filter used to convert the source image. When null is passed an identity filter is used.
     *
     * @param filter the filter to use.
     * @see java.awt.image.BandCombineOp
     */
    public void setFilter(float[][] filter) {
        float[][] old = getFilter();
        this.operation = filter == null ? null : new BandCombineOp(filter, null);
        firePropertyChange("filter", old, getFilter());
    }





    @Override
    protected BufferedImage filter(BufferedImage image) {
        if (operation != null) {
            WritableRaster raster = image.getRaster();
            operation.filter(raster, raster);
        }
        return image;
    }





    @Override
    protected boolean isFilterEnabled() {
        return operation != null;
    }
}
