package org.jdesktop.swingx.filmstrip;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.ImageScaler;

/**
 * Extends the AbstractImageSource and implements caching and asynchronious loading
 * support by using a swingworker to call the loadimage method.
 * @author Nick
 *
 */
public abstract class ASyncImageSource extends AbstractImageSource {


	private final static ThreadPoolExecutor imageSourceExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	private final static AtomicInteger counter = new AtomicInteger();


	static {
		int amount = 2;
		String cfgAmount = System.getProperty("ImageSource.concurrentLeases");
		if (cfgAmount != null) {
			try {
				int val = Integer.parseInt(cfgAmount);
				amount = val;
			} catch (NumberFormatException e) {
				throw new RuntimeException("Unable to convert value of ImageSource.concurrentLeases (" + cfgAmount + ") to an Integer");
			}
		}
		imageSourceExecutor.setCorePoolSize(amount);
	}
	
	
	

	public ASyncImageSource() {
		super();
		myCount = counter.incrementAndGet();
	}

	public ASyncImageSource(boolean caching) {
		super(caching);
		myCount = counter.incrementAndGet();
	}


	private final Map<Object, SwingWorker<?, ?>> runningTasks = new HashMap<Object, SwingWorker<?,?>>();
	
	// a counter to be sure a unique filename will be generated when caching to disc
	private final int myCount;
	private final AtomicReference<File> tmpFile = new AtomicReference<File>();
	private final Map<Object, Object> fetching = new HashMap<Object, Object>();
	
	final Semaphore loadingSemaphore = new Semaphore(1);
	
	/**
	 * Caches the given image to a temp file
	 * @param image
	 * @throws IOException
	 */
	void cacheToTempFile(BufferedImage image) throws IOException {
		try {
			if (image == null) return;
			File cache = File.createTempFile("CachingImageSource_" + myCount, null);
			// try to set the file, if it fails it has already been cached and we are done
			if (tmpFile.compareAndSet(null, cache)) {
				ImageIO.write(image, getCacheFormat(), cache);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelTask(Object id) {
		if (this.runningTasks.get(id) != null) {
			this.runningTasks.get(id).cancel(true);
		}
	}
	
	
	public void createCachedImage(final Object id, final int maxWidth,
			final int maxHeight, final ImageScaler scaler, final Runnable callBack, final boolean cacheVolatile) {
		if (isUnretrievable()) {
			if (callBack != null) {
				callBack.run();
			}
			return;
		}
		if (fetching.containsKey(id)) return;
		if (fetching.put(id, id) != null) {
			return;
		}
			
		SwingWorker<BufferedImage, Void> sw = new SwingWorker<BufferedImage, Void>() {
			
			@Override
			protected BufferedImage doInBackground() throws Exception {
				BufferedImage b = null;
				
				// prevent simultanious access to this part to prevent double loading
				loadingSemaphore.acquire(1);
				try {
					ASyncImageSource.this.setState(State.LOADING);
					if (isCaching() && tmpFile.get() != null) {
						b = ImageIO.read(tmpFile.get());
					} else {
						b = loadImage();
						if (isCaching()) {
							cacheToTempFile(b);
						}
					}
				} finally {
					loadingSemaphore.release(1);
				}
				if (maxWidth != -1 && maxHeight != -1) {
					b = scaler.scaleImage(b, maxWidth, maxHeight, false);
				}
				return b;
			}
			
			@Override
			protected void done() {
				fetching.remove(id);
				// by clearing runningTask, memory used by the ImageIO.loadImage call
				// will also be freed. if we do not do this Memory usage grows out of 
				// control
				runningTasks.remove(id);
				try {
					BufferedImage image = get();
					// the call did not throw an exception but the image was
					// not read somehow (unsupported format etc)
					if (image == null) {
						setUnretrievable(true);
						ASyncImageSource.this.setState(State.ERROR);
						return;
					}
					if (cacheVolatile) {
						cacheSoft(id, image);
					} else {
						cacheHard(id, image);
					}
					ASyncImageSource.this.setState(State.DONE);
				} catch (CancellationException e) {
					e.printStackTrace();
					ASyncImageSource.this.setState(State.IDLE);
				} catch (InterruptedException e) {
					e.printStackTrace();
					ASyncImageSource.this.setState(State.IDLE);
				} catch (ExecutionException e) {
					e.printStackTrace();
					setUnretrievable(true);
					ASyncImageSource.this.setState(State.ERROR);
				} catch (Throwable t) {
					t.printStackTrace();
					setUnretrievable(true);
					ASyncImageSource.this.setState(State.ERROR);
				} finally {
					if (callBack != null) {
						callBack.run();
					}
				}
			}
		};
		imageSourceExecutor.submit(sw);
		this.runningTasks.put(id, sw);
	}
	
}
