import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.MattePainter;

import javax.swing.*;
import java.awt.*;

/*
* Tries buttons under a number of toolbars while looking for consistency
* (or lack of) across the bundled LAFs ..look for command line options in main().
*
* Also note togglebutton changes in 6.0 that make its state unclear
* (while owning focus in a toolbar).
 *
 * Created by IntelliJ IDEA.
 * User: richard osbaldeston
 * Date: 18-Sep-2007
 * Time: 16:07:26
 */

public class ButtonAndBarsIssues extends JPanel {

    public ButtonAndBarsIssues() {
        //setBackground(SystemColor.window);
        setBackground(Color.YELLOW);

        JButton jb = new JButton("JButton");
        jb.setOpaque(false);
        add(jb);
        JButton jb2 = new JButton("JButton B=null");
        jb2.setBorder(null);
        add(jb2);
        JButton jb3 = new JButton("JButton BP=false");
        jb3.setBorderPainted(false);
        add(jb3);

        JButton jxb = new JXButton("JXButton");
        jxb.setOpaque(false);
        add(jxb);
        JButton jxb2 = new JXButton("JXButton B=null");
        jxb2.setBorder(null);
        add(jxb2);
        JButton jxb3 = new JXButton("JXButton BP=false");
        jxb3.setBorderPainted(false);
        add(jxb3);

        JXButton jxb4 = new JXButton("JXButton BPt=null");
        jxb4.setBackgroundPainter(null);
        jxb4.setOpaque(false);
        add(jxb4);
        JXButton jxb5 = new JXButton("JXButton BPt=null BP=false");
        jxb5.setBackgroundPainter(null);
        jxb5.setBorderPainted(false);
        jxb5.setOpaque(false);
        add(jxb5);
        JXButton jxb6 = new JXButton("JXButton Bpt=Alpha");
        jxb6.setBackgroundPainter(new MattePainter(ColorUtil.setAlpha(Color.BLACK, 128)));
        jxb6.setOpaque(false);
        add(jxb6);

        JPanel p2 = new JPanel(new VerticalLayout(8));
        p2.setOpaque(false);
        p2.add(new JLabel("JToolBar rollover=true"));
        JToolBar tb = createToolbar();
        tb.setRollover(true);
        p2.add(tb);
        p2.add(new JLabel("JToolBar rollover=false"));
        JToolBar tb2 = createToolbar();
        tb2.setRollover(false);
        p2.add(tb2);
        add(p2);
    }

    JToolBar createToolbar() {
        JToolBar tb = new JToolBar();
        tb.setOpaque(false);
        tb.add(new JButton("JButton"));
        tb.addSeparator();
        JButton button = new JButton("JButton (disbaled)");
        button.setEnabled(false);
        tb.add(button);
        tb.addSeparator();
        JXButton jxb = new JXButton("JXButton");
        //hmmm jxb.setEnabled(false);
        tb.add(jxb);
        tb.addSeparator();
        tb.add(new JToggleButton("JToggleButton"));
        return tb;
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            if ("-w".equals(args[0])) {
                UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
            } else if ("-o".equals(args[0])) {
                UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            } else if ("-m".equals(args[0])) {
                UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
            } else if ("-c".equals(args[0])) {
                UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel() {
                    public String getName() {
                        return "WindowsClassic";
                    }
                });
            } else if ("-b".equals(args[0])) {
                UIManager.setLookAndFeel(new javax.swing.plaf.basic.BasicLookAndFeel() {
                    public String getName() {
                        return "BasicLookAndFeel";
                    }

                    public String getID() {
                        return getName();
                    }

                    public String getDescription() {
                        return "Shouldn't be seen";
                    }

                    public boolean isNativeLookAndFeel() {
                        return false;
                    }

                    public boolean isSupportedLookAndFeel() {
                        return true;
                    }
                });
            }
        } else {
            // nah! I'll let the user override defaultlaf..
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JXFrame f = new JXFrame("Button It! - " + UIManager.getLookAndFeel().getName(), true);
                f.add(new ButtonAndBarsIssues());
                f.setSize(460, 300);
                f.setStartPosition(JXFrame.StartPosition.CenterInScreen);
                f.setVisible(true);
            }
        });
    }
}
