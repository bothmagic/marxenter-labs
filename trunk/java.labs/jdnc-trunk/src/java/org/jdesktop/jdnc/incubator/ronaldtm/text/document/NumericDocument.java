/*
 * $Id: NumericDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Ronald Tetsuo Miura
 */
public class NumericDocument extends DecoratorDocument {

    /**
     * @param doc -
     */
    protected NumericDocument(Document doc) {
        super(doc);
    }

    /**
     * @param doc -
     * @return -
     */
    public static Document decorate(Document doc) {
        return new NumericDocument(doc);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return;
            }
        }
        super.insertString(offset, str, a);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(NumericDocument.decorate(tf.getDocument()));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}