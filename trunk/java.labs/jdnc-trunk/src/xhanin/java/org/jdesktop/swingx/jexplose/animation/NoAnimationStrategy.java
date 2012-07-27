/*
 * $Id: NoAnimationStrategy.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.animation;

import org.jdesktop.swingx.jexplose.AnimationStrategy;
import org.jdesktop.swingx.jexplose.core.AnimationUpdate;
import org.jdesktop.swingx.jexplose.core.ExploseLayout;

/**
 * This particular animation strategy does no animation at all.
 * It is useful on small configurations, not able to draw animation
 * quickly.
 * 
 * @see org.jdesktop.swingx.jexplose.JExplose
 * 
 * @author Xavier Hanin
 */
public class NoAnimationStrategy implements AnimationStrategy {
    public void explose(ExploseLayout layout, AnimationUpdate update) {
        update.end();
    }
    public void implose(ExploseLayout layout, AnimationUpdate update) {
        update.end();
    }
}
