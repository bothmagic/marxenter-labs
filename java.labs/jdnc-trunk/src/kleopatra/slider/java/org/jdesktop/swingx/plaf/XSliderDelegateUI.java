/*
 * Created on 23.03.2011
 *
 */
package org.jdesktop.swingx.plaf;

import java.awt.Graphics;
import java.awt.event.MouseListener;

import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXSlider;

public interface XSliderDelegateUI {

    void install(JXSlider slider);
    
    void uninstall(JXSlider slider);
    
    ChangeListener createChangeListener(ChangeListener delegate);
    
    MouseListener createMouseListener(MouseListener delegate);

    void paintThumb(Graphics g);
    
}
