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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import com.sun.istack.internal.Nullable;

/**
 * Restricts input to be numeric and integer.
 * @author Ryan Cuprak
 */
public class LongFieldDocument extends PlainDocument {

    /**
     * Positive only
     */
    private Pattern positive = Pattern.compile("^\\d*$");

    /**
     * Negative only
     */
    private Pattern negative = Pattern.compile("^-?\\d*$");

    /**
     * Maximum value allowed
     */
    private String maximumValue = Long.toString(Long.MAX_VALUE);

    /**
     * Minimum value allowed
     */
    private String minimumValue = Long.toString(Long.MIN_VALUE);

    /**
     * Equal to or less than maximum value
     */
    private boolean maximumEqualTo = true;

    /**
     * Equal to or greater than minimum value
     */
    private boolean minimumEqualTo = true;

    /**
     * Sets the maximum value allowed
     * @param maximumValue - maximum value
     */
    public void setMaximumValue(long maximumValue) {
        this.maximumValue = Long.toString(maximumValue);
    }

    /**
     * Returns the maximum value allowed
     * @return maximum value allowed
     */
    public long getMaximumValue() {
        return Long.parseLong(this.maximumValue);
    }

    /**
     * Sets the minimum value allowed
     * @param minimumValue minimum value allowed
     */
    public void setMinimumValue(long minimumValue) {
        this.minimumValue = Long.toString(minimumValue);
    }

    /**
     * Returns the minimum value that is allowed.
     * @return minimum value
     */
    public long getMinimumValue() {
        return Long.parseLong(this.minimumValue);
    }

    /**
     * Returns true if the value is equal to the maximum
     * @param maximumEqualTo - true if equal to the maximum
     */
    public void setMaximumEqualTo(boolean maximumEqualTo) {
        this.maximumEqualTo = maximumEqualTo;
    }

    /**
     * Returns maximum equal to
     * @return maximum equal to
     */
    public boolean getMaximumEqualTo() {
        return maximumEqualTo;
    }

    /**
     * Sets the minimum equal to
     * @param minimumEqualTo - minimum equal to
     */
    public void setMinimumEqualTo(boolean minimumEqualTo) {
        this.minimumEqualTo = minimumEqualTo;
    }

    /**
     * REturns minimum equal to
     * @return minimum equal to
     */
    public boolean getMinimumEqualTo() {
        return minimumEqualTo;
    }

    /**
     * Inserts the string
     * @param offs - offset
     * @param str - string to be inserted
     * @param a - attribute set (ignored)
     * @throws BadLocationException - bad location
     */
    @Override
    public void insertString(int offs, String str,
                             @Nullable AttributeSet a) throws BadLocationException {
        String currentText = getText(0,getLength());
        StringBuilder builder = new StringBuilder(currentText);
        builder.insert(offs,str);
        if(getMinimumValue() > 0) {
            Matcher m = positive.matcher(builder.toString());
            if(m.find()) {
                if(!checkMax(builder.toString())) {
                    super.insertString(offs, str, a);
                }
            } else {
                // beep!
            }
        } else {
            Matcher m = negative.matcher(builder.toString());
            if(m.find()) {
                if(!checkMin(builder.toString())) {
                    super.insertString(offs, str, a);
                }
            } else {
                // beep!
            }
        }
    }

    /**
     * Check to make sure that the number is a valid long.
     * @param value to be checked
     * @return true if the value is a valid long
     */
    protected boolean isValidLong(String value) {
        if(value.startsWith("-")) {
            String min = Long.toString(Long.MIN_VALUE);
            if(value.length() > min.length()) {
                return false;
            }
            for(int i = value.length()-1; i >= 0; i--) {
                char valueChar = value.charAt(i);
                char minChar = min.charAt(i);
                if(valueChar > minChar) {
                    return true;
                }
            }
        } else {
            String max = Long.toString(Long.MAX_VALUE);
            if(value.length() < max.length()) {
                return false;
            }
            for(int i = value.length()-1; i >= 0; i--) {
                char valueChar = value.charAt(i);
                char maxChar = max.charAt(i);
                if(valueChar > maxChar) {
                    return true;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if the value is within the maximum range.
     * @param value - value to be checked
     * @return true if the value exceeds the maximum boundary
     */
    protected boolean checkMax(String value) {
        Long max = Long.parseLong(maximumValue);
        if(value.startsWith("-") && max > 0) {
            return false;
        }
        String maxStr = maximumValue;
        if(value.startsWith("-") && max < 0) {
            value = value.substring(1);
            maxStr = maxStr.substring(1);
            String tmp = value;
            value = maxStr;
            maxStr = tmp;
        }
        if(value.length() < maxStr.length()) {
            return false;
        } else if (value.length() > maxStr.length()) {
            return true;
        }
        // same length, let's go over each number
        for(int i = value.length()-1; i >= 0; i--) {
            char a = value.charAt(i);
            char b = maxStr.charAt(i);
            if(a > b) {
                return true;
            }
        }
        return !maximumEqualTo && maxStr.equals(value);
    }

    /**
     * Checks the minimum value
     * @param value - minimum value
     * @return true if it exceeds the minimum value
     */
    protected boolean checkMin(String value) {
        Long min = Long.parseLong(minimumValue);
        if(min < 0 && value.indexOf("-") == 0) {
            return false;
        }
        String minStr = minimumValue;
        if(value.length() > minStr.length()) {
            return false;
        }
        return false;
    }

    /**
     * Returns the value of the field as a long
     * @return Long
     */
    public Long getLongValue() {
        try {
            String txt = getText(0,getLength());
            if(isValidLong(txt)) {
                return Long.parseLong(txt);
            }
            return null;
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
