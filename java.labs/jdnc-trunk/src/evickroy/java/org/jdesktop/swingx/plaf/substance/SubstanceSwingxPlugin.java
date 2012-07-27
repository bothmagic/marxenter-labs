package org.jdesktop.swingx.plaf.substance;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.*;
import org.jvnet.lafplugin.LafPlugin;

import org.jdesktop.swingx.JXGroupableTableHeader;

public class SubstanceSwingxPlugin implements LafPlugin {

    public SubstanceSwingxPlugin() {
    }
    
    public Map<String, String> getUiDelegates() {
            Map<String, String> result = new HashMap<String, String>();
            result.put(JXGroupableTableHeader.muiClassID,
                            "org.jdesktop.swingx.plaf.substance.SubstanceGroupableTableHeaderUI");
            return result;
    }

    public Map<String, Font> getFonts() {
            Map<String, Font> result = new HashMap<String, Font>();
            return result;
    }

    public Object[] getDefaults(Object theme) {
            Object[] defaults = new Object[] {};
            return defaults;
    }

    public void resetCaches() {
    }

    public void initialize() {
    }

    public void reset() {
    }

}
