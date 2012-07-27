/*
 * Created on 17.03.2009
 *
 */
package core.combo16;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.ComboBoxUI;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import core.combo16.plaf.XXComboBoxAddon;

/**
 * Experiment with separation of notification concerns. Fires actionEvents only after
 * user changes, not after programmatic selection changes. Probably needs the help of 
 * the ui delegate to always fire. For now I'm interested in editing only.
 * 
 */
public class JXXComboBox extends JComboBox {

    /**
     * @see #getUIClassID
     * @see #readObject
     */
    public static final String uiClassID = "XXComboBoxUI";
    
    static {
        LookAndFeelAddons.contribute(new XXComboBoxAddon());
    }
    
    /**
     * Overridden to differentiate commits requests by ui-delegate from editor
     * notifications, the former should pass-in a null event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e == null) {
            hidePopup();
            fireActionEvent();
        } else {
            super.actionPerformed(e);
        }
    }

    /**
      * Overridden to not trying to be clever on setting the selected item, simply
      * delegate to dataModel. Combined with contentsChanged no longer firing 
      * action events, this requires deeper changes in the ui-delegate.
      * 
      */
    @Override
    public void setSelectedItem(Object anObject) {
        dataModel.setSelectedItem(anObject);
        // @KEEP: fire action events for testing/debugging only!
//        fireActionEvent();
    }

    /**
     * Overridden to not fire action events. We come here from the data model, not
     * from user interaction.
     */
    @Override
    public void contentsChanged(ListDataEvent e) {
        Object oldSelection = selectedItemReminder;
        Object newSelection = dataModel.getSelectedItem();
        if (oldSelection == null || !oldSelection.equals(newSelection)) {
            selectedItemChanged();
        }
    }

    
    
    @Override
    public void setModel(ComboBoxModel model) {
        Object oldSelection = selectedItemReminder;
        super.setModel(model);
        if (oldSelection == null || !oldSelection.equals(selectedItemReminder)) {
            selectedItemChanged();
        }
    }

    public JXXComboBox() {
        super();
    }

    public JXXComboBox(ComboBoxModel model) {
        super(model);
    }

    public JXXComboBox(Object[] items) {
        super(items);
    }

    public JXXComboBox(Vector<?> items) {
        super(items);
    }

    
    /**
     * Returns a string that specifies the name of the L&F class that renders
     * this component.
     * 
     * @return "XComboBoxUI"
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     * @beaninfo expert: true description: A string that specifies the name of
     *           the L&F class.
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateUI() {
        setUI((ComboBoxUI) LookAndFeelAddons.getUI(this, ComboBoxUI.class));
    }

}
