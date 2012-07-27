/*
 * $Id: MutableDetails.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

/**
 * Extension to the Details interface which allows mutation of the source items. Only items at indices which return true
 * for isEditableAt(index) will be editable, calling setValueAt(int, Object) with an uneditable index should result in
 * an IllegalArgumentException being thrown. Null values for {@code value} in setValueAt are allowed as specified by the
 * underlying source of the details and may or may not be supported.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface MutableDetails extends Details {
    /**
     * Set the value for the given index. This should modify the source of these details to set the corresponding
     * property to value.
     *
     * @param index The index of the value to set.
     * @param value The value to set the source property to.
     * @throws MutableDetailException if the given value cannot be set on the source.
     */
    public void setValueAt(int index, Object value) throws MutableDetailException;





    /**
     * returns whether the detail item at the given index is editable.
     *
     * @param index The index of the detail item.
     * @return {@code true} if the detail item is editable.
     */
    public boolean isEditableAt(int index);

}
