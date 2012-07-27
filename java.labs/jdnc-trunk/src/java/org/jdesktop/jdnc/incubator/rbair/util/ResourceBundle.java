/*
 * $Id: ResourceBundle.java 102 2004-10-06 19:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.ImageIcon;

//import org.apache.log4j.Logger;

/**
 * Wraps a java.util.ResourceBundle so that Exceptions are not thrown if a
 * string resource does not exist.  Also provides a useful getString method
 * that returns a default if the key was not found or the bundle did not
 * exist.<br>
 * This ResourceBundle also provides a mechanism for retrieving files and images
 * as resources.  All of these resources are designed to work whether the files
 * are on the filesystem, or within a jar.
 * <p>
 * TODO Change error/warning handling when a proper logging facility is decided
 * upon. The problem is java's default logging mechanism doesn't interoperate
 * (I don't think) with log4j, and many if not most projects using logging use
 * log4j, and we don't want to force the use of two different logging backends.
 * We may use jakarta commons logging to overcome the difference.
 * @author Richard Bair
 */
public class ResourceBundle {
	/**
	 * Logger for logging errors, warnings, debug statements, etc.
	 */
//	private static final Logger LOG = Logger.getLogger(ResourceBundle.class);
	/**
	 * java.util.ResourceBundle that THIS ResourceBundle wraps.  The model for
	 * this object.
	 */
	private java.util.ResourceBundle bundle = null;
	/**
	 * Cache of loaded icons and images.
	 */
	private Map iconCache = new HashMap();
	
	/**
	 * Create a new ResourceBundle.  This method is private because only
	 * this class should create instances of this class via factory methods.
	 * @param bundle The java.util.ResourceBundle to wrap with this new instance.
	 */
	private ResourceBundle(java.util.ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	/**
	 * Return a <code>String</code>.  This value returned with either be:<br>
	 * <ol>
	 * <li>a translated value that was looked up in the resource file via the <code>key</code></li>
	 * <li>the default value <code>def</code> if either the resource bundle
	 * could not be found or if the key could not be found in the resource file.</li>
	 * </ol>
	 * @param key Key of the resource in the resource file
	 * @param def default value should the resource be not found or the file unavailable
	 * @return the resourced string, or the default value
	 */
	public String getString(String key, String def) {
		try {
			String value = bundle.getString(key);
			return value;
		} catch (Exception e) {
			return def;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.ResourceBundle#getString(String)
	 */
	public String getString(String key) {
		try {
			return bundle.getString(key);
		} catch (Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.util.ResourceBundle#getBundle(String)
	 */
	public static ResourceBundle getBundle(String baseName) {
		try {
			java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle(baseName);
			return new ResourceBundle(bundle);
		} catch (MissingResourceException mre) {
//			LOG.warn("failed to find bundle " + baseName, mre);
			System.out.println("failed to find bundle " + baseName);
			mre.printStackTrace();
			return new ResourceBundle(null);
		}
	}

	/**
	 * Gets a single char from the resource bundle, or returns the default if the key was not found.
	 * @param key
	 * @param def
	 * @return
	 */
	public char getChar(String key, char def) {
		String s = getString(key);
		return s == null || s.length() == 0 ? def : s.charAt(0);
	}

	/**
	 * Gets a single char from the resource bundle, or returns null (char '\0') if the key was not found.
	 * @param key
	 * @param def
	 * @return
	 */
	public char getChar(String key) {
		return getChar(key, '\0');
	}
	
//	/**
//	 * Retrieves the given image.  There is no assumption made as to where the images are located (that
//	 * is, I don't assume they are in the same directory as the ResourceBundle).
//	 * @param iconName
//	 * @return
//	 */
//	public Icon getIcon(String iconName) {
//		//try to get the icon from cache
//		if (iconCache.containsKey(iconName)) {
//			return (Icon)iconCache.get(iconName);
//		}
//		
//		//couldn't get the icon from cache, so try to load it up.
//		try {
//			URL url = this.getClass().getClassLoader().getResource(iconName);
//			Icon icon = new ImageIcon(url);
//			//add the icon to the cache
//			iconCache.put(iconName, icon);
//			//return it
//			return icon;
//		} catch (Exception e) {
//			LOG.error("Failed to get Icon for name '" + iconName + "'", e);
//			return null;
//		}
//	}
	
	/**
	 * Private function that does the work of loading the images from disk.
	 */
	private ImageIcon getIcon(String iconName, String size, boolean shadow) {
		//construct the path to the icon
		String path = "images/";
		if (size == null) {
			//this is a real ico icon, so...
			path = path + "ico-files/" + iconName;
		} else {
			//this is really an image, so look in the proper size directory
			path = path + size + "/" + (shadow ? "shadow/" : "plain/") + iconName;
		}
		
		//try to get the icon from cache
		if (iconCache.containsKey(path)) {
			return (ImageIcon)iconCache.get(path);
		}
		
		//couldn't get the icon from cache, so try to load it up.
		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			ImageIcon icon = new ImageIcon(url);
			//add the icon to the cache
			iconCache.put(path, icon);
			//return it
			return icon;
		} catch (Exception e) {
//			LOG.error("Failed to get " + (size == null ? "" : size) + " Icon with path '" + path + "'", e);
			System.out.println("Failed to get " + (size == null ? "" : size) + " Icon with path '" + path + "'");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves a 16x16 pixel icon.  The image must be located in
	 * the images/16x16/plain directory if shadow is false, or else
	 * the images/16x16/shadow directory if shadow is true.
	 * @param iconName
	 * @param shadow
	 * @return
	 */
	public ImageIcon get16x16Icon(String iconName, boolean shadow) {
		return getIcon(iconName, "16x16", shadow);
	}
	
	/**
	 * 
	 * @param iconName
	 * @return
	 */
	public ImageIcon get16x16Icon(String iconName) {
		return getIcon(iconName, "16x16", false);
	}
	
	/**
	 * Retrieves a 24x24 pixel icon.  The image must be located in
	 * the images/24x24/plain directory if shadow is false, or else
	 * the images/24x24/shadow directory if shadow is true.
	 * @param iconName
	 * @param shadow
	 * @return
	 */
	public ImageIcon get24x24Icon(String iconName, boolean shadow) {
		return getIcon(iconName, "24x24", shadow);
	}
	
	/**
	 * 
	 * @param iconName
	 * @return
	 */
	public ImageIcon get24x24Icon(String iconName) {
		return getIcon(iconName, "24x24", false);
	}

	/**
	 * Retrieves a 32x32 pixel icon.  The image must be located in
	 * the images/32x32/plain directory if shadow is false, or else
	 * the images/32x32/shadow directory if shadow is true.
	 * @param iconName
	 * @param shadow
	 * @return
	 */
	public ImageIcon get32x32Icon(String iconName, boolean shadow) {
		return getIcon(iconName, "32x32", shadow);
	}
	
	/**
	 * 
	 * @param iconName
	 * @return
	 */
	public ImageIcon get32x32Icon(String iconName) {
		return getIcon(iconName, "32x32", false);
	}
	
	/**
	 * Retrieves a 48x48 pixel icon.  The image must be located in
	 * the images/48x48/plain directory if shadow is false, or else
	 * the images/48x48/shadow directory if shadow is true.
	 * @param iconName
	 * @param shadow
	 * @return
	 */
	public ImageIcon get48x48Icon(String iconName, boolean shadow) {
		return getIcon(iconName, "48x48", shadow);
	}

	/**
	 * 
	 * @param iconName
	 * @return
	 */
	public ImageIcon get48x48Icon(String iconName) {
		return getIcon(iconName, "48x48", false);
	}
	
	/**
	 * Retrieves an icon.  The icon (.ico file) must be located in
	 * the images/ico-files directory
	 * @param iconName
	 * @return
	 */
	public ImageIcon getIcon(String iconName) {
		return getIcon(iconName, null, false);
	}
	
	/**
	 * Returns the icon at the given path
	 * @param iconName
	 * @return
	 */
	public ImageIcon getIconByPath(String path) {
		//try to get the icon from cache
		if (iconCache.containsKey(path)) {
			return (ImageIcon)iconCache.get(path);
		}
		
		//couldn't get the icon from cache, so try to load it up.
		try {
			URL url = this.getClass().getClassLoader().getResource(path);
			ImageIcon icon = new ImageIcon(url);
			//add the icon to the cache
			iconCache.put(path, icon);
			//return it
			return icon;
		} catch (Exception e) {
//			LOG.error("Failed to get Icon for path '" + path + "'", e);
			System.out.println("Failed to get Icon for path '" + path + "'");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Retrieves a URL witht the given file name.  There is no assumption made as to where the file is
	 * located (that is, I don't assume it is in the same directory as ResourceBundle).
	 */
	public static URL getURL(String fileName) {
		URL url = ResourceBundle.class.getClassLoader().getResource(fileName);
		return url;
	}
	
	/**
	 * Returns an input stream either from the local filesystem or from a jar. This method is
	 * compatible with Java Web Start
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(String fileName) throws IOException {
		URL url = getURL(fileName);
		return url.openStream();
	}
	
}
