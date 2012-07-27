/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractSpinnerModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.time.calendar.DateProvider;
import javax.time.calendar.ISOChronology;
import javax.time.calendar.LocalDate;
import javax.time.calendar.OffsetDateTime;
import javax.time.calendar.Period;
import javax.time.calendar.format.DateTimeFormatter;
import javax.time.calendar.format.DateTimeFormatterBuilder;

public class ZoomableSpinnerModel extends AbstractSpinnerModel {

    private SpinnerListModel models;
    private ChangeListener changeListener;
    List<OffsetDateTimeSpinnerModel> dateSpinners = new ArrayList<OffsetDateTimeSpinnerModel>();

    public ZoomableSpinnerModel(OffsetDateTime calendar) {
        OffsetDateTimeSpinnerModel monthSpinner = new OffsetDateTimeSpinnerModel(
                calendar, Period.ofMonths(1));
        DateTimeFormatter monthFormatter = new DateTimeFormatterBuilder()
                .appendText(ISOChronology.monthOfYearRule()).appendLiteral(" ")
                .appendValue(ISOChronology.yearRule()).toFormatter();
        Format monthNameFormat = monthFormatter.toFormat();
        monthSpinner.setFormat(monthNameFormat);
        monthSpinner.setName("monthDetails");
        dateSpinners.add(monthSpinner);

        OffsetDateTimeSpinnerModel yearSpinner = new OffsetDateTimeSpinnerModel(
                calendar, Period.ofYears(1));
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendValue(ISOChronology.yearRule()).toFormatter();
        Format yearNameFormat = formatter.toFormat();
        yearSpinner.setFormat(yearNameFormat);
        yearSpinner.setName("yearDetails");
        dateSpinners.add(yearSpinner);

        OffsetDateTimeSpinnerModel decadeSpinner = new OffsetDateTimeSpinnerModel(
                calendar, Period.ofYears(10));
//        DateTimeFormatter decadeFormatter = new DateTimeFormatterBuilder()
//                .appendLiteral("Decade around ").appendValue(
//                        ISOChronology.yearRule()).toFormatter();
//        Format decadeFormat = decadeFormatter.toFormat();
        Format decadeFormat = new YearRangeFormat();
        decadeSpinner.setFormat(decadeFormat);
        decadeSpinner.setName("decadeDetails");
        dateSpinners.add(decadeSpinner);

        models = new SpinnerListModel(dateSpinners);
        models.addChangeListener(getChangeListener());
    }
    
    public int getDepth() {
        return dateSpinners.size();
    }
    
    public OffsetDateTimeSpinnerModel getSpinner(int index) {
        return dateSpinners.get(index);
    }
    
    public String getSpinnerName(int index) {
        return getSpinner(index).getName();
    }
    
    public void setAllValues(OffsetDateTime date) {
        for (OffsetDateTimeSpinnerModel spinner : dateSpinners) {
            spinner.setValue(date);
        }
    };
    
    public void zoomIn(OffsetDateTime date) {
        setAllValues(date);
        SpinnerModel model = getPreviousValue();
        if (model != null) {
            setValue(model);
        }

    }

    
    public void zoomOut() {
        Object next = models.getNextValue();
        if (next != null) {
            models.setValue(next);
        }
    }
    
    
    public void next() {
       SpinnerModel currentModel = getCurrentModel(); 
       Object next = currentModel.getNextValue();
       if (next != null) {
           currentModel.setValue(next);
       }
    }

    public SpinnerModel getCurrentModel() {
        SpinnerModel currentModel = (SpinnerModel) models.getValue();
        return currentModel;
    }
    
    public void previous() {
        SpinnerModel currentModel = getCurrentModel(); 
        Object next = currentModel.getPreviousValue();
        if (next != null) {
            currentModel.setValue(next);
        }
        
    }
    
    private ChangeListener getChangeListener() {
        if (changeListener == null) {
           changeListener =  new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireStateChanged();
                
            }
               
           };
        }
        return changeListener;
    }

    public SpinnerModel getNextValue() {
        return (SpinnerModel) models.getNextValue();
    }

    public SpinnerModel getPreviousValue() {
         return (SpinnerModel) models.getPreviousValue();
    }

    public SpinnerModel getValue() {
        return (SpinnerModel) models.getValue();
    }

    public void setValue(Object value) {
        models.setValue(value);
    }

    
    public static class YearRangeFormat extends Format {

        @Override
        public StringBuffer format(Object value, StringBuffer toAppendTo,
                FieldPosition pos) {
            if (value instanceof DateProvider) {
                LocalDate date = ((DateProvider) value).toLocalDate();
                formatLocalDate(date, toAppendTo);
            }
            return toAppendTo;
        }

        private void formatLocalDate(LocalDate date, StringBuffer toAppendTo) {
            int year = date.getYear();
            int decadeStart = (year / 10) * 10;
            int decadeEnd = decadeStart + 9;
            toAppendTo.append(decadeStart);
            toAppendTo.append(" - ");
            toAppendTo.append(decadeEnd);
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
        
    }
}
