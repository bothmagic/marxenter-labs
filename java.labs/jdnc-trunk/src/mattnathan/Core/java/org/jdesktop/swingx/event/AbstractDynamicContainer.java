/*
 * $Id: AbstractDynamicContainer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Abstract DynamicContainer for forwarding change events from children to listeners on this object. See the nested
 * class Delegate for a mechanism of using this class if you cannot extend this class directly.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractDynamicContainer extends AbstractDynamicObject {

    /**
     * The listener instance responsible for forwarding events.
     */
    private transient ChangeListener childListener = null;

    /**
     * Default constructor for use by subclasses.
     */
    protected AbstractDynamicContainer() {
        super();
    }





    /**
     * Checks to see if the given object is a DynamicObject, if it is calls installChild(DynamicObject).
     *
     * @param child The child object to install event forwarding on.
     */
    protected void installChild(Object child) {
        if (child instanceof DynamicObject) {
            installChild((DynamicObject) child);
        }
    }





    /**
     * Install the given object on this container.
     *
     * @param child The child to install event forwarding on.
     */
    protected void installChild(DynamicObject child) {
        if (childListener == null) {
            childListener = new ChildListener();
        }
        child.addChangeListener(childListener);
    }





    /**
     * Checks to see if the given object is a DynamicObject, if it is calls uninstallChild(DynamicObject).
     *
     * @param child The child to uninstall event forwarding on.
     */
    protected void uninstallChild(Object child) {
        if (child instanceof DynamicObject) {
            uninstallChild((DynamicObject) child);
        }
    }





    /**
     * Uninstalls any listeners added to the given object through installChild.
     *
     * @param child The child to uninstall event forwarding on.
     */
    protected void uninstallChild(DynamicObject child) {
        if (childListener != null) {
            child.removeChangeListener(childListener);
        }
    }





    /**
     * Class responsible for forwarding events.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ChildListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            fireStateChanged();
        }
    }







    /**
     * Extends this class to provide easy dynamic container support. Typical implementation of the fireStateChanged
     * method would be: {@code ParentObject.this.fireStateChanged()}.
     * <p/>
     * <p>This class is designed to be extended by inner classes of the class that requires this functionality.</p>
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public abstract static class Delegate extends AbstractDynamicContainer {
        /**
         * Implement this method to call the fireStateChanged method of the containing class to the sub-class of this
         * class.
         */
        @Override
        public abstract void fireStateChanged();





        /**
         * Overriden for performance reasons
         *
         * @param l ChangeListener
         */
        @Override
        public void addChangeListener(ChangeListener l) {
        }





        /**
         * Overriden for performance reasons
         *
         * @param l ChangeListener
         */
        @Override
        public void removeChangeListener(ChangeListener l) {
        }





        /**
         * Promotes this method to public.
         *
         * @param o Object
         */
        @Override
        public void uninstallChild(Object o) {
            super.uninstallChild(o);
        }





        /**
         * Promotes this method to public.
         *
         * @param o Object
         */
        @Override
        public void uninstallChild(DynamicObject o) {
            super.uninstallChild(o);
        }





        /**
         * Promotes this method to public.
         *
         * @param o Object
         */
        @Override
        public void installChild(Object o) {
            super.installChild(o);
        }





        /**
         * Promotes this method to public.
         *
         * @param o Object
         */
        @Override
        public void installChild(DynamicObject o) {
            super.installChild(o);
        }
    }
}
