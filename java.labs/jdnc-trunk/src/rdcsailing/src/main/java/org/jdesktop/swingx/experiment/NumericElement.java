package org.jdesktop.swingx.experiment;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class NumericElement implements Element {

    protected Document document;

    public NumericElement(Document document) {
        this.document = document;
    }

    @Override
    public Document getDocument() {
        return document;
    }

    @Override
    public Element getParentElement() {
        return null;
    }

    @Override
    public String getName() {
        return AbstractDocument.ContentElementName;
    }

    @Override
    public AttributeSet getAttributes() {
        return null;
    }

    @Override
    public int getStartOffset() {
        return 0;
    }

    @Override
    public int getEndOffset() {
        return this.getDocument().getLength();
    }

    @Override
    public int getElementIndex(int offset) {
        return -1; // Return -1 if we are a leaf
    }

    @Override
    public int getElementCount() {
        return 0;   // Return 0 we are a leaf
    }

    @Override
    public Element getElement(int index) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isLeaf() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
