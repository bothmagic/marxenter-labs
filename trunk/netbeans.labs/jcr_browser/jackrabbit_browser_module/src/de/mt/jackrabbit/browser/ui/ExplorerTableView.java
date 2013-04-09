/*
 * ExplorerTableView.java
 *
 * Created on 12. Oktober 2007, 09:51
 */

package de.mt.jackrabbit.browser.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeSelectionModel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.ListTableView;
import org.openide.explorer.view.TreeTableView;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author  Markus
 */
public class ExplorerTableView extends javax.swing.JPanel  implements PropertyChangeListener, Lookup.Provider {
  
  private  ExplorerManager explorerManager;
  private Lookup lookup;
  
  private ExplorerManager parentExplorerManager;
  /** Creates new form ExplorerTableView */
  public ExplorerTableView() {
    initComponents();
    //explorerManager = aExplorerManager;
    //((TreeTableView)jScrollPane1).setRootVisible(false);
    //((TreeTableView)jScrollPane1).setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    
    //explorerManager.addPropertyChangeListener(this);
  }

  public void propertyChange(PropertyChangeEvent evt) {
    
    
    
    
  }
  
  
  
  @Override
  public void addNotify() {
    
    explorerManager = ExplorerManager.find(SwingUtilities.getAncestorOfClass(ExplorerTopComponent.class, this));
    explorerManager.addPropertyChangeListener(this);
    //explorerManager.setRootContext(parentExplorerManager.getRootContext());
    
    super.addNotify();
  }
  
  

  public Lookup getLookup() {
    return lookup;
  }
  
  public ExplorerManager getExplorerManager() {
    return explorerManager;
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new ListTableView();

    setLayout(new java.awt.BorderLayout());
    add(jScrollPane1, java.awt.BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents
  
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables
  
}