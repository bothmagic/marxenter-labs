package org.jdesktop.swingx.filmstrip;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

import javax.swing.UIManager;
import javax.swing.text.AttributeSet.FontAttribute;

import org.apache.batik.ext.awt.LinearGradientPaint;
import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;

public class DiaCellRenderer extends AbstractFilmStripCellRenderer {
	
	BufferedImage bgCache;
	
	@Override
	protected void paintBackground(Graphics2D g, BufferedImage scaledImage,
			ImageSource imageSource, FilmStrip strip, int idx,
			boolean mouseOver, boolean selected, Dimension size) {
		
    	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g.drawImage(getBackgroundImage(getWidth(), getHeight()), 0, 0, null);
	}
	
	@Override
	protected void paintForeground(Graphics2D g2,
			BufferedImage scaledImage, ImageSource imageSource,
			FilmStrip strip, int idx, boolean mouseOver, boolean selected,
			Dimension size) {
		
		ImageSource src = filmStrip.getModel().getImage(idx);
		BufferedImage img = scaledImage;
		
		if (imageSource.isUnretrievable()) {
			img = src.getUnRetrievableImageIcon();
		}
		if (img == null) {
			img = src.getPlaceHolderImage();
		} 
		
		Point p = getImageLocationInCell();
		Graphics2D g2d = (Graphics2D) g2.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int w = img.getWidth();
		int h = img.getHeight();
		int x = p.x;
		int y = p.y;
		
		
        Color base;
        if (selected) {
        	base = UIManager.getColor("FilmStrip.selectionBackground");
        } else if (mouseOver) {
        	base = UIManager.getColor("FilmStrip.selectionBackground").darker();
        } else {
        	base = Color.DARK_GRAY;
        }
        
        g2d.setColor(base);
        g2d.drawRect(x - 1,y - 1, w + 1, h + 1);
        g2d.setColor(base.brighter());
        g2d.drawRect(x -2, y -2, w + 3, h + 3);
        
    	if (!mouseOver && !selected) {
    		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .95f));
    	}
        g2d.drawImage(img, x, y, null);
        
		paintText(g2d);
	}
	
	void paintText(Graphics2D g2d) {
        Map<TextAttribute, Integer> map = (Map) (new Font("Arial", Font.PLAIN, 10)).getAttributes();
        map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_LOW_TWO_PIXEL);
        Font newFont = new Font(map);
        setFont(newFont);
        setForeground(Color.blue);
        TextLayout l = new TextLayout(imageSource.getImageName(), map, g2d.getFontRenderContext());

        Rectangle2D bounds = l.getBounds();
        double x = (size.width / 2) + bounds.getWidth() / -2;
        double y = (size.height - cellInsets.bottom + l.getAscent() + 2);
        
        g2d.setColor(Color.WHITE);
        l.draw(g2d, (float) x, (float) y);
	}
	
	BufferedImage getBackgroundImage(int width, int height) {
		if (this.bgCache == null || this.bgCache.getWidth() != width || this.bgCache.getHeight() != height) {
			this.bgCache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bgCache.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			LinearGradientPaint gp = new LinearGradientPaint(width, 0, 0, height,
					new float[] { 0, .2f, .499f, .5f, .8f, 1},
					new Color[] { Color.black, Color.DARK_GRAY, Color.black, Color.DARK_GRAY, Color.BLACK, Color.DARK_GRAY}
					);
			g2.setPaint(gp);

			RoundRectangle2D r2 = new RoundRectangle2D.Float(4, 4, width - 8, height - 8, 14, 14);
			g2.setClip(r2);
			g2.fillRect(0, 0, width, height);
			g2.dispose();
		}
		return this.bgCache;
	}
	
}
