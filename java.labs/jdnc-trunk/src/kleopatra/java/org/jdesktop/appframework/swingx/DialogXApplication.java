
package org.jdesktop.appframework.swingx;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.jdesktop.appframework.ExampleDialog;
import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;

/**
 * Creates and shows a modal dialog. The close/cancel of the dialog are implemented
 * with the @Action annotation. <p>
 * 
 */
public final class DialogXApplication extends SingleXFrameApplication {
    private static final Logger logger = Logger
            .getLogger(DialogXApplication.class.getName());
    @Override
    public void startup() {
        deleteSessionState();
//        super.startup();
        WindowStateListener l = new WindowStateListener() {

            public void windowStateChanged(final WindowEvent e) {
                logger.info("state changed");
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if ((e.getOldState() & WindowEvent.WINDOW_ICONIFIED) != 0)
                        getMainFrame().setFocusableWindowState(true);
                    }
                });
            }
            
        };
        getMainFrame().addWindowStateListener(l);
        getMainFrame().setFocusableWindowState(false);
        getMainFrame().addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
                logger.info("activated");
                
            }

            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub
                logger.info("deactivated");

            }

            public void windowDeiconified(WindowEvent e) {
                logger.info("deiconified");
                 
            }

            public void windowIconified(WindowEvent e) {
                logger.info("iconified");
                getMainFrame().setFocusableWindowState(false);
                
            }

            public void windowOpened(WindowEvent e) {
                logger.info("opened");
                getMainFrame().setFocusableWindowState(true);
            }});
        show(buildPanel());
    }
    
    @Action
    public void openNewDialog() {
        ExampleDialog dialog = new ExampleDialog(getMainFrame());
        dialog.setName("exampleDialog");
        dialog.prepareOpen();
        prepareDialog(dialog, true);
    }
    
    @Action
    public void openFileDialog() {
        JFileChooser chooser = new JAppFileChooser();
        chooser.showDialog(getMainFrame(), null);
    }
    
    @Action
    public void openConfirmDialog() {
        KeyboardFocusManager k = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof JDialog) {
                    ((Component) evt.getNewValue()).setName("confirmDialog");
                    prepareDialog((JDialog) evt.getNewValue(), false);
                }
                
            }
            
        };
        k.addPropertyChangeListener("activeWindow", l);
        JOptionPane.showConfirmDialog(getMainFrame(), "123455789 - asdf - jkla");
        k.removePropertyChangeListener("activeWindow", l);
        
    }
    
    private JComponent buildPanel() {
        JComponent box = Box.createVerticalBox();
        ApplicationContext context = getContext();
        ActionMap actionMap = context.getActionMap();
        Object[] keys = actionMap.keys();
        for (int i = 0; i < keys.length; i++) {
            box.add(new JButton(actionMap.get(keys[i])));
        }
        return box;
    }


    public static void main(String[] args) {
        launch(DialogXApplication.class, args);
    }
        
}
