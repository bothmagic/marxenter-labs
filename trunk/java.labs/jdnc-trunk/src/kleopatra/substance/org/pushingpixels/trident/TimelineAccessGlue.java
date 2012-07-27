/*
 * Created on 09.12.2009
 *
 */
package org.pushingpixels.trident;

import java.util.logging.Logger;

import org.pushingpixels.trident.TimelinePropertyBuilder.AbstractFieldInfo;

/**
 * Same functionality as super, main purpose is to widen access to 
 * package private state.
 */
public class TimelineAccessGlue extends Timeline {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TimelineAccessGlue.class
            .getName());
    private float initialDurationFraction;
    
    protected TimelineAccessGlue() {
    }
    
    protected TimelineAccessGlue(Object target) {
        super(target);
    }
    
    protected Object getTarget() {
        return mainObject;
    }
    
    
    public void moveToDurationFraction(float fraction) {
        if (this.getState() != TimelineState.IDLE) {
            throw new IllegalStateException(
                            "Cannot change state of non-idle timeline");
        }
        initialDurationFraction = fraction;
        // PENDNG JW: PropertyInterpolators should be updated here - but need to
        // to after READY because of TimelineEngine zeroing somewhere in readying.
    }
    
    /**
     * Hack around TimelineEngine forcing everything to 0.0 initially.
     */
    public void tweakInitialState() {
       durationFraction = initialDurationFraction; 
       timelinePosition = ease.map(durationFraction);
       for (AbstractFieldInfo info : propertiesToInterpolate) { 
           info.updateFieldValue(timelinePosition);
       }
        
    
    }
    


}
