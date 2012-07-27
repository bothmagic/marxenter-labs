/*
 * $Id: JNComponent.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.jdesktop.jdnc.incubator.rbair.swing.MouseMessagingHandler;
import org.jdesktop.jdnc.incubator.rbair.swing.actions.Targetable;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageSource;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageSourceSupport;

/**
 * Base class for all JN components.
 *
 * @author Ramesh Gupta
 * @author Amy Fowler
 * @author Mark Davidson
 *
 * @javabean.class
 *    displayName="Base JDNC Component"
 *    name="JNComponent"
 *    shortDesctiption="The base component"
 */
public abstract class JNComponent extends JPanel
    implements Targetable, MessageSource {

    private JComponent component;
    private JPopupMenu popup;

    private PopupHandler popupHandler;
    private MouseMessagingHandler messageHandler;

    /**
     * A reference to the MessageSourceSupport. Use the fire methods on
     * this class to send messages to the status bar.
     */
    protected MessageSourceSupport support;

    /**
     * Creates a new JNComponent configured with an instance of
     * {@link java.awt.BorderLayout}.
     */
    protected JNComponent() {
        super(new BorderLayout());
    }

    /**
     * Set the component that this JNComponent wraps.
     *
     * @javabean.property 
     *     shortDescription="Set the component that this JNComponent wraps"
     *     bound="true"
     */
    protected void setComponent(JComponent component) {
        JComponent oldComponent = this.component;
        this.component = component;
        component.setFont(getFont());
        component.setForeground(getForeground());
        component.setBackground(getBackground());

        // register/unregister
        if (popupHandler != null) {
            if (oldComponent != null) {
                oldComponent.removeMouseListener(popupHandler);
            }
            if (component != null) {
                component.addMouseListener(popupHandler);
            }
        }
        firePropertyChange("component", oldComponent, component);
    }

    public JComponent getComponent() {
        return component;
    }

    //
    // Targetable implementation
    //

    public boolean doCommand(Object command, Object value) {
        // Look at the internal component first.
        ActionMap map = component.getActionMap();
        Action action = map.get(command);

        if (action == null) {
            // look in the action map of the JNComponent
            map = getActionMap();
            action = map.get(command);
        }

        if (action != null) {
            if (value instanceof ActionEvent) {
                action.actionPerformed( (ActionEvent) value);
            }
            else {
                // XXX should the value represent the event source?
                action.actionPerformed(new ActionEvent(value, 0,
                    command.toString()));
            }
            return true;
        }
        return false;
    }

    public Object[] getCommands() {
        ActionMap map = component.getActionMap();
        return map.keys();
        // TODO: Should Return the keys the current JNComponent as well.
    }

    public boolean hasCommand(Object command) {
        Object[] commands = getCommands();
        for (int i = 0; i < commands.length; i++) {
            if (commands[i].equals(command)) {
                return true;
            }
        }
        return false;
    }

    //
    // MessageSource implementation
    //

    public void addMessageListener(MessageListener l) {
        if (support == null) {
            support = new MessageSourceSupport(this);
        }
        if (messageHandler == null) {
            messageHandler = new MouseMessagingHandler(this, support);
            if (toolBar != null) {
                // XXX This may go away when if we dont support component
                // level toolbars.
                messageHandler.registerListeners(toolBar.getComponents());
            }
            if (popup != null) {
                messageHandler.registerListeners(popup.getSubElements());
            }
        }
        support.addMessageListener(l);
    }

    public void removeMessageListener(MessageListener l) {
        if (support == null) {
            return;
        }
        support.removeMessageListener(l);
    }

    public MessageListener[] getMessageListeners() {
        if (support == null) {
            support = new MessageSourceSupport(this);
        }
        return support.getMessageListeners();
    }

    /**
     * Convenience method to send a transient message to the MessageListeners
     *
     * @param message text of message to send
     */
    protected void sendMessage(String message) {
        if (support != null) {
            support.fireMessage(message);
        }
    }

    /**
     * Adds the specified action to the end of the tool bar for this component.
     * If no tool bar exists, a new tool bar is automatically created and added
     * to the top of this component.
     *
     * @param action the action added to the tool bar
     * @return the buton that must be clicked to perform the specified action
     */
    public JButton addAction(Action action) {
        if (toolBar == null) {
            addToolBar();
        }

        Object delegate = action.getValue("delegate");
        if (delegate == null) {
            action.putValue("delegate", this);
        }

        /*
             Since JDK 1.3, this is how we should be adding an Action to a JToolBar:
                 JButton button = new JButton(action);
                 toolBar.add(button);	// shows both icon and title
         */
        JButton button = toolBar.add(action); // NOT the same as commented code;
        // shows only icon; no title

        button.setToolTipText( (String) action.getValue(Action.
            SHORT_DESCRIPTION));
        return button;
    }

    /**
     * Adds a separator to the end of the tool bar, if any, for this component.
     * This method does nothing if there is no tool bar for this component.
     */
    public void addSeparator() {
        if (toolBar != null) {
            toolBar.addSeparator();
        }
    }

    public void addToolBarComponent(JComponent component) {
        if (toolBar == null) {
            addToolBar();
        }
        toolBar.add(component);
    }

    public void setFont(Font font) {
        super.setFont(font);
        if (component != null) {
            component.setFont(font);
        }
    }

    /**
     * Sets the popup menu for this component.
     * TODO: if this is not the first time this method is called,
     * should unregister the previous popup listener.
     */
    public void setPopupMenu(JPopupMenu popup) {
        JPopupMenu oldPopup = this.popup;
        this.popup = popup;

        if (oldPopup != null) {
            // Unregister the old popup
            if (messageHandler != null) {
                messageHandler.unregisterListeners(oldPopup.getSubElements());
            }
            if (getComponent() != null) {
                getComponent().removeMouseListener(popupHandler);
                popupHandler = null;
            }
        }

        if (popup != null) {
            // Register the new popup
            if (messageHandler != null) {
                messageHandler.registerListeners(popup.getSubElements());
            }
            if (getComponent() != null) {
                popupHandler = new PopupHandler(popup);
                getComponent().addMouseListener(popupHandler);
            }
        }
    }

    public JPopupMenu getPopupMenu() {
        return popup;
    }

    /**
     * Handles the popup mouse gestures on the component.
     */
    private class PopupHandler extends MouseAdapter {

        private JPopupMenu popup;

        public PopupHandler(JPopupMenu popup) {
            setPopup(popup);
        }

        public void setPopup(JPopupMenu popup) {
            this.popup = popup;
        }

        public void mousePressed(MouseEvent e) {
            showPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            showPopup(e);
        }

        private void showPopup(MouseEvent e) {
            if (e.isPopupTrigger() && popup != null) {
                popup.show( (Component) e.getSource(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Returns the background image for the component.
     *
     * @return the background image for the component.
     */
    public Icon getBackgroundImage() {
        return image;
    }

    /**
     * Sets the background image for a transparent or translucent component.
     *
     * @param image specifies the background image for the component
     *
     * @javabean.property 
     *     shortDescription="Set the background image for the component"
     *
     */
    public void setBackgroundImage(Icon image) {
        if (this.image != image) {
            this.image = image;
            repaint();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            Dimension size = this.getSize();
            int x, y;
            x = (size.width - image.getIconWidth()) >> 1;
            y = (size.height - image.getIconHeight()) >> 1;
            image.paintIcon(this, g, x, y);
        }
    }

    protected JToolBar addToolBar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
        return toolBar;
    }

    protected JToolBar toolBar = null;
    protected Icon image = null;
}
