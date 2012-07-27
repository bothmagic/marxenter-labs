/*
 * $Id: DecoratorDocument.java 149 2004-11-04 20:12:53Z ronaldtm $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.ronaldtm.text.document;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

/**
 * @author Ronald Tetsuo Miura
 */
public class DecoratorDocument implements Document {

    /** */
    private final Document _decoratedDocument;

    /**
     * @param doc -
     */
    protected DecoratorDocument(Document doc) {
        this._decoratedDocument = doc;
    }

    /**
     * @return -
     */
    public int getLength() {
        return this._decoratedDocument.getLength();
    }

    /**
     * @param listener -
     */
    public void addDocumentListener(DocumentListener listener) {
        this._decoratedDocument.addDocumentListener(listener);
    }

    /**
     * @param listener -
     */
    public void removeDocumentListener(DocumentListener listener) {
        this._decoratedDocument.removeDocumentListener(listener);
    }

    /**
     * @param listener -
     */
    public void addUndoableEditListener(UndoableEditListener listener) {
        this._decoratedDocument.addUndoableEditListener(listener);
    }

    /**
     * @param listener -
     */
    public void removeUndoableEditListener(UndoableEditListener listener) {
        this._decoratedDocument.removeUndoableEditListener(listener);
    }

    /**
     * @param key -
     * @return -
     */
    public Object getProperty(Object key) {
        return this._decoratedDocument.getProperty(key);
    }

    /**
     * @param key -
     * @param value -
     */
    public void putProperty(Object key, Object value) {
        this._decoratedDocument.putProperty(key, value);
    }

    /**
     * @param offs -
     * @param len -
     * @throws javax.swing.text.BadLocationException -
     */
    public void remove(int offs, int len) throws BadLocationException {
        this._decoratedDocument.remove(offs, len);
    }

    /**
     * @param offset -
     * @param str -
     * @param a -
     * @throws javax.swing.text.BadLocationException -
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        this._decoratedDocument.insertString(offset, str, a);
    }

    /**
     * @param offset -
     * @param length -
     * @return -
     * @throws javax.swing.text.BadLocationException -
     */
    public String getText(int offset, int length) throws BadLocationException {
        return this._decoratedDocument.getText(offset, length);
    }

    /**
     * @param offset -
     * @param length -
     * @param txt -
     * @throws javax.swing.text.BadLocationException -
     */
    public void getText(int offset, int length, Segment txt) throws BadLocationException {
        this._decoratedDocument.getText(offset, length, txt);
    }

    /**
     * @return -
     */
    public Position getStartPosition() {
        return this._decoratedDocument.getStartPosition();
    }

    /**
     * @return -
     */
    public Position getEndPosition() {
        return this._decoratedDocument.getEndPosition();
    }

    /**
     * @param offs -
     * @return -
     * @throws javax.swing.text.BadLocationException -
     */
    public Position createPosition(int offs) throws BadLocationException {
        return this._decoratedDocument.createPosition(offs);
    }

    /**
     * @return -
     */
    public Element[] getRootElements() {
        return this._decoratedDocument.getRootElements();
    }

    /**
     * @return -
     */
    public Element getDefaultRootElement() {
        return this._decoratedDocument.getDefaultRootElement();
    }

    /**
     * @param r -
     */
    public void render(Runnable r) {
        this._decoratedDocument.render(r);
    }
}