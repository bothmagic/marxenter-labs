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

public class DefaultFilmStripCellRenderer extends AbstractFilmStripCellRenderer {
	
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
		
		ImageSource src = filmStrip.getModel().getImage(idx);
		BufferedImage img = scaledImage;
		
		if (imageSource.isUnretrievable()) {
			img = src.getUnRetrievableImageIcon();
		}
		if (img == null) {
			img = src.getPlaceHolderImage();
		} 
		
		int w = img.getWidth();
		int h = img.getHeight();
		
		
		final int horImageBorder = (size.width - w) / 2;
		final int verImageBorder = (size.height - h) / 2;
        g2.drawImage(img, horImageBorder, verImageBorder, w, h, null);
		
	}
	
}