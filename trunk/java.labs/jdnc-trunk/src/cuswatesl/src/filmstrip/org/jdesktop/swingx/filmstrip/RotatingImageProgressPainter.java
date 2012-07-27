package org.jdesktop.swingx.filmstrip;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ProgressPainter;

public class RotatingImageProgressPainter implements ProgressPainter {

	BufferedImage image;
	double pi2 = Math.PI * 2;
	float alpha;
	
	public RotatingImageProgressPainter(BufferedImage image) {
		this(image, .9f);
	}
	public RotatingImageProgressPainter(BufferedImage image, float alpha) {
		this.image = image;
		this.alpha = alpha;
	}

    public void paintProgress(Graphics2D g2, BufferedImage scaledImage,
    		ImageSource imageSource, FilmStrip strip, int idx,
    		boolean mouseOver, boolean selected, Dimension size, int progress) {

    	int width = size.width;
    	int height = size.height;
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(width / 2, height / 2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        float rotation = progress / 5f;
        g2.rotate(rotation);
        g2.drawImage(image, - image.getWidth() / 2, -image.getHeight() / 2, null);
    }
    

}
