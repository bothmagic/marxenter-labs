/*
 * Created on 20.12.2006
 *
 */
package org.jdesktop.swingx.rollover;

import javax.swing.JComponent;


/**
 * Allows access to a component for "live" addition.
 *
 */
public interface LiveRolloverRenderer extends RolloverRenderer {
    /**
     * Returns a component suitable for live addition to the
     * container hierarchy (during rollover). <p>
     * Note: implementations should use a different instance than 
     * the "normal" renderer component because there might be 
     * unrelated normal rendering requests during the rollover.
     *  
     * @return a component suitable for live addition to the container
     *   hierarchy.
     */
    public JComponent getLiveRendererComponent();
}
