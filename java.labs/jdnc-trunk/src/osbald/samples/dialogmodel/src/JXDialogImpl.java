package uk.co.osbald.swingx;

import org.jdesktop.swingx.action.BoundAction;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
 * Bit of a guess as to where JXDialog might go next..
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 10:50:57
 */

public class JXDialogImpl extends JDialog {

    static {
        // Hack to enforce loading of SwingX framework ResourceBundle
        LookAndFeelAddons.getAddon();
    }

    public static final String EXECUTE_ACTION_COMMAND = "execute";
    public static final String CLOSE_ACTION_COMMAND = "close";
    public static final String APPLY_ACTION_COMMAND = "apply";
    public static final String UIPREFIX = "XDialog.";
    private static final List<String> DEFAULT_BUTTONS = Arrays.asList(
            EXECUTE_ACTION_COMMAND,
            CLOSE_ACTION_COMMAND,
            APPLY_ACTION_COMMAND);

    protected JComponent content;
    protected JComponent buttonPanel;

    public JXDialogImpl(Frame frame, JComponent content) {
        this(frame, content.getName(), content);
    }

    public JXDialogImpl(Frame frame, String title, JComponent content) {
        super(frame, title, true);
        setContent(content);
    }

    public JXDialogImpl(Dialog dialog, JComponent content) {
        this(dialog, content.getName(), content);
    }

    public JXDialogImpl(Dialog dialog, String title, JComponent content) {
        super(dialog, title, true);
        setContent(content);
    }

    private void setContent(JComponent content) {
        if (this.content != null) {
            throw new IllegalStateException("content must not be set more than once");
        }

        initActions();
        ActionMap actionMap = content.getActionMap();
        Object[] keys = actionMap != null ? actionMap.keys() : new Object[0];
        for (int i = 0; keys != null && i < keys.length; i++) {
            putAction(keys[i], actionMap.get(keys[i]));
        }

        this.content = content;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle(content.getName());
        build();
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * pre: content != null.
     */
    private void build() {
        JComponent contentBox = new Box(BoxLayout.PAGE_AXIS);
        contentBox.add(content);
        this.buttonPanel = createButtonPanel();
        contentBox.add(buttonPanel);
        contentBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
//        content.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//        fieldPanel.setAlignmentX();
//        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(contentBox);
    }

    public void setVisible(boolean visible) {
        if (content == null) throw
                new IllegalStateException("content must be built before showing the dialog");
        super.setVisible(visible);
    }

    public void doClose() {
        dispose();
    }

    private void initActions() {
        // PENDING: factor a common dialog containing the following
        Action defaultAction = createCloseAction();
        putAction(CLOSE_ACTION_COMMAND, defaultAction);
    }

    private Action createCloseAction() {
        String actionName = getUIString(CLOSE_ACTION_COMMAND);
        BoundAction action = new BoundAction(actionName, CLOSE_ACTION_COMMAND);
        action.registerCallback(this, "doClose");
        return action;
    }

    /**
     * create the dialog button controls.
     *
     * @return panel containing button controls
     */
    protected JComponent createButtonPanel() {
        // PENDING: this is a hack until we have a dedicated ButtonPanel!
        JPanel panel = new JPanel(new BasicOptionPaneUI.ButtonAreaLayout(true, 6)) {
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }
        };

        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        List<Action> actions = getAvailibleActions(DEFAULT_BUTTONS);
        for (int i = 0; i < actions.size(); i++) {
            Action action = actions.get(i);
            JButton button = new JButton(action);
            panel.add(button);
            if (i == 0)
                getRootPane().setDefaultButton(button);
        }

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);

        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(enterKey, EXECUTE_ACTION_COMMAND);
        inputMap.put(escapeKey, CLOSE_ACTION_COMMAND);

        return panel;
    }

    /**
     * convenience wrapper to access rootPane's actionMap.
     *
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
     * tries to find a String value from the UIManager, prefixing the
     * given key with the UIPREFIX.
     * <p/>
     * TODO: move to utilities?
     *
     * @param key
     * @return the String as returned by the UIManager or key if the returned
     *         value was null.
     */
    private String getUIString(String key) {
        String text = UIManager.getString(UIPREFIX + key);
        return text != null ? text : key;
    }

    public JComponent getContent() {
        return content;
    }

    public JComponent getButtonPanel() {
        return buttonPanel;
    }

    public Border getBorder() {
        return getContent().getBorder();
    }

    public void setBorder(Border border) {
        getContent().setBorder(border);
    }

    private List<Action> getAvailibleActions(List order) {
        List<Action> availible = new ArrayList<Action>();
        Action action;
        for (Iterator it = order.iterator(); it.hasNext();) {
            String name = (String) it.next();
            if ((action = getAction(name)) != null) {
                availible.add(action);
            }
        }
        return availible;
    }
}