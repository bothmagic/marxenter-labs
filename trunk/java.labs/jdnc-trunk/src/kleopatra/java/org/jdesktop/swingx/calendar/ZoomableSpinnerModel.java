/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractSpinnerModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZoomableSpinnerModel extends AbstractSpinnerModel {

    private SpinnerListModel models;
    private ChangeListener changeListener;
    List<DateSpinner> dateSpinners = new ArrayList<DateSpinner>();

    public ZoomableSpinnerModel(Calendar calendar) {
        DateSpinner monthSpinner = new DateSpinner(calendar, Calendar.MONTH, 1);
        SimpleDateFormat monthNameFormat = (SimpleDateFormat) DateFormat.getDateInstance();
        monthNameFormat.applyPattern("MMMM yyyy");
        monthSpinner.setFormat(monthNameFormat);
        monthSpinner.setName("monthDetails");
        dateSpinners.add(monthSpinner);
        
        DateSpinner yearSpinner = new DateSpinner(calendar, Calendar.YEAR, 1);
        SimpleDateFormat yearNameFormat = (SimpleDateFormat) DateFormat.getDateInstance();
        yearNameFormat.applyPattern("yyyy");
        yearSpinner.setFormat(yearNameFormat);
        yearSpinner.setName("yearDetails");
        dateSpinners.add(yearSpinner);
        
        DateSpinner decadeSpinner = new DateSpinner(calendar, Calendar.YEAR, 10);
//        MessageFormat format = new MessageFormat("{0, date, yyyy } - {1, date, yyyy}");
        SimpleDateFormat yearIntervalNameFormat = (SimpleDateFormat) DateFormat.getDateInstance();
        yearIntervalNameFormat.applyPattern("yyyy");
        decadeSpinner.setFormat(yearIntervalNameFormat);
        decadeSpinner.setName("decadeDetails");
        dateSpinners.add(decadeSpinner);
        
        models = new SpinnerListModel(dateSpinners);
        models.addChangeListener(getChangeListener());
    }
    
    public int getDepth() {
        return dateSpinners.size();
    }
    
    public DateSpinner getSpinner(int index) {
        return dateSpinners.get(index);
    }
    
    public String getSpinnerName(int index) {
        return getSpinner(index).getName();
    }
    
    public void setAllValues(Date date) {
        for (DateSpinner spinner : dateSpinners) {
            spinner.setValue(date);
        }
    };
    
    public void zoomIn(Date date) {
        setAllValues(date);
        SpinnerModel model = getPreviousValue();
        if (model != null) {
            setValue(model);
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
    
    public void zoomOut() {
        Object next = models.getNextValue();
        if (next != null) {
            models.setValue(next);
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
        public StringBuffer format(Object obj, StringBuffer toAppendTo,
                FieldPosition pos) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }
        
    }
}
