/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//de.mx.nb.nodemngmt//ListView//EN",
autostore = false)
@TopComponent.Description(preferredID = "ListViewTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "de.mx.nb.nodemngmt.ListViewTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_NodeAction",
preferredID = "NodeTopComponent")
public final class ListViewTopComponent extends TopComponent implements ExplorerManager.Provider {
    
    public static ExplorerManager em = new ExplorerManager();
    
    public ListViewTopComponent() {
        
        
        initComponents();
        setName(NbBundle.getMessage(ListViewTopComponent.class, "CTL_NodeTopComponent"));
        setToolTipText(NbBundle.getMessage(ListViewTopComponent.class, "HINT_NodeTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        
        List<MyChildNode> list = new ArrayList<MyChildNode>();
        
        for (int i = 0; i < 20; i++) list.add(new MyChildNode("name " + i, "descr " + 1, new Date(), i));
        
        associateLookup (ExplorerUtils.createLookup(em, getActionMap()));
        em.setRootContext(new AbstractNode(Children.create(new MyChildFactory(list), true)));
        
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listTableView1 = new org.openide.explorer.view.ListTableView();

        setLayout(new java.awt.BorderLayout());
        add(listTableView1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.ListTableView listTableView1;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    public static ListViewTopComponent getInstance() {
        for (TopComponent tp : getRegistry().getOpened()) {
            if (tp instanceof ListViewTopComponent) return (ListViewTopComponent)tp;
        }
        return null;
    }
}
