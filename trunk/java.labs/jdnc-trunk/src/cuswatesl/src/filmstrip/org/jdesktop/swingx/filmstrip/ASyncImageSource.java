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


	private SwingWorker<?, ?> runningTask;
	
	// a counter to be sure a unique filename will be generated when caching to disc
	private final int myCount;
	private final AtomicReference<File> tmpFile = new AtomicReference<File>();
	private final Map<String, String> fetching = new HashMap<String, String>();

	
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
	
	public void cancelTask() {
		if (this.runningTask != null) {
			this.runningTask.cancel(true);
		}
	}
	
	
	public void createCachedImage(final String name, final int maxWidth,
			final int maxHeight, final ImageScaler scaler, final Runnable callBack, final boolean cacheVolatile) {
		if (isUnretrievable()) {
			if (callBack != null) {
				callBack.run();
			}
			return;
		}
		if (fetching.containsKey(name)) return;
		if (fetching.put(name, name) != null) {
			return;
		}
			
		SwingWorker<BufferedImage, Void> sw = new SwingWorker<BufferedImage, Void>() {
			
			@Override
			protected BufferedImage doInBackground() throws Exception {
				if (isCancelled()) throw new InterruptedException();
				BufferedImage b = null;
				
				// prevent simultanious access to this part to prevent double loading
				loadingSemaphore.acquire(1);
				try {
					ASyncImageSource.this.setState(State.LOADING);
					if (isCancelled()) throw new InterruptedException();
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
				fetching.remove(name);
				// by clearing runningTask, memory used by the ImageIO.loadImage call
				// will also be freed. if we do not do this Memory usage grows out of 
				// control
				runningTask = null;
				try {
					BufferedImage image = get();
					
					if (cacheVolatile) {
						cacheSoft(name, image);
					} else {
						cacheHard(name, image);
					}
					ASyncImageSource.this.setState(State.DONE);
				} catch (CancellationException e) {
					ASyncImageSource.this.setState(State.IDLE);
				} catch (InterruptedException e) {
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
		this.runningTask = sw;
	}
}
