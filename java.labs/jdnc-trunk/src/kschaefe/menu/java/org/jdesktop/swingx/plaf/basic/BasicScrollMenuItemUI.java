/**
 * 
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * 
 */
public class BasicScrollMenuItemUI extends BasicMenuItemUI {
    private class ScrollListener extends MouseInputHandler implements
            ActionListener {
        // Because we are handling both mousePressed and Actions
        // we need to make sure we don't fire under both conditions.
        // (keyfocus on scrollbars causes action without mousePress
        boolean handledEvent;

        Timer scrollTimer;

        public ScrollListener() {
            scrollTimer = new Timer(60, this);
            scrollTimer.setInitialDelay(300); // default InitialDelay?
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                ((AbstractButton) e.getSource()).doClick();
            }
        }
        
//        public void mouseEntered(MouseEvent e) {
//            MenuSelectionManager.defaultManager().setSelectedPath(getPath());
//        }
//        
        @Override
        public void mousePressed(MouseEvent e) {
             AbstractButton b = (AbstractButton) e.getSource();
            
             if (!b.isEnabled()) {
                 return;
             }
             
             if (!SwingUtilities.isLeftMouseButton(e)) {
                 return;
             }
             
             scrollTimer.stop();
             scrollTimer.start();
             handledEvent = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            scrollTimer.stop();
            handledEvent = false;
        }

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            menuItem.doClick();
        }
    }

    public static ComponentUI createUI(JComponent c) {
        return new BasicScrollMenuItemUI();
    }
    
    @Override
    protected MouseInputListener createMouseInputListener(JComponent c) {
        return new ScrollListener();
    }

    @Override
    protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon,
            Icon arrowIcon, Color background, Color foreground,
            int defaultTextIconGap) {
        JMenuItem b = (JMenuItem) c;
        ButtonModel model = b.getModel();

        // Paint background
        paintBackground(g, b, background);

        Color holdc = g.getColor();

        if (model.isArmed() || (b instanceof JMenu && model.isSelected())) {
            g.setColor(foreground);
        }

        // should never be null for this menu item type
        Icon icon = b.getIcon();

        // Paint the Icon
        if (icon != null) {
            int x = b.getWidth() / 2 - icon.getIconWidth() / 2;
            int y = b.getHeight() / 2 - icon.getIconHeight() / 2;

            g.translate(x, y);
            icon.paintIcon(b, g, x, y);
            g.translate(-x, -y);
        }

        g.setColor(holdc);
    }
}
