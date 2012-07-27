/*
 * $Id: PilePolicyDemo.java 2805 2008-10-15 11:18:36Z mattnathan $
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

public class PilePolicyDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        PilePolicy policy = new PilePolicy();
        CompoundIcon icon = new CompoundIcon(policy,
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
//                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon(),
                                             new TempIcon());
        policy.setScaleFactor(0.97f);
        policy.setVisibleIcons(PilePolicy.CONTAINER_SIZE);

        policy.setRotation(Math.PI / 2d);
        policy.setRotationType(PilePolicy.RotationType.SPANNING);
        new TestFrame("PilePolicyTest Test", new PilePolicyDemo(icon)).setVisible(true);
    }





    public PilePolicyDemo(ScalableIcon icon) {
        super(icon);
//        setBackground(new Color(255, 0, 0, 32));
    }
}
