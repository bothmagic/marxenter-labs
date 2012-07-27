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

import javax.swing.text.Position;

/**
 * Encapsulates a word and its position within the document.
 * @author Ryan Cuprak
 */
public class Word {

    /**
     * Position of the word
     */
    private Position position;

    /**
     * Word
     */
    private CharSequence word;

    /**
     * Creates a new word
     * @param position - position of the word within the document
     * @param word - word
     */
    public Word(Position position, CharSequence word) {
        this.position = position;
        this.word = word;
    }

    /**
     * Returns the position of the word
     * @return word position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the word
     * @return word
     */
    public CharSequence getWord() {
        return word;
    }

    public String toString() {
        return word.toString();
    }
}
