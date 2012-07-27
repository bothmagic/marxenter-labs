/*
 * $Id: WindowUtils.java 20 2004-09-06 18:35:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.util.prefs.Preferences;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.RootPaneContainer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Encapsulates various utilities for windows (ie: <code>Frame</code> and
 * <code>Dialog</code> objects and descendants, in particular).
 * @author Richard Bair
 */
public final class WindowUtils {
	
	/**
	 * Hide the constructor - don't wan't anybody creating an instance of this
	 */
	private WindowUtils() {
	}
	
	/**
	 * <p>
	 * Returns the <code>Point</code> at which a window should be placed to
	 * center that window on the screen.
	 * </p>
	 * <p>
	 * Some thought was taken as to whether to implement a method such as this,
	 * or to simply make a method that, given a window, will center it.  It was
	 * decided that it is better to not alter an object within a method.
	 * </p>
	 * @param window The window to calculate the center point for.  This object
	 * can not be null.
	 * @return the <code>Point</code> at which the window should be placed to
	 * center that window on the screen.
	 */
	public static Point getPointForCentering(Window window) {
		assert window != null;
		int width = window.getWidth();
		int height = window.getHeight();
		Dimension screenSize = window.getToolkit().getScreenSize(); 
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		Point p = new Point((screenWidth - width) / 2, (screenHeight - height) / 2);
		return p;
	}
	
	/**
	 * <p>
	 * Returns the <code>Point</code> at which a window should be placed to
	 * center that window on the given desktop.
	 * </p>
	 * <p>
	 * Some thought was taken as to whether to implement a method such as this,
	 * or to simply make a method that, given a window, will center it.  It was
	 * decided that it is better to not alter an object within a method.
	 * </p>
	 * @param window The window (JInternalFrame) to calculate the center point
	 * for.  This object can not be null.
	 * @param desktop The JDesktopPane that houses this window.
	 * @return the <code>Point</code> at which the window should be placed to
	 * center that window on the given desktop
	 */
	public static Point getPointForCentering(JInternalFrame window, JDesktopPane desktop) {
		assert window != null;
		int width = window.getWidth();
		int height = window.getHeight();
		Dimension screenSize = desktop.getSize(); 
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		Point p = new Point((screenWidth - width) / 2, (screenHeight - height) / 2);
		return p;
	}

	/**
	 * Utility method used to load a GridBagConstraints object (param gbc) with the
	 * data in the other parameters.  This method saves code space over doing the
	 * assignments by hand, and also allows you to reuse the same GridBagConstraints
	 * object reducing temporary object creating (at the expense of a method call.
	 * Go figure).
	 */
	public static void setConstraints(GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight,
	 double weightx, double weighty, int anchor, int fill, int top, int left, int bottom, int right) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbc.insets = new Insets(top, left, bottom, right);
	}
	
	/**
	 * Get a <code>Spatial</code> object representing the given window's position and
	 * magnitude in space.
	 * @param win The window to get a Spatial object for
	 * @return a Spatial object.  @see com.jgui.Spatial
	 */
	public static Spatial getSpatial(Window win) {
		Spatial spatial = new Spatial(win.getY(), win.getX(), win.getWidth(), win.getHeight());
		return spatial;
	}

	/**
	 * Saves the spatial layout of a window to preferences
	 * @param win
	 */
	public static void saveSpatialToPreferences(Window win) {
		Spatial spatial = WindowUtils.getSpatial(win);
		Preferences node = Prefs.getNodeFromUserRoot(win);
		node.putInt("top", spatial.getTop());
		node.putInt("left", spatial.getLeft());
		node.putInt("width", spatial.getWidth());
		node.putInt("height", spatial.getHeight());
	}
	
	/**
	 * Retrieves the spatial layout of a window from preferences, or a default
	 * spatial if none existed in preferences.
	 * @param win
	 * @return
	 */
	public static Spatial getSpatialFromPreferences(Window win) {
		return getSpatialFromPreferences(win, 0, 0, win.getToolkit().getScreenSize().width, win.getToolkit().getScreenSize().height);
	}

	/**
	 * Retrieves the spatial layout of a window from preferences, or a default
	 * spatial if none existed in preferences.  The other integer parameters are the
	 * default values to use if no spatial exists in preferences.
	 * @param win
	 * @param top
	 * @param left
	 * @param width
	 * @param height
	 * @return
	 */
	public static Spatial getSpatialFromPreferences(Window win, int top, int left, int width, int height) {
		Preferences node = Prefs.getNodeFromUserRoot(win);
		int t = node.getInt("top", top);
		int l = node.getInt("left", left);
		int w = node.getInt("width", width);
		int h = node.getInt("height", height);
		return new Spatial(t, l, w, h);
	}

	/**
	 * Saves the spatial layout of a window to preferences
	 * @param win
	 */
	public static void saveSpatialToPreferences(JInternalFrame win) {
		Spatial spatial = WindowUtils.getSpatial(win);
		Preferences node = Prefs.getNodeFromUserRoot(win);
		node.putInt("top", spatial.getTop());
		node.putInt("left", spatial.getLeft());
		node.putInt("width", spatial.getWidth());
		node.putInt("height", spatial.getHeight());
	}
	
	/**
	 * Retrieves the spatial layout of a window from preferences, or a default
	 * spatial if none existed in preferences.
	 * @param win
	 * @return
	 */
	public static Spatial getSpatialFromPreferences(JInternalFrame win) {
		return getSpatialFromPreferences(win, 0, 0, win.getToolkit().getScreenSize().width, win.getToolkit().getScreenSize().height);
	}

	/**
	 * Retrieves the spatial layout of a window from preferences, or a default
	 * spatial if none existed in preferences.
	 * @param win
	 * @return
	 */
	public static Spatial getSpatialFromPreferences(JInternalFrame win, int defTop, int defLeft, int defWidth, int defHeight) {
		Preferences node = Prefs.getNodeFromUserRoot(win);
		int top = node.getInt("top", defTop);
		int left = node.getInt("left", defLeft);
		int width = node.getInt("width", defWidth);
		int height = node.getInt("height", defHeight);
		return new Spatial(top, left, width, height);
	}

	/**
	 * Get a <code>Spatial</code> object representing the given JComponent's position and
	 * magnitude in space.
	 * @param comp The JComponent to get a Spatial object for
	 * @return a Spatial object.  @see com.jgui.Spatial
	 */
	public static Spatial getSpatial(JComponent comp) {
		Spatial spatial = new Spatial(comp.getY(), comp.getX(), comp.getWidth(), comp.getHeight());
		return spatial;
	}

	/**
	 * Saves the spatial layout of a JComponent to preferences
	 * @param win
	 */
	public static void saveSpatialToPreferences(JComponent comp, String name) {
		Spatial spatial = WindowUtils.getSpatial(comp);
		Preferences node = Prefs.getNodeFromUserRoot(comp.getClass().getName() + "." + name);
		node.putInt("top", spatial.getTop());
		node.putInt("left", spatial.getLeft());
		node.putInt("width", spatial.getWidth());
		node.putInt("height", spatial.getHeight());
	}
	
	/**
	 * Retrieves the spatial layout of a JComponent from preferences, or a default
	 * spatial consisting of the preferred size and current x/y of the component
	 * if none existed in preferences.
	 * @param win
	 * @return
	 */
	public static Spatial getSpatialFromPreferences(JComponent comp, String name) {
		Preferences node = Prefs.getNodeFromUserRoot(comp.getClass().getName() + "." + name);
		Dimension pref = comp.getPreferredSize();
		int top = node.getInt("top", comp.getY());
		int left = node.getInt("left", comp.getX());
		int width = node.getInt("width", pref.width);
		int height = node.getInt("height", pref.height);
		return new Spatial(top, left, width, height);
	}
	
	/**
	 * Saves the divider location for a JSplitPane to preferences.  Name can (but doesn't
	 * have to be) the name of the variable itself.  This is the recommended approach for
	 * situations that I can forsee.
	 * @param sp
	 * @param name
	 */
	public static void saveDividerLocationToPreferences(JSplitPane sp, String name) {
		Preferences node = Prefs.getNodeFromUserRoot("javax.swing.JSplitPane." + name);
		node.putInt("dividerLocation", sp.getDividerLocation());
	}
	
	/**
	 * Returns the divider location for the given JSplitPane with the given name from
	 * preferences.  If the item is not in the preferences backing store, then the
	 * current divider location is returned instead.
	 * @param sp
	 * @param name
	 * @return
	 */
	public static int getDividerLocationFromPreferences(JSplitPane sp, String name) {
		Preferences node = Prefs.getNodeFromUserRoot("javax.swing.JSplitPane." + name);
		return node.getInt("dividerLocation", sp.getDividerLocation());
	}
	
	/**
	 * TODO !!
	 * Saves the divider location for a JMasterDetail to preferences.  Name can (but doesn't
	 * have to be) the name of the variable itself.  This is the recommended approach for
	 * situations that I can forsee.
	 * @param md
	 * @param name
	 */
//	public static void saveDividerLocationToPreferences(JMasterDetail md, String name) {
//		Preferences node = Prefs.getNodeFromUserRoot("com.jgui.swing.JMasterDetail." + name);
//		node.putInt("dividerLocation", md.getDividerLocation());
//	}
	
	/**
	 * TODO !!
	 * Returns the divider location for the given JMasterDetail with the given name from
	 * preferences.  If the item is not in the preferences backing store, then the
	 * current divider location is returned instead.
	 * @param md
	 * @param name
	 * @return
	 */
//	public static int getDividerLocationFromPreferences(JMasterDetail md, String name) {
//		Preferences node = Prefs.getNodeFromUserRoot("com.jgui.swing.JMasterDetail." + name);
//		return node.getInt("dividerLocation", md.getDividerLocation());
//	}
	
	/**
	 * Saves the column layout (column widths, ordering) for the given table to preferences.
	 * The name parameter tells this method where to save the preferences.
	 * @param table
	 * @param name
	 */
	public static void saveTableLayoutToPreferences(JTable table, String name) {
		saveTableLayoutToPreferences(table.getColumnModel(), name);
	}
	
	public static void saveTableLayoutToPreferences(TableColumnModel cm, String name) {
		Preferences pnode = Prefs.getNodeFromUserRoot("table/layout/" + name);
		for (int i=0; i<cm.getColumnCount(); i++) {
			TableColumn col = cm.getColumn(i);
			Preferences node = Prefs.getNode(pnode, col.getHeaderValue().toString());
			int modelIndex = i;
			int width = col.getWidth();
			node.putInt("modelIndex", modelIndex);
			node.putInt("width", width);
		}
	}

	/**
	 * Restores the layout for the given JTable from what has been saved in the preferences.
	 * Note, that this method differs from the others in that it changes/mutates the table, rather
	 * than letting the table make its own changes from a returned set of values.  This was do to the
	 * complexity of the situation.
	 * @param table
	 * @param name
	 */
	public static void restoreTableLayoutFromPreferences(JTable table, String name) {
		restoreTableLayoutFromPreferences(table.getColumnModel(), name);
	}

	public static void restoreTableLayoutFromPreferences(TableColumnModel cm, String name) {
		Preferences pnode = Prefs.getNodeFromUserRoot("table/layout/" + name);
		Object[] order = new Object[cm.getColumnCount()];
		for (int i=0; i<cm.getColumnCount(); i++) {
			TableColumn col = cm.getColumn(i);
			Preferences node = Prefs.getNode(pnode, col.getHeaderValue().toString());
			int modelIndex = node.getInt("modelIndex", -1);
			if (modelIndex != -1) {
				order[modelIndex] = col.getIdentifier();
			} else {
				order[i] = col.getIdentifier();
			}
			int width = node.getInt("width", -1);
			if (width != -1) {
				col.setWidth(width);
				col.setPreferredWidth(width);
			}
		}
		
		//now, arrange the columns according to their order
		for (int i=0; i<order.length; i++) {
			if (order[i] != null) {
				int currentIndex = cm.getColumnIndex(order[i]);
				cm.moveColumn(currentIndex, i);
			}
		}
	}
	
	/**
	 * Locates the RootPaneContainer for the given component
	 * @param c
	 * @return
	 */
	public static RootPaneContainer findRootPaneContainer(Component c) {
		if (c == null) {
			return null;
		} else if (c instanceof RootPaneContainer) {
			return (RootPaneContainer)c;
		} else {
			return findRootPaneContainer(c.getParent());
		}
	}

	/**
	 * Locates the JFrame for the given component
	 * @param c
	 * @return
	 */
	public static JFrame findJFrame(Component c) {
		if (c == null) {
			return null;
		} else if (c instanceof RootPaneContainer) {
			return (JFrame)c;
		} else {
			return findJFrame(c.getParent());
		}
	}

	/**
	 * Locates the JDialog for the given component
	 * @param c
	 * @return
	 */
	public static JDialog findJDialog(Component c) {
		if (c == null) {
			return null;
		} else if (c instanceof JDialog) {
			return (JDialog)c;
		} else {
			return findJDialog(c.getParent());
		}
	}
}
