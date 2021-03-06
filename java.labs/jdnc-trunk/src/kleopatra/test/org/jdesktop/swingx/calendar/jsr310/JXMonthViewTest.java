/**
 * $Id: JXMonthViewTest.java 3339 2011-02-04 17:03:23Z kleopatra $
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
package org.jdesktop.swingx.calendar.jsr310;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.time.calendar.Clock;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;
import org.jdesktop.swingx.calendar.jsr310.Date310SelectionModel.SelectionMode;
import org.jdesktop.test.ActionReport;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Test case for <code>JXMonthView</code>
 *
 * There's another class with passing unit tests for JXMonthView (JXMonthViewVisualTest)
 * because this 
 * extends mock while the other extends InteractiveTestCase. Both are expected
 * to pass.
 * 
 * @author Joshua Outwater
 */
@RunWith(JUnit4.class)
public class JXMonthViewTest extends InteractiveTestCase {
    private static final Logger LOG = Logger.getLogger(JXMonthViewTest.class
            .getName());
    private Locale componentLocale;
    // pre-defined reference dates - all relative to current date at around 5 am
    private LocalDateTime today;
    private LocalDateTime tomorrow;
    private LocalDateTime afterTomorrow;
    private LocalDateTime yesterday;

    private JXMonthView monthView;
    private Clock clock;
    
    @Override
    @Before
       public void setUp() {
        setUpCalendar();
        //the test is configured for a US defaulted system
        //the localization tests handle actual localization issues
        componentLocale = JComponent.getDefaultLocale();
//        LOG.info("componentLocale " + componentLocale);
//        JComponent.setDefaultLocale(Locale.US);
        monthView = new JXMonthView();
    }

    /**
     * Initializes the calendar to the default instance and the predefined dates
     * in the coordinate system of the calendar. Note that the hour is set
     * to "about 5" in all dates, to be reasonably well into the day. The time
     * fields of all dates are the same, the calendar is pre-set with the
     * today field.
     */
    protected void setUpCalendar() {
        clock = Clock.systemDefaultZone();
        today = clock.dateTime();
        today = today.withHourOfDay(5);
        
        yesterday = today.minus(Period.ofDays(1));
        tomorrow = today.plus(Period.ofDays(1));
        afterTomorrow = today.plus(Period.ofDays(2));
    }

    @Override
    @After
       public void tearDown() {
        JComponent.setDefaultLocale(componentLocale);
    }

    @Test
    public void testZoomableProperty() {
        assertFalse("default zoomable is off", monthView.isZoomable());
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setZoomable(true);
        TestUtils.assertPropertyChangeEvent(report, "zoomable", false, true);
        assertTrue("traversable follows zoomable", monthView.isTraversable());
    }
    
    /**
     * Issue #932-swingx: ui overwrites custom settings.
     * test daysoftheweekforeground property.
     * 
     */
    @Test
    public void testDaysOfTheWeekForegroundUpdateUI() {
        Color old = monthView.getDaysOfTheWeekForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        monthView.setDaysOfTheWeekForeground(color);
        monthView.updateUI();
        assertEquals(color, monthView.getDaysOfTheWeekForeground());
    }

//    /**
//     * Issue #??-swingx: test accaptable values in setDayForeground.
//     * Doc'ed to accept Calendar.SUNDAY - Calendar.SATURDAY, but not 
//     * enforced.
//     * 
//     */
//    @Test(expected=IllegalArgumentException.class)
//    public void testDayForegroundBeforeSunday() {
//        monthView.setDayForeground(Calendar.SUNDAY - 1, Color.RED);
//    }
//
//    /**
//     * Issue #??-swingx: test accaptable values in setDayForeground.
//     * Doc'ed to accept Calendar.SUNDAY - Calendar.SATURDAY, but not 
//     * enforced.
//     * 
//     */
//    @Test(expected=IllegalArgumentException.class)
//    public void testDayForegroundAfterSaturday() {
//        monthView.setDayForeground(Calendar.SATURDAY + 1, Color.RED);
//    }

 
    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringInsets property.
     * 
     */
    @Test
    public void testPreferredRowCount() {
        int old = monthView.getPreferredRowCount();
        int color = old + 20;
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setPreferredRowCount(color);
        assertEquals(color, monthView.getPreferredRowCount());
        TestUtils.assertPropertyChangeEvent(report, "preferredRowCount", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringInsets property.
     * 
     */
    @Test
    public void testPreferredColumnCount() {
        int old = monthView.getPreferredColumnCount();
        int color = old + 20;
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setPreferredColumnCount(color);
        assertEquals(color, monthView.getPreferredColumnCount());
        TestUtils.assertPropertyChangeEvent(report, "preferredColumnCount", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringInsets property.
     * 
     */
    @Test
    public void testMonthStringInsets() {
        Insets old = monthView.getMonthStringInsets();
        Insets color = new Insets(21, 22, 23, 24);
        assertFalse("sanity: the new insets are not equals", color.equals(old));
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setMonthStringInsets(color);
        assertEquals(color, monthView.getMonthStringInsets());
        TestUtils.assertPropertyChangeEvent(report, "monthStringInsets", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringBackground property.
     * 
     */
    @Test
    public void testTodayBackground() {
        Color old = monthView.getTodayBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setTodayBackground(color);
        assertEquals(color, monthView.getTodayBackground());
        TestUtils.assertPropertyChangeEvent(report, "todayBackground", old, color);
    }

    
    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringBackground property.
     * 
     */
    @Test
    public void testMonthStringBackground() {
        Color old = monthView.getMonthStringBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setMonthStringBackground(color);
        assertEquals(color, monthView.getMonthStringBackground());
        TestUtils.assertPropertyChangeEvent(report, "monthStringBackground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting monthStringForeground property.
     * 
     */
    @Test
    public void testMonthStringForeground() {
        Color old = monthView.getMonthStringForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setMonthStringForeground(color);
        assertEquals(color, monthView.getMonthStringForeground());
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
        Font old = monthView.getFont();
        Font padding = old.deriveFont(old.getSize2D() * 2);
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setFont(padding);
        assertEquals(padding, monthView.getFont());
        TestUtils.assertPropertyChangeEvent(report, "font", old, padding);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting boxPaddingY property.
     * 
     */
    @Test
    public void testBoxPaddingY() {
        int old = monthView.getBoxPaddingY();
        int padding = old + 2;
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setBoxPaddingY(padding);
        assertEquals(padding, monthView.getBoxPaddingY());
        TestUtils.assertPropertyChangeEvent(report, "boxPaddingY", old, padding);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting boxPaddingX property.
     * 
     */
    @Test
    public void testBoxPaddingX() {
        int old = monthView.getBoxPaddingX();
        int padding = old + 2;
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setBoxPaddingX(padding);
        assertEquals(padding, monthView.getBoxPaddingX());
        TestUtils.assertPropertyChangeEvent(report, "boxPaddingX", old, padding);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testDaysOfTheWeekForeground() {
        Color old = monthView.getDaysOfTheWeekForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setDaysOfTheWeekForeground(color);
        assertEquals(color, monthView.getDaysOfTheWeekForeground());
        TestUtils.assertPropertyChangeEvent(report, "daysOfTheWeekForeground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testFlaggedDayForeground() {
        Color old = monthView.getFlaggedDayForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setFlaggedDayForeground(color);
        assertEquals(color, monthView.getFlaggedDayForeground());
        TestUtils.assertPropertyChangeEvent(report, "flaggedDayForeground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testSelectionForeground() {
        Color old = monthView.getSelectionForeground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setSelectionForeground(color);
        assertEquals(color, monthView.getSelectionForeground());
        TestUtils.assertPropertyChangeEvent(report, "selectionForeground", old, color);
    }

    /**
     * Issue #931-swingx: missing change notification.
     * test setting selectionBackground property.
     * 
     */
    @Test
    public void testSelectionBackground() {
        Color old = monthView.getSelectionBackground();
        Color color = Color.PINK;
        if (color.equals(old)) {
            color = Color.MAGENTA;
        }
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setSelectionBackground(color);
        assertEquals(color, monthView.getSelectionBackground());
        TestUtils.assertPropertyChangeEvent(report, "selectionBackground", old, color);
    }
    
    @Test
    public void testInitialOpaque() {
        assertTrue(monthView.isOpaque());
    }
    
    @Test
    public void testInitialBoxPadding() {
        assertTrue(monthView.getBoxPaddingX() > 0);
        assertEquals(UIManager.getInt("JXMonthView.boxPaddingX"), monthView.getBoxPaddingX());
        assertTrue(monthView.getBoxPaddingY() > 0);
        assertEquals(UIManager.getInt("JXMonthView.boxPaddingY"), monthView.getBoxPaddingY());
        
    }
    
    
    /**
     * Issue #752-swingx: custom daysOfWeek lost in updateUI
     */
    @Test
    public void testDaysOfWeekUpdateUI() {
        fail("TODO: support configurable dayOfWeek strings in JXMonthView");
//        JXMonthView monthView = new JXMonthView();
//        String[] days = {"S", "M", "D", "M", "D", "F", "S"};
//        monthView.setDaysOfTheWeek(days);
//        assertEquals(Arrays.asList(days), Arrays.asList(monthView.getDaysOfTheWeek()));
//        monthView.updateUI();
//        assertEquals(Arrays.asList(days), Arrays.asList(monthView.getDaysOfTheWeek()));
    }
    
    /**
     * Issue #752-swingx: custom daysOfWeek lost in updateUI
     */
    @Test
    public void testDaysOfWeekUpdateUIAllowNull() {
        fail("TODO: support configurable dayOfWeek strings in JXMonthView");
//        JXMonthView monthView = new JXMonthView();
//        String[] days = null;
//        monthView.setDaysOfTheWeek(days);
//        assertNotNull("daysOfTheWeek must not be null", monthView.getDaysOfTheWeek());
       
   }

    /**
     * Issue #752-swingx: custom daysOfWeek lost in updateUI
     */
    @Test
    public void testDaysOfWeekInitial() {
        JXMonthView monthView = new JXMonthView();
        assertNotNull("daysOfTheWeek must not be null", monthView.getDayOfWeekTexts());
    }
    
    /**
     * Issue #752-swingx: custom daysOfWeek lost in updateUI
     */
    @Test
    public void testDaysOfWeekCopied() {
        fail("TODO: support configurable dayOfWeek strings in JXMonthView");
//        JXMonthView monthView = new JXMonthView();
//        assertNotSame(monthView.getDayOfWeekTexts(), monthView.getDayOfWeekTexts());
    }
    
    /**
     * Issue #752-swingx: custom daysOfWeek lost in updateUI
     */
    @Test
    public void testDaysOfWeekReset() {
        fail("TODO: support configurable dayOfWeek strings in JXMonthView");
//        JXMonthView monthView = new JXMonthView();
//        // start off with ui-provided dates
//        String[] uiDays = monthView.getDaysOfTheWeek();
//        String[] days = {"S", "M", "D", "M", "D", "F", "S"};
//        // set the custom
//        monthView.setDaysOfTheWeek(days);
//        // sanity
//        assertEquals(Arrays.asList(days), Arrays.asList(monthView.getDaysOfTheWeek()));
//        monthView.setDaysOfTheWeek(null);
//        assertEquals("use ui-provided daysOfWeek after reset", 
//                Arrays.asList(uiDays), Arrays.asList(monthView.getDaysOfTheWeek()));
    }


 

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingLeadingNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingLeadingDays();
        monthView.setShowingLeadingDays(!showing);
        TestUtils.assertPropertyChangeEvent(report, "showingLeadingDays", showing, !showing);
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingLeadingNoNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingLeadingDays();
        monthView.setShowingLeadingDays(showing);
        assertEquals(0, report.getEventCount("showingLeadingDates"));
    }



    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingTrailingNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingTrailingDays();
        monthView.setShowingTrailingDays(!showing);
        TestUtils.assertPropertyChangeEvent(report, "showingTrailingDays", showing, !showing);
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingTrailingNoNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingTrailingDays();
        monthView.setShowingTrailingDays(showing);
        assertEquals(0, report.getEventCount("showingTrailingDays"));
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingWeekNumbersNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingWeekNumber();
        monthView.setShowingWeekNumber(!showing);
        TestUtils.assertPropertyChangeEvent(report, "showingWeekNumber", showing, !showing);
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testShowingWeekNumbersNoNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isShowingWeekNumber();
        monthView.setShowingWeekNumber(showing);
        assertEquals(0, report.getEventCount("showingWeekNumber"));
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testTraversableNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isTraversable();
        monthView.setTraversable(!showing);
        TestUtils.assertPropertyChangeEvent(report, "traversable", showing, !showing);
    }

    /**
     * Issue #751-swingx: property naming violations
     */
    @Test
    public void testTraversableNoNotification() {
        JXMonthView monthView = new JXMonthView();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        boolean showing = monthView.isTraversable();
        monthView.setTraversable(showing);
        assertEquals(0, report.getEventCount("traversable"));
    }


    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: test that monthView is updated to model after setSelectionModel.
     */
    @Test
    public void testCalendarsSetModel() {
//        JXMonthView monthView = new JXMonthView();
//        int firstDayOfWeek = monthView.getFirstDayOfWeek();
//        Locale locale = Locale.UK;
//        if (locale.equals(monthView.getLocale())) {
//            locale = Locale.FRENCH;
//        }
//        TimeZone tz = TimeZone.getTimeZone("GMT+4");
//        if (monthView.getTimeZone().equals(tz)) {
//            tz = TimeZone.getTimeZone("GMT+5");
//        }
//        Date310SelectionModel model = new Day310SelectionModel(locale);
//        model.setTimeZone(tz);
//        int modelMinimal = model.getMinimalDaysInFirstWeek();
//        monthView.setSelectionModel(model);
//        assertEquals("timeZone must be updated from model", tz, monthView.getTimeZone());
//        assertEquals("Locale must be updated from model", locale, monthView.getLocale());
//        // be aware if it makes no sense to assert
//        if (firstDayOfWeek != model.getFirstDayOfWeek()) {
//            assertEquals("firstDayOfWeek must be updated from model", 
//                    model.getFirstDayOfWeek(), monthView.getFirstDayOfWeek());
//        } else {
//            LOG.info("cannot assert firstDayOfWeek - was same");
//        }
//        // @KEEP - this is an open issue: monthView must not change the
//        // model settings but minimalDaysInFirstWeek > 1 confuse the 
//        // BasicMonthViewUI - remove if passing in xIssues
//        assertEquals("model minimals must not be changed", 
//                modelMinimal, model.getMinimalDaysInFirstWeek());
    }


    /**
     * Issue #736-swingx: model and monthView cal not synched.
     * 
     * Here: test that model settings are respected in setModel - minimaldays.
     * 
     * Model must not reset minimalDaysInfirstWeek, but Locales with values
     * > 1 confuse the BasicDatePickerUI - need to track down and solve there.
     */
    @Test
    public void testCalendarsSetModelUnchangedMinimalDaysInFirstWeek() {
//        JXMonthView monthView = new JXMonthView();
//        Date310SelectionModel model = new Day310SelectionModel();
//        int first = model.getMinimalDaysInFirstWeek() + 1;
//        model.setMinimalDaysInFirstWeek(first);
//        monthView.setSelectionModel(model);
//        assertEquals("model minimals must not be changed", 
//                first, model.getMinimalDaysInFirstWeek());
    }
    


    
    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: setting the timezone clears the flagged dates, must notify of change.
      */
    @Test
    public void testFlaggedDatesTimeZoneNotifyOnChange() {
        fail("TODO: define behaviour of date state on change of TimeZone");
//        JXMonthView monthView = new JXMonthView();
//        monthView.setFlaggedDates(today);
//        SortedSet<LocalDateTime> flagged = monthView.getFlaggedDates();
//        // config with a known timezone and date
//        TimeZone tz = TimeZone.getTimeZone("GMT+4");
//        if (tz.equals(monthView.getTimeZone())) {
//            tz = TimeZone.getTimeZone("GMT+5");
//        }
//        PropertyChangeReport report = new PropertyChangeReport();
//        monthView.addPropertyChangeListener(report);
//        monthView.setTimeZone(tz);
//        TestUtils.assertPropertyChangeEvent(report, "flaggedDates", 
//                flagged, monthView.getFlaggedDates(), false);
    }
    
    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: setting the timezone clears the flagged dates, must notify of change.
      */
    @Test
    public void testFlaggedDatesTimeZoneNotNotifyWithoutChange() {
        fail("TODO: define behaviour of date state on change of TimeZone");
//        JXMonthView monthView = new JXMonthView();
//        // config with a known timezone and date
//        TimeZone tz = TimeZone.getTimeZone("GMT+4");
//        if (tz.equals(monthView.getTimeZone())) {
//            tz = TimeZone.getTimeZone("GMT+5");
//        }
//        PropertyChangeReport report = new PropertyChangeReport();
//        monthView.addPropertyChangeListener(report);
//        monthView.setTimeZone(tz);
//        assertEquals("no change in flaggedDates must not fire", 0, report.getEventCount("flaggedDates"));
    }
    
    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: Locale changed in monthView.
     */
    @Test
    public void testCalendarsLocaleChangedMonthView() {
        JXMonthView monthView = new JXMonthView();
        Locale locale = Locale.UK;
        if (locale.equals(monthView.getLocale())) {
            locale = Locale.FRENCH;
        }
        monthView.setLocale(locale);
        assertEquals("locale set in monthView must be passed to model", 
                locale, monthView.getSelectionModel().getLocale());
    }
    
    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: Locale changed in selection model.
     */
    @Test
    public void testCalendarsLocaleChangedModel() {
        JXMonthView monthView = new JXMonthView();
        Locale locale = Locale.UK;
        if (locale.equals(monthView.getLocale())) {
            locale = Locale.FRENCH;
        }
        monthView.getSelectionModel().setLocale(locale);
        assertEquals("locale set in model must be passed to monthView", 
                locale, monthView.getLocale());
    }
    
    

    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: Locale changed in monthView.
     */
    @Test
    public void testCalendarsLocaleContructor() {
        Locale locale = Locale.UK;
        if (locale.equals(JComponent.getDefaultLocale())) {
            locale = Locale.FRENCH;
        }
        JXMonthView monthView = new JXMonthView(locale);
        assertEquals("initial locale in constructor must be passed to model", 
                locale, monthView.getSelectionModel().getLocale());
    }

//    /**
//     * Issue #733-swingx: model and monthView cal not synched.
//     * FirstDayofWeek no longer settable.
//     * Here: set first day of week in monthView.
//     */
//    @Test
//    public void testCalendarsFirstDayOfWeekMonthView() {
//        JXMonthView monthView = new JXMonthView();
//        int first = monthView.getFirstDayOfWeek() + 1;
//        // sanity
//        assertTrue(first <= Calendar.SATURDAY);
//        monthView.setFirstDayOfWeek(first);
//        assertEquals(first, monthView.getFirstDayOfWeek());
//        assertEquals(first, monthView.getCalendar().getFirstDayOfWeek());
//        assertEquals(first, monthView.getSelectionModel().getFirstDayOfWeek());
//    }
    
//    /**
//     * Issue #733-swingx: model and monthView cal not synched.
//     * 
//     * Here: set first day of week in model.
//     */
//    @Test
//    public void testCalendarsFirstDayOfWeekModel() {
//        JXMonthView monthView = new JXMonthView();
//        int first = monthView.getFirstDayOfWeek() + 1;
//        // sanity
//        assertTrue(first <= Calendar.SATURDAY);
//        monthView.getSelectionModel().setFirstDayOfWeek(first);
//        assertEquals(first, monthView.getFirstDayOfWeek());
//        assertEquals(first, monthView.getCalendar().getFirstDayOfWeek());
//        assertEquals(first, monthView.getSelectionModel().getFirstDayOfWeek());
//    }
    /**
     * Issue #733-swingx: model and monthView cal not synched.
     * 
     * Here: first day of week.
     */
    @Test
    public void testCalendarsFirstDayOfWeekInitial() {
        JXMonthView monthView = new JXMonthView();
        assertEquals(monthView.getFirstDayOfWeek(), 
                monthView.getSelectionModel().getFirstDayOfWeek());
    }
//    /**
//     * Issue #733-swingx: model and monthView cal not synched.
//     * 
//     * Here: minimal days of first week.
//     */
//    @Test
//    public void testCalendarsMinimalDaysOfFirstWeekInitial() {
//        JXMonthView monthView = new JXMonthView();
//        int first = monthView.getCalendar().getMinimalDaysInFirstWeek();
//        assertEquals(first, monthView.getSelectionModel().getMinimalDaysInFirstWeek());
//    }
    
    
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        monthView.setSelectionDate(date);
        assertTrue(monthView.isSelected(date));
        assertTrue(monthView.getSelectionModel().isSelected(date));
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        monthView.setSelectionDate(date);
        assertEquals(monthView.getSelectionDate(), 
                monthView.getSelectionModel().getFirstSelectionDate());
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        monthView.setUnselectableDates(date);
        assertTrue(monthView.isUnselectableDate(date));
        assertTrue(monthView.getSelectionModel().isUnselectableDate(date));
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        SortedSet<LocalDateTime> unselectables = new TreeSet<LocalDateTime>();
        unselectables.add(date);
        monthView.getSelectionModel().setUnselectableDates(unselectables);
        assertTrue(monthView.getSelectionModel().isUnselectableDate(date));
        assertTrue(monthView.isUnselectableDate(date));
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        monthView.getSelectionModel().setSelectionInterval(date, date);
        assertTrue(monthView.getSelectionModel().isSelected(date));
        assertTrue(monthView.isSelected(date));
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = tomorrow;
        monthView.getSelectionModel().setSelectionInterval(date, date);
        assertEquals(monthView.getSelectionModel().getFirstSelectionDate(), 
                monthView.getSelectionDate());
    }
    

    /**
     * #703-swingx: set date to first of next doesn't update the view.
     * 
     * Behaviour is consistent with core components, must not update
     * 
     */
    @Test
    public void testAutoScrollOnSelection() {
        JXMonthView us = new JXMonthView();
        us.setSelectionDate(today);
        LocalDateTime first = us.getFirstDisplayedDay();
        LocalDateTime nextMonth = DateTimeUtils.endOfMonth(today).plusDays(2);
        us.setSelectionDate(nextMonth);
        assertEquals(first, us.getFirstDisplayedDay());
    }

    /**
     * #705-swingx: revalidate must not reset first firstDisplayed.
     * 
     * 
     */
    @Test
    public void testAutoScrollOnSelectionRevalidate() throws InterruptedException, InvocationTargetException {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        final JXMonthView us = new JXMonthView();
//        final Calendar today = Calendar.getInstance();
//        CalendarUtils.endOfMonth(today);
        us.setSelectionDate(today);
        final JXFrame frame = new JXFrame();
        frame.add(us);
        final LocalDateTime first = us.getFirstDisplayedDay();
        us.setSelectionDate(DateTimeUtils.endOfMonth(today).plusDays(2));
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                us.revalidate();
                // need to validate frame - why?
                frame.validate();
                assertEquals("firstDisplayed must not be changed on revalidate", 
                        first, us.getFirstDisplayedDay());
//                assertEquals(first, us.getFirstDisplayedDate());
//                fail("weird (threading issue?): the firstDisplayed is changed in layoutContainer - not testable here");
            }
        });
    }
    
    /**
     * Issue 711-swingx: today is notify-only property.
     * Today is start of day.
     */
    @Test
    public void testTodayInitial() {
        fail("TODO: define, implement today in monthView");
//        JXMonthView monthView = new JXMonthView();
//        CalendarUtils.startOfDay(calendar);
//        assertEquals(calendar.getTime(), monthView.getToday());
    }
    
    /**
     * Issue 711-swingx: today is notify-only property.
     * Increment sets to start of day of tomorrow.
     */
    @Test
    public void testTodayIncrement() {
        fail("TODO: define, implement today in monthView");
//        JXMonthView monthView = new JXMonthView();
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        CalendarUtils.startOfDay(calendar);
//        monthView.incrementToday();
//        assertEquals(calendar.getTime(), monthView.getToday());
    }
    
    
    /**
     * Issue 711-swingx: today is notify-only property.
     * SetToday should 
     */
    @Test
    public void testTodaySetNotification() {
        fail("TODO: define, implement today in monthView");
//        JXMonthView monthView = new JXMonthView();
//        LocalDateTime today = monthView.getToday();
//        // tomorrow
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        PropertyChangeReport report = new PropertyChangeReport();
//        monthView.addPropertyChangeListener(report);
//        monthView.setToday(calendar.getTime());
//        CalendarUtils.startOfDay(calendar);
//        TestUtils.assertPropertyChangeEvent(report, "today", 
//                today, calendar.getTime());
    }

    
    /**
     * Issue 711-swingx: today is notify-only property.
     * SetToday should 
     */
    @Test
    public void testTodaySet() {
        fail("TODO: define, implement today in monthView");
//        JXMonthView monthView = new JXMonthView();
//        // tomorrow
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        monthView.setToday(calendar.getTime());
//        CalendarUtils.startOfDay(calendar);
//        assertEquals(calendar.getTime(), monthView.getToday());
    }

    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that today is unchanged.
     */
    @Test
    public void testUpdateUIToday() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getToday();
        monthView.updateUI();
        assertEquals(first, monthView.getToday());
    };

    
    /**
     * Issue #711-swingx: remove fake property change notification.
     * 
     * Here: test that ensureVisibleDate fires once only.
     */
    @Test
    public void testEnsureVisibleDateNofication() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime firstDisplayedDate = monthView.getFirstDisplayedDay();
        // previous month
        LocalDateTime previousMonth = firstDisplayedDate.minusMonths(1);
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.ensureDateVisible(previousMonth);
//        CalendarUtils.startOfMonth(calendar);
        TestUtils.assertPropertyChangeEvent(report, "firstDisplayedDay", 
                firstDisplayedDate, DateTimeUtils.startOfMonth(previousMonth), false);
    }

    
    /**
     * Issue #711-swingx: remove fake property change notification.
     * 
     * Here: test that setFirstDisplayedDate fires once only.
     */
    @Test
    public void testFirstDisplayedDateNofication() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime firstDisplayedDate = monthView.getFirstDisplayedDay();
        // previous month
        LocalDateTime previousMonth = firstDisplayedDate.minusMonths(1);
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setFirstDisplayedDay(previousMonth);
//      CalendarUtils.startOfMonth(calendar);
        TestUtils.assertPropertyChangeEvent(report, "firstDisplayedDay", 
                firstDisplayedDate, DateTimeUtils.startOfMonth(previousMonth), false);
    }
    
    /**
     * Issue #708-swingx
     * 
     * test update of lastDisplayedDate if resized.
     */
    @Test
    public void testLastDisplayedOnResize() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        // get a reference width so we can simulate a one-month resize
        JXMonthView compare = new JXMonthView();
        compare.setPreferredColumnCount(2);
        JXMonthView monthView = new JXMonthView();
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        LocalDateTime last = monthView.getLastDisplayedDay().plusMonths(1);
        // set a size that should guarantee the same number of columns as the compare monthView
        frame.setSize(compare.getPreferredSize().width + 50, monthView.getPreferredSize().height + 50);
        frame.validate();
//        // build a date corresponding to the expected end of next month
//        calendar.setTime(last);
//        // next month
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//        CalendarUtils.endOfMonth(calendar);
        assertEquals(DateTimeUtils.endOfMonth(last), monthView.getLastDisplayedDay());
    }

    
    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that firstDisplayedDate is unchanged.
     */
    @Test
    public void testUpdateUIFirst() {
        final JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getFirstDisplayedDay();
        monthView.updateUI();
        assertEquals(first, monthView.getFirstDisplayedDay());
    };


    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that lastDisplayedDate is unchanged.
     */
    @Test
    public void testUpdateUILast() {
        final JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getLastDisplayedDay();
        monthView.updateUI();
        assertEquals(first, monthView.getLastDisplayedDay());
    };



    /**
     * safety net: add api ensureDateVisible with LocalDateTime parameter
     */
    @Test
    public void testEnsureDateVisibleDateParamNextMonth() {
        JXMonthView monthView = new JXMonthView();
//        Calendar temp = (Calendar) calendar.clone();
//        CalendarUtils.startOfMonth(temp);
        LocalDateTime temp = DateTimeUtils.startOfMonth(today);
        assertEquals("sanity..", temp, monthView.getFirstDisplayedDay());
        LocalDateTime nextMonth = temp.plusMonths(1).plusDays(3);
        monthView.ensureDateVisible(nextMonth);
        assertEquals("must be scrolled to next month", 
                DateTimeUtils.startOfMonth(nextMonth), monthView.getFirstDisplayedDay());
    }

    /**
     * safety net: add api ensureDateVisible with LocalDateTime parameter
     */
    @Test
    public void testEnsureDateVisibleDateParamThisMonth() {
        JXMonthView monthView = new JXMonthView();
//      Calendar temp = (Calendar) calendar.clone();
//      CalendarUtils.startOfMonth(temp);
        LocalDateTime temp = DateTimeUtils.startOfMonth(today);
        LocalDateTime first = monthView.getFirstDisplayedDay();
        assertEquals("sanity...", temp, first);
//        CalendarUtils.endOfMonth(calendar);
        LocalDateTime thisMonth = DateTimeUtils.endOfMonth(today);
        monthView.ensureDateVisible(thisMonth);
        assertEquals("same month, nothing changed", 
                first, monthView.getFirstDisplayedDay());
    }


    /**
     * safety net: refactor ensureDateVisible
     */
    @Test
    public void testEnsureDateVisibleNextYear() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime nextYear = today.plusYears(1);
        monthView.ensureDateVisible(nextYear);
        assertEquals("must be scrolled to next year", 
                DateTimeUtils.startOfMonth(nextYear), monthView.getFirstDisplayedDay());
    }
    
    /**
     * safety net: refactor ensureDateVisible
     */
    @Test
    public void testEnsureDateVisibleNextMonth() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime nextMonth = today.plusMonths(1);
        monthView.ensureDateVisible(nextMonth);
        assertEquals("must be scrolled to next month", 
                DateTimeUtils.startOfMonth(nextMonth), monthView.getFirstDisplayedDay());
    }

    /**
     * safety net: refactor ensureDateVisible
     */
    @Test
    public void testEnsureDateVisibleThisMonth() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getFirstDisplayedDay();
        monthView.ensureDateVisible(DateTimeUtils.endOfMonth(today));
        assertEquals("same month, nothing changed", first, monthView.getFirstDisplayedDay());
    }

    /**
     * safety net: move responsibility for lastDisplayedDate completely into ui.
     */
    @Test
    public void testLastDisplayedDateInitial() {
        JXMonthView monthView = new JXMonthView();
        assertEquals(DateTimeUtils.endOfMonth(monthView.getFirstDisplayedDay()), 
                monthView.getLastDisplayedDay());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Selected dates are "start of day" in the timezone they had been 
     * selected. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the selection. 
     */
    @Test
    public void testTimeZoneChangeClearSelection() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        LocalDateTime date = today;
//        monthView.setSelectionDate(date);
//        // sanity
//        assertTrue(monthView.isSelected(date));
//        monthView.setTimeZone(getTimeZone(monthView.getTimeZone(), CalendarUtils.THREE_HOURS));
//        // accidentally passes - because it is meaningful only in the timezone 
//        // it was set ...
//        assertFalse(monthView.isSelected(date));
//        assertTrue("selection must have been cleared", monthView.isSelectionEmpty());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetLowerBound() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        monthView.setLowerBound(yesterday);
//        monthView.setTimeZone(getTimeZone(monthView.getTimeZone(), CalendarUtils.THREE_HOURS));
//        assertEquals("lowerBound must have been reset", null, monthView.getLowerBound());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetUpperBound() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        monthView.setUpperBound(yesterday);
//        monthView.setTimeZone(getTimeZone(monthView.getTimeZone(), CalendarUtils.THREE_HOURS));
//        assertEquals("upperbound must have been reset", null, monthView.getUpperBound());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Flagged dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear them. 
     */
    @Test
    public void testTimeZoneChangeResetFlaggedDates() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        monthView.setFlaggedDates(new LocalDateTime[] {yesterday});
//        monthView.setTimeZone(getTimeZone(monthView.getTimeZone(), CalendarUtils.THREE_HOURS));
//        // accidentally passes - because it is meaningful only in the timezone 
//        // it was set ...
//        assertFalse(monthView.isFlaggedDate(yesterday));
//        // missing api
//        // assertEquals(0, monthView.getFlaggedDates().size());
//        assertFalse("flagged dates must have been cleared", monthView.hasFlaggedDates());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Unselectable dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear them. 
     */
    @Test
    public void testTimeZoneChangeResetUnselectableDates() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        monthView.setUnselectableDates(yesterday);
//        monthView.setTimeZone(getTimeZone(monthView.getTimeZone(), CalendarUtils.THREE_HOURS));
//        // accidentally passes - because it is meaningful only in the timezone 
//        // it was set ...
//        assertFalse(monthView.isUnselectableDate(yesterday));
//        // missing api on JXMonthView
//        assertEquals("unselectable dates must have been cleared", 
//                0, monthView.getSelectionModel().getUnselectableDates().size());
    }
    
    /**
     * test anchor: set to param as passed int setFirstDisplayedDate
     */
    @Test
    public void testAnchorDateInitial() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        // sometime next month
//        calendar.add(Calendar.MONTH, 1);
//        monthView.setFirstDisplayedDay(calendar.getTime());
//        assertEquals(calendar.getTime(), monthView.getAnchorDate());
//        CalendarUtils.startOfMonth(calendar);
//        assertEquals(calendar.getTime(), monthView.getFirstDisplayedDay());
    }

    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Here: test anchor invariant to time zone change
     */
    @Test
    public void testTimeZoneChangeAnchorInvariant() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        LocalDateTime anchor = monthView.getAnchorDate();
//        TimeZone timeZone = monthView.getTimeZone();
//        // just interested in a different timezone, no quantification intended
//        monthView.setTimeZone(getTimeZone(timeZone, CalendarUtils.THREE_HOURS));
//        assertEquals("anchor must be invariant to timezone change", 
//                anchor, monthView.getAnchorDate());
    }

    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Here: test that the first displayed date is offset by offset diff of
     * timezones. Configure the monthView with a fixed timezone to clear up the
     * mist ...
     * 
     * This did fail on the server on 31mar2008, us/pacific timezone, en_US
     * locale. Trying to sim the context then.
     * 
     * Failed again at Tue Sep 30 10:12:31 PDT 2008, en_US locale. Next
     * (testTimeZoneChangeOffsetFirstDisplayedDate) failed at the same time, 
     */
    @Test
    public void testTimeZoneChangeToday() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        // config with a known timezone and date
//        TimeZone tz = TimeZone.getTimeZone("GMT+4");
//        monthView.setTimeZone(tz);
//        Calendar calendar = Calendar.getInstance(tz);
//        LocalDateTime today = calendar.getTime();
//        monthView.setFirstDisplayedDay(today);
//        LocalDateTime anchor = monthView.getAnchorDate();
//        assertEquals(today, anchor);
//        LocalDateTime firstDisplayed = monthView.getFirstDisplayedDay();
//        calendar.setTime(firstDisplayed);
//        assertTrue(CalendarUtils.isStartOfMonth(calendar));
//
//        // get another timezone with known offset
//        TimeZone tzOther = TimeZone.getTimeZone("GMT+7");
//        // newOffset minus oldOffset (real time, adjusted to DST)
//        int oldOffset = tz.getOffset(anchor.getTime());
//        int newOffset = tzOther.getOffset(anchor.getTime());
//        int realOffset = oldOffset - newOffset;
//        monthView.setTimeZone(tzOther);
//        Calendar otherCalendar = Calendar.getInstance(tzOther);
//        otherCalendar.setTime(monthView.getFirstDisplayedDay());
//        assertTrue(CalendarUtils.isStartOfMonth(otherCalendar));
//        // PENDING JW: sure this is the correct direction of the shift?
//        // yeah, think so: the anchor is fixed, moving the timezone results
//        // in a shift into the opposite direction of the offset
//        Calendar localCal = Calendar.getInstance();
//        String server = "server locale: " + Locale.getDefault()
//                + "\n server timezone: " + localCal.getTimeZone()
//                + "\n server local time: " + localCal.getTime();
//        String timeZones = "first timeZone " + tz
//                + "\n first timezone offset / min " + oldOffset / (1000 * 60)
//                + "\n second timezone " + tzOther
//                + "\n second timezone offset / min " + newOffset / (1000 * 60);
//        String monthViewProps = "monthView locale: " + monthView.getLocale()
//                + "\n monthView anchor " + anchor
//                + "\n monthView firstDisplayed " + monthView.getFirstDisplayedDay();
//
//        assertEquals(
//                "first displayed must be offset by real offset "
//                        + "\n ********** spurious failure - so try extensiv debug output:"
//                        + "\n " + server 
//                        + "\n " + timeZones 
//                        + "\n " + monthViewProps, 
//                (realOffset) / (1000 * 60),
//                (monthView.getFirstDisplayedDay().getTime() - firstDisplayed.getTime()) / (1000 * 60));
/*
 * The output of failure:
 * 
 * junit.framework.AssertionFailedError: first displayed must be offset by real offset 
 ********** spurious failure - so try extensiv debug output:
 server locale: en_US
 server timezone: sun.util.calendar.ZoneInfo[id="US/Pacific",offset=-28800000,dstSavings=3600000,useDaylight=true,transitions=185,
lastRule=java.util.SimpleTimeZone[id=US/Pacific,offset=-28800000,dstSavings=3600000,useDaylight=true,startYear=0,startMode=3,startMonth=2,startDay=8,startDayOfWeek=1,startTime=7200000,startTimeMode=0,endMode=3,endMonth=10,endDay=1,endDayOfWeek=1,endTime=7200000,endTimeMode=0]]
 server local time: Tue Sep 30 10:12:31 PDT 2008
 first timeZone sun.util.calendar.ZoneInfo[id="GMT+04:00",offset=14400000,dstSavings=0,useDaylight=false,transitions=0,lastRule=null]
 first timezone offset / min 240
 second timezone sun.util.calendar.ZoneInfo[id="GMT+07:00",offset=25200000,dstSavings=0,useDaylight=false,transitions=0,lastRule=null]
 second timezone offset / min 420
 monthView locale: en_US
 monthView anchor Tue Sep 30 10:12:31 PDT 2008
 monthView firstDisplayed Tue Sep 30 10:00:00 PDT 2008 expected:<-180> but was:<43020>
        at org.jdesktop.swingx.JXMonthViewTest.testTimeZoneChangeToday(JXMonthViewTest.java:1385)
        at org.jmock.core.VerifyingTestCase.runBare(VerifyingTestCase.java:39)


 */
    
    }   

    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Here: test that the first displayed date is offset by offset diff of 
     * timezones.
     * Configure the monthView with a fixed timezone to clear up the mist ...
     * 
     * failed along with previous test, (Tue Sep 30 10:12:31 PDT 2008, en_US locale)
     * error out:
     * first displayed must be offset by real offset expected:
     *  <-10800000> but was:<2581200000>
     * 
     */
    @Test
    public void testTimeZoneChangeOffsetFirstDisplayedDate() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        // config with a known timezone and date
//        TimeZone tz = TimeZone.getTimeZone("GMT+4");
//        monthView.setTimeZone(tz);
//        Calendar calendar = Calendar.getInstance(tz);
//        LocalDateTime today = calendar.getTime();
//        monthView.setFirstDisplayedDay(today);
//        LocalDateTime anchor = monthView.getAnchorDate();
//        assertEquals(today, anchor);
//        LocalDateTime firstDisplayed = monthView.getFirstDisplayedDay();
//        calendar.setTime(firstDisplayed);
//        assertTrue(CalendarUtils.isStartOfMonth(calendar));
//        
//        // get another timezone with known offset
//        TimeZone tzOther = TimeZone.getTimeZone("GMT+7");
//        // newOffset minus oldOffset (real time, adjusted to DST)
//        int oldOffset = tz.getOffset(anchor.getTime());
//        int newOffset = tzOther.getOffset(anchor.getTime());
//        int realOffset = oldOffset - newOffset;
//        monthView.setTimeZone(tzOther);
//        Calendar otherCalendar = Calendar.getInstance(tzOther);
//        otherCalendar.setTime(monthView.getFirstDisplayedDay());
//        assertTrue(CalendarUtils.isStartOfMonth(otherCalendar));
//        // PENDING JW: sure this is the correct direction of the shift?
//        // yeah, think so: the anchor is fixed, moving the timezone results
//        // in a shift into the opposite direction of the offset
//        assertEquals("first displayed must be offset by real offset", 
//                realOffset,  monthView.getFirstDisplayedDay().getTime() - firstDisplayed.getTime());
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
//    private TimeZone getTimeZone(TimeZone timeZone, int diffRawOffset) {
//        int offset = timeZone.getRawOffset();
//        int newOffset = offset < 0 ? offset + diffRawOffset : offset - diffRawOffset;
//        String[] availableIDs = TimeZone.getAvailableIDs(newOffset);
//        TimeZone newTimeZone = TimeZone.getTimeZone(availableIDs[0]);
//        return newTimeZone;
//    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Here: test timezone fire
     */
    @Test
    public void testTimeZoneChangeNotification() {
        fail("TODO: time zone related behaviour");
//        JXMonthView monthView = new JXMonthView();
//        TimeZone timezone = monthView.getTimeZone();
//        int offset = timezone.getRawOffset();
//        int oneHour = 60 * 1000 * 60;
//        int newOffset = offset < 0 ? offset + oneHour : offset - oneHour;
//        String[] availableIDs = TimeZone.getAvailableIDs(newOffset);
//        TimeZone newTimeZone = TimeZone.getTimeZone(availableIDs[0]);
//        // sanity
//        assertFalse(timezone.equals(newTimeZone));
//        PropertyChangeReport report = new PropertyChangeReport();
//        monthView.addPropertyChangeListener(report);
//        monthView.setTimeZone(newTimeZone);
//        TestUtils.assertPropertyChangeEvent(report, 
//                "timeZone", timezone, newTimeZone, false);
    }
    

    /**
     * Issue #563-swingx: keybindings active if not focused.
     * Test that the bindings are dynamically installed when
     * shown in popup and de-installed if shown not in popup.
    */
    @Test
    public void testComponentInputMapEnabledControlsFocusedKeyBindings() {
        JXMonthView monthView = new JXMonthView();
        // initial: no bindings
        assertEquals("monthView must not have in-focused keyBindings", 0, 
                monthView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size());
        monthView.setComponentInputMapEnabled(true);
        // setting the flag installs bindings
        assertTrue("monthView must have in-focused keyBindings after showing in popup",  
              monthView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size() > 0);
        monthView.setComponentInputMapEnabled(false);
        // resetting the flag uninstalls the bindings
        assertEquals("monthView must not have in-focused keyBindings", 0, 
                monthView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).size());
    }

    /**
     * Test default value and property change notificateion of 
     * the componentInputMapEnabled property.
     *
     */
    @Test
    public void testComponentInputMapEnabled() {
        JXMonthView monthView = new JXMonthView();
        assertFalse("the default value must be false", 
                monthView.isComponentInputMapEnabled());
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setComponentInputMapEnabled(true);
        TestUtils.assertPropertyChangeEvent(report, 
                "componentInputMapEnabled", false, true, false);
        report.clear();
        monthView.setComponentInputMapEnabled(false);
        TestUtils.assertPropertyChangeEvent(report, 
                "componentInputMapEnabled", true, false, false);
    }
    
    /**
     * test doc'ed behaviour: model must not be null.
     *
     */
    @Test
    public void testSetModelNull() {
        JXMonthView monthView = new JXMonthView();
        assertNotNull(monthView.getSelectionModel());
        try {
            monthView.setSelectionModel(null);
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
        JXMonthView monthView = new JXMonthView();
        monthView.getSelectionModel().setAdjusting(true);
        monthView.commitSelection();
        assertFalse("commit must reset adjusting", 
                monthView.getSelectionModel().isAdjusting());
        monthView.getSelectionModel().setAdjusting(true);
        monthView.cancelSelection();
        assertFalse("cancel must reset adjusting", 
                monthView.getSelectionModel().isAdjusting());
        
    }
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions fire as expected.
     *
     */
    @Test
    public void testCommitCancelAPIFires() {
        JXMonthView picker = new JXMonthView();
        ActionReport report = new ActionReport();
        picker.addActionListener(report);
        picker.commitSelection();
        assertEquals(1, report.getEventCount());
        assertEquals(JXMonthView.COMMIT_KEY, report.getLastActionCommand());
        report.clear();
        picker.cancelSelection();
        assertEquals(1, report.getEventCount());
        assertEquals(JXMonthView.CANCEL_KEY, report.getLastActionCommand());
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions fire as expected.
     *
     */
    @Test
    public void testCommitCancelActionsFire() {
        JXMonthView picker = new JXMonthView();
        Action commitAction = picker.getActionMap().get(JXMonthView.COMMIT_KEY);
        ActionReport report = new ActionReport();
        picker.addActionListener(report);
        commitAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXMonthView.COMMIT_KEY, report.getLastActionCommand());
        report.clear();
        Action cancelAction = picker.getActionMap().get(JXMonthView.CANCEL_KEY);
        cancelAction.actionPerformed(null);
        assertEquals(1, report.getEventCount());
        assertEquals(JXMonthView.CANCEL_KEY, report.getLastActionCommand());
    }


    /**
     * Enhanced commit/cancel.
     * 
     * test that actions are registered.
     *
     */
    @Test
    public void testCommitCancelActionExist() {
        JXMonthView picker = new JXMonthView();
        assertNotNull(picker.getActionMap().get(JXMonthView.CANCEL_KEY));
        assertNotNull(picker.getActionMap().get(JXMonthView.COMMIT_KEY));
    }
    
    /**
     * Enhanced commit/cancel.
     * 
     * test that actions are the same for new/old cancel/accept.
     *
     */
    @Test
    public void testCommitCancelSameAsOld() {
        JXMonthView picker = new JXMonthView();
        assertSame(picker.getActionMap().get("cancelSelection"),
                picker.getActionMap().get(JXMonthView.CANCEL_KEY));
        assertSame(picker.getActionMap().get("acceptSelection"),
                picker.getActionMap().get(JXMonthView.COMMIT_KEY));
    }

    /**
     * BasicMonthViewUI: use adjusting api in keyboard actions.
     * Here: test reset in cancel action.
     */
    @Test
    public void testAdjustingResetOnCancel() {
        JXMonthView view = new JXMonthView();
        Action select = view.getActionMap().get("selectNextDay");
        select.actionPerformed(null);
        Date310SelectionReport report = new Date310SelectionReport(view.getSelectionModel());
        Action cancel = view.getActionMap().get("cancelSelection");
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
        JXMonthView view = new JXMonthView();
        Action select = view.getActionMap().get("selectNextDay");
        select.actionPerformed(null);
        Date310SelectionReport report = new Date310SelectionReport(view.getSelectionModel());
        Action cancel = view.getActionMap().get("acceptSelection");
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
        JXMonthView view = new JXMonthView();
        Date310SelectionReport report = new Date310SelectionReport(view.getSelectionModel());
        Action select = view.getActionMap().get("selectNextDay");
        select.actionPerformed(null);
        assertTrue("ui keyboard action must have started model adjusting", 
                view.getSelectionModel().isAdjusting());
        assertEquals(2, report.getEventCount());
        // assert that the adjusting is fired before the set
        assertEquals(EventType.DATES_SET, report.getLastEvent().getEventType());
    }
 
    /**
     * BasicMonthViewUI: use adjusting api in keyboard actions.
     * Here: test add selection action.
     */
    @Test
    public void testAdjustingSetOnAdd() {
        JXMonthView view = new JXMonthView();
        // otherwise the add action isn't called
        view.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        Date310SelectionReport report = new Date310SelectionReport(view.getSelectionModel());
        Action select = view.getActionMap().get("adjustSelectionNextDay");
        select.actionPerformed(null);
        assertTrue("ui keyboard action must have started model adjusting", 
                view.getSelectionModel().isAdjusting());
        assertEquals(2, report.getEventCount());
        // assert that the adjusting is fired before the add
        // only: the type a set instead or the expected added - bug or feature?
        // assertEquals(EventType.DATES_ADDED, report.getLastEvent().getEventType());
        // for now we are only interested in the adjusting (must not be the last)
        // so go for what's actually fired instead of what's expected
         assertEquals(EventType.DATES_SET, report.getLastEvent().getEventType());
        
    }

    /**
     * Issue #557-swingx: always fire actionEvent after esc/enter.
     * 
     * test fire after accept.
     */
    @Test
    public void testFireOnKeyboardAccept()  {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = today;
        monthView.setSelectionInterval(date, date);
        ActionReport report = new ActionReport();
        monthView.addActionListener(report);
        Action accept = monthView.getActionMap().get("acceptSelection"); 
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
        JXMonthView monthView = new JXMonthView();
        LocalDateTime date = today;
        monthView.setSelectionInterval(date, date);
        ActionReport report = new ActionReport();
        monthView.addActionListener(report);
        Action accept = monthView.getActionMap().get("cancelSelection");
        accept.actionPerformed(null);
        assertEquals(1, report.getEventCount());
    }

    /**
     * expose more selection constraint methods in JXMonthView
     *
     */
    @Test
    public void testUpperBound() {
        JXMonthView view = new JXMonthView();
        view.setUpperBound(today);
        assertEquals(today, view.getUpperBound());
        // remove again
        view.setUpperBound(null);
        assertEquals(null, view.getUpperBound());
    }
    
    /**
     * expose more selection constraint methods in JXMonthView
     *
     */
    @Test
    public void testLowerBound() {
        JXMonthView view = new JXMonthView();
        view.setLowerBound(today);
        assertEquals(today, view.getLowerBound());
        // remove again
        view.setLowerBound(null);
        assertEquals(null, view.getLowerBound());
    }

    /**
     * test unselectable: use methods with LocalDateTime.
     *
     */
    @Test
    public void testUnselectableDate() {
        JXMonthView monthView = new JXMonthView();
        // initial
        assertFalse(monthView.isUnselectableDate(today));
        // set unselectable today
        monthView.setUnselectableDates(today);
        assertTrue("raqw today must be unselectable", 
                monthView.isUnselectableDate(today));
        assertTrue("start of today must be unselectable", 
                monthView.isUnselectableDate(startOfDay(today)));
        assertTrue("end of today must be unselectable", 
                monthView.isUnselectableDate(endOfDay(today)));
        monthView.setUnselectableDates();
        assertFalse(monthView.isUnselectableDate(today));
        assertFalse(monthView.isUnselectableDate(startOfDay(today)));
        assertFalse(monthView.isUnselectableDate(endOfDay(today)));
    }

    /**
     * test unselectable: use methods with LocalDateTime.
     * test NPE as doc'ed.
     */
    @Test
    public void testUnselectableDatesNPE() {
        JXMonthView monthView = new JXMonthView();
        try {
            monthView.setUnselectableDates((LocalDateTime[])null);
            fail("null array must throw NPE");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            monthView.setUnselectableDates(new LocalDateTime[] {today, null});
            fail("null elements must throw NPE");
        } catch (NullPointerException e) {
            // expected
        }
    }

   
    /**
     * test cover method: isSelectedDate
     *
     */
    @Test
    public void testIsSelectedDate() {
        JXMonthView monthView = new JXMonthView();
        monthView.setSelectionDate(today);
        assertTrue(monthView.isSelected(today));
        assertTrue(monthView.isSelected(startOfDay(today)));
    }
    

    /**
     * test new (convenience) api on JXMonthView
     *
     */
    @Test
    public void testGetSelected() {
        JXMonthView monthView = new JXMonthView();
        assertNull(monthView.getSelectionDate());
        monthView.setSelectionInterval(today, today);
        assertEquals("same day", today, monthView.getSelectionDate());
        // clear selection
        monthView.clearSelection();
        assertNull(monthView.getSelectionDate());
    }
    
    
    @Test
    public void testDefaultConstructor() {
        JXMonthView monthView = new JXMonthView(Locale.US);
        assertTrue(monthView.isSelectionEmpty());
        assertTrue(SelectionMode.SINGLE_SELECTION == monthView.getSelectionMode());
        assertSame(DayOfWeek.SUNDAY, monthView.getFirstDayOfWeek());
    }

    @Test
    public void testLocale() {
        Locale[] locales = Locale.getAvailableLocales();

        for (Locale l : locales) {
            JComponent.setDefaultLocale(l);

            JXMonthView monthView = new JXMonthView();
            Locale locale = monthView.getLocale();
            DayOfWeek expectedFirstDayOfWeek = DateTimeUtils.getFirstDayOfWeek(locale);

            assertSame(expectedFirstDayOfWeek, monthView.getFirstDayOfWeek());
        }
    }

    @Test
    public void testEmptySelectionInitial() {
        JXMonthView monthView = new JXMonthView();
        assertTrue(monthView.isSelectionEmpty());
        SortedSet<LocalDateTime> selection = monthView.getSelection();
        assertTrue(selection.isEmpty());
    }
    
    @Test
    public void testEmptySelectionClear() {
        JXMonthView monthView = new JXMonthView();
        monthView.setSelectionInterval(today, today);
        // sanity
        assertTrue(1 == monthView.getSelection().size());

        monthView.clearSelection();
        assertTrue(monthView.isSelectionEmpty());
        assertTrue(monthView.getSelection().isEmpty());
    }

    @Test
    public void testSelectionModes() {
        JXMonthView monthView = new JXMonthView();
        assertEquals(SelectionMode.SINGLE_SELECTION, monthView
                .getSelectionMode());
        for (SelectionMode mode : SelectionMode.values()) {
            monthView.setSelectionMode(mode);
            assertEquals(mode, monthView.getSelectionModel().getSelectionMode());
            assertEquals(mode, monthView.getSelectionMode());
        }

    }

    @Test
    public void testSingleSelection() {
        JXMonthView monthView = new JXMonthView();
        monthView.setSelectionMode(SelectionMode.SINGLE_SELECTION);

        monthView.setSelectionInterval(yesterday, yesterday);
        assertTrue(1 == monthView.getSelection().size());
        assertEquals(yesterday, monthView.getFirstSelectionDate());

        monthView.setSelectionInterval(yesterday, afterTomorrow);
        assertTrue(1 == monthView.getSelection().size());
        assertEquals(yesterday, monthView.getFirstSelectionDate());
    }

    @Test
    public void testSingleIntervalSelection() {
        JXMonthView monthView = new JXMonthView();
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);

        monthView.setSelectionInterval(yesterday, yesterday);
        assertTrue(1 == monthView.getSelection().size());
        assertEquals(yesterday, monthView.getFirstSelectionDate());

        monthView.setSelectionInterval(yesterday, tomorrow);
        
        assertTrue(3 == monthView.getSelection().size());
        assertEquals(yesterday, monthView.getFirstSelectionDate());
        assertEquals(tomorrow, monthView.getLastSelectionDate());

    }



    @Test
    public void testMultipleIntervalSelection() {
        JXMonthView monthView = new JXMonthView();
        monthView.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);

        monthView.setSelectionInterval(yesterday, yesterday);
        monthView.addSelectionInterval(afterTomorrow, afterTomorrow);
        
        assertEquals(2, monthView.getSelection().size());
        assertEquals(yesterday, monthView.getFirstSelectionDate());
        assertEquals(afterTomorrow, monthView.getLastSelectionDate());
    }




    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateRemoveNotify() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.setFlaggedDates(tomorrow, yesterday);
        SortedSet<LocalDateTime> oldFlagged = monthView.getFlaggedDates();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.removeFlaggedDates(tomorrow);
        TestUtils.assertPropertyChangeEvent(report, "flaggedDates", 
                oldFlagged, monthView.getFlaggedDates());
    }

    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateRemove() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.addFlaggedDates(tomorrow, yesterday);
        assertEquals(2, monthView.getFlaggedDates().size());
        monthView.removeFlaggedDates(tomorrow);
        assertTrue(monthView.isFlaggedDate(yesterday));
        assertFalse(monthView.isFlaggedDate(tomorrow));
    }

    
    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateClear() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.addFlaggedDates(tomorrow, yesterday);
        assertEquals(2, monthView.getFlaggedDates().size());
        monthView.clearFlaggedDates();
        assertFalse("flagged dates must be cleared", monthView.hasFlaggedDates());
    }

    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateClearNotify() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.setFlaggedDates(tomorrow, yesterday);
        SortedSet<LocalDateTime> oldFlagged = monthView.getFlaggedDates();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.clearFlaggedDates();
        TestUtils.assertPropertyChangeEvent(report, "flaggedDates", 
                oldFlagged, monthView.getFlaggedDates());
    }

    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateAdd() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.setFlaggedDates(yesterday);
        monthView.addFlaggedDates(tomorrow);
        assertEquals(2, monthView.getFlaggedDates().size());
        assertTrue(monthView.isFlaggedDate(yesterday));
        assertTrue(monthView.isFlaggedDate(tomorrow));
    }
    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateAddNotify() {
        JXMonthView monthView = new JXMonthView();
        
        monthView.setFlaggedDates(yesterday);
        SortedSet<LocalDateTime> oldFlagged = monthView.getFlaggedDates();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.addFlaggedDates(tomorrow);
        TestUtils.assertPropertyChangeEvent(report, "flaggedDates", 
                oldFlagged, monthView.getFlaggedDates());
    }
    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateSet() {
        JXMonthView monthView = new JXMonthView();
        monthView.setFlaggedDates(today);
        assertTrue(monthView.isFlaggedDate(today));
        monthView.setFlaggedDates();
        assertFalse(monthView.isFlaggedDate(today));
    }
    /**
     * test setting/checking flagged dates (api with LocalDateTime)
     */
    @Test
    public void testFlaggedDateNotification() {
        JXMonthView monthView = new JXMonthView();
        SortedSet<LocalDateTime> oldFlagged = monthView.getFlaggedDates();
        PropertyChangeReport report = new PropertyChangeReport();
        monthView.addPropertyChangeListener(report);
        monthView.setFlaggedDates(today);
        TestUtils.assertPropertyChangeEvent(report, "flaggedDates", 
                oldFlagged, monthView.getFlaggedDates());
    }

   
    @Test
    public void testShowLeadingDates() {
        JXMonthView monthView = new JXMonthView();
        assertFalse(monthView.isShowingLeadingDays());
        monthView.setShowingLeadingDays(true);
        assertTrue(monthView.isShowingLeadingDays());
    }

    @Test
    public void testShowTrailingDates() {
        JXMonthView monthView = new JXMonthView();
        assertFalse(monthView.isShowingTrailingDays());
        monthView.setShowingTrailingDays(true);
        assertTrue(monthView.isShowingTrailingDays());
    }

    private LocalDateTime startOfDay(LocalDateTime date) {
        return LocalDateTime.ofMidnight(date);
    }
 
    private LocalDateTime endOfDay(LocalDateTime date) {
        date = LocalDateTime.ofMidnight(date.plusDays(1));
        return date.minusNanos(1);
    }
}