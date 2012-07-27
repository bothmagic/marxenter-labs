package org.jdesktop.swingx.filmstrip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.batik.ext.awt.LinearGradientPaint;
import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ProgressPainter;

public class LineProgressPainter implements ProgressPainter {

	
	public LineProgressPainter() {
	}
	
    public void paintProgress(Graphics2D g2, BufferedImage scaledImage,
    		ImageSource imageSource, FilmStrip strip, int idx,
    		boolean mouseOver, boolean selected, Dimension size, int progress) {

    	int usedProgress = progress / 2;
    	
    	int width = size.width;
    	int height = size.height;
    	int x = width / 4;
    	int w = width / 2;
    	int h = 10;
    	int y = (height + h) / 2;
    	g2.translate(x, y);
    	g2.setColor(new Color(255, 255, 255, 100));
    	g2.fillRect(0, 0, w, h);
    	
    	boolean inverse = usedProgress % 20 >= 10;
    	
    	int pos = (((usedProgress) % 10) - 1);
    	if (inverse) {
    		pos = 10 - pos; 
    		pos -= 2;
    	}
    	int pwidth = w / 10;
    	int xpos = pos * pwidth;
    	int toX = xpos + 2 * pwidth;
    	
    	
    	LinearGradientPaint gp = new LinearGradientPaint(xpos, 0, toX, 0, new float[] {0, .5f, 1}, new Color[] {new Color(0, 0, 0, 0), Color.black, new Color(0,0,0,0)} );
    	g2.setPaint(gp);
    	g2.fillRect(0, 0, w, h);
    }
    

}
