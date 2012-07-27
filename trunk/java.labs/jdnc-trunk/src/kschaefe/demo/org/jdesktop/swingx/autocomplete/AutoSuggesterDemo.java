/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author Karl George Schaefer
 *
 */
public class AutoSuggesterDemo extends JFrame {
    protected void frameInit() {
        super.frameInit();
        
        setTitle("AutoSuggest Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JTextField text = new JTextField();
        List<String> strings = Arrays.asList("A", "b", "c", "d");
        AutoSuggestDecorator.decorate(text, strings, false);
        add(text);
        add(new JButton("Nothing"), BorderLayout.SOUTH);
        
        pack();
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AutoSuggesterDemo().setVisible(true);
            }
        });
    }

}
