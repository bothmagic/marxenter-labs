/*
 * $Id: JXDialog2.java 2578 2008-07-29 23:16:17Z osbald $
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
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicOptionPaneUI;

import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.util.OS;
import org.jdesktop.swingx.HorizontalLayout;

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
 * @deprecated RJO: experimental test of how far I could push a custom JXDialog subclass
 */

public class JXDialog2 extends org.jdesktop.swingx.JXDialog {

    public static final String OK_ACTION_COMMAND = EXECUTE_ACTION_COMMAND;
    public static final String CANCEL_ACTION_COMMAND = CLOSE_ACTION_COMMAND;
    public static final String APPLY_ACTION_COMMAND = "apply";

    static final List<String> DEFAULT_BUTTONS = Arrays.asList(
            OK_ACTION_COMMAND,
            CANCEL_ACTION_COMMAND,
            APPLY_ACTION_COMMAND);

    public JXDialog2(JComponent content) {
        super(content);
        setLocationRelativeTo(null);
    }
    
    public JXDialog2(Frame frame, JComponent content) {
        super(frame, content);
        setLocationRelativeTo(frame);
    }

    public JXDialog2(Dialog dialog, JComponent content) {
        super(dialog, content);
        setLocationRelativeTo(dialog);
    }

    public JXDialog2(Frame frame, JComponent content, String title) {
        super(frame, content);
        setTitle(title);
        setLocationRelativeTo(frame);
    }

    public JXDialog2(Dialog dialog, JComponent content, String title) {
        super(dialog, content);
        setTitle(title);
        setLocationRelativeTo(dialog);
    }

    @Override
    protected void setTitleFromContent() {
        if (content != null) {
            if ((getTitle() == null || getTitle().length() == 0)
                    && content.getName() != null) {
                setTitle(content.getName());
            }
        }
    }

    /*
    private void build() {
        JComponent contentBox = new Box(BoxLayout.PAGE_AXIS);
        contentBox.add(content);
        JComponent buttonPanel = createButtonPanel();
        contentBox.add(buttonPanel);
        contentBox.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        add(contentBox);

    }

    private void build() {
        // RJO: Couldn't tease the alignment with resizing from the pre-exiting layout
        getContentPane().setLayout(new GridBagLayout());
        add(content, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JComponent buttonPanel = createButtonPanel();
        int location = buttonPanel.getAlignmentX() ==
                Component.RIGHT_ALIGNMENT ? GridBagConstraints.EAST : GridBagConstraints.CENTER;
        add(buttonPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, location,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        pack();
    }

    private void initActions() {
        Action defaultAction = createCloseAction();
        putAction(CLOSE_ACTION_COMMAND, defaultAction);
        //putAction(EXECUTE_ACTION_COMMAND, defaultAction);
    }
    */

    @Override
    protected JComponent createButtonPanel() {
        // PENDING: this is a hack until we have a dedicated ButtonPanel!
        // PENDING: what about swinghelpers JXButtonPanel? (adds cursor key support)
        JPanel panel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 6)) {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };
        panel.setLayout(new HorizontalLayout(5));
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

        panel.setBorder(new LineBorder(Color.BLUE, 1));
        //? panel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_BORDER_SIZE, 0, 0, 0));

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(enterKey, EXECUTE_ACTION_COMMAND);
        inputMap.put(escapeKey, CLOSE_ACTION_COMMAND);

        // having a default button will cause dialog to exit on first <enter> key press which is awkward for input
        // dialogs. Logic dictates its less likely to be input form if there's only one action ~ yes this is sucky
        if (buttons.size() == 1) {
            getRootPane().setDefaultButton(buttons.get(0));
        }
        return panel;
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

    protected Action getAction(Object key) {
        return getRootPane().getActionMap().get(key);
    }

    /*
    public Border getBorder() {
        return ((JComponent) getContentPane()).getBorder();
    }

    public void setBorder(Border border) {
        ((JComponent) getContentPane()).setBorder(border);
    }

    public Border getContentBorder() {
        return getContent().getBorder();
    }

    public void setContentBorder(Border border) {
        getContent().setBorder(border);
    }
    */
}
