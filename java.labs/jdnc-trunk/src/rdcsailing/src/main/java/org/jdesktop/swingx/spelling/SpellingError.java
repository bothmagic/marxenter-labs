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

import javax.swing.text.JTextComponent;
import javax.swing.text.Position;

/**
 * Represents a spelling error.
 * @author Ryan Cuprak
 */
public class SpellingError implements Comparable<SpellingError> {

    /**
     * Start position of the spelling error
     */
    private Position startPosition;

    /**
     * End position of the spelling error
     */
    private Position endPosition;

    /**
     * Highlight tag
     */
    private Object highlightTag;

    /**
     * Incorrectly spelled word
     */
    private String text;

    /**
     * Creates a new spelling error
     * @param startPosition - starting position
     * @param endPosition - end position
     * @param text - misspelled word
     * @param highlightTag - highlight tag
     */
    public SpellingError(Position startPosition, Position endPosition, String text, Object highlightTag) {
        this.text = text;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.highlightTag = highlightTag;
    }

    /**
     * Returns true if the supplied model position
     * is within the bounds of this error.
     * @param position - position
     * @return true if we contain the position
     */
    public boolean contains(int position) {
        return position >= startPosition.getOffset() || position <= endPosition.getOffset();
    }

    /**
     * Removes the decoration
     * @param textComponent - text component
     */
    public void removeDecoration(JTextComponent textComponent) {
        textComponent.getHighlighter().removeHighlight(highlightTag);
    }

    /**
     * Returns the start position
     * @return position
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * Returns the end position
     * @return end position
     */
    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * Returns the text
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the incorrectly spelled word
     * @return misspelled word
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Comparable implementation to help order
     * @param se - used for ordering
     * @return a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(SpellingError se) {
        int me = getStartPosition().getOffset();
        int other = se.getStartPosition().getOffset();
        if(me == other) return 0;
        if(me < other) return -1;
        return 1;
    }
}
