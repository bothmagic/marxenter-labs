/*
 * $Id: JXDatePickerFormatterFactory.java 46 2004-09-08 17:33:01Z rbair $
 * 
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved. 
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.UIManager;

/**
 * Default formatter factory for the JXDatePicker component.  This factory
 * creates and returns a formatter that can handle a variety of date formats.
 *
 * @author Joshua Outwater
 */
public class JXDatePickerFormatterFactory extends AbstractFormatterFactory {
    /** Cached formatter */
    protected AbstractFormatter formatter = null;

    /**
     * {@inheritDoc}
     */
    public AbstractFormatter getFormatter(JFormattedTextField ftf) {
        if (formatter == null) {
            formatter = new JXDatePickerFormatter();
        }
        return formatter;
    }

    /**
     * Default formatter class for the JXDatePicker component.  This formatter
     * supports the following three default formats:
     * <ul>
     * <li>EEE MM/dd/yyyy (Fri 04/09/2004)
     * <li>MM/dd/yyyy     (04/09/2004)
     * <li>dd/yy          (04/09)
     * </ul>
     * These formats are localizable and fields may be re-arranged, such as
     * swapping the month and day fields.  The keys for localizing these fields
     * are:
     * <ul>
     * <li>JXDatePicker.longFormat
     * <li>JXDatePicker.mediumFormat
     * <li>JXDatePicker.shortFormat
     * </ul>
     * It is important to order the formats in the order of most complex to
     * least complex as it is possible for less complex formats to match more
     * complex strings.
     */
    private class JXDatePickerFormatter extends
            JFormattedTextField.AbstractFormatter {
        private SimpleDateFormat _formats[] = null;
        private int _formatIndex = 0;
        
        public JXDatePickerFormatter() {
            _formats = new SimpleDateFormat[3];
            String format = UIManager.getString("JXDatePicker.longFormat");
            if (format == null) {
                format = "EEE MM/dd/yyyy";
            }
            _formats[0] = new SimpleDateFormat(format);

            format = UIManager.getString("JXDatePicker.mediumFormat");
            if (format == null) {
                format = "MM/dd/yyyy";
            }
            _formats[1] = new SimpleDateFormat(format);

            format = UIManager.getString("JXDatePicker.shortFormat");
            if (format == null) {
                format = "MM/dd";
            }
            _formats[2] = new SimpleDateFormat(format);
        }

        /**
         * {@inheritDoc}
         */
        public Object stringToValue(String text) throws ParseException {
            Object result = null;
            ParseException pex = null;

            if (text == null) {
                return null;
            }

            // Try the current formatter first.
            try {
                result = _formats[_formatIndex].parse(text);
            } catch (ParseException ex) {
                pex = ex;
            }

            // If the current formatter did not work loop through the other
            // formatters and see if any of them can parse the string passed
            // in.
            if (result == null) {
                for (int i = 0; i < _formats.length; i++) {
                    if (i == _formatIndex) {
                        continue;
                    }

                    try {
                        result = ((SimpleDateFormat)_formats[i]).parse(text);

                        // We got a successful formatter.  Update the current
                        // formatter index.
                        _formatIndex = i;
                        pex = null;
                        break;
                    } catch (ParseException ex) {
                        pex = ex;
                    }
                }
            }

            if (pex != null) {
                throw pex;
            }

            return result;
        }

        /**
         * {@inheritDoc}
         */
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                return _formats[_formatIndex].format(value);
            }
            return null;
        }
    }
}
