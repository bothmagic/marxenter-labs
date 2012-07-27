/*
 * Created on 22.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.SpinnerModel;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.renderer.StringValue;

public class ListFactory {

    Calendar calendar;
    
    private JXList createConfigureMonthDetailsList(ListModel dateListModel) {
        JXList list = new JXList(dateListModel);
        list.setLayoutOrientation(JXList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(5);
        
        StringValue sv = new FormatStringValue(new SimpleDateFormat("d"));
        list.setCellRenderer(new DefaultListRenderer(sv, JLabel.RIGHT));
        return list;
    }

    

    private DateListModel createDateListModel() {
        return new DateListModel();
    }

    public class DateListModel extends AbstractListModel {
 
        SpinnerModel current;
        
        public void dateChanged(Date date) {
            fireContentsChanged(this, -1, -1);
        }
        
        public Object getElementAt(int index) {
            calendar.setTime((Date) current.getValue());
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if ((index + 1) <= max ) {
                calendar.set(Calendar.DAY_OF_MONTH, index + 1);
                return calendar.getTime();
            }
            return null;
         }

         public int getSize() {
             return 32; 
         } 

    }
    

}
