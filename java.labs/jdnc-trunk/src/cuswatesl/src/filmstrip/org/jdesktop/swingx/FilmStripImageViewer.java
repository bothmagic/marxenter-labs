package org.jdesktop.swingx;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.UIManager;

import org.jdesktop.swingx.FilmStrip.CenterStyle;
import org.jdesktop.swingx.FilmStrip.ScrollMode;


/**
 * Combines an ImageViewer with the FilmStrip, supports
 * north, east, south and west placement of the filmstrip
 * @author Nick
 *
 */
public class FilmStripImageViewer extends JComponent {

	private static final String uiClassID = "FilmStripImageViewerUI";
	
	static {
		// set defaults used by the UI
		UIManager.put(uiClassID, FilmStripImageViewerUI.class.getName());
	}
	
	/**
	 * The location of the Strip
	 * @author Nick
	 */
	public enum StripPlacement { NORTH, EAST, SOUTH, WEST}
	
	private StripPlacement placement;
	
	public FilmStripImageViewer() {
		this(StripPlacement.SOUTH);
	}
	
	public FilmStripImageViewer(StripPlacement placement) {
		this.placement = placement;
		updateUI();
	}

	/**
	 * Returns the UIClassID, "FilmStripImageViewerUI" in this case.
	 */
    @Override
	public String getUIClassID() {
        return uiClassID;
    }
    
    /**
     * Resets the UI property with a value from the current look and feel.
     *
     * @see JComponent#updateUI
     */
    @Override
	public void updateUI() {
        setUI((FilmStripImageViewerUI) UIManager.getUI(this));
    }
	
	public FilmStripImageViewerUI getUI() {
        return (FilmStripImageViewerUI) ui;
    }
	
	public void setImages(ImageSourceFactory factory) {
		getFilmStrip().setImages(factory);
	}

	public void setImages(Collection<ImageSource> images) {
		getFilmStrip().setImages(images);
	}

	public Dimension getPreviewDimension() {
		return getFilmStrip().getPreviewDimension();
	}

	public void setPreviewDimension(Dimension previewDimension) {
		getFilmStrip().setPreviewDimension(previewDimension);
		revalidate();
	}

	public void setPlacement(StripPlacement placement) {
		StripPlacement old = getPlacement();
		this.placement = placement;
		firePropertyChange("placement", old, this.placement);
	}
	
	public StripPlacement getPlacement() {
		return placement;
	}
	
	public ScrollMode getScrollMode() {
		return getFilmStrip().getScrollMode();
	}

	public ImageSource getSelectedImage() {
		return getFilmStrip().getSelectedImage();
	}

	public void setScrollMode(ScrollMode scrollMode) {
		getFilmStrip().setScrollMode(scrollMode);
	}

	public CenterStyle getCenterStyle() {
		return getFilmStrip().getCenterStyle();
	}

	public void setCenterStyle(CenterStyle centerStyle) {
		getFilmStrip().setCenterStyle(centerStyle);
	}

	public boolean isScaleAsync() {
		return getViewer().isScaleAsync();
	}

	public boolean isScaleUp() {
		return getViewer().isScaleUp();
	}

	public void setScaleAsync(boolean scaleAsync) {
		getViewer().setScaleAsync(scaleAsync);
	}

	public void setScaleUp(boolean scaleUp) {
		getViewer().setScaleUp(scaleUp);
	}

	public FilmStripCellRenderer getCellRenderer() {
		return getFilmStrip().getCellRenderer();
	}

	public void setCellRenderer(FilmStripCellRenderer cellRenderer) {
		getFilmStrip().setCellRenderer(cellRenderer);
	}

	public ProgressPainter getProgressPainter() {
		return getFilmStrip().getProgressPainter();
	}

	public void setProgressPainter(ProgressPainter progressPainter) {
		getFilmStrip().setProgressPainter(progressPainter);
	}
	
	public void setCellInsets(Insets cellInsets) {
		getFilmStrip().setCellInsets(cellInsets);
	}
	
	public Insets getCellInsets() {
		return getFilmStrip().getCellInsets();
	}
	
	@Override
	public void add(Component comp, Object constraints) {
		System.out.println("FilmStripImageViewer.add()");
		super.add(comp, constraints);
	}
	
	protected FilmStrip getFilmStrip() {
		return getUI().getFilmStrip();
	}
	protected ImageSourceViewer getViewer() {
		return getUI().getImageViewer();
	}
	
}