/*
 * $Id: ThreadPoolExecutor2.java 2631 2008-08-06 09:23:10Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Provides that expected functionality of ThreadPoolExecutor in that it will
 * fill all threads up to MAX_THREAD_SIZE before blocking not what the default
 * ThreadPoolExecutor does which is to only spawn new threads when the task
 * queues are full.
 *
 * <p>This implementation is taken from SwingWorker in Java SE 6.
 */
public class ThreadPoolExecutor2 extends ThreadPoolExecutor {

    private final ReentrantLock pauseLock = new ReentrantLock();
    private final Condition unpaused = pauseLock.newCondition();
    private boolean isPaused = false;
    private final ReentrantLock executeLock = new ReentrantLock();

    public ThreadPoolExecutor2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }





    public ThreadPoolExecutor2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }





    public ThreadPoolExecutor2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }





    public ThreadPoolExecutor2(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }





    @Override
    public void execute(Runnable command) {
        /*
         * ThreadPoolExecutor first tries to run task
         * in a corePool. If all threads are busy it
         * tries to add task to the waiting queue. If it
         * fails it run task in maximumPool.
         *
         * We want corePool to be 0 and
         * maximumPool to be MAX_WORKER_THREADS
         * We need to change the order of the execution.
         * First try corePool then try maximumPool
         * pool and only then store to the waiting
         * queue. We can not do that because we would
         * need access to the private methods.
         *
         * Instead we enlarge corePool to
         * MAX_WORKER_THREADS before the execution and
         * shrink it back to 0 after.
         * It does pretty much what we need.
         *
         * While we changing the corePoolSize we need
         * to stop running worker threads from accepting new
         * tasks.
         */

        //we need atomicity for the execute method.
        executeLock.lock();
        try {

            pauseLock.lock();
            try {
                isPaused = true;
            } finally {
                pauseLock.unlock();
            }

            setCorePoolSize(getMaximumPoolSize());
            super.execute(command);
            setCorePoolSize(0);

            pauseLock.lock();
            try {
                isPaused = false;
                unpaused.signalAll();
            } finally {
                pauseLock.unlock();
            }
        } finally {
            executeLock.unlock();
        }
    }





    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException ignore) {
            // ignore this exception and continue
        } finally {
            pauseLock.unlock();
        }
    }

}
