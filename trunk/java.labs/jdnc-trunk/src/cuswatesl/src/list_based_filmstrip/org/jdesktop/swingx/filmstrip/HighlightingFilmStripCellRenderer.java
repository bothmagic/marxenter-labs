package org.jdesktop.swingx.filmstrip;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.UIManager;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.ImageSource;

public class HighlightingFilmStripCellRenderer extends AbstractFilmStripCellRenderer {
	
	@Override
	protected void paintBackground(Graphics2D g, BufferedImage scaledImage,
			ImageSource imageSource, FilmStrip strip, int idx,
			boolean mouseOver, boolean selected, Dimension size) {
		if (selected) {
			Color col = UIManager.getColor("FilmStrip.selectionBackground");
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f));
			
			
			//g.setColor(UIManager.getColor("Table.selectionBackground").darker());
			GradientPaint gp2 = new GradientPaint(0, 0, col, 0, size.height, col.darker());
			g.setPaint(gp2);
			g.fillRoundRect(0, 0, size.width, size.height, 30, 30);
			GradientPaint gp = new GradientPaint(0, 0, col.brighter(), 0, size.height, col);
			//g.setColor(UIManager.getColor("Table.selectionBackground"));
			g.setPaint(gp);
			g.fillRoundRect(2, 2, size.width - 4, size.height - 4, 29, 29);				
		}
		if (mouseOver) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
			
			Color col = UIManager.getColor("FilmStrip.selectionBackground");
			GradientPaint gp = new GradientPaint(0, 0, col, 0, size.height, col);
			
			g.setPaint(gp);
			g.fillRoundRect(2, 2, size.width - 4, size.height - 4, 29, 29);
		}
	}
	
	@Override
	protected void paintForeground(Graphics2D g2,
			BufferedImage scaledImage, ImageSource imageSource,
			FilmStrip strip, int idx, boolean mouseOver, boolean selected,
			Dimension size) {
		
		ImageSource src = filmStrip.getModel().getElementAt(idx);
		BufferedImage img = scaledImage;
		
		if (imageSource.isUnretrievable()) {
			img = src.getUnRetrievableImageIcon();
		}
		if (img == null) {
			img = src.getPlaceHolderImage();
		} 
		
		int w = img.getWidth();
		int h = img.getHeight();
		if (!mouseOver && !selected) {
			img = decolorify(img);
		}
		
		final int horImageBorder = (size.width - w) / 2;
		final int verImageBorder = (size.height - h) / 2;
        g2.drawImage(img, horImageBorder, verImageBorder, w, h, null);
	}
	
	BufferedImage decolorify(BufferedImage src) {
		BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
		for (int x = 0; x < src.getWidth(); x ++) {
			for (int y = 0; y < src.getHeight(); y++) {
				int orig[] = toARGBArray(src.getRGB(x, y));
				int val = (orig[1] + orig[2] + orig[3]) / 3;
				
				orig[1] = (orig[1] + val * 3) / 4;
				orig[2] = (orig[2] + val * 3) / 4;
				orig[3] = (orig[3] + val * 3) / 4;
				int decolorified = fromARGBArray(orig);
				result.setRGB(x, y, decolorified);
			}
		}
		return result;
	}
	
	private int fromARGBArray(int[] argb) {
		return (argb[0] & 0xFF) << 24 |
			   (argb[1] & 0xFF) << 16 |
			   (argb[2] & 0xFF) << 8 |
			   (argb[3] & 0xFF);
	}
	
	private int[] toARGBArray(int pixel) {
		int[] result = { 
				(pixel >> 24) & 0xFF, 
				(pixel >> 16) & 0xFF, 
				(pixel >> 8) & 0xFF, 
				 pixel & 0xFF};
		return result;
	}

}