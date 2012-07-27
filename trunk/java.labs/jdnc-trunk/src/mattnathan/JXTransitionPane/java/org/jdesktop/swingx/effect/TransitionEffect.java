/*
 * $Id: TransitionEffect.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.beans.PropertySupporter;

import java.awt.*;

/**
 * Generated comment for TransitionEffect.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface TransitionEffect<P, T> extends PropertySupporter {
    public void paintTransition(Graphics g, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias);





    public boolean transform(Rectangle area, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias);
}
