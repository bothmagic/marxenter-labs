/*
 * Created on 01.09.2009
 *
 */
package kschaefe.xbutton;


import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.test.XTestUtils;

public class Button2VisualCheck extends InteractiveTestCase {

    public static void main(String[] args) {
        Button2VisualCheck test = new Button2VisualCheck();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveButton2PaintingLoop() {
        JXButton b = new JXButton(XTestUtils.loadDefaultIcon());
        JXFrame frame = showInFrame(b, "loop?");
        addStatusMessage(frame, "xbutton stresses cpu - xbutton2?");
        show(frame);
    }
    
    public void interactiveButtonPaintingLoop() {
        JXButton b = new JXButton(XTestUtils.loadDefaultIcon());
        JXFrame frame = showInFrame(b, "loop?");
        addStatusMessage(frame, "xbutton stresses cpu?");
        show(frame);
    }
}
