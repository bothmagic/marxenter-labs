/*
 * $Id: ShearTransformEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
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
 * Simple demo for showing off the ShearTransformEffect.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ShearTransformEffectDemo extends AbstractGraphicsEffectDemo<ShearTransformEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Shear Transform Effect Demo", new ShearTransformEffectDemo()).setVisible(true);
    }





    private BoundedRangeModel shearx;
    private BoundedRangeModel sheary;

    private EventBinding eventBinding;

    @Override
    protected ShearTransformEffect<Object> createDefaultSource() {
        return new ShearTransformEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        shearx = new DefaultBoundedRangeModel(0, 0, -200, 200);
        sheary = new DefaultBoundedRangeModel(0, 0, -200, 200);

        // components
        JSlider shearxSlider = new JSlider(shearx);
        JSlider shearySlider = new JSlider(sheary);

        shearxSlider.setMajorTickSpacing(100);
        shearxSlider.setMinorTickSpacing(25);
        shearxSlider.setPaintTicks(true);

        shearySlider.setMajorTickSpacing(100);
        shearySlider.setMinorTickSpacing(25);
        shearySlider.setPaintTicks(true);

        // listeners
        eventBinding = new EventBinding();
        shearx.addChangeListener(eventBinding);
        sheary.addChangeListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Shear X:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 4, 4);
        result.add(new JLabel("-200%"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(shearxSlider, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 4, 4, 0);
        result.add(new JLabel("200%"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 0, 6);
        result.add(new JLabel("Shear Y:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 0, 4);
        result.add(new JLabel("-200%"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 0, 0);
        result.add(shearySlider, gbc);

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
            if (model == shearx) {
                ShearTransformEffect<?> effect = getSource();
                if (effect != null) {
                    effect.setShearX(model.getValue() / 100d);
                }
            } else {
                assert model == sheary;
                ShearTransformEffect<?> effect = getSource();
                if (effect != null) {
                    effect.setShearY(model.getValue() / 100d);
                }
            }
        }
    }
}
