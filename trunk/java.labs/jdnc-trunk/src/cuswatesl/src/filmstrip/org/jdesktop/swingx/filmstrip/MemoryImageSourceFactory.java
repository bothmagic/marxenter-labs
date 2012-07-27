package org.jdesktop.swingx.filmstrip;

import java.util.ArrayList;
import java.util.Collection;

import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ImageSourceFactory;
import org.jdesktop.swingx.ImageSourcePublisher;

/**
 * A basic implementation of the <code>ImageSourceFactory</code> using <code>ImageSource</code>s 
 * which already exist.
 * @author Nick
 *
 */
public class MemoryImageSourceFactory implements ImageSourceFactory {

	private final Collection<ImageSource> imageSources = new ArrayList<ImageSource>();

	/**
	 * Creates a new MemoryImageSourceFactory with the given <code>ImageSource</code> collection 
	 * @param imageSources the <code>ImageSource</code> collection
	 */
	public MemoryImageSourceFactory(Collection<ImageSource> imageSources) {
		this.imageSources.addAll(imageSources);
	}
	
	public void loadImages(ImageSourcePublisher publisher) {
		for (ImageSource src : this.imageSources) {
			publisher.publish(src);
		}
	}
	
}
