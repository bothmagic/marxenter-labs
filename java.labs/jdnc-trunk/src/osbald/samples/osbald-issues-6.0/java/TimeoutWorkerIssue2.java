import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
 
import javax.swing.SwingWorker;
 
public class TimeoutWorkerIssue2 {

    public static void main(String[] args) {
	new SwingWorker<String, Void>() {
	    @Override
	    protected String doInBackground() throws Exception {
		SwingWorker<String, Void> actualWorker = new SwingWorker<String, Void>() {
 
		    @Override
		    protected String doInBackground() throws Exception {
			Thread.sleep(6 * 1000);
			return "Foo";
		    }
		};
		actualWorker.execute();
		try {
		    return actualWorker.get(3, TimeUnit.SECONDS);
		} catch (TimeoutException toe) {
		    actualWorker.cancel(true);
		    // this.cancel(true);
		    throw toe;
		}
	    }
 
	    @Override
	    protected void done() {
		try {
		    System.out.println(get());
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (ExecutionException e) {
		    e.printStackTrace();
		}
	    }
	}.execute();
    }
}
