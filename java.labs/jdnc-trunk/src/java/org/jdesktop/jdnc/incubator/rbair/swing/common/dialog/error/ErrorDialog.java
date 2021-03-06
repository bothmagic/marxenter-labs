/*
 * $Id: ErrorDialog.java 18 2004-09-06 18:19:19Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.error;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jdesktop.jdnc.incubator.rbair.util.ResourceBundle;

/**
 * Common Error Dialog.  Composed of a title, message, and details.  Example pictures:
 * <br>
 * <img src="http://www.jgui.com/jgui/screenshots/errorDialog1.jpg" border=0 />
 * <br>
 * <img src="http://www.jgui.com/jgui/screenshots/errorDialog2.jpg" border=0 />
 * <br>
 * //TODO Need to improve the display of the error dialog.  It automatically scrolls to the bottom,
 * and I'd like it to start at the top.  Also, when details is clicked, the dialog doesn't optimize
 * its width, and I'd like it to.  An algorithm for determining the optimal width needs to be written.
 * @author Richard Bair
 */
public class ErrorDialog extends JDialog {
	/**
	 * ResourceBundle reference for the jgui resource bundle
	 */
	private static final ResourceBundle RES = ResourceBundle.getBundle("etc.resources.jgui");
	/**
	 * Text representing expanding the details section of the dialog
	 */
	private static final String DETAILS_EXPAND_TEXT = RES.getString("Error_Dialog_expand_details","Details >>");
	/**
	 * Text representing contracting the details section of the dialog
	 */
	private static final String DETAILS_CONTRACT_TEXT = RES.getString("Error_Dialog_contract_details","Details <<");
	/**
	 * Text for the Ok button.
	 */
	private static final String OK_BUTTON_TEXT = RES.getString("Error_Dialog_ok_button", "Ok");
	/**
	 * Icon for the error dialog (stop sign, etc)
	 */
	private static final Icon icon = RES.get48x48Icon("error.png", false);
	/**
	 * Error message label
	 */
	private JLabel errorMessage;
	/**
	 * details text area
	 */
	private JTextArea details;
	/**
	 * detail button
	 */
	private JButton detailButton;
	/**
	 * details scroll pane
	 */
	private JScrollPane detailsScrollPane;
	
	/**
	 * Create a new ErrorDialog with the given Frame as the owner
	 * @param owner
	 */
	private ErrorDialog(Frame owner) {
		super(owner, true);
		initGui();
	}
	
	/**
	 * Create a new ErrorDialog with the given Dialog as the owner
	 * @param owner
	 */
	private ErrorDialog(Dialog owner) {
		super(owner, true);
		initGui();
	}
	
	/**
	 * initialize the gui.
	 */
	private void initGui() {
		//initialize the gui
		GridBagLayout layout = new GridBagLayout();
		this.getContentPane().setLayout(layout);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridheight = 1;
		gbc.insets = new Insets(22, 12, 11, 17);
		this.getContentPane().add(new JLabel(icon), gbc);
		
		errorMessage = new JLabel();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(12, 0, 0, 11);
		this.getContentPane().add(errorMessage, gbc);
		
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(12, 0, 11, 5);
		JButton okButton = new JButton("Ok");
		this.getContentPane().add(okButton, gbc);
		
		detailButton = new JButton(DETAILS_EXPAND_TEXT);
		gbc.gridx = 2;
		gbc.weightx = 0.0;
		gbc.insets = new Insets(12, 0, 11, 11);
		this.getContentPane().add(detailButton, gbc);
		
		details = new JTextArea();
		detailsScrollPane = new JScrollPane(details);
		detailsScrollPane.setPreferredSize(new Dimension(300, 200));
		details.setEditable(false);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(6, 0, 11, 11);
		this.getContentPane().add(detailsScrollPane, gbc);
		
		//make the buttons the same size
		int buttonLength = detailButton.getPreferredSize().width;
		int buttonHeight = detailButton.getPreferredSize().height;
		Dimension buttonSize = new Dimension(buttonLength, buttonHeight);
		okButton.setPreferredSize(buttonSize);
		detailButton.setPreferredSize(buttonSize);
		
		//set the event handling
		okButton.addActionListener(new OkClickEvent());
		detailButton.addActionListener(new DetailsClickEvent());
	}
	
	/**
	 * Set the details section of the error dialog.  If the details are either
	 * null or an empty string, then hide the details button and hide the detail
	 * scroll pane.  Otherwise, just set the details section.
	 * @param details
	 */
	private void setDetails(String details) {
		if (details == null || details.equals("")) {
			setDetailsVisible(false);
			detailButton.setVisible(false);
		} else {
			this.details.setText(details);
			setDetailsVisible(false);
			detailButton.setVisible(true);
		}
	}

	/**
	 * Set the details section to be either visible or invisible.  Set the
	 * text of the Details button accordingly.
	 * @param b
	 */
	private void setDetailsVisible(boolean b) {
		if (b) {
			detailsScrollPane.setVisible(true);
			detailButton.setText(DETAILS_CONTRACT_TEXT);
			
			/*
			 * increase the width and height of the dialog according to the following algorithm:
			 * Double the width and height.
			 * FUTURE check to see what width and height would be necessary to show all of the contents
			 * of the details text area.  If the width and height are below some given threshold
			 * (for instance, 2x the original size) then set the dialog to the optimal width/height.
			 * If above that threshold, then set the dialog to be the threshold
			 */
			
		} else {
			detailsScrollPane.setVisible(false);
			detailButton.setText(DETAILS_EXPAND_TEXT);
			
			/*
			 * FUTURE Set the dialog back to its proper size
			 */
		}
		
		pack();
	}

	/**
	 * Set the error message for the dialog box
	 * @param errorMessage
	 */
	private void setErrorMessage(String errorMessage) {
		this.errorMessage.setText(errorMessage);
	}

	/**
	 * Listener for Ok button click events
	 * @author Richard Bair
	 */
	private final class OkClickEvent implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			//close the window
			setVisible(false);
		}
	}

	/**
	 * Listener for Details click events.  Alternates whether the details section
	 * is visible or not.
	 * @author Richard Bair
	 */
	private final class DetailsClickEvent implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			setDetailsVisible(!detailsScrollPane.isVisible());
		}
	}

	/**
	 * Constructs and shows the error dialog for the given exception.  The exceptions message will be the
	 * errorMessage, and the stacktrace will be the details.
	 * @param owner
	 * @param title
	 * @param e
	 */
	public static void showDialog(Window owner, String title, Throwable e) {
		//TODO I may be able to reuse sw and pw from call to call.  That could improve performance, etc.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		showDialog(owner, title, e.getLocalizedMessage(), sw.toString());
	}
	
	/**
	 * Show the error dialog.
	 * @param owner Owner of this error dialog.  This cannot be null.
	 * @param title Title of the error dialog
	 * @param errorMessage Message for the error dialog
	 * @param details Details to be shown in the detail section of the dialog.  This can be null
	 * if you do not want to display the details section of the dialog.
	 */
	public static void showDialog(Window owner, String title, String errorMessage, String details) {
		ErrorDialog dlg;
		assert owner != null;
		if (owner instanceof Dialog) {
			dlg = new ErrorDialog((Dialog)owner);
		} else {
			dlg = new ErrorDialog((Frame)owner);
		}
		dlg.setTitle(title);
		dlg.setErrorMessage(errorMessage);
		dlg.setDetails(details);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dlg.pack();
		dlg.setLocationRelativeTo(owner);
		dlg.setVisible(true);
	}
	
}
