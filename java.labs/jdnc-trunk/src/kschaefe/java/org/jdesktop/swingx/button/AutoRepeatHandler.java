/*
 * $Id: AutoRepeatHandler.java 3180 2009-07-01 19:48:52Z kschaefe $
 * 
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.button;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.jdesktop.swingx.util.Contract;

/**
 * {@code AutoRepeatHandler} enables buttons (and other action containers) to fire repeated
 * {@code ActionEvent}s over the lifetime of a click. When the user depresses the mouse, the timer
 * begins and will fire an {@code ActionEvent} at every interval until the mouse is released. This
 * is a valid action and it will always respond to at least one event. In other words, the user is
 * not required to keep the mouse depressed until the first event is fired to have anything happen.
 * <p>
 * The {@code AutoRepeatHandler} requires an {@code Action} to delegate its work to. However, unlike
 * a standard {@code Action}, {@code AutoRepeatHandler} must also be registered as a
 * {@code MouseListener} or it will not function correctly (it will act as a normal action).
 * <pre><code>
 * AutoRepeatHandler repeat = new AutoRepeatHandler(action);
 * JButton button = new JButton(repeat);
 * button.addMouseListener(repeat);
 * </code></pre>
 * 
 * @author Karl Schaefer
 */
public class AutoRepeatHandler extends MouseAdapter implements Action {
    /**
     * The action that backs this handler. The handler is a framework for performing this action at
     * intervals.
     */
    protected final Action delegate;

    /**
     * The timer backing this handler. The handler will register itself with the timer. Access is
     * provided for subclasses, so that additional timer properties can be queried.
     */
    protected final Timer repeatTimer;

    /**
     * Creates a handler for auto-repeating clicks. This is useful for scroll buttons and other
     * items that need to allow the user to maintain the button in a depressed state and repeat the
     * action.
     * 
     * @param delegate
     *            the action to delegate activity to
     * @throw NullPointerException if {@code delegate} is {@code null}
     */
    public AutoRepeatHandler(Action delegate) {
        this(delegate, 60);
    }
    
    /**
     * Creates a handler for auto-repeating clicks. This is useful for scroll buttons and other
     * items that need to allow the user to maintain the button in a depressed state and repeat the
     * action.
     * 
     * @param delegate
     *            the action to delegate activity to
     * @param delay
     *            the delay before firing an additional action event
     * @throw NullPointerException if {@code delegate} is {@code null}
     */
    public AutoRepeatHandler(Action delegate, int delay) {
        this(delegate, delay, 300);
    }
    
    /**
     * Creates a handler for auto-repeating clicks. This is useful for scroll buttons and other
     * items that need to allow the user to maintain the button in a depressed state and repeat the
     * action.
     * 
     * @param delegate
     *            the action to delegate activity to
     * @param delay
     *            the delay before firing an additional action event
     * @param initialDelay
     *            the delay before firing the first action event; an event is fired even if this
     *            threshold is not reached
     * @throw NullPointerException if {@code delegate} is {@code null}
     */
    public AutoRepeatHandler(Action delegate, int delay, int initialDelay) {
        this.delegate = Contract.asNotNull(delegate, "delegate cannot be null"); //$NON-NLS-1$
        repeatTimer = new Timer(delay, this);
        repeatTimer.setInitialDelay(initialDelay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //nothing should call a disabled action
        if (!isEnabled() || !SwingUtilities.isLeftMouseButton(e)) {
            return;
        }

        repeatTimer.stop();
        repeatTimer.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        repeatTimer.stop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        delegate.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        delegate.removePropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean b) {
        delegate.setEnabled(b);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValue(String key) {
        return delegate.getValue(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putValue(String key, Object value) {
        delegate.putValue(key, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        delegate.actionPerformed(e);
    }
}
