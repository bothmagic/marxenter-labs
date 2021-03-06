package org.jdesktop.swingx;

/**
 * An <code>ImageSourceFactory</code> is a class that can load ImageSources from a given resource.
 * a resource can be anything, for example an url to a html document that contains img tags, or a
 * directory in the filesystem containing images.
 * @author Nick
 *
 */
public interface ImageSourceFactory {

	/**
	 * Publishes the <code>ImageSource</code>s found in the resource using the given publisher
	 * @param publisher
	 */
	void loadImages(final ImageSourcePublisher publisher);

	/**
	 * Cancels the ImageSource production. After this method is called the Factory
	 * may no longer publish images. Production can be restarted by a new call
	 * to loadImages.
	 */
	void cancelProduction(); 
	
}