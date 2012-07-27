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

/**
 * Represents a long comparison.
 * The value and whether we are doing an "equal to" comparison.
 * @author
 */
public class LongComparison {

    /**
     * Equal to
     */
    private boolean equalTo;

    /**
     * Value
     */
    private long value;

    public LongComparison(boolean equalTo,long value) {
        this.equalTo = equalTo;
        this.value = value;
    }

    /**
     * Returns true if we are doing an equal to comparison
     * @return true for equal to
     */
    public boolean isEqualTo() {
        return equalTo;
    }

    /**
     * Returns the value
     * @return value
     */
    public long getValue() {
        return value;
    }
}
