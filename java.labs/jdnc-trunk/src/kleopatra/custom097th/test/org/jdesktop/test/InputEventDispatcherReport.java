/*
 * Created on 16.04.2009
 *
 */
package org.jdesktop.test;

import java.awt.AWTEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdesktop.swingx.event.InputEventDispatcher;

public class InputEventDispatcherReport implements InputEventDispatcher {

    /**
     * Holds a list of all received PropertyAWTEvents.
     */
    protected List<AWTEvent> events = Collections.synchronizedList(new LinkedList<AWTEvent>());
    protected Map<Object, AWTEvent> eventMap = Collections.synchronizedMap(new HashMap<Object, AWTEvent>());

    //---------------------- implement InputEventDispatcher    
 
    public void dispatchEvent(AWTEvent evt) {
        events.add(0, evt);
        if (evt.getSource() != null) {
            eventMap.put(evt.getSource(), evt);
        }

    }
    
    
    public int getEventCount() {
        return events.size();
    }
 
    public void clear() {
        events.clear();
        eventMap.clear();
    }
    
    public boolean hasEvents() {
        return !events.isEmpty();
    }
 
     public AWTEvent getLastEvent() {
        return events.isEmpty()
            ? null
            : (AWTEvent) events.get(0);
    }

     public AWTEvent getEvent(Object source) {
         return (AWTEvent) eventMap.get(source);
     }

}
