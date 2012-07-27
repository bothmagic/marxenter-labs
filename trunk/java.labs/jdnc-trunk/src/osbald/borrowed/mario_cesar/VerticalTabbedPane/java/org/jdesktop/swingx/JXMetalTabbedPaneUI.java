/*
 * JXMetalTabbedPaneUI.java
 *
 * Created on 12 de Dezembro de 2006, 04:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 *
 * @author Mário César
 */
public class JXMetalTabbedPaneUI extends MetalTabbedPaneUI {
    public JXMetalTabbedPaneUI() {
        super();
    }
    //@Override // cannot override static methods
    public static ComponentUI createUI(JComponent x) {
        return new JXMetalTabbedPaneUI();
    }
    @Override
    protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
        Insets insets = getTabAreaInsets(tabPlacement);
        int tabAreaWidth = vertRunCount * maxTabWidth - (vertRunCount - 1) * getTabRunOverlay(tabPlacement);
        tabAreaWidth += insets.left + insets.right;
        return tabAreaWidth;
    }
    @Override
    protected int getTabRunOverlay(int tabPlacement) {
        return 0;
    }
}
