/*
 * $Id: ExplosableDesktop.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import java.awt.Graphics;

import javax.swing.JDesktopPane;

/**
 * An explosable desktop is a <code>JDesktopPane</code> 
 * ready to be explosed.
 * <p>
 * Use it like any <code>JDesktopPane</code>, and when you want to
 * explose it, call 
 * {@link #explose()} or {@link #explose(JExplose)}.
 * </p>
 * <p>
 * You can also use it with its explosable instance,
 * by calling {@link org.jdesktop.swingx.jexplose.JExplose#explose(Explosable)}
 * with the Explosable returned by {@link #getExplosable()}.<br/>
 * For instance:<br/>
 * <pre>
 * ExplosableDesktop desktop = new ExplosableDesktop();
 * 
 * ...
 *  
 * JExplose.getInstance().explose(desktop.getExplosable());
 * </pre>
 * </p>
 * @see JExplose
 * @see JDesktopPane
 * 
 * @author Xaver Hanin
 */
public class ExplosableDesktop extends JDesktopPane {
    private Explosable _explosable;
    /**
     * Default Constructor.
     */
    public ExplosableDesktop() {
        _explosable = new Explosable(this);
    }
    /**
     * Returns the explosable instance associated with this desktop.
     * @return the explosable instance associated with this desktop.
     */
    public Explosable getExplosable() {
        return _explosable;
    }
    /**
     * Paints this ExplosableDesktop within the specified graphics context.
     *
     * @param g  the Graphics context within which to paint
     */
    public void paint(Graphics g) {
//        if (g.getClipBounds().x == 0) {
//            System.out.println(System.currentTimeMillis() + " - painting... clip bounds="+g.getClipBounds());
//        }
        _explosable.preparePaint(g);
        super.paint(g);
        _explosable.finishPaint(g);
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
     */
    protected void paintChildren(Graphics g) {
        _explosable.preparePaintChildren(g);
        super.paintChildren(g);
    }
    /**
     * Convenience method for explosing this ExplosableDesktop.
     * This is equivalent to:
     * <pre>
     * JExplose.getInstance().explose(desktop.getExplosable());
     * </pre>
     */
    public void explose() {
        JExplose.getInstance().explose(getExplosable());
    }
    /**
     * Convenience method for explosing this ExplosableDesktop
     * with a given <code>JExplose</code> instance.
     * This is equivalent to:
     * <pre>
     * explose.explose(desktop.getExplosable());
     * </pre>
     * @param explose the JExplose instance which should be used
     * to explose this desktop.
     */
    public void explose(JExplose explose) {
        explose.explose(getExplosable());
    }
    
    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
     */
//    protected void paintChildren(Graphics g) {
//        int i = getComponentCount();
//        for (i--; i>= 0; i--) {
//            Component c = getComponent(i);
//            if (c instanceof JComponent) {
//                ((JComponent)c).setOpaque(false);
//            }
//        }
//        super.paintChildren(g);
//        i = getComponentCount();
//        for (i--; i>= 0; i--) {
//            Component c = getComponent(i);
//            if (c instanceof JComponent) {
//                ((JComponent)c).setOpaque(true);
//            }
//        }
//    }
}
