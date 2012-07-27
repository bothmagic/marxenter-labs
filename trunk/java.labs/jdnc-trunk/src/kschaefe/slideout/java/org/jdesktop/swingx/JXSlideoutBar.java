/*
 * $Id: JXSlideoutBar.java 3180 2009-07-01 19:48:52Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JApplet;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXCollapsiblePane.Direction;

/**
 * A {@code JXSlideoutBar} is a tool bar that contains a group of {@code JXLabel}s that activate a
 * popup panel. This panel "slides out," hence the name, from the inner edge of the component. As
 * long as the mouse maintains focus over the {@code JXSlideoutBar} or the popup panel, then the
 * panel will continue to display. The panel will disappear, if this focus is not maintained.
 * 
 * @author Karl Schaefer
 */
public class JXSlideoutBar extends JXPanel {
    public enum Orientation {
        NORTH, SOUTH, EAST, WEST
    }

    private class DisplayActivator extends MouseAdapter {
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            Dimension dpd = displayPanel.getPreferredSize();
            Rectangle r = JXSlideoutBar.this.getBounds();

            displayPanel.setBounds(r.x + r.width, r.y, dpd.width, r.y + r.height);

            String id = Integer.toString(e.getSource().hashCode());
            displayController.show(displayPanel.getContentPane(), id);

            displayPanel.setVisible(true);
            displayPanel.setCollapsed(false);
        }
    }

    private class DisplayDeactivator extends MouseAdapter {
        private Timer visibilityTimer;

        public DisplayDeactivator() {
            visibilityTimer = new Timer(250, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    displayPanel.setCollapsed(true);
                }
            });
            visibilityTimer.setRepeats(false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            visibilityTimer.stop();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited(MouseEvent e) {
            Component source = e.getComponent();

            // prevent children from causing the display to disappear
            if (!source.contains(e.getPoint())) {
                visibilityTimer.start();
            }
        }
    }

    private JXCollapsiblePane displayPanel;

    private CardLayout displayController;

    private MouseListener activator;

    private MouseListener deactivator;

    private Orientation orientation;

    public JXSlideoutBar() {
        activator = new DisplayActivator();
        deactivator = new DisplayDeactivator();
        displayPanel = new JXCollapsiblePane();
        displayController = new CardLayout();
        setOrientation(Orientation.WEST);

        // configure self
        addMouseListener(deactivator);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // configure children
        displayPanel.setLayout(displayController);
        ((JComponent) displayPanel.getContentPane()).setBorder(BorderFactory
                .createBevelBorder(BevelBorder.RAISED));
        displayPanel.addMouseListener(deactivator);
        displayPanel.setCollapsed(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        JLabel label;

        if (constraints instanceof String) {
            label = new JXLabel((String) constraints);
        } else if (constraints instanceof JLabel) {
            label = (JLabel) constraints;
        } else if (constraints instanceof Icon) {
            label = new JXLabel();
            label.setIcon((Icon) constraints);
        } else {
            throw new IllegalArgumentException("constraints must be a String, Icon, or JLabel");
        }

        label.addMouseListener(activator);
        label.setBorder(LineBorder.createGrayLineBorder());

        displayPanel.add(comp, Integer.toString(label.hashCode()));
        super.addImpl(label, null, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() {
        super.addNotify();

        addDisplayPanel();

        setOrientation(discernOrientation());
    }

    /**
     * @return
     */
    private Orientation discernOrientation() {
        Orientation o = Orientation.WEST;
        Container parent = getParent();

        if (parent != null) {
            if (parent instanceof JComponent) {
                JComponent comp = (JComponent) parent;

                LayoutManager mgr = comp.getLayout();

                if (mgr instanceof BoxLayout) {
                    BorderLayout blm = (BorderLayout) mgr;

                    Object constraint = blm.getConstraints(this);

                    // TODO handle bidi compliance
                    if (BorderLayout.NORTH.equals(constraint)) {
                        o = Orientation.NORTH;
                    } else if (BorderLayout.SOUTH.equals(constraint)) {
                        o = Orientation.SOUTH;
                    } else if (BorderLayout.EAST.equals(constraint)) {
                        o = Orientation.EAST;
                    }
                }
            }
        }

        return o;
    }

    private void addDisplayPanel() {
        Container parent = null;

        // Copied from PopupFactory.LightWeightPopup.show
        // unable to get PopupFactory to work with dynamically resizing
        // JXCollapsiblePane

        // Try to find a JLayeredPane and Window to add
        for (Container p = this; p != null; p = p.getParent()) {
            if (p instanceof JRootPane) {
                if (p.getParent() instanceof JInternalFrame) {
                    continue;
                }
                parent = ((JRootPane) p).getLayeredPane();
                // Continue, so that if there is a higher JRootPane, we'll
                // pick it up.
            } else if (p instanceof Window) {
                if (parent == null) {
                    parent = p;
                }
                break;
            } else if (p instanceof JApplet) {
                // Painting code stops at Applets, we don't want
                // to add to a Component above an Applet otherwise
                // you'll never see it painted.
                break;
            }
        }

        if (parent instanceof JLayeredPane) {
            ((JLayeredPane) parent).add(displayPanel, JLayeredPane.POPUP_LAYER, 0);
        } else {
            parent.add(displayPanel);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
        displayPanel.getParent().remove(displayPanel);

        super.removeNotify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Component comp) {
        synchronized (getTreeLock()) {
            Component[] comps = displayPanel.getComponents();

            for (int i = comps.length; --i >= 0;) {
                if (comps[i] == comp) {
                    remove(i);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(int index) {
        synchronized (getTreeLock()) {
            displayPanel.remove(index);

            super.remove(index);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAll() {
        synchronized (getTreeLock()) {
            displayPanel.removeAll();

            super.removeAll();
        }
    }

    /**
     * @return the orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * @param orientation
     *            the orientation to set
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == this.orientation) {
            System.err.println("in short circuit");
            return;
        }

        this.orientation = orientation;

        // reset BoxLayout since it doesn't have useful getters until 1.6
        switch (orientation) {
        case NORTH:
            displayPanel.setDirection(Direction.UP);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            break;
        case SOUTH:
            displayPanel.setDirection(Direction.DOWN);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            break;
        case EAST:
            displayPanel.setDirection(Direction.RIGHT);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        case WEST:
            displayPanel.setDirection(Direction.LEFT);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            break;
        default:
            // does nothing
            break;
        }
    }
}
