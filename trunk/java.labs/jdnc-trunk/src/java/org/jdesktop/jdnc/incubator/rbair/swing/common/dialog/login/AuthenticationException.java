/*
 * $Id: AuthenticationException.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

/**
 * Represents an exception that has occured during authentication.
 * @author Richard Bair
 */
public final class AuthenticationException extends Exception {
	/**
	 * Create an AuthenticationException
	 * @param msg
	 */
	public AuthenticationException(String msg) {
		super(msg);
	}
	
	/**
	 * Create an AuthenticationException
	 * @param msg
	 * @param cause
	 */
	public AuthenticationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Create an AuthenticationException
	 * @param cause
	 */
	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}
