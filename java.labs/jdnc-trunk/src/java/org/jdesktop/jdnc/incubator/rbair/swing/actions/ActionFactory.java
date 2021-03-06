/*
 * $Id: ActionFactory.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * A collection of static methods to make it easier to construct 
 * Actions. Not sure how usefull they are in reality but it saves a 
 * lot of typing.
 *
 * @author Mark Davidson
 */
public class ActionFactory {

    /**
     * Factory Methods for creating BoundActions
     */
    public static BoundAction createBoundAction(String id, String name, 
						String mnemonic) {
	return createBoundAction(id, name, mnemonic, false);
    }

    public static BoundAction createBoundAction(String id, String name, 
						String mnemonic, boolean toggle) {
	return createBoundAction(id, name, mnemonic, toggle, null);
    }


    public static BoundAction createBoundAction(String id, String name, 
						String mnemonic, boolean toggle,
						String group) {
	return (BoundAction)configureAction(new BoundAction(name, id), 
					    mnemonic, toggle, group);
    }

    /**
     * Factory Methods for creating <code>CompositeAction</code>
     * @see CompositeAction
     */
    public static CompositeAction createCompositeAction(String id, String name, 
						String mnemonic) {
	return createCompositeAction(id, name, mnemonic, false);
    }

    public static CompositeAction createCompositeAction(String id, String name, 
						String mnemonic, boolean toggle) {
	return createCompositeAction(id, name, mnemonic, toggle, null);
    }

    public static CompositeAction createCompositeAction(String id, String name, 
							String mnemonic, boolean toggle,
							String group) {
	return (CompositeAction)configureAction(new CompositeAction(name, id), 
						mnemonic, toggle, group);
    }


    public static ServerAction createServerAction(String id, String name,
						  String mnemonic) {
	ServerAction action = new ServerAction(name, id);
	if (mnemonic != null && !mnemonic.equals("")) {
	    action.putValue(Action.MNEMONIC_KEY, new Integer(mnemonic.charAt(0)));
	}
	return action;
    }
	

    /**
     * These methods are usefull for creating targetable actions
     */
    public static TargetableAction createTargetableAction(String id, String name) {
	return createTargetableAction(id, name, null);
    }

    public static TargetableAction createTargetableAction(String id, String name, 
					      String mnemonic) {
	return createTargetableAction(id, name, mnemonic, false);
    }

    public static TargetableAction createTargetableAction(String id, String name, 
							  String mnemonic, boolean toggle) {
	return createTargetableAction(id, name, mnemonic, toggle, null);
    }

    public static TargetableAction createTargetableAction(String id, String name, 
						  String mnemonic, boolean toggle, 
						  String group) {
	return (TargetableAction)configureAction(new TargetableAction(name, id), 
						 mnemonic, toggle, group);
    }
    
    private static Action configureAction(AbstractActionExt action, 
					  String mnemonic, boolean toggle, 
					  String group) {
	action.setMnemonic(mnemonic);
	String description = action.getName() + " action with comand " + action.getActionCommand();
	action.setShortDescription(description);
	action.setLongDescription(description);

	if (toggle) {
	    action.setStateAction();
	}
	if (group != null) {
	    action.setGroup(group);
	}
	return action;
    }

    /**
     * Add additional attributes to the action. If any of these attributes
     * are null then they will still be set on the action. Many of these 
     * attributes map to the set methods on <code>AbstractActionExt</code>
     *
     * @see AbstractActionExt
     * @param action the action which will all the attributes will be applied
     */
    public static void decorateAction(AbstractAction action, 
				      String shortDesc, String longDesc,
				      Icon smallIcon, Icon largeIcon, 
				      KeyStroke accel) {
	if (action instanceof AbstractActionExt) {
	    AbstractActionExt a = (AbstractActionExt)action;
	    a.setShortDescription(shortDesc);
	    a.setLongDescription(longDesc);
	    a.setSmallIcon(smallIcon);
	    a.setLargeIcon(smallIcon);
	    a.setAccelerator(accel);
	}
	else {
	    action.putValue(Action.SHORT_DESCRIPTION, shortDesc);
	    action.putValue(Action.LONG_DESCRIPTION, longDesc);
	    action.putValue(Action.SMALL_ICON, smallIcon);
	    action.putValue(AbstractActionExt.LARGE_ICON, largeIcon);
	    action.putValue(Action.ACCELERATOR_KEY, accel);
	}
    }
				      
    
}
