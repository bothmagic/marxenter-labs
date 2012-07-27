/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx;

import org.jdesktop.swingx.plaf.SwitchComponentAddon;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.SwitchLever;

/**
 *
 * @author rcuprak
 */
public class JXSwitch extends AbstractButton implements FocusListener, MouseListener, MouseMotionListener, ChangeListener, Serializable {

    static {
        LookAndFeelAddons.contribute(new SwitchComponentAddon());
    }

    /**
     * The UI Delegate class
     */
    private static String uiClassID = "SwitchUI";

    /**
     * Starting point of a drag
     */
    private Point dragStart;

    /**
     * Switch lever
     */
    private SwitchLever switchLever;

    /**
     * Constructs a new SwitchComponent
     */
    public JXSwitch() {
        DefaultButtonModel dbm = new DefaultButtonModel();
        dbm.addChangeListener(this);
        setModel(dbm);
        switchLever = new SwitchLever(this);
        updateUI();
        addMouseListener(this);
        addMouseMotionListener(this);
        addFocusListener(this);
    }

    /**
     * Sets the bounds of the component
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - width of the component
     * @param height - height of the component
     */
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        Insets is = getInsets();
        int leverWidth = width - is.left - is.right;
        switchLever.updateBounds((int)(leverWidth*0.43f),height);
    }

    /**
     * Returns the switch lever
     * @return SwitchLever
     */
    public SwitchLever getSwitchLever() {
        return switchLever;
    }

    /**
     * De-register our listeners
     */
    @Override
    public void removeNotify() {
        removeMouseListener(this);
        removeMouseMotionListener(this);
    }

    public void updateUI() {
        setUI((ButtonUI)UIManager.getUI(this));
    }

    /**
     * Returns the UIDefaults key to be used in rendering this class.
     * @return the <code>UIDefaults</code> key for a
     *		<code>ComponentUI</code> subclass
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * Returns the preferred size from the delegate
     * @return Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return getUI().getPreferredSize(this);
    }

    /**
     * Capture where the
     * @param e - mouse event
     */
    public void mousePressed(MouseEvent e) {
        if(!isEnabled()) {
            return;
        }
        if(switchLever.contains(e.getPoint())) {
            dragStart = e.getPoint();
        } else {
            dragStart = null;
        }
    }

    /**
     * Mouse is released - so complete the drag.
     * @param e - mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if(dragStart != null) {
            switchLever.animateSlide((e.getX() > getWidth()/2));
            dragStart = null;
            repaint();
        }

    }

    public void mouseEntered(MouseEvent e) {
        // Don't do anything
    }

    public void mouseExited(MouseEvent e) {
        // Dont't do anything
    }

    /**
     * Handle the drag event - slide the button on over
     * @param e - event
     */
    public void mouseDragged(MouseEvent e) {
        if(!isEnabled()) {
            return;
        }
        if(dragStart != null) {
            int difference = dragStart.x - e.getPoint().x;
            switchLever.setX(switchLever.getX() - difference);
            repaint();
            dragStart = e.getPoint();
        }
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        if(!isEnabled()) {
            return;
        }
        if(dragStart != null) {
            return;
        }
        // find out if the click was on the other side
        if(!switchLever.contains(e.getPoint())) {
            switchLever.animateSlide(!getModel().isSelected());
        }
    }

    public void mouseMoved(MouseEvent e) {
        // Don't care about
    }

    /**
     * Yes, we are focusable
     * @return true
     */
    @Override
    public boolean isFocusable() {
        return true;
    }

    public void stateChanged(ChangeEvent e) {
        this.switchLever.setOn(getModel().isSelected());
        repaint();
    }

    public void focusGained(FocusEvent e) {
        repaint();
    }

    public void focusLost(FocusEvent e) {
        repaint();
    }
}
