/*
 * $Id: EffectIconDemo.java 2756 2008-10-09 10:08:48Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.effect.BlurGraphicsEffect;
import org.jdesktop.swingx.effect.GraphicsEffect;
import org.jdesktop.swingx.effect.IdentityGraphicsEffect;
import org.jdesktop.swingx.effect.MaskGraphicsEffect;
import org.jdesktop.swingx.effect.ParallelGraphicsEffect;
import org.jdesktop.swingx.effect.RelativeTranslationEffect;
import org.jdesktop.swingx.effect.ScaleTransformEffect;
import org.jdesktop.swingx.effect.SerialGraphicsEffect;
import org.jdesktop.swingx.effect.StaticTranslationEffect;

public class EffectIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }


    
    @SuppressWarnings("unchecked")
    private static void createAndShowGUI(String[] args) {
        GraphicsEffect<Object> effect = new ParallelGraphicsEffect<Object>(
              new SerialGraphicsEffect<Object>(
                    ScaleTransformEffect.FLIP_VERTICAL,
                    MaskGraphicsEffect.FADE_DOWN,
                    new RelativeTranslationEffect<Object>(0, 1),
                    new StaticTranslationEffect<Object>(0, 10),
                    new BlurGraphicsEffect<Object>(2)),
              IdentityGraphicsEffect.INSTANCE);
        EffectIcon icon = new EffectIcon(new TempIcon(100, 100), effect);
        new TestFrame("EffectIconDemo Test", new EffectIconDemo(icon)).setVisible(true);
    }





    public EffectIconDemo(ScalableIcon icon) {
        super(icon);
    }
}
