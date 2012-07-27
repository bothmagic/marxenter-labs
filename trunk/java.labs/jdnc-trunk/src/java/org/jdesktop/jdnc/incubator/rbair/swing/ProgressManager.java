/*
 * $Id: ProgressManager.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jdesktop.jdnc.incubator.rbair.swing.icon.CancelTaskIcon;

/**
 * The ProgressManager is a singleton instance (per Application) that is used
 * by any component that wants to register a progressable task. For instance,
 * in JDNC a DataSource is a class that will communicate with
 * a database or some other data store asynchronously. It will therefore
 * register that asynchronous action with the ProgressManager. The
 * ProgressManager will periodically query the task to see what its completion
 * status is, what the description for the task is, or perhaps to cancel it.
 * <p>
 * If the ProgressManager discovers that a task has taken longer than a few
 * milliseconds it will display the ProgressDialog. There is only ever one
 * instance of a ProgressDialog per Application. The ProgressManager is the
 * only class that can create the ProgressDialog, or interact with it directly.
 * The ProgressManager comes with a default implementation of ProgressDialog,
 * but it is possible for a developer to construct and use a custom
 * ProgressDialog implementation instead of the default implementation.
 * <p>
 * Progress events can also be displayed on a StatusBar, or in some other way.
 * In such cases, the component on the StatusBar would query the ProgressManager
 * for progress information, and display the proper graphic or progress state.
 * If clicked, such a component could ask the ProgressManager to display the
 * ProgressDialog.
 * <p>
 * An object that adds Progressables to this ProgressManager does not have to
 * worry about removing those Progressables once they finish execution. The
 * ProgressManager will remove the Progressables in due time after the
 * Progressable has finished execution.
 * 
 * @author Richard Bair
 */
public class ProgressManager {
    /**
     * A list containing all of the <code>Progressable</code>s being managed
     * and monitored for this ProgressManager.
     */
    private List progressables = new ArrayList();
    /**
     * The <code>ProgressDialog</code> used with this ProgressManager.
     */
    private JDialog progressDlg;
    /**
     * A timer that is started whenever a modal Progressable is added to this
     * manager (assuming that the timer is not already running). When the timer
     * expires, if there are any modal tasks then the ProgressDialogImpl is
     * displayed.
     */
    private Timer popupTimer;

    /**
     * This contructor is package private. Only the Application object should
     * create an instance of this class. Too bad Java doesn't have a friend
     * keyword...
     */
    ProgressManager() {
        popupTimer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateProgressables();
                synchronized(progressables) {
                    for (int i=0; i<progressables.size(); i++) {
                        Progressable p = (Progressable)progressables.get(i);
                        if (p.isModal()) {
                            if (progressDlg == null) {
                                progressDlg = new ProgressDialogImpl();
                            }
                            ((ProgressDialogImpl)progressDlg).setVisible(true);
                            break;
                        }
                    }
                    popupTimer.stop();
                }
            }
        });
        popupTimer.setCoalesce(true);
    }
    
    /**
     * @see javax.swing.ProgressMonitor#getMillisToPopup()
     * @return the number of millis to allow a modal task to run before popping
     * up the ProgressDialog.
     */
    public int getMillisToPopup() {
        /*
	     * An integer value representing the number of milliseconds to wait
	     * before popping up the ProgressDialogImpl from the time that a
	     * Progressable is added to this manager. Only modal Progressables
	     * cause the popup to occur. This value is stored in the timer
         */
        return popupTimer.getDelay();
    }
    
    /**
     * @see javax.swing.ProgressMonitor#setMillisToPopup()
     * @param millis If less than 0, then millisToPopup will be set to 0.
     */
    public void setMillisToPopup(int millis) {
        popupTimer.setDelay(millis < 0 ? 0 : millis);
    }

    /**
     * Adds the given Progressable to the list of Progressables managed by this
     * ProgressManager.
     * @param p
     */
    public void addProgressable(Progressable p) {
        synchronized(progressables) {
            progressables.add(p);
            //if a countdown timer has not already been started for a previously
            //modal item, and if p is modal, then start a countdown timer.
            if (!popupTimer.isRunning() && p.isModal()) {
                popupTimer.start();
            }
        }
    }
    
    /**
     * @return an array of Progressables. This array contains the Progressable
     * objects that are being managed/monitored by this ProgressManager as of
     * the last time this method was called. If a Progressable is no longer
     * managed by this ProgressManager between calls to this method, then your
     * array will no longer be accurate.
     */
    public Progressable[] getProgressables() {
        updateProgressables();
        return (Progressable[])progressables.toArray(new Progressable[progressables.size()]);
    }
    
    private void updateProgressables() {
        //iterate through all of the progressables and remove any that are
        //completed. Return an array of all that remain
        synchronized(progressables) {
            for (int i=progressables.size()-1; i>=0; i--) {
                Progressable p = (Progressable)progressables.get(i);
                if (p.getProgress() >= p.getMaximum()) {
                    progressables.remove(i);
                }
            }
        }
    }
    
    /**
     * A default implementation of the ProgressDialog interface. This
     * implementation constructs a ProgressDialog that should be useful in all
     * instances except those with a custom look and feel.
     * <p>
     * This Dialog has the following characteristics:
     * <ul>
     * <li>One JProgressBar per <code>Progressable</code></li>
     * <li><code>Progressable</code>s must be independent of each other</li>
     * <li>Each <code>Progressable</code> is cancellable. A small cancel button
     * is placed to the right of the JProgressBar</li>
     * <li>A <code>Progressable</code> can request modality. In that case, the
     * &quot;hide&quot; action is disabled</li>
     * <li>The dialog can be &quot;hid&quot; if no <code>Progressable</code>s
     * require modality</li>
     * <li>A &quot;Hide when possible&quot; check box is placed at the bottom of
     * the dialog. If selected, then when there are no modal 
     * <code>Progressable</code>s, then the dialog is automatically hidden. When
     * not selected, the dialog stays visible until the &quot;hide&quot action
     * is invoked.</li>
     * <li>The hide action is represented by a button in the bottom right corner
     * of the dialog. This action will hide the dialog if no modal
     * <code>Progressable</code>s are currently being managed</li>
     * </ul>
     * @author Richard Bair
     */
    private static final class ProgressDialogImpl extends JDialog {
        /**
         * This is the panel on which all of the ProgressPanel panels will be
         * placed. This panel is updated whenever the dialog is displayed, and
         * whenever the swing Timer fires requiring a gui refresh.
         */
        private JScrollablePanel panel;
        /**
         * A checkbox used to know whether this dialog should be automatically
         * hidden when no modal tasks are pending
         */
        private JCheckBox hideWhenPossibleCB;
        /**
         * A button used for the HideAction
         */
        private JButton hideButton;
        /**
         * The action that does the hiding. Used by hideButton.
         */
        private HideAction hideAction;
        /**
         * A swing Timer used for initiating refreshes. The rational for this
         * timer is described in the following post to the JDNC forums:
         * <p>
         * &quot;In the current architecture a ProgressListener registers with
         * some object to receive progress notifications (progress started, 
         * finished, % completed, etc). These notifications must be fired off in
         * a background thread and then perhaps posted to the gui thread via 
         * SwingUtilities.invokeLater. The gui then receives the notifications 
         * and updates the ui accordingly.
         * <p>
         * &quot;However, when using this architecture I found that my reading
         * of bytes from an InputStream (in this case, what I was monitoring)
         * was slowed down tremendously. Of course I was being naive and firing
         * off a progress message for every byte/byte buffer read. It brought up
         * a concern however -- that naive implementations may bring programs to
         * a crawl and the blame may well be placed on Java as opposed to the
         * offending code (how many times have I had to deal with this one?!!).
         * <p>
         * &quot;Also, it seemed to me that the optimal number of messages would
         * be one every 1/25 of a second or so (25fps) since there's no point
         * passing messages faster than the eye can detect and very poor form to
         * pass messages slower than the eye can detect.&quot;
         * <p>
         * Thus, the role filled by this Timer. Every 1/32 of a second (32fps is
         * the number I've decided to go with, keeping with movie film rates, 
         * which is approx. every 32 milliseconds).
         */
        private Timer refreshTimer;
        
        /**
         * This constructor is package private, and should only be constructed
         * by the ProgressManager.
         */
        ProgressDialogImpl() {
            super(Application.getInstance().getMainFrame(), "Tasks...");
            initGui();
            refreshTimer = new Timer(32, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //get the latest set of progressables. Remove those
                    //ProgressPanels that are no longer in the array. Update
                    //all those ProgressPanels that are in the array. Add any
                    //new ProgressPanels necessary.
                    Progressable[] progressables = Application.getInstance().getProgressManager().getProgressables();
                    ProgressPanel[] panels = new ProgressPanel[panel.getComponentCount()];
                    for (int i=0; i<panels.length ;i++) {
                        panels[i] = (ProgressPanel)panel.getComponent(i);
                    }
                    
                    ProgressPanel[] newPanels = new ProgressPanel[progressables.length];
                    boolean modal = false;
                    for (int i=0; i<progressables.length; i++) {
                        Progressable p = progressables[i];
                        //while I'm at it, find out if any task is modal
                        if (!modal && p.isModal()) {
                            modal = true;
                        }
                        //look for an accompanying ProgressPanel
                        ProgressPanel panel = null;
                        for (int j=0; j<panels.length; j++) {
                            panel = panels[j];
                            if (panel.p == p) {
                                break;
                            }
                        }
                        if (panel == null) {
                            //no panel in the array, so I'll need a new panel.
                            newPanels[i] = new ProgressPanel(p);
                        } else {
                            //the panel was found, update the GUI
                            panel.updateGui();
                            newPanels[i] = panel;
                        }
                    }
                    //clear off all of the components on the panel and replace
                    //them. TODO Need to keep in mind scroll bar positioning
                    panel.removeAll();
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.anchor = GridBagConstraints.NORTH;
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.gridheight = 1;
                    gbc.gridwidth = 1;
                    gbc.gridx = 0;
                    gbc.weightx = 1.0;
                    gbc.weighty = 0.0;
                    for (int i=0; i<newPanels.length ;i++) {
                        gbc.gridy = i;
                        if (i == newPanels.length -1) {
                            gbc.weighty = 1.0;
                        }
                        panel.add(newPanels[i], gbc);
                    }
                    //if hideWhenPossibleCB is selected, then hide unless there
                    //was some progressable that was modal. Also toggle the
                    //HideAction's state
                    hideAction.setEnabled(!modal);
                    if (!modal && hideWhenPossibleCB.isSelected()) {
                        setVisible(false);
                    }
                    setModal(modal);
                }
            });
        }
        
        /**
         * Only construct the basic gui framework: the scrollable panel, the
         * hide-when-possible checkbox and the hide button. The contents of the
         * scrollable panel will be reconstructed every time the swing Timer
         * fires.
         */
        private void initGui() {
            getContentPane().setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(12, 12, 0, 11);
            gbc.weightx = 1.0;
            gbc.weighty = 0.0;
            
            JLabel label = new JLabel("<html><h2><b>Current tasks being performed</b></h2></html>");
            label.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            getContentPane().add(label, gbc);
            
            panel = new JScrollablePanel();
            panel.setScrollableTracksViewportWidth(true);
            panel.setBackground(Color.WHITE);
            panel.setLayout(new GridBagLayout());
            JScrollPane sp = new JScrollPane(panel);
            sp.getViewport().setBackground(Color.WHITE);
            sp.setBorder(BorderFactory.createLoweredBevelBorder());
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridy = 1;
            gbc.insets = new Insets(7, 12, 0, 11);
            gbc.weighty = 1.0;
            getContentPane().add(sp, gbc);
            
            hideWhenPossibleCB = new JCheckBox("Hide when possible");
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy = 2;
            gbc.weighty = 0.0;
            getContentPane().add(hideWhenPossibleCB, gbc);
            
            hideAction = new HideAction();
            hideButton = new JButton(hideAction);
            hideButton.setPreferredSize(new Dimension(80, hideButton.getPreferredSize().height));
            gbc.anchor = GridBagConstraints.EAST;
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridy = 3;
            gbc.insets = new Insets(7, 12, 11, 11);
            getContentPane().add(hideButton, gbc);
            
            setSize(new Dimension(500, 315));
        }
        
        /**
         * @inheritDoc
         */
        public void setVisible(boolean b) {
            super.setVisible(b);
            if (b) {
                refreshTimer.start();
            } else {
                refreshTimer.stop();
            }
        }
        
        /**
         * An aggregate Panel composed of other components to form a single
         * entity that displays the status of a single <code>Progressable</code>
         * 
         * TODO Once the DataModel stuff is ironed out, this panel could
         * possibly be constructed via the Binding architecture
         * @author Richard Bair
         */
        private static final class ProgressPanel extends JPanel {
            /**
             * The <code>Progressable</code> that this panel is based on
             */
            private Progressable p;
            /**
             * The progress bar used for displaying the status of the
             * <code>Progressable</code>
             */
            private JProgressBar pb;
            /**
             * A button that exists to allow the user to cancel a current
             * operation. If the cancel operation is not supported on this
             * <code>Progressable</code>, then the button is removed from the
             * panel.
             */
            private JButton cancelButton;
            /**
             * Shows a description for the <code>Progressable</code>
             */
            private JLabel descriptionLabel;
            /**
             * Displays the textual status of the <code>Progressable</code>,
             * such as &quot;Connection to server...&quot, etc.
             */
            private JLabel statusLabel;
            
            /**
             * Only created by the ProgressDialogImpl
             * @param p
             */
            ProgressPanel(Progressable p) {
                if (p == null) {
                    throw new NullPointerException("Progressable p cannot be null");
                }
                this.p = p;
                initGui();
            }
            
            /**
             * Init the gui
             */
            private void initGui() {
                setOpaque(false);
                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.WEST;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.gridheight = 1;
                gbc.gridwidth = 1;
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.insets = new Insets(5, 5, 0, 5);
                gbc.weightx = 1.0;
                gbc.weighty = 0.0;
                
                descriptionLabel = new JLabel(p.getDescription());
                descriptionLabel.setIcon(p.getIcon());
                add(descriptionLabel, gbc);
                
                gbc.gridy = 1;
                gbc.insets = new Insets(3, 5, 0, 5);
                pb = new JProgressBar(p.getMinimum(), p.getMaximum());
                pb.setValue(p.getProgress());
                pb.setIndeterminate(p.isIndeterminate());
                add(pb, gbc);
                
                gbc.gridx = 1;
                gbc.weightx = 0.0;
                //TODO use an action instead
                cancelButton = new JButton(new CancelTaskIcon());
                cancelButton.setMargin(new Insets(0, 0, 0, 0));
                cancelButton.setIconTextGap(0);
                cancelButton.setBorder(BorderFactory.createEmptyBorder());
                cancelButton.setContentAreaFilled(false);
                cancelButton.setVisible(p.canCancel());
                add(cancelButton, gbc);
                
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.insets = new Insets(3, 5, 5, 5);
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                statusLabel = new JLabel(p.getMessage());
                statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN));
                statusLabel.setHorizontalAlignment(JLabel.CENTER);
                add(statusLabel, gbc);
            }
            
            /**
             * Causes the gui to be refreshed according to the
             * <code>Progressable</code>
             */
            void updateGui() {
                descriptionLabel.setIcon(p.getIcon());
                if (pb.getValue() != p.getProgress()) {
                    pb.setValue(p.getProgress());
                }
                if (pb.isIndeterminate() != p.isIndeterminate()) {
                    pb.setIndeterminate(p.isIndeterminate());
                }
                cancelButton.setVisible(p.canCancel());
                statusLabel.setText(p.getMessage());
            }
            
            /**
             * @inheritDoc
             */
            protected void paintComponent(Graphics g) {
                ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        }
        
        
        /**
         * Action to hide the ProgressDialogImpl
         * @author Richard Bair
         */
    	private final class HideAction extends AbstractAction {
    		public HideAction() {
    			super.putValue(AbstractAction.NAME, "Hide");
    			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Hide the progress window");
    		}
    		
    		/**
    		 * @inheritDoc
    		 */
    		public void actionPerformed(ActionEvent e) {
    		    //check to make sure that there are no modal Progressables in
    		    //the ProgressManager.
    		    Progressable[] progressables = Application.getInstance().getProgressManager().getProgressables();
    		    boolean isModal = false;
    		    for (int i=0; i<progressables.length; i++) {
    		        if (progressables[i].isModal()) {
    		            isModal = true;
    		            break;
    		        }
    		    }
    		    
    		    if (!isModal) {
    		        //hides the ProgressDialogImpl instance
    		        setVisible(false);
    		    } else {
    		        //TODO log that this action should have been disabled
    		    }
    		}
    		
    	}
    }
}
