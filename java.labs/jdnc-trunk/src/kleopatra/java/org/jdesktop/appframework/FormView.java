/*
 * Created on 05.02.2007
 *
 */
package org.jdesktop.appframework;

import javax.swing.JComponent;

/**
 * Quick convenience interface: provides sources (component and
 * object which implements model-related the apply/discard actions)
 * for FormDialog to auto-configure itself from. 
 */
public interface FormView {

    /**
     * Returns the component to display in the dialog's content 
     * area.
     * 
     * @return
     */
    public JComponent getContent();
    
    /**
     * Returns the object which implements the apply/discard operations.
     * 
     * @return
     */
    public Object getActionsObject();
    
    /**
     * Returns the name of the content. This is for the sake of 
     * storing/restoring user/application preferences.
     * 
     * @return
     */
    public String getName();
}
