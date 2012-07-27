/*
 * $Id: AbstractDynamicScalableIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.event.AbstractDynamicObject;
import org.jdesktop.swingx.event.DynamicObject;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Represents a dynamically changing scalable icon. The implementation of the event processing is handled by an
 * AbstractDynamicObject.Delegate instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractDynamicScalableIcon extends AbstractScalableIcon implements DynamicObject {
    /**
     * Used when dynamicAdapter is null and {@link #getChangeListeners()} is called.
     */
    private static final ChangeListener[] CHANGE_LISTENER = new ChangeListener[0];
    /**
     * Provides the support for ChangeEvents.
     */
    private transient AbstractDynamicObject.Delegate dynamicAdapter;

    /**
     * Create a new dynamic icon.
     */
    public AbstractDynamicScalableIcon() {
        super();
    }





    /**
     * Create a new dynamic icon with the given policies.
     *
     * @param scalePolicy The policy to use to scale this icon.
     */
    public AbstractDynamicScalableIcon(ScalePolicy scalePolicy) {
        super(scalePolicy);
    }





    /**
     * {@inheritDoc}
     */
    public void addChangeListener(ChangeListener l) {
        if (l != null) {
            if (dynamicAdapter == null) {
                dynamicAdapter = new AbstractDynamicObject.Delegate(this);
            }
            dynamicAdapter.addChangeListener(l);
        }
    }





    /**
     * {@inheritDoc}
     */
    public void removeChangeListener(ChangeListener l) {
        if (dynamicAdapter != null) {
            dynamicAdapter.removeChangeListener(l);
        }
    }





    /**
     * {@inheritDoc}
     */
    public ChangeListener[] getChangeListeners() {
        ChangeListener[] result;
        if (dynamicAdapter == null) {
            result = CHANGE_LISTENER;
        } else {
            result = dynamicAdapter.getChangeListeners();
        }
        return result;
    }





    /**
     * Call this method when the icon need repainting.
     */
    protected void fireStateChanged() {
        if (dynamicAdapter != null) {
            dynamicAdapter.fireStateChanged();
        }
    }
}
