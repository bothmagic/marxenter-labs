package org.jdesktop.swingx.effect;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.*;

public class IdentityGraphicsEffectDemo extends AbstractGraphicsEffectDemo {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        new TestFrame("IdentityGraphicsEffectDemo Test", new IdentityGraphicsEffectDemo()).setVisible(true);
    }





    public IdentityGraphicsEffectDemo() {
        super();
    }





    @Override
    protected JComponent createControls() {
        return null;
    }





    @Override
    protected GraphicsEffect createDefaultSource() {
        return IdentityGraphicsEffect.INSTANCE;
    }
}
