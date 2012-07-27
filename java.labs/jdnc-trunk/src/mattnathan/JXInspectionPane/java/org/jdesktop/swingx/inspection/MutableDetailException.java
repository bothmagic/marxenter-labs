/*
 * $Id: MutableDetailException.java 2771 2008-10-10 09:01:40Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.inspection;

/**
 * Throws when a Details value cannot be set for some reason. This defines the type of the source exception.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MutableDetailException extends Exception {
    /**
     * Defines the type of the failure.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum Type {
        /**
         * The given type for the mutation was incorrect. Usually triggered by a ClassCastException.
         */
        INVALID_TYPE,
        /**
         * Null values are not allowed for the property value.
         */
        NULL_NOT_ALLOWED,
        /**
         * The given value does not match the property constraints.
         */
        ILLEGAL_ARGUMENT,
        /**
         * An unknown exception was thrown.
         */
        CLIENT_ERROR
    }







    private final Type type;

    /**
     * Create a new Exception.
     *
     * @param msg The displayable message.
     * @param type The type of the error.
     * @param cause The cause of the error.
     */
    public MutableDetailException(String msg, Type type, Throwable cause) {
        super(msg, cause);
        if (type == null) {
            throw new NullPointerException("type cannot be null");
        }
        this.type = type;
    }





    /**
     * Get the type of the error.
     *
     * @return The error type.
     */
    public Type getType() {
        return type;
    }
}
