package org.jdesktop.swingx;

import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.filmstrip.ASyncImageSource;
import org.jdesktop.swingx.filmstrip.AbstractImageSource;

/**
 * Describes a source from which an image can be loaded. eg a file in the 
 * fileSystem or an URL pointing to an image. A basic implementation of this
 * interface can be found in the AbstractImageSource class, and a more
 * advanced implementation, which supports asynchronious loading and caching
 * of images, be found in the ASyncImageSource
 * @author Nick
 * @see ASyncImageSource
 * @see AbstractImageSource
 */
public interface ImageSource {

	/**
	 * The states the imageSource can be in.
	 */
	public enum State { IDLE, LOADING, DONE, ERROR } 

	
	/**
	 * Create a named (volatile) cached instance of the represented image asynchroniously, 
	 * with the given maximum width and height. The provided imageScaler will be used to 
	 * scale the image. After the scaled instance has been created the given callBack will 
	 * be run.
	 * If maxWidth or maxHeight is -1 the image will not be scaled.
	 *  
	 * @param id the name bound to the scaled version of the image, and the task loading it
	 * @param maxWidth the maximum allowed width
	 * @param maxHeight the maximum allowed height
	 * @param scaler the imagescaler to scale the image with
	 * @param callBack the callback to run when the scaling is complete
	 * 
	 * @see #createCachedImage(String, int, int, ImageScaler, Runnable, boolean)
	 * @see ImageScaler#scaleImage(BufferedImage, float, float, boolean)
	 */
	void createCachedImage(Object id, int maxWidth, int maxHeight, ImageScaler scaler, Runnable callBack);
	
	/**
	 * same as above method, but allows the client to choose between volatile or non-volatile
	 * cache. Volatile cache is the default. non-volatile cache should only be used for small 
	 * images, as large image have a much bigger memory footprint.
	 * 
	 * @param id the name bound to the scaled version of the image, and the task loading it.
	 * @param maxWidth the maximum allowed width
	 * @param maxHeight the maximum allowed height
	 * @param scaler the imagescaler to scale the image with
	 * @param callBack the callback to run when the scaling is complete
	 * @param cacheVolatile if true a volatile cache will be used (the default)
	 * 
	 * @see #createCachedImage(String, int, int, ImageScaler, Runnable)
	 * @see ImageScaler#scaleImage(BufferedImage, float, float, boolean)
	 */
	void createCachedImage(Object id, int maxWidth, int maxHeight, ImageScaler scaler, Runnable callBack, boolean cacheVolatile);
	
	/**
	 * Returns the image that should be displayed when showing a placeholder for the image 
	 * when it has not yet been loaded from its souce
	 * @return
	 */
	BufferedImage getPlaceHolderImage();
	
	/**
	 * Returns the named image created with a call to <code>createCachedImage</code>. This 
	 * method may return <code>null</code> if the cached image has been cleared on behalf
	 * of the garbage collector. 
	 * @param id
	 * @return
	 */
	BufferedImage getCachedImage(Object id);
	
	/**
	 * Cache the given image with the name given, cached images may be lost when the garbage
	 * collector requires free memory
	 * @param name
	 * @param id
	 * @see #cancelTask(Object)
	 */
	void cacheImage(Object id, BufferedImage image);
	
	/**
	 * Remove the cached image with the given name
	 * @param name
	 */
	void removeCachedImage(String name);
	
	/**
	 * Clear all cached images
	 */
	void clearCache();
	
	/**
	 * Returns a string representing the name of the image.
	 * @return a name representing the image or <code>null</code> if no is available
	 */
	String getImageName();
	
	/**
	 * Returns a string representing the path of the image. In case of a web image this
	 * may be the URL to the image, in case of a file on disc this could be the directory
	 * followed by the name of the image
	 * @return a path-name representing the image location or <code>null</code> if no is 
	 * available
	 */
	String getImageLocation();
	
	/**
	 * Returns a <code>String</code> describing the image
	 * @return a description of the image
	 */
	String getDescription();
	
	/**
	 * Returns a collection of actions that can be performed on this <code>ImageSource</code>
	 * @return a collection of performable actions
	 */
	Collection<Action> getActions();
	
	/**
	 * Adds an action that should be available for this <code>ImageSource</code>
	 * @param action
	 */
	void addAction(Action action);
	
	/**
	 * @return <code>true</code> if the image cannot be loaded from it's source
	 */
	boolean isUnretrievable();
	
	/**
	 * Returns an image representing the Image of this type of imagesource, if the image could not be loaded
	 * and no further attempts will be made
	 * @return
	 */
	BufferedImage getUnRetrievableImageIcon();
	
	/**
	 * Adds a changelistener to this <code>ImageSource</code>. This listener will be called
	 * if the state of the <code>ImageSource</code> changes 
	 * @param l
	 */
	void addChangeListener(ChangeListener l);
	
	/**
	 * Removes a ChangeListener from this ImageSource.
	 * @param l
	 */
	void removeChangeListener(ChangeListener l);
	
	/**
	 * Cancels any loading tasks in the ImageSource.
	 * @param taskName the name of the image being loaded this is the same
	 * as the name used in cacheImage and getCachedImage
	 * @see #cacheImage(String, BufferedImage) 
	 */
	void cancelTask(Object id);
	
	/**
	 * @return the state the ImageSource is in.
	 * @see State
	 */
	State getState(); 
}
