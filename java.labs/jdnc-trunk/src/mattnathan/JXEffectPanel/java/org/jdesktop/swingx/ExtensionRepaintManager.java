/*
 * $Id: ExtensionRepaintManager.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import javax.swing.JComponent;
import javax.swing.RepaintManager;
import java.awt.Component;
import java.awt.Rectangle;

/**
 * An extension to the default RepaintManager to provide a mechanism to allow JComponents to extend the repaint region.
 * A JComponent which requires the repaint region be extended or the source of the repaint event be changed should
 * implement the RepaintManagerExtension interface.
 * <p/>
 * There are two main reasons why you would want to install this RepaintManager; one you require that the source of
 * repaint events are changed, this is required for components that change the opacity of child components without
 * updating their opaque property (i.e. TranslucentPanel). Two: you are adjusting the painting of child components so
 * that an area outside the dirty area needs updating, this can be for any reason some of the most common are
 * reflections and shadows.
 * <p/>
 * Note: It is currently impossible to paint into areas outside the bounds of a component, this is enforced by the
 * painting and buffering mechanism of Swing itself. For this reason any component who attempts to extend the diry
 * region beyond their bounds will have that area clipped.
 *
 * @author Matt Nathan
 * @see org.jdesktop.swingx.JXEffectPanel
 */
public class ExtensionRepaintManager extends RepaintManager {

    /**
     * Creates a new instance of ExtensionRepaintManager
     *
     * @see #install()
     */
    public ExtensionRepaintManager() {
    }





    /**
     * Add a component in the list of components that should be refreshed. If <i>c</i> already has a dirty region, the
     * rectangle <i>(x,y,w,h)</i> will be union-ed with the region that should be redrawn.
     * <p/>
     * This method additionally provides support for the RepaintManagerExtension extension API. Any components which
     * implement RepaintManagerExtension will have their extendDirtyRegion method called allowing them to extend the
     * paint region.
     *
     * @param c Component to repaint, null results in nothing happening.
     * @param x X coordinate of the region to repaint
     * @param y Y coordinate of the region to repaint
     * @param w Width of the region to repaint
     * @param h Height of the region to repaint
     * @see JComponent#repaint
     */
    @Override
    public void addDirtyRegion(JComponent c, int x, int y, int w, int h) {

        Rectangle region = new Rectangle(x, y, w, h);
        JComponent original = c;
        for (Component comp = c; comp != null; comp = comp.getParent()) {
            if (comp instanceof RepaintManagerExtension && comp instanceof JComponent) {
                boolean result = ((RepaintManagerExtension) comp).extendDirtyRegion(original, region);
                if (result) {
                    c = (JComponent) comp;

                    // correct any bounds extending beyond their source.
                    region.x = Math.max(0, region.x);
                    region.y = Math.max(0, region.y);
                    region.width = Math.min(c.getWidth(), region.width);
                    region.height = Math.min(c.getHeight(), region.height);

                    // extend the region
                    x = region.x;
                    y = region.y;
                    w = region.width;
                    h = region.height;
                } else {
                    // correct any changed bounds that do not return a new source component
                }
            }

            region.x += comp.getX();
            region.y += comp.getY();
        }

        super.addDirtyRegion(c, x, y, w, h);
    }





    /**
     * Install this repaint manager. This calls {@code install(false)}.
     *
     * @return {@code true} if the repaint manager is now a ExtensionRepaintManager.
     * @see #install(boolean)
     */
    public static boolean install() {
        return install(false);
    }





    /**
     * Installs this repaint manager. The current RepaintManager will only be replaced if it is the default
     * RepaintManager or force is true.
     *
     * @param force true if the existing repaint manager should be replaced even if it not the default repaint manager.
     * @return {@code true} if the repaint manager is now an ExtensionRepaintManager.
     */
    public static boolean install(boolean force) {
        RepaintManager old = currentManager(null);
        boolean installed = old instanceof ExtensionRepaintManager;
        if (!installed && (force || old.getClass() == RepaintManager.class)) {
            setCurrentManager(new ExtensionRepaintManager());
            installed = true;
        }
        return installed;
    }





    /**
     * Implement this interface if your component requires that the dirty region or source of the update needs changing.
     *
     * @author Matt Nathan
     */
    public static interface RepaintManagerExtension {
        /**
         * Gives the ability for a container to extend the dirty region of a child's repaint request.
         * <p/>
         * There are two parts to this method that you should take note of, one; the return value indicates that the
         * source of the repaint event should be changed to be this implementing instance. This has the effect of
         * bypassing any optimisations the Swing painting stack is doing based on the opacity (or any other property)
         * of the original repaint source and instead does those optimisation based on this instance instead. The second
         * part is more complicated and as a result more powerful; {@code dirtyRegion}. This property represent the
         * dirty area of the source component that a repaint is to be scheduled for, that is when this method is entered
         * it represents the original dirty are and when the method returns it represent a new repaint area. Because of
         * these requisites it is possible for a parent component to extend a repaint area requested by a child, for
         * example to update a reflection at the same time, or a drop shadow, or a transformation or some other state
         * like validation markers.
         * <p/>
         * Notes:
         * <ul>
         * <li>The {@code dirtyRegion} rectangle passed into this method will always be in the coordinate space of this
         * instance.</li>
         * <li>Extending the bounds of {@code dirtyRegion} past the bounds of this component instance will be
         * automatically trimmed to be enclosed withing this components bounds.</li>
         * <li>No unioning is performed on the old dirty region and the new dirty region, this allows an implementor to
         * completely ignore any repaint requests if needed, or to translate dirty regions to somewhere else
         * entirely.</li>
         * </ul>
         *
         * @param original    The original component that is the current originator of the addDirtyRegion call.
         * @param dirtyRegion The current dirty region in the coordinate space of this component. Set the x, y, width
         *                    and height of this Rectangle to adjust the dirty region. Any dimensions outside of this
         *                    components bounds will be trimmed.
         * @return true if the repaint source should be changed to this component.
         */
        public boolean extendDirtyRegion(JComponent original, Rectangle dirtyRegion);
    }
}
