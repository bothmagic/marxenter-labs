import org.jdesktop.swingworker.SwingWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * SwingWorkers don't die just because your frame is disposed and there isnt access to its internal ExecutorService
 * to initiate shutdown(). System.exit() would work, but if you've got a multi-framed document model app like mine
 * you can't call exit() just because you close a document (maintain a frame count and force exit when it hits zero?).
 *
 * System.exit() is pretty harsh :- Prefer a custom SwingWorker ExecutorService per documentframe
 * and expose shutdown. Workers need to check for interruptions, but critial tasks can at least decide for
 * themselves when to stop (persisting settings etc..).
 *
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 15-Jun-2007
 * Time: 17:17:38
 */

public class NeverEndingStory {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NeverEndingStory();
            }
        });
    }

    public NeverEndingStory() {
        createFrame().setVisible(true);
    }

    Frame createFrame() {
        final JFrame frame = new JFrame("Do Some Work and Close me..");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        content.setLayout(new FlowLayout());
        content.add(new JButton(new AbstractAction("Do Some Work") {
            public void actionPerformed(ActionEvent e) {
                setEnabled(false);
                new BackgroundTask().execute();
            }
        }));
        frame.setSize(320, 128);
        return frame;
    }

    class BackgroundTask extends SwingWorker<Void, Void> {
        Timer tm, tm2;

        public BackgroundTask() {
            tm = new Timer(1000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Still Working...");
                }
            });
            tm.start();
            tm2 = new Timer(5000, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("You'll have to call System.exit() to kill me crazy fool!");
                    System.out.println("- You can't shutdown() the SwingWorkers ExecutorService?");
                    tm2.stop();
                }
            });
            tm2.start();
        }

        @Override
        public Void doInBackground() throws Exception {
            Thread.currentThread().sleep(90000);
            tm.stop();
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
