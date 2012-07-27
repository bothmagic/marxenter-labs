package core.combo16.plaf;

import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.ComboBoxEditor;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

import org.jdesktop.swingx.plaf.basic.BasicMonthViewVisualCheck.MyMonthViewUI;

import core.combo16.XComboBoxEditor;

/**
 * UI delegate with custom editor and access to super's popup.
 */
public class TweakedMetalComboBoxUI extends BasicComboBoxUI implements ComboPopupProvider {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(TweakedMetalComboBoxUI.class.getName());

    @SuppressWarnings({"UnusedDeclaration"})
    public static ComponentUI createUI(JComponent c) {
        return new TweakedMetalComboBoxUI();
    }

    /**
     * Implement ComboPopupProvider.
     */
    public ComboPopup getComboPopup() {
        return popup;
    }

    /**
     * Overridden to remove the Handler as ActionListener.
     */
    @Override
    protected void configureEditor() {
        super.configureEditor();
        for (ActionListener l : ((JTextField) editor).getActionListeners()) {
            if (l.getClass().getName().contains("Handler")) {
                ((JTextField) editor).removeActionListener(l);
            }
        }
        for (ActionListener l : ((XComboBoxEditor) comboBox.getEditor())
                .getActionListeners()) {
            if (l.getClass().getName().contains("Handler")) {
                comboBox.getEditor().removeActionListener(l);
            }
        }
    }
    
    

    @Override
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        comboBox.getActionMap().put("enterPressed", new ComboBoxEnterAction());
    }

    /**
     * Overridden to create custom editor.
     */
    @Override
    protected ComboBoxEditor createEditor() {
        // return new QuickOverrideComboBoxEditor();
        return new BasicXComboBoxEditor();
    }

    @Override
    protected ComboPopup createPopup() {
        return new BasicXComboPopup(comboBox);
    }

}