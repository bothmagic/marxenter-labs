/*
 * $Id: SplashScreen.java 26 2004-09-06 19:21:51Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.splash;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.common.dialog.error.ErrorDialog;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataLoader;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressListener;
import org.jdesktop.jdnc.incubator.rbair.util.WindowUtils;

/**
 * Uses the Application object to populate the splash dialog.
 * This dialog has no window decorations.  The only way to close it is
 * programatically.
 * @author Richard Bair
 */
public final class SplashScreen extends JWindow implements ProgressListener, MessageListener {
	/**
	 * Singleton instance of the splash screen
	 */
	private static SplashScreen INSTANCE;
	/**
	 * Progress bar on the splash screen
	 */
	private JProgressBar progressBar;
	/**
	 * A list of messages received from the DataLoader during loading. Any error messages are displayed after the loading is done
	 */
	private List messages = new ArrayList();
	/**
	 * A label that is just below the progress bar, centered horizontally in the window.
	 * This label is a place for the app to track progress, etc.
	 */
	private JLabel messageLabel;
	
	/**
	 * Hidden constructor - enforces the singleton design pattern.
	 * Contains the gui layout code.
	 */
	private SplashScreen() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(layout);
		contentPane.setOpaque(false);
		
		//create a border
		/////////////////////////
		contentPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
		
		progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
		progressBar.setIndeterminate(true);
		//TODO let these two colors be set optionally
//		progressBar.setBackground(Color.DARK_GRAY);
//		progressBar.setForeground(Color.LIGHT_GRAY);
		//////////////////////////////////////////////
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.gridheight = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		gbc.insets = new Insets(0, 2, 0, 2);
		contentPane.add(progressBar, gbc);
		
		//Build number, appears below the progressbar
		////////////////////////////////////
		JLabel buildNumbr = new JLabel("Build " + Application.getInstance().getVersion().getBuild());
		buildNumbr.setOpaque(false);
		buildNumbr.setHorizontalAlignment(SwingConstants.RIGHT);
		buildNumbr.setFont(buildNumbr.getFont().deriveFont(4));
		gbc.gridy = 2;
		gbc.insets = new Insets(4, 2, 2, 2);
		contentPane.add(buildNumbr, gbc);

		//message label
		messageLabel = new JLabel(" ");
		messageLabel.setOpaque(false);
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setFont(messageLabel.getFont().deriveFont(4));
		contentPane.add(messageLabel, gbc);
		
		//initialize the gui from the Application object
		Image splashImage = Application.getInstance().getSplashImage();
		JLabel img = new JLabel(new ImageIcon(splashImage));
		gbc.gridx = 0;
		gbc.weighty = 0.0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 2;
		gbc.insets = new Insets(0,0,0,0);
		contentPane.add(img, gbc);
		
		pack();
		this.setLocation(WindowUtils.getPointForCentering(this));
	}
	
	private static SplashScreen getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SplashScreen();
		}
		return INSTANCE;
	}
	
	/**
	 * Shows the splash screen.  The Screen is built based on the Application object.
	 * @param displayTime How long the screen should be displayed in milliseconds.
	 * If the displayTime is greater than 0, then it will go away after this time
	 * expires regardless whether the app is finished loading or not.  If it is &lt;=0,
	 * then the splash screen will stay up until the application calls the
	 * <code>terminateSplashScreen()</code> method.  
	 */
	public static void showSplashScreen(final long displayTime) {
		showSplashScreen(displayTime, false, false);
	}
	
	/**
	 * Shows the splash screen.  The Screen is built based on the Application object.
	 * Also takes a parameter to indicate whether the progress bar should be shown.
	 * @param displayTime How long the screen should be displayed in milliseconds.
	 * If the displayTime is greater than 0, then it will go away after this time
	 * expires regardless whether the app is finished loading or not.  If it is &lt;=0,
	 * then the splash screen will stay up until the application calls the
	 * <code>terminateSplashScreen()</code> method.
	 * @param showProgress pass in true if you want a progress bar to be shown.
	 * @param indeterminate  determines whether to show the indeterminate progress bar or not
	 */
	public static void showSplashScreen(final long displayTime, final boolean showProgress, final boolean indeterminate) {
		// TODO showProgress does not work yet like it ought to if you set indeterminate = false
		SplashScreen splash = getInstance();
		if (displayTime > 0) {
			splash.progressBar.setVisible(showProgress);
			splash.progressBar.setIndeterminate(indeterminate);
			splash.setVisible(true);
			Thread th = new Thread() {
				public void run() {
					try {
						Thread.sleep(displayTime);
					} catch (Exception e) {
					} finally {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {
								public void run() {
									terminateSplashScreen();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			th.start();
		} else {
			splash.progressBar.setVisible(showProgress);
			splash.progressBar.setIndeterminate(indeterminate);
			splash.setVisible(true);
		}
	}

	/**
	 * Shows the splash screen. The progress bar will be based on this loader. When the loader is finished, then the splash screen
	 * will automatically terminate.
	 * @param loader
	 */
	public static void showSplashScreen(DataLoader loader) {
		final SplashScreen splash = getInstance();
		splash.progressBar.setVisible(true);
		loader.addMessageListener(splash);
		loader.addProgressListener(splash);
		splash.setVisible(true);
	}
	
	/**
	 * Terminates the splash screen, regardless as to whether the splash screen has
	 * reached its displayTime or not.
	 */
	public static void terminateSplashScreen() {
		if (INSTANCE != null) {
			INSTANCE.setVisible(false);
			INSTANCE.dispose();
			INSTANCE = null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.event.ProgressListener#progressStarted(org.jdesktop.swing.event.ProgressEvent)
	 */
	public void progressStarted(ProgressEvent evt) {
		SplashScreen splash = getInstance();
		splash.progressBar.setMinimum(evt.getMinimum());
		splash.progressBar.setMaximum(evt.getMaximum());
		splash.progressBar.setValue(evt.getProgress());
		splash.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.event.ProgressListener#progressEnded(org.jdesktop.swing.event.ProgressEvent)
	 */
	public void progressEnded(ProgressEvent evt) {
		terminateSplashScreen();
		SplashScreen splash = getInstance();
		((DataLoader)evt.getSource()).removeProgressListener(splash);
		((DataLoader)evt.getSource()).removeMessageListener(splash);
		
		//go through the messages. Show an error dialog for each error
		for (int i=0; i<messages.size(); i++) {
			MessageEvent msg = (MessageEvent)messages.get(i);
			if (msg.getLevel() == Level.SEVERE) {
				StringBuffer details = new StringBuffer("<html><head><style><!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style></head><body><h1>Error</h1><HR size=\"1\" noshade=\"noshade\"><p><b>message</b> <u>");
				details.append(msg.getMessage());
				details.append("</u></p><p><b>description</b> <u>");
				details.append(msg.getThrowable().getLocalizedMessage());
				details.append("</u>");
				StackTraceElement[] els = msg.getThrowable().getStackTrace();
				for (int j=0; j<els.length; j++) {
					details.append("<br>");
					details.append(els[j].toString());
				}
				details.append("</p><HR size=\"1\" noshade=\"noshade\"><h3>");
				details.append(Application.getInstance().getName() + " " + Application.getInstance().getVersionString());
				details.append("</h3></body></html>");
				ErrorDialog.showDialog((Window)null, "Error Starting " + Application.getInstance().getTitle(), msg.getMessage(), details.toString());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.event.ProgressListener#progressIncremented(org.jdesktop.swing.event.ProgressEvent)
	 */
	public void progressIncremented(ProgressEvent evt) {
		SplashScreen splash = getInstance();
		if (splash.progressBar.isIndeterminate() && evt.getMaximum() > 0) {
			splash.progressBar.setIndeterminate(false);
			showProgressPercentage(true);
		}
		splash.progressBar.setMinimum(evt.getMinimum());
		splash.progressBar.setMaximum(evt.getMaximum());
		splash.progressBar.setValue(evt.getProgress());
		setProgressMessage(evt.getMessage());
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.swing.event.MessageListener#message(org.jdesktop.swing.event.MessageEvent)
	 */
	public void message(MessageEvent evt) {
		//save up the messages
		SplashScreen splash = getInstance();
		splash.messages.add(evt);
	}
	
	/**
	 * Returns true if this splash screen is still visible
	 */
	public static boolean isRunning() {
		return getInstance().isVisible();
	}

	/**
	 * Sets the progress message that is displayed in the center of the splash screen
	 * (horizontally), and right below the progress bar vertically.
	 * @param msg
	 */
	public static void setProgressMessage(String msg) {
		getInstance().messageLabel.setText(msg);
	}
	
	/**
	 * 
	 * @param b
	 */
	public static void showProgressPercentage(boolean b) {
		//if I should show the progress %, set the string to null (which
		//causes the progressBar to show the % done). Otherwise, set it to
		//empty string which basically hides it.
		getInstance().progressBar.setStringPainted(b);
	}
}
