package org.jdesktop.swingx;

import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JSlider;
import org.jdesktop.swingx.slider.*;

/**
 * Generated JavaDoc Comment.
 *
 * @author <a href="mailto:matt.nathan@paphotos.com">Matt Nathan</a>
 */
public class JXSliderDemo {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXSliderDemo().createAndShowGUI(args);
            }
        });
    }





    private void createAndShowGUI(String[] args) {
        final JXSlider slider = new JXSlider(0, 5000, 50);
        slider.setProjection(new ValueFrameProjection(1000, 150, 150, false));
        BagMarkerGroup vals = new BagMarkerGroup();
        vals.addValue(275);
        vals.addValue(1925);
        vals.addValue(2955);
        ((DefaultSliderModel) slider.getModel()).addMarkerGroup(vals);

        MarkerGroup g = new RepeatingMarkerGroup(100, 50);
        ((DefaultSliderModel) slider.getModel()).addMarkerGroup(g);
//        slider.setOrientation(Orientation.VERTICAL);

//        slider.setBorder(BorderFactory.createRaisedBevelBorder());

//        slider.setFocusable(true);

        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setOpaque(false);

//        JPanel cent = new JPanel(new GridBagLayout());
//        cent.setOpaque(false);
//        cent.add(slider);
//        p.add(cent, BorderLayout.CENTER);
        p.add(slider, BorderLayout.CENTER);
//        p.add(new JSlider(), BorderLayout.CENTER);

        p.add(new JSlider(), BorderLayout.NORTH);
        p.add(new JSlider(), BorderLayout.SOUTH);
        JSlider s = new JSlider();
        s.setOrientation(JSlider.VERTICAL);
        p.add(s, BorderLayout.EAST);
        s = new JSlider();
        s.setEnabled(false);
        s.setOrientation(JSlider.VERTICAL);
        p.add(s, BorderLayout.WEST);

//        Animator a = new Animator(1600, Animator.INFINITE, Animator.RepeatBehavior.REVERSE,
//                                  new PropertySetter(slider.getMarkerGroup(0), "value", 25L, 75L));
//        a.setAcceleration(0.4f);
//        a.setDeceleration(0.4f);
//        a.setResolution(30);
//        a.start();

        new TestFrame("JXSlider Test", p).setVisible(true);
    }

}
