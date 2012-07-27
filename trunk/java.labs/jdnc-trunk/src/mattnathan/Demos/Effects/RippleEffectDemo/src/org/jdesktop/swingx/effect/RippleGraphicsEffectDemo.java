package org.jdesktop.swingx.effect;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.swingx.TestFrame;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Generated JavaDoc Comment.
 *
 * @author <a href="mailto:matt.nathan@paphotos.com">Matt Nathan</a>
 */
public class RippleGraphicsEffectDemo extends AbstractGraphicsEffectDemo<RippleGraphicsEffect<Object>> {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainOnEDT(args);
            }
        });
    }


    private static void mainOnEDT(String[] args) {
        new TestFrame("Ripple Graphics Effect Demo", new RippleGraphicsEffectDemo()).setVisible(true);
    }


    @Override
    protected RippleGraphicsEffect<Object> createDefaultSource() {
        RippleGraphicsEffect<Object> ripple = new RippleGraphicsEffect<Object>();
        Animator a = PropertySetter.createAnimator(1000, ripple, "phase", 0f, 1f);
        a.addTarget(new TimingTargetAdapter() {
            @Override
            public void timingEvent(float v) {
                repaint();
            }
        });
        a.setRepeatBehavior(Animator.RepeatBehavior.LOOP);
        a.setRepeatCount(Animator.INFINITE);
        a.start();
        return ripple;
    }

    @Override
    protected JComponent createControls() {
        JSlider falloff = new JSlider(0, 100, (int) getSource().getFalloff());
        falloff.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setFalloff(((JSlider) e.getSource()).getValue());
            }
        });
        JSlider waveHeight = new JSlider(0, 100, (int) getSource().getWaveHeight());
        waveHeight.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setWaveHeight(((JSlider) e.getSource()).getValue());
            }
        });

        JComponent result = Box.createVerticalBox();
        result.add(falloff);
        result.add(waveHeight);
        return result;
    }
}
