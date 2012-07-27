/*
 * $Id: LimitedLengthDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Ronald Tetsuo Miura
 */
public final class LimitedLengthDocument extends DecoratorDocument {

    /** */
    private int _maxinumLength;

    /** */
    private boolean _insertPartial;

    /**
     * @param doc -
     * @param maxinumLength -
     * @param insertPartial -
     */
    protected LimitedLengthDocument(Document doc, int maxinumLength, boolean insertPartial) {
        super(doc);
        this._maxinumLength = maxinumLength;
        setInsertPartial(insertPartial);
    }

    /**
     * @param doc -
     * @param maxinumLength -
     * @return -
     */
    public static LimitedLengthDocument decorate(Document doc, int maxinumLength) {

        return decorate(doc, maxinumLength, true);
    }

    /**
     * @param doc -
     * @param maxinumLength -
     * @param insertPartial -
     * @return -
     */
    public static LimitedLengthDocument decorate(Document doc, int maxinumLength,
        boolean insertPartial) {

        return new LimitedLengthDocument(doc, maxinumLength, insertPartial);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        if (getInsertPartial()) {
            int maxAddition = this._maxinumLength - getLength();
            super.insertString(offset, str.substring(0, Math.min(maxAddition, str.length())), a);
        } else {
            int newLength = getLength() + str.length();
            if (this._maxinumLength - newLength >= 0) {
                super.insertString(offset, str, a);
            }
        }
    }

    /**
     * @param insertPartial O novo valor de insertPartial.
     */
    public final void setInsertPartial(boolean insertPartial) {
        this._insertPartial = insertPartial;
    }

    /**
     * @return Retorna o insertPartial.
     */
    public final boolean getInsertPartial() {
        return this._insertPartial;
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf1 = new JTextField(20);
        tf1.setDocument(LimitedLengthDocument.decorate(tf1.getDocument(), 10, true));

        JTextField tf2 = new JTextField(20);
        tf2.setDocument(LimitedLengthDocument.decorate(tf2.getDocument(), 10, false));

        JFrame f = new JFrame("Teste");
        f.getContentPane().setLayout(new GridLayout(2, 1));
        f.getContentPane().add(tf1);
        f.getContentPane().add(tf2);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}