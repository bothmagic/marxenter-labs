/*
 * Created on 23.03.2011
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXSlider;
import org.jdesktop.swingx.plaf.XSliderDelegateUI;

public class BasicXSliderDelegateUI implements XSliderDelegateUI {

    private JXSlider slider;
    private Rectangle thumbExtRect;

    @Override
    public void install(JXSlider slider) {
        this.slider = slider;
        thumbExtRect = new Rectangle();
    }

    @Override
    public void uninstall(JXSlider slider) {
        this.slider = null;
        thumbExtRect = null;
    }

    @Override
    public ChangeListener createChangeListener(ChangeListener delegate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MouseListener createMouseListener(MouseListener delegate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void paintThumb(Graphics g) {
        // TODO Auto-generated method stub

    }

    
    protected JXSlider getSlider() {
        return slider;
    }
    
    

}
