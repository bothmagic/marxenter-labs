/*
 * $Id: PDFDocument.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.awt.Graphics2D;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.CurrentPageChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.CurrentPageChangeListener;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.PDFDocumentModelChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.PDFDocumentModelChangeListener;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.ZoomChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.pdf.event.ZoomChangeListener;

/**
 * @author Richard Bair
 */
public class PDFDocument {
	/**
	 * The parser that will parser this pdf document
	 */
	private PDFParser parser;
	/**
	 * The renderer that will render pages for this pdf document
	 */
	private PDFRenderer renderer;
	/**
	 * The list of PDFDocumentModelChangeListener objects registered with this PDFDocument
	 */
	private List modelChangeListeners = new ArrayList();
	/**
	 * The list of ZoomChangeListener objects registered with this PDFDocument
	 */
	private List zoomChangeListeners = new ArrayList();
	/**
	 * The list of CurrentPageChangeListener objects registered with this PDFDocument
	 */
	private List currentPageChangeListeners = new ArrayList();
	/**
	 * The current page in the document
	 */
	private int currentPage;
	/**
	 * The zoom factor (1.0 == 100%)
	 */
	private double zoom = 1.0;
	/**
	 * The current page's width (cached here)
	 */
	private double pageWidth;
	/**
	 * The current page's height (cached here)
	 */
	private double pageHeight;
	
	/**
	 * Create a new PDFDocument. Pass in a File object for the pdf file you want to open
	 */
	public PDFDocument(File pdfFile) {
		parser = new PDFParser(pdfFile);
		renderer = new PDFRenderer(parser);
		firePDFDocumentModelChanged();
	  nextPage();
	}
	
	public PDFDocument(InputStream is) {
		parser = new PDFParser(is);
		renderer = new PDFRenderer(parser);
		firePDFDocumentModelChanged();
	  nextPage();
	}
	
	public PDFDocument(ByteBuffer buffer) {
		parser = new PDFParser(buffer);
		renderer = new PDFRenderer(parser);
		firePDFDocumentModelChanged();
	  nextPage();
	}
	
	/**
	 * Adds the given listener to this objects list
	 * @param l
	 */
	public void addPDFDocumentModelChangeListener(PDFDocumentModelChangeListener l) {
		if (!modelChangeListeners.contains(l)) {
			modelChangeListeners.add(l);
		}
	}
	
	/**
	 * Removes the given listener from this objects list
	 * @param l
	 */
	public void removePDFDocumentModelChangeListener(PDFDocumentModelChangeListener l) {
		modelChangeListeners.remove(l);
	}
	
	/**
	 * Notifies all PDFDocumentModelChangeListeners that a change has occurred
	 * @param e
	 */
	private void firePDFDocumentModelChanged() {
		PDFDocumentModelChangeEvent e = new PDFDocumentModelChangeEvent(this);
		for (int i=0; i<modelChangeListeners.size(); i++) {
			((PDFDocumentModelChangeListener)modelChangeListeners.get(i)).modelChanged(e);
		}
	}
	
	/**
	 * Adds the given listener to this objects list
	 * @param l
	 */
	public void addZoomChangeListener(ZoomChangeListener l) {
		if (!zoomChangeListeners.contains(l)) {
			zoomChangeListeners.add(l);
		}
	}
	
	/**
	 * Removes the given listener from this objects list
	 * @param l
	 */
	public void removeZoomChangeListener(ZoomChangeListener l) {
		zoomChangeListeners.remove(l);
	}
	
	/**
	 * Notifies all ZoomChangeListeners that a change has occurred
	 * @param e
	 */
	private void fireZoomChanged() {
		ZoomChangeEvent e = new ZoomChangeEvent(this);
		for (int i=0; i<zoomChangeListeners.size(); i++) {
			((ZoomChangeListener)zoomChangeListeners.get(i)).zoomChanged(e);
		}
	}
	
	/**
	 * Adds the given listener to this objects list
	 * @param l
	 */
	public void addCurrentPageChangeListener(CurrentPageChangeListener l) {
		if (!currentPageChangeListeners.contains(l)) {
			currentPageChangeListeners.add(l);
		}
	}
	
	/**
	 * Removes the given listener from this objects list
	 * @param l
	 */
	public void removeCurrentPageChangeListener(CurrentPageChangeListener l) {
		currentPageChangeListeners.remove(l);
	}
	
	/**
	 * Notifies all CurrentPageChangeListeners that a change has occurred
	 * @param e
	 */
	private void fireCurrentPageChanged() {
		CurrentPageChangeEvent e = new CurrentPageChangeEvent(this);
		for (int i=0; i<currentPageChangeListeners.size(); i++) {
			((CurrentPageChangeListener)currentPageChangeListeners.get(i)).pageChanged(e);
		}
	}
	
	/**
	 * Returns the number of pages in this document
	 * @return
	 */
	public int getPageCount() {
		return parser.getPageCount();
	}
	
	public void setPage(int pageNumber) {
		if (currentPage != pageNumber) {
			assert pageNumber > 0 && pageNumber <= getPageCount();
			currentPage = pageNumber;
			PDFParser.PDFPage page = parser.getPage(currentPage);
			pageWidth = page.getPageWidth();
			pageHeight = page.getPageHeight();
			fireCurrentPageChanged();
		}
	}
	
	public void nextPage() {
		if (hasNextPage()) {
			setPage(currentPage + 1);
		}
	}
	
	public void prevPage() {
		if (hasPrevPage()) {
			setPage(currentPage - 1);
		}
	}
	
	public boolean hasNextPage() {
		return currentPage + 1 <= getPageCount();
	}
	
	public boolean hasPrevPage() {
		return currentPage - 1 > 0;
	}
	
	public void setZoom(double zoom) {
		if (this.zoom != zoom) {
			this.zoom = zoom;
			fireZoomChanged();
		}
	}
	
	public void zoomIn() {
		if (canZoomIn()) {
			setZoom(zoom + .25);
		}
	}
	
	public void zoomOut() {
		if (canZoomOut()) {
			setZoom(zoom - .25);
		}
	}
	
	public boolean canZoomIn() {
		return zoom <= 7.75;
	}
	
	public boolean canZoomOut() {
		return zoom > .25;
	}

	public int getCurrentPage() {
		return currentPage;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public double getPageWidth() {
		return pageWidth;
	}
	
	public double getPageHeight() {
		return pageHeight;
	}
	
	public void render(Graphics2D g) throws Exception {
		g.scale(zoom, zoom);
		renderer.render(g, currentPage);
	}
	
	public void render(Graphics2D g, int pageNumber) throws Exception {
		g.scale(zoom, zoom);
		renderer.render(g, pageNumber);
	}
	
	public void render(Graphics2D g, double zoom) throws Exception {
		g.scale(zoom, zoom);
		renderer.render(g, currentPage);
	}
	
	public void render(Graphics2D g, int pageNumber, double zoom) throws Exception {
		g.scale(zoom, zoom);
		renderer.render(g, pageNumber);
	}
}
