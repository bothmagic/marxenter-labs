/*
 * $Id: JExploseUtils.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

import org.jdesktop.swingx.jexplose.util.JExploseKeyDispatcher;


/**
 * Helper class that give you preconfigured ways to trigger JExplose.<br/>
 * <code>JExploseUtils</code> provide preconfigured actions and convenient way to install shortcut.<br/>
 */
public class JExploseUtils {
    
    /**
     * Provide an Swing action that is configured to launch JExplose on the given  <code>JDesktopPane</code><br/>
     * Note: you can change text and ico associated with the action using <code>Action.putValue()</code> mecanism.
     * @param desktopPane    the <code>JDesktopPane</code> to play the effect on.
     * @return              the predefined Thunder action
     * @see Action
     */
    public static Action getThunderAction(final JDesktopPane desktopPane) {
        AbstractAction action = new AbstractAction("JExplose") {
            public void actionPerformed(ActionEvent e) {
                JExplose.getInstance().explose(desktopPane);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(JExploseUtils.class.getResource("icon.png")));
        return action;
    }
    
    /**
     * Provide an Swing action that is configured to launch JExplose on the given  <code>Explosable</code><br/>
     * Note: you can change text and ico associated with the action using <code>Action.putValue()</code> mecanism.
     * @param explosable    the <code>Explosable</code> to play the effect on.
     * @return              the predefined Lightning action
     * @see Action
     */
    public static Action getLightningAction(final Explosable explosable) {
        AbstractAction action = new AbstractAction("JExplose") {
            public void actionPerformed(ActionEvent e) {
                JExplose.getInstance().explose(explosable);
            }
        };
        action.putValue(Action.SMALL_ICON, new ImageIcon(JExploseUtils.class.getResource("icon.png")));
        return action;
    }

    /**
     * Register JExplose Lightning triggering with the given key <br/>
     * 
     * @param explosable    the <code>Explosable</code> to play the effect on.
     * @param key           the key to associate
     */
    public static void installLightningHotKey(final Explosable explosable, final int key) {
        try {
            //test if KeyboardFocusManager exist in currebt jdk
            Class c = Class.forName("java.awt.KeyboardFocusManager");
            JExploseKeyDispatcher.registerLightning(explosable, key);
        } catch (ClassNotFoundException e) {
            //Old jdk, old way to register keylistener hook
            Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                public void eventDispatched(AWTEvent event) {
                    if(((KeyEvent)event).getKeyCode() == key) {
                        JExplose.getInstance().explose(explosable);
                    }
                }
            }, AWTEvent.KEY_EVENT_MASK);
        }
    }
    
    /**
     * Register JExplose Thunder triggering with the given key <br/>
     * 
     * @param desktopPane    the <code>JDesktopPane</code> to play the effect on.
     * @param key           the key to associate
     */
    public static void installThunderHotKey(final JDesktopPane desktopPane, final int key) {
        try {
            //test if KeyboardFocusManager exist in currebt jdk
            Class c = Class.forName("java.awt.KeyboardFocusManager");
            JExploseKeyDispatcher.registerThunder(desktopPane, key);
        } catch (ClassNotFoundException e) {
            //Old jdk, old way to register keylistener hook
            Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                public void eventDispatched(AWTEvent event) {
                    if(((KeyEvent)event).getKeyCode() == key) {
                        JExplose.getInstance().explose(desktopPane);
                    }
                }
            }, AWTEvent.KEY_EVENT_MASK);
        }
    }    
}
