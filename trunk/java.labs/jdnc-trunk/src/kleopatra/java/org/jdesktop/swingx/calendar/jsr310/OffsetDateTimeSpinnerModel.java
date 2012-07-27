/*
 * Created on 08.01.2009
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.text.Format;

import javax.swing.AbstractSpinnerModel;
import javax.time.calendar.Clock;
import javax.time.calendar.OffsetDateTime;
import javax.time.calendar.Period;
import javax.time.calendar.PeriodProvider;

public class OffsetDateTimeSpinnerModel extends AbstractSpinnerModel {

    private OffsetDateTime dateTime;
    private PeriodProvider provider;
    private Format format;
    private String name;
    
    public OffsetDateTimeSpinnerModel() {
        this(Clock.systemDefaultZone().offsetDateTime());
    }
    
    public OffsetDateTimeSpinnerModel(OffsetDateTime dateTime) {
        this(dateTime, Period.ofYears(1));
    }
    
    public OffsetDateTimeSpinnerModel(OffsetDateTime dateTime,
            PeriodProvider provider) {
        this.dateTime = dateTime;
        this.provider = provider;
    }

    public OffsetDateTimeSpinnerModel(PeriodProvider provider) {
        this(Clock.systemDefaultZone().offsetDateTime(), provider);
    }

    public Object getNextValue() {
        return dateTime.plus(provider);
    }

    public Object getPreviousValue() {
        return dateTime.minus(provider);
    }

    public Object getValue() {
        return dateTime;
    }

    public void setValue(Object value) {
        if (!(value instanceof OffsetDateTime)) return;
        this.dateTime = (OffsetDateTime) value;
        fireStateChanged();
    }

    /**
     * Sets the format to use with the model. Note: quick hack, doesn't belong here! 
     * @param format
     */
    public void setFormat(Format format) {
        this.format = format;
        fireStateChanged();
    }
    
    public Format getFormat() {
        return this.format;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    

    public String getName() {
        return name;
    }
    
}
