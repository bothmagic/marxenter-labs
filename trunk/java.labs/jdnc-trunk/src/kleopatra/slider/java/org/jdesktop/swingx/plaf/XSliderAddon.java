/*
 * Created on 22.03.2011
 *
 */
package org.jdesktop.swingx.plaf;

import org.jdesktop.swingx.JXSlider;

public class XSliderAddon extends AbstractComponentAddon {

    public XSliderAddon() {
        super("JXSlider");
    }

    /** 
     * @inherited <p>
     */
    @Override
    protected void addBasicDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addBasicDefaults(addon, defaults);
        defaults.add(JXSlider.uiClassID,
            "org.jdesktop.swingx.plaf.basic.BasicXSliderUI");
    }

    /** 
     * @inherited <p>
     */
    @Override
    protected void addWindowsDefaults(LookAndFeelAddons addon,
            DefaultsList defaults) {
        super.addWindowsDefaults(addon, defaults);
        defaults.add(JXSlider.uiClassID,
            "org.jdesktop.swingx.plaf.windows.WindowsXSliderUI");
        
    }


    
}
