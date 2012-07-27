/*
 * $Id: AuthenticationListener.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

import java.util.Map;

/**
 * A listener that is notified of a successful or an unsuccessful authentication
 * attempt.
 * @author Richard Bair
 */
public interface AuthenticationListener {
	/**
	 * Called if all parts of the authentication process were successful.
	 * @param credentials
	 */
	public void successful(Map credentials);
	
	/**
	 * Called if anything happened making the authentication process unsuccessful.
	 * @param credentials
	 * @param cause Some authentications may fail due to an exception in the
	 * authentication process.  This param may be null, or it may contain the
	 * "cause" exception of bad authentication.
	 */
	public void failure(Map credentials, Throwable cause);
}
