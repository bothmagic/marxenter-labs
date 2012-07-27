/*
 * $Id: ThreadedImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jdesktop.concurrent.ThreadPoolExecutor2;

/**
 * Instance of the URLImageProxy that loads images via a background thread as
 * provided by an ExecutorService instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ThreadedImageProxy extends AbstractImageProxy {

    /**
     * The shared instance of the executor used by all subclasses that don't
     * override getExecutorService.
     */
    private static ExecutorService executor;
    /**
     * Map used to link a Runnable to the references that spawned them. This is
     * necessary as there is no way the get back to this reference from within
     * the afterExecute and beforeExecute methods of ExecutorService.
     */
    private static Map<Runnable, ThreadedImageProxy> referenceMap;

    /**
     * Create a new ImageProxy for the given ImageLoader.
     *
     * @param imageLoader ImageLoader
     */
    public ThreadedImageProxy(ImageLoader imageLoader) {
        super(imageLoader);
    }





    /**
     * Off loads the task of loading images to the ExecutorService and the logic
     * for loading the image to the readImageData abstract method.
     *
     * @param imageLoader the class that does the image loading
     * @return Future
     */
    @Override
    protected Future<Image> loadImage(final ImageLoader imageLoader) {
        if (referenceMap == null) {
            // WeakHashMap used as this will clean up the references in the map
            // that are no longer needed
            referenceMap = new WeakHashMap<Runnable, ThreadedImageProxy>();
        }
        Future<Image> f = getExecutorService().submit(new Callable<Image>() {
            public Image call() throws Exception {
                Image result;
                result = imageLoader.loadImageData();
                return result;
            }
        });
        if (f instanceof Runnable && !f.isDone()) {
            referenceMap.put((Runnable) f, this);
        }
        return f;
    }





    /**
     * Gets the ExecutorService the image loading should be performed by.
     *
     * @return ExecutorService
     */
    protected ExecutorService getExecutorService() {
        if (executor == null) {
            executor = new ThreadPoolExecutor2(0, 3, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>()) {
                /**
                 * Overridden to call before in the enclosing class.
                 *
                 * @param t Thread
                 * @param r Runnable
                 */
                @Override
                protected void beforeExecute(Thread t, Runnable r) {
                    if (referenceMap != null) {
                        ThreadedImageProxy object = referenceMap.get(r);
                        if (object != null) {
                            object.before();
                        }
                    }
                    super.beforeExecute(t, r);
                }





                /**
                 * Overriden to call after in the enclosing class.
                 *
                 * @param r Runnable
                 * @param t Throwable
                 */
                @Override
                protected void afterExecute(Runnable r, Throwable t) {
                    super.afterExecute(r, t);
                    if (referenceMap != null) {
                        ThreadedImageProxy object = referenceMap.get(r);
                        if (object != null) {
                            object.after();
                        }
                    }
                }
            };
        }
        return executor;
    }
}
