/*
 * $Id: SystemClockCalendarModel.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.calendar;

import javax.swing.SwingUtilities;

/**
 * @author Ronald Tetsuo Miura
 */
public class SystemClockCalendarModel extends DefaultCalendarModel {

    /**
     * Comment for <code>_clockThread</code>
     */
    private final ClockThread _clockThread;

    /**
     */
    public SystemClockCalendarModel() {
        this(1000);
    }

    /**
     * @param delay
     */
    public SystemClockCalendarModel(long delay) {
        this._clockThread = new ClockThread(delay);
        this._clockThread.setDaemon(true);
        this._clockThread.setPriority(Thread.MAX_PRIORITY);
    }

    /**
     * 
     */
    public void start() {
        this._clockThread.start();
    }

    /**
     * 
     */
    public void stop() {
        this._clockThread.shutdown();
    }

    /**
     * @author Ronald Tetsuo Miura
     */
    private class ClockThread extends Thread {

        /**
         * Comment for <code>_delay</code>
         */
        private long _delay;

        /**
         * @param delay
         */
        public ClockThread(long delay) {
            this._delay = delay;
        }

        /**
         * 
         */
        public void shutdown() {
            this._delay = -1;
        }

        /**
         * @see java.lang.Thread#run()
         */
        public void run() {
            while (this._delay > 0) {
                SwingUtilities.invokeLater(new Runnable() {

                    /**
                     * @see java.lang.Runnable#run()
                     */
                    public void run() {
                        setTime(System.currentTimeMillis());
                    }
                });
                try {
                    Thread.sleep(this._delay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}