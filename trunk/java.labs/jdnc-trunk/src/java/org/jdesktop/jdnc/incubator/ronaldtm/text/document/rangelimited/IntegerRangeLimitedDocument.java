/*
 * $Id: IntegerRangeLimitedDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
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
public class IntegerRangeLimitedDocument extends RangeLimitedDocument {

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     */
    protected IntegerRangeLimitedDocument(Document doc, int lowerLimit, int higherLimit) {
        super(doc, Integer.class, new Integer(lowerLimit), new Integer(higherLimit));
    }

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     * @return -
     */
    public static Document decorate(Document doc, int lowerLimit, int higherLimit) {
        return new IntegerRangeLimitedDocument(doc, lowerLimit, higherLimit);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(IntegerRangeLimitedDocument.decorate(tf.getDocument(), -10, 10));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}