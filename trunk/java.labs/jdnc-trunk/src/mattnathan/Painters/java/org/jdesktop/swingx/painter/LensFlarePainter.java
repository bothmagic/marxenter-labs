/*
 * $Id: LensFlarePainter.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.painter;

import java.awt.*;
import java.util.Random;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * Defines a painter for displaying a lens flare effect. Additional control over the painting can be accomplished by
 * using the background and foreground painters supplied as part of this painter. Each of these painters are lightweight
 * painters which can be split and share the properties of the source LensFlarePainter.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class LensFlarePainter<T> extends AbstractPainter<T> {
    // these variables are used to avoid excessive object creation
    private final Point locationPoint = new Point();
    private final Dimension sourceSize = new Dimension();
    private static final Dimension ZERO_BOUNDS = new Dimension(0, 0);

    // The locations of the flare and anchor point.
    private LocationPolicy anchorLocation;
    private LocationPolicy flareLocation;

    // paints the flare
    private Painter<? super T> flarePainter;

    // the scale of the shortest side the flare will be painted at. this affects the glow rings too.
    private float flareScale;

    // painters for each of the layers. Allows the painter to be split to paint some behind another object
    // and some in front on another object, i.e. the flare and glare.
    private Painter<T> foregroundPainter;
    private Painter<T> backgroundPainter;





    /**
     * Create a new LensFlarePainter with an anchor location in the centre of the screen and the flare location at
     * 10%, 10% of the screen.
     */
    public LensFlarePainter() {
        this(new LocationPolicy(0.1f, 0.1f));
    }





    /**
     * Create a new LensFlarePainter with the primary flare placed according to the given LocationPolicy.
     *
     * @param flareLocation The location policy for the primary flare.
     * @throws IllegalArgumentException if flareLocation is null.
     */
    public LensFlarePainter(LocationPolicy flareLocation) {
        if (flareLocation == null) {
            throw new IllegalArgumentException("flareLocation cannot be null");
        }
        this.flareLocation = flareLocation;

        anchorLocation = LocationPolicy.valueOf(LocationPolicy.CENTER);
        flareScale = 0.3f;
        flarePainter = new FlarePainter<T>();
        foregroundPainter = new Painter<T>() {
            public void paint(Graphics2D g, T object, int width, int height) {
                paintForeground(g, object, width, height);
            }
        };
        backgroundPainter = new Painter<T>() {
            public void paint(Graphics2D g, T object, int width, int height) {
                paintBackground(g, object, width, height);
            }
        };
        setAntialiasing(false);
    }





    /**
     * Get the size of the flare compared to the shortest side of the painted area. The default is 0.3f
     *
     * @return The scale factor used to size the primary flare.
     */
    public float getFlareScale() {
        return flareScale;
    }





    /**
     * Set the scale factor for sizing the flare for this painter. This must be > 0.
     *
     * @param flareScale The scale factor for the flare.
     * @throws IllegalArgumentException if the given value is <= 0.
     */
    public void setFlareScale(float flareScale) {
        if (flareScale <= 0) {
            throw new IllegalArgumentException("flareScale cannot be <= 0");
        }
        float old = getFlareScale();
        this.flareScale = flareScale;
        firePropertyChange("flareScale", old, getFlareScale());
    }





    /**
     * Get the location of the anchor position. This is the point in the painter that the flare rings are anchored
     * about. The default policy anchors at the centre of the screen.
     *
     * @return The policy for locating the anchor of the flare rings.
     */
    public LocationPolicy getAnchorLocation() {
        return anchorLocation;
    }





    /**
     * Set the location policy used to locate the flare ring line. The default locates to the center of the painted
     * area. This value cannot be null.
     *
     * @param anchorLocation The anchor location for the flare rings.
     */
    public void setAnchorLocation(LocationPolicy anchorLocation) {
        if (anchorLocation == null) {
            throw new IllegalArgumentException("anchorLocation cannot be null");
        }
        LocationPolicy old = getAnchorLocation();
        this.anchorLocation = anchorLocation;
        firePropertyChange("anchorLocation", old, getAnchorLocation());
    }





    /**
     * Get the policy used to locate the flare in the paint area. This will always be non-null and by default locates
     * the flare at (10%, 10%) of the paint area.
     *
     * @return The policy used to locate the primary flare.
     */
    public LocationPolicy getFlareLocation() {
        return flareLocation;
    }





    /**
     * Set the policy used to locate the primary flare. This value cannot be null.
     *
     * @param flareLocation The policy used to locate the primary flare.
     * @throws IllegalArgumentException if flareLocation is null.
     */
    public void setFlareLocation(LocationPolicy flareLocation) {
        if (flareLocation == null) {
            throw new IllegalArgumentException("flareLocation cannot be null");
        }
        LocationPolicy old = getFlareLocation();
        this.flareLocation = flareLocation;
        firePropertyChange("flareLocation", old, getFlareLocation());
    }





    /**
     * Get the painter used to paint the primary flare for this lens flare painter.
     *
     * @return The painter used to paint the primary flare.
     * @see #setFlarePainter(Painter)
     */
    public Painter<? super T> getFlarePainter() {
        return flarePainter;
    }





    /**
     * Set the painter used to paint the primary flare for this lens flare painter. The painter should fill all
     * available as the scale and location are managed externally of the given painter.
     *
     * @param flarePainter The painter to use to paint the primary lens flare. This cannot be null.
     * @throws IllegalArgumentException if the given painter is null.
     * @see #getFlarePainter()
     */
    public void setFlarePainter(Painter<? super T> flarePainter) {
        if (flarePainter == null) {
            throw new IllegalArgumentException("flarePainter cannot be null");
        }
        Painter<? super T> old = getFlarePainter();
        this.flarePainter = flarePainter;
        firePropertyChange("flarePainter", old, getFlarePainter());
    }





    /**
     * Paints the foreground of the lens flare. This is generally the flare rings which extrude from the primary flare
     * along the anchor line.
     *
     * @param g The graphics to paint to.
     * @param t The calling object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintForeground(Graphics2D g, T t, int w, int h) {
        int flarex;
        int flarey;
        int anchorx;
        int anchory;
        final int minDim = Math.min(w, h);
        int flareSize = (int) (minDim * flareScale);

        sourceSize.setSize(w, h);

        // get the anchorLocation point. This is the point about which the flareLocation is expanded
        LocationPolicy anchor = getAnchorLocation();
        assert anchor != null;

        anchor.locate(ZERO_BOUNDS, sourceSize, locationPoint);
        anchorx = locationPoint.x;
        anchory = locationPoint.y;

        // get the flareLocation coordinates. The place the main flareLocation graphic is painted.
        LocationPolicy flare = getFlareLocation();
        assert flare != null;
        flare.locate(ZERO_BOUNDS, sourceSize, locationPoint);
        flarex = locationPoint.x;
        flarey = locationPoint.y;

        final int flareCount = 10;
        final FlareCirclePainter<T> flareCirclePainter = new FlareCirclePainter<T>();
        final Random random = new Random(2);
        final int px = anchorx - flarex;
        final int py = anchory - flarey;

        for (int i = 0; i < flareCount; i++) {
            float location = 0.1f + (random.nextFloat() * (1 - 0.1f));
            float size = 0.1f + (random.nextFloat() * (0.2f + location * (1 - 0.2f)) * (1.2f - 0.1f));
            float alpha = 0.2f + (random.nextFloat() * (0.5f - 0.2f));
            int fx = (int) (anchorx - (px * (location * -2 + 1)));
            int fy = (int) (anchory - (py * (location * -2 + 1)));
            int fs = (int) (flareSize * size);
            int ftx = fx - (fs >> 1);
            int fty = fy - (fs >> 1);
            g.translate(ftx, fty);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            flareCirclePainter.paint(g, t, fs, fs);
            g.translate(-ftx, -fty);
        }
    }





    /**
     * Paints the background of the lens flare. This is generally the primary flare.
     *
     * @param g The graphics to paint to.
     * @param t The calling object.
     * @param w The width.
     * @param h The height.
     */
    protected void paintBackground(Graphics2D g, T t, int w, int h) {
        int flarex;
        int flarey;
        final int minDim = Math.min(w, h);

        int flareSize = (int) (minDim * flareScale);
        sourceSize.setSize(w, h);

        // get the flareLocation coordinates. The place the main flareLocation graphic is painted.
        LocationPolicy flare = getFlareLocation();
        assert flare != null;
        flare.locate(ZERO_BOUNDS, sourceSize, locationPoint);
        flarex = locationPoint.x;
        flarey = locationPoint.y;

        int flaretx = flarex - flareSize / 2;
        int flarety = flarey - flareSize / 2;

        g.translate(flaretx, flarety);
        flarePainter.paint(g, t, flareSize, flareSize);
        g.translate(-flaretx, -flarety);
    }





    @Override
    protected void doPaint(Graphics2D g, T t, int w, int h) {
        backgroundPainter.paint(g, t, w, h);
        foregroundPainter.paint(g, t, w, h);

    }





    /**
     * Get the painter used to paint the foreground of this painter. The foreground generally consists of the lens flare
     * rings, which are usually painted on top of anything else. This will not return null.
     *
     * @return The foreground painter.
     * @see #getBackgroundPainter()
     */
    public Painter<T> getForegroundPainter() {
        return foregroundPainter;
    }





    /**
     * Returns the painter used to paint the background of this painter. The background usually consists of the primary
     * flare and anything else that should appear behind other objects. This can be used to separate the painting of
     * the background and foreground to give interesting effects.
     *
     * @return The background painter. Not null.
     */
    public Painter<T> getBackgroundPainter() {
        return backgroundPainter;
    }





    /**
     * Painter used to paint the flare rings extruded from the primary flare.
     */
    protected static class FlareCirclePainter<T> extends AbstractPainter<T> {
        // this is used to produce strength and colour of the rings.
        private Random rand = new Random(0);





        @Override
        protected void doPaint(Graphics2D g, T t, int w, int h) {
            if (w > 1 && h > 1) {
                final float red = rand.nextFloat();
                final float green = rand.nextFloat();
                final float blue = rand.nextFloat();
                final Color color1 = new Color(red, green, blue);
                final Color color2 = new Color(red, green, blue, 0);
                RadialGradientPaint paint = new RadialGradientPaint(w / 2, h / 2, w / 2,
                        new float[]{
                                rand.nextFloat() * 0.8f, 0.95f, 1f
                        },
                        new Color[]{
                                color2, color1, color2
                        }, RadialGradientPaint.CycleMethod.NO_CYCLE);
                g.setPaint(paint);
                g.fillOval(0, 0, w, h);
            }
        }
    }
}
