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
package org.jdesktop.swingx;

import javax.swing.JTextField;

/**
 * A numeric input field that limits input to an integer (not floating point!).
 * This component has the following features:
 * <ul>
 *
 *
 * </ul>
 * @author Ryan Cuprak
 */
public class JXLongField extends JTextField {

    /**
     * Constructs a new <code>LongField</code>.  A default model is created,
     * the initial string is <code>null</code>,
     * and the number of columns is set to 0.
     */
    public JXLongField() {
        this("", 0);
    }

    /**
     * Constructs a new <code>LongField</code> initialized with the
     * specified text. A default model is created and the number of
     * columns is 0.
     *
     * @param text the text to be displayed, or <code>null</code>
     */
    public JXLongField(String text) {
        this(text, 0);
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
    public JXLongField(int columns) {
        this("",columns);
    }

    /**
     * Constructs a new JXLongField with the given value
     * @param value - value
     */
    public JXLongField(long value) {
        this(Long.toString(value));
    }

    /**
     * Constructs a new JXLongField with the given value
     * @param value - value
     * @param columns - columns to be displayed
     */
    public JXLongField(long value, int columns) {
        this(Long.toString(value),columns);
    }

    /**
     * Creates a new JXLongField with the initial value
     * @param text - text to be rendered
     * @param columns - columns
     */
    public JXLongField(String text, int columns) {
        super(text,columns);
        LongFieldDocument lfd = new LongFieldDocument();
        setDocument(lfd);
        if (text != null) {
            setText(text);
        }
        setHorizontalAlignment(JTextField.RIGHT);
    }

    /**
     * Sets the minimum comparison
     * @param minimumComparison - minimum comparison
     */
    public void setMinimumComparison(LongComparison minimumComparison) {
        LongFieldDocument ldf = (LongFieldDocument)getDocument();
        ldf.setMinimumEqualTo(minimumComparison.isEqualTo());
        ldf.setMinimumValue(minimumComparison.getValue());
    }

    /**
     * Sets the maximum comparison
     * @param maximumComparison - maximum comparison
     */
    public void setMaximumComparison(LongComparison maximumComparison) {
        LongFieldDocument ldf = (LongFieldDocument)getDocument();
        ldf.setMaximumEqualTo(maximumComparison.isEqualTo());
        ldf.setMaximumValue(maximumComparison.getValue());
    }

    /**
     * Returns the value of this long field/
     * @return long
     */
    public Long getValue() {
        return ((LongFieldDocument)getDocument()).getLongValue();
    }

}
