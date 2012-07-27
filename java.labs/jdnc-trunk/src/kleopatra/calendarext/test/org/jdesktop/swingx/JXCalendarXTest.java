/*
 * Created on 28.01.2010
 *
 */
package org.jdesktop.swingx;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit test for changed behaviour when moving from JXMonthView to JXCalendar.
 */
@RunWith(JUnit4.class)
public class JXCalendarXTest extends InteractiveTestCase {

    
    
    private Locale componentLocale;
    private Locale alternateLocale;
    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    // calendar default instance init with today
    private Calendar calendar;

    private JXCalendar calendarView;
    
    
    
    @Test
    public void testConstructorWithSelectionModel() {
        DateSelectionModel model = new DaySelectionModel(alternateLocale);
        model.setSelectionInterval(afterTomorrow, afterTomorrow);
        JXCalendar calendarView = new JXCalendar(model);
        assertEquals(model.getSelection().first(), calendarView.getSelectionDate());
        assertEquals(alternateLocale, calendarView.getLocale());
    }
    
    @Test
    public void testConstructorWithSelectionDateAndLocale() {
        JXCalendar calendarView = new JXCalendar(tomorrow, alternateLocale);
        assertTrue(calendarView.getSelectionModel() instanceof DaySelectionModel);
        assertEquals(alternateLocale, calendarView.getLocale());
        Calendar calendar = calendarView.getPage();
        calendar.setTime(tomorrow);
        assertTrue(CalendarUtils.isSameDay(calendar, calendarView.getSelectionDate()));
    }
    
    @Test
    public void testUnselectable() {
        calendarView.setUnselectableDates(afterTomorrow);
        assertTrue(calendarView.isUnselectableDate(afterTomorrow));
    }
    
    @Test
    public void testDefaults() {
        assertSame(DateSelectionModel.SelectionMode.SINGLE_SELECTION, calendarView.getSelectionMode());
    }
    
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

        componentLocale = JComponent.getDefaultLocale();
        alternateLocale = Locale.CHINA;
        if (alternateLocale.equals(componentLocale)) {
            alternateLocale = Locale.UK;
        }

        calendarView = new JXCalendar();
    }

    @Override
    @After
       public void tearDown() {
        JComponent.setDefaultLocale(componentLocale);
    }

}
