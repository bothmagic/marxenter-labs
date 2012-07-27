package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class JXMenuBarTest extends JFrame {

    /**
     * @param args
     */
    public static void main(String[] args) {
        JXMenuBarTest t = new JXMenuBarTest();
        t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.pack();
        t.setVisible(true);
    }
    
    Icon i = null;

    public JXMenuBarTest() {
        final JXMenuBar bar = new JXMenuBar();
        bar.add(new JMenu("File"));
        bar.add(new JMenu("Edit"));
        bar.add(new JMenu("Etc."));
        //bar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.add(bar, BorderLayout.NORTH);
        JPanel ctrl = new JPanel(new GridLayout(1,2));
        add(ctrl, BorderLayout.SOUTH);
        JButton btn = new JButton(new AbstractAction("Start/Stop") {
            public void actionPerformed(ActionEvent e) {
                if (i == null) {
                    i = bar.getBusyIcon().getIcon();
                    bar.getBusyIcon().setIcon(null);
                } else {
                    bar.getBusyIcon().setIcon(i);
                    i = null;
                }
                bar.getBusyIcon().setBusy(!bar.getBusyIcon().isBusy());
            }});
        ctrl.add(btn);
        btn = new JButton(new AbstractAction("R2L/L2R") {

            public void actionPerformed(ActionEvent e) {
                if (bar.getComponentOrientation().equals(ComponentOrientation.RIGHT_TO_LEFT)) {
                    bar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                } else { //L2R or UNK
                    bar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                }
                bar.revalidate();
                
            }});
        ctrl.add(btn);
        setPreferredSize(new Dimension(200,200));
        bar.getBusyIcon().setBusy(true);
    }

}
