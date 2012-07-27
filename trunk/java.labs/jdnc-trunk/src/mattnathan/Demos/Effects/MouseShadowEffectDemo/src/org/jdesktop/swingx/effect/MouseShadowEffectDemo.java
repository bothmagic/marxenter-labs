package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.JXEffectPanel;
import org.jdesktop.swingx.TestComponent;
import org.jdesktop.swingx.TestFrame;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Small demo for shadows, this is very buggy and needs work.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MouseShadowEffectDemo extends JXComponent {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI(args);
            }
        });
    }





    private static void createAndShowUI(String[] args) {
        MouseShadowEffectDemo mse = new MouseShadowEffectDemo();
        Adapter a = mse.new Adapter();
        final JPanel gp = new JPanel(null);
        gp.setOpaque(false);

        gp.addMouseListener(a);
        gp.addMouseMotionListener(a);

        TestFrame tf = new TestFrame("MouseShadowEffectDemo Demo", mse);
        tf.setGlassPane(gp);
        tf.getGlassPane().setVisible(true);
        tf.setVisible(true);
    }





    private JXEffectPanel effectPanel;
    private JComponent testComponent;
    private ShadowEffect<Object> effect;





    public MouseShadowEffectDemo() {
        updateUI();
        init();
    }





    private void init() {
        setOpaque(false);

        setLayout(new Layout());
        testComponent = new TestComponent();

        effect = new ShadowEffect<Object>();

        effectPanel = new JXEffectPanel(effect);

        effectPanel.add(testComponent);

        add(effectPanel);
    }





    private void configure(int x, int y) {
        // translate to central coordinates
        x -= getWidth() / 2;
        y -= getHeight() / 2;

        final float minAlpha = 0.4f;
        final float maxAlpha = 0.9f;
        final float maxAlphaDistance = (float) Math.hypot(testComponent.getWidth() / 2, testComponent.getHeight() / 2);

        final float minBlur = 0;
        final float maxBlur = 15;
        final float maxBlurDistance = maxAlphaDistance * 2;

        int distance = (int) Math.hypot(x, y);
        float alpha = minAlpha + (maxAlpha - minAlpha) * (distance / maxAlphaDistance);
        float blur = minBlur + (maxBlur - minBlur) * (distance / maxBlurDistance);
        float angle;
        if (x < 0) {
            angle = (float) (Math.atan(y / (float) x));
        } else if (x > 0) {
            angle = (float) (Math.atan(y / (float) x) - Math.PI);
        } else {
            angle = (float) (y > 0 ? Math.PI * 3 / 2 : Math.PI / 2);
        }

        effect.setDistance(distance);
        effect.setOpacity(1 - alpha);
        effect.setBlur(blur);
        effect.setAngle(angle);
    }





    private class Adapter implements MouseMotionListener, MouseListener {
        private void update(MouseEvent e) {
            configure(e.getX(), e.getY());
            invalidate();
            repaint();
        }





        public void mouseDragged(MouseEvent e) {
            update(e);
        }





        public void mouseMoved(MouseEvent e) {
            update(e);
        }





        public void mouseClicked(MouseEvent e) {
        }





        public void mousePressed(MouseEvent e) {
        }





        public void mouseReleased(MouseEvent e) {
        }





        public void mouseEntered(MouseEvent e) {
            update(e);
        }





        public void mouseExited(MouseEvent e) {
            update(e);
        }
    }


    private class Layout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }





        public void removeLayoutComponent(Component comp) {
        }





        public Dimension preferredLayoutSize(Container parent) {
            return effectPanel.getPreferredSize();
        }





        public Dimension minimumLayoutSize(Container parent) {
            return effectPanel.getMinimumSize();
        }





        public void layoutContainer(Container parent) {
            int x;
            int y;
            int w;
            int h;

            Dimension effectPref = effectPanel.getPreferredSize();
            w = effectPref.width;
            h = effectPref.height;

            effectPanel.setSize(w, h);
            effectPanel.doLayout();

            Point off = SwingUtilities.convertPoint(testComponent, 0, 0, effectPanel);

            int offx = off.x;
            int offy = off.y;

            x = (parent.getWidth() - testComponent.getWidth()) / 2;
            y = (parent.getHeight() - testComponent.getHeight()) / 2;

            x -= offx;
            y -= offy;

            effectPanel.setBounds(x, y, w, h);
        }
    }
}
