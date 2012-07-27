package org.jdesktop.swingx.spelling;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Default suggestion algorithm.
 * This a simple algorithm based upon an article posted by Peter Norvig:
 * http://www.norvig.com/spell-correct.html
 * TODO: Finish implementing this algorithm and add test cases.
 *
 * @author Ryan Cuprak
 */
public class DefaultSuggestionAlgorithm implements SuggestionAlgorithm {

    /**
     * Supported locales
     */
    private static final Locale supportedLocales[] = new Locale[2];

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    static {
        supportedLocales[0] = Locale.US;
        supportedLocales[1] = Locale.CANADA;
    }

    /**
     * Returns an array of suggestions for the misspelled word.
     *
     * @param misspelledWord - misspelled word
     * @param dictionary     - dictionary presenting in use
     * @return suggestions
     */
    @Override
    public Suggestion[] suggest(String misspelledWord, Dictionary dictionary) {
        List<Suggestion> candidates = new LinkedList<Suggestion>();
        String str;
        // delete one letter
        StringBuilder builder;
        for (int i = 0; i < misspelledWord.length(); i++) {
            builder = new StringBuilder(misspelledWord);
            builder.delete(i, i + 1);
            if (dictionary.exists(builder.toString())) {
                str = builder.toString();
                candidates.add(new Suggestion(dictionary.getOccurrenceCount(str),str));
            }
        }
        // transposition
        char first[] = new char[1];
        char second[] = new char[1];
        for (int i = 0; i < misspelledWord.length() - 1; i++) {
            builder = new StringBuilder(misspelledWord);
            builder.getChars(i, i + 1, first, 0);
            builder.getChars(i + 1, i + 2, second, 0);
            builder.setCharAt(i, second[0]);
            builder.setCharAt(i + 1, first[0]);
            if (dictionary.exists(builder.toString())) {
                str = builder.toString();
                candidates.add(new Suggestion(dictionary.getOccurrenceCount(str),str));
            }
        }
        builder = new StringBuilder(misspelledWord);
        // alteration
        for (int i = 0; i < misspelledWord.length(); i++) {
            for (int k = 0; k < ALPHABET.length(); k++) {
                builder.replace(i, i + 1, ALPHABET.substring(k, k + 1));
                if (dictionary.exists(builder.toString())) {
                    str = builder.toString();
                    candidates.add(new Suggestion(dictionary.getOccurrenceCount(str),str));
                }
                builder.replace(0, builder.length(), misspelledWord);
            }
        }
        // insertion
        builder = new StringBuilder(misspelledWord);
        for (int i = 0; i < misspelledWord.length(); i++) {
            for (int k = 0; k < ALPHABET.length(); k++) {
                builder.insert(i, ALPHABET.substring(k, k + 1));
                if (dictionary.exists(builder.toString())) {
                    str = builder.toString();
                    candidates.add(new Suggestion(dictionary.getOccurrenceCount(str),str));
                }
                builder.replace(0, builder.length(), misspelledWord);
            }
        }
        Suggestion a[] = new Suggestion[candidates.size()];
        return candidates.toArray(a);
    }

    /**
     * Returns the locales for which the spell.
     *
     * @return supported locales
     */
    @Override
    public Locale[] supportedLocales() {
        return supportedLocales;
    }
}
