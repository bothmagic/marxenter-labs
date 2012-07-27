/*
 * $Id: JXMonthView.java 3339 2011-02-04 17:03:23Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.time.calendar.Clock;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;
import javax.time.calendar.TimeZone;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;
import org.jdesktop.swingx.calendar.jsr310.Date310SelectionModel.SelectionMode;
import org.jdesktop.swingx.calendar.jsr310.plaf.MonthViewAddon;
import org.jdesktop.swingx.calendar.jsr310.plaf.MonthViewUI;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.util.Contract;


/**
 * Component that displays a month calendar which can be used to select a day
 * or range of days.  By default the <code>JXMonthView</code> will display a
 * single calendar using the current month and year, using
 * <code>Calendar.SUNDAY</code> as the first day of the week.
 * <p>
 * The <code>JXMonthView</code> can be configured to display more than one
 * calendar at a time by calling
 * <code>setPreferredCalCols</code>/<code>setPreferredCalRows</code>.  These
 * methods will set the preferred number of calendars to use in each
 * column/row.  As these values change, the <code>Dimension</code> returned
 * from <code>getMinimumSize</code> and <code>getPreferredSize</code> will
 * be updated.  The following example shows how to create a 2x2 view which is
 * contained within a <code>JFrame</code>:
 * <pre>
 *     JXMonthView monthView = new JXMonthView();
 *     monthView.setPreferredCols(2);
 *     monthView.setPreferredRows(2);
 *
 *     JFrame frame = new JFrame();
 *     frame.getContentPane().add(monthView);
 *     frame.pack();
 *     frame.setVisible(true);
 * </pre>
 * <p>
 * <code>JXMonthView</code> can be further configured to allow any day of the
 * week to be considered the first day of the week.  Character
 * representation of those days may also be set by providing an array of
 * strings.
 * <pre>
 *    monthView.setFirstDayOfWeek(Calendar.MONDAY);
 *    monthView.setDaysOfTheWeek(
 *            new String[]{"S", "M", "T", "W", "Th", "F", "S"});
 * </pre>
 * <p>
 * This component supports flagging days.  These flagged days are displayed
 * in a bold font.  This can be used to inform the user of such things as
 * scheduled appointment.
 * <pre><code>
 *    // Create some dates that we want to flag as being important.
 *    Calendar cal1 = Calendar.getInstance();
 *    cal1.set(2004, 1, 1);
 *    Calendar cal2 = Calendar.getInstance();
 *    cal2.set(2004, 1, 5);
 *
 *    monthView.setFlaggedDates(cal1.getTime(), cal2.getTime(), new LocalDateTime());
 * </code></pre>
 * Applications may have the need to allow users to select different ranges of
 * dates.  There are three modes of selection that are supported, single, single interval
 * and multiple interval selection.  Once a selection is made an DateSelectionEvent is
 * fired to inform listeners of the change.
 * <pre>
 *    // Change the selection mode to select full weeks.
 *    monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
 *
 *    // Register a date selection listener to get notified about
 *    // any changes in the date selection model.
 *    monthView.getSelectionModel().addDateSelectionListener(new Date310SelectionListener {
 *        public void valueChanged(DateSelectionEvent e) {
 *            log.info(e.getSelection());
 *        }
 *    });
 * </pre>
 * 
 * NOTE (for users of earlier versions): as of version 1.19 control about selection 
 * dates is moved completely into the model. The default model used is of type 
 * Day310SelectionModel, which handles dates in the same way the JXMonthView did earlier
 * (that is, normalize all to the start of the day, which means zeroing all time
 * fields).<p>
 * 
 * @author Joshua Outwater
 * @author Jeanette Winzenburg
 * @version  $Revision: 3339 $
 */
public class JXMonthView extends JComponent {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(JXMonthView.class
            .getName());
    /*
     * moved from package calendar to swingx at version 1.51
     */

    /** action command used for commit actionEvent. */
    public static final String COMMIT_KEY = "monthViewCommit";
    /** action command used for cancel actionEvent. */
    public static final String CANCEL_KEY = "monthViewCancel";

    public static final String BOX_PADDING_X = "boxPaddingX";
    public static final String BOX_PADDING_Y = "boxPaddingY";
    public static final String DAYS_OF_THE_WEEK = "daysOfTheWeek";
    public static final String SELECTION_MODEL = "selectionModel";
    public static final String TRAVERSABLE = "traversable";
    public static final String FLAGGED_DATES = "flaggedDates";

    static {
        LookAndFeelAddons.contribute(new MonthViewAddon());
    }

     /**
     * UI Class ID
     */
    public static final String uiClassID = "MonthViewUI";

    public static final int DAYS_IN_WEEK = 7;
    public static final int MONTHS_IN_YEAR = 12;


    /**
     * Keeps track of the first date we are displaying.  We use this as a
     * restore point for the calendar. This is normalized to the start of the
     * first day of the month given in setFirstDisplayedDate.
     */
    private LocalDateTime firstDisplayedDay;
    /** 
     * Start of the day which contains System.millis() in the current calendar.
     * Kept in synch via a timer started in addNotify.
     */
    private LocalDateTime today;
    private LocalDateTime anchor;
    /**
     * The timer used to keep today in synch with system time.
     */
//    private Timer todayTimer;

    private DayOfWeek firstDayOfWeek;
    //-------------- selection/flagging
    /** 
     * The Date310SelectionModel driving this component. This model's calendar
     * is the reference for all dates.
     */
    private Date310SelectionModel model;
    /**
     * Listener registered with the current model to keep Calendar dependent
     * state synched.
     */
    private Date310SelectionListener modelListener;
    /** 
     * The manager of the flagged dates. Note
     * that the type of this is an implementation detail.  
     */
    private Day310SelectionModel flaggedDates;
    /**
     * Storage of actionListeners registered with the monthView.
     */
    private EventListenerMap listenerMap;
    
    private boolean traversable;
    private boolean leadingDays;
    private boolean trailingDays;
    private boolean showWeekNumber;
    private boolean componentInputMapEnabled;
    
    //-------------------
    // PENDING JW: ??
    @SuppressWarnings({"FieldCanBeLocal"})
    protected LocalDateTime modifiedStartDate;
    @SuppressWarnings({"FieldCanBeLocal"})
    protected LocalDateTime modifiedEndDate;
    
    //------------- visuals
    
    /**
     * localizable day column headers. Default typically installed by the uidelegate.
     */
//    private String[] _daysOfTheWeek;
    /**
     * Insets used in determining the rectangle for the month string
     * background.
     */
    private boolean antialiased;
    protected Insets _monthStringInsets = new Insets(0, 0, 0, 0);
    private int boxPaddingX;
    private int boxPaddingY;
    private int minCalCols = 1;
    private int minCalRows = 1;
    private Color todayBackgroundColor;
    private Color monthStringBackground;
    private Color monthStringForeground;
    private Color daysOfTheWeekForeground;
    private Color selectedBackground;
    private Map<DayOfWeek, Color> dayToColorTable = new HashMap<DayOfWeek, Color>();
    private Color flaggedDayForeground;

    private Color selectedForeground;
    private boolean zoomable;

    private TimeZone timeZone;

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * default Locale and the current system time as the first date to 
     * display.
     */
    public JXMonthView() {
        this(Clock.systemDefaultZone().dateTime(), null, null);
    }

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the 
     * default Locale and the current system time as the first date to 
     * display.
     * 
     * @param locale desired locale, if null the system default locale is used
     */
    public JXMonthView(final Locale locale) {
        this(Clock.systemDefaultZone().dateTime(), null, locale);
    }

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * default Locale and the given time as the first date to 
     * display.
     *
     * @param firstDisplayedDate The first month to display.
     */
    public JXMonthView(LocalDateTime firstDisplayedDate) {
        this(firstDisplayedDate, null, null);
    }

    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * default Locale, the given time as the first date to 
     * display and the given selection model. 
     * 
     * @param firstDisplayedDate The first month to display.
     * @param model the selection model to use, if null a <code>DefaultSelectionModel</code> is
     *   created.
     */
    public JXMonthView(LocalDateTime firstDisplayedDate, final Date310SelectionModel model) {
        this(firstDisplayedDate, model, null);
    }


    /**
     * Create a new instance of the <code>JXMonthView</code> class using the
     * given Locale, the given time as the first date to 
     * display and the given selection model. 
     * 
     * @param firstDisplayedDay 
     * @param model the selection model to use, if null a <code>DefaultSelectionModel</code> is
     *   created.
     * @param locale desired locale, if null the system default locale is used
     */
    public JXMonthView(LocalDateTime firstDisplayedDay, final Date310SelectionModel model, final Locale locale) {
        super();
        listenerMap = new EventListenerMap();

        initModel(model, locale);
        superSetLocale(locale);
        setFirstDisplayedDay(firstDisplayedDay);
        // Keep track of today
        updateTodayFromCurrentTime();

        // install the controller
        updateUI();

        setFocusable(true);
        todayBackgroundColor = getForeground();

    }

    
//------------------ Calendar related properties
    
    /**
     * Sets locale and resets text and format used to display months and days. 
     * Also resets firstDayOfWeek. <p>
     * 
     * PENDING JW: the following warning should be obsolete (installCalendar
     * should take care) - check if it really is!
     * 
     * <p>
     * <b>Warning:</b> Since this resets any string labels that are cached in UI
     * (month and day names) and firstDayofWeek, use <code>setDaysOfTheWeek</code> and/or
     * setFirstDayOfWeek after (re)setting locale.
     * </p>
     * 
     * @param   locale new Locale to be used for formatting
     * @see     #setDaysOfTheWeek(String[])
     * @see     #setFirstDayOfWeek(int)
     */
    @Override
    public void setLocale(Locale locale) {
        model.setLocale(locale);
    }

    /**
     * 
     * @param locale
     */
    private void superSetLocale(Locale locale) {
        // PENDING JW: formally, a null value is allowed and must be passed on to super
        // I suspect this is not done here to keep the logic out off the constructor?
        // 
        if (locale != null) {
            super.setLocale(locale);
            repaint();
       }
    }
    

    /**
     * Gets the time zone.
     *
     * @return The <code>TimeZone</code> used by the <code>JXMonthView</code>.
     */
    public TimeZone getTimeZone() {
        // PENDING JW: looks fishy (left-over?) .. why not ask the model?
        return timeZone;
    }

    /**
     * Sets the time zone with the given time zone value.
     * 
     * This is a bound property. 
     * 
     * @param tz The <code>TimeZone</code>.
     */
    public void setTimeZone(TimeZone tz) {
        model.setTimeZone(tz);
    }

    /**
     * Gets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in the U.S., <code>Calendar.MONDAY</code>
     * in France.
     *
     * @return int The first day of the week.
     */
    public DayOfWeek getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    /**
     * Sets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in US, <code>Calendar.MONDAY</code>
     * in France.
     *
     * @param firstDayOfWeek The first day of the week.
     * @see java.util.Calendar
     */
//    public void setFirstDayOfWeek(int firstDayOfWeek) {
//        getSelectionModel().setFirstDayOfWeek(firstDayOfWeek);
//    }



//---------------------- synch to model's calendar    

    /**
     * Initializes selection model related internals. If the Locale is
     * null, it falls back to JComponent.defaultLocale. If the model
     * is null it creates a default model with the locale.
     * 
     * PENDING JW: leave default locale fallback to model?
     * 
     * @param model the Date310SelectionModel which should drive the monthView. 
     *    If null, a default model is created and initialized with the given locale.
     * @param locale the Locale to use with the selectionModel. If null,
     *   JComponent.getDefaultLocale is used.
     */
    private void initModel(Date310SelectionModel model, Locale locale) {
        if (locale == null) {
            locale = JComponent.getDefaultLocale();
        }
        if (model == null) {
            model = new Day310SelectionModel(locale);
        }
        this.model = model;
        // PENDING JW: do better to synchronize Calendar related 
        // properties of flaggedDates to those of the selection model.
        // plus: should use the same normalization?
        this.flaggedDates = new Day310SelectionModel(locale);
        flaggedDates.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        
        installCalendar();
        model.addDateSelectionListener(getDateSelectionListener());
    }

    /**
     * Lazily creates and returns the Date310SelectionListener which listens
     * for model's calendar properties.
     * 
     * @return a Date310SelectionListener for model's CALENDAR_CHANGED notification.
     */
    private Date310SelectionListener getDateSelectionListener() {
        if (modelListener == null) {
            modelListener = new Date310SelectionListener() {

                public void valueChanged(Date310SelectionEvent ev) {
                    if (EventType.CALENDAR_CHANGED.equals(ev.getEventType())) {
                        updateCalendar();
                    }
                    
                }
                
            };
        }
        return modelListener;
    }

    /**
     * Installs the internal calendars from the selection model.
     * 
     */
    private void installCalendar() {
        firstDayOfWeek = model.getFirstDayOfWeek();
        timeZone = model.getTimeZone();
    }

    /**
     * Returns the anchor date. Currently, this is the "uncleaned" input date 
     * of setFirstDisplayedDate. This is a quick hack for Issue #618-swingx, to
     * have some invariant for testing. Do not use in client code, may change
     * without notice!
     * 
     * @return the "uncleaned" first display date.
     */
    protected LocalDateTime getAnchorDate() {
        return anchor;
    }

    /**
     * Callback from selection model calendar changes.
     */
    private void updateCalendar() {
       if (!getLocale().equals(model.getLocale())) {
           installCalendar();
           superSetLocale(model.getLocale());
       } else {
           if (!model.getTimeZone().equals(getTimeZone())) {
               updateTimeZone();
           }
//           if (cal.getMinimalDaysInFirstWeek() != model.getMinimalDaysInFirstWeek()) {
//               updateMinimalDaysOfFirstWeek();
//           }
           if (firstDayOfWeek != model.getFirstDayOfWeek()) {
              updateFirstDayOfWeek(); 
           }
       }
    }


    /**
     * Callback from changing timezone in model.
     */
    private void updateTimeZone() {
        TimeZone old = getTimeZone();
//        TimeZone tz = model.getTimeZone();
//        cal.setTimeZone(tz);
//        anchor.setTimeZone(tz);
        setFirstDisplayedDay(anchor);
        updateTodayFromCurrentTime();
        updateDatesAfterTimeZoneChange(old);
        firePropertyChange("timeZone", old, getTimeZone());
    }
    
    /**
     * All dates are "cleaned" relative to the timezone they had been set.
     * After changing the timezone, they need to be updated to the new.
     * 
     * Here: clear everything. 
     * 
     * @param oldTimeZone the timezone before the change
     */
    protected void updateDatesAfterTimeZoneChange(TimeZone oldTimeZone) {
        SortedSet<LocalDateTime> flagged = getFlaggedDates();
        flaggedDates.setTimeZone(getTimeZone());
        firePropertyChange("flaggedDates", flagged, getFlaggedDates());
     }
    
    /**
     * Call back from listening to model firstDayOfWeek change.
     */
    private void updateFirstDayOfWeek() {
        DayOfWeek oldFirstDayOfWeek = this.firstDayOfWeek;

        firstDayOfWeek = getSelectionModel().getFirstDayOfWeek();
        firePropertyChange("firstDayOfWeek", oldFirstDayOfWeek, firstDayOfWeek);
    }

    /**
     * Call back from listening to model minimalDaysOfFirstWeek change.
     * <p>
     * NOTE: this is not a property as we have no public api to change
     * it on JXMonthView.
     */
//    private void updateMinimalDaysOfFirstWeek() {
//        cal.setMinimalDaysInFirstWeek(model.getMinimalDaysInFirstWeek());
//        anchor.setMinimalDaysInFirstWeek(model.getMinimalDaysInFirstWeek());
//    }


    
//-------------------- scrolling
    /**
     * Returns the last date able to be displayed.  For example, if the last
     * visible month was April the time returned would be April 30, 23:59:59.
     *
     * @return long The last displayed date.
     */
    public LocalDateTime getLastDisplayedDay() {
        return getUI().getLastDisplayedDay();
    }

    
    /**
     * Returns the first displayed date.
     *
     * @return long The first displayed date.
     */
    public LocalDateTime getFirstDisplayedDay() {
        return firstDisplayedDay;
    }

    
    /**
     * Set the first displayed date.  We only use the month and year of
     * this date.  The <code>Calendar.DAY_OF_MONTH</code> field is reset to
     * 1 and all other fields, with exception of the year and month,
     * are reset to 0.
     *
     * @param date The first displayed date.
     */
    public void setFirstDisplayedDay(LocalDateTime date) {
        anchor = date;
        LocalDateTime oldDate = getFirstDisplayedDay();
        LocalDateTime local = date.withDayOfMonth(1);
        firstDisplayedDay = startOfDay(local);

        firePropertyChange("firstDisplayedDay", oldDate, getFirstDisplayedDay() );
    }



    /**
     * Moves the <code>date</code> into the visible region of the calendar. If
     * the date is greater than the last visible date it will become the last
     * visible date. While if it is less than the first visible date it will
     * become the first visible date. <p>
     * 
     * NOTE: this is the recommended method to scroll to a particular date, the
     * functionally equivalent method taking a long as parameter will most 
     * probably be deprecated.
     * 
     * @param date LocalDateTime to make visible, must not be null.
     * @see #ensureDateVisible(LocalDateTime)
     */
    public void ensureDateVisible(LocalDateTime date) {
        if (date.isBefore(firstDisplayedDay)) {
            setFirstDisplayedDay(date);
        } else {
            LocalDateTime lastDisplayedDate = getLastDisplayedDay();
            if (date.isAfter(lastDisplayedDate)) {
                int month = date.getMonthOfYear().getValue();
                int year = date.getYear();

                int lastMonth = lastDisplayedDate.getMonthOfYear().getValue();
                int lastYear = lastDisplayedDate.getYear();

                int diffMonths = month - lastMonth
                        + ((year - lastYear) * MONTHS_IN_YEAR);
                
                setFirstDisplayedDay(firstDisplayedDay.plus(Period.ofMonths(diffMonths)));
            }
        }
    }


    /**
     * Returns the LocalDateTime at the given location. May be null if the
     * coordinates don't map to a day in the month which contains the 
     * coordinates. Specifically: hitting leading/trailing dates returns null.
     * 
     * Mapping pixel to calendar day.
     *
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the day at the given location or null if the location
     *   doesn't map to a day in the month which contains the coordinates.
     */ 
    public LocalDateTime getDayAtLocation(int x, int y) {
        return getUI().getDayAtLocation(x, y);
    }
   
//------------------ today
    
    /**
     * Sets today from the current system time. 
     * 
     * temporary widened access for testing.
     */
    protected void updateTodayFromCurrentTime() {
        
        setToday(Clock.system(getTimeZone()).dateTime());
    }

    /**
     * Increments today. This is used by the timer.
     * 
     * PENDING: is it safe? doesn't check if we are really tomorrow?
     * temporary widened access for testing.
     */
    protected void incrementToday() {
        setToday(getToday().plus(Period.ofDays(1)));
    }

    /**
     * Sets the date which represents today. Internally 
     * modified to the start of the day which contains the
     * given date in this monthView's calendar coordinates.
     *  
     * temporary widened access for testing.
     * 
     * @param date the date which should be used as today.
     */
    protected void setToday(LocalDateTime date) {
        LocalDateTime oldToday = getToday();
        // PENDING JW: do we really want the start of today? 
        this.today = startOfDay(date);
        firePropertyChange("today", oldToday, getToday());
    }

    /**
     * Returns the start of today in this monthviews calendar coordinates.
     * 
     * @return the start of today as LocalDateTime.
     */
    public LocalDateTime getToday() {
        // null only happens in the very first time ... 
        return today;
    }

    
    
//----   internal date manipulation ("cleanup" == start of day in monthView's calendar)
    

    /**
     * Returns the start of the day as LocalDateTime.
     * 
     * @param date the LocalDateTime.
     * @return start of the given day as LocalDateTime, relative to this
     *    monthView's calendar.
     *    
     */
    private LocalDateTime startOfDay(LocalDateTime date) {
        return LocalDateTime.ofMidnight(date);
    }

//------------------- ui delegate    
    /**
     * @inheritDoc
     */
    public MonthViewUI getUI() {
        return (MonthViewUI)ui;
    }

    /**
     * Sets the L&F object that renders this component.
     *
     * @param ui UI to use for this {@code JXMonthView}
     */
    public void setUI(MonthViewUI ui) {
        super.setUI(ui);
    }

    /**
     * Resets the UI property with the value from the current look and feel.
     *
     * @see UIManager#getUI(JComponent)
     */
    @Override
    public void updateUI() {
        setUI((MonthViewUI)LookAndFeelAddons.getUI(this, MonthViewUI.class));
        invalidate();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    
//---------------- Date310SelectionModel

    /**
     * Returns the date selection model which drives this
     * JXMonthView.
     * 
     * @return the date selection model
     */
    public Date310SelectionModel getSelectionModel() {
        return model;
    }

    /**
     * Sets the date selection model to drive this monthView.
     * 
     * @param model the selection model to use, must not be null.
     * @throws NullPointerException if model is null
     */
    public void setSelectionModel(Date310SelectionModel model) {
        Contract.asNotNull(model, "date selection model must not be null");
        Date310SelectionModel oldModel = getSelectionModel();
        model.removeDateSelectionListener(getDateSelectionListener());
        this.model = model;
        installCalendar();
        if (!model.getLocale().equals(getLocale())) {
            super.setLocale(model.getLocale());
        }
        model.addDateSelectionListener(getDateSelectionListener());
        firePropertyChange(SELECTION_MODEL, oldModel, getSelectionModel());
    }

//-------------------- delegates to model
    
    /**
     * Clear any selection from the selection model
     */
    public void clearSelection() {
        getSelectionModel().clearSelection();
    }

    /**
     * Return true if the selection is empty, false otherwise
     *
     * @return true if the selection is empty, false otherwise
     */
    public boolean isSelectionEmpty() {
        return getSelectionModel().isSelectionEmpty();
    }

    /**
     * Get the current selection
     *
     * @return sorted set of selected dates
     */
   public SortedSet<LocalDateTime> getSelection() {
        return getSelectionModel().getSelection();
    }

    /**
     * Adds the selection interval to the selection model. 
     * 
     * @param startDate Start of date range to add to the selection
     * @param endDate End of date range to add to the selection
     */
    public void addSelectionInterval(LocalDateTime startDate, LocalDateTime endDate) {
            getSelectionModel().addSelectionInterval(startDate, endDate);
    }

    /**
     * Sets the selection interval to the selection model.  
     *
     * @param startDate Start of date range to set the selection to
     * @param endDate End of date range to set the selection to
     */
    public void setSelectionInterval(final LocalDateTime startDate, final LocalDateTime endDate) {
            getSelectionModel().setSelectionInterval(startDate, endDate);
    }

    /**
     * Removes the selection interval from the selection model.  
     * 
     * @param startDate Start of the date range to remove from the selection
     * @param endDate End of the date range to remove from the selection
     */
    public void removeSelectionInterval(final LocalDateTime startDate, final LocalDateTime endDate) {
        getSelectionModel().removeSelectionInterval(startDate, endDate);
    }

    /**
     * Returns the current selection mode for this JXMonthView.
     *
     * @return int Selection mode.
     */
    public SelectionMode getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }

    /**
     * Set the selection mode for this JXMonthView.

     * @param selectionMode The selection mode to use for this {@code JXMonthView}
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        getSelectionModel().setSelectionMode(selectionMode);
    }

    /**
     * Returns the earliest selected date. 
     * 
     *   
     * @return the first LocalDateTime in the selection or null if empty.
     */
    public LocalDateTime getFirstSelectionDate() {
        return getSelectionModel().getFirstSelectionDate();    
     }
   

    /**
     * Returns the earliest selected date. 
     * 
     * @return the first LocalDateTime in the selection or null if empty.
     */
    public LocalDateTime getLastSelectionDate() {
        return getSelectionModel().getLastSelectionDate();    
     }

    /**
     * Returns the earliest selected date. 
     * 
     * PENDING JW: keep this? it was introduced before the first/last 
     *   in model. When delegating everything, we duplicate here.
     *   
     * @return the first LocalDateTime in the selection or null if empty.
     */
    public LocalDateTime getSelectionDate() {
        return getFirstSelectionDate();    
    }

    /**
     * Sets the model's selection to the given date or clears the selection if
     * null.
     * 
     * @param newDate the selection date to set
     */
    public void setSelectionDate(LocalDateTime newDate) {
        if (newDate == null) {
            clearSelection();
        } else {
            setSelectionInterval(newDate, newDate);
        }
    }

    /**
     * Returns true if the specified date falls within the _startSelectedDate
     * and _endSelectedDate range.  
     *
     * @param date The date to check
     * @return true if the date is selected, false otherwise
     */
    public boolean isSelected(LocalDateTime date) {
        return getSelectionModel().isSelected(date);
    }


    /**
     * Set the lower bound date that is allowed to be selected. <p>
     * 
     * 
     * @param lowerBound the lower bound, null means none.
     */
    public void setLowerBound(LocalDateTime lowerBound) {
        getSelectionModel().setLowerBound(lowerBound);
    }

    /**
     * Set the upper bound date that is allowed to be selected. <p>
     * 
     * @param upperBound the upper bound, null means none.
     */
    public void setUpperBound(LocalDateTime upperBound) {
        getSelectionModel().setUpperBound(upperBound);
    }


    /**
     * Return the lower bound date that is allowed to be selected for this
     * model.
     *
     * @return lower bound date or null if not set
     */
    public LocalDateTime getLowerBound() {
        return getSelectionModel().getLowerBound();
    }

    /**
     * Return the upper bound date that is allowed to be selected for this
     * model.
     *
     * @return upper bound date or null if not set
     */
    public LocalDateTime getUpperBound() {
        return getSelectionModel().getUpperBound();
    }

    /**
     * Identifies whether or not the date passed is an unselectable date.
     * <p>
     * 
     * @param date date which to test for unselectable status
     * @return true if the date is unselectable, false otherwise
     */
    public boolean isUnselectableDate(LocalDateTime date) {
        return getSelectionModel().isUnselectableDate(date);
    }

    /**
     * Sets the dates that should be unselectable. This will replace the model's
     * current set of unselectable dates. The implication is that calling with
     * zero dates will remove all unselectable dates.
     * <p>
     * 
     * NOTE: neither the given array nor any of its elements must be null.
     * 
     * @param unselectableDates zero or more not-null dates that should be
     *        unselectable.
     * @throws NullPointerException if either the array or any of the elements
     *         are null
     */
    public void setUnselectableDates(LocalDateTime... unselectableDates) {
        Contract.asNotNull(unselectableDates,
                "unselectable dates must not be null");
        SortedSet<LocalDateTime> unselectableSet = new TreeSet<LocalDateTime>();
        for (LocalDateTime unselectableDate : unselectableDates) {
            unselectableSet.add(unselectableDate);
        }
        getSelectionModel().setUnselectableDates(unselectableSet);
        // PENDING JW: check that ui does the repaint!
        repaint();
    }

    // --------------------- flagged dates
    /**
     * Identifies whether or not the date passed is a flagged date. 
     * 
     * @param date date which to test for flagged status
     * @return true if the date is flagged, false otherwise
     */
    public boolean isFlaggedDate(LocalDateTime date) {
        if (date == null)
            return false;
        return flaggedDates.isSelected(date);
    }
    
    /**
     * Replace all flags with the given dates.<p>
     * 
     * NOTE: neither the given array nor any of its elements should be null.
     * Currently, a null array will be tolerated to ease migration. A null
     * has the same effect as clearFlaggedDates.
     * 
     *
     * @param flagged the dates to be flagged
     */
    public void setFlaggedDates(LocalDateTime... flagged) {
//        Contract.asNotNull(flagged, "must not be null");
        SortedSet<LocalDateTime> oldFlagged = flaggedDates.getSelection();
        flaggedDates.clearSelection();
        if (flagged != null) {
            for (LocalDateTime date : flagged) {
                flaggedDates.addSelectionInterval(date, date);
            }
        }
        firePropertyChange("flaggedDates", oldFlagged, flaggedDates.getSelection());
   }
    /**
     * Adds the dates to the flags. 
     * 
     * NOTE: neither the given array nor any of its elements should be null.
     * Currently, a null array will be tolerated to ease migration. A null
     * does nothing.
     *
     * @param flagged the dates to be flagged
     */
    public void addFlaggedDates(LocalDateTime... flagged) {
//        Contract.asNotNull(flagged, "must not be null");
        SortedSet<LocalDateTime> oldFlagged = flaggedDates.getSelection();
        if (flagged != null) {
            for (LocalDateTime date : flagged) {
                flaggedDates.addSelectionInterval(date, date);
            }
        }
        firePropertyChange("flaggedDates", oldFlagged, flaggedDates.getSelection());
    }
    
    /**
     * Unflags the given dates.
     * 
     * NOTE: neither the given array nor any of its elements should be null.
     * Currently, a null array will be tolerated to ease migration. 
     *
     * @param flagged the dates to be unflagged
     */
    public void removeFlaggedDates(LocalDateTime... flagged) {
//        Contract.asNotNull(flagged, "must not be null");
        SortedSet<LocalDateTime> oldFlagged = flaggedDates.getSelection();
        if (flagged != null) {
            for (LocalDateTime date : flagged) {
                flaggedDates.removeSelectionInterval(date, date);
            }
        }
        firePropertyChange("flaggedDates", oldFlagged, flaggedDates.getSelection());
    }
    /**
     * Clears all flagged dates.
     * 
     */
    public void clearFlaggedDates() {
        SortedSet<LocalDateTime> oldFlagged = flaggedDates.getSelection();
        flaggedDates.clearSelection();
        firePropertyChange("flaggedDates", oldFlagged, flaggedDates.getSelection());
    }
    
    /**
     * Returns a sorted set of flagged Dates. The returned set is guaranteed to
     * be not null, but may be empty.
     * 
     * @return a sorted set of flagged dates.
     */
    public SortedSet<LocalDateTime> getFlaggedDates() {
        return flaggedDates.getSelection();
    }

    /**
     * Returns a boolean indicating if this monthView has flagged dates.
     * 
     * @return a boolean indicating if this monthView has flagged dates.
     */
    public boolean hasFlaggedDates() {
        return !flaggedDates.isSelectionEmpty();
    }


//------------------- visual properties    
    /**
     * Sets a boolean property indicating whether or not to show leading dates
     * for a months displayed by this component.<p>
     * 
     * The default value is false.
     * 
     * @param value true if leading dates should be displayed, false otherwise.
     */
    public void setShowingLeadingDays(boolean value) {
        boolean old = isShowingLeadingDays();
        leadingDays = value;
        firePropertyChange("showingLeadingDays", old, isShowingLeadingDays());
    }

    /**
     * Returns a boolean indicating whether or not we're showing leading dates.
     *
     * @return true if leading dates are shown, false otherwise.
     */
    public boolean isShowingLeadingDays() {
        return leadingDays;
    }

    /**
     * Sets a boolean property indicating whether or not to show 
     * trailing dates for the months displayed by this component.<p>
     * 
     * The default value is false.
     *
     * @param value true if trailing dates should be displayed, false otherwise.
     */
    public void setShowingTrailingDays(boolean value) {
        boolean old = isShowingTrailingDays();
        trailingDays = value;
        firePropertyChange("showingTrailingDays", old, isShowingTrailingDays());
    }

    /**
     * Returns a boolean indicating whether or not we're showing trailing dates.
     *
     * @return true if trailing dates are shown, false otherwise.
     */
    public boolean isShowingTrailingDays() {
        return trailingDays;
    }
    
    /**
     * Returns whether or not the month view supports traversing months.
     * If zoomable is enabled, traversable is enabled as well. Otherwise
     * returns the traversable property as set by client code.
     * 
     * @return <code>true</code> if month traversing is enabled.
     * @see #setZoomable(boolean)
     */
    public boolean isTraversable() {
        if (isZoomable()) return true;
        return traversable;
    }

    /**
     * Set whether or not the month view will display buttons to allow the user
     * to traverse to previous or next months. <p>
     * 
     * The default value is false. <p>
     * 
     * PENDING JW: fire the "real" property or the compound with zoomable?
     * 
     * @param traversable set to true to enable month traversing, false
     *        otherwise.
     * @see #isTraversable()       
     * @see #setZoomable(boolean)
     */
    public void setTraversable(boolean traversable) {
        boolean old = isTraversable();
        this.traversable = traversable;
        firePropertyChange(TRAVERSABLE, old, isTraversable());
    }

    /**
     * Returns true if zoomable (through date ranges).
     * 
     * @return true if zoomable is enabled.
     * @see #setZoomable(boolean)
     */
    public boolean isZoomable() {
        return zoomable;
    }

    /**
     * Sets the zoomable property. If true, the calendar's date range can
     * be zoomed. This state implies that the calendar is traversable and
     * showing exactly one calendar box, effectively ignoring the properties.
     * 
     * @param zoomable a boolean indicating whether or not zooming date
     *    ranges is enabled.
     *    
     * @see #setTraversable(boolean)
     */
    public void setZoomable(boolean zoomable) {
        boolean old = isZoomable();
        this.zoomable = zoomable;
        firePropertyChange("zoomable", old, isZoomable());
    }

    /**
     * Returns whether or not this <code>JXMonthView</code> should display
     * week number.
     *
     * @return <code>true</code> if week numbers should be displayed
     */
    public boolean isShowingWeekNumber() {
        return showWeekNumber;
    }

    /**
     * Set whether or not this <code>JXMonthView</code> will display week
     * numbers or not.
     *
     * @param showWeekNumber true if week numbers should be displayed,
     *        false otherwise
     */
    public void setShowingWeekNumber(boolean showWeekNumber) {
        boolean old = isShowingWeekNumber();
        this.showWeekNumber = showWeekNumber;
        firePropertyChange("showingWeekNumber", old, isShowingWeekNumber());
    }

    /**
     * Sets the String representation for each day of the week as used
     * in the header of the day's grid. For
     * this method the first days of the week days[0] is assumed to be
     * <code>Calendar.SUNDAY</code>. If null, the representation provided
     * by the MonthViewUI is used.
     * 
     * The default value is the representation as 
     * returned from the MonthViewUI.
     * 
     * @param days Array of characters that represents each day
     * @throws IllegalArgumentException if not null and <code>days.length</code> !=
     *         DAYS_IN_WEEK
     */
//    public void setDaysOfTheWeek(String[] days) {
//        if ((days != null) && (days.length != DAYS_IN_WEEK)) {
//            throw new IllegalArgumentException(
//                    "Array of days is not of length " + DAYS_IN_WEEK
//                            + " as expected.");
//        }
//
//        String[] oldValue = getDaysOfTheWeek();
//        _daysOfTheWeek = days;
//        firePropertyChange(DAYS_OF_THE_WEEK, oldValue, days);
//    }

    /**
     * Returns the String representation for each day of the
     * week. 
     *
     * @return String representation for the days of the week, guaranteed to
     *   never be null.
     *   
     * @see #setDaysOfTheWeek(String[])
     * @see MonthViewUI  
     */
//    public String[] getDaysOfTheWeek() {
//        if (_daysOfTheWeek != null) {
//            String[] days = new String[DAYS_IN_WEEK];
//            System.arraycopy(_daysOfTheWeek, 0, days, 0, DAYS_IN_WEEK);
//            return days;
//        } 
//        return getUI().getDaysOfTheWeek();
//    }

    /**
     * 
     * @param dayOfWeek
     * @return String representation of day of week.
     */
//    public String getDayOfTheWeek(int dayOfWeek) {
//        return getDaysOfTheWeek()[dayOfWeek - 1];
//    }
    
    
    /**
     * Returns the String represention of the day of week.
     * 
     * PENDING: need to re-visit - with the renderer approach in
     * place this should be removed? Config is done via the renderer's
     * StringValue.
     * 
     */
    public String getDayOfTheWeekText(DayOfWeek day) {
        return getDayOfWeekTexts().get(day);
    }
    
    /**
     * Return the String representation for all days of the week.
     * This implementation returns the Map as returned by the MonthViewUI.
     * 
     * @return
     */
    protected Map<DayOfWeek, String> getDayOfWeekTexts() {
        return getUI().getDaysOfTheWeekTexts();
    }
    
    /**
     * Returns the padding used between days in the calendar.
     *
     * @return Padding used between days in the calendar
     */
    public int getBoxPaddingX() {
        return boxPaddingX;
    }

    /**
     * Sets the number of pixels used to pad the left and right side of a day.
     * The padding is applied to both sides of the days.  Therefore, if you
     * used the padding value of 3, the number of pixels between any two days
     * would be 6.
     *
     * @param boxPaddingX Number of pixels applied to both sides of a day
     */
    public void setBoxPaddingX(int boxPaddingX) {
        int oldBoxPadding = getBoxPaddingX();
        this.boxPaddingX = boxPaddingX;
        firePropertyChange(BOX_PADDING_X, oldBoxPadding, getBoxPaddingX());
    }

    /**
     * Returns the padding used above and below days in the calendar.
     *
     * @return Padding used between dats in the calendar
     */
    public int getBoxPaddingY() {
        return boxPaddingY;
    }

    /**
     * Sets the number of pixels used to pad the top and bottom of a day.
     * The padding is applied to both the top and bottom of a day.  Therefore,
     * if you used the padding value of 3, the number of pixels between any
     * two days would be 6.
     *
     * @param boxPaddingY Number of pixels applied to top and bottom of a day
     */
    public void setBoxPaddingY(int boxPaddingY) {
        int oldBoxPadding = getBoxPaddingY();
        this.boxPaddingY = boxPaddingY;
        firePropertyChange(BOX_PADDING_Y, oldBoxPadding, getBoxPaddingY());
    }


    /**
     * Returns the selected background color.
     *
     * @return the selected background color.
     */
    public Color getSelectionBackground() {
        return selectedBackground;
    }

    /**
     * Sets the selected background color to <code>c</code>.  The default color
     * is installed by the ui.
     *
     * @param c Selected background.
     */
    public void setSelectionBackground(Color c) {
        Color old = getSelectionBackground();
        selectedBackground = c;
        firePropertyChange("selectionBackground", old, getSelectionBackground());
    }

    /**
     * Returns the selected foreground color.
     *
     * @return the selected foreground color.
     */
    public Color getSelectionForeground() {
        return selectedForeground;
    }

    /**
     * Sets the selected foreground color to <code>c</code>.  The default color
     * is installed by the ui.
     *
     * @param c Selected foreground.
     */
    public void setSelectionForeground(Color c) {
        Color old = getSelectionForeground();
        selectedForeground = c;
        firePropertyChange("selectionForeground", old, getSelectionForeground());
    }


    /**
     * Returns the color used when painting the today background.
     *
     * @return Color Color
     */
    public Color getTodayBackground() {
        return todayBackgroundColor;
    }

    /**
     * Sets the color used to draw the bounding box around today.  The default
     * is the background of the <code>JXMonthView</code> component.
     *
     * @param c color to set
     */
    public void setTodayBackground(Color c) {
        Color oldValue = getTodayBackground();
        todayBackgroundColor = c;
        firePropertyChange("todayBackground", oldValue, getTodayBackground());
        // PENDING JW: remove repaint, ui must take care of it
        repaint();
    }

    /**
     * Returns the color used to paint the month string background.
     *
     * @return Color Color.
     */
    public Color getMonthStringBackground() {
        return monthStringBackground;
    }

    /**
     * Sets the color used to draw the background of the month string.  The
     * default is <code>138, 173, 209 (Blue-ish)</code>.
     *
     * @param c color to set
     */
    public void setMonthStringBackground(Color c) {
        Color old = getMonthStringBackground();
        monthStringBackground = c;
        firePropertyChange("monthStringBackground", old, getMonthStringBackground());
        // PENDING JW: remove repaint, ui must take care of it
        repaint();
    }

    /**
     * Returns the color used to paint the month string foreground.
     *
     * @return Color Color.
     */
    public Color getMonthStringForeground() {
        return monthStringForeground;
    }

    /**
     * Sets the color used to draw the foreground of the month string.  The
     * default is <code>Color.WHITE</code>.
     *
     * @param c color to set
     */
    public void setMonthStringForeground(Color c) {
        Color old = getMonthStringForeground();
        monthStringForeground = c;
        firePropertyChange("monthStringForeground", old, getMonthStringForeground());
        // PENDING JW: remove repaint, ui must take care of it
        repaint();
    }

    /**
     * Sets the color used to draw the foreground of each day of the week. These
     * are the titles
     *
     * @param c color to set
     */
    public void setDaysOfTheWeekForeground(Color c) {
        Color old = getDaysOfTheWeekForeground();
        daysOfTheWeekForeground = c;
        firePropertyChange("daysOfTheWeekForeground", old, getDaysOfTheWeekForeground());
    }

    /**
     * @return Color Color
     */
    public Color getDaysOfTheWeekForeground() {
        return daysOfTheWeekForeground;
    }

    /**
     * Set the color to be used for painting the specified day of the week.
     * Acceptable values are Calendar.SUNDAY - Calendar.SATURDAY. <p>
     * 
     * PENDING JW: this is not a property - should it be and 
     * fire a change notification? If so, how?
     * 
     *
     * @param dayOfWeek constant value defining the day of the week.
     * @param c         The color to be used for painting the numeric day of the week.
     */
    public void setDayForeground(DayOfWeek dayOfWeek, Color c) {
        dayToColorTable.put(dayOfWeek, c);
        repaint();
    }

    /**
     * Return the color that should be used for painting the numerical day of the week.
     *
     * @param dayOfWeek The day of week to get the color for.
     * @return The color to be used for painting the numeric day of the week.
     *         If this was no color has yet been defined the component foreground color
     *         will be returned.
     */
    public Color getDayForeground(DayOfWeek dayOfWeek) {
        Color c;
        c = dayToColorTable.get(dayOfWeek);
        if (c == null) {
            c = getForeground();
        }
        return c;
    }

    /**
     * Set the color to be used for painting the foreground of a flagged day.
     *
     * @param c The color to be used for painting.
     */
    public void setFlaggedDayForeground(Color c) {
        Color old = getFlaggedDayForeground();
        flaggedDayForeground = c;
        firePropertyChange("flaggedDayForeground", old, getFlaggedDayForeground());
    }

    /**
     * Return the color that should be used for painting the foreground of the flagged day.
     *
     * @return The color to be used for painting
     */
    public Color getFlaggedDayForeground() {
        return flaggedDayForeground;
    }

    /**
     * Returns a copy of the insets used to paint the month string background.
     *
     * @return Insets Month string insets.
     */
    public Insets getMonthStringInsets() {
        return (Insets) _monthStringInsets.clone();
    }

    /**
     * Insets used to modify the width/height when painting the background
     * of the month string area.
     *
     * @param insets Insets
     */
    public void setMonthStringInsets(Insets insets) {
        Insets old = getMonthStringInsets();
        if (insets == null) {
            _monthStringInsets.top = 0;
            _monthStringInsets.left = 0;
            _monthStringInsets.bottom = 0;
            _monthStringInsets.right = 0;
        } else {
            _monthStringInsets.top = insets.top;
            _monthStringInsets.left = insets.left;
            _monthStringInsets.bottom = insets.bottom;
            _monthStringInsets.right = insets.right;
        }
        firePropertyChange("monthStringInsets", old, getMonthStringInsets());
        // PENDING JW: remove repaint, ui must take care of it
        repaint();
    }

    /**
     * Returns the preferred number of columns to paint calendars in. 
     * <p>
     * @return int preferred number of columns of calendars.
     * 
     * @see #setPreferredColumnCount(int)
     */
    public int getPreferredColumnCount() {
        return minCalCols;
    }

    /**
     * Sets the preferred number of columns of calendars. Does nothing if cols
     * <= 0. The default value is 1.
     * <p>
     * @param cols The number of columns of calendars.
     * 
     * @see #getPreferredColumnCount()
     */
    public void setPreferredColumnCount(int cols) {
        if (cols <= 0) {
            return;
        }
        int old = getPreferredColumnCount();
        minCalCols = cols;
        firePropertyChange("preferredColumnCount", old, getPreferredColumnCount());
        // PENDING JW: remove revalidate/repaint, ui must take care of it
        revalidate();
        repaint();
    }
    

    /**
     * Returns the preferred number of rows to paint calendars in.
     * <p>
     * @return int Rows of calendars.
     * 
     * @see #setPreferredRowCount(int)
     */
    public int getPreferredRowCount() {
        return minCalRows;
    }

    /**
     * Sets the preferred number of rows to paint calendars.Does nothing if rows
     * <= 0. The default value is 1.
     * <p>
     *
     * @param rows The number of rows of calendars.
     * 
     * @see #getPreferredRowCount()
     */
    public void setPreferredRowCount(int rows) {
        if (rows <= 0) {
            return;
        }
        int old = getPreferredRowCount();
        minCalRows = rows;
        firePropertyChange("preferredRowCount", old, getPreferredRowCount());
        // PENDING JW: remove revalidate/repaint, ui must take care of it
        revalidate();
        repaint();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
//        todayTimer.stop();
        super.removeNotify();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() {
        super.addNotify();

        // Setup timer to update the value of today.
//        int secondsTillTomorrow = 86400;
//
//        if (todayTimer == null) {
//            todayTimer = new Timer(secondsTillTomorrow * 1000,
//                    new ActionListener() {
//                        public void actionPerformed(ActionEvent e) {
//                            incrementToday();
//                        }
//                    });
//        }

        // Modify the initial delay by the current time.
//        cal.setTimeInMillis(System.currentTimeMillis());
//        secondsTillTomorrow = secondsTillTomorrow -
//                (cal.get(Calendar.HOUR_OF_DAY) * 3600) -
//                (cal.get(Calendar.MINUTE) * 60) -
//                cal.get(Calendar.SECOND);
//        todayTimer.setInitialDelay(secondsTillTomorrow * 1000);
//        todayTimer.start();
    }

//-------------------- action and listener
    

    /**
     * Commits the current selection. <p>
     * 
     * Resets the model's adjusting property to false
     * and fires an ActionEvent
     * with the COMMIT_KEY action command.
     * 
     * <p>PENDING: define what "commit selection" means ... currently
     * only fires (to keep the picker happy).
     * 
     * @see #cancelSelection()
     * @see org.jdesktop.swingx.calendar.Date310SelectionModel#setAdjusting(boolean)
     */
    public void commitSelection() {
        getSelectionModel().setAdjusting(false);
        fireActionPerformed(COMMIT_KEY);
    }

    /**
     * Cancels the selection. <p>
     * 
     * Resets the model's adjusting to 
     * false and fires an ActionEvent with the CANCEL_KEY action command.
     * 
     * @see #commitSelection
     * @see org.jdesktop.swingx.calendar.Date310SelectionModel#setAdjusting(boolean)
     */
    public void cancelSelection() {
        getSelectionModel().setAdjusting(false);
        fireActionPerformed(CANCEL_KEY);
        
    }

    /**
     * Sets the component input map enablement property.<p>
     * 
     * If enabled, the keybinding for WHEN_IN_FOCUSED_WINDOW are
     * installed, otherwise not. Changing this property will
     * install/clear the corresponding key bindings. Typically, clients 
     * which want to use the monthview in a popup, should enable these.<p>
     * 
     * The default value is false.
     * 
     * @param enabled boolean to indicate whether the component
     *   input map should be enabled.
     * @see #isComponentInputMapEnabled()  
     */
    public void setComponentInputMapEnabled(boolean enabled) {
        boolean old = isComponentInputMapEnabled();
        this.componentInputMapEnabled = enabled;
        firePropertyChange("componentInputMapEnabled", old, isComponentInputMapEnabled());
    }

    /**
     * Returns the componentInputMapEnabled property.
     * 
     * @return a boolean indicating whether the component input map is 
     *   enabled.
     * @see #setComponentInputMapEnabled(boolean)  
     *   
     */
    public boolean isComponentInputMapEnabled() {
        return componentInputMapEnabled;
    }

    /**
     * Adds an ActionListener.
     * <p/>
     * The ActionListener will receive an ActionEvent when a selection has
     * been made.
     *
     * @param l The ActionListener that is to be notified
     */
    public void addActionListener(ActionListener l) {
        listenerMap.add(ActionListener.class, l);
    }

    /**
     * Removes an ActionListener.
     *
     * @param l The action listener to remove.
     */
    public void removeActionListener(ActionListener l) {
        listenerMap.remove(ActionListener.class, l);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        java.util.List<T> listeners = listenerMap.getListeners(listenerType);
        T[] result;
        if (!listeners.isEmpty()) {
            //noinspection unchecked
            result = (T[]) java.lang.reflect.Array.newInstance(listenerType, listeners.size());
            result = listeners.toArray(result);
        } else {
            result = super.getListeners(listenerType);
        }
        return result;
    }

    /**
     * Creates and fires an ActionEvent with the given action 
     * command to all listeners.
     * 
     * @param actionCommand the command for the created.
     */
    protected void fireActionPerformed(String actionCommand) {
        ActionListener[] listeners = getListeners(ActionListener.class);
        ActionEvent e = null;

        for (ActionListener listener : listeners) {
            if (e == null) {
                e = new ActionEvent(JXMonthView.this,
                        ActionEvent.ACTION_PERFORMED,
                        actionCommand);
            }
            listener.actionPerformed(e);
        }
    }


//--- deprecated code - NOTE: these methods will be removed soon! 

    /**
     * Returns true if anti-aliased text is enabled for this component, false
     * otherwise.
     *
     * @return boolean <code>true</code> if anti-aliased text is enabled,
     * <code>false</code> otherwise.
     * 
     * @deprecated will be removed without replacement to align with core Swing 
     * which api for setting per-instance antialiased property. No longer used
     * by the ui delegate.
     */
    @Deprecated
    public boolean isAntialiased() {
        return antialiased;
    }

    /**
     * Turns on/off anti-aliased text for this component.
     *
     * @param antiAlias <code>true</code> for anti-aliased text,
     * <code>false</code> to turn it off.
     * 
     * @deprecated will be removed without replacement to align with core Swing 
     * which api for setting per-instance antialiased property. 
     */
    @Deprecated
    public void setAntialiased(boolean antiAlias) {
        if (this.antialiased == antiAlias) {
            return;
        }
        this.antialiased = antiAlias;
        firePropertyChange("antialiased", !this.antialiased, this.antialiased);
    }

    /**
     * Returns the selected background color.
     *
     * @return the selected background color.
     * 
     * @deprecated use {@link #getSelectionBackground()} renamed for 
     *   cross-component consistency.
     */
    @Deprecated
    public Color getSelectedBackground() {
        return getSelectionBackground();
    }

    /**
     * Sets the selected background color to <code>c</code>.  The default color
     * is <code>138, 173, 209 (Blue-ish)</code>
     *
     * @param c Selected background.
     * 
     * @deprecated use {@link #setSelectionBackground(Color)} renamed for
     *   cross-component consistency.
     */
    @Deprecated
    public void setSelectedBackground(Color c) {
        setSelectionBackground(c);
    }


    /**
     * Returns the preferred number of columns to paint calendars in. 
     * <p>
     * PENDING JW: rename to a "full" name preferredColumnCount
     * @return int Columns of calendars.
     * 
     * @deprecated use {@link #getPreferredColumnCount()}
     */
    @Deprecated
    public int getPreferredCols() {
        return minCalCols;
    }

    /**
     * The preferred number of columns to paint calendars.
     * <p>
     * PENDING JW: rename to a "full" name preferredColumnCount
     *   and make bound property
     * @param cols The number of columns of calendars.
     * 
     * @deprecated use {@link #setPreferredColumnCount(int)}
     */
    @Deprecated
    public void setPreferredCols(int cols) {
        if (cols <= 0) {
            return;
        }
        minCalCols = cols;
        revalidate();
        repaint();
    }

    /**
     * Returns the preferred number of rows to paint calendars in.
     * <p>
     * PENDING JW: rename to a "full" name preferredRowCount
     *  or maybe visibleRowCount to be consistent with JXTable/JXList 
     * @return int Rows of calendars.
     * 
     * @deprecated use {@link #getPreferredRowCount()}
     */
    @Deprecated
    public int getPreferredRows() {
        return minCalRows;
    }

    /**
     * Sets the preferred number of rows to paint calendars.
     * <p>
     * PENDING JW: rename to a "full" name preferredRowCount
     *   and make bound property
     *
     * @param rows The number of rows of calendars.
     * 
     * @deprecated use {@link #setPreferredRowCount(int)}
     */
    @Deprecated
    public void setPreferredRows(int rows) {
        if (rows <= 0) {
            return;
        }
        minCalRows = rows;
        revalidate();
        repaint();
    }







}
