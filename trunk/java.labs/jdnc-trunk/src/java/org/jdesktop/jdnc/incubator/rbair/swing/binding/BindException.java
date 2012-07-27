/*
 * $Id: BindException.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

/**
 * Thrown when a binding could not be established between a user-interface
 * component and a data model.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class BindException extends Exception {

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     */
    public BindException(Object dataModel) {
        this("could not bind to "+dataModel.getClass().getName());
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param cause the specific throwable which caused the bind failure
     */
    public BindException(Object dataModel, Throwable cause) {
        this("could not bind to "+dataModel.getClass().getName(), cause);
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param fieldName string containing the name of the field or element
     *        within the data model
     */
    public BindException(Object dataModel, String fieldName) {
        this("could not bind to field"+fieldName+" on "+dataModel.getClass().getName());
    }

    /**
     * Instantiates a bind exception.
     * @param dataModel data model object which could not be bound
     * @param fieldName string containing the name of the field or element
     *        within the data model
     * @param cause the specific throwable which caused the bind failure
     */
    public BindException(Object dataModel, String fieldName, Throwable cause) {
        this("could not bind to field"+fieldName+" on "+dataModel.getClass().getName(),
             cause);
    }


    /**
     * Instantiates bind exception.
     * @param message String containing description of why exception occurred
     */
    public BindException(String message) {
        super(message);
    }

    /**
     * Instantiates bind exception.
     * @param message String containing description of why exception occurred
     * @param cause the specific throwable which caused bind failure
     */
    public BindException(String message, Throwable cause) {
        super(message, cause);
    }

}
