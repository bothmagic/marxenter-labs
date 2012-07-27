package org.jdesktop.swingx.table;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Bryan Young
 */
public class SimpleMatchingFilter implements InteruptableFilterStrategy {

    String filter = "";
    private AsynchronousFilter asynchronousFilter;

    public SimpleMatchingFilter(final JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent documentEvent) {
                change();
            }

            public void removeUpdate(DocumentEvent documentEvent) {
                change();
            }

            public void changedUpdate(DocumentEvent documentEvent) {
                change();
            }

            private void change() {
                filter = field.getText();
                asynchronousFilter.refresh();
            }
        });
    }


    public void init(AsynchronousFilter asynchronousFilter) {
        this.asynchronousFilter = asynchronousFilter;
    }

    public boolean isIncluded(Object row) {
        return "".equals(filter) || row.toString().toLowerCase().contains(filter.toLowerCase());
    }
}