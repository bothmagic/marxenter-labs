/*
 * $Id: LoginDialog.java 23 2004-09-06 18:39:25Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.login;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;

import org.jdesktop.jdnc.incubator.rbair.swing.JHyperLink;
import org.jdesktop.jdnc.incubator.rbair.swing.SetComboBoxModel;
import org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.error.ErrorDialog;
import org.jdesktop.jdnc.incubator.rbair.util.ResourceBundle;
import org.jdesktop.jdnc.incubator.rbair.util.StringUtils;
import org.jdesktop.jdnc.incubator.rbair.util.WindowUtils;

/**
 * <p>
 * Simple login dialog that asks the user for their name and their password.
 * The password and username are then passed to the authentication module.
 * If authenticated, then the dialog returns true.  Otherwise, it returns false.
 * </p>
 * <p>
 * This dialog does not attempt to be all things to all people.  Your dialog may
 * need different widgets, or more widgets.  However, every login dialog requires
 * a place for users to enter information, performs authentication, and then 
 * takes some action based on a successful athentication and some action based on 
 * an unsuccesful authentication.  This dialog provides for those three actions.
 * </p>
 * <p>
 * It should also prove to be a suitable base class for constructing your own
 * LoginDialogs.
 * </p>
 * <br>
 * <br>
 * Some features to implement in the future (perhaps)
 * <ul>
 * <li>SHOW_ICON</li>
 * <li>USER_PICS</li>
 * <li>USER_SPECIFIC_PICS</li>
 * <li>SHOW_HELP_BTN</li>
 * </ul>
 * 
 * <br>
 * If you do not specify a dialog for editing the server settings, then a default dialog is used.  This dialog
 * simply asks for the server to connect to, and saves the data as a DefaultServer.
 * </br>
 * TODO get the login to remember the server it was logged in with last
 * LoginPanel, that contains the login logic.
 * TODO finish class documentation
 * - insert list of features and how they work
 * - insert a link to a tutorial called "how to use LoginDialog"
 * - insert a picture of the login dialog
 * - document that "instructions" are in html format
 * TODO There is one known bug in this class; if you fail your login attempt, the dialog never lets you try again.
 * This is most likely due to the threading code -- it was my first attempt at threading and probably doesn't work
 * so well :-)
 * @author Richard Bair
 */
public class LoginDialog extends JDialog {
	/**
	 * Logger for logging errors, warnings, debug statements, etc.
	 */
//	private static final Logger LOG = Logger.getLogger(LoginDialog.class);
	/**
	 * Resource bundle
	 */
	private static final ResourceBundle RES = ResourceBundle.getBundle("etc.resources.jgui");
	/**
	 * Default banner to use.  The banner is the image that goes along the
	 * top of the login dialog.
	 * TODO, I want to be able to use loginBanner.jpg, unless it doesn't exist, and then use
	 * a default banner.
	 */
	private static final Icon DEFAULT_BANNER = RES.getIconByPath("images/loginBanner.png");
	/**
	 * Parameter name used for the status line.  This parameter can be used in
	 * resource files to insert the name of the server you are connecting to
	 * into the resourced string.
	 */
	public static final String SERVER_NAME_PARAM = "${SERVER}";
	/**
	 *Shows the "Remember me" check box.  This option, REMEMBER_PASSWORD_OPTION, 
	 *and FORGOT_PASSWORD_OPTION are mutually exclusive.  If one or more of those
	 *options are enabled, they will be honored in the following order:
	 *<ol>
	 *<li>REMEMBER_PASSWORD_OPTION</li>
	 *<li>REMEMBER_ME_OPTION</li>
	 *<li>FORGOT_PASSWORD_OPTION</li>
	 *</ol> 
	 */
	public static final String REMEMBER_ME_OPTION = "REMEMBER_ME";
	/**
	 *Shows the "Remember my passwordField" check box.  This option, REMEMBER_ME_OPTION, 
	 *and FORGOT_PASSWORD_OPTION are mutually exclusive.  If one or more of those
	 *options are enabled, they will be honored in the following order:
	 *<ol>
	 *<li>REMEMBER_PASSWORD_OPTION</li>
	 *<li>REMEMBER_ME_OPTION</li>
	 *<li>FORGOT_PASSWORD_OPTION</li>
	 *</ol> 
	 */
	public static final String REMEMBER_PASSWORD_OPTION = "REMEMBER_PASSWORD";
	/**
	 *Shows the "Forgot my passwordField" hyper link.  This option, REMEMBER_ME_OPTION, 
	 *and REMEMBER_PASSWORD_OPTION are mutually exclusive.  If one or more of those
	 *options are enabled, they will be honored in the following order:
	 *<ol>
	 *<li>REMEMBER_PASSWORD_OPTION</li>
	 *<li>REMEMBER_ME_OPTION</li>
	 *<li>FORGOT_PASSWORD_OPTION</li>
	 *</ol> 
	 */
	public static final String FORGOT_PASSWORD_OPTION = "FORGOT_PASSWORD";
	/**
	 * Shows the "Options" button.  The panel/dialog that will be shown when the
	 * option button is pressed can be set via the <code>setOptionsPanel</code>
	 * or <code>setOptionsDialog</code> methods.  Defaults to using a drop
	 * down panel.  Set the OPTIONS_DIALOG_OPTION to show a dialog when
	 * this button is pressed.  FUTURE: Those methods have not been written yet.
	 * @deprecated
	 */
	public static final String OPTIONS_OPTION = "OPTIONS";
	/**
	 * Shows the "Options" dialog when the options button is pressed.  Use the
	 * <code>setOptionsDialog</code> method to override the default options'
	 * dialog that is displayed.
	 * @deprecated
	 */
	public static final String OPTIONS_DIALOG_OPTION = "OPTIONS_DIALOG";
	/**
	 * Option to show the bannerLabel at the top of the dialog.  If you wish to use
	 * a custom bannerLabel aside from the default bannerLabel, use the
	 * <code>setBannerImage(Icon)</code> method.
	 */
	public static final String SHOW_BANNER_OPTION = "SHOW_BANNER";
	/**
	 * Displays special instructions at the top of the dialog.  If the bannerLabel is
	 * showing, then the instructions are displayed on the bannerLabel (right hand side).
	 * If the bannerLabel is not displayed, then the instructions will be displayed at
	 * the top of the dialog, left hand side (across most if not all of the top).
	 */
	public static final String SHOW_INSTRUCTIONS_OPTION = "INSTRUCTIONS";
	/**
	 * Option to allow the user to change the current server.  This is currently
	 * accomplished via a dropdown box.  
	 */
	public static final String CHANGE_SERVER_OPTION = "CHANGE_SERVER";
	/**
	 * Allows the user to "hand edit" the server to log into by typing the server
	 * name.  This causes the server combo box to behave exactly the same as the
	 * user name combo box.  This option is mutually exclusive with the
	 * PROMPT_SERVER_DATA_OPTION.  If both options are set, then the
	 * PROMPT_SERVER_DATA option has precedence. 
	 */
	public static final String HAND_EDIT_SERVER_OPTION = "HAND_EDIT_SERVER";
	/**
	 * Disallows users from hand editing the server they are connecting to, and
	 * adds an "Edit..." hyperlink to the right of the combo box.  Clicking this
	 * hyperlink will display a dialog that allows the user to set their
	 * server configuration (what the server name is, connection properties,
	 * etc).  Since this dialog is very application specific, you may change
	 * what dialog is displayed on edit by calling the
	 * <code>setServerDataDialog()</code> method.<br>
	 * This option is mutually exclusive with the HAND_EDIT_SERVER_OPTION, and
	 * has precedence.
	 */
	public static final String PROMPT_SERVER_DATA_OPTION = "PROMPT_SERVER_DATA";

	/**
	 * Create a thread for doing the login.  This is important to keep the user interface painting
	 * as the login process proceeds.
	 */
	private LoginThread loginThread = new LoginThread();
	/**
	 * Contains settings such as all previous successful login names, passwords
	 * (where appropriate), servers, etc.
	 */
	private LoginSettings settings;
	/**
	 * The authentication module to use for authentication.
	 */
	private AuthenticationModule authMod;
	/**
	 * The authentication listener to notify on successful authentication.
	 */
	private AuthenticationListener auth;
	/**
	 * Indicates a successful login
	 */
	private boolean successful = false;
	/**
	 * ComboBox model for the userNamesComboBox.
	 */
	private SetComboBoxModel userNamesModel;
	/**
	 * ComboBox model for the serverNamesComboBox.
	 */
	private SetComboBoxModel serverNamesModel;
	/**
	 * Map of credentials.  Credentials are used by the authentication
	 * system to authenticate a user.  They ALWAYS include a user name
	 * and passwordField (if they originated from this dialog). 
	 */
	private Map credentials = new HashMap();
	/**
	 * List of options for this dialog.  They are read-only, and can only be
	 * set at construction time.
	 */
	private List options = new ArrayList();
	/**
	 * Reference to the (possibly) user defined EditServerSettings object.
	 * This object is shown when the user click the "Edit" button for
	 * editing the server settings.
	 */
	private EditServerSettings userDefinedEditServerSettings = null;

	/**
	 * Banner across the top of the dialog (optional)
	 */
	private JLabel bannerLabel;
	/**
	 * Instructions printed at top of the dialog (optional)
	 */
	private JEditorPane instructionsEditorPane;
	/**
	 * Status line printed beneath instructions/banner (mandatory)
	 */
	private JLabel statusLabel;
	/**
	 * User Name combo box (mandatory)
	 */
	private JComboBox userNameBox;
	/**
	 * Password field (mandatory) 
	 */
	private JPasswordField passwordField;
	/**
	 * Server name combo box (optional)
	 */
	private JComboBox serverBox;
	/**
	 * Remember Password check box (optional)
	 */
	private JCheckBox rememberPasswordCheckBox;
	/**
	 * remember me check box (optional)
	 */
	private JCheckBox rememberMeCheckBox;
	/**
	 * Forgot my password hyperlink (optional)
	 */
	private JHyperLink forgotPasswordLink;
	/**
	 * Hyperlink for editing server information (optional)
	 */
	private JHyperLink editServerLink;
	
	/**
	 * Create a new LoginDialog.  Pass in the owner (must be an already
	 * "realized" Frame), an <code>AuthenticationModule</code> which whill
	 * perform the authentication for this dialog box, and a <code>List</code>
	 * of options defining the look and behavior of this Login dialog box.  The
	 * options are copied within this method, so feel free to do whatever you
	 * like with your List after this dialog is constructed.
	 * @param owner An already realized Frame
	 * @param authMod AuthenticationModule for authentication
	 * @param options List of options.  Available options are enumerated above.
	 */
	public LoginDialog(Frame owner, AuthenticationModule authMod, List options) {
		super(owner, true);
		//validate the authentication module
		assert authMod != null;
		this.authMod = authMod;
		//create a default authentication listner
		this.auth = new DefaultAuthenticationListener(owner, this);
		//validate and copy over the options
		Iterator itr = options.iterator();
		while (itr.hasNext()) {
			Object option = itr.next();
			assert validateOption(option);
			this.options.add(option);
		}
		//get the login settings from disk
		settings = readSettingsFromDisk();
		//initialize the gui
		initGui();
	}

	/**
	 * Utility method for validating an option.  It checks the given option
	 * against all of the current options to make sure only correct options
	 * are passed into this object.
	 * @param option The option that needs validation
	 * @return true if it is a valid option, false otherwise.
	 */
	private static boolean validateOption(Object option) {
		return option.equals(REMEMBER_ME_OPTION) || option.equals(REMEMBER_PASSWORD_OPTION) || option.equals(FORGOT_PASSWORD_OPTION) || option.equals(OPTIONS_OPTION) || option.equals(OPTIONS_DIALOG_OPTION) || option.equals(SHOW_BANNER_OPTION) || option.equals(SHOW_INSTRUCTIONS_OPTION) || option.equals(CHANGE_SERVER_OPTION) || option.equals(HAND_EDIT_SERVER_OPTION) || option.equals(PROMPT_SERVER_DATA_OPTION);
	}

	/**
	 * utility method for initializing the gui.
	 * TODO May want to make protected or public so people can subclass this
	 * method.
	 */
	private void initGui() {
		//create the layout manager, and initialize the ContentPane
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		Container contentPane = getContentPane();
		contentPane.setLayout(layout);

		//set the title to it's default
		setTitle(RES.getString("Login_Dialog_title", "Login"));
		//do not allow this dialog to be resized
		setResizable(false);

		//add the banner
		if (options.contains(SHOW_BANNER_OPTION)) {
			bannerLabel = new JLabel(DEFAULT_BANNER);
			bannerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			WindowUtils.setConstraints(gbc, 0, 0, 6, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0);
			contentPane.add(bannerLabel, gbc);

			//add instructions to the banner
			if (options.contains(SHOW_INSTRUCTIONS_OPTION)) {
				instructionsEditorPane = new JEditorPane();
				instructionsEditorPane.setBorder(BorderFactory.createEmptyBorder());
				instructionsEditorPane.setOpaque(false);
				instructionsEditorPane.setContentType("text/html");
				instructionsEditorPane.setEditable(false);
				instructionsEditorPane.setFocusable(false);
				bannerLabel.setLayout(new GridBagLayout());
				WindowUtils.setConstraints(gbc, 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 15, 110, 15, 20);
				bannerLabel.add(instructionsEditorPane, gbc);
			}
		} else {
			//add instructions at the top of the dialog
			if (options.contains(SHOW_INSTRUCTIONS_OPTION)) {
				instructionsEditorPane = new JEditorPane();
				instructionsEditorPane.setBorder(BorderFactory.createEmptyBorder());
				instructionsEditorPane.setOpaque(false);
				instructionsEditorPane.setContentType("text/html");
				instructionsEditorPane.setEditable(false);
				instructionsEditorPane.setFocusable(false);
				WindowUtils.setConstraints(gbc, 0, 0, 6, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 12, 12, 0, 11);
				contentPane.add(instructionsEditorPane, gbc);
			}
		}

		//add the status line
		String defaultStatusText = RES.getString("Login_Dialog_default_status", "Connecting...");
		statusLabel = new JLabel(defaultStatusText);
		WindowUtils.setConstraints(gbc, 0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 17, 12, 0, 11);
		contentPane.add(statusLabel, gbc);

		//add the user name line
		JLabel userNameLabel = new JLabel(RES.getString("Login_Dialog_user_name", "User name:"));
		userNameLabel.setDisplayedMnemonic(RES.getChar("Login_Dialog_user_name_mnemonic", 'U'));
		WindowUtils.setConstraints(gbc, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 12, 12, 0, 30);
		contentPane.add(userNameLabel, gbc);
		//get data to populate the combo box
		Set userNames = settings.getLogins();
		userNamesModel = new LoginNameSetComboBoxModel(userNames);
		userNameBox = new JComboBox(userNamesModel);
		userNameBox.setEditable(true);
		userNameBox.addItemListener(new UserNameSelectionListener());
		WindowUtils.setConstraints(gbc, 1, 2, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 12, 20, 0, 11);
		contentPane.add(userNameBox, gbc);
		userNameLabel.setLabelFor(userNameBox);

		//add the passwordField line
		JLabel passwordLabel = new JLabel(RES.getString("Login_Dialog_password", "Password:"));
		passwordLabel.setDisplayedMnemonic(RES.getChar("Login_Dialog_password_mnemonic", 'P'));
		WindowUtils.setConstraints(gbc, 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 5, 12, 0, 30);
		contentPane.add(passwordLabel, gbc);
		passwordField = new JPasswordField();
		WindowUtils.setConstraints(gbc, 1, 3, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 5, 20, 0, 11);
		contentPane.add(passwordField, gbc);
		passwordLabel.setLabelFor(passwordField);

		//add the server line
		if (options.contains(CHANGE_SERVER_OPTION)) {
			//add the label
			JLabel serverLabel = new JLabel(RES.getString("Login_Dialog_server", "Server:"));
			serverLabel.setDisplayedMnemonic(RES.getChar("Login_Dialog_server_mnemonic", 'S'));
			WindowUtils.setConstraints(gbc, 0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 5, 12, 0, 30);
			contentPane.add(serverLabel, gbc);
			//get data to populate the server combo box
			Set serverNames = settings.getServers();
			serverNamesModel = new SetComboBoxModel(serverNames);
			serverBox = new JComboBox(serverNamesModel);
			serverBox.addItemListener(new ServerSelectionListener());
			serverLabel.setLabelFor(serverBox);
			//default to read only mode
			serverBox.setEditable(false);
			if (options.contains(HAND_EDIT_SERVER_OPTION)) {
				//make server combo box read/write
				serverBox.setEditable(true);
			}
			//I cannot place the serverBox onto the content pane until I know
			//whether I need to make room for the "edit" hyperlink
			if (options.contains(PROMPT_SERVER_DATA_OPTION)) {
				//make server combo box read only
				serverBox.setEditable(false);
				editServerLink = new JHyperLink(RES.getString("Login_Dialog_edit_server", "Edit..."), new DefaultEditServerClickEvent(this));
				//place the server box AND the Edit hyperlink
				WindowUtils.setConstraints(gbc, 1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 5, 20, 0, 0);
				contentPane.add(serverBox, gbc);
				WindowUtils.setConstraints(gbc, 3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 10, 10, 0, 11);
				contentPane.add(editServerLink, gbc);
			} else {
				//just place the server box
				WindowUtils.setConstraints(gbc, 1, 4, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 5, 20, 0, 11);
				contentPane.add(serverBox, gbc);
			}
		}

		//add the "remember my passwordField" radio, "remember me" radio, or "forgot my passwordField" hyperlink
		JComponent component = null;
		if (options.contains(REMEMBER_PASSWORD_OPTION)) {
			rememberPasswordCheckBox = new JCheckBox(RES.getString("Login_Dialog_remember_password", "Remember my password"));
			rememberPasswordCheckBox.setMnemonic(RES.getChar("Login_Dialog_remember_password_mnemonic", 'R'));
			component = rememberPasswordCheckBox;
		} else if (options.contains(REMEMBER_ME_OPTION)) {
			rememberMeCheckBox = new JCheckBox(RES.getString("Login_Dialog_remember_me", "Remember me"));
			rememberMeCheckBox.setMnemonic(RES.getChar("Login_Dialog_remember_me_mnemonic", 'R'));
			component = rememberMeCheckBox;
		} else if (options.contains(FORGOT_PASSWORD_OPTION)) {
			forgotPasswordLink = new JHyperLink(RES.getString("Login_Dialog_remember_password", "Forgot your password?"), new DefaultForgotPasswordClickEvent());
			forgotPasswordLink.setHidden(false);
			component = forgotPasswordLink;
		}

		if (component != null) {
			WindowUtils.setConstraints(gbc, 1, 5, 3, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, 5, 20, 0, 11);
			contentPane.add(component, gbc);
		}

		//add the buttons
		JButton okButton = new JButton(RES.getString("Common_ok_button", "OK"));
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton(RES.getString("Common_cancel_button", "Cancel"));
		okButton.addActionListener(new LoginClickEvent());
		//add the ability to type the escape key and have it close the dialog
		KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		CancelClickEvent cce = new CancelClickEvent();
		cancelButton.registerKeyboardAction(cce, escapeKeyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		//add the ability to click the cancel button and have it close the dialog
		cancelButton.addActionListener(cce);
		//deal with the options button
		if (options.contains(OPTIONS_OPTION)) {
			JButton optionsButton = new JButton();
			if (options.contains(OPTIONS_DIALOG_OPTION)) {
				//show a dialog on click instead of expanding/contracting
				optionsButton.setText(RES.getString("Common_options_button", "Options"));
			} else {
				//do contracting expanding
				optionsButton.setText(RES.getString("Common_options_expand_button", "Options >>"));
			}
			int width = optionsButton.getPreferredSize().width;
			int height = optionsButton.getPreferredSize().height;
			Dimension bestSize = new Dimension(width, height);
			okButton.setPreferredSize(bestSize);
			cancelButton.setPreferredSize(bestSize);
			optionsButton.setPreferredSize(bestSize);
			WindowUtils.setConstraints(gbc, 1, 6, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 17, 0, 11, 0);
			contentPane.add(okButton, gbc);
			WindowUtils.setConstraints(gbc, 2, 6, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 17, 5, 11, 0);
			contentPane.add(cancelButton, gbc);
			WindowUtils.setConstraints(gbc, 3, 6, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 17, 5, 11, 11);
			contentPane.add(optionsButton, gbc);
		} else {
			int width = cancelButton.getPreferredSize().width;
			int height = cancelButton.getPreferredSize().height;
			Dimension bestSize = new Dimension(width, height);
			okButton.setPreferredSize(bestSize);
			cancelButton.setPreferredSize(bestSize);
			WindowUtils.setConstraints(gbc, 2, 6, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 17, 5, 11, 0);
			contentPane.add(okButton, gbc);
			WindowUtils.setConstraints(gbc, 3, 6, 1, 1, 0.0, 1.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, 17, 5, 11, 11);
			contentPane.add(cancelButton, gbc);
		}
		
		loginThread.start();
	}

	/**
	 * Read the LoginSettings from the login.dat file
	 * @return a valid LoginSettings object, even if there is no
	 * login.dat file found.
	 */
	private static LoginSettings readSettingsFromDisk() {
		try {
			//read the login settings from disk
			FileInputStream fis = new FileInputStream("login.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			if (ois != null) {
				Object obj = ois.readObject();
				LoginSettings settings = (LoginSettings)obj;
				ois.close();
				fis.close();
				return settings;
			}
			fis.close();
		} catch (FileNotFoundException fnfe) {
			//don't worry about it
//			LOG.info("login.dat not found on disk.  Creating new one.");
			System.out.println("login.dat not found on disk.  Creating new one.");
		} catch (Exception e) {
//			LOG.error(e, e);
			e.printStackTrace();
		}
		return new LoginSettings();
	}
	
	/**
	 * Save the LoginSettings to the login.dat file.
	 * @param settings Settings to save.
	 */
	private static void saveSettingsToDisk(LoginSettings settings) {
		try {
			FileOutputStream fos = new FileOutputStream("login.dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(settings);
			fos.close();
		} catch (Exception e) {
//			LOG.error(e, e);
			e.printStackTrace();
		}
	}

	/**
	 * Show the login dialog.  Returns true if the login was successful, false othewise.
	 * If you want to display a dialog to the user indicating success or failure,
	 * the proper way to do that is to specify an AuthorizationListener, not by
	 * reading this boolean.  This return value is intended to be used to take
	 * action in the application based on whether the user is logged in or not
	 * (enabling/disabling certain features).<br>
	 * If the user has previously clicked the "Remember Me" button, and the login
	 * is still valid, then this dialog will never be shown.  Beware, therefore,
	 * before enabling the "Remember Me" option.
	 * @return
	 */
	public boolean prompt() {
		//repack...don't know why I can't do this just once
		pack();
		//center the window
		setLocation(WindowUtils.getPointForCentering(this));
		//check to see whether there is a "remember me" login.
		//if there is, try to log in with that login.  If it works, don't show
		//the dialog.  If it doesn't work, then show the dialog and a message dialog
		//indicating that it was a bad login.
		if (options.contains(REMEMBER_ME_OPTION)) {
			Login login = settings.getRememberMeLogin();
			if (login != null && settings.isRememberMe()) {
				if (authenticate(login.getUserName(), login.getPassword(), settings.getRememberMeServer())) {
					return true;
				}//else { 
				//change the top of the box saying that the login was invalid and to try again
				//}
			}
		}

		if (userNameBox.getItemCount() > 0) {
			//load the dialog with the data in the first user name slot
			//but we don't really want the first user name slot, but the last user name slot
			
			Object obj = settings.getLastLogin();
			userNameBox.setSelectedItem(obj);
			if (obj != null) {
				loadData(obj);
			}

			if(options.contains(CHANGE_SERVER_OPTION)) {
				obj = serverBox.getItemAt(0);
				if (obj != null) {
					loadServerData(obj);
				}
			}
		}
		
		//show the dialog
		setVisible(true);
		return successful;
	}

	/**
	 * Get the Server that was logged into.
	 * <p>
	 * <b>This section for LoginDialog implementors only:</b>
	 * The credentials map, from which this data comes, is only
	 * "stocked" with the server, userName, and password after
	 * either the user cancels the dialog, or the user closes
	 * the dialog (via the "x"), or the user successfully logs
	 * in.  Since this information should not be available to anyone 
	 * outside of this method until one of those events occurs, it
	 * should be no harm that it is implemented this way.
	 * </p>
	 * @return
	 */
	public Server getServer() {
		return (Server)credentials.get(Credentials.SERVER_KEY);
	}
	
	/**
	 * Specifies the server to log into.  This will initialize the
	 * server combo box (if it is visible), and will also alter the
	 * status line, and the dialog title to indicate what server is
	 * being logged into (via the toString() method).  Reinitializes
	 * everything to their default state if a <code>null</code> is
	 * passed in.
	 * @param serverName
	 */
	public void setServer(Server server) {
		if (server == null) {
			//reinitialize everything to defaults
			setTitle(RES.getString("Login_Dialog_title", "Login"));
			statusLabel.setText(RES.getString("Login_Dialog_default_status", "Connecting ..."));

			if (serverBox != null && serverBox.getItemCount() > 0) {
				serverBox.setSelectedIndex(0);
			}
		} else {
			String serverName = server.toString();
			String title = RES.getString("Login_Dialog_title_with_server", "Login to ${SERVER}");
			title = StringUtils.replaceAll(title, SERVER_NAME_PARAM, serverName);
			setTitle(title);

			String statusText = RES.getString("Login_Dialog_default_status_with_server", "Connecting to ${SERVER}...");
			statusText = StringUtils.replaceAll(statusText, SERVER_NAME_PARAM, serverName);
			statusLabel.setText(statusText);
			
			if (serverBox != null) {
				if (serverBox.isEditable()) {
					serverBox.setSelectedItem(server);
				} else {
					serverBox.setEditable(true);
					serverNamesModel.addObject(server);
					serverBox.setSelectedItem(server);
					serverBox.setEditable(false);
				}
			}
		}
		
		//add to the credentials map
		credentials.put(Credentials.SERVER_KEY, server);
	}

	/**
	 * Get the name of the user that was logged in.
	 * <p>
	 * <b>This section for LoginDialog implementors only:</b>
	 * The credentials map, from which this data comes, is only
	 * "stocked" with the server, userName, and password after
	 * either the user cancels the dialog, or the user closes
	 * the dialog (via the "x"), or the user successfully logs
	 * in.  Since this information should not be available to anyone 
	 * outside of this method until one of those events occurs, it
	 * should be no harm that it is implemented this way.
	 * </p>
	 * @return
	 */
	public String getUserName() {
		return (String)credentials.get(Credentials.USER_NAME_KEY);
	}

	/**
	 * Sets the default name of the user that should be logged in.  This will
	 * replace the user name in the userNameBox with the given userName.  Nulls
	 * will be converted into empty strings.
	 * @param userName
	 */
	public void setUserName(String userName) {
		userName = (userName == null ? "" : userName);
		userNameBox.setSelectedItem(userName);
		//add to the credentials
		credentials.put(Credentials.USER_NAME_KEY, userName);
	}

	/**
	 * Sets the password to the given password.  Nulls will be converted into
	 * empty strings.  NOTE: There is no getPassword() method because I don't
	 * want people wandering around with unencrypted passwords.  Is there ever
	 * a good use for it?
	 * @param password
	 */
	public void setPassword(String password) {
		password = (password == null ? "" : password);
		passwordField.setText(password);
		//add to the credentials
		credentials.put(Credentials.PASSWORD_KEY, password);
	}

	/**
	 * Retrieves the instructions for this dialog box
	 * @return
	 */
	public String getInstructions() {
		if (instructionsEditorPane != null) {
			return instructionsEditorPane.getText();
		}
		return "";
	}
	
	/**
	 * Sets the instructions for this dialog box.
	 * @param text
	 */
	public void setInstructions(String text) {
		if (instructionsEditorPane != null) {
			instructionsEditorPane.setText(text);
		}
	}

	/**
	 * Utility method encapsulating the logic for authentication.  Calls
	 * the <code>AuthenticationModule</code> and the
	 * <code>AuthenticationListener</code> at the appropriate times, and
	 * basically coordinates the process of authentication.
	 * @param userName
	 * @param password
	 * @return
	 */
	private boolean authenticate(String userName, String password, Server server) {
		//authenticate
		credentials.put(Credentials.USER_NAME_KEY, userName);
		credentials.put(Credentials.PASSWORD_KEY, password);
		credentials.put(Credentials.SERVER_KEY, server);
		try {
			if (authMod.authenticate(credentials)) {
				auth.successful(credentials);
				successful = true;

			} else {
				auth.failure(credentials, null);
				successful = false;
			}
		} catch (Throwable t) {
			auth.failure(credentials, t);
			successful = false;
		}
		return successful;
	}

	/**
	 * Basic event handler for handling Login button click events.
	 * @author Richard Bair
	 * date: Jun 7, 2003
	 */
	private class LoginClickEvent implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			loginThread.doLogin();
		}
	}

	/**
	 * Utility method to get a Server object from the serverBox.  This method
	 * correctly handles the situation of creating a Server from a String (which
	 * occurs if the user hand edits the server box).
	 * @return
	 */
	private Server getServerFromBox() {
		Object obj = serverBox.getSelectedItem();
		if (obj == null) {
			return new DefaultServer("");
		}
		if (obj instanceof Server) {
			return (Server)obj;
		} else {
			return new DefaultServer(obj.toString());
		}
	}

	/**
	 * Utility method that loads data from what is ostensibly a Login object.
	 * @param obj
	 */
	private void loadData(Object obj) {
		if (obj != null && obj instanceof Login) {
			Login login = (Login) obj;
			passwordField.setText(login.getPassword());
			if (rememberPasswordCheckBox != null) {
				rememberPasswordCheckBox.setSelected(login.isRememberPassword());
				if (!login.isRememberPassword()) {
					passwordField.setText("");
				}
			} else {
				passwordField.setText("");
			}
		} else if (obj != null) {
			passwordField.setText("");
			if (rememberPasswordCheckBox != null) {
				rememberPasswordCheckBox.setSelected(false);
			}
		}
	}

	/**
	 * Utility method that loads data from what is ostensibly a Server object.
	 * @param obj
	 */
	private void loadServerData(Object obj) {
		if (obj != null && obj instanceof Server) {
			setServer((Server)obj);
		}
	}

	/**
	 * Handles a cancel click event.
	 * @author Richard Bair
	 * date: Jun 7, 2003
	 */
	private class CancelClickEvent implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			//load the credentials with the user_name, and the server
			String userName;
			Object obj = userNameBox.getSelectedItem();
			if (obj == null) {
				userName = "";
			} else {
				userName = obj.toString();
			}
			credentials.put(Credentials.USER_NAME_KEY, userName);
			
			Server server;
			if (options.contains(CHANGE_SERVER_OPTION)) {
				server = (Server)getServerFromBox();
				credentials.put(Credentials.SERVER_KEY, server);
			}
			
			setVisible(false);
		}
	}

	/**
	 * @author Richard Bair
	 */
	private static final class DefaultAuthenticationListener implements AuthenticationListener {
		private Frame owner;
		private LoginDialog parent;

		DefaultAuthenticationListener(Frame owner, LoginDialog parent) {
			assert owner != null;
			assert parent != null;
			this.owner = owner;
			this.parent = parent;
		}

		/* (non-Javadoc)
		 * @see com.jgui.login.AuthenticationListener#successful(java.util.Map)
		 */
		public void successful(Map credentials) {
			// TODO may want to log successful login via log4j
			//save the settings to file
			//first, get the current list of logins and (from the userNameBox)
			Set names = parent.userNamesModel.getData();
			//then, if there was a user selected in the box (should always be)
			Object selected = parent.userNameBox.getSelectedItem();
			if (selected != null) {
				//create/get the Login object for this login.  If the user name was
				//added by hand, then this will be a string and a new Login objet
				//needs to be created.  If this was already in the list, then it
				//will be a Login object.
				Login login = null;
				if (selected instanceof String) {
					login = new Login((String) selected, new String(parent.passwordField.getPassword()), (parent.rememberPasswordCheckBox == null ? false : parent.rememberPasswordCheckBox.isSelected()));
					names.add(login);
				} else if (selected instanceof Login) {
					login = (Login) selected;
					login.setPassword(new String(parent.passwordField.getPassword()));
					if (parent.options.contains(REMEMBER_PASSWORD_OPTION)) {
						login.setRememberPassword(parent.rememberPasswordCheckBox.isSelected());
					} else {
						login.setRememberPassword(false);
					}
					//this login may already be in the set, but hey, this is a set, so 
					//what the heck!
					names.add(login);
				}
				//now that the login object has been updated/added to the logins list,
				//save the list to the settings object.
				parent.settings.setLogins(names);
				
				//now, if the REMEMBER_ME option is selected, then we need to save the
				//login as the RememberMe login, and the server as the RememberMe server,
				//and we need to save whether the remember me checkbox is set to true.
				if (parent.options.contains(REMEMBER_ME_OPTION) && !parent.options.contains(REMEMBER_PASSWORD_OPTION)) {
					parent.settings.setRememberMeLogin(login);
					Server server;
					if (parent.options.contains(CHANGE_SERVER_OPTION)) {
						server = (Server)parent.getServerFromBox();
					} else {
						server = (Server)parent.credentials.get(Credentials.SERVER_KEY);
					}
					parent.settings.setRememberMeServer(server);
					parent.settings.setRememberMe(parent.rememberMeCheckBox.isSelected());
				}
			}
			
			//now, save the servers
			if (parent.options.contains(CHANGE_SERVER_OPTION)) {
				selected = parent.serverBox.getSelectedItem();
				Set servers = parent.serverNamesModel.getData(); 
				if (selected != null) {
					if (selected instanceof String) {
						servers.add(new DefaultServer((String)selected));
					} else {
						servers.add((Server)selected);
					}
				}
				parent.settings.setServers(servers);
			}

			parent.settings.setLastLogin((Login)parent.userNameBox.getSelectedItem());
			//save the login settings
			saveSettingsToDisk(parent.settings);
		}

		/* (non-Javadoc)
		 * @see com.jgui.login.AuthenticationListener#failure(java.util.Map, java.lang.Throwable)
		 */
		public void failure(Map credentials, Throwable cause) {
			//display a dialog informing the user why login failed
			String failureMessage = "<html>Either <b>${USER_NAME}</b> is an invalid user name, or the password was incorrect</html>";
			//replace the ${} params in the message with the items from the credentials
			Set set = credentials.entrySet();
			Iterator itr = set.iterator();
			while (itr.hasNext()) {
				Map.Entry entry = (Map.Entry) itr.next();
				if (entry != null && entry.getKey() != null && entry.getValue() != null) {
					failureMessage = failureMessage.replaceAll("\\$\\{" + entry.getKey().toString() + "\\}", entry.getValue().toString());
				}
			}

			if (cause == null) {
				ErrorDialog.showDialog(owner, "Login Failure", failureMessage, null);
			} else {
				ErrorDialog.showDialog(owner, "Login Failure", cause);
			}
			// TODO potentially log via log4j
		}

	}

	private final class UserNameSelectionListener implements ItemListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.ITEM_STATE_CHANGED) {
				//new item was selected, load the passwordField and (if not null)
				//the remember passwordField check box
				Object obj = e.getItem();
				loadData(obj);
			}
		}
	}

	private final class ServerSelectionListener implements ItemListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED || e.getStateChange() == ItemEvent.ITEM_STATE_CHANGED) {
				//new item was selected, load the passwordField and (if not null)
				//the remember passwordField check box
				Object obj = e.getItem();
				loadServerData(obj);
			}
		}
	}

	private static final class Login implements Serializable, Comparable {
		private String userName;
		private String password;
		private boolean rememberPassword;

		public Login(String userName, String password) {
			this(userName, password, false);
		}

		public Login(String userName, String password, boolean rememberPassword) {
			setUserName(userName);
			setPassword(password);
			this.rememberPassword = rememberPassword;
		}
		/**
		 * Return the unencrypted passwordField
		 * @return
		 */
		public String getPassword() {
			return password;
		}

		/**
		 * @return
		 */
		public boolean isRememberPassword() {
			return rememberPassword;
		}

		/**
		 * Return the unencrypted userNameBox
		 * @return
		 */
		public String getUserName() {
			return userName;
		}

		/**
		 * encrypt and set the passwordField
		 * @param string
		 */
		public void setPassword(String string) {
			password = string;
		}

		/**
		 * @param b
		 */
		public void setRememberPassword(boolean b) {
			rememberPassword = b;
		}

		/**
		 * encrypt and set the user name
		 * @param string
		 */
		public void setUserName(String string) {
			userName = string;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return getUserName();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (obj instanceof Login) {
				Login l = (Login) obj;
				return userName.equals(l.userName);
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return password.hashCode() + userName.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object o) {
			if (o instanceof Login) {
				Login l = (Login) o;
				return userName.compareTo(l.userName);
			}
			return -1;
		}

	}

	private static final class DefaultForgotPasswordClickEvent implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			JOptionPane.showMessageDialog((Component) ae.getSource(), LoginDialog.RES.getString("Default_Forgot_Password_Click_Event_message", "See your network administrator about recovering your forgotten password."));
		}
	}

	private static final class DefaultEditServerClickEvent implements ActionListener {
		private LoginDialog parent;
		public DefaultEditServerClickEvent(LoginDialog dlg) {
			assert dlg != null;
			parent = dlg;
		}
		public void actionPerformed(ActionEvent ae) {
			if (parent.userDefinedEditServerSettings == null) {
				//show the default option pane
				String serverName = JOptionPane.showInputDialog(RES.getString("Default_Edit_Server_Click_Event_prompt", "Server Name: "));
				Server server = new DefaultServer(serverName);
				parent.setServer(server);
			} else {
				//show the user defined edit screen
				Object obj = parent.serverBox.getSelectedItem();
				Server currentServer = null; 
				if (obj instanceof Server) {
					currentServer = (Server)obj;
				}
				Server server = parent.userDefinedEditServerSettings.prompt(currentServer);
				//since a user can cancel out of the edit box, check for null
				if (server != null) {
					parent.setServer(server);
				}
			}
		}
	}
	
	private static final class DefaultServer extends Server {
		private String serverName = "";
		public DefaultServer(String serverName) {
			this.serverName = serverName == null ? "" : serverName;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return serverName;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals(Object obj) {
			if (obj instanceof DefaultServer) {
				return serverName.equals(((DefaultServer)obj).serverName);
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		public int hashCode() {
			return serverName.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		public int compareTo(Object obj) {
			if (obj instanceof DefaultServer) {
				return serverName.compareTo(((DefaultServer)obj).serverName);
			} else {
				return serverName.compareTo(obj.toString());
			}
		}
	}
	
	/**
	 * Contains all of the settings for a LoginDialog, ready to be saved to
	 * disk.  It is intended to encapsulate the data for being saved/retrieved
	 * from disk.
	 * @author Richard Bair
	 * date: Jun 7, 2003
	 */
	private static final class LoginSettings implements Serializable {
		private Set logins;
		private Set servers;
		private boolean rememberMe;
		private Login rememberMeLogin;
		private Server rememberMeServer;
		private Login lastLogin;
		
		LoginSettings() {
			this(null, null, false, null, null);
		}
		
		LoginSettings(Set logins, Set servers, boolean rememberMe, Login rememberMeLogin, Server rememberMeServer) {
			setLogins(logins);
			setServers(servers);
			setRememberMe(rememberMe);
			setRememberMeLogin(rememberMeLogin);
			setRememberMeServer(rememberMeServer);
		}
		
		/**
		 * @return
		 */
		public Set getLogins() {
			return logins;
		}

		/**
		 * @return
		 */
		public boolean isRememberMe() {
			return rememberMe;
		}

		/**
		 * @return
		 */
		public Login getRememberMeLogin() {
			return rememberMeLogin;
		}

		/**
		 * @return
		 */
		public Set getServers() {
			return servers;
		}

		/**
		 * @param set
		 */
		public void setLogins(Set logins) {
			this.logins = (logins == null ? new TreeSet() : logins);
		}

		/**
		 * @param b
		 */
		public void setRememberMe(boolean rememberMe) {
			this.rememberMe = rememberMe;
		}

		/**
		 * @param login
		 */
		public void setRememberMeLogin(Login login) {
			this.rememberMeLogin = (login == null ? new Login("", "", true) : login);
		}

		/**
		 * @param set
		 */
		public void setServers(Set servers) {
			this.servers = (servers == null ? new TreeSet() : servers);
		}

		/**
		 * @return
		 */
		public Server getRememberMeServer() {
			return rememberMeServer;
		}

		/**
		 * @param server
		 */
		public void setRememberMeServer(Server server) {
			rememberMeServer = server;
		}

		/**
		 * @return Returns the lastLogin.
		 */
		public Login getLastLogin() {
			return lastLogin;
		}
		/**
		 * @param lastLogin The lastLogin to set.
		 */
		public void setLastLogin(Login lastLogin) {
			this.lastLogin = lastLogin;
		}
	}
	
	/**
	 * Make the credentials Map accessible so that "edit server" dialogs can
	 * get access to the credentials.
	 * @return
	 */
	public Map getCredentials() {
		return credentials;
	}

	/**
	 * @return
	 */
	public EditServerSettings getUserDefinedEditServerSettings() {
		return userDefinedEditServerSettings;
	}

	/**
	 * @param settings
	 */
	public void setUserDefinedEditServerSettings(EditServerSettings settings) {
		userDefinedEditServerSettings = settings;
	}

	private final class LoginThread extends Thread {
		public synchronized void doLogin() {
			this.resume();
		}
		
		public void run() {
			this.suspend();
			
			Cursor origCursor = getRootPane().getCursor();
			try {
				getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				//Get the userName, password, and server to the
				//authenticate method.
				String userName;
				Object obj = userNameBox.getSelectedItem();
				if (obj == null) {
					userName = "";
				} else {
					userName = obj.toString();
				}
				//get password
				String passwordString = new String(passwordField.getPassword());
				//If the serverBox is visible
				//(ie: option CHANGE_SERVER_OPTION exists), get the
				//server from there, otherswise get the server
				//from the credentials
				Server server;
				if (options.contains(CHANGE_SERVER_OPTION)) {
					server = (Server)getServerFromBox();
				} else {
					server = (Server)credentials.get(Credentials.SERVER_KEY);
				}
		
				boolean successful = authenticate(userName, passwordString, server);
				if (successful) {
					setVisible(false);
				}
			} finally {
				getRootPane().setCursor(origCursor);
			}
		}
	}
	
	private static final class LoginNameSetComboBoxModel extends SetComboBoxModel {

		/**
		 * @param data
		 */
		public LoginNameSetComboBoxModel(Set data) {
			super(data);
		}
		
		/* (non-Javadoc)
		 * @see com.jgui.swing.SetComboBoxModel#addObject(java.lang.Object)
		 */
		public void addObject(Object obj) {
			Login login = null;
			if (!(obj instanceof Login)) {
				login = new Login(obj == null ? "" : obj.toString(), "");
			} else {
				login = (Login)obj;
			}
			super.addObject(login);
		}
		/* (non-Javadoc)
		 * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
		 */
		public void setSelectedItem(Object anItem) {
			Login login = null;
			if (!(anItem instanceof Login)) {
				login = new Login(anItem == null ? "" : anItem.toString(), "");
			} else {
				login = (Login)anItem;
			}
			super.setSelectedItem(login);
			//This hack exists because without it there is a problem loading that last person to log in.  It
			//loads the last person, but doesn't show them on the bar, this hack tells the bar to redraw itself
			fireContentsChanged(this, 0, getSize() - 1);
		}
	}
}
