package notanissue;

import org.jdesktop.swingx.JXDialog;

import javax.swing.*;

public class JXDialogPrematureClosingIssue {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXDialogPrematureClosingIssue();
            }
        });
    }

    public JXDialogPrematureClosingIssue() {
        final JFrame frame = new JFrame("Close Me.");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JPanel content = new JPanel();
        // I've reused component name in some of my code too :- so JSR296 really bites
        content.setName("Why does [enter] in any of the textfields close the dialog?");
        for (int i = 0; i < 3; i++) {
            content.add(new JTextField(10));
        }
        JXDialog dialog = new JXDialog(frame, content);
        dialog.pack();
        dialog.setVisible(true);
    }
}
