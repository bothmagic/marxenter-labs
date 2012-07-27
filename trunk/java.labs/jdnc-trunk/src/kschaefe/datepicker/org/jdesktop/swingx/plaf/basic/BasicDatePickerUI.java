/*
 * $Id: BasicDatePickerUI.java 2384 2008-04-04 17:21:09Z osbald $
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.ComboPopup;

import org.jdesktop.swingx.DatePickerEditor;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.UIAction;
import org.jdesktop.swingx.plaf.DatePickerUI;

import sun.swing.DefaultLookup;

/**
 * The basic implementation of a <code>DatePickerUI</code>.
 * <p>
 * 
 * 
 * @author Joshua Outwater
 * @author Jeanette Winzenburg
 * @author Karl Schaefer
 */
public class BasicDatePickerUI extends DatePickerUI {
    private class Handler implements ActionListener, FocusListener,
            KeyListener, LayoutManager, PropertyChangeListener {

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            Date date = datePicker.getEditor().getDate();

            if (date != null) {
                if (!datePicker.isPopupVisible()
                        && !date.equals(datePicker.getDate())) {
                    datePicker.setDate(date);
                }

                ActionMap am = datePicker.getActionMap();

                if (am != null) {
                    Action action = am.get("enterPressed");
                    if (action != null) {
                        action.actionPerformed(new ActionEvent(datePicker, e
                                .getID(), e.getActionCommand(), e
                                .getModifiers()));
                    }
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        public void focusGained(FocusEvent e) {
            DatePickerEditor datePickerEditor = datePicker.getEditor();

            if ((datePickerEditor != null)
                    && (e.getSource() == datePickerEditor.getEditorComponent())) {
                return;
            }

            hasFocus = true;
            datePicker.repaint();

            if (datePicker.isEditable() && editor != null) {
                editor.requestFocus();
            }
        }

        /**
         * {@inheritDoc}
         */
        public void focusLost(FocusEvent e) {
            DatePickerEditor editor = datePicker.getEditor();
            if ((editor != null)
                    && (e.getSource() == editor.getEditorComponent())) {
                Date newDate = editor.getDate();
                Date currentDate = datePicker.getDate();

                if (!e.isTemporary() && newDate != null
                        && !newDate.equals(currentDate)) {
                    // TODO centralize the actionPerformed logic without
                    // implementing ActionListener
                    // datePicker.actionPerformed
                    // (new ActionEvent(editor, 0, "",
                    // EventQueue.getMostRecentEventTime(), 0));
                }
            }

            hasFocus = false;
            if (!e.isTemporary()) {
                setPopupVisible(datePicker, false);
            }
            datePicker.repaint();
        }

        /**
         * {@inheritDoc}
         */
        public void keyPressed(KeyEvent e) {
            // TODO handle navigation keys

        }

        /**
         * {@inheritDoc}
         */
        public void keyReleased(KeyEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        public void keyTyped(KeyEvent e) {
        }

        /**
         * {@inheritDoc}
         */
        public void addLayoutComponent(String name, Component comp) {
        }

        /**
         * {@inheritDoc}
         */
        public void layoutContainer(Container parent) {
            JXDatePicker dp = (JXDatePicker) parent;
            int width = dp.getWidth();
            int height = dp.getHeight();

            Insets insets = getInsets();
            int buttonSize = height - (insets.top + insets.bottom);
            Rectangle cvb;

            if (arrowButton != null) {
                if (dp.getComponentOrientation().isLeftToRight()) {
                    arrowButton.setBounds(width - (insets.right + buttonSize),
                            insets.top, buttonSize, buttonSize);
                } else {
                    arrowButton.setBounds(insets.left, insets.top, buttonSize,
                            buttonSize);
                }
            }

            if (editor != null) {
                cvb = rectangleForCurrentValue();
                editor.setBounds(cvb);
            }
        }

        /**
         * {@inheritDoc}
         */
        public Dimension minimumLayoutSize(Container parent) {
            return parent.getMinimumSize();
        }

        /**
         * {@inheritDoc}
         */
        public Dimension preferredLayoutSize(Container parent) {
            return parent.getPreferredSize();
        }

        /**
         * {@inheritDoc}
         */
        public void removeLayoutComponent(Component comp) {
        }

        /**
         * {@inheritDoc}
         */
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            JXDatePicker datePicker = (JXDatePicker) evt.getSource();

            if (propertyName == "editor" && datePicker.isEditable()) {
                addEditor();
                datePicker.revalidate();
            } else if (propertyName == "editable") {
                if (datePicker.isEditable()) {
                    datePicker.setRequestFocusEnabled(false);
                    addEditor();
                } else {
                    datePicker.setRequestFocusEnabled(true);
                    removeEditor();
                }

                updateToolTipTextForChildren();

                datePicker.revalidate();
            } else if (propertyName == "date") { //XYZZY
                Date date = datePicker.getDate();
                if (datePicker.getEditor() != null) {
                    datePicker.getEditor().setDate(date);
                }
                datePicker.repaint();
            } else if (propertyName == "enabled") {
                boolean enabled = datePicker.isEnabled();
                if (editor != null)
                    editor.setEnabled(enabled);
                if (arrowButton != null)
                    arrowButton.setEnabled(enabled);
                datePicker.repaint();
            } else if (propertyName == "focusable") {
                boolean focusable = datePicker.isFocusable();
                if (editor != null)
                    editor.setFocusable(focusable);
                if (arrowButton != null)
                    arrowButton.setFocusable(focusable);
                datePicker.repaint();
            } else if (propertyName == "font") {
                if (editor != null) {
                    editor.setFont(datePicker.getFont());
                }
                isMinimumSizeDirty = true;
                datePicker.validate();
            } else if (propertyName == JComponent.TOOL_TIP_TEXT_KEY) {
                updateToolTipTextForChildren();
            }
            // else if ( propertyName == BasicComboBoxUI.IS_TABLE_CELL_EDITOR )
            // {
            // Boolean inTable = (Boolean)e.getNewValue();
            // isTableCellEditor = inTable.equals(Boolean.TRUE) ? true : false;
            // }
        }

    }

    private static class Actions extends UIAction {
        private static final String HIDE = "hidePopup";

        private static final String DOWN = "selectNext";

        private static final String DOWN_2 = "selectNext2";

        private static final String TOGGLE = "togglePopup";

        private static final String TOGGLE_2 = "spacePopup";

        private static final String UP = "selectPrevious";

        private static final String UP_2 = "selectPrevious2";

        private static final String ENTER = "enterPressed";

        private static final String PAGE_DOWN = "pageDownPassThrough";

        private static final String PAGE_UP = "pageUpPassThrough";

        private static final String HOME = "homePassThrough";

        private static final String END = "endPassThrough";

        /**
         * @param name
         */
        public Actions(String name) {
            super(name);
        }

        /**
         * {@inheritDoc}
         */
        public void actionPerformed(ActionEvent e) {
            String key = getName();
            System.err.println(key);
            JXDatePicker datePicker = (JXDatePicker) e.getSource();
            BasicDatePickerUI ui = BasicDatePickerUI.class
                    .isInstance(datePicker.getUI()) ? (BasicDatePickerUI) datePicker
                    .getUI()
                    : null;

            if (key == HIDE) {
                // datePicker.firePopupMenuCanceled();
                datePicker.setPopupVisible(false);
            }
            // TODO for navigating the month view
            // else if (key == PAGE_DOWN || key == PAGE_UP ||
            // key == HOME || key == END) {
            // int index = getNextIndex(datePicker, key);
            // if (index >= 0 && index < datePicker.getItemCount()) {
            // datePicker.setSelectedIndex(index);
            // }
            // }
            else if (key == DOWN) {
                if (datePicker.isShowing()) {
                    if (datePicker.isPopupVisible()) {
                        // if (ui != null) {
                        // ui.selectNextPossibleValue();
                        // }
                    } else {
                        datePicker.setPopupVisible(true);
                    }
                }
            } else if (key == DOWN_2) {
                // Special case in which pressing the arrow keys will not
                // make the popup appear - except for editable combo boxes
                // and combo boxes inside a table.
                if (datePicker.isShowing()) {
                    if ((datePicker.isEditable() || (ui != null /*
                                                                 * &&
                                                                 * ui.isTableCellEditor()
                                                                 */))
                            && !datePicker.isPopupVisible()) {
                        datePicker.setPopupVisible(true);
                        // } else {
                        // if (ui != null) {
                        // ui.selectNextPossibleValue();
                        // }
                    }
                }
            } else if (key == TOGGLE || key == TOGGLE_2) {
                if (ui != null && (key == TOGGLE || !datePicker.isEditable())) {
                    // if ( ui.isTableCellEditor() ) {
                    // // Forces the selection of the list item if the
                    // // combo box is in a JTable.
                    // datePicker.setSelectedIndex(ui.popup.getList().
                    // getSelectedIndex());
                    // }
                    // else {
                    datePicker.setPopupVisible(!datePicker.isPopupVisible());
                    // }
                }
            } else if (key == UP) {
                if (ui != null) {
                    /*
                     * if (ui.isPopupVisible(datePicker)) {
                     * ui.selectPreviousPossibleValue(); } else
                     */if (DefaultLookup.getBoolean(datePicker, ui,
                            "JXDatePicker.showPopupOnNavigation", false)) {
                        ui.setPopupVisible(datePicker, true);
                    }
                }
            } else if (key == UP_2) {
                // Special case in which pressing the arrow keys will not
                // make the popup appear - except for editable combo boxes.
                if (datePicker.isShowing() && ui != null) {
                    if (datePicker.isEditable() && !datePicker.isPopupVisible()) {
                        datePicker.setPopupVisible(true);
                        // } else {
                        // ui.selectPreviousPossibleValue();
                    }
                }
            }

            else if (key == ENTER) {
                if (datePicker.isPopupVisible()) {
                    // Forces the selection of the list item
                    boolean isEnterSelectablePopup = UIManager
                            .getBoolean("JXDatePicker.isEnterSelectablePopup");
                    System.err.println("processing enter");
                    if (!datePicker.isEditable() || isEnterSelectablePopup) {
                        System.err.println("setting date");
                        Date newDate = ui.popup.getMonthView().getSelectionDate();
                        if (newDate != null) {
                            datePicker.setDate(newDate);
//                            // Ensure that JComboBox.actionPerformed()
//                            // doesn't set editor value as selected item
//                            datePicker.getEditor().setDate(newDate);
                        }
                    }
                    datePicker.setPopupVisible(false);
                } else {
                    // Call the default button binding.
                    // This is a pretty messy way of passing an event through
                    // to the root pane.
                    JRootPane root = SwingUtilities.getRootPane(datePicker);
                    if (root != null) {
                        InputMap im = root
                                .getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
                        ActionMap am = root.getActionMap();
                        if (im != null && am != null) {
                            Object obj = im.get(KeyStroke.getKeyStroke(
                                    KeyEvent.VK_ENTER, 0));
                            if (obj != null) {
                                Action action = am.get(obj);
                                if (action != null) {
                                    action.actionPerformed(new ActionEvent(
                                            root, e.getID(), e
                                                    .getActionCommand(), e
                                                    .getWhen(), e
                                                    .getModifiers()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //
    // Fields
    //

    /**
     * The date picker managed by this delegate.
     */
    protected JXDatePicker datePicker;

    protected DatePickerPopup popup;

    protected JButton arrowButton;

    protected JXMonthView monthBox;

    // The Component that the ComboBoxEditor uses for editing
    protected Component editor;

    private boolean hasFocus = false;

    // Used to render the currently selected item in the combo box.
    // It doesn't have anything to do with the popup's rendering.
    protected CellRendererPane currentValuePane = new CellRendererPane();

    protected KeyListener keyListener;

    protected FocusListener focusListener;

    protected PropertyChangeListener propertyChangeListener;

    // Cached minimum preferred size.
    protected Dimension cachedMinimumSize = new Dimension(0, 0);

    // Flag for recalculating the minimum preferred size.
    protected boolean isMinimumSizeDirty = true;

    protected ItemListener itemListener;

    static final StringBuffer HIDE_POPUP_KEY = new StringBuffer("HidePopupKey");

    private Handler handler;

    // Listeners that the Month View produces.
    protected MouseListener popupMouseListener;

    protected MouseMotionListener popupMouseMotionListener;

    protected KeyListener popupKeyListener;

    // this would be for a LazyActionMap if it were core
    static void loadActionMap(ActionMap map) {
        map.put(Actions.HIDE, new Actions(Actions.HIDE));
        map.put(Actions.PAGE_DOWN, new Actions(Actions.PAGE_DOWN));
        map.put(Actions.PAGE_UP, new Actions(Actions.PAGE_UP));
        map.put(Actions.HOME, new Actions(Actions.HOME));
        map.put(Actions.END, new Actions(Actions.END));
        map.put(Actions.DOWN, new Actions(Actions.DOWN));
        map.put(Actions.DOWN_2, new Actions(Actions.DOWN_2));
        map.put(Actions.TOGGLE, new Actions(Actions.TOGGLE));
        map.put(Actions.TOGGLE_2, new Actions(Actions.TOGGLE_2));
        map.put(Actions.UP, new Actions(Actions.UP));
        map.put(Actions.UP_2, new Actions(Actions.UP_2));
        map.put(Actions.ENTER, new Actions(Actions.ENTER));
    }

    // ========================
    // begin UI Initialization
    //

    public static ComponentUI createUI(JComponent c) {
        return new BasicDatePickerUI();
    }

    public void installUI(JComponent c) {
        // isMinimumSizeDirty = true;

        datePicker = (JXDatePicker) c;
        installDefaults();
        popup = createPopup();

        // Is this date picker a cell editor?
        // Boolean inTable = (Boolean)c.getClientProperty(IS_TABLE_CELL_EDITOR);
        // if (inTable != null) {
        // isTableCellEditor = inTable.equals(Boolean.TRUE) ? true : false;
        // }

        if (datePicker.getEditor() == null
                || datePicker.getEditor() instanceof UIResource) {
            datePicker.setEditor(createEditor());
        }

        installListeners();
        installComponents();

        datePicker.setLayout(createLayoutManager());

        datePicker.setRequestFocusEnabled(true);

        // installKeyboardActions();

        datePicker.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);

        // if (keySelectionManager == null || keySelectionManager instanceof
        // UIResource) {
        // keySelectionManager = new DefaultKeySelectionManager();
        // }
        // comboBox.setKeySelectionManager(keySelectionManager);
    }

    /**
     * Installs the default colors, default font, default renderer, and default
     * editor into the JComboBox.
     */
    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(datePicker, "JXDatePicker.background",
                "JXDatePicker.foreground", "JXDatePicker.font");
        // LookAndFeel.installBorder(datePicker, "JXDatePicker.border");
        LookAndFeel.installProperty(datePicker, "opaque", Boolean.TRUE);
        // TODO add action/input map
        ActionMap actionMap = new ActionMap();
        loadActionMap(actionMap);
        SwingUtilities.replaceUIActionMap(datePicker, actionMap);

        InputMap inputMap = LookAndFeel.makeInputMap((Object[]) UIManager
                .get("JXDatePicker.ancestorInputMap"));
        SwingUtilities.replaceUIInputMap(datePicker,
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, inputMap);

        // Long l = (Long)UIManager.get("ComboBox.timeFactor");
        // timeFactor = (l!=null) ? l.longValue() : 1000L;
    }

    protected void installListeners() {
        if ((propertyChangeListener = createPropertyChangeListener()) != null) {
            datePicker.addPropertyChangeListener(propertyChangeListener);
        }
        if ((keyListener = createKeyListener()) != null) {
            datePicker.addKeyListener(keyListener);
        }
        if ((focusListener = createFocusListener()) != null) {
            datePicker.addFocusListener(focusListener);
        }
        if ((popupMouseListener = popup.getMouseListener()) != null) {
            datePicker.addMouseListener(popupMouseListener);
        }
        if ((popupKeyListener = popup.getKeyListener()) != null) {
            datePicker.addKeyListener(popupKeyListener);
        }
    }

    /**
     * Creates and initializes the components which make up the aggregate date
     * picker. This method is called as part of the UI installation process.
     */
    protected void installComponents() {
        arrowButton = createArrowButton();
        datePicker.add(arrowButton);

        if (arrowButton != null) {
            configureArrowButton();
        }

        if (datePicker.isEditable()) {
            addEditor();
        }

        // TODO review for inclusion or removal
        // datePicker.add(currentValuePane);
    }

    /**
     * Creates an button which will be used as the control to show or hide the
     * popup portion of the date picker.
     * 
     * @return a button which represents the popup control
     */
    protected JButton createArrowButton() {
        JButton button = new BasicArrowButton(BasicArrowButton.SOUTH, UIManager
                .getColor("JXDatePicker.buttonBackground"), UIManager
                .getColor("JXDatePicker.buttonShadow"), UIManager
                .getColor("JXDatePicker.buttonDarkShadow"), UIManager
                .getColor("JXDatePicker.buttonHighlight"));
        button.setName("JXDatePicker.arrowButton");
        return button;
    }

    private void configureArrowButton() {
        if (arrowButton != null) {
            arrowButton.setEnabled(datePicker.isEnabled());
            arrowButton.setFocusable(datePicker.isFocusable());
            arrowButton.setRequestFocusEnabled(false);
            arrowButton.addMouseListener(popup.getMouseListener());
            arrowButton.resetKeyboardActions();
            arrowButton.putClientProperty("doNotCancelPopup", HIDE_POPUP_KEY);
            arrowButton.setInheritsPopupMenu(true);
        }
    }

    /**
     * This public method is implementation specific and should be private. do
     * not call or override. To implement a specific editor create a custom
     * <code>ComboBoxEditor</code>
     * 
     * @see #createEditor
     * @see javax.swing.JComboBox#setEditor
     * @see javax.swing.ComboBoxEditor
     */
    private void addEditor() {
        removeEditor();
        editor = datePicker.getEditor().getEditorComponent();
        if (editor != null) {
            configureEditor();
            datePicker.add(editor);
            if (datePicker.isFocusOwner()) {
                // Switch focus to the editor component
                editor.requestFocusInWindow();
            }
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * do not call or override.
     * 
     * @see #addEditor
     */
    private void configureEditor() {
        // Should be in the same state as the combobox
        editor.setEnabled(datePicker.isEnabled());

        editor.setFocusable(datePicker.isFocusable());

        editor.setFont(datePicker.getFont());

        if (focusListener != null) {
            editor.addFocusListener(focusListener);
        }

        editor.addFocusListener(getHandler());

        datePicker.getEditor().addActionListener(getHandler());

        if (editor instanceof JComponent) {
            ((JComponent) editor).putClientProperty("doNotCancelPopup",
                    HIDE_POPUP_KEY);
            ((JComponent) editor).setInheritsPopupMenu(true);
        }

        // TODO review this line for inclusion/removal
        // datePicker.configureEditor(datePicker.getEditor(),
        // datePicker.getSelectedItem());
    }

    /**
     * This public method is implementation specific and should be private. do
     * not call or override.
     * 
     * @see #addEditor
     */
    private void removeEditor() {
        if (editor != null) {
            unconfigureEditor();
            datePicker.remove(editor);
            editor = null;
        }
    }

    /**
     * This protected method is implementation specific and should be private.
     * Do not call or override.
     * 
     * @see #addEditor
     */
    protected void unconfigureEditor() {
        if (focusListener != null) {
            editor.removeFocusListener(focusListener);
        }

        editor.removeFocusListener(getHandler());
        datePicker.getEditor().removeActionListener(getHandler());
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected KeyListener createKeyListener() {
        return getHandler();
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    /**
     * Creates a layout manager for managing the components which make up the
     * date picker.
     * 
     * @return an instance of a layout manager
     */
    protected LayoutManager createLayoutManager() {
        return getHandler();
    }

    protected DatePickerEditor createEditor() {
        return new BasicDatePickerEditor.UIResource();
    }

    /**
     * Creates the popup portion of the combo box.
     * 
     * @return an instance of <code>ComboPopup</code>
     * @see ComboPopup
     */
    protected DatePickerPopup createPopup() {
        return new BasicDatePickerPopup(datePicker);
    }

    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * Gets the insets from the JComboBox.
     */
    protected Insets getInsets() {
        return datePicker.getInsets();
    }

    /**
     * Returns the area that is reserved for drawing the currently selected
     * item.
     */
    protected Rectangle rectangleForCurrentValue() {
        int width = datePicker.getWidth();
        int height = datePicker.getHeight();
        Insets insets = getInsets();
        int buttonSize = height - (insets.top + insets.bottom);
        if (arrowButton != null) {
            buttonSize = arrowButton.getWidth();
        }
        if (datePicker.getComponentOrientation().isLeftToRight()) {
            return new Rectangle(insets.left, insets.top, width
                    - (insets.left + insets.right + buttonSize), height
                    - (insets.top + insets.bottom));
        } else {
            return new Rectangle(insets.left + buttonSize, insets.top, width
                    - (insets.left + insets.right + buttonSize), height
                    - (insets.top + insets.bottom));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPopupVisible(JXDatePicker c) {
        return popup.isVisible();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPopupVisible(JXDatePicker c, boolean v) {
        if (v) {
            popup.show();
        } else {
            popup.hide();
        }
    }

    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }

    /**
     * The minumum size is the size of the display area plus insets plus the
     * button.
     */
    public Dimension getMinimumSize(JComponent c) {
        if (!isMinimumSizeDirty) {
            return new Dimension(cachedMinimumSize);
        }
        Dimension size = getDisplaySize();
        Insets insets = getInsets();
        size.height += insets.top + insets.bottom;
        int buttonSize = size.height - (insets.top + insets.bottom);
        size.width += insets.left + insets.right + buttonSize;

        cachedMinimumSize.setSize(size.width, size.height);
        isMinimumSizeDirty = false;

        return new Dimension(size);
    }

    public Dimension getPreferredSize(JComponent c) {
        return getMinimumSize(c);
    }

    /**
     * Determines if the JComboBox is focus traversable. If the JComboBox is
     * editable this returns false, otherwise it returns true.
     */
    public boolean isFocusTraversable(JXDatePicker c) {
        return !datePicker.isEditable();
    }

    protected Dimension getDisplaySize() {
        // TODO cache? is it necessary? we're not as heavy as combo
        return new Dimension(100, 25);
    }

    // Syncronizes the ToolTip text for the components within the combo box to
    // be the
    // same value as the combo box ToolTip text.
    private void updateToolTipTextForChildren() {
        Component[] children = datePicker.getComponents();
        for (int i = 0; i < children.length; ++i) {
            if (children[i] instanceof JComponent) {
                ((JComponent) children[i]).setToolTipText(datePicker
                        .getToolTipText());
            }
        }
    }

    public void paint(Graphics g, JComponent c) {
        hasFocus = datePicker.isFocusOwner();
    }

    public void uninstallUI(JComponent c) {
        setPopupVisible(datePicker, false);
        // popup.uninstallingUI();

        // uninstallKeyboardActions();

        datePicker.setLayout(null);

        uninstallComponents();
        uninstallListeners();
        uninstallDefaults();

        DatePickerEditor datePickerEditor = datePicker.getEditor();
        if (datePickerEditor instanceof UIResource) {
            if (datePickerEditor.getEditorComponent().isFocusOwner()) {
                // Leave focus in JComboBox.
                datePicker.requestFocusInWindow();
            }
            datePicker.setEditor(null);
        }

        // if (keySelectionManager instanceof UIResource) {
        // comboBox.setKeySelectionManager(null);
        // }

        handler = null;
        keyListener = null;
        focusListener = null;
        propertyChangeListener = null;
        popup = null;
        monthBox = null;
        datePicker = null;
    }

    protected void uninstallComponents() {
        if (arrowButton != null) {
            unconfigureArrowButton();
        }
        if (editor != null) {
            unconfigureEditor();
        }
        datePicker.removeAll(); // Just to be safe.
        arrowButton = null;
    }

    private void unconfigureArrowButton() {
        if (arrowButton != null) {
            arrowButton.removeMouseListener(popup.getMouseListener());
        }
    }

    protected void uninstallListeners() {
        if (keyListener != null) {
            datePicker.removeKeyListener(keyListener);
        }
        if (propertyChangeListener != null) {
            datePicker.removePropertyChangeListener(propertyChangeListener);
        }
        if (focusListener != null) {
            datePicker.removeFocusListener(focusListener);
        }
        if (popupMouseListener != null) {
            datePicker.removeMouseListener(popupMouseListener);
        }
        if (popupMouseMotionListener != null) {
            datePicker.removeMouseMotionListener(popupMouseMotionListener);
        }
        if (popupKeyListener != null) {
            datePicker.removeKeyListener(popupKeyListener);
        }
    }

    protected void uninstallDefaults() {
        LookAndFeel.installColorsAndFont(datePicker, "JXDatePicker.background",
                "JXDatePicker.foreground", "JXDatePicker.font");
        LookAndFeel.uninstallBorder(datePicker);
    }
}
