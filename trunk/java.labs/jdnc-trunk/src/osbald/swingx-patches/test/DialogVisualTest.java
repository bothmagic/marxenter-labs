
import org.jdesktop.swingx.JXDialog;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.incubator.JXDialog2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DialogVisualTest {
    public static void main(String[] args) {
        AbstractAction okAction = new AbstractAction("OK") {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        JComponent content = new JXLabel("Blah Blah Blah");
        content.setName("org.jdesktop.swingx.JXDialog");
        content.setBorder(new LineBorder(Color.RED, 1));
        content.getActionMap().put(JXDialog.EXECUTE_ACTION_COMMAND, okAction);
        JDialog jxDialog = new org.jdesktop.swingx.JXDialog((Frame) null, content);
        jxDialog.pack();
        jxDialog.setVisible(true);

        JComponent content1 = new JXLabel("Blah Blah Blah");
        content1.setName("org.jdesktop.incubator.JXDialog");
        content1.setBorder(new LineBorder(Color.RED, 1));
        content1.getActionMap().put(JXDialog.EXECUTE_ACTION_COMMAND, okAction);
        JDialog jxDialog1 = new org.jdesktop.incubator.JXDialog((Frame)null, content1);
        jxDialog1.setVisible(true);

        JComponent content2 = new JXLabel("Blah Blah Blah");
        content2.setName("org.jdesktop.incubator.JXDialog2");
        content2.setBorder(new LineBorder(Color.RED, 1));
        content2.getActionMap().put(JXDialog.EXECUTE_ACTION_COMMAND, okAction);
        JDialog jxDialog2 = new org.jdesktop.incubator.JXDialog2((Frame) null, content2, "JXDialog2");
        jxDialog2.pack();
        jxDialog2.setVisible(true);
    }
}
