/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Edit",
id = "de.mx.nb.nodemngmt.OpenChildNode")
@ActionRegistration(displayName = "#CTL_OpenChildNode")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1300)
})
@Messages("CTL_OpenChildNode=OpenChildNode")
public final class OpenChildNode extends AbstractAction {

    private final AbstractNode context;
    
    public OpenChildNode(AbstractNode context) {
        this.context = context;
    }

    public void actionPerformed(ActionEvent ev) {
        JOptionPane.showConfirmDialog(null, "hello from " + context.getDisplayName());
    }
}
