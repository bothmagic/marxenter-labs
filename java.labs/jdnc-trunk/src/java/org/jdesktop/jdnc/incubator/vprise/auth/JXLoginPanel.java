package org.jdesktop.jdnc.incubator.vprise.auth;

/*
 * $Id: JXLoginPanel.java 516 2005-05-29 08:18:37Z vprise $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import org.jdesktop.swing.JXImagePanel;

// Doesn't compile in incubator should be enabled with JDNC
import org.jdesktop.swingx.util.WindowUtils;

/**
 *  <p>JXLoginPanel is a JPanel that implements a Login dialog with
 *  support for saving passwords supplied for future use in a secure
 *  manner. It is intended to work with <strong>LoginService</strong>
 *  and <strong>PasswordStore</strong> to implement the
 *  authentication.</p>
 *
 *  <p> In order to perform the authentication, <strong>JXLoginPanel</strong>
 *  calls the <code>authenticate</code> method of the <strong>LoginService
 *  </strong>. In order to perform the persistence of the password,
 *  <strong>JXLoginPanel</strong> calls the put method of the
 *  <strong>PasswordStore</strong> object that is supplied. If
 *  the <strong>PasswordStore</strong> is <code>null</code>, then the password
 *  is not saved. Similarly, if a <strong>PasswordStore</strong> is
 *  supplied and the password is null, then the <strong>PasswordStore</strong>
 *  will be queried for the password using the <code>get</code> method.
 *
 * Changes by Shai:
 * Clarified the save mode a little bit including hiding the save checkbox when there
 * is no password store.
 * Changed the class to derive from JXImagePanel to make customization easier (need to
 * check my ImagePanel which has some technical advantages).
 * Removed the static keyword from the ok/cancel buttons since this can cause an issue
 * with more than one login dialogs (yes its an unlikely situation but documenting this
 * sort of behavior or dealing with one bug resulting from this can be a real pain!).
 * Allowed the name field to be represented as a text field when there is no password store.
 * Rewrote the layout code to mostly work with a single container.
 * Removed additional dialogs for progress and error messages and incorporated their 
 * functionality into the main dialog.
 * Allowed for an IOException with a message to be thrown by the login code. This message
 * is displayed to the user when the login is stopped.
 * Removed repetetive code and moved it to a static block.
 * i18n converted some of the strings that were not localized.
 *
 * @author Bino George
 * @author Shai Almog
 */

public class JXLoginPanel extends JXImagePanel {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3544949969896288564L;

    /**
     * This is the area in which the name field is placed. That way it can toggle on the fly 
     * between text field and a combo box depending on the situation.
     */
    private JPanel nameArea = new JPanel(new BorderLayout());
    
	private JLabel nameLabel;

	private JLabel passwordLabel;

	private JComboBox nameCombo;
	private JTextField nameField;

	private String username;

	private JPasswordField passwordField;

	private JCheckBox save;

	private JComponent header;

	private JComponent label;

	private JButton okButton;

	// Cancel Button on the login dialog
	private JButton cancelButton;

	private JPanel savePanel;

	private JPanel headerPanel;

    private JPanel loginPanel;
    
	private LoginService loginService;

	private PasswordStore passwordStore;

	private UsernameStore usernameStore;
    
    /**
     * This is the progress indicator which is a part of the login dialog
     */
    private JProgressBar progressIndicator;
    
    /**
     * Here login messages are displayed next to the progress indicator
     */
    private JLabel loginProgress;

    /**
     * This button can be used to cancel a login that is in progress
     */
    private JButton cancelLogin;

	private Handler handler = new Handler();

	//TODO This belongs in Application or some other class.
	private static HashMap<String, Object> _resources;

	/**
	 *  Whether to save password or username ?
	 */
	private int saveMode;

	/**
	 * Dont save anything.
	 */
	public static final int SAVE_NONE = 0;

	/**
	 * Save the password using PasswordStore
	 */
	public static final int SAVE_PASSWORD = 1;

	/**
	 * Save the username in the Preferences node for this class.
	 */
	public static final int SAVE_USERNAME = 2;

    private static final String CLASS_NAME;
    
	/**
	 * Creates a default JXLoginPanel instance
	 */
	
	static {
	
		// Popuplate UIDefaults with the localisable Strings we will use
		// in the Login panel.
		
		String keys[] = {  "okString", "okString.mnemonic", 
                                   "cancelString", "cancelString.mnemonic",
                                    "nameString", "loginString",
				"passwordString","rememberUserString", 
				"rememberPasswordString", "loggingInString", 
                "loginIntructionString", "loginFailed" };
		
		CLASS_NAME = JXLoginPanel.class.getCanonicalName();
		String lookup;
		for (String key : keys) {
			lookup = CLASS_NAME + "." + key;
			if (UIManager.getString(lookup) == null) {
				UIManager.put(lookup, getResourceAsObject(key));
			}
		}
	}

    
	public JXLoginPanel() {
		this(null, null, null, null, null, null);
	}

	/**
	 * Creates a JXLoginPanel with the supplied parameters
	 *
	 * @param name Name, can be null.
	 * @param password Password, can be null.
	 * @param service an Object that implements LoginService
	 * @param store an Object that implements Password store, can be null.
	 */
	public JXLoginPanel(String name, String password, LoginService service,
			PasswordStore store) {
		this(name, password, service, store, null, null);
	}

	/**
	 * Creates a JXLoginPanel with the supplied parameters
	 *
	 * @param name Name, can be null.
	 * @param password Password, can be null.
	 * @param service an Object that implements LoginService
	 * @param store an Object that implements Password store, can be null.
	 * @param header a JComponent to use as the header, can be null.
	 * @paran label a JComponent to use as the label, can be null.
	 */
	public JXLoginPanel(String name, String password, LoginService service,
			PasswordStore store, JComponent header, JComponent label) {
		loginService = service;
		passwordStore = store;
		this.username = name;
		this.header = header;
		this.label = label;
		service.addLoginListener(new SaveListener());
		init(name, password);
	}

	void init(String nameStr, String passwordStr) {
		usernameStore = UsernameStore.getUsernameStore();
		if (username != null) {
			usernameStore.addUsername(username);
		}
		setLayout(new BorderLayout(5, 5));
		add(createLoginPanel(nameStr, passwordStr), BorderLayout.CENTER);
		add(headerPanel, BorderLayout.NORTH);
	}
    
    /**
     * Includes code to layout the dialog container 
     */
    private void layoutLoginPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        panel.add(loginProgress, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(nameLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(nameArea, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(passwordLabel, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(passwordField, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 4);
        panel.add(progressIndicator, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(cancelLogin, gridBagConstraints);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        panel.add(savePanel, gridBagConstraints);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);

        buttonPanel.add(cancelButton);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        panel.add(buttonPanel, gridBagConstraints);        
    }
    
	private JPanel createLoginPanel(String nameStr, String passwordStr) {
        loginPanel = new JPanel();

        nameLabel = new JLabel(UIManager.getString(CLASS_NAME + ".nameString"));
        passwordLabel = new JLabel(UIManager.getString(CLASS_NAME + ".passwordString"));

		savePanel = new JPanel();
		headerPanel = new JPanel();

		headerPanel.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
        
        nameField = new JTextField(nameStr, 15);
        nameField.setEditable(true);
        nameField.addActionListener(handler);
        nameField.addFocusListener(handler);
        nameArea.add(nameField, BorderLayout.CENTER);
        nameCombo = new JComboBox(usernameStore.getUsernames());
        nameCombo.setEditable(true);
        nameCombo.addActionListener(handler);
        nameCombo.addFocusListener(handler);

		passwordField = new JPasswordField(passwordStr, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		if (header != null) {
			gbc.insets = new Insets(0, 0, 0, 0);
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			headerPanel.add(header, gbc);
			gbc.gridy++;
		}
		if (label != null) {
			gbc.insets = new Insets(12, 12, 0, 0);
			headerPanel.add(label, gbc);
		}

        progressIndicator = new JProgressBar();
        loginProgress = new JLabel(UIManager.getString(CLASS_NAME + ".loginIntructionString"));
        cancelLogin = new JButton(UIManager.getString(CLASS_NAME + ".cancelString"));
        cancelLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                loginService.cancelAuthentication();
                progressIndicator.setIndeterminate(false);
                loginPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                cancelLogin.setEnabled(false);
            }
        });
        cancelLogin.setEnabled(false);

        okButton = new JButton(UIManager.getString(CLASS_NAME + ".okString"));
        okButton.setMnemonic(UIManager.getInt(CLASS_NAME + ".okString.mnemonic")); 
        cancelButton = new JButton(UIManager.getString(CLASS_NAME + ".cancelString"));
        cancelButton.setMnemonic(UIManager.getInt(CLASS_NAME + ".cancelString.mnemonic"));
        
        layoutLoginPanel(loginPanel);
        return loginPanel;
	}

	/**
	 * Listener class to implement saving of passwords and usernames.
	 * 
	 * 
	 */
	class SaveListener implements LoginListener {
		public void loginFailed(LoginEvent source) {
		}

		public void loginSucceeded(LoginEvent source) {
			if (getSaveMode() == SAVE_PASSWORD) {
				savePassword();
			} else if (getSaveMode() == SAVE_USERNAME) {
				usernameStore.addUsername(username);
				usernameStore.saveUsernames();
			}
		}

		public void loginStarted(LoginEvent source) {
		}

		public void loginCanceled(LoginEvent source) {
		}
	}

	void savePassword() {
		if (passwordStore != null) {
			passwordStore.set(getUserName(),getLoginService().getServer(),getPassword());
		}
	}

	private class Handler implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == nameCombo) {
				username = (String) nameCombo.getSelectedItem();
                return;
			}
            
			if (ae.getSource() == nameField) {
				username = nameField.getText();
			}            
		}

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			if (e.getSource() == nameCombo) {
				username = (String) nameCombo.getSelectedItem();
                return;
			}
            
			if (e.getSource() == nameField) {
				username = (String) nameField.getText();
			}
		}
	}

	private static class DialogHelper implements LoginListener, ActionListener {
		private JDialog dialog;

		private JXLoginPanel loginPanel;

		private LoginService service;

		private boolean cancelled;

		DialogHelper(Frame frame, JXLoginPanel panel, LoginService service) {
			this.loginPanel = panel;
			this.service = service;
			dialog = new JDialog(frame, UIManager.getString(CLASS_NAME + ".loginString"));
			service.addLoginListener(this);
            panel.okButton.addActionListener(this);
			panel.cancelButton.addActionListener(this);
			dialog.getContentPane().add(loginPanel, BorderLayout.CENTER);
                        
            dialog.getRootPane().setDefaultButton(panel.okButton);
			dialog.pack();
			dialog.setModal(true);
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // TODO!!!!!!
            // Doesn't compile in incubator should be enabled with JDNC
            dialog.setLocation(WindowUtils.getPointForCentering(dialog));
		}

		public void show() {
			dialog.setVisible(true);
		}

		public void loginFailed(LoginEvent source) {
			finishedLogin(false);
			loginPanel.loginProgress.setText(UIManager.getString(CLASS_NAME + ".loginFailed"));
		}

		public void loginSucceeded(LoginEvent source) {
			finishedLogin(true);
			dialog.dispose();
		}

		public void loginStarted(LoginEvent source) {

		}

		/**
		 *
		 */
		void startLogin() {
            try {
                cancelled = false;
                loginPanel.progressIndicator.setIndeterminate(true);
                loginPanel.cancelLogin.setEnabled(true);
                loginPanel.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                String name = loginPanel.getUserName();
                char[] password = loginPanel.getPassword();
                service.startAuthentication(name, password, null);
                UserPermissions.getInstance().setRoles(service.getUserRoles());
            } catch(IOException ioerr) {
                loginPanel.loginProgress.setText(ioerr.getMessage());
                finishedLogin(false);
            }
		}

		void finishedLogin(boolean result) {
            loginPanel.cancelLogin.setEnabled(false);
			loginPanel.progressIndicator.setIndeterminate(false);
			loginPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		void cancelAuthentication() {
			service.cancelAuthentication();
            loginPanel.cancelLogin.setEnabled(false);
			loginPanel.progressIndicator.setIndeterminate(false);
			loginPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		public void loginCanceled(LoginEvent source) {
			cancelled = true;
		}

		public void actionPerformed(ActionEvent ae) {
			Object source = ae.getSource();
			if (source == loginPanel.okButton) {
				startLogin();
			} else if (source == loginPanel.cancelLogin) {
				cancelAuthentication();
			} else if (source == loginPanel.cancelButton) {
				dialog.dispose();
			}
		}
	}

    /**
     * This method returns the button that can cancel the login dialog. When working with the
     * static helper methods this method is useless. However, if this component is embedded
     * within a parent container then the behavior of the cancel button should be customized
     * and thus it has to be implemented by the user since pressing this button will not cause
     * any login event (technically login hasn't started until the login button is pressed).
     */
    public JButton getCancelButton() {
        return cancelButton;
    }
    
	/**
	 * A convenience method to show a Login dialog
	 * 
	 * @param frame parent Frame
	 * @param name name
	 * @param password password
	 * @param service service
	 * @param store password store
	 * @param saveMode saveMode
	 */
	public static void showLoginDialog(Frame frame, String name,
			String password, LoginService service, PasswordStore store,
			int saveMode) {
		showLoginDialog(frame, name, password, service, store, null, null,
				saveMode);
	}

	/**
	 * A convenience method to show a Login dialog
	 * 
	 * @param frame parent Frame
	 * @param name name
	 * @param password password
	 * @param service service
	 * @param store password store
	 * @param header header component
	 * @param label label component
	 * @param saveMode saveMode
	 */
	public static void showLoginDialog(Frame frame, String name,
			String password, LoginService service, PasswordStore store,
			JComponent header, JComponent label, int saveMode) {
		JXLoginPanel loginPanel = new JXLoginPanel(name, password, service,
				store, header, label);
		loginPanel.setSaveMode(saveMode);
		DialogHelper helper = new DialogHelper(frame, loginPanel, service);
		helper.show();
	}

	/**
	 * Returns the name to use for the resource bundle for this module. The
	 * default implementation returns the
	 * <code>getClass().getName().resources.Resources</code>, subclasses
	 * wanting different behavior should override appropriately.
	 * 
	 * @return Name for resource bundle
	 */

	
	static String getResourceBundleName() {
		Package package1 = JXLoginPanel.class.getPackage();
		return package1 == null ? "resources.Resources" : package1.getName()
				+ ".resources.Resources";
	}

	static String getResourceAsString(String key) {
		Object value = getResourceAsObject(key);
		return value.toString();
	}

	static int getResourceAsInt(String key) throws NumberFormatException {
		Object value = getResourceAsObject(key);
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		return Integer.parseInt(value.toString());
	}

	static Object getResourceAsObject(String key)
			throws MissingResourceException {
		synchronized (JXLoginPanel.class) {
			if (_resources == null) {
				_resources = new HashMap<String, Object>();
				ResourceBundle bundle = ResourceBundle
						.getBundle(getResourceBundleName());
				Enumeration bundleKeys = bundle.getKeys();
				while (bundleKeys.hasMoreElements()) {
					String resourceKey = (String) bundleKeys.nextElement();
					_resources.put(resourceKey, bundle.getObject(resourceKey));
				}
			}
		}
		return _resources.get(key);
	}

	/**
	 * @return Returns the saveMode.
	 */
	public int getSaveMode() {
		return saveMode;
	}
    
    /**
     * The save mode indicates whether the "save" password is checked by default. This method
     * makes no difference if the passwordStore is null.
     *
	 * @param saveMode The saveMode to set either SAVE_NONE, SAVE_PASSWORD or SAVE_USERNAME
	 */
	public void setSaveMode(int saveMode) {
        nameArea.removeAll();
		this.saveMode = saveMode;
		savePanel.removeAll();
		if (saveMode == SAVE_PASSWORD) {
            if(passwordStore == null) {
                throw new IllegalArgumentException("saveMode may not be set to SAVE_PASSWORD when a password store is not defined");
            }
            nameArea.add(nameCombo, BorderLayout.CENTER);
			save = new JCheckBox(UIManager.getString(CLASS_NAME + ".rememberPasswordString"),
                       UIManager.getBoolean(CLASS_NAME + ".rememberPasswordDefault"));
			savePanel.add(save);
            revalidate();
            return;
		} 
        
        if(saveMode == SAVE_NONE) {
            nameArea.add(nameField, BorderLayout.CENTER);
            if(save != null) {
                save.setSelected(false);
                save.setVisible(false);
            }
            revalidate();
            return;
        } 
        
        if(saveMode == SAVE_USERNAME) {
            nameArea.add(nameCombo, BorderLayout.CENTER);
            save = new JCheckBox(UIManager.getString(CLASS_NAME + ".rememberUserString"),
                    passwordStore != null ? true : false);
            savePanel.add(save);
            revalidate();
            return;
        }

        throw new IllegalArgumentException("Save mode may be either SAVE_NONE, SAVE_PASSWORD or SAVE_USERNAME");
	}

	/**
	 * Sets the <strong>LoginService</strong> for this panel.
	 *
	 * @param service service
	 */
	public void setLoginService(LoginService service) {
		loginService = service;
	}

	/**
	 * Gets the <strong>LoginService</strong> for this panel.
	 *
	 * @return service service
	 */
	public LoginService getLoginService() {
		return loginService;
	}

	/**
	 * Sets the <strong>PasswordStore</strong> for this panel.
	 *
	 * @param store PasswordStore
	 */
	public void setPasswordStore(PasswordStore store) {
		passwordStore = store;
	}

	/**
	 * Gets the <strong>PasswordStore</strong> for this panel.
	 *
	 * @return store PasswordStore
	 */
	public PasswordStore getPasswordStore() {
		return passwordStore;
	}

	/**
	 * Sets the <strong>User name</strong> for this panel.
	 *
	 * @param username User name
	 */
	public void setUserName(String username) {
        if(saveMode == SAVE_NONE) {
            nameField.setText(username);
        } else {
            nameCombo.setSelectedItem(username);
        }
	}

	/**
	 * Gets the <strong>User name</strong> for this panel.
	 *
	 * @param username User name
	 */
	public String getUserName() {
		if (username == null) {
            if(saveMode != SAVE_NONE) {
                username = (String)nameCombo.getSelectedItem();
            } else {
                username = nameField.getText();
            }
		}
		return username;
	}

	/**
	 * Sets the <strong>Password</strong> for this panel.
	 *
	 * @param password Password
	 */
	public void setPassword(char[] password) {
		passwordField.setText(new String(password));
	}

	/**
	 * Gets the <strong>Password</strong> for this panel.
	 *
	 * @return password Password
	 */
	public char[] getPassword() {
		return passwordField.getPassword();
	}

}