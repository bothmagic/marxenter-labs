/*
 * $Id: SerialGraphicsEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import static java.awt.GridBagConstraints.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.TestFrame;

/**
 * Simple demo showing variaous combinations of serial effects.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class SerialGraphicsEffectDemo extends AbstractGraphicsEffectDemo<SerialGraphicsEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Serial Component Effect Demo", new SerialGraphicsEffectDemo()).setVisible(true);
    }





    private ComboBoxModel children;

    private EventBinding eventBinding;

    @Override
    protected SerialGraphicsEffect<Object> createDefaultSource() {
        return new SerialGraphicsEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        children = new DefaultComboBoxModel(new Object[] {
                                            new EffectStack("None"),
                                            new EffectStack("Translate -> Blur",
              new RelativeTranslationEffect<Object>(0, 1),
              new BlurGraphicsEffect<Object>(2)),
                                            new EffectStack("Scale -> Translate",
              new ScaleTransformEffect<Object>(1, -1),
              new RelativeTranslationEffect<Object>(0, 1)),
                                            new EffectStack("Scale -> Mask -> Translate -> Blur",
              ScaleTransformEffect.FLIP_VERTICAL,
              MaskGraphicsEffect.FADE_DOWN,
              new RelativeTranslationEffect<Object>(0, 1),
              new BlurGraphicsEffect<Object>(2)),
                                            new EffectStack("Reflection",
              new ParallelGraphicsEffect<Object>(
                    new SerialGraphicsEffect<Object>(
                          ScaleTransformEffect.FLIP_VERTICAL,
                          MaskGraphicsEffect.FADE_DOWN,
                          new RelativeTranslationEffect<Object>(0, 1.1),
                          new BlurGraphicsEffect<Object>(2)),
                    IdentityGraphicsEffect.INSTANCE)),
                                            new EffectStack("Shadow",
              new ParallelGraphicsEffect<Object>(
                    new SerialGraphicsEffect<Object>(
                          new RadialTranslationEffect<Object>(Math.toRadians(45), 10),
                          new BlurGraphicsEffect<Object>(5),
                          ColorFilterEffect.ALPHA,
                          new TranslucentGraphicsEffect<Object>(0.5f)),
                    IdentityGraphicsEffect.INSTANCE))
        });

        // components
        JComboBox childrenCombo = new JComboBox(children);

        // listeners
        eventBinding = new EventBinding();
        childrenCombo.addActionListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Effect Stack:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(childrenCombo, gbc);
        return result;
    }





    private class EventBinding implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getSource().setChildren(((EffectStack) ((JComboBox) e.getSource()).getSelectedItem()).getEffects());
        }
    }







    private static class EffectStack {
        private GraphicsEffect<Object>[] effects;
        private String name;

        public EffectStack(String name, GraphicsEffect<Object>...effects) {
            this.effects = effects;
            this.name = name;
        }





        public GraphicsEffect<Object>[] getEffects() {
            return effects;
        }





        @Override
        public String toString() {
            return name;
        }
    }
}
