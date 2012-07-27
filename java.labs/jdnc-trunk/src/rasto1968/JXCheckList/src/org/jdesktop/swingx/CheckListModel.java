/*
 * $Id: CheckListModel.java 1115 2007-03-20 23:21:10Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.ListModel;

/**
 * Describes the minimum that must be provided in order for a model to be used
 * with a JXCheckList.
 *
 * @author Rob Stone
 */
public interface CheckListModel extends ListModel {
    /**
     * Indicates whether or not an element in the list is checked.
     * @param index the element to check
     * @return <code>true</code>=checked, <code>false</code>=not checked
     */
    boolean isChecked(final int index);

    /**
     * @return an array of booleans indicating which elements are checked.
     */
    boolean[] getChecked();

    /**
     * Sets the checked state of the specified list elements.
     * @param start the first element to set
     * @param end the last element to set
     * @param checked <code>true</code>=checked, <code>false</code>=not checked
     */
    public void setChecked(final int start, final int end, final boolean checked);

    /**
     * Add a listener
     * @param listener the listener to add
     */
    public void addCheckListListener(final CheckListListener listener);

    /**
     * Remove a listener
     * @param listener the listener to remove
     */
    public void removeCheckListListener(final CheckListListener listener);
}
