/*
 * $Id: UIPropertySupport.java 2578 2008-07-29 23:16:17Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import javax.swing.JComponent;

import java.util.Set;
import java.util.TreeSet;

/**
 * Provides simple support for component which can be configured by a UI delegate.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class UIPropertySupport {
    private JComponent source;

    private Set<String> userProperties;

    public UIPropertySupport(JComponent source) {
        this.source = source;
    }





    /**
     * Returns true if the given property name is currently managed by the UI.
     *
     * @param property The property name.
     * @return {@code true} if the property is UI managed.
     */
    public boolean isUIProperty(String property) {
        return userProperties == null || !userProperties.contains(property);
    }





    /**
     * Sets the given property to be UI managed.
     *
     * @param property The property name.
     * @param b {@code true} if the given property is managed by the UI.
     */
    public void setUIProperty(String property, boolean b) {
        if (!b) {
            if (userProperties == null) {
                userProperties = new TreeSet<String>();
            }
            userProperties.add(property);
        } else {
            if (userProperties != null) {
                userProperties.remove(property);
            }
        }
    }
}
