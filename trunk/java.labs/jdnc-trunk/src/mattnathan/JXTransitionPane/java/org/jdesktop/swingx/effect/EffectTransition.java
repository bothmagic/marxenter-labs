/*
 * $Id: EffectTransition.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.beans.AbstractBean;

import java.awt.*;

/**
 * Generated comment for EffectTransition.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class EffectTransition<P, T> extends AbstractBean implements TransitionEffect<P, T> {

    private PropertySetter[] properties;
    private GraphicsEffect<? super T> effect;





    public EffectTransition(GraphicsEffect<? super T> effect, PropertySetter... properties) {
        this.effect = effect;
        this.properties = properties;
    }





    public void paintTransition(Graphics g, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        if (effect != null) {
            if (properties != null) {
                for (PropertySetter property : properties) {
                    property.timingEvent(bias);
                }
            }
            effect.paintEffect(g, from);

            if (properties != null) {
                for (PropertySetter property : properties) {
                    property.timingEvent(1 - bias);
                }
            }
            effect.paintEffect(g, to);
        }
    }





    public boolean transform(Rectangle area, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        return bias > 0 && bias < 1;
    }
}
