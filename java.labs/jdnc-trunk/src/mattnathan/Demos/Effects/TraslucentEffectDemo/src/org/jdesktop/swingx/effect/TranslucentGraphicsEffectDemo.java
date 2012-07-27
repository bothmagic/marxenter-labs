package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.TestFrame;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Generated JavaDoc Comment.
 *
 * @author <a href="matt.nathan@paphotos.com">Matt Nathan</a>
 */
public class TranslucentGraphicsEffectDemo extends AbstractGraphicsEffectDemo<TranslucentGraphicsEffect<Object>> {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("TranslucentGraphicsEffectDemo Test", new TranslucentGraphicsEffectDemo()).setVisible(true);
    }





    protected TranslucentGraphicsEffect<Object> createDefaultSource() {
        return new TranslucentGraphicsEffect<Object>(0.5f);
    }





    protected JComponent createControls() {
        JSlider slider = new JSlider(0, 100, (int) (getSource().getAlpha() * 100));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                getSource().setAlpha(((JSlider) e.getSource()).getValue() / 100f);
            }
        });
        return slider;
    }
}
