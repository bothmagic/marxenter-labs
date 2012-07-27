/*
 * $Id: XComponentUI.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;

import java.util.EventListener;
import java.util.ResourceBundle;

import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;
import java.awt.event.*;

/**
 * Extension to the ComponentUI to add Painter support and provide simple utility methods to create UI delegates.
 * <p>
 * Hooks are provided to allow a subclass to quickly extend the functionality they need for their custom component.
 * These hooks follow the standard swing layout calling the following in order:
 *
 * <dl>
 * <dt>{@link #installDefaults}</dt>
 * <dd>Install any ui defaults on the component. Use {@link LookAndFeelUtilities#installProperty} to allow the user to
 * override you choice of default value. Use one of the getUIProperty methods when you are unsure if the required ui
 * property will be present in the UIManager/UIDefaults classes.</dd>
 * <dt>{@link #installLayout}</dt>
 * <dd>While this is not present in most swing ui delegates this will be called before components are created and will
 * by default call createLayoutManager and install even non-null values as the component layout</dd>
 * <dt>{@link #installComponents}</dt>
 * <dd>Any child components should be installed here. The recommended practice is to call addUIComponent giving a
 * descriptive and unique (for this instance) name for the component. This name will then be used as the constraints
 * for the layout manager and as the id for future lookup (and removal) of the component.</dd>
 * <dt>{@link #installKeyBindings}</dt>
 * <dd>Any key bindings needed for your component should be installed here.</dd>
 * <dt>{@link #installListeners}</dt>
 * <dd>Any listeners you need to install can be installed here. Note that default PropertyChangeListener support can be
 * provided by either overriding {@link #installDefaultPropertySupport} of by passing true to the constructor. This will
 * then install a PropertyChangeListener on the component and will notify the {@link #propertyChange} method when an
 * event is triggered. It is recommended that all listeners added to the component are annotated with the
 * {@code @UIListener} annotation, that way they can automatically be retrieved via getUIListener and can be removed on
 * uninstall of the ui delegate.</dd>
 * </dl>
 *
 * If you decide to override any of the above methods then you should also call super.installXXX at the head of your
 * method to ensure that any managed functionality it taken care of.
 * <p>
 * The majority of uninstallation tasks are handled for you so long as the above practices are followed. If your
 * components were installed via addUIComponent then they will be removed, likewise if your listeners have the
 * {@code @UIListener} annotation. Any user defined properties will need to be uninstalled manually by overriding the
 * uninstallDefaults method and calling LookAndFeelUtilities.installProperty with a null value for each. The default
 * implementation of uninstallDefaults will remove the background/foregroundPainter, border and font properties of the
 * component only. Typically you should call the superclass's uninstallXXX method after you have uninstalled your own
 * custom settings.
 * <p>
 * If the above practices are followed the vast majority of ui delegates can follow the singleton approach which is
 * recommended and should reduce object construction.
 *
 * @param <C> The type of component that this ui delegate represents.
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see JXComponent
 * @see UIListener
 */
public abstract class XComponentUI<C extends JXComponent> extends ComponentUI {
    /**
     * Defines the key applied to a child component to show that it was added by the UI delegate.
     */
    private static final Object UI_COMPONENT_CHILD_KEY = new Object();

    private static XComponentUI SINGLETON = null;

    public static ComponentUI createUI(JComponent c) {
        assert SwingUtilities.isEventDispatchThread(): "Cannot create UI off of EDT";
        if (SINGLETON == null) {
            SINGLETON = new XComponentUI("XComponentUI") {};
        }
        return SINGLETON;
    }





    private final String propertyString;
    private boolean installDefaultPropertySupport;
    private boolean installDefaultFocusSupport;

    /**
     * Create a new XComponentUI. The given propertyString is the string that is generally prepended to UIManager
     * resource lookups. For example for a UI delegate for a component named JXCustomComp the property string may be
     * {@code "CustomComp"} and as such the foreground property may be looked up using the {@code
     * "CustomComp.foreground"} key in the UIManager defaults.
     *
     * @param propertyString The prefix for property lookup.
     */
    protected XComponentUI(String propertyString) {
        this.propertyString = propertyString;
        this.installDefaultPropertySupport = false;
        installDefaultFocusSupport = false;
    }





    /**
     * Gets a resource bundle in the sub-classes package with the name Resources.
     *
     * @param c The source component.
     * @return The resource bundle for this ui delegate.
     */
    protected ResourceBundle getResourceBundle(C c) {
        return ResourceBundle.getBundle(getClass().getPackage().getName() + ".Resources");
    }





    /**
     * Get the string to use as the prefix for property lookup. E.G. "MyComponent" will be used to create
     * "MyComponent.background".
     *
     * @return The property string prefix.
     */
    protected String getUIPropertyString() {
        return propertyString;
    }





    /**
     * Adds the given component to the parent component using the given key for both identification and as the
     * constraint to the components layout.
     *
     * <p>This method will do one of two things in an attempt to keep track of components added via the UI delegate. If
     * the given Component is an instance of JComponent then a clientProperty with the key UI_COMPONENT_CHILD_KEY and a
     * value of {@code key} will get added to the component and the name will be set to {@code key}. If the given
     * Component is not a JComponent then the name of the given component will be set to {@code '$' +
     * getUIPropertyString() + '.' + key}.
     *
     * @param p The parent component.
     * @param c The component to add.
     * @param key The key to use for identification and the constrain to use when adding to the layout.
     */
    protected void addUIComponent(C p, Component c, String key) {
        if (c instanceof JComponent) {
            c.setName(key);
            ((JComponent) c).putClientProperty(UI_COMPONENT_CHILD_KEY, key);
        } else {
            c.setName('$' + getUIPropertyString() + '.' + key);
        }
        p.add(c, key);
    }





    /**
     * Removes the component with the given id from the parent component.
     *
     * @param p The parent component.
     * @param key The key for the component to remove
     * @return The component that was removed. This may return null if nothing
     *   was removed.
     */
    protected Component removeUIComponent(C p, String key) {
        Component result = null;
        int index = 0;
        String compKey = null;
        for (Component c : p.getComponents()) {
            if (c instanceof JComponent) {
                if (key.equals(((JComponent) c).getClientProperty(UI_COMPONENT_CHILD_KEY))) {
                    ((JComponent) c).putClientProperty(UI_COMPONENT_CHILD_KEY, null);
                    p.remove(index);
                    result = c;
                    break;
                }
            } else {
                if (compKey == null) {
                    compKey = '$' + getUIPropertyString() + '.' + key;
                }
                if (compKey.equals(c.getName())) {
                    p.remove(index);
                    result = c;
                    break;
                }
            }
            index++;
        }
        return result;
    }





    /**
     * Gets the UI child component which was added using the given key.
     *
     * @param p The parent component.
     * @param key The key for the child component.
     * @return The component requested or null if it doesn't exist.
     */
    protected Component getUIComponent(C p, String key) {
        Component result = null;
        String compKey = null;
        for (Component c : p.getComponents()) {
            if (c instanceof JComponent) {
                if (key.equals(((JComponent) c).getClientProperty(UI_COMPONENT_CHILD_KEY))) {
                    result = c;
                    break;
                }
            } else {
                if (compKey == null) {
                    compKey = '$' + getUIPropertyString() + '.' + key;
                }
                if (compKey.equals(c.getName())) {
                    result = c;
                    break;
                }
            }
        }
        return result;
    }





    /**
     * Removes all components added via addUIComponent from the given component.
     *
     * @param p The component to remove all UI components from.
     */
    protected void removeAllUIComponents(C p) {
        Component[] comps = p.getComponents();
        String pre = null;
        for (int i = comps.length - 1; i >= 0; i--) {
            Component c = comps[i];
            if (c instanceof JComponent) {
                if (((JComponent) c).getClientProperty(UI_COMPONENT_CHILD_KEY) != null) {
                    ((JComponent) c).putClientProperty(UI_COMPONENT_CHILD_KEY, null);
                    p.remove(i);
                }
            } else {
                if (pre == null) {
                    pre = '$' + getUIPropertyString() + '.';
                }
                if (c.getName() != null && c.getName().startsWith(pre)) {
                    p.remove(i);
                }
            }

        }
    }





    /**
     * Gets the client property for the given key. THis method will first check the client properties of the given
     * component then check the UIManager prepending the uiPropertyString to create the key. For example if the property
     * {@code "background"} is given then first the given components client property {@code "background"} is checked, if
     * not found then the UIManagers get method will be called with the key {@code getUIPropertyString() + "." +
     * "background"}.
     *
     * <p>This method should not be used directly, instead use one of {@link #getUIProperty(JXComponent,String)} or
     * {@link #getUIProperty(JXComponent,String,Object)}.
     *
     * @param c The container of the property.
     * @param property The key for the property.
     * @return The value of the property, this can be null.
     */
    @SuppressWarnings("unchecked")
    protected <T> T getUIPropertyImpl(C c, String property) {
        T result = (T) c.getClientProperty(property);
        if (result == null) {
            result = (T) UIManager.get(getUIPropertyString() + '.' + property);
        }
        return result;
    }





    /**
     * Gets a UI property for the given component. This may return null if there is no ui property. This calls the
     * protected getUIPropertyImpl method of this class.
     *
     * @param c The component to find the property for.
     * @param property The property name to find.
     * @return The property requested or null.
     * @see #getUIPropertyImpl
     */
    @SuppressWarnings("unchecked")
    public static <T, C extends JXComponent> T getUIProperty(C c, String property) {
        XComponentUI<C> ui = c.getUI();
        //noinspection RedundantTypeArguments
        return ui.<T> getUIPropertyImpl(c, property);
    }





    /**
     * Gets a ui property returning the backup value if it cannot be found.
     *
     * @param c The component.
     * @param property The property.
     * @param backup The result if not found.
     * @return The result.
     */
    public static <T, C extends JXComponent> T getUIProperty(C c, String property, T backup) {
        //noinspection RedundantTypeArguments
        T result = XComponentUI.<T, C> getUIProperty(c, property);
        if (result == null) {
            result = backup;
        }
        return result;
    }





    /**
     * Set a ui property on the given object. This simply sets a client property on the component after prepending the
     * ui property string to the key.
     *
     * @param c The target for the property.
     * @param key The key for the property.
     * @param value The value for the property.
     */
    protected void addUIProperty(C c, String key, Object value) {
        c.putClientProperty(key, value);
    }





    /**
     * Called to install the layout for the source component. This calls createLayoutManager, the result of which is
     * installed on the given component.
     *
     * @param c The source component.
     */
    protected void installLayout(C c) {
        LayoutManager lm = createLayoutManager(c);
        LookAndFeelUtilities.installProperty(c, "layout", lm);
    }





    /**
     * Uninstalls the ui added layout manager.
     *
     * @param c The component to uninstall the layout from.
     */
    protected void uninstallLayout(C c) {
        LookAndFeelUtilities.installProperty(c, "layout", null); // will only clear if set by the UI
    }





    /**
     * Called to create the layout for this ui. This may return null.
     *
     * @param c The component the layout will be added onto.
     * @return The layout manager to add.
     */
    protected LayoutManager createLayoutManager(C c) {
        return null;
    }





    /**
     * Creates the default foreground painter for the given component. The returned foreground painter will delegate the
     * painting to this UI delegates paint method.
     *
     * @param c The component to paint.
     * @return A painter which calls the ui delegates paint method.
     */
    @SuppressWarnings("unchecked")
    protected Painter<? super C> createForegroundPainter(C c) {
        class P extends AbstractPainter<C> implements UIResource {
            @Override
            protected void doPaint(Graphics2D g, C object, int width, int height) {
                XComponentUI ui = object.getUI();
                ui.paint(g, object);
            }
        }
        return new P();
    }





    /**
     * Creates the default background painter for the given component. The returned background painter will delegate the
     * painting to this UI delegates paintBackground method.
     *
     * @param c The component to paint.
     * @return A painter which calls the ui delegates paintBackground method.
     */
    @SuppressWarnings("unchecked")
    protected Painter<? super C> createBackgroundPainter(C c) {
        class P extends AbstractPainter<C> implements UIResource {
            @Override
            protected void doPaint(Graphics2D g, C object, int width, int height) {
                XComponentUI ui = object.getUI();
                ui.paintBackground(g, object);
            }
        }
        return new P();
    }





    /**
     * Configures the specified component appropriate for the look and feel.
     *
     * @param c the component where this UI delegate is being installed
     */
    @Override
    @SuppressWarnings("unchecked")
    public void installUI(JComponent c) {
        C component = (C) c;

        installDefaults(component);
        installLayout(component);
        installComponents(component);
        installKeyBindings(component);
        installListeners(component);
    }





    /**
     * Installs the common defaults for the source component. This installs the background, foreground, font, border,
     * backgroundPainter and foregroundPainter properties. Default background, foreground and font properties will be
     * provided if no ui specified values can be found.
     *
     * @param component The component to install the defaults on.
     */
    protected void installDefaults(C component) {
        LookAndFeelUtilities.installDefaultColorsAndFonts(component, getUIPropertyString(), "Panel");
        LookAndFeelUtilities.installDefaultProperty(component, "border", getUIPropertyString(), (Border)null);
        LookAndFeelUtilities.installDefaultProperty(component, "backgroundPainter", getUIPropertyString(), createBackgroundPainter(component));
        LookAndFeelUtilities.installDefaultProperty(component, "foregroundPainter", getUIPropertyString(), createForegroundPainter(component));
        LookAndFeelUtilities.installDefaultProperty(component, "opaque", getUIPropertyString(), Boolean.FALSE);
        LookAndFeelUtilities.installDefaultProperty(component, "borderBackgroundPainted", getUIPropertyString(), Boolean.FALSE);
    }





    /**
     * Install any child components in this method.
     *
     * <p>Example Implementation:</p>
     * <p><pre><code>
     * {@code @Override}
     * protected static final String TITLE_COMPONENT = "title";
     * protected void installComponent(MyComp c) {
     *    addUIComponent(c, createTitleComponent(c), TITLE_COMPONENT);
     * }
     *
     * protected JComponent createTitleComponent(MyComp c) {
     *    return new JLabel();
     * }
     * </code></pre></p>
     *
     * @param component The container to add the children to.
     */
    protected void installComponents(C component) {}





    /**
     * Add any key bindings to the component.
     *
     * @param component The component to add key bindings to.
     */
    protected void installKeyBindings(C component) {}





    /**
     * Add listeners to the source component. This method will optionally add the default PropertyChange support
     * depending on the return value of {@link #shouldInstallDefaultPropertySupport} and the default focus support
     * depending on the value of {@link #shouldInstallDefaultFocusSupport}.
     *
     * @param component The component to add the listeners to.
     */
    protected void installListeners(C component) {
        if (shouldInstallDefaultPropertySupport(component)) {
            installDefaultPropertySupport(component);
        }
        if (shouldInstallDefaultFocusSupport(component)) {
            installDefaultFocusSupport(component);
        }
    }





    /**
     * Searches the given listener array for a listener that was added by the ui. This looks for the UIListener
     * annotation.
     *
     * @param component The component the listeners were added to.
     * @param listeners The listeners to check.
     * @return The listener found or null.
     */
    protected <T extends EventListener> T getUIListenerImpl(C component, T[] listeners) {
        for (T listener : listeners) {
            if (listener.getClass().isAnnotationPresent(UIListener.class)) {
                return listener;
            }
        }
        return null;
    }





    /**
     * Searches the given listener array for a listener that was added by the ui. This looks for the UIListener
     * annotation.
     *
     * @param component The component the listeners were added to.
     * @param listeners The listeners to check.
     * @return The listener found or null.
     */
    @SuppressWarnings("unchecked")
    public static <T extends EventListener> T getUIListener(JXComponent component, T[] listeners) {
        XComponentUI<JXComponent> ui = component.getUI();
        return ui.getUIListenerImpl(component, listeners);
    }





    /**
     * Reverses configuration which was done on the specified component during <code>installUI</code>.
     *
     * @param c the component from which this UI delegate is being removed; this argument is often ignored, but
     *   might be used if the UI object is stateless and shared by multiple components
     */
    @Override
    @SuppressWarnings("unchecked")
    public void uninstallUI(JComponent c) {
        C component = (C) c;

        uninstallListeners(component);
        uninstallKeyBindings(component);
        uninstallComponents(component);
        uninstallLayout(component);
        uninstallDefaults(component);
    }





    /**
     * Uninstalls the default properties for this component. This uninstalls the border, font, backgroundPainter and
     * foregroundPainter properties.
     *
     * @param component The component to uninstall the defaults from.
     */
    protected void uninstallDefaults(C component) {
        // don't do background/foreground colours
        LookAndFeelUtilities.installProperty(component, "border", null);
        LookAndFeelUtilities.installProperty(component, "font", null);
        LookAndFeelUtilities.installProperty(component, "backgroundPainter", null);
        LookAndFeelUtilities.installProperty(component, "foregroundPainter", null);
    }





    /**
     * Uninstall the components from the container. This will remove all components added via the addUIComponent method.
     *
     * @param component The container to remove children from.
     */
    protected void uninstallComponents(C component) {
        removeAllUIComponents(component);
    }





    /**
     * Uninstall any key bindings from the source component.
     *
     * @param component The component to remove key bindings from.
     */
    protected void uninstallKeyBindings(C component) {}





    /**
     * Uninstalls listeners from the source component. This will call {@link #uninstallListenersByReflection} which
     * removes all listeners that have the UIListener annotation and can be accessed via BeanInfo.
     *
     * @param component The component to remove listeners from.
     */
    protected void uninstallListeners(C component) {
        uninstallListenersByReflection(component);
    }





    /**
     * Uses the C's BeanInfo class to find all listeners that have the
     * UIListener annotation and removes them.
     *
     * @param e The component to remove listeners from.
     */
    protected void uninstallListenersByReflection(C e) {
        try {
            BeanInfo info = Introspector.getBeanInfo(e.getClass());
            EventSetDescriptor[] events = info.getEventSetDescriptors();
            for (EventSetDescriptor event : events) {
                Method getListeners = event.getGetListenerMethod();
                try {
                    EventListener[] listeners = (EventListener[]) getListeners.invoke(e);
                    for (EventListener listener : listeners) {
                        if (listener.getClass().isAnnotationPresent(UIListener.class)) {
                            Method removeListener = event.getRemoveListenerMethod();
                            if (removeListener != null) {
                                removeListener.invoke(e, listener);
                            }
                        }
                    }
                } catch (InvocationTargetException ex1) {
                    throw new RuntimeException("The following exception occured while trying to remove a listener", ex1);
                } catch (IllegalArgumentException ex1) {
                    throw (Error)new InternalError("Should be handled by the Introspector").initCause(ex1);
                } catch (IllegalAccessException ex1) {
                    throw (Error)new InternalError("Should be handled by the Introspector").initCause(ex1);
                }

            }
        } catch (IntrospectionException ex) {
            // we just return if this happens
        }
    }





    /**
     * Notifies this UI delegate that it's time to paint the specified component. This has been extended to add support
     * for painters.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted; this argument is often ignored, but might be used if the UI object is
     *   stateless and shared by multiple components
     */
    @Override
    public void update(Graphics g, JComponent c) {
        if (c instanceof JXComponent) {
            if (c.isOpaque()) {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            JXComponent comp = (JXComponent) c;
            if (!paintPainter(g, comp, comp.getBackgroundPainter(), comp.isBorderBackgroundPainted())) {
                paintBackground(g, c);
            }
            if (!paintPainter(g, comp, comp.getForegroundPainter(), false)) { // its up to the painter to include the insets
                paint(g, c); // only paint if the foreground painter hasn't painted for us.
            }
        } else {
            super.update(g, c);
        }
    }





    /**
     * Paint the component background. There is no guarantee that this method will ever be called. This is the default
     * hook for overriding the background painting of this component and is called by the default backgroundPainter and
     * also if the backgroundPainter is null. 
     *
     * @param g The graphics to paint to.
     * @param c The component to paint.
     */
    protected void paintBackground(Graphics g, JComponent c) {
        // left for sub classes to implement
    }





    /**
     * Utility method to paint the given painter onto the given Graphics.
     *
     * @param g the Graphics to paint to.
     * @param comp The component to paint.
     * @param painter The painter to do the painting.
     * @param includeInsets {@code true} if the Inset space should be painted.
     * @return {@code true} if the painter was painted.
     */
    protected <T extends JComponent> boolean paintPainter(Graphics g, T comp, Painter<? super T> painter, boolean includeInsets) {
        boolean result;
        if (result = (painter != null)) {
            if (includeInsets) {
                Insets i = comp.getInsets();
                g.translate(i.left, i.top);
                try {
                    painter.paint((Graphics2D) g, comp, comp.getWidth() - i.left - i.right, comp.getHeight() - i.top - i.bottom);
                } finally {
                    g.translate( -i.left, -i.top);
                }
            } else {
                painter.paint((Graphics2D) g, comp, comp.getWidth(), comp.getHeight());
            }
        }
        return result;
    }





    /**
     * Creates the default property change listener.
     *
     * @param c The source component.
     * @return The property change listener to add.
     */
    protected PropertyChangeListener createPropertyChangeListener(C c) {
        return new DefaultPropertyListener();
    }





    /**
     * Override this method to return true if you wish the default property support to be turned on. This will result in
     * a default PropertyChangeListener being added to the source controller and the {@link #propertyChange} method
     * being called on change. This defaults to {@code false}. For convenience you may pas the value this method returns
     * to the constructor.
     *
     * @param c The source component.
     * @return {@code true} to install default property support.
     */
    protected boolean shouldInstallDefaultPropertySupport(C c) {
        return installDefaultPropertySupport;
    }





    /**
     * Set whether the default property support should be installed on this component.
     *
     * @param b {@code true} if {@link #propertyChange} will be called on property change events.
     */
    protected void setInstallDefaultPropertySupport(boolean b) {
        installDefaultPropertySupport = b;
    }





    /**
     * Installs the default property support on the given component.
     *
     * @param c The component.
     */
    protected void installDefaultPropertySupport(C c) {
        c.addPropertyChangeListener(createPropertyChangeListener(c));
    }





    /**
     * Called each time a property changes on the source component. This will only be called if defaultPropertySupport
     * is enabled. By default this will call the separated version of this method.
     *
     * @param c The source component.
     * @param e The event fired.
     */
    protected void propertyChange(C c, PropertyChangeEvent e) {
        propertyChange(c, e.getPropertyName(), e.getOldValue(), e.getNewValue());
    }





    /**
     * Called by {@link #propertyChange(JXComponent, PropertyChangeEvent)} when a property has changed.
     *
     * @param c The source component.
     * @param property The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void propertyChange(C c, String property, Object oldValue, Object newValue) {
    }





    /**
     * Set whether the default focus support should be installed on the component. This will only affect installation if
     * called before the installUI method. The best place to set this property is in the constructor.
     *
     * @param b {@code true} if the component should request focus on mouse click.
     */
    protected void setInstallDefaultFocusSupport(boolean b) {
        this.installDefaultFocusSupport = b;
    }





    /**
     * Returns true if the default focus support should be installed.
     *
     * @param c The component.
     * @return {@code true} if the component should request focus on mouse click.
     */
    protected boolean shouldInstallDefaultFocusSupport(C c) {
        return installDefaultFocusSupport;
    }





    /**
     * Install default focus support on the given component. This will add a MouseListener to watch for mouse click
     * events requesting focus when these events are found. Note: this method should only be called once per installUI.
     *
     * @param c The component to add the support to.
     */
    protected void installDefaultFocusSupport(C c) {
        c.addMouseListener(new DefaultFocusListener());
    }





    /**
     * Request focus on the given event as a result of the given mouse event. This should call
     * {@link #requestFocus(JXComponent)} to perform the actual focus request after filtering any events that should not
     * trigger this action.
     *
     * @param e The mouse event triggering the focus.
     * @param c The component to request focus on.
     */
    protected void requestFocus(C c, MouseEvent e) {
        requestFocus(c);
    }





    /**
     * Requests focus on the given component.
     *
     * @param c The component to request focus on.
     */
    protected void requestFocus(C c) {
        c.requestFocusInWindow();
    }





    /**
     * Default PropertyChangeListener support which delegates the processing to the {@link XComponentUI#propertyChange}
     * method.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    @UIListener
    protected static class DefaultPropertyListener implements PropertyChangeListener {
        @SuppressWarnings("unchecked")
        public void propertyChange(PropertyChangeEvent e) {
            Object source = e.getSource();
            if (source instanceof JXComponent) {
                XComponentUI ui = ((JXComponent) source).getUI();
                assert ui != null;
                ui.propertyChange((JXComponent) source, e);
            }
        }
    }







    /**
     * Provides the default focus support for the component. This forwards mouseClicked events to the
     * {@link XComponentUI#requestFocus(JXComponent,MouseEvent)}.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    @UIListener
    protected static class DefaultFocusListener extends MouseAdapter {
        /**
         * Invoked when the mouse has been clicked on a component.
         *
         * @param e The event
         */
        @SuppressWarnings("unchecked")
        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (source instanceof JXComponent) {
                XComponentUI ui = ((JXComponent) source).getUI();
                assert ui != null;
                ui.requestFocus((JXComponent) source, e);
            }
        }
    }
}
