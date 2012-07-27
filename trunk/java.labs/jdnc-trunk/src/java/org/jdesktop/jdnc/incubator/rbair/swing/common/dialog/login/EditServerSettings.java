/*
 * $Id: EditServerSettings.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

/**
 * Represents an object capable of editing server settings.  Examples might include a simple
 * dialog that allows one to set the URL of the server they are trying to connect to.
 * @author Richard Bair
 */
public interface EditServerSettings {
	/**
	 * Prompts the user for settings for the server they would like to connect to.
	 * @param currentServer The currently selected server in the Login dialog
	 * @return A Server object representing the server the user would like to log in to.
	 */
	public Server prompt(Server currentServer);
}
