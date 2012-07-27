package org.jdesktop.beans;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 13-Mar-2007
 * Time: 15:58:12
 */

/**
 * @deperecate old & busted!
 */
public class EDTVetoableChangeSupport extends VetoableChangeSupport {
    EDTVetoableChangeSupport(Object source) {
        super(source);
    }

    @Override
    public void fireVetoableChange(final PropertyChangeEvent event) throws PropertyVetoException {
        if (SwingUtilities.isEventDispatchThread()) {
            super.fireVetoableChange(event);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        /*PENDING oh crap! VetoExceptions not designed to happen 'later' - wherefore art thou exception?
                          Use invokeAndWait() instead? how fragile would that be?
                         */
                        fireVetoableChange(event);
                    } catch (PropertyVetoException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
