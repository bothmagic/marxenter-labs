package org.jdesktop.swingx.demo;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * RichTextEditor
 * @author Ryan Cuprak
 */
public class RichTextEditor extends JFrame {

    /**
     * Text Pane
     */
    private JTextPane textPane;

    public RichTextEditor() {
        super("Text Pane");

        JScrollPane pane = new JScrollPane(textPane);


        pack();
        setVisible(true);
    }

    public static void main(String args[]) {
        new RichTextEditor();
    }
}
