/*
 * TransitionPainter.java
 *
 * Created on April 25, 2006, 5:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.painter;

import com.jhlabs.image.OffsetFilter;
import com.jhlabs.image.OpacityFilter;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import org.jdesktop.animation.timing.Cycle;
import org.jdesktop.animation.timing.Envelope;
import org.jdesktop.animation.timing.TimingController;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.ObjectModifier;
import org.jdesktop.animation.timing.interpolation.PropertyRange;

/**
 *
 * @author Bill
 */
public class FlyInTransitionEffect implements TransitionEffect {
    
    private OffsetFilter offset = new OffsetFilter();
    private OpacityFilter opacity = new OpacityFilter();
        
    private Cycle cycle;
    private Envelope envelope;
    private PropertyRange propertyRange;
    private TimingController controller;
    
    private JFrame owner;
    
    
    /** Creates a new instance of TransitionPainter */
    public FlyInTransitionEffect(JFrame parent) {
        super();
        this.owner = parent;
        setupEffects();
        setupTimingController();
        
    }

    private void setupTimingController() {
        cycle = new Cycle(2000, 60);
        envelope = new Envelope(TimingController.INFINITE, 0,
            Envelope.RepeatBehavior.REVERSE, Envelope.EndBehavior.RESET); 
        propertyRange =
                PropertyRange.createPropertyRangeInt("yOffset", 0,200);
                //PropertyRange.createPropertyRangePoint("location",from,to)
        ObjectModifier modifier = new ObjectModifier(offset, propertyRange);
        controller = new TimingController(cycle,envelope, modifier);
        
        PropertyRange opacityRange = 
                PropertyRange.createPropertyRangeInt("xOffset",0,50);
        final ObjectModifier omodifier = new ObjectModifier(offset, opacityRange);
        
        controller.addTarget(new TimingTarget(){
            public void timingEvent(long cycleElapsedTime, long totalElapsedTime, float fraction) {
                //JOptionPane.getRootFrame().repaint();
                omodifier.timingEvent(cycleElapsedTime,totalElapsedTime,fraction);
                owner.repaint();
                //maybe try getFrameForComponent(jcomponent).repaint();
            }

            public void begin() {
                owner.repaint();
            }

            public void end() {
                owner.repaint();
            }
        
        });
        
    }
    
    public void start(){
        this.controller.start();
    }
    
    public void stop(){
        this.controller.stop();
    }

    private void setupEffects() {
        offset.setWrap(false);
        offset.setYOffset(0);
        offset.setXOffset(0);
        opacity.setOpacity(100);
    }

    public BufferedImage apply(BufferedImage image) {
        return offset.filter(image,null);
    }
    
    public String toString(){
        return "Fly In";
    }
}
