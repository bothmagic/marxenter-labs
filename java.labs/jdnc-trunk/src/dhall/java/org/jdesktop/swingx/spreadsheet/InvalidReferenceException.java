// ============================================================================
// $Id: InvalidReferenceException.java 1290 2007-05-01 02:18:21Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

/**
 */
public class InvalidReferenceException extends RuntimeException {
    static private final String DEFAULT_MESSAGE = "Invalid Reference";
    
    public InvalidReferenceException() { super(DEFAULT_MESSAGE); }
    
    public InvalidReferenceException(String msg) { super(msg); }
}
