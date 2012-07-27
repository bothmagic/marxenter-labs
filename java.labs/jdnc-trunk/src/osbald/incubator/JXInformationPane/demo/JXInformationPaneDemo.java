import org.jdesktop.incubator.JXInformationPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 10-Jul-2007
 * Time: 13:23:15
 */

public class JXInformationPaneDemo {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JXInformationPaneDemo();
            }
        });
    }

    public JXInformationPaneDemo() {
        final JFrame frame = new JFrame("Sample of JXInformationPane in action.");
        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JXInformationPane informationPane = new JXInformationPane();
        informationPane.setText("Hello World! Hello World! Hello World! Hello World! \nHello World! Hello World! Hello World! \nHello World! Hello World! \nHello World! ");
        frame.getContentPane().add(informationPane);

        Action changeMessageAction = new AbstractAction("Click Me!") {
            int count = 0;
            Color defaultColor = informationPane.getBackground();

            public void actionPerformed(ActionEvent e) {
                switch (count++ % 4) {
                    case 1:
                        informationPane.setText(JXInformationPane.Level.ERROR, "It's all gone a bit Pete Tong! ");
                        informationPane.setBackground(new Color(255, 225, 225));
                        break;
                    case 0:
                        informationPane.setText(JXInformationPane.Level.WARNING, "Danger! Danger! Danager!");
                        //informationPane.setBorder(BorderFactory.createLineBorder(new Color(225, 215, 180), 1));
                        break;
                    case 2:
                        StringBuilder sb = new StringBuilder("<html><h1>A New Novel</h1><p>");
                        for (int i = 0; i < 20; i++) sb.append("All work and no play makes Richard a dull boy. ");
                        sb.append("</p><p>PS. <u>redruM!</u></p>");
                        informationPane.setText(sb.toString());
                        informationPane.setBackground(defaultColor);
                        break;
                    default:
                        informationPane.setText(null, "Nowt!");
                }
            }
        };
        JButton button = new JButton(changeMessageAction);
        contentPane.add(button, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack(); // sizing?
        frame.setVisible(true);
    }
}
