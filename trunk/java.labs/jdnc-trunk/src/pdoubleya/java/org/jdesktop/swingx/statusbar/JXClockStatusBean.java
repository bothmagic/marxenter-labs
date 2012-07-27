package org.jdesktop.swingx.statusbar;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 * Displays a constantly updated clock as a status component, or an elapsed timer (like a stopwatch, counting
 * upward). The clock can be created using the regular constructors; the elapsed timer can be created
 * using the {@link #newElapsedTimeBean(int)} method.
 * 
 * @author Patrick Wright
 */
public class JXClockStatusBean extends JXLabelStatusBean {
    /**
     * For block synchronization.
     */
    public Object lock = new Object();
    /**
     * The date we are currently displaying.
     */
    private Date currentDisplayedDate;
    /**
     * The default date format used for showing the time; see {@link java.text.DateFormat}.
     */
    private static final String DEFAULT_DATE_FORMAT;
    /**
     * The actual date format used for showing the time; see {@link java.text.DateFormat}.
     */
    private DateFormat dateFormat;
    /**
     * Delay in milliseconds between updates to this clock or timer.
     */
    private int updateDelay;
    /**
     * Our Timer class, that pings updates.
     */
    private Timer dateTimer;
    /**
     * The ActionListener that receives pings from our Timer; handles the update work.
     */
    private DateTimerListener dateTimerListener;
    /**
     * Whether this is a clock or an elapsed timer.
     */
    private UpdateType updateType;
    
    
    public enum UpdateType { ACTUAL_DATE, ELAPSED };
    
    static {
        DEFAULT_DATE_FORMAT = "h:mm:ss a";
    }
    
    /**
     * Default constructor; initializes a clock at the time of creation, with an update every second.
     */
    public JXClockStatusBean() {
        this(new Date());
    }
    
    /**
     * Initializes a clock at the specified date/time, with an update every second.
     * @param startAt The Date at which the clock should start.
     */
    public JXClockStatusBean(Date startAt) {
        this(startAt, 1000);
    }
    
    /**
     * Initializes a clock at the specified date/time, with an update on the specified interval.
     * @param startAt The Date at which the clock starts.
     * @param updateEveryMS Time in milliseconds between updates.
     */
    public JXClockStatusBean(Date startAt, int updateEveryMS) {
        this(startAt, 1000, DEFAULT_DATE_FORMAT, UpdateType.ACTUAL_DATE);
    }
    
    /**
     * Initializes either a clock or an elapsed timer at the specified date/time, 
     * with an update on the specified interval, and with a specified date format.
     * @param startAt The Date at which the clock starts.
     * @param updateEveryMS Time in milliseconds between updates.
     * @param dateFormat The DateFormat string to use in displaying the clock.
     * @param updateType Value from enumeration UpdateType, either a clock or an elapsed timer.
     */
    public JXClockStatusBean(Date startAt, int updateEveryMS, String dateFormat, UpdateType updateType) {
        super("Clock");
        this.currentDisplayedDate = startAt;
        this.updateDelay = updateEveryMS;
        this.dateFormat = new SimpleDateFormat(dateFormat);
        this.updateType = updateType;
        this.getLabel().setHorizontalAlignment(SwingConstants.CENTER);
        this.getLabel().setVerticalAlignment(SwingConstants.CENTER);
        
        // TODO: need to set the preferred size based on the date format (which affects length)
        this.getLabel().setPreferredSize(new Dimension(75, 19));
        
        initClockTimer();
    }
    
    /**
     * Convenience factory method to create a new elapsed time timer updated on a certain interval.
     * @param updateDelay Time in milliseconds between updates.
     * @return A JXClockStatusBean acting as an elapsed timer.
     */
    public static JXClockStatusBean newElapsedTimeBean(int updateDelay) {
        return newElapsedTimeBean(updateDelay, "mm:ss:SSSS");
    }
    
    /**
     * Convenience factory method to create a new elapsed time timer updated on a certain interval.
     * @param updateDelay Time in milliseconds between updates.
     * @param displayFormat The display format for the elapsed timer.
     * @return A JXClockStatusBean acting as an elapsed timer.
     */    
    public static JXClockStatusBean newElapsedTimeBean(int updateDelay, String displayFormat) {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        JXClockStatusBean bean = new JXClockStatusBean(midnight.getTime(), updateDelay, displayFormat, JXClockStatusBean.UpdateType.ELAPSED);
        bean.beanName = "Elapsed-time";
        return bean;
    }
    
    // HACK: used for elapsed only
    /** For an elapsed timer, resets the timer to zero. */
    public void resetToZero() {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        this.currentDisplayedDate = midnight.getTime();
        this.dateTimerListener.reset();
    }
    
    /** Stops updates to the clock or elapsed timer. */
    public void stopClock() {
        synchronized (lock) {
            if ( dateTimer != null && dateTimer.isRunning()) {
                dateTimer.stop();
                dateTimer = null;
            }
        }
    }
    
    /** Starts updates to the clock or elapsed timer. */
    public void startClock() {
        synchronized (lock) {
            if ( dateTimer == null ) {
                dateTimer = new Timer(updateDelay, dateTimerListener);
                dateTimerListener.actionPerformed(null);
            }
            if ( dateTimer != null && ! dateTimer.isRunning()) {
                dateTimer.start();
            }
        }
    }

    /** 
     * Sets a new date format string to use for displaying the date/time. For the elapsed timer, the date portion
     * will probably be irrelevant.
     */
    public void setDateFormat(String newDateFormat) {
        SimpleDateFormat sdf = null;
        try {
            sdf = new SimpleDateFormat(newDateFormat);
            dateFormat = sdf;
        } catch (IllegalArgumentException e) {
            // do nothing, leave format as is.
        }
    }
    
    private void initClockTimer() {
        this.dateTimerListener= new DateTimerListener();
        startClock();
    }
    
    // used to receive pings from the timer, to update the current clock
    private class DateTimerListener implements ActionListener {
        long startedAtMS;
        long lastPing;
        Calendar calendar;
        
        DateTimerListener() {
            this.startedAtMS = currentDisplayedDate.getTime();
            this.lastPing = System.currentTimeMillis();
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(currentDisplayedDate);
        }
        
        public void reset() {
            this.startedAtMS = currentDisplayedDate.getTime();
            this.lastPing = System.currentTimeMillis();
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(currentDisplayedDate);            
        }
        
        public void actionPerformed(ActionEvent e) {
            if ( JXClockStatusBean.this.updateType == UpdateType.ACTUAL_DATE ) {
                currentDisplayedDate.setTime(System.currentTimeMillis());
            } else {
                long elapsed = System.currentTimeMillis() - lastPing;
                lastPing = System.currentTimeMillis();
                calendar.add(Calendar.MILLISECOND, (int)(elapsed));
                currentDisplayedDate.setTime(calendar.getTimeInMillis());
            }
            getLabel().setText(dateFormat.format(currentDisplayedDate));
        }
    }
}
