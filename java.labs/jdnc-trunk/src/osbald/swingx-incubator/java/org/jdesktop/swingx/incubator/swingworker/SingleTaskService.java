package org.jdesktop.swingx.incubator.swingworker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 08-Jun-2007
 * Time: 10:11:02
 */

//TODO why does a corepool of zero cause so many problems?
//TODO what happens to a long running worker when main application exits (shutown() isn't exposed?)
//TODO what about timing out workers? suspend/resume?

public class SingleTaskService extends AbstractExecutorService {
    private ExecutorService executorService;
    private BlockingQueue<Runnable> queue;

    public SingleTaskService() {
        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            }
        };
        //Executors.newSingleThreadExecutor();
        queue = new ArrayBlockingQueue<Runnable>(1, true);
        //queue = new LinkedBlockingQueue<Runnable>();
        executorService = new ThreadPoolExecutor(0, 1, 3000L, TimeUnit.MILLISECONDS, queue,
                threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy()) {
            private List<Future> workers = Collections.synchronizedList(new LinkedList<Future>());
            private final ReentrantLock mainLock = new ReentrantLock();

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                if (r instanceof Future) {
                    workers.add((Future) r);
                    assert (System.out.printf("(+) before added=%s\n", r) != null);
                }
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                if (r instanceof Future) {
                    workers.remove((Future) r);
                    assert (System.out.printf("(-) after removed=%s\n", r) != null);
                }
            }

            void cancelAll() {
                for (Future worker : workers) {
                    if (worker != null && !worker.isCancelled() && !worker.isDone()) {
                        worker.cancel(true);
                    }
                }
                workers.clear();
                queue.clear();  // Shouldn't be needed. But might be quicker than DiscardOldestPolicy?
            }

            @Override
            public void execute(Runnable command) {
                mainLock.lock();
                try {
                    assert (System.out.printf("1. queue size=%d; head=%s; workers=%s\n", queue.size(), queue.peek(), workers) != null);
                    cancelAll();
                    assert (System.out.printf("2. queue size=%d; head=%s; workers=%s\n", queue.size(), queue.peek(), workers) != null);
                } finally {
                    mainLock.unlock();
                }
                super.execute(command);
            }
        };
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    public void execute(Runnable command) {
        executorService.execute(command);
    }
}
