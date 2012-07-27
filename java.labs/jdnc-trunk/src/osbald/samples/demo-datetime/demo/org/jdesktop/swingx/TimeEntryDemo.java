package org.jdesktop.swingx;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Error and boundary conditions have interesting (sometimes non-consistent) effects here (enter values & hit return).
 * The FormattedField derivatives don't accept values on <enter> (while JSpinner does)? Some values overflow/add modulus
 * while other formatting errors causes the whole input to be ignored (previous value restored which can be confusing! wtf!).
 * Try setting minutes to 90 and swap focus(tab). eg. "00:90:00" (1.30am) vs "19:90:00" (adds one hour thirty or 8:30pm)
 * whereas "77:00:00" is always 5am.
 *
 * Almost handy but far from intuitive..? IMHO The strictness of the default controls makes them uncomfortable to use.
 *
 * Created by IntelliJ IDEA.
 * User: rosbaldeston
 * Date: 05-Apr-2007
 * Time: 11:05:38
 */

public class TimeEntryDemo {
    Date date = new Date();
    JLabel dateLabel = new JLabel("date=" + date.toString());

    //nb tendancy for intial sizes of formatted intputs to be too small?
    static boolean FLEXO = System.getProperty("resizable") != null; // oh dear!

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TimeEntryDemo();
            }
        });
    }

    public TimeEntryDemo() {
        JFrame frame = new JFrame("Time inputs...");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JComponent content = (JComponent) frame.getContentPane();
        content.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        String dateString = getTimeFormat().format(getDate());
        PanelBuilder pb = new PanelBuilder();

        final JTextField textField = new JTextField(8);
        textField.setText(dateString);
        pb.addLabeledComponent("JTextField (8):", textField);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setDate(getTimeFormat().parse(textField.getText().trim()));
                } catch (ParseException err) {
                    JOptionPane.showMessageDialog(null, err);
                }
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setLenient(true);
        final JFormattedTextField formattedTextField = new JFormattedTextField(dateFormat);
        formattedTextField.setValue(getDate());
        pb.addLabeledComponent("JFormattedTextField:", formattedTextField);
        formattedTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDate((Date) formattedTextField.getValue());
            }
        });

        MaskFormatter formatter = null;
        try {
            // note lack of mask formats for hours, mins, secs
            // I like the autoinsertion of colons here - except for the forced zero padding
            formatter = new MaskFormatter("##:##:##");
        } catch (ParseException e) {
            e.printStackTrace(System.err);
        }
        final JFormattedTextField maskedTextField = new JFormattedTextField(formatter);
        //maskedTextField.setText(dateString);
        pb.addLabeledComponent("MaskedFormattedField:", maskedTextField);
        maskedTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setDate(getTimeFormat().parse(maskedTextField.getText().trim()));
                } catch (ParseException err) {
                    // shouldnt happen although youll see some pretty odd overflow effects (try 99:99:99)
                    JOptionPane.showMessageDialog(null, err);
                }
            }
        });

        final JSpinner timeSpinner = new JSpinner(new SpinnerDateModel(getDate(), null, null, Calendar.MINUTE));
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm:ss"));
        pb.addLabeledComponent("JSpinner (DateEditor):", timeSpinner);
        timeSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setDate((Date) timeSpinner.getValue());
            }
        });

        final JXDatePicker dateTimePicker = new JXDatePicker(getDate());
        dateTimePicker.setFormats(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        pb.addLabeledComponent("JXDatePicker (karls):", dateTimePicker);
        dateTimePicker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDate(dateTimePicker.getDate());
            }
        });

        try {
            // fallback gracefully ..as incubator can be a PITA when building contributions
            Class JTimeFieldClass = Class.forName("org.jdesktop.jdnc.incubator.ronaldtm.calendar.JTimeField");
            pb.addLabeledComponent("ronaldtm.calendar.JTimeField:", (JComponent) JTimeFieldClass.newInstance());
        } catch (Exception e) {
            pb.addLabeledComponent("ronaldtm.calendar.JTimeField:", new JLabel("<html><body style='color:red'>Not Found!"));
            e.printStackTrace(System.err);
        }

        try {
            // fallback gracefully ..as incubator can be a PITA when building contributions
            Class DateInputFieldClass = Class.forName("dateinput.DateInputField");
            Constructor cons = DateInputFieldClass.getConstructor(String.class);
            pb.addLabeledComponent("rturnbull.DateInputField:", (JComponent) cons.newInstance("HH:mm:ss"));
        } catch (Exception e) {
            pb.addLabeledComponent("rturnbull.DateInputField:", new JLabel("<html><body style='color:red'>Not Found!"));
            e.printStackTrace(System.err);
        }

        content.add(pb.getPanel());
        content.add(dateLabel, BorderLayout.SOUTH);
        frame.setSize(360, 320);
        frame.setVisible(true);
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    void setDate(Date date) {
        this.date = date;
        dateLabel.setText("date=" + date.toString());
    }

    DateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    // only layout.. nothing to see here.. move on

    class PanelBuilder {
        JPanel panel;
        GridBagConstraints labelGBC;
        GridBagConstraints componentGBC;
        int row;

        public PanelBuilder() {
            this.panel = new JPanel(new GridBagLayout());
            Insets insets = new Insets(5, 5, 5, 5);
            labelGBC = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, insets, 0, 0);
            componentGBC = new GridBagConstraints(GridBagConstraints.RELATIVE, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, FLEXO ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE, insets, 0, 0);
        }

        public void addLabeledComponent(String label, JComponent component) {
            panel.add(new JLabel(label), getLabelGBC());
            panel.add(component, getComponentGBC());
            component.setMinimumSize(component.getPreferredSize());     // ok, I derseve a slap for this - I still (heart) gridbag<g>
            row += 1;
        }

        GridBagConstraints getLabelGBC() {
            labelGBC.gridy = row;
            return labelGBC;
        }

        GridBagConstraints getComponentGBC() {
            componentGBC.gridy = row;
            return componentGBC;
        }

        public JPanel getPanel() {
            return panel;
        }
    }
}
