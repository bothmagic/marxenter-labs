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
package org.jdesktop.swingx;

import java.awt.Font;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.jdesktop.swingx.currency.CurrencyDocument;
import org.jdesktop.swingx.currency.CurrencyFieldUI;

/**
 * JXCurrencyField is a lightweight component which edits a currency value. It accepts either a BigDecimal
 * or a Long. In the case of a Long, 1 must be defined.
 *
 * It is sensitive
 * to the current locale and adjusts accordingly.
 * Currencies are unique:
 * US Currency, US Locale: $4.45
 * GB Currency, GB Locale:
 * US Currency, GB Locale:
 * US Currency, New Zealand Locale:
 *
 * @author Ryan Cuprak
 */
public class JXCurrencyField extends JTextField {

    /**
     * Format of the currency field
     */
    private DecimalFormat format = (DecimalFormat)DecimalFormat.getCurrencyInstance();

    /**
     * Configures the currency field with an initial format.
     * @param format - format of the text field.
     */
    public JXCurrencyField(DecimalFormat format) {
        super(new CurrencyDocument(), "", 10);
        this.format = format;
    }

    /**
     * Creates a new currency field
     */
    public JXCurrencyField() {
        this((DecimalFormat) DecimalFormat.getCurrencyInstance());
        format = (DecimalFormat)DecimalFormat.getCurrencyInstance();
        ((CurrencyDocument)getDocument()).setCurrencyField(this);
        setHorizontalAlignment(JTextField.RIGHT);
        setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    /**
     * Sets the format
     * @param format - new format
     */
    public void setFormat(DecimalFormat format) {
        this.format = format;
        repaint();
    }

    /**
     * Sets the currency
     * @param currency - sets the currency
     */
    public void setCurrency(Currency currency) {
        format.setCurrency(currency);
        repaint();
    }

    public void setLocale(Locale locale) {
        super.setLocale(locale);
        repaint();
    }

    /**
     * Returns the decimal format being
     * @return format
     */
    public DecimalFormat getFormat() {
        if(format == null) {
            return (DecimalFormat)DecimalFormat.getCurrencyInstance();
        }
        return format;

    }

    /**
     * Sets the value of the field
     * @param value - field value
     */
    public void setValue(long value) {

    }

    /**
     * Sets the value as a BigDecimal
     * @param value - value as a BigDecimal
     */
    public void setValue(BigDecimal value) {
        setText(value.toString());
    }

    /**
     * Returns the value of the currency
     * @return currency value
     */
    public BigDecimal getValue() {
        return ((CurrencyDocument)getDocument()).getValue();
    }

    /**
     * Make sure we make the right UI
     */
    @Override
    public void updateUI() {
        setUI(new CurrencyFieldUI());
    }

    /**
     * Returns a new Decimal document
     * @return Document
     */
    @Override
    protected Document createDefaultModel() {
        return new CurrencyDocument();
    }

    /**
     * Returns the current value
     * @return currency
     */
    public BigDecimal getCurrency() {
        return null;
    }
}
