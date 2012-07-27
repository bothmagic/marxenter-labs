/*
 * $Id: BlurGraphicsEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
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
import javax.swing.BoundedRangeModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.TestFrame;

/**
 * Simple demo for showing off the BlurGraphicsEffect.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BlurGraphicsEffectDemo extends AbstractGraphicsEffectDemo<BlurGraphicsEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Blur Component Effect Demo", new BlurGraphicsEffectDemo()).setVisible(true);
    }





    private BoundedRangeModel blurRadius;
    private ComboBoxModel quality;

    private EventBinding eventBinding;

    @Override
    protected BlurGraphicsEffect<Object> createDefaultSource() {
        return new BlurGraphicsEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        blurRadius = new DefaultBoundedRangeModel((int) getSource().getBlurRadius(), 0, 0, 10);
        quality = new DefaultComboBoxModel(BlurGraphicsEffect.Quality.values());
        quality.setSelectedItem(getSource().getQuality());

        // components
        JSlider blurRadiusSlider = new JSlider(blurRadius);

        blurRadiusSlider.setMajorTickSpacing(5);
        blurRadiusSlider.setMinorTickSpacing(1);
        blurRadiusSlider.setPaintTicks(true);

        JComboBox qualityCombo = new JComboBox(quality);

        // listeners
        eventBinding = new EventBinding();
        blurRadius.addChangeListener(eventBinding);
        qualityCombo.addActionListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 4);
        result.add(new JLabel("Blur Quality:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(qualityCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Blur Radius:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = EAST;
        gbc.insets.set(0, 0, 4, 4);
        result.add(new JLabel("0"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(blurRadiusSlider, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 4, 4, 0);
        result.add(new JLabel("10"), gbc);
        return result;
    }





    private class EventBinding implements ChangeListener, ActionListener {
        public void stateChanged(ChangeEvent e) {
            BoundedRangeModel model = (BoundedRangeModel) e.getSource();
            BlurGraphicsEffect<?> effect = getSource();
            if (effect != null) {
                effect.setBlurRadius(model.getValue());
            }
        }





        public void actionPerformed(ActionEvent e) {
            getSource().setQuality((BlurGraphicsEffect.Quality) ((JComboBox) e.getSource()).getSelectedItem());
        }
    }
}
