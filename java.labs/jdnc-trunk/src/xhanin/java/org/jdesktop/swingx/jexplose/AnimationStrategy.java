/*
 * $Id: AnimationStrategy.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import org.jdesktop.swingx.jexplose.core.AnimationUpdate;
import org.jdesktop.swingx.jexplose.core.ExploseLayout;


/**
 * An animation strategy defines a way to animate internal frames
 * from an initial state to an explosed state, and vice versa.
 * Use available implementations to configure the animation used
 * in your JExplose effect.
 * 
 * @see JExplose
 * 
 * @author Xavier Hanin 
 */
public interface AnimationStrategy {

    void explose(ExploseLayout layout, AnimationUpdate update);

    void implose(ExploseLayout layout, AnimationUpdate update);

}
