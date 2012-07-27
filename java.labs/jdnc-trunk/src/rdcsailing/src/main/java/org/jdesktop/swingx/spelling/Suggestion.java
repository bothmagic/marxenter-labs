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
package org.jdesktop.swingx.spelling;

/**
 * Represents a suggestions.
 * @author Ryan Cuprak
 */
public class Suggestion {

    /**
     * Occurrence count - in our trained set how many times does this word appear
     */
    private int occurrenceCount;

    /**
     * Word
     */
    private String word;

    /**
     * Creates a new suggestion
     * @param occurrenceCount - occurrence count
     * @param word - suggested word
     */
    public Suggestion(int occurrenceCount, String word) {
        this.occurrenceCount = occurrenceCount;
        this.word = word;
    }

    /**
     * Returns the occurrence count
     * @return occurrence count
     */
    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    /**
     * Returns the suggested word
     * @return word
     */
    public String getWord() {
        return word;
    }

    /**
     * Returns the suggested word
     * @return suggested word
     */
    @Override
    public String toString() {
        return word;
    }
}
