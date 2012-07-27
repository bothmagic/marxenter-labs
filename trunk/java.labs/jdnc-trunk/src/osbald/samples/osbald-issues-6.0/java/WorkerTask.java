import java.util.concurrent.ExecutionException;
 
import javax.swing.SwingWorker;
 
public abstract class WorkerTask<T, V> extends SwingWorker<T, V> {
 
    @Override
    protected final void done() {
        if(isCancelled()) {
            cancelled();
        }
        else {
            try {
                finished(get());
            }
            catch (InterruptedException e) {
                failed(e);
            }
            catch (ExecutionException e) {
                failed(e.getCause());
            }
        }
    }
 
    /**
     * Called on the EDT when the task is finished.
     * By default does nothing.
     * 
     * @param result The result of the task.
     */
    protected void finished(T result) {
    }
 
    /**
     * Called on the EDT when the task failed.
     * By default does nothing.
     * 
     * @param cause The exception which occured while running the task.
     */
    protected void failed(Throwable cause) {
    }
 
    /**
     * Called on the EDT when the task is cancelled.
     * By default does nothing.
     */
    protected void cancelled() {
    }
}
