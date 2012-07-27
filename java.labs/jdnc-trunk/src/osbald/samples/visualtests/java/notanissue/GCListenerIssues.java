package notanissue;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/*
 * Test case related to Issue 519
 * My bad really. But who remembers to remove the internal changelisteners on highlighters.
 * @deprecated shared highlighters no longer prevents gc
 *
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 23-May-2007
 * Time: 11:00:49
 */

/**
 * @deprecated shared highlighters no longer prevents gc
 */
public class GCListenerIssues {
    static int count = 0;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GCListenerIssues();
            }
        });
    }

    public GCListenerIssues() {
        createFrame().setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("More headaches with swings garbage collection");
        final JCheckBox checkBox = new JCheckBox("Create bad uncollectable forms");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JMenuBar menubar = new JMenuBar();
        JMenu menu = menubar.add(new JMenu("File"));
        menu.add(new AbstractAction("New") {
            public void actionPerformed(ActionEvent event) {
                BeanForm beanForm = new BeanForm(checkBox.isSelected());
                JFrame f = new TellTaleFrame("Child Frame - " + (++count), beanForm.getView());
                f.setLocationRelativeTo(frame);
                f.setVisible(true);
            }
        });
        menu.add(new AbstractAction("Call GC") {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(frame, "Click to call System.gc().. Had to ask so we can toggle kbfocus first see #4726458");
                System.gc();
            }
        });
        frame.setJMenuBar(menubar);

        JComponent content = (JComponent) frame.getContentPane();
        JLabel message = new JLabel("<html>Create & dispose some child frames and toggle gc until something happens. Oh, and have you enabled assertions?</html>");
        message.setForeground(Color.GRAY);
        content.add(message, BorderLayout.NORTH);
        content.add(checkBox, BorderLayout.SOUTH);

        frame.pack();
        frame.setSize(new Dimension(440, 280));
        return frame;
    }

    // mimic one of my typical Netbeans BeanForms

    class BeanForm {
        JComponent content;
        JLabel message;
        JScrollPane scrollPane;
        JXTable table;
        boolean badFormPlease;

        public BeanForm(boolean badFormPlease) {
            this.badFormPlease = badFormPlease;
            initComponents();
        }

        private void initComponents() {
            ComponentFactory factory = ComponentFactory.getInstance();
            content = new JPanel(new BorderLayout(5, 5));
            message = new JLabel("<html>I'm a little teapot short and stout Here is my handle <br>Here is my spout When I get all steamed up Hear me shout!</html>");
            content.add(message, BorderLayout.NORTH);
            if (badFormPlease) {
                // oh bum.. leak turns out to be my ComponentFactorys default highlighter!
                table = factory.createLeakyTable();
            } else {
                table = factory.createTable();
            }
            scrollPane = new JScrollPane(table);
            content.add(scrollPane);
        }

        JComponent getView() {
            return content;
        }
    }

    static class TellTaleFrame extends JFrame {
        public TellTaleFrame(String title, JComponent content) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle(title);
            getContentPane().add(content);
            pack();
        }

        protected void finalize() throws Throwable {
            //assert (System.out.printf("finalize() %s\n", this) != null);
            System.out.println("finalize()"+this);
            super.finalize();
        }
    }

    static class ComponentFactory {
        private static ComponentFactory instance = new ComponentFactory();
        private Highlighter defaultHighlighter = HighlighterFactory.createAlternateStriping();

        private ComponentFactory() {
        }

        static ComponentFactory getInstance() {
            return instance;
        }

        JXTable createTable() {
            return new JXTable(new Object[][]{{null, null}}, new String[]{"", ""});
        }

        JXTable createLeakyTable() {
            JXTable table = createTable();
            table.setHighlighters(defaultHighlighter);      // gah! Highlighter is shared
            return table;
        }
    }
}
