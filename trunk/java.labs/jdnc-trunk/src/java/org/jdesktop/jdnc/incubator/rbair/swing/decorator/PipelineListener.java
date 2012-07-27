/*
 * $Id: PipelineListener.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.util.EventListener;

/**
 * PipelineListener
 *
 * @author Ramesh Gupta
 */
public interface PipelineListener extends EventListener {
    /**
     * Sent when the pipeline has changed in any way.
     *
     * @param e  a <code>PipelineEvent</code> encapsulating the
     *    event information
     */
    void contentsChanged(PipelineEvent e);
}

