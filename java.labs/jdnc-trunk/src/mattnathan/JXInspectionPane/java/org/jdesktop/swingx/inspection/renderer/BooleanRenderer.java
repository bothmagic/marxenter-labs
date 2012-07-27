package org.jdesktop.swingx.inspection.renderer;

import org.jdesktop.swingx.inspection.*;
import javax.swing.*;
import java.awt.Component;
import org.jdesktop.swingx.JXInspectionPane;

public class BooleanRenderer extends JCheckBox implements InspectionValueRenderer {

    private String lookAndFeel;
    public BooleanRenderer() {
        super();
        lookAndFeel = UIManager.getLookAndFeel().getName();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }





    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row,
          boolean editable, boolean enabled) {
        if (UIManager.getLookAndFeel().getName() != lookAndFeel) {
            lookAndFeel = UIManager.getLookAndFeel().getName();
            updateUI();
        }

        setEnabled(enabled);
        setSelected((Boolean) value);
        return this;
    }
}
