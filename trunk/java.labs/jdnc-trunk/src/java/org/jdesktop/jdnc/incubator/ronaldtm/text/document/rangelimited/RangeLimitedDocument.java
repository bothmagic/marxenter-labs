/*
 * $Id: RangeLimitedDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document.rangelimited;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.jdesktop.jdnc.incubator.ronaldtm.text.document.PrimitiveWrapperDocument;


/**
 * @author Ronald Tetsuo Miura
 */
public class RangeLimitedDocument extends PrimitiveWrapperDocument {

    /** */
    private Comparable _lowerLimit;

    /** */
    private Comparable _higherLimit;

    /**
     * @param doc -
     * @param wrapperClass -
     * @param start -
     * @param end -
     */
    protected RangeLimitedDocument(Document doc, Class wrapperClass, Comparable start,
        Comparable end) {

        super(doc, wrapperClass);
        setLowerLimit(start);
        setHigherLimit(end);
    }

    /**
     * @param doc -
     * @param wrapperClass -
     * @param lowerLimit -
     * @param higherLimit -
     * @return -
     */
    public static Document decorate(Document doc, Class wrapperClass, Comparable lowerLimit,
        Comparable higherLimit) {
        return new RangeLimitedDocument(doc, wrapperClass, lowerLimit, higherLimit);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        String value = new StringBuffer(getText(0, getLength())).insert(offset, str).toString();

        try {
            Constructor constructor = getWrapperClass().getConstructor(new Class[] { String.class});
            Comparable cValue = (Comparable) constructor.newInstance(new String[] { value});

            if ((getLowerLimit().compareTo(cValue) <= 0)
                && (getHigherLimit().compareTo(cValue) >= 0)) {

                super.insertString(offset, str, a);
            }

        } catch (Exception e) {
            //ignore
        }
    }

    /**
     * @param lowerLimit O novo valor de lowerLimit.
     */
    protected void setLowerLimit(Comparable lowerLimit) {
        this._lowerLimit = lowerLimit;
    }

    /**
     * @return Retorna o valor de lowerLimit.
     */
    protected Comparable getLowerLimit() {
        return this._lowerLimit;
    }

    /**
     * @param higherLimit O novo valor de higherLimit.
     */
    protected void setHigherLimit(Comparable higherLimit) {
        this._higherLimit = higherLimit;
    }

    /**
     * @return Retorna o valor de higherLimit.
     */
    protected Comparable getHigherLimit() {
        return this._higherLimit;
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(RangeLimitedDocument.decorate(tf.getDocument(), Integer.class, new Integer(
            -10), new Integer(10)));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}