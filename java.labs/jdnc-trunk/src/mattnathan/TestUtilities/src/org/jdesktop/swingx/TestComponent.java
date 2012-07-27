package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

/**
 * Simple test component with a nnumber of different components in it.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class TestComponent extends JXComponent {
    public TestComponent() {
        updateUI();
        init();
    }





    private void init() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(false);

        String[] items = new String[10];
        for (int i = 0; i < items.length; i++) {
            items[i] = "Item " + (i + 1);
        }
        JList list = new JList(items);
        JScrollPane listSP = new JScrollPane(list);
        listSP.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        JProgressBar progBar = new JProgressBar();
        progBar.setIndeterminate(true);

        JButton but = new JButton("Button");
        but.setOpaque(false);

        JLabel label = new JLabel("Label");

        JTextField field = new JTextField("TextField");
        field.setMaximumSize(new Dimension(Short.MAX_VALUE, field.getPreferredSize().height));

        JComponent cont = Box.createVerticalBox();
        cont.setOpaque(false);
        cont.setBorder(BorderFactory.createTitledBorder("Controls"));
        cont.add(label);
        cont.add(Box.createVerticalStrut(8));
        cont.add(but);
        cont.add(Box.createVerticalStrut(8));
        cont.add(field);
        cont.add(Box.createVerticalGlue());
        cont.add(progBar);


        add(listSP, BorderLayout.CENTER);
        add(cont, BorderLayout.EAST);
    }


}
