import org.jdesktop.swingworker.SwingWorker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutWorkerIssue {
    public static void main(String[] args) {
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                Thread.sleep(30 * 1000);
                return "Foo";
            }

            @Override
            protected void done() {
                try {
                    System.out.println(get(10, TimeUnit.SECONDS));
                } catch (TimeoutException e) {
                    System.out.println("Bar!");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
