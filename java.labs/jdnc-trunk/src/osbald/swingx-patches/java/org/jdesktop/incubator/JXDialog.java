/*
 * $Id: JXDialog.java 2773 2008-10-10 11:27:59Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.incubator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.BorderUIResource;

import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.util.OS;

/**
 * First cut for enhanced Dialog. The idea is to have a pluggable content
 * from which the dialog auto-configures all its "dialogueness".
 *
 * <ul>
 * <li> accepts a content and configures itself from content's properties -
 *  replaces the execute action from the appropriate action in content's action map (if any)
 *  and set's its title from the content's name.
 * <li> registers stand-in actions for close/execute with the dialog's RootPane
 * <li> registers keyStrokes for esc/enter to trigger the close/execute actions
 * <li> takes care of building the button panel using the close/execute actions.
 * </ul>
 *
 * <ul>
 * <li>TODO: add link to forum discussion, wiki summary?
 * <li>PENDING: add support for vetoing the close.
 * <li>PENDING: add complete set of constructors
 * <li>PENDING: add windowListener to delegate to close action
 * </ul>
 *
 * @author Jeanette Winzenburg
 */

/* 
 * Notes
 *
 * RJO: MY version is mostly a copy of the existing JXDialog, but with a few changes to
 * constructors, border and button panel layout to make it look more like native dialogs.
 * In particular I didn't like the way the existing BoxLayout resizes (try a JLabel as content).
 *
 * Lots of questions on customising the dialog for general use. DefaultButton
 * for instance causes problems as enter in any contained sub-components will
 * commit the dialog.
 *
 * Probably need UI delegates to boot was Windows has the buttons
 * right aligned and OSX reverses their order.
 */

public class JXDialog extends JDialog {

    static {
        // Hack to enforce loading of SwingX framework ResourceBundle
        LookAndFeelAddons.getAddon();

        //TODO RJO: shift these into Addon (when created and if useful)
        if (OS.isMacOSX()) {
            UIManager.getDefaults().put("JXDialog.border",
                    new BorderUIResource(BorderFactory.createEmptyBorder(14, 14, 14, 14)));
            UIManager.getDefaults().put("JXDialog.buttonPanelBorder",
                    new BorderUIResource(BorderFactory.createEmptyBorder(14, 0, 0, 0)));
        } else {
            UIManager.getDefaults().put("JXDialog.border",
                    new BorderUIResource(BorderFactory.createEmptyBorder(7, 7, 7, 7)));
            UIManager.getDefaults().put("JXDialog.buttonPanelBorder",
                    new BorderUIResource(BorderFactory.createEmptyBorder(7, 0, 0, 0)));
        }
    }

    public static final String EXECUTE_ACTION_COMMAND = "execute";
    public static final String CLOSE_ACTION_COMMAND = "close";
    public static final String UIPREFIX = "XDialog.";

    public static final String OK_ACTION_COMMAND = EXECUTE_ACTION_COMMAND;
    public static final String CANCEL_ACTION_COMMAND = CLOSE_ACTION_COMMAND;
    public static final String APPLY_ACTION_COMMAND = "apply";

    static final List<String> DEFAULT_BUTTONS = Arrays.asList(
            OK_ACTION_COMMAND,
            CANCEL_ACTION_COMMAND,
            APPLY_ACTION_COMMAND);

    protected JComponent content;

    /**
     * Creates a non-modal dialog with the given component as 
     * content and without specified owner.  A shared, hidden frame will be
     * set as the owner of the dialog.
     * <p>
     * @param content the component to show and to auto-configure from.
     */
    public JXDialog(JComponent content) {
        super();
        setContent(content);
        setLocationRelativeTo(null);
    }
    
    /**
     * Creates a non-modal dialog with the given component as content and the
     * specified <code>Frame</code> as owner.
     * <p>
     * @param frame the owner
     * @param content the component to show and to auto-configure from.
     */
    public JXDialog(Frame frame, JComponent content) {
        super(frame);
        setContent(content);
        setLocationRelativeTo(null);
    }

    /**
     * Creates a non-modal dialog with the given component as content and the
     * specified <code>Dialog</code> as owner.
     * <p>
     * @param dialog the owner
     * @param content the component to show and to auto-configure from.
     */
    public JXDialog(Dialog dialog, JComponent content) {
        super(dialog);
        setContent(content);
        setLocationRelativeTo(dialog);
    }

    public JXDialog(Frame frame, String title, JComponent content) {
        super(frame, title, true);
        setContent(content);
        setLocationRelativeTo(frame);
    }

    public JXDialog(Dialog dialog, String title, JComponent content) {
        super(dialog, title, true);
        setContent(content);
        setLocationRelativeTo(dialog);
    }

    /**
     * PENDING: widen access - this could be public to make the content really 
     * pluggable?
     * 
     * @param content
     */
    protected void setContent(JComponent content) {
        if (this.content != null) {
            throw new IllegalStateException("content must not be set more than once");
        }
        initActions();
        Action contentCloseAction = content.getActionMap().get(CLOSE_ACTION_COMMAND);
        if (contentCloseAction != null) {
            putAction(CLOSE_ACTION_COMMAND, contentCloseAction);
        }
        Action contentExecuteAction = content.getActionMap().get(EXECUTE_ACTION_COMMAND);
        if (contentExecuteAction != null) {
            putAction(EXECUTE_ACTION_COMMAND, contentExecuteAction);
        }
        this.content = content;
        build();
        setTitleFromContent();
    }

    /**
     * Infers and sets this dialog's title from the the content. 
     * Does nothing if content is null. 
     * 
     * Here: uses the content's name as title. 
     */
    protected void setTitleFromContent() {
        if (content != null && content.getName() != null) {
            setTitle(content.getName());
        }
    }

    /**
     * pre: content != null.
     *
     */
    private void build() {
        // RJO: Couldn't tease the alignment with resizing from the pre-exiting layout
        getContentPane().setLayout(new GridBagLayout());
        ((JComponent)getContentPane()).setBorder(getDialogBorder());
        add(content, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JComponent buttonPanel = createButtonPanel();
        int location = buttonPanel.getAlignmentX() ==
                Component.RIGHT_ALIGNMENT ? GridBagConstraints.EAST : GridBagConstraints.CENTER;
        add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, location,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pack();
    }

    /**
     * {@inheritDoc}
     * 
     * Overridden to check if content is available. <p>
     * PENDING: doesn't make sense - the content is immutable and guaranteed
     * to be not null.
     */
    @Override
    public void setVisible(boolean visible) {
        if (content == null) throw
            new IllegalStateException("content must be built before showing the dialog");
        super.setVisible(visible);
    }

//------------------------ dynamic locale support
    

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the content's Locale and then updated
     * this dialog's internal state. <p>
     * 
     * 
     */
    @Override
    public void setLocale(Locale l) {
        /*
         * NOTE: this is called from super's constructor as one of the
         * first methods (prior to setting the rootPane!). So back out
         * 
         */  
        if (content != null) {
            content.setLocale(l);
            updateLocaleState(l);
        }
        super.setLocale(l);
    }
    
    /**
     * Updates this dialog's locale-dependent state.
     * 
     * Here: updates title and actions.
     * <p>
     * 
     * 
     * @see #setLocale(Locale)
     */
    protected void updateLocaleState(Locale locale) {
        setTitleFromContent();
        for (Object key : getRootPane().getActionMap().allKeys()) {
            if (key instanceof String) {
                Action contentAction = content.getActionMap().get(key);
                Action rootPaneAction = getAction(key);
                if ((!rootPaneAction.equals(contentAction))) {
                    String keyString = getUIString((String) key, locale);
                    if (!key.equals(keyString)) {
                        rootPaneAction.putValue(Action.NAME, keyString);
                    }
                }
            }
        }
    }
    
    /**
     * The callback method executed when closing the dialog. <p>
     * Here: calls dispose. 
     *
     */
    public void doClose() {
        dispose();
    }

    private void initActions() {
        Action defaultAction = createCloseAction();
        putAction(CLOSE_ACTION_COMMAND, defaultAction);
        //putAction(EXECUTE_ACTION_COMMAND, defaultAction);
    }

    private Action createCloseAction() {
        String actionName = getUIString(CLOSE_ACTION_COMMAND);
        BoundAction action = new BoundAction(actionName,
                CLOSE_ACTION_COMMAND);
        action.registerCallback(this, "doClose");
        return action;
    }

    /**
     * create the dialog button controls.
     *
     *
     * @return panel containing button controls
     */
    protected JComponent createButtonPanel() {
        // PENDING: this is a hack until we have a dedicated ButtonPanel!
        // PENDING: what about swinghelpers JXButtonPanel? (adds cursor key support)
        JPanel panel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 6)) {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        panel.setBorder(getButtonPanelBorder());
        panel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // RJO: if we've got an execute should be paired with cancel rather than close
        if (getAction(EXECUTE_ACTION_COMMAND)!=null) {
            getAction(CLOSE_ACTION_COMMAND).putValue(Action.NAME, getUIString("cancel"));
        }

        List<Action> actions = getAvailableActions(DEFAULT_BUTTONS);
        List<JButton> buttons = new ArrayList<JButton>();
        //PENDING RJO: emulate platforms preferred button order (UI delegates should do this)
        //TODO windows min button width? 75px? etc..
        if (OS.isMacOSX()) {
            for (int i = actions.size()-1; i>=0; i--) {
                JButton button = new JButton(actions.get(i));
                panel.add(button);
                buttons.add(button);
            }
        } else {
            for (int i = 0; i < actions.size(); i++) {
                JButton button = new JButton(actions.get(i));
                panel.add(button);
                buttons.add(button);
            }
        }

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(enterKey, EXECUTE_ACTION_COMMAND);
        inputMap.put(escapeKey, CLOSE_ACTION_COMMAND);

        // RJO: activate default if only one active button (stops closing dialog on first <enter>)
        // logic dictates its less likely to be input form if there's only one action ~ yes this is sucky
        //btn if (buttons.size() == 1) {
            getRootPane().setDefaultButton(buttons.get(0));
        //btn }
        return panel;
    }

    /**
     * convenience wrapper to access rootPane's actionMap.
     * @param key
     * @param action
     */
    private void putAction(Object key, Action action) {
        getRootPane().getActionMap().put(key, action);
    }

    /**
     * convenience wrapper to access rootPane's actionMap.
     *
     * @param key
     * @return root pane's <code>ActionMap</code>
     */
    private Action getAction(Object key) {
        return getRootPane().getActionMap().get(key);
    }

    /**
     * Returns a potentially localized value from the UIManager. The given key
     * is prefixed by this component|s <code>UIPREFIX</code> before doing the
     * lookup. The lookup respects this table's current <code>locale</code>
     * property. Returns the key, if no value is found.
     *
     * @param key the bare key to look up in the UIManager.
     * @return the value mapped to UIPREFIX + key or key if no value is found.
     */
    protected String getUIString(String key) {
        return getUIString(key, getLocale());
    }

    /**
     * Returns a potentially localized value from the UIManager for the 
     * given locale. The given key
     * is prefixed by this component's <code>UIPREFIX</code> before doing the
     * lookup. Returns the key, if no value is found.
     * 
     * @param key the bare key to look up in the UIManager.
     * @param locale the locale use for lookup
     * @return the value mapped to UIPREFIX + key in the given locale,
     *    or key if no value is found.
     */
    protected String getUIString(String key, Locale locale) {
        String text = UIManagerExt.getString(UIPREFIX + key, locale);
        return text != null ? text : key;
    }

    Border getDialogBorder() {
        //PENDING Hack lack of addon (L&F change purges UIDefaults)
        Border border = UIManager.getDefaults().getBorder("JXDialog.border");
        return border != null ? border : BorderFactory.createEmptyBorder(7, 7, 7, 7);
    }

    Border getButtonPanelBorder() {
        //PENDING Hack lack of addon (L&F change purges UIDefaults)
        Border border = UIManager.getDefaults().getBorder("JXDialog.buttonPanelBorder");
        return border != null ? border : BorderFactory.createEmptyBorder(7, 0, 0, 0);
    }

    JComponent getContent() {
        return content;
    }

    protected List<Action> getAvailableActions(List order) {
        List<Action> available = new ArrayList<Action>();
        for (Object key : order) {
            Action action = getAction(key);
            if (action != null) {
                available.add(action);
            }
        }
        return available;
    }
}
