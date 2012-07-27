package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jdesktop.swingx.filmstrip.DefaultImageScaler;

/**
 * A Simple viewer that can display an ImageSource
 * @author Nick
 */
class SimpleImageViewer extends JPanel {
	
	private ImageSource imageSource;
	private BufferedImage image;
	private ImageScaler scaler = new DefaultImageScaler();
	private BufferedImage cache;

	private boolean scaleUp = false;
	
	public SimpleImageViewer() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				cache = null;
				validateImageHeight();
				revalidate();
				repaint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPopup(e);
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					doPopup(e);
				}
			}
			
			private void doPopup(MouseEvent e) {
				ImageSource src = imageSource;
				if (src != null) {
					FilmStripUtils.showImagePopup(e, src);
				}
			}
			
		} );
		setOpaque(true);
	}
	
	public void setImage(final ImageSource image) {
		if (this.imageSource != null) {
			this.imageSource.removeCachedImage("Viewer.fullSize");
		}
		this.imageSource = image == null ? null : image;
		this.image = imageSource.getCachedImage("Viewer.fullSize");
		if (this.image == null) {
			image.createCachedImage("Viewer.fullSize", -1, -1, null, new Runnable() {
				
				public void run() {
					SimpleImageViewer.this.image = image.getCachedImage("Viewer.fullSize");
					cache = null;
					validateImageHeight();
					revalidate();
					repaint();
				}
			});
		}
		this.cache = null;
		validateImageHeight();
		revalidate();
		repaint();
	}
	
	public void setScaleUp(boolean scaleUp) {
		boolean old = this.scaleUp;
		this.scaleUp = scaleUp;
		firePropertyChange("scaleUp", old, this.scaleUp);
	}
	
	public boolean isScaleUp() {
		return scaleUp;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(0, this.imageSource == null ? 100 : Math.max(drawnHeight + 4, 100));
	}
	
	private int drawnHeight = 0;
	
	private void validateImageHeight() {
		BufferedImage img = this.image;
		if (img == null) {
			this.drawnHeight = 0;
			return;
		}
		
        final int imgWidth = img.getWidth(null);
        final int imgHeight = img.getHeight(null);
        
        int pw = getWidth();
        int ph = img.getHeight();
        
        boolean isFullHeight = false;
        
        if (imgHeight < ph) {
        	ph = imgHeight;
        	isFullHeight = true;
        	this.drawnHeight = ph;
        }
        
        int w;
        int h;
        if ((imgWidth - pw) > (imgHeight - ph)) {
            w = pw;
            final float ratio = ((float)w) / ((float)imgWidth);
            h = (int)(imgHeight * ratio);
        } else {
            h = ph;
            final float ratio = ((float)h) / ((float)imgHeight);
            w = (int)(imgWidth * ratio);
        }
        
        if (!isFullHeight) {
        	this.drawnHeight = h;
        }
	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics.create();
		g.clearRect(0, 0, getWidth(), getHeight());
		if (this.cache == null) {
			this.cache = this.scaler.scaleImage(this.image, getWidth(), getHeight(), this.scaleUp);
		}
		if (this.cache == null) return;
		int x = (getWidth() - this.cache.getWidth()) / 2; 
		int y = (getHeight() - this.cache.getHeight()) / 2; 
		g.drawImage(this.cache, x, y, null);
	}
	
}