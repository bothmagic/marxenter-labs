/*
 * $Id: EmptyIterator.java 2631 2008-08-06 09:23:10Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple Iterator instance with no values.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class EmptyIterator<T> implements Iterator<T> {

    /**
     * Singleton instance of this iterator. Use getInstance() instead.
     */
    public static final Iterator<Object> SINGLETON = new EmptyIterator<Object>();

    /**
     * Returns a generic Iterator with no elements.
     *
     * @return The iterator.
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> getInstance() {
        return (Iterator<T>) SINGLETON;
    }





    /**
     * Create a new empty iterator.
     */
    public EmptyIterator() {
    }





    /**
     * Always returns false.
     *
     * @return {@code false}.
     */
    public boolean hasNext() {
        return false;
    }





    /**
     * Throws an exception if called.
     *
     * @return nothing.
     * @throws NoSuchElementException if called.
     */
    public T next() {
        throw new NoSuchElementException("Empty Iterator");
    }





    /**
     * Throws an exception if called.
     *
     * @throws IllegalStateException if called.
     */
    public void remove() {
        throw new IllegalStateException("Empty Iterator");
    }
}
