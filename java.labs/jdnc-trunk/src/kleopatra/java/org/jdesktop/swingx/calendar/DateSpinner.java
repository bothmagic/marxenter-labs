/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

/**
 * DateSpinner which allows to configure
 * - calendar
 * - amount of spin
 * 
 * Holds a format (for convenience only - doesn't belong here).
 * Shouldn't subclass but extend AbstractXX (everything important had be
 * overridden).
 */
public class DateSpinner extends SpinnerDateModel {


    private Calendar calendar;
    private int amount;
    private Date currentDate;
    private Format format;
    private String name;
    
    public DateSpinner(int field) {
        this(Calendar.getInstance(), field, 1);
    }
    
    public DateSpinner(Calendar calendar, int field, int amount) {
        super(new Date(), null, null, field);
        this.calendar = calendar;
        this.amount = amount;
        this.currentDate = super.getDate();
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
        fireStateChanged();
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setFormat(Format format) {
        this.format = format;
        fireStateChanged();
    }
    
    public Format getFormat() {
        return format;
    }
    
    public void setName(String name) {
        this.name = name;
        fireStateChanged();
    }
    
    public String getName() {
        return name;
    }
    
    
//------------------ override super's methods to use given calendar
    
    @Override
    public void setValue(Object value) {
        if ((value == null) || !(value instanceof Date)) {
            throw new IllegalArgumentException("illegal value");
        }
        if (!value.equals(getDate())) {
            currentDate = (Date) value;
            fireStateChanged();
        }
    }
    
    @Override
    public Date getDate() {
        return currentDate;
    }
    
    /**
     * Returns the current element in this sequence of <code>Date</code>s.
     * 
     * @return the <code>value</code> property
     * @see #setValue
     * @see #getDate
     */
    @Override
    public Object getValue() {
        return getDate();
    }

    /**
     * Returns the next <code>Date</code> in the sequence, or <code>null</code> if 
     * the next date is after <code>end</code>.
     * 
     * @return the next <code>Date</code> in the sequence, or <code>null</code> if 
     *     the next date is after <code>end</code>.
     * 
     * @see SpinnerModel#getNextValue
     * @see #getPreviousValue
     * @see #setCalendarField
     */
    @Override
    public Date getNextValue() {
        Calendar cal = calendar;
        cal.setTime(getDate());
        cal.add(getCalendarField(), getAmount());
        Date next = cal.getTime();
        return ((getEnd() == null) || (getEnd().compareTo(next) >= 0)) ? next : null;
    }


    /**
     * Returns the previous <code>Date</code> in the sequence, or <code>null</code>
     * if the previous date is before <code>start</code>.
     * 
     * @return the previous <code>Date</code> in the sequence, or
     *     <code>null</code> if the previous date
     *     is before <code>start</code>
     * 
     * @see SpinnerModel#getPreviousValue
     * @see #getNextValue
     * @see #setCalendarField
     */
    @Override
    public Date getPreviousValue() {
        Calendar cal = calendar;
        cal.setTime(getDate());
        cal.add(getCalendarField(), - getAmount());
        Date prev = cal.getTime();
        return ((getStart() == null) || (getStart().compareTo(prev) <= 0)) ? prev : null;
    }


}
