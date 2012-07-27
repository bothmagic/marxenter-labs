/*
 * $Id: Details.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

/**
 * Defines a description of some source. This provides a number of global values which summarise the details, values
 * like TITLE and ICON. There is no guarantee that a given Details implementation will support all global values and
 * should return {@code null} if that value is not supported. Along with global values a Details object supports a
 * number of indexed values which give more information about the source. Neither label nor value may be null for any
 * index in the range {@code 0 <= x < getDetaulsCount()}.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see DetailInspector
 */
public interface Details {
    /**
     * The global title to represent these Details. Values got using this key should be of type {@link String}.
     *
     * @see #getValue(String)
     */
    public static final String TITLE = "Title";
    /**
     * An Icon to use to represent these Details. Values got using this key should be of type {@link javax.swing.Icon}.
     *
     * @see #getValue(String)
     */
    public static final String ICON = "Icon";
    /**
     * A short readable description of these Details, should be no longer than one line and is suitable as a sub-title.
     * Values got using this key should be of type {@link String}.
     *
     * @see #getValue(String)
     */
    public static final String SHORT_DESCRIPTION = "Short Description";
    /**
     * A long readable description of these details, this may be an arbitrary length. Values got using this key should
     * be of type {@link String}.
     *
     * @see #getValue(String)
     */
    public static final String LONG_DESCRIPTION = "Long Description";

    /**
     * Gets a global value for these Details. This may return null for any given key and may support more keys than
     * those provided by this interface. The key cannot be null.
     *
     * @param key The key for the global detail to get.
     * @return A value representing the given key.
     */
    public Object getValue(String key);





    /**
     * Get the number of Detail parts these Details have. This should be a number >= 0.
     *
     * @return The number of details this object has.
     */
    public int getDetailCount();





    /**
     * Get the string to use as a label for the details part at the given index. This will not return null.
     *
     * @param index The index for the detail part.
     * @return The label for the detail part.
     * @throws IndexOutOfBoundsException if the given index is not {@code >= 0} and {@code < getDetailCount()}.
     */
    public String getLabelAt(int index);





    /**
     * Get the value of the detail part for the given index. This should not return null.
     *
     * @param index The index for the detail part.
     * @return The value for the detail part.
     * @throws IndexOutOfBoundsException if the given index is not {@code >= 0} and {@code < getDetailCount()}.
     */
    public Object getValueAt(int index);
}
