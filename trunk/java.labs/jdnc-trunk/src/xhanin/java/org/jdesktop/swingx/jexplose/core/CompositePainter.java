/*
 * $Id: CompositePainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * For internal use only
 */
public class CompositePainter implements Painter {
    private Painter _painterA;
    private Painter _painterB;

    /**
     * @param painterA
     * @param painterB
     */
    public CompositePainter(Painter painterA, Painter painterB) {
        _painterA = painterA;
        _painterB = painterB;
    }
    /* (non-Javadoc)
     * @see org.jdesktop.swingx.Painter#prepare(org.jdesktop.swingx.EffectPanel)
     */
    public void prepare(JComponent comp) {
        _painterA.prepare(comp);
        _painterB.prepare(comp);        
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swingx.Painter#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        _painterA.paint(g);
        _painterB.paint(g);        
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swingx.Painter#dispose()
     */
    public void dispose() {
        _painterA.dispose();
        _painterB.dispose();
    }

}
