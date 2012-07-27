import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;

public class TabPaintingXPLAFIssue {

    public static void main(String[] args) throws Exception {
        // hardcoded because I'm spefically talking about Windows XP LAF here!
        UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
        //NB would also work.. (dirty boy) UIManager.put("TabbedPane.tabAreaInsets", new Insets(12, 5, 5, 5));
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TabPaintingXPLAFIssue();
            }
        });
    }

    public TabPaintingXPLAFIssue() {
        JFrame frame = new JFrame("Shows unwanted edge artefacts on tabs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // add a menu & toolbar.. with all three (tabbedpane background) set to SystemColor.control they tend to 'blend' together
        JMenuBar menubar = new JMenuBar();
        JMenu menu = menubar.add(new JMenu("File"));
        frame.setJMenuBar(menubar);

        JToolBar toolbar = new JToolBar();
        //toolbar.setFloatable(false);
        //toolbar.addSeparator();
        toolbar.add(new JButton("X"));
        toolbar.add(new JButton("Y"));
        toolbar.add(new JButton("Z"));

        JComponent content = (JComponent) frame.getContentPane();
        content.add(toolbar, BorderLayout.PAGE_START);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("One", new JButton("One"));
        tabbedPane.addTab("Two", new JButton("Two"));
        tabbedPane.addTab("Three", new JButton("Three"));
        content.add(tabbedPane);

        // add a gap between toolbar and tabbedpane, both for visual & spacial seperation
        // ..caters for both clumsy & stupid users (but then aren't they all ;)
        tabbedPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 0, 0), tabbedPane.getBorder()));
        content.setBackground(Color.GRAY); // enhance the separation

        frame.pack();
        frame.setSize(new Dimension(640, 480));
        frame.setVisible(true);
    }
}