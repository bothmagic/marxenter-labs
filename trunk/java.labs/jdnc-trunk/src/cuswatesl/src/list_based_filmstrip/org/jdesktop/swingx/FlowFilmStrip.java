package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import org.jdesktop.swingx.FilmStrip.CenterStyle;
import org.jdesktop.swingx.FilmStrip.ScrollMode;

/**
 * Simple component that wraps the filmstrip in a scrollpane, and
 * allows setting the layoutOrientation and other properties of the 
 * FilmStrip. This component works great if you want to show several
 * images in a Thumbnail overview. This is just a helper class to
 * help easily create a ThumbnailViewer. For most of the options of
 * the FilmStrip a delegating setter are provided, if another method
 * is requires use <code>getFilmStrip()</code>
 *  
 * @see #getFilmStrip()
 * @author Nick
 */
public class FlowFilmStrip extends JComponent {

	private FilmStrip filmStrip;
	private JScrollPane scrollPane;
	
	/**
	 * Initializes a FlowFilmStrip with an Horizontal wrapping 
	 * layoutOrientation
	 * @see #FlowFilmStrip(int)
	 * @see JList#setLayoutOrientation(int)
	 */
	public FlowFilmStrip() {
		this(JList.HORIZONTAL_WRAP);
	}
	
	public FlowFilmStrip(int layoutOrientation) {
		setLayout(new BorderLayout());
		this.filmStrip = new FilmStrip();
		this.filmStrip.setVisibleRowCount(-1);
		filmStrip.setLayoutOrientation(layoutOrientation);
		this.scrollPane = new JScrollPane(filmStrip);
		add(scrollPane);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				filmStrip.setBackgroundPaint(new GradientPaint(0, 0, Color.DARK_GRAY, 0, scrollPane.getViewport().getExtentSize().height, Color.BLACK));
			}
		} );
		setOpaque(true);
	}
	
	/**
	 * Returns the actual FilmStrip used by the FlowFilmStrip. this comes in handy
	 * when access is required to methods for which no delegate is available.
	 * @return the actual filmstrip in this component.
	 */
	public FilmStrip getFilmStrip() {
		return filmStrip;
	}

	public void setCellInsets(Insets cellInsets) {
		filmStrip.setCellInsets(cellInsets);
	}

	public void setCellRenderer(ListCellRenderer cellRenderer) {
		filmStrip.setCellRenderer(cellRenderer);
	}

	public void setCenterStyle(CenterStyle centerStyle) {
		filmStrip.setCenterStyle(centerStyle);
	}

	public void setFixedCellHeight(int height) {
		filmStrip.setFixedCellHeight(height);
	}

	public void setFixedCellWidth(int width) {
		filmStrip.setFixedCellWidth(width);
	}

	public void setImages(Collection<ImageSource> images) {
		filmStrip.setImages(images);
	}

	public void setImages(ImageSourceFactory factory) {
		filmStrip.setImages(factory);
	}

	public void setProgressPainter(ProgressPainter progressPainter) {
		filmStrip.setProgressPainter(progressPainter);
	}

	public void setScrollMode(ScrollMode scrollMode) {
		filmStrip.setScrollMode(scrollMode);
	}

	/**
	 * Sets the layoutOrientation. <code>JList.Vertical</code> is not accepted
	 * as a valid layoutOrientation and causes an IllegalArgumentException.
	 * @see JList#setLayoutOrientation(int)
	 * @param layoutOrientation
	 */
	public void setLayoutOrientation(int layoutOrientation) {
		if (layoutOrientation == JList.VERTICAL) throw new IllegalArgumentException("Vertical layout is not valid for a FlowFilmStrip");
		filmStrip.setLayoutOrientation(layoutOrientation);
	}
}
