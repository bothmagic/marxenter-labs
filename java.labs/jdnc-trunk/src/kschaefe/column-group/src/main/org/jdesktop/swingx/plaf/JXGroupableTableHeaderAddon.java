/*
 * $Id: JXGroupableTableHeaderAddon.java,v 1.4 2006/03/31 06:51:23 evickroy Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXGroupableTableHeader;
import org.jdesktop.swingx.plaf.windows.WindowsClassicLookAndFeelAddons;
import org.jdesktop.swingx.plaf.windows.WindowsLookAndFeelAddons;

/**
 * Addon for <code>JXGroupableTableHeader</code>.<br>
 * 
 */
public class JXGroupableTableHeaderAddon extends AbstractComponentAddon {
    public JXGroupableTableHeaderAddon() {
        super("JXGroupableTableHeader");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);

        defaults.add(JXGroupableTableHeader.muiClassID,
                "org.jdesktop.swingx.plaf.basic.BasicGroupableTableHeaderUI");
    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void addMacDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
//        super.addMacDefaults(addon, defaults);
//
//        defaults.add(JXGroupableTableHeader.muiClassID,
//                "org.jdesktop.swingx.plaf.metal.MetalGroupableTableHeaderUI");
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void addMetalDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
//        super.addMetalDefaults(addon, defaults);
//
//        defaults.add(JXGroupableTableHeader.muiClassID,
//                "org.jdesktop.swingx.plaf.metal.MetalGroupableTableHeaderUI");
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void addWindowsDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
//        super.addWindowsDefaults(addon, defaults);
//
//        if (addon instanceof WindowsLookAndFeelAddons) {
//            defaults.add(JXGroupableTableHeader.muiClassID,
//                    "org.jdesktop.swingx.plaf.windows.WindowsGroupableTableHeaderUI");
//        }
//
//        if (addon instanceof WindowsClassicLookAndFeelAddons) {
//            defaults.add(JXGroupableTableHeader.muiClassID,
//                    "org.jdesktop.swingx.plaf.windows.WindowsClassicGroupableTableHeaderUI");
//        }
//    }
}
