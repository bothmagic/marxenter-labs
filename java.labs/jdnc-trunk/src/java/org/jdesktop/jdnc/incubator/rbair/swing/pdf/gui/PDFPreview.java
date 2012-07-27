/*
 * $Id: PDFPreview.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.jdnc.incubator.rbair.swing.pdf.PDFDocument;

/**
 * @author Richard Bair
 */
public class PDFPreview extends JPanel {
	private PDFViewer editor;
	private PDFDocument doc;
	private JScrollPane scrollPane;
	/**
	 * 
	 */
	public PDFPreview() {
		initGui();
	}
	
	private void initGui() {
		try {
			doc = new PDFDocument(new File("/data/downloads/FirstJasper.pdf"));
			doc.setPage(1);
			doc.setZoom(1.0);
			editor = new PDFViewer(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JPanel buttonPanel = new JPanel(new GridBagLayout());
		JButton print = new JButton("Print");
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PrinterJob printJob = PrinterJob.getPrinterJob();
					printJob.setPrintable(new Printable() {
					  public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
					    if (pageIndex >= doc.getPageCount()) {
					      return(NO_SUCH_PAGE);
					    } else {
					      Graphics2D g2d = (Graphics2D)g;
					      try {
					      	doc.render(g2d, pageIndex + 1, 1.0);
					      } catch (Exception e) {
					      	e.printStackTrace();
					      	return NO_SUCH_PAGE;
					      }
					      return(PAGE_EXISTS);
					    }
					  }
					});
					printJob.print();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		buttonPanel.add(print, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		JButton zoomIn = new JButton("+");
		zoomIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double oldZoom = doc.getZoom();
				doc.zoomIn();
				adjustScrollBars(oldZoom);
			}
		});
		buttonPanel.add(zoomIn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		JButton zoomOut = new JButton("-");
		zoomOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double oldZoom = doc.getZoom();
				doc.zoomOut(); 
				adjustScrollBars(oldZoom);
			}
		});
		buttonPanel.add(zoomOut, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		JButton nextPage = new JButton("next");
		nextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doc.nextPage();
				validate();
				scrollPane.getHorizontalScrollBar().setValue(0);
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		buttonPanel.add(nextPage, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		JButton prevPage = new JButton("previous");
		prevPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doc.prevPage();
				validate();
			}
		});
		buttonPanel.add(prevPage, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
		
		setLayout(new BorderLayout());
		add(buttonPanel, BorderLayout.NORTH);
		scrollPane = new JScrollPane(editor);
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Utility method to adjust the scroll bars so that when you zoom in or zoom out, whatever the
	 * center "pixel" is in the view will remain the center "pixel" after zooming. I say pixel in quotes
	 * because its not really a pixel, but an infinately small point in space.
	 * @param originalZoomFactor
	 */
	private void adjustScrollBars(double originalZoomFactor) {
		double zoomFactor = doc.getZoom();
		if (zoomFactor != originalZoomFactor && originalZoomFactor > 0 && zoomFactor > 0) {
			int scrollBarPosition = scrollPane.getVerticalScrollBar().getValue();
			double height = doc.getPageHeight() * zoomFactor;
			double oldHeight = doc.getPageHeight() * originalZoomFactor;
			int centerPixelY = scrollBarPosition + (scrollPane.getViewport().getHeight()/2);
			int newCenterPixelY = (int)(centerPixelY + (centerPixelY * ((height - oldHeight)/oldHeight)));
			scrollBarPosition = scrollBarPosition + (newCenterPixelY - centerPixelY); 
			scrollPane.getVerticalScrollBar().setValue(scrollBarPosition);
			
			scrollBarPosition = scrollPane.getHorizontalScrollBar().getValue();
			double width = doc.getPageWidth() * zoomFactor;
			double oldWidth = doc.getPageWidth() * originalZoomFactor;
			int centerPixelX = scrollBarPosition + (scrollPane.getViewport().getWidth()/2);
			int newCenterPixelX = (int)(centerPixelX + (centerPixelX * ((width - oldWidth)/oldWidth)));
			scrollBarPosition = scrollBarPosition + (newCenterPixelX - centerPixelX);
			scrollPane.getHorizontalScrollBar().setValue(scrollBarPosition);
		}
		
	}
}
