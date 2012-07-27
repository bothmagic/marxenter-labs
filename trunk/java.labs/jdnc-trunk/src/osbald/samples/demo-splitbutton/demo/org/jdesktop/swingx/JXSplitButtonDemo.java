package org.jdesktop.swingx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class JXSplitButtonDemo {

    public static void main(String[] args) throws Exception {
        if (System.getProperty("swing.defaultlaf") == null)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXSplitButtonDemo();
            }
        });
    }

    public JXSplitButtonDemo() {
        JXFrame frame = new JXFrame("JXSplitButtonDemo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent pane = (JComponent) frame.getContentPane();
        pane.setLayout(new FlowLayout());
        pane.add(new JXSplitButton("JXSplitButton", null, createMenu()));
        pane.add(new JXButton("JXButton"));
        pane.add(new JButton("JButton"));
        pane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame.pack();
        frame.setVisible(true);
    }

    JPopupMenu createMenu() {
        class TellTaleAction extends AbstractAction {
            TellTaleAction(String name) {
                super(name);
            }

            public void actionPerformed(ActionEvent event) {
                System.out.println("clicked :- " + this.getValue(Action.NAME));
            }
        }
        JPopupMenu popupMenu = new JPopupMenu("Other stuff");
        popupMenu.add(new JMenuItem(new TellTaleAction("Option 1")));
        popupMenu.add(new JMenuItem(new TellTaleAction("Option 2")));
        popupMenu.add(new JMenuItem(new TellTaleAction("Option 3")));
        return popupMenu;
    }
}
