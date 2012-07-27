/*
 * $Id: JXToolBar.java 2355 2008-03-27 03:10:59Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.jdesktop.swingx.painter.Painter;

/**
 * <p>
 * A {@link org.jdesktop.swingx.painter.Painter} enabled subclass of
 * {@link javax.swing.JToolBar}. This class supports setting a background
 * painter. If the painter is {@code null}, then traditional painting is used.
 * By default the background painter is {@code null}.
 * </p>
 * <p>
 * Not only does {@code JXToolBar} supports a background painter, but by default
 * buttons created by this class when {@code add(Action)} is called are
 * {@code JXButton}s. These buttons are both foreground- and background-painter
 * enabled. {@code JXToolBar} provides convenience methods for defaulting these
 * painters or manipulating them in batch.
 * </p>
 * 
 * @author Karl Schaefer
 */
public class JXToolBar extends JToolBar {
//    private Painter<?> bgPainter;
//
//    private Painter<?> buttonBgPainter;
//
//    private Painter<?> buttonFgPainter;
//
//    /**
//     * Creates a new tool bar with a {@code HORIZONTAL} orientation.
//     */
//    public JXToolBar() {
//        super();
//    }
//
//    /**
//     * Creates a new tool bar with the specified {@code orientation}. The
//     * {@code orientation} must be either {@code HORIZONTAL} or {@code VERTICAL}.
//     * 
//     * @param orientation
//     *            the orientation desired
//     * @throws IllegalArgumentException
//     *             if the orientation is neither {@code HORIZONTAL} nor
//     *             {@code VERTICAL}.
//     */
//    public JXToolBar(int orientation) {
//        super(orientation);
//    }
//
//    /**
//     * Creates a new tool bar with the specified {@code name}. The name is used
//     * as the title of the undocked tool bar. The default orientation is
//     * {@code HORIZONTAL}.
//     * 
//     * @param name
//     *            the name of the tool bar
//     */
//    public JXToolBar(String name) {
//        super(name);
//    }
//
//    /**
//     * Creates a new tool bar with a specified {@code name} and
//     * {@code orientation}. All other constructors defer to this constructor.
//     * The {@code orientation} must be either {@code HORIZONTAL} or
//     * {@code VERTICAL}.
//     * 
//     * @param name
//     *            the name of the tool bar
//     * @param orientation
//     *            the orientation desired
//     * @throws IllegalArgumentException
//     *             if the orientation is neither {@code HORIZONTAL} nor
//     *             {@code VERTICAL}.
//     */
//    public JXToolBar(String name, int orientation) {
//        super(name, orientation);
//    }
//
//    /**
//     * Factory method which creates {@code JXButton}s for {@code Action}s
//     * added to this {@code JXToolBar}. The default name is empty if a
//     * {@code null} action is passed.
//     * 
//     * @param a
//     *            the {@code Action} for the button to be added
//     * @return the newly created button
//     * @see Action
//     */
//    @Override
//    protected JXButton createActionComponent(Action a) {
//        JXButton b = new JXButton() {
//            protected PropertyChangeListener createActionPropertyChangeListener(
//                    Action a) {
//                PropertyChangeListener pcl = createActionChangeListener(this);
//                if (pcl == null) {
//                    pcl = super.createActionPropertyChangeListener(a);
//                }
//                return pcl;
//            }
//        };
//
//        if (a != null
//                && (a.getValue(Action.SMALL_ICON) != null || a
//                        .getValue(Action.LARGE_ICON_KEY) != null)) {
//            b.setHideActionText(true);
//        }
//
//        b.setHorizontalTextPosition(JButton.CENTER);
//        b.setVerticalTextPosition(JButton.BOTTOM);
//
//        if (getButtonBackgroundPainter() != null) {
//            b.setBackgroundPainter(getButtonBackgroundPainter());
//        }
//
//        if (getButtonForegroundPainter() != null) {
//            b.setForegroundPainter(getButtonForegroundPainter());
//        }
//
//        return b;
//    }
//
//    /**
//     * Paints the {@code Painter} returned by {@code getBackgroundPainter}
//     * unless it is {@code null}, in which case this method delegates to
//     * {@code super}.
//     * 
//     * @param g
//     *            the {@code Graphics} object to protect
//     * @see javax.swing.JComponent#paintComponent(Graphics)
//     */
//    protected void paintComponent(Graphics g) {
//        Painter<?> bgPainter = getBackgroundPainter();
//
//        if (bgPainter == null) {
//            super.paintComponent(g);
//        } else {
//            // if you don't create a copy, toolbar may not paint correctly
//            Graphics2D g2 = (Graphics2D) g.create();
//            bgPainter.paint(g2, this, getWidth(), getHeight());
//            g2.dispose();
//        }
//    }
//
//    /**
//     * Adds a new {@code JXButton} which dispatches the action.
//     * 
//     * @param a
//     *            the {@code Action} object to add as a new menu item
//     * @return the new button which dispatches the action
//     */
//    @Override
//    public JXButton add(Action a) {
//        return (JXButton) super.add(a);
//    }
//
//    /**
//     * Gets the background painter for this {@code JXToolBar}.
//     * 
//     * @return the background painter
//     */
//    public Painter<?> getBackgroundPainter() {
//        return bgPainter;
//    }
//
//    /**
//     * Sets the background painter for this {@code JXToolBar}.
//     * 
//     * @param backgroundPainter
//     *            the backgroundPainter to set
//     */
//    public void setBackgroundPainter(Painter<?> bgPainter) {
//        Painter<? super JXToolBar> old = getBackgroundPainter();
//        this.bgPainter = bgPainter;
//
//        firePropertyChange("backgroundPainter", old, getBackgroundPainter());
//        repaint();
//    }
//
//    /**
//     * Gets the background painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called.
//     * 
//     * @return the background painter
//     */
//    public Painter<?> getButtonBackgroundPainter() {
//        return buttonBgPainter;
//    }
//
//    /**
//     * Sets the background painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called and updates all
//     * existing {@code JXButton}s to use this painter as well.
//     * 
//     * @impl defers to {@link #setButtonBackgroundPainter(Painter, boolean)}
//     * @param painter
//     *            the painter used to paint the action component background
//     */
//    public void setButtonBackgroundPainter(Painter<?> painter) {
//        setButtonBackgroundPainter(painter, true);
//    }
//
//    /**
//     * Sets the background painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called.
//     * {@code updateExisting} allows the user to updates all of the current
//     * installed {@code JXButtons} to use the background painter.
//     * 
//     * @param painter
//     *            the painter used to paint the action component background
//     * @param updateExisting
//     *            if {@code true} updates all current {@code JXButton}s to use
//     *            the painter as well
//     */
//    public void setButtonBackgroundPainter(Painter<?> painter,
//            boolean updateExisting) {
//        Painter<?> old = getButtonBackgroundPainter();
//        buttonBgPainter = painter;
//
//        if (updateExisting) {
//            Component[] children = getComponents();
//
//            for (Component c : children) {
//                if (c instanceof JXButton) {
//                    ((JXButton) c)
//                            .setBackgroundPainter(getButtonBackgroundPainter());
//                }
//            }
//        }
//
//        firePropertyChange("buttonBackgroundPainter", old,
//                getButtonBackgroundPainter());
//    }
//
//    /**
//     * Gets the foreground painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called.
//     * 
//     * @return the foreground painter
//     */
//    public Painter<?> getButtonForegroundPainter() {
//        return buttonFgPainter;
//    }
//
//    /**
//     * Sets the foreground painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called and updates all
//     * existing {@code JXButton}s to use this painter as well.
//     * 
//     * @impl defers to {@link #setButtonForegroundPainter(Painter, boolean)}
//     * @param painter
//     *            the painter used to paint the action component foreground
//     */
//    public void setButtonForegroundPainter(Painter<?> painter) {
//        setButtonForegroundPainter(painter, true);
//    }
//
//    /**
//     * Sets the foreground painter that is currently used when creating new
//     * {@code JXButton}s, when {@code add(Action)} is called.
//     * {@code updateExisting} allows the user to updates all of the current
//     * installed {@code JXButtons} to use the foreground painter.
//     * 
//     * @param painter
//     *            the painter used to paint the action component foreground
//     * @param updateExisting
//     *            if {@code true} updates all current {@code JXButton}s to use
//     *            the painter as well
//     */
//    public void setButtonForegroundPainter(Painter<?> painter,
//            boolean updateExisting) {
//        Painter<?> old = getButtonForegroundPainter();
//        buttonFgPainter = painter;
//
//        if (updateExisting) {
//            Component[] children = getComponents();
//
//            for (Component c : children) {
//                if (c instanceof JXButton) {
//                    ((JXButton) c)
//                            .setBackgroundPainter(getButtonForegroundPainter());
//                }
//            }
//        }
//
//        firePropertyChange("buttonBackgroundPainter", old,
//                getButtonForegroundPainter());
//    }
}
