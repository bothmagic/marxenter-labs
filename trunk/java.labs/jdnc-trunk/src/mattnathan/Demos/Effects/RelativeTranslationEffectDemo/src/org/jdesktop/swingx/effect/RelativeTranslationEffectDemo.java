/*
 * $Id: RelativeTranslationEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import static java.awt.GridBagConstraints.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.TestFrame;

/**
 * Simple demo for showing off the RelativeTranslationEffect
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class RelativeTranslationEffectDemo extends AbstractGraphicsEffectDemo<RelativeTranslationEffect<Object>> {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Translate Transform Effect Demo", new RelativeTranslationEffectDemo()).setVisible(true);
    }





    private BoundedRangeModel translatex;
    private BoundedRangeModel translatey;

    private EventBinding eventBinding;

    public RelativeTranslationEffectDemo() {
    }





    public RelativeTranslationEffectDemo(RelativeTranslationEffect<Object> source) {
        super(source);
    }





    public RelativeTranslationEffectDemo(RelativeTranslationEffect<Object> source, boolean previewVisible) {
        super(source, previewVisible);
    }





    @Override
    protected RelativeTranslationEffect<Object> createDefaultSource() {
        return new RelativeTranslationEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        translatex = new DefaultBoundedRangeModel(0, 0, -200, 200);
        translatey = new DefaultBoundedRangeModel(0, 0, -200, 200);

        // components
        JSlider scalexSlider = new JSlider(translatex);
        JSlider scaleySlider = new JSlider(translatey);

        scalexSlider.setMajorTickSpacing(100);
        scalexSlider.setMinorTickSpacing(25);
        scalexSlider.setPaintTicks(true);

        scaleySlider.setMajorTickSpacing(100);
        scaleySlider.setMinorTickSpacing(25);
        scaleySlider.setPaintTicks(true);

        // listeners
        eventBinding = new EventBinding();
        translatex.addChangeListener(eventBinding);
        translatey.addChangeListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Translate X:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 4, 4);
        result.add(new JLabel("-200%"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(scalexSlider, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 4, 4, 0);
        result.add(new JLabel("200%"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 0, 6);
        result.add(new JLabel("Translate Y:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 0, 4);
        result.add(new JLabel("-200%"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 0, 0);
        result.add(scaleySlider, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 4, 0, 0);
        result.add(new JLabel("200%"), gbc);
        return result;
    }





    private class EventBinding implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            if (model == translatex) {
                RelativeTranslationEffect<?> effect = getSource();
                if (effect != null) {
                    effect.setTranslateX(model.getValue() / 100d);
                }
            } else {
                assert model == translatey;
                RelativeTranslationEffect<?> effect = getSource();
                if (effect != null) {
                    effect.setTranslateY(model.getValue() / 100d);
                }
            }
        }
    }
}
