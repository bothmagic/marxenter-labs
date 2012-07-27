/*
 * Created on 22.03.2011
 *
 */
package org.jdesktop.swingx;

import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.XSliderAddon;

/**
 * Extension of JSlider with support for range selection.
 * 
 * @author Jeanette Winzenburg, Berlin
 */
public class JXSlider extends JSlider {

    /**
     * UI Class ID
     */
    public final static String uiClassID = "XSliderUI";
    
    /**
     * Registers a Addon for JXSlider.
     */
    static {
        LookAndFeelAddons.contribute(new XSliderAddon());
    }

    private boolean rangeEnabled;

    public void setRangeEnabled(boolean enabled) {
        boolean old = isRangeEnabled();
        this.rangeEnabled = enabled;
        firePropertyChange("rangeEnabled", old, isRangeEnabled());
    }
    
    public boolean isRangeEnabled() {
        return rangeEnabled;
    }
    
//-------------------- extended ui    

    
    /** 
     * @inherited <p>
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    /** 
     * @inherited <p>
     */
    @Override
    public void updateUI() {
        setUI((SliderUI) LookAndFeelAddons.getUI(this, SliderUI.class));
    }

    

}
