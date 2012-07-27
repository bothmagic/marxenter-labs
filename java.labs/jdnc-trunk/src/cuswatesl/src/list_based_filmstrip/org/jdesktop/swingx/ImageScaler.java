package org.jdesktop.swingx;

import java.awt.image.BufferedImage;

/**
 * Interface for implemting a scaling algorithm. Caution must be used when implementing this interface as
 * implementations will be shared omang threads.
 * @author Nick
 *
 */
public interface ImageScaler {

	/**
	 * Scales the given image instance to the given maximum width and height. The aspect ratio must be retained at 
	 * all times. The <code>boolean</code> allowUpscaling is used to indicate whether or not the image must be scaled
	 * beyond it's original boundaries. Notice that implementors must expect this method to be called on multiple 
	 * threads.
	 * @param img the image to resize
	 * @param targetWidth the minimum width
	 * @param targetHeight the maximim width
	 * @param allowUpscaling if <code>true</code> the image size will be increased if that is needed to reach the
	 * targetWidth and targetHeight 
	 * @return a scaled instance of the given image
	 */
	BufferedImage scaleImage(BufferedImage img, float targetWidth, float targetHeight, boolean allowUpscaling);
}
