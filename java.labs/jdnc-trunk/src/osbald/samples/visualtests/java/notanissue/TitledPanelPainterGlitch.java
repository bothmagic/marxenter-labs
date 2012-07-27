package notanissue;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.action.BoundAction;

import javax.swing.*;
import java.awt.*;

/**@deprecated painting flaw for titlepanes decorator components was fixed
 *
 */
public class TitledPanelPainterGlitch {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        Class.forName("org.jdesktop.swingx.JXHyperlink");

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TitledPanelPainterGlitch();
            }
        });
    }

    public TitledPanelPainterGlitch() {
        JFrame frame = new JFrame("JXTitledPanel Painter and JXHyperLink not playing nice");
        JXPanel content = new JXPanel(new GridLayout(3, 1));
        content.setPreferredSize(new Dimension(200, 200));
        content.setBackgroundPainter(new MattePainter(ColorUtil.setAlpha(Color.GRAY, 50), true));
        //content.setBackgroundPainter(new MattePainter(ColorUtil.getCheckerPaint(ColorUtil.setAlpha(Color.GRAY, 60), ColorUtil.setAlpha(Color.WHITE, 60), 16), true));
        content.add(new JXHyperlink(new BoundAction("Choice 1")));
        content.add(new JXHyperlink(new BoundAction("Choice 2")));
        content.add(new JXLabel("Choice 3"));

        JXTitledPanel titledPanel = new JXTitledPanel("Title", content);
        titledPanel.setTitlePainter(new MattePainter(getPaint(), true));

        JCheckBox component = new JCheckBox("Check Me");
        component.setOpaque(false);
        component.setFocusPainted(false);
        titledPanel.setRightDecoration(component);

        frame.getContentPane().add(titledPanel);
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    Paint getPaint() {
        Color start = SystemColor.activeCaption;
        Color end = new Color(Math.min(start.getRed() + 30, 255),
                Math.min(start.getGreen() + 30, 255),
                Math.min(start.getBlue() + 30, 255), 100);
        return new GradientPaint(100, 0, start, 150, 0, end);
    }
}
