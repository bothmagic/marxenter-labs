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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.filmstrip.DefaultImageScaler;

/**
 * A Simple viewer that can display an ImageSource
 * @author Nick
 */
public class ImageSourceViewer extends JComponent {
	
	private ImageSource imageSource;
	private transient BufferedImage image;
	private ImageScaler scaler = new DefaultImageScaler();
	private BufferedImage cache;
	boolean updateCache = false;
	boolean scaleAsync = true;

	private boolean scaleUp = false;
	
	public ImageSourceViewer() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
				updateCache = true;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						validateImageHeight();
						revalidate();
						repaint();
					}
				});
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
	}
	
	public void setImage(final ImageSource imageSource) {
		if (this.imageSource != null) {
			this.imageSource.removeCachedImage("Viewer.fullSize");
		}
		if (imageSource == null) {
			this.image = null;
			this.cache = null;
			revalidate();
			repaint();
			return;
		}
		this.imageSource = imageSource;
		this.image = imageSource.getCachedImage("Viewer.fullSize");
		if (this.image == null) {
			imageSource.createCachedImage("Viewer.fullSize", -1, -1, null, new Runnable() {
				public void run() {
					ImageSourceViewer.this.image = imageSource.getCachedImage("Viewer.fullSize");
					updateCache = true;
					validateImageHeight();
					revalidate();
					repaint();
				}
			});
		}
	}
	
	public void setScaleUp(boolean scaleUp) {
		boolean old = this.scaleUp;
		this.scaleUp = scaleUp;
		firePropertyChange("scaleUp", old, this.scaleUp);
		this.cache = null;
		repaint();
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
		//fillBackground(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		if (this.cache == null || updateCache) {
			final int width = getWidth();
			final int height = getHeight();
			final AtomicReference<BufferedImage> imgRef = new AtomicReference<BufferedImage>(image);
			if (scaleAsync) {
				new SwingWorker<BufferedImage, Void>() {
					@Override
					protected BufferedImage doInBackground() throws Exception {
						return scaler.scaleImage(imgRef.get(), width, height, isScaleUp());
					}
					@Override
					protected void done() {
						try {
							cache = get();
							updateCache = false;
							repaint();
						} catch (InterruptedException e) {
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
				}.execute();
			} else {
				this.cache = scaler.scaleImage(image, width, height, scaleUp);
			}
		}
		if (this.cache == null) return;
		int x = (getWidth() - this.cache.getWidth()) / 2; 
		int y = (getHeight() - this.cache.getHeight()) / 2; 
		g.drawImage(this.cache, x, y, null);
	}
	
	public void setScaleAsync(boolean scaleAsync) {
		boolean old = this.scaleAsync;
		this.scaleAsync = scaleAsync;
		firePropertyChange("scaleAsynv", old, scaleAsync);
	}
	
	public boolean isScaleAsync() {
		return scaleAsync;
	}
}