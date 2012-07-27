package org.jdesktop.swingx.icon;

import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;

public class TempIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        TempIcon icon = new TempIcon();
        new TestFrame("TempIconDemo Test", new TempIconDemo(icon)).setVisible(true);
    }





    public TempIconDemo(ScalableIcon icon) {
        super(icon);
    }
}