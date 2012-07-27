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

import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.jdesktop.swingx.decimal.DecimalDocument;
import org.jdesktop.swingx.decimal.DecimalFieldUI;

/**
 * This is a decimal field that handles decimal values WITHOUT converting them to floating point.
 */
public class JXDecimalField extends JTextField {

    /**
     * Constructs a new <code>LongField</code>.  A default model is created,
     * the initial string is <code>null</code>,
     * and the number of columns is set to 0.
     */
    public JXDecimalField() {
        this("0.0",15);
    }

    /**
     * Constructs a new <code>LongField</code> initialized with the
     * specified text. A default model is created and the number of
     * columns is 0.
     *
     * @param text the text to be displayed, or <code>null</code>
     */
    public JXDecimalField(String text) {
        this(text,15);
    }

    /**
     * Constructs a new empty <code>LongField</code> with the specified
     * number of columns.
     * A default model is created and the initial string is set to
     * <code>null</code>.
     *
     * @param columns  the number of columns to use to calculate
     *   the preferred width; if columns is set to zero, the
     *   preferred width will be whatever naturally results from
     *   the component implementation
     */
    public JXDecimalField(int columns) {
        this("",columns);
    }

    /**
     * Constructs a new JXLongField with the given value
     * @param value - value
     */
    public JXDecimalField(BigDecimal value) {
        this(value.toString());
    }

    /**
     * Constructs a new JXLongField with the given value
     * @param value - value
     * @param columns - columns to be displayed
     */
    public JXDecimalField(BigDecimal value, int columns) {
        this(value.toString(),columns);
    }

    /**
     * Creates a new JXLongField with the initial value
     * @param text - text to be rendered
     * @param columns - columns
     */
    public JXDecimalField(String text, int columns) {
        super(text,columns);
        DecimalDocument dd = new DecimalDocument(new DecimalFormat("##,####,###0.00"));
        setDocument(dd);
        if (text != null) {
            setText(text);
        }
        setHorizontalAlignment(JTextField.RIGHT);
    }

    /**
     * Make sure we make the right UI
     */
    @Override
    public void updateUI() {
        setUI(new DecimalFieldUI());
    }


    /**
     * Returns a new Decimal document
     * @return Document
     */
    @Override
    protected Document createDefaultModel() {
        return new DecimalDocument();
    }

    /**
     * Sets the presentation format
     * @param decimalFormat - decimal format
     */
    public void setFormat(DecimalFormat decimalFormat) {
        ((DecimalDocument)getDocument()).setDecimalFormat(decimalFormat);
    }

    /**
     * Returns the formatter being used.
     * @return format
     */
    public DecimalFormat getFormat() {
        return ((DecimalDocument)getDocument()).getDecimalFormat();
    }

    /**
     * Override to force a repaint.
     * @param e - focus event
     */
    @Override
    protected void processFocusEvent(FocusEvent e) {
        repaint();
        super.processFocusEvent(e);
    }

    /**
     * Sets the value
     * @param bigDecimal - BigDecimal
     */
    public void setValue(BigDecimal bigDecimal) {

    }

    /**
     * Returns the value as a BigDecimal
     * @return value
     */
    public BigDecimal getValue() {
        DecimalDocument dd = (DecimalDocument)getDocument();
        return dd.getValue();
    }
}
