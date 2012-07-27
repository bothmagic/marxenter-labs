/*
 * $Id: CharacterRangeLimitedDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document.rangelimited;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author Ronald Tetsuo Miura
 */
public class CharacterRangeLimitedDocument extends RangeLimitedDocument {

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     */
    protected CharacterRangeLimitedDocument(Document doc, char lowerLimit, char higherLimit) {
        super(doc, Character.class, new Character(lowerLimit), new Character(higherLimit));
    }

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     * @return -
     */
    public static Document decorate(Document doc, char lowerLimit, char higherLimit) {
        return new CharacterRangeLimitedDocument(doc, lowerLimit, higherLimit);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(CharacterRangeLimitedDocument.decorate(tf.getDocument(), 'A', 'Z'));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}