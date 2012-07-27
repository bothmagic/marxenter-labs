/*
 * $Id: ReflectionEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.TestFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ReflectionEffectDemo extends AbstractGraphicsEffectDemo<ReflectionEffect<Object>> {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("ReflectionEffectDemo Test", new ReflectionEffectDemo()).setVisible(true);
    }





    public ReflectionEffectDemo() {
        super();
    }





    @Override
    protected JComponent createControls() {
        JPanel p = new JPanel(new GridBagLayout());

        JSlider length = new JSlider(0, 100, (int) (getSource().getLength() * 100));
        length.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setLength(((JSlider) e.getSource()).getValue() / 100f);
                ReflectionEffectDemo.this.repaint();
            }
        });
        JSlider opacity = new JSlider(0, 100, (int) (getSource().getOpacity() * 100));
        opacity.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setOpacity(((JSlider) e.getSource()).getValue() / 100f);
                ReflectionEffectDemo.this.repaint();
            }
        });

        JSlider blurRadius = new JSlider(0, 500, (int) (getSource().getBlurRadius() * 100));
        blurRadius.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setBlurRadius(((JSlider) e.getSource()).getValue() / 100f);
                ReflectionEffectDemo.this.repaint();
            }
        });

        JSlider gap = new JSlider(0, 50, getSource().getGap());
        gap.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setGap(((JSlider) e.getSource()).getValue());
                ReflectionEffectDemo.this.repaint();
            }
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets.set(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = -1;

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Length:"), gbc);
        gbc.gridx++;
        p.add(length, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Opacity:"), gbc);
        gbc.gridx++;
        p.add(opacity, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Gap:"), gbc);
        gbc.gridx++;
        p.add(gap, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Blur Radius:"), gbc);
        gbc.gridx++;
        p.add(blurRadius, gbc);

        return p;
    }





    @Override
    protected ReflectionEffect<Object> createDefaultSource() {
        return new ReflectionEffect<Object>();
    }
}
