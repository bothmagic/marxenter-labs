/*
 * $Id: TextBinding.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;


/**
 * Class which binds a component that supports editing text values
 * (JTextField, JTextArea, JEditorPane) to a data model field.
 * Although this binding is most commonly used for Swing's text
 * components, it may be used with any component that defines a
 * <code>javax.swing.text.Document</code> to represent its contents.
 * @author Amy Fowler
 * @version 1.0
 */
public class TextBinding extends AbstractBinding {
    private JComponent component;
    private Document document;

    public TextBinding(JTextComponent textComponent,
                            DataModel model, String fieldName) {
        super(textComponent, model, fieldName, AbstractBinding.AUTO_VALIDATE);
        initDocument(textComponent.getDocument());
    }

    public TextBinding(JTextComponent textComponent,
                            DataModel model, String fieldName,
                           int validationPolicy) {
        super(textComponent, model, fieldName, validationPolicy);
        initDocument(textComponent.getDocument());
    }

    public TextBinding(JComponent component, Document document,
                            DataModel dataModel, String fieldName,
                            int validationPolicy) {
        super(component, dataModel, fieldName, validationPolicy);
        initDocument(document);
    }

    public JComponent getComponent() {
        return component;
    }

    protected void setComponent(JComponent component) {
        this.component = component;
    }

    protected Object getComponentValue() {
        String txt;
        try {
            txt = document.getText(0, document.getLength());
        }
        catch (BadLocationException e) {
            txt = null;
        }
        return txt;
    }

    protected void setComponentValue(Object value) {
        try {
            document.remove(0, document.getLength());
            document.insertString(0, convertFromModelType(value), null);
        }
        catch (BadLocationException e) {
            UIManager.getLookAndFeel().provideErrorFeedback(component);
        }

    }

    private void initDocument(Document document) {
        this.document = document;
        document.addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                maybeModified();
            }

            public void insertUpdate(DocumentEvent e) {
                maybeModified();
            }

            public void removeUpdate(DocumentEvent e) {
                maybeModified();
            }

            public void maybeModified() {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
        //TODO This is kind of test code...
        component.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			public void focusLost(FocusEvent e) {
				//auto-commit the changes to the DataModel
				if (isModified()) {
					push();
				}
			}
		});
    }
}