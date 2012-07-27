package org.jdesktop.incubator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* tidied-up version of the source borrowed from:
http: www.soft-amis.com/serendipity/index.php?/archives/12-Class-that-any-Swing-application-cant-live-without.html
*/

public class DelayedInvoker {

    protected static final int DEFAULT_DELAY = 200;

    protected InvokeLaterTimer timer;

    /**
     * data that could be passed to listener
     */
    protected Object parameter = null;

    /**
     * Creates invoker that will fire event to provided
     * listener after specified delay. Default delay (200 ms)
     * is used for invocation.
     *
     * @param actionListener listener that will be invoked
     */
    public DelayedInvoker(ActionListener actionListener) {
        this(actionListener, DEFAULT_DELAY);
    }

    /**
     * Creates invoker that will fire event to provided
     * listener after  specified delay
     *
     * @param actionListener listener that will be invoked
     * @param delay          delay in milliseconds
     */
    public DelayedInvoker(ActionListener actionListener, int delay) {
        timer = new InvokeLaterTimer(delay, actionListener);
        timer.setRepeats(false);
    }

    /**
     * Data associated with invoker
     */
    public Object getParameter() {
        return parameter;
    }

    /**
     * Setter for data associated
     */
    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    /**
     * Schedules invocation of listener with provided
     * data and delay. Many subsequent calls to this
     * method could be performed, but listener will
     * be called after specified delay only after
     * LAST invocation.
     */
    public void takeUp(Object parameter, int delay) {
        setParameter(parameter);
        takeUp(delay);
    }

    /**
     * Scheduled invocation of listener - if timer
     * is currently running, it's restarts it (so
     * already scheduled invocation will not fired,
     * if timer is not started - it simply starts it.
     * Therefore, after calling this method the
     * provided listener will be invoked once after
     * specified delay if no subsequent invocations
     * of that method will be performed.
     * Many subsequent calls to this method could be
     * performed, but listener will be called after
     * specified delay only after LAST invocation.
     */
    public void takeUp() {
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }

    /**
     * Schedules invocation of listener. Stores
     * parameter and does the same as takeUp().
     */
    public void takeUp(Object parameter) {
        setParameter(parameter);
        takeUp();
    }

    /**
     * Schedules invocation of specified listener after
     * specified delay.
     * If timer is started, performs restart of it,
     * otherwise starts it.
     * Many subsequent calls to this method could be
     * performed, but listener
     * will be called after specified delay only after
     * LAST invocation.
     */
    public void takeUp(int delay) {
        timer.setDelay(delay);
        timer.setInitialDelay(delay);
        if (timer.isRunning()) {
            timer.restart();
        } else {
            timer.start();
        }
    }

    /**
     * Utility method that allows to invoke listener
     * associated with invoker immediately.
     */
    public void force() {
        timer.fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        if (timer.isRunning()) {
            timer.stop();
        }
    }


    public static class InvokeLaterTimer extends Timer {
        public InvokeLaterTimer(int delay, ActionListener listener) {
            super(delay, listener);
        }

        /**
         * Internal method that fires invocation of associated listener
         */
        @Override
        protected void fireActionPerformed(final ActionEvent event) {
            SwingUtilities.invokeLater(new Runnable() {
                private final ActionEvent actionEvent = event;

                public void run() {
                    InvokeLaterTimer.super.fireActionPerformed(actionEvent);
                }
            });
        }
    }
}
