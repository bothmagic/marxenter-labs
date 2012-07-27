/*
 * $Id: DynamicObject.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import javax.swing.event.ChangeListener;

/**
 * All objects implementing this interface are specifying that their content
 * is likely to update internally.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 1.0
 */
public interface DynamicObject {
    /**
     * Add a listener for notification of changes to this object. This usually
     * requires a repaint.
     *
     * @param l ChangeListener
     */
    public void addChangeListener(ChangeListener l);





    /**
     * Remove a listener added through the addChangeListener method.
     *
     * @param l ChangeListener
     */
    public void removeChangeListener(ChangeListener l);





    /**
     * Get all listeners added to this object.
     *
     * @return ChangeListener[]
     */
    public ChangeListener[] getChangeListeners();
}
