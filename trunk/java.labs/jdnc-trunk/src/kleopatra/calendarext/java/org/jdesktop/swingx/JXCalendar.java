/*
 * $Id: JXCalendar.java 3427 2009-08-03 11:27:12Z kleopatra $
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
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;

import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;
import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.swingx.calendar.RendererHandler;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.event.DateSelectionListener;
import org.jdesktop.swingx.event.EventListenerMap;
import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
import org.jdesktop.swingx.plaf.CalendarAddon;
import org.jdesktop.swingx.plaf.CalendarUI;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.Contract;


/**
 * Component that displays a zoomable calendar which can be used to select a 
 * single day, similar to Vista date component.
 * <p>
 * 
 * 
 * @author Joshua Outwater
 * @author Jeanette Winzenburg
 * @version  $Revision: 3427 $
 */
public class JXCalendar extends JComponent {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(JXCalendar.class
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

    static {
        LookAndFeelAddons.contribute(new CalendarAddon());
    }

     /**
     * UI Class ID
     */
    public static final String uiClassID = "CalendarUI";

    public static final int DAYS_IN_WEEK = 7;
    public static final int MONTHS_IN_YEAR = 12;

//------------------ auto-scroll
    /**
     * Keeps track of the first date we are displaying.  Legacy? 
     */
    private Date firstDisplayedDay;
    
    /**
     * Keeps track of the current pagetype - use in notification only
     */
    private FieldType pageType;
    
    /** 
     * The calendar to base all selections and calendar-related upon. 
     * NOTE: the time of this calendar is undefined - before using, internal
     * code must explicitly set it.
     * PENDING JW: as of version 1.26 all calendar/properties are controlled by the model.
     * We keep a clone of the model's calendar here for notification reasons: 
     * model fires DateSelectionEvent of type CALENDAR_CHANGED which neither carry the
     * oldvalue nor the property name needed to map into propertyChange notification.
     * Since JXCalendar, this is used for internal book-keeping only.
     */
    private Calendar cal;
//    /** 
//     * Start of the day which contains System.millis() in the current calendar.
//     * Kept in synch via a timer started in addNotify.
//     */
//    private Date today;
//    /**
//     * The timer used to keep today in synch with system time.
//     */
//    private Timer todayTimer;
    // PENDING JW: why kept apart from cal? Why writable? - shouldn't the calendar have complete
    // control?
    private int firstDayOfWeek;
    //-------------- selection
    /** 
     * The DateSelectionModel driving this component. This model's calendar
     * is the reference for all dates.
     */
    private DateSelectionModel model;
    /**
     * Listener registered with the current model to keep Calendar dependent
     * state synched.
     */
    private DateSelectionListener modelListener;
    /**
     * Storage of actionListeners registered with the calendarView.
     */
    private EventListenerMap listenerMap;
    
    private boolean showWeekNumber;
    private boolean componentInputMapEnabled;
    
    
    //------------- visuals
    

    protected Insets _monthStringInsets = new Insets(0, 0, 0, 0);
    private int boxPaddingX;
    private int boxPaddingY;
    private Color todayBackgroundColor;
    private Color monthStringBackground;
    private Color monthStringForeground;
    private Color selectedBackground;
    private Color selectedForeground;
    private RendererHandler rendererHandler;

    /**
     * Create a new instance of the <code>JXCalendar</code> class using the
     * default Locale. 
     */
    public JXCalendar() {
        this(null, null);
    }

    /**
     * Create a new instance of the <code>JXCalendar</code> class using the 
     * default Locale and the current system time as the first date to 
     * display.
     * 
     * @param locale desired locale, if null the system default locale is used
     */
    public JXCalendar(Locale locale) {
        this(null, locale);
    }

    /**
     * Create a new instance of the <code>JXCalendar</code> class using the
     * default Locale and the given Date as selected. 
     *
     * @param selectionDate the Date to select, maybe null.
     */
    public JXCalendar(Date selectionDate) {
        this(selectionDate, null);
    }

    /**
     * Create a new instance of the <code>JXCalendar</code> class with the given
     * Date as selection and the given Locale.
     *
     * @param selectionDate the Date to select, maybe null.
     * @param locale desired locale, if null the system default locale is used
     */
    public JXCalendar(Date selectionDate, Locale locale) {
        this(null, selectionDate, locale);
    }
    
    /**
     * Create a new instance of the <code>JXCalendar</code> class using the
     * default Locale, the given selection model. 
     * 
     * @param selectionDate the initially selected date, may be null
     * @param model the selection model to use, if null a <code>DaySelectionModel</code> is
     *   created.
     */
    public JXCalendar(DateSelectionModel model) {
        this(model, null, null);
    }


    /**
     * Create a new instance of the <code>JXCalendar</code> class using the
     * given Locale, the given time as selectionDate to 
     * display and the given selection model. <p>
     * 
     * Note: the assumption is that either the model is given (rules-it-all) 
     * or the selectionDate/Locale which creates a default model and configures
     * it with the given params. 
     * 
     * PENDING JW: change constructors parameters - either have a selectionDate or a model, not
     *   both.
     * @param model the selection model to use, if null a <code>DefaultSelectionModel</code> is
     *   created.
     * @param selectionDate the initially selected date, may be null
     * @param locale desired locale, if null the system default locale is used
     * 
     * @throws IllegalStateException if the model it != null and one of the other params is
     *   != null
     */
    private JXCalendar(DateSelectionModel model, Date selectionDate, Locale locale) {
        if ((model != null) && (locale != null || selectionDate != null)) {
            throw new IllegalStateException("shouldn't happen: if model is given, the " +
                        "convenience params must be null, but was " +
                        "\n model/date/locale: " + model + " / " + selectionDate + " / " + locale);
        }
        listenerMap = new EventListenerMap();

        initModel(model, locale);
//        superSetLocale(locale);
        if (selectionDate != null)
            setSelectionDate(selectionDate);
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
     * Returns a Calendar representing the current page, which is controlled
     * by the ui-delegate. The Calendar's date is set to start of the period
     * shown in the page.
     * 
     * @return a Calendar representing th current page.
     * @throws IllegalStateException if called before instantitation is completed
     */
    public Calendar getPage() {
        // JW: this is to guard against a regression of not-fully understood 
        // problems in constructor (UI used to call back into this before we were ready)
        if (cal == null) throw 
            new IllegalStateException("must not be called before instantiation is complete");
        return getUI().getPage();
    }

    /**
     * Returns the field type of the current page.
     * 
     * @return the field type of the current page.
     */
    public FieldType getPageType() {
        return getUI().getPageType();
    }
    /**
     * Gets the time zone.
     *
     * @return The <code>TimeZone</code> used by the <code>JXCalendar</code>.
     */
    public TimeZone getTimeZone() {
        return model.getTimeZone();
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
    public int getFirstDayOfWeek() {
        return model.getFirstDayOfWeek(); //firstDayOfWeek;
    }

    /**
     * Sets what the first day of the week is; e.g.,
     * <code>Calendar.SUNDAY</code> in US, <code>Calendar.MONDAY</code>
     * in France.
     *
     * @param firstDayOfWeek The first day of the week.
     * @see java.util.Calendar
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        getSelectionModel().setFirstDayOfWeek(firstDayOfWeek);
    }



//---------------------- synch to model's calendar    

    /**
     * Initializes selection model related internals. If the model
     * is null it creates a default model with the locale. <p>
     * 
     * @param model the DateSelectionModel which should drive the calendarView. 
     *    If null, a default model is created and initialized with the given locale.
     * @param locale the Locale to use with the selectionModel. 
     */
    private void initModel(DateSelectionModel model, Locale locale) {
        if (model == null) {
            model = new DaySelectionModel(locale);
        }
        this.model = model;
        superSetLocale(model.getLocale());
        installCalendar();
        model.addDateSelectionListener(getDateSelectionListener());
    }

    /**
     * Lazily creates and returns the DateSelectionListener which listens
     * for model's calendar properties.
     * 
     * @return a DateSelectionListener for model's CALENDAR_CHANGED notification.
     */
    private DateSelectionListener getDateSelectionListener() {
        if (modelListener == null) {
            modelListener = new DateSelectionListener() {

                public void valueChanged(DateSelectionEvent ev) {
                    if (EventType.CALENDAR_CHANGED.equals(ev.getEventType())) {
                        updateCalendar();
                    }
                    
                }
                
            };
        }
        return modelListener;
    }

    /**
     * Installs the internal calendars from the selection model.<p>
     * 
     * PENDING JW: in fixing #11433, added update of firstDisplayedDay and
     * today here - check if correct place to do so. 
     * 
     */
    private void installCalendar() {
        cal = model.getCalendar();
        firstDayOfWeek = cal.getFirstDayOfWeek();
        getRendererHandler().setLocale(model.getLocale());
    }


    /**
     * Callback from selection model calendar changes.
     */
    private void updateCalendar() {
       if (!getLocale().equals(model.getLocale())) {
           installCalendar();
           superSetLocale(model.getLocale());
       } else {
           if (!model.getTimeZone().equals(cal.getTimeZone())) {
               updateTimeZone();
           }
           if (cal.getMinimalDaysInFirstWeek() != model.getMinimalDaysInFirstWeek()) {
               updateMinimalDaysOfFirstWeek();
           }
           if (cal.getFirstDayOfWeek() != model.getFirstDayOfWeek()) {
              updateFirstDayOfWeek(); 
           }
       }
    }


    /**
     * Callback from changing timezone in model.
     */
    private void updateTimeZone() {
        TimeZone old = cal.getTimeZone();
        TimeZone tz = model.getTimeZone();
        cal.setTimeZone(tz);
        firePropertyChange("timeZone", old, cal.getTimeZone());
    }
    
    
    /**
     * Call back from listening to model firstDayOfWeek change.
     */
    private void updateFirstDayOfWeek() {
        int oldFirstDayOfWeek = this.firstDayOfWeek;

        firstDayOfWeek = getSelectionModel().getFirstDayOfWeek();
        cal.setFirstDayOfWeek(firstDayOfWeek);
        firePropertyChange("firstDayOfWeek", oldFirstDayOfWeek, firstDayOfWeek);
    }

    /**
     * Call back from listening to model minimalDaysOfFirstWeek change.
     * <p>
     * NOTE: this is not a property as we have no public api to change
     * it on JXCalendar.
     */
    private void updateMinimalDaysOfFirstWeek() {
        cal.setMinimalDaysInFirstWeek(model.getMinimalDaysInFirstWeek());
    }


    
//-------------------- scrolling
    
    
    /**
     * Set the first displayed date. <p>
     * 
     * PENDING JW: used internally only to fire change notification for the sake
     * of internal listeners which need to update themselves. Revisit after 
     * synch of calendars in nav/selectionModel to see if we still need it for 
     * TimeZone.
     *
     * @param date The first displayed date.
     */
    private void setFirstDisplayedDay(Date date) {
        Date oldDate = firstDisplayedDay;
        cal.setTime(date);
        firstDisplayedDay = cal.getTime();
        firePropertyChange("firstDisplayedDay", oldDate, firstDisplayedDay);
    }

    /**
     * Updates internal state to ensure that the current page is visible.
     * Notifies listener about changes to properties <code>firstDisplayedDay</code> and/or
     * <code>pageType</code>, as appropriate. <p>
     * 
     * NOTE: this is called by the ui automatically, client code should have
     * no need to use.
     */
    public void ensurePageVisible() {
        setFirstDisplayedDay(getUI().getPage().getTime());
        FieldType old = pageType;
        pageType = getUI().getPageType();
        firePropertyChange("pageType", old, pageType);
    }


    /**
     * Returns the Date at the given location. May be null if the
     * coordinates don't map to a Date in the current page.
     * 
     * Mapping pixel to calendar day.
     *
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the date at the given location or null if the location
     *   doesn't map to a a Date in the current page
     */ 
    public Calendar getCellAtLocation(int x, int y) {
        return getUI().getCellAtLocation(x, y);
    }
   
//------------------ today


    /**
     * Returns today properties (whateverthatmeans). 
     * 
     * @return  today as Date.
     */
    public Date getToday() {
        return new Date(System.currentTimeMillis());
    }

    public boolean isToday(Date date) {
        cal.setTime(getToday());
        return CalendarUtils.isSameDay(cal, date);
    }
//---------------- DateSelectionModel

    /**
     * Returns the date selection model which drives this
     * JXCalendar.
     * 
     * @return the date selection model
     */
    public DateSelectionModel getSelectionModel() {
        return model;
    }

    /**
     * Sets the date selection model to drive this calendarView.
     * 
     * @param model the selection model to use, must not be null.
     * @throws NullPointerException if model is null
     */
    public void setSelectionModel(DateSelectionModel model) {
        Contract.asNotNull(model, "date selection model must not be null");
        DateSelectionModel oldModel = getSelectionModel();
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
   public SortedSet<Date> getSelection() {
        return getSelectionModel().getSelection();
    }

    /**
     * Adds the selection interval to the selection model. 
     * 
     * @param startDate Start of date range to add to the selection
     * @param endDate End of date range to add to the selection
     */
    public void addSelectionInterval(Date startDate, Date endDate) {
            getSelectionModel().addSelectionInterval(startDate, endDate);
    }

    /**
     * Sets the selection interval to the selection model.  
     *
     * @param startDate Start of date range to set the selection to
     * @param endDate End of date range to set the selection to
     */
    public void setSelectionInterval(final Date startDate, final Date endDate) {
            getSelectionModel().setSelectionInterval(startDate, endDate);
    }

    /**
     * Removes the selection interval from the selection model.  
     * 
     * @param startDate Start of the date range to remove from the selection
     * @param endDate End of the date range to remove from the selection
     */
    public void removeSelectionInterval(final Date startDate, final Date endDate) {
        getSelectionModel().removeSelectionInterval(startDate, endDate);
    }

    /**
     * Returns the current selection mode for this JXCalendar.
     *
     * @return int Selection mode.
     */
    public SelectionMode getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }

    /**
     * Set the selection mode for this JXCalendar.

     * @param selectionMode The selection mode to use for this {@code JXCalendar}
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        getSelectionModel().setSelectionMode(selectionMode);
    }

    /**
     * Returns the earliest selected date. 
     * 
     *   
     * @return the first Date in the selection or null if empty.
     */
    public Date getFirstSelectionDate() {
        return getSelectionModel().getFirstSelectionDate();    
     }
   

    /**
     * Returns the earliest selected date. 
     * 
     * @return the first Date in the selection or null if empty.
     */
    public Date getLastSelectionDate() {
        return getSelectionModel().getLastSelectionDate();    
     }

    /**
     * Returns the earliest selected date. 
     * 
     * PENDING JW: keep this? it was introduced before the first/last 
     *   in model. When delegating everything, we duplicate here.
     *   
     * @return the first Date in the selection or null if empty.
     */
    public Date getSelectionDate() {
        return getFirstSelectionDate();    
    }

    /**
     * Sets the model's selection to the given date or clears the selection if
     * null.
     * 
     * @param newDate the selection date to set
     */
    public void setSelectionDate(Date newDate) {
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
    public boolean isSelected(Date date) {
        return getSelectionModel().isSelected(date);
    }


    /**
     * Set the lower bound date that is allowed to be selected. <p>
     * 
     * 
     * @param lowerBound the lower bound, null means none.
     */
    public void setLowerBound(Date lowerBound) {
        getSelectionModel().setLowerBound(lowerBound);
    }

    /**
     * Set the upper bound date that is allowed to be selected. <p>
     * 
     * @param upperBound the upper bound, null means none.
     */
    public void setUpperBound(Date upperBound) {
        getSelectionModel().setUpperBound(upperBound);
    }


    /**
     * Return the lower bound date that is allowed to be selected for this
     * model.
     *
     * @return lower bound date or null if not set
     */
    public Date getLowerBound() {
        return getSelectionModel().getLowerBound();
    }

    /**
     * Return the upper bound date that is allowed to be selected for this
     * model.
     *
     * @return upper bound date or null if not set
     */
    public Date getUpperBound() {
        return getSelectionModel().getUpperBound();
    }

    /**
     * Identifies whether or not the date passed is an unselectable date.
     * <p>
     * 
     * @param date date which to test for unselectable status
     * @return true if the date is unselectable, false otherwise
     */
    public boolean isUnselectableDate(Date date) {
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
    public void setUnselectableDates(Date... unselectableDates) {
        Contract.asNotNull(unselectableDates,
                "unselectable dates must not be null");
        SortedSet<Date> unselectableSet = new TreeSet<Date>();
        for (Date unselectableDate : unselectableDates) {
            unselectableSet.add(unselectableDate);
        }
        getSelectionModel().setUnselectableDates(unselectableSet);
        // PENDING JW: check that ui does the repaint!
        repaint();
    }

//--------------------- renderer/highlighter    

    /**
     * Returns a component configured to render the cell with the given date and state.
     * This methods can be called on any state of the calendarView (doesn't access
     * the "life" component or guards against malfunction), typically by a LayoutManager
     * to measure the cell dimensions for layout.
     * 
     * @param value
     * @param state
     * @return a component to render the cell at the given coordinates, guaranteed to be 
     *   not null. 
     */
    public JComponent prepareRendererComponent(Calendar value, CalendarCellState dayState) {
        return getRendererHandler().prepareRendererComponent(value, dayState);
    }


    /**
     * Returns a component configured to render the cell at the given coordinates
     * on the current page.<p>
     * 
     * PENDING JW: the calendar param is redundant - but not yet all values supported
     * in PageAdapter (missing week/day header values). Revisit and remove if that is 
     * done.
     * 
     * @param row the row index of the cell to configure, must be valid on the 
     *    current page
     * @param column the column index of the cell to configure, must be valid on the
     *    current page
     * @return a component to render the cell at the given coordinates, maybe null
     *   if the controller is not bound to a JXCalendarView.
     * @throws IllegalArgumentException if the coordinates are not valid on the current page.
     */
    public JComponent prepareRendererComponent(Calendar calendar,
            int row, int column) {
        return getRendererHandler().prepareRendererComponent(calendar, row, column);
    }

    
    /**
     * Returns the string value for the given state, guaranteed to be not null.
     * 
     * @param state the type of calendar cell.
     * @return the StringValue for rendering a calendar cell of the given type.
     */
    public StringValue getStringValue(CalendarCellState state) {
        return getRendererHandler().getStringValue(state);
    }

    /**
     * Sets the StringValue for rendering a calendar cell of the given type.
     * 
     * @param sv the StringValue to use. Maybe null to indicate use of default.
     * @param state the type of cell to use it for, must not be null.
     */
    public void setStringValue(StringValue sv, CalendarCellState state) {
        getRendererHandler().setStringValue(sv, state);
    }


    /**
     * Sets the <code>Highlighter</code>s to the table, replacing any old
     * settings. None of the given Highlighters must be null.
     * <p>
     * 
     * This is a bound property.
     * <p>
     * 
     * @param highlighters zero or more not null highlighters to use for
     *        renderer decoration.
     * @throws NullPointerException if array is null or array contains null
     *         values.
     * 
     * @see #getHighlighters()
     * @see #addHighlighter(Highlighter)
     * @see #removeHighlighter(Highlighter)
     * 
     */
    public void setHighlighters(Highlighter... highlighters) {
        Highlighter[] old = getHighlighters();
        getRendererHandler().setHighlighters(highlighters);
        firePropertyChange("highlighters", old, getHighlighters());
    }

    /**
     * Returns the <code>Highlighter</code>s used by this table. Maybe empty,
     * but guarantees to be never null.
     * 
     * @return the Highlighters used by this table, guaranteed to never null.
     * @see #setHighlighters(Highlighter[])
     */
    public Highlighter[] getHighlighters() {
        return getRendererHandler().getHighlighters();
    }

    /**
     * Appends a <code>Highlighter</code> to the end of the list of used
     * <code>Highlighter</code>s. The argument must not be null.
     * <p>
     * 
     * @param highlighter the <code>Highlighter</code> to add, must not be null.
     * @throws NullPointerException if <code>Highlighter</code> is null.
     * 
     * @see #removeHighlighter(Highlighter)
     * @see #setHighlighters(Highlighter[])
     */
    public void addHighlighter(Highlighter highlighter) {
        Highlighter[] old = getHighlighters();
        getRendererHandler().addHighlighter(highlighter);
        firePropertyChange("highlighters", old, getHighlighters());
    }
    
    /**
     * Removes the given Highlighter.
     * <p>
     * 
     * Does nothing if the Highlighter is not contained.
     * 
     * @param highlighter the Highlighter to remove.
     * @see #addHighlighter(Highlighter)
     * @see #setHighlighters(Highlighter...)
     */
    public void removeHighlighter(Highlighter highlighter) {
        Highlighter[] old = getHighlighters();
        getRendererHandler().removeHighlighter(highlighter);
        firePropertyChange("highlighters", old, getHighlighters());
    }


    protected RendererHandler getRendererHandler() {
        if (rendererHandler == null) {
            rendererHandler = new RendererHandler(this);
        }
        return rendererHandler;
    }
    
    
    
//------------------- visual properties    
    /**
     * Returns whether or not this <code>JXCalendar</code> should display
     * week number.
     *
     * @return <code>true</code> if week numbers should be displayed
     */
    public boolean isShowingWeekNumber() {
        return showWeekNumber;
    }

    /**
     * Set whether or not this <code>JXCalendar</code> will display week
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
     * is the background of the <code>JXCalendar</code> component.
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

//-------------------- action and listener

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
     * Commits the current selection. <p>
     * 
     * Resets the model's adjusting property to false
     * and fires an ActionEvent
     * with the COMMIT_KEY action command.
     * 
     * 
     * @see #cancelSelection()
     * @see org.jdesktop.swingx.calendar.DateSelectionModel#setAdjusting(boolean)
     */
    public void commitSelection() {
        getSelectionModel().setAdjusting(false);
        fireActionPerformed(COMMIT_KEY);
    }

    /**
     * Cancels the selection. <p>
     * 
     * Resets the model's adjusting property to 
     * false and fires an ActionEvent with the CANCEL_KEY action command.
     * 
     * @see #commitSelection
     * @see org.jdesktop.swingx.calendar.DateSelectionModel#setAdjusting(boolean)
     */
    public void cancelSelection() {
        getSelectionModel().setAdjusting(false);
        fireActionPerformed(CANCEL_KEY);
    }
    /**
     * Adds an ActionListener.
     * <p/>
     * The ActionListener will receive an ActionEvent with its actionCommand
     * set to COMMIT_KEY or CANCEL_KEY after the selection has been committed
     * or canceled, respectively.
     * <p>
     * 
     * Note that actionEvents are typically fired after a dedicated user gesture 
     * to end an ongoing selectin (like ENTER, ESCAPE) or after explicit programmatic
     * commits/cancels. It is usually not fired after each change to the selection state.
     * Client code which wants to be notified about all selection changes should 
     * register a DateSelectionListener to the DateSelectionModel.
     * 
     * @param l The ActionListener that is to be notified
     * 
     * @see #commitSelection()
     * @see #cancelSelection()
     * @see #getSelectionModel()
     */
    public void addActionListener(ActionListener l) {
        listenerMap.add(ActionListener.class, l);
    }

    /**
     * Removes an ActionListener.
     *
     * @param l The ActionListener to remove.
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
                e = new ActionEvent(JXCalendar.this,
                        ActionEvent.ACTION_PERFORMED,
                        actionCommand);
            }
            listener.actionPerformed(e);
        }
    }

    

  //------------------- ui delegate    
      /**
       * @inheritDoc
       */
      public CalendarUI getUI() {
          return (CalendarUI)ui;
      }

      /**
       * Sets the L&F object that renders this component.
       *
       * @param ui UI to use for this {@code JXCalendar}
       */
      public void setUI(CalendarUI ui) {
          super.setUI(ui);
      }

      /**
       * Resets the UI property with the value from the current look and feel.
       *
       * @see UIManager#getUI(JComponent)
       */
      @Override
      public void updateUI() {
          setUI((CalendarUI)LookAndFeelAddons.getUI(this, CalendarUI.class));
          invalidate();
          getRendererHandler().updateUI();
      }

      /**
       * @inheritDoc
       */
      @Override
      public String getUIClassID() {
          return uiClassID;
      }

      


}
