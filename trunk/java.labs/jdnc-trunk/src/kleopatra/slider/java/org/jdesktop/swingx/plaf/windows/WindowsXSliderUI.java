/*
 * Created on 23.03.2011
 *
 */
package org.jdesktop.swingx.plaf.windows;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXSlider;
import org.jdesktop.swingx.plaf.windows.TMSchema.Part;
import org.jdesktop.swingx.plaf.windows.TMSchema.State;
import org.jdesktop.swingx.plaf.windows.XPStyle.Skin;

import com.sun.java.swing.plaf.windows.WindowsSliderUI;

public class WindowsXSliderUI extends WindowsSliderUI {

    
    private Rectangle thumbExtRect;
    private boolean rollover = false;
    private boolean pressed = false;
   
    

    /** 
     * @inherited <p>
     */
    @Override
    protected ChangeListener createChangeListener(final JSlider slider) {
        final ChangeListener delegate = super.createChangeListener(slider);
        ChangeListener wrapper = new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                if (((JXSlider) slider).isRangeEnabled()) {
                    calculateGeometry();
                    slider.repaint();
                } else {
                    delegate.stateChanged(e);
                }
            }
        };
        return wrapper;
    }


    /** 
     * @inherited <p>
     */
    @Override
    protected void calculateThumbSize() {
        super.calculateThumbSize();
        thumbExtRect.setSize(thumbRect.width, thumbRect.height);
    }

    
//    /** 
//     * @inherited <p>
//     */
//    @Override
//    protected Dimension getThumbSize() {
//        Dimension dim = super.getThumbSize();
////        if (getSlider().isRangeEnabled()) {
////            dim.width /= 2;
////        }
//        return dim;
//    }

    
    /** 
     * @inherited <p>
     */
    @Override
    protected void calculateThumbLocation() {
        super.calculateThumbLocation();
        if (getSlider().isRangeEnabled()) {
            if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
                int valuePosition = xPositionForValue(slider.getValue() + slider.getExtent());

                thumbExtRect.x = valuePosition - (thumbExtRect.width / 2);
                thumbExtRect.y = trackRect.y;
            }
            else {
                int valuePosition = yPositionForValue(slider.getValue() + slider.getExtent());
                
                thumbExtRect.x = trackRect.x;
                thumbExtRect.y = valuePosition - (thumbExtRect.height / 2);
            }
            
        }
    }

    @Override
    public void paintThumb(Graphics g)  {    
        LOG.info("paint?");
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            Part part = getXPThumbPart();
            State state = State.NORMAL;

            if (slider.hasFocus()) {
                state = State.FOCUSED;
            }
            if (rollover) {
                state = State.HOT;
            }
            if (pressed) {
                state = State.PRESSED;
            }
            if(!slider.isEnabled()) {
                state = State.DISABLED;
            }
            
            xp.getSkin(slider, part).paintSkin(g, thumbRect.x, thumbRect.y, state);
            if (getSlider().isRangeEnabled()) {
                xp.getSkin(slider, part).paintSkin(g, thumbExtRect.x, thumbExtRect.y, state);
            }
        } else {
            super.paintThumb(g);
        }
    }

    @Override
    protected Dimension getThumbSize() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            Dimension size = new Dimension();
            Skin s = xp.getSkin(slider, getXPThumbPart());
            size.width = s.getWidth();
            size.height = s.getHeight();
            return size;
        } else {
            return super.getThumbSize();
        }
    }

    private Part getXPThumbPart() {
        XPStyle xp = XPStyle.getXP();
        Part part;
        boolean vertical = (slider.getOrientation() == JSlider.VERTICAL);
        boolean leftToRight = slider.getComponentOrientation().isLeftToRight();
        Boolean paintThumbArrowShape =
                (Boolean)slider.getClientProperty("Slider.paintThumbArrowShape");
        if ((!slider.getPaintTicks() && paintThumbArrowShape == null) ||
            paintThumbArrowShape == Boolean.FALSE) {
                part = vertical ? Part.TKP_THUMBVERT
                                : Part.TKP_THUMB;
        } else {
                part = vertical ? (leftToRight ? Part.TKP_THUMBRIGHT : Part.TKP_THUMBLEFT)
                                : Part.TKP_THUMBBOTTOM;
        }
        return part;
    }
    
    
    protected JXSlider getSlider() {
        return (JXSlider) slider;
    }
    

    /** 
     * @inherited <p>
     */
    @Override
    public void installUI(JComponent c) {
        thumbExtRect = new Rectangle();
        super.installUI(c);
    }
    

    public WindowsXSliderUI(JXSlider slider) {
        super(slider);
    }

    public static ComponentUI createUI(JComponent b) {
        return new WindowsXSliderUI((JXSlider)b);
    }

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(WindowsXSliderUI.class
            .getName());
}
