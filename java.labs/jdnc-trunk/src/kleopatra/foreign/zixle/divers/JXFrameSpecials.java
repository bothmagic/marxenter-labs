/*
 * Created on 26.06.2006
 *
 */
package zixle.divers;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.action.AbstractActionExt;

public class JXFrameSpecials {

    private JXFrame frame;

    private JXFrame getXFrame() {
        if (frame == null) {
            frame = buildFrame();
        }
        return frame;
    }
    
    private JXFrame buildFrame() {
        final JXFrame frame = new JXFrame("Show Scott's HeapView in SwingX",
                true);

        final JXStatusBar bar = new JXStatusBar();
        JLabel statusLabel = new JLabel("Ready");
        bar.add(statusLabel); // weight of 0.0 and no insets
        JProgressBar pbar = new JProgressBar();
        bar.add(pbar); // weight of 0.0 and no insets

        HeapView heapView = new HeapView();
        bar.add(heapView);
        frame.getRootPaneExt().setStatusBar(bar);
        Action action = new AbstractActionExt("toggle CO") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame
                            .applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame
                            .applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }
                bar.revalidate();
                bar.repaint();
            }

        };
        frame.add(new JButton(action));
        return frame;
    }

    public static void main(String[] args) throws Exception {
        JXFrame frame = new JXFrameSpecials().getXFrame();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    

}
