/*
 * TransitionPainter.java
 *
 * Created on April 25, 2006, 5:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.painter;

import com.jhlabs.image.OpacityFilter;
import com.jhlabs.image.RippleFilter;
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
public class RippleTransitionEffect implements TransitionEffect {
    
    private RippleFilter ripple = new RippleFilter();
    private OpacityFilter opacity = new OpacityFilter();
        
    private Cycle cycle;
    private Envelope envelope;
    private PropertyRange propertyRange;
    private TimingController controller;
    
    private JFrame owner;
    
    
    /** Creates a new instance of TransitionPainter */
    public RippleTransitionEffect(JFrame parent) {
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
                PropertyRange.createPropertyRangeFloat("YAmplitude", 0f,25f);
        ObjectModifier modifier = new ObjectModifier(ripple, propertyRange);
        controller = new TimingController(cycle,envelope, modifier);
        
        PropertyRange xAmplitudeRange = 
                PropertyRange.createPropertyRangeFloat("XAmplitude", 0f,25f);
        final ObjectModifier xAmpmodifier = new ObjectModifier(ripple, xAmplitudeRange);
        
        PropertyRange opacityRange = 
                PropertyRange.createPropertyRangeInt("opacity",100,0);
        final ObjectModifier omodifier = new ObjectModifier(opacity, opacityRange);
    
        controller.addTarget(new TimingTarget(){
            public void timingEvent(long cycleElapsedTime, long totalElapsedTime, float fraction) {
                //JOptionPane.getRootFrame().repaint();
                omodifier.timingEvent(cycleElapsedTime,totalElapsedTime,fraction);
                xAmpmodifier.timingEvent(cycleElapsedTime,totalElapsedTime,fraction);
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
        ripple = new RippleFilter();
        ripple.setWaveType(RippleFilter.SINE);
        ripple.setXAmplitude(0f);
        ripple.setXWavelength(5f);
        ripple.setYAmplitude(0f);
        ripple.setYWavelength(5f);
        opacity.setOpacity(100);
    }

    public BufferedImage apply(BufferedImage image) {
        return opacity.filter(ripple.filter(image,null),null);
    }
    
    public String toString(){
        return "Ripple Fade";
    }
    
}
