/*
 * $Id: BackgroundPainter.java 2730 2008-10-08 09:08:28Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.painter;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * A Simple painter which paints the given components background colour over the specified area.
 *
 * @author Matt Nathan
 */
public class BackgroundPainter<T extends Component> extends AbstractPainter<T> {

    /**
     * Creates a new instance of BackgroundPainter
     */
    public BackgroundPainter() {
    }





    /**
     * Subclasses should implement this method and perform custom painting operations here. Common behaviour, such as
     * setting the clip and composite, saving and restoring state, is performed in the "paint" method automatically, and
     * then delegated here.
     *
     * @param g The Graphics2D object in which to paint
     * @param component The JComponent that the Painter is delegate for.
     */
    @Override
    protected void doPaint(Graphics2D g, T component, int width, int height) {
        g.setColor(component.getBackground());
        g.fillRect(0, 0, width, height);
    }

}
