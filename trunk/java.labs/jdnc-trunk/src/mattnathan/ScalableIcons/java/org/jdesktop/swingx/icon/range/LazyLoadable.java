/*
 * $Id: LazyLoadable.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

/**
 * Defines a standard interface for items that can be lazily loaded.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface LazyLoadable {
    /**
     * Returns true if this object is ready to be painted.
     *
     * @return {@code true} if this object is ready to be painted.
     */
    public boolean isReady();





    /**
     * Makes this object ready for painting. If wait is true then the calling
     * thread should be suspended until this ready, otherwise this
     * operation can occur asynchronously. If this object is ready by the time this
     * method returns then true is returned.
     *
     * @param wait {@code true} if this method should return only when this object is ready.
     * @return {@code true} if this object is ready.
     */
    public boolean makeReady(boolean wait);

}
