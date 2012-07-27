/*
 * $Id: PainterIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Graphics2D;

import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Simple Icon which delegates its rendering to a Painter instance. This class is intended for use where a painter has
 * the painting code but needs to be represented as an icon.
 * <p/>
 * This class is especially useful for painting gradiented Icons.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class PainterIcon<P extends Component> extends AbstractScalableIcon {
    /**
     * The icons preferred width.
     */
    private int width;
    /**
     * The icons preferred height.
     */
    private int height;

    /**
     * The painter delegate
     */
    private Painter<? super P> painter;

    /**
     * Create a new Icon with 32x32 size and a null painter.
     */
    public PainterIcon() {
        this(32, 32);
    }





    /**
     * Create a new PainterIcon with the given preferred size and a null Painter delegate.
     *
     * @param width The preferred width of this icon.
     * @param height The preferred height of this icon.
     */
    public PainterIcon(int width, int height) {
        this(width, height, null);
    }





    /**
     * Create a new Icon with the given painter as a delegate and 32x32 preferred size.
     *
     * @param painter The painter to delegate the painting to.
     */
    public PainterIcon(Painter<? super P> painter) {
        this(32, 32, painter);
    }





    /**
     * Create a new Icon with the given size and painter delegate.
     *
     * @param width The preferred width of this icon.
     * @param height The preferred height of this icon.
     * @param painter The painter to delegate the painting to.
     */
    public PainterIcon(int width, int height, Painter<? super P> painter) {
        super(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.BOTH, ScalePolicy.ResizePolicy.BEST_FIT));
        this.width = width;
        this.height = height;
        this.painter = painter;
    }





    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return height;
    }





    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return width;
    }





    /**
     * Get the painter responsible for the rendering of this icon.
     *
     * @return The painter used for the painting of this icon.
     */
    public Painter<? super P> getPainter() {
        return painter;
    }





    /**
     * Set the painter responsible for the rendering of this icon. Null painters means that no rendering occurs.
     *
     * @param painter The painter to use for painting this icon.
     */
    public void setPainter(Painter<? super P> painter) {
        Painter<? super P> old = getPainter();
        this.painter = painter;
        firePropertyChange("painter", old, getPainter());
    }





    /**
     * Delegates the painting of this icon to its Painter instance.
     *
     * @param c The component calling the paint.
     * @param g2 The graphics to paint to.
     * @param x The x coordinate to paint to.
     * @param y The y coordinate to paint to.
     * @param width The width of the icon.
     * @param height The height of the icon.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
        Painter<? super P> painter = getPainter();
        P comp = (P) c; // hate to do this but its the only way
        if (painter != null) {
            g2.translate(x, y);
            painter.paint(g2, comp, width, height);
        }
    }
}
