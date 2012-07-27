/*
 * $Id: AbstractContainerEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract superclass for effect that consist of a collection of child effects. This class will install listeners onto
 * the child effects
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractContainerEffect<T> extends AbstractGraphicsEffect<T> implements
        Iterable<GraphicsEffect<? super T>> {

    private static final GraphicsEffect<?>[] EMPTY = new GraphicsEffect[0];

    private EventHandler eventHandler;

    private GraphicsEffect<? super T>[] children;





    /**
     * Create a new container effect with no children.
     */
    public AbstractContainerEffect() {
    }





    /**
     * Create a new container effect with the given children. This calls setChildren()
     *
     * @param children The children for this container.
     * @see #setChildren(GraphicsEffect[])
     */
    public AbstractContainerEffect(GraphicsEffect<? super T>... children) {
        setChildren(children);
    }





    /**
     * Creates a new container effect with the given children. This calls {@link #AbstractContainerEffect(GraphicsEffect[])}.
     *
     * @param children The children for this container.
     * @see #setChildren(GraphicsEffect[])
     */
    public AbstractContainerEffect(Collection<GraphicsEffect<? super T>> children) {
        this(children == null ? null : children.toArray(create(children.size())));
    }





    /**
     * {@inheritDoc}
     * <p/>
     * The iterator returned does not support the remove method.
     */
    public Iterator<GraphicsEffect<? super T>> iterator() {
        return new Iterator<GraphicsEffect<? super T>>() {
            private int index = 0;
            private GraphicsEffect<? super T>[] items = children;





            public boolean hasNext() {
                return items != null && index < items.length;
            }





            public GraphicsEffect<? super T> next() {
                if (items.length > index) {
                    return items[index++];
                } else {
                    throw new NoSuchElementException();
                }
            }





            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }





    /**
     * Gets the children of this effect. This will never return null but may return an empty array. This will return a
     * new array preventing modifications externally to the children of this container.
     *
     * @return The children of this effect.
     * @see #setChildren(GraphicsEffect[])
     */
    public GraphicsEffect<? super T>[] getChildren() {
        return copy(this.children);
    }





    /**
     * Sets the children for this container. This will copy the values into a new array so that modifications cannot
     * happen externally. For each item in the children array the {@link #install(GraphicsEffect) install} method will
     * be called. If there are existing children then they will be uninstalled.
     *
     * @param children The children for this container.
     * @see #getChildren()
     */
    public void setChildren(GraphicsEffect<? super T>... children) {
        GraphicsEffect<? super T>[] old = getChildren();
        assert old != null;
        if (old.length > 0) {
            for (GraphicsEffect<? super T> child : old) {
                uninstall(child);
            }
        }
        this.children = copy(children);
        if (this.children.length > 0) {
            for (GraphicsEffect<? super T> child : this.children) {
                install(child);
            }
        }
        firePropertyChange("children", old, getChildren());
    }





    /**
     * Called when the child has been added to this container. This will by default install a PropertyChangeListener so
     * that child events may be forwarded to listeners on this effect.
     *
     * @param child The child to install.
     * @see #uninstall(GraphicsEffect)
     */
    protected void install(GraphicsEffect<? super T> child) {
        if (eventHandler == null) {
            eventHandler = new EventHandler();
        }
        child.addPropertyChangeListener(eventHandler);
    }





    /**
     * Uninstalls the child from this container. By default this will remove the event listeners added in install.
     *
     * @param child The child to uninstall.
     * @see #install(GraphicsEffect)
     */
    protected void uninstall(GraphicsEffect<? super T> child) {
        if (eventHandler != null) {
            child.removePropertyChangeListener(eventHandler);
        }
    }





    /**
     * Utility method for copying the given array into a new array. This will return EMPTY if the given arr is null or
     * has a length of 0.
     *
     * @param arr the source array
     * @return the destination array
     */
    @SuppressWarnings({"unchecked"})
    private static <T> GraphicsEffect<T>[] copy(GraphicsEffect<T>... arr) {
        GraphicsEffect[] result = arr == null || arr.length == 0 ? EMPTY : new GraphicsEffect[arr.length];
        if (result.length > 0) {
            System.arraycopy(arr, 0, result, 0, result.length);
        }
        return (GraphicsEffect<T>[]) result;
    }





    /**
     * Utility method for creating an array of the given length. this will return EMPTY if length == 0.
     *
     * @param length The length of the result.
     * @return The new array.
     */
    @SuppressWarnings({"unchecked"})
    private static <T> GraphicsEffect<T>[] create(int length) {
        return (GraphicsEffect<T>[]) (length == 0 ? EMPTY : new GraphicsEffect[length]);
    }





    /**
     * Gets the index of the given child within this containers children.
     *
     * @param child The child to get the index of.
     * @return The index of the given child or -1 if it was not found.
     */
    protected int indexOf(GraphicsEffect<? super T> child) {
        int index = -1;
        GraphicsEffect<? super T>[] children = this.children;
        if (children != null) {
            for (GraphicsEffect<? super T> c : children) {
                index++;
                if (child == c) {
                    break;
                }
            }
            if (index >= children.length) {
                index = -1;
            }
        }
        return index;
    }





    /**
     * Forwards events from the children of this container to listeners on this effect. the event will be converted into
     * an IndexedPropertyChangeEvent new fired.
     */
    private class EventHandler implements PropertyChangeListener {
        @SuppressWarnings({"unchecked"})
        public void propertyChange(PropertyChangeEvent e) {
            GraphicsEffect<? super T> child = (GraphicsEffect<? super T>) e.getSource();
            int index = indexOf(child);
            assert index != -1;
            fireIndexedPropertyChange(e.getPropertyName(), index, e.getOldValue(), e.getNewValue());
        }
    }
}
