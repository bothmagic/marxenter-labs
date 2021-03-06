/*
 * $Id: PipelineEvent.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.util.EventObject;


/**
 * Defines an event that encapsulates changes to a pipeline.
 *
 * @author Ramesh Gupta
 */
public class PipelineEvent extends EventObject
{
    /** Identifies one or more changes in the pipeline. */
    public static final int CONTENTS_CHANGED = 0;

    private int type;

    /**
     * Returns the event type. The possible values are:
     * <ul>
     * <li> {@link #CONTENTS_CHANGED}
     * </ul>
     *
     * @return an int representing the type value
     */
    public int getType() { return type; }

    /**
     * Constructs a PipelineEvent object.
     *
     * @param source  the source Object (typically <code>this</code>)
     * @param type    an int specifying the event type
     */
    public PipelineEvent(Object source, int type) {
        super(source);
        this.type = type;
    }

    /**
     * Returns a string representation of this event. This method
     * is intended to be used only for debugging purposes, and the
     * content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not
     * be <code>null</code>.
     *
     * @return  a string representation of this event.
     */
    public String toString() {
        return getClass().getName() + "[type=" + type + "]";
    }
}



