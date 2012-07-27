import org.jdesktop.swingworker.SwingWorker;

import javax.swing.*;
import java.util.Locale;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/*
    Simple fix for WonkySwingWorkersIssue, keep a record of the previous worker and cancel it
    when a new worker is started. NB some methods can't be cancelled (e.g. RMI) so they'll
    still run in the background threads but fail the !isCancelled() check in done().
*/

public class WonkySwingWorkersFix1 extends WonkySwingWorkersIssue {
    SwingWorker populateWorker;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WonkySwingWorkersFix1();
            }
        });
    }

    @Override
    void populateMaster(Locale target) {
        if (populateWorker != null) {
            populateWorker.cancel(true);
        }
        populateWorker = new PopulateDetailTask(target);
        populateWorker.execute();
    }

    class PopulateDetailTask extends WonkySwingWorkersIssue.PopulateDetailTask {
        PopulateDetailTask(Locale parameter) {
            super(parameter);
        }

        @Override
        protected void done() {
            try {
                updateMaster(get());
            } catch (ExecutionException e) {
                throw new Error(e.getCause());
            } catch (InterruptedException e) {
                //ignore
            } catch (CancellationException e) {
                //ignore
            }
        }
    }
}