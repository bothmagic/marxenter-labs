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
import javax.swing.JList;

import org.jdesktop.swingx.FilmStrip;
import org.jdesktop.swingx.FilmStripCellRenderer;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ImageSource.State;

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
	
	public Component getListCellRendererComponent(JList list, Object value,
			final int index, boolean isSelected, boolean cellHasFocus) {
		final FilmStrip filmStrip = (FilmStrip) list;
		
		ImageSource src = filmStrip.getModel().getElementAt(index);
		BufferedImage img = src.getCachedImage("FilmStrip.preview");
		Insets ci = filmStrip.getCellInsets();

		// note that we only support fixed cell dimensions
		int imageWidth = list.getFixedCellWidth() - (ci.left + ci.right);
		int imageHeight = list.getFixedCellHeight() - (ci.top + ci.bottom);
		
		// if the img == null ask the imagesource to create a scaled image
		// and run repaint after its done
		if (img == null || src.getState() == State.IDLE) {
			if (src.isUnretrievable()) {
				img = src.getUnRetrievableImageIcon();
			} else {
				src.createCachedImage("FilmStrip.preview", imageWidth, imageHeight, filmStrip.getImageScaler(), new Runnable() {
					public void run() {
						filmStrip.repaint(filmStrip.getCellBounds(index, index));
					}
				}, false);
				img = src.getPlaceHolderImage();
			}
		}
		
		return getCellRenderComponent(filmStrip, img, src, idx, 
				filmStrip.getFixedCellWidth(), filmStrip.getFixedCellHeight(), filmStrip.getCellInsets(), cellHasFocus, isSelected);
	}
	
	public Component getCellRenderComponent(FilmStrip strip, BufferedImage scaledImage,
			ImageSource imageSource, int idx, int width, int height, Insets insets, boolean mouseOver, boolean selected) {
		
		this.selected = selected;
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
		// add the offset from the insets
		x += cellInsets.left;
		y += cellInsets.top;
		
		return new Point(x, y);
	}
	
	public void setRollover(boolean rollover) {
		this.mouseOver = rollover;
	}
	protected boolean isRollover() {
		return this.mouseOver;
	}
}