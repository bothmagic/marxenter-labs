/*
 * Created on 29.02.2008
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.Graphics2D;

import org.jdesktop.swingx.painter.AbstractLayoutPainter;
import org.jdesktop.swingx.painter.Painter;

public class RelativePainter<T> extends AbstractLayoutPainter<T> {

    private Painter<T> painter;
    private double xFactor;
    private double yFactor;

    public RelativePainter() {
        this(null);
    }
    
    public void setPainter(Painter<T> painter) {
        this.painter = painter;
    }
    
    public Painter<T> getPainter() {
        return painter;
    }
    
    public RelativePainter(Painter<T> delegate) {
        this.painter = delegate;
    }
    
    public void setXFactor(double xPercent) {
        this.xFactor = xPercent;
    }
    
    public void setYFactor(double yPercent) {
        this.yFactor = yPercent;
    }
    @Override
    protected void doPaint(Graphics2D g, T object, int width, int height) {
        if (painter == null) return;
        // use epsilon
        if (xFactor != 0.0) {
            int oldWidth = width;
            width = (int) (xFactor * width);
            if (getHorizontalAlignment() == HorizontalAlignment.RIGHT) {
                g.translate(oldWidth - width, 0);
            }
        }
        if (yFactor != 0.0) {
            int oldHeight = height;
            height = (int) (yFactor * height);
            if (getVerticalAlignment() == VerticalAlignment.BOTTOM) {
                g.translate(0, oldHeight - height);
            }
        }
        
        painter.paint(g, object, width, height);
    }
    
}
