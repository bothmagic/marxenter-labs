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
package org.jdesktop.swingx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Dissected Decimal Number
 * @author Ryan Cuprak
 */
public class DissectedNumber {

    /**
     * Pattern for splitting a number apart
     */
    private static final Pattern decimalPattern = Pattern.compile("(-)?(\\d*).?(\\d*)");

    /**
     * Integer part
     */
    private String integer;

    /**
     * Decimal part
     */
    private String decimal;

    /**
     * Flag indicating if the number is negative
     */
    private boolean isNegative;


    /**
     * Discests a number
     * @param number - number
     */
    public DissectedNumber(String number) {
        number = number.trim();
        if(number.length() == 0) {
            isNegative = false;
            integer = "0";
            decimal = "0";
        } else {
            Matcher matcher = decimalPattern.matcher(number);
            if(matcher.matches()) {
                isNegative = matcher.group(1) != null;
                integer = matcher.group(2) != null ? matcher.group(2) : "0";
                decimal = matcher.group(3) != null ? matcher.group(3) : "0";
            } else {
                throw new IllegalArgumentException("The number " + number + " is not a valid number.");
            }
        }
    }

    /**
     * Creates a new Number
     * @param isNegative - flag indicating if the number is negative
     * @param integer - integer part
     * @param decimal - decimal part
     */
    public DissectedNumber(boolean isNegative, String integer,String decimal) {
        this.isNegative = isNegative;
        this.integer = integer;
        this.decimal = decimal;
    }

    public String getInteger() {
        return integer;
    }

    public String getDecimal() {
        return decimal;
    }

    /**
     * Returns the decimal with the specified number of places. No rounding is performed.
     * @param places - places
     * @return String
     */
    public String getDecimal(int places) {
        if(decimal.length() < places) {
            StringBuilder builder = new StringBuilder(decimal);
            int missingCount = places - builder.length();
            for(int i = 0; i < missingCount; i++) {
                builder.append("0");
            }
            return builder.toString();
        }
        return decimal.substring(0,places);
    }

    /**
     * Returns true if the number is negative
     * @return true if negative
     */
    public boolean isNegative() {
        return isNegative;
    }
}
