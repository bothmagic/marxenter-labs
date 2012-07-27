/*
 * $Id: AuthenticationModule.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

import java.util.Map;

/**
 * Implementing classes use a <code>Map</code> of credentials to
 * authenticate.  Example implementations could include:<br>
 * <ul>
 * <li>Unix PAM module</li>
 * <li>JDBC module</li>
 * <li>XML Permissions file<li>
 * <li>etc</li>
 * </ul>
 * @author Richard Bair
 */
public interface AuthenticationModule {
	/**
	 * Called by the login dialog (or other object) to authenticate an individual
	 * with the given credentials.  The Map of credentials probably includes
	 * a user name and password, but may also include other "credentials" such
	 * as the name of the server to log into, the database, a second key word,
	 * etc.
	 * @param credentials
	 * @return true if the user's credentials pan out.  False otherwise
	 * @throws AuthenticationException
	 */
	public boolean authenticate(Map credentials) throws AuthenticationException;
}
