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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.JXSpellChecker;

/**
 * Suggestion dialog
 */
public class SuggestionDialog extends JDialog {

    /**
     * Change button
     */
    private JButton change = new JButton("Change");

    /**
     * Jump to the next spelling suggestion
     */
    private JButton find = new JButton("Find Next");

    /**
     * Ignore the current spelling error
     */
    private JButton ignore = new JButton("Ignore");

    /**
     * Add the the dictionary
     */
    private JButton learn = new JButton("Learn");

    /**
     * Close this window
     */
    private JButton close = new JButton("Close");

    /**
     * Message regarding the searching
     */
    private JLabel message = new JLabel();

    /**
     * Misspelled word
     */
    private JTextField misspelledWord = new JTextField(25);

    /**
     * Suggestions
     */
    private JList suggestions = new JList(new String[] {"Hi","El"});

    /**
     * Spellchecker
     */
    private JXSpellChecker JXSpellChecker;

    /**
     * Current spelling error we are working on
     */
    private SpellingError spellingError;

    /**
     * List of spelling errors
     */
    private Iterator<SpellingError> spellingErrors;

    /**
     * Creates a new suggestion dialog.
     * @param owner - owning frame
     * @param JXSpellChecker - spellchecker
     */
    public SuggestionDialog(Frame owner, JXSpellChecker JXSpellChecker) {
        super(owner,"Spelling",true);
        this.JXSpellChecker = JXSpellChecker;
        init();
    }

    /**
     * Creates a new suggestion dialog.
     * @param owner - owning frame
     * @param JXSpellChecker - spellchecker
     */
    public SuggestionDialog(Dialog owner, JXSpellChecker JXSpellChecker) {
        super(owner,"Spelling",true);
        this.JXSpellChecker = JXSpellChecker;
        init();
    }

    /**
     * Initializes the dialog
     */
    protected void init() {
        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        content.add(misspelledWord,gc);
        gc.gridx = 1; gc.weightx = 0; gc.fill = GridBagConstraints.HORIZONTAL;
        content.add(change,gc);
        gc.gridx = 0; gc.gridy = 1;
        content.add(message,gc);
        gc.gridx = 1;
        content.add(find,gc);
        suggestions.setVisibleRowCount(10);
        gc.gridy = 2; gc.gridx = 0; gc.fill = GridBagConstraints.BOTH; gc.weightx = 1; gc.weighty = 1; gc.gridheight = 4;
        content.add(new JScrollPane(suggestions),gc);

        gc.gridheight = 1; gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.anchor = GridBagConstraints.NORTH;
        content.add(createRightPanel(),gc);

        suggestions.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    handleSelectionChange();
                }
            }
        });
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                change();
            }
        });
        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                find();
            }
        });
        learn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                learn();
            }
        });
        ignore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                find();
            }
        });

        setContentPane(content);
        pack();
    }

    /**
     * Sets the current spelling error we are working on.
     * @param spellingError - spelling error
     */
    public void setSpellingError(SpellingError spellingError) {
        spellingErrors = JXSpellChecker.getSpellingErrors().iterator();
        setupNewWord(spellingError);
    }

    /**
     * Sets up a new
     * @param spellingError - spelling error
     */
    private void setupNewWord(SpellingError spellingError) {
        this.spellingError = spellingError;
        misspelledWord.setText(spellingError.getText());
        try {
            JXSpellChecker.getTextComponent().select(spellingError.getStartPosition().getOffset(),spellingError.getEndPosition().getOffset());
            Suggestion spellingSuggestions[] = JXSpellChecker.getSuggestion(spellingError.getText());
            if(spellingSuggestions.length == 0) {
                message.setText("No suggestions.");
            } else {
                message.setText("Word not found in dictionary.");
            }
            suggestions.setListData(spellingSuggestions);
            if(spellingErrors.hasNext()) {
                find.setEnabled(true);
            } else {
                find.setEnabled(false);
                ignore.setEnabled(false);
            }
        } catch (SpellCheckerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the next word instance
     */
    protected void find() {
        SpellingError se = spellingErrors.next();
        setupNewWord(se);
    }

    /**
     * Handles the selection change
     */
    protected void handleSelectionChange() {
        Suggestion sg = (Suggestion)suggestions.getSelectedValue();
        if(sg != null) {
            misspelledWord.setText(sg.getWord());
        }
    }

    /**
     * Changes the word to the value entered
     */
    protected void change() {
        String txt = misspelledWord.getText();
        JXSpellChecker.getTextComponent().replaceSelection(txt);
    }

    /**
     * Closes the dialog
     */
    protected void close() {
        this.setVisible(false);
    }

    /**
     * Adds the current word to the dictionary
     */
    protected void learn() {
        JXSpellChecker.learn(spellingError.getText());
    }

    /**
     * Creates a new right panel.\
     * @return JPanel
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        panel.add(ignore,gc);
        gc.gridy = 1;
        panel.add(learn,gc);
        gc.gridy = 2;
        panel.add(close,gc);
        return panel;
    }
}
