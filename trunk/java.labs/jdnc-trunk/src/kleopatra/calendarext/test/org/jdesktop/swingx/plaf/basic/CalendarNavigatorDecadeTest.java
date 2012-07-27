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
public class CalendarNavigatorDecadeTest extends AbstractTestNavigator {

    
    @Test
    public void testFirstCell() {
        Calendar page = getPage();
        Calendar calendar = getNavigator().getFirstCell();
        assertTrue("first cell must be before page but was: " 
                + calendar.getTime() + " / " + getPage().getTime(),
                 calendar.before(getPage()));
        page.add(Calendar.YEAR, -1);
        assertEquals("first cell must be start of week", page.getTime(), calendar.getTime());
    }
    

    @Test
    public void testLeadValue() {
        Calendar page = getPage();
        page.set(Calendar.YEAR, 2005);
        getNavigator().setLeadDate(page.getTime());
        assertEquals(5, getNavigator().getLeadValue());
    }
    

    @Test
    public void testPageIsDecade() {
        Calendar page = getPage();
        page.set(Calendar.YEAR, 2005);
        getNavigator().setLeadDate(page.getTime());
        assertTrue("must be start of decade but was: " + getPage().getTime(), 
                CalendarUtils.isStartOf(getPage(), CalendarUtils.DECADE));
    }
    

    private void assertDefaults(Navigator navigator) {
        assertSame(FieldType.DECADE, navigator.getPageType());
        assertSame(FieldType.YEAR, navigator.getCellType());
        assertTrue(CalendarUtils.isSame(Calendar.getInstance(), 
                navigator.getLeadDate(), 
                navigator.getCellType().getCalendarField()));
        assertTrue(CalendarUtils.isStartOfDecade(navigator.getPage()));
        assertEquals(4, navigator.getVerticalCellUnit());
        assertEquals(12, navigator.getVerticalPageUnit());
        assertEquals(0, navigator.getMinimumLeadValue());
        assertEquals(9, navigator.getMaximumLeadValue());
    }

    @Test
    public void testDefaultConstructor() {
        assertDefaults(getNavigator());
    }

    
    @Override
    protected Navigator createNavigator() {
        return new CalendarNavigator(Calendar.getInstance(), FieldType.DECADE, 4, 3);
    }

}
