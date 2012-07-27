/*
 * Created on 22.02.2010
 *
 */
package org.jdesktop.swingx.calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.jdesktop.swingx.Localizable;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.StringValues;

/**
 * Collection of StringValues useful in Calendar rendering
 */
public class CalendarStringValues {
    public static class CalendarStringValue extends FormatStringValue implements Localizable {

        private String pattern;
        private Locale locale;

        public CalendarStringValue(Locale locale, String pattern) {
            super(null);
            this.pattern = pattern;
            setLocale(locale);
        }
        
        /**
         * {@inheritDoc} <p>
         * 
         * Implemented to update the Format appropriately.
         */
        @Override
        final public void setLocale(Locale locale) {
            if (locale == null) {
                locale = Locale.getDefault();
            }
            this.locale = locale;
            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, locale);
            if (format instanceof SimpleDateFormat) {
                ((SimpleDateFormat) format).applyPattern(pattern);
            } else {
                // we ever come here? custom providers?
                format = new SimpleDateFormat(pattern, locale);
            }
            this.format = format;
        }

        public Locale getLocale() {
            return locale;
        }
        /** 
         * @inherited <p>
         */
        @Override
        public SimpleDateFormat getFormat() {
            return (SimpleDateFormat) super.getFormat();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public String getString(Object value) {
            if (value instanceof Calendar) {
                Calendar calendar = (Calendar) value;
                getFormat().setTimeZone(calendar.getTimeZone());
                return super.getString(calendar.getTime());
            }
            return StringValues.EMPTY.getString(value);
        }
        
    }
    
    public static class DecadeTitleStringValue extends CalendarStringValue {

        public DecadeTitleStringValue(Locale locale, String pattern) {
            super(locale, pattern);
        }

        /** 
         * @inherited <p>
         */
        @Override
        public String getString(Object value) {
            String text = super.getString(value);
            if (value instanceof Calendar) {
                Calendar calendar = (Calendar) ((Calendar) value).clone();
                CalendarUtils.startOf(calendar, CalendarUtils.DECADE);
                calendar.add(Calendar.YEAR, 9);
                text += " - " + super.getString(calendar);
            }
            return text;
        }
        
        
    }
    private CalendarStringValues() {};
}
