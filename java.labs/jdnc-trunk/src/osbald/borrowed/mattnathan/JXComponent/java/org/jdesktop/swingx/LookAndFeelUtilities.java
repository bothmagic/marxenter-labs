/*
 * $Id: LookAndFeelUtilities.java 2578 2008-07-29 23:16:17Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 * Provides common LookAndFeel utilities.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class LookAndFeelUtilities {
    private LookAndFeelUtilities() {
    }

    /**
     * Installs the given property onto the given component. This will set it so that if set by this method again it will
     * be overriden only if not set directly.
     *
     * @param c The component to set the property on.
     * @param property The property to set.
     * @param value The value to set the property to.
     */
    public static void installProperty(JXComponent c, String property, Object value) {
        c.setLaFUIProperty(property, value);
    }





    /**
     * Installs the given property from the UIManager. This will fall back onto the backup key if the preferred key
     * cannot be found. This is equivalent to {@code installProperty(c, property, get(preferred + "." + property, backup
     * + "." + property));}.
     *
     * @param c The component to install the properties on.
     * @param property The property name.
     * @param preferred The preferred key.
     * @param backup The backup key.
     */
    public static void installDefaultProperty(JXComponent c, String property, String preferred, String backup) {
        installProperty(c, property, get(preferred + '.' + property, backup + '.' + property));
    }





    /**
     * Installs the given property from the UIManager. This will fall back onto the backup key if the preferred key
     * cannot be found. This is equivalent to {@code installProperty(c, property, get(preferred + "." + property,
     * backup));}.
     *
     * @param c The component to install the properties on.
     * @param property The property name.
     * @param preferred The preferred key.
     * @param backup The backup value.
     */
    public static void installDefaultProperty(JXComponent c, String property, String preferred, Object backup) {
        installProperty(c, property, get(preferred + '.' + property, backup));
    }





    /**
     * Returns true if the given property on the given component is that set by the installProperty method.
     *
     * @param c The component to check.
     * @param property The property to check.
     * @return {@code true} if the value of the property is that set via {@link #installProperty}
     */
    public static boolean isUIProperty(JXComponent c, String property) {
        return c.isUIProperty(property);
    }





    /**
     * Used by {@link #addInsets} as the cache when dealing with a JComponent
     */
    private static final Insets insets = new Insets(0, 0, 0, 0);
    /**
     * Adds the given Containers insets to the given Dimension and returns the result. If the given dimension is null
     * then a new one will be created otherwise the given dimension will be reused.
     *
     * @param c The container to get the insets from.
     * @param d The dimension to add the insets to.
     * @return The given dimension plus the containers insets.
     */
    public static Dimension addInsets(Container c, Dimension d) {
        if (d == null) {
            d = new Dimension();
        }
        Insets i;
        if (c instanceof JComponent) {
            i = ((JComponent) c).getInsets(insets);
        } else {
            i = c.getInsets();
        }
        d.width += i.left + i.right;
        d.height += i.top + i.bottom;
        return d;
    }





    /**
     * Subtract the given insets from the given rectangle. This is equivalent to:
     * <code><pre>
     * bounds.x += insets.left;
     * bounds.y += insets.top;
     * bounds.width -= insets.left + insets.right;
     * bounds.height -= insets.top + insets.bottom;
     * </pre></code>
     *
     * @param bounds The bounds to adjust.
     * @param insets The insets to subtract.
     * @return The result of the subtraction.
     */
    public static Rectangle subtractInsets(Rectangle bounds, Insets insets) {
        bounds.x += insets.left;
        bounds.y += insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        return bounds;
    }





    /**
     * Adds the given insets to the given rectangle. This is equivalent to:
     * <code><pre>
     * bounds.x -= insets.left;
     * bounds.y -= insets.top;
     * bounds.width += insets.left + insets.right;
     * bounds.height += insets.top + insets.bottom;
     * </pre></code>
     *
     * @param bounds The bounds to adjust.
     * @param insets The insets to add.
     * @return The result of the addition.
     */
    public static Rectangle addInsets(Rectangle bounds, Insets insets) {
        bounds.x -= insets.left;
        bounds.y -= insets.top;
        bounds.width += insets.left + insets.right;
        bounds.height += insets.top + insets.bottom;
        return bounds;
    }





    /**
     * Returns the bounds of the given container inside its insets. If the given rectangle is null then a new rectangle
     * will be created and returned.
     *
     * @param c The container to check.
     * @param r The rectangle to place the result in.
     * @return The inner bounds of the given container.
     */
    public static Rectangle getInnerBounds(Container c, Rectangle r) {
        r = c.getBounds(r);
        Insets i;
        if (c instanceof JComponent) {
            i = ((JComponent) c).getInsets(insets);
        } else {
            i = c.getInsets();
        }
        r.x = i.left;
        r.y = i.top;
        r.width -= i.left + i.right;
        r.height -= i.top + i.bottom;
        return r;
    }





    /**
     * Gets an Object from the UIManager. This will attempt to use {@code preferred} to collect the Object via {@code
     * UIManager.get(preferred)} but if that fails then {@code UIManager.get(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static Object get(String preferred, String backup) {
        Object o = UIManager.get(preferred);
        return o == null ? UIManager.get(backup) : o;
    }





    /**
     * Gets a Object from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static Object get(String preferred, Object backup) {
        Object o = UIManager.get(preferred);
        return o == null ? backup : o;
    }





    /**
     * Gets a Color from the UIManager. This will attempt to use {@code preferred} to collect the Color via {@code
     * UIManager.getColor(preferred)} but if that fails then {@code UIManager.getColor(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static Color getColor(String preferred, String backup) {
        Color c = UIManager.getColor(preferred);
        return c == null ? UIManager.getColor(backup) : c;
    }





    /**
     * Gets a Color from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static Color getColor(String preferred, Color backup) {
        Color c = UIManager.getColor(preferred);
        return c == null ? backup : c;
    }





    /**
     * Gets a Font from the UIManager. This will attempt to use {@code preferred} to collect the Font via {@code
     * UIManager.getFont(preferred)} but if that fails then {@code UIManager.getFont(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static Font getFont(String preferred, String backup) {
        Font f = UIManager.getFont(preferred);
        return f == null ? UIManager.getFont(backup) : f;
    }





    /**
     * Gets a Font from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static Font getFont(String preferred, Font backup) {
        Font f = UIManager.getFont(preferred);
        return f == null ? backup : f;
    }





    /**
     * Gets a Border from the UIManager. This will attempt to use {@code preferred} to collect the Border via {@code
     * UIManager.getBorder(preferred)} but if that fails then {@code UIManager.getBorder(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static Border getBorder(String preferred, String backup) {
        Border b = UIManager.getBorder(preferred);
        return b == null ? UIManager.getBorder(backup) : b;
    }





    /**
     * Gets a Border from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static Border getBorder(String preferred, Border backup) {
        Border b = UIManager.getBorder(preferred);
        return b == null ? backup : b;
    }





    /**
     * Gets a boolean from the UIManager. This will attempt to use {@code preferred} to collect the boolean via {@code
     * UIManager.getBoolean(preferred)} but if that fails then {@code UIManager.getBoolean(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static boolean getBoolean(String preferred, String backup) {
        Boolean b = (Boolean) UIManager.get(preferred);
        return b == null ? UIManager.getBoolean(backup) : b;
    }





    /**
     * Gets a boolean from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static boolean getBoolean(String preferred, boolean backup) {
        Boolean b = (Boolean) UIManager.get(preferred);
        return b == null ? backup : b;
    }





    /**
     * Gets a int from the UIManager. This will attempt to use {@code preferred} to collect the int via {@code
     * UIManager.getInt(preferred)} but if that fails then {@code UIManager.getInt(backup)} will be used.
     *
     * @param preferred The preferred ui key for the resource.
     * @param backup The backup ui key for the resource.
     * @return The requested ui resource.
     */
    public static int getInt(String preferred, String backup) {
        Integer b = (Integer) UIManager.get(preferred);
        return b == null ? UIManager.getInt(backup) : b;
    }





    /**
     * Gets a int from the UIManager. if the UIManager contains no resource for the {@code preferred} key the given
     * backup value will be returned.
     *
     * @param preferred The preferred ui resource key.
     * @param backup The backup resource value.
     * @return The resource value requested.
     */
    public static int getInt(String preferred, int backup) {
        Integer b = (Integer) UIManager.get(preferred);
        return b == null ? backup : b;
    }





    /**
     * Installs the background, foreground and font on the given component. This will attempt to add the preferred
     * values denoted by {@code preferred + ".background"}, {@code preferred + ".foreground"} and {@code preferred +
     * ".font"} if this fails then the corresponding {@code backup} value will be used.
     *
     * <p>Example usage:</p>
     * <p>{@code installDefaultColorAndFonts(comp, "MyComponent", "Panel");}</p>
     *
     * @param c The component to add the properties to.
     * @param preferred The preferred ui key pre-script.
     * @param backup The backup ui key pre-script.
     */
    public static void installDefaultColorsAndFonts(JXComponent c, String preferred, String backup) {
        installProperty(c, "background", getColor(preferred + '.' + "background", backup + '.' + "background"));
        installProperty(c, "foreground", getColor(preferred + '.' + "foreground", backup + '.' + "foreground"));
        installProperty(c, "font", getFont(preferred + '.' + "font", backup + '.' + "font"));
    }





    /**
     * Demotes the given Color to a non-UIResource instance. This can be used
     * when sub-components properties are managed by the container and the ui
     * tree is updated. If the given Color is not a UIResource then it is
     * returned unchanged.
     *
     * @param color The original color.
     * @return The non-UIResource color.
     */
    public static Color createNonUIResource(Color color) {
        if (color instanceof UIResource) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }
        return color;
    }





    /**
     * Demotes the given Font to a non-UIResource instance. This can be used when
     * sub-components properties are managed by the container and the ui tree is
     * updated. If the given Font is not a UIResource then it is returned
     * unchanged.
     *
     * @param font The original font
     * @return The non-UIResource font.
     */
    public static Font createNonUIResource(Font font) {
        if (font instanceof UIResource) {
            font = font.deriveFont(font.getSize2D());
        }
        return font;
    }





    /**
     * Demotes the given Icon to a non-UIResource instance. This can be used when
     * sub-components properties are managed by the container and the ui tree is
     * updated. If the given Icon is not a UIResource then it is returned
     * unchanged.
     *
     * @param icon The original icon
     * @return The non-UIResource icon.
     */
    public static Icon createNonUIResource(Icon icon) {
        if (icon instanceof UIResource) {
            final Icon temp = icon;
            icon = new Icon() {
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    temp.paintIcon(c, g, x, y);
                }





                public int getIconWidth() {
                    return temp.getIconWidth();
                }





                public int getIconHeight() {
                    return temp.getIconHeight();
                }

            };
        }
        return icon;
    }





    /**
     * Validates any HTML view associated with the given component so that it will wrap after the given width. This
     * method can be used when laying out labels and buttons and any other component which has HTML content with the
     * intent of wrapping the text.
     *
     * @param c The source component.
     * @param width The width to wrap the HTML view at.
     * @see #getPreferredHTMLSize
     */
    public static void wrapHTMLView(Component c, int width) {
        if (c instanceof JComponent) {
            Object o = ((JComponent) c).getClientProperty(BasicHTML.propertyKey);
            if (o instanceof View) {
                View v = (View) o;
                v.setSize(width, 0);
            }
        }
    }





    /**
     * Gets the preferred size of the given component validating any HTML content the component may have. The width
     * argument can be used as a hint as to what width any wrapped text should be.
     *
     * @param c The component to get the preferred size of.
     * @param width The maximum width for the component.
     * @return The preferred size of the component.
     * @see #wrapHTMLView
     */
    public static Dimension getPreferredHTMLSize(Component c, int width) {
        Dimension dim = null;
        if (c instanceof JComponent) {
            Object o = ((JComponent) c).getClientProperty(BasicHTML.propertyKey);
            if (o instanceof View) {
                View v = (View) o;
                // BasicHTML.Renderer uses a child view and overrides the width to lay out the view.
                // By using this child we can get the preferred width of the HTML before we altered the
                // width of the parent in any previous calls.
                if (v.getViewCount() > 0) {
                    v.setSize(Math.min(width, (int) v.getView(0).getPreferredSpan(View.X_AXIS)), 0);
                }
                dim = c.getPreferredSize();
                dim.width = Math.max(c.getMinimumSize().width, dim.width);
            }
        }
        return dim == null ? c.getPreferredSize() : dim;
    }
}
