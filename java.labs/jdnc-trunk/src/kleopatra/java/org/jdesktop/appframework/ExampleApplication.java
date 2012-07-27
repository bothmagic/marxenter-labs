
package org.jdesktop.appframework;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Creates and shows a modeal dialog. The close/cancel of the dialog are implemented
 * with the @Action annotation. <p>
 * 
 * Exersize the show(Dialog) - not working, state not saved? 
 *  
 */
public final class ExampleApplication extends SingleFrameApplication {

    @Override
    public void startup() {
      show(buildPanel());
    }
    
    @Action
    public void openDialog() {
        ExampleDialog dialog = new ExampleDialog(getMainFrame());
        dialog.setName("exampleDialog");
        dialog.prepareOpen();
        show(dialog);
    }
    
    private JComponent buildPanel() {
        JButton button = new JButton("openDialog");
        ApplicationContext context = getContext();
        ActionMap actionMap = context.getActionMap();
        button.setAction(actionMap.get("openDialog"));
        return button;
    }


    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        launch(ExampleApplication.class, args);
    }
        
}
