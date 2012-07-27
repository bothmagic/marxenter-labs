package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class JXMenuBarTest2 extends JFrame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JXMenuBarTest2 t = new JXMenuBarTest2();
        t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.pack();
        t.setVisible(true);
    }
    
    public JXMenuBarTest2() {
        final JMenuBar bar = new JMenuBar();
        bar.add(new JMenu("File"));
        bar.add(new JMenu("Edit"));
        bar.add(new JMenu("Etc."));
        JPanel p = new JPanel(new BorderLayout());
        p.add(bar);
        p.add(new JXBusyLabel(new Dimension(23,23)), BorderLayout.EAST);
        this.add(p, BorderLayout.NORTH);
        JButton btn = new JButton(new AbstractAction("Click me") {

            public void actionPerformed(ActionEvent e) {
//                bar.getBusyIcon().setBusy(!bar.getBusyIcon().isBusy());
            }});
        add(btn, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(200,200));
    }

}
