package org.jdesktop.swingx.experiment;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;

/**
 * A numeric content object.
 */
public class NumericContent implements AbstractDocument.Content {

    private String strValue = "0";

    public NumericContent() {

    }

    @Override
    public Position createPosition(int offset) throws BadLocationException {
        return new StickyNumericPosition(offset);
    }

    @Override
    public int length() {
        return strValue.length();
    }

    @Override
    public UndoableEdit insertString(int where, String str) throws BadLocationException {
        StringBuilder builder = new StringBuilder(strValue);
        builder.insert(where,str);
        strValue = builder.toString();
        return null;
    }

    @Override
    public UndoableEdit remove(int where, int nitems) throws BadLocationException {
        StringBuilder builder = new StringBuilder(strValue);
        builder.delete(where,where+nitems);
        return null;
    }

    @Override
    public String getString(int where, int len) throws BadLocationException {
        return strValue.substring(where,len);
    }

    @Override
    public void getChars(int where, int len, Segment txt) throws BadLocationException {
        char array[] = strValue.toCharArray();
        txt.array = array;
    }
}
