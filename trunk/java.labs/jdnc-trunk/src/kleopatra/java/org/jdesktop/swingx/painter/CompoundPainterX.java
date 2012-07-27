/*
 * Created on 03.04.2007
 *
 */
package org.jdesktop.swingx.painter;

/*
 * $Id: CompoundPainterX.java 1192 2007-04-03 14:59:06Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */


import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * 
 *  Alternative Cache/validation handling/notification. 
 *  Removed dirty property. Added listener to contained painters.
 *  
 * <p>A {@link Painter} implemention composed of an array of <code>Painter</code>s.
 * <code>CompoundPainter</code> provides a means for combining several individual
 * <code>Painter</code>s, or groups of them, into one logical unit. Each of the
 * <code>Painter</code>s are executed in order. BufferedImageOp filter effects can
 * be applied to them together as a whole. The entire set of painting operations
 * may be cached together.</p>
 *
 * <p></p>
 *
 * <p>For example, if I want to create a CompoundPainter that started with a blue
 * background, had pinstripes on it running at a 45 degree angle, and those
 * pinstripes appeared to "fade in" from left to right, I would write the following:
 * <pre><code>
 *  Color blue = new Color(0x417DDD);
 *  Color translucent = new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), 0);
 *  panel.setBackground(blue);
 *  panel.setForeground(Color.LIGHT_GRAY);
 *  GradientPaint blueToTranslucent = new GradientPaint(
 *    new Point2D.Double(.4, 0),
 *    blue,
 *    new Point2D.Double(1, 0),
 *    translucent);
 *  Painter veil =  new BasicGradientPainter(blueToTranslucent);
 *  Painter pinstripes = new PinstripePainter(45);
 *  Painter backgroundPainter = new BackgroundPainter();
 *  Painter p = new CompoundPainter(backgroundPainter, pinstripes, veil);
 *  panel.setBackgroundPainter(p);
 * </code></pre></p>
 *
 * @author rbair
 */
public class CompoundPainterX<T> extends AbstractPainterX<T> {
    private Painter[] painters = new Painter[0];
    private AffineTransform transform;
    private boolean clipPreserved = false;
    private PropertyChangeListener painterListener;

    /** Creates a new instance of CompoundPainter */
    public CompoundPainterX() {
    }
    
    /**
     * Convenience constructor for creating a CompoundPainter for an array
     * of painters. A defensive copy of the given array is made, so that future
     * modification to the array does not result in changes to the CompoundPainter.
     *
     * @param painters array of painters, which will be painted in order
     */
    public CompoundPainterX(Painter... painters) {
        setPainters(painters);
    }
    
    /**
     * Sets the array of Painters to use. These painters will be executed in
     * order. A null value will be treated as an empty array. A 
     * PropertyChangeListener will be installed to the contained painters, if
     * any.
     *
     * PENDING: use generified param!
     * 
     * @param painters array of painters, which will be painted in order
     */
    public void setPainters(Painter... painters) {
        Painter[] old = getPainters();
        uninstallPainterListener(old);
        this.painters = new Painter[painters == null ? 0 : painters.length];
        System.arraycopy(painters, 0, this.painters, 0, this.painters.length);
        installPainterListener(getPainters());
        clearLocalCache();
        firePropertyChange("painters", old, getPainters());
    }
    
    private void installPainterListener(Painter[] old) {
        for (int i = 0; i < old.length; i++) {
            if (old[i] instanceof AbstractPainterX) {
                ((AbstractPainterX) old[i]).addPropertyChangeListener(getPainterListener());
            }
        }
    }

    private void uninstallPainterListener(Painter[] old) {
        for (int i = 0; i < old.length; i++) {
            if (old[i] instanceof AbstractPainterX) {
                ((AbstractPainterX) old[i]).removePropertyChangeListener(getPainterListener());
            }
        }
        
    }

    /**
     * Guaranteed to be not null.
     * 
     * @return the listener for property changes of contained painters. 
     */
    protected PropertyChangeListener getPainterListener() {
        if (painterListener == null) {
            painterListener = createPainterListener();
        }
        return painterListener;
    }

    /**
     * Factory method to create the listener for property changes
     * of contained painters. Must not be null. Subclasses may 
     * override. Here: calls clearLocalCache on property change.
     * 
     * @return
     */
    protected PropertyChangeListener createPainterListener() {
        
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                clearLocalCache();
            }
        };
        return l;
    }

    /**
     * Gets the array of painters used by this CompoundPainter
     * @return a defensive copy of the painters used by this CompoundPainter.
     *         This will never be null.
     */
    public final Painter[] getPainters() {
        Painter[] results = new Painter[painters.length];
        System.arraycopy(painters, 0, results, 0, results.length);
        return results;
    }
    
    
    /**
     * Indicates if the clip produced by any painter is left set once it finishes painting. 
     * Normally the clip will be reset between each painter. Setting clipPreserved to
     * true can be used to let one painter mask other painters that come after it.
     * @return if the clip should be preserved
     * @see #setClipPreserved(boolean)
     */
    public boolean isClipPreserved() {
        return clipPreserved;
    }
    
    /**
     * Sets if the clip should be preserved.
     * Normally the clip will be reset between each painter. Setting clipPreserved to
     * true can be used to let one painter mask other painters that come after it.
     * 
     * @param shouldRestoreState new value of the clipPreserved property
     * @see #isClipPreserved()
     */
    public void setClipPreserved(boolean shouldRestoreState) {
        boolean oldShouldRestoreState = isClipPreserved();
        this.clipPreserved = shouldRestoreState;
        clearCache();
//        setDirty(true);
        firePropertyChange("shouldRestoreState",oldShouldRestoreState,shouldRestoreState);
    }

    /**
     * Gets the current transform applied to all painters in this CompoundPainter. May be null.
     * @return the current AffineTransform
     */
    public AffineTransform getTransform() {
        return transform;
    }

    /**
     * Set a transform to be applied to all painters contained in this CompoundPainter
     * @param transform a new AffineTransform
     */
    public void setTransform(AffineTransform transform) {
        AffineTransform old = getTransform();
        this.transform = transform;
        clearCache();
//        setDirty(true);
        firePropertyChange("transform",old,transform);
    }
    
    /**
     * <p>Iterates over all child <code>Painter</code>s and gives them a chance
     * to validate themselves. If any of the child painters are dirty, then
     * this <code>CompoundPainter</code> marks itself as dirty.</p>
     *
     * @inheritDoc
     */
    @Override
    protected void validate(T object) {
        for (Painter p : painters) {
            if (p instanceof AbstractPainter) {
                AbstractPainterX ap = (AbstractPainterX)p;
                ap.validate(object);
            }
        }
    }


    /**
     * <p>
     * Clears the cache of this <code>Painter</code>, and all child
     * <code>Painters</code>. This is done to ensure that resources are
     * collected, even if clearCache is called by some framework or other code
     * that doesn't realize this is a CompoundPainter.
     * </p>
     * 
     * <p>
     * Call #clearLocalCache if you only want to clear the cache of this
     * <code>CompoundPainter</code>
     * 
     * @inheritDoc
     */
    @Override
    public void clearCache() {
        for (Painter p : painters) {
            if (p instanceof AbstractPainter) {
                AbstractPainter ap = (AbstractPainter) p;
                ap.clearCache();
            }
        }
        clearLocalCache();
    }

    /**
     * <p>
     * Clears the cache of this painter only, and not of any of the children.
     * </p>
     */
    public void clearLocalCache() {
        super.clearCache();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void doPaint(Graphics2D g, T component, int width, int height) {
        for (Painter p : getPainters()) {
            Graphics2D temp = (Graphics2D) g.create();
            p.paint(temp, component, width, height);
            if(isClipPreserved()) {
                g.setClip(temp.getClip());
            }
            temp.dispose();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void configureGraphics(Graphics2D g) {
        //applies the transform
        AffineTransform tx = getTransform();
        if (tx != null) {
            g.setTransform(tx);
        }
    }
    
    /**
     * @inheritDoc
     * 
     * PENDING: depend on contained painter's cacheable?
     */
    @Override
    protected boolean shouldUseCache() {
        return (isCacheable() && painters != null && painters.length > 0) || super.shouldUseCache();
    }
}
