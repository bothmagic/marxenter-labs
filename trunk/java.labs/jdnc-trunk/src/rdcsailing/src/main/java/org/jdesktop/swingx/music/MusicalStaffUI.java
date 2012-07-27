package org.jdesktop.swingx.music;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicEditorPaneUI;

/**
 * MusicalStaffEditorKit
 * @author Ryan Cuprak
 */
public class MusicalStaffUI extends BasicEditorPaneUI {

    /**
     * Configures the CurrencyFieldUI
     * @param component - component
     * @return ComponentUI
     */
    public static ComponentUI createUI(JComponent component) {
        return new MusicalStaffUI();
    }

    /**
     * Paints the musical staff
     * @param g - graphics context
     */
    protected void paintBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.YELLOW);
        int width = this.getComponent().getWidth();
        int height = this.getComponent().getHeight();
        g2d.fillRect(0,0,width,height);
    }


}
