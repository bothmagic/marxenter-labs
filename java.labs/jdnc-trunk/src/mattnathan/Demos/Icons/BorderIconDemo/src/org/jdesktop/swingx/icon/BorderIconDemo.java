/*
 * $Id: BorderIconDemo.java 2756 2008-10-09 10:08:48Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Generated comment for BorderIconTest.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BorderIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                runOnEDT(args);
            }
        });
    }





    private static void runOnEDT(String[] args) {
        BorderIcon icon = new BorderIcon(new TempIcon(32, 64, null, new Color(255, 0, 0, 128)), BorderFactory.createRaisedBevelBorder());
        icon.setChildScalePolicy(null);
        ((AbstractScalableIcon) icon.getIcon()).setScalePolicy(ScalePolicy.valueOf(
              ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.BEST_FIT
              ));

        new TestFrame("BorderIcon Test", new BorderIconDemo(icon)).setVisible(true);
    }





    public BorderIconDemo(ScalableIcon icon) {
        super(icon);
    }

}
