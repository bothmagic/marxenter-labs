package org.jdesktop.swingx.painter;

import java.awt.Graphics2D;
import javax.swing.*;
import java.awt.*;

public class ComponentPainter extends AbstractPainter {

    private Component component;
    private CellRendererPane renderer;

    public ComponentPainter() {
        super();
    }





    public ComponentPainter(Component component) {
        setComponent(component);
    }





    public void setComponent(Component component) {
        if (renderer == null) {
            renderer = new CellRendererPane();
        }
        this.component = component;
    }





    public Component getComponent() {
        return component;
    }





    /**
     * Subclasses must implement this method and perform custom painting operations here.
     *
     * @param g The Graphics2D object in which to paint
     * @param object Object
     * @param width int
     * @param height int
     */
    @Override
    protected void doPaint(Graphics2D g, Object object, int width, int height) {
        Component component = getComponent();
        if (component != null) {
            assert renderer != null;
            if (object instanceof Container) {
                renderer.paintComponent(g, component, (Container) object, 0, 0, width, height);
            } else {
                renderer.paintComponent(g, component, null, 0, 0, width, height);
            }
        }
    }
}
