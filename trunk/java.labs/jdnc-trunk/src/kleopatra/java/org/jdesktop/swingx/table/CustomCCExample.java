package org.jdesktop.swingx.table;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * Quick driver for alternative ColumnControlButtons.
 * 
 * @author Jeanette Winzenburg
 */
public class CustomCCExample {


    public static void main(String[] args) {
        setUpLookAndFeel();
        final JXFrame frame = new JXFrame(
                "JXTable :: Advanced Customization",
                true);
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Two List Dialog", new DialogCCExample().getContent());
        pane.addTab("Single List Dialog", new SingleListCCExample().getContent());
        frame.add(pane);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setSize(800, 600);
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }
        });        
    }


    private static void setUpLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }

}
