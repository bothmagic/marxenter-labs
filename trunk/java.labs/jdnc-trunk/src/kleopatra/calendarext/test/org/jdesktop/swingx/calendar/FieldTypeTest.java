/*
 * Created on 09.02.2010
 *
 */
package org.jdesktop.swingx.calendar;


import static org.jdesktop.swingx.calendar.FieldType.MONTH;
import static org.jdesktop.swingx.calendar.FieldType.YEAR;

import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for FieldType.
 */
@RunWith(JUnit4.class)
public class FieldTypeTest extends InteractiveTestCase {

    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    private Calendar calendar;

    @Test
    public void testMonthInYearCellContained() {
        assertTrue("must be same day " + calendar.getTime() + " / " + today, 
                YEAR.isContainedInCell(calendar, today));
        calendar.add(Calendar.MONTH, 1);
        assertFalse(YEAR.isContainedInCell(calendar, today));
    }

    
    @Test
    public void testDayInMonthCellContained() {
        assertFalse(MONTH.isContainedInCell(calendar, tomorrow));
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        assertTrue("must be same day " + calendar.getTime() + " / " + today, 
                MONTH.isContainedInCell(calendar, today));
        
    }

    @Test
    public void testChildCalendarField() {
        for (FieldType type : FieldType.values()) {
            if (type.isNativeCalendarField()) {
                int childField = 0;
                if (FieldType.DAY != type) {
                    childField = type.getCellType().getCalendarField();
                }
                assertEquals("childField on native type " + type, 
                        childField, type.getCellCalendarField());
            } else {
                assertEquals("childfield on custom type " + type,
                        CalendarUtils.YEAR_IN_DECADE, type.getCellCalendarField());
            }
        }
        
    }
    
    @Test
    public void testAdd() {
        for (FieldType type : FieldType.values()) {
            Calendar calendar = Calendar.getInstance();
            Calendar clone = (Calendar) calendar.clone();
            CalendarUtils.add(calendar, type.getCalendarField(), 5);
            type.add(clone, 5);
            assertEquals("adding: " + type, calendar.getTime(), clone.getTime());
        }
    }
    
    @Test
    public void testGet() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        int decade = FieldType.DECADE.get(calendar);
        assertEquals(2010, decade);
    }
    
    @Test
    public void testSet() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        FieldType.DECADE.set(calendar, 2030);
        assertEquals(2035, calendar.get(Calendar.YEAR));
    }
    
    
    @Test
    public void testIsNativeCalendarField() {
        for (FieldType type : FieldType.values()) {
            boolean isNative = FieldType.DECADE != type; 
            assertEquals("nativeField " + type, isNative, type.isNativeCalendarField());
        }
    }
    
    @Test
    public void testUnitIncrememt() {
        for (FieldType type : FieldType.values()) {
            assertEquals("increment for " + type, 1, type.getUnitIncrement());
        }
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
