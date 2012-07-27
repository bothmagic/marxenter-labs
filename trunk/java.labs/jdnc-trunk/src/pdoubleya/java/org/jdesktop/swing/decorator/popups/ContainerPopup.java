package org.jdesktop.swing.decorator.popups;

import javax.swing.*;

import java.applet.Applet;
import java.awt.*;

/**
 * @author Gilles Philippart
 */
public class ContainerPopup extends Popup {

    /**
     * Component we are to be added to.
     */
    Component owner;
    /**
     * Desired x location.
     */
    int x;
    /**
     * Desired y location.
     */
    int y;

    public void hide() {
        Component component = getComponent();
        if (component != null) {
            Container parent = component.getParent();
            if (parent != null) {
                Rectangle bounds = component.getBounds();
                parent.remove(component);
                parent.repaint(bounds.x, bounds.y, bounds.width,
                        bounds.height);
            }
        }
        owner = null;
    }

    public void pack() {
        Component component = getComponent();
        if (component != null) {
            component.setSize(component.getPreferredSize());
        }
    }

    public void reset(Component owner, Component contents, int ownerX,
                      int ownerY) {
        if ((owner instanceof JFrame) || (owner instanceof JDialog) ||
                (owner instanceof JWindow)) {
            // Force the content to be added to the layered pane, otherwise
            // we'll get an exception when adding to the RootPaneContainer.
            owner = ((RootPaneContainer) owner).getLayeredPane();
        }
        super.reset(owner, contents, ownerX, ownerY);
        x = ownerX;
        y = ownerY;
        this.owner = owner;
    }

    /**
     * Returns true if the Popup can fit on the screen.
     */
    boolean fitsOnScreen() {
        Component component = getComponent();
        if (owner != null && component != null) {
            Container parent;
            int width = component.getWidth();
            int height = component.getHeight();
            for (parent = owner.getParent(); parent != null;
                 parent = parent.getParent()) {
                if (parent instanceof JFrame ||
                        parent instanceof JDialog ||
                        parent instanceof JWindow) {
                    Rectangle r = parent.getBounds();
                    Insets i = parent.getInsets();
                    r.x += i.left;
                    r.y += i.top;
                    r.width -= (i.left + i.right);
                    r.height -= (i.top + i.bottom);
                    return SwingUtilities.isRectangleContainingRectangle(r, new Rectangle(x, y, width, height));
                } else if (parent instanceof JApplet) {
                    Rectangle r = parent.getBounds();
                    Point p = parent.getLocationOnScreen();
                    r.x = p.x;
                    r.y = p.y;
                    return SwingUtilities.isRectangleContainingRectangle(r, new Rectangle(x, y, width, height));
                } else if (parent instanceof Window ||
                        parent instanceof Applet) {
                    // No suitable swing component found
                    break;
                }
            }
        }
        return false;
    }
}

