// ============================================================================
// $Id: CircularReferenceException.java 1290 2007-05-01 02:18:21Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

/**
 */
public class CircularReferenceException extends RuntimeException {

    static private final String DEFAULT_MESSAGE = "Circular Reference";
    
    public CircularReferenceException() { super(DEFAULT_MESSAGE); }
    
    public CircularReferenceException(String msg) { super(msg); }
}
