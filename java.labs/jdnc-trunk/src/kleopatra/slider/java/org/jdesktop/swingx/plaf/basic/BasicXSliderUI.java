/*
 * Created on 22.03.2011
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

import org.jdesktop.swingx.JXSlider;

public class BasicXSliderUI extends BasicSliderUI {
    private Rectangle thumbExtRect;
    
    

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

    
    /** 
     * @inherited <p>
     */
    @Override
    protected Dimension getThumbSize() {
        Dimension dim = super.getThumbSize();
//        if (getSlider().isRangeEnabled()) {
//            dim.width /= 2;
//        }
        return dim;
    }

    
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
        paintThumb(g, thumbRect);
        if (getSlider().isRangeEnabled()) {
            paintThumb(g, thumbExtRect);
        }
    }


    private void paintThumb(Graphics g, Rectangle knobBounds) {
        int w = knobBounds.width;
        int h = knobBounds.height;      

        g.translate(knobBounds.x, knobBounds.y);

        if ( slider.isEnabled() ) {
            g.setColor(slider.getBackground());
        }
        else {
            g.setColor(slider.getBackground().darker());
        }

        Boolean paintThumbArrowShape =
            (Boolean)slider.getClientProperty("Slider.paintThumbArrowShape");

        if ((!slider.getPaintTicks() && paintThumbArrowShape == null) ||
            paintThumbArrowShape == Boolean.FALSE) {

            // "plain" version
            g.fillRect(0, 0, w, h);

            g.setColor(Color.black);
            g.drawLine(0, h-1, w-1, h-1);    
            g.drawLine(w-1, 0, w-1, h-1);    

            g.setColor(getHighlightColor());
            g.drawLine(0, 0, 0, h-2);
            g.drawLine(1, 0, w-2, 0);

            g.setColor(getShadowColor());
            g.drawLine(1, h-2, w-2, h-2);
            g.drawLine(w-2, 1, w-2, h-3);
        }
        else if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
//            int cw = w / 2;
            int cw = w;
            g.setColor(Color.black);
            g.fillRect(1, 1, w-3, h-1-cw);
            Polygon p = new Polygon();
            p.addPoint(1, h-cw);
            p.addPoint(cw-1, h-1);
            p.addPoint(w-2, h-1-cw);
            g.fillPolygon(p);       

//            g.setColor(getHighlightColor());
//            g.drawLine(0, 0, w-2, 0);
//            g.drawLine(0, 1, 0, h-1-cw);
//            g.drawLine(0, h-cw, cw-1, h-1); 
//
//            g.setColor(Color.black);
//            g.drawLine(w-1, 0, w-1, h-2-cw);    
//            g.drawLine(w-1, h-1-cw, w-1-cw, h-1);       
//
//            g.setColor(getShadowColor());
//            g.drawLine(w-2, 1, w-2, h-2-cw);    
//            g.drawLine(w-2, h-1-cw, w-1-cw, h-2);       
        }
        else {  // vertical
            int cw = h / 2;
            if(slider.getComponentOrientation().isLeftToRight()) {
                  g.fillRect(1, 1, w-1-cw, h-3);
                  Polygon p = new Polygon();
                  p.addPoint(w-cw-1, 0);
                  p.addPoint(w-1, cw);
                  p.addPoint(w-1-cw, h-2);
                  g.fillPolygon(p);

                  g.setColor(getHighlightColor());
                  g.drawLine(0, 0, 0, h - 2);                  // left
                  g.drawLine(1, 0, w-1-cw, 0);                 // top
                  g.drawLine(w-cw-1, 0, w-1, cw);              // top slant

                  g.setColor(Color.black);
                  g.drawLine(0, h-1, w-2-cw, h-1);             // bottom
                  g.drawLine(w-1-cw, h-1, w-1, h-1-cw);        // bottom slant

                  g.setColor(getShadowColor());
                  g.drawLine(1, h-2, w-2-cw,  h-2 );         // bottom
                  g.drawLine(w-1-cw, h-2, w-2, h-cw-1 );     // bottom slant
            }
            else {
                  g.fillRect(5, 1, w-1-cw, h-3);
                  Polygon p = new Polygon();
                  p.addPoint(cw, 0);
                  p.addPoint(0, cw);
                  p.addPoint(cw, h-2);
                  g.fillPolygon(p);

                  g.setColor(getHighlightColor());
                  g.drawLine(cw-1, 0, w-2, 0);             // top
                  g.drawLine(0, cw, cw, 0);                // top slant

                  g.setColor(Color.black);
                  g.drawLine(0, h-1-cw, cw, h-1 );         // bottom slant
                  g.drawLine(cw, h-1, w-1, h-1);           // bottom

                  g.setColor(getShadowColor());
                  g.drawLine(cw, h-2, w-2,  h-2 );         // bottom
                  g.drawLine(w-1, 1, w-1,  h-2 );          // right
            }
        }

        g.translate(-knobBounds.x, -knobBounds.y);
    }

    
    protected JXSlider getSlider() {
        return (JXSlider) slider;
    }
    
    
    

    public static ComponentUI createUI(JComponent b)    {
        return new BasicXSliderUI((JXSlider)b);
    }


    public BasicXSliderUI(JXSlider slider)   {
        super(slider);
    }

    /** 
     * @inherited <p>
     */
    @Override
    public void installUI(JComponent c) {
        thumbExtRect = new Rectangle();
        super.installUI(c);
    }

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(BasicXSliderUI.class
            .getName());
}
