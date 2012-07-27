/*
 * Created on 02.03.2007
 *
 */
package org.jdesktop.appframework.swingx;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;

public class JAppFileChooser extends JFileChooser {

    @Override
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        if (context.getApplication() instanceof SingleXFrameApplication) {
            dialog.setName("filechooser");
            ((SingleXFrameApplication) context.getApplication()).prepareDialog(dialog, false);
        }
        return dialog;
    }
    

}
