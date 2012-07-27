/*
 * Created on 27.04.2009
 *
 */
package core.combo16.plaf;

import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.DefaultsList;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import core.combo16.JXXComboBox;

public class XXComboBoxAddon extends AbstractComponentAddon {

    /**
     * @param name
     */
    public XXComboBoxAddon() {
        super("JXXComboBox");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXXComboBox.uiClassID, TweakedBasicComboBoxUI.class.getName());
    }

    @Override
    protected void addMetalDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addMetalDefaults(addon, defaults);
        defaults.add(JXXComboBox.uiClassID, TweakedMetalComboBoxUI.class.getName());
    }

    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addWindowsDefaults(addon, defaults);
        defaults.add(JXXComboBox.uiClassID, TweakedWindowsComboBoxUI.class.getName());
    }

    
}
