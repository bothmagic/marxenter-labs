/*
 * $Id: JXTimelineAddon.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.util.List;
import java.util.*;
import javax.swing.UIDefaults.*;

/**
 * UI addons for a JXTimeline.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXTimelineAddon extends AbstractComponentAddon {
    public JXTimelineAddon() {
        super("JXTimeline");
    }





    /**
     * Adds default key/value pairs to the given list.
     *
     * @param addon The addons the defaults are targeted for.
     * @param defaults The list to add to.
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        final String packageName = "org.jdesktop.swingx.plaf.basic.";
        defaults.add("TimelineUI", packageName + "BasicTimelineUI");
        defaults.add("Timeline.upMoreIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineUpMoreIcon"));
        defaults.add("Timeline.downMoreIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineDownMoreIcon"));
        defaults.add("Timeline.leftMoreIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineLeftMoreIcon"));
        defaults.add("Timeline.rightMoreIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineRightMoreIcon"));
        defaults.add("Timeline.upDoneIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineUpDoneIcon"));
        defaults.add("Timeline.downDoneIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineDownDoneIcon"));
        defaults.add("Timeline.leftDoneIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineLeftDoneIcon"));
        defaults.add("Timeline.rightDoneIcon", new ProxyLazyValue(packageName + "BasicIcons", "createTimelineRightDoneIcon"));
    }

}
