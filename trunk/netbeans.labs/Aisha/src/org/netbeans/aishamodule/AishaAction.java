/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.aishamodule;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action which shows Aisha component.
 */
public class AishaAction extends AbstractAction {

    public AishaAction() {
        super(NbBundle.getMessage(AishaAction.class, "CTL_AishaAction"));
//        putValue(SMALL_ICON, new ImageIcon(Utilities.loadImage(AishaTopComponent.ICON_PATH, true)));
    }

    public void actionPerformed(ActionEvent evt) {
        TopComponent win = AishaTopComponent.findInstance();
        win.open();
        win.requestActive();
    }
}
