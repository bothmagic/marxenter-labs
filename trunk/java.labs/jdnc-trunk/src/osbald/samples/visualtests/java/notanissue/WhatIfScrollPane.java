package notanissue;

import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/*
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 15-Jun-2007
 * Time: 16:17:49
 */

public class WhatIfScrollPane {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WhatIfScrollPane();
            }
        });
    }

    public WhatIfScrollPane() {
        createFrame().setVisible(true);
        createWonkyFrame().setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("What If..");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new JLabel("Squish this ScrollPane - Watch for Scrollbars"), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane();
        content.add(scrollPane);

        JPanel panel = new JPanel(new VerticalLayout(5));
        panel.add(new JList(new String[]{"One", "Two", "Three"}));
        JLabel label = new JLabel("More to follow..");
        label.setOpaque(true);
        panel.add(label);
        panel.add(new JList(new String[]{"Four", "Five", "Six"}));
        panel.setOpaque(false);

        JViewport viewport = scrollPane.getViewport();
        viewport.setBackground(Color.WHITE);
        viewport.add(panel);

        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.pack();
        return frame;
    }

    Frame createWonkyFrame() {
        final JFrame frame = new JFrame("What If.. don't even think it!");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        content.setLayout(new BorderLayout());
        content.add(new JLabel("Squish this ScrollPane - Watch for Scrollbars"), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane();
        content.add(scrollPane);

        JViewport viewport = new GreedyViewport();
        viewport.setLayout(new VerticalLayout(5));
        viewport.add(new JList(new String[]{"One", "Two", "Three"}));
        JLabel label = new JLabel("More to follow..");
        label.setOpaque(true);
        viewport.add(label);
        viewport.add(new JList(new String[]{"Four", "Five", "Six"}));
        viewport.setBackground(Color.WHITE);
        scrollPane.setViewport(viewport);

        content.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.pack();
        return frame;
    }

    class GreedyViewport extends JViewport {
        private boolean dontRemove;

        public void setView(Component view) {
            try {
                dontRemove = true;
                super.setView(view);
            } finally {
                dontRemove = false;
            }
        }

        public void remove(Component child) {
            if (!dontRemove) {
                super.remove(child);
            }
        }
    }
}
