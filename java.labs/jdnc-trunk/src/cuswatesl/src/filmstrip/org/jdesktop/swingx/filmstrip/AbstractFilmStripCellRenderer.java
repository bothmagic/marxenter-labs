package org.jdesktop.swingx.filmstrip;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.FilmStripCellRenderer;
import org.jdesktop.swingx.ImageSource;

/**
 * A base implementation of the FilmStripCellRenderer. Implements the getCellRenderComponent 
 * method and leaves the client to implement a paintBackground and a paintForeground method
 * @author Nick
 *
 */
public abstract class AbstractFilmStripCellRenderer extends JComponent implements FilmStripCellRenderer {

	public AbstractFilmStripCellRenderer() {
	}
	
	boolean selected;
	boolean mouseOver;
	BufferedImage image;
	ImageSource imageSource;
	FilmStrip filmStrip;
	int idx;
	Dimension size;
	Insets cellInsets;
	
	
	public Component getCellRenderComponent(FilmStrip strip, BufferedImage scaledImage,
			ImageSource imageSource, int idx, int width, int height, Insets insets, boolean mouseOver, boolean selected) {
		
		this.selected = selected;
		this.mouseOver = mouseOver;
		this.image = scaledImage;
		this.imageSource = imageSource;
		this.filmStrip = strip;
		this.idx = idx;
		this.size = new Dimension(width, height);
		this.cellInsets = insets;
		return this;
	}
	
	protected abstract void paintBackground(Graphics2D g2, BufferedImage scaledImage, ImageSource imageSource,
			FilmStrip strip, int idx, boolean mouseOver, boolean selected, Dimension size);
	
	protected abstract void paintForeground(Graphics2D g2, BufferedImage scaledImage, ImageSource imageSource,
			FilmStrip strip, int idx, boolean mouseOver, boolean selected, Dimension size);
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintBackground((Graphics2D) g2.create(), image, imageSource, filmStrip, idx, mouseOver, selected, size);
		paintForeground((Graphics2D) g2.create(), image, imageSource, filmStrip, idx, mouseOver, selected, size);
	}
	
	public Insets getCellInsets() {
		return cellInsets;
	}
	
	protected Point getImageLocationInCell() {
		Dimension imageSpace = new Dimension(size.width - (cellInsets.left + cellInsets.right), 
											 size.height - (cellInsets.top + cellInsets.bottom));
		// find where in the image is located, centered in the insets
		int x = (imageSpace.width - image.getWidth()) / 2;
		int y = (imageSpace.height - image.getHeight()) / 2;
		System.out.println(new Point(x, y));
		// add the offset from the insets
		x += cellInsets.left;
		y += cellInsets.top;
		
		return new Point(x, y);
	}
	
}