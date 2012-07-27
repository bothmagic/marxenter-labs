
package org.jdesktop.appframework;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdesktop.appframework.goodies.album.TutorialUtils;
import org.jdesktop.appframework.swingx.SingleXFrameApplication;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Application.ExitListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Generic Dialog showing a FormView as content. The goal (same 
 * intention as in JXDialog, see the wiki for a draft spec) is to 
 * extract the ever same dialog logic here and make it 
 * auto-configure from a "content form". <p>
 * 
 * PENDING: no need to really extend JDialog? The ui-specifics 
 * (button-bar layout, borders) here are handled by goodies forms.
 * Everything else is "wiring" the participants. <p>
 * 
 * 
 * Extracted from AblumEditorDialog in JGoodies binding tutorial example.
 */
public final class FormDialog extends JDialog {
    private static final Logger LOG = Logger.getLogger(FormDialog.class
            .getName());
    /**
     * Will be set to <code>true</code> if the dialog is canceled.
     * 
     * @see #hasBeenCanceled()
     */
    private boolean canceled;

    private JButton okayButton;

    private JButton cancelButton;
    /** the FormView to show. */
    private FormView form;

    private List<ExitListener> exitListeners;
 
    // Instance Creation ******************************************************
    
    /**
     * Constructs an AlbumEditorDialog for the given Album.
     * 
     * @param parent   this dialog's parent frame
     * @param formView    the content to show
     */
    public FormDialog(Frame parent, FormView form) {
        super(parent, true);
        this.form = form;
        setName(form.getName()+ "Dialog");
        canceled = false;
        initExitHook();
    }
    
    
    private void initExitHook() {
        exitListeners = new CopyOnWriteArrayList<ExitListener>();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowListener l = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                exit(e);
            }
            
        };
        addWindowListener(l);
        
    }


    // Dialog Life Cycle ******************************************************

    /**
     * Builds the dialog content, marks it as not canceled and makes it visible.
     * This method is called by client code after creation.
     */
    public void open() {
        build();
        canceled = false;
        setVisible(true);
    }

    /**
     * Called when closing through the system button.
     * 
     * 
     * @param event
     */
    public void exit(EventObject event) {
        if (!canExit(event)) {
            return;
        }
        // sure?
        cancel();
    }
    
    private boolean canExit(EventObject event) {
        for (ExitListener listener : exitListeners) {
            if (!listener.canExit(event)) {
                return false;
            }
        }
        return true;
    }
    /**
     * Closes the dialog: releases obsolete bindings, and disposes the dialog, 
     * which in turn releases all required OS resources.
     */
    private void close(boolean canceled) {
        this.canceled = canceled;
        release();
        dispose();
    }
    

    /**
     * Checks and answers whether the dialog has been canceled.
     * This indicator is set in #doAccept and #doCancel.
     * 
     * @return true indicates that the dialog has been canceled
     */
    public boolean hasBeenCanceled() {
        return canceled;
    }
    
    
    /**
     * Intendet for internal cleanup, if any. Called just before
     * disposing the dialog.
     */
    protected void release() {
        LOG.info("");
    }

    // Actions ****************************************************************
    /**
     * Configures components and actions from the ApplicationContext.
     *
     */
    private void inject() {
        ApplicationContext context = Application.getInstance().getContext();
        context.getResourceMap().injectComponents(this);
        ActionMap actionMap = context.getActionMap(this.getClass(), this);
        okayButton.setAction(actionMap.get("ok"));
        cancelButton.setAction(actionMap.get("cancel"));
    }

    /**
     * Action bound to the okay-button. Performs the
     * form's apply before calling close. The canceled
     * property is set to false.
     *
     */
    @Action
    public void ok() {
        ApplicationContext context = Application.getInstance().getContext();
        Object actionsObject = form.getActionsObject();
        ActionMap actionMap = context.getActionMap(actionsObject.getClass(), 
                actionsObject);
        javax.swing.Action delegate = actionMap.get("apply");
        if (delegate != null) {
            delegate.actionPerformed(null);
        }
        if (!canExit(null)) return;
        close(false);
    }

    /**
     * Action bound to the cancel-button. Performs the
     * form's discard before calling close. The canceled
     * property is set to false.
     *
     */
    @Action
    public void cancel() {
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        Object actionsObject = form.getActionsObject();
        ActionMap actionMap = context.getActionMap(actionsObject.getClass(), 
                actionsObject);
        javax.swing.Action delegate = actionMap.get("discard");
        if (delegate != null) {
            delegate.actionPerformed(null);
        }
        close(true);
    }


    
    // Building ***************************************************************

    /**
     * Builds the dialog's content pane, packs it, sets the resizable property,
     * and locates it on the screen. The dialog is then ready to be opened.<p>
     * 
     * PENDING: packs and locates. Accesses the application to get hold
     * of user preferences and prepare itself. But: inapproriate coupling into
     * <here>.appframework.swingx
     * 
     * Subclasses should rarely override this method.
     */
    private void build() {
        setContentPane(buildContentPane());
        inject();
        pack(); 
//        setResizable(false);
        TutorialUtils.locateOnOpticalScreenCenter(this);
        Application application = Application.getInstance();
        if (application instanceof SingleXFrameApplication) {
            ((SingleXFrameApplication) application).prepareDialog(this, false);
        } 
    }
    
    
    private JComponent buildContentPane() {
        FormLayout layout = new FormLayout(
                "fill:pref", 
                "fill:pref, 6dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.getPanel().setBorder(new EmptyBorder(18, 12, 12, 12));
        CellConstraints cc = new CellConstraints();
        builder.add(buildEditorPanel(), cc.xy(1, 1));
        builder.add(buildButtonBar(),   cc.xy(1, 3));
        return builder.getPanel();
    }
    
    private JComponent buildEditorPanel() {
        return form.getContent();
    }
    
    private JComponent buildButtonBar() {
        okayButton = new JButton();
        cancelButton = new JButton();
        JPanel bar = ButtonBarFactory.buildOKCancelBar(
                okayButton, 
                cancelButton);
        bar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        return bar;
    }

//-------------- exitListener bookkeeping, c&ped from Application    
    /**
     * Add an {@code ExitListener} to the list.
     * 
     * @param listener the {@code ExitListener}
     * @see #removeExitListener
     * @see #getExitListeners
     */
    public void addExitListener(ExitListener listener) {
        exitListeners.add(listener);
    }

    /**
     * Remove an {@code ExitListener} from the list.
     * 
     * @param listener the {@code ExitListener}
     * @see #addExitListener
     * @see #getExitListeners
     */
    public void removeExitListener(ExitListener listener) {
        exitListeners.remove(listener);
    }

    /**
     * All of the {@code ExitListeners} added so far.
     * 
     * @return all of the {@code ExitListeners} added so far.
     */
    public ExitListener[] getExitListeners() {
        int size = exitListeners.size();
        return exitListeners.toArray(new ExitListener[size]);
    }

    
}
