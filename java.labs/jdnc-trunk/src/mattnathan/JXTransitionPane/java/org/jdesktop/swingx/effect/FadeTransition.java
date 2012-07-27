/*
 * $Id: FadeTransition.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.beans.AbstractBean;

import java.awt.*;

/**
 * Generated comment for FadeTransition.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class FadeTransition<P, T> extends AbstractBean implements TransitionEffect<P, T>{
    public void paintTransition(Graphics g, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        TranslucentGraphicsEffect<T> effect = new TranslucentGraphicsEffect<T>();
        if (from != null) {
            effect.setAlpha(1 - bias);
            effect.paintEffect(g, from);
        }
        if (to != null) {
            effect.setAlpha(bias);
            effect.paintEffect(g, to);
        }
    }





    public boolean transform(Rectangle area, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        return bias > 0 && bias < 1;
    }
}
