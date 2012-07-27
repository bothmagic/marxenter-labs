/*
 * Created on 04.02.2010
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.util.Calendar;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CalendarNavigatorYearTest extends AbstractTestNavigator {


    private void assertDefaults(Navigator navigator) {
        assertSame(FieldType.YEAR, navigator.getPageType());
        assertSame(FieldType.MONTH, navigator.getCellType());
        assertTrue(CalendarUtils.isSame(Calendar.getInstance(), 
                navigator.getLeadDate(), 
                navigator.getCellType().getCalendarField()));
        assertTrue(CalendarUtils.isStartOfYear(navigator.getPage()));
        assertEquals(4, navigator.getVerticalCellUnit());
        assertEquals(12, navigator.getVerticalPageUnit());
        assertEquals(Calendar.JANUARY, navigator.getMinimumLeadValue());
        assertEquals(Calendar.DECEMBER, navigator.getMaximumLeadValue());
    }

    @Test
    public void testDefaultConstructor() {
        assertDefaults(getNavigator());
    }

    
    @Override
    protected Navigator createNavigator() {
        return new CalendarNavigator(Calendar.getInstance(), FieldType.YEAR, 4, 3);
    }

}
