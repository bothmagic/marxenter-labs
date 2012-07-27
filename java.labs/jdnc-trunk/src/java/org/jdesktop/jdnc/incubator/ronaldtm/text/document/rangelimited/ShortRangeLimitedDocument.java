/*
 * $Id: ShortRangeLimitedDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
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
public class ShortRangeLimitedDocument extends RangeLimitedDocument {

    /**
     * @param doc -
     * @param start -
     * @param end -
     */
    protected ShortRangeLimitedDocument(Document doc, int start, int end) {
        super(doc, Integer.class, new Integer(start), new Integer(end));
    }

    /**
     * @param doc -
     * @param lowerLimit -
     * @param higherLimit -
     * @return -
     */
    public static Document decorate(Document doc, int lowerLimit, int higherLimit) {
        return new ShortRangeLimitedDocument(doc, lowerLimit, higherLimit);
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(ShortRangeLimitedDocument.decorate(tf.getDocument(), -10, 10));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}