package org.jdesktop.swingx.filmstrip;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.jdesktop.swingx.ImageScaler;
import org.jdesktop.swingx.ImageSource;

/**
 * An asynchronious implementation of the ImageSource which leaves only the <code>loadImage</code>
 * method unimplemented. LoadImage will be called asynchroniously to load the image, the result 
 * will be cached in a temporary file to speed up sequential calls if the resource is not on local 
 * disc. To prevent memory depletion the calls to <code>loadImage</code> are protected with a 
 * <code>Semaphore</code> which, by default, allows two concurrent calls throughout all 
 * AbstractImageSources. This does not deplete the normal SwingWorkers, executors as the 
 * <code>AbstractImageSource</code> uses it's own <code>Executor</code>. The amount of leases can 
 * be tweaked using the <code>ImageSource.concurrentLeases</code> system property proir to loading 
 * this class. After that, the amount cannot be tweaked. FIXME can do now; On initialization this 
 * class will throw an exception if the value of ImageSource.concurrentLeases is not <code>null</code> 
 * and cannot be parsed using <code>Integer.parseInt</code> 
 * 
 * @author Nick
 *
 */
public abstract class AbstractImageSource implements ImageSource {
	
	// backed by a static variable to decrease memory footprint
	private static BufferedImage cacheImageIcon;
	private static BufferedImage unRetrievableImageIcon;

	/**
	 * Returns an image representing this type of image, when it has not yet been loaded
	 * @return
	 */
	protected BufferedImage getImageIcon() {
		if (cacheImageIcon == null) {
			cacheImageIcon = createTypeIcon();
		}
		return cacheImageIcon;
	}
	
	public BufferedImage getUnRetrievableImageIcon() {
		if (unRetrievableImageIcon == null) {
			BufferedImage icon = getImageIcon();
			unRetrievableImageIcon = new BufferedImage(icon.getWidth(), icon.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = unRetrievableImageIcon.createGraphics();
			g2.drawImage(icon, 0, 0, null);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			BasicStroke str = new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
			g2.setStroke(str);
			g2.setColor(Color.RED);
			g2.setPaint(new GradientPaint(4, 46, Color.ORANGE, 27, 66, Color.red.brighter()));
			g2.drawLine(4, 46, 24, 66);
			g2.drawLine(4, 66, 27, 41);
		}
		return unRetrievableImageIcon;
	}
	
	private final Map<Object, SoftReference<BufferedImage>> namedSoftCache = new HashMap<Object, SoftReference<BufferedImage>>();
	private final Map<Object, BufferedImage> namedHardCache = new HashMap<Object, BufferedImage>();
	
	private boolean caching = true;
	private String cacheFormat = "JPG";
	private String description;
	private List<Action> actions = new ArrayList<Action>();
	private boolean unretrievable = false;
	State state = State.IDLE;
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Creates a caching imagesource
	 */
	public AbstractImageSource() {
		this(true);
	}
	
	/**
	 * Creates an imageSource
	 * @param caching
	 */
	public AbstractImageSource(boolean caching) {
		this.caching = caching; 
	}
	
	/**
	 * Sets the state of the ImageSource. Unlike other methods this method is threadsafe, and
	 * may be called from any thread. Listeners will be informed on the EDT
	 * @param state
	 */
	protected void setState(final State state) {
		if (state == null) throw new IllegalArgumentException("state == null");
		
		Runnable doStateChange = new Runnable() {
			public void run() {
				if (AbstractImageSource.this.state != state) {
					AbstractImageSource.this.state = state;
					fireStateChanged();
				}
			}
		};
		
		if (SwingUtilities.isEventDispatchThread()) {
			doStateChange.run();
		} else {
			SwingUtilities.invokeLater(doStateChange);
		}
	}
	
	public State getState() {
		return this.state;
	}
	
	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	public ChangeListener[] getChangeListeners() {
		return listenerList.getListeners(
				ChangeListener.class);
	}

	protected void fireStateChanged() {
		Object[] listeners = listenerList.getListenerList();
		ChangeEvent e = null;

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				if (e == null) {
					e = new ChangeEvent(this);
				}
				((ChangeListener)listeners[i+1]).stateChanged(e);
			}	       
		}
	}

	/**
	 * Sets the format of the cache, this value must be accepted by <code>ImageIO.write</code> or exceptions
	 * will occur when caching the image
	 * @param cacheFormat
	 */
	public void setCacheFormat(String cacheFormat) {
		this.cacheFormat = cacheFormat;
	}
	
	/**
	 * returns the set cacheFormat
	 * @return
	 */
	public String getCacheFormat() {
		return cacheFormat;
	}
	
	public void createCachedImage(final Object id, final int maxWidth,
			final int maxHeight, final ImageScaler scaler, final Runnable callBack) {
		this.createCachedImage(id, maxWidth, maxHeight, scaler, callBack, true);
	}
	
	
	public BufferedImage getPlaceHolderImage() {
		return getImageIcon();
	}
	
	public void cacheImage(Object id, BufferedImage image) {
		this.namedSoftCache.put(id, new SoftReference<BufferedImage>(image));
	}
	public void clearCache() {
		this.namedSoftCache.clear();
		this.namedHardCache.clear();
	}
	
	public BufferedImage getCachedImage(Object id) {
		if (unretrievable) return getUnRetrievableImageIcon();
		if (namedHardCache.containsKey(id)) return namedHardCache.get(id);
		if (! namedSoftCache.containsKey(id)) return null;
		return namedSoftCache.get(id).get();
	}
	
	public void removeCachedImage(String name) {
		this.namedSoftCache.remove(name);
		this.namedHardCache.remove(name);
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Collection<Action> getActions() {
		return new ArrayList<Action>(this.actions);
	}
	
	public void addAction(Action action) {
		this.actions.add(action);
	}
	
	public void removeAction(Action action) {
		this.actions.remove(action);
	}
	
	protected void cacheHard(Object id, BufferedImage image) {
		this.namedHardCache.put(id, image);
	}
	
	protected void cacheSoft(Object id, BufferedImage image) {
		this.namedSoftCache.put(id, new SoftReference<BufferedImage>(image));
	}
	
	/**
	 * This method must load the image represented by this <code>ImageSource</code>s implementations
	 * must note that this method may be called on threads other then the EDT, and should preferrably
	 * be stateless.
	 * @return
	 * @throws IOException
	 */
	public abstract BufferedImage loadImage() throws IOException;

	public boolean isUnretrievable() {
		return unretrievable;
	}
	
	public void setUnretrievable(boolean unretrievable) {
		this.unretrievable = unretrievable;
	}
	
	public boolean isCaching() {
		return caching;
	}
	
	protected BufferedImage createTypeIcon() {
		BufferedImage im = new BufferedImage(50, 70, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = im.createGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
		GradientPaint back = new GradientPaint(0, 0, Color.gray, 0, 70, Color.LIGHT_GRAY);
		g2.setPaint(back);
		g2.fillRoundRect(1, 1, 48, 68, 20, 15);
		
		GradientPaint yr = new GradientPaint(0, 0, Color.yellow, 0, 70, new Color(255, 167, 0));
		g2.setPaint(yr);
		g2.fillRect(8, 28, 34, 34);

		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));		
		GradientPaint gp = new GradientPaint(0, 0, Color.white, 0, 70, Color.GRAY);
		g2.setPaint(gp);
		g2.drawLine(50, 53, 33, 70);
		g2.drawLine(50, 50, 30, 70);
		g2.drawLine(50, 47, 27, 70);
		BasicStroke str = new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g2.setStroke(str);
		g2.drawRoundRect(1, 1, 48, 68, 20, 15);
		g2.dispose();
		return im;
	}

}