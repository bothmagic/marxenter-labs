/*
 * $Id: AbstractImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;
import javax.swing.SwingUtilities;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.jdesktop.swingx.event.AbstractDynamicObject;

/**
 * ImageProxy implementation for use with ImageLoaders. As there are many ways
 * to load an image this class is abstract with the method loadImage(ImageLoader)
 * available to subclasses.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractImageProxy extends AbstractDynamicObject implements ImageProxy, DynamicProxy {
    /**
     * Object responsible for loading the image.
     */
    private ImageLoader imageLoader;

    /**
     * The loaded image.
     */
    private Image image;
    /**
     * Any exception that may have been thrown.
     */
    private ImageLoaderException exceptionCaught;

    /**
     * The placeholder for the image loading process.
     */
    private Future<? extends Image> placeholder;

    /**
     * Creates a new ImageProxy which uses the given imageLoader to load its
     * images.
     *
     * @param imageLoader The object responsible for loading the image data.
     */
    public AbstractImageProxy(ImageLoader imageLoader) {
        if (imageLoader == null) {
            throw new IllegalArgumentException("imageLoader cannot be null");
        }
        this.imageLoader = imageLoader;
    }





    /**
     * Returns true only if getImage() returns true. This checks the done status
     * of the Future placeholder and sets the image if needed before checking
     * this.
     *
     * @return {@code true} if the image is loaded.
     */
    public boolean isReady() {
        if (placeholder != null && placeholder.isDone()) {
            try {
                setImage(placeholder.get());
            } catch (ExecutionException ex) {
                Throwable exceptionCaught = ex.getCause();
                if (exceptionCaught instanceof RuntimeException) {
                    throw (RuntimeException) exceptionCaught;
                } else if (exceptionCaught instanceof Error) {
                    throw (Error) exceptionCaught;
                } else {
                    assert exceptionCaught instanceof ImageLoaderException;
                    this.exceptionCaught = (ImageLoaderException) exceptionCaught;
                }

            } catch (InterruptedException ex) {
                // continue
            } finally {
                placeholder = null;
            }
        }
        try {
            return getImage() != null;
        } catch (ImageLoaderException ex) {
            return true;
        }
    }





    /**
     * Handles the case where the same thread may call this method more than once
     * while the image is being made ready. This support is provided by the
     * Future interface in concurrent package. The creation of this Future object
     * is delegated to the {@code loadImage} method.
     *
     * @param wait {@code true} to make this method block until the image has been loaded.
     * @return {@code true} if the image has been loaded by the time this method returns.
     */
    public boolean makeReady(boolean wait) {
        boolean ready = isReady();
        if (!ready) {
            if (placeholder == null) {
                placeholder = loadImage(getImageLoader());
            }

            if (wait || placeholder.isDone()) {
                try {
                    setImage(placeholder.get());
                } catch (ExecutionException ex) {
                    Throwable exceptionCaught = ex.getCause();
                    if (exceptionCaught instanceof RuntimeException) {
                        throw (RuntimeException) exceptionCaught;
                    } else if (exceptionCaught instanceof Error) {
                        throw (Error) exceptionCaught;
                    } else {
                        assert exceptionCaught instanceof ImageLoaderException;
                        this.exceptionCaught = (ImageLoaderException) exceptionCaught;
                    }
                } catch (InterruptedException ex) {
                    // continue
                } finally {
                    placeholder = null;
                }

            }

            ready = isReady();
        }
        return ready;
    }





    /**
     * Sets the image for this image proxy.
     *
     * @param image The image.
     */
    protected void setImage(Image image) {
        this.image = image;
    }





    /**
     * Starts the load process for the image returning the Future placeholder
     * which will eventually contain the Image instance.
     *
     * @param imageLoader The object used to load the image.
     * @return The object that will be populated with the image once it has been loaded.
     */
    protected abstract Future<? extends Image> loadImage(ImageLoader imageLoader);





    /**
     * Gets the object responsible for loading the image data.
     *
     * @return The object responsible for loading the image.
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }





    /**
     * Get the image instance. This returns null when the image has not been made
     * ready.
     *
     * @return The image loaded or null if the load is ongoing.
     * @throws ImageLoaderException when there was a problem loading the image.
     */
    public Image getImage() throws ImageLoaderException {
        throwExceptions();
        return image;
    }





    /**
     * Throws any caught exceptions from the image load process.
     *
     * @throws ImageLoaderException if one has been caught during image loading.
     */
    protected void throwExceptions() throws ImageLoaderException {
        if (exceptionCaught != null) {
            throw exceptionCaught;
        }
    }





    /**
     * {@inheritDoc}
     */
    public int getImageWidth() {
        return getImageLoader().getImageWidth();
    }





    /**
     * {@inheritDoc}
     */
    public int getImageHeight() {
        return getImageLoader().getImageHeight();
    }





    /**
     * This method is called just before the image loading is started. This is
     * called on the image loading thread.
     */
    protected void before() {}





    /**
     * This method is called immediately after the image is loaded. This fires a
     * ChangeEvent.
     */
    protected void after() {
        if (SwingUtilities.isEventDispatchThread()) {
            fireStateChanged();
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    after();
                }
            });
        }
    }
}
