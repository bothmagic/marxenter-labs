/*
 * $Id: Prefs.java 20 2004-09-06 18:35:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.util.prefs.Preferences;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.User;



/**
 * Utility class for jdk1.4 Preferences.
 * <br>
 * This Prefs object provides convenience methods for getting Preference nodes from
 * the backing store.  4 basic nodes are provided:
 * <ul>
 *   <li>user root</li>
 *   <li>app user root</li>
 *   <li>sys root</li>
 *   <li>app sys root</li>
 * </ul>
 * <br>
 * User Root is the root node in the user heiarchy.<br>
 * App User Root is the root application node in the user root heiarchy.  Note that the app node is
 * named according to the settings in the Application object.<br>
 * Sys Root is the root node in the system heiarchy.<br>
 * App Sys Root is the root application node in the sys root heiarchy.  Note that the app node is
 * named according to the settings in the Application object.<br>
 * <br>
 * Sub nodes can be retrieved simply by passing in the path to them.  For instance, calling
 * getNodeFromUserRoot("mynode/myothernode") will retrieve the node found at:
 * application user root/mynode/myothernode.<strong>The actual location of the nodes is transparent
 * to the calling program, and should not be relied upon being in a certain physical location</strong>.
 * Think of all nodes as either being in user or sys space (associated with a particular user, or
 * associated with the system as a whole), and can always be retrieved by the same <b>path</b> in
 * a deterministic manner.
 * @author Richard Bair
 */
public final class Prefs {
	/**
	 * hide the constructor
	 */
	private Prefs() {
	}
	
	/**
	 * Return a <code>Preferences</code> node given a parent node and a path.
	 * For instance, the parent node could be <code>userRoot</code> and the
	 * path could be "/myapp/myversion/windows".
	 * <br>
	 * This method circumvents the rules established for this class in the placement of
	 * particular nodes, but give ultimate flexibility to the calling program in describing
	 * EXACTLY where a node is.
	 * @param parentNode
	 * @param path
	 * @return
	 */
	public static Preferences getNode(Preferences parentNode, String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		Preferences node = parentNode.node(path);
		return node;
	}
	
	/**
	 * Returns a Preferences node for the given object.
	 * <br>
	 * NOTE: this
	 * method is only good for storing class level variables, not for specific instances.
	 * For instance, it would be a mistake to try to store 10 windows' settings (if they
	 * are all of the same class) and expect to get all 10 settings back later.  You may
	 * store 10 windows' settings with this method, but only the last settings saved will
	 * be saved (the previous 9 windows' settings having been overwritten in turn).
	 * @param parentNode the node that is the parent.  Usually this will be either the
	 * system node or the user node.
	 * @param obj
	 * @return
	 */
	public static Preferences getNode(Preferences parentNode, Object obj) {
		String className = obj.getClass().getName();
		className = className.replaceAll("\\.", "\\/");
		return getNode(parentNode, className);
	}
	
	/**
	 * Get a <code>Preferences</code> node for the given object from within userRoot.
	 * These preferences are Application specific, and User specific (the current user
	 * in the application object).  If no user is registered with the Application object, the
	 * the settings can be considered for the null user.
	 * @see getNode(Preferences, Object)
	 * @param obj
	 * @return
	 */
	public static Preferences getNodeFromUserRoot(Object obj) {
		Preferences userAppNode = getUserAppNode();
		return getNode(userAppNode, obj);
	}
	
	/**
	 * Get a <code>Preferences</code> node for the given object from within sysRoot.
	 * These preferences are Application specific.
	 * @see getNode(Preferences, Object)
	 * @param obj
	 * @return
	 */
	public static Preferences getNodeFromSysRoot(Object obj) {
		Preferences appNode = getSysAppNode();
		return getNode(appNode, obj);
	}
	
	/**
	 * Get a <code>Preferences</code> node for the given object from within userRoot.
	 * These preferences are Application specific, and User specific (the current user
	 * in the application object).  If no user is registered with the Application object, the
	 * the settings can be considered for the null user.
	 * @see getNode(Preferences, String)
	 * @param obj
	 * @return
	 */
	public static Preferences getNodeFromUserRoot(String path) {
		Preferences userAppNode = getUserAppNode();
		return getNode(userAppNode, path);
	}
	
	/**
	 * Get a <code>Preferences</code> node for the given object from within sysRoot.
	 * These preferences are Application specific.
	 * @see getNode(Preferences, String)
	 * @param obj
	 * @return
	 */
	public static Preferences getNodeFromSysRoot(String path) {
		Preferences appNode = getSysAppNode();
		return getNode(appNode, path);
	}
	
	/**
	 * Returns the root node for this application <b>and</b> user in user space.
	 * @return
	 */
	public static Preferences getUserAppNode() {
		StringBuffer path = new StringBuffer();
		Application app = Application.getInstance();
		path.append(app.getManufacturer());
		path.append("/");
		path.append(app.getName());
		User user = app.getUser();
		if (user != null) {
			path.append("/");
			path.append(user.toString());
		}
		return getNode(Preferences.userRoot(), path.toString());
	}

	/**
	 * Returns the root node for this application in sys space.
	 * @return
	 */
	public static Preferences getSysAppNode() {
		StringBuffer path = new StringBuffer();
		path.append(Application.getInstance().getManufacturer());
		path.append("/");
		path.append(Application.getInstance().getName());
		return getNode(Preferences.systemRoot(), path.toString());
	}
	
}
