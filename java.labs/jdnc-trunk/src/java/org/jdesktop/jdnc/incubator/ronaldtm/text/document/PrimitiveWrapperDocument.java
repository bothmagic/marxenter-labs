/*
 * $Id: PrimitiveWrapperDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document;

import java.lang.reflect.Constructor;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * @author Ronald Tetsuo Miura
 */
public class PrimitiveWrapperDocument extends DecoratorDocument {

    /** */
    private Class _wrapperClass;

    /**
     * @param doc -
     * @param wrapperClass
     */
    protected PrimitiveWrapperDocument(Document doc, Class wrapperClass) {
        super(doc);
        setWrapperClass(wrapperClass);
    }

    /**
     * @param doc -
     * @return -
     */
    public static Document decorate(Document doc) {
        return new PrimitiveWrapperDocument(doc, Integer.class);
    }

    /**
     * @param doc -
     * @param wrapperClass -
     * @return -
     */
    public static Document decorate(Document doc, Class wrapperClass) {
        return new PrimitiveWrapperDocument(doc, wrapperClass);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i))) {
                return;
            }
        }
        try {
            Constructor constructor = getWrapperClass().getConstructor(new Class[] { String.class});
            String text = getText(0, getLength());
            String newValue = new StringBuffer(text).insert(offset, str).toString();

            constructor.newInstance(new Object[] { newValue});

        } catch (Exception e) {
            return;
        }
        super.insertString(offset, str, a);
    }

    /**
     * @param wrapperClass O novo valor de wrapperClass.
     */
    public final void setWrapperClass(Class wrapperClass) {
        this._wrapperClass = wrapperClass;
    }

    /**
     * @return Retorna o wrapperClass.
     */
    public final Class getWrapperClass() {
        return this._wrapperClass;
    }

    /**
     * @param args -
     */
    public static void main(String[] args) {
        JTextField tf = new JTextField();
        tf.setDocument(PrimitiveWrapperDocument.decorate(tf.getDocument()));

        JFrame f = new JFrame("Teste");
        f.getContentPane().add(tf);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}