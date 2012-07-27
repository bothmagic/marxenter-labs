package org.jdesktop.swing.decorator.popups;

import javax.swing.*;

import java.awt.*;

/**
 * @author Gilles Philippart
 */
public class LightWeightPopup extends ContainerPopup {

    public void hide() {
        super.hide();
        Container component = (Container) getComponent();
        component.removeAll();
    }

    public void show() {
        Container parent = null;
        if (owner != null) {
            parent = (owner instanceof Container ? (Container) owner : owner.getParent());
        }

        // Try to find a JLayeredPane and Window to add
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof JInternalFrame) {
                parent = ((JInternalFrame) p).getLayeredPane();
                break;
            } else if (p instanceof JRootPane) {
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
        Point p = convertScreenLocationToParent(parent, x, y);
        Component component = getComponent();
        component.setLocation(p.x, p.y);
        if (parent instanceof JLayeredPane) {
            parent.add(component, JLayeredPane.POPUP_LAYER, 0);
        } else {
            if (parent != null) {
                parent.add(component);
            }
        }
    }

    static Point convertScreenLocationToParent(Container parent, int x, int y) {
        for (Container p = parent; p != null; p = p.getParent()) {
            if (p instanceof Window) {
                Point point = new Point(x, y);
                SwingUtilities.convertPointFromScreen(point, parent);
                return point;
            }
        }
        throw new Error("convertScreenLocationToParent: no window ancestor");
    }

    Component createComponent(Component owner) {
        JComponent component = new JPanel(new BorderLayout(), true);
        component.setOpaque(true);
        return component;
    }

    /**
     * Resets the <code>Popup</code> to an initial state.
     */
    public void reset(Component owner, Component contents, int ownerX, int ownerY) {
        super.reset(owner, contents, ownerX, ownerY);
        Component component = getComponent();
        if (component instanceof JComponent) {
            JComponent comp = (JComponent) component;
            comp.setLocation(ownerX, ownerY);
            comp.add(contents, BorderLayout.CENTER);
        }
        contents.invalidate();
        pack();
    }
}
