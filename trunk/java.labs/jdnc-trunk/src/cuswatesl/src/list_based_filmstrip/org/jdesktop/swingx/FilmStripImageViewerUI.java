package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;

import org.apache.batik.ext.awt.LinearGradientPaint;
import org.jdesktop.swingx.FilmStripImageViewer.StripPlacement;

public class FilmStripImageViewerUI extends javax.swing.plaf.PanelUI {

	public static ComponentUI createUI(JComponent c) {
		return new FilmStripImageViewerUI((FilmStripImageViewer) c);
	}
	
	final FilmStripImageViewer filmStripImageViewer;
	final FilmStrip filmStrip;
	final ImageSourceViewer imageViewer;
	JScrollPane scrollPane;
	
	public FilmStripImageViewerUI(FilmStripImageViewer filmStripImageViewer) {
		this.filmStripImageViewer = filmStripImageViewer;
		this.filmStrip = new FilmStrip();
		this.imageViewer = new ImageSourceViewer();
		this.scrollPane = new JScrollPane();
	}
	
	@Override
	public void installUI(JComponent c) {
		installDefaults();
		installListeners();
		installBackgroundPainter();
		reinstallComponents();
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		filmStripImageViewer.remove(imageViewer);
		filmStripImageViewer.remove(scrollPane);
		uninstallListeners();
	}
	
	private void installDefaults() {
		if (filmStripImageViewer.getPlacement() == null) {
			filmStripImageViewer.setPlacement(StripPlacement.SOUTH);
		}
	}
	
	private void installListeners() {
		filmStripImageViewer.addPropertyChangeListener("placement", this.placementPCL);
		filmStrip.addPropertyChangeListener(this.bgPCL);
		filmStrip.addListSelectionListener(this.selectionForwarder);
	}
	
	private void uninstallListeners() {
		filmStripImageViewer.removePropertyChangeListener("placement", this.placementPCL);
		filmStrip.removePropertyChangeListener(this.bgPCL);
		filmStrip.removeListSelectionListener(this.selectionForwarder);
	}
	
	
	private void installBackgroundPainter() {
		int height = filmStrip.getFixedCellHeight();
		int width = filmStrip.getFixedCellWidth();
		if (filmStrip.getLayoutOrientation() == JList.HORIZONTAL_WRAP) {
			filmStrip.setBackgroundPaint(new LinearGradientPaint(0, 0, 0, height, 
						new float[]{0, .299f, .3f, 1}, new Color[]{Color.gray, Color.darkGray, Color.black, Color.DARK_GRAY}));
		} else {
			filmStrip.setBackgroundPaint(new LinearGradientPaint(0, 0, width, 0, 
					new float[]{0, .299f, .3f, 1}, new Color[]{Color.gray, Color.darkGray, Color.black, Color.DARK_GRAY}));
		}
	}
	
	private void reinstallComponents() {
		filmStripImageViewer.remove(imageViewer);
		filmStripImageViewer.remove(scrollPane);
		// simply use a borderlayout, as this does actually exactly wat we want
		// and i just love the constraintnames north east south and west
		filmStripImageViewer.setLayout(new BorderLayout());
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setViewportView(filmStrip);
		
		StripPlacement placement = filmStripImageViewer.getPlacement();
		switch (placement) {
		case NORTH:
			filmStrip.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			filmStripImageViewer.add(scrollPane, BorderLayout.NORTH);
			break;
		case SOUTH:
			filmStrip.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			//filmStripImageViewer.add(scrollPane, gc);
			filmStripImageViewer.add(scrollPane, BorderLayout.SOUTH);
			break;
		case EAST:
			filmStrip.setLayoutOrientation(JList.VERTICAL);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			filmStripImageViewer.add(scrollPane, BorderLayout.EAST);
			break;
		case WEST:
			filmStrip.setLayoutOrientation(JList.VERTICAL);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			filmStripImageViewer.add(scrollPane, BorderLayout.WEST);
			break;
		}
		filmStripImageViewer.add(imageViewer, BorderLayout.CENTER);
		filmStripImageViewer.revalidate();
	}

	
	private PropertyChangeListener placementPCL = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			reinstallComponents();
			filmStripImageViewer.revalidate();
		};
	};
	private PropertyChangeListener bgPCL = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("layoutOrientation") || 
					evt.getPropertyName().equals("fixedCellWidth") || 
					evt.getPropertyName().equals("fixedCellHeight")) {
				installBackgroundPainter();
				filmStripImageViewer.revalidate();
			}
		}
	};
	private ListSelectionListener selectionForwarder = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			imageViewer.setImage(filmStrip.getSelectedImage());
		}
	};
	
	public FilmStrip getFilmStrip() {
		return filmStrip;
	}
	public ImageSourceViewer getImageViewer() {
		return imageViewer;
	}
	
}
