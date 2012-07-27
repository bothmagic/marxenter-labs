/*
 * $Id: JXRootPane.java 888 2006-10-02 05:58:45Z alius $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JToolBar;

/**
 * Extends the JRootPane by adding possibility to add menu and status bars in the desired side of the frame. It also
 * allows to add up to four <code>JToolBar</code> components.
 * <p>
 * It wraps content pane with menu/status and tool bar panes. So all standard JFrame functionality is preserved.
 *
 * @author Aleksandras Novikovas
 */
public class JXRootPane extends JRootPane {

    /**
     * This pane replaces content pane in the layered pane. It uses standard BorderLayout and can holds menu and status
     * bars on every side of the frame. Center is occupied with the toolBarPane.
     */
    protected Container statusPane;

    /**
     * This pane allows to hold up to four tool bars on each side of the frame. Center is occupied by real contenPane.
     */
    protected Container toolBarPane;

    /**
     * Holds list of tool bars contained in this frame.
     */
    protected Vector<JToolBar> toolBars;

    /**
     * Holds status bar which will be shown at the <code>BorderLayout.LINE_START</code>.
     */
    protected Container lineStartStatusBar;

    /**
     * Holds status bar which will be shown at the <code>BorderLayout.LINE_END</code>.
     */
    protected Container lineEndStatusBar;

    /**
     * Holds status bar which will be shown at the <code>BorderLayout.PAGE_END</code>.
     */
    protected Container pageEndStatusBar;
    
    /**
     * Creates a new instance of JXRootPane. It creates and adds <code>statusPane</code> and <code>toolBarPane</code>
     * and put them between <code>layeredPane</code> and
     * <code>contentPane</code>.
     */
    public JXRootPane () {
        lineStartStatusBar = null;
        lineEndStatusBar = null;
        pageEndStatusBar = null;
        setStatusPane (createStatusPane ());
        toolBars = new Vector<JToolBar> ();
        setToolBarPane (createToolBarPane ());
        setContentPane(createContentPane());
    }

    /**
     * Called by the constructor to create <code>statusPane</code>.
     * <code>BorderLayout</code> as its <code>LayoutManager</code>.
     *
     * @return <code>Container</code> created status pane.
     */
    protected Container createStatusPane () {
        JComponent c = new JPanel ();
        c.setName (this.getName ()+".statusPane");
        c.setLayout (new BorderLayout ());
        c.setOpaque (true);
        return c;
    }

    /**
     * Called by the constructor to create <code>toolBarPane</code>.
     * <code>BorderLayout</code> as its <code>LayoutManager</code>.
     *
     * @return <code>Container</code> created tool bar pane.
     */
    protected Container createToolBarPane () {
        JComponent c = new JPanel ();
        c.setName (this.getName ()+".toolBarPane");
        c.setLayout (new BorderLayout ());
        c.setOpaque (true);
        return c;
    }

    /**
     * Called by the constructor methods to create layout manager for a frame.
     *
     * @return <code>LayoutManager</code> used to lay out frame.
     */
    protected LayoutManager createRootLayout () {
        return new XRootLayout ();
    }

    /**
     * Desription copied:<p>
     * Adds or changes the menu bar used in the layered pane.
     * @param menu the <code>JMenuBar</code> to add
     */
    public void setJMenuBar (JMenuBar menu) {
        if(menuBar != null && menuBar.getParent () == statusPane) statusPane.remove (menuBar);
        menuBar = menu;
        if(menuBar != null) statusPane.add (menuBar, BorderLayout.PAGE_START);
    }

    /**
     * Desription copied:<p>
     * Specifies the menu bar value.
     * @deprecated As of Swing version 1.0.3
     *  replaced by <code>setJMenuBar(JMenuBar menu)</code>.
     * @param menu the <code>JMenuBar</code> to add.
     */
    @Deprecated
    public void setMenuBar(JMenuBar menu){
        setJMenuBar (menu);
    }

    /** 
     * Desription copied:<p>
     * Returns the menu bar from the layered pane. 
     * @return the <code>JMenuBar</code> used in the pane
     */
    public JMenuBar getJMenuBar() {
        return menuBar;
    }

    /**
     * Desription copied:<p>
     * Returns the menu bar value.
     * @deprecated As of Swing version 1.0.3
     *  replaced by <code>getJMenubar()</code>.
     * @return the <code>JMenuBar</code> used in the pane
     */
    @Deprecated
    public JMenuBar getMenuBar() {
        return getJMenuBar();
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
        return addToolBar (toolBar, BorderLayout.PAGE_START);
    }

    /**
     * Adds new <code>JToolBar</code> to the frame with specified constraints.
     * <p>
     * Current implementation of <code>javax.swing.plaf.basic.BasicToolBarUI</code> allows to have only one tool bar
     * per area if <code>BorderLayout</code> is used. In order not to remove already existing tool bar checking is done
     * before adding a new one. New tool bar is not added if requested area is already occupied.
     *
     * @param toolBar <code>JToolBar</code> which has to be added into frame.
     * @param constraints <code>Object</code> for the added tool bar.
     *
     * @return <code>boolean</code> true if tool bar was added successfully; false if requested area is already
     *      occupied by another toolbar.
     *
     * @throws IllegalComponentStateException if passed toolBar is <code>null</code> or it is requested to put tool
     *      bar into center of the panel.
     * @throws IllegalArgumentException if passed constraint is not valid <code>BorderLayout</code> constant.
     */
    public synchronized boolean addToolBar (JToolBar toolBar, Object constraints) {
        if (toolBar == null) throw new IllegalComponentStateException ("toolBar cannot be set to null.");
        if (constraints == null || BorderLayout.CENTER.equals (constraints))
            throw new IllegalComponentStateException ("toolBar cannot be added to center of the pane.");
        LayoutManager lm = toolBarPane.getLayout ();
        BorderLayout blm = null;
        if (lm instanceof BorderLayout) blm = (BorderLayout) lm;
        if (blm != null) {
            if (blm.getLayoutComponent (constraints) == null) {
                if (!toolBars.contains (toolBar)) toolBars.add (toolBar);
                if (BorderLayout.PAGE_START.equals (constraints) ||
                    BorderLayout.BEFORE_FIRST_LINE.equals (constraints) ||
                    BorderLayout.NORTH.equals (constraints) ||
                    BorderLayout.PAGE_END.equals (constraints) ||
                    BorderLayout.AFTER_LAST_LINE.equals (constraints) ||
                    BorderLayout.SOUTH.equals (constraints)) {
                    toolBar.setOrientation (JToolBar.HORIZONTAL);
                }
                if (BorderLayout.LINE_START.equals (constraints) ||
                    BorderLayout.BEFORE_LINE_BEGINS.equals (constraints) ||
                    BorderLayout.WEST.equals (constraints) ||
                    BorderLayout.LINE_END.equals (constraints) ||
                    BorderLayout.AFTER_LINE_ENDS.equals (constraints) ||
                    BorderLayout.EAST.equals (constraints)) {
                    toolBar.setOrientation (JToolBar.VERTICAL);
                }
                toolBarPane.add (toolBar, constraints);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes specified <code>JToolBar</code> from the frame.
     *
     * @param toolBar <code>JToolBar</code> which has to be removed from frame.
     */
    public synchronized void removeToolBar (JToolBar toolBar) {
        toolBars.remove (toolBar);
        toolBarPane.remove (toolBar);
    }

    /**
     * Returns an array containing all tool bars assigned to this frame.
     */
    public JToolBar[] getAllToolBars () {
        return toolBars.toArray (new JToolBar[toolBars.size ()]);
    }

    /**
     * Sets new status bar component to frame at <code>BorderLayout.PAGE_END</code>.
     *
     * @param statusBar <code>Container</code> status bar.
     */
    public void setStatusBar (Container statusBar) {
        setStatusBar (statusBar, BorderLayout.PAGE_END);
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
        if (BorderLayout.PAGE_START.equals (constraints) ||
            BorderLayout.BEFORE_FIRST_LINE.equals (constraints) ||
            BorderLayout.NORTH.equals (constraints))
            throw new IllegalComponentStateException ("statusBar cannot replace menu component.");
        // First we remove component if it is already added in other place.
        if (statusBar != null) {
            if (lineStartStatusBar == statusBar) {
                statusPane.remove (lineStartStatusBar);
                lineStartStatusBar = null;
            }
            if (lineEndStatusBar == statusBar) {
                statusPane.remove (lineEndStatusBar);
                lineEndStatusBar = null;
            }
            if (pageEndStatusBar == statusBar) {
                statusPane.remove (pageEndStatusBar);
                pageEndStatusBar = null;
            }
        }
        boolean ltr = getComponentOrientation ().isLeftToRight ();
        if (BorderLayout.PAGE_END.equals (constraints) ||
            BorderLayout.AFTER_LAST_LINE.equals (constraints) ||
            BorderLayout.SOUTH.equals (constraints)) {
            if (pageEndStatusBar != null) statusPane.remove (pageEndStatusBar);
            pageEndStatusBar = statusBar;
            if (pageEndStatusBar != null) statusPane.add (pageEndStatusBar, constraints);
            return;
        }
        if (BorderLayout.LINE_START.equals (constraints) ||
            BorderLayout.BEFORE_LINE_BEGINS.equals (constraints) ||
            ((ltr)?BorderLayout.WEST:BorderLayout.EAST).equals (constraints)) {
            if (lineStartStatusBar != null) statusPane.remove (lineStartStatusBar);
            lineStartStatusBar = statusBar;
            if (lineStartStatusBar != null) statusPane.add (lineStartStatusBar, constraints);
            return;
        }
        if (BorderLayout.LINE_END.equals (constraints) ||
            BorderLayout.AFTER_LINE_ENDS.equals (constraints) ||
            ((ltr)?BorderLayout.EAST:BorderLayout.WEST).equals (constraints)) {
            if (lineEndStatusBar != null) statusPane.remove (lineEndStatusBar);
            lineEndStatusBar = statusBar;
            if (lineEndStatusBar != null) statusPane.add (lineEndStatusBar, constraints);
            return;
        }
    }

    /**
     * Returns status bar component at <code>BorderLayout.PAGE_END</code> position.
     * Returns <code>null</code> if frame does not contain status bar.
     *
     * @return <code>Container</code> status bar.
     */
    public Container getStatusBar () {
        return getStatusBar (BorderLayout.PAGE_END);
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
        boolean ltr = getComponentOrientation ().isLeftToRight ();
        if (BorderLayout.PAGE_END.equals (constraints) ||
            BorderLayout.AFTER_LAST_LINE.equals (constraints) ||
            BorderLayout.SOUTH.equals (constraints)) {
            return pageEndStatusBar;
        }
        if (BorderLayout.LINE_START.equals (constraints) ||
            BorderLayout.BEFORE_LINE_BEGINS.equals (constraints) ||
            ((ltr)?BorderLayout.WEST:BorderLayout.EAST).equals (constraints)) {
            return lineStartStatusBar;
        }
        if (BorderLayout.LINE_END.equals (constraints) ||
            BorderLayout.AFTER_LINE_ENDS.equals (constraints) ||
            ((ltr)?BorderLayout.EAST:BorderLayout.WEST).equals (constraints)) {
            return lineEndStatusBar;
        }
        return null;
    }

    /**
     * Sets the status pane into frame.
     *
     * @param pane <code>Container</code> to use for manu and status bars.
     *
     * @throws IllegalComponentStateException if passed pane is <code>null</code>.
     */
    public void setStatusPane (Container pane) {
        if (pane == null) throw new IllegalComponentStateException ("statusPane cannot be set to null.");
        if (statusPane != null && statusPane.getParent () == layeredPane) layeredPane.remove (statusPane);
        statusPane = pane;
        layeredPane.add (statusPane, JLayeredPane.FRAME_CONTENT_LAYER);
    }

    /**
     * Sets the tool bar pane into frame.
     *
     * @param pane <code>Container</code> to use for tool bars.
     *
     * @throws IllegalComponentStateException if passed pane is <code>null</code>.
     */
    public void setToolBarPane (Container pane) {
        if (pane == null) throw new IllegalComponentStateException ("toolBarPane cannot be set to null.");
        if (toolBarPane != null && toolBarPane.getParent () == statusPane) {
            statusPane.remove (toolBarPane);
            for (JToolBar toolBar : toolBars) {
                toolBarPane.remove (toolBar);
            }
        }
        toolBarPane = pane;
        for (JToolBar toolBar : this.toolBars) {
            toolBarPane.add (toolBar);
        }
        statusPane.add (toolBarPane, BorderLayout.CENTER);
    }

    /**
     * Overwrides default behaviour to put content pane into <code>toolBarPane</code> instead of
     * <code>layeredPane</code>.
     *
     * @param content <code>Container</code> which holds all frame components.
     *
     * @throws IllegalComponentStateException if passed content pane is <code>null</code>.
     */
    public void setContentPane (Container content) {
        if (content == null) throw new IllegalComponentStateException ("contentPane cannot be set to null.");
        if (contentPane != null && contentPane.getParent () == toolBarPane) toolBarPane.remove (contentPane);
        contentPane = content;
        if (toolBarPane != null) toolBarPane.add (contentPane, BorderLayout.CENTER);
    }

    /**
     * Custom layout manager for the frame. It lays out layeredPane, glassPane and statusPane.
     */
    protected class XRootLayout extends RootLayout {

        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param parent <code>Container</code> for which this layout manager is being used.
         *
         * @return <code>Dimension</code> object containing the layout's preferred size.
         */ 
        public Dimension preferredLayoutSize (Container parent) {
            Dimension rd;
            Insets i = getInsets ();
            if (statusPane != null) {
                rd = statusPane.getPreferredSize ();
            }
            else {
                rd = parent.getSize ();
            }
            return new Dimension (rd.width + i.left + i.right, rd.height + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param parent <code>Container</code> for which this layout manager is being used.
         *
         * @return <code>Dimension</code> object containing the layout's minimum size.
         */ 
        public Dimension minimumLayoutSize (Container parent) {
            Dimension rd;
            Insets i = getInsets ();
            if (statusPane != null) {
                rd = statusPane.getMinimumSize ();
            }
            else {
                rd = parent.getSize ();
            }
            return new Dimension (rd.width + i.left + i.right, rd.height + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param target <code>Container</code> for which this layout manager is being used.
         *
         * @return <code>Dimension</code> object containing the layout's maximum size.
         */ 
        public Dimension maximumLayoutSize (Container target) {
            Dimension rd;
            Insets i = getInsets ();
            if (statusPane != null) {
                rd = statusPane.getMaximumSize ();
            }
            else {
                rd = new Dimension (Integer.MAX_VALUE - i.left - i.right - 1, Integer.MAX_VALUE - i.top - i.bottom - 1);
            }
            return new Dimension (rd.width + i.left + i.right, rd.height + i.top + i.bottom);
        }

        /**
         * Instructs the layout manager to perform the layout for the specified container.
         *
         * @param parent <code>Container</code> for which this layout manager is being used.
         */ 
        public void layoutContainer (Container parent) {
            Rectangle b = parent.getBounds ();
            Insets i = getInsets ();
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;
            if (layeredPane != null) layeredPane.setBounds (i.left, i.top, w, h);
            if (glassPane != null) glassPane.setBounds (i.left, i.top, w, h);
            if (statusPane != null) {
                statusPane.setBounds (0, 0, w, h);
            }
        }
    }

}
