package org.jdesktop.swingx;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * An interface to implement custom ProgressPainters
 * @author Nick
 *
 */
public interface ProgressPainter {
	
	/**
	 * Paint some progressindicator with the specified size.
	 * @see FilmStripCellRenderer
	 */
	void paintProgress(Graphics2D g2, BufferedImage scaledImage,
    		ImageSource imageSource, FilmStrip strip, int idx,
    		boolean mouseOver, boolean selected, Dimension size, int progress);
	
	
}
