package org.jdesktop.swingx.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class NumericDocument implements Document {

    /**
     * Numeric Content
     */
    private NumericContent numericContent;

    /**
     * Document Listeners
     */
    private List<DocumentListener> documentListeners = new ArrayList<DocumentListener>();

    /**
     * Properties
     */
    private Map<Object,Object> properties = new HashMap<Object, Object>();

    /**
     * Elements
     */
    private Element[] elements;

    /**
     * Creates a new NumericDocument
     */
    public NumericDocument() {
        elements = new Element[1];
        elements[0] = new NumericElement(this);
        numericContent = new NumericContent();
    }

    @Override
    public int getLength() {
        return numericContent.length();
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {
        if(!documentListeners.contains(listener)) {
            documentListeners.add(listener);
        }
    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {
        if(documentListeners.contains(listener)) {
            documentListeners.remove(listener);
        }
    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeUndoableEditListener(UndoableEditListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object getProperty(Object key) {
        return properties.get(key);
    }

    @Override
    public void putProperty(Object key, Object value) {
        properties.put(key,value);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        numericContent.remove(offs,len);
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        numericContent.insertString(offset,str);
    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        return numericContent.getString(offset,length);
    }

    @Override
    public void getText(int offset, int length, Segment txt) throws BadLocationException {
        numericContent.getChars(offset,length,txt);
    }

    @Override
    public Position getStartPosition() {
        Position p;
        try {
            p = createPosition(0);
        } catch (BadLocationException bl) {
            p = null;
        }
        return p;
    }

    @Override
    public Position getEndPosition() {
        return new StickyNumericPosition(this.getLength());
    }

    @Override
    public Position createPosition(int offs) throws BadLocationException {
        return new StickyNumericPosition(offs);
    }

    /**
     * Returns the elements
     * @return elements
     */
    @Override
    public Element[] getRootElements() {
        return elements;
    }

    @Override
    public Element getDefaultRootElement() {
        return elements[0];
    }

    @Override
    public void render(Runnable r) {
        r.run();
    }
}
