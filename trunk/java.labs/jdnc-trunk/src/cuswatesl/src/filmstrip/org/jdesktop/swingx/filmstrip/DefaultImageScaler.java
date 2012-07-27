package org.jdesktop.swingx.filmstrip;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.ImageScaler;

/**
 * Default implementation of the ImageScaler interface
 * @author Nick
 */
public class DefaultImageScaler implements ImageScaler {

	public BufferedImage scaleImage(BufferedImage img, float targetWidth, float targetHeight, boolean fillMax) {

		if (img == null) return null;
		if (targetWidth == -1 || targetHeight == -1) return img;
        final float imgWidth = img.getWidth(null);
        final float imgHeight = img.getHeight(null);
        
        boolean isFullWidth = false;
        boolean isFullHeight = false;
        
        if (imgHeight < targetHeight) {
        	isFullHeight = true;
        }
        
        if (imgWidth < targetWidth) {
        	isFullWidth = true;
        }
        
        
        float w;
        float h;
        BufferedImage result;
        if (isFullWidth && isFullHeight && !fillMax) {
        	w = imgWidth;
        	h = imgHeight;
        } else {
        	float ratiow = ((float) imgWidth / (float) targetWidth);
        	float ratioh = ((float) imgHeight / (float) targetHeight);
        	if (ratioh > ratiow) {
        		h = targetHeight;
        		w = (int) (imgWidth / ratioh);
        	} else {
        		h = (int) (imgHeight / ratiow);
        		w = targetWidth;
        	}
        }
    	result = new BufferedImage((int) w, (int) h, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g = result.createGraphics();
    	// use nearest neighbor interpolation, on some images other settings will take
    	// a lot of time (about 20 secs on my pc)
    	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
    	g.drawImage(img, 0, 0, (int) w, (int) h, null);
    	g.dispose();
    	
        return result;
	}
	
}
