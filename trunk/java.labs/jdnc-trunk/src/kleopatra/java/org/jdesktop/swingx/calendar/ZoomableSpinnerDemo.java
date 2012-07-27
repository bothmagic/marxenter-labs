/*
 * Created on 20.01.2008
 *
 */
package org.jdesktop.swingx.calendar;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;

public class ZoomableSpinnerDemo extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ZoomableSpinnerDemo.class
            .getName());
    
    public static void main(String[] args) {
        setSystemLF(true);
            ZoomableSpinnerDemo test = new ZoomableSpinnerDemo();
            try {
              test.runInteractiveTests();
            } catch (Exception e) {
                System.err.println("exception when executing interactive tests:");
                e.printStackTrace();
            } 
        
    }

    
    public void interactiveSpinner() {
        Calendar asia = Calendar.getInstance(TimeZone.getTimeZone("Asia/Calcutta"));
        asia.set(2009,Calendar.MARCH, 29, 0, 0, 0);
        SpinnerModel model = new DateSpinner(asia, Calendar.DAY_OF_MONTH, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.setValue(asia.getTime());
        JSpinner core = new JSpinner(new SpinnerDateModel());
        core.setValue(asia.getTime());
        JComponent box = Box.createHorizontalBox();
        box.add(new JLabel("core: "));
        box.add(core);
        box.add(new JLabel("swingx: "));
        box.add(spinner);
        showInFrame(box, "spinner ");
    }


    public void interactiveXMonthView() {
        JXMonthView monthView = new JXMonthView();
        monthView.setZoomable(true);
        showInFrame(monthView, "zoomable monthView");
    }
    
    public void interactiveShowSpinner() {
        JXZoomableSpinner spinner = new JXZoomableSpinner();
        showInFrame(spinner, "demo");
    }
}
