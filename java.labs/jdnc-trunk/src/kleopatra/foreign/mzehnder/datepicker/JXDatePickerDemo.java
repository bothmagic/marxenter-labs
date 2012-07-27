/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package mzehnder.datepicker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultFormatterFactory;

import org.jdesktop.swingx.JXDatePicker;

/**
 * Simple demo showing the <code>JXDatePicker</code> in action with the non 
 * default formatter <code>JXDatePickerDateFormatter</code>.
 * That formatter allows to spin through the date fields with the up / down
 * cursor keys.
 * 
 * JW: c&p'ed from mzehnder section. Uses standard JXDatePicker. 
 * 
 * @author Markus Zehnder
 */
public class JXDatePickerDemo {
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                
                JXDatePicker datePicker = new JXDatePicker();

                // replace the default formatter with another one
                JFormattedTextField dateField = datePicker.getEditor();
                dateField.setFormatterFactory(new DefaultFormatterFactory(
                        new JXDatePickerDateFormatter(datePicker.getFormats())));
                datePicker.getMonthView().setShowingWeekNumber(true);
                datePicker.getMonthView().setShowingLeadingDays(true);
                datePicker.getMonthView().setShowingTrailingDays(true);
                
                datePicker.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(
                                ((JXDatePicker) e.getSource()).getMonthView().getSelection());
                    }
                });
                frame.getContentPane().add(datePicker);
                frame.setResizable(true);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

}
