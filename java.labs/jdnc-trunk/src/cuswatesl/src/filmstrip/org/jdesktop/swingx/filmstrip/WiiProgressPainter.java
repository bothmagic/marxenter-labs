package org.jdesktop.swingx.filmstrip;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ProgressPainter;

public class WiiProgressPainter implements ProgressPainter {

	private int boxSize;
	private int margin;
	
	public WiiProgressPainter(int boxSize, int margin) {
		this.boxSize = boxSize;
		this.margin = margin;
	}
	public WiiProgressPainter() {
		this(6, 6);
	}
	
    public void paintProgress(Graphics2D g2, BufferedImage scaledImage,
    		ImageSource imageSource, FilmStrip strip, int idx,
    		boolean mouseOver, boolean selected, Dimension size, int progress) {

    	int cx = size.width / 2;
    	int cy = size.height / 2;
    	
    	int xl = cx - (int) (boxSize * 1.5 + margin);
    	int xc = cx - (int) (boxSize * .5);
    	int xr = cx + (int) (boxSize * .5 + margin);
    	int yt = cy - (int) (boxSize * 1.5 + margin);
    	int yc = cy - (int) (boxSize * .5);
    	int yb = cy + (int) (boxSize * .5 + margin);
    	
    	int skip = (progress / 4) % 8;
    	
    	// topleft
    	if (skip != 0) paintRect(g2, xl, yt, boxSize, boxSize);
    	
    	// topcenter
    	if (skip != 1) paintRect(g2, xc, yt, boxSize, boxSize);
    	
    	// topright
    	if (skip != 2) paintRect(g2, xr, yt, boxSize, boxSize);
    	
    	// left
    	if (skip != 7) 	paintRect(g2, xl, yc, boxSize, boxSize);
    	
    	// center
    	paintRect(g2, xc, yc, boxSize, boxSize);
    	
    	// right
    	if (skip != 3) paintRect(g2, xr, yc, boxSize, boxSize);
    	
    	// bottomleft
    	if (skip != 6) paintRect(g2, xl, yb, boxSize, boxSize);
    	
    	// bottomcenter
    	if (skip != 5) paintRect(g2, xc, yb, boxSize, boxSize);
    	
    	// bottomright
    	if (skip != 4) paintRect(g2, xr, yb, boxSize, boxSize);
    	
    }
    
    void paintRect(Graphics2D g2, int x, int y, int w, int h) {
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
    	g2.setPaint(Color.black);
    	g2.fillRect(x + 4, y + 4, w, h);
    	
    	GradientPaint gp = new GradientPaint(x, y, new Color(140, 150, 190), x, y + h, new Color(200, 220, 255));
    	g2.setPaint(gp);
    	g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .9f));
    	g2.fillRect(x, y, w, h);
    	
    	g2.setColor(Color.gray);
    	g2.drawRect(x, y, w, h);
    }
    

}
