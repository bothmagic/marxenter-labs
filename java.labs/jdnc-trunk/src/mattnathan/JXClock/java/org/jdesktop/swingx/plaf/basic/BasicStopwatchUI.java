/*
 * $Id: BasicStopwatchUI.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.clock.VistaStopwatchIcon;
import org.jdesktop.swingx.*;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.InputMap;
import org.jdesktop.swingx.plaf.LazyActionMap;
import javax.swing.*;

/**
 * Basic implementation of the look and feel of a stopwatch.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicStopwatchUI<C extends JXStopwatch> extends BasicClockUI<C> implements LazyActionMap.ActionMapLoader {

    private static BasicStopwatchUI<JXStopwatch> SINGLETON;
    public static ComponentUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicStopwatchUI<JXStopwatch>();
        }
        return SINGLETON;
    }





    public BasicStopwatchUI() {
        super();
        setInstallDefaultFocusSupport(true);
    }





    /**
     * Create the icon used to paint this component.
     *
     * @param c The source component to paint.
     * @return The icon to paint the component.
     */
    @Override
    protected Icon createForegroundIcon(C c) {
        return new VistaStopwatchIcon();
    }





    /**
     * Target for the start action.
     *
     * @param c The source component.
     */
    protected void start(C c) {
        if (!c.isRunning()) {
            c.start();
        }
    }





    /**
     * Target for the stop action.
     *
     * @param c The source component.
     */
    protected void stop(C c) {
        if (c.isRunning()) {
            c.mark(true);
        }
    }





    /**
     * Target for the reset action.
     *
     * @param c The source component.
     */
    protected void reset(C c) {
        c.reset();
    }





    /**
     * Target for the mark action.
     *
     * @param c The source component.
     */
    protected void mark(C c) {
        if (c.isRunning()) {
            c.mark(false);
        }
    }





    /**
     * Add any key bindings to the component.
     *
     * @param c The component to add key bindings to.
     */
    @Override
    protected void installKeyBindings(C c) {
        InputMap imap = getInputMap(JComponent.WHEN_FOCUSED, c);
        SwingUtilities.replaceUIInputMap(c, JComponent.WHEN_FOCUSED, imap);
        LazyActionMap.installLazyActionMap(c, this, "Stopwatch.actionMap");
    }





    /**
     * Uninstall any key bindings from the source component.
     *
     * @param component The component to remove key bindings from.
     */
    @Override
    protected void uninstallKeyBindings(C component) {
        SwingUtilities.replaceUIActionMap(component, null);
        SwingUtilities.replaceUIInputMap(component, JComponent.WHEN_FOCUSED, null);
    }





    InputMap getInputMap(int condition, C s) {
        InputMap result = null;
        if (condition == JComponent.WHEN_FOCUSED) {
            result = (InputMap) LookAndFeelUtilities.get("Stopwatch.focusInputMap", createDefaultInputMap(s));
        }
        return result;
    }





    public void loadActionMap(LazyActionMap map) {
        map.put(new Actions(Actions.ACTION_MARK));
        map.put(new Actions(Actions.ACTION_RESET));
        map.put(new Actions(Actions.ACTION_START));
        map.put(new Actions(Actions.ACTION_STOP));
        map.put(new Actions(Actions.ACTION_START_MARK));
        map.put(new Actions(Actions.ACTION_START_STOP));
    }





    /**
     * Creates the default input map for the component.
     *
     * @param c The component.
     * @return The input map.
     */
    protected InputMap createDefaultInputMap(C c) {
        InputMap result = new InputMap();

        result.put(KeyStroke.getKeyStroke("SPACE"), Actions.ACTION_START_MARK);
        result.put(KeyStroke.getKeyStroke("ENTER"), Actions.ACTION_START_STOP);
        result.put(KeyStroke.getKeyStroke("control S"), Actions.ACTION_START_STOP);
        result.put(KeyStroke.getKeyStroke("control M"), Actions.ACTION_MARK);
        result.put(KeyStroke.getKeyStroke("control R"), Actions.ACTION_RESET);
        result.put(KeyStroke.getKeyStroke("F5"), Actions.ACTION_RESET);
        result.put(KeyStroke.getKeyStroke("ESCAPE"), Actions.ACTION_STOP);

        return result;
    }





    /**
     * Provides action support for keyboard and other actions.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private static class Actions extends UIAction {
        public static final String ACTION_START_MARK = "startMark";
        public static final String ACTION_START_STOP = "startStop";

        public static final String ACTION_START = "start";
        public static final String ACTION_MARK = "mark";
        public static final String ACTION_STOP = "stop";
        public static final String ACTION_RESET = "reset";

        public Actions(String name) {
            super(name);
        }





        @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent e) {
            JXStopwatch w = (JXStopwatch) e.getSource();
            BasicStopwatchUI<JXStopwatch> ui = (BasicStopwatchUI<JXStopwatch>) w.getUI();
            String name = getName();

            if (name == ACTION_START) {
                ui.start(w);
            } else if (name == ACTION_MARK) {
                ui.mark(w);
            } else if (name == ACTION_STOP) {
                ui.stop(w);
            } else if (name == ACTION_RESET) {
                ui.reset(w);
            } else if (name == ACTION_START_MARK) {
                if (w.isRunning()) {
                    ui.mark(w);
                } else {
                    ui.start(w);
                }
            } else if (name == ACTION_START_STOP) {
                if (w.isRunning()) {
                    ui.stop(w);
                } else {
                    ui.start(w);
                }
            }
        }
    }
}
