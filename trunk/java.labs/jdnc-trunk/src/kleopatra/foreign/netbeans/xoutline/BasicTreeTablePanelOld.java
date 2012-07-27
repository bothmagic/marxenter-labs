/*
 * Created on 18.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
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
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.jdesktop.swingx.renderer.PainterAware;

/**
 * PENDING JW: obsolete?
 * 
 * Quick shot for a panel specialized on painting the tree cell in a TreeTable.
 * It manages the delegate and icon directly (not via a WrappingIconPanel!),
 * adjusts the leading/trailing (dependent on
 * ComponentOrientation) indent with the help of a Filler component and paints
 * a handle on the Filler as appropriate. NOTE: this has the core BoxLayout!<p>
 * 
 * Supports routing of key strokes as used in JTable for non-focused editing
 * mode. <p>
 * 
 * PENDING JW: This should be handled in the BasicTreeTableUI, maybe. <p> 
 * PENDING JW: Could fold into WrappingIconPanel? The filler width can be 0.
 * Doing so would allow to re-use for plain tree editing as well as for 
 * treetable editing. Plus, we'd need only one provider for all.<p>
 * 
 * PENDING JW: implement PainterAware
 * 
 */
public class BasicTreeTablePanelOld extends JXPanel {
    protected JComponent delegate;
    private JRendererLabel iconLabel;
    private int labelPosition = 2;
//    String labelPosition = BorderLayout.CENTER; //2;
//    private Filler filler;
    int iconLabelGap;
    private Border ltorBorder;
    private Border rtolBorder;
    private boolean dropHackEnabled;

//    private WrappingPanel realComponent;
    private Filler filler;
    private int indent = 20;
    private Icon handle;
    private int visualDepth;

    public BasicTreeTablePanelOld() {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        filler = (Filler) Box.createHorizontalStrut(indent);
        add(filler);
//        setOpaque(false);
        iconLabel = new JRendererLabel();
        iconLabelGap = iconLabel.getIconTextGap();
        iconLabel.setOpaque(false);
        add(iconLabel);
//        realComponent = new WrappingPanel(false);
//        add(realComponent);
        installFocusListener();
        dropHackEnabled = true;
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
//                realComponent.requestFocusForDelegate();
                if (delegate != null) {
                    delegate.requestFocus();
                }
                
            }
            
        };
        addFocusListener(focusListener);
    }


    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        updateIconBorder();
    }


    private void updateIconBorder() {
        if (ltorBorder == null) {
            ltorBorder = BorderFactory.createEmptyBorder(0, 0, 0, iconLabelGap);
            rtolBorder = BorderFactory.createEmptyBorder(0, iconLabelGap, 0, 0);
        } 
        if (getComponentOrientation().isLeftToRight()) {
            iconLabel.setBorder(ltorBorder);
        } else {
            iconLabel.setBorder(rtolBorder);
        }
        invalidate();
    }

    /**
     * Sets the icon.
     * 
     * @param icon the icon to use.
     */
    public void setIcon(Icon icon) {
        iconLabel.setIcon(icon);
        iconLabel.setText(null);
    }
 
    /**
     * Returns the icon used in this panel, may be null.
     * 
     * @return the icon used in this panel, may be null.
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }


    /**
     * Sets the delegate component. 
     * 
     * @param comp the component to add as delegate.
     */
    public void setComponent(JComponent comp) {
        if (delegate != null) {
            remove(delegate);
        }
        delegate = comp;
        add(delegate, labelPosition);
        invalidate();
    }


    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the background of the delegate and icon label as well.
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (iconLabel != null) {
            iconLabel.setBackground(bg);
        }
        if (delegate != null) {
            delegate.setBackground(bg);
        }
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the foreground of the delegate and icon label as well.
     */
    @Override
    public void setForeground(Color bg) {
        super.setForeground(bg);
        if (iconLabel != null) {
            iconLabel.setForeground(bg);
        }
        if (delegate != null) {
            delegate.setForeground(bg);
        }
    }


    
    
    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the Font of the delegate as well.
     */
    @Override
    public void setFont(Font font) {
        if (delegate != null) {
            delegate.setFont(font);
        }
        super.setFont(font);
    }

    
    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to hack around #766-swingx: cursor flickering in DnD when
     * dragging over tree column. This is a core bug (#6700748) related to
     * painting the rendering component on a CellRendererPane. A trick around is
     * to let this return false.
     * <p>
     * 
     * Some LayoutManagers don't layout an invisible component, so need to make
     * the hack-enabled configurable. This implementation will return false 
     * if isDropHackEnabled, super.isVisible otherwise.
     */
    @Override
    public boolean isVisible() {
        return dropHackEnabled ? false : super.isVisible();
    }


    /**
     * {@inheritDoc}
     * <p>
     * 
     * Returns the delegate's Painter if it is of type PainterAware or null
     * otherwise.
     * 
     * @return the delegate's Painter or null.
     */
    public Painter getPainter() {
        if (delegate instanceof PainterAware) {
            return ((PainterAware) delegate).getPainter();
        }
        return null;
    }


    /**
     * Sets the delegate's Painter if it is of type PainterAware. Does nothing otherwise.
     * 
     * @param painter the Painter to apply to the delegate.
     */
    public void setPainter(Painter painter) {
        if (delegate instanceof PainterAware) {
            ((PainterAware) delegate).setPainter(painter);
        }
        
    }
    
    /**
     * 
     * Returns the bounds of the delegate component or null if the delegate is null.
     * 
     * PENDING JW: where do we use it? Maybe it was for testing only?
     * 
     * @return the bounds of the delegate, or null if the delegate is null.
     */
    public Rectangle getDelegateBounds() {
        if (delegate == null) return null;
        return delegate.getBounds();
    }


    /**
     * Sets the dropHackEnabled property. <p>
     * 
     * The default value is true.
     * 
     * @param dropHackEnabled 
     * 
     * @see #isVisible()
     */
    public void setDropHackEnabled(boolean dropHackEnabled) {
        this.dropHackEnabled = dropHackEnabled;
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
        if(delegate != null) {
            delegate.setBorder(border);
        }
//        realComponent.setCellBorder(border);
        
    }
    
    
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        boolean processed = super.processKeyBinding(ks, e, condition, pressed);
        if (!processed) {
//            processed = realComponent.processKeyBinding(ks, e, condition, pressed);
            processed = processKeyBinding(delegate, ks, e, condition, pressed);
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
    
    

//  public void setIcon(Icon icon) {
//      realComponent.setIcon(icon);
//      invalidate();
//  }
//
//  public void setComponent(JComponent rendererComponent) {
//      realComponent.setComponent(rendererComponent);
//      invalidate();
//  }
//  @Override
//  public void setBackground(Color bg) {
//      super.setBackground(bg);
//      if (realComponent != null)
//      realComponent.setBackground(bg);
//  }
//
//  @Override
//  public void setForeground(Color bg) {
//      super.setForeground(bg);
//      if (realComponent != null)
//      realComponent.setForeground(bg);
//  }
//
//  
//    public static class WrappingPanel extends WrappingIconPanel {
//        public WrappingPanel(boolean dropHackEnabled) {
//            super(dropHackEnabled);
//        }
//
//        public void requestFocusForDelegate() {
//            if (delegate !=  null) {
//                delegate.requestFocus();
//            }
//            
//        }
//
//        public void setCellBorder(Border border) {
//            if(delegate != null) {
//                delegate.setBorder(border);
//            }
//            
//        }
//
//        
//        @Override
//        protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
//                int condition, boolean pressed) {
//            boolean processed = super.processKeyBinding(ks, e, condition, pressed);
//            if (!processed && (delegate != null)) {
//                processed = processKeyBinding(delegate, ks, e,  condition, pressed);
//            }
//            return processed;
//        }
//
//        private boolean processKeyBinding(JComponent delegate, KeyStroke ks,
//                KeyEvent e, int condition, boolean pressed) {
//            InputMap map = delegate.getInputMap(condition);
//            ActionMap am = delegate.getActionMap();
//
//            if(map != null && am != null && isEnabled()) {
//                Object binding = map.get(ks);
//                Action action = (binding == null) ? null : am.get(binding);
//                if (action != null) {
//                    return SwingUtilities.notifyAction(action, ks, e, delegate,
//                                                       e.getModifiers());
//                }
//            }
//            return false;
//        }
//        
//        
//        
//    }

    
}
