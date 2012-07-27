package org.jdesktop.incubator.util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility methods capable of navigating component hierarchy up to an AWTEvents owning Frame, Dialog or Window.
 * As described in Hans Mullers Dialog Diatribe:
 * http://weblogs.java.net/blog/hansmuller/archive/2006/10/dialog_diatribe.html
 */

//PENDING ..or suitable for inclusion into WindowUtils?

public class EventUtils {

    private EventUtils() {
    }

    /**
     * Finds parent frame for the given event. This method knows about the disjoint invoker
     * containment when events are fired from menus.
     *
     * @param event event whose source is our starting location and ancesor hierarchy we'll search
     * @return nearest owning parent frame for the component associated with the event, or null if none found
     */
    public static Frame frameForEvent(AWTEvent event) {
        return (Frame) ancestorForEvent(Frame.class, event);
    }

    /**
     * Finds parent dialog for the given event. This method knows about the disjoint invoker
     * containment when events are fired from menus.
     *
     * @param event event whose source is our starting location and ancesor hierarchy we'll search
     * @return nearest owning parent dialog for the component associated with the event, or null if none found
     */
    public static Dialog dialogForEvent(AWTEvent event) {
        return (Dialog) ancestorForEvent(Dialog.class, event);
    }

    /**
     * Finds parent window for the given event. This method knows about the disjoint invoker
     * containment when events are fired from menus.
     *
     * @param event event whose source is our starting location and ancesor hierarchy we'll search
     * @return nearest owning parent window, dialog or frame for the component associated with the event, or null if none found
     */
    public static Window windowForEvent(AWTEvent event) {
        return (Window) ancestorForEvent(Window.class, event);
    }

    /**
     * Finds first ancesor of the given class for the event. This method knows about the disjoint invoker
     * containment when events are fired from menus.
     *
     * @param target Class of the container we are looking for
     * @param event event whose source is our starting location and ancesor hierarchy we'll search
     * @return first container of the given type, or null if nothing found
     */
    public static Component ancestorForEvent(java.lang.Class<? extends Container> target, AWTEvent event) {
        if (event.getSource() instanceof Component) {
            Component source = (Component) event.getSource();
            while (source != null) {
                if (target.isInstance(source)) {
                    return source;
                }
                source = (source instanceof JPopupMenu) ? ((JPopupMenu) source).getInvoker() : source.getParent();
            }
        }
        return null;
    }
}