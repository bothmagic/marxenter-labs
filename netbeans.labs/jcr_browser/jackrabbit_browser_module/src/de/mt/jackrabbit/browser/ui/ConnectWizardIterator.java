package de.mt.jackrabbit.browser.ui;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.WizardDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.xml.sax.InputSource;

public final class ConnectWizardIterator implements WizardDescriptor.ProgressInstantiatingIterator {
  
 private WizardDescriptor wizardDescriptor;
  
  // To invoke this wizard, copy-paste and run the following code, e.g. from
  // SomeAction.performAction():
  /*
  WizardDescriptor.Iterator iterator = new ConnectWizardIterator();
  WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
  // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
  // {1} will be replaced by WizardDescriptor.Iterator.name()
  wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
  wizardDescriptor.setTitle("Your wizard dialog title here");
  Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
  dialog.setVisible(true);
  dialog.toFront();
  boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
  if (!cancelled) {
  // do something
  }
   */
  private int index;
  private WizardDescriptor.Panel[] panels;

  /**
   * Initialize panels representing individual wizard's steps and sets
   * various properties for them influencing wizard appearance.
   */
  private WizardDescriptor.Panel[] getPanels() {
    if (panels == null) {
      panels = new WizardDescriptor.Panel[]{new ConnectWizardPanel1()};
      String[] steps = new String[panels.length];
      for (int i = 0; i < panels.length; i++) {
        Component c = panels[i].getComponent();
        // Default step name to component name of panel.
        steps[i] = c.getName();
        if (c instanceof JComponent) {
          // assume Swing components
          JComponent jc = (JComponent) c;
          // Sets step number of a component
          jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
          // Sets steps names for a panel
          jc.putClientProperty("WizardPanel_contentData", steps);
          // Turn on subtitle creation on each step
          jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
          // Show steps on the left side with the image on the background
          jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
          // Turn on numbering of all steps
          jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
        }
      }
    }
    return panels;
  }

  public WizardDescriptor.Panel current() {
    return getPanels()[index];
  }

  public String name() {
    return index + 1 + ". from " + getPanels().length;
  }

  public boolean hasNext() {
    return index < getPanels().length - 1;
  }

  public boolean hasPrevious() {
    return index > 0;
  }

  public void nextPanel() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    index++;
  }

  public void previousPanel() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    index--;
  }

  // If nothing unusual changes in the middle of the wizard, simply:
  public void addChangeListener(ChangeListener l) {
  }

  public void removeChangeListener(ChangeListener l) {
  }

  public Set instantiate(ProgressHandle progressHandle) throws IOException {
    String repoDir = null;
    String configFile = null;
    String username = null;
    char[] password = null;
    Repository repository = null;
    Session session = null;
    
    current().getComponent().setEnabled(false);
    
    
    
    progressHandle.start();
    
    try {
      
        configFile = (String) wizardDescriptor.getProperty(ConnectWizardPanel1.PROPERTY_CONFIGFILE);
        repoDir = (String) wizardDescriptor.getProperty(ConnectWizardPanel1.PROPERTY_REPODIR);
        username = (String) wizardDescriptor.getProperty(ConnectWizardPanel1.PROPERTY_USERNAME);
        password = (char[]) wizardDescriptor.getProperty(ConnectWizardPanel1.PROPERTY_PASSSWORD);
        repository = new TransientRepository(RepositoryConfig.create(new InputSource(new FileReader(configFile)), repoDir));
        
        session = repository.login(new SimpleCredentials(username, password));
      } catch (FileNotFoundException ex) {
        ex.printStackTrace();
      } catch (RepositoryException ex) {
        ex.printStackTrace();
      }
    
    progressHandle.finish();
    
    HashSet<Object> set = new HashSet<Object>();
    set.add(repository);
    set.add(session);
    return set;
  
  }

  public Set instantiate() throws IOException {
    return Collections.EMPTY_SET;
  }

  public void initialize(WizardDescriptor arg0) {
    this.wizardDescriptor = arg0;
  }

  public void uninitialize(WizardDescriptor wizardDescriptor) {
    
    //throw new UnsupportedOperationException("Not supported yet.");
  }
  // If something changes dynamically (besides moving between panels), e.g.
  // the number of panels changes in response to user input, then uncomment
  // the following and call when needed: fireChangeEvent();
  /*
  private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
  public final void addChangeListener(ChangeListener l) {
  synchronized (listeners) {
  listeners.add(l);
  }
  }
  public final void removeChangeListener(ChangeListener l) {
  synchronized (listeners) {
  listeners.remove(l);
  }
  }
  protected final void fireChangeEvent() {
  Iterator<ChangeListener> it;
  synchronized (listeners) {
  it = new HashSet<ChangeListener>(listeners).iterator();
  }
  ChangeEvent ev = new ChangeEvent(this);
  while (it.hasNext()) {
  it.next().stateChanged(ev);
  }
  }
   */
}