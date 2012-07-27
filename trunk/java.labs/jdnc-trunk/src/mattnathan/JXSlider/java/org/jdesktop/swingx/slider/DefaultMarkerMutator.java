/*
 * $Id: DefaultMarkerMutator.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

/**
 * The default mutator for MArkerGroups. This uses a collection of specific mutators which are looked up on MarkerGroup
 * class type to perform the actual mutation.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultMarkerMutator implements MarkerMutator {

    private Map<Class<? extends MarkerGroup>, MarkerMutator> mutatorDelegates = new HashMap<Class<? extends MarkerGroup>, MarkerMutator>();

    public DefaultMarkerMutator() {
        initDefaultMutators();
    }





    /**
     * initialise any default mutators to use when looking up group types.
     */
    protected void initDefaultMutators() {
        mutatorDelegates.put(ValueMarkerGroup.class, new ValueMarkerMutator());
        mutatorDelegates.put(BagMarkerGroup.class, new BagMarkerMutator());
    }





    /**
     * Returns true if the given value can be mutated within the given MarkerGroup.
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group of values containing the given value.
     * @param value The value to check for mutability.
     * @return {@code true} if the given value can be mutated.
     */
    public boolean isMutable(SliderModel model, MarkerGroup group, long value) {
        MarkerMutator m = getMutator(group.getClass());
        return m != null && m.isMutable(model, group, value);
    }





    /**
     * gets the MarkerMutator designed to modify the given MarkerGorup type.
     *
     * @param type The type of marker group to modify.
     * @return The marker group of null if none can be found.
     */
    protected MarkerMutator getMutator(Class<? extends MarkerGroup> type) {
        MarkerMutator result = mutatorDelegates.get(type);
        if (result == null) {
            LinkedList<Class<?>> queue = new LinkedList<Class<?>>();
            queue.addLast(type);
            while (!queue.isEmpty()) {
                Class<?> t = queue.removeFirst();
                //noinspection SuspiciousMethodCalls
                result = mutatorDelegates.get(t);
                if (result == null) {
                    for (Class<?> c : t.getInterfaces()) {
                        queue.addLast(c);
                    }
                    t = t.getSuperclass();
                    if (t != null && t != Object.class) {
                        queue.addLast(t);
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }





    /**
     * Mutate the currentValue within the given MarkerGroup so that it becomes the new value.
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group to alter the value in.
     * @param currentValue The current value to change.
     * @param newValue The value to set in the given MarkerGroup.
     * @param valueAdjusting {@code true} if the process of changing the value is only partially complete.
     * @return The actual value set on the marker group.
     */
    public long mutate(SliderModel model, MarkerGroup group, long currentValue, long newValue, boolean valueAdjusting) {
        MarkerMutator m = getMutator(group.getClass());
        long result = currentValue;
        if (m != null) {
            result = m.mutate(model, group, currentValue, newValue, valueAdjusting);
        }
        return result;
    }
}
