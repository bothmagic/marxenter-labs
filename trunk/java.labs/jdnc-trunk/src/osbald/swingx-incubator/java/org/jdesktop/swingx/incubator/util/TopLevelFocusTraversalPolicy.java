package org.jdesktop.swingx.incubator.util;

import javax.swing.*;
import java.awt.*;

/**
 * Custom FTP which returns the initial component of the
 * policy looked up in the default hierarchy walk.
 */

// ref: http://forums.java.net/jive/thread.jspa?messageID=199272

public class TopLevelFocusTraversalPolicy extends LayoutFocusTraversalPolicy {

    @Override
    public Component getInitialComponent(Window window) {
        Component defaultComponent = getDefaultComponent(window);
        if (defaultComponent != null) {
            Container root = getPolicyProviderAncestor(defaultComponent);
            FocusTraversalPolicy policy = root.getFocusTraversalPolicy();
            if (policy != this) {
                return policy.getInitialComponent(window);
            }
        }
        return super.getInitialComponent(window);
    }

    /**
     * Looks up the nearest policy provider.
     *
     * @param defaultComponent
     * @return
     */
    private Container getPolicyProviderAncestor(Component defaultComponent) {
        Container rootAncestor = defaultComponent.getParent();
        while (rootAncestor != null && !rootAncestor.isFocusTraversalPolicyProvider()) {
            rootAncestor = rootAncestor.getParent();
        }
        return rootAncestor;
    }
}