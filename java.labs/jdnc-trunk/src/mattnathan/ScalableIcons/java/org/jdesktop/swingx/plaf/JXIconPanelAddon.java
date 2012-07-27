/*
 * $Id: JXIconPanelAddon.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.util.List;

/**
 * Addon for the JXIconPanel component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXIconPanelAddon extends AbstractComponentAddon {
    public JXIconPanelAddon() {
        super("JXIconPanel");
    }





    /**
     * Adds default key/value pairs to the given list. Simply adds the Basic ui binding for the uiClassID.
     *
     * @param addon The addons object.
     * @param defaults The list of defaults to add to.
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        defaults.add("IconPanelUI", "org.jdesktop.swingx.plaf.basic.BasicIconPanelUI");
    }
}
