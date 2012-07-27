/*
 * $Id: DetailInspector.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

/**
 * Defines a simple factory that inspects a collection of objects to create details about those objects.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface DetailInspector {
    /**
     * Inspect the given items to create a set of details for those items. The returned Details object may be null and
     * in this case the meaning is that the given items cannot be inspected. The given items may be an empty collection
     * but will not be null.
     *
     * @param items The items to inspect.
     * @return The details found for the items.
     */
    public Details inspect(Object ...items);
}
