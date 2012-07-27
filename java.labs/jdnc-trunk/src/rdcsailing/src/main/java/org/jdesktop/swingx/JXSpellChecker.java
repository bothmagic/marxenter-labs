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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import org.jdesktop.swingx.spelling.DefaultSuggestionAlgorithm;
import org.jdesktop.swingx.spelling.Dictionary;
import org.jdesktop.swingx.spelling.SpellCheckerException;
import org.jdesktop.swingx.spelling.SpellCheckerHighlightPainter;
import org.jdesktop.swingx.spelling.SpellingError;
import org.jdesktop.swingx.spelling.Suggestion;
import org.jdesktop.swingx.spelling.SuggestionAlgorithm;
import org.jdesktop.swingx.spelling.SuggestionDialog;
import org.jdesktop.swingx.spelling.Word;
import org.jdesktop.swingx.util.ComponentUtil;

/**
 * This class is responsible for handling spell checking.
 * @author Ryan Cuprak
 */
public class JXSpellChecker {

    /**
     * Dictionary
     */
    private Dictionary dictionary;

    /**
     * Locale for the spellchecker
     */
    private Locale locale;

    /**
     * Text component to which this spell checker is attached.
     * This cannot be changed after it is instantiated!
     */
    private final JTextComponent textComponent;

    /**
     * Highlight paint
     */
    private SpellCheckerHighlightPainter highlightPaint;

    /**
     * Spelling errors we are tracking
     */
    private Set<SpellingError> spellingErrors = new HashSet<SpellingError>();

    /**
     * Preferred algorithms.
     */
    private static Map<Locale,SuggestionAlgorithm> preferredSuggestionAlgorithms = new HashMap<Locale, SuggestionAlgorithm>();

    /**
     * All of the configured suggestion algorithms.
     */
    private static Set<SuggestionAlgorithm> allSuggestionAlgorithms = new HashSet<SuggestionAlgorithm>();

    /**
     * Pattern to extract words for spellchecking
     */
    protected static Pattern wordExtractionPattern = Pattern.compile("\\b(\\w*)\\b");

    /**
     * Last position of the caret
     */
    private int lastCaretPosition;

    /**
     * Suggestion dialog
     */
    private SuggestionDialog suggestionDialog;

    /**
     * Configure the default algorithm
     */
    static {
        DefaultSuggestionAlgorithm dsa = new DefaultSuggestionAlgorithm();
        for(Locale l : dsa.supportedLocales()) {
            preferredSuggestionAlgorithms.put(l,dsa);
        }
        allSuggestionAlgorithms.add(dsa);
    }

    /**
     * Throws a SpellCheckerException
     * @param textComponent - text component
     * @throws org.jdesktop.swingx.spelling.SpellCheckerException - thrown if a suitable dictionary is not available.
     */
    public JXSpellChecker(JTextComponent textComponent) throws SpellCheckerException {
        this(textComponent,Locale.getDefault());
    }

    /**
     * Constructs a new spell checker
     * @param textComponent - text component
     * @param locale - locale of the spell checker
     * @throws SpellCheckerException - thrown if a suitable dictionary is not available.
     */
    public JXSpellChecker(JTextComponent textComponent, Locale locale) throws SpellCheckerException {
        this.textComponent = textComponent;
        this.locale = locale;
        dictionary = new Dictionary();
        textComponent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                expandScanCheck(documentEvent.getOffset(),documentEvent.getOffset()+documentEvent.getLength());
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                expandScanCheck(documentEvent.getOffset(), documentEvent.getOffset() + documentEvent.getLength());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        textComponent.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                // If the caret did a big jump, like arrowed down, check the last position
                if(Math.abs(e.getDot() - lastCaretPosition) > 1) {
                    expandScanCheck(lastCaretPosition, lastCaretPosition + 1);
                }
                lastCaretPosition = e.getDot();
                // If the caret is now position on a spelling error, remove it.
                if(e.getDot() == e.getMark()) {
                    SpellingError se = getSpellingErrorForCaret(e.getDot());
                    if(se != null) {
                        removeSpellingError(se);
                    }
                }
            }
        });
        highlightPaint = new SpellCheckerHighlightPainter();
        Keymap keyMap =  textComponent.getKeymap();
        keyMap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SEMICOLON, InputEvent.CTRL_MASK), new DisplaySpellCheckerAction());
        textComponent.setKeymap(keyMap);
    }

    /**
     * Displays the spellchecker if the cursor is on a spelling error.
     */
    public class DisplaySpellCheckerAction extends AbstractAction {

        /**
         * Displays the dialog for correcting spelling errors.
         */
        public DisplaySpellCheckerAction() {
            super("Check Spelling");
        }

        /**
         * Displays the dialog if it exists.
         * @param e - action event
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Caret caret = textComponent.getCaret();
            if(caret.getDot() == caret.getMark()) {
                SpellingError se =  checkSingleWord(caret.getDot());
                if(se != null) {
                    if(suggestionDialog == null) {
                        Object obj = ComponentUtil.getParentFrameDialog(textComponent);
                        if(obj instanceof Frame) {
                            suggestionDialog = new SuggestionDialog((Frame)obj,JXSpellChecker.this);
                        } else if(obj instanceof Dialog) {
                            suggestionDialog = new SuggestionDialog((Dialog)obj,JXSpellChecker.this);
                        }
                    }
                    suggestionDialog.setSpellingError(se);
                    suggestionDialog.setVisible(true);
                }
            }
        }
    }

    /**
     * Returns the text component on which we are bound
     * @return text component
     */
    public JTextComponent getTextComponent() {
        return textComponent;
    }

    /**
     * Returns the list of suggestion algorithms that have been registered
     * @return suggestion algorithms
     */
    public static SuggestionAlgorithm[] getSuggestionAlgorithms() {
        SuggestionAlgorithm sa[] = new SuggestionAlgorithm[allSuggestionAlgorithms.size()];
        return allSuggestionAlgorithms.toArray(sa);
    }

    /**
     * Registers a suggestion algorithm
     * @param suggestionAlgorithm - suggestion algorithm
     */
    public static void registerSuggestionAlgorithm(SuggestionAlgorithm suggestionAlgorithm) {
        allSuggestionAlgorithms.add(suggestionAlgorithm);
    }

    /**
     * Configures a preferred algorithm for a given locale
     * @param locale - locale
     * @param suggestionAlgorithm - suggestion algorithm
     */
    public static void setPreferredAlgorithm(Locale locale, SuggestionAlgorithm suggestionAlgorithm) {
        preferredSuggestionAlgorithms.put(locale,suggestionAlgorithm);
    }

    /**
     * Returns true if the caret is in an error
     * @param dot - cursor position
     * @return true if in spelling error
     */
    protected SpellingError getSpellingErrorForCaret(int dot) {
        for(SpellingError se : spellingErrors) {
            if(dot >= se.getStartPosition().getOffset() && dot <= se.getEndPosition().getOffset()) {
                return se;
            }
        }
        return null;
    }

    /**
     * Returns the next spelling error
     * @return Spelling Error
     */
    public List<SpellingError> getSpellingErrors() {
        List<SpellingError> errors = new ArrayList<SpellingError>(spellingErrors);
        Collections.sort(errors);
        return errors;
    }

    /**
     * Removes a spelling error - both the record and the decoration.
     * This is usually performed if the caret is in a word that is misspelled or
     * a word has been corrected.
     * @param spellingError - spelling error
     */
    protected void removeSpellingError (SpellingError spellingError) {
        spellingError.removeDecoration(textComponent);
        spellingErrors.remove(spellingError);
    }

    /**
     * Scans both forward and backward for words.
     * @param start - starting position of the inserted text
     * @param end - end position of the inserted text
     */
    protected void expandScanCheck(int start,int end) {
        try {
            if(start != 0) {
                start--;
            }
            Segment segment = new Segment();
            textComponent.getDocument().getText(0, textComponent.getDocument().getLength(), segment);
            for(int i = start; i >= 0; i--) {
                if(segment.charAt(i) == ' ' || segment.charAt(i) == '\n') {
                    start = i;
                    break;
                }
            }
            for(int i = end; i < textComponent.getDocument().getLength(); i++) {
                if(segment.charAt(i) == ' ' || segment.charAt(i) == '\n') {
                    end = i;
                    break;
                }
            }
            spellCheck(start,end);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the word to the dictionary
     * @param word - word to be added to the dictionary
     */
    public void learn(String word) {
        dictionary.addWord(word);
    }

    /**
     * Checks the spelling of the entire document.
     */
    public void check() {
        spellCheck(0,textComponent.getDocument().getLength());
    }

    /**
     * Performs spellchecking on the specified range.
     * @param start - start of the section to check
     * @param stop - end of the section to check
     */
    public void spellCheck(int start, int stop) {
        String text = textComponent.getText().substring(start,stop);
        Matcher m = wordExtractionPattern.matcher(text);
        String word;
        while(m.find()) {
            word = m.group(0);
            if(word.length() > 0) {
                if(!dictionary.exists(word)) {
                    try {
                        Object tag = textComponent.getHighlighter().addHighlight(start+m.start(),start+m.end(),highlightPaint);
                        spellingErrors.add(new SpellingError(textComponent.getDocument().createPosition(start+m.start()),textComponent.getDocument().createPosition(start+m.end()),word,tag));
                    } catch (BadLocationException e) {
                        throw new RuntimeException("An error occurred performing spell checking.",e);
                    }
                }
            }
        }
    }

    /**
     * Returns a spelling error if one exists at the current position.
     * @param start - position of the spelling error
     * @return SpellingError
     */
    protected SpellingError checkSingleWord(int start) {
        try {
            int end = start;
            String txt = textComponent.getText();
            int i = start;
            while(i >= 0) {
                if(txt.charAt(i) == ' ' || txt.charAt(i) == '\n') {
                    start = i;
                    break;
                }
                i--;
            }
            i = start+1;
            while(i < txt.length()) {
                if(txt.charAt(i) == ' ' || txt.charAt(i) == '\n') {
                    end = i;
                    break;
                }
                i++;
            }
            txt = txt.substring(start,end);
            Matcher m = wordExtractionPattern.matcher(txt);
            if(m.find()) {
                txt = m.group();
                Position s = textComponent.getDocument().createPosition(start);
                Position e = textComponent.getDocument().createPosition(start+txt.length());
                return new SpellingError(s,e,txt,null);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Returns a suggestion for the word. Assuming that the word is invalid.
     * @param word - improperly spelled word.
     * @return candidate words in ranked order
     * @throws SpellCheckerException - thrown if we attempt to get a suggestion for an unsupported locale.
     */
    public Suggestion[] getSuggestion(String word) throws SpellCheckerException {
        if(preferredSuggestionAlgorithms.get(locale) != null) {
            return preferredSuggestionAlgorithms.get(locale).suggest(word,dictionary);
        }
        SuggestionAlgorithm algorithm = null;
        for(SuggestionAlgorithm sa : allSuggestionAlgorithms) {
            for(Locale l : sa.supportedLocales()) {
                if(l.equals(locale)) {
                    algorithm = sa;
                }
            }
        }
        if(algorithm != null) {
            return algorithm.suggest(word,dictionary);
        }
        throw new SpellCheckerException("No spell suggestion algorithm could be found for the locale: " + locale);
    }

    /**
     * Checks a block of text and marks it up with a spelling error if an error exists.
     * @param word - word to be checked
     */
    protected void checkSpelling(Word word) {
        if(!dictionary.exists(word.getWord().toString())) {
            try {
                int start = word.getPosition().getOffset();
                int stop = start + word.getWord().length();
                Object tag = textComponent.getHighlighter().addHighlight(start,stop,highlightPaint);
                spellingErrors.add(new SpellingError(textComponent.getDocument().createPosition(start),textComponent.getDocument().createPosition(stop),word.getWord().toString(),tag));
                SpellingError se = new SpellingError(textComponent.getDocument().createPosition(start),textComponent.getDocument().createPosition(stop),word.getWord().toString(),tag);
                spellingErrors.add(se);
            } catch (Exception e) {

            }
        }
    }

}
