/**
 * 
 */
package org.jdesktop.swingx;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.plaf.MenuItemUI;

import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.plaf.JXScrollMenuItemAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 *
 */
public class JXScrollMenuItem extends JMenuItem {
    public static final String uiClassID = "ScrollMenuItemUI";
    
    static {
        LookAndFeelAddons.contribute(new JXScrollMenuItemAddon());
    }
    
    public JXScrollMenuItem() {
        this(new EmptyIcon());
    }
    
    public JXScrollMenuItem(Icon icon) {
        super(icon);
        
        updateUI();
    }
    
    /**
     * Resets the UI property with a value from the current look and feel.
     *
     * @see JComponent#updateUI
     */
    @Override
    public void updateUI() {
        setUI((MenuItemUI) LookAndFeelAddons.getUI(this, MenuItemUI.class));
    }


    /**
     * Returns the suffix used to construct the name of the L&F class used to
     * render this component.
     *
     * @return the string "ScrollMenuItemUI"
     * @see JComponent#getUIClassID
     * @see UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

}
