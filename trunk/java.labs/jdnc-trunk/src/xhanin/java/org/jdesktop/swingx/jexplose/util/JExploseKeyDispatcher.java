/*
 * $Id: JExploseKeyDispatcher.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;

import org.jdesktop.swingx.jexplose.Explosable;
import org.jdesktop.swingx.jexplose.JExplose;


/**
 * This helper register a key for triggering JExplose effect.<br/>
 * This is done using the JDK1.4 KeyEventDispatcher class 
 */
public class JExploseKeyDispatcher {

    /**
     * Register for <code>KeyEvent.VK_F12</code> key by default
     * @param explosable the component to play the effect on.
     */
    public static void register(Explosable explosable) {
        registerLightning(explosable, KeyEvent.VK_F12);
    }
    
    /**
     * Register JExplose Lightning triggering with the given key 
     * @param explosable    the component to play the effect on.
     * @param key           the key to associate
     */
    public static void registerLightning(final Explosable explosable, final int key) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == key) {
                    JExplose.getInstance().explose(explosable);
                    return true;
                }
                return false;
            }
        });
    }
    /**
     *  Register JExplose Thunder triggering with the given key 
     * @param explosable    the JDesktopPane to play the effect on.
     * @param key           the key to associate
     */
    public static void registerThunder(final JDesktopPane explosable, final int key) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == key) {
                    JExplose.getInstance().explose(explosable);
                    return true;
                }
                return false;
            }
        });
    }    
}
