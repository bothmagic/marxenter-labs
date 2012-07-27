/*
 * $Id: ColorFilterEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
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
 * Generated comment for ColorFilterEffectDemo.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ColorFilterEffectDemo extends AbstractGraphicsEffectDemo<ColorFilterEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Color Filter Effect Demo", new ColorFilterEffectDemo()).setVisible(true);
    }





    private ComboBoxModel transformation;

    private EventBinding eventBinding;

    @Override
    protected ColorFilterEffect<Object> createDefaultSource() {
        return new ColorFilterEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        transformation = new DefaultComboBoxModel(new Object[] {
                                                  new TransformationMeta(null, "None"),
                                                  new TransformationMeta(ColorFilterEffect.RED_FILTER, "Red"),
                                                  new TransformationMeta(ColorFilterEffect.GREEN_FILTER, "Green"),
                                                  new TransformationMeta(ColorFilterEffect.BLUE_FILTER, "Blue"),
                                                  new TransformationMeta(ColorFilterEffect.ALPHA_FILTER, "Alpha"),
                                                  new TransformationMeta(ColorFilterEffect.GRAYSCALE_FILTER, "Grayscale")
        });

        // components

        JComboBox transformationCombo = new JComboBox(transformation);

        // listeners
        eventBinding = new EventBinding();
        transformationCombo.addActionListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Transforation:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(transformationCombo, gbc);
        return result;
    }





    private static class TransformationMeta {
        private float[][] transformation;
        private String name;

        public TransformationMeta(float[][] transformation, String name) {
            this.transformation = transformation;
            this.name = name;
        }





        @Override
        public String toString() {
            return name;
        }





        public float[][] getTransformation() {
            return transformation;
        }
    }







    private class EventBinding implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getSource().setFilter(((TransformationMeta) ((JComboBox) e.getSource()).getSelectedItem()).getTransformation());
        }
    }
}
