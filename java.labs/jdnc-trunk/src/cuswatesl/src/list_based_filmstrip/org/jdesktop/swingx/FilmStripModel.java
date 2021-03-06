package org.jdesktop.swingx;

import java.util.Collection;
import java.util.EventListener;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * Defines the model for the <code>FilmStrip</code> component. This model 
 * extends the <code>ListModel</code> as FilmStrip is a JList. Note that 
 * it is for most purposes not needed to implement a custom model, usually 
 * a custom ImageSourceFactory will do.
 * @see ImageSourceFactory
 * @author Nick
 */
public interface FilmStripModel extends ListModel {
	
	/**
	 * Clears the current <code>ImageSource</code>s and adds the given collection 
	 * @param images the new <code>ImageSource</code>s to set
	 */
	void setImages(Collection<ImageSource> images);
	
	/**
	 * Adds the given <code>ImageSource</code>s to the model
	 * @param images the <code>ImageSource</code>s to add
	 */
	void addImages(Collection<ImageSource> images);
	
	/**
	 * Adds the given <code>ImageSource</code> to the model
	 * @param image the <code>ImageSource</code> to add
	 */
	void addImage(ImageSource image);
	
	/**
	 * Removes all <code>ImageSource</code>s
	 */
	void removeAll();
	
	/**
	 * @param index the index from which the <code>ImageSource</code> must be returned
	 * @return the <code>ImageSource</code> at the given index
	 * @throws ArrayIndexOutOfBoundsException if <code>index >= getSize()</code> 
	 */
	ImageSource getElementAt(int index);
	
	/**
	 * @return All currently set <code>ImageSource</code>s. Note that at any given moment 
	 * a factory may still be adding <code>ImageSource</code>s
	 */
	Collection<ImageSource> getImages();
	
	
	/**
	 * @return the list of currently set <code>ListDataListener</code>s
	 */
	ListDataListener[] getListDataListeners();
	
	/**
	 * @param src
	 * @return the index of the given ImageSource in the model
	 */
	int indexOf(ImageSource src);
	
	/**
	 * Returns all registered EventListeners
	 * @param <T>
	 * @param listenerType
	 * @return
	 */
	public <T extends EventListener> T[] getListeners(Class<T> listenerType);	
}
