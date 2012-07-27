/*
 * Created on 06.11.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TimeZoneSpinnerLabels {
    
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          new TimeZoneSpinnerLabels().makeUI();
        }
      });
    }
   
    public void makeUI() {
      final String[] zones = {"Asia/Tokyo", "Asia/Hong_Kong",
        "Asia/Calcutta", "Europe/Paris", "Europe/London",
        "America/New_York", "America/Los_Angeles"
      };
      final JLabel[] labels = new JLabel[zones.length];
      final SimpleDateFormat[] formats = new SimpleDateFormat[zones.length];
   
      SpinnerDateModel model = new SpinnerDateModel();
      Calendar cal = Calendar.getInstance();
      Date date = cal.getTime();
      model.setValue(date);
   
      JSpinner spinner = new JSpinner(model);
      spinner.addChangeListener(new ChangeListener(){
        public void stateChanged(ChangeEvent e){
          Date date = (Date)((JSpinner) e.getSource()).getValue();
          for(int i = 0; i < labels.length; i++){
            labels[i].setText(formats[i].format(date));
          }
        }
      });
   
      SimpleDateFormat format = ((JSpinner.DateEditor) spinner.getEditor()).getFormat();
      format.setTimeZone(TimeZone.getTimeZone(zones[0]));
      format.applyPattern("yyyy-MM-dd HH:mm:ss");
   
      JPanel panel = new JPanel(new GridLayout(zones.length, 2, 4, 4));
      for (int i = 0; i < zones.length; i++) {
        formats[i] = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        formats[i].setTimeZone(TimeZone.getTimeZone(zones[i]));
   
        JLabel label = new JLabel(zones[i]);
        labels[i] = new JLabel("yyyy-MMM-dd HH:mm:ss");
        panel.add(label);
        panel.add(labels[i]);
      }
   
      JFrame frame = new JFrame();
      frame.getContentPane().add(spinner, "North");
      frame.getContentPane().add(panel, "Center");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.pack();
      frame.setVisible(true);
    }
  }