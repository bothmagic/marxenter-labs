package org.jdesktop.swingx.plaf.liquid;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneContainerUI;

public class LiquidTaskPaneContainerUI extends BasicTaskPaneContainerUI {

  public static ComponentUI createUI(JComponent c) {
    return new LiquidTaskPaneContainerUI();
  }

  public void installUI(JComponent c) {
    super.installUI(c);

    JXTaskPaneContainer taskPane = (JXTaskPaneContainer)c;
    taskPane.setLayout(new VerticalLayout(14));
    taskPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    taskPane.setOpaque(false);
  }
}
