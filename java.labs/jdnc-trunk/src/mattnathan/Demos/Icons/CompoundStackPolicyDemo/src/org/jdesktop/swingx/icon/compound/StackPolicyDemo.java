/*
 * $Id: StackPolicyDemo.java 2804 2008-10-15 11:12:03Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.icon.AbstractScalableIconTest;
import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.icon.TempIcon;

public class StackPolicyDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {

        StackPolicy policy = new StackPolicy();
        CompoundIcon icon = new CompoundIcon(policy,
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon());
//        icon.setChildScalePolicy(ScalePolicy.FIXED_RATIO);
//        policy.setVisibleIcons(StackPolicy.CONTAINER_SIZE);
//        policy.setScaleFactor(0.85f);
//        policy.setStackDirection(SwingConstants.SOUTH_WEST);

        new TestFrame("StackPolicyTest Test", new StackPolicyDemo(icon)).setVisible(true);
    }





    public StackPolicyDemo(ScalableIcon icon) {
        super(icon);
    }
}
