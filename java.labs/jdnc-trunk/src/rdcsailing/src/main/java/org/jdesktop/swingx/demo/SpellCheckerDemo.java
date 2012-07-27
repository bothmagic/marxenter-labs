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
package org.jdesktop.swingx.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdesktop.swingx.JXSpellChecker;
import org.jdesktop.swingx.spelling.SpellCheckerException;

/**
 * Demonstrates a basic spell check.
 * @author Ryan Cuprak
 */
public class SpellCheckerDemo extends JFrame {

    /**
     * Logger for the demo
     */
    private static final Logger logger = Logger.getLogger("SpellDemo");

    /**
     * TextArea
     */
    private JTextArea textArea;

    /**
     * Creates a new SpellCheckerDemo
     * @throws Exception - thrown if there is an unsupported locale.
     */
    public SpellCheckerDemo() throws Exception {
        super("Spellchecker Demo");
        textArea = new JTextArea(20,80);
        getContentPane().add(new JScrollPane(textArea));
        load();
        try {
            JXSpellChecker sc = new JXSpellChecker(textArea);
            sc.check();
        } catch (SpellCheckerException e) {
            logger.log(Level.SEVERE, "Unable to load the spellchecker", e);
        }
        pack();
        setVisible(true);
    }

    /**
     * Loads the text into the text area
     */
    private void load() {
        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(SpellCheckerDemo.class.getResourceAsStream("/org/jdesktop/swingx/demo/JuliusCaesar.txt"));
            BufferedReader br = new BufferedReader(isr);
            String str;
            while((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            str = sb.toString();
            textArea.setText(str);
        } catch (Throwable t) {
            logger.log(Level.SEVERE,"Unable to load the text.",t);
        }
    }

    public static void main(String args[]) throws Exception {
        new SpellCheckerDemo();
    }
}
