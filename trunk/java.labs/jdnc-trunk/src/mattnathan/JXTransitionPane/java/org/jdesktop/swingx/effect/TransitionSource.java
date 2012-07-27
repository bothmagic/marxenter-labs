/*
 * $Id: TransitionSource.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

/**
 * Defines an extended effect source which represents a contained source.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @param <P> the container type for this transition source
 * @param <T> the source type for the transition.
 */
public interface TransitionSource<P, T> extends EffectSource<T> {
    /**
     * Gets the container for the transition.
     *
     * @return The container the source is part of.
     */
    public P getContainer();
}
