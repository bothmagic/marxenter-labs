/*
 * $Id: MorphingShapeDemo.java 915 2006-10-22 02:59:18Z gfx $
 *
 * Dual-licensed under LGPL (Sun and Romain Guy) and BSD (Romain Guy).
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * Copyright (c) 2006 Romain Guy <romain.guy@mac.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jdesktop.swingx.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.GradientPaint;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.geom.Morphing2D;
import org.jdesktop.swingx.geom.Star2D;

/**
 * <p>See {@link org.jdesktop.swingx.geom.Morphing2D}.</p>
 *
 * @author Romain Guy <romain.guy@mac.com>
 */
public class MorphingShapeDemo extends JFrame {
        private MorphingShapePanel morphingShapePanel;
    private JSlider morphingSlider;

    public MorphingShapeDemo() {
        super("Morphing Shape");

        morphingShapePanel = new MorphingShapePanel();
        add(morphingShapePanel);

        morphingSlider = new JSlider(0, 100, 0);
        morphingSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                morphingShapePanel.setMorphing(morphingSlider.getValue() / 100.0);
            }
        });

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controls.add(new JLabel("Morphing: 0%"));
        controls.add(morphingSlider);
        controls.add(new JLabel("100%"));

        add(controls, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class MorphingShapePanel extends JPanel {
        private Morphing2D shape;
        private Rectangle bounds = new Rectangle();
        private static final int BORDER = 10;

        public MorphingShapePanel() {
            this.shape = buildMorphingShape();
            calculateBounds();
        }

        private static Morphing2D buildMorphingShape() {
            Shape startShape = new Rectangle2D.Double(20.0, 20.0, 80.0, 80.0);
            Shape endShape = new Star2D(0.0, 0.0, 40.0, 60.0, 12);

            return new Morphing2D(startShape, endShape);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BORDER * 2 + bounds.width,
                                 BORDER * 2 + bounds.height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            g2.translate((getWidth() - bounds.width) / 2,
                         (getHeight() - bounds.height) / 2);
            g2.setPaint(new GradientPaint(
                    new Point2D.Double(0, 0),
                    new Color(81, 141, 236),
                    new Point2D.Double(0, bounds.height),
                    new Color(36, 96, 192)));
            g2.fill(shape);

            g2.dispose();
        }

        public void setMorphing(double morphing) {
            this.shape.setMorphing(morphing);
            repaint();
        }

        public void calculateBounds() {
            double savet = shape.getMorphing();
            shape.setMorphing(0);
            bounds = shape.getBounds();
            shape.setMorphing(1);
            bounds.add(shape.getBounds());
            shape.setMorphing(savet);
        }
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MorphingShapeDemo().setVisible(true);
            }
        });
    }
}
