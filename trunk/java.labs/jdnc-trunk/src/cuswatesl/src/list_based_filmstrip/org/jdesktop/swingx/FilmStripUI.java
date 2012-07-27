package org.jdesktop.swingx;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.FilmStrip.CenterStyle;
import org.jdesktop.swingx.FilmStrip.ScrollMode;
import org.jdesktop.swingx.ImageSource.State;

/**
 * The UIDelegate that paints the FilmStrip and listens for 
 * event on it. This delegate cannot be shared across 
 * different instances of the FilmStrip component
 * @author Nick
 */
public class FilmStripUI extends BasicListUI {

	private FilmStrip filmStrip;
	
	/**
	 * creates the UI for the given component
	 * @param c
	 * @return
	 */
	public static ComponentUI createUI(JComponent c) {
		return new FilmStripUI((FilmStrip) c);
	}
	
	/**
	 * Creates a new UI and binds to the given FilmStrip
	 * @param filmStrip
	 */
	public FilmStripUI(FilmStrip filmStrip) {
		super();
		this.filmStrip = filmStrip;
	}
	
	@Override
	protected void installListeners() {
		super.installListeners();
		filmStrip.addPropertyChangeListener("fixedCellWidth", imageDimensionPcl);
		filmStrip.addPropertyChangeListener("fixedCellHeight", imageDimensionPcl);
		filmStrip.addPropertyChangeListener("cellInsets", imageDimensionPcl);
		filmStrip.addPropertyChangeListener("backgroundPaint", bkgPaintPcl);
		filmStrip.getSelectionModel().addListSelectionListener(this.centererListener);
		filmStrip.addHierarchyListener(hl);
		filmStrip.addMouseListener(inoutListener);
		filmStrip.addMouseMotionListener(mml);
	}
	
	@Override
	protected void uninstallListeners() {
		super.uninstallListeners();
		filmStrip.removePropertyChangeListener("fixedCellWidth", imageDimensionPcl);
		filmStrip.removePropertyChangeListener("fixedCellHeight", imageDimensionPcl);
		filmStrip.removePropertyChangeListener("cellInsets", imageDimensionPcl);
		filmStrip.removePropertyChangeListener("backgroundPaint", bkgPaintPcl);
		filmStrip.removeHierarchyListener(hl);
		filmStrip.removeMouseListener(inoutListener);
		filmStrip.removeMouseMotionListener(mml);
	}
	
	@Override
	protected void installDefaults() {
		super.installDefaults();
		FilmStrip filmStrip = (FilmStrip) list;
		filmStrip.setVisibleRowCount(1);
		filmStrip.setScrollMode((ScrollMode) UIManager.get("FilmStrip.defaultScrollMode"));
		filmStrip.setCenterStyle((CenterStyle) UIManager.get("FilmStrip.defaultCenterStyle"));
		filmStrip.setProgressPainter((ProgressPainter) UIManager.get("FilmStrip.defaultProgressPainter"));
		filmStrip.setFixedCellHeight(UIManager.getInt("FilmStrip.defaultCellHeight"));
		filmStrip.setFixedCellWidth(UIManager.getInt("FilmStrip.defaultCellWidth"));
		filmStrip.setImageScaler((ImageScaler) UIManager.get("FilmStrip.defaultImageScaler"));
		filmStrip.setCellInsets(UIManager.getInsets("FilmStrip.defaultInsets"));		
	}
	
	@Override
	public void update(Graphics gr, JComponent c) {
		Graphics2D g = (Graphics2D) gr;
		
		Paint paint = filmStrip.getBackgroundPaint();
		Rectangle clip = g.getClipBounds();
		
		// paint background if specific paint is set
		if (paint != null) {
			g.clearRect(clip.x, clip.y, clip.width, clip.height);
			g.setPaint(paint);
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			paint(gr, c);
		} else {
			// g.clearRect(clip.x, clip.y, clip.width, clip.height);
			super.update(g, c);
		}
	}
	
	@Override
	protected void paintCell(Graphics g, final int index, Rectangle rowBounds,
			ListCellRenderer cellRenderer, ListModel dataModel,
			ListSelectionModel selModel, int leadIndex) {

		boolean mouseOver = index == rollover;
		((FilmStripCellRenderer) cellRenderer).setRollover(mouseOver);
		
		// allow the parent to paint the cell, after this we paint our progressRenderer
		super.paintCell(g, index, rowBounds, cellRenderer, dataModel, selModel,
						leadIndex);

		
		ImageSource src = (ImageSource) dataModel.getElementAt(index);
		BufferedImage img = src.getCachedImage("FilmStrip.preview");
		
		ProgressPainter progressPainter = filmStrip.getProgressPainter();
		
		Integer progress = filmStrip.getImageSourceProgress(src);
		boolean selected = selModel.isSelectedIndex(index);
		Rectangle bounds = getCellBounds(list, index, index);
		if (progressPainter != null && progress != null && src.getState() == State.LOADING) {
			Graphics2D g2 = (Graphics2D) g.create();
			Rectangle rect = getCellBounds(list, index, index);
			g2.translate(rect.x, rect.y);
			progressPainter.paintProgress(g2, img, src, filmStrip, index, mouseOver, selected, new Dimension(bounds.width, bounds.height), progress);
		}

	}
	
	@Override
	public Dimension getPreferredSize(JComponent c) {
		Dimension sps = super.getPreferredSize(c);
		int h = filmStrip.getFixedCellHeight();
		int w = filmStrip.getFixedCellWidth();
		if (sps.height < h && sps.width < w) {
			return new Dimension(w, h);
		}
		if (sps.height < h) {
			return new Dimension(sps.width, h);
		}
		if (sps.width < w) {
			return new Dimension(w, sps.height);
		}
		return sps;
    }
	
	
	private void imageDimensionChanged() {
		Collection<ImageSource> imgs = filmStrip.getModel().getImages();
		for (ImageSource img : imgs) {
			img.removeCachedImage("FilmStrip.preview");
		}
		filmStrip.repaint();
	}
	
	private void cancelInvisibleImageSources() {
		for (ImageSource src : filmStrip.getModel().getImages()) {
			int idx = filmStrip.getModel().indexOf(src);
			if (!filmStrip.isCellVisible(idx)) {
				src.cancelTask("FilmStrip.preview");
			}
		}
	};

	private JViewport previousViewport;
	
	private HierarchyListener hl = new HierarchyListener() {
		public void hierarchyChanged(HierarchyEvent e) {
			if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) > 0) {
				Container np = e.getChangedParent();
				if (np instanceof JViewport) {
					JViewport vp = (JViewport) np;
					if (previousViewport != null) {
						previousViewport.removeChangeListener(viewportListener);
					}
					vp.addChangeListener(viewportListener);
					previousViewport = vp;
				}
			}
		};
	};
	
	private Animator centerer;
	boolean scrolling = false;
	
	/**
	 * Scrolls the selected index to the center of the surrounding scrollpane, if it exists
	 */
	void scrollToCenter() {
		final JScrollPane ssc = filmStrip.getSurroundingScrollPane();
		if (ssc == null) return;
		
		int idx = filmStrip.getSelectionModel().getLeadSelectionIndex();
		
		boolean centeringRequired = false;
		switch (filmStrip.getCenterStyle()) {
		case ALWAYS:
			centeringRequired = true;
			break;
		case ON_EXIT:
			if (! filmStrip.getVisibleRect().intersects(getCellBounds(filmStrip, idx, idx))) {
				centeringRequired = true;
			}
			break;
		case NEVER:
			centeringRequired = false;
			break;
		}
		
		if (centeringRequired) {
			Rectangle rect = getCellBounds(filmStrip, idx, idx);
			int tox = rect.x - (filmStrip.getVisibleRect().width - filmStrip.getFixedCellWidth()) / 2;
			int toy = rect.y - (filmStrip.getVisibleRect().height - filmStrip.getFixedCellHeight()) / 2;
			final JScrollBar barx = ssc.getHorizontalScrollBar();
			final JScrollBar bary = ssc.getVerticalScrollBar();
			
			switch (filmStrip.getScrollMode()) {
			case DEFAULT:
				barx.setValue(tox);
				bary.setValue(toy);
				break;
			case SMOOTH:
				if (centerer != null) {
					centerer.cancel();
				}
				centerer = new Animator(500);
				centerer.addTarget(new PropertySetter(barx, "value", tox));
				centerer.addTarget(new PropertySetter(bary, "value", toy));
				
				centerer.addTarget(new TimingTargetAdapter() {
					@Override
					public void begin() {
						scrolling = true;
					}
					@Override
					public void end() {
						cancelInvisibleImageSources();
						scrolling = false;
					}
				});
				centerer.setAcceleration(.4f);
				centerer.setDeceleration(.3f);
				centerer.start();
				break;
			default:
				break;
			}
		}
	}
	
	private ChangeListener viewportListener = new ChangeListener() {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			if (!scrolling) cancelInvisibleImageSources();
		}
	};
	
	private PropertyChangeListener imageDimensionPcl = new PropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			imageDimensionChanged();
		};
	};
	private PropertyChangeListener selectionModelPcl = new PropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			ListSelectionModel old = (ListSelectionModel) evt.getOldValue();
			if (old != null) {
				old.removeListSelectionListener(centererListener);
			}
			((ListSelectionModel) evt.getNewValue()).addListSelectionListener(centererListener);
		}
	};
	
	private ListSelectionListener centererListener = new ListSelectionListener() {
		public void valueChanged(javax.swing.event.ListSelectionEvent e) {
			scrollToCenter();
		};
	};
	
	private PropertyChangeListener bkgPaintPcl = new PropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			filmStrip.repaint();
		};
	};
	
	
	
	
	private int rollover = -1;
	private MouseMotionListener mml = new MouseMotionAdapter() {
		public void mouseMoved(java.awt.event.MouseEvent e) {
			int idx = getCellForMouseEvent(e);
			if (idx != rollover) {
				if (idx != -1) {
					filmStrip.repaint(getCellBounds(list, idx, idx));
				}
				if (rollover != -1) {
					filmStrip.repaint(getCellBounds(list, rollover, rollover));
				}
				rollover = idx;
			}
		}
		private int getCellForMouseEvent(MouseEvent e) {
			return filmStrip.locationToIndex(e.getPoint());
		}
	};
	private MouseListener inoutListener = new MouseAdapter() {
		
		public void mouseExited(java.awt.event.MouseEvent e) {
			if (rollover != -1) {
				filmStrip.repaint(getCellBounds(list, rollover, rollover));
				rollover = -1;
			}
		};
		
	};
	
}
