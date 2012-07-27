/*
 * Created on 12.01.2009
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.time.calendar.DateAdjusters;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.LocalTime;

/**
 * Utility methods for jsr310 api. 
 */
public class DateTimeUtils {

    
//----------------------------- formatting/parsing
    
    public static Map<DayOfWeek, String> getShortDayOfWeekTexts(Locale locale) {
        Map<DayOfWeek, String> daysOfTheWeek = new HashMap<DayOfWeek, String>();
        for (DayOfWeek day : DayOfWeek.values()) {
            daysOfTheWeek.put(day, day.getShortText(locale));
        }
        return daysOfTheWeek;
    }
 
    
// ------------------------- temporary workaround
    
    /**
     * Temporary workaround to get hold of the Locale dependent first day of week.
     * 
     * @param locale
     * @return
     */
    public static DayOfWeek getFirstDayOfWeek(Locale locale) {
        Calendar calendar = Calendar.getInstance(locale);
        int first = calendar.getFirstDayOfWeek();
        return DayOfWeek.of(first > 1 ? first - 1 : 7);
    }
    
//    public static int getMinimalDaysInFirstWeek(Locale locale) {
//        return Calendar.getInstance(locale).getMinimalDaysInFirstWeek();
//    }

//------------------------------ date calculation
    
    /**
     * Returns a LocalDateTime representing the end of the last day in 
     * the month of the given dateTime
     * 
     * @param dateTime 
     * @return
     */
    public static LocalDateTime endOfMonth(LocalDateTime dateTime) {
        LocalDateTime startOfNextMonth = startOfMonth(dateTime).plusMonths(1);
        return startOfNextMonth.minusNanos(1L);
    }
    
    /**
     * Returns a LocalDateTime representing the start of day of the 
     * first day of the week in the default Locale.
     * 
     * @param dateTime the dateTtime to adjust
     * @return the start of day of the first day of week
     */
    public static LocalDateTime startOfWeek(LocalDateTime dateTime) {
        return startOfWeek(dateTime, Locale.getDefault());
    }
    
    /**
     * Returns a LocalDateTime representing the start of day of the first
     * day of the week in the given Locale.
     * 
     * @param dateTime the dateTime to adjust
     * @param locale the Locale to use
     * @return the start of day of the first day of week
     */
    public static LocalDateTime startOfWeek(LocalDateTime dateTime,
            Locale locale) {
        DayOfWeek first = getFirstDayOfWeek(locale);
        dateTime = dateTime.with(DateAdjusters.previousOrCurrent(first));
        return LocalDateTime.ofMidnight(dateTime);
    }

    /**
     * Returns a LocalDateTime representing the start of day of the first
     * day of the month.
     * 
     * @param dateTime
     * @return
     */
    public static LocalDateTime startOfMonth(LocalDateTime dateTime) {
        return LocalDateTime.ofMidnight(dateTime.withDayOfMonth(1));
    }

    public static boolean isStartOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth()== 1 
            && LocalTime.MIDNIGHT == dateTime.toLocalTime();
    }
    
    
    private DateTimeUtils()  { };
}
