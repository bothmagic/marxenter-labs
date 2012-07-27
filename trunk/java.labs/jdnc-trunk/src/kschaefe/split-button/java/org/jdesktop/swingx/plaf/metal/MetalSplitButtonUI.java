package org.jdesktop.swingx.plaf.metal;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;

import org.jdesktop.swingx.plaf.basic.BasicSplitButtonUI;

public class MetalSplitButtonUI extends BasicSplitButtonUI {
    public static ComponentUI createUI(JComponent c) {
        return new MetalSplitButtonUI();
    }
}
