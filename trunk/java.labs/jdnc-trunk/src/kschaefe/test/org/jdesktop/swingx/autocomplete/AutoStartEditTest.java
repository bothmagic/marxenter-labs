/**
 * 
 */
package org.jdesktop.swingx.autocomplete;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

public class AutoStartEditTest extends JFrame {
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JTable table = new JTable(2, 10);
        table.getColumnModel().getColumn(0).setCellEditor(createExampleEditor());
        
        table.setSurrendersFocusOnKeystroke(true);
        
        add(new JScrollPane(table));
        
        pack();
    }

    private TableCellEditor createExampleEditor() {
        JComboBox combo = new JComboBox(new Object[] {
                "apple",
                "orange",
                "kiwi",
                "pineapple",
                "banana",
        }) {
            class CellEditingFocusHandler extends FocusAdapter {

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void focusGained(FocusEvent e) {
                    Component c = editor.getEditorComponent();
                    
                    if (c instanceof JTextComponent) {
                        ((JTextComponent) c).setText("o");
                    }
                }

                /**
                 * {@inheritDoc}
                 */
                @Override
                public void focusLost(FocusEvent e) {
                    // TODO Auto-generated method stub
                    super.focusLost(e);
                }
                
            }
            
            protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                    int condition, boolean pressed) {
                boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
                
                if (!retValue && !pressed && isStartingCellEdit() && editor != null) {
                    // this is where the magic happens
                    
                    retValue = selectWithKeyChar(e.getKeyChar());
                    System.out.println(".processKeyBinding()");
                    System.out.println(pressed);
                    System.out.println(editor.getItem());
                    
//                    Component c = editor.getEditorComponent();
//                    
//                    if (c instanceof JTextComponent) {
//                        ((JTextComponent) c).setCaretPosition(1);
//                    }
                    
//                    System.out.println(isFocusOwner());
//                    KeyEvent event = new KeyEvent(editor.getEditorComponent(), e.getID(), e
//                            .getWhen(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e
//                            .getKeyLocation());
//                    SwingUtilities.processKeyBindings(event);
                }
                
                return retValue;
            }
            
            private boolean isStartingCellEdit() {
                JTable table = (JTable) SwingUtilities.getAncestorOfClass(
                        JTable.class, this);

                return table != null
                        && table.isFocusOwner()
                        && !Boolean.FALSE.equals((Boolean) table
                                .getClientProperty("JTable.autoStartsEdit"));
            }
        };
        AutoCompleteDecorator.decorate(combo);
        
        return new ComboBoxCellEditor(combo);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("error setting l &f " + e);
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AutoStartEditTest().setVisible(true);
            }
        });
    }
}
