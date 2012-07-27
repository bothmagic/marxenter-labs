package org.jdesktop.beans;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

/**
 * PropertyChangeSupport that guarantees PropertyChangeEvents will only happen on the Event Dispatch Thread (EDT).
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 13-Mar-2007
 * Time: 15:40:44
 */

public class EDTPropertyChangeSupport extends PropertyChangeSupport {
    public EDTPropertyChangeSupport(Object source) {
        super(source);
    }

    @Override
    public void firePropertyChange(final PropertyChangeEvent event) {
        if (SwingUtilities.isEventDispatchThread()) {
            super.firePropertyChange(event);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    firePropertyChange(event);
                }
            });
        }
    }
}
