/*
 * $Id: MaskGraphicsEffectDemo.java 2752 2008-10-08 16:14:01Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import static java.awt.GridBagConstraints.*;
import java.awt.Color;
import java.awt.GradientPaint;
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
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.MattePainterTmp;
import org.jdesktop.swingx.painter.Painter;

/**
 * Simple demo designed to show of the MaskGraphicsEffect
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MaskGraphicsEffectDemo extends AbstractGraphicsEffectDemo<MaskGraphicsEffect<Object>> {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }





    private static void mainOnEDT(String[] args) {
        new TestFrame("Mask Component Effect Demo", new MaskGraphicsEffectDemo()).setVisible(true);
    }





    private ComboBoxModel mask;

    private EventBinding eventBinding;

    @Override
    protected MaskGraphicsEffect<Object> createDefaultSource() {
        return new MaskGraphicsEffect<Object>();
    }





    @Override
    protected JComponent createControls() {
        JComponent result = new JXPanel(new GridBagLayout());

        // models
        mask = new DefaultComboBoxModel(new Object[] {
                                        new MaskMeta(null, "None"),
                                        new MaskMeta(new MattePainterTmp<Object>(new GradientPaint(0, 0, Color.BLACK, 0, 1, new Color(0, 0, 0, 0), true)),
              "Fade out"),
                                        new MaskMeta(new MattePainterTmp<Object>(new GradientPaint(0, 1, Color.BLACK, 0, 0, new Color(0, 0, 0, 0), true)),
              "Fade in"),
                                        new MaskMeta(new CheckerboardPainter<Object>(Color.BLACK, new Color(0, 0, 0, 0)), "Checked")
        });

        // components

        JComboBox maskCombo = new JComboBox(mask);

        // listeners
        eventBinding = new EventBinding();
        maskCombo.addActionListener(eventBinding);

        // layout
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = WEST;
        gbc.insets.set(0, 0, 4, 6);
        result.add(new JLabel("Mask:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = CENTER;
        gbc.insets.set(0, 0, 4, 0);
        result.add(maskCombo, gbc);
        return result;
    }





    private static class MaskMeta {
        private Painter<Object> mask;
        private String name;

        public MaskMeta(Painter<Object> mask, String name) {
            this.mask = mask;
            this.name = name;
        }





        @Override
        public String toString() {
            return name;
        }





        public Painter<Object> getMask() {
            return mask;
        }
    }







    private class EventBinding implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            getSource().setMask(((MaskMeta) ((JComboBox) e.getSource()).getSelectedItem()).getMask());
        }
    }
}
