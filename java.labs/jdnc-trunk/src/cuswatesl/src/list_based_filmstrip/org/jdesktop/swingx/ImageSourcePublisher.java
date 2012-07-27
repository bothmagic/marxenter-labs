package org.jdesktop.swingx;

/**
 * A simple publisher for ImageSources.
 * @author Nick
 *
 */
public interface ImageSourcePublisher {

	/**
	 * Publishes the imagesource.
	 * @param src
	 */
	void publish(ImageSource src);
	
}
