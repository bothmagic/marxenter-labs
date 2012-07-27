import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXStatusFrame;

/*
 * StatusFrameDemo.java
 *
 * Created on August 19, 2005, 2:13 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author patrick
 */
public class StatusFrameDemo extends JXStatusFrame {
    
    /**
     * Creates a new instance of StatusFrameDemo 
     */
    public StatusFrameDemo() {
        super("Status Bar Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        
        add(new StatusFrameControl(this), BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new StatusFrameDemo().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void run() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
