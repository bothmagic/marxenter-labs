/*
 * $Id: OverlayPolicyDemo.java 2804 2008-10-15 11:12:03Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import java.awt.Color;
import java.awt.Component;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.icon.AbstractScalableIconTest;
import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.icon.TempIcon;
import org.jdesktop.swingx.painter.GlossPainter;

public class OverlayPolicyDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        CompoundIcon icon = new CompoundIcon(new OverlayPolicy(),
                                             new TempIcon(100, 100, null, Color.RED),
                                             new PainterIcon<Component>(100, 100, new GlossPainter<Component>()));
        new TestFrame("OverlyPolicyTest Test", new OverlayPolicyDemo(icon)).setVisible(true);
    }





    public OverlayPolicyDemo(ScalableIcon icon) {
        super(icon);
    }
}
