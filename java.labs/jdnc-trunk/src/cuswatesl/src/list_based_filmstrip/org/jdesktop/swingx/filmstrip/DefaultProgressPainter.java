package org.jdesktop.swingx.filmstrip;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ProgressPainter;

public class DefaultProgressPainter implements ProgressPainter {

    private int frame = -1;
    private int points = 13;
    private float barWidth = 4f;
    private float barLength = 10;
    private float centerDistance = 8;
    
    private Color baseColor = new Color(200,200,200);
    private Color highlightColor = Color.BLACK;
    private int trailLength = 4;

    public void paintProgress(Graphics2D g2, BufferedImage scaledImage,
    		ImageSource imageSource, FilmStrip strip, int idx,
    		boolean mouseOver, boolean selected, Dimension size, int progress) {

    	int width = size.width;
    	int height = size.height;
    	setFrame(progress);
    	
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D rect = new RoundRectangle2D.Float(getCenterDistance(), -getBarWidth()/2,
                getBarLength(), getBarWidth(),
                getBarWidth(), getBarWidth());
        g2.setColor(Color.GRAY);
        
        g2.translate(width / 2, height / 2);
        for(int i=0; i<getPoints(); i++) {
            g2.setColor(calcFrameColor(i));
            g2.fill(rect);
            g2.rotate(Math.PI * 2.0 / getPoints());
        }
    }
    
    private Color calcFrameColor(final int i) {
        if(frame == -1) {
            return getBaseColor();
        }
        
        for(int t=0; t<getTrailLength(); t++) {
            if(i == (frame-t+getPoints()) % getPoints()) {
                float terp = 1 - ((float) (getTrailLength() - t)) / (float) getTrailLength();
                return interpolate(getBaseColor(), getHighlightColor(), terp);
            }
        }
        return getBaseColor();
    }
    
    public int getFrame() {
        return frame;
    }
    
    public void setFrame(int frame) {
        this.frame = frame;
    }
    
    public Color getBaseColor() {
        return baseColor;
    }
    
    public void setBaseColor(Color baseColor) {
        this.baseColor = baseColor;
    }
    
    public Color getHighlightColor() {
        return highlightColor;
    }
    
    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }
    
    public float getBarWidth() {
        return barWidth;
    }
    
    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
    }
    
    public float getBarLength() {
        return barLength;
    }
    
    public void setBarLength(float barLength) {
        this.barLength = barLength;
    }
    
    public float getCenterDistance() {
        return centerDistance;
    }
    
    public void setCenterDistance(float centerDistance) {
        this.centerDistance = centerDistance;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }

    public int getTrailLength() {
        return trailLength;
    }

    public void setTrailLength(int trailLength) {
        this.trailLength = trailLength;
    }

    public static Color interpolate(Color b, Color a, float t) {
        float[] acomp = a.getRGBComponents(null);
        float[] bcomp = b.getRGBComponents(null);
        float[] ccomp = new float[4];
        
        for(int i=0; i<4; i++) {
            ccomp[i] = acomp[i] + (bcomp[i]-acomp[i])*t;
        }
        
        return new Color(ccomp[0],ccomp[1],ccomp[2],ccomp[3]);
    }

}
