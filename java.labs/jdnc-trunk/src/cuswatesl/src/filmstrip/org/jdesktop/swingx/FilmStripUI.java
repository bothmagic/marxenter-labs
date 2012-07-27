package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.FilmStrip.CenterStyle;
import org.jdesktop.swingx.FilmStrip.Orientation;
import org.jdesktop.swingx.FilmStrip.ScrollMode;
import org.jdesktop.swingx.ImageSource.State;

/**
 * The UIDelegate that paints the FilmStrip and listens for 
 * event on it. This delegate cannot be shared across 
 * different instances of the FilmStrip component
 * @author Nick
 */
public class FilmStripUI extends ComponentUI {

	/**
	 * The filmStrip we are delegating for.
	 */
	private final FilmStrip filmStrip;
	
	/**
	 * The current rollover index, -1 if no cell is hovering
	 */
	private int rollOverIdx = -1;
	
	/**
	 * The cellRendererPane used for rendering the cells
	 */
	private CellRendererPane cellRendererPane = new CellRendererPane();
	
	/**
	 * creates the UI for the goven component
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
		this.filmStrip = filmStrip;
	}
	
	@Override
	public void installUI(JComponent c) {
		
		installDefaults();
		
		filmStrip.setLayout(new BorderLayout());
		filmStrip.add(cellRendererPane, BorderLayout.CENTER);
		filmStrip.setFocusable(true);
		filmStrip.setFocusCycleRoot(true);
		
		installListeners();
		installInputActions();
	}
	
	/**
	 * Configures the filmstrip to its defaults
	 */
	protected void installDefaults() {
		
		filmStrip.setModel((FilmStripModel) UIManager.get("FilmStrip.defaultModel"));
		filmStrip.setScrollMode((ScrollMode) UIManager.get("FilmStrip.defaultScrollMode"));
		filmStrip.setCenterStyle((CenterStyle) UIManager.get("FilmStrip.defaultCenterStyle"));
		filmStrip.setOrientation((Orientation) UIManager.get("FilmStrip.defaultOrientation"));
		filmStrip.setProgressPainter((ProgressPainter) UIManager.get("FilmStrip.defaultProgressPainter"));
		filmStrip.setCellRenderer((FilmStripCellRenderer) UIManager.get("FilmStrip.defaultCellRenderer"));
		filmStrip.setPreviewDimension(UIManager.getDimension("FilmStrip.defaultPreviewDimension"));
		filmStrip.setImageScaler((ImageScaler) UIManager.get("FilmStrip.defaultImageScaler"));
		filmStrip.setCellInsets(UIManager.getInsets("FilmStrip.defaultInsets"));
		
		filmStrip.setFocusable(true);

	}
	
	/**
	 * Install listeners on the <code>FilmStrip</code>
	 */
	protected void installListeners() {
		filmStrip.addHierarchyListener(hl);
		installModelListeners(filmStrip.getModel());
		filmStrip.addPropertyChangeListener("model", modelPcl);
		installSelectionListeners(filmStrip.getSelectionModel());
		filmStrip.addMouseListener(ml);
		filmStrip.addMouseMotionListener(mml);
		filmStrip.addPropertyChangeListener("previewDimension", this.clearCachePcl);
		filmStrip.addPropertyChangeListener("cellInsets", this.clearCachePcl);
		filmStrip.addPropertyChangeListener("orientation", this.orientationPcl);
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		filmStrip.remove(this.cellRendererPane);
		uninstallListeners();
		uninstallInputActions();
	}

	/**
	 * uninstalls all installed listeners on the <code>FilmStrip</code>
	 */
	protected void uninstallListeners() {
		filmStrip.removeHierarchyListener(hl);
		uninstallModelListeners(filmStrip.getModel());
		filmStrip.removePropertyChangeListener("model", modelPcl);
		uninstallSelectionListeners(filmStrip.getSelectionModel());
		filmStrip.removeMouseListener(ml);
		filmStrip.removeMouseMotionListener(mml);
		filmStrip.removePropertyChangeListener("previewDimension", this.clearCachePcl);
		filmStrip.removePropertyChangeListener("cellInsets", this.clearCachePcl);
		filmStrip.removePropertyChangeListener("orientation", this.orientationPcl);
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
	public void paint(Graphics gr, JComponent c) {
		int idx = 0;
		Graphics2D g = (Graphics2D) gr;
		
		
		int width = getCellWidth(filmStrip);
		int height = getCellHeight(filmStrip);
		
		for (ImageSource src : filmStrip.getModel().getImages()) {
			
			final Rectangle rendererRect = filmStrip.getCellRect(idx);
			if (g.getClipBounds().intersects(rendererRect)) {
				BufferedImage img = src.getCachedImage("FilmStrip.preview");
				
				// if the img == null ask the imagesource to create a scaled image
				// and run repaint after its done
				if (img == null || src.getState() == State.IDLE) {
					src.createCachedImage("FilmStrip.preview", getImageWidth(filmStrip), getImageHeight(filmStrip), filmStrip.getImageScaler(), new Runnable() {
						public void run() {
							filmStrip.repaint(rendererRect);
						}
					}, false );
					img = src.getPlaceHolderImage();
				}
				
				boolean mouseOver = rollOverIdx == idx;
				boolean selected = filmStrip.getSelectionModel().getMinSelectionIndex() == idx;
				
				Component renderer = filmStrip.getCellRenderer().getCellRenderComponent(filmStrip, img, src, idx, width, height, filmStrip.getCellInsets(), mouseOver, selected);
				int xpos = 0;
				int ypos = 0;
				switch (filmStrip.getOrientation()) {
				case HORIZONTAL:
					xpos = idx * getCellWidth(filmStrip);
					break;
				case VERTICAL:
					ypos = idx * getCellHeight(filmStrip);
					break;
				}
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				cellRendererPane.paintComponent(g2d, renderer, filmStrip, xpos, ypos, width, height);
				
				ProgressPainter progressPainter = filmStrip.getProgressPainter();
				
				Integer progress = filmStrip.getImageSourceProgress(src);
				if (progressPainter != null && progress != null && src.getState() == State.LOADING) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.translate(xpos, ypos);
					progressPainter.paintProgress(g2, img, src, filmStrip, idx, mouseOver, selected, new Dimension(width, height), progress);
				}
			}
			
			if (! filmStrip.isCellVisible(idx)) {
				src.cancelTask();
			}
			
			idx ++;
		}
		
	}
	
	/**
	 * Installs the required listeners on the model
	 * @param model
	 */
	void installModelListeners(FilmStripModel model) {
		if (model != null) {
			model.addListDataListener(ldl);
		}
	}
	
	/**
	 * Removes installed selectionlisteners from the selectionmodel
	 * @param model
	 */
	void uninstallSelectionListeners(ListSelectionModel model) {
		if (model != null) {
			model.removeListSelectionListener(lsl);
		}
	}
	
	/**
	 * Installs SelectionListeners on the given model
	 * @param model
	 */
	void installSelectionListeners(ListSelectionModel model) {
		if (model != null) {
			model.addListSelectionListener(lsl);
		}
	}
	
	/**
	 * uninstalls listeners from the model
	 * @param model
	 */
	void uninstallModelListeners(FilmStripModel model) {
		if (model != null) {
			model.removeListDataListener(ldl);
		}
	}
	
	/**
	 * installs the possible actions into the actionmap and links them
	 * using the inputmap
	 */
	protected void installInputActions() {
		filmStrip.getActionMap().put("filmstrip.up", new UpAction());
		filmStrip.getActionMap().put("filmstrip.down", new DownAction());
		filmStrip.getActionMap().put("filmstrip.left", new LeftAction());
		filmStrip.getActionMap().put("filmstrip.right", new RightAction());
		InputMap im = filmStrip.getInputMap(JComponent.WHEN_FOCUSED);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "filmstrip.up");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "filmstrip.down");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "filmstrip.left");
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "filmstrip.right");
	}
	
	/**
	 * Removes all installed actions from the actionmap and
	 * the inputmap
	 */
	protected void uninstallInputActions() {
		filmStrip.getActionMap().remove("filmstrip.up");
		filmStrip.getActionMap().remove("filmstrip.down");
		filmStrip.getActionMap().remove("filmstrip.left");
		filmStrip.getActionMap().remove("filmstrip.right");
		InputMap im = filmStrip.getInputMap(JComponent.WHEN_FOCUSED);
		im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
		im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
		im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
		im.remove(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
	}
	
	/**
	 * Converts a MouseEvent to an index in the model
	 * @param e
	 * @return the index the mouseEvent was over, or -1 if it wasn't over any cell
	 */
	protected int mouseEventToModelIdx(MouseEvent e) {
		int idx = -1;
		if (filmStrip.getOrientation() == Orientation.HORIZONTAL) {
			idx = (e.getPoint().x) / getCellWidth(filmStrip);
		} else {
			idx = (e.getPoint().y) / getCellHeight(filmStrip);
		}
		if (idx >= filmStrip.getModel().getSize()) idx = -1;
		return idx;
	}

	private void imageDimensionChanged() {
		Collection<ImageSource> imgs = filmStrip.getModel().getImages();
		for (ImageSource img : imgs) {
			img.removeCachedImage("FilmStrip.preview");
		}
		filmStrip.repaint();
		reconfigureSurroundingScrollPane();
	}
	
	private void reconfigureSurroundingScrollPane() {
		// FIXME get scrollbar size from UI
		JScrollPane sp = filmStrip.getSurroundingScrollPane();
		Orientation orientation = filmStrip.getOrientation();
		if (sp != null) {
			int eh = 18;
			int ev = 18;
			switch (orientation) {
			case HORIZONTAL:
				eh = 0;
				break;
			case VERTICAL:
				ev = 0;
				break;
			}
			Dimension min = filmStrip.getMinimumSize();
			sp.setMinimumSize(new Dimension(min.width + eh, min.height + ev));
			Dimension ps = filmStrip.getPreferredSize();
			sp.setPreferredSize(new Dimension(ps.width + eh, ps.height + ev));
			Dimension max = filmStrip.getMaximumSize();
			sp.setMaximumSize(new Dimension(max.width + eh, max.height + ev));
		}
		filmStrip.revalidate();
		
	}
	
	
	protected int getCellWidth(FilmStrip fs) {
		return fs.getPreviewDimension().width;
	}
	protected int getCellHeight(FilmStrip fs) {
		return fs.getPreviewDimension().height;
	}
	protected int getImageWidth(FilmStrip fs) {
		Insets i = fs.getCellInsets();
		return fs.getPreviewDimension().width - (i.left + i.right);
	}
	protected int getImageHeight(FilmStrip fs) {
		Insets i = fs.getCellInsets();
		return fs.getPreviewDimension().height - (i.top + i.bottom);
	}
	
	/**
	 * The ListDataListener to be installed on the model
	 */
	private ListDataListener ldl = new ListDataListener() {
		public void contentsChanged(ListDataEvent e) {
			// revalidate to ensure dimension propagation up the component tree
			filmStrip.revalidate();
			filmStrip.repaint();
		}
		public void intervalAdded(ListDataEvent e) {
			int idx = e.getIndex0();
			filmStrip.repaint(filmStrip.getCellRect(idx));
		}
		public void intervalRemoved(ListDataEvent e) {
			int idx = e.getIndex0();
			filmStrip.repaint(filmStrip.getCellRect(idx));
		}
	};
	
	private PropertyChangeListener orientationPcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			reconfigureSurroundingScrollPane();
		};
	};
	
	private PropertyChangeListener clearCachePcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			imageDimensionChanged();
		};
	};
	
	/**
	 * The listener to be installed on the selectionModel
	 */
	private ListSelectionListener lsl = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			for (int x = e.getFirstIndex(); x <= e.getLastIndex(); x++) {
				filmStrip.repaint(filmStrip.getCellRect(x));
			}
			filmStrip.scrollToCenter();
		}
	};
	
	private PropertyChangeListener insetsPcl = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			filmStrip.revalidate();
		};
	};
	
	/**
	 * the propertyChangeListener that will listen for model changes
	 */
	private PropertyChangeListener modelPcl = new PropertyChangeListener(){
		public void propertyChange(PropertyChangeEvent evt) {
			// remove listener from the old model, install on the new model
			FilmStripModel oldModel = (FilmStripModel) evt.getOldValue();
			FilmStripModel newModel = (FilmStripModel) evt.getNewValue();
			uninstallModelListeners(oldModel);
			installModelListeners(newModel);
		}
	};
	
	/**
	 * Hierarchy listener that makes sure the actual size of the filmstrip
	 * will also be set on the scrollpane surrounding the filmstrip
	 */
	private HierarchyListener hl = new HierarchyListener() {
		public void hierarchyChanged(HierarchyEvent e) {
			// ensures the correct size of a possible scrollpane
			filmStrip.setPreviewDimension(filmStrip.getPreviewDimension());
		}
	};
	
	/**
	 * The mouseListener to handle mouseclicks
	 */
	private MouseListener ml = new MouseInputAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			int selected = mouseEventToModelIdx(e);
			filmStrip.requestFocusInWindow();
			if (selected < filmStrip.getModel().getSize()) {
				filmStrip.getSelectionModel().setSelectionInterval(selected, selected);
			}
			if (e.isPopupTrigger()) {
				doPopup(e);
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				doPopup(e);
			}
		};
		
		void doPopup(MouseEvent e) {
			int clickedOn = mouseEventToModelIdx(e);
			if (clickedOn == -1) return;
			ImageSource src = filmStrip.getImage(clickedOn);
			FilmStripUtils.showImagePopup(e, src);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			int old = rollOverIdx;
			rollOverIdx = -1;
			if (old != -1) {
				filmStrip.repaint(filmStrip.getCellRect(old));
			}
		}
	};
	
	/**
	 * MouseMotionListener, listens for mouse movement
	 */
	private MouseMotionListener mml = new MouseMotionAdapter(){
		@Override
		public void mouseMoved(MouseEvent e) {
			int over = mouseEventToModelIdx(e);

			if (rollOverIdx != over) {
				if (rollOverIdx != -1) {
					filmStrip.repaint(filmStrip.getCellRect(rollOverIdx));
				}
				if (over != -1) {
					filmStrip.repaint(filmStrip.getCellRect(over));
				}
				rollOverIdx = over;
			}
		}
	};
	
	/**
	 * Selects the next element in the model
	 */
	void selectNext() {
		int idx = filmStrip.getSelectionModel().getMinSelectionIndex();
		if (idx < filmStrip.getModel().getSize() - 1) {
			filmStrip.getSelectionModel().setSelectionInterval(idx + 1, idx + 1);
		}
	}
	
	/**
	 * Selects the previous element in the model
	 */
	void selectPrevious() {
		int sel = filmStrip.getSelectionModel().getMinSelectionIndex();
		if (sel > 0) {
			filmStrip.getSelectionModel().setSelectionInterval(sel - 1, sel - 1);
		}
	}
	
	/**
	 * action to perform when down is pushed
	 */
	class DownAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			Orientation orientation = filmStrip.getOrientation();
			if (orientation == Orientation.VERTICAL) {
				selectNext();
			}
		}
	}
		
	/**
	 * action to perform when up is pushed
	 */
	class UpAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			Orientation orientation = filmStrip.getOrientation();
			if (orientation == Orientation.VERTICAL) {
				selectPrevious();
			}
		}
	}
	
	/**
	 * action to perform when left is pushed
	 */
	class LeftAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			Orientation orientation = filmStrip.getOrientation();
			if (orientation == Orientation.HORIZONTAL) {
				selectPrevious();
			}
		}
	}
	
	/**
	 * action to perform when right is pushed
	 */
	class RightAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			Orientation orientation = filmStrip.getOrientation();
			if (orientation == Orientation.HORIZONTAL) {
				selectNext();
			}
		}
	}

	
}
