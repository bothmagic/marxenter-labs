package org.jdesktop.swingx;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jdesktop.swingx.painter.MattePainter;

public class JXLabelWrapTest extends JFrame {

    private JXLabel lbl;
    private String text = "Some <i>realy long and nasty</i> <u>text goes in here</u> to see whether multiple "
        + "line wrapping works or not. Here follows a tab char:\tI would really hate to have to do it manually. "
        + "This should be enough. Have a fun.";

    public static void main(String[] args) {
        JXLabelWrapTest t = new JXLabelWrapTest();
        t.setDefaultCloseOperation(EXIT_ON_CLOSE);
        t.setPreferredSize(new Dimension(300, 500));
        t.pack();
        t.setVisible(true);
    }

    public JXLabelWrapTest() {
        lbl = new JXLabel(text);
//        try {
//            lbl.setIcon(new javax.swing.ImageIcon(javax.imageio.ImageIO.read(this.getClass().getResource("attitude.gif"))));
//        } catch (java.io.IOException e1) {
//            e1.printStackTrace();
//        }
        lbl.setLineWrap(true);
        lbl.setVerticalAlignment(JXLabel.TOP);
        MattePainter matte = new MattePainter(Color.WHITE);
        matte.setPaintStretched(true);
        lbl.setBackgroundPainter(matte);
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("tahoma", Font.BOLD, 16));
        add(lbl);

        JPanel controlPane = new JPanel();
        add(controlPane, BorderLayout.NORTH);
        JButton btn = new JButton(new AbstractAction("Change text") {
            public void actionPerformed(ActionEvent e) {
                String s = lbl.getText();
                boolean b = s.startsWith("<html>"); 
                StringBuffer buf = new StringBuffer(b ? s.substring(6, s.length() - 7) : s);
                s = buf.reverse().toString();
                lbl.setText(b ? "<html>" + s + "</html>" : s);
                buf.setLength(0);
            }});
        controlPane.add(btn);
        btn = new JButton(new AbstractAction("toggle html") {
            public void actionPerformed(ActionEvent e) {
                lbl.setText(lbl.getText().length() == text.length() ? "<html>" + lbl.getText() + "</html>" : lbl
                        .getText().substring(6, lbl.getText().length() - 7));
            }});
        controlPane.add(btn);

        controlPane = new JPanel();
        add(controlPane, BorderLayout.SOUTH);
        btn = new JButton(new AbstractAction("toggle multiLine") {
            public void actionPerformed(ActionEvent e) {
                lbl.setLineWrap(!lbl.isLineWrap());
            }});
        controlPane.add(btn);

}

}
