/*
 * Created on 22.06.2006
 *
 */
package org.jdesktop.swingx.table;

import java.awt.Frame;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXDialog;

/**
 * Extended to store the current location on close.
 * NOTE: this is necessary because the default close
 * action is a BoundAction registered to a callback method 
 * on the handler (the JXDialog). Handlers must be top-level
 * public classes.
 * 
 */
public class JXLocDialog extends JXDialog {

    public JXLocDialog(Frame frame, JComponent content) {
        super(frame, content);
    }
    
    @Override
    public void doClose() {
        content.putClientProperty("oldLocation", getLocation());
        content.putClientProperty("oldSize", getSize());
        super.doClose();
    }

}
