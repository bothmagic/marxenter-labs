package org.jdesktop.appframework;

import java.awt.Frame;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;


/** 
 * A simple modal dialog with close/cancel actions implemented
 * via @Action. 
  */
public final class ExampleDialog extends JDialog {

    private JButton okayButton;

    private JButton cancelButton;

    public ExampleDialog() {
        this(null);
    }
    public ExampleDialog(Frame parent) {
        super(parent, "Example", true);
    }
    
    public void prepareOpen() {
        setContentPane(buildContentPane());
        inject();
//        pack();
    }
    /**
     * register components and actions.
     */
    private void inject() {
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
//        context.getResourceMap().injectComponents(this);
        ActionMap actionMap = context.getActionMap(getClass(), this);
        okayButton.setAction(actionMap.get("ok"));
        cancelButton.setAction(actionMap.get("cancel"));
    }

    @Action
    public void ok() {
        close();
    }

    @Action
    public void cancel() {
        close();
    }
    
    public void close() {
        release();
        dispose();
    }

    private void release() {
        /* ?? anything to do here */
    }
    

    private JComponent buildContentPane() {
        JPanel panel = new JPanel();
        okayButton = new JButton();
        cancelButton = new JButton();
        panel.add(okayButton);
        panel.add(cancelButton);
        return panel;
    }
    
}
