/*
 * $Id: ReferenceImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

import java.awt.Image;

/**
 * Defines a threaded image proxy that stores its image in a Reference. The
 * default implementation will create SoftReference instances for the image
 * storage. To override this behaviour extend this class and override the
 * createReference method.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ReferenceImageProxy extends ThreadedImageProxy {

    private Reference<Image> reference;
    private ReferenceQueue<? super Image> queue;

    /**
     * Creates a new image proxy using the given loader to load the image data.
     *
     * @param loader The instance that loads the image data.
     */
    public ReferenceImageProxy(ImageLoader loader) {
        this(loader, null);
    }





    /**
     * Creates a new image proxy which stores its image in a reference. The
     * reference is created so that the given queue will be populated when the
     * reference is cleared.
     *
     * @param loader The image data loader.
     * @param queue The queue to place cleared references on.
     */
    public ReferenceImageProxy(ImageLoader loader, ReferenceQueue<? super Image> queue) {
        super(loader);
        this.queue = queue;
    }





    /**
     * Wraps the given instance in a reference.
     *
     * @param instance T
     * @param queue ReferenceQueue
     * @return Reference
     */
    protected <T> Reference<T> createReference(T instance, ReferenceQueue<? super T> queue) {
        return new SoftReference<T>(instance, queue);
    }





    /**
     * Gets the proxy which this reference wrapper represents.
     *
     * @return ImageProxy
     */
    public Image getInstance() {
        return reference == null ? null : reference.get();
    }





    /**
     * Gets the image ready to be rendered.
     *
     * @return Image
     * @throws ImageLoaderException
     */
    @Override
    public Image getImage() throws ImageLoaderException {
        throwExceptions();
        return getInstance();
    }





    /**
     * Sets the image to be rendered. This stored the image as a reference which
     * can be cleared by the garbage collector.
     *
     * @param image Image
     */
    @Override
    public void setImage(Image image) {
        reference = createReference(image, queue);
    }

}
