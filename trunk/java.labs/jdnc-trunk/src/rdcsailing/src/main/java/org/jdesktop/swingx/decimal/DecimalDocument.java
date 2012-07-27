/**
 * Copyright 2011 Cuprak Enterprise LLC.
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
package org.jdesktop.swingx.decimal;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * DecimalDocument - ensures that only valid values are inserted.
 * @author Ryan Cuprak
 */
public class DecimalDocument extends PlainDocument {

    /**
     * Formatter used for the field
     */
    private DecimalFormat decimalFormat;

    /**
     * Set of legal characters that a user is allowed to enter
     */
    protected String legalCharacters;

    /**
     * Constructs a new DecimalDocument
     * @param format - format
     */
    public DecimalDocument(DecimalFormat format) {
        setDecimalFormat(format);
    }

    /**
     * Constructs a new DecimalDocument
     */
    public DecimalDocument() {
        DecimalFormat df = (DecimalFormat)DecimalFormat.getNumberInstance();
        setDecimalFormat(df);
    }

    /**
     * Sets the decimal format
     * @param decimalFormat - decimal format
     */
    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
        StringBuilder allowed = new StringBuilder(20);
        allowed.append(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator());
        allowed.append(decimalFormat.getDecimalFormatSymbols().getMinusSign());
        allowed.append(decimalFormat.getDecimalFormatSymbols().getDecimalSeparator());
        allowed.append("1234567890");
        this.legalCharacters = allowed.toString();
    }

    /**
     * Returns the decimal format
     * @return decimal format
     */
    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    /**
     * Handles insertion of the string
     * @param offset - offset
     * @param str - string
     * @param s - attribute set (ignored)
     * @throws javax.swing.text.BadLocationException - thrown if the insert is invalid
     */
    @Override
    public void insertString(int offset, String str, AttributeSet s) throws BadLocationException {
        if(str.length() == 1) {
            if(legalCharacters.contains(str)) {
                super.insertString(offset,str,s);
            }
        } else {
            for(int i = 0; i < str.length(); i++) {
                if(!legalCharacters.contains(""+str.charAt(i))) {
                    return;
                }
            }
            super.insertString(offset, str, s);
        }
    }

    /***
     * Returns the formatted presentation
     * @return formatted presentation
     */
    public char[] getFormattedPresentation() {
        try {
            String text = super.getText(0,super.getLength());
            if(text.length() == 0) {
                return decimalFormat.format(0.0d).toCharArray();
            } else {
                return decimalFormat.format(Double.parseDouble(text)).toCharArray();
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the vale as a big decimal.
     * Note, this is predicated on restricting what the user can enter into the control.
     * @return value
     */
    public BigDecimal getValue() {
        try {
            return new BigDecimal(getText(0,getLength()));
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
