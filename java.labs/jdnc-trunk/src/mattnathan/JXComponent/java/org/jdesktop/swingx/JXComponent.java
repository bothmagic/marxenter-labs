/*
 * $Id: JXComponent.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.plaf.UIPropertySupport;
import org.jdesktop.swingx.plaf.XComponentUI;

/**
 * The base class for all new swing extension components. For a description of how to use components see {@link
 * JComponent}.
 *
 * <p> JXComponent extends the standard JComponent in a number of ways in order to aid in the creation of new
 * components, not part of the standard Swing distribution. The primary extensions are support for <a href="#painters">
 * foreground and background painters</a>, easy <a href="#lnf">look and feel</a> support and improved (fixed) <a
 * href="#uiresource">UIResource</a> support.
 *
 *
 * <a name="painters"><h4>Foreground and Background Painters</h4></a>
 *
 * <p> While JComponent has the {@link JComponent#setForeground foreground} and {@link JComponent#setBackground
 * background} colour properties which can be used to hint to the UI delegate (or other painting code) how the content
 * should be painted they are very limited in scope. JXComponent adds support for SwingX Painters to all sub-classes
 * allowing complete control over how a component looks, not just its foreground and background colours.
 *
 * <p> SwingX Painters are similar to both {@link javax.swing.border.Border borders} and {@link javax.swing.Icon icons}
 * in that they simply define a set of painting rules to be rendered to a Graphics object when needed.
 *
 * <p> The <em>background</em> painter of a JXComponent is responsible for painting the background of the component, typically
 * this involves painting the background colour over the component but can be used to paint anything from gradiented
 * backgrounds to patterns and images. The background painter is always painted before the foreground painter and should
 * and is invoked by the installed XComponentUI instance.
 *
 * <p> The <em>foreground</em> painter is responsible for painting the content of the component, this could include the
 * text of a label or button or more complicated contents as found in lists or tables. Note that the foreground painter
 * does not have any bearing on the children of the component and as such any effects applied to a painter will not be
 * applied to these children. As with the background painter the foreground painter is invoked by the XComponentUI
 * instance assigned to the component.
 *
 *
 * <a name="lnf"><h4>Look and Feel Support</h4></a>
 *
 * <p> In the default JComponent the look and feel is defined and assigned through a lookup in the {@link UIManager
 * UIManagers} defaults tables. This is installed on a call to {@link JComponent#updateUI} and generally follows the
 * pattern:
 * <blockquote><code><pre>
 * public void updateUI() {
 *    setUI((MyComponentUI) UIManager.getUI(this));
 * }
 * </pre></code></blockquote>
 *
 * The problem with this approach is that when creating a new component that is not a part of core swing the UIManager
 * generally knows nothing of your new component and will not have an entry in its defaults tables providing the
 * ComponentUI instance to assign to it.
 *
 * <p> There are a two ways to solve (or work around) this problem: one, you provide a way to adjust the defaults table
 * of the UIManager so that your custom component is known and will show up when {@code UIManager.getUI(this)} is
 * called. This is the approach used by the LookAndFeelAddons class and framework provided and used by SwingX. The
 * typical addon component will have a static initialiser who contributes a list of extension components which are then
 * managed by the framework so that their ui defaults are available through the UIManager at all times.
 *
 * <blockquote><code><pre>
 * public class MyComponent extends JXComponent {
 *     static {
 *         LookAndFeelAddons.contribute(new MyComponentAddon());
 *     }
 *
 *     public MyComponent() {
 *         updateUI();
 *     }
 * }
 * </pre></code></blockquote>
 *
 * <p> The second approach involves extending the places the component looks for the assigned ui delegate instance. This
 * functionality is provided by the JXComponent class in the form of the {@link #createUI} method. This method will be
 * invoked if no ui delegate is found from the UIManagers defaults tables and by default creates an instance of the
 * XComponentUI class. Because the creation of the custom look and feel delegate is performed only if a suitable
 * delegate is not found in the UIManager the LookAndFeelAddons framework will override this mechanism when used.
 *
 *
 * <a name="uiresource"><h4>UI Resource Support</h4></a>
 *
 * <p> JComponent provides a mechanism for specifying if a particular properties value was set by the UI delegate and as
 * such can be overridden by any subsequent UI delegate that may be installed. This mechanism is provided in two parts
 * <dl>
 * <dt>{@link javax.swing.plaf.UIResource}</dt>
 * <dd>Provides a marker interface for specifying that a particular object was set by the current look and feels ui
 * delegate instance. There are a number of utility classes associated with this marker interface; namely
 * <ul>
 *   <li>{@link javax.swing.plaf.ColorUIResource}</li>
 *   <li>{@link javax.swing.plaf.BorderUIResource}</li>
 *   <li>{@link javax.swing.plaf.IconUIResource}</li>
 *   <li>{@link javax.swing.plaf.DimensionUIResource}</li>
 * </ul>
 * among others.</dd>
 * <dt>{@link javax.swing.LookAndFeel#installProperty installProperty} and {@link JComponent#setUIProperty
 * setUIProperty}</dt>
 * <dd>These pair of methods are provided to allow the setting of ui properties where the property type cannot be
 * extend the UIResource interface. This is enforced as being of primitive type and only for the properties
 * <ul>
 *   <li>opaque</li>
 *   <li>autoscrolls</li>
 *   <li>focusTraversalKeysForward</li>
 *   <li>focusTraversalKeysBackwards</li>
 * </ul>
 * This support is provided by a package private method in JComponent which is called from within the LookAndFeel class.
 * </dd>
 *
 * <p> There are a number of problems with each of these approaches; with the installProperty approach there is no way
 * to extend the list of properties that can be set through this method due to the JComponent.setUIProperty method being
 * package private. This in itself is the main cause of all other problems as there are no easy ways to fix the problems
 * with UIResource. These problems come in two flavours; one, not all property types can be extended to implement
 * UIResource (the most important being String) and two, when the ui delegate manages child components and wants to pass
 * on properties (like foreground colour) to these children they need to be demoted to non-UIResource instances so that
 * they are not overridden when the look and feel changes. This can sometimes be impossible, take the special cases for
 * ImageIcon and disabled JLabels and JButtons where the type of the property matters.
 *
 * <p> JXComponent fixes these problems by providing complete UI property tagging for all types. The process of defining
 * a property as set by the UI delegate follows a similar pattern to the primitive property support in JComponent where
 * there is a package level relationship between {@link LookAndFeelUtilities#installProperty installProperty} and {@link
 * JXComponent#setLaFUIProperty setLaFProperty}. The difference is in the implementation; the JXComponent method
 * provides a number of extension hooks for sub-classes to extend the number of properties that can be configured by the
 * UI delegate. The {@link #setUIPropertyImpl} method has details of how to extend these hooks but this is not
 * necessarily needed as support for all properties on a component is provided via the {@link
 * #setUIPropertyByReflection} method which automatically provides support for any JavaBean property. The actual
 * information as to which properties have been set by the UI delegate is provided by the {@link UIPropertySupport} class
 * which can also be used as an aid to provide this support to classes that don't extend JXComponent.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see JComponent
 * @see XComponentUI
 * @see Painter
 * @see org.jdesktop.swingx.plaf.LookAndFeelAddons
 * @see LookAndFeelUtilities
 * @see UIPropertySupport
 */
public abstract class JXComponent extends JComponent {

    private static final String uiClassID = "XComponentUI";

    private UIPropertySupport uiPropertySupport;

    private Painter<?> backgroundPainter;
    private Painter<?> foregroundPainter;
    private boolean borderBackgroundPainted = true;

    public JXComponent() {
        super();
    }





    /**
     * Called by LookAndFeelUtils to set a ui property.
     *
     * @param propertyName The property name
     * @param value The value to set the given property to.
     */
    void setLaFUIProperty(String propertyName, Object value) {
        //noinspection StringEquality
        if (propertyName == "opaque" || // use == instead of .equals as they should be interned
            propertyName == "autoscrolls" ||
            propertyName == "focusTraversalKeysForward" ||
            propertyName == "focusTraversalKeysBackward") { // default behaviour
            if (value != null) {
                LookAndFeel.installProperty(this, propertyName, value);
            }
        } else {
            if (!setUIPropertyImpl(propertyName, value) && !setUIPropertyByReflection(propertyName, value)) {
                throw new IllegalArgumentException("property \"" + propertyName + "\" cannot be set using this method");
            }
        }
    }





    /**
     * Returns true if the given property contains a value set by the UI
     * delegate.
     *
     * @param propertyName The property name
     * @return {@code true} if the given property has not been set by the user.
     */
    boolean isLaFUIProperty(String propertyName) {
        boolean result = false;

        //noinspection StringEquality
        if (propertyName == "opaque" ||
            propertyName == "autoscrolls" ||
            propertyName == "focusTraversalKeysForward" ||
            propertyName == "focusTraversalKeysBackward") {
            // we have no way of knowing this other than to try setting the value
            // through the ui and seeing if it changes. That isn't a good idea ;)

            // instead we simply return true and assume that the setting of the
            // property by the ui will do the required checking for us.

            result = true;
        }

        return result || isUIProperty(propertyName);
    }





    /**
     * Return true if the given property is the value set by the UI.
     *
     * @param propertyName The property name.
     * @return {@code true} if the given property has not been set by the user.
     */
    protected boolean isUIProperty(String propertyName) {
        return uiPropertySupport == null || uiPropertySupport.isUIProperty(propertyName);
    }





    /**
     * Call this method when setting a property with a primitive value via a ui
     * delegate.
     *
     * <p>Note: This delegates to a UIPropertySupport object which is created
     * lazily.
     *
     * @param propertyName The name of the property
     * @param aValue {@code true} to specify that the given property is set by
     *   the UI.
     */
    protected void setUIProperty(String propertyName, boolean aValue) {
        if (!aValue) {
            if (uiPropertySupport == null) {
                uiPropertySupport = new UIPropertySupport(this);
            }
            uiPropertySupport.setUIProperty(propertyName, aValue);
        } else {
            if (uiPropertySupport != null) {
                uiPropertySupport.setUIProperty(propertyName, aValue);
            }
        }
    }





    /**
     * Override this method to add custom properties to this class. The format of
     * the overridden method usually follows the following:
     *
     * <pre><code>
     *   {@code @Override}
     *   protected boolean setUIProperty(String propertyName, Object value) {
     *      boolean result = false;
     *      if (propertyName == "integerProperty") {
     *         result = true;
     *         if (isUIProperty(propertyName)) {
     *            setIntegerProperty((Integer) value);
     *            setUIProperty(propertyName, true);
     *         }
     *      } else if (propertyName == "iconProperty") {
     *         result = true;
     *         if (isUIProperty(propertyName)) {
     *            setIconProperty((Icon) value);
     *            setUIProperty(propertyName, true);
     *         }
     *      } else {
     *         result = super.setUIProperty(propertyName, value);
     *      }
     *      return result;
     *   }
     * </code></pre>.
     *
     * <p>By default overriding this method will only add a performance improvement over not implementing it as the
     * property values are looked up reflectively by the calling method.</p>
     *
     * @param propertyName The property name. This is interned
     * @param value The value to set the property to.
     * @return {@code true} if the property could have been set through this
     *   method. This does not necessarily mean that the property was set.
     * @see #setLaFUIProperty
     */
    @SuppressWarnings({"StringEquality"}) // we are using interned strings here.
    protected boolean setUIPropertyImpl(String propertyName, Object value) {
        boolean result = false;
        if (propertyName == "backgroundPainter") {
            result = true;
            if (isUIProperty(propertyName)) {
                setBackgroundPainter((Painter) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "foregroundPainter") {
            result = true;
            if (isUIProperty(propertyName)) {
                setForegroundPainter((Painter) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "layout") {
            result = true;
            if (isUIProperty(propertyName)) {
                setLayout((LayoutManager) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "border") {
            result = true;
            if (isUIProperty(propertyName)) {
                setBorder((Border) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "foreground") {
            result = true;
            if (isUIProperty(propertyName)) {
                setForeground((Color) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "background") {
            result = true;
            if (isUIProperty(propertyName)) {
                setBackground((Color) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "font") {
            result = true;
            if (isUIProperty(propertyName)) {
                setFont((Font) value);
                setUIProperty(propertyName, true);
            }
        } else if (propertyName == "enabled") {
            result = true;
            if (isUIProperty(propertyName)) {
                setEnabled((Boolean) value);
                setUIProperty(propertyName, true);
            }
        }

        return result;
    }





    /**
     * Attempts to set the given property using reflection. This is performed via the Introspector and BenaInfo classes
     * and is called after the sub-class extended {@link #setUIProperty(String,Object)} method.
     *
     * @param propertyName The property name.
     * @param value The value to set the property to.
     * @return {@code true} if the property could have been set.
     */
    protected boolean setUIPropertyByReflection(String propertyName, Object value) {
        boolean result = false;
        try {
            BeanInfo info = Introspector.getBeanInfo(getClass());
            PropertyDescriptor[] ps = info.getPropertyDescriptors();
            for (PropertyDescriptor p : ps) {
                if (p.getName().equals(propertyName)) {
                    Method set = p.getWriteMethod();
                    if (set != null) {
                        result = true;
                        if (isUIProperty(propertyName)) {
                            set.invoke(this, value);
                            setUIProperty(propertyName, true);
                        }
                    }
                    break;
                }
            }
        } catch (IntrospectionException ex) {
            // default result
        } catch (InvocationTargetException ex) {
            // default result
        } catch (IllegalArgumentException ex) {
            // default result
        } catch (IllegalAccessException ex) {
            // default result
        }

        return result;
    }





    /**
     * @return {@code "XComponentUI"}.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }





    /**
     * Get the UI delegate for this class.
     *
     * @return The ui delegate.
     * @see #updateUI()
     */
    public XComponentUI getUI() { // should really be XComponentUI<? super This>
        return (XComponentUI) ui;
    }





    /**
     * First tries to find a UIManager managed UI delegate then if that fails calls createUI to create a fallback UI.
     */
    @Override
    public void updateUI() {
        Object o = UIManager.get(getUIClassID());
        if (o == null) {
            setUI(createUI());
        } else {
            setUI(UIManager.getUI(this));
        }
    }





    /**
     * Override this method if you don't want to implement the whole ComponentAddon API. Example implementation would be
     * for MyComponent.createUI() you would return BasicMyComponentUI.createUI(this). By default this returns a UI
     * delegate which supports the Painter additions added in this component.
     *
     * @return The fallback UI for this component.
     */
    protected ComponentUI createUI() {
        return XComponentUI.createUI(this);
    }





    /**
     * Set the background painter for this component.
     *
     * @param backgroundPainter The background painter.
     */
    public void setBackgroundPainter(Painter<?> backgroundPainter) {
        Painter old = getBackgroundPainter();
        this.backgroundPainter = backgroundPainter;
        firePropertyChange("backgroundPainter", old, getBackgroundPainter());
        repaint();
    }





    /**
     * Set whether the background painter will be inset by the borders insets before painting. If true then the
     * background painter will be offset by the borders top and left inset values and will fill the space excluding the
     * borders right and bottom insets.
     *
     * @param borderBackgroundPainted {@code false} if the background painter should be inset from the borders insets.
     * @see #isBorderBackgroundPainted()
     */
    public void setBorderBackgroundPainted(boolean borderBackgroundPainted) {
        boolean old = isBorderBackgroundPainted();
        this.borderBackgroundPainted = borderBackgroundPainted;
        firePropertyChange("borderBackgroundPainted", old, isBorderBackgroundPainted());
        if (getBackgroundPainter() != null) {
            repaint();
        }
    }





    /**
     * Set the painter to use to paint this components foreground.
     *
     * @param foregroundPainter The foreground painter.
     * @see #getForegroundPainter()
     */
    public void setForegroundPainter(Painter<?> foregroundPainter) {
        Painter<?> old = getForegroundPainter();
        this.foregroundPainter = foregroundPainter;
        firePropertyChange("foregroundPainter", old, getForegroundPainter());
        repaint();
    }





    /**
     * Get the background painter to this component. The background painter generally does not add information to the
     * component (i.e. the text in a label) but can add style to the components look.
     *
     * @return The background painter for this component.
     * @see #setBackgroundPainter
     */
    @SuppressWarnings("unchecked")
    public Painter<? super JXComponent> getBackgroundPainter() {
        return (Painter<? super JXComponent>) backgroundPainter;
    }





    /**
     * Returns whether the background painter for this component should paint behind the borders insets.
     *
     * @return {@code true} if the background painter paints behind the border insets.
     * @see #setBorderBackgroundPainted
     */
    public boolean isBorderBackgroundPainted() {
        return borderBackgroundPainted;
    }





    /**
     * Get the foreground painter for this component. The foreground painter is generally responsible for painting this
     * components state (i.e. the text in a label). You can add effects to the foreground painter in the usual way but
     * care should be taken to ensure that you call setForegroundPainter if you don't want the look and feel to override
     * your painter on lnf change.
     *
     * @return The foreground painter for this component.
     */
    @SuppressWarnings("unchecked")
    public Painter<? super JXComponent> getForegroundPainter() {
        return (Painter<? super JXComponent>) foregroundPainter;
    }





    /**
     * Support for reporting bound property changes for Object properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName the property whose value has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    @Override
    protected void firePropertyChange(String propertyName,
                                      Object oldValue, Object newValue) {
        setUIProperty(propertyName, false);

        super.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * Support for reporting bound property changes for boolean properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName the property whose value has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    @Override
    public void firePropertyChange(String propertyName,
                                   boolean oldValue, boolean newValue) {
        setUIProperty(propertyName, false);

        super.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * Support for reporting bound property changes for integer properties.
     * This method can be called when a bound property has changed and it will
     * send the appropriate PropertyChangeEvent to any registered
     * PropertyChangeListeners.
     *
     * @param propertyName the property whose value has changed
     * @param oldValue the property's previous value
     * @param newValue the property's new value
     */
    @Override
    public void firePropertyChange(String propertyName,
                                   int oldValue, int newValue) {
        setUIProperty(propertyName, false);

        super.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * Sets the layout manager for this container. This has been overriden
     * as the default setLayout does not fire a property change which is
     * where our UIResource management hooks are placed.
     *
     * @param mgr the specified layout manager
     * @see #doLayout
     * @see #getLayout
     */
    @Override
    public void setLayout(LayoutManager mgr) {
        setUIProperty("layout", false);
        super.setLayout(mgr);
    }
}
