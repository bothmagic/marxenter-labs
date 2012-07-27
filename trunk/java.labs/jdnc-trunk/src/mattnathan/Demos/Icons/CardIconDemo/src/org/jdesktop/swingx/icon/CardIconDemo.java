package org.jdesktop.swingx.icon;

import java.awt.Color;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.util.*;

public class CardIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        CardIcon icon = new CardIcon(
              new TempIcon(64, 64, null, Color.RED),
              new TempIcon(64, 64, null, Color.BLUE));
        icon.setFrame(0.3f);
//        icon.setScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.NONE));
        icon.setChildScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.NONE));
        new TestFrame("CardIconDemo Test", new CardIconDemo(icon)).setVisible(true);
    }





    public CardIconDemo(ScalableIcon icon) {
        super(icon);
//        setScalePolicy(null);
    }
}
