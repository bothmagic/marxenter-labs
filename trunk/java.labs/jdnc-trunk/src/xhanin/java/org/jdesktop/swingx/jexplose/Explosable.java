/*
 * $Id: Explosable.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JDesktopPane;

import org.jdesktop.swingx.jexplose.core.Painter;


/**
 * Defines a way to explose desktop with better performance
 * than directly with a <code>JDesktopPane</code>. Use it with 
 * a <code>JDesktopPane</code> subclass, delegating some paints to
 * it. 
 * <p>
 * If you do not usually use a custom <code>JDesktopPane</code>
 * subclass, you can simply use the ExplosableDesktop.
 * </p>
 * <p>
 * If you usually use a custom <code>JDesktopPane</code>
 * subclass, you just have to subclass it (or modify it if you can), 
 * and call {@link #preparePaint(Graphics)} at the beginning of your paint,
 * then do your usual painting (with <code>super.paint(g)</code> if you are 
 * subclassing a custom <code>JDesktopPane</code>), and then call 
 * {@link #finishPaint(Graphics)} before returning.<br/>
 * You also need to overwrite the <code>paintChildren(Graphics g)</code> method to called <code>preparePaintChildren(g)</code> on your exposable.<br/>
 * You will also have to be able to return the instance of explosable you use,
 * to let users call the explose effect with it using 
 * {@link org.jdesktop.swingx.jexplose.JExplose#explose(Explosable)}.
 * </p>
 * <p>
 * Here is an example of custom JDestopPane subclassing:
 * <pre>
 * public class MyCustomExplosableDesktop extends MyCustomDesktopPane {
 *    private Explosable explosable;
 *    public MyCustomExplosableDesktop() {
 *        explosable = new Explosable(this);
 *    }
 *    public Explosable getExplosable() {
 *        return explosable;
 *    }
 *    public void paint(Graphics g) {
 *        explosable.preparePaint(g);
 *        super.paint(g);
 *        explosable.finishPaint(g);
 *    }
 *    protected void paintChildren(Graphics g) {
 *       explosable.preparePaintChildren(g);
 *       super.paintChildren(g);
 *    }
 * }
 * </pre>
 *  
 * 
 * @see ExplosableDesktop
 * @see JExplose
 * 
 * @author Xavier Hanin
 */
public class Explosable {
    private JDesktopPane _desktop;
    private double _scale = 1.0;
    private AffineTransform _t;
    private List _painters = new ArrayList();
    private transient boolean _doEffect;
    
    /**
     * Public constructor.
     * @param desktop   the JDesktopPane (or subclasse) instance to explose
     */
    public Explosable(JDesktopPane desktop) {
        _desktop = desktop;
    }
    
    /**
     * 
     * @return the JDesktopPane the Explosable is associated with
     */
    public JDesktopPane getDesktop() {
        return _desktop;
    }
    
    void setScale(double scale) {
        _scale = scale;
        getDesktop().repaint();
    }
    
    public void start() {
        _doEffect = true;
    }
    public void end() {
        _doEffect = false;
    }
    
    public void preparePaint(Graphics g) {
        _t = ((Graphics2D)g).getTransform();
        if (_doEffect) {
            ((Graphics2D)g).scale(_scale, _scale);

            // we shift upside of the desktop height, to counter balanced
            // the LayoutExplosablePainter trick ensuring that no iframe
            // has coordinates visible in the non scaled (and non translated)
            // coordinates system of the desktop, preventing it from flicking
            g.translate(0, - _desktop.getHeight());
        }
        
    }
    
    public void finishPaint(Graphics g) {
        ((Graphics2D)g).setTransform(_t);
    }
    
    public void addPainter(Painter painter) {
        painter.prepare(_desktop);
        synchronized(_painters) {
            _painters.add(painter);
        }
        _desktop.repaint();
    }
    
    public void removePainter(Painter painter) {
        synchronized(_painters) {
            if (_painters.remove(painter)) {
                painter.dispose();
            }
        }
        _desktop.repaint();
    }
    
    public void preparePaintChildren(Graphics g) {
        if (_doEffect) {
            List painters;
            synchronized(_painters) {
                painters = new ArrayList(_painters);
            }
            for (ListIterator it = painters.listIterator(); it.hasNext();) {
                Painter painter = (Painter)it.next();
                Graphics2D gcopy = (Graphics2D)g.create();
                gcopy.setTransform(_t);
                painter.paint(gcopy);
            }   
        }
    }

}
