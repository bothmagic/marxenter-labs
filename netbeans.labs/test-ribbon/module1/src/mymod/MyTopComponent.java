/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mymod;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.shapesample.palette.PaletteSupport;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//mymod//My//EN",
autostore = false)
@TopComponent.Description(preferredID = "MyTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "mymod.MyTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MyAction",
preferredID = "MyTopComponent")
public final class MyTopComponent extends TopComponent {
    private JComponent myView;
    
    public MyTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(MyTopComponent.class, "CTL_MyTopComponent"));
        setToolTipText(NbBundle.getMessage(MyTopComponent.class, "HINT_MyTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        
        GraphSceneImpl scene = new GraphSceneImpl();
        myView = scene.createView();

        shapePane.setViewportView(myView);
        add(scene.createSatelliteView(), BorderLayout.WEST);
        
        associateLookup( Lookups.fixed( new Object[] { PaletteSupport.createPalette() } ) );
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        shapePane = new javax.swing.JScrollPane();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));
        add(shapePane);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane shapePane;
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
}
