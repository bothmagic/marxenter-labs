/*
 * $Id: DoubleRangeLimitedDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
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
public class DoubleRangeLimitedDocument extends RangeLimitedDocument {

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     */
    protected DoubleRangeLimitedDocument(Document doc, double lowerLimit, double higherLimit) {
        super(doc, Double.class, new Double(lowerLimit), new Double(higherLimit));
    }

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     * @return -
     */
    public static Document decorate(Document doc, double lowerLimit, double higherLimit) {
        return new DoubleRangeLimitedDocument(doc, lowerLimit, higherLimit);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(DoubleRangeLimitedDocument.decorate(tf.getDocument(), -10d, 10d));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}