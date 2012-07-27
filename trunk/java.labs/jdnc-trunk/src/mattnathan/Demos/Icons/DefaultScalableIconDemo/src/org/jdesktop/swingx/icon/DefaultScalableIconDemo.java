/*
 * $Id: DefaultScalableIconDemo.java 2756 2008-10-09 10:08:48Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Color;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.icon.range.IconRange;

public class DefaultScalableIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        TempIcon one = new TempIcon(32, 32, null, Color.RED);
        TempIcon two = new TempIcon(128, 128, null, Color.GREEN);
        TempIcon three = new TempIcon(256, 256, null, Color.BLUE);
        DefaultScalableIcon icon = new DefaultScalableIcon(
              new IconRange(one),
              new IconRange(two),
              new IconRange(three));
        new TestFrame("DefaultScalableIconDemo Test", new DefaultScalableIconDemo(icon)).setVisible(true);
    }





    public DefaultScalableIconDemo(ScalableIcon icon) {
        super(icon);
    }
}
