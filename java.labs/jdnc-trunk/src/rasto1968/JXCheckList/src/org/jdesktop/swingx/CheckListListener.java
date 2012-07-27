/*
 * $Id: CheckListListener.java 1009 2007-01-09 21:07:18Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.util.EventListener;

/**
 * An interface via which changes in the check states of the list model can
 * be returned.
 *
 * @author Rob Stone
 */
public interface CheckListListener extends EventListener
{
    /**
     * Called when one of the checkboxes has changed state.
     * @param list the list
     * @param start the index of the first checkbox that has changed
     * @param end the index of the last checkbox that has changed
     */
    void checkListChanged(final JXCheckList list, final int start, final int end);
}
