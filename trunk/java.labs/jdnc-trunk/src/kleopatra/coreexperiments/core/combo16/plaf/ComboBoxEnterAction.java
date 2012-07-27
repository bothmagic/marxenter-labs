/*
 * Created on 17.03.2009
 *
 */
package core.combo16.plaf;

import java.awt.event.ActionEvent;

import javax.swing.JComboBox;

import sun.swing.UIAction;
/**
 * Experiment with enter action for combo. As is, it is suitable for ComboBoxEditors
 * which fire only if the selected value had been edited. Still relies on combo firing action
 * events in setSelectedItem.
 */
public class ComboBoxEnterAction extends UIAction {

    public ComboBoxEnterAction() {
        super("enterPressed");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        // back out ... 
        if (!(box.getUI() instanceof ComboPopupProvider)) return;
        ComboPopupProvider ui = (ComboPopupProvider) box.getUI();
        if (isEditingComponent(box)) {
            // we reach here if enter is pressed while editing as a CellEditor
            // Forces the selection of the list item if the
            // combo box is in a JTable.
            int selectedIndex = ui.getComboPopup().getList()
                    .getSelectedIndex();
            // list item selected
            if (selectedIndex >= 0) {
                box.setSelectedIndex(selectedIndex);
            } else {
                // editable combo with nothing changed, need to force an actionEvent
            } 
            box.actionPerformed(null);
        } else {
            box.actionPerformed(null);
//             c&p from 1.6 - 
//            if (box.isPopupVisible()) {
//                // Forces the selection of the list item
//                boolean isEnterSelectablePopup = UIManager
//                        .getBoolean("ComboBox.isEnterSelectablePopup");
//                if (!box.isEditable() || isEnterSelectablePopup
//                        || isEditingComponent(box)) {
//                    Object listItem = ui.getComboPopup().getList()
//                            .getSelectedValue();
//                    if (listItem != null) {
//                        box.getModel().setSelectedItem(listItem);
//                        // Ensure that JComboBox.actionPerformed()
//                        // doesn't set editor value as selected item
//                        box.getEditor().setItem(listItem);
//                    }
//                }
//                box.setPopupVisible(false);
//            }
            // Note: do not pass assumed enter to rootpane
        }
    }

    private boolean isEditingComponent(JComboBox box) {
        Object tableMarker = box.getClientProperty("JComboBox.isTableCellEditor");
        return Boolean.TRUE.equals(tableMarker);
    }

    @Override
    public boolean isEnabled(Object sender) {
        if (!(sender instanceof JComboBox)) {
            return super.isEnabled(sender);
        }
        return isEditingComponent((JComboBox) sender) || ((JComboBox) sender).isPopupVisible();
    }
    
    
}