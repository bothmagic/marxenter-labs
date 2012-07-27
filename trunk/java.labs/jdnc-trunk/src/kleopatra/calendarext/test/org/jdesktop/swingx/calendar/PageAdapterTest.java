/*
 * Created on 15.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.plaf.basic.Navigator;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for PageAdapter.
 */
public class PageAdapterTest extends InteractiveTestCase {


    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    private Date midJune;
    // calendar default instance init with today
    private Calendar calendar;

    // calendarView default instantiated
    private JXCalendar calendarView;
    private PageAdapter adapter;
    
    @Test
    public void testUnselectable() {
        calendarView.setSelectionDate(midJune);
        Calendar location = calendarView.getUI().getCell(2, 2);
        assertFalse("sanity: unselectable != midJune as selected", 
                CalendarUtils.isSameDay(location, midJune));
        calendarView.setUnselectableDates(location.getTime());
        adapter.moveTo(2, 2);
        assertEquals(true, calendarView.isUnselectableDate(location.getTime()));
        assertEquals(true, adapter.isUnselectable());
    }
    
    @Test
    public void testSelectedOnYearPage() {
        calendarView.setSelectionDate(midJune);
        performAction(Navigator.ZOOM_OUT_KEY);
        Calendar location = calendarView.getUI().getCell(1, 1);
        assertTrue("sanity: cell contains selection " + location.getTime(), 
                adapter.getCellState(1, 3).isContainedInCell(location, midJune));
        adapter.row = 1;
        adapter.column = 1;
        assertTrue("cell must be selected ", adapter.isSelected());
    }
    
    @Test
    public void testCellState() {
        // first day in second week, guaranteed to be onPage
        CalendarCellState state = adapter.getCellState(2, 1);
        assertTrue(state.isOnPage());
    }
    
    @Test
    public void testMonthValue() {
        adapter.column = 1;
        adapter.row = 1;
        Calendar page = adapter.getComponent().getPage();
        CalendarUtils.startOfWeek(page);
        assertEquals(page.getTime(), adapter.getValue().getTime());
    }
    
    @Test
    public void testMonthValueShowingWeekNumbers() {
        calendarView.setShowingWeekNumber(true);
        adapter.column = 1;
        adapter.row = 1;
        Calendar page = adapter.getComponent().getPage();
        CalendarUtils.startOfWeek(page);
        assertEquals(page.getTime(), adapter.getValue().getTime());
    }
    
    @Test
    public void testFilteredValue() {
       adapter.column = 1;
       adapter.row = 1;
       assertEquals(adapter.getValue(), adapter.getFilteredValueAt(1, 1));
    }
    
    @Test
    public void testRowCount() {
        assertEquals(6 + 1, adapter.getRowCount());
        performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(3, adapter.getRowCount());
        performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(3, adapter.getRowCount());
    }
    
    @Test
    public void testColumnCount() {
        assertEquals(JXCalendar.DAYS_IN_WEEK + 1, adapter.getColumnCount());
        performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(4, adapter.getColumnCount());
        performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(4, adapter.getColumnCount());
    }
    
    
    private boolean performAction(Object id) {
        Action action = calendarView.getActionMap().get(id);
        action.actionPerformed(null);
        return true;
    }
    

    /** 
     * @inherited <p>
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        calendarView = new JXCalendar();
        adapter = new PageAdapter(calendarView);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        today = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        afterTomorrow = calendar.getTime();
        
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.DATE, 15);
        midJune = calendar.getTime();
        
        calendar.setTime(today);

    }


}
