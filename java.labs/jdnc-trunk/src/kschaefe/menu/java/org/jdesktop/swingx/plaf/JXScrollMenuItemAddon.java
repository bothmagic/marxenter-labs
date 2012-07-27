/**
 * 
 */
package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXScrollMenuItem;
import org.jdesktop.swingx.plaf.basic.BasicScrollMenuItemUI;

/**
 *
 */
public class JXScrollMenuItemAddon extends AbstractComponentAddon {
    public JXScrollMenuItemAddon() {
        super("JXScrollMenuItem");
    }

    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon, DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        
        defaults.add(JXScrollMenuItem.uiClassID, BasicScrollMenuItemUI.class.getName());
    }
}
