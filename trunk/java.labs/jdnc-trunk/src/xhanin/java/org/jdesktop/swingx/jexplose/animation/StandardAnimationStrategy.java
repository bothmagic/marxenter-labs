/*
 * $Id: StandardAnimationStrategy.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.animation;

import java.awt.Point;
import java.util.List;
import java.util.ListIterator;

import org.jdesktop.swingx.jexplose.AnimationStrategy;
import org.jdesktop.swingx.jexplose.core.AnimationUpdate;
import org.jdesktop.swingx.jexplose.core.ExploseLayout;
import org.jdesktop.swingx.jexplose.core.Thumbnail;


/**
 * This is the standard animation strategy, which moves internal frames
 * linearly. This animation can be customized with a number of steps
 * and a period between each step.
 * 
 * @see org.jdesktop.swingx.jexplose.JExplose
 * 
 * @author Xavier Hanin
 */
public class StandardAnimationStrategy implements AnimationStrategy {
    private int _nbSteps;
    private long _period;

    /**
     * Unique constructor. Use it to define your own settings
     * for the number of steps and the period between each step.
     * Usual settings are between 2 and 10 steps, with a period between
     * 20 and 60. Use the explose demo to configure it adequatly.
     * 
     * @param nbSteps the number of steps this animation will do 
     * betwee the initial and the final positions. Must be non negative.
     * With no steps (0), behave like a no animation strategy.
     * @param period the period in millisecond between each step.
     * Must be non negative. With a 0 period, no pause is marked
     * between each step; 
     */
    public StandardAnimationStrategy(int nbSteps, long period) {
        if (nbSteps < 0) {
            throw new IllegalArgumentException("negative number of steps not allowed");
        }
        if (period < 0) {
            throw new IllegalArgumentException("negative period not allowed");
        }
        _nbSteps = nbSteps;
        _period = period;
    }

    /**
     * Called by JExplose to explose internal frames
     */
    public void explose(final ExploseLayout layout, final AnimationUpdate update) {
        anim(layout, update, false);
    }


    /**
     * Called by JExplose to implose internal frames
     */
    public void implose(final ExploseLayout layout, final AnimationUpdate update) {
        anim(layout, update, true);
    }

    private void anim(final ExploseLayout layout, final AnimationUpdate update, boolean reverse) {
        double finalScale = layout.getFinalScale();
        Point finalOffset = layout.getFinalOffset();
        double scaleStep = (1.0 - finalScale)/ (_nbSteps + 1);
        double offsetXStep = (double)finalOffset.x / (_nbSteps + 1);
        double offsetYStep = (double)finalOffset.y / (_nbSteps + 1);
        Point offset = new Point();
        int add = reverse?-1:1;
        for (int step = (reverse?_nbSteps-1:0); (reverse?step>=0:step<_nbSteps); step+=add) {
            List thumbs = layout.getThumbs();
            synchronized (layout) {
                for (ListIterator it = thumbs.listIterator(); it.hasNext();) {
                    Thumbnail thumb = (Thumbnail)it.next();
                    synchronized (thumb) {
                        thumb.x = thumb.getOriginalLocation().x + ((thumb.getFinalLocation().x - thumb.getOriginalLocation().x) / (_nbSteps + 1)) * (step+1);
                        thumb.y = thumb.getOriginalLocation().y + ((thumb.getFinalLocation().y - thumb.getOriginalLocation().y) / (_nbSteps + 1)) * (step+1);
                    }
                }
                layout.computeDimension();
                offset.x = (int)(offsetXStep * (step+1));
                offset.y = (int)(offsetYStep * (step+1));
                layout.updateScale(1.0 - (scaleStep * (step+1)), offset);
            }
            update.step();
            if (_period > 0) {
                try {
                    Thread.sleep(_period);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
        update.end();
    }
}
