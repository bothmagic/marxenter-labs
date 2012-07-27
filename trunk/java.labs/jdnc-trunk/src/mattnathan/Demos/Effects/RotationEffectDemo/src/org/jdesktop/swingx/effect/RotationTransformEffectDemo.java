/*
 * $Id: RotationTransformEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
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
 * Generated comment for RotationTransformEffectDemo.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class RotationTransformEffectDemo extends AbstractGraphicsEffectDemo<RotationTransformEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Rotation Transform Effect Demo", new RotationTransformEffectDemo()).setVisible(true);
    }





    private BoundedRangeModel rotation;

    private EventBinding eventBinding;

    @Override
    protected RotationTransformEffect<Object> createDefaultSource() {
        return new RotationTransformEffect<Object>(Math.toRadians(90));
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        rotation = new DefaultBoundedRangeModel((int) Math.toDegrees(getSource().getRotation()), 0, -180, 180);

        // components
        JSlider rootationSlider = new JSlider(rotation);

        rootationSlider.setMajorTickSpacing(90);
        rootationSlider.setMinorTickSpacing(15);
        rootationSlider.setPaintTicks(true);

        // listeners
        eventBinding = new EventBinding();
        rotation.addChangeListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Rotation:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 4, 4);
        result.add(new JLabel("-180%"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(rootationSlider, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = WEST;
        gbc.insets.set(0, 4, 4, 0);
        result.add(new JLabel("180%"), gbc);
        return result;
    }





    private class EventBinding implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            RotationTransformEffect<?> effect = getSource();
            if (effect != null) {
                effect.setRotation(Math.toRadians(model.getValue()));
            }
        }
    }
}
