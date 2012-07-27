package uk.co.osbald.sample;

import org.jdesktop.swingx.util.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Collection;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 11:26:41
 */

public class OpenModuleAction extends AbstractAction {

    public OpenModuleAction() {
        putValue(Action.NAME, "Open..");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
    }

    public void actionPerformed(ActionEvent event) {

        Collection<Module> modules = DomainLogic.getInstance().getAvailableModules();
        OpenDialogModel dialogModel = new OpenDialogModel(modules);

        class CommitAction extends AbstractAction {
            private OpenDialogModel dialogModel;

            public CommitAction(OpenDialogModel dialogModel) {
                super("Open");
                this.dialogModel = dialogModel;
            }

            public void actionPerformed(ActionEvent event) {
                Window source = WindowUtils.findWindow((Component) event.getSource());
                source.dispatchEvent(new WindowEvent(source, WindowEvent.WINDOW_CLOSING));
                System.out.println("Was Going to Open Module:" + dialogModel.getSelectedModule());
            }
        }

        //JFrame frame = WindowUtils.findJFrame((Component)event.getSource());    // won't work!
        JFrame frame = Application.getInstance().getFrame();
        JDialog dialog = OpenDialog.createDialog(frame, dialogModel, new CommitAction(dialogModel));
        dialog.setVisible(true);
    }
}
