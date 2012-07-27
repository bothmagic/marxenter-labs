/*
 * $Id: DefaultInspectionValueRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BoundedRangeModel;
import javax.swing.Icon;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.jdesktop.swingx.JXInspectionPane;

/**
 * The default renderer for inspection values. This delegates its rendering to type specific renderers.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultInspectionValueRenderer implements InspectionValueRenderer {

    // UIDefaults as this supports Lazy values and will create objects as a privileged action.
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") // pops up because we use putDefaults instead of putAll
    private static final UIDefaults rendererDefaults = new UIDefaults();

    static {
        // populate default renderers

        Object[] defaults = {
                            Object.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.ObjectRenderer"),
                            Dimension.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.DimensionRenderer"),
                            Date.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.DateRenderer"),
                            Color.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.ColorRenderer"),
                            Boolean.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.BooleanRenderer"),
                            Boolean.TYPE, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.BooleanRenderer"),
                            BoundedRangeModel.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.ProgressRenderer"),
                            Icon.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.ImageRenderer"),
                            Image.class, new UIDefaults.ProxyLazyValue("org.jdesktop.swingx.inspection.renderer.ImageRenderer"),

        };
        rendererDefaults.putDefaults(defaults);
    }





    private Map<Class<?>, InspectionValueRenderer> renderers;

    public DefaultInspectionValueRenderer() {
        super();
    }





    /**
     * Set a custom renderer for the given type.
     *
     * @param type The type to render.
     * @param renderer The renderer for the given type.
     * @see #getRendererForType
     */
    public void setRenderer(Class<?> type, InspectionValueRenderer renderer) {
        if (type == null) {
            throw new NullPointerException("type cannot be null");
        }
        if (renderer == null) {
            if (renderers != null) {
                renderers.remove(type);
            }
        } else {
            if (renderers == null) {
                renderers = new HashMap<Class<?>, InspectionValueRenderer>();
            }
            renderers.put(type, renderer);
        }
    }





    /**
     * Get the user set renderer for the given type.
     *
     * @param type The type to render.
     * @return The renderer or null if no custom renderer.
     * @see #getRendererForType
     */
    public InspectionValueRenderer getRenderer(Class<?> type) {
        return renderers == null ? null : renderers.get(type);
    }





    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row,
          boolean editable, boolean enabled) {
        return getRendererForType(value == null ? null : value.getClass()).getInspectionValueRendererComponent(
              pane, value, label, row, editable, enabled);
    }





    /**
     * Checks for the closest matching renderer for the given type. This will check the user-set renderers, the UI
     * managed renderers and the default renderers in that order for all types that this given type can be assigned to.
     *
     * @param type The type to render.
     * @return The renderer for the given type. This will be non null.
     */
    public InspectionValueRenderer getRendererForType(Class<?> type) {
        InspectionValueRenderer result = null;
        if (type != null) {
            LinkedList<Class<?>> types = new LinkedList<Class<?>>();
            types.addFirst(type);
            while (result == null && !types.isEmpty()) {
                Class<?> curType = types.removeFirst();
                result = getRendererForTypeOnly(curType);
                if (result == null && !curType.equals(Object.class) && !curType.isInterface()) {
                    types.add(curType.getSuperclass());
                    for (Class<?> i : curType.getInterfaces()) {
                        types.addFirst(i);
                    }
                }
            }
        } else {
            result = getRendererForTypeOnly(Object.class);
            assert result != null;
        }
        return result;
    }





    /**
     * Gets the renderer for the given type only. This does not check superclass or interface types but does check the
     * user-set, UIManager and default renderers in that order. The UIManager key will be constructed as {@code
     * InspectionPane.DefaultRenderer.<full type name>}, e.g. {@code InspectionPane.DefaultRenderer.java.util.Date}.
     *
     * @param type The type to check for.
     * @return The renderer, can be null.
     */
    protected InspectionValueRenderer getRendererForTypeOnly(Class<?> type) {
        InspectionValueRenderer result = renderers == null ? null : renderers.get(type);
        if (result == null) {
            // check UI
            result = (InspectionValueRenderer) UIManager.get("InspectionPane.DefaultRenderer." + type.toString());
            if (result == null) {
                result = (InspectionValueRenderer) rendererDefaults.get(type);
            }
        }
        return result;
    }





    /**
     * A subclass of DefaultInspectionValueRenderer that implements UIResource. DefaultInspectionValueRenderer doesn't
     * implement UIResource directly so that applications can safely override the valueRenderer property with
     * DefaultInspectionValueRenderer subclasses.
     */
    public static class UIResource extends DefaultInspectionValueRenderer implements javax.swing.plaf.UIResource {
    }
}
