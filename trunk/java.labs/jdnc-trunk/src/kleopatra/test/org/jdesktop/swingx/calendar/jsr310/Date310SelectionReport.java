/*
 * $Id: Date310SelectionReport.java 2966 2009-01-13 16:25:16Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */


package org.jdesktop.swingx.calendar.jsr310;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;


/**
 * A ChangeListener that stores the received ChangeEvents.
 * 
 */
public class Date310SelectionReport implements Date310SelectionListener {
    
    /**
     * Holds a list of all received DateSelectionEvents.
     */
    protected List<Date310SelectionEvent> events = Collections.synchronizedList(new LinkedList<Date310SelectionEvent>());
    
    /**
     * Instantiates a DateSelectionReport.
     */
    public Date310SelectionReport() {}

    /**
     * Instantiates a DateSelectionReport and registers itself to the given model.
     * @param model the DateSelectionModel to listen to, must not be null.
     */
    public Date310SelectionReport(Date310SelectionModel model) {
        model.addDateSelectionListener(this);
    }
    
//------------------------ implement DateSelectionListener
    

    public void valueChanged(Date310SelectionEvent evt) {
        events.add(0, evt);
    }

// ------------------- accessors
    
    public int getEventCount() {
        return events.size();
    }
 
    public void clear() {
        events.clear();
    }
    
    public boolean hasEvents() {
        return !events.isEmpty();
    }
 
     public Date310SelectionEvent getLastEvent() {
        return hasEvents() ? events.get(0) : null;
    }

    /**
     * @return the EventType of the last event or null if no event received. 
     */
    public EventType getLastEventType() {
        return hasEvents() ? getLastEvent().getEventType() : null;
    }

    public boolean hasEvent(EventType type) {
        for (Date310SelectionEvent ev : events) {
            if (ev.getEventType().equals(type)) {
                return true;
            }
        }
        return false;
    }
    
    public int getEventCount(EventType type) {
        int count = 0;
        for (Date310SelectionEvent ev : events) {
            if (ev.getEventType().equals(type)) {
                count++;
            }
        }
        return count;
    }
}
