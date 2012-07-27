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

import java.util.Locale;

/**
 * A suggestion algorithm suggests a list of candidate words given a word that is
 * not contained within the dictionary. The suggestions should include a probability
 * denoting how likely the suggestion is.
 * @author Ryan Cuprak
 */
public interface SuggestionAlgorithm {

    /**
     * Returns an array of suggestions for the misspelled word.
     * @param misspelledWord - misspelled word
     * @param dictionary - dictionary presenting in use
     * @return suggestions
     */
    public Suggestion[] suggest(String misspelledWord,Dictionary dictionary);

    /**
     * Returns the locales for which the spell.
     * @return supported locales
     */
    public Locale[] supportedLocales();
}
