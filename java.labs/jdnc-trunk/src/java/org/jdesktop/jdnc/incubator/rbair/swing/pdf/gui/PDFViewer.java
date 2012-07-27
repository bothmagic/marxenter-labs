/*
 * $Id: PDFViewer.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import org.jdesktop.jdnc.incubator.rbair.swing.JScrollablePanel;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.PDFDocument;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.CurrentPageChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.CurrentPageChangeListener;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.PDFDocumentModelChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.PDFDocumentModelChangeListener;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.ZoomChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.ZoomChangeListener;

/**
 * This is an object that contains a rendered PDF page
 * @author Richard Bair
 */
public class PDFViewer extends JScrollablePanel implements PDFDocumentModelChangeListener, ZoomChangeListener, CurrentPageChangeListener {
	/**
	 * This number is used to determine what zoom factor to use when actually
	 * rendering the pdf document. Rendering is only performed ONCE PER PAGE
	 * rendered. After that, an intermediate object, renderedImage, stores the
	 * rendered image from the pdf document and THAT is scaled according to the
	 * user's wishes. This provides incredibly improved rendering performance
	 * without massive visual imperfections
	 */
	private static final double RENDER_ZOOM_FACTOR = 2.0;
	/**
	 * PDFDocument model that this editor is based on
	 */
	private PDFDocument doc;
	/**
	 * This image contains the composed PDF file. This is used for caching the output (we don't
	 * re-render unless the zoom or page number has changed).
	 */
	private ImageIcon helper;
	/**
	 * An array that contains all of the rendered pages (as images). The page number is the index into the array
	 */
	private ImageIcon[] renderedPages;
	/**
	 * This is the thread that will actually perform the rendering. When it is done rendering it will load the
	 * rendered image into the renderedPages array.
	 */
	private RenderThread rt;
	/**
	 * A little helper variable that indicates whether the init() method has been called the first time
	 */
	private boolean initialized;
	
	/**
	 * Default construct. This will return an empty image UNLESS you have specified a PDF document in the setPDFDocument method.
	 */
	public PDFViewer() {
		initialized = false;
	}

	/**
	 * 
	 * @param doc
	 */
	public PDFViewer(PDFDocument doc) {
		setPDFDocument(doc);
	}

	/**
	 * Allows the user to set what PDFDocument the viewer is to use now. This resets everything.
	 * @param doc
	 */
	public void setPDFDocument(PDFDocument doc) {
		if (this.doc != null) {
			this.doc.removeCurrentPageChangeListener(this);
			this.doc.removePDFDocumentModelChangeListener(this);
			this.doc.removeZoomChangeListener(this);
		}
		this.doc = doc;
		doc.addCurrentPageChangeListener(this);
		doc.addPDFDocumentModelChangeListener(this);
		doc.addZoomChangeListener(this);
		helper = new ImageIcon();
		renderedPages = new ImageIcon[doc.getPageCount()];
		this.setBorder(BorderFactory.createEmptyBorder());
		rt = null;
		initialized = false;
		setPreferredSize(new Dimension((int)(doc.getPageWidth()*doc.getZoom()), (int)(doc.getPageHeight()*doc.getZoom())));
	}
	
	/**
	 * Initializes the "helper" ImageIcon with a scaled version of the PDF document, including any necessary
	 * gutters (as when the pdf document is too small to fit inside the parent component);
	 */
	private void init() {
		//if the RenderThread hasn't yet been kicked off, now's a great time to do it!
		if (rt == null) {
			//fire up the RenderThread
			rt = new RenderThread();
			rt.start();
		}
		
		try {
			//resize according to the zoom
			double zoom = doc.getZoom();
      int height = (int) (doc.getPageHeight() * zoom);
      int width = (int) (doc.getPageWidth() * zoom);
      Dimension parentDim = getParent() == null ? new Dimension(width, height) : getParent().getSize();
      height = parentDim.height > height ? parentDim.height : height;
      width = parentDim.width > width ? parentDim.width : width;
      setPreferredSize(new Dimension(width, height));
      setSize(width, height);
			
			//Check to see if the page has been rendered. If it has, then use it. Otherwise, ask the
			//RenderThread to render this page. (this call to the render thread will block until the
			//page is rendered).
			int currentPage = doc.getCurrentPage();
			if (renderedPages[currentPage-1] == null) {
				rt.renderPage(currentPage);
			}

			//either the zoom or the current page has changed. Change the image to be zoomed to the proper size
			// get the pageWidth and pageHeight. These are the pixel width/height of the document
			// as it will appear AFTER resizing.
      int pageHeight = (int) (doc.getPageHeight() * zoom);
      int pageWidth = (int) (doc.getPageWidth() * zoom);
      // create an image object large enough to handle the ENTIRE panel
			Image pdfImage = createImage(getPreferredSize().width, getPreferredSize().height);
			//get the graphics2D object and use it to paint the background (standard panel paint method)
			Graphics2D g2 = (Graphics2D)pdfImage.getGraphics();
			super.paintComponent(g2);
			//set the rendering hints to be high quality
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    //translate the painting coords such that the old image will be centered in the new image
      g2.translate((getPreferredSize().width - pageWidth)/2, (getPreferredSize().height - pageHeight)/2);
			//set the scale factor
	    g2.scale(zoom/2, zoom/2);
      //draw the old image into the new image
			g2.drawImage(renderedPages[currentPage-1].getImage(), 0, 0, renderedPages[currentPage-1].getImageObserver());
			//save the new image
			helper.setImage(pdfImage);
			
			//set initialized to true so as to avoid reinit'ing when not necessary
			initialized = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		if (!initialized && doc != null) {
			init();
		} else if (doc == null) {
			super.paintComponent(g);
		} else {
			g.drawImage(helper.getImage(), 0, 0, helper.getImageObserver());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.jgui.pdf.event.PDFDocumentModelChangeListener#modelChanged(com.jgui.pdf.event.PDFDocumentModelChangeEvent)
	 */
	public void modelChanged(PDFDocumentModelChangeEvent e) {
		init();
		repaint();
	}

	/* (non-Javadoc)
	 * @see com.jgui.pdf.event.ZoomChangeListener#zoomChanged(com.jgui.pdf.event.ZoomChangeEvent)
	 */
	public void zoomChanged(ZoomChangeEvent e) {
		init();
		repaint();
	}

	/* (non-Javadoc)
	 * @see com.jgui.pdf.event.CurrentPageChangeListener#pageChanged(com.jgui.pdf.event.CurrentPageChangeEvent)
	 */
	public void pageChanged(CurrentPageChangeEvent e) {
		init();
		repaint();
	}

	/**
	 * Thread that renders pages in the background. This allows many pages to be rendered ahead of use, allowing the EventDispatch
	 * thread to quickly load the image, not having to wait for the PDF renderer to do its business.
	 * @author Richard Bair
	 * date: May 27, 2004
	 */
	private final class RenderThread extends Thread {
		/**
		 * Contains a list of integers representing the page numbers that the client application would like
		 * to render. This is a priority queue with the most important pages at the head. These must all be
		 * rendered before continuing on.
		 */
		private LinkedList queue = new LinkedList();
		/**
		 * Lock mutex used to keep multiple clients from adding to the queue at the same time
		 */
		private Object renderLock = "RENDER_LOCK";
		/**
		 * Renders a specific page and places it in the renderedPages array of the PDFViewer. This method blocks
		 * until the page is rendered.
		 * @param pageNumber
		 */
		public void renderPage(int pageNumber) {
			synchronized (renderLock) {
				queue.add(new Integer(pageNumber));
			}
			while (renderedPages[pageNumber-1] == null) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException ie) {
					//wow, I was woke up. Excellent. Let the while loop try again.
				}
			}
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			int pageCount = doc.getPageCount();
			for (int i=1; i<=pageCount; i++) {
				//first, before continuing this loop, go through the queue until it is empty
				while (queue.size() > 0) {
					int pageNumber = ((Integer)queue.removeFirst()).intValue();
					doRender(pageNumber);
				}
				
				//render the ith page
				doRender(i);
			}
		}

		/**
		 * Actually renders the page and puts it in the array of rendered pages.
		 * This method is only called by the RenderThread thread, so it doesn't need any synchronizing locks
		 * @param pageNumber
		 * @throws Exception
		 */
		private void doRender(int pageNumber) {
			try {
				if (renderedPages[pageNumber-1] == null) {
					Image pdfImage = createImage((int)doc.getPageWidth() * 2, (int)doc.getPageHeight() * 2);
		      //render at 400%, then do less than perfect scaling...
		      doc.render((Graphics2D)pdfImage.getGraphics(), pageNumber, 2.00);
		      ImageIcon page = new ImageIcon(pdfImage);
		      renderedPages[pageNumber-1] = page;
				}
			} catch (Exception e) {
				System.err.println("Failed to render pdf page " + pageNumber + ". Returning an empty imageicon");
				e.printStackTrace();
				renderedPages[pageNumber-1] = new ImageIcon();
			}
    }
	}
}
