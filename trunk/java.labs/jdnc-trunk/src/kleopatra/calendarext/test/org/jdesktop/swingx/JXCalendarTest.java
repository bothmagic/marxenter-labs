/**
 * $Id: JXMonthViewTest.java 3571 2010-01-05 15:07:17Z kleopatra $
 * 
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.event.DateSelectionEvent.EventType;
import org.jdesktop.swingx.hyperlink.AbstractHyperlinkAction;
import org.jdesktop.swingx.plaf.basic.CalendarHeader;
import org.jdesktop.swingx.plaf.basic.CalendarNavigator;
import org.jdesktop.swingx.plaf.basic.Navigator;
import org.jdesktop.swingx.test.DateSelectionReport;
import org.jdesktop.test.ActionReport;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Test case for <code>JXCalendar</code>
 *
 * There's another class with passing unit tests for JXCalendar (JXMonthViewVisualTest)
 * because this 
 * extended mock while the other extends InteractiveTestCase. Both are expected
 * to pass. As mock had been removed, they should be merged.
 * 
 * @author Joshua Outwater
 */
@RunWith(JUnit4.class)
public class JXCalendarTest extends InteractiveTestCase {
    private static final Logger LOG = Logger.getLogger(JXCalendarTest.class
            .getName());
    private Locale componentLocale;
    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    // calendar default instance init with today
    private Calendar calendar;

    private JXCalendar calendarView;
    
    @Override
    @Before
       public void setUp() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        today = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        afterTomorrow = calendar.getTime();
        
        calendar.setTime(today);

        //the test is configured for a US defaulted system
        //the localization tests handle actual localization issues
        componentLocale = JComponent.getDefaultLocale();
//        LOG.info("componentLocale " + componentLocale);
//        JComponent.setDefaultLocale(Locale.US);
        calendarView = new JXCalendar();
    }

    @Override
    @After
       public void tearDown() {
        JComponent.setDefaultLocale(componentLocale);
    }

    public static void main(String[] args) {
        JXCalendarTest test = new JXCalendarTest();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    /**
     * Issue #1046-swingx: month title not updated when traversing months
     * (programatically or by navigating in calendarView)
     * Issue #1245-swingx: incorrect month/dayofweek names for non-core-supported Locales.
     * 
     */
    @Test
    public void testZoomableNameByLocaleProvider() {
        Locale serbianLatin = getLocal("sh");
        if (serbianLatin == null) {
            LOG.fine("can't run, no service provider for serbian latin" );
            return;
        }
        JXCalendar calendarView = new JXCalendar();
        AbstractHyperlinkAction<?> action = (AbstractHyperlinkAction<?>) calendarView.getActionMap().get(CalendarHeader.ZOOM_OUT_KEY);
        assertSame(calendarView, action.getTarget());
        calendarView.setLocale(serbianLatin);
        String[] monthNames = DateFormatSymbols.getInstance(calendarView.getLocale()).getMonths();
        Calendar calendar = calendarView.getPage();
        int month = calendar.get(Calendar.MONTH);
        assertTrue("name must be updated with locale, expected: " + monthNames[month] + " was: " + action.getName(), 
                action.getName().startsWith(monthNames[month]));
    }

    
    
    /**
     * @param string
     * @return
     */
    private Locale getLocal(String language) {
        Locale[] available = Locale.getAvailableLocales();
        for (Locale locale : available) {
            if (language.equals(locale.getLanguage())) return locale;
        }
        return null;
    }


    /**
     * Issue #1143-swingx: NPE after setTimeZone/setModel.
     */
    @Test
    public void testFirstDisplayedDayAfterTimeZone() {
        JXCalendar calendarView = new JXCalendar();
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (calendarView.getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        calendarView.setTimeZone(tz);
        assertTrue(CalendarUtils.isStartOfDay(calendarView.getPage()));
        calendarView.setSelectionModel(new SingleDaySelectionModel());
        assertTrue(CalendarUtils.isStartOfDay(calendarView.getPage()));
    }
    /**
     * Quick class to inject a current time source.
     */
    public static class Clock {
        private static int millisPerDay = 24 * 60 * 60 * 1000;
        private Date current;

        /**
         * Instantiates a time source with default current.
         */
        public Clock() {
            this(null);
        }
        
        /**
         * Instantiates a time source with the given date as current. If null, uses
         * System.currentTimeMillis().
         * 
         * @param date the current date.
         */
        public Clock(Date date) {
            this.current = date != null ? date : new Date(System.currentTimeMillis()); 
        }
        /**
         * Returns the current time.
         * 
         * @return current time.
         */
        public Date getCurrentDate() {
            return current;
        }
        
        /**
         * Increments current by one day.
         */
        public void nextDay() {
            current = new Date(current.getTime() + millisPerDay);
        }
        
    }
    /**
     * Issue #1072-swingx: nav icons incorrect for RToL if zoomable
     */
    @Test
    public void testNavigationIconsUpdatedWithCO() {
       Action action = calendarView.getActionMap().get(CalendarHeader.NEXT_PAGE_KEY); 
       if (calendarView.getComponentOrientation().isLeftToRight()) {
           Icon icon = (Icon) action.getValue(Action.SMALL_ICON);
           assertNotNull("sanity: the decorated month nav action has an icon", icon);
           assertEquals(UIManager.getIcon("JXCalendar.monthUpFileName"), icon);
           calendarView.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//           assertNotSame(icon, action.getValue(Action.SMALL_ICON));
           assertEquals(action.getValue(Action.SMALL_ICON), UIManager.getIcon("JXCalendar.monthDownFileName"));
       } else {
           
       }
    }
    
    /**
     * Issue #1046-swingx: month title not updated when traversing months
     * (programatically or by navigating in calendarView)
     */
    @Test
    public void testZoomableNameOnMonthChange() {
        JXCalendar calendarView = new JXCalendar();
        AbstractHyperlinkAction<?> action = (AbstractHyperlinkAction<?>) 
            calendarView.getActionMap().get(CalendarHeader.ZOOM_OUT_KEY);
        assertSame(calendarView, action.getTarget());
        String[] monthNames = DateFormatSymbols.getInstance(calendarView.getLocale()).getMonths();
        Calendar calendar = calendarView.getPage();
        int month = calendar.get(Calendar.MONTH);
        assertTrue(action.getName().startsWith(monthNames[month]));
        calendar.add(Calendar.MONTH, 1);
        calendarView.setSelectionDate(calendar.getTime());
        int nextMonth = calendar.get(Calendar.MONTH);
        assertTrue("month changed: old/new " + month + "/" + nextMonth, nextMonth != month);
        assertTrue("name must be updated, expected: " + monthNames[nextMonth] + " was: " + action.getName()
                , action.getName().startsWith(monthNames[nextMonth]));
    }

    /**
     * Issue #1046-swingx: month title not updated when traversing months
     * (programatically or by navigating in calendarView)
     */
    @Test
    public void testZoomableNameOnLocaleChange() {
        JXCalendar calendarView = new JXCalendar();
        AbstractHyperlinkAction<?> action = (AbstractHyperlinkAction<?>) 
            calendarView.getActionMap().get(CalendarHeader.ZOOM_OUT_KEY);
        assertSame(calendarView, action.getTarget());
        Locale locale = Locale.FRENCH;
        if (locale.equals(calendarView.getLocale())) {
            locale = Locale.GERMAN;
        }
        calendarView.setLocale(locale);
        String[] monthNames = DateFormatSymbols.getInstance(calendarView.getLocale()).getMonths();
        Calendar calendar = calendarView.getPage();
        int month = calendar.get(Calendar.MONTH);
        assertTrue("name must be updated with locale, expected: " + monthNames[month] + " was: " + action.getName(), 
                action.getName().startsWith(monthNames[month]));
    }

    /**
     * Test that navigational actions are installed
     */
    @Test
    public void testNavigationActionsInstalled() {
        JXCalendar calendarView = new JXCalendar();
        assertActionInstalled(calendarView, Navigator.PREVIOUS_PAGE_KEY);
        assertActionInstalled(calendarView, Navigator.NEXT_PAGE_KEY);
        assertActionInstalled(calendarView, Navigator.ZOOM_OUT_KEY);
        // actions mapped by CalendarHeaderHandler
        assertActionInstalled(calendarView, CalendarHeader.NEXT_PAGE_KEY);
        assertActionInstalled(calendarView, CalendarHeader.PREVIOUS_PAGE_KEY);
        assertActionInstalled(calendarView, CalendarHeader.ZOOM_OUT_KEY);
    }
    
    /**
     * @param calendarView
     * @param actionKey
     */
    private void assertActionInstalled(JXCalendar calendarView, String actionKey) {
        assertNotNull("ui must have installed action for " + actionKey, calendarView.getActionMap().get(actionKey));
    }

    /**
     * Test that navigational actions are working as expected.
     */
    @Test
    public void testNavigationActionsWorking() {
        assertActionPerformed(new JXCalendar(), 
                CalendarHeader.NEXT_PAGE_KEY, Calendar.MONTH, 1);
        assertActionPerformed(new JXCalendar(), 
                Navigator.NEXT_PAGE_KEY, Calendar.MONTH, 1);
        assertActionPerformed(new JXCalendar(), 
                CalendarHeader.PREVIOUS_PAGE_KEY, Calendar.MONTH, -1);
        assertActionPerformed(new JXCalendar(), 
                Navigator.PREVIOUS_PAGE_KEY, Calendar.MONTH, -1);
    }
    
    private void assertActionPerformed(JXCalendar calendarView, String actionKey, int calendarField, int amount) {
        Calendar calendar = calendarView.getPage();
        calendar.add(calendarField, amount);
        Action action = calendarView.getActionMap().get(actionKey);
        action.actionPerformed(null);
        assertEquals(calendar.getTime(), calendarView.getPage().getTime());
    }

 

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringBackground property.
     * 
     */
    @Test
    public void testTodayBackground() {
        Color old = calendarView.getTodayBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setTodayBackground(color);
        assertEquals(color, calendarView.getTodayBackground());
        TestUtils.assertPropertyChangeEvent(report, "todayBackground", old, color);
    }

    
    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringBackground property.
     * 
     */
    @Test
    public void testMonthStringBackground() {
        Color old = calendarView.getMonthStringBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setMonthStringBackground(color);
        assertEquals(color, calendarView.getMonthStringBackground());
        TestUtils.assertPropertyChangeEvent(report, "monthStringBackground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringForeground property.
     * 
     */
    @Test
    public void testMonthStringForeground() {
        Color old = calendarView.getMonthStringForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setMonthStringForeground(color);
        assertEquals(color, calendarView.getMonthStringForeground());
        TestUtils.assertPropertyChangeEvent(report, "monthStringForeground", old, color);
    }
    /**
     * Issue #931-swingx: missing change notification.
     * test setting font property - was fired twice due to unneeded 
     * override of setFont (removed).
     * 
     */
    @Test
    public void testFont() {
        Font old = calendarView.getFont();
        Font padding = old.deriveFont(old.getSize2D() * 2);
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setFont(padding);
        assertEquals(padding, calendarView.getFont());
        TestUtils.assertPropertyChangeEvent(report, "font", old, padding);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting boxPaddingY property.
     * 
     */
    @Test
    public void testBoxPaddingY() {
        int old = calendarView.getBoxPaddingY();
        int padding = old + 2;
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setBoxPaddingY(padding);
        assertEquals(padding, calendarView.getBoxPaddingY());
        TestUtils.assertPropertyChangeEvent(report, "boxPaddingY", old, padding);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting boxPaddingX property.
     * 
     */
    @Test
    public void testBoxPaddingX() {
        int old = calendarView.getBoxPaddingX();
        int padding = old + 2;
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setBoxPaddingX(padding);
        assertEquals(padding, calendarView.getBoxPaddingX());
        TestUtils.assertPropertyChangeEvent(report, "boxPaddingX", old, padding);
    }


    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testSelectionForeground() {
        Color old = calendarView.getSelectionForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setSelectionForeground(color);
        assertEquals(color, calendarView.getSelectionForeground());
        TestUtils.assertPropertyChangeEvent(report, "selectionForeground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testSelectionBackground() {
        Color old = calendarView.getSelectionBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setSelectionBackground(color);
        assertEquals(color, calendarView.getSelectionBackground());
        TestUtils.assertPropertyChangeEvent(report, "selectionBackground", old, color);
    }
    
    @Test
    public void testInitialOpaque() {
        assertTrue(calendarView.isOpaque());
    }
    
    @Test
    public void testInitialBoxPadding() {
        assertTrue(calendarView.getBoxPaddingX() > 0);
        assertEquals(UIManager.getInt("JXCalendar.boxPaddingX"), calendarView.getBoxPaddingX());
        assertTrue(calendarView.getBoxPaddingY() > 0);
        assertEquals(UIManager.getInt("JXCalendar.boxPaddingY"), calendarView.getBoxPaddingY());
        
    }
    
    
     /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingWeekNumbersNotification() {
        JXCalendar calendarView = new JXCalendar();
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        boolean showing = calendarView.isShowingWeekNumber();
        calendarView.setShowingWeekNumber(!showing);
        TestUtils.assertPropertyChangeEvent(report, "showingWeekNumber", showing, !showing);
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingWeekNumbersNoNotification() {
        JXCalendar calendarView = new JXCalendar();
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        boolean showing = calendarView.isShowingWeekNumber();
        calendarView.setShowingWeekNumber(showing);
        assertEquals(0, report.getEventCount("showingWeekNumber"));
    }

    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: test that model settings are respected in constructor - minimaldays.
     */
    @Test
    public void testCalendarsContructorUnchangedFirstDayOfWeek() {
        DateSelectionModel model = new DaySelectionModel();
        int first = model.getFirstDayOfWeek() + 1;
        model.setFirstDayOfWeek(first);
        JXCalendar calendarView = new JXCalendar(model);
        assertEquals("model's calendar properties must be unchanged: minimalDays", 
                first, model.getFirstDayOfWeek());
        // sanity: taken in calendarView
        assertEquals("calendarView's calendar properties must be synched", 
                first, calendarView.getFirstDayOfWeek());
    }
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: test that calendarView is updated to model after setSelectionModel.
     */
    @Test
    public void testCalendarsSetModel() {
        JXCalendar calendarView = new JXCalendar();
        int firstDayOfWeek = calendarView.getFirstDayOfWeek();
        Locale locale = Locale.UK;
        if (locale.equals(calendarView.getLocale())) {
            locale = Locale.FRENCH;
        }
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (calendarView.getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        DateSelectionModel model = new DaySelectionModel(locale);
        model.setTimeZone(tz);
        int modelMinimal = model.getMinimalDaysInFirstWeek();
        calendarView.setSelectionModel(model);
        assertEquals("timeZone must be updated from model", tz, calendarView.getTimeZone());
        // Issue 1143
        assertTrue("firstDisplayedDay must be updated to start of day", CalendarUtils.isStartOfDay(calendarView.getPage()));
        assertEquals("Locale must be updated from model", locale, calendarView.getLocale());
        // be aware if it makes no sense to assert
        if (firstDayOfWeek != model.getFirstDayOfWeek()) {
            assertEquals("firstDayOfWeek must be updated from model", 
                    model.getFirstDayOfWeek(), calendarView.getFirstDayOfWeek());
        } else {
            LOG.info("cannot assert firstDayOfWeek - was same");
        }
        // @KEEP - this is an open issue: calendarView must not change the
        // model settings but minimalDaysInFirstWeek > 1 confuse the 
        // BasicMonthViewUI - remove if passing in xIssues
        assertEquals("model minimals must not be changed", 
                modelMinimal, model.getMinimalDaysInFirstWeek());
    }

    /**
     * Issue #736-swingx: model and calendarView cal not synched.
     * 
     * Here: test that model settings are respected in constructor - minimaldays.
     */
    @SuppressWarnings("unused")
    @Test
    public void testCalendarsContructorUnchangedMinimalDaysOfModel() {
        DateSelectionModel model = new DaySelectionModel();
        int first = model.getMinimalDaysInFirstWeek() + 1;
        model.setMinimalDaysInFirstWeek(first);
        JXCalendar calendarView = new JXCalendar(model);
        assertEquals("model's calendar properties must be unchanged: minimalDays", 
                first, model.getMinimalDaysInFirstWeek());
    }

    /**
     * Issue #736-swingx: model and calendarView cal not synched.
     * 
     * Here: test that model settings are respected in setModel - minimaldays.
     * 
     * Model must not reset minimalDaysInfirstWeek, but Locales with values
     * > 1 confuse the BasicDatePickerUI - need to track down and solve there.
     */
    @Test
    public void testCalendarsSetModelUnchangedMinimalDaysInFirstWeek() {
        JXCalendar calendarView = new JXCalendar();
        DateSelectionModel model = new DaySelectionModel();
        int first = model.getMinimalDaysInFirstWeek() + 1;
        model.setMinimalDaysInFirstWeek(first);
        calendarView.setSelectionModel(model);
        assertEquals("model minimals must not be changed", 
                first, model.getMinimalDaysInFirstWeek());
    }
    

    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: minimal days of first week.
     */
    @Test
    public void testCalendarsMinimalDaysOfFirstWeekModelChanged() {
        JXCalendar calendarView = new JXCalendar();
        int first = calendarView.getPage().getMinimalDaysInFirstWeek() + 1;
        assertTrue(first <= Calendar.SATURDAY);
        calendarView.getSelectionModel().setMinimalDaysInFirstWeek(first);
        assertEquals(first, calendarView.getPage().getMinimalDaysInFirstWeek());
    }
    

    /**
     * Issue #733-swingx: TimeZone in model and calendarView not synched.
     *  
     *  Test that the selected is normalized in the calendarView's timezone. 
     */
    @Test
    public void testCalendarsTimeZoneNormalizedDate() {
        JXCalendar calendarView = new JXCalendar();
        // config with a known timezone
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (tz.equals(calendarView.getTimeZone())) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        calendarView.setTimeZone(tz);
        calendarView.setSelectionDate(new Date());
        Date selected = calendarView.getSelectionDate();
        Calendar calendar = calendarView.getPage();
        assertEquals(selected, CalendarUtils.startOfDay(calendar, selected));
    }
    
    
    
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: setting the timezone clears the flagged dates, must notify of change.
      */
    @Test
    public void testFlaggedDatesTimeZoneNotNotifyWithoutChange() {
        JXCalendar calendarView = new JXCalendar();
        // config with a known timezone and date
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (tz.equals(calendarView.getTimeZone())) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setTimeZone(tz);
        assertEquals("no change in flaggedDates must not fire", 0, report.getEventCount("flaggedDates"));
    }
    
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: Locale changed in calendarView.
     */
    @Test
    public void testCalendarsLocaleChangedMonthView() {
        JXCalendar calendarView = new JXCalendar();
        Locale locale = Locale.UK;
        if (locale.equals(calendarView.getLocale())) {
            locale = Locale.FRENCH;
        }
        calendarView.setLocale(locale);
        assertEquals("locale set in calendarView must be passed to model", 
                locale, calendarView.getSelectionModel().getLocale());
    }
    
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: Locale changed in selection model.
     */
    @Test
    public void testCalendarsLocaleChangedModel() {
        JXCalendar calendarView = new JXCalendar();
        Locale locale = Locale.UK;
        if (locale.equals(calendarView.getLocale())) {
            locale = Locale.FRENCH;
        }
        calendarView.getSelectionModel().setLocale(locale);
        assertEquals("locale set in model must be passed to calendarView", 
                locale, calendarView.getLocale());
    }
    
    

    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: Locale changed in calendarView.
     */
    @Test
    public void testCalendarsLocaleContructor() {
        Locale locale = Locale.UK;
        if (locale.equals(JComponent.getDefaultLocale())) {
            locale = Locale.FRENCH;
        }
        JXCalendar calendarView = new JXCalendar(locale);
        assertEquals("initial locale in constructor must be passed to model", 
                locale, calendarView.getSelectionModel().getLocale());
    }

    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: set first day of week in calendarView.
     */
    @Test
    public void testCalendarsFirstDayOfWeekMonthView() {
        JXCalendar calendarView = new JXCalendar();
        int first = calendarView.getFirstDayOfWeek() + 1;
        // sanity
        assertTrue(first <= Calendar.SATURDAY);
        calendarView.setFirstDayOfWeek(first);
        assertEquals(first, calendarView.getFirstDayOfWeek());
        assertEquals(first, calendarView.getPage().getFirstDayOfWeek());
        assertEquals(first, calendarView.getSelectionModel().getFirstDayOfWeek());
    }
    
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: set first day of week in model.
     */
    @Test
    public void testCalendarsFirstDayOfWeekModel() {
        JXCalendar calendarView = new JXCalendar();
        int first = calendarView.getFirstDayOfWeek() + 1;
        // sanity
        assertTrue(first <= Calendar.SATURDAY);
        calendarView.getSelectionModel().setFirstDayOfWeek(first);
        assertEquals(first, calendarView.getFirstDayOfWeek());
        assertEquals(first, calendarView.getPage().getFirstDayOfWeek());
        assertEquals(first, calendarView.getSelectionModel().getFirstDayOfWeek());
    }
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: first day of week.
     */
    @Test
    public void testCalendarsFirstDayOfWeekInitial() {
        JXCalendar calendarView = new JXCalendar();
        assertEquals(calendarView.getFirstDayOfWeek(), 
                calendarView.getSelectionModel().getFirstDayOfWeek());
    }
    /**
     * Issue #733-swingx: model and calendarView cal not synched.
     * 
     * Here: minimal days of first week.
     */
    @Test
    public void testCalendarsMinimalDaysOfFirstWeekInitial() {
        JXCalendar calendarView = new JXCalendar();
        int first = calendarView.getPage().getMinimalDaysInFirstWeek();
        assertEquals(first, calendarView.getSelectionModel().getMinimalDaysInFirstWeek());
    }
    
    
    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: isSelected
     * 
     */
    @Test
    public void testMonthViewSameAsSelectionModelIsSelected() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        calendarView.setSelectionDate(date);
        assertTrue(calendarView.isSelected(date));
        assertTrue(calendarView.getSelectionModel().isSelected(date));
    }
    
    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: isSelected
     * 
     */
    @Test
    public void testMonthViewSameAsSelectionModelSelectedDate() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        calendarView.setSelectionDate(date);
        assertEquals(calendarView.getSelectionDate(), 
                calendarView.getSelectionModel().getFirstSelectionDate());
    }

    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: isSelected
     * 
     */
    @Test
    public void testMonthViewSameAsSelectionModelIsUnselectable() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        calendarView.setUnselectableDates(date);
        assertTrue(calendarView.isUnselectableDate(date));
        assertTrue(calendarView.getSelectionModel().isUnselectableDate(date));
    }

    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: set unselectables on model
     * 
     */
    @Test
    public void testSelectionModelSameAsMonthViewIsUnselectableDate() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        SortedSet<Date> unselectables = new TreeSet<Date>();
        unselectables.add(date);
        calendarView.getSelectionModel().setUnselectableDates(unselectables);
        assertTrue(calendarView.getSelectionModel().isUnselectableDate(date));
        assertTrue(calendarView.isUnselectableDate(date));
    }
    
    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: set selected on model
     * 
     */
    @Test
    public void testSelectionModelSameAsMonthViewIsSelected() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        calendarView.getSelectionModel().setSelectionInterval(date, date);
        assertTrue(calendarView.getSelectionModel().isSelected(date));
        assertTrue(calendarView.isSelected(date));
    }
    
    /**
     * Issue ??-swingx: selection related properties must be independent 
     * of way-of setting.
     * 
     * View must delegate to model, so asking view or model with same 
     * parameters must return the same result.
     * 
     * Here: set selected on model, ask for selected date
     * 
     */
    @Test
    public void testSelectionModelSameAsMonthViewSelectedDate() {
        JXCalendar calendarView = new JXCalendar();
        // guard against accidental startofday
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        Date date = calendar.getTime();
        calendarView.getSelectionModel().setSelectionInterval(date, date);
        assertEquals(calendarView.getSelectionModel().getFirstSelectionDate(), 
                calendarView.getSelectionDate());
    }
    
    
    
    /**
     * Issue #711-swingx: remove fake property change notification.
     * 
     * Here: test that setFirstDisplayedDate fires once only.
     */
    @Test
    public void testFirstDisplayedDateNofication() {
        JXCalendar calendarView = new JXCalendar();
        Date firstDisplayedDate = calendarView.getPage().getTime();
        // previous month
        calendar.add(Calendar.MONTH, -1);
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setSelectionDate(calendar.getTime());
        CalendarUtils.startOfMonth(calendar);
        TestUtils.assertPropertyChangeEvent(report, "firstDisplayedDay", 
                firstDisplayedDate, calendar.getTime(), false);
    }
    
    
    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that firstDisplayedDate is unchanged.
     */
    @Test
    public void testUpdateUIFirst() {
        final JXCalendar calendarView = new JXCalendar();
        Date first = calendarView.getPage().getTime();
        calendarView.updateUI();
        assertEquals(first, calendarView.getPage().getTime());
    };




    /**
     * Issue #660-swingx: JXCalendar must protect its calendar.
     * Client manipulation on calendar must not change internal state.
     * 
     * This is guaranteed by returning a clone instead of the life object.
     */
    @Test
    public void testMonthViewCalendarInvariant() {
        JXCalendar calendarView = new JXCalendar();
        TimeZone tz = calendarView.getTimeZone();
        Calendar calendar = calendarView.getPage();
        calendar.setTimeZone(getTimeZone(tz, CalendarUtils.THREE_HOURS));
        assertEquals("calendarView must protect its calendar", tz, calendarView.getTimeZone());
    }

    /**
     * Issue #660-swingx: JXCalendar must protect its calendar.
     * 
     * Added invariant to the calendarView's getCalender: clone and
     * config to firstDisplayDate.
     * 
     * The various tests are various contexts which broke the 
     * expectation before fixing the issue. 
     * Here the context is: select.
     */
    @Test
   public void testMonthViewCalendarInvariantOnSetSelection() {
      JXCalendar calendarView = new JXCalendar();
      assertEquals(1, calendarView.getPage().get(Calendar.DATE));
      Date first = calendarView.getPage().getTime();
      assertEquals("monthViews calendar represents the first day of the month", 
              first, calendarView.getPage().getTime());
      Calendar cal = Calendar.getInstance();
      // add one day, now we are on the second
      cal.setTime(first);
      cal.add(Calendar.DATE, 1);
      Date date = cal.getTime();
      calendarView.addSelectionInterval(date , date);
      assertEquals("selection must not change the calendar", 
              first, calendarView.getPage().getTime());
   }

   /**
    * Issue #660-swingx: JXCalendar must protect its calendar.
    * 
    * Added invariant to the calendarView's getCalender: clone and
    * config to firstDisplayDate.
    * 
    * The various tests are various contexts which broke the 
    * expectation before fixing the issue. 
    * Here the context is: check for selection.
    */
    @Test
   public void testMonthViewCalendarInvariantOnQuerySelectioon() {
      JXCalendar calendarView = new JXCalendar();
      assertEquals(1, calendarView.getPage().get(Calendar.DATE));
      Date first = calendarView.getPage().getTime();
      assertEquals("monthViews calendar represents the first day of the month", 
              first, calendarView.getPage().getTime());
      Calendar cal = Calendar.getInstance();
      // add one day, now we are on the second
      cal.setTime(first);
      cal.add(Calendar.DATE, 1);
      Date date = cal.getTime();
      calendarView.isSelected(date);
      assertEquals("query selection must not change the calendar", 
              first, calendarView.getPage().getTime());
   }


    /**
     * Issue #660-swingx: JXCalendar must protect its calendar.
     * 
     * Added invariant to the calendarView's getCalender: clone and
     * config to firstDisplayDate.
     * 
     * The various tests are various contexts which broke the 
     * expectation before fixing the issue. 
     * Here the context is: set first displayed date (formerly left
     * the calendar at the last displayed date).
     */
    @Test
    public void testMonthViewCalendarInvariantOnSetFirstDisplayedDate() {
      JXCalendar calendarView = new JXCalendar();
      Date first = calendarView.getPage().getTime();
      Calendar cal = Calendar.getInstance();
      // add one day, now we are on the second
      cal.setTime(first);
      cal.add(Calendar.MONTH, 1);
      Date next = cal.getTime();
      calendarView.setSelectionDate(next);
      LOG.info("next/calendar " + next + " / " + calendarView.getPage().getTime());
      assertEquals("monthViews calendar represents the first day of the month", 
              next, calendarView.getPage().getTime());
    }
 
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     * Selected dates are "start of day" in the timezone they had been 
     * selected. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the selection. 
     */
    @Test
    public void testTimeZoneChangeClearSelection() {
        JXCalendar calendarView = new JXCalendar();
        Date date = new Date();
        calendarView.setSelectionDate(date);
        // sanity
        assertTrue(calendarView.isSelected(date));
        calendarView.setTimeZone(getTimeZone(calendarView.getTimeZone(), CalendarUtils.THREE_HOURS));
        // accidentally passes - because it is meaningful only in the timezone 
        // it was set ...
        assertFalse(calendarView.isSelected(date));
        assertTrue("selection must have been cleared", calendarView.isSelectionEmpty());
    }
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetLowerBound() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setLowerBound(yesterday);
        calendarView.setTimeZone(getTimeZone(calendarView.getTimeZone(), CalendarUtils.THREE_HOURS));
        assertEquals("lowerBound must have been reset", null, calendarView.getLowerBound());
    }
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetUpperBound() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setUpperBound(yesterday);
        calendarView.setTimeZone(getTimeZone(calendarView.getTimeZone(), CalendarUtils.THREE_HOURS));
        assertEquals("upperbound must have been reset", null, calendarView.getUpperBound());
    }
    
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     * Unselectable dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear them. 
     */
    @Test
    public void testTimeZoneChangeResetUnselectableDates() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setUnselectableDates(yesterday);
        calendarView.setTimeZone(getTimeZone(calendarView.getTimeZone(), CalendarUtils.THREE_HOURS));
        // accidentally passes - because it is meaningful only in the timezone 
        // it was set ...
        assertFalse(calendarView.isUnselectableDate(yesterday));
        // missing api on JXCalendar
        assertEquals("unselectable dates must have been cleared", 
                0, calendarView.getSelectionModel().getUnselectableDates().size());
    }
    

    /**
     * Returns a timezone with a rawoffset with a different offset.
     * 
     * 
     * PENDING: this is acutally for european time, not really thought of 
     *   negative/rolling +/- problem?
     * 
     * @param timeZone the timezone to start with 
     * @param diffRawOffset the raw offset difference.
     * @return
     */
    private TimeZone getTimeZone(TimeZone timeZone, int diffRawOffset) {
        int offset = timeZone.getRawOffset();
        int newOffset = offset < 0 ? offset + diffRawOffset : offset - diffRawOffset;
        String[] availableIDs = TimeZone.getAvailableIDs(newOffset);
        TimeZone newTimeZone = TimeZone.getTimeZone(availableIDs[0]);
        return newTimeZone;
    }
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     * Here: test timezone fire
     */
    @Test
    public void testTimeZoneChangeNotification() {
        JXCalendar calendarView = new JXCalendar();
        TimeZone timezone = calendarView.getTimeZone();
        int offset = timezone.getRawOffset();
        int oneHour = 60 * 1000 * 60;
        int newOffset = offset < 0 ? offset + oneHour : offset - oneHour;
        String[] availableIDs = TimeZone.getAvailableIDs(newOffset);
        TimeZone newTimeZone = TimeZone.getTimeZone(availableIDs[0]);
        // sanity
        assertFalse(timezone.equals(newTimeZone));
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setTimeZone(newTimeZone);
        TestUtils.assertPropertyChangeEvent(report, 
                "timeZone", timezone, newTimeZone, false);
    }
    

    /**
     * Issue #563-swingx: keybindings active if not focused.
     * Test that the bindings are dynamically installed when
     * shown in popup and de-installed if shown not in popup.
    */
    @Test
    public void testComponentInputMapEnabledControlsFocusedKeyBindings() {
        JXCalendar calendarView = new JXCalendar();
        // initial: no bindings
        assertEquals("calendarView must not have in-focused keyBindings", 0, 
                calendarView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size());
        calendarView.setComponentInputMapEnabled(true);
        // setting the flag installs bindings
        assertTrue("calendarView must have in-focused keyBindings after showing in popup",  
              calendarView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size() > 0);
        calendarView.setComponentInputMapEnabled(false);
        // resetting the flag uninstalls the bindings
        assertEquals("calendarView must not have in-focused keyBindings", 0, 
                calendarView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size());
    }

    /**
     * Test default value and property change notificateion of 
     * the componentInputMapEnabled property.
     *
     */
    @Test
    public void testComponentInputMapEnabled() {
        JXCalendar calendarView = new JXCalendar();
        assertFalse("the default value must be false", 
                calendarView.isComponentInputMapEnabled());
        PropertyChangeReport report = new PropertyChangeReport();
        calendarView.addPropertyChangeListener(report);
        calendarView.setComponentInputMapEnabled(true);
        TestUtils.assertPropertyChangeEvent(report, 
                "componentInputMapEnabled", false, true, false);
        report.clear();
        calendarView.setComponentInputMapEnabled(false);
        TestUtils.assertPropertyChangeEvent(report, 
                "componentInputMapEnabled", true, false, false);
    }
    
    /**
     * test doc'ed behaviour: model must not be null.
     *
     */
    @Test
    public void testSetModelNull() {
        JXCalendar calendarView = new JXCalendar();
        assertNotNull(calendarView.getSelectionModel());
        try {
            calendarView.setSelectionModel(null);
            fail("null model must not be accepted");
        } catch (NullPointerException ex) {
            // expected - but how do we test fail-fast implemented
        }
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions resets model.adjusting to false.
     */
    @Test
    public void testCommitCancelResetsAdjusting() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.getSelectionModel().setAdjusting(true);
        calendarView.commitSelection();
        assertFalse("commit must reset adjusting", 
                calendarView.getSelectionModel().isAdjusting());
        calendarView.getSelectionModel().setAdjusting(true);
        calendarView.cancelSelection();
        assertFalse("cancel must reset adjusting", 
                calendarView.getSelectionModel().isAdjusting());
        
    }
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions fire as expected.
     *
     */
    @Test
    public void testCommitCancelAPIFires() {
        JXCalendar picker = new JXCalendar();
        ActionReport report = new ActionReport();
        picker.addActionListener(report);
        picker.commitSelection();
        assertEquals(1, report.getEventCount());
        assertEquals(JXCalendar.COMMIT_KEY, report.getLastActionCommand());
        report.clear();
        picker.cancelSelection();
        assertEquals(1, report.getEventCount());
        assertEquals(JXCalendar.CANCEL_KEY, report.getLastActionCommand());
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions fire as expected.
     *
     */
    @Test
    public void testCommitCancelActionsFire() {
        JXCalendar picker = new JXCalendar();
        Action commitAction = picker.getActionMap().get(JXCalendar.COMMIT_KEY);
        ActionReport report = new ActionReport();
        picker.addActionListener(report);
        commitAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXCalendar.COMMIT_KEY, report.getLastActionCommand());
        report.clear();
        Action cancelAction = picker.getActionMap().get(JXCalendar.CANCEL_KEY);
        cancelAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXCalendar.CANCEL_KEY, report.getLastActionCommand());
    }


    /**
     * Enhanced commit/cancel.
     * 
     * test that actions are registered.
     *
     */
    @Test
    public void testCommitCancelActionExist() {
        JXCalendar picker = new JXCalendar();
        assertNotNull(picker.getActionMap().get(JXCalendar.CANCEL_KEY));
        assertNotNull(picker.getActionMap().get(JXCalendar.COMMIT_KEY));
    }
    

    /**
     * BasicMonthViewUI: use adjusting api in keyboard actions.
     * Here: test reset in cancel action.
     */
    @Test
    public void testAdjustingResetOnCancel() {
        JXCalendar view = new JXCalendar();
        Action select = view.getActionMap().get(CalendarNavigator.NEXT_CELL_KEY);
        select.actionPerformed(null);
        DateSelectionReport report = new DateSelectionReport(view.getSelectionModel());
        Action cancel = view.getActionMap().get(JXCalendar.CANCEL_KEY);
        cancel.actionPerformed(null);
        assertFalse("ui keyboard action must have stopped model adjusting", 
                view.getSelectionModel().isAdjusting());
        assertEquals(2, report.getEventCount());
    }
    /**
     * BasicMonthViewUI: use adjusting api in keyboard actions.
     * Here: test reset in accept action.
     */
    @Test
    public void testAdjustingResetOnAccept() {
        JXCalendar view = new JXCalendar();
        Action select = view.getActionMap().get(CalendarNavigator.NEXT_CELL_KEY);
        select.actionPerformed(null);
        DateSelectionReport report = new DateSelectionReport(view.getSelectionModel());
        Action cancel = view.getActionMap().get(JXCalendar.COMMIT_KEY);
        cancel.actionPerformed(null);
        assertFalse("ui keyboard action must have stopped model adjusting", 
                view.getSelectionModel().isAdjusting());
        assertEquals(1, report.getEventCount());
        assertEquals(EventType.ADJUSTING_STOPPED, report.getLastEvent().getEventType());
    }

    /**
     * BasicMonthViewUI: use adjusting api in keyboard actions.
     * Here: test set selection action.
     */
    @Test
    public void testAdjustingSetOnSelect() {
        JXCalendar view = new JXCalendar();
        DateSelectionReport report = new DateSelectionReport(view.getSelectionModel());
        Action select = view.getActionMap().get(CalendarNavigator.NEXT_CELL_KEY);
        select.actionPerformed(null);
        assertTrue("ui keyboard action must have started model adjusting", 
                view.getSelectionModel().isAdjusting());
        assertEquals(2, report.getEventCount());
        // assert that the adjusting is fired before the set
        assertEquals(EventType.DATES_SET, report.getLastEvent().getEventType());
    }
 
    /**
     * Issue #557-swingx: always fire actionEvent after esc/enter.
     * 
     * test fire after accept.
     */
    @Test
    public void testFireOnKeyboardAccept()  {
        JXCalendar calendarView = new JXCalendar();
        Date date = new Date();
        calendarView.setSelectionInterval(date, date);
        ActionReport report = new ActionReport();
        calendarView.addActionListener(report);
        Action accept = calendarView.getActionMap().get(JXCalendar.COMMIT_KEY); 
        accept.actionPerformed(null);
        assertEquals(1, report.getEventCount());
    }

    /**
     * Issue #557-swingx: always fire actionEvent after esc/enter.
     * 
     * test fire after cancel.
     */
    @Test
    public void testFireOnKeyboardCancel()  {
        JXCalendar calendarView = new JXCalendar();
        Date date = new Date();
        calendarView.setSelectionInterval(date, date);
        ActionReport report = new ActionReport();
        calendarView.addActionListener(report);
        Action accept = calendarView.getActionMap().get(JXCalendar.CANCEL_KEY);
        accept.actionPerformed(null);
        assertEquals(1, report.getEventCount());
    }

    /**
     * expose more selection constraint methods in JXCalendar
     *
     */
    @Test
    public void testUpperBound() {
        JXCalendar view = new JXCalendar();
        view.setUpperBound(today);
        assertEquals(startOfDay(today), view.getUpperBound());
        // remove again
        view.setUpperBound(null);
        assertEquals(null, view.getUpperBound());
    }
    
    /**
     * expose more selection constraint methods in JXCalendar
     *
     */
    @Test
    public void testLowerBound() {
        JXCalendar view = new JXCalendar();
        view.setLowerBound(today);
        assertEquals(startOfDay(today), view.getLowerBound());
        // remove again
        view.setLowerBound(null);
        assertEquals(null, view.getLowerBound());
    }

    /**
     * test unselectable: use methods with Date.
     *
     */
    @Test
    public void testUnselectableDate() {
        JXCalendar calendarView = new JXCalendar();
        // initial
        assertFalse(calendarView.isUnselectableDate(today));
        // set unselectable today
        calendarView.setUnselectableDates(today);
        assertTrue("raqw today must be unselectable", 
                calendarView.isUnselectableDate(today));
        assertTrue("start of today must be unselectable", 
                calendarView.isUnselectableDate(startOfDay(today)));
        assertTrue("end of today must be unselectable", 
                calendarView.isUnselectableDate(endOfDay(today)));
        calendarView.setUnselectableDates();
        assertFalse(calendarView.isUnselectableDate(today));
        assertFalse(calendarView.isUnselectableDate(startOfDay(today)));
        assertFalse(calendarView.isUnselectableDate(endOfDay(today)));
    }

    /**
     * test unselectable: use methods with Date.
     * test NPE as doc'ed.
     */
    @Test
    public void testUnselectableDatesNPE() {
        JXCalendar calendarView = new JXCalendar();
        try {
            calendarView.setUnselectableDates((Date[])null);
            fail("null array must throw NPE");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            calendarView.setUnselectableDates(new Date[] {new Date(), null});
            fail("null elements must throw NPE");
        } catch (NullPointerException e) {
            // expected
        }
    }

   
    /**
     * Issue #494-swingx: JXCalendar changed all passed-in dates
     *
     */
    @Test
    public void testCleanupCopyDate() {
        JXCalendar calendarView = new JXCalendar();
        Date copy = new Date(today.getTime());
        calendarView.setSelectionInterval(today, today);
        assertEquals("the date used for selection must be unchanged", copy, today);
    }
    /**
     * test cover method: isSelectedDate
     *
     */
    @Test
    public void testIsSelectedDate() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionDate(today);
        assertTrue(calendarView.isSelected(today));
        assertTrue(calendarView.isSelected(startOfDay(today)));
    }
    
    /**
     * Sanity: test against regression
     * test cover method: isSelectedDate
     *
     */
    @Test
    public void testIsSelectedDate494() {
        JXCalendar calendarView = new JXCalendar();
        Date copy = new Date(today.getTime());
        calendarView.setSelectionDate(today);
        // use today
        calendarView.isSelected(today);
        assertEquals("date must not be changed in isSelected", copy, today);
    }
   
    /**
     * test cover method: setSelectedDate
     *
     */
    @Test
    public void testSetSelectedDate() {
        JXCalendar calendarView = new JXCalendar();
        Date copy = new Date(today.getTime());
        calendarView.setSelectionDate(today);
        // sanity: date unchanged
        assertEquals(copy, today);
        assertEquals(startOfDay(today), calendarView.getSelectionDate());
        calendarView.setSelectionDate(null);
        assertTrue(calendarView.isSelectionEmpty());
    }
    

    /**
     * test new (convenience) api on JXCalendar
     *
     */
    @Test
    public void testGetSelected() {
        JXCalendar calendarView = new JXCalendar();
        assertNull(calendarView.getSelectionDate());
        calendarView.setSelectionInterval(today, today);
        assertEquals("same day", startOfDay(today), calendarView.getSelectionDate());
        // clear selection
        calendarView.clearSelection();
        assertNull(calendarView.getSelectionDate());
    }
    
    
    @Test
    public void testDefaultConstructor() {
        JXCalendar calendarView = new JXCalendar(Locale.US);
        assertTrue(calendarView.isSelectionEmpty());
        assertTrue(SelectionMode.SINGLE_SELECTION == calendarView.getSelectionMode());
        assertTrue(Calendar.SUNDAY == calendarView.getFirstDayOfWeek());
    }

    @Test
    public void testLocale() {
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale l : locales) {
            JComponent.setDefaultLocale(l);

            JXCalendar calendarView = new JXCalendar();
            Locale locale = calendarView.getLocale();
            Calendar cal = Calendar.getInstance(locale);
            int expectedFirstDayOfWeek = cal.getFirstDayOfWeek();

            assertTrue(expectedFirstDayOfWeek == calendarView.getFirstDayOfWeek());
        }
    }

    @Test
    public void testEmptySelectionInitial() {
        JXCalendar calendarView = new JXCalendar();
        assertTrue(calendarView.isSelectionEmpty());
        SortedSet<Date> selection = calendarView.getSelection();
        assertTrue(selection.isEmpty());
    }
    
    @Test
    public void testEmptySelectionClear() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionInterval(today, today);
        // sanity
        assertTrue(1 == calendarView.getSelection().size());

        calendarView.clearSelection();
        assertTrue(calendarView.isSelectionEmpty());
        assertTrue(calendarView.getSelection().isEmpty());
    }

    @Test
    public void testSelectionModes() {
        JXCalendar calendarView = new JXCalendar();
        assertEquals(SelectionMode.SINGLE_SELECTION, calendarView
                .getSelectionMode());
        for (SelectionMode mode : SelectionMode.values()) {
            calendarView.setSelectionMode(mode);
            assertEquals(mode, calendarView.getSelectionModel().getSelectionMode());
            assertEquals(mode, calendarView.getSelectionMode());
        }

    }

    @Test
    public void testSingleSelection() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionMode(SelectionMode.SINGLE_SELECTION);

        calendarView.setSelectionInterval(yesterday, yesterday);
        assertTrue(1 == calendarView.getSelection().size());
        assertEquals(startOfDay(yesterday), calendarView.getFirstSelectionDate());

        calendarView.setSelectionInterval(yesterday, afterTomorrow);
        assertTrue(1 == calendarView.getSelection().size());
        assertEquals(startOfDay(yesterday), calendarView.getFirstSelectionDate());
    }

    @Test
    public void testSingleIntervalSelection() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);

        calendarView.setSelectionInterval(yesterday, yesterday);
        assertTrue(1 == calendarView.getSelection().size());
        assertEquals(startOfDay(yesterday), calendarView.getFirstSelectionDate());

        calendarView.setSelectionInterval(yesterday, tomorrow);
        
        assertTrue(3 == calendarView.getSelection().size());
        assertEquals(startOfDay(yesterday), calendarView.getFirstSelectionDate());
        assertEquals(startOfDay(tomorrow), calendarView.getLastSelectionDate());

    }



    @Test
    public void testMultipleIntervalSelection() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);

        calendarView.setSelectionInterval(yesterday, yesterday);
        calendarView.addSelectionInterval(afterTomorrow, afterTomorrow);
        
        assertEquals(2, calendarView.getSelection().size());
        assertEquals(startOfDay(yesterday), calendarView.getFirstSelectionDate());
        assertEquals(startOfDay(afterTomorrow), calendarView.getLastSelectionDate());
    }



    @Test
    public void testDateSelectionListener() {
        JXCalendar calendarView = new JXCalendar();
        DateSelectionReport listener = new DateSelectionReport();
        calendarView.getSelectionModel().addDateSelectionListener(listener);

        Date date = new Date();
        calendarView.setSelectionInterval(date, date);
        assertThat(listener.getEventCount(), is(1));
        assertThat(listener.getLastEvent().getEventType(), is(EventType.DATES_SET));
    }


    private Date startOfDay(Date date) {
        return CalendarUtils.startOfDay(calendar, date);
    }
 
    private Date endOfDay(Date date) {
        return CalendarUtils.endOfDay(calendar, date);
    }
}