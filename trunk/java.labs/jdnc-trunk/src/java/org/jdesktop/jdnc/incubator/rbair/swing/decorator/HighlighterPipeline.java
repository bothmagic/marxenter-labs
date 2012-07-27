/*
 * $Id: HighlighterPipeline.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.decorator;

import java.util.EventListener;

import java.awt.Component;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * HighlighterPipeline
 *
 * @author Ramesh Gupta
 */
public class HighlighterPipeline {
    protected EventListenerList listenerList = new EventListenerList();
    private final Highlighter[] highlighters;
    private final static Highlighter	nullHighlighter = new Highlighter(null, null);

    public HighlighterPipeline(Highlighter[] inList) {
        highlighters = (Highlighter[]) inList.clone();	// always returns a new copy of inList

        for (int i = 0; i < highlighters.length; i++) {
            if (highlighters[i].order < 0) {
                highlighters[i].order = i;
            }
            else {
                throw new IllegalArgumentException("Element " + i +
                    " is part of another pipeline.");
            }
        }
    }

    public Highlighter[] getHighlighters() {
        return (Highlighter[])highlighters.clone();
    }

    public EventListener[] getListeners(Class listenerType) {
        return listenerList.getListeners(listenerType);
    }

    /**
     * Adds a listener to the list that's notified each time there is a change
     * to the pipeline.
     *
     * @param l the <code>PipelineListener</code> to be added
     */
    public void addPipelineListener(PipelineListener l) {
        listenerList.add(PipelineListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time there is a change
     * to the pipeline.
     *
     * @param l the <code>PipelineListener</code> to be removed
     */
    public void removePipelineListener(PipelineListener l) {
        listenerList.remove(PipelineListener.class, l);
    }

    /**
     * Returns an array of all the pipeline listeners
     * registered on this <code>HighlighterPipeline</code>.
     *
     * @return all of this pipeline's <code>PipelineListener</code>s,
     *         or an empty array if no pipeline listeners
     *         are currently registered
     *
     * @see #addPipelineListener
     * @see #removePipelineListener
     */
    public PipelineListener[] getPipelineListeners() {
        return (PipelineListener[]) listenerList.getListeners(
            PipelineListener.class);
    }

    protected void fireContentsChanged(Object source) {
        Object[] listeners = listenerList.getListenerList();
        PipelineEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PipelineListener.class) {
                if (e == null) {
                    e = new PipelineEvent(source, PipelineEvent.CONTENTS_CHANGED);
                }
                ( (PipelineListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }

    public Component apply(Component stamp, ComponentAdapter adapter) {
        /** @todo optimize the following bug fix */
        stamp = nullHighlighter.highlight(stamp, adapter);	// fixed bug from M1

        int count = highlighters.length;
        for (int i = 0; i < count; i++) {
            stamp = highlighters[i].highlight(stamp, adapter);
        }
        return stamp;
    }
}