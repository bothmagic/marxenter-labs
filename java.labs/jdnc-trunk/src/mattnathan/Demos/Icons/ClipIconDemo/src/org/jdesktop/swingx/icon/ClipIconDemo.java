package org.jdesktop.swingx.icon;

import java.awt.geom.Rectangle2D;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.TestFrame;
import org.jdesktop.swingx.util.ScalePolicy;

public class ClipIconDemo extends AbstractScalableIconTest {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }





    private static void createAndShowGUI(String[] args) {
        ClipIcon clipIcon = new ClipIcon(new TempIcon(), new Rectangle2D.Float(16, 16, 16, 16));
        clipIcon.setSizeClipped(true);
        clipIcon.setChildScalePolicy(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.BOTH, ScalePolicy.ResizePolicy.BEST_FIT));
        new TestFrame("ClipIconDemo Test", new ClipIconDemo(clipIcon)).setVisible(true);
    }





    public ClipIconDemo(ScalableIcon icon) {
        super(icon);
//        setScalePolicy(null);
    }
}
