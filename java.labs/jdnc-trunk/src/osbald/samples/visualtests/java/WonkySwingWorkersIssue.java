import org.jdesktop.swingworker.SwingWorker;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/*
   Where selection events trigger a SwingWorker to fill out the detail. Somewhat contrived example
   as timings aren't often so pronounced - but still a real issue for the unwary or unwelcome
   surprise when scaling up or suffering network contention etc..

   Should worker tasks prove expensive and selections happen in quick succession they'll
   buffer up and can take some time to clear. Quitting our application (really finish) can be
   fun in this kid of scenario :- see NeverEndingStory example. Mileage varies on your cpu power.

   Worse still where tasks vary in speed the master will show the results from the last task
   that returns (the slowest) not the last task submitted (our selection). In our example
   each task takes a random time so mileage may vary due to pure dumb luck.

   ..thinks would multi-core amplify or reduce this effect?
 */

public class WonkySwingWorkersIssue {
    static final Locale[] items = Locale.getAvailableLocales();
    JFrame frame;
    JList masterList;
    JLabel detailLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WonkySwingWorkersIssue();
            }
        });
    }

    public WonkySwingWorkersIssue() {
        this.frame = new JFrame(this.getClass().getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.masterList = new JList(items);
        masterList.setVisibleRowCount(20);
        masterList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                renderer.setText(((Locale) value).getDisplayName());
                return renderer;
            }
        });
        masterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.detailLabel = new JLabel();
        JComponent content = (JComponent) frame.getContentPane();
        JPanel form = new JPanel(new GridLayout(1, 2, 5, 0));
        form.add(new JScrollPane(masterList), BorderLayout.WEST);
        form.add(detailLabel);

        content.add(new JLabel("Pick a country to populate master selection. Try using cursor keys to change through several selections (or more) quickly."), BorderLayout.NORTH);
        content.add(form);
        content.add(new JLabel("..and wait and wait ..do the master selection & shown detail still match?"), BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        masterList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // when a new item is selected fire off a SwingWorker to get the details
                // ..operation mocked via a delayed display of the selected value
                if (!event.getValueIsAdjusting()) {
                    detailLabel.setText("[ Working.. Please Wait ]");
                    populateMaster((Locale) masterList.getSelectedValue());
                }
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    void populateMaster(Locale target) {
        new PopulateDetailTask(target).execute();
    }

    void updateMaster(String detail) {
        detailLabel.setText("<html><h1>Have you selected: <span  style='color:red'>" + detail + "?</span></h1></html>");
    }

    class PopulateDetailTask extends SwingWorker<String, Void> {
        Random random = new Random();
        Locale parameter;

        PopulateDetailTask(Locale parameter) {
            this.parameter = parameter;
        }

        @Override
        public String doInBackground() throws Exception {
            // pretend we're querying some remote machine in some expensive way
            Thread.sleep(random.nextInt(5000));
            return parameter.getDisplayName();
        }

        @Override
        protected void done() {
            try {
                updateMaster(get());
            } catch (ExecutionException e) {
                throw new Error(e.getCause());
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }
}