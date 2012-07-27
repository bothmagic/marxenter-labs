package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;

import org.apache.batik.ext.awt.LinearGradientPaint;
import org.jdesktop.swingx.FilmStrip.Orientation;
import org.jdesktop.swingx.FilmStripImageViewer.StripPlacement;

public class FilmStripImageViewerUI extends ComponentUI {

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
		Dimension previewDimension = filmStrip.getPreviewDimension();
		if (filmStrip.getOrientation() == Orientation.HORIZONTAL) {
			filmStrip.setBackgroundPaint(new LinearGradientPaint(0, 0, 0, previewDimension.height, 
						new float[]{0, .299f, .3f, 1}, new Color[]{Color.gray, Color.darkGray, Color.black, Color.DARK_GRAY}));
		} else {
			filmStrip.setBackgroundPaint(new LinearGradientPaint(0, 0, previewDimension.width, 0, 
					new float[]{0, .299f, .3f, 1}, new Color[]{Color.gray, Color.darkGray, Color.black, Color.DARK_GRAY}));
		}

	}
	
	private void reinstallComponents() {
		filmStripImageViewer.remove(imageViewer);
		filmStripImageViewer.remove(scrollPane);
		filmStripImageViewer.setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setViewportView(filmStrip);
		
		StripPlacement placement = filmStripImageViewer.getPlacement();
		switch (placement) {
		case NORTH:
			filmStrip.setOrientation(Orientation.HORIZONTAL);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1.0;
			gc.weighty = 0;
			gc.gridy = 0;
			gc.gridx = 1;
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			filmStripImageViewer.add(scrollPane, gc);
			break;
		case SOUTH:
			filmStrip.setOrientation(Orientation.HORIZONTAL);
			gc.fill = GridBagConstraints.HORIZONTAL;
			gc.weightx = 1.0;
			gc.weighty = 0;
			gc.gridy = 2;
			gc.gridx = 1;
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			filmStripImageViewer.add(scrollPane, gc);
			break;
		case EAST:
			filmStrip.setOrientation(Orientation.VERTICAL);
			gc.fill = GridBagConstraints.VERTICAL;
			gc.weightx = 0;
			gc.weighty = 1.0;
			gc.gridy = 1;
			gc.gridx = 2;
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			filmStripImageViewer.add(scrollPane, gc);
			break;
		case WEST:
			filmStrip.setOrientation(Orientation.VERTICAL);
			gc.fill = GridBagConstraints.VERTICAL;
			gc.weightx = 0;
			gc.weighty = 1.0;
			gc.gridy = 1;
			gc.gridx = 0;
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			filmStripImageViewer.add(scrollPane, gc);
			break;
		}
		gc.fill = GridBagConstraints.BOTH;
		gc.weighty = 1.0;
		gc.weightx = 1.0;
		gc.gridy = 1;
		gc.gridx = 1;
		filmStripImageViewer.add(imageViewer, gc);
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
			if (evt.getPropertyName().equals("previewDimension") || evt.getPropertyName().equals("orientation")) {
				installBackgroundPainter();
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
