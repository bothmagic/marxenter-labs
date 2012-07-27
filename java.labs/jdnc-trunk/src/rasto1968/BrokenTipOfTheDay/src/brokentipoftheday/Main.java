package brokentipoftheday;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXTipOfTheDay;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run()
            {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    // Ignore
                }
                
                final JFrame frame=new JFrame("Broken Tip Of The Day");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                final JXTipOfTheDay tod=new JXTipOfTheDay();
                tod.showDialog(frame);
            }            
        });
    }

}
