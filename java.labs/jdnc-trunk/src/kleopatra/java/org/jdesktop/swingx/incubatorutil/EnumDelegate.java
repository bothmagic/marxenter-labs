/*
 * Created on 21.07.2006
 *
 */
package org.jdesktop.swingx.incubatorutil;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;

public class EnumDelegate extends PersistenceDelegate {
    protected boolean mutatesTo(Object oldInstance, Object newInstance) {
        return oldInstance == newInstance;
    }

    protected Expression instantiate(Object oldInstance, Encoder out) {
        Enum e = (Enum) oldInstance;
        return new Expression( e, e.getClass(), "valueOf", new Object[]{e.name()} );
    }
}
