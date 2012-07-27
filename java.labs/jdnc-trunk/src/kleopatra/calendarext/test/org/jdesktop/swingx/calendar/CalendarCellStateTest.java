/*
 * Created on 11.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import static org.junit.Assert.*;


import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.swingx.calendar.CalendarCellState;

import static org.jdesktop.swingx.calendar.CalendarCellState.*;
import org.junit.Before;
import org.junit.Test;
public class CalendarCellStateTest extends InteractiveTestCase {


    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    private Calendar calendar;

    @Test
    public void testMonthInYearCellContained() {
        assertSame(FieldType.YEAR, MONTH_CELL.getPageType());
        assertTrue("must be same day " + calendar.getTime() + " / " + today, 
                MONTH_CELL.isContainedInCell(calendar, today));
        calendar.add(Calendar.MONTH, 1);
        assertFalse(MONTH_CELL.isContainedInCell(calendar, today));
    }

    
    @Test
    public void testDayInMonthCellContained() {
        assertSame(FieldType.MONTH, DAY_CELL.getPageType());
        assertFalse(DAY_CELL.isContainedInCell(calendar, tomorrow));
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        assertTrue("must be same day " + calendar.getTime() + " / " + today, 
                DAY_CELL.isContainedInCell(calendar, today));
        
    }
    
    @Test
    public void testStateProperties() {
        assertStateProperties(DAY_CELL, true, true, FieldType.MONTH);
        assertStateProperties(TODAY_CELL, true, true, FieldType.MONTH);
        assertStateProperties(DAY_OFF_CELL, true, false, FieldType.MONTH);
        assertStateProperties(WEEK_OF_YEAR_TITLE, false, false, FieldType.MONTH);
        assertStateProperties(DAY_OF_WEEK_TITLE, false, false, FieldType.MONTH);
        assertStateProperties(PAGE_TITLE, false, false, FieldType.MONTH);
        assertStateProperties(MONTH_CELL, true, true, FieldType.YEAR);
        assertStateProperties(YEAR_CELL, true, true, FieldType.DECADE);
        assertStateProperties(YEAR_OFF_CELL, true, false, FieldType.DECADE);
        
        
       
    }
    
    
    
    private void assertStateProperties(CalendarCellState inMonth, boolean content,
            boolean onPage, FieldType month) {
        assertEquals(onPage, inMonth.isOnPage());
        assertEquals(content, inMonth.isContent());
        assertSame(month, inMonth.getPageType());
    }


    /**
     * @inherited <p>
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
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

    }
    
}
