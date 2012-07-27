/*
 * $Id: JXFrame.java 888 2006-10-02 05:58:45Z alius $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

/**
 * Advanced <code>JFrame<code> which additionaly can contain three status bars and up to four tool bars.
 *
 * @author Aleksandras Novikovas
 */
public class JXFrame extends JFrame {

    /**
     * Overriden to create a JXRootPane.
     *
     * @return <code>JRootPane</code> created default root pane.
     */
    protected JRootPane createRootPane() {
        return new JXRootPane ();
    }

    /**
     * Overriden to enforce <code>JXRootPane</code> usage. Sets new root pane object for a frame.
     *
     * @param root <code>JRootPane</code> new root pane for the frame. <strong>Actually this method accepts only
     *      <code>JXRootPane<code></strong>.
     */
    protected void setRootPane (JRootPane root) {
        if (root instanceof JXRootPane) {
            super.setRootPane (root);
        }
    }

    /**
     * Adds new <code>JToolBar</code> to the frame at <code>BorderLayout.PAGE_START<code>.
     *
     * @param toolBar <code>JToolBar</code> which has to be added into frame.
     *
     * @return <code>boolean</code> true if tool bar was added successfully; false if requested area is already
     *      occupied by another toolbar.
     */
    public boolean addToolBar (JToolBar toolBar) {
        return ((JXRootPane) getRootPane ()).addToolBar (toolBar);
    }

    /**
     * Adds new <code>JToolBar</code> to the frame with specified constraints.
     *
     * @param toolBar <code>JToolBar</code> which has to be added into frame.
     * @param constraints <code>Object</code> for the added tool bar.
     *
     * @return <code>boolean</code> true if tool bar was added successfully; false if requested area is already
     *      occupied by another toolbar.
     */
    public boolean addToolBar (JToolBar toolBar, Object constraints) {
        return ((JXRootPane) getRootPane ()).addToolBar (toolBar, constraints);
    }

    /**
     * Removes specified <code>JToolBar</code> from the frame.
     *
     * @param toolBar <code>JToolBar</code> which has to be removed from frame.
     */
    public void removeToolBar (JToolBar toolBar) {
        ((JXRootPane) getRootPane ()).removeToolBar (toolBar);
    }

    /**
     * Returns an array containing all tool bars assigned to this frame.
     */
    public JToolBar[] getAllToolBars () {
        return ((JXRootPane) getRootPane ()).getAllToolBars ();
    }

    /**
     * Sets new status bar component to frame at <code>BorderLayout.PAGE_END</code>.
     *
     * @param statusBar <code>Container</code> status bar.
     */
    public void setStatusBar (Container statusBar) {
        ((JXRootPane) getRootPane ()).setStatusBar (statusBar);
    }

    /**
     * Sets new status bar component to frame with specified constraints.
     *
     * @param statusBar <code>Container</code> status bar.
     * @param constraints <code>Object</code> for the added status bar.
     *
     * @throws IllegalComponentStateException if trying to replace menu with status bar.
     */
    public void setStatusBar (Container statusBar, Object constraints) {
        ((JXRootPane) getRootPane ()).setStatusBar (statusBar, constraints);
    }

    /**
     * Returns status bar component at <code>BorderLayout.PAGE_END</code> position.
     * Returns <code>null</code> if frame does not contain status bar.
     *
     * @return <code>Container</code> status bar.
     */
    public Container getStatusBar () {
        return ((JXRootPane) getRootPane ()).getStatusBar ();
    }

    /**
     * Returns status bar component at <code>BorderLayout.PAGE_END</code> position.
     * Returns <code>null</code> if frame does not contain status bar or constraints specified incorrectly.
     *
     * @param constraints <code>Object</code> to identify status bar position.
     *
     * @return <code>Container</code> status bar.
     */
    public Container getStatusBar (Object constraints) {
        return ((JXRootPane) getRootPane ()).getStatusBar (constraints);
    }

}
