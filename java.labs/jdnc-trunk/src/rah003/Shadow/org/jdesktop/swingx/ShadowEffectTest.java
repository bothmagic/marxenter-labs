package org.jdesktop.swingx;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.AreaEffect;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;


public class ShadowEffectTest extends JFrame {

    private static final Color CREAM = new Color(255, 251, 232);
    private final RectanglePainter painter = new RectanglePainter(0, 0, 0, 0, 5, 5, true, CREAM, 1f, Color.YELLOW.darker());
    private final ShadowPathEffect shadow = new ShadowPathEffect();

    public ShadowEffectTest() {
        painter.setPaintStretched(true);
        painter.setAreaEffects(shadow);
        shadow.setEffectWidth(2);
        shadow.setBrushColor(Color.GRAY);
        painter.setFillPaint(CREAM);

        final JXLabel lbl = new JXLabel("Hi there");
        lbl.setHorizontalAlignment(JXLabel.CENTER);
        lbl.setPreferredSize(new Dimension(100,100));
        lbl.setSize(lbl.getPreferredSize());
        lbl.setBackgroundPainter(painter);
        addComponentListener(new ComponentListener() {
            public void componentHidden(ComponentEvent e) {}
            public void componentMoved(ComponentEvent e) {}
            public void componentResized(ComponentEvent e) {
                lbl.setSize(getWidth() - 50, getHeight() - 150);
                lbl.repaint();
            }
            public void componentShown(ComponentEvent e) {}});

        final JXPanel p = new JXPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.translate(20,20);
                long stamp = System.nanoTime();
                lbl.paint(g);
                System.out.println("Painted lbl in " + (System.nanoTime() - stamp)/1000000 + "ms. Lbl size was " + lbl.getSize());
            }
        };
        add(p);
        JButton b = new JButton(new AbstractAction("Shadow on/off") {
            public void actionPerformed(ActionEvent e) {
                if (painter.getAreaEffects().length > 0) {
                    painter.setAreaEffects((AreaEffect[]) null);
                } else {
                    painter.setAreaEffects(shadow);
                }
                p.repaint();
            }
        });
        add(b, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(850,750));
    }

    public static void main(String[] args) {
        JFrame f = new ShadowEffectTest();
        f.setDefaultCloseOperation(EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }

}
