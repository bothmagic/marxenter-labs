/*
 * $Id: ShadowEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.TestFrame;


public class ShadowEffectDemo extends AbstractGraphicsEffectDemo<ShadowEffect<Object>> {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("ShadowEffectDemo Test", new ShadowEffectDemo()).setVisible(true);
    }





    public ShadowEffectDemo() {
        super();
    }





    @Override
    protected JComponent createControls() {
        JPanel p = new JPanel(new GridBagLayout());

        JSlider angle = new JSlider(0, (int) (Math.PI * 200), (int) (getSource().getAngle() * 100));
        angle.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setAngle(((JSlider) e.getSource()).getValue() / 100f);
                ShadowEffectDemo.this.repaint();
            }
        });
        JSlider opacity = new JSlider(0, 100, (int) (getSource().getOpacity() * 100));
        opacity.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setOpacity(((JSlider) e.getSource()).getValue() / 100f);
                ShadowEffectDemo.this.repaint();
            }
        });

        JSlider blur = new JSlider(0, 900, (int) (getSource().getBlur() * 100));
        blur.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setBlur(((JSlider) e.getSource()).getValue() / 100f);
                ShadowEffectDemo.this.repaint();
            }
        });

        JSlider distance = new JSlider(0, 50, getSource().getDistance());
        distance.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setDistance(((JSlider) e.getSource()).getValue());
                ShadowEffectDemo.this.repaint();
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = -1;

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Angle:"), gbc);
        gbc.gridx++;
        p.add(angle, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Distance:"), gbc);
        gbc.gridx++;
        p.add(distance, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Opacity:"), gbc);
        gbc.gridx++;
        p.add(opacity, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Blur:"), gbc);
        gbc.gridx++;
        p.add(blur, gbc);

        return p;
    }





    @Override
    protected ShadowEffect<Object> createDefaultSource() {
        return new ShadowEffect<Object>();
    }
}
