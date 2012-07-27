/*
 * Created on 05.11.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
 
public class TimeZoneSpinners {
 
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
 
      @Override
      public void run() {
        new TimeZoneSpinners().makeUI();
      }
    });
  }
 
  public void makeUI() {
    TimeZone baseTimeZone = TimeZone.getTimeZone("Etc/UTC");
    int offset = baseTimeZone.getRawOffset();
 
    Calendar cal = Calendar.getInstance(baseTimeZone);
    cal.set(Calendar.MONTH, Calendar.JUNE);
    cal.set(Calendar.YEAR, 2009);
    cal.set(Calendar.MONTH, Calendar.MARCH);
    cal.set(Calendar.DAY_OF_MONTH, 29);
    cal.set(Calendar.HOUR_OF_DAY, offset / (1000 * 60 * 60));
    cal.set(Calendar.MINUTE, (offset % (1000 * 60 * 60)) / (1000 * 60));
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Date date = cal.getTime();
 
    String[] zones = {"zulu", "Europe/London", "Etc/UTC", "Asia/Calcutta"};
    final JSpinner[] spinners = new JSpinner[zones.length];
    final JSpinner[] withCalendar =  new JSpinner[zones.length];
    
    JComponent box = Box.createHorizontalBox();
    box.add(createPanel(cal, zones, spinners, true));
    box.add(createPanel(cal, zones, withCalendar, false));
    
    JFrame frame = new JFrame();
    frame.add(box);
 
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

    /**
     * @param cal
     * @param zones
     * @param spinners
     * @return
     */
    private JPanel createPanel(Calendar cal, String[] zones,
            final JSpinner[] spinners, boolean core) {
        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));

        ChangeListener listener = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Object value = ((JSpinner) e.getSource()).getValue();
                for (JSpinner spinner : spinners) {
                    if (spinner != e.getSource()) {
                        spinner.setValue(value);
                    }
                }
            }
        };

        for (int i = 0; i < spinners.length; i++) {
            SpinnerDateModel model = null;
            if (core)
                model = new SpinnerDateModel();
            else {
                model = new DateSpinner(cal, Calendar.DAY_OF_MONTH, 1);
            }
            spinners[i] = new JSpinner(model);
            spinners[i].setBorder(new TitledBorder(zones[i]));
            SimpleDateFormat format = ((JSpinner.DateEditor) spinners[i]
                    .getEditor()).getFormat();
            format.setTimeZone(i == 0 ? new SimpleTimeZone(0, zones[i])
                    : TimeZone.getTimeZone(zones[i]));
            format.applyPattern("dd MMM yyyy, HH:mm");
            model.setValue(cal.getTime());
            spinners[i].addChangeListener(listener);
            panel.add(spinners[i]);
        }
        return panel;
    }
}
