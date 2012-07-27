package de.mt.jackrabbit.browser.ui;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * 
 * @author markus
 */
public class ConnectWizardPanel1 implements WizardDescriptor.Panel {
  
  public static final String PROPERTY_REPODIR = "repo_dir";
  public static final String PROPERTY_CONFIGFILE = "config_file";  
  public static final String PROPERTY_USERNAME = "username";  
  public static final String PROPERTY_PASSSWORD = "password";  
    
  /**
   * The visual component that displays this panel. If you need to access the
   * component from this class, just use getComponent().
   */
  private Component component;

  // Get the visual component for the panel. In this template, the component
  // is kept separate. This can be more efficient: if the wizard is created
  // but never displayed, or not all panels are displayed, it is better to
  // create only those which really need to be visible.
  public Component getComponent() {
    if (component == null) {
      component = new ConnectVisualPanel1();
    }
    return component;
  }
  
  public String getName() {
    return NbBundle.getMessage(ConnectWizardPanel1.class, "Wizard-ChooseLocalRepo");
  }

  public HelpCtx getHelp() {
    
    return HelpCtx.DEFAULT_HELP;
    
  }

  public boolean isValid() {
    
    return true;
        
  }

  public final void addChangeListener(ChangeListener l) {
  }

  public final void removeChangeListener(ChangeListener l) {
  }

  
  public void readSettings(Object settings) {
  }

  public void storeSettings(Object settings) {
    WizardDescriptor wizardDescriptor = (WizardDescriptor)settings;
    ConnectVisualPanel1 connVisualPanel1 = (ConnectVisualPanel1)component;
    
    wizardDescriptor.putProperty(PROPERTY_CONFIGFILE, connVisualPanel1.getConfigFile());
    wizardDescriptor.putProperty(PROPERTY_REPODIR, connVisualPanel1.getRepoDir());
    wizardDescriptor.putProperty(PROPERTY_USERNAME, connVisualPanel1.getUsername());
    wizardDescriptor.putProperty(PROPERTY_PASSSWORD, connVisualPanel1.getPassword());
  }
}