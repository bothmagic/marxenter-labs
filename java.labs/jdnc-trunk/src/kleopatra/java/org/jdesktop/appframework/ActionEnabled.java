
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */ 

package org.jdesktop.appframework;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;



/**
 * Added button to show "binding" to target enabled property: updating
 * the action's enabled synchs the target enabled (if the setter is public,
 * which it shouldn't maybe?)
 * 
 * Copied from ActionExample3. <p>
 * The {@code enabledProperty} {@code @Action} annotation parameter.
 * <p>
 * This example is nearly identical to {@link ActionExample1 ActionExample1}.
 * We've added a parameter to the {@code @Action} annotation for the
 * {@code clearTitle} action:
 * <pre>
 * &#064;Action(enabledProperty = "clearEnabled")
 * public void clearTitle() { 
 *     appFrame.setTitle(textField.getText());
 *     setClearEnabled(true);
 * }
 * </pre>
 * The annotation parameter names a bound property from the same class.
 * When the {@code clearEnabled} property is set to false, as it is after
 * the window's title has been cleared, the {@code clearTitle} 
 * {@code Action} is disabled.
 * 
 * @author Hans Muller (Hans.Muller@Sun.COM)
 */
public class ActionEnabled extends Application {
    private static final Logger LOG = Logger.getLogger(ActionEnabled.class
            .getName());
    private JFrame appFrame = null;
    private JTextField textField = null;
    private boolean clearEnabled = false;
    private static final String CLEAR_ENABLED = "clearEnabled";

    @Action public void setTitle() {
	appFrame.setTitle(textField.getText());
	setClearEnabled(true);
    }

    @Action(enabledProperty = CLEAR_ENABLED)
    public void clearTitle() {
	appFrame.setTitle("");
	setClearEnabled(false);
    }

    public boolean isClearEnabled() {
	return clearEnabled;
    }

    /**
     * Here we have a unwanted side-effect: as it is public (== full read/write) bound
     * property the enabled can be forced to get out-of-synch with the model state.
     * That's because the action.enabled is enforced as a bi-directional synch instead
     * of read-only. <p>
     * 
     * To workaround: make this private to have a read-only bound property.
     * 
     * @param clearEnabled
     */
    public void setClearEnabled(boolean clearEnabled) {
	boolean oldValue = this.clearEnabled;
	this.clearEnabled = clearEnabled;
	firePropertyChange("clearEnabled", oldValue, this.clearEnabled);
    }

    @Override protected void startup() {
	appFrame = new JFrame("");
	textField = new JTextField("<Enter the window title here>");
        textField.setFont(new Font("LucidSans", Font.PLAIN, 32));
	JButton clearTitleButton = new JButton("Set Window Title");
	JButton setTitleButton = new JButton("Clear Window Title");
	JPanel buttonPanel = new JPanel();
	buttonPanel.add(setTitleButton);
	buttonPanel.add(clearTitleButton);
	appFrame.add(textField, BorderLayout.CENTER);
	appFrame.add(buttonPanel, BorderLayout.SOUTH);
	
	/* Lookup up the Actions for this class/object in the
	 * ApplicationContext, and bind them to the GUI controls.
	 */
	ApplicationContext app = getContext();
	ActionMap actionMap = app.getActionMap();
	setTitleButton.setAction(actionMap.get("setTitle"));
	textField.setAction(actionMap.get("setTitle"));
	final javax.swing.Action action = actionMap.get("clearTitle");
    clearTitleButton.setAction(action);
        javax.swing.Action forceEnable = new javax.swing.AbstractAction("force clearEnabled") {

            public void actionPerformed(ActionEvent arg0) {
                action.setEnabled(true);
            }
            
        };
        buttonPanel.add(new JButton(forceEnable));
	appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	appFrame.pack();
	appFrame.setLocationRelativeTo(null);
	appFrame.setVisible(true); 
    }

    public static void main(String[] args) {
        Application.launch(ActionEnabled.class, args);
    }
} 
