package org.jdesktop.swingx.incubator.util;

import javax.swing.*;
import java.awt.*;

/**
 * Custom FTP configurable with a initial component.
 */

// ref: http://forums.java.net/jive/thread.jspa?messageID=199272

public class InitialFocusTraversalPolicy extends LayoutFocusTraversalPolicy {
    protected Component defaultComponent;

    public InitialFocusTraversalPolicy(Component initial) {
        this.defaultComponent = initial;
    }

    @Override
    public Component getInitialComponent(Window frame) {
        if (defaultComponent != null) {
            return defaultComponent;
        }
        return super.getInitialComponent(frame);
    }
}
