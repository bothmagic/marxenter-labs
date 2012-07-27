/*
 * Created on 21.06.2008
 *
 */
package org.jdesktop.swingx.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.Document;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTable.GenericEditor;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.AndHighlightPredicate;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.util.WindowUtils;

public class TableSelectAll {

    /**
     * TextField specialized as editingComponent which tries its best
     * to start with content selected. 
     * 
     * The implication is that 
     * the text is replaced by the first keystroke. This might be 
     * a usability issue if the table's autoStartEditOnKeyStroke is 
     * true (default): users might be surprised that the content is 
     * deleted without warning. Might smoothened by a visual clue that the 
     * focused cell is editable.<p>
     * 
     * Supports mode which appends the 
     * very first char received from the table. This will make it 
     * backward compatible.
     * 
     * 
     */
    public static class EditorTextField extends JTextField {

        private boolean appendFirstKey;
        private boolean firstHandled;

        /**
         * {@inheritDoc}
         * Overridden to select all.
         * 
         */
        @Override
        public void addNotify() {
            super.addNotify();
            selectAll();
            firstHandled = false;
        }

        /**
         * Sets a flag indicating whether the first key passed-in from the 
         * table should be appended. This is for backward compatibility, as
         * it behaves in the same manner as the default editors. <p>
         * 
         * The default value of this flag is false.
         * 
         * @param appendFirst
         */
        public void setAppendFirstKey(boolean appendFirst) {
            this.appendFirstKey = appendFirst;
        }

        /**
         * {@inheritDoc} <p>
         * 
         * Overridden to handle backward compatible append key.
         */
        @Override
        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                int condition, boolean pressed) {
            checkSelection(e);
            return super.processKeyBinding(ks, e, condition, pressed);
        }

        /**
         * Clears the selection if necessary for backward compatible append
         * first key mode. 
         * 
         * @param e the event received in processKeyBinding
         * 
         * @see #processKeyBinding(KeyStroke, KeyEvent, int, boolean)
         */
        private void checkSelection(KeyEvent e) {
            if (!appendFirstKey || firstHandled) return;
            firstHandled = true;
            if ((e == null) || (e.getSource() != this)) {
                clearSelection();
            }
        }

        /**
         * Clears the selection and moves the caret to the end of the document.
         */
        private void clearSelection() {
            Document doc = getDocument();
            select(doc.getLength(), doc.getLength());
            
        }
       
        
    }
    
    /**
     * CellEditor specialized on selecting content after starting
     * an edit.
     */
    public static class CellEditor extends GenericEditor {
        
        public CellEditor() {
            this(false);
        }

        public CellEditor(boolean appendFirstKey) {
            super(new EditorTextField());
            getComponent().setAppendFirstKey(appendFirstKey);
        }

        /**
         * Overridden to select all before calling super. This
         * is for selecting the text after starting edits with
         * the mouse. <p>
         * 
         * NOTE: ui-delegates are not guaranteed to call this after
         * starting the edit triggered with the mouse. On the other
         * hand, not all are expected to dispatch the starting mouse
         * event to the editing component (which is the event which
         * destroys the selection done in addNotify).
         */
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            getComponent().selectAll();
            return super.shouldSelectCell(anEvent);
        }
        
        @Override
        public EditorTextField getComponent() {
            return (EditorTextField) super.getComponent();
        }
    }
    
    private void configureTable(JXTable table) {
        table.setDefaultEditor(Object.class, new CellEditor(false));
//        table.setSurrendersFocusOnKeystroke(true);
        // install visual clue if cell is editable and has focus
        // alerts users that key input will replace the content
        HighlightPredicate pred = new AndHighlightPredicate(
                HighlightPredicate.EDITABLE,
                HighlightPredicate.HAS_FOCUS
                );
        Color background = ColorUtil.setAlpha(table.getSelectionBackground(), 127);
        table.addHighlighter(new ColorHighlighter(pred, background, null, background, null));
    }
    
    private JComponent getContent() {
        // use model of your choice - I'm too lazy ;-)
        JXTable table = new JXTable(new AncientSwingTeam());
        configureTable(table);
        JComponent content = new JXPanel(new BorderLayout());
        content.add(new JScrollPane(table));
        return content;
    }
    
    public static void main(String[] args) {
        setUpLookAndFeel();
        final JXFrame frame = new JXFrame(
                "JXTable :: Advanced Customization",
                true);
        frame.add(new TableSelectAll().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setSize(800, 600);
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }
        });        
    }


    private static void setUpLookAndFeel() {
        try {
//            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }

}
