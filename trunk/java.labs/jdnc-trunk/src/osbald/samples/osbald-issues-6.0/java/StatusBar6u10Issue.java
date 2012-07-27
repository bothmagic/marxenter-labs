import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.*;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXStatusBar;
import org.jdesktop.swingx.JXTable;

/* JXStatusBar broken under jdk16u10.. is it me or does it look a little squashed when it does appear? */

public class StatusBar6u10Issue {
    protected JXFrame createFrame() {
        JXTable table = new JXTable(20,10);
        JXFrame frame = new JXFrame("add statusbar to frame - jdk16u10");
        frame.add(new JScrollPane(table));
        final JLabel label = new JLabel("just to see ... lonnnnnnnger and longer ........... ");
        JXStatusBar statusBar = new JXStatusBar();
        statusBar.add(label);
        frame.setStatusBar(statusBar);
        label.setForeground(Color.RED);
        label.setOpaque(true);
        label.setBackground(Color.GREEN);
        label.setBorder(BorderFactory.createLineBorder(Color.RED));
        label.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                LOG.info("got it from label .." + label.getText());
            }

        });
        statusBar.setBorder(BorderFactory.createLineBorder(Color.RED));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.BLUE);
        statusBar.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                LOG.info("got it from statusBar ..");
            }

        });
        frame.pack();
        return frame;
    }

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(StatusBar6u10Issue.class.getName());

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new StatusBar6u10Issue().createFrame().setVisible(true);
            }
        });
    }
}