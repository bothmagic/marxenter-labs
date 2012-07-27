/*
 * $Id: PanTransition.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.beans.AbstractBean;

import java.awt.*;

/**
 * Generated comment for PanTransition.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class PanTransition<P, T> extends AbstractBean implements TransitionEffect<P, T> {


    public static enum Direction {
        NORTH, SOUTH, WEST, EAST, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST
    }

    public static enum LayerPolicy {
        MOVE, COVER, EXPOSE
    }


    private Direction direction;
    private LayerPolicy layerPolicy;
    private RelativeTranslationEffect<T> effect;



    public PanTransition() {
        this(Direction.EAST, LayerPolicy.MOVE);
    }





    public PanTransition(Direction direction, LayerPolicy layerPolicy) {
        this.direction = direction;
        this.layerPolicy = layerPolicy;
    }





    public void paintTransition(Graphics g, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        if(effect == null) {
            effect = new RelativeTranslationEffect<T>();
        }
        float x1 = 0;
        float x2 = 0;
        float y1 = 0;
        float y2 = 0;
        switch (direction) {
            case SOUTH:
                y1 = bias;
                y2 = bias - 1;
                break;
            case WEST:
                x1 = -bias;
                x2 = 1 - bias;
                break;
            case EAST:
                x1 = bias;
                x2 = bias - 1;
                break;
            case NORTH:
                y1 = -bias;
                y2 = 1 - bias;
                break;
            case NORTH_EAST:
                y1 = -bias;
                y2 = 1 - bias;
                x1 = bias;
                x2 = bias - 1;
                break;
            case NORTH_WEST:
                y1 = -bias;
                y2 = 1 - bias;
                x1 = -bias;
                x2 = 1 - bias;
                break;
            case SOUTH_EAST:
                y1 = bias;
                y2 = bias - 1;
                x1 = bias;
                x2 = bias - 1;
                break;
            case SOUTH_WEST:
                y1 = bias;
                y2 = bias - 1;
                x1 = -bias;
                x2 = 1 - bias;
                break;
        }
        switch (layerPolicy) {
            case MOVE:
                break;
            case COVER:
                x1 = y1 = 0;
                break;
            case EXPOSE:
                TransitionSource<? extends P, ? extends T> tmp = from;
                from = to;
                to = tmp;
                x2 = x1;
                y2 = y1;
                x1 = y1 = 0;
                break;
        }

        if (from != null) {
            effect.setTranslateX(x1);
            effect.setTranslateY(y1);
            effect.paintEffect(g, from);
        }
        if (to != null) {
            effect.setTranslateX(x2);
            effect.setTranslateY(y2);
            effect.paintEffect(g, to);
        }
    }





    public boolean transform(Rectangle area, TransitionSource<? extends P, ? extends T> from, TransitionSource<? extends P, ? extends T> to, float bias) {
        return bias > 0 && bias < 1;
    }
}
