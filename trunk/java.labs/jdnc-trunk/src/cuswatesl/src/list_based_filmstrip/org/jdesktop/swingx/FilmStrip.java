package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.filmstrip.AbstractFilmStripCellRenderer;
import org.jdesktop.swingx.filmstrip.AbstractFilmStripModel;
import org.jdesktop.swingx.filmstrip.DefaultFilmStripCellRenderer;
import org.jdesktop.swingx.filmstrip.DefaultFilmStripModel;
import org.jdesktop.swingx.filmstrip.DefaultImageScaler;
import org.jdesktop.swingx.filmstrip.DefaultProgressPainter;
import org.jdesktop.swingx.filmstrip.MemoryImageSourceFactory;
import org.jdesktop.swingx.filmstrip.ProgressHelper;

/**
 * A component that displays a list of Images and allows the user to select
 * one of these. A separate model, {@code FilmStripModel}, maintains the
 * contents of the list.
 * <p>
 * The FilmStrip can be used easily to display images using an ImageSourceFactory,
 * the FilmStrip will ask the ImageSourceFactory to start producing ImageSources
 * and display these as they become available.<br>
 * It is also possible to set a Collection of BufferedImages. In this case an
 * ImageSourceFactory will be created to display these images. But in general 
 * this approach is not recommended because of the large amount of memory that
 * these images will use. A better approuch would be to create an 
 * ImageSourceFactory that loads the images you manually loaded and use that one
 * so that the images will be cached and loaded on demand.<br>
 * Another approach is to add ImageSources to the FilmStrip, these will then be
 * added to the model and loaded when demanded. For this purpose the classes
 * URLImageSource and FileImageSource exists.<br>
 * In most cases it will not be nessesary to set a model by yourself, but setting
 * an ImageSourceFactory to dynamically add the and create the ImageSources.<br>
 * <p>
 * For the purpose of renderering the images a custom cellrenderer can be set,
 * this way it is possible to render the images yourself. If you wish to do some
 * custom painting the best approch would be to extend the 
 * <code>AbstractFilmStripCellRenderer</code>. A simple implementation could 
 * be something like 
 * <pre>
 * FilmStripCellRenderer myRenderer = new AbstractFilmStripCellRenderer() {
 * 		@Override
 * 		protected void paintBackground(Graphics2D g, BufferedImage scaledImage,
 *				ImageSource imageSource, FilmStrip strip, int idx,
 *			boolean mouseOver, boolean selected, Dimension size) {
 *		// no implementation as we do not intend to paint a background
 *		}
 *		
 *		@Override
 *		protected void paintForeground(Graphics2D g2,
 *				BufferedImage scaledImage, ImageSource imageSource,
 *				FilmStrip strip, int idx, boolean mouseOver, boolean selected,
 *				Dimension size) {
 *			
 *			// a correctly scaled image is provided by the UI delegate, if
 *			// it's ready.
 *			BufferedImage img = scaledImage;
 *			
 *			// if the image cannot be retrieved ask the imagesource for an
 *			// image to use
 *			if (imageSource.isUnretrievable()) {
 *				img = imageSource.getUnRetrievableImageIcon();
 *			}
 *			// if no image was provided it was not yet loaded, draw a placeholder
 *			if (img == null) {
 *				img = imageSource.getPlaceHolderImage();
 *			} 
 *			
 *			// don't spoil the provided graphics object
 *			Graphics2D g2d = (Graphics2D) g2.create();
 *			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 *			
 *			// translate to where we can just paint the image
 *			int w = img.getWidth();
 *			int h = img.getHeight();
 *			g2d.translate(size.width / 2, size.height / 2);
 *			g2d.translate(w / -2, h / -2);
 *
 *			// create a color based on the state of the cell
 *			Color base;
 *			if (selected) {
 *				base = UIManager.getColor("Table.selectionBackground");
 *			} else if (mouseOver) {
 *				base = UIManager.getColor("Table.selectionBackground").darker();
 *			} else {
 *				base = Color.DARK_GRAY;
 *			}
 *			// draw a border around the image
 *			g2d.setColor(base);
 *			g2d.drawRect(- 1, - 1, w + 1, h + 1);
 *			g2d.setColor(base.brighter());
 *			g2d.drawRect(-2, -2, w + 3, h + 3);
 *			// draw the image in it
 *			g2d.drawImage(img, 0, 0, null);
 *		}
 *	};
 * 	filmStrip.setCellRenderer(myCellRenderer);
 * </pre>
 * As opposed to the JList, the size of the cells is not determined by the 
 * size of the CellRendererComponents, but by setting fixedCellWidth an 
 * fixedCellHeight, and the image provided to the renderer by the delegate 
 * has this same dimension minus the cellInsets dimension. The Delegate 
 * will prevent aspect ratio loss, so the provided scaledImage actual 
 * dimensions may differ. It is up to the cellRenderer to decide how to fill
 * the possible gap. When a cell is rendered the width and height will be 
 * provided as parameters.
 * </p>
 * <p>
 * For the scaling of the image an instance of an ImageScaler is used. The
 * imagescaler simply implements an algorithm to scale the image to the 
 * needed size. This ImageScaler can be set to provide an alternate way of
 * scaling the image. Note that this scaler does not have to be optimized
 * for speed as the scaling is done asynchoniously by the ImageSource using 
 * the ImageScaler, on a background thread.
 * </p>
 * <p>
 * The selection state of a {@code FilmStrip} is managed by a separate
 * model, an instance of {@code ListSelectionModel}. {@code FilmStrip} is
 * initialized with a selection model on construction, and also contains
 * methods to query or set this selection model. At this moment only single
 * selection is supported, other settings will be ignored.
 * </p>
 * <p>
 * A correct {@code ListSelectionModel} implementation notifies the set of
 * {@code javax.swing.event.ListSelectionListener}s that have been added to it
 * each time a change to the selection occurs. These changes are characterized
 * by a {@code javax.swing.event.ListSelectionEvent}, which identifies the range
 * of the selection change.
 * </p>
 * <p>
 * Using a <code>ListSelectionListener</code> on the SelectionModel of the 
 * FilmStrip it is possible to track changes in the selection. This is also the
 * way FilmStripImageViewer tracks selection changes.
 * </p>
 * <p>
 * Responsibility for listening to selection changes in order to keep the list's
 * visual representation up to date lies with the FilmStrip's {@code FilmStripUI}.
 * </p>
 * <p>
 * The filmstrip doesn't implement scrolling directly. To create a filmstrip that
 * scrolls, make it the viewport view of a {@code JScrollPane}. For example:
 * <pre>
 * JScrollPane scrollPane = new JScrollPane(myFilmStrip);
 *
 * // Or in two steps:
 * JScrollPane scrollPane = new JScrollPane();
 * scrollPane.getViewport().setView(myFilmStrip);
 * </pre>
 * The filmStrip will depending on it's settings try to center the selected image.
 * For this purpose it will lookup the <code>JScrollPane</code> and alter the 
 * positions of it's scrollbars. <br>
 * See {@code #setCenterStyle(filmstrip.FilmStrip.CenterStyle)} for more information
 * on this. 
 * </p>
 * <p>
 * FilmStrip doesn't provide any special handling of double or triple
 * (or N) mouse clicks, but it's easy to add a {@code MouseListener} if you
 * wish to take action on these events.
 * </p>
 * <p>
 * When a cell is being rendered it may still be loading the image behind it. 
 * For this purpose the UIDelegate uses a ProgressPainter to paint an infinite 
 * progress indicator over the rendered cell. For example to paint a progress
 * indicator as found in some menu's of the Nintendo WII, the following code 
 * can be used:
 * <pre>
 * 
 *	public void paintProgress(Graphics2D g2, BufferedImage scaledImage,
 *    	ImageSource imageSource, FilmStrip strip, int idx,
 *    	boolean mouseOver, boolean selected, Dimension size, int progress) {
 *
 *		int cx = size.width / 2;
 *		int cy = size.height / 2;
 *
 *		int xl = cx - (int) (boxSize * 1.5 + margin);
 *		int xc = cx - (int) (boxSize * .5);
 *		int xr = cx + (int) (boxSize * .5 + margin);
 *		int yt = cy - (int) (boxSize * 1.5 + margin);
 *		int yc = cy - (int) (boxSize * .5);
 *		int yb = cy + (int) (boxSize * .5 + margin);
 *    	
 * 		int skip = (progress / 4) % 8;
 *    	
 * 		// topleft
 *		if (skip != 0) paintRect(g2, xl, yt, boxSize, boxSize);
 *		// topcenter
 *		if (skip != 1) paintRect(g2, xc, yt, boxSize, boxSize);
 * 		// topright
 * 		if (skip != 2) paintRect(g2, xr, yt, boxSize, boxSize);
 * 		// left
 *		if (skip != 7) 	paintRect(g2, xl, yc, boxSize, boxSize);
 *		// center
 *		paintRect(g2, xc, yc, boxSize, boxSize);
 * 		// right
 *		if (skip != 3) paintRect(g2, xr, yc, boxSize, boxSize);
 *		// bottomleft
 *		if (skip != 6) paintRect(g2, xl, yb, boxSize, boxSize);
 *		// bottomcenter
 *		if (skip != 5) paintRect(g2, xc, yb, boxSize, boxSize);
 *		// bottomright
 *		if (skip != 4) paintRect(g2, xr, yb, boxSize, boxSize);
 *	}
 * 
 * </pre>
 * This will paint 9 squares, skipping one of them each time. The contents
 * of the paintRect method has been left out to simplify the exampe. see
 * WiiProgressPainter for the full implementation, the <code>paintRect</code>
 * method draws a simple shaded rectangle.
 * </p>
 * <p>
 * <strong>Warning:</strong> Swing is not thread safe. For more
 * information see <a
 * href="package-summary.html#threading">Swing's Threading
 * Policy</a>. This also applies on the FilmStrip, only query and alter
 * it's state on the Event Dispatch Thread.
 * </p>
 * @see FilmStripModel
 * @see AbstractFilmStripModel
 * @see DefaultFilmStripModel
 * @see ListSelectionModel
 * @see DefaultListSelectionModel
 * @see FilmStripCellRenderer
 * @see AbstractFilmStripCellRenderer
 * @see #setScrollMode(org.jdesktop.swingx.FilmStrip.ScrollMode)
 * @see FilmStrip.ScrollMode
 *
 * @author Nick
 */
public class FilmStrip extends JList {
	
	private static final long serialVersionUID = -1816149225159412170L;

	static {
		// set defaults used by the UI
		UIManager.put("FilmStripUI", FilmStripUI.class.getName());
		UIManager.put("FilmStrip.defaultModel", new DefaultFilmStripModel());
		UIManager.put("FilmStrip.defaultScrollMode", ScrollMode.SMOOTH);
		UIManager.put("FilmStrip.defaultCenterStyle", CenterStyle.ALWAYS);
		UIManager.put("FilmStrip.defaultProgressPainter", new DefaultProgressPainter());
		UIManager.put("List.cellRenderer", new DefaultFilmStripCellRenderer());
		UIManager.put("FilmStrip.defaultCellWidth", 90);
		UIManager.put("FilmStrip.defaultCellHeight", 90);
		UIManager.put("FilmStrip.defaultImageScaler", new DefaultImageScaler());
		UIManager.put("FilmStrip.selectionBackground", new Color(184, 207, 229));
		UIManager.put("FilmStrip.defaultInsets", new Insets(10, 10, 10, 10));
		//184,g=207,b=229
	}

	private static final String uiClassID = "FilmStripUI";
	
	
	/**
	 * Possible scrollmodes for the <code>FilmStrip</code>
	 * @author Nick
	 */
	public enum ScrollMode { 
		/** Use smooth scrolling */ 
		SMOOTH, 
		/** Use non-smooth scrolling */ 
		DEFAULT 
	}
	
	/**
	 * Possible centerstyles for the <code>FilmStrip</code>
	 * @author Nick
	 */
	public enum CenterStyle {
		/** Allways try to center the selected image */
		ALWAYS, 
		/** Center the selected image when the selection leaves the visible part of the component */
		ON_EXIT, 
		/** Never autocenter the selected image */
		NEVER 
	}
	
	// FIXME how to do this little trick without making a ref to the imageSource
	private Map<ImageSource, Integer> progressMap = new HashMap<ImageSource, Integer>();

	// bound properties; will be initialized by the UI delegate
	private ScrollMode scrollMode;
	private CenterStyle centerStyle;
	private ProgressPainter progressPainter;
	private ImageScaler imageScaler;
	private Paint backgroundPaint;
	private Insets cellInsets;

	/**
	 * Creates a <code>FilmStrip</code> with a default model
	 */
	public FilmStrip() {
		super(new DefaultFilmStripModel());
	}
	
	/**
	 * Create a <code>FilmStrip</code> with the given model
	 * @param model the <code>FilmStripModel</code> to use.
	 */
	public FilmStrip(FilmStripModel model) {
		super(model);
		// make sure all set properties are properly propagated to the UI delegate
		updateUI();
	}
	
	/**
	 * Returns the UIClassID, "FilmStripUI" in this case.
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
    	super.updateUI();
        setUI((FilmStripUI) UIManager.getUI(this));
    }
	
	/**
	 * @param idx 
	 * @return the image at index <code>idx</code>
	 */
	ImageSource getImage(int idx) {
		return getModel().getElementAt(idx);
	}

	/**
	 * Sets the images through a <code>ImageSourceFactory</code> on this
	 * factory loadImages will be called with a publisher that will add
	 * each <code>ImageSource</code> to the <code>FilmStrip</code>. 
	 * Currently displayed images will be removed from the 
	 * <code>FilmStrip</code>
	 * @see #addImage(ImageSource)
	 * @param factory
	 */
	public void setImages(final ImageSourceFactory factory) {
		if (factory == null) throw new IllegalArgumentException("factory == null");
		for (ImageSource src : this.getModel().getImages()) {
			src.clearCache();
		}
		this.progressMap.clear();
		this.getModel().removeAll();
		getSelectionModel().clearSelection();
		factory.loadImages(new ImageSourcePublisher() {
			public void publish(ImageSource src) {
				addImage(src);
			}
		} );
	}
	
	/**
	 * Sets the ImageSources to display. These imageSource will be wrapped in
	 * a <code>MemoryImageSourceFactory</code> and this factory will be set
	 * on the <code>FilmStrip</code>
	 * @see #setImages(ImageSourceFactory)
	 * @param images
	 */
	public void setImages(Collection<ImageSource> images) {
		this.setImages(new MemoryImageSourceFactory(images));
	}
	
	/**
	 * Adds an imageSource to the <code>FilmStrip</code>
	 * @param imageSrc
	 * @throws IllegalArgumentException if <code>image == null</code>
	 */
	public void addImage(final ImageSource imageSrc) {
		if (imageSrc == null) throw new IllegalArgumentException("image == null");
		this.getModel().addImage(imageSrc);
		final int idx = getModel().indexOf(imageSrc);
		if (progressMap.containsKey(imageSrc)) throw new IllegalStateException("progressMap.containsKey(imageSrc)");
		new ProgressHelper(new Runnable() {
			public void run() {
				int value = 0;
				if (progressMap.containsKey(imageSrc)) {
					value = progressMap.get(imageSrc);
				}
				progressMap.put(imageSrc, value + 1);
				repaint(getCellBounds(idx, idx));
			}
		}, imageSrc);
	}
	
	/**
	 * Sets the style in which the <code>FilmStrip</code> centers the selected
	 * Image
	 * @see FilmStrip.CenterStyle
	 * @param centerStyle
	 * @throws IllegalArgumentException if <code>centerStyle == null</code>
	 */
	public void setCenterStyle(CenterStyle centerStyle) {
		if (centerStyle == null) throw new IllegalArgumentException("centerStyle == null");
		CenterStyle old = this.centerStyle;
		this.centerStyle = centerStyle;
		firePropertyChange("centerStyle", old, this.centerStyle);
	}
	
	/**
	 * @return the currently set CenterStyle
	 */
	public CenterStyle getCenterStyle() {
		return centerStyle;
	}
	
	/**
	 * Sets the <code>ScrollMode</code> of the <code>FilmStrip</code>
	 * @param scrollMode
	 */
	public void setScrollMode(ScrollMode scrollMode) {
		if (scrollMode == null) throw new IllegalArgumentException("scrollMode == null");
		ScrollMode old = this.scrollMode;
		this.scrollMode = scrollMode;
		firePropertyChange("scrollMode", old, this.scrollMode);
	}
	
	/**
	 * @return the currently set <code>ScrollMode</code>
	 */
	public ScrollMode getScrollMode() {
		return scrollMode;
	}
	
	/**
	 * @return the Selected ImageSource
	 */
	public ImageSource getSelectedImage() {
		return getModel().getElementAt(getSelectionModel().getMinSelectionIndex());
	}
	
	/**
	 * @return the selected <code>ImageSource</code>
	 */
	ImageSource getSelectedImageSource() {
		int idx = getSelectionModel().getMinSelectionIndex();
		if (idx == -1) return null;
		return getModel().getElementAt(idx);
	}
	

	/**
	 * Sets the height of the cells, in the FilmStrip this property is mandatory.
	 * If a surrounding scrollPane is found it's size
	 * is updated to reflect the change.
	 * @throws IllegalArgumentException if height < 1
	 */
	@Override
	public void setFixedCellHeight(int height) {
		if (height < 1) throw new IllegalArgumentException("height must be > 0");
		super.setFixedCellHeight(height);
	}

	/**
	 * Sets the width of the cells, in the FilmStrip this property is mandatory.
	 * If a surrounding scrollPane is found it's size
	 * is updated to reflect the change.
	 * @throws IllegalArgumentException if width < 1
	 */
	@Override
	public void setFixedCellWidth(int width) {
		if (width < 1) throw new IllegalArgumentException("width must be > 0");
		super.setFixedCellWidth(width);
	}
	
	/**
	 * Sets the model of this <code>FilmStrip</code>
	 * @throws IllegalArgumentException if model is not a FilmStripModel
	 * @param model
	 */
	@Override
	public void setModel(ListModel model) {
		if (! (model instanceof FilmStripModel)) throw new IllegalArgumentException("model must be a FilmStripModel");
		super.setModel(model);
	}
	
	/**
	 * @return the model of the <code>FilmStrip</code>
	 */
	@Override
	public FilmStripModel getModel() {
		return (FilmStripModel) super.getModel();
	}
	
	/**
	 * @return the surrouding <code>JScrollPane</code> if it 
	 * exists, <code>null</code> otherwise
	 */
	protected JScrollPane getSurroundingScrollPane() {
		Component parent = getParent();
		if (parent instanceof JViewport) {
			Component pp = parent.getParent();
			if (pp instanceof JScrollPane) {
				return (JScrollPane) pp;
			}
		}
		return null;
	}

	/**
	 * Sets the <code>Paint</code> used to paint the background. If
	 * the paint is equal to <code>null</code> the default background
	 * color will be used.
	 * @param backgroundPaint the backgroundPaint to set
	 */
	public void setBackgroundPaint(Paint backgroundPaint) {
		Paint old = this.backgroundPaint;
		this.backgroundPaint = backgroundPaint;
		firePropertyChange("backgroundPaint", old, backgroundPaint);
	}
	
	/**
	 * @return the currently set <code>backgroundPaint</code>
	 */
	public Paint getBackgroundPaint() {
		return backgroundPaint;
	}
	
	/**
	 * @param idx the index to evaluate
	 * @return <code>true</code> if the cell at index <code>idx</code>
	 * is visible, <code>false</code> otherwise.
	 */
	boolean isCellVisible(int idx) {
		return getVisibleRect().intersects(getCellBounds(idx, idx));
	}
	
	/**
	 * Sets the <code>FilmStripCellRenderer</code> used by the <code>FilmStrip</code> 
	 * @param cellRenderer the renderer to set
	 * @throws <code>IllegalArgumentException</code> if cellRenderer is <code>null</code>
	 * or not an instance of FilmStripCellRenderer
	 */
	@Override
	public void setCellRenderer(ListCellRenderer cellRenderer) {
		if (! (cellRenderer instanceof FilmStripCellRenderer)) 
			throw new IllegalArgumentException("cellRenderer is not a FilmStripCellRenderer");
		
		super.setCellRenderer(cellRenderer);
	}
	
	/**
	 * @return the current cellRenderer
	 */
	@Override
	public FilmStripCellRenderer getCellRenderer() {
		return (FilmStripCellRenderer) super.getCellRenderer();
	}
	
	/**
	 * Sets the progressPainter to use, if progressPainter is <code>null</code> no
	 * progressPainter will be used
	 * @param progressPainter the progressPainter to set
	 */
	public void setProgressPainter(ProgressPainter progressPainter) {
		ProgressPainter old = this.progressPainter;
		this.progressPainter = progressPainter;
		firePropertyChange("progressPainter", old, progressPainter);
	}
	
	/**
	 * @return the current progressPainter. Note that this may be <code>null</code>
	 */
	public ProgressPainter getProgressPainter() {
		return progressPainter;
	}
	
	/**
	 * @return the current <code>ImageScaler</code> used to scale the images
	 */
	public ImageScaler getImageScaler() {
		return imageScaler;
	}
	
	/**
	 * Sets the <code>ImageScaler</code> to use.
	 * @param imageScaler the <code>ImageScaler</code> to use
	 * @throws <code>IllegalArgumentException</code> if imageScaler is <code>null</code>
	 */
	public void setImageScaler(ImageScaler imageScaler) {
		if (imageScaler == null) throw new IllegalArgumentException("imageScaler == null");
		ImageScaler old = this.imageScaler;
		this.imageScaler = imageScaler;
		firePropertyChange("imageScaler", old, imageScaler);
	}
	
	/**
	 * @param src
	 * @return the progress of the imageloading of the given source
	 */
	Integer getImageSourceProgress(ImageSource src) {
		return progressMap.get(src);
	}
	
	public void setCellInsets(Insets cellInsets) {
		if (cellInsets == null) throw new IllegalArgumentException("cellInsets == null");
		Insets old = this.cellInsets;
		this.cellInsets = cellInsets;
		firePropertyChange("cellInsets", old, this.cellInsets);
	}
	
	public Insets getCellInsets() {
		return cellInsets;
	}
}
