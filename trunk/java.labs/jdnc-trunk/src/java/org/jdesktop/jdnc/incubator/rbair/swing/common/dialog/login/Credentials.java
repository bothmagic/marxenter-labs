/*
 * $Id: Credentials.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

/**
 * TODO Finish implementing the typesafe enum pattern.  Switch to real enums when
 * 1.5 comes out.
 * @author Richard Bair
 */
public final class Credentials {
	/**
	 * Key used to save/retrieve the user name from the <code>credentials</code> Map
	 */
	public static final String USER_NAME_KEY = "USER_NAME";
	/**
	 * Key used to save/retrieve the password from the <code>credentials</code> Map
	 */
	public static final String PASSWORD_KEY = "PASSWORD";
	/**
	 * Key used to save/retrieve the server from the <code>credentials</code> Map
	 */
	public static final String SERVER_KEY = "SERVER";
}
