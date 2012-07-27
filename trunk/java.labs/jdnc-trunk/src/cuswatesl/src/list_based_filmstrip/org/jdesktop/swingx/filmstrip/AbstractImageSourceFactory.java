package org.jdesktop.swingx.filmstrip;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ImageSourceFactory;
import org.jdesktop.swingx.ImageSourcePublisher;

/**
 * A basic implementation of the <code>ImageSourceFactory</code> with support for
 * background processing.
 * @author Nick
 */
public abstract class AbstractImageSourceFactory extends SwingWorker<Void, ImageSource> implements ImageSourceFactory {

	public AbstractImageSourceFactory() {
	}
	
	private volatile ImageSourcePublisher publisher;

	public void loadImages(ImageSourcePublisher publisher) {
		this.publisher = publisher;
		execute();
	}

	/**
	 * Asynchroniously extracts the <code>ImageSources</code> for the resource this Factory represents.
	 * the resulting imagesources can be published through the publish method.
	 * @throws Exception
	 */
	protected abstract void publishImageSources() throws Exception;
	
	/**
	 * A inner worker class based on the SwingWorker
	 * @author Nick
	 *
	 */
	@Override
	protected Void doInBackground() throws Exception {
		publishImageSources();
		return null;
		
	}

	@Override
	protected void process(List<ImageSource> chunks) {
		for (ImageSource src : chunks) {
			this.publisher.publish(src);
		}
	}
		
	@Override
	protected void done() {
		try {
			get();
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelProduction() {
		cancel(true);
	}
	
	
}
