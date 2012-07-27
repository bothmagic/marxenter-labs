package org.jdesktop.swingx;

import javax.swing.SwingUtilities;

/**
 * Simple demo to showcase the new JXTitledSeparator component
 *
 * @author <a href="mailto:matt.nathan@paphotos.com">Matt Nathan</a>
 */
public class JXTitledSeparatorDemo {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXTitledSeparatorDemo().createAndShowGUI(args);
            }
        });
    }





    private void createAndShowGUI(String[] args) {
        JXTitledSeparator2 s = new JXTitledSeparator2("Title");
//        s.setEnabled(false);
        new TestFrame("JXTitledSeparator2 Test", s).setVisible(true);
    }

}
