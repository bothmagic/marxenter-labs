/*
 * Created on 13.03.2009
 *
 */
package core.combo16.plaf;

import java.awt.event.ActionEvent;
import java.text.ParseException;

import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.test.ActionReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import core.combo16.plaf.BasicXComboBoxEditor.EditorFormatter;

@RunWith(JUnit4.class)
public class TestBasicXComboBoxEditor extends InteractiveTestCase {

    private BasicXComboBoxEditor editor;

    @Test
    public void testActionListenerSource() {
        ActionReport report = new ActionReport();
        editor.addActionListener(report);
        ActionEvent event = new ActionEvent(report, ActionEvent.ACTION_PERFORMED,  "something");
        editor.fireActionEvent(event);
        assertEquals(editor, report.getLastEvent().getSource());
    }
    
    /**
     * brute force testing of actionListener notication (manually trigger firing)
     */
    @Test
    public void testActionListenerFire() throws ParseException {
        ActionReport report = new ActionReport();
        editor.addActionListener(report);
        editor.fireActionEvent(null);
        assertEquals(1, report.getEventCount());
        report.clear();
        editor.removeActionListener(report);
        editor.fireActionEvent(null);
        assertEquals(0, report.getEventCount());
    }
    
    @Test
    public void testActionListenerRemove() {
        ActionReport report = new ActionReport();
        editor.removeActionListener(report);
    }

    /**
     * Test actionListener notification.
     * 
     * Not possible by default: TextActions only fire if textComponent is currently focused
     * 
     * @throws ParseException
     */
    @Test
    public void testActionListener() throws ParseException {
        editor.setItem(new TestPerson("mine") );
        editor.getEditorComponent().setText("whatever");
        ActionReport report = new ActionReport();
        editor.addActionListener(report);
        Action action = editor.getEditorComponent().getActionMap().get("notify-field-accept");
        action.actionPerformed(null);
        fail("cannot test notificationAction: TextActions fire only if comp focused");
        assertEquals(1, report.getEventCount());
    }
    
    
    @Test
    public void testEditorFormatter() {
        JFormattedTextField comp = editor.getEditorComponent();
        AbstractFormatter formatter = comp.getFormatter();
        assertTrue("expected EditorFormatter but was" + formatter, 
                comp.getFormatter() instanceof EditorFormatter);
        assertEquals(false, ((DefaultFormatter) formatter).getOverwriteMode());
    }
    
    @Test
    public void testEditorCaret() {
        editor.setItem("somevalue");
        int textLength = editor.getEditorComponent().getDocument().getLength();
        assertEquals(textLength, editor.getEditorComponent().getCaretPosition());
    }

    /**
     * sanity: debug setValue in formatted text field.
     */
    @Test
    public void testDebug() {
        TestPerson person = new TestPerson("person");
        TestPerson other = new TestPerson("other");
        editor.setItem(person);
        editor.setItem(other);
    }
    
    @Test
    public void testEditedOnDocumentEvent() {
        editor.getEditorComponent().setText("other");
        assertEquals("editor comp must be edited", true, editor.getEditorComponent().isEdited());
    }

    @Test
    public void testEditedOnValue() {
        editor.getEditorComponent().setText("other");
        editor.setItem(new TestPerson("person"));
        assertEquals("editor comp must be edited", false, editor.getEditorComponent().isEdited());
    }

    @Test
    public void testEditedOnCommit() throws ParseException {
        editor.getEditorComponent().setText("other");
        editor.getEditorComponent().commitEdit();
        assertEquals("editor comp must be edited", false, editor.getEditorComponent().isEdited());
    }
    /**
     * Test item creation/retention with custom types
     */
    @Test
    public void testItemCreationCustom() {
        TestPerson person = new TestPerson("person");
        editor.setItem(person);
        assertSame(person, editor.getItem());
        String name = "person1";
        editor.getEditorComponent().setText(name);
        Object item = editor.getItem();
        assertTrue("expected TestPerson, was " + item.getClass(), item instanceof TestPerson);
        assertEquals(new TestPerson(name), item);
    }

    /**
     * Test item creation/retention with base types
     */
    @Test
    public void testItemCreationBase() {
        Integer person = new Integer(3);
        editor.setItem(person);
        assertSame(person, editor.getItem());
        String name = "4";
        editor.getEditorComponent().setText(name);
        Object item = editor.getItem();
        assertTrue("expected Integer, was " + item.getClass(), item instanceof Integer);
        assertEquals(new Integer(name), item);
    }
    
    /**
     * Sanity: how does core create a new object?
     * Does so only for types which are known to String.valueOf(...)
     */
    @Test
    public void testCoreItemCreationCustom() {
        BasicComboBoxEditor core = new BasicComboBoxEditor();
        TestPerson person = new TestPerson("person");
        core.setItem(person);
        assertSame(person, core.getItem());
        ((JTextComponent) core.getEditorComponent()).setText("person1");
        Object item = core.getItem();
        assertTrue("expected TestPerson, was " + item.getClass(), item instanceof TestPerson);
    }
    
    /**
     * Sanity: how does core create a new object?
     * Does so only for types which are known to String.valueOf(...)
     */
    @Test
    public void testCoreItemCreationBase() {
        BasicComboBoxEditor core = new BasicComboBoxEditor();
        Integer person = new Integer(3);
        core.setItem(person);
        assertSame(person, core.getItem());
        ((JTextComponent) core.getEditorComponent()).setText("4");
        Object item = core.getItem();
        assertTrue("expected TestPerson, was " + item.getClass(), item instanceof Integer);
    }
    
    private ComboBoxModel createTestPersons(int count) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (int i = 0; i < count; i++) {
            m.addElement(new TestPerson("person" + i));
        }
        return m;
    }
    
    public static class TestPerson {
        String name;
        public TestPerson(String name) {
            this.name = name;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestPerson)) return false;
            return this.name.equals(((TestPerson)obj).name);
        }
        @Override
        public String toString() {
            return name;
        }
        
        
    }
    @Before
    @Override
    public void setUp() throws Exception {
        editor = new BasicXComboBoxEditor();
    }
    
    
}
