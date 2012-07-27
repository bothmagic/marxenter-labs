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
package org.jdesktop.swingx.plaf;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonModel;
import javax.swing.Timer;
import org.jdesktop.swingx.JXSwitch;

/**
 *
 * @author rcuprak
 */
public class SwitchLever implements ActionListener {

    /**
     * Reference to the switch
     */
    private JXSwitch switchComponent;

    /**
     * X position
     */
    private int x;

    /**
     * Width of the lever
     */
    private int width;

    /**
     * Off position
     */
    private static final int offPosition = 0;

    /**
     * Shadow rectangle used for figuring out where the user clicked
     */
    private Rectangle hitBox;

    /**
     * Timer used to animate, null if no animation is in progress
     */
    private Timer timer;


    /**
     * Animation direction
     */
    private int animationDirection;


    /**
     * Constructs a new switch lever
     * @param switchComponent - switch button
     */
    public SwitchLever(JXSwitch switchComponent) {
        this.switchComponent = switchComponent;
        x = 0;
        hitBox = new Rectangle(0,0,41,switchComponent.getHeight());
    }

    /**
     * Sets the width of the lever
     * @param width - lever width
     * @param height - height
     */
    public void updateBounds(int width, int height) {
        this.width = width;
        hitBox.height = height;
    }

    /**
     * Returns the width of the lever
     * @return lever width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the x coordinate of the switch in the On position
     * @return int
     */
    public int getOnPosition() {
        Insets insets = switchComponent.getInsets();
        return switchComponent.getPreferredSize().width - width - 1 - insets.left - insets.right;
    }


    /**
     * Initiates an animation to either the on or off position
     * @param toOnPosition - direction which we should slide to
     */
    public synchronized void animateSlide(boolean toOnPosition) {
        if(timer == null) {
            animationDirection = toOnPosition ? 1 : -1;
            timer = new Timer(10,this);
            timer.start();
        }
    }

    /**
     * Performs the animation
     * @param e - ActionEvent
     */
    public synchronized void actionPerformed(ActionEvent e) {
        ButtonModel buttonModel = switchComponent.getModel();
        int newPosition = x+(animationDirection*4);
        if(newPosition < offPosition || newPosition == offPosition) {
            newPosition = offPosition;
            buttonModel.setSelected(false);
            timer.stop();
            timer = null;
        } else if (newPosition > getOnPosition() || newPosition == getOnPosition()) {
            newPosition = getOnPosition();
            buttonModel.setSelected(true);
            timer.stop();
            timer = null;
        }
        setX(newPosition);
        switchComponent.repaint();
    }

    /**
     * True if we contain the point
     * @param pt - point
     * @return true if we contain the point
     */
    public boolean contains(Point pt) {
        return hitBox.contains(pt);
    }


    /**
     * Returns the off position
     * @return off position
     */
    public int getOffPosition() {
        return offPosition;
    }

    /**
     * Sets the left-most boundary
     * @param x - x coordinate
     */
    public void setX(int x) {
        if(x < 0 || x > getOnPosition()) {
            return;
        }
        this.x = x;
        hitBox.setLocation(x,0);
    }

    /**
     * Returns the current X position of the switch
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the switch to the on position
     * @param on - flag indicating if the switch is on.
     */
    public void setOn(boolean on) {
        if(on) {
            x = getOnPosition();
        } else {
            x = offPosition;
        }
    }

}
