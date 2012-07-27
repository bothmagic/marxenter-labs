package org.jdesktop.swingx.inspection.renderer;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import org.jdesktop.swingx.inspection.*;
import java.awt.Component;
import org.jdesktop.swingx.JXInspectionPane;
import javax.swing.UIManager;
import javax.swing.*;

public class ProgressRenderer extends JProgressBar implements InspectionValueRenderer {

    private static final BoundedRangeModel DEFAULT_MODEL = new DefaultBoundedRangeModel(0, 0, 0, 100);

    private String lookAndFeel;
    public ProgressRenderer() {
        super();
        lookAndFeel = UIManager.getLookAndFeel().getName();
    }





    public Component getInspectionValueRendererComponent(JXInspectionPane pane, Object value, String label, int row,
          boolean editable, boolean enabled) {
        if (UIManager.getLookAndFeel().getName() != lookAndFeel) {
            lookAndFeel = UIManager.getLookAndFeel().getName();
            updateUI();
        }

        setModel(DEFAULT_MODEL);
        if (value instanceof Integer) {
            DEFAULT_MODEL.setValue((Integer) value);
        } else if (value instanceof Float || value instanceof Double) {
            DEFAULT_MODEL.setValue((int) (((Number) value).doubleValue() * 100));
        } else if (value instanceof BoundedRangeModel) {
            setModel((BoundedRangeModel) value);
        }

        return this;
    }
}
