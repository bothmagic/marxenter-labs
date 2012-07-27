package core.combo16.plaf;

import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ComboBoxEditor;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.Document;

import core.combo16.XComboBoxEditor;

/**
 * Quick test for a ComboBoxEditor with better commit semantics.
 */
public class BasicXComboBoxEditor implements XComboBoxEditor {
    EditorTextField editor;

    KeyEventDispatcher k;
    public BasicXComboBoxEditor() {
        editor = createEditorComponent();
        editor.addActionListener(createEditorListener());
    }
    

    /**
     * Overridden to guarantee returning a JFormattedTextField as editingComponent.
     */
    @Override
    public EditorTextField getEditorComponent() {
        return editor;
    }


    @Override
    public void setItem(Object value) {
        getEditorComponent().setValue(value);
    }
    
    
    @Override
    public Object getItem() {
        return getEditorComponent().getTextAsValue();
    }

    
    protected EditorTextField createEditorComponent() {
        EditorTextField field = new EditorTextField();
        field.setBorder(null);
        return field;
    }

    @Override
    public void selectAll() {
        getEditorComponent().selectAll();
        getEditorComponent().requestFocusInWindow();
    }

    
    public static class EditorTextField extends JFormattedTextField {
        
        private boolean edited;
        private DocumentHandler documentListener;

        public EditorTextField() {
            super(new EditorFormatter());
        }
        
        public Object getTextAsValue() {
            if (!isEdited()) {
                return getValue();
            }
            String text = getText();
            // else try to parse on-the-fly
            try {
                return getFormatter().stringToValue(text);
            } catch (ParseException e) {
                // unparseable - can't do anything
            }
            return text;
        }
        
        /**
         * Duplicate super edited logic.
         * @return
         */
        public boolean isEdited() {
            return edited;
        }
        
        @Override
        protected void setFormatter(AbstractFormatter format) {
            super.setFormatter(format);
            setEdited(false);
        }

        @Override
        public void commitEdit() throws ParseException {
            super.commitEdit();
            setEdited(false);
        }

        /**
         * Duplicate super because want keep track of dirtyness.
         */
        @Override
        public void setDocument(Document doc) {
            if (documentListener != null && getDocument() != null) {
                getDocument().removeDocumentListener(documentListener);
            }
            super.setDocument(doc);
            if (documentListener == null) {
                documentListener = new DocumentHandler();
            }
            doc.addDocumentListener(documentListener);
        }
        
        private void setEdited(boolean edited) {
            this.edited = edited;
        }
        
        /**
         * Sets the dirty state as the document changes.
         */
        private class DocumentHandler implements DocumentListener, Serializable {
            private boolean ignore;
            public void insertUpdate(DocumentEvent e) {
                if (ignore) return;
                setEdited(true);
            }
            public void setIgnore(boolean b) {
                this.ignore = b;
                
            }
            public void removeUpdate(DocumentEvent e) {
                if (ignore) return;
                setEdited(true);
            }
            public void changedUpdate(DocumentEvent e) {}
        }
    }

    public static class EditorFormatter extends DefaultFormatter {

        public EditorFormatter() {
            super();
            setOverwriteMode(false);
        }
        
        @Override
        public void install(JFormattedTextField ftf) {
            Object value = ftf.getValue();
            setValueClass(value != null ? value.getClass() : String.class);

            super.install(ftf);
            positionCursorAtInitialPosition();
        }

        private void positionCursorAtInitialPosition() {
            getFormattedTextField().setCaretPosition(getEndPosition());
        }

        private int getEndPosition() {
            return getFormattedTextField().getDocument().getLength();
        }
        
    }

//---------------- listener management
    ActionListener[] emptyListeners = new ActionListener[0];
    List<ActionListener> actionListeners;
    @Override
    public void addActionListener(ActionListener l) {
        if (actionListeners == null) {
            actionListeners = new ArrayList<ActionListener>();
        }
        actionListeners.add(l);
    }
    @Override
    public void removeActionListener(ActionListener l) {
        if (!hasListener()) return;
        actionListeners.remove(l);
    }
    
    @Override
    public ActionListener[] getActionListeners() {
        if (!hasListener()) return emptyListeners;
        return (ActionListener[]) actionListeners.toArray(new ActionListener[0]);
    }
    
    private ActionListener createEditorListener() {
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fireActionEvent(e);
                
            }};
        return l;
    }
    
    protected void fireActionEvent(ActionEvent e) {
        if (!hasListener()) return;
        ActionEvent remapped = remappedActionEvent(e);
        for (ActionListener l : actionListeners) {
            l.actionPerformed(remapped);
        }
        
    }

    private boolean hasListener() {
        return actionListeners != null && actionListeners.size() > 0;
    }


    protected ActionEvent remappedActionEvent(ActionEvent e) {
        if (e != null) {
            return new ActionEvent(this, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers());
        }
        return new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
    }



}