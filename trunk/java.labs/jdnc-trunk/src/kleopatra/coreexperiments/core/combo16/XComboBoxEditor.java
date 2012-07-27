/*
 * Created on 17.03.2009
 *
 */
package core.combo16;

import java.awt.event.ActionListener;

import javax.swing.ComboBoxEditor;

/**
 * Quick hack - allow access to registered listeners.
 */
public interface XComboBoxEditor extends ComboBoxEditor {

    public ActionListener[] getActionListeners();
}
