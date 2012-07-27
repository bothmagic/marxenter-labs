package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.Animator.EndBehavior;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.interpolation.SplineInterpolator;
import org.jdesktop.swingx.JXWheel;

public class WheelMachineDemo {

    public static class Result extends JComponent {
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private BufferedImage image;

        private float alpha;

        public Result() {
            setPreferredSize(new Dimension(80, 80));
            setMinimumSize(new Dimension(80, 80));
            setOpaque(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            g = g.create();
            if (image != null) {
                g.drawImage(image, 0, 0, null);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            if (alpha > 0) {
                g.setColor(new Color(1f,1f,1f,alpha));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            g.dispose();
        }

        public void setImage(BufferedImage screenshot) {
            Object old = this.image;
            this.image = screenshot;
            pcs.firePropertyChange("image", old, screenshot);
            repaint();
        }

        public BufferedImage getImage() {
            return image;
        }
        
        public void setAlpha(float f) {
            alpha = f;
            repaint();
        }
        
        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            this.pcs.addPropertyChangeListener(listener);
        }
        
        @Override
        public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
            pcs.removePropertyChangeListener(listener);
        }
    }

    private static JXWheel[] wheels = new JXWheel[3];

    private static Result[] results = new Result[3];

    /**
     * @param args
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Wheels");
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel troa = new JPanel(new GridLayout(1, 3));
        setWheel(0, troa);
        setWheel(1, troa);
        setWheel(2, troa);
        f.add(troa);
        JButton b = new JButton(new AbstractAction("Play!") {

            public void actionPerformed(ActionEvent e) {
                Animator a1 = setAnimator(0);
                Animator a2 = setAnimator(1);
                Animator a3 = setAnimator(2);
                
                for (Result r:results) {
                    if (r != null) {
                        r.setImage(null);
                    }
                }

                a1.start();
                a2.start();
                a3.start();
            }

            private Animator setAnimator(final int idx) {
                Animator a1 = new Animator((int) (Math.random() * 10000));
                a1.setResolution(5);
                a1.addTarget(new PropertySetter(wheels[idx], "shift", wheels[idx].getShift(), Math.random() * 8
                        * Math.PI));
                a1.addTarget(new TimingTargetAdapter() {
                    @Override
                    public void end() {
                        double shift = wheels[idx].getShift();
                        double dif = 1000;
                        for (int m = -wheels[idx].getTickCount(); m < wheels[idx].getTickCount(); m++) {
                            // TODO: should paint from top to middle and from bottom to middle to ensure middle one is
                            // on top of others
                            double tau = (Math.PI / 2 - (m + .5) * Math.PI / wheels[idx].getTickCount());
                            if (dif < Math.abs(tau - shift)) {
                                Animator a = new Animator(500);
                                a.addTarget(new PropertySetter(wheels[idx], "shift", shift, Math.PI / 2 - (m - 1 + .5)
                                        * Math.PI / wheels[idx].getTickCount()));
                                a.setDeceleration(.5f);
                                a.addTarget(new TimingTargetAdapter() {
                                    @Override
                                    public void end() {
                                        results[idx].setImage(wheels[idx].getScreenshot(new Rectangle(0, wheels[idx]
                                                .getHeight() / 2 - 6, wheels[idx].getWidth(), results[idx].getHeight())));
                                    }
                                });
                                a.start();
                                break;
                            }
                            dif = Math.abs(tau - shift);
                        }
                        super.end();
                    }
                });
                a1.setEndBehavior(EndBehavior.HOLD);
                a1.setInterpolator(new SplineInterpolator(0, 0.2f, 0, 1));
                return a1;
            }
        });

        f.add(createMenu(), BorderLayout.NORTH);
        f.add(b, BorderLayout.SOUTH);
        f.setPreferredSize(new Dimension(250, 700));

        PropertyChangeListener pcl = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("image".equals(evt.getPropertyName())) {
                    if (compare(results[0].getImage(), results[1].getImage(), results[2].getImage())) {
                        flash();
                    }
                }
            }
        };
        for (Result r : results) {
            r.addPropertyChangeListener(pcl);
        }
        f.pack();
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        f.setLocation(p.x - f.getWidth()/2, p.y - f.getHeight()/2);
        f.setVisible(true);
    }

    private static Component createMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu help = new JMenu("Help");
        bar.add(help);
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog();
                d.add(new JLabel("<html><center>Simple time waster by Jan Haderka"), BorderLayout.CENTER);
                d.add(new JLabel("<html><center>Icons courtesy of http://www.turbomilk.com"), BorderLayout.SOUTH);
                d.setPreferredSize(new Dimension(200,200));
                d.pack();
                d.setVisible(true);
            }});
        
        help.add(about);
        return bar;
    }

    private static boolean compare(BufferedImage i1, BufferedImage i2, BufferedImage i3) {
        if (i1 == null || i2 == null || i3 == null) {
            return false;
        }
        Raster r1 = i1.getData();
        Raster r2 = i2.getData();
        Raster r3 = i3.getData();
        int[] a1 = new int[3];
        int[] a2 = new int[3];
        int[] a3 = new int[3];
        for (int x = 0; x < i1.getWidth(); x++) {
            for (int y = 0; y < i1.getHeight(); y++) {
                r1.getPixel(x, y, a1);
                r2.getPixel(x, y, a2);
                r3.getPixel(x, y, a3);
                for (int i = 0; i < 3; i++) {
                    if (a1[i] != a2[i] || a2[i] != a3[i]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void flash() {
        Animator a = new Animator(3000);
        for (int i = 0; i < results.length; i++) {
            a.addTarget(new PropertySetter(results[i], "alpha", 1, 0,1,0,1,0,1,0,1,0));
        }
        a.start();
    }

    private static JXWheel setWheel(int idx, JPanel troa) {
        JPanel p = new JPanel(new BorderLayout());
        final JXWheel wheel = new JXWheel(loadImages(), 8);
        p.add(wheel);
        Result res = new Result();
        p.add(res, BorderLayout.SOUTH);
        troa.add(p);
        wheels[idx] = wheel;
        results[idx] = res;
        return wheel;
    }

    private static BufferedImage[] loadImages() {
        try {
            return new BufferedImage[] {
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/desktop.png")),
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/document.png")),
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/trash-empty.png")),
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/folder.png")),
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/search.png")),
                    ImageIO.read(WheelMachineDemo.class.getResourceAsStream("/milkicons/trash-full.png")) };
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
