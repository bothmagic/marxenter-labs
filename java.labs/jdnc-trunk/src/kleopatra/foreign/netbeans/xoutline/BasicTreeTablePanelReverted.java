/*
 * Created on 18.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Box.Filler;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.renderer.WrappingIconPanel;

/**
 * Quick shot for a panel specialized on painting the tree cell in a TreeTable.
 * It contains a WrappingIconPanel, adjusts the leading/trailing (dependent on
 * ComponentOrientation) indent with the help of a Filler component and paints
 * a handle on the Filler as appropriate.<p>
 * 
 * PENDING JW: This should be handled in the BasicTreeTableUI, maybe. <p> 
 * PENDING JW: Could fold into WrappingIconPanel? The filler width can be 0.
 * Doing so would allow to re-use for plain tree editing as well as for 
 * treetable editing. Plus, we'd need only one provider for all.
 * 
 */
public class BasicTreeTablePanelReverted extends JXPanel {

    private WrappingPanel realComponent;
    private Filler filler;
    private int indent = 20;
    private Icon handle;
    private int visualDepth;

    public BasicTreeTablePanelReverted() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        realComponent = new WrappingPanel(false);
        add(realComponent);
        filler = (Filler) Box.createHorizontalStrut(indent);
        add(filler, 0);
        installFocusListener();
    }


    /**
     * Install a focusListener to transfer the focus down to 
     * the delegate. Happens if the wrappingPanel houses an editing
     * component. <p>
     * 
     * PENDING JW: Crazy to listen to itself .. but works. Revisit.
     */
    private void installFocusListener() {
        FocusListener focusListener = new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                realComponent.requestFocusForDelegate();
                
            }
            
        };
        addFocusListener(focusListener);
    }



    public void setIcon(Icon icon) {
        realComponent.setIcon(icon);
        invalidate();
    }

    public void setComponent(JComponent rendererComponent) {
        realComponent.setComponent(rendererComponent);
        invalidate();
    }

    public void setIndent(int visualDepth) {
        this.visualDepth = visualDepth;
        Dimension min = filler.getMinimumSize();
        min.width = visualDepth * indent;
        Dimension pref = filler.getPreferredSize();
        pref.width = min.width;
        Dimension max = filler.getMaximumSize();
        max.width = min.width;
        filler.changeShape(min, pref, max);
        // don't need to revalidate here, changeShape already does
    }

    public void setCellBorder(Border border) {
        realComponent.setCellBorder(border);
        
    }
    
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (realComponent != null)
        realComponent.setBackground(bg);
    }

    @Override
    public void setForeground(Color bg) {
        super.setForeground(bg);
        if (realComponent != null)
        realComponent.setForeground(bg);
    }

    
    
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        boolean processed = super.processKeyBinding(ks, e, condition, pressed);
        if (!processed) {
            processed = realComponent.processKeyBinding(ks, e, condition, pressed);
        }
        return processed;
    }



    public static class WrappingPanel extends WrappingIconPanel {
        public WrappingPanel(boolean dropHackEnabled) {
            super(dropHackEnabled);
        }

        public void requestFocusForDelegate() {
            if (delegate !=  null) {
                delegate.requestFocus();
            }
            
        }

        public void setCellBorder(Border border) {
            if(delegate != null) {
                delegate.setBorder(border);
            }
            
        }

        
        @Override
        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
                int condition, boolean pressed) {
            boolean processed = super.processKeyBinding(ks, e, condition, pressed);
            if (!processed && (delegate != null)) {
                processed = processKeyBinding(delegate, ks, e,  condition, pressed);
            }
            return processed;
        }

        private boolean processKeyBinding(JComponent delegate, KeyStroke ks,
                KeyEvent e, int condition, boolean pressed) {
            InputMap map = delegate.getInputMap(condition);
            ActionMap am = delegate.getActionMap();

            if(map != null && am != null && isEnabled()) {
                Object binding = map.get(ks);
                Action action = (binding == null) ? null : am.get(binding);
                if (action != null) {
                    return SwingUtilities.notifyAction(action, ks, e, delegate,
                                                       e.getModifiers());
                }
            }
            return false;
        }
        
        
        
    }

    public void setHandle(Icon handle) {
        this.handle = handle;
    }
    
    

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        paintHandle(g);
    }

    private void paintHandle(Graphics g) {
        if ((handle == null) || (visualDepth <= 0)) return;
        int iconX;
        if (getComponentOrientation().isLeftToRight()) {
            iconX = (visualDepth - 1) * indent;
        } else {
            iconX = getWidth() - ((visualDepth -1) * indent) - handle.getIconWidth();
        }
        int iconY = 0;
        if (handle.getIconHeight() < getHeight()) {
            iconY = (getHeight() / 2) - (handle.getIconHeight() / 2);
        } 
        handle.paintIcon(this, g, iconX, iconY);
    }
    
    
}
