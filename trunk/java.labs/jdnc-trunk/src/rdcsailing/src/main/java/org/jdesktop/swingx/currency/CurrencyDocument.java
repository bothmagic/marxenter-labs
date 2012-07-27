/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx.currency;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.PlainDocument;
import org.jdesktop.swingx.JXCurrencyField;
import org.jdesktop.swingx.util.DissectedNumber;

/**
 * Decimal document - handles the entry of decimal data.
 * @author Ryan Cuprak
 */
public class CurrencyDocument extends PlainDocument {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(CurrencyDocument.class.getName());

    /**
     * Locale specific separator being used
     */
    private String decimalSeparator;

    /**
     * Regex pattern used to evaluate string input
     */
    protected Pattern numericPattern;

    /**
     * Maximum number of integer places to display
     */
    protected int maxIntegerPlaces = 5;

    /**
     * True if the number can be negative
     */
    protected boolean canBeNegative = true;

    /**
     * Reference to the currency field
     */
    private JXCurrencyField currencyField;

    /**
     * Creates a new decimal document
     */
    public CurrencyDocument() {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        decimalSeparator = formatSymbols.getDecimalSeparator()+"";
        numericPattern = Pattern.compile("\\d*");
    }

    /**
     * Returns true if the value is a number
     * @param str - string to examine
     * @return boolean
     */
    protected boolean isNumeric(String str) {
        Matcher m = numericPattern.matcher(str);
        return m.matches();
    }

    /**
     * Sets the currency field
     * @param currencyField - currency field
     */
    public void setCurrencyField(JXCurrencyField currencyField) {
        this.currencyField = currencyField;
    }

    /**
     * Handles insertion of the string
     * @param offset - offset
     * @param str - string
     * @param s - attribute set (ignored)
     * @throws BadLocationException - thrown if the insert is invalid
     */
    @Override
    public void insertString(int offset, String str, AttributeSet s) throws BadLocationException {
        str = str.trim();
        String currentText = getText(0,getLength());
        StringBuilder builder = new StringBuilder(currentText);
        if(str.length() == 1) {
            if(str.contains(decimalSeparator) && currentText.contains(decimalSeparator)) {
                Caret caret = currencyField.getCaret();
                caret.setDot(caret.getDot()+1);
                return; // we already have a decimal point
            } else if(str.contains(decimalSeparator)) {
                super.insertString(offset,str,s); // insert the decimal point
            }
            builder.insert(offset,str);
            DissectedNumber dn = new DissectedNumber(builder.toString());
            if(dn.getInteger().length() > maxIntegerPlaces) {
                return;
            }
            builder = new StringBuilder(currentText);
            if(str.contains("-") && offset == 0 && !currentText.contains("-")) {
                if(canBeNegative) {
                    super.insertString(offset,str,s);
                } else {
                    return;
                }
            }
            if(isNumeric(str)) {
                builder.insert(offset,str);
                if(offset > currentText.lastIndexOf(".") && currentText.lastIndexOf(".") > 0 && offset != currentText.length()) {
                    remove(offset,1);
                    currentText = getText(0,getLength());
                    builder = new StringBuilder(currentText);
                }
                int decimalPos = builder.indexOf(decimalSeparator);
                if(decimalPos > 0 && (currentText.length() - decimalPos) > 2) {
                    return;
                }

                super.insertString(offset,str,s);
            }
        } else {
            super.insertString(offset,str,s);
        }
        getText(0,getLength());
    }


    /**
     * Returns the value of the field as a BigDecimal
     * @return BigDecimal
     */
    public BigDecimal getValue() {
        try {
            String currentText = getText(0,getLength());
            return new BigDecimal(currentText);
        } catch (Exception e) {
            logger.log(Level.INFO,"Unable to create a big decimal - probably invalid data.",e);
            return null;
        }
    }

}
