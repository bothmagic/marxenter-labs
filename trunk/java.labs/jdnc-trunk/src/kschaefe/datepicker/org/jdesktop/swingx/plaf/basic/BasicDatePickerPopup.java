/*
 * $Id: BasicDatePickerPopup.java 2384 2008-04-04 17:21:09Z osbald $
 * 
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;

/**
 * @author Karl George Schaefer
 *
 */
public class BasicDatePickerPopup extends JPopupMenu implements DatePickerPopup {
    private class Handler implements MouseListener, PropertyChangeListener,
            Serializable {

        /**
         * {@inheritDoc}
         */
        public void mouseClicked(MouseEvent e) { }

        /**
         * {@inheritDoc}
         */
        public void mouseEntered(MouseEvent e) { }

        /**
         * {@inheritDoc}
         */
        public void mouseExited(MouseEvent e) { }

        /**
         * {@inheritDoc}
         */
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == monthView) {
                return;
            }
            if (!SwingUtilities.isLeftMouseButton(e) || !picker.isEnabled())
                return;

            if ( picker.isEditable() ) {
                Component comp = picker.getEditor().getEditorComponent();
                if ((!(comp instanceof JComponent)) || ((JComponent)comp).isRequestFocusEnabled()) {
                    comp.requestFocus();
                }
            }
            else if (picker.isRequestFocusEnabled()) {
                picker.requestFocus();
            }
            togglePopup();
        }

        /**
         * {@inheritDoc}
         */
        public void mouseReleased(MouseEvent e) {
            if (e.getSource() == monthView) {
                picker.setDate(monthView.getSelectionDate());
                picker.setPopupVisible(false);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void propertyChange(PropertyChangeEvent evt) {
            JXDatePicker picker = (JXDatePicker) evt.getSource();
            String propertyName = evt.getPropertyName();

            if (propertyName == "componentOrientation") {
                // Pass along the new component orientation
                // to the list and the scroller

                ComponentOrientation o =(ComponentOrientation)evt.getNewValue();

                JXMonthView monthView = getMonthView();
                if (monthView!=null && monthView.getComponentOrientation()!=o) {
                    monthView.setComponentOrientation(o);
                }

                if (o!=getComponentOrientation()) {
                    setComponentOrientation(o);
                }
            }
            else if (propertyName == "lightWeightPopupEnabled") {
                setLightWeightPopupEnabled(picker.isLightWeightPopupEnabled());
            }
        }
        
    }
    
    private static Border LIST_BORDER = new LineBorder(Color.BLACK, 1);

    private JXDatePicker picker;
    
    protected JXMonthView monthView;
    
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseMotionListener
     * @see #createMouseMotionListener
     */
    protected MouseMotionListener      mouseMotionListener;
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getMouseListener
     * @see #createMouseListener
     */
    protected MouseListener            mouseListener;

    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the accessor or create methods instead.
     *
     * @see #getKeyListener
     * @see #createKeyListener
     */
    protected KeyListener              keyListener;

    /**
     * Implementation of all the listener classes.
     */
    private Handler handler;

    // Added to the date picker for bound properties
    /**
     * This protected field is implementation specific. Do not access directly
     * or override. Use the create method instead
     *
     * @see #createPropertyChangeListener
     */
    protected PropertyChangeListener   propertyChangeListener;

    public BasicDatePickerPopup(JXDatePicker picker) {
        setName("JXDatePicker.popup");
        this.picker = picker;
        this.monthView = createMonthView();
        
        setLightWeightPopupEnabled( picker.isLightWeightPopupEnabled() );

        // UI construction of the popup. 
        configurePopup();

        installComboBoxListeners();
//        installKeyboardActions();
    }
    
    protected JXMonthView createMonthView() {
        return new JXMonthView();
    }
    
    /**
     * This method adds the necessary listeners to the JComboBox.
     */
    protected void installComboBoxListeners() {
        if ((propertyChangeListener = createPropertyChangeListener()) != null) {
            picker.addPropertyChangeListener(propertyChangeListener);
        }
    }

    public JXMonthView getMonthView() {
        return monthView;
    }
    
    /**
     * Creates a <code>PropertyChangeListener</code> which will be added to
     * the combo box. If this method returns null then it will not
     * be added to the combo box.
     * 
     * @return an instance of a <code>PropertyChangeListener</code> or null
     */
    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * Configures the popup portion of the combo box. This method is called
     * when the UI class is created.
     */
    protected void configurePopup() {
        setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        setBorderPainted( true );
        setBorder(LIST_BORDER);
        setOpaque( false );
        add(monthView);
        setDoubleBuffered( true );
        setFocusable( false );
    }

    /**
     * Implementation of ComboPopup.show().
     */
    public void show() {
        Point location = getPopupLocation();
        show( picker, location.x, location.y );
    }

    /**
     * Calculates the upper left location of the Popup.
     */
    private Point getPopupLocation() {
        Dimension popupSize = picker.getSize();
        Insets insets = getInsets();

        // reduce the width of the scrollpane by the insets so that the popup
        // is the same width as the combo box.
        popupSize.setSize(popupSize.width - (insets.right + insets.left), 
                          monthView.getHeight());
        Rectangle popupBounds = computePopupBounds( 0, picker.getBounds().height,
                                                    popupSize.width, popupSize.height);
        Point popupLocation = popupBounds.getLocation();
            
        monthView.revalidate();

        return popupLocation;
    }

    protected Rectangle computePopupBounds(int px,int py,int pw,int ph) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Rectangle screenBounds;

        // Calculate the desktop dimensions relative to the combo box.
        GraphicsConfiguration gc = picker.getGraphicsConfiguration();
        Point p = new Point();
        SwingUtilities.convertPointFromScreen(p, picker);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= (screenInsets.left + screenInsets.right);
            screenBounds.height -= (screenInsets.top + screenInsets.bottom);
            screenBounds.x += (p.x + screenInsets.left);
            screenBounds.y += (p.y + screenInsets.top);
        }
        else {
            screenBounds = new Rectangle(p, toolkit.getScreenSize());
        }

        Rectangle rect = new Rectangle(px,py,pw,ph);
        if (py+ph > screenBounds.y+screenBounds.height
            && ph < screenBounds.height) {
            rect.y = -rect.height;
        }
        return rect;
    }

    /**
     * Implementation of DatePickerPopup.hide().
     */
    public void hide() {
        MenuSelectionManager manager = MenuSelectionManager.defaultManager();
        MenuElement [] selection = manager.getSelectedPath();
        for ( int i = 0 ; i < selection.length ; i++ ) {
            if ( selection[i] == this ) {
                manager.clearSelectedPath();
                break;
            }
        }
        if (selection.length > 0) {
            picker.repaint();
        }
    }

    /**
     * Overridden to unconditionally return false.
     */
    public boolean isFocusTraversable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public KeyListener getKeyListener() {
        if (keyListener == null) {
            keyListener = createKeyListener();
        }
        return keyListener;
    }

    /**
     * {@inheritDoc}
     */
    public MouseListener getMouseListener() {
        if (mouseListener == null) {
            mouseListener = createMouseListener();
        }
        return mouseListener;
    }

    /**
     * Creates a listener 
     * that will watch for mouse-press and release events on the combo box.
     * 
     * <strong>Warning:</strong>
     * When overriding this method, make sure to maintain the existing
     * behavior.
     *
     * @return a <code>MouseListener</code> which will be added to 
     * the combo box or null
     */
    protected MouseListener createMouseListener() {
        return getHandler();
    }

    /**
     * Creates the key listener that will be added to the combo box. If 
     * this method returns null then it will not be added to the combo box.
     *
     * @return a <code>KeyListener</code> or null
     */
    protected KeyListener createKeyListener() {
        return null;
    }

    /**
     * Makes the popup visible if it is hidden and makes it hidden if it is 
     * visible.
     */
    protected void togglePopup() {
        if ( isVisible() ) {
            hide();
        }
        else {
            show();
        }
    }

    protected MouseEvent convertMouseEvent( MouseEvent e ) {
        Point convertedPoint = SwingUtilities.convertPoint( (Component)e.getSource(),
                                                            e.getPoint(), monthView );

        MouseEvent newEvent = new MouseEvent( (Component)e.getSource(),
                                              e.getID(),
                                              e.getWhen(),
                                              e.getModifiers(),
                                              convertedPoint.x,
                                              convertedPoint.y,
                                              e.getClickCount(),
                                              e.isPopupTrigger(),
                                              MouseEvent.NOBUTTON );
        return newEvent;
    }

    public AccessibleContext getAccessibleContext() {
        AccessibleContext context = super.getAccessibleContext();
        context.setAccessibleParent(picker);
        return context;
    }
}
