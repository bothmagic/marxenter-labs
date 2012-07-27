/*
 * $Id: AbstractScalableIconTest.java 2754 2008-10-09 09:39:29Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Dimension;
import java.awt.Graphics2D;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Generated comment for AbstractScalableIconTest.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class AbstractScalableIconTest extends JXPanel {
    private ScalableIcon icon;
    private ScalePolicy scalePolicy = ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.BEST_FIT);

    public AbstractScalableIconTest(ScalableIcon icon) {
        this.icon = icon;
        setOpaque(false);
        setBackgroundPainter(new AbstractPainter() {
            @Override
            protected void doPaint(Graphics2D g, Object object, int width, int height) {
                IconUtilities.paintChild(AbstractScalableIconTest.this.icon,
                                         AbstractScalableIconTest.this, g, 0, 0, width, height,
                                         scalePolicy);
            }
        });
    }





    public void setScalePolicy(ScalePolicy scalePolicy) {
        this.scalePolicy = scalePolicy;
        repaint();
    }





    public void setIcon(ScalableIcon icon) {
        this.icon = icon;
        repaint();
    }





    public ScalableIcon getIcon() {
        return icon;
    }





    @Override
    public Dimension getPreferredSize() {
        return new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }
}
