
/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */ 

package org.jdesktop.appframework;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationAction;
import org.jdesktop.application.ApplicationContext;



/**
 * 
 * My misconception of what proxy action is for ... <p>
 * 
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
public class ProxyExample extends Application {
    private static final Logger LOG = Logger.getLogger(ProxyExample.class
            .getName());
    private JFrame appFrame = null;
    private JTextField textField = null;
    private boolean clearEnabled = false;

    @Action public void setTitle() {
	appFrame.setTitle(textField.getText());
	setClearEnabled(true);
    }

    @Action(enabledProperty = "clearEnabled")
    public void clearTitle() {
	appFrame.setTitle("");
	setClearEnabled(false);
    }

    public boolean isClearEnabled() {
	return clearEnabled;
    }

    /**
     * this needs to be private to prevent accidental setting from the
     * force-clearEnabled action. doohh.. not longer part of this.. and
     * the proxy is still 
     * 
     * @param clearEnabled
     */
    private void setClearEnabled(boolean clearEnabled) {
	boolean oldValue = this.clearEnabled;
	this.clearEnabled = clearEnabled;
	firePropertyChange("clearEnabled", oldValue, this.clearEnabled);
    }

    @Action
    public void standIn() {
        
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
        clearTitleButton.setAction(actionMap.get("clearTitle"));
	textField.setAction(actionMap.get("setTitle"));
        ApplicationAction appAction = (ApplicationAction) actionMap.get("standIn");
        appAction.setProxy(actionMap.get("clearTitle"));
        buttonPanel.add(new JButton(appAction));
	appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	appFrame.pack();
	appFrame.setLocationRelativeTo(null);
	appFrame.setVisible(true); 
    }

    public static void main(String[] args) {
        Application.launch(ProxyExample.class, args);
    }
} 
