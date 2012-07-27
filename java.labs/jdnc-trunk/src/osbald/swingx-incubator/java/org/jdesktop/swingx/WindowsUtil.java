package org.jdesktop.swingx;

import javax.swing.*;
import java.awt.*;

/**
 * Utility methods via Hans Mullers 'Dialog Diatribe' ideally for inclusion into WindowUtils?
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 28-Nov-2006
 * Time: 11:45:48
 */

public class WindowsUtil {

    /**
     * Finds parent frame for the given event. This method knows about the disjuncnt invoker
     * containment when events are fired from menus.
     * see: http://weblogs.java.net/blog/hansmuller/archive/2006/10/dialog_diatribe.html
     *
     * @param event source event
     * @return nearest owning parent frame for the component associated with the event, or null if none found
     */
    public static Frame frameForEvent(AWTEvent event) {
        return (Frame) ancestorForEvent(Frame.class, event);
    }

    /**
     * Finds parent dialog for the given event. This method knows about the disjuncnt invoker
     * containment when events are fired from menus.
     * see: http://weblogs.java.net/blog/hansmuller/archive/2006/10/dialog_diatribe.html
     *
     * @param event source event
     * @return nearest owning parent dialog for the component associated with the event, or null if none found
     */
    public static Dialog dialogForEvent(AWTEvent event) {
        return (Dialog) ancestorForEvent(Dialog.class, event);
    }

    /**
     * Finds parent window for the given event. This method knows about the disjuncnt invoker
     * containment when events are fired from menus.
     * see: http://weblogs.java.net/blog/hansmuller/archive/2006/10/window_diatribe.html
     *
     * @param event source event
     * @return nearest owning parent window, dialog or frame for the component associated with the event, or null if none found
     */
    public static Window windowForEvent(AWTEvent event) {
        return (Window) ancestorForEvent(Window.class, event);
    }

    static Window ancestorForEvent(java.lang.Class<? extends Window> clazz, AWTEvent event) {
        if (event.getSource() instanceof Component) {
            Component c = (Component) event.getSource();
            while (c != null) {
                if (clazz.isInstance(c)) {
                    return (Window) c;
                }
                c = (c instanceof JPopupMenu) ? ((JPopupMenu) c).getInvoker() : c.getParent();
            }
        }
        return null;
    }

    /**
     * Find a contained component
     */
    public static Component getFirstDescendantOfClass(Class clazz, Component component) {
        if (component.getClass().equals(clazz)) {
            return component;
        }
        if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (int i = 0, n = components.length; i < n; i++) {
                Component child = getFirstDescendantOfClass(clazz, components[i]);
                if (child != null) return child;
            }
        }
        return null;
    }
}