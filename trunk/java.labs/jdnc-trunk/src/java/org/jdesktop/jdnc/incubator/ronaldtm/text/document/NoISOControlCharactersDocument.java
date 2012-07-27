/*
 * $Id: NoISOControlCharactersDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Ronald Tetsuo Miura
 */
public class NoISOControlCharactersDocument extends DecoratorDocument {

    /**
     * @param doc -
     */
    protected NoISOControlCharactersDocument(Document doc) {
        super(doc);
    }

    /**
     * @param doc -
     * @return -
     */
    public static Document decorate(Document doc) {
        return new NoISOControlCharactersDocument(doc);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

        StringBuffer sb = new StringBuffer(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isISOControl(c)) {
                sb.append(c);
            }
        }
        String finalValue = str;
        if (sb.length() != str.length()) {
            finalValue = sb.toString();
        }
        super.insertString(offset, finalValue, a);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextArea tf = new JTextArea();
        tf.setDocument(NoISOControlCharactersDocument.decorate(tf.getDocument()));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}