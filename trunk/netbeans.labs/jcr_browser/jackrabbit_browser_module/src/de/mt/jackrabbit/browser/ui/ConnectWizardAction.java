package de.mt.jackrabbit.browser.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

// An example action demonstrating how the wizard could be called from within
import org.openide.windows.TopComponent;
public final class ConnectWizardAction extends CallableSystemAction {
  
  /**
   * Open connection wizard dialog and initialize Explorer Window with new connection.
   */
  public void performAction() {
    Repository repository = null;
    Session session = null;
    Object object1 = null;
    Object object2 = null;
    Iterator configSetIterator = null;
    WizardDescriptor wizardDescriptor = new WizardDescriptor(new ConnectWizardIterator());
    // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
    wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
    wizardDescriptor.setTitle("Your wizard dialog title here");
    
    Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
    dialog.setVisible(true);
    dialog.toFront();
    boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;  
    
    if (!cancelled) {
    
      configSetIterator = wizardDescriptor.getInstantiatedObjects().iterator();
      object1 = configSetIterator.next();
      object2 = configSetIterator.next();
      
      session = (object1 instanceof Session)? (Session)object1: (Session)object2;
      repository = (object1 instanceof Repository)? (Repository)object1:(Repository)object2;
      
      ExplorerTopComponent win = new ExplorerTopComponent(repository, session);
      win.open();
      win.requestActive();
      
    }
  }

  public String getName() {
    return NbBundle.getMessage(ConnectWizardAction.class, "Action-Connect");
  }

  @Override
  public String iconResource() {
    return null;
  }

  public HelpCtx getHelpCtx() {
    return HelpCtx.DEFAULT_HELP;
  }

  @Override
  protected boolean asynchronous() {
    return false;
  }
}