/*
 * $Id: DefaultUser.java 21 2004-09-06 18:37:36Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

/**
 * Represents the User logged in to the Application object.<br>
 * This class does not keep passwords.  Does anybody want it to?  I'm a little afraid of the
 * security implications of that.
 * @author Richard Bair
 */
public class DefaultUser implements User {
	/**
	 * User id.  Application specific meaning.  This class ensures that this is never null.
	 */
	private String id = "";
	/**
	 * User name.  Application specific meaning.  May be the same as id, or it may have no value
	 * whatsoever.  This class ensures that this is never null.
	 */
	private String name = "";
	
	/**
	 * Create a new user with no id or name
	 */
	public DefaultUser() {
		this("", "");
	}
	
	/**
	 * Create a new user with the given name
	 * @param name
	 */
	public DefaultUser(String name) {
		this("", name);
	}
	
	/**
	 * Create a new user with the given id and name
	 * @param id
	 * @param name
	 */
	public DefaultUser(String id, String name) {
		setId(id);
		setName(name);
	}
	
	/**
	 * Returns the name, if there is one, or the id if there isn't.
	 */
	public String toString() {
		return name.equals("") ? id : name;
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the name, if there is one, or an empty string.
	 * @param string
	 */
	public void setName(String name) {
		this.name = (name == null ? "" : name);
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the ID, if there is one, or an empty string.
	 * @param string
	 */
	public void setId(String id) {
		this.id = (id == null ? "" : id);
	}

}
