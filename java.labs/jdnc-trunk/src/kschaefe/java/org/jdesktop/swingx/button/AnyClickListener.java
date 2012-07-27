/*
 * $Id: AnyClickListener.java 2392 2008-04-10 02:38:02Z kschaefe $
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

import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicButtonListener;

/**
 * 
 */
//TODO should we ensure that the delegate is non-null?
public class AnyClickListener extends BasicButtonListener {
    /**
     * A shortcut property representing all click types.
     */
    public static final int ALL_CLICKS = InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK
            | InputEvent.BUTTON3_MASK;
    
    /**
     * A shortcut property representing left and right clicks.
     */
    public static final int LEFT_RIGHT = InputEvent.BUTTON1_MASK | InputEvent.BUTTON3_MASK;
    
    private BasicButtonListener delegate;

    private int supportClickTypes;

    /**
     * Creates a new listener for all clicks with a delegate backing it.
     * 
     * @param l
     *            the delegate for this listener
     */
    AnyClickListener(BasicButtonListener l) {
        this(l, ALL_CLICKS);
    }

    /**
     * Creates a new listener with a delegate backing it.
     * 
     * @param l
     *            the delegate for this listener
     * @param supportClickTypes
     *            the support click types the union of all accept click types
     * @see InputEvent#BUTTON1_MASK
     * @see InputEvent#BUTTON2_MASK
     * @see InputEvent#BUTTON3_MASK
     */
    AnyClickListener(BasicButtonListener l, int supportClickTypes) {
        super(null);

        this.delegate = l;
        this.supportClickTypes = supportClickTypes;
    }

    private MouseEvent createLeftClickEvent(MouseEvent e) {
        MouseEvent evt = new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers()
                | InputEvent.BUTTON1_MASK, e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(),
                e.getClickCount(), e.isPopupTrigger(), e.getButton());
        
        return evt;
    }

    /**
     * Determines if the click represented by the specified mouse event is a valid click for this
     * listener.
     * 
     * @param e
     *            the mouse event to check
     * @return {@code true} if the click is valid (should arm the button, etc.); {@code false}
     *         otherwise
     */
    protected boolean isValidClickType(MouseEvent e) {
        int click = e.getModifiers();
        
        boolean valid = (supportClickTypes & InputEvent.BUTTON1_MASK) != 0
                && (click & InputEvent.BUTTON1_MASK) != 0;
        valid |= (supportClickTypes & InputEvent.BUTTON2_MASK) != 0
                && (click & InputEvent.BUTTON2_MASK) != 0;
        valid |= (supportClickTypes & InputEvent.BUTTON3_MASK) != 0
                && (click & InputEvent.BUTTON3_MASK) != 0;
        
        return valid;
    }
    
    /**
     * {@inheritDoc}
     */
    public void focusGained(FocusEvent e) {
        if (delegate == null) {
            super.focusGained(e);
        } else {
            delegate.focusGained(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void focusLost(FocusEvent e) {
        if (delegate == null) {
            super.focusLost(e);
        } else {
            delegate.focusLost(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void installKeyboardActions(JComponent c) {
        if (delegate == null) {
            super.installKeyboardActions(c);
        } else {
            delegate.installKeyboardActions(c);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseClicked(MouseEvent e) {
        if (delegate == null) {
            super.mouseClicked(e);
        } else {
            delegate.mouseClicked(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseDragged(MouseEvent e) {
        if (delegate == null) {
            super.mouseDragged(e);
        } else {
            delegate.mouseDragged(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e) {
        if (delegate == null) {
            super.mouseEntered(e);
        } else {
            delegate.mouseEntered(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e) {
        if (delegate == null) {
            super.mouseExited(e);
        } else {
            delegate.mouseExited(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseMoved(MouseEvent e) {
        if (delegate == null) {
            super.mouseMoved(e);
        } else {
            delegate.mouseMoved(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e) {
        if (!isValidClickType(e)) {
            return;
        }
        
        MouseEvent fake = createLeftClickEvent(e);
        
        if (delegate == null) {
            super.mousePressed(fake);
        } else {
            delegate.mousePressed(fake);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e) {
        if (!isValidClickType(e)) {
            return;
        }
        
        MouseEvent fake = createLeftClickEvent(e);
        
        if (delegate == null) {
            super.mouseReleased(fake);
        } else {
            delegate.mouseReleased(fake);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (delegate == null) {
            super.propertyChange(e);
        } else {
            delegate.propertyChange(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void stateChanged(ChangeEvent e) {
        if (delegate == null) {
            super.stateChanged(e);
        } else {
            delegate.stateChanged(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void uninstallKeyboardActions(JComponent c) {
        if (delegate == null) {
            super.uninstallKeyboardActions(c);
        } else {
            delegate.uninstallKeyboardActions(c);
        }
    }
    
    /**
     * Installs an {@code AnyClickListener} on the specified button.
     * 
     * @param b
     *            the button to add the listener to
     * @param supportedClickTypes
     *            a mask containing all of the valid buttons
     */
    //TODO this listener will not survive L&F changes
    public static void installListener(AbstractButton b, int supportedClickTypes) {
        BasicButtonListener bbl = null;
        
        for (MouseMotionListener mml : b.getMouseMotionListeners()) {
            if (mml instanceof BasicButtonListener) {
                bbl = (BasicButtonListener) mml;
            }
        }
        
        b.removeMouseListener(bbl);
        
        //we must be reinstalling so find original BBL
        if (bbl instanceof AnyClickListener) {
            bbl = ((AnyClickListener) bbl).delegate;
        }
        
        b.addMouseListener(new AnyClickListener(bbl, supportedClickTypes));
    }
}
