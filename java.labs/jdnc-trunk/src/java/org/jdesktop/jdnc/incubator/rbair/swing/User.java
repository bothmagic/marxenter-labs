/*
 * $Id: User.java 21 2004-09-06 18:37:36Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

/**
 * Represents the User logged in to the Application object.<br>
 * This interface does not support the keeping of passwords.
 * Does anybody want it to?  I'm a little afraid of the
 * security implications of that.
 * @author Richard Bair
 */
public interface User {
	/**
	 * Returns the name of the User.
	 * @return
	 */
	public String getName();

	/**
	 * Returns the ID of the User
	 * @return
	 */
	public String getId();

}
