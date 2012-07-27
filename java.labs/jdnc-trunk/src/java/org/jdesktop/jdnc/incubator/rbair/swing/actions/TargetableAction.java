/*
 * $Id: TargetableAction.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/**
 * @(#)TargetableAction.java	1.1 03/04/30
 *
 * Copyright 2003 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials
 * provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 * 
 */
package org.jdesktop.jdnc.incubator.rbair.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.Action;
import javax.swing.Icon;

/**
 * A class that represents a dynamically targetable action. The invocation of this 
 * action will be dispatched to the <code>TargetManager</code>
 * <p>
 * You would create instances of this class to let the TargetManager handle the 
 * action invocations from the ui components constructed with this action. 
 * The TargetManager could be configured depending on application state to 
 * handle these actions. Generally, this class doesn't need to be subclassed. 
 *
 * @see TargetManager
 * @author Mark Davidson
 */
public class TargetableAction extends AbstractActionExt {

    public TargetableAction() {
	this("action");
    }

    public TargetableAction(String name) {
	super(name);
    }

    /**
     * @param name display name of the action
     * @param command the value of the action command key
     */
    public TargetableAction(String name, String command) {
	super(name, command);
    }

    /**
     * @param name display name of the action
     * @param command the value of the action command key
     * @param icon icon to display
     */
    public TargetableAction(String name, String command, Icon icon) {
	super(name, command, icon);
    }

    public TargetableAction(String name, Icon icon) {
	super(name, icon);
    }

    // Callbacks...

    /**
     * Callback for command actions. This event will be redispatched to 
     * the target manager along with the value of the Action.ACTION_COMMAND_KEY
     *
     * @param evt event which will be forwarded to the TargetManager
     * @see TargetManager
     */
    public void actionPerformed(ActionEvent evt) {
	if (!isStateAction()) {
	    // Do not process this event if it's a toggle action.
	    TargetManager manager = TargetManager.getInstance();
	    manager.doCommand(getActionCommand(), evt);
	}
    }

    /**
     * Callback for toggle actions. This event will be redispatched to 
     * the target manager along with value of the the Action.ACTION_COMMAND_KEY
     *
     * @param evt event which will be forwarded to the TargetManager
     * @see TargetManager
     */
    public void itemStateChanged(ItemEvent evt) {
        // Update all objects that share this item
	boolean newValue;
	boolean oldValue = isSelected();

        if (evt.getStateChange() == ItemEvent.SELECTED) {
	    newValue = true;
	} else {
	    newValue = false;
	}

	if (oldValue != newValue) {
	    setSelected(newValue);
	    
	    TargetManager manager = TargetManager.getInstance();
	    manager.doCommand(getActionCommand(), evt);
	}
    }

    public String toString() {
	return super.toString();
    }
}
